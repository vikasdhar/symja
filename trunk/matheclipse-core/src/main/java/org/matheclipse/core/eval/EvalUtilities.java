package org.matheclipse.core.eval;

import java.io.Writer;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

import com.google.common.base.Predicate;

/**
 * Utility to evaluate math expressions.
 *
 */
public class EvalUtilities extends MathMLUtilities {

	public EvalUtilities() {
		super(new EvalEngine(), false);
	}

	public EvalUtilities(final boolean msie) {
		super(new EvalEngine(), msie);
	}

	public EvalUtilities(final EvalEngine evalEngine, final boolean msie) {
		super(evalEngine, msie);
	}

	/**
	 * Evaluate the <code>inputExpression</code> and return the resulting
	 * expression.
	 * 
	 * @param inputExpression
	 *          the expression which should be evaluated.
	 * @return
	 */
	public IExpr evaluate(final String inputExpression) throws Exception {
		IExpr parsedExpression = null;
		if (inputExpression != null) {
			startRequest();
			fEvalEngine.reset();
			parsedExpression = fEvalEngine.parse(inputExpression);
			if (parsedExpression != null) {
				fEvalEngine.reset();
				IExpr temp = fEvalEngine.evaluate(parsedExpression);
				fEvalEngine.addOut(temp);
				return temp;
			}
		}
		return null;
	}

	/**
	 * Evaluate the <code>parsedExpression</code> and return the resulting
	 * expression.
	 * 
	 * @param parsedExpression
	 *          the expression which should be evaluated.
	 * @return
	 */
	public IExpr evaluate(final IExpr parsedExpression) throws RuntimeException {
		if (parsedExpression != null) {
			startRequest();
			fEvalEngine.reset();
			IExpr temp = fEvalEngine.evaluate(parsedExpression);
			fEvalEngine.addOut(temp);
			return temp;
		}
		return null;
	}

	/**
	 * Evaluate the <code>inputExpression</code> and return the
	 * <code>Trace[inputExpression]</code> (i.e. all (sub-)expressions needed to
	 * calculate the result).
	 * 
	 * @param inputExpression
	 *          the expression which should be evaluated.
	 * @param matcher
	 *          a filter which determines the expressions which should be traced,
	 *          If the matcher is set to <code>null</code>, all expressions are
	 *          traced.
	 * @param list
	 *          an IAST object which will be cloned for containing the traced
	 *          expressions. Typically a <code>F.List()</code> will be used.
	 * @return
	 */
	public IAST evalTrace(final String inputExpression, Predicate<IExpr> matcher, IAST list) throws Exception {
		IExpr parsedExpression = null;
		if (inputExpression != null) {
			// try {
			startRequest();
			fEvalEngine.reset();
			parsedExpression = fEvalEngine.parse(inputExpression);
			if (parsedExpression != null) {
				fEvalEngine.reset();
				IAST temp = fEvalEngine.evalTrace(parsedExpression, matcher, list);
				fEvalEngine.addOut(temp);
				return temp;
			}
		}
		return null;
	}

	/**
	 * Evaluate the <code>parsedExpression</code> and return the
	 * <code>Trace[parsedExpression]</code> (i.e. all (sub-)expressions needed to
	 * calculate the result).
	 * 
	 * @param parsedExpression
	 *          the expression which should be evaluated.
	 * @param matcher
	 *          a filter which determines the expressions which should be traced,
	 *          If the matcher is set to <code>null</code>, all expressions are
	 *          traced.
	 * @param list
	 *          an IAST object which will be cloned for containing the traced
	 *          expressions. Typically a <code>F.List()</code> will be used.
	 * @return
	 */
	public IAST evalTrace(final IExpr parsedExpression, Predicate<IExpr> matcher, IAST list) throws RuntimeException {
		if (parsedExpression != null) {
			startRequest();
			fEvalEngine.reset();
			IAST temp = fEvalEngine.evalTrace(parsedExpression, matcher, list);
			return temp;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	synchronized public void toMathML(final String inputExpression, final Writer out) {
		try {
			final IExpr result = evaluate(inputExpression);
			if (result != null) {
				toMathML(result, out);
			}
		} catch (final Throwable e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		}
	}

}