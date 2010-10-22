package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.list.algorithms.EvaluationSupport;
import org.matheclipse.parser.client.SyntaxError;

/**
 * 
 * See <a href="http://en.wikipedia.org/wiki/Median">Median</a>
 */
public class Median extends AbstractTrigArg1 {
	public Median() {
	}

	@Override
	public IExpr evaluateArg1(final IExpr arg1) {
		if (arg1.isList()) {
			final IAST list = (IAST) arg1;
			if (list.size() > 1) {
				final IAST sortedList = list.clone();
				EvaluationSupport.sort(sortedList);
				int size = sortedList.size();
				if ((size & 0x00000001) == 0x00000001) {
					// odd number of elements
					size = size / 2;
					return F.Times(F.Plus(sortedList.get(size), sortedList.get(size + 1)), F.C1D2);
				} else {
					return sortedList.get(size / 2);
				}
			}
		}
		return null;
	}

	@Override
	public IExpr numericEvalArg1(final IExpr arg1) {
		return evaluateArg1(arg1);
	}

	@Override
	public void setUp(final ISymbol symbol) throws SyntaxError {
		symbol.setAttributes(ISymbol.NOATTRIBUTE);
	}
}
