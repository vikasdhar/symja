package org.matheclipse.core.reflection.system;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.matheclipse.core.convert.Object2Expr;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.eval.util.Options;
import org.matheclipse.core.generic.UnaryNumerical;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class NFourierTransform extends AbstractFunctionEvaluator {

	public NFourierTransform() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkRange(ast, 4, 5);

		IExpr expr = ast.get(1);
		ISymbol t = Validate.checkSymbolType(ast, 2);
		IExpr omega = ast.get(3);
		if (ast.size() > 4) {
			final Options options = new Options(ast.topHead(), ast, 4);
			IExpr optionFourierParameters = options.getOption("FourierParameters");
			if (optionFourierParameters.isList()) {
				// analyze the parameters, if they are correct
			}
		}

		UnivariateFunction f = new UnaryNumerical(expr, t, EvalEngine.get());
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		org.apache.commons.math3.complex.Complex[] result = fft.transform(f, -1.0, 1.0, 8, TransformType.FORWARD);
		return Object2Expr.convertComplex(result);
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDFIRST);
	}
}