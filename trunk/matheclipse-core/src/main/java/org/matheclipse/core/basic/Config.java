package org.matheclipse.core.basic;

import org.apache.commons.math3.util.Precision;

/**
 * 
 */
public class Config {
	/**
	 * COMPILER switch - set this boolean variable to <code>true</code>, if you
	 * would force a direct plot frame creation from the Plot[], Plot3D[] and
	 * ParametricPlot[] functions
	 * 
	 * On the server this switch should be set to <code>false</code>
	 */
	public static boolean SWING_PLOT_FRAME = false;

	/**
	 * The time in milliseconds an evaluation thread should run.<br/>
	 * 0 => forever
	 * 
	 */
	public static long TIME_CONSTRAINED_MILLISECONDS = 60000L;

	/**
	 * The time in milliseconds an evaluation thread should run.<br/>
	 * 0 => forever
	 * 
	 */
	public final static long FOREVER = 0L;

	/**
	 * The time in milliseconds an evaluation thread should sleep until
	 * <code>Thread#stop()</code> will be called.
	 * 
	 */
	public final static long TIME_CONSTRAINED_SLEEP_MILLISECONDS = 500;

	/**
	 * Switch debug mode on/off
	 * 
	 */
	public final static boolean DEBUG = false;

	/**
	 * Show the stack trace, if an exception is thrown in evaluation
	 * 
	 */
	public final static boolean SHOW_STACKTRACE = true;

	/**
	 * Show the console output, if an expression has a head symbol with attribute
	 * <code>ISymbol.CONSOLE_OUTPUT</code>.
	 * 
	 */
	public final static boolean SHOW_CONSOLE = true;

	/**
	 * Version string for console application
	 * 
	 */
	// public final static String VERSION = "00.00.70.00";

	/**
	 * <code>true</code> if the engine is started by a servlet<br/>
	 * In <i>server mode</i> the user can only assign values to variables with
	 * prefix '$' <br/>
	 * <br/>
	 * SERVER_MODE should be set to <code>true</code> in the initialization of a
	 * servlet
	 * 
	 */
	public static boolean SERVER_MODE = false;

	/**
	 * See <a href="http://en.wikipedia.org/wiki/Machine_epsilon">Wikipedia:
	 * Machine epsilon</a>
	 */
	public static double DOUBLE_EPSILON = Precision.EPSILON; 

	/**
	 * Maximum size of the BigInteger words in <i>server mode</i>.
	 * 
	 * @see apache.harmony.math.BigInteger
	 */
	public static int BIGINTEGER_MAX_SIZE = 65536;

	/**
	 * Maximum size of the FastTable entries in <i>server mode</i>.
	 * 
	 * @see javolution.util.FastTable
	 */
	public static int FASTTABLE_MAX_SIZE = 65536;

	/**
	 * Maximum length of the StringImpl's <code>String</code> in <i>server
	 * mode</i>.
	 * 
	 * @see org.matheclipse.core.expression.StringImpl
	 */
	public static int STRING_MAX_SIZE = 4096;

	/**
	 * Maximum size of the AST pools in <i>server mode</i>.
	 * 
	 * @see org.matheclipse.core.expression.AST
	 */
	public static int AST_MAX_POOL_SIZE = 65536;

	/**
	 * Maximum size of the IntegerImpl pools in <i>server mode</i>.
	 * 
	 * @see org.matheclipse.core.expression.IntegerImpl
	 */
	public static int INTEGER_MAX_POOL_SIZE = 4096;

	/**
	 * Maximum size of the FractionImpl pools in <i>server mode</i>.
	 * 
	 * @see org.matheclipse.core.expression.FractionImpl
	 */
	public static int FRACTION_MAX_POOL_SIZE = 4096;

	/**
	 * Maximum size of the ComplexImpl pools in <i>server mode</i>.
	 * 
	 * @see org.matheclipse.core.expression.ComplexImpl
	 */
	public static int COMPLEX_MAX_POOL_SIZE = 4096;

	/**
	 * Maximum size of the DoubleImpl pools in <i>server mode</i>.
	 * 
	 * @see org.matheclipse.core.expression.DoubleImpl
	 */
	public static int DOUBLE_MAX_POOL_SIZE = 65536;// 16384;

	/**
	 * Maximum size of the DoubleComplexImpl pools in <i>server mode</i>.
	 * 
	 * @see org.matheclipse.core.expression.DoubleComplexImpl
	 */
	public static int DOUBLECOMPLEX_MAX_POOL_SIZE = 65536;// 16384;

	/**
	 * Maximum size of the PatternImpl pools in <i>server mode</i>.
	 * 
	 * @see org.matheclipse.core.expression.PatternImpl
	 */
	public static int PATTERN_MAX_POOL_SIZE = 1024;

	/**
	 * Maximum size of the StringImpl pools in <i>server mode</i>.
	 * 
	 * @see org.matheclipse.core.expression.StringImpl
	 */
	public static int STRING_MAX_POOL_SIZE = 1024;

	/**
	 * Maximum size of the double vector's in <i>server mode</i>.
	 * 
	 * @see org.matheclipse.core.basic.Alloc
	 */
	public static int MAX_DOUBLE_VECTOR_SIZE = 65536;

	/**
	 * Maximum size of the double matrix's in <i>server mode</i>.
	 * 
	 * @see org.matheclipse.core.basic.Alloc
	 */
	public static int MAX_DOUBLE_MATRIX_SIZE = 65536;
}
