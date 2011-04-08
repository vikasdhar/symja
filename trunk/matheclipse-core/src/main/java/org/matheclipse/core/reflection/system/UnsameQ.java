package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class UnsameQ extends AbstractFunctionEvaluator {

	public UnsameQ() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() > 1) {
			int i = 2;
			int j;
			while (i < ast.size()) {
				j = i;
				while (j < ast.size()) {
					if (ast.get(i - 1).equals(ast.get(j++))) {
						return F.False;
					}
				}
				i++;
			}
			return F.True;

		}
		return F.False;
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.FLAT);
	}
}