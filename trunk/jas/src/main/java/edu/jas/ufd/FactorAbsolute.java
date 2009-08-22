/*
 * $Id: FactorAbsolute.java 2470 2009-03-01 12:47:48Z kredel $
 */

package edu.jas.ufd;


import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.PolyUtil;
import edu.jas.poly.AlgebraicNumber;
import edu.jas.poly.AlgebraicNumberRing;

import edu.jas.structure.GcdRingElem;
import edu.jas.structure.Power;
import edu.jas.structure.RingFactory;

import edu.jas.util.KsubSet;


/**
 * Absolute factorization algorithms class.  
 * This class contains
 * implementations of methods for factorization over algebraically
 * closed fields.  The required field extension is computed along with
 * the factors.  The methods have been tested for prime fields of
 * characteristic zero, that is for <code>BigRational</code>.  It
 * might eventually also be used for prime fields of non-zero characteristic,
 * that is with <code>ModInteger</code>.
 * The field extension may yet not be minimal. 
 * @author Heinz Kredel
 * @param <C> coefficient type
 */

public abstract class FactorAbsolute<C extends GcdRingElem<C>> 
                      extends FactorAbstract<C> {


    private static final Logger logger = Logger.getLogger(FactorAbsolute.class);


    private final boolean debug = true || logger.isDebugEnabled();


    /*     
     * Factorization engine for algebraic number coefficients.
     */
    //not possible here because of recursion AN -> Int|Mod -> AN -> ...
    //public final FactorAbstract<AlgebraicNumber<C>> aengine;


    /**
     * Constructor.
     * @param cfac coefficient ring factory.
     */
    public FactorAbsolute(RingFactory<C> cfac) {
        super(cfac);
        //GenPolynomialRing<C> fac = new GenPolynomialRing<C>(cfac,1);
        //GenPolynomial<C> p = fac.univariate(0);
        //AlgebraicNumberRing<C> afac = new AlgebraicNumberRing<C>(p);
        //aengine = null; //FactorFactory.<C>getImplementation(afac); // hack
    }


    /** Get the String representation.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getName();
    }


    /**
     * GenPolynomial test if is absolute irreducible.
     * @param P GenPolynomial.
     * @return true if P is absolute irreducible, else false.
     */
    public boolean isAbsoluteIrreducible(GenPolynomial<C> P) {
        if (!isIrreducible(P)) {
            return false;
        }
        List<GenPolynomial<AlgebraicNumber<C>>> F = factorsAbsoluteIrreducible(P);
        if (F.size() == 1) {
            return true;
        } else if (F.size() > 2) {
            return false;
        } else { //F.size() == 2
            boolean cnst = false;
            for ( GenPolynomial<AlgebraicNumber<C>> p : F ) {
                if ( p.isConstant() ) {
                    cnst = true;
                }
            }
            return cnst;
        }
    }


    /**
     * GenPolynomial absolute base factorization of a polynomial.
     * @param P GenPolynomial.
     * @return [p_1 -&gt; e_1, ..., p_k -&gt; e_k] with P = prod_{i=1,...,k} p_i**e_i.
     * <b>Note:</b> Q(alpha) not yet minimal.
     */
    // @Override
    public SortedMap<GenPolynomial<AlgebraicNumber<C>>,Long> baseFactorsAbsolute(GenPolynomial<C> P) {
        if (P == null) {
            throw new RuntimeException(this.getClass().getName() + " P == null");
        }
        SortedMap<GenPolynomial<AlgebraicNumber<C>>,Long> factors 
            = new TreeMap<GenPolynomial<AlgebraicNumber<C>>,Long>();
        if (P.isZERO()) {
            return factors;
        }
        //System.out.println("\nP = " + P);
        GenPolynomialRing<C> pfac = P.ring; // K[x]
        if (pfac.nvar > 1) {
            throw new RuntimeException("only for univariate polynomials");
        }
        if (!pfac.coFac.isField()) {
            throw new RuntimeException("only for field coefficients");
        }
        // factor over K (=C)
        SortedMap<GenPolynomial<C>,Long> facs = baseFactors(P);
        if (debug && !isFactorization(P, facs)) {
             System.out.println("facs   = " + facs);
             throw new RuntimeException("isFactorization = false");
        }
        if ( logger.isInfoEnabled() ) {
            logger.info("all K factors = " + facs); // Q[X]
            //System.out.println("\nall K factors = " + facs); // Q[X]
        }
        // factor over K(alpha)
        for ( GenPolynomial<C> p : facs.keySet() ) {
            Long e = facs.get(p);
            List<GenPolynomial<AlgebraicNumber<C>>> afacs = baseFactorsAbsoluteIrreducible(p);
            for ( GenPolynomial<AlgebraicNumber<C>> ap : afacs ) {
                //System.out.println("ap = (" + ap + ")**" + e + " over " + ap.ring.coFac);
                if ( logger.isInfoEnabled() ) {
                    logger.info("K(alpha) factors = " + ap + ", alpha: " + ap.ring.coFac); // Q(alpha)[X]
                }
                if ( factors.get(ap) != null ) { // should not happen
                   System.out.println("ap = (" + ap + ")**" + e); 
                   throw new RuntimeException("multiple factors");
                }
                factors.put(ap,e);
            }
        }
        //System.out.println("Q(alpha) factors = " + factors);
        return factors;
    }


    /**
     * GenPolynomial absolute base factorization of a squarefree polynomial.
     * @param P squarefree and primitive GenPolynomial.
     * @return [p_1,...,p_k] with P = prod_{i=1, ..., k} p_i.
     * <b>Note:</b> Q(alpha) not yet minimal.
     */
    // @Override
    public List<GenPolynomial<AlgebraicNumber<C>>> baseFactorsAbsoluteSquarefree(GenPolynomial<C> P) {
        if (P == null) {
            throw new RuntimeException(this.getClass().getName() + " P == null");
        }
        List<GenPolynomial<AlgebraicNumber<C>>> factors = new ArrayList<GenPolynomial<AlgebraicNumber<C>>>();
        if (P.isZERO()) {
            return factors;
        }
        //System.out.println("\nP = " + P);
        GenPolynomialRing<C> pfac = P.ring; // K[x]
        if (pfac.nvar > 1) {
            throw new RuntimeException("only for univariate polynomials");
        }
        if (!pfac.coFac.isField()) {
            throw new RuntimeException("only for field coefficients");
        }
        // factor over K (=C)
        List<GenPolynomial<C>> facs = baseFactorsSquarefree(P);
        if (debug && !isFactorization(P, facs)) {
             throw new RuntimeException("isFactorization = false");
        }
        if ( logger.isInfoEnabled() ) {
            logger.info("all K factors = " + facs); // Q[X]
            //System.out.println("\nall K factors = " + facs); // Q[X]
        }
        // factor over K(alpha)
        for ( GenPolynomial<C> p : facs ) {
            List<GenPolynomial<AlgebraicNumber<C>>> afacs = baseFactorsAbsoluteIrreducible(p);
            if ( logger.isInfoEnabled() ) {
                logger.info("K(alpha) factors = " + afacs); // Q(alpha)[X]
            }
            factors.addAll( afacs );
        }
        //System.out.println("Q(alpha) factors = " + factors);
        return factors;
    }


    /**
     * GenPolynomial base absolute factorization of a irreducible polynomial.
     * @param P irreducible! GenPolynomial.
     * @return [p_1,...,p_k] with P = prod_{i=1, ..., k} p_i in K(alpha)[x] for suitable alpha
     * and p_i irreducible over L[x], 
     * where K \subset K(alpha) \subset L is an algebraically closed field over K.
     * <b>Note:</b> Q(alpha) not yet minimal.
     */
    public List<GenPolynomial<AlgebraicNumber<C>>> baseFactorsAbsoluteIrreducible(GenPolynomial<C> P) {
        if (P == null) {
            throw new RuntimeException(this.getClass().getName() + " P == null");
        }
        List<GenPolynomial<AlgebraicNumber<C>>> factors = new ArrayList<GenPolynomial<AlgebraicNumber<C>>>();
        if (P.isZERO()) {
            return factors;
        }
        GenPolynomialRing<C> pfac = P.ring; // K[x]
        if (pfac.nvar > 1) {
            throw new RuntimeException("only for univariate polynomials");
        }
        if (!pfac.coFac.isField()) {
            throw new RuntimeException("only for field coefficients");
        }
        // setup field extension K(alpha) where alpha = z_xx
        String[] vars = new String[] { "z_" + (P.hashCode()%100) };
        pfac = pfac.clone();
        vars=pfac.setVars(vars);
        P = pfac.copy(P); // hack to exchange the variables
        AlgebraicNumberRing<C> afac = new AlgebraicNumberRing<C>(P,true); // since irreducible
        if ( logger.isInfoEnabled() ) {
            logger.info("Q(alpha) = " + afac); 
            //System.out.println("Q(alpha) = " + afac);
        }
        GenPolynomialRing<AlgebraicNumber<C>> pafac 
            = new GenPolynomialRing<AlgebraicNumber<C>>(afac, P.ring.nvar, P.ring.tord, /*old*/vars);
        // convert to K(alpha)
        GenPolynomial<AlgebraicNumber<C>> Pa = PolyUtil.<C> convertToAlgebraicCoefficients(pafac, P);
        if ( Pa.degree(0) <= 1 ) {
            factors.add(Pa);
            return factors;
        }
        if ( logger.isInfoEnabled() ) {
            logger.info("P over Q(alpha) = " + Pa); 
            //System.out.println("P in Q(alpha) = " + Pa);
        }
        // factor over K(alpha)
        FactorAbstract<AlgebraicNumber<C>> engine = FactorFactory.<C>getImplementation(afac);
        factors = engine.baseFactorsSquarefree( Pa );
        if ( debug ) {
            logger.info("factors over Q(alpha) = " + factors); 
            //System.out.println("factors over Q(alpha) = " + factors);
        }
        // find minimal field extension K(beta) \subset K(alpha)
        return factors;
    }


    /**
     * GenPolynomial absolute base factorization of a polynomial.
     * @param P GenPolynomial.
     * @return [p_1 -&gt; e_1, ..., p_k -&gt; e_k] with P = prod_{i=1,...,k} p_i**e_i.
     * <b>Note:</b> Q(alpha) not yet minimal.
     */
    public SortedMap<GenPolynomial<AlgebraicNumber<C>>,Long> factorsAbsolute(GenPolynomial<C> P) {
        if (P == null) {
            throw new RuntimeException(this.getClass().getName() + " P == null");
        }
        SortedMap<GenPolynomial<AlgebraicNumber<C>>,Long> factors 
            = new TreeMap<GenPolynomial<AlgebraicNumber<C>>,Long>();
        if (P.isZERO()) {
            return factors;
        }
        //System.out.println("\nP = " + P);
        GenPolynomialRing<C> pfac = P.ring; // K[x]
        if (pfac.nvar <= 1) {
            return baseFactorsAbsolute(P);
        }
        if (!pfac.coFac.isField()) {
            throw new RuntimeException("only for field coefficients");
        }
        // factor over K (=C)
        SortedMap<GenPolynomial<C>,Long> facs = factors(P);
        if (debug && !isFactorization(P, facs)) {
             throw new RuntimeException("isFactorization = false");
        }
        if ( debug ) {
            logger.info("all K factors = " + facs); // Q[X]
            //System.out.println("\nall K factors = " + facs); // Q[X]
        }
        // factor over K(alpha)
        for ( GenPolynomial<C> p : facs.keySet() ) {
            Long e = facs.get(p);
            List<GenPolynomial<AlgebraicNumber<C>>> afacs = factorsAbsoluteIrreducible(p);
            for ( GenPolynomial<AlgebraicNumber<C>> ap : afacs ) {
                if ( debug ) {
                    logger.info("K(alpha) factors = " + ap + ", alpha: " + ap.ring.coFac); // Q(alpha)[X]
                }
                if ( factors.get(ap) != null ) {
                   System.out.println("ap = (" + ap + ")**" + e); 
                   throw new RuntimeException("multiple factors");
                }
                factors.put(ap,e);
            }
        }
        //System.out.println("Q(alpha) factors = " + factors);
        return factors;
    }


    /**
     * GenPolynomial absolute base factorization of a squarefree polynomial.
     * @param P squarefree and primitive GenPolynomial.
     * @return [p_1,...,p_k] with P = prod_{i=1, ..., k} p_i.
     * <b>Note:</b> Q(alpha) not yet minimal.
     */
    // @Override
    public List<GenPolynomial<AlgebraicNumber<C>>> factorsAbsoluteSquarefree(GenPolynomial<C> P) {
        if (P == null) {
            throw new RuntimeException(this.getClass().getName() + " P == null");
        }
        List<GenPolynomial<AlgebraicNumber<C>>> factors = new ArrayList<GenPolynomial<AlgebraicNumber<C>>>();
        if (P.isZERO()) {
            return factors;
        }
        //System.out.println("\nP = " + P);
        GenPolynomialRing<C> pfac = P.ring; // K[x]
        if (pfac.nvar <= 1) {
            return baseFactorsAbsoluteSquarefree(P);
        }
        if (!pfac.coFac.isField()) {
            throw new RuntimeException("only for field coefficients");
        }
        // factor over K (=C)
        List<GenPolynomial<C>> facs = factorsSquarefree(P);
        if (debug && !isFactorization(P, facs)) {
             throw new RuntimeException("isFactorization = false");
        }
        if ( debug ) {
            logger.info("all K factors = " + facs); // Q[X]
            //System.out.println("\nall K factors = " + facs); // Q[X]
        }
        // factor over K(alpha)
        for ( GenPolynomial<C> p : facs ) {
            List<GenPolynomial<AlgebraicNumber<C>>> afacs = factorsAbsoluteIrreducible(p);
            if ( debug ) {
                logger.info("K(alpha) factors = " + afacs); // Q(alpha)[X]
            }
            factors.addAll( afacs );
        }
        //System.out.println("Q(alpha) factors = " + factors);
        return factors;
    }


    /**
     * GenPolynomial base absolute factorization of a irreducible polynomial.
     * @param P irreducible! GenPolynomial.
     * @return [p_1,...,p_k] with P = prod_{i=1, ..., k} p_i in K(alpha)[x] for suitable alpha
     * and p_i irreducible over L[x], 
     * where K \subset K(alpha) \subset L is an algebraically closed field over K.
     * <b>Note:</b> Q(alpha) not yet minimal.
     */
    public List<GenPolynomial<AlgebraicNumber<C>>> factorsAbsoluteIrreducible(GenPolynomial<C> P) {
        if (P == null) {
            throw new RuntimeException(this.getClass().getName() + " P == null");
        }
        List<GenPolynomial<AlgebraicNumber<C>>> factors = new ArrayList<GenPolynomial<AlgebraicNumber<C>>>();
        if ( P.isZERO() /*todo:|| P.degree() <= 1*/ ) { // max deg
            return factors;
        }
        GenPolynomialRing<C> pfac = P.ring; // K[x]
        if (pfac.nvar <= 1) {
            return baseFactorsAbsoluteIrreducible(P);
        }
        if (!pfac.coFac.isField()) {
            throw new RuntimeException("only for field coefficients");
        }
        // find field extension K(alpha)
        GenPolynomial<C> up = P;
        RingFactory<C> cf = pfac.coFac;
        long cr = cf.characteristic().longValue(); // char might be larger
        if ( cr == 0L ) {
            cr = Long.MAX_VALUE;
        }
        long rp = 0L;
        for ( int i = 0; i < (pfac.nvar-1); i++ ) {
            rp = 0L;
            GenPolynomialRing<C> nfac = pfac.contract(1);
            String[] vn = new String[] { pfac.getVars()[pfac.nvar-1] };
            GenPolynomialRing<GenPolynomial<C>> rfac 
               = new GenPolynomialRing<GenPolynomial<C>>(nfac,1,pfac.tord,vn);
            GenPolynomial<GenPolynomial<C>> upr = PolyUtil.<C>recursive(rfac,up);
            //System.out.println("upr = " + upr);
            GenPolynomial<C> ep;
            do {
                if ( rp >= cr ) {
                    throw new RuntimeException("elements of prime field exhausted: " + cr);
                }
                C r = cf.fromInteger(rp); //cf.random(rp);
                //System.out.println("r   = " + r);
                ep = PolyUtil.<C>evaluateMain(nfac,upr,r);
                //System.out.println("ep  = " + ep);
                rp++;
            } while( !isSquarefree(ep) /*todo: || ep.degree() <= 1*/ ); // max deg
            up = ep;
            pfac = nfac;
        }
        up = up.monic();
        if ( debug ) {
            logger.info("P("+rp+") = " + up); 
            //System.out.println("up  = " + up);
        }
        if ( debug && !isSquarefree(up) ) {
            throw new RuntimeException("not irreducible up = " + up);
        }
        // find irreducible factor of up
        List<GenPolynomial<C>> UF = baseFactorsSquarefree(up);
        //System.out.println("UF  = " + UF);
        long e = up.degree(0);
        // search factor polynomial with smallest degree 
        for ( int i = 0; i < UF.size(); i++ ) {
            GenPolynomial<C> upi = UF.get(i);
            long d = upi.degree(0);
            if ( 1 <= d && d <= e ) {
                up = upi;
                e = up.degree(0);
            }
        }
        if ( up.degree(0) <= 1 ) {
            // what to take ? zero would be correct but cannot be used for Q(alpha)
            System.out.println("up degree too small: " + up);
        }
        if ( debug ) {
            logger.info("field extension by " + up); 
        }

        // setup field extension K(alpha)
        String[] vars = new String[] { "alpha" /*"pfac.getVars().hashCode()*/ };
        String[] ovars = pfac.setVars(vars); // side effects! 
        //up = pfac.copy(up); // hack

        AlgebraicNumberRing<C> afac = new AlgebraicNumberRing<C>(up,true); // since irreducible
        //System.out.println("afac = " + afac);
        GenPolynomialRing<AlgebraicNumber<C>> pafac 
            = new GenPolynomialRing<AlgebraicNumber<C>>(afac, P.ring.nvar,P.ring.tord,P.ring.getVars());
        //System.out.println("pafac = " + pafac);
        // convert to K(alpha)
        GenPolynomial<AlgebraicNumber<C>> Pa = PolyUtil.<C> convertToAlgebraicCoefficients(pafac, P);
        if ( Pa.degree() <= 1 || up.degree(0) <= 1 ) { // max deg and deg_main
            factors.add(Pa);
            return factors;
        }
        //System.out.println("Pa = " + Pa);
        // factor over K(alpha)
        FactorAbstract<AlgebraicNumber<C>> engine = FactorFactory.<C>getImplementation(afac);
        factors = engine.factorsSquarefree( Pa );
        if ( debug ) {
            logger.info("Q(alpha) factors = " + factors);
            //System.out.println("Q(alpha) factors = " + factors);
        }
        if ( factors.size() > 1 ) {
            GenPolynomial<AlgebraicNumber<C>> p1 = factors.get(0);
            AlgebraicNumber<C> p1c = p1.leadingBaseCoefficient();
            if ( !p1c.isONE() ) {
                GenPolynomial<AlgebraicNumber<C>> p2 = factors.get(1);
                factors.remove(p1);
                factors.remove(p2);
                p1 = p1.divide(p1c);
                p2 = p2.multiply(p1c);
                factors.add(p1);
                factors.add(p2);
            }
        }
        // find minimal field extension K(beta) \subset K(alpha)
        return factors;
    }


    /**
     * GenPolynomial is factorization.
     * @param P GenPolynomial.
     * @param F = [p_1,...,p_k].
     * @return true if P = prod_{i=1,...,r} p_i, else false.
     */
    public boolean nonoAbsoluteFactorization(GenPolynomial<C> P, List<GenPolynomial<AlgebraicNumber<C>>> F) {
        if (P == null || F == null || F.isEmpty()) {
            throw new IllegalArgumentException("P or F may not be null or empty");
        }
        GenPolynomial<AlgebraicNumber<C>> fa = F.get(0); // not ok since different Q(alpha)
        GenPolynomialRing<AlgebraicNumber<C>> pafac = fa.ring;
        // convert to K(alpha)
        GenPolynomial<AlgebraicNumber<C>> Pa 
            = PolyUtil.<C> convertToAlgebraicCoefficients(pafac, P);

        GenPolynomial<AlgebraicNumber<C>> t = Pa.ring.getONE();
        for (GenPolynomial<AlgebraicNumber<C>> f : F) {
            t = t.multiply(f);
        }
        return Pa.equals(t) || Pa.equals(t.negate());
    }


    /**
     * GenPolynomial is factorization.
     * @param P GenPolynomial.
     * @param F = [p_1 -&gt; e_1, ..., p_k -&gt; e_k].
     * @return true if P = prod_{i=1,...,k} p_i**e_i , else false.
     */
    public boolean nonoAbsoluteFactorization(GenPolynomial<C> P, SortedMap<GenPolynomial<AlgebraicNumber<C>>, Long> F) {
        if (P == null || F == null || F.isEmpty()) {
            throw new IllegalArgumentException("P or F may not be null or empty");
        }
        GenPolynomial<AlgebraicNumber<C>> fa = F.firstKey(); // not ok since different Q(alpha)
        GenPolynomialRing<AlgebraicNumber<C>> pafac = fa.ring;
        // convert to K(alpha)
        GenPolynomial<AlgebraicNumber<C>> Pa 
            = PolyUtil.<C> convertToAlgebraicCoefficients(pafac, P);

        GenPolynomial<AlgebraicNumber<C>> t = Pa.ring.getONE();
        for (GenPolynomial<AlgebraicNumber<C>> f : F.keySet()) {
            Long E = F.get(f);
            long e = E.longValue();
            GenPolynomial<AlgebraicNumber<C>> g = Power.<GenPolynomial<AlgebraicNumber<C>>> positivePower(f, e);
            t = t.multiply(g);
        }
        boolean f = Pa.equals(t) || Pa.equals(t.negate());
        if (!f) {
            System.out.println("\nfactorization: " + f);
            System.out.println("P = " + P);
            System.out.println("t = " + t);
        }
        return f;
    }

}
