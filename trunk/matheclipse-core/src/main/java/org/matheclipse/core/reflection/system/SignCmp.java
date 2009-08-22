package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Gets the signum value of a complex number
 *
 * @return 0 for <code>this == 0</code>;<br/> +1 for <code>real(this) &gt; 0 || ( real(this) == 0 &amp;&amp; imaginary(this) &gt; 0 )</code>;<br/>
 * -1 for <code>real(this) &lt; 0 || ( real(this) == 0 &amp;&amp; imaginary(this) &lt; 0 )
 */
public class SignCmp implements IFunctionEvaluator {

  public SignCmp() {
  }


  public IExpr evaluate(final IAST functionList) {
  	if (functionList.size()!=2) {
  		return null;
  	}
  	if (functionList.get(1) instanceof INumber) {
  		final int signum = ((INumber)functionList.get(1)).complexSign();
  		return F.integer(signum);
  	}
  	return null;
  }

  public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
  }

  public void setUp(final ISymbol symbol) {
    symbol.setAttributes(ISymbol.LISTABLE);
  }

}
