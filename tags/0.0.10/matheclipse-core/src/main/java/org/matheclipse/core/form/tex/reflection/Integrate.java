package org.matheclipse.core.form.tex.reflection;

import org.matheclipse.core.form.tex.AbstractConverter;
import org.matheclipse.core.interfaces.IAST;

public class Integrate extends AbstractConverter {

  public Integrate() {
  }

  /**
   * Converts a given function into the corresponding MathML output
   *
   *@param  buf  StringBuffer for MathML output
   *@param  f    The math function which should be converted to MathML
   */
  public boolean convert(final StringBuffer buf, final IAST f, final int precedence) {
    if (f.size() == 3) {
      final Object obj = f.get(2);
      IAST list = null;
      if (obj instanceof IAST) {
        list = (IAST) obj;
      }
      if ((list != null) && (list.size() == 4) && list.head().toString().equals("List")) {
      	buf.append("\\int_{");
        fFactory.convert(buf, list.get(2), 0);
        buf.append("}^{");
        fFactory.convert(buf, list.get(3), 0);
        buf.append('}');
      } else {
        list = null;
        buf.append("\\int ");
      }

      fFactory.convert(buf, f.get(1), 0);
      buf.append("\\,d");
      if (list != null) {
        fFactory.convert(buf, list.get(1), 0);
      } else {
        fFactory.convert(buf, f.get(2), 0);
      }
      return true;
    }
    return false;
  }
}