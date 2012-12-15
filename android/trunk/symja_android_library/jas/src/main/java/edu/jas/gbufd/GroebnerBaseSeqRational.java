/*
 * $Id: GroebnerBaseSeqRational.java 4089 2012-08-04 10:33:17Z kredel $
 */

package edu.jas.gbufd;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.jas.arith.BigInteger;
import edu.jas.arith.BigRational;
import edu.jas.gb.GroebnerBaseAbstract;
import edu.jas.gb.GroebnerBaseSeq;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.PolyUtil;


/**
 * Groebner Base sequential algorithm for rational coefficients, fraction free
 * computation. Implements Groebner bases.
 * @param <C> BigRational coefficient type
 * @author Heinz Kredel
 */

public class GroebnerBaseSeqRational<C extends BigRational> extends GroebnerBaseSeq<BigRational> {


    private static final Logger logger = Logger.getLogger(GroebnerBaseSeqRational.class);


    private final boolean debug = logger.isDebugEnabled();


    public final GroebnerBaseAbstract<BigInteger> bba;


    /**
     * Constructor.
     */
    public GroebnerBaseSeqRational() {
        super();
        bba = new GroebnerBasePseudoSeq<BigInteger>(new BigInteger());
    }


    /**
     * Groebner base using fraction free computation.
     * @param modv module variable number.
     * @param F polynomial list.
     * @return GB(F) a Groebner base of F.
     */
    @Override
    public List<GenPolynomial<BigRational>> GB(int modv, List<GenPolynomial<BigRational>> F) {
        List<GenPolynomial<BigRational>> G = F;
        if (F == null || F.isEmpty()) {
            return G;
        }
        GenPolynomialRing<BigRational> rring = F.get(0).ring;
        BigInteger cf = new BigInteger();
        GenPolynomialRing<BigInteger> iring = new GenPolynomialRing<BigInteger>(cf, rring);
        List<GenPolynomial<BigInteger>> Fi = PolyUtil.integerFromRationalCoefficients(iring, F);
        //System.out.println("Fi = " + Fi);
        logger.info("#Fi = " + Fi.size());

        List<GenPolynomial<BigInteger>> Gi = bba.GB(modv, Fi);
        //System.out.println("Gi = " + Gi);
        logger.info("#Gi = " + Gi.size());

        G = PolyUtil.<BigRational> fromIntegerCoefficients(rring, Gi);
        G = PolyUtil.<BigRational> monic(G);
        return G;
    }


    /**
     * Minimal ordered Groebner basis.
     * @param Gp a Groebner base.
     * @return a reduced Groebner base of Gp.
     */
    @Override
    public List<GenPolynomial<BigRational>> minimalGB(List<GenPolynomial<BigRational>> Gp) {
        if (Gp == null || Gp.size() <= 1) {
            return Gp;
        }
        // remove zero polynomials
        List<GenPolynomial<BigRational>> G = new ArrayList<GenPolynomial<BigRational>>(Gp.size());
        for (GenPolynomial<BigRational> a : Gp) {
            if (a != null && !a.isZERO()) { // always true in GB()
                // already positive a = a.abs();
                G.add(a);
            }
        }
        if (G.size() <= 1) {
            return G;
        }
        // remove top reducible polynomials
        GenPolynomial<BigRational> a;
        List<GenPolynomial<BigRational>> F;
        F = new ArrayList<GenPolynomial<BigRational>>(G.size());
        while (G.size() > 0) {
            a = G.remove(0);
            if (red.isTopReducible(G, a) || red.isTopReducible(F, a)) {
                // drop polynomial 
                if (debug) {
                    System.out.println("dropped " + a);
                    List<GenPolynomial<BigRational>> ff;
                    ff = new ArrayList<GenPolynomial<BigRational>>(G);
                    ff.addAll(F);
                    a = red.normalform(ff, a);
                    if (!a.isZERO()) {
                        System.out.println("error, nf(a) " + a);
                    }
                }
            } else {
                F.add(a);
            }
        }
        G = F;
        if (G.size() <= 1) {
            return G;
        }

        // reduce remaining polynomials
        GenPolynomialRing<BigRational> rring = G.get(0).ring;
        BigInteger cf = new BigInteger();
        GenPolynomialRing<BigInteger> iring = new GenPolynomialRing<BigInteger>(cf, rring);
        List<GenPolynomial<BigInteger>> Fi = PolyUtil.integerFromRationalCoefficients(iring, F);
        logger.info("#Fi = " + Fi.size());

        List<GenPolynomial<BigInteger>> Gi = bba.minimalGB(Fi);
        logger.info("#Gi = " + Gi.size());

        G = PolyUtil.<BigRational> fromIntegerCoefficients(rring, Gi);
        G = PolyUtil.<BigRational> monic(G);
        return G;
    }

}
