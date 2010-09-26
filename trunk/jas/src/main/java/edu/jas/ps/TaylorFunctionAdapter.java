/*
 * $Id: TaylorFunctionAdapter.java 3332 2010-09-26 16:43:23Z kredel $
 */

package edu.jas.ps;


import java.util.List;


import edu.jas.structure.RingElem;
import edu.jas.poly.ExpVector;


/**
 * Adapter for functions capable for Taylor series expansion.
 * @param <C> ring element type
 * @author Heinz Kredel
 */

public abstract class TaylorFunctionAdapter<C extends RingElem<C>> implements TaylorFunction<C> {


    /**
     * Get the faculty coefficient.
     * @return faculty coefficient.
     */
    @Override
    public long getFacul() {
        return 1L;
    }


    /**
     * Deriviative.
     * @return deriviative of this.
     */
    public TaylorFunction<C> deriviative() {
        throw new UnsupportedOperationException("not implemented");
    }



    /**
     * Partial deriviative.
     * @param r index of the variable.
     * @return partial deriviative of this with respect to variable r.
     */
    public TaylorFunction<C> deriviative(int r) {
        throw new UnsupportedOperationException("not implemented");
    }


    /**
     * Multi-partial deriviative.
     * @param i exponent vector.
     * @return partial deriviative of this with respect to all variables.
     */
    public TaylorFunction<C> deriviative(ExpVector i) {
        throw new UnsupportedOperationException("not implemented");
    }


    /**
     * Evaluate.
     * @param a element.
     * @return this(a).
     */
    public C evaluate(C a){
        throw new UnsupportedOperationException("not implemented");
    }


    /**
     * Evaluate at a given variable.
     * @param a element.
     * @param r index of the variable.
     * @return this_r(a).
     */
    public TaylorFunction<C> evaluate(C a, int r) {
        throw new UnsupportedOperationException("not implemented");
    }


    /**
     * Evaluate at a tuple of elements.
     * @param a tuple of elements.
     * @return this(a).
     */
    public C evaluate(List<C> a) {
        throw new UnsupportedOperationException("not implemented");
    }

}
