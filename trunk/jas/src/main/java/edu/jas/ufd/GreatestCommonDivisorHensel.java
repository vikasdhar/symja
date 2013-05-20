/*
 * $Id: GreatestCommonDivisorHensel.java 4356 2013-01-05 19:33:56Z kredel $
 */

package edu.jas.ufd;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import edu.jas.arith.BigInteger;
import edu.jas.arith.ModIntegerRing;
import edu.jas.arith.ModLongRing;
import edu.jas.arith.Modular;
import edu.jas.arith.ModularRingFactory;
import edu.jas.arith.PrimeList;
import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.PolyUtil;
import edu.jas.structure.GcdRingElem;
import edu.jas.structure.Power;
import edu.jas.structure.RingFactory;


/**
 * Greatest common divisor algorithms with subresultant polynomial remainder
 * sequence and univariate Hensel lifting.
 * @author Heinz Kredel
 */

public class GreatestCommonDivisorHensel<MOD extends GcdRingElem<MOD> & Modular> extends
                GreatestCommonDivisorAbstract<BigInteger> {


    private static final Logger logger = Logger.getLogger(GreatestCommonDivisorHensel.class);


    private final boolean debug = logger.isDebugEnabled();


    /**
     * Flag for linear or quadratic Hensel lift.
     */
    public final boolean quadratic;


    /**
     * Fall back gcd algorithm.
     */
    public final GreatestCommonDivisorAbstract<BigInteger> iufd;


    /**
     * Constructor.
     */
    public GreatestCommonDivisorHensel() {
        this(true);
    }


    /**
     * Constructor.
     * @param quadratic use quadratic Hensel lift.
     */
    public GreatestCommonDivisorHensel(boolean quadratic) {
        this.quadratic = quadratic;
        iufd = new GreatestCommonDivisorSubres<BigInteger>();
    }


    /**
     * Univariate GenPolynomial greatest comon divisor. Uses univariate Hensel
     * lifting.
     * @param P univariate GenPolynomial.
     * @param S univariate GenPolynomial.
     * @return gcd(P,S).
     */
    @Override
    public GenPolynomial<BigInteger> baseGcd(GenPolynomial<BigInteger> P, GenPolynomial<BigInteger> S) {
        //if (true) {
        //    return iufd.baseGcd(P,S);
        //}
        if (S == null || S.isZERO()) {
            return P;
        }
        if (P == null || P.isZERO()) {
            return S;
        }
        if (P.ring.nvar > 1) {
            throw new IllegalArgumentException(this.getClass().getName() + " no univariate polynomial");
        }
        GenPolynomialRing<BigInteger> fac = P.ring;
        long e = P.degree(0);
        long f = S.degree(0);
        GenPolynomial<BigInteger> q;
        GenPolynomial<BigInteger> r;
        if (f > e) {
            r = P;
            q = S;
            long g = f;
            f = e;
            e = g;
        } else {
            q = P;
            r = S;
        }
        if (debug) {
            logger.debug("degrees: e = " + e + ", f = " + f);
        }
        r = r.abs();
        q = q.abs();
        // compute contents and primitive parts
        BigInteger a = baseContent(r);
        BigInteger b = baseContent(q);
        // gcd of coefficient contents
        BigInteger c = gcd(a, b); // indirection
        r = divide(r, a); // indirection
        q = divide(q, b); // indirection
        if (r.isONE()) {
            return r.multiply(c);
        }
        if (q.isONE()) {
            return q.multiply(c);
        }
        // compute normalization factor
        BigInteger ac = r.leadingBaseCoefficient();
        BigInteger bc = q.leadingBaseCoefficient();
        BigInteger cc = gcd(ac, bc); // indirection
        // compute degree vectors, only univeriate
        ExpVector rdegv = r.degreeVector();
        ExpVector qdegv = q.degreeVector();
        //initialize prime list and degree vector
        PrimeList primes = new PrimeList(PrimeList.Range.medium);
        int pn = 50; //primes.size();

        ModularRingFactory<MOD> cofac;
        GenPolynomial<MOD> qm;
        GenPolynomial<MOD> qmf;
        GenPolynomial<MOD> rm;
        GenPolynomial<MOD> rmf;
        GenPolynomial<MOD> cmf;
        GenPolynomialRing<MOD> mfac;
        GenPolynomial<MOD> cm = null;
        GenPolynomial<MOD>[] ecm = null;
        GenPolynomial<MOD> sm = null;
        GenPolynomial<MOD> tm = null;
        HenselApprox<MOD> lift = null;
        if (debug) {
            logger.debug("c = " + c);
            logger.debug("cc = " + cc);
            logger.debug("primes = " + primes);
        }

        int i = 0;
        for (java.math.BigInteger p : primes) {
            //System.out.println("next run ++++++++++++++++++++++++++++++++++");
            if (++i >= pn) {
                logger.error("prime list exhausted, pn = " + pn);
                //logger.info("primes = " + primes);
                return iufd.baseGcd(P, S);
                //throw new ArithmeticException("prime list exhausted");
            }
            // initialize coefficient factory and map normalization factor
            //cofac = new ModIntegerRing(p, true);
            if (ModLongRing.MAX_LONG.compareTo(p) > 0) {
                cofac = (ModularRingFactory) new ModLongRing(p, true);
            } else {
                cofac = (ModularRingFactory) new ModIntegerRing(p, true);
            }
            MOD nf = cofac.fromInteger(cc.getVal());
            if (nf.isZERO()) {
                continue;
            }
            nf = cofac.fromInteger(q.leadingBaseCoefficient().getVal());
            if (nf.isZERO()) {
                continue;
            }
            nf = cofac.fromInteger(r.leadingBaseCoefficient().getVal());
            if (nf.isZERO()) {
                continue;
            }
            // initialize polynomial factory and map polynomials
            mfac = new GenPolynomialRing<MOD>(cofac, fac.nvar, fac.tord, fac.getVars());
            qm = PolyUtil.<MOD> fromIntegerCoefficients(mfac, q);
            if (!qm.degreeVector().equals(qdegv)) {
                continue;
            }
            rm = PolyUtil.<MOD> fromIntegerCoefficients(mfac, r);
            if (!rm.degreeVector().equals(rdegv)) {
                continue;
            }
            if (debug) {
                logger.info("cofac = " + cofac.getIntegerModul());
            }

            // compute univariate modular gcd
            cm = qm.gcd(rm);

            // test for constant g.c.d
            if (cm.isConstant()) {
                logger.debug("cm, constant = " + cm);
                return fac.getONE().multiply(c);
            }

            // compute factors and gcd with factor
            GenPolynomial<BigInteger> crq;
            rmf = rm.divide(cm); // rm = cm * rmf
            ecm = cm.egcd(rmf);
            if (ecm[0].isONE()) {
                //logger.debug("gcd() first factor " + rmf);
                crq = r;
                cmf = rmf;
                sm = ecm[1];
                tm = ecm[2];
            } else {
                qmf = qm.divide(cm); // qm = cm * qmf
                ecm = cm.egcd(qmf);
                if (ecm[0].isONE()) {
                    //logger.debug("gcd() second factor " + qmf);
                    crq = q;
                    cmf = qmf;
                    sm = ecm[1];
                    tm = ecm[2];
                } else {
                    logger.info("both gcd != 1: Hensel not applicable");
                    return iufd.baseGcd(P, S);
                }
            }
            BigInteger cn = crq.maxNorm();
            cn = cn.multiply(crq.leadingBaseCoefficient().abs());
            cn = cn.multiply(cn.fromInteger(2));
            if (debug) {
                System.out.println("crq = " + crq);
                System.out.println("cm  = " + cm);
                System.out.println("cmf = " + cmf);
                System.out.println("sm  = " + sm);
                System.out.println("tm  = " + tm);
                System.out.println("cn  = " + cn);
            }
            try {
                if (quadratic) {
                    lift = HenselUtil.liftHenselQuadratic(crq, cn, cm, cmf, sm, tm);
                } else {
                    lift = HenselUtil.liftHensel(crq, cn, cm, cmf, sm, tm);
                }
            } catch (NoLiftingException nle) {
                logger.info("giving up on Hensel gcd reverting to Subres gcd " + nle);
                return iufd.baseGcd(P, S);
            }
            q = lift.A;
            if (debug) {
                System.out.println("q   = " + q);
                System.out.println("qf  = " + lift.B);
            }
            q = basePrimitivePart(q);
            q = q.multiply(c).abs();
            if (PolyUtil.<BigInteger> baseSparsePseudoRemainder(P, q).isZERO()
                            && PolyUtil.<BigInteger> baseSparsePseudoRemainder(S, q).isZERO()) {
                break;
            }
            logger.info("final devision not successfull");
            //System.out.println("P rem q = " + PolyUtil.<BigInteger>basePseudoRemainder(P,q));
            //System.out.println("S rem q = " + PolyUtil.<BigInteger>basePseudoRemainder(S,q));
            //break;
        }
        return q;
    }


    /**
     * Univariate GenPolynomial recursive greatest comon divisor. Uses
     * multivariate Hensel list.
     * @param P univariate recursive GenPolynomial.
     * @param S univariate recursive GenPolynomial.
     * @return gcd(P,S).
     */
    @Override
    public GenPolynomial<GenPolynomial<BigInteger>> recursiveUnivariateGcd(
                    GenPolynomial<GenPolynomial<BigInteger>> P, GenPolynomial<GenPolynomial<BigInteger>> S) {
        if (S == null || S.isZERO()) {
            return P;
        }
        if (P == null || P.isZERO()) {
            return S;
        }
        if (P.ring.nvar > 1) {
            throw new IllegalArgumentException(this.getClass().getName() + " no univariate polynomial");
        }
        long e = P.degree(0);
        long f = S.degree(0);
        GenPolynomial<GenPolynomial<BigInteger>> q, r, s;
        if (f > e) {
            r = P;
            q = S;
            long g = f;
            f = e;
            e = g;
        } else {
            q = P;
            r = S;
        }
        if (debug) {
            logger.debug("degrees: e = " + e + ", f = " + f);
        }
        r = r.abs();
        q = q.abs();
        GenPolynomial<BigInteger> a = iufd.recursiveContent(r); // TODO remove "iufd."
        GenPolynomial<BigInteger> b = iufd.recursiveContent(q); // TODO remove "iufd."

        GenPolynomial<BigInteger> c = gcd(a, b); // go to recursion
        //System.out.println("rgcd c = " + c);
        r = PolyUtil.<BigInteger> recursiveDivide(r, a);
        q = PolyUtil.<BigInteger> recursiveDivide(q, b);
        a = PolyUtil.<BigInteger> basePseudoDivide(a, c);
        b = PolyUtil.<BigInteger> basePseudoDivide(b, c);
        if (r.isONE()) {
            return r.multiply(c);
        }
        if (q.isONE()) {
            return q.multiply(c);
        }
        // convert from Z[y1,...,yr][x] to Z[x][y1,...,yr] to Z[x,y1,...,yr]
        GenPolynomial<GenPolynomial<BigInteger>> qs = PolyUtil.<BigInteger> switchVariables(q);
        GenPolynomial<GenPolynomial<BigInteger>> rs = PolyUtil.<BigInteger> switchVariables(r);

        GenPolynomialRing<GenPolynomial<BigInteger>> rfac = qs.ring;
        RingFactory<GenPolynomial<BigInteger>> rrfac = rfac.coFac;
        GenPolynomialRing<BigInteger> cfac = (GenPolynomialRing<BigInteger>) rrfac;
        GenPolynomialRing<BigInteger> dfac = cfac.extend(rfac.getVars());
        //System.out.println("pfac = " + P.ring.toScript());
        //System.out.println("rfac = " + rfac.toScript());
        //System.out.println("dfac = " + dfac.toScript());

        GenPolynomial<BigInteger> qd = PolyUtil.<BigInteger> distribute(dfac, qs);
        GenPolynomial<BigInteger> rd = PolyUtil.<BigInteger> distribute(dfac, rs);
        //System.out.println("qd = " + qd);
        //System.out.println("rd = " + rd);
        //System.out.println("a  = " + a);
        //System.out.println("b  = " + b);
        //System.out.println("c  = " + c);

        // compute normalization factor
        BigInteger ac = rd.leadingBaseCoefficient();
        BigInteger bc = qd.leadingBaseCoefficient();
        BigInteger cc = gcd(ac, bc); // indirection

        //initialize prime list
        PrimeList primes = new PrimeList(PrimeList.Range.medium); 
        Iterator<java.math.BigInteger> primeIter = primes.iterator();
        int pn = 50; //primes.size();

        // double check variables
        // need qe,re,qd,rd,a,b
        GenPolynomial<BigInteger> qe0;
        GenPolynomial<BigInteger> re0;
        GenPolynomial<BigInteger> ce0 = null;

        for (int i = 0; i < 11; i++) { // meta loop
            //System.out.println("======== run " + dfac.nvar + ", " + i);
            java.math.BigInteger p = null; //new java.math.BigInteger("19"); //primes.next();
            // 5 small, 4 medium and 2 large size primes
            if (i == 0) { // medium size
                primes = new PrimeList(PrimeList.Range.medium);
                primeIter = primes.iterator();
            }
            if (i == 4) { // small size
                primes = new PrimeList(PrimeList.Range.small);
                primeIter = primes.iterator();
                p = primeIter.next(); // 2
                p = primeIter.next(); // 3
                p = primeIter.next(); // 5
                p = primeIter.next(); // 7
            }
            if (i == 9) { // large size
                primes = new PrimeList(PrimeList.Range.large);
                primeIter = primes.iterator();
            }
            ModularRingFactory<MOD> cofac = null;
            int pi = 0;
            while (pi < pn && primeIter.hasNext()) {
                p = primeIter.next();
                //p = new java.math.BigInteger("19");
                logger.info("prime = " + p);
                // initialize coefficient factory and map normalization factor and polynomials
                ModularRingFactory<MOD> cf = null;
                if (ModLongRing.MAX_LONG.compareTo(p) > 0) {
                    cf = (ModularRingFactory) new ModLongRing(p, true);
                } else {
                    cf = (ModularRingFactory) new ModIntegerRing(p, true);
                }
                MOD nf = cf.fromInteger(cc.getVal());
                if (nf.isZERO()) {
                    continue;
                }
                nf = cf.fromInteger(q.leadingBaseCoefficient().leadingBaseCoefficient().getVal());
                if (nf.isZERO()) {
                    continue;
                }
                nf = cf.fromInteger(r.leadingBaseCoefficient().leadingBaseCoefficient().getVal());
                if (nf.isZERO()) {
                    continue;
                }
                cofac = cf;
                break;
            }
            if (cofac == null) { // no lucky prime found
                logger.info("no lucky prime: giving up on Hensel gcd reverting to Subres gcd");
                GenPolynomial<GenPolynomial<BigInteger>> T = iufd.recursiveUnivariateGcd(P, S);
                return T.abs().multiply(c); //.abs();
            }
            //System.out.println("cofac = " + cofac);

            // search evaluation points and evaluate
            List<BigInteger> V = new ArrayList<BigInteger>(P.ring.nvar);
            GenPolynomialRing<BigInteger> ckfac = dfac;
            GenPolynomial<BigInteger> qe = qd;
            GenPolynomial<BigInteger> re = rd;
            GenPolynomial<BigInteger> qei;
            GenPolynomial<BigInteger> rei;
            for (int j = dfac.nvar; j > 1; j--) {
                // evaluation to univariate case
                long degq = qe.degree(ckfac.nvar - 2);
                long degr = re.degree(ckfac.nvar - 2);
                ckfac = ckfac.contract(1);
                long vi = 1L; //(long)(dfac.nvar-j); // 1L; 0 not so good for small p
                if (p.longValue() > 1000L) {
                    //vi = (long)j+1L;
                    vi = 0L;
                }
                // search small evaluation point
                while (true) {
                    MOD vp = cofac.fromInteger(vi++);
                    //System.out.println("vp = " + vp);
                    if (vp.isZERO() && vi != 1L) { // all elements of Z_p exhausted
                        qe = null;
                        re = null;
                        break;
                    }
                    BigInteger vii = new BigInteger(vi-1);
                    qei = PolyUtil.<BigInteger> evaluateMain(ckfac, qe, vii);
                    rei = PolyUtil.<BigInteger> evaluateMain(ckfac, re, vii);
                    //System.out.println("qei = " + qei);
                    //System.out.println("rei = " + rei);

                    // check lucky evaluation point 
                    if (degq != qei.degree(ckfac.nvar - 1)) {
                        //System.out.println("degv(qe) = " + qe.degreeVector());
                        //System.out.println("deg(qe) = " + degq + ", deg(qe) = " + qei.degree(ckfac.nvar-1));
                        continue;
                    }
                    if (degr != rei.degree(ckfac.nvar - 1)) {
                        //System.out.println("degv(re) = " + re.degreeVector());
                        //System.out.println("deg(re) = " + degr + ", deg(re) = " + rei.degree(ckfac.nvar-1));
                        continue;
                    }
                    V.add(vii);
                    qe = qei;
                    re = rei;
                    break;
                }
                if (qe == null && re == null) {
                    break;
                }
            }
            if (qe == null && re == null) {
                continue;
            }
            logger.info("evaluation points  = " + V);

            // recursion base:
            GenPolynomial<BigInteger> ce = baseGcd(qe, re);
            if (ce.isConstant()) {
                return P.ring.getONE().multiply(c);
            }
            logger.info("base gcd = " + ce);

            // double check 
            // need qe,re,qd,rd,a,b
            if (i == 0) {
                qe0 = qe;
                re0 = re;
                ce0 = ce;
                continue;
            }
            long d0 = ce0.degree(0);
            long d1 = ce.degree(0);
            //System.out.println("d0, d1 = " + d0 + ", " + d1);
            if (d1 < d0) {
                qe0 = qe;
                re0 = re;
                ce0 = ce;
                continue;
            } else if (d1 > d0) {
                continue;
            }
            // d0 == d1 is ok
            long dx = r.degree(0);
            //System.out.println("d0, dx = " + d0 + ", " + dx);
            if (d0 == dx) { // gcd == r ??
                if (PolyUtil.<BigInteger> recursivePseudoRemainder(q, r).isZERO()) {
                    r = r.abs().multiply(c); //.abs();
                    logger.info("exit with r | q : " + r);
                    return r;
                }
                continue;
            }

            // norm
            BigInteger mn = null;
            //mn = mn.multiply(cc).multiply(mn.fromInteger(2));

            // prepare lifting, chose factor polynomials
            GenPolynomial<BigInteger> re1 = PolyUtil.<BigInteger> basePseudoDivide(re, ce);
            GenPolynomial<BigInteger> qe1 = PolyUtil.<BigInteger> basePseudoDivide(qe, ce);
            GenPolynomial<BigInteger> ui, he, pe;
            GenPolynomial<BigInteger> g, gi, lui;
            GenPolynomial<BigInteger> gcr, gcq;
            gcr = baseGcd(re1, ce); 
            gcq = baseGcd(qe1, ce); 
            if (gcr.isONE() && gcq.isONE() ) { // both gcds == 1: chose smaller ldcf
                GenPolynomial<BigInteger> rl = r.leadingBaseCoefficient();
                GenPolynomial<BigInteger> ql = q.leadingBaseCoefficient();
                if ( rl.totalDegree() > ql.totalDegree() ) {
                    ui = qd;
                    s = q;
                    he = qe1;
                    pe = qe;
                    BigInteger bn = qd.maxNorm();
                    mn = bn.multiply(cc).multiply(new BigInteger(2L));
                    g = b;
                    logger.debug("select deg: ui = qd, g = b"); //, qe1 = " + qe1); // + ", qe = " + qe);
                } else {
                    ui = rd;
                    s = r;
                    he = re1;
                    pe = re;
                    BigInteger an = rd.maxNorm();
                    mn = an.multiply(cc).multiply(new BigInteger(2L));
                    g = a;
                    logger.debug("select deg: ui = rd, g = a"); //, re1 = " + re1); // + ", re = " + re);
                }
            } else if (gcr.isONE()) {
                ui = rd;
                s = r;
                he = re1;
                pe = re;
                BigInteger an = rd.maxNorm();
                mn = an.multiply(cc).multiply(new BigInteger(2L));
                g = a;
                logger.debug("select: ui = rd, g = a"); //, re1 = " + re1); // + ", re = " + re);
            } else if (gcq.isONE()) {
                ui = qd;
                s = q;
                he = qe1;
                pe = qe;
                BigInteger bn = qd.maxNorm();
                mn = bn.multiply(cc).multiply(new BigInteger(2L));
                g = b;
                logger.debug("select: ui = qd, g = b"); //, qe1 = " + qe1); // + ", qe = " + qe);
            } else { // both gcds != 1: method not applicable
                logger.info("both gcds != 1: method not applicable");
                break;
            }
            lui = s.leadingBaseCoefficient();
            BigInteger ge = PolyUtil.<BigInteger> evaluateAll(g.ring.coFac, lui, V); //g, V);
            if ( ge.isZERO() ) { // this can happen
                continue;
            }
            //System.out.println("ui = " + ui + "\ns = " + s + ", lui = " + lui); // + ", pe-ce*he = " + pe.subtract(ce.multiply(he)));
            ce = ce.multiply(ge);
            //he = he.multiply(ge);
            gi = lui.extendLower(dfac,0,0L); 
            ui = ui.multiply(gi); // ! 
            //System.out.println("ui = " + ui + ", deg(ui) = " + ui.degreeVector());
            //System.out.println("ce = " + ce + ", he = " + he + ", ge = " + ge);
            logger.info("ldcf: " + lui + ", base cofactor: " + he);

            long k = Power.logarithm(new BigInteger(p), mn);
            //System.out.println("mn = " + mn);
            //System.out.println("k = " + k);

            BigInteger qp = Power.positivePower(cofac.getIntegerModul(), k); // == p
            ModularRingFactory<MOD> muqfac;
            if (ModLongRing.MAX_LONG.compareTo(qp.getVal()) > 0) {
                muqfac = (ModularRingFactory) new ModLongRing(qp.getVal());
            } else {
                muqfac = (ModularRingFactory) new ModIntegerRing(qp.getVal());
            }
            GenPolynomialRing<MOD> mucpfac = new GenPolynomialRing<MOD>(muqfac, ckfac);
            //System.out.println("mucpfac = " + mucpfac.toScript());
            if ( muqfac.fromInteger(ge.getVal()).isZERO() ) {
                continue;
            }
            GenPolynomial<MOD> cm = PolyUtil.<MOD> fromIntegerCoefficients(mucpfac, ce);
            GenPolynomial<MOD> hm = PolyUtil.<MOD> fromIntegerCoefficients(mucpfac, he);
            if ( cm.degree(0) != ce.degree(0) || hm.degree(0) != he.degree(0) ) {
                continue;
            }
            if ( cm.isZERO() || hm.isZERO() ) {
                continue;
            }
            logger.info("univariate modulo p^k: " + cm + ", " + hm);

            // convert C from Z[...] to Z_q[...]
            GenPolynomialRing<MOD> qcfac = new GenPolynomialRing<MOD>(muqfac, dfac);
            GenPolynomial<MOD> uq = PolyUtil.<MOD> fromIntegerCoefficients(qcfac, ui);
            if ( !ui.leadingExpVector().equals(uq.leadingExpVector()) ) {
                logger.info("ev(ui) = " + ui.leadingExpVector() + ", ev(uq) = " + uq.leadingExpVector() );
                continue;
            }
            logger.info("multivariate modulo p^k: " + uq);

            List<GenPolynomial<MOD>> F = new ArrayList<GenPolynomial<MOD>>(2);
            F.add(cm);
            F.add(hm);
            List<GenPolynomial<BigInteger>> G = new ArrayList<GenPolynomial<BigInteger>>(2);
            G.add(lui.ring.getONE()); // TODONE 
            G.add(lui);
            List<GenPolynomial<MOD>> lift;
            try {
                //lift = HenselMultUtil.<MOD> liftHenselFull(ui, F, V, k, G);
                lift = HenselMultUtil.<MOD> liftHensel(ui, uq, F, V, k, G);
                logger.info("lift = " + lift);
            } catch (NoLiftingException nle) {
                logger.info("NoLiftingException");
                //System.out.println("exception : " + nle);
                continue;
            } catch (ArithmeticException ae) {
                logger.info("ArithmeticException");
                //System.out.println("exception : " + ae);
                continue;
            } 
            if ( false && ! HenselMultUtil.<MOD> isHenselLift(ui, uq, F, k, lift) ) { // not meaningfull test
                logger.info("isHenselLift: false");
                //continue;
            }

            // convert Ci from Z_{p^k}[x,y1,...,yr] to Z[x,y1,...,yr] to Z[x][y1,...,yr] to Z[y1,...,yr][x]
            GenPolynomial<BigInteger> ci = PolyUtil.integerFromModularCoefficients(dfac, lift.get(0));
            ci = basePrimitivePart(ci);
            GenPolynomial<GenPolynomial<BigInteger>> Cr = PolyUtil.<BigInteger> recursive(rfac, ci);
            GenPolynomial<GenPolynomial<BigInteger>> Cs = PolyUtil.<BigInteger> switchVariables(Cr);
            if (!Cs.ring.equals(P.ring)) {
                System.out.println("Cs.ring = " + Cs.ring + ", P.ring = " + P.ring);
            }
            GenPolynomial<GenPolynomial<BigInteger>> Q = iufd.recursivePrimitivePart(Cs); // TODO remove "iufd."
            Q = iufd.baseRecursivePrimitivePart(Q);
            Q = Q.abs().multiply(c); //.abs();
            GenPolynomial<GenPolynomial<BigInteger>> Pq, Sq;
            Pq = PolyUtil.<BigInteger> recursivePseudoRemainder(P, Q);
            Sq = PolyUtil.<BigInteger> recursivePseudoRemainder(S, Q);
            if ( Pq.isZERO() && Sq.isZERO()) {
                logger.info("gcd normal exit: " + Q);
                return Q;
            }
            logger.info("bad Q = " + Q); // + ", Pq = " + Pq + ", Sq = " + Sq);
        } // end for meta loop
        // Hensel gcd failed
        GenPolynomial<GenPolynomial<BigInteger>> T = iufd.recursiveUnivariateGcd(P, S);
        T = T.abs().multiply(c); //.abs();
        logger.info("no lucky prime or evaluation points, gave up on Hensel: " + T + "= gcd(" + P + "," + S + ")");
        return T;
    }

}
