/*
 * $Id: MultiplicativeSetTest.java 2812 2009-09-14 20:31:10Z kredel $
 */

package edu.jas.application;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.Logger;

import edu.jas.kern.ComputerThreads;

import edu.jas.arith.BigRational;
import edu.jas.gb.GroebnerBase;
import edu.jas.gb.GroebnerBaseSeq;

import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.PolynomialList;
import edu.jas.poly.TermOrder;



/**
 * MultiplicativeSet tests with JUnit.
 * @author Heinz Kredel.
 */
public class MultiplicativeSetTest extends TestCase {

    //private static final Logger logger = Logger.getLogger(MultiplicativeSetTest.class);

/**
 * main
 */
   public static void main (String[] args) {
          BasicConfigurator.configure();
          junit.textui.TestRunner.run( suite() );
   }

/**
 * Constructs a <CODE>MultiplicativeSetTest</CODE> object.
 * @param name String.
 */
   public MultiplicativeSetTest(String name) {
          super(name);
   }

/**
 * suite.
 */ 
 public static Test suite() {
     TestSuite suite= new TestSuite(MultiplicativeSetTest.class);
     return suite;
   }

   TermOrder to;
   GenPolynomialRing<BigRational> fac;

   List<GenPolynomial<BigRational>> L;
   PolynomialList<BigRational> F;
   List<GenPolynomial<BigRational>> G;
   List<? extends GenPolynomial<BigRational>> M;

   GenPolynomial<BigRational> a;
   GenPolynomial<BigRational> b;
   GenPolynomial<BigRational> c;
   GenPolynomial<BigRational> d;
   GenPolynomial<BigRational> e;

   int rl = 3; //4; //3; 
   int kl = 4; //10
   int ll = 5; //7
   int el = 3;
   float q = 0.2f; //0.4f

   protected void setUp() {
       BigRational coeff = new BigRational(17,1);
       to = new TermOrder( /*TermOrder.INVLEX*/ );
       fac = new GenPolynomialRing<BigRational>(coeff,rl,to);
       a = b = c = d = e = null;
   }

   protected void tearDown() {
       a = b = c = d = e = null;
       fac = null;
       ComputerThreads.terminate();
   }


/**
 * Test multiplicative set contained.
 * 
 */
 public void testContaines() {

     a = fac.random(kl, ll, el, q );
     b = fac.random(kl, ll, el, q );
     c = fac.random(kl, ll, el, q );
     d = fac.random(kl, ll, el, q );
     e = d; //fac.random(kl, ll, el, q );

     if ( a.isZERO() || b.isZERO() || c.isZERO() || d.isZERO() ) {
        return;
     }

     MultiplicativeSet<BigRational> ms = new MultiplicativeSet<BigRational>(fac);
     //System.out.println("ms = " + ms);
     //System.out.println("a  = " + a);
     //System.out.println("b  = " + b);
     //System.out.println("c  = " + c);

     assertTrue("isEmpty ", ms.isEmpty() );

     if ( !a.isConstant() ) {
         assertFalse("not contained ", ms.contains(a) );
     }

     MultiplicativeSet<BigRational> ms2 = ms.add(a);
     //System.out.println("ms2 = " + ms2);

     if ( !a.isConstant() ) {
         assertFalse("not isEmpty ", ms2.isEmpty() );
         assertTrue("contained ", ms2.contains(a) );
     }

     if ( !a.equals(b) && !b.isConstant() ) {
         assertFalse("not contained ", ms2.contains(b) );
     }

     MultiplicativeSet<BigRational> ms3 = ms2.add(b);
     //System.out.println("ms3 = " + ms3);

     if ( !b.isConstant() ) {
         assertFalse("not isEmpty ", ms3.isEmpty() );
     }
     assertTrue("contained ", ms3.contains(a) );
     assertTrue("contained ", ms3.contains(b) );

     if ( !a.equals(c) && !b.equals(c) && !c.isConstant() ) {
         assertFalse("not contained ", ms3.contains(c) );
     }

     e = a.multiply(b);
     //System.out.println("e  = " + e);
     if ( !e.isConstant() ) {
         assertTrue("contained ", ms3.contains(e) );
     }

     MultiplicativeSet<BigRational> ms4 = ms3.add(e);
     //System.out.println("ms4 = " + ms4);

     assertTrue("m3 == m4 ", ms3.equals(ms4) );

 }


/**
 * Test multiplicative set removeFactors.
 * 
 */
 public void testRemoveFactors() {

     a = fac.random(kl, ll, el, q );
     b = fac.random(kl, ll, el, q );
     c = fac.random(kl, ll, el, q );
     d = fac.random(kl, ll, el, q );
     e = d; //fac.random(kl, ll, el, q );

     if ( a.isZERO() || b.isZERO() || c.isZERO() || d.isZERO() ) {
        return;
     }

     MultiplicativeSet<BigRational> ms = new MultiplicativeSet<BigRational>(fac);
     //System.out.println("ms = " + ms);
     //System.out.println("a  = " + a);
     //System.out.println("b  = " + b);
     //System.out.println("c  = " + c);

     assertTrue("isEmpty ", ms.isEmpty() );

     e = ms.removeFactors(a);
     //System.out.println("e  = " + e);
     assertEquals("a == remove(a) ", a, e );

     MultiplicativeSet<BigRational> ms2 = ms.add(a);
     //System.out.println("ms2 = " + ms2);

     if ( !a.isConstant() ) {
         assertFalse("not isEmpty ", ms2.isEmpty() );
         assertTrue("contained ", ms2.contains(a) );

         e = ms2.removeFactors(a);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(a) ", e.isConstant() );

         if ( !b.isConstant() ) {
             e = ms2.removeFactors(b);
             //System.out.println("e  = " + e);
             assertEquals("b == remove(b) ", e, b );
         }
     }

     d = a.multiply(b);
     MultiplicativeSet<BigRational> ms3 = ms2.add(d);
     //System.out.println("ms3 = " + ms3);

     if ( !d.isConstant() ) {
         assertFalse("not isEmpty ", ms3.isEmpty() );

         e = ms3.removeFactors(a);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(a) ", e.isConstant() );

         e = ms3.removeFactors(b);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(b) ", e.isConstant() );

         e = ms3.removeFactors(d);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(a*b) ", e.isConstant() );

         if ( !c.isConstant() ) {
             e = ms3.removeFactors(c);
             //System.out.println("e  = " + e);
             assertEquals("c == remove(c) ", e, c );
         }
     }
 }


/**
 * Test co-prime multiplicative set contained.
 * 
 */
 public void testCoPrimeContaines() {

     a = fac.random(kl, ll, el, q );
     b = fac.random(kl, ll, el, q );
     c = fac.random(kl, ll, el, q );
     d = fac.random(kl, ll, el, q );
     e = d; //fac.random(kl, ll, el, q );

     if ( a.isZERO() || b.isZERO() || c.isZERO() || d.isZERO() ) {
        return;
     }

     MultiplicativeSet<BigRational> ms = new MultiplicativeSetCoPrime<BigRational>(fac);
     //System.out.println("ms = " + ms);
     //System.out.println("a  = " + a);
     //System.out.println("b  = " + b);
     //System.out.println("c  = " + c);

     assertTrue("isEmpty ", ms.isEmpty() );

     if ( !a.isConstant() ) {
         assertFalse("not contained ", ms.contains(a) );
     }

     MultiplicativeSet<BigRational> ms2 = ms.add(a);
     //System.out.println("ms2 = " + ms2);

     if ( !a.isConstant() ) {
         assertFalse("not isEmpty ", ms2.isEmpty() );
         assertTrue("contained ", ms2.contains(a) );
     }

     if ( !a.equals(b) && !b.isConstant() ) {
         assertFalse("not contained " + ms2, ms2.contains(b) );
     }

     MultiplicativeSet<BigRational> ms3 = ms2.add(b);
     //System.out.println("ms3 = " + ms3);

     if ( !b.isConstant() ) {
         assertFalse("not isEmpty ", ms3.isEmpty() );
     }
     assertTrue("contained ", ms3.contains(a) );
     assertTrue("contained ", ms3.contains(b) );

     if ( !a.equals(c) && !b.equals(c) && !c.isConstant() ) {
         assertFalse("not contained " + ms3, ms3.contains(c) );
     }

     e = a.multiply(b);
     //System.out.println("e  = " + e);
     if ( !e.isConstant() ) {
         assertTrue("contained ", ms3.contains(e) );
     }

     MultiplicativeSet<BigRational> ms4 = ms3.add(e);
     //System.out.println("ms4 = " + ms4);

     assertTrue("m3 == m4 ", ms3.equals(ms4) );
 }


/**
 * Test co-prime multiplicative set removeFactors.
 * 
 */
 public void testCoPrimeRemoveFactors() {

     a = fac.random(kl, ll, el, q );
     b = fac.random(kl, ll, el, q );
     c = fac.random(kl, ll, el, q );
     d = fac.random(kl, ll, el, q );
     e = d; //fac.random(kl, ll, el, q );

     if ( a.isZERO() || b.isZERO() || c.isZERO() || d.isZERO() ) {
        return;
     }

     MultiplicativeSet<BigRational> ms = new MultiplicativeSetCoPrime<BigRational>(fac);
     //System.out.println("ms = " + ms);
     //System.out.println("a  = " + a);
     //System.out.println("b  = " + b);
     //System.out.println("c  = " + c);

     assertTrue("isEmpty ", ms.isEmpty() );

     e = ms.removeFactors(a);
     //System.out.println("e  = " + e);
     assertEquals("a == remove(a) ", a, e );

     MultiplicativeSet<BigRational> ms2 = ms.add(a);
     //System.out.println("ms2 = " + ms2);

     if ( !a.isConstant() ) {
         assertFalse("not isEmpty ", ms2.isEmpty() );
         assertTrue("contained ", ms2.contains(a) );

         e = ms2.removeFactors(a);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(a) ", e.isConstant() );

         if ( !b.isConstant() ) {
             e = ms2.removeFactors(b);
             //System.out.println("e  = " + e);
             assertEquals("b == remove(b) ", e, b );
         }
     }

     d = a.multiply(b);
     MultiplicativeSet<BigRational> ms3 = ms2.add(d);
     //System.out.println("ms3 = " + ms3);

     if ( !d.isConstant() ) {
         assertFalse("not isEmpty ", ms3.isEmpty() );

         e = ms3.removeFactors(a);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(a) ", e.isConstant() );

         e = ms3.removeFactors(b);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(b) ", e.isConstant() );

         e = ms3.removeFactors(d);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(a*b) ", e.isConstant() );

         if ( !c.isConstant() ) {
             e = ms3.removeFactors(c);
             //System.out.println("e  = " + e);
             assertEquals("c == remove(c) ", e, c );
         }
     }
 }


/**
 * Test squarefree multiplicative set contained.
 * 
 */
 public void testSquarefreeContaines() {

     a = fac.random(kl, ll, el, q );
     b = fac.random(kl, ll, el, q );
     c = fac.random(kl, ll, el, q );
     d = fac.random(kl, ll, el, q );
     e = d; //fac.random(kl, ll, el, q );

     if ( a.isZERO() || b.isZERO() || c.isZERO() || d.isZERO() ) {
        return;
     }

     MultiplicativeSet<BigRational> ms = new MultiplicativeSetSquarefree<BigRational>(fac);
     //System.out.println("ms = " + ms);
     //System.out.println("a  = " + a);
     //System.out.println("b  = " + b);
     //System.out.println("c  = " + c);

     assertTrue("isEmpty ", ms.isEmpty() );

     if ( !a.isConstant() ) {
         assertFalse("not contained ", ms.contains(a) );
     }

     MultiplicativeSet<BigRational> ms2 = ms.add(a);
     //System.out.println("ms2 = " + ms2);

     if ( !a.isConstant() ) {
         assertFalse("not isEmpty ", ms2.isEmpty() );
         assertTrue("contained ", ms2.contains(a) );
     }

     if ( !a.equals(b) && !b.isConstant() ) {
         assertFalse("not contained " + ms2, ms2.contains(b) );
     }

     MultiplicativeSet<BigRational> ms3 = ms2.add(b);
     //System.out.println("ms3 = " + ms3);

     if ( !b.isConstant() ) {
         assertFalse("not isEmpty ", ms3.isEmpty() );
     }
     assertTrue("contained ", ms3.contains(a) );
     assertTrue("contained ", ms3.contains(b) );

     if ( !a.equals(c) && !b.equals(c) && !c.isConstant() ) {
         assertFalse("not contained " + ms3, ms3.contains(c) );
     }

     e = a.multiply(b);
     //System.out.println("e  = " + e);
     if ( !e.isConstant() ) {
         assertTrue("contained ", ms3.contains(e) );
     }

     MultiplicativeSet<BigRational> ms4 = ms3.add(e);
     //System.out.println("ms4 = " + ms4);

     assertTrue("m3 == m4 ", ms3.equals(ms4) );
 }


/**
 * Test squarefree multiplicative set removeFactors.
 * 
 */
 public void testSquarefreeRemoveFactors() {

     a = fac.random(kl, ll, el, q );
     b = fac.random(kl, ll, el, q );
     c = fac.random(kl, ll, el, q );
     d = fac.random(kl, ll, el, q );
     e = d; //fac.random(kl, ll, el, q );

     if ( a.isZERO() || b.isZERO() || c.isZERO() || d.isZERO() ) {
        return;
     }

     MultiplicativeSet<BigRational> ms = new MultiplicativeSetSquarefree<BigRational>(fac);
     //System.out.println("ms = " + ms);
     //System.out.println("a  = " + a);
     //System.out.println("b  = " + b);
     //System.out.println("c  = " + c);

     assertTrue("isEmpty ", ms.isEmpty() );

     e = ms.removeFactors(a);
     //System.out.println("e  = " + e);
     assertEquals("a == remove(a) ", a, e );

     MultiplicativeSet<BigRational> ms2 = ms.add(a);
     //System.out.println("ms2 = " + ms2);

     if ( !a.isConstant() ) {
         assertFalse("not isEmpty ", ms2.isEmpty() );
         assertTrue("contained ", ms2.contains(a) );

         e = ms2.removeFactors(a);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(a) ", e.isConstant() );

         if ( !b.isConstant() ) {
             e = ms2.removeFactors(b);
             //System.out.println("e  = " + e);
             assertEquals("b == remove(b) ", e, b );
         }
     }

     d = a.multiply(b);
     MultiplicativeSet<BigRational> ms3 = ms2.add(d);
     //System.out.println("ms3 = " + ms3);

     if ( !d.isConstant() ) {
         assertFalse("not isEmpty ", ms3.isEmpty() );

         e = ms3.removeFactors(a);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(a) ", e.isConstant() );

         e = ms3.removeFactors(b);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(b) ", e.isConstant() );

         e = ms3.removeFactors(d);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(a*b) ", e.isConstant() );

         if ( !c.isConstant() ) {
             e = ms3.removeFactors(c);
             //System.out.println("e  = " + e);
             assertEquals("c == remove(c) ", e, c );
         }
     }
 }


/**
 * Test irreducible multiplicative set contained.
 * 
 */
 public void testFactorsContaines() {

     a = fac.random(kl-2, ll-2, el, q );
     b = fac.random(kl-2, ll-2, el, q );
     c = fac.random(kl-2, ll-2, el, q ).monic();
     d = fac.random(kl, ll, el, q );
     e = d; //fac.random(kl, ll, el, q );

     if ( a.isZERO() || b.isZERO() || c.isZERO() || d.isZERO() ) {
        return;
     }

     MultiplicativeSet<BigRational> ms = new MultiplicativeSetFactors<BigRational>(fac);
     //System.out.println("ms = " + ms);
     //System.out.println("a  = " + a);
     //System.out.println("b  = " + b);
     //System.out.println("c  = " + c);

     assertTrue("isEmpty ", ms.isEmpty() );

     if ( !a.isConstant() ) {
         assertFalse("not contained ", ms.contains(a) );
     }

     MultiplicativeSet<BigRational> ms2 = ms.add(a);
     //System.out.println("ms2 = " + ms2);

     if ( !a.isConstant() ) {
         assertFalse("not isEmpty ", ms2.isEmpty() );
         assertTrue("contained ", ms2.contains(a) );

         if ( !a.equals(b) && !b.isConstant() ) {
             assertFalse("not contained " + ms2, ms2.contains(b) );
         }
     }

     MultiplicativeSet<BigRational> ms3 = ms2.add(b);
     //System.out.println("ms3 = " + ms3);

     assertTrue("contained ", ms3.contains(a) );
     assertTrue("contained ", ms3.contains(b) );

     if ( !b.isConstant() ) {
         assertFalse("not isEmpty ", ms3.isEmpty() );

         if ( !a.monic().equals(c) && !b.monic().equals(c) && !c.isConstant() ) {
             assertFalse("not contained " + ms3, ms3.contains(c) );
         }
     }

     e = a.multiply(b);
     //System.out.println("e  = " + e);
     if ( !e.isConstant() ) {
         assertTrue("contained ", ms3.contains(e) );
     }

     MultiplicativeSet<BigRational> ms4 = ms3.add(e);
     //System.out.println("ms4 = " + ms4);

     assertTrue("m3 == m4 ", ms3.equals(ms4) );
 }


/**
 * Test irreducible multiplicative set removeFactors.
 * 
 */
 public void testFactorsRemoveFactors() {

     a = fac.random(kl-2, ll-2, el, q );
     b = fac.random(kl-2, ll-2, el, q );
     c = fac.random(kl-2, ll-2, el, q );
     d = fac.random(kl-2, ll-2, el, q );
     e = d; //fac.random(kl, ll, el, q );

     if ( a.isZERO() || b.isZERO() || c.isZERO() || d.isZERO() ) {
        return;
     }

     MultiplicativeSet<BigRational> ms = new MultiplicativeSetFactors<BigRational>(fac);
     //System.out.println("ms = " + ms);
     //System.out.println("a  = " + a);
     //System.out.println("b  = " + b);
     //System.out.println("c  = " + c);

     assertTrue("isEmpty ", ms.isEmpty() );

     e = ms.removeFactors(a);
     //System.out.println("e  = " + e);
     assertEquals("a == remove(a) ", a, e );

     MultiplicativeSet<BigRational> ms2 = ms.add(a);
     //System.out.println("ms2 = " + ms2);

     if ( !a.isConstant() ) {
         assertFalse("not isEmpty ", ms2.isEmpty() );
         assertTrue("contained ", ms2.contains(a) );

         e = ms2.removeFactors(a);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(a) ", e.isConstant() );

         if ( !b.isConstant() ) {
             e = ms2.removeFactors(b);
             //System.out.println("e  = " + e);
             assertTrue("remove(b) | b ", b.remainder(e).isZERO() );
         }
     }

     d = a.multiply(b);
     MultiplicativeSet<BigRational> ms3 = ms2.add(d);
     //System.out.println("ms3 = " + ms3);

     if ( !d.isConstant() ) {
         assertFalse("not isEmpty ", ms3.isEmpty() );

         e = ms3.removeFactors(a);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(a) ", e.isConstant() );

         e = ms3.removeFactors(b);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(b) ", e.isConstant() );

         e = ms3.removeFactors(d);
         //System.out.println("e  = " + e);
         assertTrue("1 == remove(a*b) ", e.isConstant() );

         if ( !c.isConstant() ) {
             e = ms3.removeFactors(c);
             //System.out.println("e  = " + e);
             assertTrue("remove(c) | c ", c.remainder(e).isZERO() );
         }
     }
 }

}
