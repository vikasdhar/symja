package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Blank implements IFunctionEvaluator {
	public final static Blank CONST = new Blank();

	public Blank() {
	}

	public IExpr evaluate(final IAST ast) {
		if (ast.size() == 1) {
			return F.$p((ISymbol)null);
		}
		if (ast.size() == 2) {
			return F.$p((ISymbol)null, ast.get(1));
		}
		return null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(ISymbol symbol) {
	}
}
