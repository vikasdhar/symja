package org.matheclipse.core.reflection.system;

import java.util.ArrayList;
import java.util.List;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.Symbol;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Block extends AbstractFunctionEvaluator {
	public Block() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		final EvalEngine engine = EvalEngine.get();
		if ((ast.size() == 3) && (ast.get(1)).isList()) {
			final IAST lst = (IAST) ast.get(1); 
			final List<IExpr> variables = new ArrayList<IExpr>();
			IExpr result;

			try {
				// remember which local variables we use:
				for (int i = 1; i < lst.size(); i++) {
					if (lst.get(i).isSymbol()) {
						variables.add(lst.get(i));
						((Symbol) lst.get(i)).pushLocalVariable();
					} else {
						if (lst.get(i).isAST(F.Set, 3)) {
							// lhs = rhs
							final IAST setFun = (IAST) lst.get(i);
							if (setFun.get(1).isSymbol()) {
								variables.add(setFun.get(1));
								((Symbol) setFun.get(1)).pushLocalVariable(engine.evaluate(setFun.get(2)));
							}
						}
					}
				}

				result = engine.evaluate(ast.get(2));
			} finally {
				// pop all local variables from local variable stack
				for (int i = 0; i < variables.size(); i++) {
					((Symbol) variables.get(i)).popLocalVariable();
				}
			}

			return result;
		}

		return null;
	}

	@Override
	public void setUp(ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}
