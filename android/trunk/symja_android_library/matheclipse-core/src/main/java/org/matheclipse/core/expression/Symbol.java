package org.matheclipse.core.expression;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.SystemNamespace;
import org.matheclipse.core.eval.exception.RuleCreationError;
import org.matheclipse.core.eval.interfaces.INumericConstant;
import org.matheclipse.core.eval.interfaces.ISymbolEvaluator;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.generic.UnaryVariable2Slot;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IEvaluationEngine;
import org.matheclipse.core.interfaces.IEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IPatternMatcher;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcher;
import org.matheclipse.core.patternmatching.PatternMatcherAndInvoker;
import org.matheclipse.core.patternmatching.RulesData;
import org.matheclipse.core.visit.IVisitor;
import org.matheclipse.core.visit.IVisitorBoolean;
import org.matheclipse.core.visit.IVisitorInt;
import org.matheclipse.generic.interfaces.INumericFunction;
import org.matheclipse.generic.interfaces.Pair;

import com.google.common.base.Function;

/**
 * Implements Symbols for function, constant and variable names
 * 
 */
public class Symbol extends ExprImpl implements ISymbol {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7416359407349683408L;

	/**
	 * 
	 */

	private static final int DEFAULT_VALUE_INDEX = Integer.MIN_VALUE;

	// protected static final XmlFormat<SymbolImpl> SYMBOL_XML = new
	// XmlFormat<SymbolImpl>(SymbolImpl.class) {
	// @Override
	// public void format(SymbolImpl obj, XmlElement xml) {
	// SymbolImpl expr = obj;
	// xml.setAttribute("name", expr.fSymbolName);
	// }
	//
	// @Override
	// public SymbolImpl parse(XmlElement xml) {
	// org.matheclipse.core.expression.ExprFactory factory =
	// org.matheclipse.core.expression.ExprFactory.get();
	// String name = xml.getAttribute("name", "");
	// return (SymbolImpl) factory.createSymbol(name);
	// }
	// };

	/**
	 * The attribute values of the symbol represented by single bits.
	 */
	private int fAttributes = NOATTRIBUTE;

	private transient IEvaluator fEvaluator;

	/**
	 * The pattern matching rules associated with this symbol.
	 */
	private transient RulesData fRulesData = new RulesData();

	/**
	 * {@inheritDoc}
	 */
	public IExpr[] reassignSymbolValue(Function<IExpr, IExpr> function) {
		IExpr[] result = new IExpr[2];
		IExpr symbolValue;
		if (hasLocalVariableStack()) {
			symbolValue = get();
			result[0] = symbolValue;
			IExpr calculatedResult = function.apply(symbolValue);
			if (calculatedResult != null) {
				set(calculatedResult);
				result[1] = calculatedResult;
				return result;
			}

		} else {
			Pair<ISymbol, IExpr> pair = fRulesData.getEqualRules().get(this);
			if (pair != null) {
				symbolValue = pair.getSecond();
				if (symbolValue != null) {
					result[0] = symbolValue;
					IExpr calculatedResult = function.apply(symbolValue);
					if (calculatedResult != null) {
						pair.setSecond(calculatedResult);
						result[1] = calculatedResult;
						return result;
					}
				}
			}
		}
		return null;
	}

	private Map<Integer, IExpr> fDefaultValues = null;

	static class DummyEvaluator implements IEvaluator {
		public void setUp(ISymbol symbol) {

		}
	}

	private static final DummyEvaluator DUMMY_EVALUATOR = new DummyEvaluator();

	/* package private */String fSymbolName;

	/**
	 * The hash value of this object computed in the constructor.
	 * 
	 */
	final int fHashValue;

	public Symbol(final String symbolName) {
		this(symbolName, null);
	}

	/**
	 * do not use directly, needed for XML transformations
	 * 
	 */
	private Symbol() {
		this(null, null);
	}

	public Symbol(final String symbolName, final IEvaluator evaluator) {
		super();
		fHashValue = (symbolName == null) ? 197 : 7 * symbolName.hashCode();
		fSymbolName = symbolName;
		fEvaluator = evaluator;
	}

	/** {@inheritDoc} */
	@Override
	public IExpr apply(IExpr... expressions) {
		return F.ast(expressions, this);
	}

	/** {@inheritDoc} */
	public void pushLocalVariable() {
		pushLocalVariable(null);
	}

	/** {@inheritDoc} */
	public void pushLocalVariable(final IExpr expression) {
		final Stack<IExpr> localVariableStack = EvalEngine.localStackCreate(fSymbolName);
		localVariableStack.push(expression);
	}

	/** {@inheritDoc} */
	public void popLocalVariable() {
		final Stack<IExpr> fLocalVariableStack = EvalEngine.localStack(fSymbolName);
		fLocalVariableStack.pop();
	}

	/** {@inheritDoc} */
	public void clear(EvalEngine engine) {
		if (!engine.isPackageMode()) {
			if (Config.SERVER_MODE && (fSymbolName.charAt(0) != '$')) {
				throw new RuleCreationError(null);
			}
		}
		fRulesData.clear();
	}

	/** {@inheritDoc} */
	public void clearAll(EvalEngine engine) {
		clear(engine);
		fAttributes = NOATTRIBUTE;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(this.getClass().equals(obj.getClass()))) {
			return false;
		}
		if (fSymbolName.equals(((Symbol) obj).fSymbolName)) {
			if (Config.DEBUG) {
				System.err.println(fSymbolName + " EQUALS " + ((Symbol) obj).fSymbolName);
			}
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public IExpr evaluate(EvalEngine engine) {
		if (hasLocalVariableStack()) {
			return get();
		}
		IExpr result;
		if ((result = evalDownRule(engine, this)) != null) {
			return result;
		}
		final IEvaluator module = getEvaluator();
		if (module instanceof ISymbolEvaluator) {
			if (engine.isNumericMode()) {
				return ((ISymbolEvaluator) module).numericEval(this);
			}
			return ((ISymbolEvaluator) module).evaluate(this);
		}
		return null;
	}

	/** {@inheritDoc} */
	public IExpr evalDownRule(final IEvaluationEngine ee, final IExpr expression) {
		return fRulesData.evalDownRule(ee, expression);
	}

	/** {@inheritDoc} */
	public int getAttributes() {
		return fAttributes;
	}

	/** {@inheritDoc} */
	public IEvaluator getEvaluator() {
		if (fEvaluator == null) {
			fEvaluator = DUMMY_EVALUATOR;
			if (Character.isUpperCase(fSymbolName.charAt(0))) {
				SystemNamespace.DEFAULT.setEvaluator(this);
			}
		}
		return fEvaluator;
	}

	/** {@inheritDoc} */
	public boolean hasLocalVariableStack() {
		final Stack<IExpr> localVariableStack = EvalEngine.localStack(fSymbolName);
		return (localVariableStack != null) && !(localVariableStack.isEmpty());
	}

	/** {@inheritDoc} */
	public IExpr get() {
		final Stack<IExpr> localVariableStack = EvalEngine.localStack(fSymbolName);
		if (localVariableStack == null) {
			return null;
		}
		return localVariableStack.peek();
	}

	/** {@inheritDoc} */
	public void set(final IExpr value) {
		final Stack<IExpr> localVariableStack = EvalEngine.localStack(fSymbolName);

		localVariableStack.set(localVariableStack.size() - 1, value);
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return fHashValue;
	}

	/** {@inheritDoc} */
	public int hierarchy() {
		return SYMBOLID;
	}

	/** {@inheritDoc} */
	public boolean isString(final String str) {
		return fSymbolName.equals(str);
	}

	/** {@inheritDoc} */
	public IPatternMatcher<IExpr> putDownRule(ISymbol symbol, final boolean equalRule, final IExpr leftHandSide,
			final IExpr rightHandSide) {
		return putDownRule(symbol, equalRule, leftHandSide, rightHandSide, DEFAULT_RULE_PRIORITY);
	}

	/** {@inheritDoc} */
	public PatternMatcher putDownRule(ISymbol setSymbol, final boolean equalRule, final IExpr leftHandSide,
			final IExpr rightHandSide, final int priority) {
		EvalEngine engine = EvalEngine.get();
		if (!engine.isPackageMode()) {
			if (Config.SERVER_MODE && (fSymbolName.charAt(0) != '$')) {
				throw new RuleCreationError(leftHandSide);
			}

			engine.addModifiedVariable(this);
		}
		return fRulesData.putDownRule(setSymbol, equalRule, leftHandSide, rightHandSide, priority);
	}

	/** {@inheritDoc} */
	public PatternMatcher putDownRule(final PatternMatcherAndInvoker pmEvaluator) {
		return fRulesData.putDownRule(pmEvaluator);
	}

	/** {@inheritDoc} */
	public void setAttributes(final int attributes) {
		fAttributes = attributes;
		if (fSymbolName.charAt(0) == '$' && Config.SERVER_MODE) {
			EvalEngine engine = EvalEngine.get();
			engine.addModifiedVariable(this);
		}
	}

	/** {@inheritDoc} */
	public void setEvaluator(final IEvaluator evaluator) {
		fEvaluator = evaluator;
		evaluator.setUp(this);
	}

	/**
	 * Compares this expression with the specified expression for order. Returns a
	 * negative integer, zero, or a positive integer as this expression is
	 * canonical less than, equal to, or greater than the specified expression.
	 */
	public int compareTo(final IExpr obj) {
		if (obj instanceof Symbol) {
			return fSymbolName.compareTo(((Symbol) obj).fSymbolName);
		}
		if (obj instanceof AST) {
			final AST ast = (AST) obj;
			final IExpr header = ast.head();
			if (ast.size() > 1) {
				if (header == F.Power && ast.size() == 3) {
					if (ast.get(1) instanceof ISymbol) {
						final int cp = fSymbolName.compareTo(((Symbol) ast.get(1)).fSymbolName);
						if (cp != 0) {
							return cp;
						}
						if (EvalEngine.get().isNumericMode()) {
							return F.CD1.compareTo(ast.get(2));
						}
						return F.C1.compareTo(ast.get(2));
					}
				} else if (header == F.Times) {
					// compare with the last ast element:
					final IExpr lastTimes = ast.get(ast.size() - 1);
					if (lastTimes instanceof AST) {
						final IExpr lastTimesHeader = ((IAST) lastTimes).head();
						if ((lastTimesHeader == F.Power) && (((IAST) lastTimes).size() == 3)) {
							final int cp = compareTo(((IAST) lastTimes).get(1));
							if (cp != 0) {
								return cp;
							}
							return F.C1.compareTo(((IAST) lastTimes).get(2));
						}
					}
					final int cp = compareTo(lastTimes);
					if (cp != 0) {
						return cp;
					}
				}
			}
			return -1;
		}
		return (hierarchy() - (obj).hierarchy());
	}

	/** {@inheritDoc} */
	@Override
	public boolean isAtom() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isTrue() {
		return fSymbolName.equals("True");
	}

	/** {@inheritDoc} */
	@Override
	public boolean isValue() {
		return evaluate(EvalEngine.get()) != null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isValue(IAST ast) {
		if (ast.head() instanceof ISymbol) {
			IExpr result = ((ISymbol) ast.head()).evalDownRule(EvalEngine.get(), ast);
			return result != null;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isFalse() {
		return fSymbolName.equals("False");
	}

	/** {@inheritDoc} */
	@Override
	public ISymbol head() {
		return F.SymbolHead;
	}

	/** {@inheritDoc} */
	public String getSymbol() {
		return fSymbolName;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public IExpr variables2Slots(final Map<IExpr, IExpr> map, final List<IExpr> variableList) {
		final UnaryVariable2Slot uv2s = new UnaryVariable2Slot(map, variableList);
		return uv2s.apply(this);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public String internalFormString(boolean symbolsAsFactoryMethod, int depth) {
		if (symbolsAsFactoryMethod) {
			if (Character.isUpperCase(fSymbolName.charAt(0))) {
				String alias = F.PREDEFINED_INTERNAL_STRINGS.get(fSymbolName);
				if (alias != null) {
					if (alias.contains("::")) {
						return "$s(\"" + alias + "\")";
					}
					return alias;
				}
				// if (fSymbolName.equals("Pi")) {
				// return "Pi";
				// } else if (fSymbolName.equals("E")) {
				// return "E";
				// } else if (fSymbolName.equals("False")) {
				// return "False";
				// } else if (fSymbolName.equals("True")) {
				// return "True";
				// }
			}
			return "$s(\"" + fSymbolName + "\")";
		}
		return fSymbolName;
	}

	@Override
	public String toString() {
		return fSymbolName;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IAST> definition() {
		return fRulesData.definition();
	}

	/** {@inheritDoc} */
	public IExpr getDefaultValue() {
		// special case for a general default value
		if (fDefaultValues == null) {
			return null;
		}
		return fDefaultValues.get(DEFAULT_VALUE_INDEX);
	}

	/** {@inheritDoc} */
	public IExpr getDefaultValue(int pos) {
		// default value at this position
		if (fDefaultValues == null) {
			return null;
		}
		return fDefaultValues.get(Integer.valueOf(pos));
	}

	/** {@inheritDoc} */
	public void setDefaultValue(IExpr expr) {
		// special case for a general default value
		if (fDefaultValues == null) {
			fDefaultValues = new HashMap<Integer, IExpr>();
		}
		fDefaultValues.put(DEFAULT_VALUE_INDEX, expr);
	}

	/** {@inheritDoc} */
	public void setDefaultValue(int pos, IExpr expr) {
		// default value at this position
		if (fDefaultValues == null) {
			fDefaultValues = new HashMap<Integer, IExpr>();
		}
		fDefaultValues.put(Integer.valueOf(pos), expr);
	}

	/** {@inheritDoc} */
	public String definitionToString() throws IOException {
		// dummy call to ensure, that the associated rules are loaded:
		getEvaluator();

		StringBufferWriter buf = new StringBufferWriter();
		buf.setIgnoreNewLine(true);
		List<IAST> list = definition();
		buf.append("{");
		for (int i = 0; i < list.size(); i++) {
			OutputFormFactory.get().convert(buf, list.get(i));
			if (i < list.size() - 1) {
				buf.append(",\n ");
			}
		}
		buf.append("}\n");
		return buf.toString();
	}

	/** {@inheritDoc} */
	public void readSymbol(java.io.ObjectInputStream stream) throws IOException {
		fSymbolName = stream.readUTF();
		fAttributes = stream.read();
		fRulesData.readSymbol(stream);
	}

	/** {@inheritDoc} */
	public void writeSymbol(java.io.ObjectOutputStream stream) throws java.io.IOException {
		stream.writeUTF(fSymbolName);
		stream.write(fAttributes);
		fRulesData.writeSymbol(stream);
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean accept(IVisitorBoolean visitor) {
		return visitor.visit(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public int accept(IVisitorInt visitor) {
		return visitor.visit(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public IExpr mapConstantDouble(INumericFunction<IExpr> function) {
		if ((getAttributes() & ISymbol.CONSTANT) == ISymbol.CONSTANT) {
			IEvaluator evaluator = getEvaluator();
			if (evaluator instanceof INumericConstant) {
				INumericConstant numericConstant = (INumericConstant) evaluator;
				double value = numericConstant.evalReal();
				if (value < Integer.MAX_VALUE && value > Integer.MIN_VALUE) {
					return function.apply(value);
				}
			}
		}
		return null;
	}

	@Override
	public IExpr negate() {
		return F.function(F.Times, F.CN1, this);
	}
}