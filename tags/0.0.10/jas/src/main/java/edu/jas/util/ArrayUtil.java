/*
 * $Id: ArrayUtil.java 3443 2010-12-25 16:49:30Z kredel $
 */

package edu.jas.util;


//import org.apache.log4j.Logger;


/**
 * Array utilities.
 * For example copyOf from Java 6.
 * @author Heinz Kredel
 */
public class ArrayUtil {


    //private static final Logger logger = Logger.getLogger(ArrayUtil.class);
    // private static boolean debug = logger.isDebugEnabled();


    /** 
     * Copy the specified array.
     * @param original array.
     * @param newLength new array length.
     * @return copy of original.
     */
    public static <T> T[] copyOf(T[] original, int newLength) {
        T[] copy = (T[]) new Object[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }


    /**
     * Copy the specified array.
     * @param original array.
     * @return copy of original.
     */
    public static <T> T[] copyOf(T[] original) {
        return (T[]) copyOf(original,original.length);
    }

}
