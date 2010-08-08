/*
 * $Id: IteratorsTest.java 3248 2010-08-07 10:25:22Z kredel $
 */

package edu.jas.util;


import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.jas.arith.BigInteger;


/**
 * Iterator tests with JUnit.
 * @author Heinz Kredel.
 */

public class IteratorsTest extends TestCase {


    /**
     * main.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }


    /**
     * Constructs a <CODE>ListUtilTest</CODE> object.
     * @param name String.
     */
    public IteratorsTest(String name) {
        super(name);
    }


    /**
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(IteratorsTest.class);
        return suite;
    }


    @Override
    protected void setUp() {
    }


    @Override
    protected void tearDown() {
    }


    /**
     * Test cartesian product.
     * 
     */
    public void testCartesianProduct() {
        BigInteger ai = new BigInteger();
        int s1 = 4;
        int s2 = 3;
        int s = 1;
        for (int i = 0; i < s1; i++) {
            s *= s2;
        }
        //System.out.println("s = " + s);
        List<List<BigInteger>> tlist = new ArrayList<List<BigInteger>>(s1);
        for (int i = 0; i < s1; i++) {
            List<BigInteger> list = new ArrayList<BigInteger>(s2);
            for (int j = 0; j < s2; j++) {
                list.add(ai.random(7));
            }
            tlist.add(list);
        }
        //System.out.println("tlist = " + tlist);
        int t = 0;
        for (List<BigInteger> tuple : new CartesianProduct<BigInteger>(tlist)) {
            t++;
            //System.out.println("tuple = " + tuple);
            assertTrue("|tuple| == " + s1 + " ", s1 == tuple.size());
        }
        assertTrue("#tuple == " + s + " == " + t + " ", t == s);
    }


    /**
     * Test power set.
     * 
     */
    public void testPowerSet() {
        BigInteger ai = new BigInteger();
        int s1 = 5;
        int s = 1;
        for (int i = 0; i < s1; i++) {
            s *= 2;
        }
        //System.out.println("s = " + s);
        List<BigInteger> tlist = new ArrayList<BigInteger>(s1);
        for (int j = 0; j < s1; j++) {
            tlist.add(ai.random(7));
        }
        //System.out.println("tlist = " + tlist);
        int t = 0;
        for (List<BigInteger> tuple : new PowerSet<BigInteger>(tlist)) {
            t++;
            //System.out.println("tuple = " + tuple);
        }
        assertTrue("#tuple == " + s + " == " + t + " ", t == s);
    }


    /**
     * Test k-subset set.
     * 
     */
    public void testKsubSet() {
        BigInteger ai = new BigInteger();
        int s1 = 5;
        int s = 1;
        for (int i = 0; i < s1; i++) {
            s *= 2;
        }
        //System.out.println("s = " + s);
        List<BigInteger> tlist = new ArrayList<BigInteger>(s1);
        for (int j = 0; j < s1; j++) {
            tlist.add(ai.random(7));
        }
        //System.out.println("tlist = " + tlist);
        int t = 0;
        for (int k = 0; k <= s1; k++) {
            for (List<BigInteger> tuple : new KsubSet<BigInteger>(tlist, k)) {
                t++;
                //System.out.println("tuple = " + tuple);
                assertTrue("|tuple| == " + k + " ", k == tuple.size());
            }
        }
        assertTrue("#tuple == " + s + " == " + t + " ", t == s);
    }

}
