package org.matheclipse.core.reflection.system;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.exception.JASConversionException;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.exception.WrongArgumentType;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

/**
 * 
 */
public class Resultant extends AbstractFunctionEvaluator {

	public Resultant() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 4);
		IExpr arg1 = F.evalExpandAll(ast.get(1));
		IExpr arg2 = F.evalExpandAll(ast.get(2));
		IExpr arg3 = ast.get(3);
		if (!arg3.isSymbol()) {
			// TODO allow multinomials
			return null;
		}
		try {
			IAST result = F.List();
			long degree1 = CoefficientList.univariateCoefficientList(arg1, (ISymbol) arg3, result);
			if (degree1 >= Short.MAX_VALUE) {
				throw new WrongArgumentType(ast, ast.get(1), 1, "Polynomial degree" + degree1 + " is larger than: " + " - "
						+ Short.MAX_VALUE);
			}
			IAST resultListDiff = F.List();
			long degree2 = CoefficientList.univariateCoefficientList(arg2, (ISymbol) arg3, resultListDiff);
			if (degree2 >= Short.MAX_VALUE) {
				throw new WrongArgumentType(ast, ast.get(1), 1, "Polynomial degree" + degree2 + " is larger than: " + " - "
						+ Short.MAX_VALUE);
			}
			return resultant(result, resultListDiff);
		} catch (JASConversionException jce) {
			// toInt() conversion failed
			if (Config.DEBUG) {
				jce.printStackTrace();
			}
		}
		return null;
	}

	public static IExpr resultant(IAST result, IAST resultListDiff) {
		// create sylvester matrix
		IAST sylvester = F.List();
		IAST row = F.List();
		IAST srow;
		final int n = resultListDiff.size() - 2;
		final int m = result.size() - 2;
		final int n2 = m + n;

		for (int i = result.size() - 1; i > 0; i--) {
			row.add(result.get(i));
		}
		for (int i = 0; i < n; i++) {
			// for each row
			srow = F.List();
			int j = 0;
			while (j < n2) {
				if (j < i) {
					srow.add(F.C0);
					j++;
				} else if (i == j) {
					for (int j2 = 1; j2 < row.size(); j2++) {
						srow.add(row.get(j2));
						j++;
					}
				} else {
					srow.add(F.C0);
					j++;
				}
			}
			sylvester.add(srow);
		}

		row = F.List();
		for (int i = resultListDiff.size() - 1; i > 0; i--) {
			row.add(resultListDiff.get(i));
		}
		for (int i = n; i < n2; i++) {
			// for each row
			srow = F.List();
			int j = 0;
			int k = n;
			while (j < n2) {
				if (k < i) {
					srow.add(F.C0);
					j++;
					k++;
				} else if (i == k) {
					for (int j2 = 1; j2 < row.size(); j2++) {
						srow.add(row.get(j2));
						j++;
						k++;
					}
				} else {
					srow.add(F.C0);
					j++;
					k++;
				}
			}
			sylvester.add(srow);
		}

		// System.out.println(sylvester);
		return F.eval(F.Det(sylvester));
	}

	@Override
	public void setUp(final ISymbol symbol) throws SyntaxError {
		symbol.setAttributes(ISymbol.LISTABLE);
		super.setUp(symbol);
	}
}