package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class PreIncrement extends PreDecrement {

	@Override
	protected IExpr execute(final IExpr first, final EvalEngine engine) {
		return engine.evaluate(F.Plus(first, F.C1));
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}

}