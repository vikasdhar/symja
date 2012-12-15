package org.matheclipse.core.form.tex;

import java.util.Hashtable;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.expression.IConstantHeaders;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.IComplexNum;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.operator.ASTNodeFactory;

/**
 * PresentationGenerator generates MathML presentation output
 * 
 */
public class TeXFormFactory extends AbstractTeXFormFactory implements IConstantHeaders {

	static class Operator {
		String fOperator;

		Operator(final String oper) {
			fOperator = oper;
		}

		public void convert(final StringBuffer buf) {
			buf.append(fOperator);
		}

		public String toString() {
			return fOperator;
		}

	}

	/**
	 * Table for constant symbols
	 */
	public final Hashtable<String, Object> CONSTANT_SYMBOLS = new Hashtable<String, Object>(199);

	/**
	 * Description of the Field
	 */
	public final Hashtable<String, AbstractConverter> operTab = new Hashtable<String, AbstractConverter>(199);

	private int plusPrec;

	/**
	 * Constructor
	 */
	public TeXFormFactory() {
		this("");
	}

	public TeXFormFactory(final String tagPrefix) {
		super();
		init();
	}

	public void convertDouble(final StringBuffer buf, final INum d, final int precedence) {
		if (d.isNegative() && (precedence > plusPrec)) {
			buf.append("\\left( ");
		}
		buf.append(d.toString());
		if (d.isNegative() && (precedence > plusPrec)) {
			buf.append("\\right) ");
		}
	}

	public void convertDoubleComplex(final StringBuffer buf, final IComplexNum dc, final int precedence) {
		if (precedence > plusPrec) {
			buf.append("\\left( ");
		}
		convert(buf, dc.getRealPart(), 0);
		buf.append(" + ");
		convert(buf, dc.getImaginaryPart(), 0);
		buf.append("\\,"); // InvisibleTimes
		buf.append("\\imag");
		if (precedence > plusPrec) {
			buf.append("\\right) ");
		}
	}

	public void convertInteger(final StringBuffer buf, final IInteger i, final int precedence) {
		if (i.isNegative() && (precedence > plusPrec)) {
			buf.append("\\left( ");
		}
		buf.append(i.getBigNumerator().toString());
		if (i.isNegative() && (precedence > plusPrec)) {
			buf.append("\\right) ");
		}
	}

	public void convertFraction(final StringBuffer buf, final IFraction f, final int precedence) {
		if (f.isNegative() && (precedence > plusPrec)) {
			buf.append("\\left( ");
		}
		buf.append("\\frac{");
		buf.append(f.getBigNumerator().toString());
		buf.append("}{");
		buf.append(f.getBigDenominator().toString());
		buf.append('}');
		if (f.isNegative() && (precedence > plusPrec)) {
			buf.append("\\right) ");
		}
	}

	public void convertComplex(final StringBuffer buf, final IComplex c, final int precedence) {
		if (precedence > plusPrec) {
			buf.append("\\left( ");
		}
		convert(buf, c.getRealPart(), 0);
		buf.append(" + ");
		convert(buf, c.getImaginaryPart(), 0);
		buf.append("\\,"); // InvisibleTimes
		buf.append("\\imag");
		if (precedence > plusPrec) {
			buf.append("\\right) ");
		}
	}

	public void convertString(final StringBuffer buf, final String str) {
		buf.append(str);
	}

	public void convertSymbol(final StringBuffer buf, final ISymbol sym) {
		final Object convertedSymbol = CONSTANT_SYMBOLS.get(sym.toString());
		if (convertedSymbol == null) {
			buf.append(sym.toString());
		} else {
			if (convertedSymbol.equals(True)) {
				buf.append('\\');
				buf.append(sym.toString());
			} else {
				if (convertedSymbol instanceof Operator) {
					((Operator) convertedSymbol).convert(buf);
				} else {
					buf.append(convertedSymbol.toString());
				}
			}
		}
	}

	public void convertHead(final StringBuffer buf, final Object obj) {
		if (obj instanceof ISymbol) {
			final Object ho = CONSTANT_SYMBOLS.get(((ISymbol) obj).toString());
			if ((ho != null) && ho.equals(True)) {
				buf.append('\\');
			}
			buf.append(((ISymbol) obj).toString());
			return;
		}
		convert(buf, obj, 0);
	}

	public void convert(final StringBuffer buf, final Object o, final int precedence) {
		if (o instanceof IAST) {
			final IAST f = ((IAST) o);
			final IConverter converter = reflection(f.head().toString());
			if ((converter == null) || (!converter.convert(buf, f, precedence))) {
				convertAST(buf, f);
			}
			return;
		}
		if (o instanceof INum) {
			convertDouble(buf, (INum) o, precedence);
			return;
		}
		if (o instanceof IComplexNum) {
			convertDoubleComplex(buf, (IComplexNum) o, precedence);
			return;
		}
		if (o instanceof IInteger) {
			convertInteger(buf, (IInteger) o, precedence);
			return;
		}
		if (o instanceof IFraction) {
			convertFraction(buf, (IFraction) o, precedence);
			return;
		}
		if (o instanceof IComplex) {
			convertComplex(buf, (IComplex) o, precedence);
			return;
		}
		if (o instanceof ISymbol) {
			convertSymbol(buf, (ISymbol) o);
			return;
		}
		convertString(buf, o.toString());
	}

	private void convertAST(final StringBuffer buf, final IAST f) {
		convertHead(buf, f.head());
		buf.append("\\left( ");
		for (int i = 1; i < f.size(); i++) {
			convert(buf, f.get(i), 0);
			if (i < f.size() - 1) {
				buf.append(',');
			}
		}
		buf.append(" \\right)");

	}

	public String getReflectionNamespace() {
		return "org.matheclipse.core.form.tex.reflection.";
	}

	public IConverter reflection(final String headString) {
		final IConverter converter = operTab.get(headString);
		if (converter != null) {
			return converter;
		}
		final String namespace = getReflectionNamespace() + headString;

		Class clazz = null;
		try {
			clazz = Class.forName(namespace);
		} catch (final ClassNotFoundException e) {
			// not a predefined function
			return null;
		}

		AbstractConverter module;
		try {
			module = (AbstractConverter) clazz.newInstance();
			module.setFactory(this);
			// module.setExpressionFactory(fExprFactory);
			operTab.put(headString, module);
			return module;
		} catch (final Throwable se) {
			if (Config.DEBUG) {
				se.printStackTrace();
			}
		}
		return null;
	}

	public void init() {
		// operTab.put(Plus, new MMLOperator(this, "mrow", "+"));
		// operTab.put(Equal, new MMLOperator(this, "mrow", "="));
		// operTab.put(Less, new MMLOperator(this, "mrow", "&lt;"));
		// operTab.put(Greater, new MMLOperator(this, "mrow", "&gt;"));
		// operTab.put(LessEqual, new MMLOperator(this, "mrow", "&leq;"));
		// operTab.put(GreaterEqual, new MMLOperator(this, "mrow",
		// "&GreaterEqual;"));
		// operTab.put(Rule, new MMLOperator(this, "mrow", "-&gt;"));
		// operTab.put(RuleDelayed, new MMLOperator(this, "mrow",
		// "&RuleDelayed;"));
		// operTab.put(Set, new MMLOperator(this, "mrow", "="));
		// operTab.put(SetDelayed, new MMLOperator(this, "mrow", ":="));
		// operTab.put(And, new MMLOperator(this, "mrow", "&and;"));
		// operTab.put(Or, new MMLOperator(this, "mrow", "&or;"));
		// operTab.put(Not, new MMLNot(this));

		// operTab.put(Times, new MMLTimes(this, "mrow", "&InvisibleTimes;",
		// exprFactory));
		// operTab.put(Power, new MMLOperator(this, "msup", ""));
		plusPrec = ASTNodeFactory.MMA_STYLE_FACTORY.get("Plus").getPrecedence();

		operTab.put("Sin", new TeXFunction(this, "sin"));
		operTab.put("Cos", new TeXFunction(this, "cos"));
		operTab.put("Tan", new TeXFunction(this, "tan"));
		operTab.put("Cot", new TeXFunction(this, "cot"));
		operTab.put("ArcSin", new TeXFunction(this, "arcsin"));
		operTab.put("ArcCos", new TeXFunction(this, "arccos"));
		operTab.put("ArcTan", new TeXFunction(this, "arctan"));
		operTab.put("ArcCot", new TeXFunction(this, "arccot"));
		operTab.put("ArcSinh", new TeXFunction(this, "arcsinh"));
		operTab.put("ArcCosh", new TeXFunction(this, "arccosh"));
		operTab.put("ArcTanh", new TeXFunction(this, "arctanh"));
		operTab.put("ArcCoth", new TeXFunction(this, "arccoth"));
		operTab.put("Log", new TeXFunction(this, "log"));

		CONSTANT_SYMBOLS.put("Pi", "\\pi");

		CONSTANT_SYMBOLS.put("Alpha", True);
		CONSTANT_SYMBOLS.put("Beta", True);
		CONSTANT_SYMBOLS.put("Chi", True);
		CONSTANT_SYMBOLS.put("Delta", True);
		CONSTANT_SYMBOLS.put("Epsilon", True);
		CONSTANT_SYMBOLS.put("Phi", True);
		CONSTANT_SYMBOLS.put("Gamma", True);
		CONSTANT_SYMBOLS.put("Eta", True);
		CONSTANT_SYMBOLS.put("Iota", True);
		CONSTANT_SYMBOLS.put("varTheta", True);
		CONSTANT_SYMBOLS.put("Kappa", True);
		CONSTANT_SYMBOLS.put("Lambda", True);
		CONSTANT_SYMBOLS.put("Mu", True);
		CONSTANT_SYMBOLS.put("Nu", True);
		CONSTANT_SYMBOLS.put("Omicron", True);

		CONSTANT_SYMBOLS.put("Theta", True);
		CONSTANT_SYMBOLS.put("Rho", True);
		CONSTANT_SYMBOLS.put("Sigma", True);
		CONSTANT_SYMBOLS.put("Tau", True);
		CONSTANT_SYMBOLS.put("Upsilon", True);
		CONSTANT_SYMBOLS.put("Omega", True);
		CONSTANT_SYMBOLS.put("Xi", True);
		CONSTANT_SYMBOLS.put("Psi", True);
		CONSTANT_SYMBOLS.put("Zeta", True);

		CONSTANT_SYMBOLS.put("alpha", True);
		CONSTANT_SYMBOLS.put("beta", True);
		CONSTANT_SYMBOLS.put("chi", True);
		CONSTANT_SYMBOLS.put("selta", True);
		CONSTANT_SYMBOLS.put("epsilon", True);
		CONSTANT_SYMBOLS.put("phi", True);
		CONSTANT_SYMBOLS.put("gamma", True);
		CONSTANT_SYMBOLS.put("eta", True);
		CONSTANT_SYMBOLS.put("iota", True);
		CONSTANT_SYMBOLS.put("varphi", True);
		CONSTANT_SYMBOLS.put("kappa", True);
		CONSTANT_SYMBOLS.put("lambda", True);
		CONSTANT_SYMBOLS.put("mu", True);
		CONSTANT_SYMBOLS.put("nu", True);
		CONSTANT_SYMBOLS.put("omicron", True);
		// see "Pi"
		// CONSTANT_SYMBOLS.put("pi", True);
		CONSTANT_SYMBOLS.put("theta", True);
		CONSTANT_SYMBOLS.put("rho", True);
		CONSTANT_SYMBOLS.put("sigma", True);
		CONSTANT_SYMBOLS.put("tau", True);
		CONSTANT_SYMBOLS.put("upsilon", True);
		CONSTANT_SYMBOLS.put("varomega", True);
		CONSTANT_SYMBOLS.put("omega", True);
		CONSTANT_SYMBOLS.put("xi", True);
		CONSTANT_SYMBOLS.put("psi", True);
		CONSTANT_SYMBOLS.put("zeta", True);

	}

}
