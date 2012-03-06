package org.matheclipse.core.reflection.system;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.matheclipse.core.convert.Convert;
import org.matheclipse.core.eval.exception.WrappedException;
import org.matheclipse.core.eval.interfaces.AbstractMatrix1Expr;
import org.matheclipse.core.expression.ExprFieldElement;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Compute the numerical Eigenvectors of a real symmetric matrix
 * 
 * See: <a
 * href="http://en.wikipedia.org/wiki/Eigenvalue,_eigenvector_and_eigenspace"
 * >Eigenvalue, eigenvector and eigenspace</a>
 */
public class Eigenvectors extends AbstractMatrix1Expr {

	public Eigenvectors() {
		super();
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		// switch to numeric calculation
		return numericEval(ast);
	}

	public IAST realMatrixEval(RealMatrix matrix) {
		try {
			IAST list = F.List();
			EigenDecomposition ed = new EigenDecomposition(matrix, Precision.SAFE_MIN);
			for (int i = 0; i < matrix.getColumnDimension(); i++) {
				RealVector rv = ed.getEigenvector(i);
				list.add(Convert.realVector2List(rv));
			}
			return list;
		} catch (Exception ime) {
			throw new WrappedException(ime);
		}
	}

	@Override
	public ExprFieldElement matrixEval(FieldMatrix<ExprFieldElement> matrix) {
		// TODO Auto-generated method stub
		return null;
	}
}