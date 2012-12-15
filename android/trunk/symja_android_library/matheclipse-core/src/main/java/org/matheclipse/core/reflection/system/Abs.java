package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.eval.interfaces.INumeric;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.generic.interfaces.INumericFunction;
import org.matheclipse.parser.client.SyntaxError;

import com.google.common.base.Function;

/**
 * Absolute value of a number. See <a
 * href="http://en.wikipedia.org/wiki/Absolute_value">Wikipedia:Absolute
 * value</a>
 */
public class Abs extends AbstractTrigArg1 implements INumeric {

	private final class AbsTimesFunction implements Function<IExpr, IExpr> {
		public IExpr apply(IExpr expr) {
			if (expr.isNumber()) {
				return ((INumber) expr).eabs();
			}
			IExpr temp = F.eval(F.Abs(expr));
			if (!temp.topHead().equals(F.Abs)) {
				return temp;
			}
			return null;
		}
	}

	private final class AbsNumericFunction implements INumericFunction<IExpr> {
		final ISymbol symbol;

		public AbsNumericFunction(ISymbol symbol) {
			this.symbol = symbol;
		}

		public IExpr apply(double value) {
			if (value < Integer.MAX_VALUE && value > Integer.MIN_VALUE) {
				double result = Math.abs(value);
				if (result > 0.0) {
					return symbol;
				}
			}
			return null;
		}
	}

	public Abs() {
	}

	public double evalReal(final double[] stack, final int top, final int size) {
		if (size != 1) {
			throw new UnsupportedOperationException();
		}
		return Math.abs(stack[top]);
	}

	public IExpr evaluateArg1(final IExpr arg1) {
		if (arg1.isNumber()) {
			return ((INumber) arg1).eabs();
		}
		if (arg1.isSymbol()) {
			ISymbol sym = (ISymbol) arg1;
			return sym.mapConstantDouble(new AbsNumericFunction(sym));
		}
		if (arg1.isTimes()) {
			IAST[] result = ((IAST) arg1).split(new AbsTimesFunction());
			if (result[0].size() > 1) {
				if (result[1].size() > 1) {
					result[0].add(F.Abs(result[1]));
				}
				return result[0];
			}
		}
		return null;
	}

	public IExpr numericEvalD1(final Num arg1) {
		return F.num(Math.abs(arg1.getRealPart()));
	}

	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return F.num(arg1.dabs());
	}

	@Override
	public void setUp(final ISymbol symbol) throws SyntaxError {
		symbol.setAttributes(ISymbol.LISTABLE | ISymbol.NUMERICFUNCTION);
		super.setUp(symbol);
	}
}
