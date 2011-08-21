package org.matheclipse.core.convert;

import static org.matheclipse.core.expression.F.List;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.visit.VisitorCollectionBoolean;

/**
 * Determine the variable symbols from a MathEclipse expression.
 * 
 */
public class ExprVariables {
	
	public static class VariablesVisitor extends VisitorCollectionBoolean {
		public VariablesVisitor(int hOffset, Collection<IExpr> collection) {
			super(hOffset, collection);
		}

		public boolean visit(ISymbol symbol) {
			if ((symbol.getAttributes() & ISymbol.CONSTANT) == ISymbol.CONSTANT) {
				return false;
			}
			return true;
		}
	}

	private final Set<IExpr> set = new TreeSet<IExpr>();

	/**
   * 
   */
	public ExprVariables(final IExpr expression) {
		super();
		expression.accept(new VariablesVisitor(1, set));
	}

	/**
	 * Add the symbol to the set of variables.
	 * 
	 * @param symbol
	 * @return <tt>true</tt> if the underlying set did not already contain the
	 *         symbol
	 */
	public boolean add(final ISymbol symbol) {
		return set.add(symbol);
	}

	/**
	 * Add the variables of the given expression
	 * 
	 * @param expression
	 */
	public void addVarList(final IExpr expression) {
		expression.accept(new VariablesVisitor(1, set));
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.Set#contains(java.lang.Object)
	 */
	public boolean contains(IExpr o) {
		return set.contains(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<? extends IExpr> c) {
		return set.containsAll(c);
	}

	/**
	 * @return the varList
	 */
	public IAST getVarList() {
		final Iterator<IExpr> iter = set.iterator();
		final IAST list = List();
		while (iter.hasNext()) {
			list.add(iter.next());
		}
		return list;
	}

	public String[] getVarListAsString() {
		String[] result = new String[set.size()];
		final Iterator<IExpr> iter = set.iterator();
		int i = 0;
		while (iter.hasNext()) {
			result[i++] = iter.next().toString();
		}
		return result;
	}

	/**
	 * @return
	 * @see java.util.Set#isEmpty()
	 */
	public boolean isEmpty() {
		return set.isEmpty();
	}

	/**
	 * Check if the expression contains the given number of variables.
	 * 
	 * @param expr
	 * @return <code>true</code> if the expr contains the given number of
	 *         variables.
	 */
	public boolean isSize(int size) {
		return set.size() == size;
	}

	/**
	 * @param a
	 * @return
	 * @see java.util.Set#toArray(T[])
	 */
	public IExpr[] toArray(IExpr[] a) {
		return set.toArray(a);
	}
}
