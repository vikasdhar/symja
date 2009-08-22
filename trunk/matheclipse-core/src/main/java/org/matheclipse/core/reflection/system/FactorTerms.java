package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

import edu.jas.arith.BigRational;
import edu.jas.poly.GenPolynomial;

public class FactorTerms extends AbstractFunctionEvaluator {

	public FactorTerms() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() != 2 && lst.size() != 3) {
			return null;
		}
		IAST variableList = null;
		if (lst.size() == 2) {
			variableList = Variables.call(lst.get(1));
		}
		if (lst.size() == 3) {
			if (lst.get(2) instanceof ISymbol) {
				ISymbol variable = (ISymbol) lst.get(2);
				variableList = F.List(variable);
			} else if (lst.get(2).isList()) {
				variableList = (IAST) lst.get(2);
			} else {
				return null;
			}
		}
		if (variableList.size() != 2) {
			// FactorTerms only possible for univariate polynomials
			return null;
		}
		// IExpr variable = variableList.get(1);
		try {
			IExpr expr = F.eval(F.ExpandAll, lst.get(1));
			ASTRange r = new ASTRange(variableList, 1);
			JASConvert<BigRational> jas = new JASConvert<BigRational>(r.toList());
			GenPolynomial<BigRational> poly = jas.expr2Poly(expr);
			Object[] objects = jas.factorTerms(poly);
			java.math.BigInteger gcd = (java.math.BigInteger) objects[0];
			java.math.BigInteger lcm = (java.math.BigInteger) objects[1];
			if (lcm.equals(java.math.BigInteger.ZERO)) {
				// no changes
				return expr;
			}
			GenPolynomial<edu.jas.arith.BigInteger> iPoly = (GenPolynomial<edu.jas.arith.BigInteger>) objects[2];
			IAST result = F.Times();
			result.add(F.fraction(gcd, lcm));
			result.add(jas.integerPoly2Expr(iPoly));
			return result;
		} catch (Exception e) {
			if (Config.DEBUG) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.FLAT);
	}
}