/*
 * $Id: GCDSubresRatTest.java 2725 2009-07-09 20:19:37Z kredel $
 */

package edu.jas.ufd;


import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.jas.arith.BigRational;
import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.PolyUtil;
import edu.jas.poly.TermOrder;


/**
 * GCD Subres with rational coefficients algorithm tests with JUnit.
 * @author Heinz Kredel.
 */

public class GCDSubresRatTest extends TestCase {


    /**
     * main.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }


    /**
     * Constructs a <CODE>GCDSubresRatTest</CODE> object.
     * @param name String.
     */
    public GCDSubresRatTest(String name) {
        super(name);
    }


    /**
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(GCDSubresRatTest.class);
        return suite;
    }


    //private final static int bitlen = 100;

    GreatestCommonDivisorAbstract<BigRational> ufd;


    TermOrder to = new TermOrder(TermOrder.INVLEX);


    GenPolynomialRing<BigRational> dfac;


    GenPolynomialRing<BigRational> cfac;


    GenPolynomialRing<GenPolynomial<BigRational>> rfac;


    BigRational mi;


    BigRational ai;


    BigRational bi;


    BigRational ci;


    BigRational di;


    BigRational ei;


    GenPolynomial<BigRational> a;


    GenPolynomial<BigRational> b;


    GenPolynomial<BigRational> c;


    GenPolynomial<BigRational> d;


    GenPolynomial<BigRational> e;


    GenPolynomial<GenPolynomial<BigRational>> ar;


    GenPolynomial<GenPolynomial<BigRational>> br;


    GenPolynomial<GenPolynomial<BigRational>> cr;


    GenPolynomial<GenPolynomial<BigRational>> dr;


    GenPolynomial<GenPolynomial<BigRational>> er;


    int rl = 3;


    int kl = 2;


    int ll = 3;


    int el = 3;


    float q = 0.25f;


    @Override
    protected void setUp() {
        a = b = c = d = e = null;
        ai = bi = ci = di = ei = null;
        ar = br = cr = dr = er = null;
        mi = new BigRational(1);
        ufd = new GreatestCommonDivisorSubres<BigRational>();
        String[] vars = ExpVector.STDVARS(rl);
        String[] cvars = ExpVector.STDVARS(rl - 1);
        String[] rvars = new String[] { vars[rl - 1] };
        dfac = new GenPolynomialRing<BigRational>(mi, rl, to, vars);
        cfac = new GenPolynomialRing<BigRational>(mi, rl - 1, to, cvars);
        rfac = new GenPolynomialRing<GenPolynomial<BigRational>>(cfac, 1, to, rvars);
        //System.out.println("mi = " + mi);
    }


    @Override
    protected void tearDown() {
        a = b = c = d = e = null;
        ai = bi = ci = di = ei = null;
        ar = br = cr = dr = er = null;
        mi = null;
        ufd = null;
        dfac = null;
        cfac = null;
        rfac = null;
    }


    /**
     * Test gcd.
     * 
     */
    public void testGcd() {

        for (int i = 0; i < 1; i++) {
            a = dfac.random(kl * (i + 2), ll + 2 * i, el + 0 * i, q);
            b = dfac.random(kl * (i + 2), ll + 2 * i, el + 0 * i, q);
            c = dfac.random(kl * (i + 2), ll + 2 * i, el + 0 * i, q);
            c = c.multiply(dfac.univariate(0));
            //a = ufd.basePrimitivePart(a);
            //b = ufd.basePrimitivePart(b);

            if (a.isZERO() || b.isZERO() || c.isZERO()) {
                // skip for this turn
                continue;
            }
            assertTrue("length( c" + i + " ) <> 0", c.length() > 0);
            //assertTrue(" not isZERO( c"+i+" )", !c.isZERO() );
            //assertTrue(" not isONE( c"+i+" )", !c.isONE() );

            a = a.multiply(c);
            b = b.multiply(c);
            //System.out.println("a  = " + a);
            //System.out.println("b  = " + b);

            d = ufd.gcd(a, b);

            c = ufd.basePrimitivePart(c).abs();
            e = PolyUtil.<BigRational> basePseudoRemainder(d, c);
            //System.out.println("c  = " + c);
            //System.out.println("d  = " + d);
            assertTrue("c | gcd(ac,bc) " + e, e.isZERO());

            e = PolyUtil.<BigRational> basePseudoRemainder(a, d);
            //System.out.println("e = " + e);
            assertTrue("gcd(a,b) | a" + e, e.isZERO());

            e = PolyUtil.<BigRational> basePseudoRemainder(b, d);
            //System.out.println("e = " + e);
            assertTrue("gcd(a,b) | b" + e, e.isZERO());
        }
    }


    /**
     * Test base quotioent and remainder.
     * 
     */
    public void testBaseQR() {

        dfac = new GenPolynomialRing<BigRational>(mi, 1, to);

        for (int i = 0; i < 3; i++) {
            a = dfac.random(kl * (i + 2), ll + 2 * i, el + 2 * i, q);
            c = dfac.random(kl * (i + 2), ll + 2 * i, el + 2 * i, q);
            //a = ufd.basePrimitivePart(a).abs();
            //c = ufd.basePrimitivePart(c);
            do {
                ci = mi.random(kl * (i + 2));
                ci = ci.sum(mi.getONE());
            } while (ci.isZERO());

            //System.out.println("a  = " + a);
            //System.out.println("c  = " + c);
            //System.out.println("ci = " + ci);

            if (a.isZERO() || c.isZERO()) {
                // skip for this turn
                continue;
            }
            assertTrue("length( c" + i + " ) <> 0", c.length() > 0);
            //assertTrue(" not isZERO( c"+i+" )", !c.isZERO() );
            //assertTrue(" not isONE( c"+i+" )", !c.isONE() );

            b = a.multiply(c);
            //System.out.println("b  = " + b);
            d = PolyUtil.<BigRational> basePseudoRemainder(b, c);
            //System.out.println("d  = " + d);

            assertTrue("rem(ac,c) == 0", d.isZERO());

            b = a.multiply(ci);
            //System.out.println("b  = " + b);
            d = b.divide(ci);
            //System.out.println("d  = " + d);

            assertEquals("a == ac/c", a, d);

            b = a.multiply(c);
            //System.out.println("b  = " + b);
            d = PolyUtil.<BigRational> basePseudoDivide(b, c);
            //System.out.println("d  = " + d);

            assertEquals("a == ac/c", a, d);
        }
    }


    /**
     * Test base content and primitive part.
     * 
     */
    public void testBaseContentPP() {

        for (int i = 0; i < 9; i++) {
            c = dfac.random(kl * (i + 2), ll + 2 * i, el + i, q);
            c = c.multiply(mi.random(kl * (i + 2)));

            if (c.isZERO()) {
                // skip for this turn
                continue;
            }
            assertTrue("length( c" + i + " ) <> 0", c.length() > 0);
            //assertTrue(" not isZERO( c"+i+" )", !c.isZERO() );
            //assertTrue(" not isONE( c"+i+" )", !c.isONE() );

            ci = ufd.baseContent(c);
            d = ufd.basePrimitivePart(c);
            //System.out.println("c  = " + c);
            //System.out.println("ci = " + ci);
            //System.out.println("d  = " + d);

            a = d.multiply(ci);
            assertEquals("c == cont(c)pp(c)", c, a);
        }
    }


    /**
     * Test base gcd.
     * 
     */
    public void testBaseGcd() {

        dfac = new GenPolynomialRing<BigRational>(mi, 1, to);

        for (int i = 0; i < 3; i++) {
            a = dfac.random(kl * (i + 2), ll + 2 * i, el + 2 * i, q);
            b = dfac.random(kl * (i + 2), ll + 2 * i, el + 2 * i, q);
            c = dfac.random(kl * (i + 2), ll + 2 * i, el + 2 * i, q);
            //a = ufd.basePrimitivePart(a);
            //b = ufd.basePrimitivePart(b);
            //c = ufd.basePrimitivePart(c).abs();

            //System.out.println("a  = " + a);
            //System.out.println("b  = " + b);
            //System.out.println("c  = " + c);

            if (a.isZERO() || b.isZERO() || c.isZERO()) {
                // skip for this turn
                continue;
            }
            assertTrue("length( c" + i + " ) <> 0", c.length() > 0);
            //assertTrue(" not isZERO( c"+i+" )", !c.isZERO() );
            //assertTrue(" not isONE( c"+i+" )", !c.isONE() );

            a = a.multiply(c);
            b = b.multiply(c);

            d = ufd.baseGcd(a, b);
            e = PolyUtil.<BigRational> basePseudoRemainder(d, c);
            //System.out.println("d  = " + d);

            assertTrue("c | gcd(ac,bc) " + e, e.isZERO());
        }
    }


    /**
     * Test recursive quotioent and remainder.
     * 
     */
    public void testRecursiveQR() {
        dfac = new GenPolynomialRing<BigRational>(mi, 2, to);
        cfac = new GenPolynomialRing<BigRational>(mi, 2 - 1, to);
        rfac = new GenPolynomialRing<GenPolynomial<BigRational>>(cfac, 1, to);

        for (int i = 0; i < 3; i++) {
            a = dfac.random(kl * (i + 1), ll + i, el + i, q);
            a = ufd.basePrimitivePart(a).abs();

            c = dfac.random(kl * (i + 1), ll + i, el + i, q);
            c = ufd.basePrimitivePart(a).abs();
            cr = PolyUtil.<BigRational> recursive(rfac, c);

            c = cfac.random(kl * (i + 1), ll + 2 * i, el + 2 * i, q);
            c = ufd.basePrimitivePart(c).abs();

            ar = PolyUtil.<BigRational> recursive(rfac, a);
            //System.out.println("ar = " + ar);
            //System.out.println("a  = " + a);
            //System.out.println("c  = " + c);
            //System.out.println("cr = " + cr);

            if (cr.isZERO() || c.isZERO()) {
                // skip for this turn
                continue;
            }
            assertTrue("length( cr" + i + " ) <> 0", cr.length() > 0);
            //assertTrue(" not isZERO( c"+i+" )", !c.isZERO() );
            //assertTrue(" not isONE( c"+i+" )", !c.isONE() );


            br = ar.multiply(cr);
            //System.out.println("br = " + br);
            dr = PolyUtil.<BigRational> recursivePseudoRemainder(br, cr);
            //System.out.println("dr = " + dr);
            d = PolyUtil.<BigRational> distribute(dfac, dr);
            //System.out.println("d  = " + d);

            assertTrue("rem(ac,c) == 0", d.isZERO());

            br = ar.multiply(c);
            //System.out.println("br = " + br);
            dr = PolyUtil.<BigRational> recursiveDivide(br, c);
            //System.out.println("dr = " + dr);
            d = PolyUtil.<BigRational> distribute(dfac, dr);
            //System.out.println("d  = " + d);

            assertEquals("a == ac/c", a, d);
        }
    }


    /**
     * Test recursive content and primitive part.
     * 
     */
    public void testRecursiveContentPP() {
        dfac = new GenPolynomialRing<BigRational>(mi, 2, to);
        cfac = new GenPolynomialRing<BigRational>(mi, 2 - 1, to);
        rfac = new GenPolynomialRing<GenPolynomial<BigRational>>(cfac, 1, to);

        for (int i = 0; i < 3; i++) {
            cr = rfac.random(kl * (i + 2), ll + 2 * i, el + i, q);
            //System.out.println("cr = " + cr);

            assertTrue("length( cr" + i + " ) <> 0", cr.length() > 0);
            //assertTrue(" not isZERO( c"+i+" )", !c.isZERO() );
            //assertTrue(" not isONE( c"+i+" )", !c.isONE() );

            c = ufd.recursiveContent(cr);
            dr = ufd.recursivePrimitivePart(cr);
            //System.out.println("c  = " + c);
            //System.out.println("dr = " + dr);

            ar = dr.multiply(c);
            assertEquals("c == cont(c)pp(c)", cr, ar);
        }
    }


    /**
     * Test recursive gcd.
     * 
     */
    public void testRecursiveGCD() {
        dfac = new GenPolynomialRing<BigRational>(mi, 2, to);
        cfac = new GenPolynomialRing<BigRational>(mi, 2 - 1, to);
        rfac = new GenPolynomialRing<GenPolynomial<BigRational>>(cfac, 1, to);

        for (int i = 0; i < 2; i++) {
            ar = rfac.random(kl, ll, el + i, q);
            br = rfac.random(kl, ll, el, q);
            cr = rfac.random(kl, ll, el, q);
            //System.out.println("ar = " + ar);
            //System.out.println("br = " + br);
            //System.out.println("cr = " + cr);

            if (ar.isZERO() || br.isZERO() || cr.isZERO()) {
                // skip for this turn
                continue;
            }
            assertTrue("length( cr" + i + " ) <> 0", cr.length() > 0);
            //assertTrue(" not isZERO( c"+i+" )", !c.isZERO() );
            //assertTrue(" not isONE( c"+i+" )", !c.isONE() );

            ar = ar.multiply(cr);
            br = br.multiply(cr);
            //System.out.println("ar = " + ar);
            //System.out.println("br = " + br);

            dr = ufd.recursiveUnivariateGcd(ar, br);
            //System.out.println("dr = " + dr);

            er = PolyUtil.<BigRational> recursivePseudoRemainder(dr, cr);
            //System.out.println("er = " + er);

            assertTrue("c | gcd(ac,bc) " + er, er.isZERO());
        }
    }


    /**
     * Test arbitrary recursive gcd.
     * 
     */
    public void testArbitraryRecursiveGCD() {
        dfac = new GenPolynomialRing<BigRational>(mi, 2, to);
        cfac = new GenPolynomialRing<BigRational>(mi, 2 - 1, to);
        rfac = new GenPolynomialRing<GenPolynomial<BigRational>>(cfac, 1, to);

        for (int i = 0; i < 2; i++) {
            ar = rfac.random(kl, ll, el + i, q);
            br = rfac.random(kl, ll, el, q);
            cr = rfac.random(kl, ll, el, q);
            //System.out.println("ar = " + ar);
            //System.out.println("br = " + br);
            //System.out.println("cr = " + cr);

            if (ar.isZERO() || br.isZERO() || cr.isZERO()) {
                // skip for this turn
                continue;
            }
            assertTrue("length( cr" + i + " ) <> 0", cr.length() > 0);
            //assertTrue(" not isZERO( c"+i+" )", !c.isZERO() );
            //assertTrue(" not isONE( c"+i+" )", !c.isONE() );

            ar = ar.multiply(cr);
            br = br.multiply(cr);
            //System.out.println("ar = " + ar);
            //System.out.println("br = " + br);

            dr = ufd.recursiveGcd(ar, br);
            //System.out.println("dr = " + dr);

            er = PolyUtil.<BigRational> recursivePseudoRemainder(dr, cr);
            //System.out.println("er = " + er);

            assertTrue("c | gcd(ac,bc) " + er, er.isZERO());
        }
    }


    /**
     * Test content and primitive part.
     * 
     */
    public void testContentPP() {
        dfac = new GenPolynomialRing<BigRational>(mi, 3, to);

        for (int i = 0; i < 3; i++) {
            c = dfac.random(kl * (i + 2), ll + 2 * i, el + i, q);
            //System.out.println("cr = " + cr);

            assertTrue("length( c" + i + " ) <> 0", c.length() > 0);
            //assertTrue(" not isZERO( c"+i+" )", !c.isZERO() );
            //assertTrue(" not isONE( c"+i+" )", !c.isONE() );

            a = ufd.content(c);
            e = a.extend(dfac, 0, 0L);
            b = ufd.primitivePart(c);
            //System.out.println("c  = " + c);
            //System.out.println("a  = " + a);
            //System.out.println("e  = " + e);
            //System.out.println("b  = " + b);

            d = e.multiply(b);
            assertEquals("c == cont(c)pp(c)", d, c);
        }
    }


    /**
     * Test gcd 3 variables.
     * 
     */
    public void testGCD3() {
        dfac = new GenPolynomialRing<BigRational>(mi, 3, to);

        for (int i = 0; i < 3; i++) {
            a = dfac.random(kl, ll, el + i, q);
            b = dfac.random(kl, ll, el, q);
            c = dfac.random(kl, ll, el, q);
            //System.out.println("a = " + a);
            //System.out.println("b = " + b);
            //System.out.println("c = " + c);

            if (a.isZERO() || b.isZERO() || c.isZERO()) {
                // skip for this turn
                continue;
            }
            assertTrue("length( c" + i + " ) <> 0", c.length() > 0);
            //assertTrue(" not isZERO( c"+i+" )", !c.isZERO() );
            //assertTrue(" not isONE( c"+i+" )", !c.isONE() );

            a = a.multiply(c);
            b = b.multiply(c);
            //System.out.println("a = " + a);
            //System.out.println("b = " + b);

            d = ufd.gcd(a, b);
            //System.out.println("d = " + d);

            e = PolyUtil.<BigRational> basePseudoRemainder(d, c);
            //System.out.println("e = " + e);

            assertTrue("c | gcd(ac,bc) " + e, e.isZERO());
        }
    }


    /**
     * Test gcd.
     * 
     */
    public void testGCD() {
        // dfac = new GenPolynomialRing<BigRational>(mi,3,to);

        for (int i = 0; i < 1; i++) {
            a = dfac.random(kl, ll, el, q);
            b = dfac.random(kl, ll, el, q);
            c = dfac.random(kl, ll, el, q);
            //System.out.println("a = " + a);
            //System.out.println("b = " + b);
            //System.out.println("c = " + c);

            if (a.isZERO() || b.isZERO() || c.isZERO()) {
                // skip for this turn
                continue;
            }
            assertTrue("length( c" + i + " ) <> 0", c.length() > 0);
            //assertTrue(" not isZERO( c"+i+" )", !c.isZERO() );
            //assertTrue(" not isONE( c"+i+" )", !c.isONE() );

            a = a.multiply(c);
            b = b.multiply(c);
            //System.out.println("a = " + a);
            //System.out.println("b = " + b);

            d = ufd.gcd(a, b);
            //System.out.println("d = " + d);

            e = PolyUtil.<BigRational> basePseudoRemainder(d, c);
            //System.out.println("e = " + e);
            assertTrue("c | gcd(ac,bc) " + e, e.isZERO());

            e = PolyUtil.<BigRational> basePseudoRemainder(a, d);
            //System.out.println("e = " + e);
            assertTrue("gcd(a,b) | a " + e, e.isZERO());

            e = PolyUtil.<BigRational> basePseudoRemainder(b, d);
            //System.out.println("e = " + e);
            assertTrue("gcd(a,b) | b " + e, e.isZERO());
        }
    }


    /**
     * Test lcm.
     * 
     */
    public void testLCM() {
        dfac = new GenPolynomialRing<BigRational>(mi, 3, to);

        for (int i = 0; i < 1; i++) {
            a = dfac.random(kl, ll, el, q);
            b = dfac.random(kl, ll, el, q);
            c = dfac.random(kl, 3, 2, q);
            //System.out.println("a = " + a);
            //System.out.println("b = " + b);
            //System.out.println("c = " + c);

            if (a.isZERO() || b.isZERO() || c.isZERO()) {
                // skip for this turn
                continue;
            }
            assertTrue("length( a" + i + " ) <> 0", a.length() > 0);
            //assertTrue(" not isZERO( c"+i+" )", !c.isZERO() );
            //assertTrue(" not isONE( c"+i+" )", !c.isONE() );

            a = a.multiply(c);
            b = b.multiply(c);

            c = ufd.gcd(a, b);
            //System.out.println("c = " + c);

            d = ufd.lcm(a, b);
            //System.out.println("d = " + d);

            e = c.multiply(d);
            //System.out.println("e = " + e);
            c = a.multiply(b);
            //System.out.println("c = " + c);

            assertEquals("ab == gcd(a,b)lcm(ab)", c, e);
        }
    }


    /**
     * Test co-prime factors.
     * 
     */
    public void testCoPrime() {

        dfac = new GenPolynomialRing<BigRational>(mi, 3, to);

        a = dfac.random(kl, 3, 2, q);
        b = dfac.random(kl, 3, 2, q);
        c = dfac.random(kl, 3, 2, q);
        //System.out.println("a  = " + a);
        //System.out.println("b  = " + b);
        //System.out.println("c  = " + c);

        if (a.isZERO() || b.isZERO() || c.isZERO()) {
            // skip for this turn
            return;
        }
        assertTrue("length( a ) <> 0", a.length() > 0);

        d = a.multiply(a).multiply(b).multiply(b).multiply(b).multiply(c);
        e = a.multiply(b).multiply(c);
        //System.out.println("d  = " + d);
        //System.out.println("c  = " + c);

        List<GenPolynomial<BigRational>> F = new ArrayList<GenPolynomial<BigRational>>(5);
        F.add(d);
        F.add(a);
        F.add(b);
        F.add(c);
        F.add(e);


        List<GenPolynomial<BigRational>> P = ufd.coPrime(F);
        //System.out.println("F = " + F);
        //System.out.println("P = " + P);

        assertTrue("is co-prime ", ufd.isCoPrime(P));
        assertTrue("is co-prime of ", ufd.isCoPrime(P, F));


        //P = ufd.coPrimeSquarefree(F);
        //System.out.println("F = " + F);
        //System.out.println("P = " + P);
        //assertTrue("is co-prime ", ufd.isCoPrime(P) );
        //assertTrue("is co-prime of ", ufd.isCoPrime(P,F) );


        P = ufd.coPrimeRec(F);
        //System.out.println("F = " + F);
        //System.out.println("P = " + P);

        assertTrue("is co-prime ", ufd.isCoPrime(P));
        assertTrue("is co-prime of ", ufd.isCoPrime(P, F));
    }

}
