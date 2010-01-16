/*
 * $Id: PrimeList.java 2958 2010-01-01 17:43:39Z kredel $
 */

package edu.jas.arith;

//import java.util.Random;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

//import edu.jas.util.StringUtil;


/**
 * List of big primes.
 * Provides an Iterator for generating prime numbers.
 * Similar to ALDES/SAC2 SACPOL.PRIME list.
 * @author Heinz Kredel
 * See Knuth vol 2,page 390, for list of known primes.
 * See ALDES/SAC2 SACPOL.PRIME
 */

public final class PrimeList implements Iterable<java.math.BigInteger> {


    /** Range of probable primes. 
      */
    public static enum Range { small, medium, large };


    /** The list of probable primes in requested range. 
      */
    protected final List<java.math.BigInteger> val
              = new ArrayList<java.math.BigInteger>(50);


    /** The last prime in the list.
      */
    protected java.math.BigInteger last;


    //private final static Random random = new Random();


    /**
     * Constructor for PrimeList.
     */
    public PrimeList() {
        this(Range.medium);
    }


    /**
     * Constructor for PrimeList.
     * @param r size range for primes.
     */
    public PrimeList(Range r) {
        // initialize with some known primes, see knuth (2,390)
        switch( r ) {
        case small: addSmall();
            break;
        default:
        case medium: addMedium();
            break;
        case large: addLarge();
            break;
        }
        last = get( size()-1 );
    }


    /**
     * Add small primes.
     */
    private void addSmall() {
        // really small
        val.add( new java.math.BigInteger(""+2) );
        val.add( new java.math.BigInteger(""+3) );
        val.add( new java.math.BigInteger(""+5) );
        val.add( new java.math.BigInteger(""+7) );
        val.add( new java.math.BigInteger(""+11) );
        val.add( new java.math.BigInteger(""+13) );
        val.add( new java.math.BigInteger(""+17) );
        val.add( new java.math.BigInteger(""+19) );
        val.add( new java.math.BigInteger(""+23) );
    }


    /**
     * Add medium sized primes.
     */
    private void addMedium() {
        // 2^28-x
        val.add( getLongPrime(28,57) );
        val.add( getLongPrime(28,89) );
        val.add( getLongPrime(28,95) );
        val.add( getLongPrime(28,119) );
        val.add( getLongPrime(28,125) );
        val.add( getLongPrime(28,143) );
        val.add( getLongPrime(28,165) );
        val.add( getLongPrime(28,183) );
        val.add( getLongPrime(28,213) );
        val.add( getLongPrime(28,273) );
        // 2^29-x
        val.add( getLongPrime(29,3) );
        val.add( getLongPrime(29,33) );
        val.add( getLongPrime(29,43) );
        val.add( getLongPrime(29,63) );
        val.add( getLongPrime(29,73) );
        val.add( getLongPrime(29,75) );
        val.add( getLongPrime(29,93) );
        val.add( getLongPrime(29,99) );
        val.add( getLongPrime(29,121) );
        val.add( getLongPrime(29,133) );
    }


    /**
     * Add large sized primes.
     */
    private void addLarge() {
        // 2^60-x
        val.add( getLongPrime(60,93) );
        val.add( getLongPrime(60,107) );
        val.add( getLongPrime(60,173) );
        val.add( getLongPrime(60,179) );
        val.add( getLongPrime(60,257) );
        val.add( getLongPrime(60,279) );
        val.add( getLongPrime(60,369) );
        val.add( getLongPrime(60,395) );
        val.add( getLongPrime(60,399) );
        val.add( getLongPrime(60,453) );
        // 2^59-x
        val.add( getLongPrime(59,55) );
        val.add( getLongPrime(59,99) );
        val.add( getLongPrime(59,225) );
        val.add( getLongPrime(59,427) );
        val.add( getLongPrime(59,517) );
        val.add( getLongPrime(59,607) );
        val.add( getLongPrime(59,649) );
        val.add( getLongPrime(59,687) );
        val.add( getLongPrime(59,861) );
        val.add( getLongPrime(59,871) );
        // 2^63-x
        val.add( getLongPrime(63,25) );
        val.add( getLongPrime(63,165) );
        val.add( getLongPrime(63,259) );
        val.add( getLongPrime(63,301) );
        val.add( getLongPrime(63,375) );
        val.add( getLongPrime(63,387) );
        val.add( getLongPrime(63,391) );
        val.add( getLongPrime(63,409) );
        val.add( getLongPrime(63,457) );
        val.add( getLongPrime(63,471) );
    }


    /**
     * Method to compute a prime as 2**n - m.
     * @param n power for 2.
     * @param m for 2**n - m.
     */
    protected static java.math.BigInteger getLongPrime(int n, int m) {
       long prime = 2; // knuth (2,390)
       for ( int i = 1; i < n; i++ ) {
           prime *= 2;
       }
       prime -= m;
       //System.out.println("p1 = " + prime);
       return new java.math.BigInteger(""+prime);
    }


    /**
     * Check if the list contains really prime numbers.
     */
    protected boolean checkPrimes() {
        boolean isPrime;
        for ( java.math.BigInteger p : val ) {
            isPrime = p.isProbablePrime(63);
            if ( !isPrime ) {
               System.out.println("not prime = " + p);
               return false;
            }
        }
        return true;
    }


    /**
     * toString.
     */
    @Override
     public String toString() {
        return val.toString();
    }


    /**
     * size of current list.
     */
    public int size() {
        return val.size();
    }


    /**
     * get prime at index i.
     */
    public java.math.BigInteger get(int i) {
        java.math.BigInteger p;
        if ( i < size() ) {
            p = val.get(i);
        } else {
            p = last.nextProbablePrime();
            val.add( p );
            last = p;
        }
        return p;
    }


    /**
     * Iterator.
     */
    public Iterator<java.math.BigInteger> iterator() {
        return new Iterator<java.math.BigInteger>() {
            int index = -1;
            public boolean hasNext() {
                return true;
            }
            public void remove() {
                throw new RuntimeException("remove not implemented");
            }
            public java.math.BigInteger next() {
                index++;
                return get(index);
            }
        };
    }

}
