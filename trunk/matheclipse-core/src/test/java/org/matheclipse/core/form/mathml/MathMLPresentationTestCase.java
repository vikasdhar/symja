package org.matheclipse.core.form.mathml;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.MathMLUtilities;
import org.matheclipse.core.expression.F;

/**
 * Tests MathML presentation function
 */
public class MathMLPresentationTestCase extends TestCase {

  MathMLUtilities mathUtil;

  public MathMLPresentationTestCase(String name) {
    super(name);
    F.initSymbols(null, null, false);
  }

  /**
   * Test mathml function
   */
  public void testMathMLPresentation() {
  	check("Abs[-x]", "<mrow><mo>&LeftBracketingBar;</mo><mrow><mo>-</mo><mi>x</mi></mrow><mo>&RightBracketingBar;</mo></mrow>");
    check(
        "a*b*c*d",
        "<mrow><mi>a</mi><mo>&#x2062;</mo><mi>b</mi><mo>&#x2062;</mo><mi>c</mi><mo>&#x2062;</mo><mi>d</mi></mrow>");
    check("k/2", "<mfrac><mi>k</mi><mn>2</mn></mfrac>");
    check(
        "Binomial[n,k/2]",
        "<mrow><mo>(</mo><mfrac linethickness=\"0\"><mi>n</mi><mfrac><mi>k</mi><mn>2</mn></mfrac></mfrac><mo>)</mo></mrow>");

    check(
        "a*b+c", 
        "<mrow><mi>c</mi><mo>+</mo><mrow><mi>a</mi><mo>&#x2062;</mo><mi>b</mi></mrow></mrow>");
    check("HEllipsis", "<mo>&hellip;</mo>");
    check(
        "MatrixForm[{a,b,c,d}]",
        "<mrow><mo>(</mo><mtable><mtr><mtd><mi>a</mi></mtd></mtr><mtr><mtd><mi>b</mi></mtd></mtr><mtr><mtd><mi>c</mi></mtd></mtr><mtr><mtd><mi>d</mi></mtd></mtr></mtable><mo>)</mo></mrow>");
    check(
        "MatrixForm[{{a,b},{c,d}}]",
        "<mrow><mo>(</mo><mtable><mtr><mtd><mi>a</mi></mtd><mtd><mi>b</mi></mtd></mtr><mtr><mtd><mi>c</mi></mtd><mtd><mi>d</mi></mtd></mtr></mtable><mo>)</mo></mrow>");

    check(
        "a*b+c",
        "<mrow><mi>c</mi><mo>+</mo><mrow><mi>a</mi><mo>&#x2062;</mo><mi>b</mi></mrow></mrow>");
    check(
        "a*b+c-2",
        "<mrow><mrow><mo>-</mo><mn>2</mn></mrow><mo>+</mo><mi>c</mi><mo>+</mo><mrow><mi>a</mi><mo>&#x2062;</mo><mi>b</mi></mrow></mrow>");
    check(
        "a*b+c-2-d",
        "<mrow><mrow><mo>-</mo><mi>d</mi></mrow><mo>-</mo><mn>2</mn><mo>+</mo><mi>c</mi><mo>+</mo><mrow><mi>a</mi><mo>&#x2062;</mo><mi>b</mi></mrow></mrow>");

    check(
        "a*(b+c)",
        "<mrow><mi>a</mi><mo>&#x2062;</mo><mrow><mrow><mo>(</mo><mi>c</mi><mo>+</mo><mi>b</mi><mo>)</mo></mrow></mrow></mrow>");
    check("I", "<mrow><mi>i</mi></mrow>");
    check("2I", "<mrow><mn>2</mn><mo>&#x2062;</mo><mrow><mi>i</mi></mrow></mrow>");
    check("2/3", "<mfrac><mn>2</mn><mn>3</mn></mfrac>");

    check("a+b", "<mrow><mi>b</mi><mo>+</mo><mi>a</mi></mrow>");
    check("a*b", "<mrow><mi>a</mi><mo>&#x2062;</mo><mi>b</mi></mrow>");
    check("a^b", "<msup><mi>a</mi><mi>b</mi></msup>");
    check("n!", "<mrow><mi>n</mi><mo>!</mo></mrow>");
    check(
        "4*x+4",
        "<mrow><mn>4</mn><mo>+</mo><mrow><mn>4</mn><mo>&#x2062;</mo><mi>x</mi></mrow></mrow>");

    check(
        "x^2+4*x+4==0",
        "<mrow><mrow><mn>4</mn><mo>+</mo><mrow><mn>4</mn><mo>&#x2062;</mo><mi>x</mi></mrow><mo>+</mo><msup><mi>x</mi><mn>2</mn></msup></mrow><mo>=</mo><mn>0</mn></mrow>");

    check("n!", "<mrow><mi>n</mi><mo>!</mo></mrow>");

  }

  public void check(String strEval, String strResult) {
    StringWriter stw = new StringWriter();
    mathUtil.toMathML(strEval, stw);
    // fParser.initialize(strEval);
    // Object obj = fParser.start();
    // StringBuffer buf = new StringBuffer();
    // fMathMLFactory.convert(buf, obj, 0);
    assertEquals(stw.toString(), "<?xml version=\"1.0\"?>\n" + 
    		"<!DOCTYPE math PUBLIC \"-//W3C//DTD MathML 2.0//EN\" \"http://www.w3.org/TR/MathML2/dtd/mathml2.dtd\">\n" + 
    		"<math mode=\"display\">\n" + strResult + "</math>");

  }

  /**
   * The JUnit setup method
   */
  protected void setUp() {
    try {
      EvalEngine engine = new EvalEngine();
      mathUtil = new MathMLUtilities(engine, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    // fParser = new Parser(null);
    // ExpressionFactory factory = new ExpressionFactory(new Namespace());
    // fParser.setFactory(factory);
    // fMathMLFactory = new MathMLFormFactory(factory);
  }

}
