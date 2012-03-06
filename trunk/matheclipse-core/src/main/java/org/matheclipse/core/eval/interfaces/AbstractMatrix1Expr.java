package org.matheclipse.core.eval.interfaces;

import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.matheclipse.core.basic.Config;
import org.matheclipse.core.convert.Convert;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.expression.ExprFieldElement;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public abstract class AbstractMatrix1Expr extends AbstractFunctionEvaluator {

	public AbstractMatrix1Expr() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 2);

		FieldMatrix<ExprFieldElement> matrix;
		try {

			int[] dim = ast.get(1).isMatrix();
			if (dim != null) {
				final IAST list = (IAST) ast.get(1);
				matrix = Convert.list2Matrix(list);
				return matrixEval(matrix).getExpr();
			}

		} catch (final ClassCastException e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		} catch (final IndexOutOfBoundsException e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	public IExpr numericEval(final IAST ast) {
		Validate.checkSize(ast, 2);

		RealMatrix matrix;
		try {

			final IAST list = (IAST) ast.get(1);
			matrix = Convert.list2RealMatrix(list);
			return realMatrixEval(matrix);

		} catch (final ClassCastException e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		} catch (final IndexOutOfBoundsException e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public abstract ExprFieldElement matrixEval(FieldMatrix<ExprFieldElement> matrix);

	public abstract IExpr realMatrixEval(RealMatrix matrix);
}