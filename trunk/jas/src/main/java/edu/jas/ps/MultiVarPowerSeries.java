/*
 * $Id: MultiVarPowerSeries.java 3327 2010-09-18 12:16:19Z kredel $
 */

package edu.jas.ps;


import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.SortedMap;

import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.structure.BinaryFunctor;
import edu.jas.structure.RingElem;
import edu.jas.structure.Selector;
import edu.jas.structure.UnaryFunctor;
import edu.jas.util.ExpVectorIterable;


/**
 * Multivariate power series implementation. Uses inner classes and lazy
 * evaluated generating function for coefficients. All ring element methods use
 * lazy evaluation except where noted otherwise. Eager evaluated methods are
 * <code>toString()</code>, <code>compareTo()</code>, <code>equals()</code>,
 * <code>evaluate()</code>, or methods which use the <code>order()</code> or
 * <code>orderExpVector()</code> methods, like <code>signum()</code>,
 * <code>abs()</code>, <code>divide()</code>, <code>remainder()</code> and
 * <code>gcd()</code>.
 * <b>Note: </b> Currently the term order is fixed to the order defined by 
 * the iterator over exponent vectors <code>ExpVectorIterator</code>.
 * @param <C> ring element type
 * @author Heinz Kredel
 */

public class MultiVarPowerSeries<C extends RingElem<C>> implements RingElem<MultiVarPowerSeries<C>> {


    /**
     * Power series ring factory.
     */
    public final MultiVarPowerSeriesRing<C> ring;


    /**
     * Data structure / generating function for coeffcients. Cannot be final
     * because of fixPoint, must be accessible in factory.
     */
    /*package*/MultiVarCoefficients<C> lazyCoeffs;


    /**
     * Truncation of computations.
     */
    private int truncate;


    /**
     * Order of power series.
     */
    private int order = -1; // == unknown


    /**
     * ExpVector of order of power series.
     */
    private ExpVector evorder = null; // == unknown


    /**
     * Private constructor.
     */
    private MultiVarPowerSeries() {
        throw new IllegalArgumentException("do not use no-argument constructor");
    }


    /**
     * Package constructor. Use in fixPoint only, must be accessible in factory.
     * @param ring power series ring.
     */
    /*package*/MultiVarPowerSeries(MultiVarPowerSeriesRing<C> ring) {
        this.ring = ring;
        this.lazyCoeffs = null;
        this.truncate = ring.truncate;
    }


    /**
     * Constructor.
     * @param ring power series ring.
     * @param lazyCoeffs generating function for coefficients.
     */
    public MultiVarPowerSeries(MultiVarPowerSeriesRing<C> ring, MultiVarCoefficients<C> lazyCoeffs) {
        this(ring, lazyCoeffs, ring.truncate);
    }


    /**
     * Constructor.
     * @param ring power series ring.
     * @param lazyCoeffs generating function for coefficients.
     * @param trunc truncate parameter for this power series.
     */
    public MultiVarPowerSeries(MultiVarPowerSeriesRing<C> ring, MultiVarCoefficients<C> lazyCoeffs, int trunc) {
        if (lazyCoeffs == null || ring == null) {
            throw new IllegalArgumentException("null not allowed: ring = " + ring + ", lazyCoeffs = "
                    + lazyCoeffs);
        }
        this.ring = ring;
        this.lazyCoeffs = lazyCoeffs;
        this.truncate = trunc;
        if ( trunc > ring.truncate ) {
            //System.out.println("trunc = " + trunc + ", ring.trunc = " + ring.truncate);// + ", this = " + toString());
        }
    }


    /**
     * Get the corresponding element factory.
     * @return factory for this Element.
     * @see edu.jas.structure.Element#factory()
     */
    public MultiVarPowerSeriesRing<C> factory() {
        return ring;
    }


    /**
     * Clone this power series.
     * @see java.lang.Object#clone()
     */
    @Override
    public MultiVarPowerSeries<C> clone() {
        return new MultiVarPowerSeries<C>(ring, lazyCoeffs);
    }


    /**
     * String representation of power series.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return toString(truncate);
    }


    /**
     * To String with given truncate.
     * @return string representation of this to given truncate.
     */
    public String toString(int truncate) {
        StringBuffer sb = new StringBuffer();
        MultiVarPowerSeries<C> s = this;
        String[] vars = ring.vars;
        //System.out.println("cache1 = " + s.lazyCoeffs.coeffCache);
        for (ExpVector i : new ExpVectorIterable(ring.nvar, true, truncate)) {
            C c = s.coefficient(i);
            //System.out.println("i = " + i + ", c = " +c);
            int si = c.signum();
            if (si != 0) {
                if (si > 0) {
                    if (sb.length() > 0) {
                        sb.append(" + ");
                    }
                } else {
                    c = c.negate();
                    sb.append(" - ");
                }
                if (!c.isONE() || i.isZERO()) {
                    sb.append(c.toString());
                    if (!i.isZERO()) {
                        sb.append(" * ");
                    }
                }
                if (i.isZERO()) {
                    //skip; sb.append(" ");
                } else {
                    sb.append(i.toString(vars));
                }
                //sb.append(c.toString() + ", ");
            }
            //System.out.println("cache = " + s.coeffCache);
        }
        if (sb.length() == 0) {
            sb.append("0");
        }
        sb.append(" + BigO( (" + ring.varsToString() + ")^" + truncate + "(" + ring.truncate + ") )");
        //sb.append("...");
        //System.out.println("cache2 = " + s.lazyCoeffs.coeffCache);
        return sb.toString();
    }


    /**
     * Get a scripting compatible string representation.
     * @return script compatible representation for this Element.
     * @see edu.jas.structure.Element#toScript()
     */
    //JAVA6only: @Override
    public String toScript() {
        // Python case
        StringBuffer sb = new StringBuffer("");
        MultiVarPowerSeries<C> s = this;
        String[] vars = ring.vars;
        //System.out.println("cache = " + s.coeffCache);
        for (ExpVector i : new ExpVectorIterable(ring.nvar, true, truncate)) {
            C c = s.coefficient(i);
            int si = c.signum();
            if (si != 0) {
                if (si > 0) {
                    if (sb.length() > 0) {
                        sb.append(" + ");
                    }
                } else {
                    c = c.negate();
                    sb.append(" - ");
                }
                if (!c.isONE() || i.isZERO()) {
                    sb.append(c.toScript());
                    if (!i.isZERO()) {
                        sb.append(" * ");
                    }
                }
                if (i.isZERO()) {
                    //skip; sb.append(" ");
                } else {
                    sb.append(i.toScript(vars));
                }
                //sb.append(c.toString() + ", ");
            }
            //System.out.println("cache = " + s.coeffCache);
        }
        if (sb.length() == 0) {
            sb.append("0");
        }
        sb.append(" + BigO( (" + ring.varsToString() + ")**" + truncate + " )");
        // sb.append("," + truncate + "");
        return sb.toString();
    }


    /**
     * Get a scripting compatible string representation of the factory.
     * @return script compatible representation for this ElemFactory.
     * @see edu.jas.structure.Element#toScriptFactory()
     */
    //JAVA6only: @Override
    public String toScriptFactory() {
        // Python case
        return factory().toScript();
    }


    /**
     * Get coefficient.
     * @param index number of requested coefficient.
     * @return coefficient at index.
     */
    public C coefficient(ExpVector index) {
        if (index == null) {
            throw new IndexOutOfBoundsException("null index not allowed");
        }
        return lazyCoeffs.get(index);
    }


    /**
     * Homogeneous part.
     * @param tdeg requested degree.
     * @return polynomial part of given degree.
     */
    public GenPolynomial<C> homogeneousPart(long tdeg) {
        if (lazyCoeffs == null) {
            throw new IllegalArgumentException("null cache not allowed");
        }
        return lazyCoeffs.getHomPart(tdeg);
    }


    /**
     * Get a GenPolynomial&lt;C&gt; from this.
     * @return a GenPolynomial&lt;C&gt; from this up to truncate homogeneous
     *         parts.
     */
    public GenPolynomial<C> asPolynomial() {
        GenPolynomial<C> p = homogeneousPart(0L);
        for (int i = 1; i <= truncate; i++) {
            p = p.sum(homogeneousPart(i));
        }
        return p;
    }


    /**
     * Leading base coefficient.
     * @return first coefficient.
     */
    public C leadingCoefficient() {
        return coefficient(ring.EVZERO);
    }


    /**
     * Reductum.
     * @param r variable for taking the reductum.
     * @return this - leading monomial in the direcrion of r.
     */
    public MultiVarPowerSeries<C> reductum(final int r) {
        if (r < 0 || ring.nvar < r) {
            throw new IllegalArgumentException("variable index out of bound");
        }
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            @Override
            public C generate(ExpVector i) {
                ExpVector e = i.subst(r, i.getVal(r) + 1);
                return coefficient(e);
            }
        });
    }


    /**
     * Prepend a new leading coefficient.
     * @param r variable for the direction.
     * @param h new coefficient.
     * @return new power series.
     */
    public MultiVarPowerSeries<C> prepend(final C h, final int r) {
        if (r < 0 || ring.nvar < r) {
            throw new IllegalArgumentException("variable index out of bound");
        }
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            @Override
            public C generate(ExpVector i) {
                if (i.isZERO()) {
                    return h;
                } else {
                    ExpVector e = i.subst(r, i.getVal(r) - 1);
                    if ( e.signum() < 0 ) {
                        return pfac.coFac.getZERO();
                    }
                    return coefficient(e);
                }
            }
        });
    }


    /**
     * Shift coefficients.
     * @param k shift index.
     * @param r variable for the direction.
     * @return new power series with coefficient(i) = old.coefficient(i-k).
     */
    public MultiVarPowerSeries<C> shift(final int k, final int r) {
        if (r < 0 || ring.nvar < r) {
            throw new IllegalArgumentException("variable index out of bound");
        }
        int nt = truncate; // + k;
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            @Override
            public C generate(ExpVector i) {
                long d = i.getVal(r);
                if (d - k < 0) {
                    return ring.coFac.getZERO();
                } else {
                    ExpVector e = i.subst(r, i.getVal(r) - k);
                    return coefficient(e);
                }
            }
        });
    }


    /**
     * Reductum.
     * @return this - leading monomial.
     */
    public MultiVarPowerSeries<C> reductum() {
        Map.Entry<ExpVector, C> m = orderMonomial();
        ExpVector e = m.getKey();
        long d = e.totalDeg();
        MultiVarCoefficients<C> mc = lazyCoeffs;
        HashMap<Long, GenPolynomial<C>> cc = new HashMap<Long, GenPolynomial<C>>(mc.coeffCache);
        GenPolynomial<C> p = cc.get(d);
        if ( p != null && !p.isZERO() ) {
            p = p.subtract(m.getValue(),e); // p contains this term after orderMonomial()
            cc.put(d,p);
        }
        HashSet<ExpVector> z = new HashSet<ExpVector>(mc.zeroCache);
        z.add(e);
        //System.out.println("z = " + z);

        return new MultiVarPowerSeries<C>(ring, 
                   new MultiVarCoefficients<C>(mc.pfac,cc,z,mc.homCheck) {
            @Override
            public C generate(ExpVector i) {
                return coefficient(i);
            }
        });
    }


    /**
     * Shift coefficients. Multiply by exponent vector.
     * @param k shift ExpVector.
     * @return new power series with coefficient(i) = old.coefficient(i-k).
     */
    public MultiVarPowerSeries<C> shift(final ExpVector k) {
        if (k == null) {
            throw new IllegalArgumentException("null ExpVector not allowed");
        }
        if (k.signum() == 0) {
            return this;
        }
        int nt = truncate; // + (int)k.totalDeg();
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            @Override
            public C generate(ExpVector i) {
                ExpVector d = i.subtract(k);
                if (d.signum() < 0) {
                    return ring.coFac.getZERO();
                } else {
                    return coefficient(d);
                }
            }
        },nt);
    }


    /**
     * Multiply by exponent vector and coefficient.
     * @param k shift ExpVector.
     * @param c coefficient multiplier.
     * @return new power series with coefficient(i) = old.coefficient(i-k)*c.
     */
    public MultiVarPowerSeries<C> multiply(final C c, final ExpVector k) {
        if (k == null) {
            throw new IllegalArgumentException("null ExpVector not allowed");
        }
        if (k.signum() == 0) {
            return this.multiply(c);
        }
        if (c.signum() == 0) {
            return ring.getZERO();
        }
        int nt = truncate; // + (int)k.totalDeg();
        //System.out.println("nt = " + nt + ", truncate = " + truncate + ", tdeg = " + k.totalDeg());
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            @Override
            public C generate(ExpVector i) {
                ExpVector d = i.subtract(k);
                if (d.signum() < 0) {
                    return ring.coFac.getZERO();
                } else {
                    return coefficient(d).multiply(c);
                }
            }
        },nt);
    }


    /**
     * Sum monomial.
     * @param m ExpVector , coeffcient pair
     * @return this + ONE.multiply(m.coefficient,m.exponent).
     */
    public MultiVarPowerSeries<C> sum(Map.Entry<ExpVector,C> m) {
        if (m == null) {
            throw new IllegalArgumentException("null Map.Entry not allowed");
        }
        return sum(m.getValue(),m.getKey());
    }


    /**
     * Sum exponent vector and coefficient.
     * @param k ExpVector.
     * @param c coefficient.
     * @return this + ONE.multiply(c,k).
     */
    public MultiVarPowerSeries<C> sum(final C c, final ExpVector k) {
        if (k == null) {
            throw new IllegalArgumentException("null ExpVector not allowed");
        }
        if (c.signum() == 0) {
            return this;
        }
        long d = k.totalDeg();
        MultiVarCoefficients<C> mc = lazyCoeffs;
        HashMap<Long, GenPolynomial<C>> cc = new HashMap<Long, GenPolynomial<C>>(mc.coeffCache);
        GenPolynomial<C> p = cc.get(d);
        if ( p == null ) {
            p = mc.pfac.getZERO();
        }
        p = p.sum(c,k); 
        //System.out.println("p = " + p);
        cc.put(d,p);
        HashSet<ExpVector> z = new HashSet<ExpVector>(mc.zeroCache);
        //System.out.println("z = " + z);
        C x = p.coefficient(k);
        if ( x.isZERO() ) {
            z.add(k);
        }

        return new MultiVarPowerSeries<C>(ring, 
                   new MultiVarCoefficients<C>(mc.pfac,cc,z,mc.homCheck) {
            @Override
            public C generate(ExpVector i) {
                return coefficient(i);
            }
        });
    }


    /**
     * Subtract exponent vector and coefficient.
     * @param k ExpVector.
     * @param c coefficient.
     * @return this - ONE.multiply(c,k).
     */
    public MultiVarPowerSeries<C> subtract(final C c, final ExpVector k) {
        if (k == null) {
            throw new IllegalArgumentException("null ExpVector not allowed");
        }
        if (c.signum() == 0) {
            return this;
        }
        long d = k.totalDeg();
        MultiVarCoefficients<C> mc = lazyCoeffs;
        HashMap<Long, GenPolynomial<C>> cc = new HashMap<Long, GenPolynomial<C>>(mc.coeffCache);
        GenPolynomial<C> p = cc.get(d);
        if ( p == null ) {
            p = mc.pfac.getZERO();
        }
        p = p.subtract(c,k); 
        cc.put(d,p);
        HashSet<ExpVector> z = new HashSet<ExpVector>(mc.zeroCache);
        //System.out.println("z = " + z);
        C x = p.coefficient(k);
        if ( x.isZERO() ) {
            z.add(k);
        }
        return new MultiVarPowerSeries<C>(ring, 
                   new MultiVarCoefficients<C>(mc.pfac,cc,z,mc.homCheck) {
            @Override
            public C generate(ExpVector i) {
                return coefficient(i);
            }
        });
    }


    /**
     * Sum exponent vector and coefficient.
     * @param mvc cached coefficients.
     * @return this + mvc.
     */
    public MultiVarPowerSeries<C> sum(MultiVarCoefficients<C> mvc) {
        MultiVarCoefficients<C> mc = lazyCoeffs;
        TreeMap<Long, GenPolynomial<C>> cc = new TreeMap<Long, GenPolynomial<C>>(mc.coeffCache);
        TreeMap<Long, GenPolynomial<C>> ccv = new TreeMap<Long, GenPolynomial<C>>(mvc.coeffCache);
        long d1 = ( cc.size() > 0  ? cc.lastKey() : 0 );
        long d2 = ( ccv.size() > 0 ? ccv.lastKey() : 0 );
        HashSet<ExpVector> z = new HashSet<ExpVector>(mc.zeroCache);
        z.addAll(mvc.zeroCache);
        long d = Math.max(d1,d2);
        for ( long i = 0; i < d; i++ ) {
            GenPolynomial<C> p1 = cc.get(i);
            GenPolynomial<C> p2 = mvc.coeffCache.get(i);
            if ( p1 == null ) {
                p1 = mc.pfac.getZERO();
            }
            if ( p2 == null ) {
                p2 = mc.pfac.getZERO();
            }
            GenPolynomial<C> p = p1.sum(p2);
            //System.out.println("p = " + p);
            cc.put(i,p);
            Set<ExpVector> ev = new HashSet<ExpVector>(p1.getMap().keySet());
            ev.addAll(p2.getMap().keySet());
            ev.removeAll(p.getMap().keySet());
            z.addAll(ev);
        }
        //System.out.println("z = " + z);

        return new MultiVarPowerSeries<C>(ring, 
                                          new MultiVarCoefficients<C>(mc.pfac,new HashMap<Long, GenPolynomial<C>>(cc),z) {
            @Override
            public C generate(ExpVector i) {
                return coefficient(i);
            }
        });
    }


    /**
     * Select coefficients.
     * @param sel selector functor.
     * @return new power series with selected coefficients.
     */
    public MultiVarPowerSeries<C> select(final Selector<? super C> sel) {
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            @Override
            public C generate(ExpVector i) {
                C c = coefficient(i);
                if (sel.select(c)) {
                    return c;
                } else {
                    return ring.coFac.getZERO();
                }
            }
        });
    }


    /**
     * Shift select coefficients. Not selected coefficients are removed from the
     * result series.
     * @param sel selector functor.
     * @return new power series with shifted selected coefficients.
     */
    public MultiVarPowerSeries<C> shiftSelect(final Selector<? super C> sel) {
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            ExpVectorIterable ib = new ExpVectorIterable(ring.nvar, true, truncate);


            Iterator<ExpVector> pos = ib.iterator();


            @Override
            public C generate(ExpVector i) {
                C c;
                if (i.signum() > 0) {
                    int[] deps = i.dependencyOnVariables();
                    ExpVector x = i.subst(deps[0], i.getVal(deps[0]) - 1L);
                    c = get(x);
                }
                do {
                    c = null;
                    if (pos.hasNext()) {
                        ExpVector e = pos.next();
                        c = coefficient(e);
                    } else {
                        break;
                    }
                } while (!sel.select(c));
                if (c == null) { // not correct because not known
                    c = ring.coFac.getZERO();
                }
                return c;
            }
        });
    }


    /**
     * Map a unary function to this power series.
     * @param f evaluation functor.
     * @return new power series with coefficients f(this(i)).
     */
    public MultiVarPowerSeries<C> map(final UnaryFunctor<? super C, C> f) {
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            @Override
            public C generate(ExpVector i) {
                return f.eval(coefficient(i));
            }
        });
    }


    /**
     * Map a binary function to this and another power series.
     * @param f evaluation functor with coefficients f(this(i),other(i)).
     * @param ps other power series.
     * @return new power series.
     */
    public MultiVarPowerSeries<C> zip(final BinaryFunctor<? super C, ? super C, C> f, 
                                      final MultiVarPowerSeries<C> ps) {
        int m = truncate();
        int n = ps.truncate();
        int pos = (m > n) ? m : n;
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            @Override
            public C generate(ExpVector i) {
                return f.eval(coefficient(i), ps.coefficient(i));
            }
        },m);
    }


    /**
     * Sum of two power series.
     * @param ps other power series.
     * @return this + ps.
     */
    public MultiVarPowerSeries<C> sum(MultiVarPowerSeries<C> ps) {
        return zip(new BinaryFunctor<C, C, C>() {


            @Override
            public C eval(C c1, C c2) {
                return c1.sum(c2);
            }
        }, ps);
    }


    /**
     * Subtraction of two power series.
     * @param ps other power series.
     * @return this - ps.
     */
    public MultiVarPowerSeries<C> subtract(MultiVarPowerSeries<C> ps) {
        return zip(new BinaryFunctor<C, C, C>() {


            @Override
            public C eval(C c1, C c2) {
                return c1.subtract(c2);
            }
        }, ps);
    }


    /**
     * Multiply by coefficient.
     * @param a coefficient.
     * @return this * a.
     */
    public MultiVarPowerSeries<C> multiply(final C a) {
        if (a.isZERO()) {
            return ring.getZERO();
        }
        if (a.isONE()) {
            return this;
        }
        return map(new UnaryFunctor<C, C>() {


            @Override
            public C eval(C c) {
                return c.multiply(a);
            }
        });
    }


    /**
     * Monic.
     * @return 1/orderCoeff() * this.
     */
    public MultiVarPowerSeries<C> monic() {
        ExpVector e = orderExpVector();
        C a = coefficient(e);
        if (a.isONE()) {
            return this;
        }
        if (a.isZERO()) { // sic
            return this;
        }
        final C b = a.inverse();
        return map(new UnaryFunctor<C, C>() {


            @Override
            public C eval(C c) {
                return b.multiply(c);
            }
        });
    }


    /**
     * Negate.
     * @return - this.
     */
    public MultiVarPowerSeries<C> negate() {
        return map(new UnaryFunctor<C, C>() {


            @Override
            public C eval(C c) {
                return c.negate();
            }
        });
    }


    /**
     * Absolute value.
     * @return abs(this).
     */
    public MultiVarPowerSeries<C> abs() {
        if (signum() < 0) {
            return negate();
        } else {
            return this;
        }
    }


    /**
     * Evaluate at given point.
     * @return ps(c).
     */
    public C evaluate(C e) {
        if (true)
            throw new UnsupportedOperationException("not implemented");
        C v = coefficient(ring.EVZERO);
        C p = e;
        for (ExpVector i : new ExpVectorIterable(ring.nvar, true, truncate)) {
            C c = coefficient(i).multiply(p);
            v = v.sum(c);
            p = p.multiply(e);
        }
        return v;
    }


    /**
     * Order.
     * @return index of first non zero coefficient.
     */
    public int order() {
        if (order < 0) { // compute it
            int ord = -1;
//             GenPolynomial<C> p = null;
//             long t = 0;
//             while ( true ) {
//              p = lazyCoeffs.getHomPart(t);
//                 if ( ! p.isZERO() ) {
//                     break;
//              }
//                 t++;
//          }
            ExpVector x = null;
            for (ExpVector i : new ExpVectorIterable(ring.nvar, true, truncate)) {
                ord++;
                if (!coefficient(i).isZERO()) {
                    order = ord;
                    evorder = i;
                    return order;
                }
                x = i;
            }
            order = truncate + 1;
            if (x != null) {
                evorder = x.subst(0, x.getVal(0) + 1L);
            } else {
                evorder = ring.EVZERO;
            }
        }
        return order;
    }


    /**
     * Order ExpVector.
     * @return ExpVector of first non zero coefficient.
     */
    public ExpVector orderExpVector() {
        int x = order(); // ensure evorder is set
        return evorder;
    }


    /**
     * Truncate.
     * @return truncate index of power series.
     */
    public int truncate() {
        return truncate;
    }


    /**
     * Set truncate.
     * @param t new truncate index.
     * @return old truncate index of power series.
     */
    public int setTruncate(int t) {
        if (t < 0) {
            throw new IllegalArgumentException("negative truncate not allowed");
        }
        int ot = truncate;
        truncate = t;
        return ot;
    }


    /**
     * Leading monomial.
     * @return first map entry or null.
     */
    public Map.Entry<ExpVector, C> orderMonomial() {
        ExpVector e = orderExpVector();
        return new AbstractMap.SimpleImmutableEntry<ExpVector, C>(e, coefficient(e));
    }


    /**
     * Ecart.
     * @return ecart.
     */
    public long ecart() {
        boolean b = this.isZERO();
        ExpVector e = orderExpVector();
        long d = e.totalDeg();
        long hd = d;
        for (long i = d + 1L; i <= truncate; i++) {
            if (!homogeneousPart(i).isZERO()) {
                hd = i;
            }
        }
        //System.out.println("d = " + d + ", hd = " + hd + ", coeffCache = " + lazyCoeffs.coeffCache + ", this = " + this);
        return hd - d;
    }


    /**
     * Signum.
     * @return sign of first non zero coefficient.
     */
    public int signum() {
        int i = order(); // ensure evorder is defined
        if (evorder != null) {
            return coefficient(evorder).signum();
        } else {
            return 0;
        }
    }


    /**
     * Compare to. <b>Note: </b> compare only up to max(truncates).
     * @return sign of first non zero coefficient of this-ps.
     */
    //JAVA6only: @Override
    public int compareTo(MultiVarPowerSeries<C> ps) {
        final int m = truncate();
        final int n = ps.truncate();
        final int pos = Math.min(m,n);
        int s = 0;
        //System.out.println("coeffCache_c1 = " + lazyCoeffs.coeffCache);
        //System.out.println("coeffCache_c2 = " + ps.lazyCoeffs.coeffCache);
        // test homogeneous parts first is slower
        for (ExpVector i : new ExpVectorIterable(ring.nvar, true, pos)) { 
            s = coefficient(i).compareTo(ps.coefficient(i));
            if (s != 0) {
                //System.out.println("i = " + i + ", coeff = " + coefficient(i) + ", ps.coeff = " + ps.coefficient(i));
                return s;
            }
        }
        if ( pos == Math.max(m,n) ) {
            return s;
        }
        for ( int j = pos+1; j < pos+2; j++ ) { // Math.max(m,n)
            for (ExpVector i : new ExpVectorIterable(ring.nvar, j)) { 
                s = coefficient(i).compareTo(ps.coefficient(i));
                //System.out.println("i = " + i + ", coeff = " + coefficient(i) + ", ps.coeff = " + ps.coefficient(i));
                if (s != 0) {
                    //System.out.println("i = " + i + ", coeff = " + coefficient(i) + ", ps.coeff = " + ps.coefficient(i));
                    return s;
                }
            }
        }
        return s;
    }


    /**
     * Is power series zero. <b>Note: </b> compare only up to truncate.
     * @return If this is 0 then true is returned, else false.
     * @see edu.jas.structure.RingElem#isZERO()
     */
    public boolean isZERO() {
        return (signum() == 0); 
    }


    /**
     * Is power series one. <b>Note: </b> compare only up to truncate.
     * @return If this is 1 then true is returned, else false.
     * @see edu.jas.structure.RingElem#isONE()
     */
    public boolean isONE() {
        if ( !leadingCoefficient().isONE() ) {
            return false;
        }
        return (compareTo(ring.ONE) == 0);
        //return reductum().isZERO();
    }


    /**
     * Comparison with any other object. <b>Note: </b> compare only up to
     * truncate.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object B) {
        if (!(B instanceof MultiVarPowerSeries)) {
            return false;
        }
        MultiVarPowerSeries<C> a = null;
        try {
            a = (MultiVarPowerSeries<C>) B;
        } catch (ClassCastException ignored) {
        }
        if (a == null) {
            return false;
        }
        return compareTo(a) == 0;
    }


    /**
     * Hash code for this polynomial. <b>Note: </b> only up to truncate.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int h = 0;
        //h = ( ring.hashCode() << 23 );
        //h += val.hashCode();
        for (ExpVector i : new ExpVectorIterable(ring.nvar, true, truncate)) {
            // h += i.hashCode(); h = ( h << 23);
            h += coefficient(i).hashCode();
            h = (h << 23);
        };
        return h;
    }


    /**
     * Is unit.
     * @return true, if this power series is invertible, else false.
     */
    public boolean isUnit() {
        return leadingCoefficient().isUnit();
    }


    /**
     * Multiply by another power series.
     * @return this * ps.
     */
    public MultiVarPowerSeries<C> multiply(final MultiVarPowerSeries<C> ps) {
        //final MultiVarPowerSeries<C> ps1 = this; // method name was ambiguous in generate
        int nt = truncate() + ps.truncate();
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            @Override
            public C generate(ExpVector e) {
                long tdeg = e.totalDeg();
                // generate respective homogeneous polynomial
                GenPolynomial<C> p = null; //fac.getZERO();
                for (int k = 0; k <= tdeg; k++) {
                    GenPolynomial<C> m = homogeneousPart(k).multiply(ps.homogeneousPart(tdeg - k));
                    if (p == null) {
                        p = m;
                    } else {
                        p = p.sum(m);
                    }
                }
                coeffCache.put(tdeg, p); // overwrite
                C c = p.coefficient(e);
                return c;
            }
        },nt);
    }


    /**
     * Inverse power series.
     * @return ps with this * ps = 1.
     */
    public MultiVarPowerSeries<C> inverse() {
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            @Override
            public C generate(ExpVector e) {
                long tdeg = e.totalDeg();
                C d = leadingCoefficient().inverse(); // may fail
                if (tdeg == 0) {
                    return d;
                }
                GenPolynomial<C> p = null; //fac.getZERO();
                for (int k = 0; k < tdeg; k++) {
                    GenPolynomial<C> m = getHomPart(k).multiply(homogeneousPart(tdeg - k));
                    if (p == null) {
                        p = m;
                    } else {
                        p = p.sum(m);
                    }
                }
                p = p.multiply(d.negate());
                //System.out.println("tdeg = " + tdeg + ", p = " + p);
                coeffCache.put(tdeg, p); // overwrite
                C c = p.coefficient(e);
                return c;
            }
        });
    }


    /**
     * Divide by another power series.
     * @return this / ps.
     */
    public MultiVarPowerSeries<C> divide(MultiVarPowerSeries<C> ps) {
        if (ps.isUnit()) {
            return multiply(ps.inverse());
        }
        int m = order();
        int n = ps.order();
        if (m < n) {
            return ring.getZERO();
        }
        ExpVector em = orderExpVector();
        ExpVector en = ps.orderExpVector();
        if (!ps.coefficient(en).isUnit()) {
            throw new ArithmeticException("division by non unit coefficient " + ps.coefficient(ps.evorder)
                    + ", evorder = " + ps.evorder);
        }
        // now m >= n
        MultiVarPowerSeries<C> st, sps, q, sq;
        if (m == 0) {
            st = this;
        } else {
            st = this.shift(em.negate());
        }
        if (n == 0) {
            sps = ps;
        } else {
            sps = ps.shift(en.negate());
        }
        q = st.multiply(sps.inverse());
        sq = q.shift(em.subtract(en));
        return sq;
    }


    /**
     * Power series remainder.
     * @param ps nonzero power series with invertible leading coefficient.
     * @return remainder with this = quotient * ps + remainder.
     */
    public MultiVarPowerSeries<C> remainder(MultiVarPowerSeries<C> ps) {
        int m = order();
        int n = ps.order();
        if (m >= n) {
            return ring.getZERO();
        }
        return this;
    }


    /**
     * Differentiate with respect to variable r.
     * @param r variable for the direction.
     * @return differentiate(this).
     */
    public MultiVarPowerSeries<C> differentiate(final int r) {
        if (r < 0 || ring.nvar < r) {
            throw new IllegalArgumentException("variable index out of bound");
        }
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            @Override
            public C generate(ExpVector i) {
                long d = i.getVal(r);
                ExpVector e = i.subst(r, d + 1);
                C v = coefficient(e);
                v = v.multiply(ring.coFac.fromInteger(d + 1));
                return v;
            }
        });
    }


    /**
     * Integrate with respect to variable r and with given constant.
     * @param c integration constant.
     * @param r variable for the direction.
     * @return integrate(this).
     */
    public MultiVarPowerSeries<C> integrate(final C c, final int r) {
        if (r < 0 || ring.nvar < r) {
            throw new IllegalArgumentException("variable index out of bound");
        }
        int nt = truncate; // + 1;
        return new MultiVarPowerSeries<C>(ring, new MultiVarCoefficients<C>(ring) {


            @Override
            public C generate(ExpVector i) {
                if (i.isZERO()) {
                    return c;
                }
                long d = i.getVal(r);
                if (d > 0) {
                    ExpVector e = i.subst(r, d - 1);
                    C v = coefficient(e);
                    v = v.divide(ring.coFac.fromInteger(d));
                    return v;
                } else {
                    return ring.coFac.getZERO();
                }
            }
        },nt);
    }


    /**
     * Power series greatest common divisor.
     * @param ps power series.
     * @return gcd(this,ps).
     */
    public MultiVarPowerSeries<C> gcd(MultiVarPowerSeries<C> ps) {
        if (ps.isZERO()) {
            return this;
        }
        if (this.isZERO()) {
            return ps;
        }
        ExpVector em = orderExpVector();
        ExpVector en = ps.orderExpVector();
        return ring.getONE().shift(em.gcd(en));
    }


    /**
     * Power series extended greatest common divisor. <b>Note:</b> not
     * implemented.
     * @param S power series.
     * @return [ gcd(this,S), a, b ] with a*this + b*S = gcd(this,S).
     */
    //SuppressWarnings("unchecked")
    public MultiVarPowerSeries<C>[] egcd(MultiVarPowerSeries<C> S) {
        throw new UnsupportedOperationException("egcd for power series not implemented");
    }

}
