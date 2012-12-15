package org.matheclipse.core.expression;

import org.apache.commons.math3.complex.Complex;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.interfaces.IComplexNum;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.visit.IVisitor;
import org.matheclipse.core.visit.IVisitorBoolean;
import org.matheclipse.core.visit.IVisitorInt;

/**
 * 
 */
public class ComplexNum extends ExprImpl implements IComplexNum {

	// private static final ObjectFactory<ComplexNum> FACTORY = new
	// ObjectFactory<ComplexNum>() {
	// @Override
	// protected ComplexNum create() {
	// if (Config.SERVER_MODE && currentQueue().getSize() >=
	// Config.DOUBLECOMPLEX_MAX_POOL_SIZE) {
	// throw new PoolMemoryExceededException("DoubleComplex",
	// currentQueue().getSize());
	// }
	// return new ComplexNum(0.0, 0.0);
	// }
	// };

	/**
	 * Be cautious with this method, no new internal couble complex is created
	 * 
	 * @param numerator
	 * @return
	 */
	protected static ComplexNum newInstance(final Complex value) {
		// ComplexNum d;
		// if (Config.SERVER_MODE) {
		// d = FACTORY.object();
		// } else {
		// d = new ComplexNum(0.0, 0.0);
		// }
		ComplexNum d = new ComplexNum(0.0, 0.0);
		d.fComplex = value;
		return d;
	}

	public static ComplexNum valueOf(final INum d) {
		return newInstance(new Complex(d.getRealPart(), 0.0));
	}

	public static ComplexNum valueOf(final double real) {
		return newInstance(new Complex(real, 0.0));
	}

	public static ComplexNum valueOf(final double real, final double imaginary) {
		return newInstance(new Complex(real, imaginary));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6033055105824482264L;

	/** The square root of -1. A number representing "0.0 + 1.0i" */
	public static final ComplexNum I = valueOf(0.0, 1.0);

	/** A complex number representing "NaN + NaNi" */
	public static final ComplexNum NaN = valueOf(Double.NaN, Double.NaN);

	/** A complex number representing "1.0 + 0.0i" */
	public static final ComplexNum ONE = valueOf(1.0, 0.0);

	/** A complex number representing "0.0 + 0.0i" */
	public static final ComplexNum ZERO = valueOf(0.0, 0.0);

	Complex fComplex;

	private ComplexNum(final double r, final double i) {
		fComplex = new Complex(r, i);
	}

	/**
	 * @return
	 */
	public double getImaginaryPart() {
		double temp = fComplex.getImaginary();
		if (temp == (-0.0)) {
			temp = 0.0;
		}
		return temp;
	}

	/**
	 * @return
	 */
	public double getRealPart() {
		double temp = fComplex.getReal();
		if (temp == (-0.0)) {
			temp = 0.0;
		}
		return temp;
	}

	public boolean isZero() {
		return (fComplex.getReal() == 0.0) && (fComplex.getImaginary() == 0.0);
	}

	public int hierarchy() {
		return DOUBLECOMPLEXID;
	}

	public IComplexNum add(final IComplexNum val) {
		return newInstance(fComplex.add(((ComplexNum) val).fComplex));
	}

	public ComplexNum add(final ComplexNum that) {
		return newInstance(fComplex.add(that.fComplex));
	}

	public IComplexNum multiply(final IComplexNum val) {
		return newInstance(fComplex.multiply(((ComplexNum) val).fComplex));
	}

	public IComplexNum pow(final IComplexNum val) {
		return newInstance(fComplex.pow(((ComplexNum) val).fComplex));
	}

	/**
	 * @param that
	 * @return
	 */
	public Complex add(final Complex that) {
		return fComplex.add(that);
	}

	/**
	 * @return
	 */
	public IComplexNum conjugate() {
		return newInstance(fComplex.conjugate());
	}

	/**
	 * @param that
	 * @return
	 */
	public Complex divide(final Complex that) {
		return fComplex.divide(that);
	}

	public ComplexNum divide(final ComplexNum that) throws ArithmeticException {
		return newInstance(fComplex.divide(that.fComplex));
	}

	/**
	 * @param k
	 * @return
	 */
	// public Complex divide(final double k) {
	// return fComplex.divide(k);
	// }
	/**
	 * @return
	 */
	// public double doubleValue() {
	// return fComplex.doubleValue();
	// }
	/**
	 * @param that
	 * @param tolerance
	 * @return
	 */
	// public boolean equals(final Complex that, final double tolerance) {
	// return fComplex.equals(that, tolerance);
	// }
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof ComplexNum) {
			return fComplex.equals(((ComplexNum) obj).fComplex);
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public IExpr evaluate(EvalEngine engine){
		if (F.isZero(getImaginaryPart())) {
			return F.num(getRealPart());
		}
		return null;
	}
	
	public boolean isSame(IExpr expression, double epsilon) {
		if (expression instanceof ComplexNum) {
			return F.isZero(fComplex.getReal() - ((ComplexNum) expression).fComplex.getReal(), epsilon)
					&& F.isZero(fComplex.getImaginary() - ((ComplexNum) expression).fComplex.getImaginary(), epsilon);
			// return Math.abs(fComplex.getReal() - ((ComplexNum)
			// expression).fComplex.getReal()) < epsilon
			// && Math.abs(fComplex.getImaginary() - ((ComplexNum)
			// expression).fComplex.getImaginary()) < epsilon;
		}
		return false;
	}

	/**
	 * Return the absolute value of this complex number.
	 * <p>
	 * Returns <code>NaN</code> if either real or imaginary part is
	 * <code>NaN</code> and <code>Double.POSITIVE_INFINITY</code> if neither part
	 * is <code>NaN</code>, but at least one part takes an infinite value.
	 * 
	 * @return the absolute value
	 */
	public double dabs() {
		if (isNaN()) {
			return Double.NaN;
		}

		if (isInfinite()) {
			return Double.POSITIVE_INFINITY;
		}

		if (Math.abs(getReal()) < Math.abs(getImaginary())) {
			if (getImaginary() == 0.0) {
				return Math.abs(getReal());
			}
			final double q = getReal() / getImaginary();
			return (Math.abs(getImaginary()) * Math.sqrt(1 + q * q));
		} else {
			if (getReal() == 0.0) {
				return Math.abs(getImaginary());
			}
			final double q = getImaginary() / getReal();
			return (Math.abs(getReal()) * Math.sqrt(1 + q * q));
		}
	}

	public Num eabs() {
		return Num.valueOf(dabs());
	}

	/**
	 * @return
	 */
	// public Complex exp() {
	// return fComplex.exp();
	// }
	/**
	 * @return
	 */
	// public Object export() {
	// return fComplex.export();
	// }
	/**
	 * @return
	 */
	// public float floatValue() {
	// return fComplex.floatValue();
	// }
	/**
	 * @return
	 */
	public double getImaginary() {
		return fComplex.getImaginary();
	}

	/**
	 * @return
	 */
	public double getReal() {
		return fComplex.getReal();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return fComplex.hashCode();
	}

	/**
	 * @return
	 */
	// public int intValue() {
	// return fComplex.intValue();
	// }
	/**
	 * @return
	 */
	public boolean isInfinite() {
		return fComplex.isInfinite();
	}

	/**
	 * @return
	 */
	public boolean isNaN() {
		return fComplex.isNaN();
	}

	/**
	 * @return
	 */
	// public Complex log() {
	// return fComplex.log();
	// }
	/**
	 * @return
	 */
	// public long longValue() {
	// return fComplex.longValue();
	// }
	/**
	 * @return
	 */
	// public double magnitude() {
	// return fComplex.magnitude();
	// }
	/**
	 * @param cs
	 */
	// @Override
	// public boolean move(final ObjectSpace cs) {
	// fComplex.move(cs);
	// return true;
	// }
	// public ComplexNum copy() {
	// // ComplexNum d;
	// // if (Config.SERVER_MODE) {
	// // d = FACTORY.object();
	// // } else {
	// // d = new ComplexNum(0.0, 0.0);
	// // }
	// ComplexNum d = new ComplexNum(0.0, 0.0);
	// d.fComplex = fComplex.copy();
	// return d;
	// }
	//
	// public ComplexNum copyNew() {
	// ComplexNum dci = new ComplexNum(0.0, 0.0);
	// dci.fComplex = new Complex(fComplex.getReal(), fComplex.getImaginary());
	// return dci;
	// }

	// public void recycle() {
	// FACTORY.recycle(this);
	// }

	/**
	 * @param that
	 * @return
	 */
	public ComplexNum multiply(final ComplexNum that) {
		return newInstance(fComplex.multiply(that.fComplex));
	}

	/**
	 * @return
	 */
	public ComplexNum negate() {
		return newInstance(fComplex.negate());
	}

	/**
	 * @return
	 */
	@Override
	public IExpr opposite() {
		return newInstance(fComplex.negate());
	}

	/**
	 * @param that
	 * @return
	 */
	@Override
	public IExpr plus(final IExpr that) {
		if (that instanceof ComplexNum) {
			return newInstance(fComplex.add(((ComplexNum) that).fComplex));
		}
		return super.plus(that);
	}

	/**
	 * @param that
	 * @return
	 */
	// public Complex pow(final Complex that) {
	// return fComplex.pow(that);
	// }
	/**
	 * @param e
	 * @return
	 */
	// public Complex pow(final double e) {
	// return fComplex.pow(e);
	// }
	/**
	 * @param exp
	 * @return
	 */
	// public Complex pow(final int exp) {
	// return fComplex.pow(exp);
	// }
	/**
	 * @param isPreserved
	 * @return
	 */
	// public Object preserve(boolean isPreserved) {
	// return fComplex.preserve(isPreserved);
	// }
	@Override
	public IExpr inverse() {
		// return new DoubleComplexImpl(fComplex.inverse());
		final double tmp = (fComplex.getReal() * fComplex.getReal()) + (fComplex.getImaginary() * fComplex.getImaginary());
		return valueOf(fComplex.getReal() / tmp, -fComplex.getImaginary() / tmp);
	}

	/**
	 * @return
	 */
	// public short shortValue() {
	// return fComplex.shortValue();
	// }
	/**
	 * @return
	 */
	// public Complex sqrt() {
	// return fComplex.sqrt();
	// }
	/**
	 * @param that
	 * @return
	 */
	public Complex subtract(final Complex that) {
		return fComplex.subtract(that);
	}

	public ComplexNum subtract(final ComplexNum that) {
		return newInstance(fComplex.subtract(that.fComplex));
	}

	/**
	 * @param that
	 * @return
	 */
	@Override
	public IExpr times(final IExpr that) {
		if (that instanceof ComplexNum) {
			return newInstance(fComplex.multiply(((ComplexNum) that).fComplex));
		}
		return super.times(that);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	// public String toString() {
	// return fComplex.toString();
	// }
	/**
	 * @return
	 */
	// @Override
	// public Text toText() {
	// return fComplex.toText();
	// }
	public int complexSign() {
		final int i = (int) Math.signum(fComplex.getReal());
		if (i == 0) {
			return (int) Math.signum(fComplex.getImaginary());
		}
		return i;
	}

	public int compareTo(final Complex that) {
		if (fComplex.getReal() < that.getReal()) {
			return -1;
		}
		if (fComplex.getReal() > that.getReal()) {
			return 1;
		}
		long l1 = Double.doubleToLongBits(fComplex.getReal());
		long l2 = Double.doubleToLongBits(that.getReal());
		if (l1 < l2) {
			return -1;
		}
		if (l2 > l1) {
			return 1;
		}
		if (fComplex.getImaginary() < that.getImaginary()) {
			return -1;
		}
		if (fComplex.getImaginary() > that.getImaginary()) {
			return 1;
		}
		l1 = Double.doubleToLongBits(fComplex.getImaginary());
		l2 = Double.doubleToLongBits(that.getImaginary());
		if (l1 < l2) {
			return -1;
		}
		if (l2 > l1) {
			return 1;
		}
		return 0;
	}

	/**
	 * Compares this expression with the specified expression for order. Returns a
	 * negative integer, zero, or a positive integer as this expression is
	 * canonical less than, equal to, or greater than the specified expression.
	 */
	public int compareTo(final IExpr obj) {
		if (obj instanceof ComplexNum) {
			return compareTo(((ComplexNum) obj).fComplex);
			// return fComplex.compareTo(((DoubleComplexImpl) obj).fComplex);
		}
		return (hierarchy() - (obj).hierarchy());
	}

	public ISymbol head() {
		return F.ComplexHead;
	}

	public Complex getComplex() {
		return fComplex;
	}

	public org.matheclipse.parser.client.math.Complex getCMComplex() {
		return new org.matheclipse.parser.client.math.Complex(fComplex.getReal(), fComplex.getImaginary());
	}

	/** {@inheritDoc} */
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}

	/** {@inheritDoc} */
	public boolean accept(IVisitorBoolean visitor) {
		return visitor.visit(this);
	}

	/** {@inheritDoc} */
	public int accept(IVisitorInt visitor) {
		return visitor.visit(this);
	}
	
	@Override
	public boolean equalsInt(int i) {
		return false;
	}
}