package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.generic.PositionConverter;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

public class Extract extends AbstractFunctionEvaluator {

	public Extract() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkRange(ast, 3, 4);
		
		if (ast.get(1).isAST() && ast.get(2).isAST()) {
			return extract((IAST) ast.get(1), (IAST) ast.get(2));
		}
		// if (ast.size() == 4 && ast.get(1).isAST()) {
		// LevelSpec level = new LevelSpecification(f, (IExpr) ast.get(3));
		// return extract((IAST) ast.get(1), ast.get(2), level);
		// }
		return null;
	}

	public static IExpr extract(final IAST list, final IAST position) {
		final PositionConverter converter = new PositionConverter();
		if ((position.size() > 1) && (position.get(1) instanceof IInteger)) {
			return AST.COPY.extract(list, position, converter, 1);
		} else {
			// construct an array
			// final IAST resultList = List();
			// NestedFinding.position(list, resultList, pos, 1);
			// return resultList;
		}
		return null;
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.NHOLDREST);
	}
}
