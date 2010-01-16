/*
 * $Id: FactorMoreTest.java 2949 2009-12-30 18:25:34Z kredel $
 */

package edu.jas.ufd;


import java.util.SortedMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.jas.application.Quotient;
import edu.jas.application.QuotientRing;
import edu.jas.arith.BigInteger;
import edu.jas.arith.BigRational;
import edu.jas.arith.Modular;
import edu.jas.arith.ModInteger;
import edu.jas.arith.ModIntegerRing;
import edu.jas.kern.ComputerThreads;
import edu.jas.poly.AlgebraicNumber;
import edu.jas.poly.AlgebraicNumberRing;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.TermOrder;
import edu.jas.structure.GcdRingElem;
import edu.jas.structure.RingFactory;


/**
 * Factor tests with JUnit.
 * @author Heinz Kredel.
 */

public class FactorMoreTest extends TestCase {


    /**
     * main.
     */
    public static void main(String[] args) {
        //BasicConfigurator.configure();
        junit.textui.TestRunner.run(suite());
    }


    /**
     * Constructs a <CODE>FactorTest</CODE> object.
     * @param name String.
     */
    public FactorMoreTest(String name) {
        super(name);
    }


    /**
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(FactorMoreTest.class);
        return suite;
    }


    int rl = 3;


    int kl = 5;


    int ll = 5;


    int el = 3;


    float q = 0.3f;


    @Override
    protected void setUp() {
    }


    @Override
    protected void tearDown() {
        ComputerThreads.terminate();
    }


    /**
     * Test dummy for Junit.
     * 
     */
    public void testDummy() {
    }


    /**
     * Test integral function factorization.
     * 
     */
    public void testIntegralFunctionFactorization() {

        TermOrder to = new TermOrder(TermOrder.INVLEX);
        BigRational cfac = new BigRational(1);
        String[] qvars = new String[] { "t" };
        GenPolynomialRing<BigRational> pfac = new GenPolynomialRing<BigRational>(cfac, 1, to, qvars);
        GenPolynomial<BigRational> t = pfac.univariate(0);

        FactorAbstract<BigRational> fac = new FactorRational();

        String[] vars = new String[] { "x" };
        GenPolynomialRing<GenPolynomial<BigRational>> pqfac = new GenPolynomialRing<GenPolynomial<BigRational>>(
                pfac, 1, to, vars);
        GenPolynomial<GenPolynomial<BigRational>> x = pqfac.univariate(0);
        GenPolynomial<GenPolynomial<BigRational>> x2 = pqfac.univariate(0, 2);

        for (int i = 1; i < 3; i++) {
            int facs = 0;
            GenPolynomial<GenPolynomial<BigRational>> a;
            GenPolynomial<GenPolynomial<BigRational>> b = pqfac.random(2, 3, el, q);
            //b = b.monic();
            //b = b.multiply(b);
            GenPolynomial<GenPolynomial<BigRational>> c = pqfac.random(2, 3, el, q);
            //c = c.monic();
            if (c.degree() < 1) {
                c = x2.subtract(pqfac.getONE().multiply(t));
            }
            if (b.degree() < 1) {
                b = x.sum(pqfac.getONE());
            }

            if (c.degree() > 0) {
                facs++;
            }
            if (b.degree() > 0) {
                facs++;
            }
            a = c.multiply(b);
            //System.out.println("\na = " + a);
            //System.out.println("b = " + b);
            //System.out.println("c = " + c);

            SortedMap<GenPolynomial<GenPolynomial<BigRational>>, Long> sm = fac.recursiveFactors(a);
            //System.out.println("\na   = " + a);
            //System.out.println("sm = " + sm);

            if (sm.size() >= facs) {
                assertTrue("#facs < " + facs, sm.size() >= facs);
            } else {
                long sf = 0;
                for (Long e : sm.values()) {
                    sf += e;
                }
                assertTrue("#facs < " + facs, sf >= facs);
            }

            boolean tt = fac.isRecursiveFactorization(a, sm);
            //System.out.println("t        = " + tt);
            assertTrue("prod(factor(a)) = a", tt);
        }
        ComputerThreads.terminate();
    }


    /**
     * Test integer integral function factorization.
     * 
     */
    public void testIntegerIntegralFunctionFactorization() {

        TermOrder to = new TermOrder(TermOrder.INVLEX);
        BigInteger cfac = new BigInteger(1);
        String[] qvars = new String[] { "t" };
        GenPolynomialRing<BigInteger> pfac = new GenPolynomialRing<BigInteger>(cfac, 1, to, qvars);
        GenPolynomial<BigInteger> t = pfac.univariate(0);

        FactorAbstract<BigInteger> fac = new FactorInteger<ModInteger>();

        String[] vars = new String[] { "x" };
        GenPolynomialRing<GenPolynomial<BigInteger>> pqfac = new GenPolynomialRing<GenPolynomial<BigInteger>>(
                pfac, 1, to, vars);
        GenPolynomial<GenPolynomial<BigInteger>> x = pqfac.univariate(0);
        GenPolynomial<GenPolynomial<BigInteger>> x2 = pqfac.univariate(0, 2);

        for (int i = 1; i < 3; i++) {
            int facs = 0;
            GenPolynomial<GenPolynomial<BigInteger>> a;
            GenPolynomial<GenPolynomial<BigInteger>> b = pqfac.random(2, 3, el, q);
            //b = b.monic();
            //b = b.multiply(b);
            GenPolynomial<GenPolynomial<BigInteger>> c = pqfac.random(2, 3, el, q);
            //c = c.monic();
            if (c.degree() < 1) {
                c = x2.subtract(pqfac.getONE().multiply(t));
            }
            if (b.degree() < 1) {
                b = x.sum(pqfac.getONE());
            }

            if (c.degree() > 0) {
                facs++;
            }
            if (b.degree() > 0) {
                facs++;
            }
            a = c.multiply(b);
            //System.out.println("\na = " + a);
            //System.out.println("b = " + b);
            //System.out.println("c = " + c);

            SortedMap<GenPolynomial<GenPolynomial<BigInteger>>, Long> sm = fac.recursiveFactors(a);
            //System.out.println("\na   = " + a);
            //System.out.println("sm = " + sm);

            if (sm.size() >= facs) {
                assertTrue("#facs < " + facs, sm.size() >= facs);
            } else {
                long sf = 0;
                for (Long e : sm.values()) {
                    sf += e;
                }
                assertTrue("#facs < " + facs, sf >= facs);
            }

            boolean tt = fac.isRecursiveFactorization(a, sm);
            //System.out.println("t        = " + tt);
            assertTrue("prod(factor(a)) = a", tt);
        }
        ComputerThreads.terminate();
    }


    /**
     * Test rational function factorization.
     * 
     */
    public void testRationalFunctionFactorization() {

        TermOrder to = new TermOrder(TermOrder.INVLEX);
        BigRational cfac = new BigRational(1);
        String[] qvars = new String[] { "t" };
        GenPolynomialRing<BigRational> pfac = new GenPolynomialRing<BigRational>(cfac, 1, to, qvars);
        QuotientRing<BigRational> qfac = new QuotientRing<BigRational>(pfac);
        Quotient<BigRational> t = qfac.generators().get(1);

        FactorQuotient<BigRational> fac = new FactorQuotient<BigRational>(qfac);

        String[] vars = new String[] { "x" };
        GenPolynomialRing<Quotient<BigRational>> pqfac = new GenPolynomialRing<Quotient<BigRational>>(qfac,
                1, to, vars);
        GenPolynomial<Quotient<BigRational>> x = pqfac.univariate(0);
        GenPolynomial<Quotient<BigRational>> x2 = pqfac.univariate(0, 2);

        for (int i = 1; i < 3; i++) {
            int facs = 0;
            GenPolynomial<Quotient<BigRational>> a;
            GenPolynomial<Quotient<BigRational>> b = pqfac.random(2, 3, el, q);
            //b = b.monic();
            //b = b.multiply(b);
            GenPolynomial<Quotient<BigRational>> c = pqfac.random(2, 3, el, q);
            //c = c.monic();
            if (c.degree() < 1) {
                c = x2.subtract(pqfac.getONE().multiply(t));
            }
            if (b.degree() < 1) {
                b = x.sum(pqfac.getONE());
            }

            if (c.degree() > 0) {
                facs++;
            }
            if (b.degree() > 0) {
                facs++;
            }
            a = c.multiply(b);
            //System.out.println("\na = " + a);
            //System.out.println("b = " + b);
            //System.out.println("c = " + c);

            SortedMap<GenPolynomial<Quotient<BigRational>>, Long> sm = fac.factors(a);
            //System.out.println("\na   = " + a);
            //System.out.println("sm = " + sm);

            if (sm.size() >= facs) {
                assertTrue("#facs < " + facs, sm.size() >= facs);
            } else {
                long sf = 0;
                for (Long e : sm.values()) {
                    sf += e;
                }
                assertTrue("#facs < " + facs, sf >= facs);
            }

            boolean tt = fac.isFactorization(a, sm);
            //System.out.println("t        = " + tt);
            assertTrue("prod(factor(a)) = a", tt);
        }
        ComputerThreads.terminate();
    }


    /**
     * Test modular rational function factorization.
     * 
     */
    public void testModularRationalFunctionFactorization() {

        TermOrder to = new TermOrder(TermOrder.INVLEX);
        ModIntegerRing cfac = new ModIntegerRing(19, true);
        String[] qvars = new String[] { "t" };
        GenPolynomialRing<ModInteger> pfac = new GenPolynomialRing<ModInteger>(cfac, 1, to, qvars);
        QuotientRing<ModInteger> qfac = new QuotientRing<ModInteger>(pfac);
        Quotient<ModInteger> t = qfac.generators().get(1);

        FactorQuotient<ModInteger> fac = new FactorQuotient<ModInteger>(qfac);

        String[] vars = new String[] { "x" };
        GenPolynomialRing<Quotient<ModInteger>> pqfac = new GenPolynomialRing<Quotient<ModInteger>>(qfac, 1,
                to, vars);
        GenPolynomial<Quotient<ModInteger>> x = pqfac.univariate(0);
        GenPolynomial<Quotient<ModInteger>> x2 = pqfac.univariate(0, 2);

        for (int i = 1; i < 3; i++) {
            int facs = 0;
            GenPolynomial<Quotient<ModInteger>> a;
            GenPolynomial<Quotient<ModInteger>> b = pqfac.random(2, 3, el, q);
            //b = b.monic();
            //b = b.multiply(b);
            GenPolynomial<Quotient<ModInteger>> c = pqfac.random(2, 3, el, q);
            //c = c.monic();
            if (c.degree() < 1) {
                c = x2.subtract(pqfac.getONE().multiply(t));
            }
            if (b.degree() < 1) {
                b = x.sum(pqfac.getONE());
            }

            if (c.degree() > 0) {
                facs++;
            }
            if (b.degree() > 0) {
                facs++;
            }
            a = c.multiply(b);
            //System.out.println("\na = " + a);
            //System.out.println("b = " + b);
            //System.out.println("c = " + c);

            SortedMap<GenPolynomial<Quotient<ModInteger>>, Long> sm = fac.factors(a);
            //System.out.println("\na   = " + a);
            //System.out.println("sm = " + sm);

            if (sm.size() >= facs) {
                assertTrue("#facs < " + facs, sm.size() >= facs);
            } else {
                long sf = 0;
                for (Long e : sm.values()) {
                    sf += e;
                }
                assertTrue("#facs < " + facs, sf >= facs);
            }

            boolean tt = fac.isFactorization(a, sm);
            //System.out.println("t        = " + tt);
            assertTrue("prod(factor(a)) = a", tt);
        }
        ComputerThreads.terminate();
    }

}
