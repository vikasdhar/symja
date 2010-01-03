package org.matheclipse.core.system;

import org.matheclipse.core.expression.Matrix;
import org.matheclipse.core.expression.Vector;

/**
 * Tests system.reflection classes
 */
public class SystemTestCase extends AbstractTestCase {

  public SystemTestCase(String name) {
    super(name);
  }

  // public void testSystemCream000() {
  // check("Cream[{x>=0,y>=0,x+y==7,2*x+4*y==20},{x,y}]", "{x->4,y->3}");
  // }

  /**
   * Test system functions
   */
  public void testSystem000() {
    check("1^(-1)", "1");
    check("test+0", "test");
    check("Times[3, Power[1, -1]]", "3");
    check("%", "3");
    check("%%%*%2", "test^2");
    check("1-x", "-x+1");
    check("5+x^4*(33+x^2)", "(x^2+33)*x^4+5");
    check("x^(-7)", "x^(-7)");
    check("x^(-7.0)", "x^(-7.0)");
    check("x^(1+I*3)", "x^(1+I*3)");
    check("x^(-1+I*3)", "x^(-1+I*3)");
    check("x^(1.0+I*3)", "x^(1.0+I*3.0)");
    check("x^(-1+I*3.0)", "x^(-1.0+I*3.0)");
    check("x^(I*3)", "x^(I*3)");
    check("x^(I*3.0)", "x^(0.0+I*3.0)");
    check("x^(-I*3)", "x^(-I*3)");
    check("x^(-I*3.0)", "x^(0.0+I*(-3.0))");
    check("Sin[3/10*Pi]", "1/4*5^(1/2)+1/4");
    check("Sin[Pi/5]", "1/4*2^(1/2)*(-5^(1/2)+5)^(1/2)");
    check("2^(-1)", "1/2");
    check("x^3+x^2+x+42", "x^3+x^2+x+42");
    check("x^3+2*x^2+4*x+3", "x^3+2*x^2+4*x+3");
    check("y*x^3+y*x^2+y*x+y+x+42", "x^3*y+x^2*y+x*y+y+x+42");
    check("2I", "I*2");
  }

  public void testSystem000a() {
    check("Rational[2,3]", "2/3");
    check("Rational[3,1]", "3");
    check("Rationalize[6.75]", "27/4");
    check("Rationalize[42]", "42");
    check("Rationalize[0.25+I*0.33333]", "1/4+I*33333/100000");
  }

  public void testSystem000b() {
    check("Complex[2,1/3]", "2+I*1/3");
    check("Complex[3/4,3]", "3/4+I*3");
  }

  // public void testSystemRelaxedSyntax() {
  // check("1^(-1)", "1", true);
  // check("test+0", "test", true);
  // check("Times(3, Power(1, -1))", "3", true);
  // check("%", "3", true);
  // check("%%%*%2", "test^2", true);
  // check("1-x", "1-x", true);
  // check("5+x^4*(33+x^2)", "5+(33+x^2)*x^4", true);
  // check("x^(-7)", "x^(-7)", true);
  // check("x^(-7.0)", "x^(-7.0)", true);
  // check("x^(1+I*3)", "x^(1+I*3)", true);
  // check("x^(-1+I*3)", "x^(-1+I*3)", true);
  // check("x^(1.0+I*3)", "x^(1.0+I*3.0)", true);
  // check("x^(-1+I*3.0)", "x^(-1.0+I*3.0)", true);
  // check("x^(I*3)", "x^(I*3)", true);
  // check("x^(I*3.0)", "x^(0.0+I*3.0)", true);
  // check("x^(-I*3)", "x^(-I*3)", true);
  // check("x^(-I*3.0)", "x^(0.0+I*(-3.0))", true);
  // check("Sin(3/10*Pi)", "1/4+1/4*5^(1/2)", true);
  // check("Sin(Pi/5)", "1/4*2^(1/2)*(5-5^(1/2))^(1/2)", true);
  // check("2^(-1)", "1/2", true);
  // check("f(x)", "f(x)", true);
  // check("{a,b,c,d}[y]", "{a,b,c,d}[y]", true);
  // check("Rational(2,3)", "2/3", true);
  // check("Rational(3,1)", "3", true);
  // check("Complex(2,1/3)", "2+I*1/3", true);
  // check("Complex(3/4,3)", "3/4+I*3", true);
  // }

  public void testSystem001() {
    check("Sin[0.5]", "0.479425538604203");
  }

  public void testSystem002() {
    check("1+1", "2");

  }

  public void testSystem003() {
    check("-0.0001E+15", "-1.0E11");
  }

  public void testSystem004() {
    check("-0.0001E-15", "-1.0E-19");
  }

  // public void testSystem004() {
  // check("-0.0001E15", "-10.0E-5*E15");
  // }

  // syntax error:
  // public void testSystem014a { check("-0.0001E+",""); }

  public void testSystem005() {
    check("1/3+1/4", "7/12");
  }

  public void testSystem006() {
    check("2/3*3/4", "1/2");
  }

  public void testSystem007() {
    check("(-27)^(1/3)", "-3");
    check("(-27)^(2/3)", "9");
    check("8^(1/3)", "2");
    check("81^(3/4)", "27");
    check("82^(3/4)", "82^(3/4)");
    check("(20/7)^(-1)", "7/20");
    check("(-27/64)^(1/3)", "-3/4");
    check("(27/64)^(-2/3)", "16/9");
    // check("16/9","");
    check("10^4", "10000");
  }

  public void testSystem008() {
    check("I^(-1)", "-I");
  }

  public void testSystem009() {
    check("1/2-I", "1/2-I");
  }

  public void testSystem010() {
    check("1/2+I*(-1/3)", "1/2-I*1/3");
  }

  public void testSystem011() {
    check("0.5-I", "0.5+I*(-1.0)");
  }

  public void testSystem012() {
    check("$a=2;$a+=b", "b+2");
  }

  public void testSystem013() {
    check("$a=2;$a-=b", "-b+2");
  }

  public void testSystem014() {
    check("$a=2;$a*=b", "2*b");
  }

  public void testSystem015() {
    check("$a=2;$a/=b", "2*b^(-1)");
  }

  public void testSystem016() {
    check("Depth[13]", "1");
  }

  public void testSystem017() {
    check("Depth[{}]", "2");
  }

  public void testSystem018() {
    check("Depth[f[x]]", "2");
  }

  public void testSystem019() {
    check("Depth[f[x,g[y]]]", "3");

  }

  public void testSystem020() {
    check("LeafCount[s[x, y]]", "3");
  }

  public void testSystem021() {
    check("LeafCount[s[a][x, y]]", "4");
  }

  public void testSystem022() {
    check("LeafCount[a]", "1");
  }

  public void testSystem023() {
    check("LeafCount[{}]", "1");
  }

  public void testSystem024() {
    check("Map[f,a]", "a");
    check("f/@a", "a");
  }

  public void testSystem025() {
    check("Map[f, s[g[u,v], y],{-3,-2}]", "f[s[f[g[u,v]],y]]");
  }

  public void testSystem026() {
    check("Map[f, s[g[u,v], y],{-2,-1}]", "s[f[g[f[u],f[v]]],f[y]]");

  }

  public void testSystem027() {
    check("Map[f, s[g[u,v], y],{-2}]", "s[f[g[u,v]],y]");
  }

  public void testSystem028() {
    check("Map[f, s[x, y]]", "s[f[x],f[y]]");
    check("f/@s[x, y]", "s[f[x],f[y]]");
  }

  public void testSystem029() {
    check("Map[(#+2)&, s[x, y]]", "s[x+2,y+2]");
    check("(#+2)&/@s[x, y]", "s[x+2,y+2]");
  }

  public void testSystem030() {
    check("Map[f, s[g[u,v], y]]", "s[f[g[u,v]],f[y]]");
    check("f/@ s[g[u,v], y]", "s[f[g[u,v]],f[y]]");
  }

  public void testSystem031() {
    check("Map[f, s[g[u,v], y],{2}]", "s[g[f[u],f[v]],y]");
  }

  public void testSystem032() {
    check("Map[(#+2)&, s[g[u,v], y],{2}]", "s[g[u+2,v+2],y]");
  }

  public void testSystem033() {
    check("Map[f, s[g[u[x[1]],v], y],{2,-1}]", "s[g[f[u[f[x[f[1]]]]],f[v]],y]");
  }

  public void testSystem034() {
    check("Map[f, s[g[u,v], y],{2,-1}]", "s[g[f[u],f[v]],y]");

  }

  public void testSystem035() {
    check("MapAll[f, s[x, y]]", "f[s[f[x],f[y]]]");
    check("f//@s[x, y]", "f[s[f[x],f[y]]]");
    check("MapAll[f,a]", "f[a]");
    check("f//@a", "f[a]");
  }

  public void testSystem036() {
    check("Thread[f[{x,y,z},{u,v,w}]]", "{f[x,u],f[y,v],f[z,w]}");
    check("Thread[{x,y,z}=={u,v,w}]", "{x==u,y==v,z==w}");
    check("Thread[f[x==y],Equal]", "f[x]==f[y]");
    // check("MapThread[f, {{x,y,z},{u,v,w}}]", "{f[x,u],f[y,v],f[z,w]}");
    // check("MapThread[f, {{{x,y,z},{u,v,w}}}, 2]", "");
  }

  public void testSystem037() {
    check("Trace[a]", "{}");
  }

  public void testSystem038() {
    check("Trace[D[Sin[x],x]]",
        "{D[Sin[x],x],Cos[x]*D[x,x],{D[x,x],1},1*Cos[x],Cos[x]}");
    check("D[Sin[x]^Cos[x],x]",
        "(-Log[Sin[x]]*Sin[x]+Cos[x]^2*Sin[x]^(-1))*Sin[x]^Cos[x]");
    check(
        "Trace[D[Sin[x]^Cos[x],x]]",
        "{D[Sin[x]^Cos[x],x],Sin[x]^Cos[x]*(Log[Sin[x]]*D[Cos[x],x]+Cos[x]*D[Sin[x],x]*Sin[x]^(\n"
            + "-1)),{D[Sin[x],x],Cos[x]*D[x,x],{D[x,x],1},1*Cos[x],Cos[x]},{Cos[x]*D[Sin[x],x]*Sin[x]^(\n"
            + "-1),Cos[x]*Cos[x]*Sin[x]^(-1),Cos[x]^2*Sin[x]^(-1)},{D[Cos[x],x],(-1)*Sin[x]*D[x,x],(\n"
            + "-1)*Sin[x]*D[x,x],{D[x,x],1},(-1)*1*Sin[x],(-1)*Sin[x]},{Log[Sin[x]]*D[Cos[x],x],Log[Sin[x]]*(\n"
            + "-1)*Sin[x],(-1)*Log[Sin[x]]*Sin[x]},{Log[Sin[x]]*D[Cos[x],x]+Cos[x]*D[Sin[x],x]*Sin[x]^(\n"
            + "-1),-Log[Sin[x]]*Sin[x]+Cos[x]^2*Sin[x]^(-1)},(-Log[Sin[x]]*Sin[x]+Cos[x]^2*Sin[x]^(\n"
            + "-1))*Sin[x]^Cos[x]}");
    check("Trace[NumberQ[1/3]]", "{NumberQ[1/3],True}");
  }

  public void testSystem039() {
    check("a+a", "2*a");

    // test numericMode:
  }

  public void testSystem040() {
    check("N[Sin[1/2]]", "0.479425538604203");
    check("N[1/6*(I*44^(1/2)+2)]", "0.3333333333333333+I*1.1055415967851332");
    // test automatic numericMode (triggered by double value "0.5"):
  }

  public void testSystem041() {
    check("Sin[0.5]", "0.479425538604203");
  }

  public void testSystem042() {
    check("Sin[Pi]", "0");
  }

  public void testSystem043() {
    check("Sin[ArcSin[a]]", "a");

  }

  public void testSystem044() {
    check("a[b[x_],_]", "a[b[x_],_]");

  }

  public void testSystem045() {
    check("MemberQ[{g[x], f[a]}, f[x_]]", "True");
  }

  public void testSystem046() {
    check("MemberQ[{g[x, c], f[a, b]}, f[x_, b]]", "True");
  }

  public void testSystem047() {
    check("MemberQ[{g[x], f[a, b]}, f[x_]]", "False");
  }

  public void testSystem048() {
    check("MemberQ[{g[x, c], f[a, b, c]}, f[x_, b]]", "False");

  }

  public void testSystem049() {
    check("FreeQ[{g[x], f[a]}, f[x_]]", "False");
    check("FreeQ[{g[x], f[a]}, h[x_]]", "True");
  }

  public void testSystem050() {
    check("FreeQ[{g[x,3], f[a]}, 3]", "False");
  }

  public void testSystem051() {
    check("FreeQ[3, 2]", "True");
  }

  public void testSystem052() {
    check("FreeQ[{g[x,3], f[a]}, 2]", "True");
  }

  public void testSystem053() {
    check("FreeQ[{g[x], f[a]}, f[_Integer]]", "True");

  }

  public void testSystem054() {
    check("NumberQ[1/3]", "True");
  }

  public void testSystem055() {
    check("NumberQ[2.5]", "True");

  }

  public void testSystem056() {
    check("PrimeQ[997]", "True");
    check("NextPrime[41]", "43");
    check("NextPrime[37]", "41");
    check("NextPrime[37,2]", "43");
    check("NextPrime[37,3]", "47");
    check("CoprimeQ[6,35]", "True");
    check("CoprimeQ[6,27]", "False");
    check("CoprimeQ[6,35,49]", "False");
  }

  public void testSystem057() {
    check("$var=10", "10");
    check("$var", "10");
    check("$var+$var", "20");
  }

  public void testSystem058() {
    check("$function[2]=42", "42");
    check("$function[2]", "42");
  }

  // two rules are associated with condf
  public void testSystem059() {
    check("$condf[x_,y_]:={x,y}/;NumberQ[x];$condf[x_,y_]:={y,x}/;NumberQ[y]",
        "");
    check("$condf[c,7]", "{7,c}");
    check("{$condf[a,a],$condf[42,b],$condf[c,7]}",
        "{$condf[a,a],{42,b},{7,c}}");
    // only the last rule is associated with condg
    check(
        "$condg[x_,y_]={x,y};$condg[x_,y_]:={y,x};{$condg[a,b],$condg[42,b],$condg[c,7]}",
        "{{b,a},{b,42},{7,c}}");

  }

  public void testSystem060() {
    check("$pf[y_]:={y}", "");
    check("$pf[test]", "{test}");
    check("$pf[y_,a]:={y}", "");
    check("$pf[test,a]", "{test}");
    check("$pf[test,b]", "$pf[test,b]");
  }

  // test attribute ISymbol.FLAT
  public void testSystem061() {
    check("SetAttributes[$f, Flat]", "");
    check("$f[a,b,$f[x,y,$f[u,v]],z]", "$f[a,b,x,y,u,v,z]");
    check("$f[x_,y_]:={x,y}", "");
    check("$f[a,b,c]", "{$f[a],{b,c}}");
  }

  // test attribute ISymbol.ORDERLESS
  public void testSystem062() {
    check("SetAttributes[$o, Orderless]", "");
    check("$o[z,d,a,b,g]", "$o[a,b,d,g,z]");
    check("$o[9,12,3,33]", "$o[3,9,12,33]");
    check("$o[x_,y_]:={x,y}", "");
    check("$o[a,10]", "{10,a}");
  }

  // test attribute ISymbol.ONEIDENTITY
  public void testSystem063() {
    check("SetAttributes[$oi, OneIdentity]", "");
    check("$oi[$oi[test]]", "test");

  }

  public void testSystem064() {
    check("SetAttributes[$ooi, {Orderless, OneIdentity}]", "");
    check("$ooi[z,d,a,b,g]", "$ooi[a,b,d,g,z]");
    check("$ooi[9,12,3,33]", "$ooi[3,9,12,33]");
    check("$ooi[x_NumberQ,y_]:={x,y}", "");
    check("$ooi[a,10]", "{10,a}");
  }

  // test attribute ISymbol.ORDERLESS && ISymbol.FLAT
  public void testSystem065() {
    check("SetAttributes[$of, {Orderless,Flat}]", "");
    check("$of[z,d,a,b,g]", "$of[a,b,d,g,z]");
    check("$of[9,12,3,33]", "$of[3,9,12,33]");
    check("$of[x_,y_]:={x,y}", "");
    check("$of[a,10]", "{10,a}");
    check("$of[a,10,b]", "{$of[10],{a,b}}");
    check("$of[a,10,b,c]", "{$of[10],{$of[a],{b,c}}}");
  }

  // test attribute ISymbol.ORDERLESS && ISymbol.FLAT &&
  // ISymbol.ONEIDENTITY
  public void testSystem066() {
    check("SetAttributes[$ofoi, {Orderless,Flat,OneIdentity}]", "");
    check("$ofoi[z,d,a,$ofoi[b],g]", "$ofoi[a,b,d,g,z]");
    check("$ofoi[9,12,$ofoi[3,33]]", "$ofoi[3,9,12,33]");
    check("$ofoi[x_,y_]:={x,y}", "");
    check("$ofoi[a,10]", "{10,a}");
    check("$ofoi[a,10,b,c]", "{10,{a,{b,c}}}");
  }

  // test attribute ISymbol.LISTABLE
  public void testSystem067() {
    check("Sin[{a,b,c}]", "{Sin[a],Sin[b],Sin[c]}");
  }

  public void testSystem068() {
    check("NumberQ[{a,b,c}]", "{False,False,False}");
  }

  public void testSystem069() {
    check("SetAttributes[$l, Listable]", "");
    check("$l[{a,b,c},d]", "{$l[a,d],$l[b,d],$l[c,d]}");

  }

  public void testSystem070() {
    check("Degree", "1/180*Pi");
    check("GoldenRatio", "1/2*(5^(1/2)+1)");
  }

  public void testSystem071() {
    check("N[EulerGamma]", "0.5772156649015329");

  }

  public void testSystem072() {
    check("D[Sinh[x],x]", "Cosh[x]");
  }

  public void testSystem073() {
    check("D[Log[Sin[x]],x]", "Cos[x]*Sin[x]^(-1)");
  }

  public void testSystem074() {
    check("D[f[a]^2+g[x]^3,x]", "3*D[g[x],x]*g[x]^2");
  }

  public void testSystem075() {
    check("D[f[x]^2+g[x]^3,x]", "2*D[f[x],x]*f[x]+3*D[g[x],x]*g[x]^2");
  }

  public void testSystem076() {
    check("D[2*x^2 + 1,x] ", "4*x");
  }

  public void testSystem077() {
    check("D[Sin[x]Cos[x],x]", "-Sin[x]^2+Cos[x]^2");
  }

  public void testSystem078() {
    check("D[Sin[x]^Cos[x],x]",
        "(-Log[Sin[x]]*Sin[x]+Cos[x]^2*Sin[x]^(-1))*Sin[x]^Cos[x]");
  }

  public void testSystem079() {
    check("{{1,2},{3,4}}.{{1,2},{3,4}}", "{{7,10},\n" + " {15,22}}");
  }

  public void testSystem080() {
    check("{{1,2},{3,4}}.{{1,2},{3,-4}}.{{1,2},{3,4}}", "{{-11,-10},\n"
        + " {-15,-10}}");
  }

  public void testSystem081() {
    check("Inverse[{{1,2},{3,4}}]", "{{-2,1},\n" + " {3/2,-1/2}}");
    check("Inverse[{{1,2.0},{3,4}}]", "{{-1.9999999999999998,1.0},\n"
        + " {1.4999999999999998,-0.49999999999999994}}");
  }

  public void testSystem082() {
    check("Det[{{1,2},{3,4}}]", "-2");
    check("Det[{{1,2.0},{3,4}}]", "-2.0");
    check("Det[{{a,b},{c,d}}]", "a*(d-a^(-1)*b*c)");
  }

  public void testSystem083() {
    check("Eigenvalues[{{0.0,1.0,-1.0},{1.0,1.0,0.0},{-1.0,0.0,1.0}}]",
        "{2.0,1.0,-1.0000000000000004}");
    check("Eigenvalues[{{1,0,0},{0,1,0},{0,0,1}}]", "{1.0,1.0,1.0}");
    check("Eigenvalues[{{1,0,0},{-2,1,0},{0,0,1}}]",
        "eigen decomposition of assymetric matrices not supported yet");

    check(
        "Fit[{2,3,5,7,11,13},3,x]",
        "-0.08333333333333395*x^3.0+1.1071428571428645*x^2.0-1.9523809523809792*x+3.0000000000000293");
    check("Fit[{{1,1},{2,4},{3,9},{4,16}},2,x]", "x^2.0");
    check("Fit[{1,4,9,16},2,x]", "x^2.0");

    // double[][] m = { { 0.0, 1.0, -1.0 }, { 1.0, 1.0, 0.0 }, { -1.0, 0.0, 1.0
    // } };
    // RealMatrix rm = new Array2DRowRealMatrix(m);
    // assertEquals(rm.toString(),
    // "Array2DRowRealMatrix{{0.0,1.0,-1.0},{1.0,1.0,0.0},{-1.0,0.0,1.0}}");
    // EigenDecompositionImpl ed = new EigenDecompositionImpl(rm,
    // MathUtils.SAFE_MIN);
    // RealVector rv0 = ed.getEigenvector(0);
    // RealVector rv1 = ed.getEigenvector(1);
    // RealVector rv2 = ed.getEigenvector(2);
    // assertEquals(rv0.toString(), "{0,58; 0,58; -0,58}");
    // assertEquals(rv1.toString(), "{-0; -0,71; -0,71}");
    // assertEquals(rv2.toString(), "{0,82; -0,41; 0,41}");
    // check(
    // "Eigenvectors[{{0.0,1.0,-1.0},{1.0,1.0,0.0},{-1.0,0.0,1.0}}]",
    // "{{0.577350269189626,0.5773502691896254,-0.5773502691896257},"
    // + "{-1.73268085632541E-16,-0.7071067811865476,-0.7071067811865474},"
    // + "{0.8164965809277261,-0.40824829046386296,0.40824829046386296}}");
    // check(
    // "{{0.0,1.0,-1.0},{1.0,1.0,0.0},{-1.0,0.0,1.0}}.{-1.73268085632541E-16,-0.7071067811865476,-0.7071067811865474}",
    // "{-2.220446049250313E-16,-0.7071067811865478,-0.7071067811865471}");
  }

  public void testSystem084() {
    // trace of a matrix
    check("Tr[{{1,2},{3,4}}]", "5");
    check("Tr[{{1,2},{3,4},{5,6}}]", "5");
    check("Tr[{{1,2,5},{3,4,6}}]", "5");
  }

  public void testSystem085() {
    check("Tr[{{a,b,c},{d,e,f}}]", "e+a");
  }

  public void testSystem086() {
    check("Transpose[{{1,2},{3,4}}]", "{{1,3},\n" + " {2,4}}");
  }

  public void testSystem087() {
    check("Transpose[{{1,2},{3,4},{5,6}}]", "{{1,3,5},\n" + " {2,4,6}}");
  }

  public void testSystem088() {
    check("MatrixPower[{{1,2},{3,4}},3]", "{{37,54},\n" + " {81,118}}");
  }

  public void testSystem089() {
    check("MatrixPower[{{1,2},{3,4}},1]", "{{1,2},\n {3,4}}");
  }

  public void testSystem090() {
    check("MatrixPower[{{1,2},{3,4}},0]", "{{1,0},\n" + " {0,1}}");
  }

  public void testSystem091() {
    check("HilbertMatrix[4]", "{{1,1/2,1/3,1/4}," + "{1/2,1/3,1/4,1/5},"
        + "{1/3,1/4,1/5,1/6}," + "{1/4,1/5,1/6,1/7}}");
    check("HilbertMatrix[2,3]", "{{1,1/2,1/3},{1/2,1/3,1/4}}");
    check("HilbertMatrix[3,2]", "{{1,1/2},{1/2,1/3},{1/3,1/4}}");
  }

  public void testSystem092() {
    check("IdentityMatrix[0]", "{}");
    check("IdentityMatrix[1]", "{{1}}");
  }

  public void testSystem093() {
    check("IdentityMatrix[3]", "{{1,0,0},\n" + " {0,1,0},\n" + " {0,0,1}}");
  }

  public void testSystem094() {
    check("VandermondeMatrix[{a,b,c,d}]",
        "{{1,a,a^2,a^3},{1,b,b^2,b^3},{1,c,c^2,c^3},{1,d,d^2,d^3}}");
  }

  public void testSystem095() {
    check("MatrixQ[{{1, 2, 3}, {3, 4, 11}, {13, 7, 8}}]", "True");
  }

  public void testSystem096() {
    check("MatrixQ[{{1, 2, 3}, {3, 4, 11}, {13, 7, 8, 6}}]", "False");
  }

  public void testSystem097() {
    check("VectorQ[{-11/4,33/4,-5/4}]", "True");
  }

  public void testSystem098() {
    check("VectorQ[{-11/4,33/4,{-5/4,b}}]", "False");
  }

  public void testSystem099() {
    check("{{1, 2, 3}, {3, 4, 11}, {13, 7, 8}}.{-11/4,33/4,-5/4}", "{10,11,12}");
  }

  public void testSystem100() {
    check("{-11/4,33/4,-5/4}.{{1, 2, 3}, {3, 4, 11}, {13, 7, 8}}",
        "{{23/4,75/4,145/2}}");
  }

  public void testSystem101() {
    // check("LUDecomposition[{{1,2},{3,4}}]", "{{{1,2},{3,-2}},{1,2},0}");
    check("LUDecomposition[{{1,2},{3,4}}]", "{{{1,0},\n"
        + " {3,1}},{{1,2},{0,-2}},{1,2}}");
    check("LUDecomposition[{{1,1},{5,-8}}]", "{{{1,0},\n"
        + " {5,1}},{{1,1},{0,-13}},{1,2}}");
  }

  // public void testSystem102() {
  // check("LUBackSubstitution[{{{1,2},{3,-2}},{1,2},0},{1,2}]", "{0,1/2}");
  // }
  public void testSystem102() {
    check(
        "LinearSolve[{ { 1/10, 6/5, 1/9 },{ 1, 59/45, 1/10 },{6/5, 1/10, 1/9 } },{ 1/10, 6/5, 1/9 }]",
        "{99109/101673,10898/11297,-9034/869}");
    check(
        "{ { 1/10, 6/5, 1/9 },{ 1, 59/45, 1/10 },{6/5, 1/10, 1/9 } }.{99109/101673,10898/11297,-9034/869}",
        "{1/10,6/5,1/9}");
  }

  public void testSystem103() {
    // check("LUDecomposition[{{1, 2, 3}, {3, 4, 11}, {13, 7, 8}}]",
    // "{{{1,2,3},{3,-2,2},{13,19/2,-50}},{1,2,3},0}");
    check("LUDecomposition[{{1, 2, 3}, {3, 4, 11}, {13, 7, 8}}]",
        "{{{1,0,0},\n" + " {3,1,0},\n"
            + " {13,19/2,1}},{{1,2,3},{0,-2,2},{0,0,-50}},{1,2,3}}");

    // check(
    // "LUBackSubstitution[{{{1,2,3},{3,-2,2},{13,19/2,-50}},{1,2,3},0},{10,11,12}]"
    // ,
    // "{-11/4,33/4,-5/4}");
    check(
        "SingularValueDecomposition[{{ 24.0/25.0, 43.0/25.0 },{57.0/25.0, 24.0/25.0 }}]",
        "{{{0.6000000000000001,-0.7999999999999996},\n"
            + " {0.8,0.6000000000000001}},{{3.0,0.0},\n"
            + " {0.0,1.0000000000000004}},{{0.7999999999999998,0.6000000000000001},\n"
            + " {0.6,-0.7999999999999998}}}");

    // WRONG results. See http://issues.apache.org/jira/browse/MATH-320:
    check(
        "SingularValueDecomposition[{{1,2},{1,2}}]",
        "{{{-0.7071067811865472},{-0.7071067811865475}},{{3.162277660168379}},{{-0.44721359549995804},\n"
            + " {-0.8944271909999159}}}");
  }

  public void testSystem104() {
    check("{{a,2}}.{{a},{3}}", "{{a^2+6}}");
  }

  public void testSystem105() {
    check("{{3,4}}.{{a},{3}}", "{{3*a+12}}");
  }

  public void testSystem106() {
    check("{{a,2},{3,4}}.{{a,2},{3,4}}", "{{a^2+6,2*a+8},{3*a+12,22}}");
  }

  public void testSystem107() {
    check("MatrixPower[{{a,2},{3,4}},3]",
        "{{a^3+12*a+24,2*a^2+8*a+44},{3*a^2+12*a+66,6*a+112}}");

  }

  public void testSystem108() {
    check("10!", "3628800");
  }

  public void testSystem109() {
    check("10!!", "3840");
    check("11!!", "10395");
    check("-10!!", "-3840");
    check("-11!!", "-10395");
    check("-12!!", "-46080");
    check("-13!!", "-135135");
  }

  public void testSystem110() {
    check("Table[Gamma[x],{x,10}]", "{1,1,2,6,24,120,720,5040,40320,362880}");
    check(
        "Table[Gamma[x],{x,10.0}]",
        "{0.9999999999999996,1.0,2.000000000000001,5.999999999999996,24.000000000000004,119.99999999999977,720.0000000000001,5039.999999999993,40320.00000000003,362879.9999999998}");
  }

  public void testSystem111() {
    check("ArcCos[I]", "-I*ArcSinh[1]+1/2*Pi");
    check("Exp[Pi*I]", "-1");
    check("E^(Pi*I)", "-1");
  }

  public void testSystem112() {
    check("Table[x!,{x,10}]", "{1,2,6,24,120,720,5040,40320,362880,3628800}");
    check("Table[x,{x,10.0}]", "{1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0}");
    check(
        "Table[x!,{x,10.0}]",
        "{1.0,2.000000000000001,5.999999999999996,24.000000000000004,119.99999999999977,720.0000000000001,5039.999999999993,40320.00000000003,362879.9999999998,3628800.0000000214}");
  }

  public void testSystem113() {

  }

  public void testSystem114() {

  }

  public void testSystem115() {
    check("\"test1 \"<>\"test2 \"<>\"test3 \"", "\"test1 test2 test3 \"");
  }

  public void testSystem116() {
    check("b**(a**c)", "b**a**c");
  }

  public void testSystem117() {
    check("b**13**14**d", "b**13**14**d");
  }

  public void testSystem118() {
    check("a==a==a", "True");
  }

  public void testSystem119() {
    check("a!=b!=a", "a!=b!=a");
  }

  public void testSystem120() {
    check("a===a===a", "True");
  }

  public void testSystem121() {
    check("a=!=b=!=c=!=d", "True");
  }

  public void testSystem122() {
    check("a=!=b=!=c=!=a", "False");
  }

  public void testSystem123() {
    check("I!=2/3!=42", "True");
  }

  public void testSystem124() {
    check("42!=2/3!=42", "False");
  }

  public void testSystem125() {
    check("SameQ[42]", "True");
  }

  public void testSystem126() {
    check("42.01===42.0", "False");
  }

  public void testSystem127() {
    check("5>4", "True");
  }

  public void testSystem128() {
    check("5>4>a>3", "4>a>3");
  }

  public void testSystem129() {
    check("5>4>a>3>2>c", "4>a>3>2>c");
  }

  public void testSystem130() {
    check("5>4>a>3>2>1>z>w", "4>a>3>1>z>w");
  }

  public void testSystem131() {
    check("5>=5", "True");
  }

  public void testSystem132() {
    check("5>=4>=a>=3", "4>=a>=3");
  }

  public void testSystem133() {
    check("5>=4>=a>=3>=2>=c", "4>=a>=3>=2>=c");
  }

  public void testSystem134() {
    check("5>=4>=a>=3>=2>=1>=z>=w", "4>=a>=3>=1>=z>=w");
  }

  public void testSystem135() {
    check("3<4<a<5<6", "4<a<5");
  }

  public void testSystem136() {
    check("4<=4<=a<=5<=6", "4<=a<=5");
  }

  public void testSystem137() {
    check("Fibonacci[10]", "55");
  }

  public void testSystem138() {
    check("Append[z[a,b,c],d]", "z[a,b,c,d]");
  }

  public void testSystem139() {
    check("Prepend[z[a,b,c],d]", "z[d,a,b,c]");
  }

  public void testSystem140() {
    check("First[z[1,2,3]]", "1");
    check("Last[z[1,2,3]]", "3");
    check("Rest[z[1,2,3]]", "z[2,3]");
    check("Most[z[1,2,3]]", "z[1,2]");
  }

  public void testSystem141() {
    check("10==10", "True");
  }

  public void testSystem142() {
    check("10==1/2", "False");
  }

  public void testSystem143() {
    check("I==1/3", "False");
  }

  public void testSystem144() {
    check("I!=1/3", "True");
  }

  public void testSystem145() {
    check("qr==10", "qr==10");
  }

  public void testSystem146() {
    check("I", "I");
  }

  public void testSystem147() {
    check("I*I", "-1");
  }

  public void testSystem148() {
    check("1+I", "1+I");
  }

  public void testSystem149() {
    check("1/3+(1/4)I", "1/3+I*1/4");
  }

  public void testSystem150() {
    check("3*a+4*a", "7*a");
  }

  public void testSystem151() {
    check("4*a+a", "5*a");
  }

  public void testSystem152() {
    check("(2*a*b)^(1/3)", "2^(1/3)*(a*b)^(1/3)");
  }

  public void testSystem153() {
    check("a^1/3*b*a^3", "1/3*a^4*b");
  }

  public void testSystem154() {
    check("a^(1/3)*b*a^3", "a^(10/3)*b");
  }

  public void testSystem155() {
    check("a^2*b*a^3", "a^5*b");
  }

  public void testSystem156() {
    check("Sqrt[-42]", "I*42^(1/2)");
  }

  public void testSystem157() {
    check("Sqrt[x]", "x^(1/2)");
  }

  public void testSystem158() {
    check("a*(-1/3)+b*(-1/3)", "-1/3*b-1/3*a");

  }

  public void testSystem159() {
    check("Binomial[10,3]", "120");
    check("Binomial[2,4]", "0");
    check("Binomial[4,2]", "6");
    check("Binomial[100!,100!]", "1");
  }

  public void testSystem160() {
    check("CatalanNumber[-4]", "0");
  }

  public void testSystem161() {
    check("CatalanNumber[0]", "1");
  }

  public void testSystem162() {
    check("CatalanNumber[1]", "1");
  }

  public void testSystem163() {
    check("CatalanNumber[4]", "14");
    check("CatalanNumber[10]", "16796");
  }

  public void testSystem164() {
    check("HarmonicNumber[0]", "0");
    check("HarmonicNumber[1]", "1");
    check("HarmonicNumber[2]", "3/2");
    check("HarmonicNumber[10]", "7381/2520");
    check("HarmonicNumber[20]", "55835135/15519504");
  }

  public void testSystem165() {
    check("Expand[(a+b)*(c+d)]", "b*d+a*d+b*c+a*c");
    check("Expand[(x+3)/((x+4)*(x+2))]", "(x+3)*(x^2+6*x+8)^(-1)");
  }

  public void testSystem166() {
    check("Expand[(a+b)^2]", "b^2+2*a*b+a^2");
  }

  public void testSystem167() {
    check("Expand[(a+b+c)^2]", "c^2+2*b*c+2*a*c+b^2+2*a*b+a^2");
  }

  public void testSystem168() {
    check("Expand[x*(x+1)]", "x^2+x");
  }

  public void testSystem169() {
    check("FactorInteger[2^32-1]", "{{3,1},{5,1},{17,1},{257,1},{65537,1}}");
  }

  public void testSystem170() {
    check("Cross[{1, 2, 3}, {a, b, c}]", "{2*c-3*b,-c+3*a,b-2*a}");
  }

  public void testSystem171() {
    check("Integrate[x,x]", "1/2*x^2");
    check("Integrate[Sin[x],x]", "(-1)*Cos[x]");

    // check("Integrate[1/(x^5+x-7),x]", "");
    check("(-1.0)/48", "-0.020833333333333332");
    check("NIntegrate[(x-1)*(x-0.5)*x*(x+0.5)*(x+1),{x,0,1}]",
        "-0.020833327124516472");
    check("NIntegrate[(x-1)*(x-0.5)*x*(x+0.5)*(x+1),{x,0,1},Simpson]",
        "-0.0208333320915699");
    check("NIntegrate[(x-1)*(x-0.5)*x*(x+0.5)*(x+1),{x,0,1},Romberg]",
        "-0.020833333333333332");
    check("NIntegrate[(x-1)*(x-0.5)*x*(x+0.5)*(x+1),{x,0,1},LegendreGauss]",
        "-0.020833333333333336");
  }

  public void testSystem172() {
    check("Integrate[(10 x^2 - 63 x + 29)/(x^3 - 11 x^2 + 40 x -48),x]",
        "63*(x-4)^(-1)-70*Log[x-3]+80*Log[x-4]");
    check(
        "Integrate[(x^7 - 24*x^4 - 4*x^2 + 8*x - 8)/(x^8 + 6*x^6 + 12*x^4 + 8*x^2),x]",
        "6*x*(x^4+4*x^2+4)^(-1)+(-x+3)*(x^2+2)^(-1)+x^(-1)+Log[x]");
  }

  public void testSystem173() {
    check("N[1.0]", "1.0");
  }

  public void testSystem174() {
    check("N[42]", "42.0");
  }

  public void testSystem175() {
    check("N[I]", "0.0+I*1.0");
  }

  public void testSystem176() {
    check("True && a", "a");
  }

  public void testSystem177() {
    check("False || a", "a");
  }

  public void testSystem178() {
    check("!False", "True");
  }

  public void testSystem179() {
    check("!True", "False");

  }

  public void testSystem180() {
    check("Pi==E", "False");
  }

  public void testSystem181() {
    check("Pi!=E", "True");
  }

  public void testSystem182() {
    check("I==I", "True");
  }

  public void testSystem183() {
    check("(#^3)&", "#1^3&");
  }

  public void testSystem184() {
    check("(#^3)&[]", "#1^3");
  }

  public void testSystem185() {
    check("Function[x,(x^3)][]", "Function[x,x^3][]");
  }

  public void testSystem186() {
    check("Function[x,(x^3)][x,y]", "x^3");
  }

  public void testSystem187() {
    check("(#^3)&[x]", "x^3");
  }

  public void testSystem188() {
    check("$i = 10; $i=$i-1", "9");
  }

  public void testSystem189() {
    check("$i = 10; If[$i>0, 1, -1]", "1");
  }

  public void testSystem190() {
    check(
        "$i = 10; $result = 1; While[$i >= 0, $result = $result + $i; $i=$i-1]; $result",
        "56");
  }

  public void testSystem191() {
    check(
        "$i = 10; $result = 1; While[$i >= 0, $result = $result + $i; If[$result>20, Break[]]; $i=$i-1]; $result",
        "28");
  }

  public void testSystem192() {
    check("$blck=Block[{$i=0}, $i=$i+1; Return[$i]]", "1");

  }

  public void testSystem193() {
    check("Sum[i,{i,10}]", "55");
    check("Sum[i,{a,10,z}]", "Sum[i,{a,10,z}]");
    check("Sum[x,{x,1,1}]", "1");
    check("Sum[x,{x,3,2,-1}]", "5");
    check("Sum[x,{x,10,3,-4}]", "16");
    // use default value "0" for iterator with invalid range
    check("Sum[x,{x,1,0}]", "0");
    check("Sum[x,{x,2,3,-1}]", "0");
    // 1*1 + 2*1 + 3*1 + 2*2 + 2*3 + 3*3
    check("Sum[i*j, {i, 1, 3}, {j, 1, i}]", "25");
  }

  public void testSystem194() {
    check("Product[i,{i,10}]", "3628800");
    check("Product[x,{x,1,1}]", "1");
    check("Product[x,{x,3,2,-1}]", "6");
    check("Product[x,{x,10,3,-4}]", "60");
    // use default value "0" for iterator with invalid range
    check("Product[x,{x,1,0}]", "0");
    check("Product[x,{x,2,3,-1}]", "0");
    check("Product[i,{a,10,z}]", "Product[i,{a,10,z}]");
  }

  public void testSystem195() {
    check("ComposeList[{x, y, z}, a]", "{a,x[a],y[x[a]],z[y[x[a]]]}");
  }

  public void testSystem196() {
    check("Fold[fl, 0, {1, 2, 3}]", "fl[fl[fl[0,1],2],3]");
  }

  public void testSystem197() {
    check("FoldList[fl, 0, {1, 2, 3}]",
        "{0,fl[0,1],fl[fl[0,1],2],fl[fl[fl[0,1],2],3]}");
  }

  public void testSystem198() {
    check("Nest[n,10,0]", "10");
  }

  public void testSystem199() {
    check("Nest[n,10,4]", "n[n[n[n[10]]]]");
  }

  public void testSystem200() {
    check("NestList[n,10,0]", "{10}");
  }

  public void testSystem201() {
    check("NestList[n,10,4]", "{10,n[10],n[n[10]],n[n[n[10]]],n[n[n[n[10]]]]}");
  }

  public void testSystem202() {
    check("Outer[List, {a, b, c, d}, {{{1, 2}}}]",
        "{{{{{a,1},{a,2}}}},{{{{b,1},{b,2}}}},{{{{c,1},{c,2}}}},{{{{d,1},{d,2}}}}}");
  }

  public void testSystem203() {
    check("Outer[List, {1, 2, 3}, {4, 5}]",
        "{{{1,4},{1,5}},{{2,4},{2,5}},{{3,4},{3,5}}}");
  }

  public void testSystem204() {
    check("Outer[Times, {a, b, c}, {{{1, 2}, {3, 4}}}]",
        "{{{{a,2*a},{3*a,4*a}}},{{{b,2*b},{3*b,4*b}}},{{{c,2*c},{3*c,4*c}}}}");
  }

  public void testSystem205() {
    check("Array[hd,3]", "{hd[1],hd[2],hd[3]}");
  }

  public void testSystem206() {
    check("Array[hd,4,2,g]", "g[hd[2],hd[3],hd[4],hd[5]]");
  }

  public void testSystem207() {
    check(
        "Array[hd,{3,4}]",
        "{{hd[1,1],hd[1,2],hd[1,3],hd[1,4]},{hd[2,1],hd[2,2],hd[2,3],hd[2,4]},{hd[3,1],hd[\n"
            + "3,2],hd[3,3],hd[3,4]}}");
  }

  public void testSystem208() {
    check("Table[i,{i,3}]", "{1,2,3}");
    check("f @@ Table[i+1,List[i,1,5]]", "f[2,3,4,5,6]");
    check("Table[i,{i,1,1}]", "{1}");
    check("Table[i,{i,1,0}]", "Table[i,{i,1,0}]");
  }

  public void testSystem209() {
    check("Table[i*j,{i,3},{j,2}]", "{{1,2},{2,4},{3,6}}");
  }

  public void testSystem210() {
    check("Table[i*j,{i,3,10,2},{j,2}]", "{{3,6},{5,10},{7,14},{9,18}}");
  }

  public void testSystem211() {
    check("Range[5]", "{1,2,3,4,5}");
  }

  public void testSystem211a() {
    check("$g[x_Integer]:=x+1; Range/@ $g /@Range[3]",
        "{{1,2},{1,2,3},{1,2,3,4}}");
  }

  public void testSystem212() {
    check("Range[3,10,2]", "{3,5,7,9}");
  }

  public void testSystem213() {
    check("Range[3,10,1/2]",
        "{3,7/2,4,9/2,5,11/2,6,13/2,7,15/2,8,17/2,9,19/2,10}");
  }

  public void testSystem214() {
    check("Range[1,2,0.25]", "{1.0,1.25,1.5,1.75,2.0}");

  }

  public void testSystem215() {
    check("Extract[{u+v+w^5, 42, w^10, 12, u+w^3, w^2}, {3, 2}]", "10");
  }

  public void testSystem216() {
    check("Extract[{u+v+w^5, 42, w^10, 12, x[u,w^3], w^2}, {5, 2, 2}]", "3");
  }

  public void testSystem217() {
    check("Position[{42}, 42]", "{{1}}");
  }

  public void testSystem218() {
    check("Position[{u+v+w^5, 42, w^10}, w^_]", "{{1,3},{3}}");
  }

  public void testSystem219() {
    check("Position[{u+v+w^5, 42, w^10}, w^_, {1}]", "{{3}}");
  }

  public void testSystem220() {
    check("Position[{u+v+w^5, 42, w^10, 12, u+w^3, w^2}, w^_, {1,2}]",
        "{{1,3},{3},{5,2},{6}}");

  }

  public void testSystem221() {
    check("Take[{1,2,3,4,5,6,7,8,9,10},3]", "{1,2,3}");
  }

  public void testSystem222() {
    check("Take[{1,2,3,4,5,6,7,8,9,10},-3]", "{8,9,10}");
  }

  public void testSystem223() {
    check("Take[{1,2,3,4,5,6,7,8,9,10},{2,7,3}]", "{2,5}");
  }

  public void testSystem224() {
    check("Take[{1,2,3,4,5,6,7,8,9,10},{2}]", "{2}");
  }

  public void testSystem225() {
    check("Take[{1,2,3,4,5,6,7,8,9,10},{2,2}]", "{2}");
  }

  public void testSystem226() {
    check("Take[{1,2,3,4,5,6,7,8,9,10},{2,7}]", "{2,3,4,5,6,7}");
  }

  public void testSystem227() {
    check("Take[{{1,2,3,4},{5,6,7,8,9,10}},-1,3]", "{{5,6,7}}");
  }

  public void testSystem228() {
    check("Take[{{1,2,3,4},{{5,6,7},{8,9,10}}},-1,1,2]", "{{{5,6}}}");
  }

  public void testSystem229() {
    check("Take[{{1,2,3,4},{w[5,6,7],{8,9,10}}},-1,1,2]", "{{w[5,6]}}");

  }

  public void testSystem230() {
    check("Drop[{1,2,3,4,5,6,7,8,9,10},3]", "{4,5,6,7,8,9,10}");
  }

  public void testSystem231() {
    check("Drop[{1,2,3,4,5,6,7,8,9,10},-3]", "{1,2,3,4,5,6,7}");
  }

  public void testSystem232() {
    check("Drop[{1,2,3,4,5,6,7,8,9,10},{2,7,3}]", "{1,3,4,6,7,8,9,10}");
  }

  public void testSystem233() {
    check("Drop[{1,2,3,4,5,6,7,8,9,10},{2}]", "{1,3,4,5,6,7,8,9,10}");
  }

  public void testSystem234() {
    check("Drop[{1,2,3,4,5,6,7,8,9,10},{2,2}]", "{1,3,4,5,6,7,8,9,10}");
  }

  public void testSystem235() {
    check("Drop[{1,2,3,4,5,6,7,8,9,10},{2,7}]", "{1,8,9,10}");

  }

  public void testSystem236() {
    check("Sign[0.0]", "0.0");
  }

  public void testSystem237() {
    check("Sign[-(6/12)]", "-1");
  }

  public void testSystem238() {
    check("Sign[42]", "1");
  }

  public void testSystem239() {
    check("SignCmp[0.0]", "0.0");
  }

  public void testSystem240() {
    check("SignCmp[-(6/12)]", "-1");
  }

  public void testSystem241() {
    check("SignCmp[42]", "1");
  }

  public void testSystem242() {
    check("SignCmp[I]", "1");
  }

  public void testSystem243() {
    check("SignCmp[-I]", "-1");
  }

  public void testSystem244() {
    check("SignCmp[3-I]", "1");
  }

  public void testSystem245() {
    check("SignCmp[-3+I]", "-1");

  }

  public void testSystem246() {
    check("Ceiling[42]", "42");
  }

  public void testSystem247() {
    check("Floor[42]", "42");
  }

  public void testSystem248() {
    check("Trunc[42]", "42");
  }

  public void testSystem249() {
    check("Ceiling[3/4]", "1");
  }

  public void testSystem250() {
    check("Floor[3/4]", "0");
  }

  public void testSystem251() {
    check("Trunc[3/4]", "0");
  }

  public void testSystem252() {
    check("Ceiling[-3/4]", "0");
  }

  public void testSystem253() {
    check("Floor[-3/4]", "-1");
  }

  public void testSystem254() {
    check("Trunc[-3/4]", "0");
  }

  public void testSystem255() {
    check("Ceiling[42.0]", "42.0");
  }

  public void testSystem256() {
    check("Floor[42.0]", "42.0");
  }

  public void testSystem257() {
    check("Trunc[42.0]", "42.0");
  }

  public void testSystem258() {
    check("Ceiling[0.75]", "1.0");
  }

  public void testSystem259() {
    check("Floor[0.75]", "0.0");
  }

  public void testSystem260() {
    check("Trunc[0.75]", "0.0");
  }

  public void testSystem261() {
    check("Ceiling[-0.75]", "0.0");
  }

  public void testSystem262() {
    check("Floor[-0.75]", "-1.0");
  }

  public void testSystem263() {
    check("Trunc[-0.75]", "0.0");
  }

  public void testSystem264() {
    check("Trunc[Pi]", "3");
  }

  public void testSystem265() {
    check("Trunc[-42*x]", "(-1)*Trunc[42*x]");
  }

  public void testSystem266() {
    check("GCD[6,35]", "1");
    check("GCD[6,27]", "3");
    check("GCD[12,54,66]", "6");

    check("LCM[14,6,9]", "126");
    check("LCM[12,54,66]", "1188");

    check("PowerMod[3,4,7]", "4");
    check("PowerMod[5,-1,3]", "2");
    check("PowerMod[7,-1,5]", "3");

    check("ExtendedGCD[3,4]", "{1,{-1,1}}");

    check("ExtendedGCD[12,60]", "{12,{1,0}}");
    check("ExtendedGCD[12,256]", "{4,{-21,1}}");
    check("ExtendedGCD[12,60,256]", "{4,{-21,0,1}}");
    check("ExtendedGCD[12,60,256,282]", "{2,{1470,0,-70,1}}");

  }

  public void testSystem267() {
    check("Negative[0.0]", "False");
  }

  public void testSystem268() {
    check("Negative[-(6/12)]", "True");
  }

  public void testSystem269() {
    check("Negative[42]", "False");
  }

  public void testSystem270() {
    check("Positive[1/3]", "True");
  }

  public void testSystem271() {
    check("Positive[42]", "True");
  }

  public void testSystem272() {
    check("Negative[I]", "False");
  }

  public void testSystem273() {
    check("Negative[-I]", "False");
  }

  public void testSystem274() {
    check("NonNegative[3-I]", "False");
  }

  public void testSystem275() {
    check("Positive[-3+I]", "False");

  }

  public void testSystem276() {
    check("{10,9,8,7}[[4]]", "7");
    // this should now throw an exception
  }

  public void testSystem277() {
    check("{10,9,8,7}[[5]]", "{10,9,8,7}[[5]]");
  }

  public void testSystem278() {
    check("{10,9,8,7}[[1]]", "10");
  }

  public void testSystem279() {
    check("{10,9,8,7}[[0]]", "List");

  }

  public void testSystem280() {
    check("2<9/4", "True");
  }

  public void testSystem281() {
    check("5/4<9/4", "True");
  }

  public void testSystem282() {
    check("Order[x,y]", "1");
  }

  public void testSystem283() {
    check("Order[y,x]", "-1");
  }

  public void testSystem284() {
    check("OrderedQ[{x,y,z}]", "True");
    check("OrderedQ[{abc,abc}]", "True");
  }

  public void testSystem285() {
    check("OrderedQ[{2,5/2,3,10/3,a,-b}]", "True");
  }

  public void testSystem286() {
    check("OrderedQ[{-2*a,a^2,b^3,c}]", "True");
  }

  public void testSystem287() {
    check("OrderedQ[{x,a,z}]", "False");
  }

  public void testSystem288() {
    check("Sort[{a^2,b^3,c,-2*a}]", "{(-2)*a,a^2,b^3,c}");
    check("Sort[{3,4,2,5,6,42,21,33,15}, Less]", "{2,3,4,5,6,15,21,33,42}");
    check("Sort[gogo[3,4,2,5,6,42,21,33,15], Greater]",
        "gogo[42,33,21,15,6,5,4,3,2]");
  }

  public void testSystem289() {
    check("Head[hello]", "Symbol");
  }

  public void testSystem290() {
    check("Head[\"hello world\"]", "String");
  }

  public void testSystem291() {
    check("Head[2/3]", "Rational");
  }

  public void testSystem292() {
    check("Head[17+2]", "Integer");
  }

  public void testSystem293() {
    check("Head[I+1]", "Complex");
  }

  public void testSystem294() {
    check("Head[I*0.5]", "Complex");
  }

  public void testSystem295() {
    check("Head[3.12]", "Real");
  }

  public void testSystem296() {
    check("Head[q+p]", "Plus");
  }

  public void testSystem297() {
    check("Head[g[x][y]]", "g[x]");

  }

  public void testSystem298() {
    check("Length[g[x,y,z]]", "3");
  }

  public void testSystem299() {
    check("Length[g[x,y,z][u,v]]", "2");

  }

  public void testSystem300() {
    check("RotateLeft[r[1,2,3,4]]", "r[2,3,4,1]");
  }

  public void testSystem301() {
    check("RotateRight[r[1,2,3,4]]", "r[4,1,2,3]");
  }

  public void testSystem302() {
    check("RotateLeft[r[1,2,3,4],2]", "r[3,4,1,2]");
  }

  public void testSystem303() {
    check("RotateRight[r[1,2,3,4],3]", "r[2,3,4,1]");
  }

  public void testSystem304() {
    check("Reverse[r[1,2,3,4]]", "r[4,3,2,1]");

  }

  public void testSystem305() {
    check("u[v[w,x,y] /. x->y]", "u[v[w,y,y]]");
  }

  public void testSystem306() {
    check("u[v[w,x,y] /. { x->y, w->y}]", "u[v[y,y,y]]");
    check("{x,x,x} /. x->y+1", "{y+1,y+1,y+1}");
    // check("{a+b,x,c+d+e} /. x_+y_->{x,y}", "");
  }

  public void testSystem307() {
    check("Apply[u, v[w,x,y]]", "u[w,x,y]");
    check("u@@v[w,x,y]", "u[w,x,y]");
  }

  public void testSystem308() {
    check("Apply[u, v[w,x,y[z]],{1}]", "v[w,x,u[z]]");
  }

  public void testSystem309() {
    check("Apply[g, {{{u[w]}}}, -2]", "g[g[g[g[w]]]]");
  }

  public void testSystem310() {
    check("Apply[g, {{{u[w]}}}, {2,-2}]", "{{g[g[w]]}}");
  }

  public void testSystem311() {
    check("Apply[g, {{{u[w]}}}, {-4, -2}]", "{g[g[g[w]]]}");
  }

  public void testSystem312() {
    check("Level[w[w[g[a], a], h[a], u[b], w],2]",
        "{g[a],a,w[g[a],a],a,h[a],b,u[b],w}");
    check("Level[w[w[g[a], a], h[a], u[b], w],{2}]", "{g[a],a,a,b}");
    check("Level[w[w[g[a], a], h[a], u[b], w],{-1}]", "{a,a,a,b,w}");
  }

  public void testSystem313() {
    check("Total[{2,4*x^5, y*x},{2}]", "y+x+4");
    check("Total[{2,4*x^5, y*x},{2,3}]", "y+2*x+9");
    check("Total[w[{1,2,3},{4,5,6},{7,8,9}]]", "{12,15,18}");
    check("Total[w[{1,2,3},{4,5,6},{7,8,9}],2]", "45");
  }

  public void testSystem315() {
    check("Arg[-3]", "Pi");
  }

  public void testSystem316() {
    check("Arg[-3*I]", "(-1/2)*Pi");
  }

  public void testSystem317() {
    check("Arg[3 I]", "1/2*Pi");
  }

  public void testSystem318() {
    check("Arg[42]", "0");
  }

  public void testSystem319() {
    check("Arg[-2.1]", "3.141592653589793");
    // } public void testSystem300() { check("Arg[-2.1*I]", "");
    // } public void testSystem300() { check("Arg[2.1 I]",
    // "1.5707963267948966");
  }

  public void testSystem320() {
    check("Arg[42.2]", "0.0");

  }

  public void testSystem321() {
    check("ArcCos[Infinity]", "I*Infinity");
  }

  public void testSystem322() {
    check("ArcSin[-3*f]", "(-1)*ArcSin[3*f]");
  }

  public void testSystem323() {
    check("ArcTan[-3*f]", "(-1)*ArcTan[3*f]");
  }

  public void testSystem324() {
    check("Sin[-3*Pi]", "0");
  }

  public void testSystem325() {
    check("Cos[-3*Pi]", "-1");
  }

  public void testSystem326() {
    check("Tan[-3*Pi]", "0");

  }

  public void testSystem327() {
    check("Union[{},{}]", "{}");
  }

  public void testSystem328() {
    check("Union[{1},{2}]", "{1,2}");
  }

  public void testSystem329() {
    check("Union[{1,2,2,4},{2,3,4,5}]", "{1,2,3,4,5}");
  }

  public void testSystem330() {
    check("Intersection[{},{}]", "{}");
  }

  public void testSystem331() {
    check("Intersection[{1},{2}]", "{}");
  }

  public void testSystem332() {
    check("Intersection[{1,2,2,4},{2,3,4,5}]", "{2,4}");
    check("Intersection[{2,3,4,5},{1,2,2,4}]", "{2,4}");
  }

  public void testSystem333() {
    check("Complement[{},{}]", "{}");
  }

  public void testSystem334() {
    check("Complement[{1},{2}]", "{1}");
  }

  public void testSystem335() {
    check("Complement[{1,2,2,4,6},{2,3,4,5}]", "{1,6}");
  }

  public void testSystem336() {
    check("Join[{},{}]", "{}");
  }

  public void testSystem337() {
    check("Join[{1},{2}]", "{1,2}");
    // not evaluated:
  }

  public void testSystem338() {
    check("Join[{1},{2},3]", "Join[{1},{2},3]");
  }

  public void testSystem339() {
    check("Join[{1,2,2,4},{2,3,4,5}]", "{1,2,2,4,2,3,4,5}");

  }

  public void testSystem340() {
    check("Select[{1, I, f, g[2], 3/4}, NumberQ]", "{1,I,3/4}");
  }

  public void testSystem341() {
    check("Select[a*c+b*c+a*d+b*d, FreeQ[#,a]&]", "b*d+b*c");
  }

  public void testSystem342() {
    check("Select[a*c+b*c+a*d+b*d, FreeQ[#,a]&, 1]", "b*c");
  }

  public void testSystem343() {
    check(
        "Cases[{a[b], a[b,c], a[b[c], d], a[b[c], d[e]], a[b[c], d, e]}, a[b[_],_]]",
        "{a[b[c],d],a[b[c],d[e]]}");
  }

  public void testSystem344() {
    check("Cases[{a, b, 3/4, I, 4, 1/2}, a_Rational]", "{3/4,1/2}");
  }

  public void testSystem345() {
    check("Exp[x]", "E^x");
  }

  public void testSystem346() {
    check("Exp[1.0]", "2.718281828459045");
  }

  public void testSystem347() {
    check("Log[1.0]", "0.0");

  }

  public void testSystem348() {
    check("PolynomialQ[13 x^4 y^7+a^7*x, {x,y}]", "False");
  }

  public void testSystem349() {
    check("PolynomialQ[x + f[x], x]", "False");
    check("PolynomialQ[x + Sin[x^2], x]", "False");
  }

  public void testSystem350() {
    check("PolynomialQ[(2 + a)^2 (a - b - c^2)^2, a]", "False");
    check("PolynomialQ[(2 + a)^2 (a - b - c^2)^2, {a,b,c}]", "True");
  }

  public void testSystem351() {
    check("Variables[x + f[x]+Pi*E]", "{x}");
  }

  public void testSystem352() {
    check("Variables[(2 + a)^2 (a - b - c^2)^2]", "{a,b,c}");
  }

  public void testSystem353() {
    check("DigitQ[\"0123456789\"]", "True");
  }

  public void testSystem354() {
    check("LetterQ[\"abcJHFHG\"]", "True");
  }

  public void testSystem355() {
    check("LowerCaseQ[\"abc\"]", "True");
  }

  public void testSystem356() {
    check("UpperCaseQ[\"JHFHG\"]", "True");
  }

  public void testSystem357() {
    check("ToCharacterCode[\"123abcABC\"]", "{49,50,51,97,98,99,65,66,67}");
  }

  public void testSystem358() {
    check("FromCharacterCode[55]", "\"7\"");
  }

  public void testSystem359() {
    check("FromCharacterCode[{49,50,51,97,98,99,65,66,67}]", "\"123abcABC\"");
  }

  public void testSystem360() {
    check("ToUnicode[\"123abcABC\"]",
        "\"\\u0031\\u0032\\u0033\\u0061\\u0062\\u0063\\u0041\\u0042\\u0043\"");
  }

  public void testSystem361() {
    check("SyntaxQ[\"a+b)*3\"]", "False");
  }

  public void testSystem362() {
    check("SyntaxLength[\"a+b)*3\"]", "3");
    check("SyntaxLength[\"(a+b)*3\"]", "7");
  }

  public void testSystem363() {
    check("$decr=10;$decr--", "10");
  }

  public void testSystem364() {
    check("$decr=10;$decr--;$decr", "9");
  }

  public void testSystem365() {
    check("$predecr=10;--$predecr", "9");
  }

  public void testSystem366() {
    check("$predecr=10;--$predecr;$predecr", "9");
  }

  public void testSystem367() {
    check("$incr=10;$incr++", "10");
  }

  public void testSystem368() {
    check("$incr=10;$incr++;$incr", "11");
  }

  public void testSystem369() {
    check("$preincr=10;++$preincr", "11");
  }

  public void testSystem370() {
    check("$preincr=10;++$preincr;$preincr", "11");
  }

  public void testSystem371() {
    check("Mean[{a,b,2,3}]", "1/4*(b+a+5)");
    check("Mean[{1., 0.3, 4.7}]", "2.0");
  }

  public void testSystem372() {
    check("Median[{1,5,2,8,7}]", "5");
    check("Median[{1,5,2,10,8,7}]", "6");
    check("Median[{a,b,c,d,e}]", "c");
    check("Median[{f,g,h,i,j,k}]", "1/2*(i+h)");
  }

  public void testSystem373() {
    check("Max[7,3,8]", "8");
  }

  public void testSystem374() {
    check("Max[{7,3,8,11,22,15,4,3},{{47,15}}]", "47");
  }

  public void testSystem375() {
    check("Max[{7,3,8,11,22,15,4,3},{{ft[at],15}}]", "Max[22,ft[at]]");
  }

  public void testSystem376() {
    check("Min[7,3,8]", "3");
  }

  public void testSystem377() {
    check("Min[{7,3,8,11,22,-15,4,3},{{47,15}}]", "-15");
  }

  public void testSystem378() {
    check("Min[{7,3,8,11,-22,15,4,3},{{ft[at],15}}]", "Min[-22,ft[at]]");

  }

  public void testSystem379() {
    check("FindRoot[Exp[x]==Pi^3,{x,-1,10}]", "{x->3.4341896575482007}");
    check(
        "$K=10000;\n"
            + "$g=0.0;\n"
            + "$n=10*12;\n"
            + "$Z=12;\n"
            + "$AA=0.0526;\n"
            + "$R=100;\n"
            + "$d=0.00;\n"
            + "$vn=0;\n"
            + "$EAj=0;\n"
            + "$zj=0;\n"
            + "$sz=1;\n"
            + "FindRoot[(($K*(1+p-$g)^($n/$Z))/(1+$AA))+(Sum[(($R*(1+$d)^(Floor[i/$Z]))/(1+$AA))*(1+p-$g)^(($n-i-$vn)/$Z),{i,0,$n-1}])+(Sum[($EAj*(1+p-$g)^(($n-$zj)/$Z))/(1+$AA),{j,1,$sz}]) - 30199, {p, 0, 0.1}]",
        "{p->0.049997093938224026}");
    check("$K=10000;\n" + "$g=0.0;\n" + "$n=10*12;\n" + "$Z=12;\n"
        + "$AA=0.0526;\n" + "$res=15474;\n"
        + "FindRoot[(($K*(1+p-$g)^($n/$Z))/(1+$AA)) - $res, {p, 0, 0.1}]",
        "{p->0.04999346433486659}");
  }

  public void testSystem380() {
    check("Exp[3.4341896]", "31.006274895944433");
    check("Pi^3.0", "31.006276680299816");
    check("FindRoot[Exp[x]==Pi^3,{x,-1,10}, Bisection]",
        "{x->3.434189647436142}");
    check("FindRoot[Exp[x]==Pi^3,{x,-1,10}, Brent]", "{x->3.434189604205737}");
    check("FindRoot[Exp[x]==Pi^3,{x,-1,10}, Muller]", "{x->3.4341896575482025}");
    check("FindRoot[Exp[x]==Pi^3,{x,-1,10}, Ridders]",
        "{x->3.4341896575482007}");
    check("FindRoot[Exp[x]==Pi^3,{x,-1,10}, Secant]", "{x->3.4341896575482007}");

  }

  public void testSystem381() {
    check("FindRoot[Exp[x]==Pi^3,{x,-1,10}, Brent]", "{x->3.434189604205737}");
  }

  public void testSystem382() {
    check("FindRoot[Sin[x],{x,-0.5,0.5}, Secant]", "{x->0.0}");

  }

  public void testSystem383() {
    check("Conjugate[I]", "-I");
  }

  public void testSystem384() {
    check("Conjugate[2.0-I]", "2.0+I*1.0");
  }

  public void testSystem385() {
    check("Conjugate[1/3]", "1/3");
  }

  // public void testSystem386() {
  // check("JCall[\"JCall\", \"test1\", Sin[x]]", "\"Sin[x]\"");
  // }

  public void testSystem387() {
    check("FullForm[3/4+ #2+b+c*3]",
        "\"Plus[Rational[3,4], b, Slot[2], Times[3, c]]\"");
    check("$a=1+I;FullForm[$a]", "\"Complex[1,1]\"");
    check("FullForm[1/3+I]", "\"Complex[Rational[1,3],1]\"");
    check("FullForm[ff[x_*y_]]",
        "\"ff[Times[Pattern[x, Blank[]], Pattern[y, Blank[]]]]\"");
  }

  public void testSystem388() {
    check("ff[Times[Pattern[x, Blank[]], Pattern[y, Blank[]]]]", "ff[x_*y_]");
  }

  public void testSystem389() {
    check("$f389[a_]:={a}; Clear[$f389]; $f389[$test]", "$f389[$test]");
    check("$f389[a_]:={a}; ClearAll[$f389]; $f389[a_]:=g[a]", "");
    check("$f389[$test]", "g[$test]");
  }

  public void testSystem390() {
    check("FixedPoint[(1 + 1/#) &, 10, 3]", "32/21");
    check("FixedPoint[(Cos[#])&,0.8]", "0.7390851332151603");
  }

  public void testSystem391() {
    check("StringJoin[\"Hello\", \" World\"]", "\"Hello World\"");
  }

  public void testSystem392() {
    check("StringDrop[\"Hello\", 2]", "\"llo\"");
    check("StringDrop[\"Hello\", -2]", "\"Hel\"");
  }

  public void testSystem393() {
    check("EulerPhi[EulerPhi[25]]", "8");
    check("EulerPhi[10]", "4");
  }

  public void testSystem394() {
    check("PrimitiveRoots[8]", "{}");
    check("PrimitiveRoots[9]", "{2,5}");
    check("PrimitiveRoots[13]", "{2,6,7,11}");
    check("PrimitiveRoots[25]", "{2,3,8,12,13,17,22,23}");
  }

  public void testSystem395() {
    check("MoebiusMu[990]", "0");
    check("MoebiusMu[991]", "-1");
    check("MoebiusMu[992]", "0");
    check("MoebiusMu[993]", "1");
    check("MoebiusMu[994]", "-1");
    check("MoebiusMu[995]", "1");
    check("MoebiusMu[996]", "0");
    check("MoebiusMu[997]", "-1");
    check("MoebiusMu[998]", "1");
    check("MoebiusMu[999]", "0");
    check("MoebiusMu[1000]", "0");
  }

  public void testSystem396() {
    check("JacobiSymbol[2,13]", "-1");
    check("JacobiSymbol[4,13]", "1");
  }

  public void testSystem397() {
    check("Re[42+I]", "42");
    check("Im[1/3+I]", "1");
    check("Re[0.5+I]", "0.5");
    check("Im[0.5+I*1.5]", "1.5");
    check("Re[42]", "42");
    check("Im[42]", "0");
  }

  public void testSystem398() {
    check("Numerator[3/4]", "3");
    check("Denominator[3/4]", "4");
    check("Numerator[42]", "42");
    check("Denominator[42]", "1");
    check("Numerator[3/4*x^(-3)]", "3");
    check("Denominator[3/4*x^(-3)]", "4*x^3");
    check("Numerator[x+3/4*x^(-3)]", "x+3/4*x^(-3)");
    check("Denominator[x+3/4*x^(-3)]", "1");
    check("Together[x+3/4*x^(-3)]", "1/4*(4*x^4+3)*x^(-3)");
    check("Together[(x^2-2)^3/(x^2-2)+(x^2-2)^2/(x^2-2)]", "x^4-3*x^2+2");
    check("Together[a/b+c/d]", "(a*d+b*c)*b^(-1)*d^(-1)");
  }

  public void testSystem399() {
    check("Erf[3.0]", "0.9999779095030024");
  }

  public void testSystem401() {
    check("ExpandAll[3.0+x*(4.0+x*(5.0+(33.0+x^2.0)*x^4.0))]",
        "x^8.0+33.0*x^6.0+5.0*x^2.0+4.0*x+3.0");
    check("Horner[3+4*x+5*x^2+33*x^6.0+x^8]",
        "x*(x*((x^2.0+33.0)*x^4.0+5.0)+4.0)+3.0");
    check("ExpandAll[3+x*(4+x*(5+(33+x^2)*x^4))]", "x^8+33*x^6+5*x^2+4*x+3");
    check("Horner[3+4*x+5*x^2+33*x^6+x^8]", "x*(x*((x^2+33)*x^4+5)+4)+3");
  }

  public void testSystem402() {
    check("Expand[(x-1)^10]",
        "x^10-10*x^9+45*x^8-120*x^7+210*x^6-252*x^5+210*x^4-120*x^3+45*x^2-10*x+1");
    check(
        "Expand[(x-1)^20]",
        "x^20-20*x^19+190*x^18-1140*x^17+4845*x^16-15504*x^15+38760*x^14-77520*x^13+\n"
            + "125970*x^12-167960*x^11+184756*x^10-167960*x^9+125970*x^8-77520*x^7+38760*x^6\n"
            + "-15504*x^5+4845*x^4-1140*x^3+190*x^2-20*x+1");
    check("ExpandAll[3+x*(4+x*(Sin[5+(33+x^2)*x^4]))]",
        "x^2*Sin[x^6+33*x^4+5]+4*x+3");
  }

  public void testSystem403() {
    check("ToString[a^2+2*a*b+b^2]", "\"b^2+2*a*b+a^2\"");
  }

  public void testSystem404() {
    check("Cos[0]", "1");
  }

  public void testSystem405() {
    check("Taylor[Cos[x],{x,0,4}]", "1/24*x^4-1/2*x^2+1");
  }

  public void testSystem406() {
    check("JacobiMatrix[{f[u],f[v],f[w],f[x]}, {u,v,w}]",
        "{{D[f[u],u],0,0},{0,D[f[v],v],0},{0,0,D[f[w],w]},{0,0,0}}");
    check(
        "Curl[{f[u,v,w],f[v,w,u],f[w,u,v],f[x]}, {u,v,w}]",
        "{-D[f[v,w,u],w]+D[f[w,u,v],v],-D[f[w,u,v],u]+D[f[u,v,w],w],-D[f[u,v,w],v]+D[f[v,w,u],u],f[x]}");
    check("Divergence[{f[u,v,w],f[v,w,u],f[w,u,v]}, {u,v,w}]",
        "D[f[w,u,v],w]+D[f[v,w,u],v]+D[f[u,v,w],u]");
  }

  public void testSystem407() {
    check("ContinuedFraction[45/16]", "{2,1,4,3}");
    check("FromContinuedFraction[{2,1,4,3}]", "45/16");
    check("ContinuedFraction[0.753]", "{0.0,1.0,3.0,20.0,1.0,1.0,2.0,2.0}");
    check("FromContinuedFraction[{0.0,1.0,3.0,20.0,1.0,1.0,2.0,2.0}]", "0.753");
    check("FromContinuedFraction[{0,1,3,20,1,1,2,2}]", "753/1000");
    check("FromContinuedFraction[{3}]", "3");
    check("FromContinuedFraction[{2,3}]", "7/3");
    check("FromContinuedFraction[{1,2,3}]", "10/7");
  }

  public void testSystem408() {
    check("Infinity", "Infinity");
    check("ComplexInfinity", "ComplexInfinity");
  }

  public void testSystem409() {// 
    check("Abs[-0.5]", "0.5");
    check("Abs[-3]", "3");
    check("Abs[Pi]", "Pi");
    check("Abs[E]", "E");
    check("Abs[42]", "42");
    check("Abs[-12/90]", "2/15");
    check("Abs[2/15]", "2/15");
    check("Abs[-5.0 +  3.0*I ]", "5.8309518948453");
    check("Abs[3-4*I]", "5");
    check("Norm[-0.5]", "0.5");
    check("Norm[-3]", "3");
    check("Norm[42]", "42");
    check("Norm[-5.0 +  3.0*I ]", "5.8309518948453");
    check("Norm[3-4*I]", "5");
    check("Norm[-12/90]", "2/15");
    check("Norm[2/15]", "2/15");
    check("Norm[{3,I,x,y}]", "(Abs[y]^2+Abs[x]^2+10)^(1/2)");
    check("Norm[{3.0,I,x,y}]", "(Abs[y]^2.0+Abs[x]^2.0+10.0)^0.5");
    check("EuclidianDistance[{1,2,3,4},{5,6,7,8}]", "8");
    check("SquaredEuclidianDistance[{1,2,3,4},{5,6,7,8}]", "64");
    check("ChessboardDistance[{1,2,3,4},{5,6,9,8}]", "6");
    check("ManhattanDistance[{1,2,3,4},{5,6,7,8}]", "16");
  }

  public void testSystem410() {
    check("        $l2 = {}; \n" + "          For[$j = 1, $j <= 10, $j++,\n"
        + "             $l2 = Append[$l2, $j ] \n" + "          ]; $l2",
        "{1,2,3,4,5,6,7,8,9,10}");
  }

  public void testSystem411() {
    check("Dimensions[{{{},{}}}]", "{1,2,0}");
    check("Dimensions[{{{},{{},{a}}}}]", "{1,2}");
    check("Dimensions[{{{{{a,b}}},{{},{a}}}}]", "{1,2}");
    check("Dimensions[{{{0,0}}}]", "{1,1,2}");
    check("Dimensions[{{1,0,0},{0,1,0},{0,0,1}}]", "{3,3}");
    check("Dimensions[{{1,0},{0,1},{0,0}}]", "{3,2}");
    check("Dimensions[{{{1},{0}},{{0},{1}},{{0},{0}}}]", "{3,2,1}");
  }

  public void testSystem412() {
    check("DiagonalMatrix[{1,2,3,4}]",
        "{{1,0,0,0},{0,2,0,0},{0,0,3,0},{0,0,0,4}}");
    check("DiagonalMatrix[{1,2,3,4},2]",
        "{{0,0,1,0},{0,0,0,2},{0,0,0,0},{0,0,0,0}}");
    check("DiagonalMatrix[{1,2,3,4},-2]",
        "{{0,0,0,0},{0,0,0,0},{3,0,0,0},{0,4,0,0}}");
  }

  public void testSystem413() {
    check("Inner[r,{1,2,3,4},{5,6,7,8},t]", "t[r[1,5],r[2,6],r[3,7],r[4,8]]");
  }

  public void testSystem414() {
    check("Through[f[g, h][x,y]]", "f[g[x,y],h[x,y]]");
    check("Through[f[g, h][x,y], f]", "f[g[x,y],h[x,y]]");
    check("Through[f[g, h][x,y], g]", "f[g,h][x,y]");
  }

  public void testSystem415() {
    check("Multinomial[1,4,4,2]", "34650");
    check("Multinomial[11,3,5]", "4232592");
  }

  // public void testSystem404() {
  // check("Plot3D[Sin[x]*Cos[y],{x,-10,10},{y,-10,10},{PlotRange->Automatic}]",
  // "");
  // };
  //
  // public void testSystem405() {
  // check("Plot[Sin[x],{x,0,10}]", "");
  // };

  // public void testSystem997() {
  // check("5000!", "");
  // }
  public void testSystem800() {
    int[] values1 = { 1, 2, 3 };
    int[] values2 = { 4, 5, 6 };
    Vector v1 = new Vector(values1);
    Vector v2 = new Vector(values2);
    Vector v3 = v1.plus(v2);
    check(v3.getAST(), "{5,7,9}");
  }

  public void testSystem801() {
    int[][] values1 = { { 1, 2, 3, 4 }, { 11, 12, 13, 14 }, { -1, -2, -3, -4 } };
    int[][] values2 = { { 4, 5, 6, 7 }, { 4, 5, 6, 7 }, { 4, 5, 6, 7 } };
    Matrix v1 = new Matrix(values1);
    Matrix v2 = new Matrix(values2);
    Matrix v3 = v1.plus(v2);
    check(v3.getAST(), "{{5,7,9,11},{15,17,19,21},{3,3,3,3}}");
  }

  public void testSystem802() {
    check("LegendreP[0,x]", "1");
    check("LegendreP[1,x]", "x");
    check("LegendreP[4,x]", "1/16*(70*x^4-60*x^2+6)");
    check("LegendreP[7,x]", "1/128*(3432*x^7-5544*x^5+2520*x^3-280*x)");
    check("LegendreP[10,x]",
        "1/1024*(184756*x^10-437580*x^8+360360*x^6-120120*x^4+13860*x^2-252)");
  }

  public void testSystem991() {
    check("PolynomialQuotient[x^2+2*x+1,x+2]", "x");
    check("PolynomialQuotient[x^2+x+1,2*x+1]", "1/2*x+1/4");
    check("PolynomialQuotient[x^2-1,x-1]", "x+1");
  }

  public void testSystem992() {
    check("PolynomialRemainder[x^2+2*x+1,x+2]", "1");
    check("PolynomialRemainder[x^2+x+1,2*x+1]", "3/4");
    check("PolynomialRemainder[x^2-1,x-1]", "0");
  }

  public void testSystem993() {
    check("PolynomialQuotientRemainder[x^2+2*x+1,x+2]", "{x,1}");
    check("PolynomialQuotientRemainder[x^2+x+1,2*x+1]", "{1/2*x+1/4,3/4}");
    check("PolynomialQuotientRemainder[x^2-1,x-1]", "{x+1,0}");
  }

  public void testSystem994() {
    check("PolynomialGCD[3+3*x^3,3+3*x^3]", "x^3+1");
    check("PolynomialGCD[x^2-1,x-1]", "x-1");
    check("PolynomialGCD[x+1,x^2-1]", "x+1");
    check("PolynomialGCD[-1+x^16,(x^2-1)*((1+x^4))]", "x^6-x^4+x^2-1");
    check(
        "PolynomialGCD[8*x^5+28*x^4+34*x^3+41*x^2+35*x-14,12*x^5+4*x^4-27*x^3-9*x^2-84*x-28]",
        "x^3+2*x^2+7/4*x+7/2");
  }

  public void testSystem995() {
    check("Apart[(x)/(x^2-1)]", "1/2*(x+1)^(-1)+1/2*(x-1)^(-1)");
    check("Apart[(x+3)/(x^2-3*x-40)]", "2/13*(x+5)^(-1)+11/13*(x-8)^(-1)");
    check("Apart[(10*x^2+12*x+20)/(x^3-8)]",
        "(3*x+4)*(x^2+2*x+4)^(-1)+7*(x-2)^(-1)");
    check("Apart[(3*x+5)*(1-2*x)^(-2)]", "3/4*(x-1/2)^(-1)+13/8*(x-1/2)^(-2)");
    check("Apart[(10*x^2+12*x+20)/(x^3-8)]",
        "(3*x+4)*(x^2+2*x+4)^(-1)+7*(x-2)^(-1)");
    check(
        "Apart[(10*x^2-63*x+29)/((x+2)*(x+3)^5)]",
        "-195*(x+3)^(-1)-195*(x+3)^(-2)-195*(x+3)^(-3)-185*(x+3)^(-4)-308*(x+3)^(-5)+195*(x+\n"
            + "2)^(-1)");
  }

  public void testSystem996() {
    check("FactorTerms[3+3*x^3]", "3*(x^3+1)");
    check("FactorTerms[3+3/4*x^3+12/17*x^2,x]", "3/68*(17*x^3+16*x^2+68)");
  }

  // public void testSystem997() {
  //
  // check("GroebnerBasis[{x-1},{x}]", "{1-x}");
  // check(
  // "GroebnerBasis[{a+b+c+d, a*b+a*d+b*c+c*d, a*b*c+a*b*d+a*c*d+b*c*d,
  // 1-a*b*c*d}, {d,c,b,a}, MonomialOrder->DegreeReverseLexicographic]"
  // ,
  // "{a+b+c+d,a^2+2*a*c+c^2,a^3-a*b^2+a^2*c-b^2*c,1+a^4-a^3*b-a^2*b^2+a^3*c-a^2*b*c,a-a^5+c-a^4*c,a+b-a^3*b^2-a^2*b^3,2*a^2-a*b-a^4*b^2+a*c-b*c}"
  // );
  // check(
  // "GroebnerBasis[{a+b+c+d, a*b+a*d+b*c+c*d, a*b*c+a*b*d+a*c*d+b*c*d,
  // 1-a*b*c*d}, {d,c,b,a}, MonomialOrder->DegreeReverseLexicographic,
  // Modulus->1]"
  // ,
  // "{a+b+c+d,a^2+2*a*c+c^2,a^3-a*b^2+a^2*c-b^2*c,1+a^4-a^3*b-a^2*b^2+a^3*c-a^2*b*c,a-a^5+c-a^4*c,a+b-a^3*b^2-a^2*b^3,2*a^2-a*b-a^4*b^2+a*c-b*c}"
  // );
  // }

  public void testSystem998() {
    check(
        "Roots[x^3+4*x^2+x+2]",
        "{(-1/3)*((1/2)^(1/3)*(-12528^(1/2)+146)^(1/3)+(1/2)^(1/3)*(12528^(1/2)+146)^(1/3)+\n"
            + "4),(-1/3)*((I*1/2*3^(1/2)-1/2)*(1/2)^(1/3)*(-12528^(1/2)+146)^(1/3)+(-I*1/2*3^(1/\n"
            + "2)-1/2)*(1/2)^(1/3)*(12528^(1/2)+146)^(1/3)+4),(-1/3)*((-I*1/2*3^(1/2)-1/2)*(1/2)^(\n"
            + "1/3)*(-12528^(1/2)+146)^(1/3)+(I*1/2*3^(1/2)-1/2)*(1/2)^(1/3)*(12528^(1/2)+146)^(\n"
            + "1/3)+4)}");
    check("NRoots[x^3+4*x^2+x+2]", "{-3.8751297941627785,"
        + "-0.06243510291861069+I*0.7156909967859645,"
        + "-0.06243510291861069+I*(-0.7156909967859645)}");
    check("Roots[4+x^2+2*x+3*x^3]",
        "{-1,1/6*(I*44^(1/2)+2),1/6*(-I*44^(1/2)+2)}");
    // check(
    // "Roots[x^4-2]",
    // "");
    check("Roots[Expand[(x-1)^3]]", "{1}");
    check(
        "Roots[x^6-1]",
        "{1,-1,1/2*(I*3^(1/2)+1),1/2*(-I*3^(1/2)+1),1/2*(I*3^(1/2)-1),1/2*(-I*3^(1/2)-1)}");
    check("Factor[4+x^2+2*x+3*x^3]", "(x+1)*(3*x^2-2*x+4)");
    check("Factor[ Expand[(x^3+4*x^2+3*x+2)*(x^3+4*x^2+x+2)]]",
        "(x^3+4*x^2+x+2)*(x^3+4*x^2+3*x+2)");
    check("Factor[4+8*x+19*x^2+20*x^3+20*x^4+8*x^5+x^6]",
        "(x^3+4*x^2+x+2)*(x^3+4*x^2+3*x+2)");
    check("Factor[Expand[(x-1)^3]]", "(x-1)^3");
    check("Factor[x^6-1]", "(x-1)*(x+1)*(x^2+x+1)*(x^2-x+1)");
    check("Expand[(-1+x)*(1+x)*(1+x+x^2)*(1-x+x^2)]", "x^6-1");

    check("Factor[x^5+x^4+x+1]", "(x+1)*(x^4+1)");

    check("Factor[-1+x^16]", "(x-1)*(x+1)*(x^2+1)*(x^4+1)*(x^8+1)");
    check("Expand[(-1+x)*(1+x)*(1+x^2)*(1+x^4)*(1+x^8)]", "x^16-1");

    check("Factor[5+x^12,Modulus->7]", "(x^3+2)*(x^6+4)*(x^3+5)");
    // not supported by JAS?
    check("Factor[1+x^10,Modulus->2]", "Factor[x^10+1,Modulus->2]");// "(1+x)^2*(1+x+x^2+x^3+x^4)^2");
  }
  // public void testSystem999() {
  // check("FactorI[4+x^2+2*x+3*x^3, x]", "(1+x)*(4-2*x+3*x^2)");
  // check("FactorI[4+8*x+19*x^2+20*x^3+20*x^4+8*x^5+x^6, x]",
  // "(2+x+4*x^2+x^3)*(2+3*x+4*x^2+x^3)");
  // check("FactorI[Expand[(x-1)^3], x]", "(-1+x)^3");
  // check("FactorI[x^6-1, x]", "(-1+x)*(1+x)*(1+x+x^2)*(1-x+x^2)");
  // check("FactorI[x^5+x^4+x+1, x]", "(1+x)*(1+x^4)");
  // check("FactorI[-1+x^16, x]", "(-1+x)*(1+x)*(1+x^2)*(1+x^4)*(1+x^8)");
  // check("FactorI[243-405*x+270*x^2-90*x^3+15*x^4-x^5,x]", "(-1)*(-3+x)^5");
  // }

  // public void testSystemTest() { check("TestIt[x^2+Sin[x]*Cos[y]]",
  // "Cos[#2]*Sin[#1]+#1^2")};

}
