package org.matheclipse.core.expression;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.FractionConversionException;
import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.visit.IVisitor;
import org.matheclipse.core.visit.IVisitorBoolean;
import org.matheclipse.core.visit.IVisitorInt;

/**
 * 
 */
public class FractionSym extends ExprImpl implements IFraction {

	/**
	 * Be cautious with this method, no new internal rational is created
	 * 
	 * @param numerator
	 * @return
	 */
	protected static FractionSym newInstance(final BigFraction rational) {
		FractionSym r = new FractionSym();
		r.fRational = rational;
		return r;
	}

	public static FractionSym valueOf(final BigInteger numerator) {
		FractionSym r = new FractionSym();
		r.fRational = new BigFraction(numerator, BigInteger.ONE);
		return r;
	}

	/**
	 * 
	 * @param rat
	 * @return
	 * 
	 * @deprecated
	 */
	public static FractionSym valueOf(final BigFraction rat) {
		FractionSym r = new FractionSym();
		r.fRational = new BigFraction(rat.getNumerator(), rat.getDenominator());
		return r;
	}

	public static FractionSym valueOf(final BigInteger numerator, final BigInteger denominator) {
		FractionSym r = new FractionSym();
		r.fRational = new BigFraction(numerator, denominator);
		return r;
	}

	public static FractionSym valueOf(final IInteger numerator, final IInteger denominator) {
		FractionSym r = new FractionSym();
		r.fRational = new BigFraction(numerator.getBigNumerator(), denominator.getBigNumerator());
		return r;
	}

	public static FractionSym valueOf(final long numerator, final long denominator) {
		FractionSym r = new FractionSym();
		r.fRational = new BigFraction(numerator, denominator);
		return r;
	}

	public static FractionSym valueOf(final double value) {
		FractionSym r = new FractionSym();
		try {
			r.fRational = new BigFraction(value, Config.DOUBLE_EPSILON, 200);
		} catch (FractionConversionException e) {
			r.fRational = new BigFraction(value);
		}
		return r;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2396715994276842438L;

	/* package private */BigFraction fRational;

	private FractionSym() {
		fRational = null;
	}

	/** {@inheritDoc} */
	public boolean isZero() {
		return fRational.getNumerator().equals(BigInteger.ZERO);
	}

	/** {@inheritDoc} */
	public boolean equalsInt(final int i) {
		return fRational.getNumerator().equals(BigInteger.valueOf(i)) && fRational.getDenominator().equals(BigInteger.ONE);
	}

	/**
	 * Returns the denominator of this fraction.
	 * 
	 * @return denominator
	 */
	public BigInteger getBigDenominator() {
		return fRational.getDenominator();
	}

	/**
	 * Returns the numerator of this Rational.
	 * 
	 * @return numerator
	 */
	public BigInteger getBigNumerator() {
		return fRational.getNumerator();
	}

	/**
	 * Returns the denominator of this fraction.
	 * 
	 * @return denominator
	 */
	public IInteger getDenominator() {
		return IntegerSym.valueOf(fRational.getDenominator());
	}

	/**
	 * Returns the numerator of this Rational.
	 * 
	 * @return numerator
	 */
	public IInteger getNumerator() {
		return IntegerSym.valueOf(fRational.getNumerator());
	}

	/** {@inheritDoc} */
	public int hierarchy() {
		return FRACTIONID;
	}

	public IFraction add(final IFraction parm1) {
		return newInstance(fRational.add(((FractionSym) parm1).fRational));
	}

	public IFraction multiply(final IFraction parm1) {
		return newInstance(fRational.multiply(((FractionSym) parm1).fRational));
	}

	public boolean isNegative() {
		return (fRational.getNumerator().compareTo(BigInteger.ZERO) == -1);
	}

	public boolean isPositive() {
		return (fRational.getNumerator().compareTo(BigInteger.ZERO) == 1);
	}

	/** {@inheritDoc} */
	public FractionSym eabs() {
		return newInstance(fRational.abs());
	}

	/**
	 * @param that
	 * @return
	 */
	public BigFraction add(final BigFraction that) {
		return fRational.add(that);
	}

	/**
	 * @param that
	 * @return
	 */
	public BigFraction divide(final BigFraction that) {
		return fRational.divide(that);
	}

	/**
	 * @return
	 */
	public double doubleValue() {
		return fRational.doubleValue();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof FractionSym) {
			return fRational.equals(((FractionSym) obj).fRational);
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public IExpr evaluate(EvalEngine engine) {
		if (engine.isNumericMode()) {
			return F.num(this);
		}
		if (getBigDenominator().equals(BigInteger.ONE)) {
			return F.integer(getBigNumerator());
		}
		return null;
	}

	/**
	 * @return
	 */
	public BigInteger getDividend() {
		return fRational.getNumerator();
	}

	/**
	 * @return
	 */
	public BigInteger getDivisor() {
		return fRational.getDenominator();
	}

	@Override
	public int hashCode() {
		return fRational.hashCode();
	}

	/**
	 * @return
	 */
	public long longValue() {
		return fRational.longValue();
	}

	/**
	 * @param that
	 * @return
	 */
	public BigFraction multiply(final BigFraction that) {
		return fRational.multiply(that);
	}

	/**
	 * @return
	 */
	public ISignedNumber negate() {
		return newInstance(fRational.negate());
	}

	/**
	 * @return
	 */
	@Override
	public IExpr opposite() {
		return newInstance(fRational.negate());
	}

	/**
	 * @param that
	 * @return
	 */
	@Override
	public IExpr plus(final IExpr that) {
		if (that instanceof FractionSym) {
			return this.add((FractionSym) that);
		}
		if (that instanceof IntegerSym) {
			return this.add(valueOf(((IntegerSym) that).fInteger));
		}
		return super.plus(that);
	}

	/** {@inheritDoc} */
	public ISignedNumber minus(ISignedNumber that) {
		if (that instanceof FractionSym) {
			return this.add((FractionSym) that.negate());
		}
		if (isZero()) {
			return that.negate();
		}
		if (that instanceof IntegerSym) {
			return this.minus(valueOf(((IntegerSym) that).fInteger));
		}
		return Num.valueOf(doubleValue() - that.doubleValue());
	}

	/**
	 * Returns this number raised at the specified positive exponent.
	 * 
	 * @param exp
	 *          the positive exponent.
	 * @return <code>this<sup>exp</sup></code>
	 * @throws IllegalArgumentException
	 *           if <code>exp &lt;= 0</code>
	 */
	public IFraction pow(int exp) {
		if (exp <= 0)
			throw new IllegalArgumentException("exp: " + exp + " should be a positive number");
		IFraction powSqr = this;
		IFraction result = null;
		while (exp >= 1) { // Iteration.
			if ((exp & 1) == 1) {
				result = (result == null) ? powSqr : result.multiply(powSqr);
			}
			powSqr = powSqr.multiply(powSqr);
			exp >>>= 1;
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public IExpr inverse() {
		return newInstance(NumberUtil.inverse(fRational));
	}

	/**
	 * @param that
	 * @return
	 */
	public BigFraction subtract(final BigFraction that) {
		return fRational.subtract(that);
	}

	/** {@inheritDoc} */
	@Override
	public IExpr times(final IExpr that) {
		if (that instanceof FractionSym) {
			return this.multiply((FractionSym) that);
		}
		if (that instanceof IntegerSym) {
			return this.multiply(valueOf(((IntegerSym) that).fInteger));
		}
		return super.times(that);
	}

	/** {@inheritDoc} */
	public String internalFormString(boolean symbolsAsFactoryMethod, int depth) {
		int numerator = fRational.getNumerator().intValue();
		int denominator = fRational.getDenominator().intValue();
		if (numerator == 1) {
			switch (denominator) {
			case 2:
				return "C1D2";
			case 3:
				return "C1D3";
			case 4:
				return "C1D4";
			}
		}
		if (numerator == -1) {
			switch (denominator) {
			case 2:
				return "CN1D2";
			case 3:
				return "CN1D3";
			case 4:
				return "CN1D4";
			}
		}
		return "fraction(" + numerator + "L," + denominator + "L)";
	}

	@Override
	public String toString() {
		return fRational.getNumerator().toString() + "/" + fRational.getDenominator().toString();
	}

	@Override
	public String fullFormString() {
		StringBuffer buf = new StringBuffer("Rational[");
		buf.append(fRational.getNumerator().toString().toString());
		buf.append(',');
		buf.append(fRational.getDenominator().toString().toString());
		buf.append(']');
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.matheclipse.parser.interfaces.IFraction#getRational()
	 */
	public BigFraction getRational() {
		return fRational;
	}

	/** {@inheritDoc} */
	public int sign() {
		return fRational.getNumerator().signum();
	}

	/** {@inheritDoc} */
	public int complexSign() {
		return sign();
	}

	/** {@inheritDoc} */
	public ISignedNumber ceil() {
		return IntegerSym.valueOf(NumberUtil.ceiling(fRational));
	}

	/** {@inheritDoc} */
	public ISignedNumber floor() {
		return IntegerSym.valueOf(NumberUtil.floor(fRational));
	}

	/** {@inheritDoc} */
	public ISignedNumber round() {
		return IntegerSym.valueOf(NumberUtil.round(fRational, BigDecimal.ROUND_HALF_EVEN));
	}

	/**
	 * Compares this expression with the specified expression for order. Returns a
	 * negative integer, zero, or a positive integer as this expression is
	 * canonical less than, equal to, or greater than the specified expression.
	 */
	public int compareTo(final IExpr obj) {
		if (obj instanceof FractionSym) {
			return fRational.compareTo(((FractionSym) obj).fRational);
		}
		if (obj instanceof IntegerSym) {
			return fRational.compareTo(new BigFraction(((IntegerSym) obj).fInteger, BigInteger.ONE));
		}
		return (hierarchy() - (obj).hierarchy());
	}

	public boolean isLessThan(ISignedNumber obj) {
		if (obj instanceof FractionSym) {
			return fRational.compareTo(((FractionSym) obj).fRational) < 0;
		}
		if (obj instanceof IntegerSym) {
			return fRational.compareTo(new BigFraction(((IntegerSym) obj).fInteger, BigInteger.ONE)) < 0;
		}
		return fRational.doubleValue() < obj.doubleValue();
	}

	public boolean isGreaterThan(ISignedNumber obj) {
		if (obj instanceof FractionSym) {
			return fRational.compareTo(((FractionSym) obj).fRational) > 0;
		}
		if (obj instanceof IntegerSym) {
			return fRational.compareTo(new BigFraction(((IntegerSym) obj).fInteger, BigInteger.ONE)) > 0;
		}
		return fRational.doubleValue() < obj.doubleValue();
	}

	public ISymbol head() {
		return F.RationalHead;
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
}