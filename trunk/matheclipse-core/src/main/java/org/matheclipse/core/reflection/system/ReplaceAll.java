package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class ReplaceAll implements IFunctionEvaluator {

	public ReplaceAll() {
	}

	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 3);
		if (ast.get(2).isListOfLists()) {
			IAST result = F.List();
			for (IExpr subList : (IAST) ast.get(2)) {
				IExpr expr = ast.get(1).replaceAll((IAST) subList);
				result.add((expr == null) ? ast.get(1) : expr);
			}
			return result;
		}
		final IExpr result = ast.get(1).replaceAll((IAST) ast.get(2));
		return (result == null) ? ast.get(1) : result;

	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
	}
}
