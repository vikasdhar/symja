package org.matheclipse.core.reflection.system;

import org.apache.commons.math.FieldElement;
import org.apache.commons.math.linear.BlockFieldMatrix;
import org.apache.commons.math.linear.FieldMatrix;
import org.apache.commons.math.linear.FieldVector;
import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.Convert;
import org.matheclipse.core.eval.interfaces.AbstractNonOrderlessArgMultiple;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Dot extends AbstractNonOrderlessArgMultiple {

	public Dot() {
	}

	@Override
	public IExpr e2ObjArg(final IExpr o0, final IExpr o1) {
		FieldMatrix<IExpr> matrix0;
		FieldMatrix<IExpr> matrix1;
		FieldVector<IExpr> vector0;
		FieldVector<IExpr> vector1;
		IAST res;
		try {
			IAST list;

			if (o0.isMatrix() != null) {
				list = (IAST) o0;
				matrix0 = Convert.list2Matrix(list);
				if (o1.isMatrix() != null) {
					list = (IAST) o1;
					matrix1 = Convert.list2Matrix(list);
					res = Convert.matrix2List(matrix0.multiply(matrix1));
					return res;
				} else if (o1.isVector() != (-1)) {
					list = (IAST) o1;
					vector1 = Convert.list2Vector(list);
					return Convert.vector2List(matrix0.operate(vector1));
				}
			} else if (o0.isVector() != (-1)) {
				list = (IAST) o0;
				vector0 = Convert.list2Vector(list);
				if (o1.isMatrix() != null) {
					list = (IAST) o1;
					matrix1 = Convert.list2Matrix(list);
					FieldElement<IExpr>[] av = vector0.getData();
					BlockFieldMatrix<IExpr> m = new BlockFieldMatrix<IExpr>(o0.getField(),1,av.length);
					m.setRow(0, vector0.getData());
					return Convert.matrix2List(m.multiply(
							matrix1));
				} else if (o1.isVector() != (-1)) {
					list = (IAST) o1;
					vector1 = Convert.list2Vector(list);
					return vector0.dotProduct(vector1);
				}
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

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.FLAT | ISymbol.ONEIDENTITY);
	}

}