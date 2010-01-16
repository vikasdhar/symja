/*
 * $Id: SquarefreeFactory.java 2942 2009-12-30 12:11:03Z kredel $
 */

package edu.jas.ufd;


import org.apache.log4j.Logger;

import edu.jas.application.Quotient;
import edu.jas.application.QuotientRing;
import edu.jas.arith.BigInteger;
import edu.jas.arith.BigRational;
import edu.jas.arith.ModInteger;
import edu.jas.arith.ModIntegerRing;
import edu.jas.arith.ModLong;
import edu.jas.arith.ModLongRing;
import edu.jas.poly.AlgebraicNumber;
import edu.jas.poly.AlgebraicNumberRing;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.structure.GcdRingElem;
import edu.jas.structure.RingFactory;
import edu.jas.structure.ComplexRing;


/**
 * Squarefree factorization algorithms factory. Select appropriate squarefree
 * factorization engine based on the coefficient types.
 * @author Heinz Kredel
 * @usage To create objects that implement the <code>Squarefree</code>
 *        interface use the <code>SquarefreeFactory</code>. It will select an
 *        appropriate implementation based on the types of polynomial
 *        coefficients C. To obtain an implementation use
 *        <code>getImplementation()</code>, it returns an object of a class
 *        which extends the <code>SquarefreeAbstract</code> class which
 *        implements the <code>Squarefree</code> interface.
 * 
 * <pre>
 * Squarefree&lt;CT&gt; engine;
 * engine = SquarefreeFactory.&lt;CT&gt; getImplementation(cofac);
 * c = engine.squarefreeFactors(a);
 * </pre>
 * 
 * For example, if the coefficient type is BigInteger, the usage looks like
 * 
 * <pre>
 * BigInteger cofac = new BigInteger();
 * Squarefree&lt;BigInteger&gt; engine;
 * engine = SquarefreeFactory.getImplementation(cofac);
 * Sm = engine.sqaurefreeFactors(poly);
 * </pre>
 * 
 * @see edu.jas.ufd.Squarefree#squarefreeFactors(edu.jas.poly.GenPolynomial P)
 * @see edu.jas.ufd.SquarefreeAbstract#squarefreeFactors(edu.jas.poly.GenPolynomial
 *      P)
 */

public class SquarefreeFactory {


    private static final Logger logger = Logger.getLogger(SquarefreeFactory.class);


    /**
     * Protected factory constructor.
     */
    protected SquarefreeFactory() {
    }


    /**
     * Determine suitable implementation of factorization algorithm, case
     * ModInteger.
     * @param fac ModIntegerRing.
     * @return squarefree factorization algorithm implementation.
     */
    public static SquarefreeAbstract<ModInteger> getImplementation(ModIntegerRing fac) {
        return new SquarefreeFiniteFieldCharP<ModInteger>(fac);
    }


    /**
     * Determine suitable implementation of factorization algorithm, case
     * ModLong.
     * @param fac ModLongRing.
     * @return squarefree factorization algorithm implementation.
     */
    public static SquarefreeAbstract<ModLong> getImplementation(ModLongRing fac) {
        return new SquarefreeFiniteFieldCharP<ModLong>(fac);
    }


    /**
     * Determine suitable implementation of squarefree factorization algorithm,
     * case BigInteger.
     * @param fac BigInteger.
     * @return squarefree factorization algorithm implementation.
     */
    public static SquarefreeAbstract<BigInteger> getImplementation(BigInteger fac) {
        return new SquarefreeRingChar0<BigInteger>(fac);
    }


    /**
     * Determine suitable implementation of squarefree factorization algorithms,
     * case BigRational.
     * @param fac BigRational.
     * @return squarefree factorization algorithm implementation.
     */
    public static SquarefreeAbstract<BigRational> getImplementation(BigRational fac) {
        return new SquarefreeFieldChar0<BigRational>(fac);
    }


    /**
     * Determine suitable implementation of squarefree factorization algorithms,
     * case AlgebraicNumber&lt;C&gt;.
     * @param fac AlgebraicNumberRing&lt;C&gt;.
     * @param <C> coefficient type, e.g. BigRational, ModInteger.
     * @return squarefree factorization algorithm implementation.
     */
    public static <C extends GcdRingElem<C>> SquarefreeAbstract<AlgebraicNumber<C>> getImplementation(
            AlgebraicNumberRing<C> fac) {
        if (fac.characteristic().signum() == 0) {
            return new SquarefreeFieldChar0<AlgebraicNumber<C>>(fac);
        } else {
            return new SquarefreeFiniteFieldCharP<AlgebraicNumber<C>>(fac);
        }
    }


    /**
     * Determine suitable implementation of squarefree factorization algorithms,
     * case Quotient&lt;C&gt;.
     * @param fac QuotientRing&lt;C&gt;.
     * @param <C> coefficient type, e.g. BigRational, ModInteger.
     * @return squarefree factorization algorithm implementation.
     */
    public static <C extends GcdRingElem<C>> SquarefreeAbstract<Quotient<C>> getImplementation(
            QuotientRing<C> fac) {
        if (fac.characteristic().signum() == 0) {
            return new SquarefreeFieldChar0<Quotient<C>>(fac);
        } else {
            return new SquarefreeInfiniteFieldCharP<C>(fac);
        }
    }


    /**
     * Determine suitable implementation of squarefree factorization algorithms,
     * case GenPolynomial&lt;C&gt;.
     * @param fac GenPolynomialRing&lt;C&gt;.
     * @param <C> coefficient type, e.g. BigRational, ModInteger.
     * @return squarefree factorization algorithm implementation.
     */
    public static <C extends GcdRingElem<C>> SquarefreeAbstract<C> getImplementation(GenPolynomialRing<C> fac) {
        if (fac.characteristic().signum() == 0) {
            if (fac.coFac.isField()) {
                return new SquarefreeFieldChar0<C>(fac.coFac);
            } else {
                return new SquarefreeRingChar0<C>(fac.coFac);
            }
        } else {
            logger.warn("not checked if finite or infinite field" + fac.coFac);
            return new SquarefreeFiniteFieldCharP<C>(fac.coFac);
        }
    }


    /**
     * Determine suitable implementation of squarefree factorization algorithms,
     * other cases.
     * @param <C> coefficient type
     * @param fac RingFactory&lt;C&gt;.
     * @return squarefree factorization algorithm implementation.
     */
    @SuppressWarnings("unchecked")
    public static <C extends GcdRingElem<C>> SquarefreeAbstract<C> getImplementation(RingFactory<C> fac) {
        //logger.info("fac = " + fac.getClass().getName());
        //System.out.println("fac_o = " + fac.getClass().getName());
        int t = 0;
        SquarefreeAbstract/*raw type<C>*/ufd = null;
        AlgebraicNumberRing afac = null;
        QuotientRing qfac = null;
        GenPolynomialRing pfac = null;
        while (true) { // switch
            Object ofac = fac;
            if (ofac instanceof BigInteger) {
                t = 1;
                break;
            }
            if (ofac instanceof BigRational) {
                t = 2;
                break;
            }
            if (ofac instanceof ModIntegerRing) {
                t = 3;
                break;
            }
            if (ofac instanceof ModLongRing) {
                t = 10;
                break;
            }
            if (ofac instanceof AlgebraicNumberRing) {
                //System.out.println("afac_o = " + ofac);
                afac = (AlgebraicNumberRing) ofac;
                ofac = afac.ring.coFac;
                //System.out.println("o_afac = " + ofac);
                if (ofac instanceof BigRational) {
                    t = 4;
                }
                if (ofac instanceof ModIntegerRing) {
                    t = 5;
                }
                if (ofac instanceof AlgebraicNumberRing) {
                    t = 6;
                }
                if (ofac instanceof ModLongRing) {
                    t = 11;
                }
                if (ofac instanceof ComplexRing) {
                    t = 12;
                }
                break;
            }
            if (ofac instanceof QuotientRing) {
                //System.out.println("qfac_o = " + ofac);
                qfac = (QuotientRing) ofac;
                t = 7;
                break;
            }
            if (ofac instanceof GenPolynomialRing) {
                //System.out.println("qfac_o = " + ofac);
                pfac = (GenPolynomialRing) ofac;
                t = 8;
                break;
            }
            if (fac.isField()) {
                //System.out.println("fac_field = " + fac);
                t = 9;
                break;
            }
            break;
        }
        //System.out.println("ft = " + t);
        if (t == 1) { // BigInteger
            ufd = new SquarefreeRingChar0/*raw*/(fac);
        }
        if (t == 2) { // BigRational
            ufd = new SquarefreeFieldChar0/*raw*/(fac);
        }
        if (t == 3) { // ModInteger
            ufd = new SquarefreeFiniteFieldCharP/*raw*/(fac);
        }
        if (t == 10) { // ModLong
            ufd = new SquarefreeFiniteFieldCharP/*raw*/(fac);
        }
        if (t == 4 || t == 5 || t == 6 || t == 11 || t == 12) { // AlgebraicNumber
            if (afac.characteristic().signum() == 0) {
                ufd = new SquarefreeFieldChar0/*raw <C>*/(afac);
            } else {
                ufd = new SquarefreeFiniteFieldCharP/*raw <C>*/(afac);
            }
        }
        if (t == 7) { // Quotient
            if (qfac.characteristic().signum() == 0) {
                ufd = new SquarefreeFieldChar0/*raw <C>*/(qfac);
            } else {
                ufd = new SquarefreeInfiniteFieldCharP/*raw <C>*/(qfac);
            }
        }
        if (t == 8) { // GenPolynomial
            if (pfac.characteristic().signum() == 0) {
                if (pfac.coFac.isField()) {
                    ufd = new SquarefreeFieldChar0/*raw <C>*/(pfac.coFac);
                } else {
                    ufd = new SquarefreeRingChar0/*raw <C>*/(pfac.coFac);
                }
            } else {
                ufd = new SquarefreeFiniteFieldCharP/*raw <C>*/(pfac.coFac);
                logger.warn("not checked if finite or infinite field" + pfac.coFac);
            }
        }
        if (t == 9) { // other fields of char 0
            if (fac.characteristic().signum() == 0) {
                ufd = new SquarefreeFieldChar0/*raw*/(fac);
            }
        }
        if (ufd == null) {
            throw new RuntimeException("no squarefree factorization implementation for "
                    + fac.getClass().getName());
        }
        logger.debug("ufd = " + ufd);
        //System.out.println("ufd = " + ufd);
        return (SquarefreeAbstract<C>) ufd;
    }

}
