package org.matheclipse.core.generic;


import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class UnaryMap extends UnaryFunctorImpl<IExpr> {
	protected final IAST fConstant;

	/**
	 * 
	 * @param constant
	 *          the AST which should be cloned in the {@code apply} method
	 */
	public UnaryMap(final IAST constant) {
		fConstant = constant;
	}

	public IExpr apply(final IExpr firstArg) {
		final IAST ast = (IAST)fConstant.clone();
		ast.add(firstArg);
		return ast;
	}

}
