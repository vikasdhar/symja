package org.matheclipse.core.reflection.system;

import java.util.ArrayList;
import java.util.List;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.exception.JASConversionException;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

import edu.jas.arith.BigRational;
import edu.jas.gbufd.GroebnerBasePartial;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.OptimizedPolynomialList;

public class GroebnerBasis extends AbstractFunctionEvaluator {

	public GroebnerBasis() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() >= 3) {
			try {
				if (lst.get(1).isVector() < 0) {
					return null;
				}
				if (lst.get(2).isVector() < 0) {
					return null;
				}
				if (lst.size() == 3) {
					IAST vars = (IAST) lst.get(2);
					if (vars.size() <= 1) {
						return null;
					}
					List<ISymbol> varList = new ArrayList<ISymbol>(vars.size() - 1);
					String[] pvars = new String[vars.size() - 1];
					for (int i = 1; i < vars.size(); i++) {
						if (!vars.get(i).isSymbol()) {
							return null;
						}
						varList.add((ISymbol) vars.get(i));
						pvars[i - 1] = ((ISymbol) vars.get(i)).toString();
					}
					GroebnerBasePartial<BigRational> gbp = new GroebnerBasePartial<BigRational>();
					IAST polys = (IAST) lst.get(1);
					List<GenPolynomial<BigRational>> polyList = new ArrayList<GenPolynomial<BigRational>>(polys.size() - 1);
					JASConvert<BigRational> jas = new JASConvert<BigRational>(varList, BigRational.ZERO);
					for (int i = 1; i < polys.size(); i++) {
						IExpr expr = F.evalExpandAll(polys.get(i));
						GenPolynomial<BigRational> poly = jas.expr2JAS(expr);
						polyList.add(poly);
					}

					OptimizedPolynomialList<BigRational> opl = gbp.partialGB(polyList, pvars);
					// System.out.println(opl);

					IAST resultList = F.List();
					for (GenPolynomial<BigRational> p : opl.list) {
						// System.out.println(p);
						resultList.add(jas.poly2Expr(p, null));
					}
					return resultList;
				}
			} catch (JASConversionException e) {
				if (Config.SHOW_STACKTRACE) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}