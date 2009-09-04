package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.polynomials.HornerScheme;

/**
 * Generate the horner scheme for univariate polynomials.
 * See: <a href="http://en.wikipedia.org/wiki/Horner_scheme">Wikipedia:Horner scheme</a>
 */
public class Horner extends AbstractFunctionEvaluator {

	public Horner() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 2) {
			return null;
		}
		IExpr expr = ast.get(1);
		if (!(expr instanceof IAST)) {
			return expr;
		}
		IAST poly = (IAST) expr;
		IAST variables = Variables.call(poly);
		if (variables.size() >= 2) {
			ISymbol sym = (ISymbol) variables.get(1);
			if (poly.isASTSizeGE(F.Plus, 2)) {
				HornerScheme scheme = new HornerScheme();
				return scheme.generate(EvalEngine.get().isNumericMode(),poly, sym);
			}
		}
		return expr;
	}

	@Override
	public void setUp(final ISymbol symbol) {
	}

}