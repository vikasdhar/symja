/*
 * $Id: PolyUfdUtilTest.java 2434 2009-02-15 19:40:24Z kredel $
 */

package edu.jas.ufd;


import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.jas.arith.BigInteger;
import edu.jas.arith.ModInteger;
import edu.jas.arith.ModIntegerRing;
import edu.jas.kern.ComputerThreads;
import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.PolyUtil;
import edu.jas.poly.TermOrder;
import edu.jas.structure.Power;
import edu.jas.util.KsubSet;


/**
 * PolyUfdUtil tests with JUnit.
 * @author Heinz Kredel.
 */

public class PolyUfdUtilTest extends TestCase {


    /**
     * main.
     */
    public static void main(String[] args) {
        //BasicConfigurator.configure();
        junit.textui.TestRunner.run(suite());
        ComputerThreads.terminate();
    }


    /**
     * Constructs a <CODE>PolyUtilTest</CODE> object.
     * @param name String.
     */
    public PolyUfdUtilTest(String name) {
        super(name);
    }


    /**
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(PolyUfdUtilTest.class);
        return suite;
    }


    //private final static int bitlen = 100;

    TermOrder to = new TermOrder(TermOrder.INVLEX);


    GenPolynomialRing<BigInteger> dfac;


    GenPolynomialRing<BigInteger> cfac;


    GenPolynomialRing<GenPolynomial<BigInteger>> rfac;


    BigInteger ai;


    BigInteger bi;


    BigInteger ci;


    BigInteger di;


    BigInteger ei;


    GenPolynomial<BigInteger> a;


    GenPolynomial<BigInteger> b;


    GenPolynomial<BigInteger> c;


    GenPolynomial<BigInteger> d;


    GenPolynomial<BigInteger> e;


    int rl = 5;


    int kl = 5;


    int ll = 5;


    int el = 3;


    float q = 0.3f;


    @Override
    protected void setUp() {
        a = b = c = d = e = null;
        ai = bi = ci = di = ei = null;
        dfac = new GenPolynomialRing<BigInteger>(new BigInteger(1), rl, to);
        cfac = new GenPolynomialRing<BigInteger>(new BigInteger(1), rl - 1, to);
        rfac = new GenPolynomialRing<GenPolynomial<BigInteger>>(cfac, 1, to);
    }


    @Override
    protected void tearDown() {
        a = b = c = d = e = null;
        ai = bi = ci = di = ei = null;
        dfac = null;
        cfac = null;
        rfac = null;
    }


    protected static java.math.BigInteger getPrime1() {
        long prime = 2; //2^60-93; // 2^30-35; //19; knuth (2,390)
        for (int i = 1; i < 60; i++) {
            prime *= 2;
        }
        prime -= 93;
        //prime = 37;
        //System.out.println("p1 = " + prime);
        return new java.math.BigInteger("" + prime);
    }


    protected static java.math.BigInteger getPrime2() {
        long prime = 2; //2^60-93; // 2^30-35; //19; knuth (2,390)
        for (int i = 1; i < 30; i++) {
            prime *= 2;
        }
        prime -= 35;
        //prime = 19;
        //System.out.println("p1 = " + prime);
        return new java.math.BigInteger("" + prime);
    }


    /**
     * Test Hensel lifting.
     * 
     */
    public void testHenselLifting() {
        java.math.BigInteger p;
        p = getPrime1();
        //p = new java.math.BigInteger("19");
        //p = new java.math.BigInteger("5");
        BigInteger m = new BigInteger(p);
        //.multiply(p).multiply(p).multiply(p);

        BigInteger mi = m;

        ModIntegerRing pm = new ModIntegerRing(p, true);
        GenPolynomialRing<ModInteger> pfac = new GenPolynomialRing<ModInteger>(pm, 1, to);

        dfac = new GenPolynomialRing<BigInteger>(mi, 1, to);

        GenPolynomial<ModInteger> ap;
        GenPolynomial<ModInteger> bp;
        GenPolynomial<ModInteger> cp;
        GenPolynomial<ModInteger> sp;
        GenPolynomial<ModInteger> tp;
        GenPolynomial<ModInteger>[] egcd;
        GenPolynomial<ModInteger> ap1;
        GenPolynomial<ModInteger> bp1;

        GenPolynomial<BigInteger>[] lift;
        GenPolynomial<BigInteger> a1;
        GenPolynomial<BigInteger> b1;
        GenPolynomial<BigInteger> c1;

        for (int i = 1; i < 3; i++) {
            a = dfac.random(kl + 70 * i, ll, el + 5, q).abs();
            b = dfac.random(kl + 70 * i, ll, el + 5, q).abs();
            //a = dfac.univariate(0).sum( dfac.fromInteger(30) );
            //b = dfac.univariate(0).subtract( dfac.fromInteger(20) );
            //b = b.multiply( dfac.univariate(0) ).sum( dfac.fromInteger(168));
            c = a.multiply(b);
            if (a.degree(0) < 1 || b.degree(0) < 2) {
                continue;
            }

            ap = PolyUtil.fromIntegerCoefficients(pfac, a);
            if (!a.degreeVector().equals(ap.degreeVector())) {
                continue;
            }
            bp = PolyUtil.fromIntegerCoefficients(pfac, b);
            if (!b.degreeVector().equals(bp.degreeVector())) {
                continue;
            }
            cp = PolyUtil.fromIntegerCoefficients(pfac, c);
            if (!c.degreeVector().equals(cp.degreeVector())) {
                continue;
            }

            ap1 = ap; //.monic();
            bp1 = bp; //.monic();
            egcd = ap1.egcd(bp1);
            if (!egcd[0].isONE()) {
                continue;
            }
            sp = egcd[1];
            tp = egcd[2];

            BigInteger an = a.maxNorm();
            BigInteger bn = b.maxNorm();
            if (an.compareTo(bn) > 0) {
                mi = an;
            } else {
                mi = bn;
            }
            BigInteger cn = c.maxNorm();
            if (cn.compareTo(mi) > 0) {
                mi = cn;
            }

            //System.out.println("a     = " + a);
            //System.out.println("b     = " + b);
            //System.out.println("c     = " + c);
            //--System.out.println("mi    = " + mi);
            //System.out.println("ap    = " + ap);
            //System.out.println("bp    = " + bp);
            //System.out.println("cp    = " + cp);
            // System.out.println("ap*bp = " + ap.multiply(bp));
            //System.out.println("gcd   = " + egcd[0]);
            //System.out.println("gcd   = " + ap1.multiply(sp).sum(bp1.multiply(tp)));
            //System.out.println("sp    = " + sp);
            //System.out.println("tp    = " + tp);

            lift = PolyUfdUtil.liftHensel(c, mi, ap, bp, sp, tp);
            a1 = lift[0];
            b1 = lift[1];
            c1 = a1.multiply(b1);

            //System.out.println("\na     = " + a);
            //System.out.println("b     = " + b);
            //System.out.println("c     = " + c);
            //System.out.println("a1    = " + a1);
            //System.out.println("b1    = " + b1);
            //System.out.println("a1*b1 = " + c1);

            //assertEquals("lift(a mod p) = a",a,a1);
            //assertEquals("lift(b mod p) = b",b,b1);
            assertEquals("lift(a b mod p) = a b", c, c1);
            //break;
        }
    }


    /**
     * Test Hensel quadratic lifting.
     * 
     */
    public void testHenselQuadraticLifting() {
        java.math.BigInteger p;
        //p = getPrime1();
        p = new java.math.BigInteger("19");
        //p = new java.math.BigInteger("5");
        BigInteger m = new BigInteger(p);
        //.multiply(p).multiply(p).multiply(p);

        BigInteger mi = m;

        ModIntegerRing pm = new ModIntegerRing(p, true);
        GenPolynomialRing<ModInteger> pfac = new GenPolynomialRing<ModInteger>(pm, 1, to);

        dfac = new GenPolynomialRing<BigInteger>(mi, 1, to);

        GenPolynomial<ModInteger> ap;
        GenPolynomial<ModInteger> bp;
        GenPolynomial<ModInteger> cp;
        GenPolynomial<ModInteger> sp;
        GenPolynomial<ModInteger> tp;
        GenPolynomial<ModInteger>[] egcd;
        GenPolynomial<ModInteger> ap1;
        GenPolynomial<ModInteger> bp1;

        GenPolynomial<BigInteger>[] lift;
        GenPolynomial<BigInteger> a1;
        GenPolynomial<BigInteger> b1;
        GenPolynomial<BigInteger> c1;

        for (int i = 1; i < 3; i++) { // 70 better for quadratic
            a = dfac.random(kl + 70 * i, ll + 10, el + 5, q).abs();
            b = dfac.random(kl + 70 * i, ll + 10, el + 5, q).abs();
            //a = dfac.univariate(0).sum( dfac.fromInteger(30) );
            //b = dfac.univariate(0).subtract( dfac.fromInteger(20) );
            //b = b.multiply( dfac.univariate(0) ).sum( dfac.fromInteger(168));
            c = a.multiply(b);
            if (a.degree(0) < 1 || b.degree(0) < 2) {
                continue;
            }

            ap = PolyUtil.fromIntegerCoefficients(pfac, a);
            if (!a.degreeVector().equals(ap.degreeVector())) {
                continue;
            }
            bp = PolyUtil.fromIntegerCoefficients(pfac, b);
            if (!b.degreeVector().equals(bp.degreeVector())) {
                continue;
            }
            cp = PolyUtil.fromIntegerCoefficients(pfac, c);
            if (!c.degreeVector().equals(cp.degreeVector())) {
                continue;
            }

            ap1 = ap; //.monic();
            bp1 = bp; //.monic();
            egcd = ap1.egcd(bp1);
            if (!egcd[0].isONE()) {
                continue;
            }
            sp = egcd[1];
            tp = egcd[2];

            BigInteger an = a.maxNorm();
            BigInteger bn = b.maxNorm();
            if (an.compareTo(bn) > 0) {
                mi = an;
            } else {
                mi = bn;
            }
            BigInteger cn = c.maxNorm();
            if (cn.compareTo(mi) > 0) {
                mi = cn;
            }

            //System.out.println("a     = " + a);
            //System.out.println("b     = " + b);
            //System.out.println("c     = " + c);
            //--System.out.println("mi    = " + mi);
            //System.out.println("ap    = " + ap);
            //System.out.println("bp    = " + bp);
            //System.out.println("cp    = " + cp);
            // System.out.println("ap*bp = " + ap.multiply(bp));
            //System.out.println("gcd   = " + egcd[0]);
            //System.out.println("gcd   = " + ap1.multiply(sp).sum(bp1.multiply(tp)));
            //System.out.println("sp    = " + sp);
            //System.out.println("tp    = " + tp);

            long tq = System.currentTimeMillis();
            lift = PolyUfdUtil.liftHenselQuadratic(c, mi, ap, bp, sp, tp);
            tq = System.currentTimeMillis() - tq;
            a1 = lift[0];
            b1 = lift[1];
            c1 = a1.multiply(b1);

            //System.out.println("\na     = " + a);
            //System.out.println("b     = " + b);
            //System.out.println("c     = " + c);
            //System.out.println("a1    = " + a1);
            //System.out.println("b1    = " + b1);
            //System.out.println("a1*b1 = " + c1);

            //assertEquals("lift(a mod p) = a",a,a1);
            //assertEquals("lift(b mod p) = b",b,b1);
            assertEquals("lift(a b mod p) = a b", c, c1);

            if (false) {
                long t = System.currentTimeMillis();
                lift = PolyUfdUtil.liftHensel(c, mi, ap, bp, sp, tp);
                t = System.currentTimeMillis() - t;
                a1 = lift[0];
                b1 = lift[1];
                c1 = a1.multiply(b1);

                //System.out.println("\na     = " + a);
                //System.out.println("b     = " + b);
                //System.out.println("c     = " + c);
                //System.out.println("a1    = " + a1);
                //System.out.println("b1    = " + b1);
                //System.out.println("a1*b1 = " + c1);

                //assertEquals("lift(a mod p) = a",a,a1);
                //assertEquals("lift(b mod p) = b",b,b1);
                assertEquals("lift(a b mod p) = a b", c, c1);

                System.out.println("\nquadratic Hensel time = " + tq);
                System.out.println("linear    Hensel time = " + t);
            }
            //break;
        }
    }


    /**
     * Test Hensel quadratic lifting with gcd.
     * 
     */
    public void testHenselQuadraticLiftingGcd() {
        java.math.BigInteger p;
        //p = getPrime1();
        p = new java.math.BigInteger("19");
        //p = new java.math.BigInteger("5");
        BigInteger m = new BigInteger(p);
        //.multiply(p).multiply(p).multiply(p);

        BigInteger mi = m;

        ModIntegerRing pm = new ModIntegerRing(p, true);
        GenPolynomialRing<ModInteger> pfac = new GenPolynomialRing<ModInteger>(pm, 1, to);

        dfac = new GenPolynomialRing<BigInteger>(mi, 1, to);

        GenPolynomial<ModInteger> ap;
        GenPolynomial<ModInteger> bp;
        GenPolynomial<ModInteger> cp;

        GenPolynomial<BigInteger>[] lift;
        GenPolynomial<BigInteger> a1;
        GenPolynomial<BigInteger> b1;
        GenPolynomial<BigInteger> c1;

        for (int i = 1; i < 3; i++) { // 70 better for quadratic
            a = dfac.random(kl + 70 * i, ll + 10, el + 5, q).abs();
            b = dfac.random(kl + 70 * i, ll + 10, el + 5, q).abs();
            //a = dfac.univariate(0).sum( dfac.fromInteger(30) );
            //b = dfac.univariate(0).subtract( dfac.fromInteger(20) );
            //b = b.multiply( dfac.univariate(0) ).sum( dfac.fromInteger(168));
            c = a.multiply(b);
            if (a.degree(0) < 1 || b.degree(0) < 2) {
                continue;
            }

            ap = PolyUtil.fromIntegerCoefficients(pfac, a);
            if (!a.degreeVector().equals(ap.degreeVector())) {
                continue;
            }
            bp = PolyUtil.fromIntegerCoefficients(pfac, b);
            if (!b.degreeVector().equals(bp.degreeVector())) {
                continue;
            }
            cp = PolyUtil.fromIntegerCoefficients(pfac, c);
            if (!c.degreeVector().equals(cp.degreeVector())) {
                continue;
            }

            BigInteger an = a.maxNorm();
            BigInteger bn = b.maxNorm();
            if (an.compareTo(bn) > 0) {
                mi = an;
            } else {
                mi = bn;
            }
            BigInteger cn = c.maxNorm();
            if (cn.compareTo(mi) > 0) {
                mi = cn;
            }

            //System.out.println("a     = " + a);
            //System.out.println("b     = " + b);
            //System.out.println("c     = " + c);
            //--System.out.println("mi    = " + mi);
            //System.out.println("ap    = " + ap);
            //System.out.println("bp    = " + bp);
            //System.out.println("cp    = " + cp);
            // System.out.println("ap*bp = " + ap.multiply(bp));
            //System.out.println("gcd   = " + egcd[0]);
            //System.out.println("gcd   = " + ap1.multiply(sp).sum(bp1.multiply(tp)));
            //System.out.println("sp    = " + sp);
            //System.out.println("tp    = " + tp);

            long tq = System.currentTimeMillis();
            try {
                lift = PolyUfdUtil.liftHenselQuadratic(c, mi, ap, bp);
                tq = System.currentTimeMillis() - tq;
                a1 = lift[0];
                b1 = lift[1];
                c1 = a1.multiply(b1);
                assertEquals("lift(a b mod p) = a b", c, c1);
            } catch (RuntimeException e) {
                // a and b not coprime
                System.out.println("e = " + e);
            }

            //System.out.println("\na     = " + a);
            //System.out.println("b     = " + b);
            //System.out.println("c     = " + c);
            //System.out.println("a1    = " + a1);
            //System.out.println("b1    = " + b1);
            //System.out.println("a1*b1 = " + c1);

            //assertEquals("lift(a mod p) = a",a,a1);
            //assertEquals("lift(b mod p) = b",b,b1);
        }
    }


    /**
     * Test Hensel quadratic lifting of list.
     * 
     */
    public void testHenselQuadraticLiftingList() {
        java.math.BigInteger p;
        //p = getPrime1();
        //p = new java.math.BigInteger("19");
        p = new java.math.BigInteger("23");
        BigInteger m = new BigInteger(p);
        //.multiply(p).multiply(p).multiply(p);

        BigInteger mi = m;

        ModIntegerRing pm = new ModIntegerRing(p, true);
        GenPolynomialRing<ModInteger> pfac = new GenPolynomialRing<ModInteger>(pm, 1, to);

        dfac = new GenPolynomialRing<BigInteger>(mi, 1, to);

        BigInteger one = mi.getONE();

        GenPolynomial<ModInteger> ap;
        GenPolynomial<ModInteger> bp;
        GenPolynomial<ModInteger> cp;
        GenPolynomial<ModInteger> dp;

        List<GenPolynomial<BigInteger>> lift;
        GenPolynomial<BigInteger> cl;

        for (int i = 1; i < 7; i++) { // 70 better for quadratic
            a = dfac.random(kl + i, ll + 0, el + i, q).abs();
            b = dfac.random(kl + i, ll + 0, el + 1, q).abs();
            d = dfac.random(kl + i, ll + 0, el + i, q).abs();
            //a = dfac.univariate(0).sum( dfac.fromInteger(30) );
            //b = dfac.univariate(0).subtract( dfac.fromInteger(20) );
            //b = b.multiply( dfac.univariate(0) ).sum( dfac.fromInteger(168));
            if (a.degree(0) < 1 || b.degree(0) < 2 || d.degree(0) < 1) {
                continue;
            }
            if (!a.leadingBaseCoefficient().isUnit()) {
                ExpVector e = a.leadingExpVector();
                a.doPutToMap(e, one);
            }
            if (!b.leadingBaseCoefficient().isUnit()) {
                ExpVector e = b.leadingExpVector();
                b.doPutToMap(e, one);
            }
            if (!d.leadingBaseCoefficient().isUnit()) {
                ExpVector e = d.leadingExpVector();
                d.doPutToMap(e, one);
            }
            GreatestCommonDivisorAbstract<BigInteger> engine = GCDFactory.getProxy(mi);
            //          a = engine.basePrimitivePart(a);
            //          b = engine.basePrimitivePart(b);
            //          d = engine.basePrimitivePart(d);

            GenPolynomial<BigInteger> g;
            g = engine.baseGcd(a, b);
            if (!g.isConstant()) {
                a = a.divide(g);
                b = b.divide(g);
            }
            g = engine.baseGcd(a, d);
            if (!g.isConstant()) {
                a = a.divide(g);
                d = d.divide(g);
            }
            g = engine.baseGcd(b, d);
            if (!g.isConstant()) {
                b = b.divide(g);
                d = d.divide(g);
            }
            c = a.multiply(b).multiply(d);

            ap = PolyUtil.fromIntegerCoefficients(pfac, a);
            if (!a.degreeVector().equals(ap.degreeVector())) {
                continue;
            }
            bp = PolyUtil.fromIntegerCoefficients(pfac, b);
            if (!b.degreeVector().equals(bp.degreeVector())) {
                continue;
            }
            dp = PolyUtil.fromIntegerCoefficients(pfac, d);
            if (!d.degreeVector().equals(dp.degreeVector())) {
                continue;
            }
            cp = PolyUtil.fromIntegerCoefficients(pfac, c);
            if (!c.degreeVector().equals(cp.degreeVector())) {
                continue;
            }

            BigInteger an = a.maxNorm();
            BigInteger bn = b.maxNorm();
            mi = (an.compareTo(bn) > 0 ? an : bn);
            BigInteger dn = d.maxNorm();
            mi = (mi.compareTo(dn) > 0 ? mi : dn);
            BigInteger cn = c.maxNorm();
            mi = (mi.compareTo(cn) > 0 ? mi : cn);

            BigInteger mip = m;
            while (mip.compareTo(mi) < 0) {
                mip = mip.multiply(m);
            }
            //mip = mip.multiply(m);

            List<GenPolynomial<BigInteger>> ilist = new ArrayList<GenPolynomial<BigInteger>>();
            ilist.add(a);
            ilist.add(b);
            ilist.add(d);
            //GreatestCommonDivisorAbstract<BigInteger> iengine = GCDFactory.getProxy(mi);
            //ilist = iengine.coPrime(ilist);

            List<GenPolynomial<ModInteger>> mlist = new ArrayList<GenPolynomial<ModInteger>>();
            mlist.add(ap);
            mlist.add(bp);
            mlist.add(dp);
            //System.out.println("mlist = " + mlist);
            // ensure coprime
            GreatestCommonDivisorAbstract<ModInteger> mengine = GCDFactory.getProxy(pm);
            mlist = mengine.coPrime(mlist);

            long mdeg = 0;
            for ( GenPolynomial<ModInteger> f : mlist ) {
                mdeg += f.degree(0);
            }
            if ( mdeg < c.degree(0) ) { // not squarefree
                continue;
            }

            boolean ih = true;
            //ih = PolyUfdUtil.isHenselLift(c, mip, m, ilist);
            //System.out.println("ih = " + ih);

            long tq = System.currentTimeMillis();
            try {
                lift = PolyUfdUtil.liftHenselQuadratic(c, mip, mlist);
            } catch(RuntimeException e) {
                lift = null;
                fail("liftHenselQuadratic: " + c + ", mlist = " + mlist);
            }
            tq = System.currentTimeMillis() - tq;

            ih = PolyUfdUtil.isHenselLift(c, mip, m, lift);
            //System.out.println("ih = " + ih);
            assertTrue("isHenselLift ", ih);
        }
        ComputerThreads.terminate();
    }


    /**
     * Test Kronecker substitution.
     * 
     */
    public void testKroneckerSubstitution() {

        for (int i = 0; i < 10; i++) {
            a = dfac.random(kl, ll * 2, el * 5, q);
            long d = a.degree() + 1L;
            //System.out.println("\na        = " + a);
            //System.out.println("deg(a)+1 = " + d);

            b = PolyUfdUtil.<BigInteger> substituteKronecker(a, d);
            //System.out.println("b        = " + b);

            c = PolyUfdUtil.<BigInteger> backSubstituteKronecker(dfac, b, d);
            //System.out.println("c        = " + c);
            e = a.subtract(c);
            //System.out.println("e        = " + e);
            assertTrue("back(subst(a)) = a", e.isZERO());
        }
    }

}
