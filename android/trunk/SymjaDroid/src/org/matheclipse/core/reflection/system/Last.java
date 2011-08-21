package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 */
public class Last extends AbstractFunctionEvaluator {

	public Last() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 2);
		if (!ast.get(1).isAST()) {
			return null;
		}
		final IAST sublist = (IAST) ast.get(1);

		if (sublist.size() > 1) {
			return sublist.get(sublist.size() - 1);
		}

		return null;
	}
}
