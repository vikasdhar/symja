package org.matheclipse.core.system;

import org.matheclipse.core.system.AbstractTestCase;

/**
 * Tests for the Java port of the <a
 * href="http://www.apmaths.uwo.ca/~arich/">Rubi - rule-based integrator</a>.
 * 
 */
public class IntegrateTrigFunctionTestCase extends AbstractTestCase {
	public IntegrateTrigFunctionTestCase(String name) {
		super(name);
	}

	/**
	 * Test combinatorial functions
	 */
	public void testTrig003() {
		check("$f[a_.+b_.*c_]:={a,b,c};$f[x]", "{0,1,x}");
		check("$h[$g[a_.+b_.*c_]]:={a,b,c};$h[$g[x]]", "{0,1,x}");
		check("Integrate[Sin[b*x^2],x]",
				"FresnelS[b^(1/2)*x*(1/2)^(-1/2)*Pi^(-1/2)]*(1/2)^(1/2)*Pi^(1/2)*b^(-1/2)");

		check("Integrate[Sin[a + b*x],x]", "(-1)*Cos[b*x+a]*b^(-1)");
		check("Integrate[Sin[a + b*x]^2,x]", "-1/2*Cos[b*x+a]*b^(-1)*Sin[b*x+a]+1/2*x");
		check("Integrate[Sin[a + b*x]^3,x]", "(-1)*(-1/3*b^(-1)*Cos[b*x+a]^3+Cos[b*x+a]*b^(-1))");
		check("Integrate[Sin[a + b*x]^4,x]", "-3/8*Cos[b*x+a]*b^(-1)*Sin[b*x+a]-1/4*Cos[b*x+a]*b^(-1)*Sin[b*x+a]^3+3/8*x");
		check("Integrate[Sin[a + b*x]^5,x]", "(-1)*(1/5*b^(-1)*Cos[b*x+a]^5-2/3*b^(-1)*Cos[b*x+a]^3+Cos[b*x+a]*b^(-1))");

		check("Integrate[Sin[a + b*x]^(1/2),x]", "2*EllipticE[1/2*(b*x+a-1/2*Pi),2]*b^(-1)");
		check("Integrate[Sin[a + b*x]^(3/2),x]", "-2/3*Cos[b*x+a]*b^(-1)*Sin[b*x+a]^(1/2)+2/3*EllipticF[1/2*(b*x+a-1/2*Pi),2]*b^(\n"
				+ "-1)");
		check("Integrate[Sin[a + b*x]^(5/2),x]", "-2/5*Cos[b*x+a]*b^(-1)*Sin[b*x+a]^(3/2)+6/5*EllipticE[1/2*(b*x+a-1/2*Pi),2]*b^(\n"
				+ "-1)");
		 
		check("Integrate[x*Sin[a + b*x],x]", "b^(-2)*Sin[b*x+a]-x*Cos[b*x+a]*b^(-1)");
		check("Integrate[x*Sin[a + b*x]^2,x]", "-1/2*x*Cos[b*x+a]*b^(-1)*Sin[b*x+a]+1/4*b^(-2)*Sin[b*x+a]^2+1/4*x^2");
		check("Integrate[x*Sin[a + b*x]^3,x]",
				"2/3*b^(-2)*Sin[b*x+a]+1/9*b^(-2)*Sin[b*x+a]^3-1/3*x*Cos[b*x+a]*b^(-1)*Sin[b*x+a]^\n" + 
				"2-2/3*x*Cos[b*x+a]*b^(-1)");
		// TODO
		check("Integrate[x^2*Sin[a + b*x],x]", "2*b^(-2)*x*Sin[b*x+a]-Cos[b*x+a]*b^(-1)*x^2+2*Cos[b*x+a]*b^(-3)");
		check("Integrate[x^2*Sin[a + b*x]^2,x]",
				"-1/2*Cos[b*x+a]*b^(-1)*x^2*Sin[b*x+a]+1/2*b^(-2)*x*Sin[b*x+a]^2+1/6*x^3-(-1/4*Cos[b*x+a]*b^(\n"
						+ "-3)*Sin[b*x+a]+1/4*b^(-2)*x)");
		// TODO
		check("Integrate[Sin[x]/Sqrt[x],x]", "Integrate[x^(-1/2)*Sin[x],x]");

		check("Integrate[Sqrt[a*Sin[x]],x]", "2*EllipticE[1/2*(x-1/2*Pi),2]*Sin[x]^(-1/2)*(a*Sin[x])^(1/2)");
		// TODO wrong result?
		check("Integrate[Sqrt[a*Sin[x]^2],x]", "(-1)*Cos[x]*a^(1/2)");

		check("Integrate[(a + b*Sin[c + d*x]),x]", "a*x-b*Cos[d*x+c]*d^(-1)");
		check("Integrate[(a + b*Sin[c + d*x])^2,x]", "-1/2*Cos[d*x+c]*b^2*d^(-1)*Sin[d*x+c]+1/2*b^2*x+a^2*x-2*a*b*Cos[d*x+c]*d^(-1)");
		// check("Integrate[,x]", "");
	}
}