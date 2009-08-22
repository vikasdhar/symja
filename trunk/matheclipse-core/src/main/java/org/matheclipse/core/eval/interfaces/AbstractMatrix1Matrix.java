package org.matheclipse.core.eval.interfaces;

import org.apache.commons.math.linear.FieldMatrix;
import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.Convert;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public abstract class AbstractMatrix1Matrix extends AbstractFunctionEvaluator {

	public AbstractMatrix1Matrix() {
	}

	@Override
	public IExpr evaluate(final IAST function) {
		FieldMatrix<IExpr> matrix;
		try {
			if (function.size() == 2) {
				final IAST list = (IAST) function.get(1);
				matrix = Convert.list2Matrix(list);
				matrix = matrixEval(matrix);

				return Convert.matrix2List(matrix);
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
	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public abstract FieldMatrix<IExpr> matrixEval(FieldMatrix<IExpr> matrix);
}