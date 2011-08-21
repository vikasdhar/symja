package org.matheclipse.core.form.tex;

import org.matheclipse.core.interfaces.IAST;

public class TeXFunction extends AbstractConverter {

  String fFunctionName;

  public TeXFunction(final TeXFormFactory factory, final String functionName) {
    super(factory);
    fFunctionName = functionName;
  }

  /**
   * Converts a given function into the corresponding TeX output
   *
   *@param  buf  StringBuffer for MathML output
   *@param  f    The math function which should be converted to MathML
   */
  public boolean convert(final StringBuffer buf, final IAST f, final int precedence) {
  	buf.append('\\');
  	buf.append(fFunctionName);
  	buf.append('(');
    for (int i = 1; i < f.size(); i++) {
			fFactory.convert(buf, f.get(i), 0);
      if (i < f.size() - 1) {
        buf.append(',');
      }
    }
    buf.append(')');
    return true;
  }
}