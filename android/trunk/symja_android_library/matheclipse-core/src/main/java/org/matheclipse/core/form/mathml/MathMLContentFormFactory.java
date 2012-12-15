package org.matheclipse.core.form.mathml;

import java.util.Hashtable;

import org.apache.commons.math3.fraction.BigFraction;
import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.exception.WrongArgumentType;
import org.matheclipse.core.expression.IConstantHeaders;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IComplexNum;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.list.algorithms.EvaluationSupport;
import org.matheclipse.parser.client.operator.ASTNodeFactory;

/**
 * Presentation generator generates MathML presentation output
 * 
 * See: <a href="http://www.w3.org/TR/MathML2/chapter4.html">MathML2</a>
 * 
 */
public class MathMLContentFormFactory extends AbstractMathMLFormFactory implements IConstantHeaders {

	class Operator {
		String fOperator;

		Operator(final String oper) {
			fOperator = oper;
		}

		public void convert(final StringBuffer buf) {
			tagStart(buf, "mo");
			buf.append(fOperator);
			tagEnd(buf, "mo");
		}

		public String toString() {
			return fOperator;
		}

	}

	/**
	 * Table for constant symbols
	 */
	public final static Hashtable<String, Object> CONSTANT_SYMBOLS = new Hashtable<String, Object>(199);

	/**
	 * Description of the Field
	 */
	public final static Hashtable<String, AbstractConverter> operTab = new Hashtable<String, AbstractConverter>(199);

	/**
	 * Constructor
	 */
	public MathMLContentFormFactory() {
		this("math:");
	}

	public MathMLContentFormFactory(final String tagPrefix) {
		super(tagPrefix);
		init();
	}

	public void convertDouble(final StringBuffer buf, final INum d, final int precedence) {
		tagStart(buf, "cn", "type=\"real\"");
		buf.append(d.toString());
		tagEnd(buf, "cn");
	}

	public void convertDoubleComplex(final StringBuffer buf, final IComplexNum dc, final int precedence) {
		// <cn type="complex-cartesian">3<sep/>4</cn>
		tagStart(buf, "cn", "type=\"complex-cartesian\"");
		buf.append(String.valueOf(dc.getRealPart()));
		tagStartEnd(buf, "sep");
		buf.append(String.valueOf(dc.getImaginaryPart()));
		tagEnd(buf, "cn");
	}

	public void convertInteger(final StringBuffer buf, final IInteger i, final int precedence) {
		tagStart(buf, "cn", "type=\"integer\"");
		buf.append(i.toString());
		tagEnd(buf, "cn");
	}

	public void convertFraction(final StringBuffer buf, final IFraction f, final int precedence) {
		// <cn type="rational">3<sep/>4</cn>
		tagStart(buf, "cn", "type=\"rational\"");
		buf.append(String.valueOf(f.getBigNumerator().toString()));
		tagStartEnd(buf, "sep");
		buf.append(String.valueOf(f.getBigDenominator().toString()));
		tagEnd(buf, "cn");
	}

	public void convertFraction(final StringBuffer buf, final BigFraction f, final int precedence) {
		tagStart(buf, "cn", "type=\"rational\"");
		buf.append(String.valueOf(f.getNumerator().toString()));
		tagStartEnd(buf, "sep");
		buf.append(String.valueOf(f.getDenominator().toString()));
		tagEnd(buf, "cn");
	}

	public void convertComplex(final StringBuffer buf, final IComplex c, final int precedence) {
		// <cn type="complex-cartesian">3<sep/>4</cn>
		tagStart(buf, "cn", "type=\"complex-cartesian\"");
		convertFraction(buf, c.getRealPart(), precedence);
		tagStartEnd(buf, "sep");
		convertFraction(buf, c.getImaginaryPart(), ASTNodeFactory.TIMES_PRECEDENCE);
		tagEnd(buf, "cn");
	}

	public void convertString(final StringBuffer buf, final String str) {
		throw new Error("Cannot convert text string to content MathML");
	}

	public void convertSymbol(final StringBuffer buf, final ISymbol sym) {
		final Object convertedSymbol = CONSTANT_SYMBOLS.get(sym.toString());
		if (convertedSymbol == null) {
			tagStart(buf, "ci");
			buf.append(sym.toString());
			tagEnd(buf, "ci");
		} else {
			// if (convertedSymbol.equals(True)) {
			// tagStart(buf, "mi");
			// buf.append('&');
			// buf.append(sym.toString());
			// buf.append(';');
			// tagEnd(buf, "mi");
			// } else {
			// if (convertedSymbol instanceof Operator) {
			// ((Operator) convertedSymbol).convert(buf);
			// } else {
			tagStart(buf, "ci");
			buf.append(convertedSymbol.toString());
			tagEnd(buf, "ci");
			// }
			// }
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param buf
	 *          Description of Parameter
	 * @param p
	 *          Description of Parameter
	 */
	// public void convertPattern(StringBuffer buf, HPattern p) {
	// buf.append(" <mi>");
	// buf.append(p.toString());
	// tagEnd(buf, "mi");
	// }
	public void convertHead(final StringBuffer buf, final IExpr obj) {
		if (obj instanceof ISymbol) {
			final Object ho = CONSTANT_SYMBOLS.get(((ISymbol) obj).toString());
			tagStart(buf, "mi");
			if ((ho != null) && ho.equals(True)) {
				buf.append('&');
			}
			buf.append(((ISymbol) obj).toString());
			tagEnd(buf, "mi");
			// &af; &#x2061;
			tag(buf, "mo", "&#x2061;");
			return;
		}
		convert(buf, obj, 0);
	}

	public void convert(final StringBuffer buf, final IExpr o, final int precedence) {
		if (o instanceof IAST) {
			final IAST f = ((IAST) o);
			// System.out.println(f.getHeader().toString());
			// IConverter converter = (IConverter)
			// operTab.get(f.getHeader().toString());
			// if (converter == null) {
			// converter = reflection(f.getHeader().toString());
			// if (converter == null || (converter.convert(buf, f, 0) == false))
			// {
			// convertHeadList(buf, f);
			// }
			// } else {
			// if (converter.convert(buf, f, precedence) == false) {
			// convertHeadList(buf, f);
			// }
			// }
			final ISymbol symbol = f.topHead();
			final int attr = symbol.getAttributes();
			IAST ast = f;
			IAST temp;
			if ((ISymbol.FLAT & attr) == ISymbol.FLAT) {
				// associative
				if ((temp = EvaluationSupport.flatten(f)) != null) {
					ast = temp;
				}
			}
			final IConverter converter = reflection(ast.head().toString());
			if ((converter == null) || (!converter.convert(buf, ast, precedence))) {
				convertAST(buf, ast);
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

	private void convertAST(final StringBuffer buf, final IAST ast) {
		tagStart(buf, "mrow");
		convertHead(buf, ast.head());
		// &af; &#x2061;
		tag(buf, "mo", "&#x2061;");
		tagStart(buf, "mrow");
		tag(buf, "mo", "(");
		tagStart(buf, "mrow");
		for (int i = 1; i < ast.size(); i++) {
			convert(buf, ast.get(i), 0);
			if (i < ast.size() - 1) {
				tag(buf, "mo", ",");
			}
		}
		tagEnd(buf, "mrow");
		tag(buf, "mo", ")");
		tagEnd(buf, "mrow");
		tagEnd(buf, "mrow");

	}

	public String getReflectionNamespace() {
		return "org.matheclipse.core.form.mathml.reflection.";
	}

	public IConverter reflection(final String headString) {
		final AbstractConverter converter = operTab.get(headString);
		if (converter != null) {
			converter.setFactory(this);
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
		// plusPrec = ASTNodeFactory.MMA_STYLE_FACTORY.get("Plus").getPrecedence();

		operTab.put("Plus", new MMLContentFunction(this, "plus"));
		operTab.put("Times", new MMLContentFunction(this, "times"));
		operTab.put("Power", new MMLContentFunction(this, "power"));
		operTab.put("Sin", new MMLContentFunction(this, "sin"));
		operTab.put("Cos", new MMLContentFunction(this, "cos"));
		operTab.put("Tan", new MMLContentFunction(this, "tan"));
		operTab.put("Cot", new MMLContentFunction(this, "cot"));
		operTab.put("ArcSin", new MMLContentFunction(this, "arcsin"));
		operTab.put("ArcCos", new MMLContentFunction(this, "arccos"));
		operTab.put("ArcTan", new MMLContentFunction(this, "arctan"));
		operTab.put("ArcCot", new MMLContentFunction(this, "arccot"));
		operTab.put("ArcSinh", new MMLContentFunction(this, "arcsinh"));
		operTab.put("ArcCosh", new MMLContentFunction(this, "arccosh"));
		operTab.put("ArcTanh", new MMLContentFunction(this, "arctanh"));
		operTab.put("ArcCoth", new MMLContentFunction(this, "arccoth"));
		operTab.put("Log", new MMLContentFunction(this, "log"));

		// operTab.put("Sum", new MMLSum(this));
		// operTab.put("Integrate", new MMLIntegrate(this));
		// operTab.put("D", new MMLD(this));
		// operTab.put(Factorial, new MMLFactorial(this));
		// operTab.put("Binomial", new MMLBinomial(this));

		CONSTANT_SYMBOLS.put("E", "\u2147");
		// CONSTANT_SYMBOLS.put("I", "\u2148"); // IMaginaryI
		CONSTANT_SYMBOLS.put("HEllipsis", new Operator("&hellip;"));
		// greek Symbols:
		CONSTANT_SYMBOLS.put("Pi", "\u03A0");
		CONSTANT_SYMBOLS.put("pi", "\u03C0");
		CONSTANT_SYMBOLS.put("Alpha", "\u0391");
		CONSTANT_SYMBOLS.put("Beta", "\u0392");
		CONSTANT_SYMBOLS.put("Gamma", "\u0393");
		CONSTANT_SYMBOLS.put("Delta", "\u0394");
		CONSTANT_SYMBOLS.put("Epsilon", "\u0395");
		CONSTANT_SYMBOLS.put("Zeta", "\u0396");
		CONSTANT_SYMBOLS.put("Eta", "\u0397");
		CONSTANT_SYMBOLS.put("Theta", "\u0398");
		CONSTANT_SYMBOLS.put("Iota", "\u0399");
		CONSTANT_SYMBOLS.put("Kappa", "\u039A");
		CONSTANT_SYMBOLS.put("Lambda", "\u039B");
		CONSTANT_SYMBOLS.put("Mu", "\u039C");
		CONSTANT_SYMBOLS.put("Nu", "\u039D");
		CONSTANT_SYMBOLS.put("Xi", "\u039E");
		CONSTANT_SYMBOLS.put("Omicron", "\u039F");
		CONSTANT_SYMBOLS.put("Rho", "\u03A1");
		CONSTANT_SYMBOLS.put("Sigma", "\u03A3");
		CONSTANT_SYMBOLS.put("Tau", "\u03A4");
		CONSTANT_SYMBOLS.put("Upsilon", "\u03A5");
		CONSTANT_SYMBOLS.put("Phi", "\u03A6");
		CONSTANT_SYMBOLS.put("Chi", "\u03A7");
		CONSTANT_SYMBOLS.put("Psi", "\u03A8");
		CONSTANT_SYMBOLS.put("Omega", "\u03A9");

		CONSTANT_SYMBOLS.put("varTheta", "\u03D1");

		CONSTANT_SYMBOLS.put("alpha", "\u03B1");
		CONSTANT_SYMBOLS.put("beta", "\u03B2");
		CONSTANT_SYMBOLS.put("chi", "\u03C7");
		CONSTANT_SYMBOLS.put("selta", "\u03B4");
		CONSTANT_SYMBOLS.put("epsilon", "\u03B5");
		CONSTANT_SYMBOLS.put("phi", "\u03C7");
		CONSTANT_SYMBOLS.put("gamma", "\u03B3");
		CONSTANT_SYMBOLS.put("eta", "\u03B7");
		CONSTANT_SYMBOLS.put("iota", "\u03B9");
		CONSTANT_SYMBOLS.put("varphi", "\u03C6");
		CONSTANT_SYMBOLS.put("kappa", "\u03BA");
		CONSTANT_SYMBOLS.put("lambda", "\u03BB");
		CONSTANT_SYMBOLS.put("mu", "\u03BC");
		CONSTANT_SYMBOLS.put("nu", "\u03BD");
		CONSTANT_SYMBOLS.put("omicron", "\u03BF");
		CONSTANT_SYMBOLS.put("theta", "\u03B8");
		CONSTANT_SYMBOLS.put("rho", "\u03C1");
		CONSTANT_SYMBOLS.put("sigma", "\u03C3");
		CONSTANT_SYMBOLS.put("tau", "\u03C4");
		CONSTANT_SYMBOLS.put("upsilon", "\u03C5");
		CONSTANT_SYMBOLS.put("varsigma", "\u03C2");
		CONSTANT_SYMBOLS.put("omega", "\u03C9");
		CONSTANT_SYMBOLS.put("xi", "\u03BE");
		CONSTANT_SYMBOLS.put("psi", "\u03C8");
		CONSTANT_SYMBOLS.put("zeta", "\u03B6");

		ENTITY_TABLE.put("&af;", "\uE8A0");
		ENTITY_TABLE.put("&dd;", "\uF74C");
		ENTITY_TABLE.put("&ImaginaryI;", "i");// "\u2148");
		ENTITY_TABLE.put("&InvisibleTimes;", "\uE89E");

		ENTITY_TABLE.put("&Integral;", "\u222B");
		ENTITY_TABLE.put("&PartialD;", "\u2202");
		ENTITY_TABLE.put("&Product;", "\u220F");

	}
}
