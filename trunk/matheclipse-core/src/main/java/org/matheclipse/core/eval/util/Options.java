package org.matheclipse.core.eval.util;

import static org.matheclipse.core.expression.F.List;
import static org.matheclipse.core.expression.F.Options;
import static org.matheclipse.core.expression.F.ReplaceAll;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Managing <i>Options</i> associated with a function symbol.
 * 
 */
public class Options {
	private IAST fDefaultOptionsList;

	private IAST fCurrentOptionsList;

	/**
	 * 
	 * @param symbol
	 *          the options symbol for determining &quot;default option
	 *          values&quot;
	 * @param currentOptionsList
	 *          the AST where the option could be defined starting at position
	 *          <code>startIndex</code>
	 * @param startIndex
	 *          the index from which tolook for options defined in
	 *          <code>currentOptionsList</code>
	 */
	public Options(final ISymbol symbol, final IAST currentOptionsList, final int startIndex) {
		// get the List of pre-defined options:
		final IExpr temp = F.eval(Options(symbol));
		if ((temp != null) && (temp instanceof IAST) && temp.isList()) {
			fDefaultOptionsList = (IAST) temp;
		} else {
			fDefaultOptionsList = null;
		}
		this.fCurrentOptionsList = null;
		if (currentOptionsList != null && startIndex < currentOptionsList.size()) {
			this.fCurrentOptionsList = List();
			for (int i = startIndex; i < currentOptionsList.size(); i++) {
				this.fCurrentOptionsList.add(1, currentOptionsList.get(i));
			}
		}
	}

	/**
	 * Get the option from the internal option list and check if it's
	 * <code>true</code> or <code>false</code>.
	 * 
	 * @param optionString
	 *          the option string
	 * @return <code>true</code> if the option is set to <code>True</code> or
	 *         <code>false</code> otherwise.
	 */
	public boolean isOption(final String optionString) {
		IExpr temp = getOption(optionString);
		return temp.equals(F.True);
	}

	/**
	 * Get the option from the internal option list.
	 * 
	 * @param optionString
	 *          the option string
	 * @return the found option value or <code>null</code> if the option is not
	 *         available
	 */
	public IExpr getOption(final String optionString) {
		IAST rule = null;
		if (fCurrentOptionsList != null) {
			try {
				for (int i = 1; i < fCurrentOptionsList.size(); i++) {
					rule = (IAST) fCurrentOptionsList.get(i);
					if (rule.isRuleAST() && rule.get(1).toString().equals(optionString)) {
						return rule.get(2);
					}
				}
			} catch (Exception e) {

			}
		}
		if (fDefaultOptionsList != null) {
			try {
				for (int i = 1; i < fDefaultOptionsList.size(); i++) {
					rule = (IAST) fDefaultOptionsList.get(i);
					if (rule.isRuleAST() && rule.get(1).toString().equals(optionString)) {
						return rule.get(2);
					}
				}
			} catch (Exception e) {

			}
		}
		return null;
	}

	public IAST replaceAll(final IAST options) {
		IAST result = options.clone();
		if (fCurrentOptionsList != null) {
			result = (IAST) F.eval(ReplaceAll(result, fCurrentOptionsList));
		}
		if (fDefaultOptionsList != null) {
			result = (IAST) F.eval(ReplaceAll(result, fDefaultOptionsList));
		}
		return result;
	}

}
