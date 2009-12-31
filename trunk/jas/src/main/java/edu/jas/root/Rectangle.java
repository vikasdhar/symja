/*
 * $Id: Rectangle.java 2926 2009-12-26 08:26:07Z kredel $
 */

package edu.jas.root;


import edu.jas.arith.BigDecimal;
import edu.jas.arith.BigRational;
import edu.jas.structure.Complex;
import edu.jas.structure.ElemFactory;
import edu.jas.structure.RingElem;


/**
 * Rectangle. For example isolating rectangle for complex roots.
 * @param <C> coefficient type.
 * @author Heinz Kredel
 */
public class Rectangle<C extends RingElem<C>> {


    /**
     * rectangle corners.
     */
    public final Complex<C>[] corners;


    /**
     * Constructor.
     * @param c array of corners.
     */
    @SuppressWarnings("unchecked")
    public Rectangle(Complex<C>[] c) {
        if (c.length < 5) {
            corners = (Complex<C>[]) new Complex[5];
            for (int i = 0; i < 4; i++) {
                corners[i] = c[i];
            }
        } else {
            corners = c;
        }
        if (corners[4] == null) {
            corners[4] = corners[0];
        }
    }


    /**
     * Constructor.
     * @param nw corner.
     * @param sw corner.
     * @param se corner.
     * @param ne corner.
     */
    @SuppressWarnings("unchecked")
    public Rectangle(Complex<C> nw, Complex<C> sw, Complex<C> se, Complex<C> ne) {
        this( (Complex<C>[]) new Complex[] { nw, sw, se, ne } );
    }


    /**
     * String representation of Rectangle.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + corners[0] + ", " + corners[1] + ", " + corners[2] + ", " + corners[3] + "]";
        //return centerApprox() + " = [" + corners[0] + ", " + corners[1] + ", " + corners[2] + ", " + corners[3] + "]";
    }


    /**
     * Get a scripting compatible string representation.
     * @return script compatible representation for this Rectangle.
     */
    public String toScript() {
        // Python case
        return "(" + corners[0] + ", " + corners[1] + ", " + corners[2] + ", " + corners[3] + ")";
    }


    /**
     * Get north west corner.
     * @return north west corner of this rectangle.
     */
    public Complex<C> getNW() {
        return corners[0];
    }


    /**
     * Get south west corner.
     * @return south west corner of this rectangle.
     */
    public Complex<C> getSW() {
        return corners[1];
    }


    /**
     * Get south east corner.
     * @return south east corner of this rectangle.
     */
    public Complex<C> getSE() {
        return corners[2];
    }


    /**
     * Get north east corner.
     * @return north east corner of this rectangle.
     */
    public Complex<C> getNE() {
        return corners[3];
    }


    /**
     * Clone this.
     * @see java.lang.Object#clone()
     */
    @Override
    public Rectangle<C> clone() {
        return new Rectangle<C>(corners);
    }


    /**
     * Comparison with any other object.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object b) {
        if (!(b instanceof Rectangle)) {
            return false;
        }
        Rectangle<C> a = null;
        try {
            a = (Rectangle<C>) b;
        } catch (ClassCastException e) {
        }
        for (int i = 0; i < 4; i++) {
            if (!corners[i].equals(a.corners[i])) {
                return false;
            }
        }
        return true;
    }


    /**
     * Hash code for this Rectangle.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hc = 0;
        for (int i = 0; i < 3; i++) {
            hc += 37 * corners[i].hashCode();
        }
        return 37 * hc + corners[3].hashCode();
    }


    /**
     * Approximation of center.
     * @return r + i m as decimal approximation of the center.
     */
    public String centerApprox() {
        C r = corners[2].getRe().subtract(corners[1].getRe());
        C m = corners[0].getIm().subtract(corners[1].getIm());
        ElemFactory<C> rf = r.factory();
        C two = rf.fromInteger(2);
        r = r.divide(two);
        m = m.divide(two);
        r = corners[1].getRe().sum(r);
        m = corners[1].getIm().sum(m);

        BigRational rs = new BigRational(r.toString());
        //System.out.println("s  = " + s);
        BigDecimal rd = new BigDecimal(rs);

        BigRational ms = new BigRational(m.toString());
        //System.out.println("m  = " + m);
        BigDecimal md = new BigDecimal(ms);

        StringBuffer s = new StringBuffer();
        s.append("[ ");
        s.append(rd.toString());
        s.append(" i ");
        s.append(md.toString());
        s.append(" ]");
        return s.toString();
    }


    /**
     * Length.
     * @return |ne-sw|**2;
     */
    public C length() {
        Complex<C> m = corners[3].subtract(corners[1]);
        return m.norm().getRe();
    }


    /**
     * Rational Length.
     * @return rational(|ne-sw|**2);
     */
    public BigRational rationalLength() {
        BigRational r = new BigRational(length().toString());
        return r;
    }

}
