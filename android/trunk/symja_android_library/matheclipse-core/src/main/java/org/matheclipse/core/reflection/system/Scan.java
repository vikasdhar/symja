package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.ReturnException;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.util.Options;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.visit.VisitorLevelSpecification;

import com.google.common.base.Function;

/**
 * @see Map
 */
public class Scan extends Map {

	public Scan() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkRange(ast, 3, 5);

		int lastIndex = ast.size() - 1;
		boolean heads = false;
		final Options options = new Options(ast.topHead(), ast, lastIndex);
		IExpr option = options.getOption("Heads");
		if (option != null) {
			lastIndex--;
			if (option.isTrue()) {
				heads = true;
			}
		} else {
			Validate.checkRange(ast, 3, 4);
		}

		try {
			final IAST arg1 = F.ast(ast.get(1));
			if (lastIndex == 3) {
				IAST result = F.List();
				Function<IExpr, IExpr> sf = Functors.scan(arg1, result);
				VisitorLevelSpecification level = new VisitorLevelSpecification(sf, ast.get(lastIndex), heads);

				ast.get(2).accept(level);
				for (int i = 1; i < result.size(); i++) {
					F.eval(result.get(i));
				}

			} else {
				if (ast.get(2).isAST()) {
					F.eval(((IAST) ast.get(2)).map(Functors.append(arg1)));
				} else {
					F.eval(ast.get(2));
				}
			}
			return F.Null;
		} catch (final ReturnException e) {
			return e.getValue();
			// don't catch Throw[] here !
		}
	}

}
