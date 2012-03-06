/*
 * $Id: RootFactory.java 3879 2012-02-05 16:51:04Z kredel $
 */

package edu.jas.application;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.jas.arith.Rational;
import edu.jas.poly.Complex;
import edu.jas.poly.ComplexRing;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.PolyUtil;
import edu.jas.poly.TermOrder;
import edu.jas.root.ComplexRoots;
import edu.jas.root.ComplexRootsSturm;
import edu.jas.root.RealRootTuple;
import edu.jas.root.Interval;
import edu.jas.structure.GcdRingElem;
import edu.jas.structure.RingFactory;
import edu.jas.ufd.SquarefreeAbstract;
import edu.jas.ufd.SquarefreeFactory;


/**
 * Roots factory.
 * @author Heinz Kredel
 */
public class RootFactory {


    private static final Logger logger = Logger.getLogger(RootFactory.class);


    //private static boolean debug = logger.isDebugEnabled();


    /**
     * Is complex algebraic number a root of a polynomial.
     * @param f univariate polynomial.
     * @param r complex algebraic number.
     * @return true, if f(r) == 0, else false;
     */
    public static <C extends GcdRingElem<C> & Rational> 
           boolean isRoot(GenPolynomial<Complex<C>> f, Complex<RealAlgebraicNumber<C>> r) {
        ComplexRing<RealAlgebraicNumber<C>> cr = r.factory(); 
        GenPolynomialRing<Complex<RealAlgebraicNumber<C>>> cfac 
           = new GenPolynomialRing<Complex<RealAlgebraicNumber<C>>>(cr,f.factory());
        GenPolynomial<Complex<RealAlgebraicNumber<C>>> p;
        p = PolyUtilApp.<C> convertToComplexRealCoefficients(cfac,f);
        //System.out.println("p = " + p);
        // test algebraic part
        Complex<RealAlgebraicNumber<C>> a = PolyUtil.<Complex<RealAlgebraicNumber<C>>> evaluateMain(cr,p,r);
        boolean t = a.isZERO();
        if ( !t ) {
            logger.info("p(r) = " + a + ", p = " + f + ", r  = " + r);
            return t;
        }
        // test real part
        RealAlgebraicRing<C> rring = (RealAlgebraicRing<C>)cr.ring;
        RealRootTuple<C> rroot = rring.getRoot();
        List<edu.jas.root.RealAlgebraicNumber<C>> rlist = rroot.tuple;
        //System.out.println("rlist = " + rlist);
        Interval<C> vr = rlist.get(0).ring.getRoot();
        Interval<C> vi = rlist.get(1).ring.getRoot();
        ComplexRing<C> ccfac = new ComplexRing<C>((RingFactory<C>)vr.left.factory()); 
        Complex<C> sw = new Complex<C>(ccfac,vr.left,vi.left);
        Complex<C> ne = new Complex<C>(ccfac,vr.right,vi.right);
        Complex<C> epsw = PolyUtil.<Complex<C>> evaluateMain(ccfac, f, sw);
        Complex<C> epne = PolyUtil.<Complex<C>> evaluateMain(ccfac, f, ne);
        int rootre = (epsw.getRe().signum()*epne.getRe().signum());
        int rootim = (epsw.getIm().signum()*epne.getIm().signum());
        t = (rootre <= 0 && rootim <= 0);
        if ( !t ) {
            logger.debug("vr = " + vr + ", vi = " + vi);
            logger.info("sw   = " + sw   + ", ne   = " + ne);
            //System.out.println("root(re) = " + rootre + ", root(im) = " + rootim);
            logger.info("p(root): p = " + f + ", epsw   = " + epsw   + ", epne   = " + epne);
            return t;
        }
        //System.out.println("r = " + r.getRe().magnitude() + " i " + r.getIm().magnitude());
        return t;
    }


    /**
     * Is complex algebraic number a root of a polynomial.
     * @param f univariate polynomial.
     * @param R list of complex algebraic numbers.
     * @return true, if f(r) == 0 for all r in R, else false;
     */
    public static <C extends GcdRingElem<C> & Rational> 
           boolean isRoot(GenPolynomial<Complex<C>> f, List<Complex<RealAlgebraicNumber<C>>> R) {
        for ( Complex<RealAlgebraicNumber<C>> r : R ) {
            boolean t = isRoot(f,r);
            if ( !t ) {
                return false;
            }
        }
        return true;
    }


    /**
     * Complex algebraic number roots.
     * @param f univariate polynomial.
     * @return a list of different complex algebraic numbers, with f(c) == 0 for c in roots.
     */
    public static <C extends GcdRingElem<C> & Rational> 
           List<Complex<RealAlgebraicNumber<C>>> complexAlgebraicNumbersComplex(GenPolynomial<Complex<C>> f) {
        GenPolynomialRing<Complex<C>> pfac = f.factory();
        if (pfac.nvar != 1) {
            throw new IllegalArgumentException("only for univariate polynomials");
        }
        ComplexRing<C> cfac = (ComplexRing<C>) pfac.coFac;
        ComplexRoots<C> cr = new ComplexRootsSturm<C>(cfac);
        SquarefreeAbstract<Complex<C>> engine = SquarefreeFactory.<Complex<C>> getImplementation(cfac);
        Map<GenPolynomial<Complex<C>>,Long> F = engine.squarefreeFactors(f.monic());
        Set<GenPolynomial<Complex<C>>> S = F.keySet();
        //System.out.println("S = " + S);
        List<Complex<RealAlgebraicNumber<C>>> list = new ArrayList<Complex<RealAlgebraicNumber<C>>>();
        for (GenPolynomial<Complex<C>> sp : S) {
            if (sp.isConstant() || sp.isZERO()) {
                continue;
            }
            List<Complex<RealAlgebraicNumber<C>>> ls = RootFactory.<C> complexAlgebraicNumbersSquarefree(sp);
            long m = F.get(sp);
            for (long i = 0L; i < m; i++) {
                list.addAll(ls);
            }
        }
        return list;
    }


    /**
     * Complex algebraic number roots.
     * @param f univariate squarefree polynomial.
     * @return a list of different complex algebraic numbers, with f(c) == 0 for c in roots.
     */
    public static <C extends GcdRingElem<C> & Rational> 
           List<Complex<RealAlgebraicNumber<C>>> complexAlgebraicNumbersSquarefree(GenPolynomial<Complex<C>> f) {
        GenPolynomialRing<Complex<C>> pfac = f.factory();
        if (pfac.nvar != 1) {
            throw new IllegalArgumentException("only for univariate polynomials");
        }
        ComplexRing<C> cfac = (ComplexRing<C>) pfac.coFac;
        ComplexRoots<C> cr = new ComplexRootsSturm<C>(cfac);
        TermOrder to = new TermOrder(TermOrder.INVLEX);
        GenPolynomialRing<Complex<C>> tfac = new GenPolynomialRing<Complex<C>>(cfac, 2, to); //,vars); //tord?
        //System.out.println("tfac = " + tfac);
        GenPolynomial<Complex<C>> t = tfac.univariate(1, 1L).sum(
                        tfac.univariate(0, 1L).multiply(cfac.getIMAG()));
        //System.out.println("t = " + t); // t = x + i y

        GenPolynomialRing<C> rfac = new GenPolynomialRing<C>(cfac.ring, tfac); //tord?
        //System.out.println("rfac = " + rfac);

        List<Complex<RealAlgebraicNumber<C>>> list = new ArrayList<Complex<RealAlgebraicNumber<C>>>();
        GenPolynomial<Complex<C>> sp = f;
        if (sp.isConstant() || sp.isZERO()) {
            return list;
        }
        GenPolynomial<Complex<C>> su = PolyUtil.<Complex<C>> substituteUnivariate(sp, t);
        //System.out.println("su = " + su);
        su = su.monic();
        //System.out.println("su = " + su);
        GenPolynomial<C> re = PolyUtil.<C> realPartFromComplex(rfac, su);
        GenPolynomial<C> im = PolyUtil.<C> imaginaryPartFromComplex(rfac, su);
        if ( logger.isInfoEnabled() ) {
            logger.debug("rfac = " + rfac.toScript());
            logger.info("t = " + t);
            logger.info("re   = " + re.toScript());
            logger.info("im   = " + im.toScript());
        }
        List<GenPolynomial<C>> li = new ArrayList<GenPolynomial<C>>(2);
        li.add(re);
        li.add(im);
        Ideal<C> id = new Ideal<C>(rfac, li);
        //System.out.println("id = " + id);

        List<IdealWithUniv<C>> idul = id.zeroDimRootDecomposition();

        IdealWithRealAlgebraicRoots<C> idr;
        for (IdealWithUniv<C> idu : idul) {
            //System.out.println("---idu = " + idu);
            idr = PolyUtilApp.<C> realAlgebraicRoots(idu);
            //System.out.println("---idr = " + idr);
            for (List<edu.jas.root.RealAlgebraicNumber<C>> crr : idr.ran) {
                //System.out.println("crr = " + crr);
                RealRootTuple<C> root = new RealRootTuple<C>(crr);
                //System.out.println("root = " + root);
                RealAlgebraicRing<C> car = new RealAlgebraicRing<C>(idu, root);
                //System.out.println("car = " + car);
                List<RealAlgebraicNumber<C>> gens = car.generators();
                //System.out.println("gens = " + gens);
                int sg = gens.size();
                RealAlgebraicNumber<C> rre = gens.get(sg-2);
                RealAlgebraicNumber<C> rim = gens.get(sg-1);
                ComplexRing<RealAlgebraicNumber<C>> cring = new ComplexRing<RealAlgebraicNumber<C>>(car);
                Complex<RealAlgebraicNumber<C>> crn = new Complex<RealAlgebraicNumber<C>>(cring,rre,rim);
                //System.out.println("crn = " + crn.toScript());

                boolean it;
                int count = 0;
                do { // refine intervals if necessary
                    Interval<C> vr = crr.get(0).ring.getRoot();
                    Interval<C> vi = crr.get(1).ring.getRoot();
                    ComplexRing<C> ccfac = new ComplexRing<C>((RingFactory<C>)vr.left.factory()); 
                    Complex<C> sw = new Complex<C>(ccfac,vr.left,vi.left);
                    Complex<C> ne = new Complex<C>(ccfac,vr.right,vi.right);
                    Complex<C> epsw = PolyUtil.<Complex<C>> evaluateMain(ccfac, f, sw);
                    Complex<C> epne = PolyUtil.<Complex<C>> evaluateMain(ccfac, f, ne);
                    int rootre = (epsw.getRe().signum()*epne.getRe().signum());
                    int rootim = (epsw.getIm().signum()*epne.getIm().signum());
                    it = (rootre <= 0 && rootim <= 0);
                    if ( !it ) {
                        logger.info("refine intervals: vr = " + vr + ", vi = " + vi);
                        //System.out.println("crn = " + crn.getRe().magnitude() + " i " + crn.getIm().magnitude());
                        //System.out.println("sw   = " + sw   + ", ne   = " + ne);
                        //System.out.println("epsw   = " + epsw   + ", epne   = " + epne);
                        //System.out.println("root(re) = " + rootre + ", root(im) = " + rootim);
                        crn.getRe().ring.realRing.halfInterval();
                        //System.out.println("crn.re = " + crn.getRe().ring.realRing);
                        crn.getIm().ring.realRing.halfInterval();
                        //System.out.println("crn.im = " + crn.getIm().ring.realRing);
                        //edu.jas.root.RealAlgebraicRing<C> rrr;
                        //rrr = (edu.jas.root.RealAlgebraicRing<C>) crn.getRe().ring.realRing.algebraic.ring.coFac;
                        //rrr.halfInterval();
                        //System.out.println("rrr.re = " + rrr);
                        //rrr = (edu.jas.root.RealAlgebraicRing<C>) crn.getIm().ring.realRing.algebraic.ring.coFac;
                        //rrr.halfInterval();
                        //System.out.println("rrr.im = " + rrr);
                        if ( count++ > 2 ) {
                            //throw new RuntimeException("no roots of " + f);
                            logger.info("break in root refinement of " + f);
                            it = true;
                        }
                    }
                } while ( !it );
                list.add(crn);
            }
        }
        return list;
    }


    /* todo
     * Complex algebraic numbers.
     * @param f univariate polynomial.
     * @param eps rational precision.
     * @return a list of different complex algebraic numbers.
     public static <C extends GcdRingElem<C> & Rational> 
     List<ComplexAlgebraicNumber<C>> complexAlgebraicNumbersComplex(GenPolynomial<Complex<C>> f, BigRational eps) {
     }
    */

}
