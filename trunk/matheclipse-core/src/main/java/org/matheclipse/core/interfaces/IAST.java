package org.matheclipse.core.interfaces;

import java.util.Iterator;
import java.util.List;

import org.matheclipse.core.eval.exception.WrongArgumentType;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.generic.util.INestedList;
import org.matheclipse.core.reflection.system.Apart;
import org.matheclipse.generic.interfaces.BiFunction;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

//import org.matheclipse.generic.INestedList;

/**
 * 
 * <p>
 * (I)nterface for the (A)bstract (S)yntax (T)ree of a given function.
 * </p>
 * 
 * <p>
 * In MathEclipse, an abstract syntax tree (AST), is a tree representation of
 * the abstract syntactic structure of the MathEclipse source code. Each node of
 * the tree denotes a construct occurring in the source code. The syntax is
 * 'abstract' in the sense that it does not represent every detail that appears
 * in the real syntax. For instance, grouping parentheses are implicit in the
 * tree structure, and a syntactic construct such as a <code>Sin[x]</code>
 * expression will be denoted by an AST with 2 nodes. One node for the header
 * <code>Sin</code> and one node for the argument <code>x</code>.
 * </p>
 * 
 * Internally an AST is represented as a <code>java.util.List</code> which
 * contains
 * <ul>
 * <li>the operator of a function (i.e. the &quot;header&quot;-symbol: Sin, Cos,
 * Inverse, Plus, Times,...) at index <code>0</code> and</li>
 * <li>the <code>n</code> arguments of a function in the index
 * <code>0 to n</code></li>
 * </ul>
 * 
 * See <a href="http://en.wikipedia.org/wiki/Abstract_syntax_tree">Abstract
 * syntax tree</a>.
 */
public interface IAST extends IExpr, INestedList<IExpr> {
	/**
	 * NO_FLAG ACTIVATED
	 */
	public final int NO_FLAG = 0x0000;

	/**
	 * The head or one of the arguments of the list contains a pattern object
	 */
	public final int CONTAINS_PATTERN = 0x0001;
	
	/**
	 * The head or one of the arguments of the list contains a pattern object
	 */
	public final int CONTAINS_PATTERN_SEQUENCE = 0x0002;

	/**
	 * One of the arguments of the list contains a pattern object which can be set
	 * to a default value.
	 */
	public final int CONTAINS_DEFAULT_PATTERN = 0x0004;

	/**
	 * Negative flag mask for CONTAINS_DEFAULT_PATTERN
	 */
	public final int CONTAINS_NO_DEFAULT_PATTERN_MASK = 0xFFFB;

	/**
	 * This expression represents a matrix
	 */
	public final int IS_MATRIX = 0x0020;

	/**
	 * This expression represents a vector
	 */
	public final int IS_VECTOR = 0x0040;

	/**
	 * This expression represents a matrix or vector if one of the following bits
	 * is set.
	 */
	public final int IS_MATRIX_OR_VECTOR = 0x0060;

	/**
	 * This expression represents an already decomposed partial fraction
	 * 
	 * @see Apart
	 */
	public final int IS_DECOMPOSED_PARTIAL_FRACTION = 0x0080;

	/**
	 * This expression is an already flattened expression
	 */
	public final int IS_FLATTENED = 0x0100;

	/**
	 * This expression is an already sorted expression
	 */
	public final int IS_SORTED = 0x0200;

	/**
	 * This expression is an already flattende or sorted expression
	 */
	public final int IS_FLATTENED_OR_SORTED_MASK = 0x0300;

	/**
	 * Get the evaluation flags for this list.
	 * 
	 * @return
	 */
	public int getEvalFlags();

	/**
	 * Returns the header. If the header itself is an ISymbol it will return the
	 * symbol object. If the header itself is an IAST it will recursively call
	 * headSymbol(). If the head is of type INumbers, the head will return one of
	 * these headers: "DoubleComplex", "Double", "Integer", "Fraction", "Complex".
	 * All other objects return <code>null</code>.
	 */
	public ISymbol topHead();

	/**
	 * Are the given evaluation flags disabled for this list ?
	 * 
	 * @return
	 */
	public boolean isEvalFlagOff(int i);

	/**
	 * Are the given evaluation flags enabled for this list ?
	 * 
	 * @return
	 */
	public boolean isEvalFlagOn(int i);

	/**
	 * Set the evaluation flags for this list.
	 * 
	 * @param i
	 */
	public void setEvalFlags(int i);

	/**
	 * Add an evaluation flag to the existing ones.
	 * 
	 * @param i
	 */
	public void addEvalFlags(int i);

	/**
	 * Appends all elements from offset <code>startPosition</code> to
	 * <code>endPosition</code> in the specified AST to the end of this AST.
	 * 
	 * @param ast
	 *          AST containing elements to be added to this AST
	 * @param startPosition
	 *          the start position, inclusive.
	 * @param endPosition
	 *          the ending position, exclusive.
	 * @return <tt>true</tt> if this AST changed as a result of the call
	 * @see #add(Object)
	 */
	public boolean addAll(List<? extends IExpr> ast, int startPosition, int endPosition);

	/**
	 * Appends all of the arguments (starting from offset <code>1</code>) in the
	 * specified AST to the end of this AST.
	 * 
	 * @param ast
	 *          AST containing elements to be added to this AST
	 * @return <tt>true</tt> if this AST changed as a result of the call
	 * @see #add(Object)
	 */
	public boolean addAll(List<? extends IExpr> ast);

	/**
	 * Is this a list (i.e. with header == List)
	 * 
	 * @return
	 */
	public boolean isList();

	public boolean isPlus();

	public boolean isPower();

	public boolean isTimes();

	/**
	 * Returns an iterator over the elements in this list starting with offset
	 * <b>1</b>.
	 * 
	 * @return an iterator over this list values.
	 */
	public Iterator<IExpr> iterator();

	/**
	 * Returns an iterator over the elements in this list starting with offset
	 * <b>0</b>.
	 * 
	 * 
	 * @return an iterator over this list values.
	 */
	public Iterator<IExpr> iterator0();

	/**
	 * Apply the given head to this expression (i.e. create a list clone and
	 * replace the old head with the given one)
	 * 
	 * @param head
	 * @return
	 */
	public IAST apply(IExpr head);

	/**
	 * Apply the given head to this expression (i.e. create a sublist clone
	 * starting from index start and replacing the old head with the given one)
	 * 
	 * @param head
	 * @return
	 */
	public IAST apply(IExpr head, int start);

	/**
	 * Apply the given head to this expression (i.e. create a sublist clone from
	 * index start to end, and replacing the old head with the given one)
	 * 
	 * @param head
	 * @return
	 */
	public IAST apply(IExpr head, int start, int end);

	/**
	 * Maps the elements of this IAST with the unary functor. If the function
	 * returns <code>null</code> the original element is used. If the function
	 * returns <code>null</code> for every argument this AST is returned.
	 * 
	 * <br />
	 * <br />
	 * Example for mapping with <code>Functors#replace1st()</code>, where the
	 * first argument will be replaced by the current argument of this AST:
	 * 
	 * <pre>
	 * plusAST.map(Functors.replace1st(F.D(F.Null, dAST.get(2))));
	 * </pre>
	 * 
	 * @param head
	 * @return
	 */
	public IAST map(final Function<IExpr, IExpr> functor);

	public IAST map(final IExpr head, final Function<IExpr, IExpr> functor);

	public IAST map(final IAST resultAST, final Function<IExpr, IExpr> functor);
	
	/**
	 * Maps the elements of this IAST with the elements of the
	 * <code>secondAST</code>.
	 * 
	 * @return the given resultAST.
	 * @throws IndexOutOfBoundsException
	 *           if the secondAST size is lesser than this AST size
	 */
	public IAST map(IAST resultAST, IAST secondAST, BiFunction<IExpr, IExpr, IExpr> function);

	/**
	 * Apply the predicate to each element in this <code>AST</code> and append the
	 * elements which satisfy the predicate to the <code>filterAST</code>.
	 * 
	 * @param filterAST
	 *          the elements which satisfy the predicate
	 * @param predicate
	 *          the predicate which filters each element in the range
	 * @return the <code>filterList</code>
	 */
	public IAST filter(IAST filterAST, Predicate<IExpr> predicate);

	/**
	 * Apply the predicate to each element in this <code>AST</code> and append the
	 * elements which satisfy the predicate to the <code>filterAST</code>, or
	 * otherwise append it to the <code>restAST</code>.
	 * 
	 * @param filterAST
	 *          the elements satisfy match the predicate
	 * @param restAST
	 *          the elements which don't match the predicate
	 * @param predicate
	 *          the predicate which filters each element in the range
	 * @return the <code>filterList</code>
	 */
	public IAST filter(IAST filterAST, IAST restAST, Predicate<IExpr> predicate);

	/**
	 * Apply the predicate to each element in this <code>AST</code> and append the
	 * elements which satisfy the predicate to the <code>0th element</code> of the
	 * result array, or otherwise append it to the <code>1st element</code> of the
	 * result array.
	 * 
	 * @param predicate
	 *          the predicate which filters each element in the range
	 * @return the resulting ASTs in the 0-th and 1-st element of the array
	 */
	public IAST[] split(Predicate<IExpr> predicate);

	/**
	 * Apply the function to each element in this <code>AST</code> and append the
	 * result elements for which the function returns non-null elements to the
	 * <code>0th element</code> of the result array, or otherwise append it to the
	 * <code>1st element</code> of the result array.
	 * 
	 * @param function
	 *          the function which filters each element in the range by returning
	 *          a non-null result.
	 * @return the resulting ASTs in the 0-th and 1-st element of the array
	 */
	public IAST[] split(final Function<IExpr, IExpr> function);

	/**
	 * Set the head element of this list
	 */
	public void setHeader(IExpr expr);

	/**
	 * Returns a shallow copy of this <code>INestedList</code> instance. (The
	 * elements themselves are not copied.)
	 * 
	 * @return a clone of this <code>IAST</code> instance.
	 */
	public IAST clone();

	/**
	 * Create a copy of this <code>IAST</code>, which only contains the head
	 * element of the list (i.e. the element with index 0).
	 */
	public IAST copyHead();

	/**
	 * Create a copy of this <code>IAST</code>, which contains alls elements up to
	 * <code>index</code> (exclusive).
	 */
	public IAST copyUntil(int index);

	/**
	 * Calculate a special hash value for pattern matching
	 * 
	 * @return
	 */
	public int patternHashCode();

	/**
	 * Get the range of elements [1..ast.size()[. These range elements are the
	 * arguments of a function (represented as an AST).
	 * 
	 * @return
	 */
	public ASTRange args();

	/**
	 * Get the range of elements [0..ast.size()[ of the AST. This range elements
	 * are the head of the function prepended by the arguments of a function.
	 * 
	 * @return
	 */
	public ASTRange range();

	/**
	 * Get the range of elements [start..sizeOfAST[ of the AST
	 * 
	 * @return
	 */
	public ASTRange range(int start);

	/**
	 * Get the range of elements [start..end[ of the AST
	 * 
	 * @return
	 */
	public ASTRange range(int start, int end);

	/**
	 * Casts an <code>IExpr</code> at position <code>index</code> to an
	 * <code>IInteger</code>.
	 * 
	 * @param index
	 * @return
	 * @throws WrongArgumentType
	 *           if the cast is not possible
	 */
	public IInteger getInt(int index);

	/**
	 * Casts an <code>IExpr</code> at position <code>index</code> to an
	 * <code>INumber</code>.
	 * 
	 * @param index
	 * @return
	 * @throws WrongArgumentType
	 *           if the cast is not possible
	 */
	public INumber getNumber(int index);

	/**
	 * Casts an <code>IExpr</code> at position <code>index</code> to an
	 * <code>IAST</code>.
	 * 
	 * @param index
	 * @return
	 * @throws WrongArgumentType
	 *           if the cast is not possible
	 */
	public IAST getAST(int index);

	/**
	 * Casts an <code>IExpr</code> which is a list at position <code>index</code>
	 * to an <code>IAST</code>.
	 * 
	 * @param index
	 * @return
	 * @throws WrongArgumentType
	 */
	public IAST getList(int index);
}
