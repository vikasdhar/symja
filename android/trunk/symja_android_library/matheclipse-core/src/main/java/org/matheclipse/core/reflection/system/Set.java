package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.ConditionException;
import org.matheclipse.core.eval.exception.ReturnException;
import org.matheclipse.core.eval.exception.RuleCreationError;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.ICreatePatternMatcher;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcher;
import org.matheclipse.parser.client.math.MathException;

public class Set implements IFunctionEvaluator, ICreatePatternMatcher {
	public Set() {
	}

	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 3);
		final IExpr leftHandSide = ast.get(1);
		IExpr rightHandSide = ast.get(2);
		if (leftHandSide.isList()) {
			// thread over lists
			try {
				rightHandSide = F.eval(rightHandSide);
			} catch (final ReturnException e) {
				rightHandSide = e.getValue();
			}
			IExpr temp = EvalEngine.threadASTListArgs(F.Set(leftHandSide, rightHandSide));
			if (temp != null) {
				return F.eval(temp);
			}
		}
		Object[] result;
		// if (rightHandSide.isCondition()) {
		// result = createPatternMatcher(leftHandSide, ((IAST)
		// rightHandSide).get(1), ((IAST) rightHandSide).get(2), null);
		// } else if (rightHandSide.isModule()) {
		// IAST module = (IAST) rightHandSide;
		// if (module.get(2).isCondition()) {
		// IAST condition = (IAST) module.get(2);
		// result = createPatternMatcher(leftHandSide, condition.get(1),
		// condition.get(2), module.get(1));
		// } else {
		// result = createPatternMatcher(leftHandSide, rightHandSide, null, null);
		// }
		// } else {
		result = createPatternMatcher(leftHandSide, rightHandSide);
		// }
		return (IExpr) result[1];
	}

	public Object[] createPatternMatcher(IExpr leftHandSide, IExpr rightHandSide)
			throws RuleCreationError {
		final Object[] result = new Object[2];
		final EvalEngine engine = EvalEngine.get();

		leftHandSide = PatternMatcher.evalLeftHandSide(leftHandSide, engine);

		try {
			rightHandSide = engine.evaluate(rightHandSide);
		} catch (final ConditionException e) {
			System.out.println("Condition[] in right-hand-side of Set[]");
		} catch (final ReturnException e) {
			rightHandSide = e.getValue();
		}  

		result[0] = null; // IPatternMatcher
		result[1] = rightHandSide;
		if (leftHandSide.isSymbol()) {
			final ISymbol lhsSymbol = (ISymbol) leftHandSide;

			if (lhsSymbol.hasLocalVariableStack()) {
				lhsSymbol.set(rightHandSide);
				return result;
			} else {
				result[0] = lhsSymbol.putDownRule(F.Set, true, leftHandSide, rightHandSide);
				return result;
			}
		}

		if (leftHandSide.isAST()) {
			final ISymbol lhsSymbol = ((IAST) leftHandSide).topHead();
			result[0] = lhsSymbol.putDownRule(F.Set, false, leftHandSide, rightHandSide);
			return result;
		}

		throw new RuleCreationError(leftHandSide);
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}

}