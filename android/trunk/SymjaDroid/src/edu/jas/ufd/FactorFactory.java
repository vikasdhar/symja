/*
 * $Id: FactorFactory.java 3622 2011-04-29 13:10:12Z kredel $
 */

package edu.jas.ufd;


import org.matheclipse.android.util.Logger;

import edu.jas.arith.BigInteger;
import edu.jas.arith.BigRational;
import edu.jas.arith.ModInteger;
import edu.jas.arith.ModIntegerRing;
import edu.jas.arith.ModLong;
import edu.jas.arith.ModLongRing;
import edu.jas.arith.Rational;
import edu.jas.poly.AlgebraicNumber;
import edu.jas.poly.AlgebraicNumberRing;
import edu.jas.poly.Complex;
import edu.jas.poly.ComplexRing;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.root.RealAlgebraicNumber;
import edu.jas.root.RealAlgebraicRing;
import edu.jas.structure.GcdRingElem;
import edu.jas.structure.RingFactory;


/**
 * Factorization algorithms factory. Select appropriate factorization engine
 * based on the coefficient types.
 * @author Heinz Kredel
 * @usage To create objects that implement the <code>Factorization</code>
 *        interface use the <code>FactorFactory</code>. It will select an
 *        appropriate implementation based on the types of polynomial
 *        coefficients C. To obtain an implementation use
 *        <code>getImplementation()</code>, it returns an object of a class
 *        which extends the <code>FactorAbstract</code> class which implements
 *        the <code>Factorization</code> interface.
 * 
 *        <pre>
 * Factorization&lt;CT&gt; engine;
 * engine = FactorFactory.&lt;CT&gt; getImplementation(cofac);
 * c = engine.factors(a);
 * </pre>
 * 
 *        For example, if the coefficient type is BigInteger, the usage looks
 *        like
 * 
 *        <pre>
 * BigInteger cofac = new BigInteger();
 * Factorization&lt;BigInteger&gt; engine;
 * engine = FactorFactory.getImplementation(cofac);
 * Sm = engine.factors(poly);
 * </pre>
 * 
 * @see edu.jas.ufd.Factorization#factors(edu.jas.poly.GenPolynomial P)
 */

public class FactorFactory {


    private static final Logger logger = Logger.getLogger(FactorFactory.class);


    /**
     * Protected factory constructor.
     */
    protected FactorFactory() {
    }


    /**
     * Determine suitable implementation of factorization algorithm, case
     * ModInteger.
     * @param fac ModIntegerRing.
     * @return factorization algorithm implementation.
     */
    public static FactorAbstract<ModInteger> getImplementation(ModIntegerRing fac) {
        return new FactorModular<ModInteger>(fac);
    }


    /**
     * Determine suitable implementation of factorization algorithm, case
     * ModInteger.
     * @param fac ModIntegerRing.
     * @return factorization algorithm implementation.
     */
    public static FactorAbstract<ModLong> getImplementation(ModLongRing fac) {
        return new FactorModular<ModLong>(fac);
    }


    /**
     * Determine suitable implementation of factorization algorithm, case
     * BigInteger.
     * @param fac BigInteger.
     * @return factorization algorithm implementation.
     */
    public static FactorAbstract<BigInteger> getImplementation(BigInteger fac) {
        return new FactorInteger<ModLong>();
    }


    /**
     * Determine suitable implementation of factorization algorithms, case
     * BigRational.
     * @param fac BigRational.
     * @return factorization algorithm implementation.
     */
    public static FactorAbstract<BigRational> getImplementation(BigRational fac) {
        return new FactorRational();
    }


    /**
     * Determine suitable implementation of factorization algorithms, case
     * AlgebraicNumber&lt;C&gt;.
     * @param fac AlgebraicNumberRing&lt;C&gt;.
     * @param <C> coefficient type, e.g. BigRational, ModInteger.
     * @return factorization algorithm implementation.
     */
    public static <C extends GcdRingElem<C>> FactorAbstract<AlgebraicNumber<C>> getImplementation(
            AlgebraicNumberRing<C> fac) {
        return new FactorAlgebraic<C>(fac);
    }


    /**
     * Determine suitable implementation of factorization algorithms, case
     * RealAlgebraicNumber&lt;C&gt;.
     * @param fac RealAlgebraicRing&lt;C&gt;.
     * @param <C> coefficient type, e.g. BigRational.
     * @return factorization algorithm implementation.
     */
    public static <C extends GcdRingElem<C> & Rational> FactorAbstract<RealAlgebraicNumber<C>> getImplementation(
            RealAlgebraicRing<C> fac) {
        return new FactorRealAlgebraic<C>(fac);
    }


    /**
     * Determine suitable implementation of factorization algorithms, case
     * RealAlgebraicNumber&lt;C&gt;.
     * @param fac RealAlgebraicRing&lt;C&gt;.
     * @param <C> coefficient type, e.g. BigRational.
     * @return factorization algorithm implementation.
     */
    public static <C extends GcdRingElem<C> & Rational> FactorAbstract<edu.jas.application.RealAlgebraicNumber<C>> getImplementation(
            edu.jas.application.RealAlgebraicRing<C> fac) {
        return new FactorRealReal<C>(fac);
    }


    /**
     * Determine suitable implementation of factorization algorithms, case
     * Complex&lt;C&gt;.
     * @param fac ComplexRing&lt;C&gt;.
     * @param <C> coefficient type, e.g. BigRational, ModInteger.
     * @return factorization algorithm implementation.
     */
    public static <C extends GcdRingElem<C>> FactorAbstract<Complex<C>> getImplementation(ComplexRing<C> fac) {
        return new FactorComplex<C>(fac);
    }


    /**
     * Determine suitable implementation of factorization algorithms, case
     * Quotient&lt;C&gt;.
     * @param fac QuotientRing&lt;C&gt;.
     * @param <C> coefficient type, e.g. BigRational, ModInteger.
     * @return factorization algorithm implementation.
     */
    public static <C extends GcdRingElem<C>> FactorAbstract<Quotient<C>> getImplementation(QuotientRing<C> fac) {
        return new FactorQuotient<C>(fac);
    }


    /**
     * Determine suitable implementation of factorization algorithms, case
     * recursive GenPolynomial&lt;C&gt;. Use <code>recursiveFactors()</code>.
     * @param fac GenPolynomialRing&lt;C&gt;.
     * @param <C> coefficient type, e.g. BigRational, ModInteger.
     * @return factorization algorithm implementation.
     */
    public static <C extends GcdRingElem<C>> FactorAbstract<C> getImplementation(GenPolynomialRing<C> fac) {
        return getImplementation(fac.coFac);
    }


    /**
     * Determine suitable implementation of factorization algorithms, other
     * cases.
     * @param <C> coefficient type
     * @param fac RingFactory&lt;C&gt;.
     * @return factorization algorithm implementation.
     */
    @SuppressWarnings("unchecked")
    public static <C extends GcdRingElem<C>> FactorAbstract<C> getImplementation(RingFactory<C> fac) {
        //logger.info("fac = " + fac.getClass().getName());
        //System.out.println("fac_o = " + fac.getClass().getName());
        int t = 0;
        FactorAbstract/*raw type<C>*/ufd = null;
        AlgebraicNumberRing afac = null;
        RealAlgebraicRing rfac = null;
        edu.jas.application.RealAlgebraicRing rrfac = null;
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
                t = 9;
                break;
            }
            if (ofac instanceof ComplexRing) {
                t = 11;
                break;
            }
            if (ofac instanceof AlgebraicNumberRing) {
                //System.out.println("afac_o = " + ofac);
                afac = (AlgebraicNumberRing) ofac;
                ofac = afac.ring.coFac;
                t = 4;
                break;
            }
            if (ofac instanceof RealAlgebraicRing) {
                //System.out.println("rfac_o = " + ofac);
                rfac = (RealAlgebraicRing) ofac;
                ofac = rfac.algebraic;
                t = 5;
                break;
            }
            if (ofac instanceof edu.jas.application.RealAlgebraicRing) {
                //System.out.println("rrfac_o = " + ofac);
                rrfac = (edu.jas.application.RealAlgebraicRing) ofac;
                ofac = rrfac.realRing;
                t = 6;
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
            break;
        }
        //System.out.println("ft = " + t);
        if (t == 0) {
            throw new IllegalArgumentException("no factorization implementation for "
                    + fac.getClass().getName());
        }
        if (t == 1) {
            ufd = new FactorInteger();
        }
        if (t == 2) {
            ufd = new FactorRational();
        }
        if (t == 3) {
            ufd = new FactorModular(fac);
        }
        if (t == 9) {
            ufd = new FactorModular(fac);
        }
        if (t == 11) {
            ufd = new FactorComplex(fac);
        }
        if (t == 4) {
            ufd = new FactorAlgebraic/*raw <C>*/(afac);
        }
        if (t == 5) {
            ufd = new FactorRealAlgebraic/*raw <C>*/(rfac);
        }
        if (t == 6) {
            ufd = new FactorRealReal/*raw <C>*/(rrfac);
        }
        if (t == 7) {
            ufd = new FactorQuotient/*raw <C>*/(qfac);
        }
        if (t == 8) {
            ufd = getImplementation(pfac.coFac);
        }
        logger.info("ufd = " + ufd);
        return (FactorAbstract<C>) ufd;
    }

}
