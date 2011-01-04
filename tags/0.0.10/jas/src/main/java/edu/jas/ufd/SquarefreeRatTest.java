/*
 * $Id: SquarefreeRatTest.java 2933 2009-12-29 13:13:34Z kredel $
 */

package edu.jas.ufd;


import java.util.SortedMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.jas.arith.BigRational;
import edu.jas.kern.ComputerThreads;
import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.PolyUtil;
import edu.jas.poly.TermOrder;


/**
 * Squarefree factorization tests with JUnit.
 * @author Heinz Kredel.
 */

public class SquarefreeRatTest extends TestCase {


    /**
     * main.
     */
    public static void main(String[] args) {
        //BasicConfigurator.configure();
        junit.textui.TestRunner.run(suite());
        ComputerThreads.terminate();
    }


    /**
     * Constructs a <CODE>SquarefreeRatTest</CODE> object.
     * @param name String.
     */
    public SquarefreeRatTest(String name) {
        super(name);
    }


    /**
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(SquarefreeRatTest.class);
        return suite;
    }


    TermOrder to = new TermOrder(TermOrder.INVLEX);


    int rl = 3;


    int kl = 3;


    int ll = 4;


    int el = 3;


    float q = 0.25f;


    String[] vars;


    String[] cvars;


    String[] c1vars;


    String[] rvars;


    BigRational fac;


    GreatestCommonDivisorAbstract<BigRational> ufd;


    SquarefreeFieldChar0<BigRational> sqf;


    GenPolynomialRing<BigRational> dfac;


    GenPolynomial<BigRational> a;


    GenPolynomial<BigRational> b;


    GenPolynomial<BigRational> c;


    GenPolynomial<BigRational> d;


    GenPolynomial<BigRational> e;


    GenPolynomialRing<BigRational> cfac;


    GenPolynomialRing<GenPolynomial<BigRational>> rfac;


    GenPolynomial<GenPolynomial<BigRational>> ar;


    GenPolynomial<GenPolynomial<BigRational>> br;


    GenPolynomial<GenPolynomial<BigRational>> cr;


    GenPolynomial<GenPolynomial<BigRational>> dr;


    GenPolynomial<GenPolynomial<BigRational>> er;


    @Override
    protected void setUp() {
        vars = ExpVector.STDVARS(rl);
        cvars = ExpVector.STDVARS(rl - 1);
        c1vars = new String[] { cvars[0] };
        rvars = new String[] { vars[rl - 1] };
        fac = new BigRational(1);
        //ufd = new GreatestCommonDivisorSubres<BigRational>();
        //ufd = GCDFactory.<BigRational> getImplementation(fac);
        ufd = GCDFactory.getProxy(fac);
        sqf = new SquarefreeFieldChar0<BigRational>(fac);

        SquarefreeAbstract<BigRational> sqff = SquarefreeFactory.getImplementation(fac);
        //System.out.println("sqf  = " + sqf);
        //System.out.println("sqff = " + sqff);
        assertEquals("sqf == sqff ", sqf.getClass(), sqff.getClass());

        a = b = c = d = e = null;
        ar = br = cr = dr = er = null;
    }


    @Override
    protected void tearDown() {
        a = b = c = d = e = null;
        ar = br = cr = dr = er = null;
        // ComputerThreads.terminate();
    }


    /**
     * Test base squarefree.
     * 
     */
    public void testBaseSquarefree() {
        //System.out.println("\nbase:");

        dfac = new GenPolynomialRing<BigRational>(fac, 1, to, rvars);

        a = dfac.random(kl, ll, el + 2, q);
        b = dfac.random(kl, ll, el + 2, q);
        c = dfac.random(kl, ll, el, q);
        //System.out.println("a  = " + a);
        //System.out.println("b  = " + b);
        //System.out.println("c  = " + c);

        if (a.isZERO() || b.isZERO() || c.isZERO()) {
            // skip for this turn
            return;
        }

        // a a b b b c
        d = a.multiply(a).multiply(b).multiply(b).multiply(b).multiply(c);
        c = a.multiply(b).multiply(c);
        //System.out.println("d  = " + d);
        //System.out.println("c  = " + c);

        c = sqf.baseSquarefreePart(c);
        d = sqf.baseSquarefreePart(d);
        //System.out.println("d  = " + d);
        //System.out.println("c  = " + c);
        assertTrue("isSquarefree(c) " + c, sqf.isSquarefree(c));
        assertTrue("isSquarefree(d) " + d, sqf.isSquarefree(d));

        e = PolyUtil.<BigRational> basePseudoRemainder(d, c);
        //System.out.println("e  = " + e);
        assertTrue("squarefree(abc) | squarefree(aabbbc) " + e, e.isZERO());
    }


    /**
     * Test base squarefree factors.
     * 
     */
    public void testBaseSquarefreeFactors() {

        dfac = new GenPolynomialRing<BigRational>(fac, 1, to, rvars);

        a = dfac.random(kl, ll, el + 3, q);
        b = dfac.random(kl, ll, el + 3, q);
        c = dfac.random(kl, ll, el + 2, q);
        //System.out.println("a  = " + a);
        //System.out.println("b  = " + b);
        //System.out.println("c  = " + c);

        if (a.isZERO() || b.isZERO() || c.isZERO()) {
            // skip for this turn
            return;
        }

        // a a b b b c
        d = a.multiply(a).multiply(b).multiply(b).multiply(b).multiply(c);
        //System.out.println("d  = " + d);

        SortedMap<GenPolynomial<BigRational>, Long> sfactors;
        sfactors = sqf.baseSquarefreeFactors(d);
        //System.out.println("sfactors = " + sfactors);

        assertTrue("isFactorization(d,sfactors) ", sqf.isFactorization(d, sfactors));
    }


    /**
     * Test recursive squarefree.
     * 
     */
    public void testRecursiveSquarefree() {
        //System.out.println("\nrecursive:");

        cfac = new GenPolynomialRing<BigRational>(fac, 2 - 1, to, c1vars);
        rfac = new GenPolynomialRing<GenPolynomial<BigRational>>(cfac, 1, to, rvars);

        ar = rfac.random(kl, ll, el, q);
        br = rfac.random(kl, ll, el, q);
        cr = rfac.random(kl, ll, el, q);
        //System.out.println("ar = " + ar);
        //System.out.println("br = " + br);
        //System.out.println("cr = " + cr);

        if (ar.isZERO() || br.isZERO() || cr.isZERO()) {
            // skip for this turn
            return;
        }

        dr = ar.multiply(ar).multiply(br).multiply(br);
        cr = ar.multiply(br);
        //System.out.println("dr  = " + dr);
        //System.out.println("cr  = " + cr);

        cr = sqf.recursiveUnivariateSquarefreePart(cr);
        dr = sqf.recursiveUnivariateSquarefreePart(dr);
        //System.out.println("dr  = " + dr);
        //System.out.println("cr  = " + cr);
        assertTrue("isSquarefree(cr) " + cr, sqf.isRecursiveSquarefree(cr));
        assertTrue("isSquarefree(dr) " + dr, sqf.isRecursiveSquarefree(dr));

        er = PolyUtil.<BigRational> recursivePseudoRemainder(dr, cr);
        //System.out.println("er  = " + er);
        assertTrue("squarefree(abc) | squarefree(aabbc) " + er, er.isZERO());
    }


    /**
     * Test recursive squarefree factors.
     * 
     */
    public void testRecursiveSquarefreeFactors() {

        cfac = new GenPolynomialRing<BigRational>(fac, 2 - 1, to, c1vars);
        rfac = new GenPolynomialRing<GenPolynomial<BigRational>>(cfac, 1, to, rvars);

        ar = rfac.random(kl, 3, 2, q);
        br = rfac.random(kl, 3, 2, q);
        cr = rfac.random(kl, 3, 2, q);
        //System.out.println("ar = " + ar);
        //System.out.println("br = " + br);
        //System.out.println("cr = " + cr);

        if (ar.isZERO() || br.isZERO() || cr.isZERO()) {
            // skip for this turn
            return;
        }

        dr = ar.multiply(cr).multiply(br).multiply(br);
        //System.out.println("dr  = " + dr);

        SortedMap<GenPolynomial<GenPolynomial<BigRational>>, Long> sfactors;
        sfactors = sqf.recursiveUnivariateSquarefreeFactors(dr);
        //System.out.println("sfactors = " + sfactors);
        assertTrue("isFactorization(d,sfactors) ", sqf.isRecursiveFactorization(dr, sfactors));
    }


    /**
     * Test squarefree.
     * 
     */
    public void testSquarefree() {
        //System.out.println("\nfull:");

        dfac = new GenPolynomialRing<BigRational>(fac, rl, to, vars);

        a = dfac.random(kl, ll, 2, q);
        b = dfac.random(kl, ll, 2, q);
        c = dfac.random(kl, ll, 2, q);
        //System.out.println("a  = " + a);
        //System.out.println("b  = " + b);
        //System.out.println("c  = " + c);

        if (a.isZERO() || b.isZERO() || c.isZERO()) {
            // skip for this turn
            return;
        }

        d = a.multiply(a).multiply(b).multiply(b).multiply(c);
        c = a.multiply(b).multiply(c);
        //System.out.println("d  = " + d);
        //System.out.println("c  = " + c);

        c = sqf.squarefreePart(c);
        d = sqf.squarefreePart(d);
        //System.out.println("c  = " + c);
        //System.out.println("d  = " + d);
        assertTrue("isSquarefree(d) " + d, sqf.isSquarefree(d));
        assertTrue("isSquarefree(c) " + c, sqf.isSquarefree(c));

        e = PolyUtil.<BigRational> basePseudoRemainder(d, c);
        //System.out.println("e  = " + e);
        assertTrue("squarefree(abc) | squarefree(aabbc) " + e, e.isZERO());
    }


    /**
     * Test squarefree factors.
     * 
     */
    public void testSquarefreeFactors() {

        dfac = new GenPolynomialRing<BigRational>(fac, rl, to, vars);

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

        d = a.multiply(a).multiply(b).multiply(b).multiply(b).multiply(c);
        //System.out.println("d  = " + d);

        SortedMap<GenPolynomial<BigRational>, Long> sfactors;
        sfactors = sqf.squarefreeFactors(d);
        //System.out.println("sfactors = " + sfactors);
        assertTrue("isFactorization(d,sfactors) ", sqf.isFactorization(d, sfactors));
    }

}
