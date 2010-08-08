/*
 * $Id: RealAlgebraicTest.java 2726 2009-07-09 20:23:53Z kredel $
 */

package edu.jas.root;


import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;

import edu.jas.arith.BigDecimal;
import edu.jas.arith.BigRational;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.structure.NotInvertibleException;
import edu.jas.structure.Power;


/**
 * RealAlgebraicNumber Test using JUnit.
 * @author Heinz Kredel.
 */

public class RealAlgebraicTest extends TestCase {


    /**
     * main.
     */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        junit.textui.TestRunner.run(suite());
    }


    /**
     * Constructs a <CODE>RealAlgebraicTest</CODE> object.
     * @param name String.
     */
    public RealAlgebraicTest(String name) {
        super(name);
    }


    /**
     * suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(RealAlgebraicTest.class);
        return suite;
    }


    //private final static int bitlen = 100;

    RealAlgebraicRing<BigRational> fac;


    GenPolynomialRing<BigRational> mfac;


    RealAlgebraicNumber<BigRational> a;


    RealAlgebraicNumber<BigRational> b;


    RealAlgebraicNumber<BigRational> c;


    RealAlgebraicNumber<BigRational> d;


    RealAlgebraicNumber<BigRational> e;


    RealAlgebraicNumber<BigRational> alpha;


    int rl = 1;


    int kl = 10;


    int ll = 10;


    int el = ll;


    float q = 0.5f;


    @Override
    protected void setUp() {
        a = b = c = d = e = null;
        BigRational l = new BigRational(1);
        BigRational r = new BigRational(2);
        Interval<BigRational> positiv = new Interval<BigRational>(l, r);
        String[] vars = new String[] { "alpha" };
        mfac = new GenPolynomialRing<BigRational>(new BigRational(1), rl, vars);
        GenPolynomial<BigRational> mo = mfac.univariate(0, 2);
        mo = mo.subtract(mfac.fromInteger(2)); // alpha^2 -2 
        fac = new RealAlgebraicRing<BigRational>(mo, positiv);
        alpha = fac.getGenerator();
    }


    @Override
    protected void tearDown() {
        a = b = c = d = e = null;
        fac = null;
        alpha = null;
    }


    /**
     * Test constructor and toString.
     * 
     */
    public void testConstruction() {
        c = fac.getONE();
        //System.out.println("c = " + c);
        //System.out.println("c.getVal() = " + c.getVal());
        assertTrue("length( c ) = 1", c.number.getVal().length() == 1);
        assertTrue("isZERO( c )", !c.isZERO());
        assertTrue("isONE( c )", c.isONE());

        d = fac.getZERO();
        //System.out.println("d = " + d);
        //System.out.println("d.getVal() = " + d.getVal());
        assertTrue("length( d ) = 0", d.number.getVal().length() == 0);
        assertTrue("isZERO( d )", d.isZERO());
        assertTrue("isONE( d )", !d.isONE());
    }


    /**
     * Test random polynomial.
     * 
     */
    public void testRandom() {
        for (int i = 0; i < 7; i++) {
            a = fac.random(el);
            //System.out.println("a = " + a);
            if (a.isZERO() || a.isONE()) {
                continue;
            }
            // fac.random(rl+i, kl*(i+1), ll+2*i, el+i, q );
            assertTrue("length( a" + i + " ) <> 0", a.number.getVal().length() >= 0);
            assertTrue(" not isZERO( a" + i + " )", !a.isZERO());
            assertTrue(" not isONE( a" + i + " )", !a.isONE());
        }
    }


    /**
     * Test addition.
     * 
     */
    public void testAddition() {
        a = fac.random(ll);
        b = fac.random(ll);

        c = a.sum(b);
        d = c.subtract(b);
        assertEquals("a+b-b = a", a, d);

        c = a.sum(b);
        d = b.sum(a);
        assertEquals("a+b = b+a", c, d);

        c = fac.random(ll);
        d = c.sum(a.sum(b));
        e = c.sum(a).sum(b);
        assertEquals("c+(a+b) = (c+a)+b", d, e);


        c = a.sum(fac.getZERO());
        d = a.subtract(fac.getZERO());
        assertEquals("a+0 = a-0", c, d);

        c = fac.getZERO().sum(a);
        d = fac.getZERO().subtract(a.negate());
        assertEquals("0+a = 0+(-a)", c, d);
    }


    /**
     * Test object multiplication.
     * 
     */
    public void testMultiplication() {
        a = fac.random(ll);
        assertTrue("not isZERO( a )", !a.isZERO());

        b = fac.random(ll);
        assertTrue("not isZERO( b )", !b.isZERO());

        c = b.multiply(a);
        d = a.multiply(b);
        assertTrue("not isZERO( c )", !c.isZERO());
        assertTrue("not isZERO( d )", !d.isZERO());

        //System.out.println("a = " + a);
        //System.out.println("b = " + b);
        e = d.subtract(c);
        assertTrue("isZERO( a*b-b*a ) " + e, e.isZERO());

        assertTrue("a*b = b*a", c.equals(d));
        assertEquals("a*b = b*a", c, d);

        c = fac.random(ll);
        //System.out.println("c = " + c);
        d = a.multiply(b.multiply(c));
        e = (a.multiply(b)).multiply(c);

        //System.out.println("d = " + d);
        //System.out.println("e = " + e);

        //System.out.println("d-e = " + d.subtract(c) );

        assertEquals("a(bc) = (ab)c", d, e);
        assertTrue("a(bc) = (ab)c", d.equals(e));

        c = a.multiply(fac.getONE());
        d = fac.getONE().multiply(a);
        assertEquals("a*1 = 1*a", c, d);


        c = a.inverse();
        d = c.multiply(a);
        //System.out.println("a = " + a);
        //System.out.println("c = " + c);
        //System.out.println("d = " + d);
        assertEquals("a*1/a = 1", fac.getONE(), d);

        try {
            a = fac.getZERO().inverse();
        } catch (NotInvertibleException expected) {
            return;
        }
        fail("0 invertible");
    }


    /**
     * Test distributive law.
     * 
     */
    public void testDistributive() {
        a = fac.random(ll);
        b = fac.random(ll);
        c = fac.random(ll);

        d = a.multiply(b.sum(c));
        e = a.multiply(b).sum(a.multiply(c));

        assertEquals("a(b+c) = ab+ac", d, e);
    }


    /**
     * Test sign of real algebraic numbers.
     * 
     */
    public void testSignum() {
        a = fac.random(ll);
        b = fac.random(ll);
        c = fac.random(ll);

        int sa = a.signum();
        int sb = b.signum();
        int sc = c.signum();

        d = a.multiply(b);
        e = a.multiply(c);

        int sd = d.signum();
        int se = e.signum();

        assertEquals("sign(a*b) = sign(a)*sign(b) ", sa * sb, sd);
        assertEquals("sign(a*c) = sign(a)*sign(c) ", sa * sc, se);

        b = a.negate();
        sb = b.signum();
        assertEquals("sign(-a) = -sign(a) ", -sa, sb);
    }


    /**
     * Test compareTo of real algebraic numbers.
     * 
     */
    public void testCompare() {
        a = fac.random(ll).abs();
        b = a.sum(fac.getONE());
        c = b.sum(fac.getONE());

        int ab = a.compareTo(b);
        int bc = b.compareTo(c);
        int ac = a.compareTo(c);

        assertTrue("a < a+1 ", ab < 0);
        assertTrue("a+1 < a+2 ", bc < 0);
        assertTrue("a < a+2 ", ac < 0);

        a = a.negate();
        b = a.sum(fac.getONE());
        c = b.sum(fac.getONE());

        ab = a.compareTo(b);
        bc = b.compareTo(c);
        ac = a.compareTo(c);

        assertTrue("a < a+1 ", ab < 0);
        assertTrue("a+1 < a+2 ", bc < 0);
        assertTrue("a < a+2 ", ac < 0);
    }


    /**
     * Test arithmetic of magnitude of real algebraic numbers.
     * 
     */
    public void testMagnitude() {
        a = fac.random(ll);
        b = fac.random(ll);
        c = fac.random(ll);

        BigDecimal ad = new BigDecimal(a.magnitude());
        BigDecimal bd = new BigDecimal(b.magnitude());
        BigDecimal cd = new BigDecimal(c.magnitude());

        d = a.multiply(b);
        e = a.sum(b);

        BigDecimal dd = new BigDecimal(d.magnitude());
        BigDecimal ed = new BigDecimal(e.magnitude());

        BigDecimal dd1 = new BigDecimal(a.magnitude().multiply(b.magnitude()));
        BigDecimal ed1 = new BigDecimal(a.magnitude().sum(b.magnitude()));

        //System.out.println("ad  = " + ad);
        //System.out.println("bd  = " + bd);
        //System.out.println("cd  = " + cd);
        //System.out.println("dd  = " + dd);
        //System.out.println("dd1 = " + dd1);
        //System.out.println("ed  = " + ed);
        //System.out.println("ed1 = " + ed1);

        //BigRational eps = Power.positivePower(new BigRational(1L,10L),BigDecimal.DEFAULT_PRECISION);
        BigRational eps = Power.positivePower(new BigRational(1L, 10L), 8);
        BigDecimal epsd = new BigDecimal(eps);

        assertTrue("mag(a*b) = mag(a)*mag(b)", dd.subtract(dd1).abs().compareTo(epsd) <= 0);
        assertTrue("mag(a+b) = mag(a)+mag(b)", ed.subtract(ed1).abs().compareTo(epsd) <= 0);


        d = a.divide(b);
        e = a.subtract(b);

        dd = new BigDecimal(d.magnitude());
        ed = new BigDecimal(e.magnitude());

        dd1 = new BigDecimal(a.magnitude().divide(b.magnitude()));
        ed1 = new BigDecimal(a.magnitude().subtract(b.magnitude()));

        //System.out.println("dd  = " + dd);
        //System.out.println("dd1 = " + dd1);
        //System.out.println("ed  = " + ed);
        //System.out.println("ed1 = " + ed1);

        assertTrue("mag(a/b) = mag(a)/mag(b)", dd.subtract(dd1).abs().compareTo(epsd) <= 0);
        assertTrue("mag(a-b) = mag(a)-mag(b)", ed.subtract(ed1).abs().compareTo(epsd) <= 0);
    }


    /**
     * Test real root isolation. Tests nothing.
     */
    public void notestRealRootIsolation() {
        System.out.println();
        GenPolynomialRing<RealAlgebraicNumber<BigRational>> dfac;
        dfac = new GenPolynomialRing<RealAlgebraicNumber<BigRational>>(fac, 1);

        GenPolynomial<RealAlgebraicNumber<BigRational>> ar;
        RealAlgebraicNumber<BigRational> epsr;

        ar = dfac.random(3, 5, 7, q);
        System.out.println("ar = " + ar);

        RealRoots<RealAlgebraicNumber<BigRational>> rrr = new RealRootsSturm<RealAlgebraicNumber<BigRational>>();

        List<Interval<RealAlgebraicNumber<BigRational>>> R = rrr.realRoots(ar);
        System.out.println("R = " + R);

        assertTrue("#roots >= 0 ", R.size() >= 0);

        BigRational eps = Power.positivePower(new BigRational(1L, 10L), BigDecimal.DEFAULT_PRECISION);
        //BigRational eps = Power.positivePower(new BigRational(1L,10L),10);

        epsr = dfac.coFac.getONE().multiply(eps);
        //System.out.println("epsr = " + epsr);

        R = rrr.refineIntervals(R, ar, epsr);
        //System.out.println("R = " + R);
        for (Interval<RealAlgebraicNumber<BigRational>> v : R) {
            BigDecimal dd = v.toDecimal(); //.sum(eps1);
            System.out.println("v = " + dd);
            // assertTrue("|dd - di| < eps ", dd.compareTo(di) == 0);
        }
    }


    /**
     * Test real root isolation for Wilkinson like polynomials.
     * product_{i=1..n}( x - i * alpha )
     * 
     */
    public void testRealRootIsolationWilkinson() {
        //System.out.println();
        GenPolynomialRing<RealAlgebraicNumber<BigRational>> dfac;
        dfac = new GenPolynomialRing<RealAlgebraicNumber<BigRational>>(fac, 1);

        GenPolynomial<RealAlgebraicNumber<BigRational>> ar, br, cr, dr, er;

        RealRoots<RealAlgebraicNumber<BigRational>> rrr = new RealRootsSturm<RealAlgebraicNumber<BigRational>>();

        final int N = 3;
        dr = dfac.getONE();
        er = dfac.univariate(0);

        List<Interval<RealAlgebraicNumber<BigRational>>> Rn = new ArrayList<Interval<RealAlgebraicNumber<BigRational>>>(
                N);
        ar = dr;
        for (int i = 0; i < N; i++) {
            cr = dfac.fromInteger(i).multiply(alpha); // i * alpha
            Rn.add(new Interval<RealAlgebraicNumber<BigRational>>(cr.leadingBaseCoefficient()));
            br = er.subtract(cr); // ( x - i * alpha )
            ar = ar.multiply(br);
        }
        //System.out.println("ar = " + ar);

        List<Interval<RealAlgebraicNumber<BigRational>>> R = rrr.realRoots(ar);
        //System.out.println("R = " + R);

        assertTrue("#roots = " + N + " ", R.size() == N);

        BigRational eps = Power.positivePower(new BigRational(1L, 10L), BigDecimal.DEFAULT_PRECISION);
        //BigRational eps = Power.positivePower(new BigRational(1L,10L),10);
        //System.out.println("eps = " + eps);
        BigDecimal eps1 = new BigDecimal(eps);
        //System.out.println("eps1 = " + eps1);
        RealAlgebraicNumber<BigRational> epsr = dfac.coFac.getONE().multiply(eps);
        //System.out.println("epsr = " + epsr);

        R = rrr.refineIntervals(R, ar, epsr);
        //System.out.println("R = " + R);
        int i = 0;
        for (Interval<RealAlgebraicNumber<BigRational>> v : R) {
            BigDecimal dd = v.toDecimal();//.sum(eps1);
            BigDecimal di = Rn.get(i++).toDecimal();
            //System.out.println("v  = " + dd);
            //System.out.println("vi = " + di);
            assertTrue("|dd - di| < eps ", dd.compareTo(di) == 0);
        }
    }

}
