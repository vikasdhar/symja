package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Predicate function
 * 
 * Returns <code>True</code> if the 1st argument is a matrix; <code>False</code>
 * otherwise
 */
public class MatrixQ extends AbstractFunctionEvaluator {

	public MatrixQ() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 2);
		return F.bool(ast.get(1).isMatrix() != null);
	}

	@Override
	public void setUp(final ISymbol symbol) {
	}

}
