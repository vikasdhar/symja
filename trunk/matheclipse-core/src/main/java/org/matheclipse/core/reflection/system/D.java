package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.BinaryBindIth1st;
import org.matheclipse.core.generic.BinaryEval;
import org.matheclipse.core.generic.BinaryMap;
import org.matheclipse.core.generic.UnaryBind1st;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Differentiation of a function. See <a
 * href="http://en.wikipedia.org/wiki/Derivative">Wikipedia:Derivative</a>
 */
public class D extends AbstractFunctionEvaluator {
	// String[] RULES = {
	// "D[Sin[x_],y_]:= Cos[x]*D[x,y]",
	// "D[Cos[x_],y_]:= -Sin[x]*D[x,y]",
	// "D[Tan[x_],y_]:= 1/(Cos[x]^2)*D[x,y]",
	// "D[Cot[x_],y_]:= (-1)/(Sin[x]^2)*D[x,y]",
	// "D[ArcSin[x_],y_]:= (1-x^2)^(-1/2)*D[x,y]",
	// "D[ArcCos[x_],y_]:= -(1-x^2)^(-1/2)*D[x,y]",
	// "D[ArcTan[x_],y_]:= (1+x^2)^(-1)*D[x,y]",
	// "D[ArcCot[x_],y_]:= -(1+x^2)^(-1)*D[x,y]",
	// "D[Sinh[x_],y_]:= Cosh[x]*D[x,y]",
	// "D[Cosh[x_],y_]:= -Sinh[x]*D[x,y]",
	// "D[Tanh[x_],y_]:= (Cosh[x]^(-2))*D[x,y]",
	// "D[Coth[x_],y_]:= -(Sinh[x]^(-2))*D[x,y]",
	// "D[ArcSinh[x_],y_]:= (1+x^2)^(-1/2)*D[x,y]",
	// "D[ArcCosh[x_],y_]:= (x^2-1)^(-1/2)*D[x,y]",
	// "D[ArcTanh[x_],y_]:= (1-x^2)^(-1)*D[x,y]",
	// "D[ArcCoth[x_],y_]:= (1-x^2)^(-1)*D[x,y]",
	// "D[Log[x_],y_]:= x^(-1)*D[x,y]"
	// };

	public D() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() < 3) {
			return null;
		}

		IExpr fx = ast.get(1);
		if (ast.size() > 3) {
			// reduce arguments by folding D[fxy, x, y] to D[ D[fxy, x], y] ...
			return ast.range(2).fold(new BinaryEval(F.D), fx);
		}

		if (fx.isList()) {
			// thread over first list
			return ((IAST) fx).args().map(F.List(), new UnaryBind1st(ast));
		}

		IExpr x = ast.get(2);
		int n = 1;
		if (x.isList()) {
			// D[fx_, {...}]
			IAST xList = (IAST) x;
			if (xList.size() == 2 && xList.get(1).isList()) {
				IAST subList = (IAST) xList.get(1);
				return subList.args().mapLeft(F.List(), new BinaryEval(F.D), fx);
			} else if (xList.size() == 3 && xList.get(2) instanceof IInteger) {
				n = Validate.checkIntType(xList, 2, 1);

				if (xList.get(1).isList()) {
					x = F.List(xList.get(1));
				} else {
					x = xList.get(1);
				}
				for (int i = 0; i < n; i++) {
					fx = F.eval(F.D, fx, x);
				}

				return fx;

			}
			return null;

		}

		if (!(x.isList()) && FreeQ.freeQ(fx, x)) {
			return F.C0;
		}
		if (fx instanceof INumber) {
			// D[x_NumberQ,y_] -> 0
			return F.C0;
		}
		if (fx.equals(x)) {
			if (n == 1) {
				// D[x_,x_] -> 1
				return F.C1;
			}
			return F.C0;
		}

		if (fx instanceof IAST) {
			final ISymbol symbolD = F.D;
			final IAST listArg1 = (IAST) fx;
			final IExpr header = listArg1.head();
			if (header == F.Plus) {
				// D[a_+b_+c_,x_] -> D[a,x]+D[b,x]+D[c,x]
				return listArg1.args().map(F.Plus(), new UnaryBind1st(F.D(F.Null, ast.get(2))));
			} else if (header == F.Times) {
				return listArg1.args().map(F.Plus(), new BinaryBindIth1st(listArg1, F.D(F.Null, ast.get(2))));
			} else if ((header == F.Power) && (listArg1.size() == 3)) {
				if (listArg1.get(2) instanceof INumber) {
					// D[x_^i_NumberQ, z_]:= i*x^(i-1)*D[x,z];
					final IAST timesList = F.Times();
					timesList.add(listArg1.get(2));
					final IAST powerList = F.Power();
					powerList.add(listArg1.get(1));
					final IAST plusList = F.Plus();
					plusList.add(listArg1.get(2));
					plusList.add(F.CN1);
					powerList.add(plusList);
					timesList.add(powerList);
					timesList.add(F.D(listArg1.get(1), ast.get(2)));
					return timesList;
				} else {
					// D[f_^g_,y_]:= f^g*(((g*D[f,y])/f)+Log[f]*D[g,y])
					final IAST resultList = F.Times();
					final IExpr f = listArg1.get(1);
					final IExpr g = listArg1.get(2);
					final IExpr y = ast.get(2);

					IAST powerList = F.Power();
					powerList.add(f);
					powerList.add(g);
					resultList.add(powerList);

					final IAST plusList = F.Plus();
					IAST timesList = F.Times();
					timesList.add(g);
					timesList.add(F.function(symbolD, f, y));
					timesList.add(F.Power(f, F.CN1));
					plusList.add(timesList);

					timesList = F.Times(F.Log(f), F.D(g, y));
					plusList.add(timesList);

					resultList.add(plusList);
					return resultList;
				}
			} else if (listArg1.size() == 2) {
				IExpr der = F.evalNull(F.Derivative, header);
				if (der != null) {
					// found a derivative for a function of the form f[x_]
					IExpr derivative = F.eval(F.$(der, listArg1.get(1)));
					return F.Times(derivative, F.D(listArg1.get(1), x));
				}
			}

		}

		return null;
	}
	// @Override
	// public String[] getRules() {
	// return RULES;
	// }

}