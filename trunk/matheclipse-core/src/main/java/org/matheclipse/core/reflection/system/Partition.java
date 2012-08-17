package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;

/**
 * 
 * @see Permutations
 * @see Subsets
 */
public class Partition extends AbstractFunctionEvaluator {

	public Partition() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkRange(ast, 3, 4);

		if (ast.get(1).isAST()) {
			if (ast.get(2).isInteger()) {
				final IAST f = (IAST) ast.get(1);
				final int n = ((IInteger) ast.get(2)).getBigNumerator().intValue();
				final IAST result = F.ast(f.head());
				IAST temp;
				int i = n;
				int v = n;
				if ((ast.size() == 4) && ast.get(3).isInteger()) {
					v = ((IInteger) ast.get(3)).getBigNumerator().intValue();
				}
				while (i <= f.size() - 1) {
					temp = F.ast(f.head());
					for (int j = i - n; j < i; j++) {
						temp.add(f.get(j + 1));
					}
					i += v;
					result.add(temp);

				}
				return result;
			}
		}
		return null;
	}

}
