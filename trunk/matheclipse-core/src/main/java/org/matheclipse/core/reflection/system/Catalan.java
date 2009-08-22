package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractSymbolEvaluator;
import org.matheclipse.core.eval.interfaces.INumericConstant;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Catalan constant
 * 
 * See: <a href="http://en.wikipedia.org/wiki/Catalan%27s_constant">Wikipedia:Catalan's constant</a>
 * 
 */
public class Catalan extends AbstractSymbolEvaluator implements
		INumericConstant {
	final static public double CATALAN = 0.91596559417721901505460351493238411077414937428167;
	
	public Catalan() {
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.CONSTANT);
	}

	@Override
	public IExpr numericEval(final ISymbol symbol) {
		return F.num(CATALAN);
	}

	public double evalReal() {
		return CATALAN;
	}

}
