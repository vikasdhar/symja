package org.matheclipse.core.reflection.system;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.convert.ExprVariables;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.exception.JASConversionException;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.eval.util.Options;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

import java.math.BigInteger;
import edu.jas.arith.BigRational;
import edu.jas.arith.ModInteger;
import edu.jas.arith.ModIntegerRing;
import edu.jas.poly.GenPolynomial;

/**
 * Greatest common divisor of two polynomials. See: <a href=
 * "http://en.wikipedia.org/wiki/Greatest_common_divisor_of_two_polynomials"
 * >Wikipedia:Greatest common divisor of two polynomials</a>
 */
public class PolynomialGCD extends AbstractFunctionEvaluator {

	public PolynomialGCD() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkRange(ast, 3);
		ExprVariables eVar = new ExprVariables(ast.get(1));
		if (!eVar.isSize(1)) {
			// gcd only possible for univariate polynomials
			return null;
		}
		ASTRange r = new ASTRange(eVar.getVarList(), 1);
		IExpr expr = F.evalExpandAll(ast.get(1));
		if (ast.size() > 3) {
			final Options options = new Options(ast.topHead(), ast, ast.size() - 1);
			IExpr option = options.getOption("Modulus");
			if (option != null && option.isInteger()) {
				try {
					// found "Modulus" option => use ModIntegerRing
					final BigInteger value = ((IInteger) option).getBigNumerator();
					int intValue = ((IInteger) option).toInt();
					ModIntegerRing modIntegerRing = new ModIntegerRing(intValue, value.isProbablePrime(32));
					JASConvert<ModInteger> jas = new JASConvert<ModInteger>(r.toList(), modIntegerRing);
					GenPolynomial<ModInteger> poly = jas.expr2JAS(expr);
					GenPolynomial<ModInteger> temp;
					for (int i = 2; i < ast.size() - 1; i++) {
						eVar = new ExprVariables(ast.get(i));
						if (!eVar.isSize(1)) {
							// gcd only possible for univariate polynomials
							return null;
						}
						expr = F.evalExpandAll(ast.get(i));
						temp = jas.expr2JAS(expr);
						poly = poly.gcd(temp);
					}
					return jas.modIntegerPoly2Expr(poly);
				} catch (JASConversionException e) {
					if (Config.DEBUG) {
						e.printStackTrace();
					}
					return null;
				}
			}
		}
		try {
			JASConvert<BigRational> jas = new JASConvert<BigRational>(r.toList(), BigRational.ZERO);
			GenPolynomial<BigRational> poly = jas.expr2JAS(expr);
			GenPolynomial<BigRational> temp;
			for (int i = 2; i < ast.size(); i++) {
				eVar = new ExprVariables(ast.get(i));
				if (!eVar.isSize(1)) {
					// gcd only possible for univariate polynomials
					return null;
				}
				expr = F.evalExpandAll(ast.get(i));
				temp = jas.expr2JAS(expr);
				poly = poly.gcd(temp);
			}
			return jas.rationalPoly2Expr(poly);
		} catch (JASConversionException e) {
			if (Config.DEBUG) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}