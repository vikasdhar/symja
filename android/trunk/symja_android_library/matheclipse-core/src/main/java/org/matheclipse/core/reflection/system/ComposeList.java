package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.List;

import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.BinaryApply;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.generic.Algorithms;

public class ComposeList implements IFunctionEvaluator {

	public ComposeList() {
	}

	public IExpr evaluate(final IAST ast) {
		return evaluateComposeList(ast, List());
	}

	public static IExpr evaluateComposeList(final IAST ast, final IAST resultList) {
		try {
			if ((ast.size() == 3) && (ast.get(1).isAST())) {
				// final EvalEngine engine = EvalEngine.get();
				final IAST list = (IAST) ast.get(1);
				Algorithms.foldLeft(ast.get(2), list, 1, list.size(), new BinaryApply(F.ast(ast.get(1))), resultList);
				return resultList;
			}
		} catch (final ArithmeticException e) {

		}
		return null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}
