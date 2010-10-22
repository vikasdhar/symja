package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * The Manhattan distance of two vectors.
 * 
 * See <a href="http://en.wikipedia.org/wiki/Taxicab_geometry">Taxicab geometry</a>
 */
public class ManhattanDistance implements IFunctionEvaluator {

	public ManhattanDistance() {
	}

	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 3) {
			throw new WrongNumberOfArguments(ast, 2, ast.size() - 1);
		}
		IExpr arg1 = ast.get(1);
		IExpr arg2 = ast.get(2);

		int dim1 = arg1.isVector();
		if (dim1 > (-1)) {
			int dim2 = arg2.isVector();
			if (dim1 == dim2) {
				if (dim1 == 0) {
					return F.C0;
				}
				IAST a1 = ((IAST) arg1);
				IAST a2 = ((IAST) arg2);
				IAST plusAST = F.Plus();
				for (int i = 1; i < a1.size(); i++) {
					plusAST.add(F.Abs(F.Subtract(a1.get(i), a2.get(i))));
				}
				return plusAST;
			}
		}
		return null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(ISymbol symbol) {

	}

}
