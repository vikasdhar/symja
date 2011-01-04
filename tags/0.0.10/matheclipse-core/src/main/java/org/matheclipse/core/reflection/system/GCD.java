package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractArgMultiple;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Greatest common divisor
 * 
 * See <a href="http://en.wikipedia.org/wiki/Greatest_common_divisor">Wikipedia:Greatest common divisor</a>
 */
public class GCD extends AbstractArgMultiple {
	/**
	 * Constructor for the GCD object
	 */
	public GCD() {
	}

	/**
	 * Compute gcd of 2 integer numbers
	 *
	 */
	@Override
	public IExpr e2IntArg(final IInteger i0, final IInteger i1) {
		return i0.gcd(i1);
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.ONEIDENTITY | ISymbol.ORDERLESS | ISymbol.FLAT | ISymbol.LISTABLE);
	}

}
