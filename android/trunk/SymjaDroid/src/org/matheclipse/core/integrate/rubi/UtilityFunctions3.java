package org.matheclipse.core.integrate.rubi;


import static org.matheclipse.core.expression.F.$;
import static org.matheclipse.core.expression.F.$p;
import static org.matheclipse.core.expression.F.$s;
import static org.matheclipse.core.expression.F.Abs;
import static org.matheclipse.core.expression.F.And;
import static org.matheclipse.core.expression.F.Apart;
import static org.matheclipse.core.expression.F.Apply;
import static org.matheclipse.core.expression.F.C0;
import static org.matheclipse.core.expression.F.C1;
import static org.matheclipse.core.expression.F.C1D2;
import static org.matheclipse.core.expression.F.C2;
import static org.matheclipse.core.expression.F.C3;
import static org.matheclipse.core.expression.F.C4;
import static org.matheclipse.core.expression.F.CI;
import static org.matheclipse.core.expression.F.CInfinity;
import static org.matheclipse.core.expression.F.CN1;
import static org.matheclipse.core.expression.F.CompoundExpression;
import static org.matheclipse.core.expression.F.Condition;
import static org.matheclipse.core.expression.F.Cos;
import static org.matheclipse.core.expression.F.Cosh;
import static org.matheclipse.core.expression.F.Cot;
import static org.matheclipse.core.expression.F.Csc;
import static org.matheclipse.core.expression.F.Denominator;
import static org.matheclipse.core.expression.F.E;
import static org.matheclipse.core.expression.F.Equal;
import static org.matheclipse.core.expression.F.EvenQ;
import static org.matheclipse.core.expression.F.Expand;
import static org.matheclipse.core.expression.F.False;
import static org.matheclipse.core.expression.F.First;
import static org.matheclipse.core.expression.F.FreeQ;
import static org.matheclipse.core.expression.F.Function;
import static org.matheclipse.core.expression.F.Greater;
import static org.matheclipse.core.expression.F.GreaterEqual;
import static org.matheclipse.core.expression.F.If;
import static org.matheclipse.core.expression.F.Im;
import static org.matheclipse.core.expression.F.IntegerQ;
import static org.matheclipse.core.expression.F.Less;
import static org.matheclipse.core.expression.F.LessEqual;
import static org.matheclipse.core.expression.F.List;
import static org.matheclipse.core.expression.F.Log;
import static org.matheclipse.core.expression.F.MatchQ;
import static org.matheclipse.core.expression.F.Max;
import static org.matheclipse.core.expression.F.MemberQ;
import static org.matheclipse.core.expression.F.Min;
import static org.matheclipse.core.expression.F.Module;
import static org.matheclipse.core.expression.F.Not;
import static org.matheclipse.core.expression.F.NumberQ;
import static org.matheclipse.core.expression.F.Numerator;
import static org.matheclipse.core.expression.F.Or;
import static org.matheclipse.core.expression.F.Part;
import static org.matheclipse.core.expression.F.Plus;
import static org.matheclipse.core.expression.F.Power;
import static org.matheclipse.core.expression.F.Prepend;
import static org.matheclipse.core.expression.F.Re;
import static org.matheclipse.core.expression.F.ReplaceAll;
import static org.matheclipse.core.expression.F.Rest;
import static org.matheclipse.core.expression.F.Rule;
import static org.matheclipse.core.expression.F.Sec;
import static org.matheclipse.core.expression.F.Set;
import static org.matheclipse.core.expression.F.SetDelayed;
import static org.matheclipse.core.expression.F.Simplify;
import static org.matheclipse.core.expression.F.Sin;
import static org.matheclipse.core.expression.F.Sinh;
import static org.matheclipse.core.expression.F.Slot1;
import static org.matheclipse.core.expression.F.Slot2;
import static org.matheclipse.core.expression.F.Sum;
import static org.matheclipse.core.expression.F.Tan;
import static org.matheclipse.core.expression.F.Tanh;
import static org.matheclipse.core.expression.F.Times;
import static org.matheclipse.core.expression.F.True;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.AlgebraicFunctionQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.CalculusQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.CommonFactors;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.CommonNumericFactors;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ConstantFactor;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ContainsQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ContentFactor;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Dist;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.DivideDegreesOfFactors;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ExpandExpression;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ExpandExpressionAux;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ExpandIntegrandQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ExpandTrigExpression;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ExpandTrigExpressionAux;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ExpnExpand;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ExpnExpandAux;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FactorOrder;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FalseQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FindKernel;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FractionQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FunctionOfHyperbolicQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FunctionOfInverseLinear;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FunctionOfKernelQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FunctionOfLinear;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FunctionOfLinearSubst;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FunctionOfTrigQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.GoodExpansionQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.LeadBase;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.LeadDegree;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.LeadFactor;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.LinearQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.LogQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Map2;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.MapAnd;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.MinimumDegree;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.MonomialFactor;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.MostMainFactorPosition;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NonnumericFactors;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NonpositiveFactors;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NonzeroQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NormalForm;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NotFalseQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NumericFactor;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.PositiveFactors;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.PositiveQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.PowerQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ProductQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.QuotientOfLinearsParts;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.QuotientOfLinearsQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RationalFunctionExponents;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RationalFunctionQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RationalQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Regularize;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RegularizeSubst;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RegularizeTerm;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RemainingFactors;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Simp;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SimpAux;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SimpSum;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SimplerRationalFunctionQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Smallest;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SmartTrigExpand;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SplitFreeFactors;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Subst;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SubstFor;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SumFreeQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SumQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.TrigHyperbolicFreeQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.TrigSimplify;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.TryTrigReduceQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ZeroQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.AtomQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Catch;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Coefficient;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Coth;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Count;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Csch;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Do;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Drop;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Exponent;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.FullSimplify;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.FunctionOfQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Length;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Map;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Order;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.OrderedQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.PolynomialQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.ReplacePart;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Return;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.SameQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Scan;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Sech;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Sign;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Throw;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.TrigExpand;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.TrigReduce;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Unequal;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.UnsameQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.While;

import org.matheclipse.core.interfaces.IAST;
/** 
 * UtilityFunctions rules from the <a href="http://www.apmaths.uwo.ca/~arich/">Rubi -
 * rule-based integrator</a>.
 *  
 */
public class UtilityFunctions3 { 
  public static IAST RULES = List( 
SetDelayed(SimpSum($p("u"),Times($p("v",true),Power(Cos($p("z")),C2))),
    Condition(Times($s("u"),Power(Sin($s("z")),C2)),SameQ($s("u"),Times(CN1,$s("v"))))),
SetDelayed(SimpSum($p("u"),Times($p("v",true),Power(Tan($p("z")),C2))),
    Condition(Times($s("u"),Power(Sec($s("z")),C2)),SameQ($s("u"),$s("v")))),
SetDelayed(SimpSum($p("u"),Times($p("v",true),Power(Cot($p("z")),C2))),
    Condition(Times($s("u"),Power(Csc($s("z")),C2)),SameQ($s("u"),$s("v")))),
SetDelayed(SimpSum($p("u"),Times($p("v",true),Power(Sec($p("z")),C2))),
    Condition(Times($s("v"),Power(Tan($s("z")),C2)),SameQ($s("u"),Times(CN1,$s("v"))))),
SetDelayed(SimpSum($p("u"),Times($p("v",true),Power(Csc($p("z")),C2))),
    Condition(Times($s("v"),Power(Cot($s("z")),C2)),SameQ($s("u"),Times(CN1,$s("v"))))),
SetDelayed(SimpAux(Power(Plus(Times($p("a",true),Cos($p("v"))),Times($p("b",true),Sin($p("v")))),$p("n"))),
    Condition(SimpAux(Power(Plus(Times(Cos($s("v")),Power($s("a"),CN1)),Times(Sin($s("v")),Power($s("b"),CN1))),Times(CN1,$s("n")))),And(And(IntegerQ($s("n")),Less($s("n"),C0)),ZeroQ(Plus(Power($s("a"),C2),Power($s("b"),C2)))))),
SetDelayed(SimpSum(Times($p("u",true),Power(Cosh($p("z")),C2)),Times($p("v",true),Power(Sinh($p("z")),C2))),
    Condition($s("u"),SameQ($s("u"),Times(CN1,$s("v"))))),
SetDelayed(SimpSum(Times($p("u",true),Power(Sech($p("z")),C2)),Times($p("v",true),Power(Tanh($p("z")),C2))),
    Condition($s("u"),SameQ($s("u"),$s("v")))),
SetDelayed(SimpSum(Times($p("u",true),Power(Coth($p("z")),C2)),Times($p("v",true),Power(Csch($p("z")),C2))),
    Condition($s("u"),SameQ($s("u"),Times(CN1,$s("v"))))),
SetDelayed(SimpSum($p("u"),Times($p("v",true),Power(Sinh($p("z")),C2))),
    Condition(Times($s("u"),Power(Cosh($s("z")),C2)),SameQ($s("u"),$s("v")))),
SetDelayed(SimpSum($p("u"),Times($p("v",true),Power(Cosh($p("z")),C2))),
    Condition(Times($s("v"),Power(Sinh($s("z")),C2)),SameQ($s("u"),Times(CN1,$s("v"))))),
SetDelayed(SimpSum($p("u"),Times($p("v",true),Power(Tanh($p("z")),C2))),
    Condition(Times($s("u"),Power(Sech($s("z")),C2)),SameQ($s("u"),Times(CN1,$s("v"))))),
SetDelayed(SimpSum($p("u"),Times($p("v",true),Power(Coth($p("z")),C2))),
    Condition(Times($s("v"),Power(Csch($s("z")),C2)),SameQ($s("u"),Times(CN1,$s("v"))))),
SetDelayed(SimpSum($p("u"),Times($p("v",true),Power(Sech($p("z")),C2))),
    Condition(Times($s("u"),Power(Tanh($s("z")),C2)),SameQ($s("u"),Times(CN1,$s("v"))))),
SetDelayed(SimpSum($p("u"),Times($p("v",true),Power(Csch($p("z")),C2))),
    Condition(Times($s("u"),Power(Coth($s("z")),C2)),SameQ($s("u"),$s("v")))),
SetDelayed(SimpAux(Power(Plus(Times($p("a",true),Cosh($p("v"))),Times($p("b",true),Sinh($p("v")))),$p("n"))),
    Condition(SimpAux(Power(Plus(Times(Cosh($s("v")),Power($s("a"),CN1)),Times(CN1,Times(Sinh($s("v")),Power($s("b"),CN1)))),Times(CN1,$s("n")))),And(And(IntegerQ($s("n")),Less($s("n"),C0)),ZeroQ(Plus(Power($s("a"),C2),Times(CN1,Power($s("b"),C2))))))),
SetDelayed(SimpSum(Times($p("u",true),Power($($p("f"),$p("a")),$p("n",true))),Times($p("v",true),Power($($p("f"),$p("a")),$p("n",true)))),
    Condition(SimpAux(Times(Simp(Simplify(Plus($s("u"),$s("v")))),Power($($s("f"),$s("a")),$s("n")))),MemberQ(List($s("Erf"),$s("Erfc"),$s("Erfi"),$s("FresnelS"),$s("FresnelC"),$s("ExpIntegralEi"),$s("SinIntegral"),$s("CosIntegral"),$s("SinhIntegral"),$s("CoshIntegral"),$s("LogIntegral")),$s("f")))),
SetDelayed(SimpSum(Times($p("u",true),Power($($p("f"),$p("a"),$p("b")),$p("n",true))),Times($p("v",true),Power($($p("f"),$p("a"),$p("b")),$p("n",true)))),
    Condition(SimpAux(Times(Simp(Simplify(Plus($s("u"),$s("v")))),Power($($s("f"),$s("a"),$s("b")),$s("n")))),MemberQ(List($s("Int"),$s("Gamma"),$s("PolyLog"),$s("EllipticF"),$s("EllipticE")),$s("f")))),
SetDelayed(ExpandIntegrandQ($p("m"),$p("n"),$p("p")),
    And(And(And(IntegerQ($s("p")),Greater($s("p"),C0)),NonzeroQ(Plus(Plus($s("m"),Times(CN1,$s("n"))),C1))),If(ZeroQ(Plus($s("n"),Times(CN1,C1))),Or(Or(Not(IntegerQ($s("m"))),And(Less($s("m"),C0),Not(LessEqual(LessEqual(Plus(Plus($s("m"),$s("p")),C2),C0),Plus(Plus($s("m"),Times(C2,$s("p"))),C2))))),LessEqual($s("p"),Plus($s("m"),C2))),Or(Or(Equal($s("p"),C2),Not(IntegerQ(Times(Plus($s("m"),C1),Power($s("n"),CN1))))),And(Not(And(Less(C0,Times(Plus($s("m"),C1),Power($s("n"),CN1))),LessEqual(Times(Plus($s("m"),C1),Power($s("n"),CN1)),C3))),Not(LessEqual(Times(Plus($s("m"),C1),Power($s("n"),CN1)),Times(CN1,Plus($s("p"),C1))))))))),
SetDelayed(ExpnExpand($p("u"),$p("x",$s("Symbol"))),
    ExpnExpandAux(ExpandExpression($s("u"),$s("x")),$s("x"))),
SetDelayed(ExpnExpandAux(Plus(Plus($p("u",true),Times($p("e",true),Power($p("x"),CN1))),Times($p("f",true),Power(Plus($p("c"),Times($p("d",true),$p("x"))),CN1))),$p("x",$s("Symbol"))),
    Condition(Plus(ExpnExpandAux($s("u"),$s("x")),Times($s("c"),Times($s("e"),Power(Times($s("x"),Plus($s("c"),Times($s("d"),$s("x")))),CN1)))),And(FreeQ(List($s("c"),$s("d"),$s("e"),$s("f")),$s("x")),ZeroQ(Plus(Times($s("d"),$s("e")),$s("f")))))),
SetDelayed(ExpnExpandAux(Plus(Plus($p("u",true),Times($p("e",true),Power(Plus($p("a"),Times($p("b",true),$p("x"))),CN1))),Times($p("f",true),Power(Plus($p("c"),Times($p("d",true),$p("x"))),CN1))),$p("x",$s("Symbol"))),
    Condition(Plus(ExpnExpandAux($s("u"),$s("x")),Times(Plus(Times($s("c"),$s("e")),Times($s("a"),$s("f"))),Power(Plus(Times($s("a"),$s("c")),Times(Times($s("b"),$s("d")),Power($s("x"),C2))),CN1))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("e"),$s("f")),$s("x")),ZeroQ(Plus(Times($s("d"),$s("e")),Times($s("b"),$s("f"))))),ZeroQ(Plus(Times($s("b"),$s("c")),Times($s("a"),$s("d"))))))),
SetDelayed(ExpnExpandAux($p("u"),$p("x",$s("Symbol"))),
    $s("u")),
SetDelayed(ExpandExpression(Times($p("u",true),Power(Plus(Times($p("a",true),Power($p("x"),$p("p",true))),Times($p("b",true),Power($p("x"),$p("q",true)))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(ExpandExpression(Times(Times($s("u"),Power($s("x"),Times($s("n"),$s("p")))),Power(Plus($s("a"),Times($s("b"),Power($s("x"),Plus($s("q"),Times(CN1,$s("p")))))),$s("n"))),$s("x")),And(And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ(List($s("n"),$s("p"),$s("q")))),GreaterEqual(Plus($s("q"),Times(CN1,$s("p"))),C0)))),
SetDelayed(ExpandExpression(Times($p("u",true),Power(Plus(Plus($p("a",true),Times($p("d",true),Power($p("c"),$p("m")))),Times($p("b",true),$p("v"))),$p("p"))),$p("x",$s("Symbol"))),
    Condition(Module(List($s("tmp")),ReplaceAll(ExpandExpression(Times($s("u"),Power(Plus(Plus($s("a"),Times($s("d"),$s("tmp"))),Times($s("b"),$s("v"))),$s("p"))),$s("x")),List(Rule($s("tmp"),Power($s("c"),$s("m")))))),And(And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),IntegerQ($s("p"))),Less($s("p"),C0)),FractionQ($s("m"))),Not(FreeQ($s("v"),$s("x")))))),
SetDelayed(ExpandExpression(Times($p("u",true),Power(Plus($p("a",true),Times(Times($p("b",true),Power($p("c"),$p("m"))),$p("v"))),$p("p"))),$p("x",$s("Symbol"))),
    Condition(Module(List($s("tmp")),ReplaceAll(ExpandExpression(Times($s("u"),Power(Plus($s("a"),Times(Times($s("b"),$s("tmp")),$s("v"))),$s("p"))),$s("x")),List(Rule($s("tmp"),Power($s("c"),$s("m")))))),And(And(And(And(FreeQ(List($s("a"),$s("b"),$s("c")),$s("x")),IntegerQ($s("p"))),Less($s("p"),C0)),FractionQ($s("m"))),Not(FreeQ($s("v"),$s("x")))))),
SetDelayed(ExpandExpression(Times(Power(Plus($p("c",true),Times($p("d",true),Power($p("x"),$p("m",true)))),$p("q",true)),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),$p("n")))),$p("p"))),$p("x",$s("Symbol"))),
    Condition(Module(List($s("aa"),$s("bb")),RegularizeTerm(ReplaceAll(Apart(Times(Power(Plus($s("c"),Times($s("d"),Power($s("x"),$s("m")))),$s("q")),Power(Plus($s("aa"),Times($s("bb"),Power($s("x"),$s("n")))),$s("p"))),$s("x")),List(Rule($s("aa"),$s("a")),Rule($s("bb"),$s("b")))),$s("x"))),And(And(And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),IntegerQ(List($s("m"),$s("n"),$s("p"),$s("q")))),Less($s("p"),C0)),Greater($s("n"),C1)),Or(GreaterEqual($s("m"),$s("n")),Less($s("m"),C0))),Greater($s("q"),C0)))),
SetDelayed(ExpandExpression(Times(Power($p("x"),$p("m")),Times(Plus($p("a"),Times($p("b",true),Power($p("x"),$p("n",true)))),Power(Plus($p("c"),Times($p("d",true),Power($p("x"),$p("n",true)))),CN1))),$p("x",$s("Symbol"))),
    Condition(Plus(Times($s("a"),Times(Power($s("x"),$s("m")),Power($s("c"),CN1))),Dist(Times(Plus(Times($s("b"),$s("c")),Times(CN1,Times($s("a"),$s("d")))),Power($s("c"),CN1)),ExpandExpression(Times(Power($s("x"),Plus($s("m"),$s("n"))),Power(Plus($s("c"),Times($s("d"),Power($s("x"),$s("n")))),CN1)),$s("x")))),And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),IntegerQ(List($s("m"),$s("n")))),Greater($s("n"),C0)),Less($s("m"),C0)))),
SetDelayed(ExpandExpression(Times($p("u",true),Power(Plus(Plus($p("a"),Times($p("b",true),Power($p("x"),$p("k",true)))),Times($p("c",true),Power($p("x"),$p("j")))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(ExpandExpression(Times($s("u"),Times(Power(Plus(Times($s("b"),C1D2),Times($s("c"),Power($s("x"),$s("k")))),Times(C2,$s("n"))),Power(Power($s("c"),$s("n")),CN1))),$s("x")),And(And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("j"),$s("k")),$s("x")),IntegerQ(List($s("n"),$s("k"),$s("j")))),Equal($s("j"),Times(C2,$s("k")))),Less($s("n"),C0)),ZeroQ(Plus(Power($s("b"),C2),Times(CN1,Times(Times(C4,$s("a")),$s("c")))))))),
SetDelayed(ExpandExpression(Power($p("u"),$p("n")),$p("x",$s("Symbol"))),
    Condition(Power(Plus(Plus(Coefficient($s("u"),$s("x"),C0),Times(Coefficient($s("u"),$s("x"),C1),$s("x"))),Times(Coefficient($s("u"),$s("x"),C2),Power($s("x"),C2))),$s("n")),And(And(And(And(RationalQ($s("n")),Less($s("n"),C0)),PolynomialQ($s("u"),$s("x"))),Equal(Exponent($s("u"),$s("x")),C2)),Not(MatchQ($s("u"),Condition(Plus(Plus($p("a",true),Times($p("b",true),$s("x"))),Times($p("c",true),Power($s("x"),C2))),FreeQ(List($s("a"),$s("b"),$s("c")),$s("x")))))))),
SetDelayed(ExpandExpression(Times($p("u"),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),C4))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Plus(Times(Plus(Coefficient($s("u"),$s("x"),C0),Times(Coefficient($s("u"),$s("x"),C2),Power($s("x"),C2))),Power(Plus($s("a"),Times($s("b"),Power($s("x"),C4))),CN1)),Times(Coefficient($s("u"),$s("x"),C1),Times($s("x"),Power(Plus($s("a"),Times($s("b"),Power($s("x"),C4))),CN1)))),Times(Coefficient($s("u"),$s("x"),C3),Times(Power($s("x"),C3),Power(Plus($s("a"),Times($s("b"),Power($s("x"),C4))),CN1)))),And(And(FreeQ(List($s("a"),$s("b")),$s("x")),PolynomialQ($s("u"),$s("x"))),Less(Exponent($s("u"),$s("x")),C4)))),
SetDelayed(ExpandExpression(Times(Power(Plus($p("a",true),Times($p("b",true),$p("x"))),$p("m",true)),Power(Plus($p("c"),Times($p("d",true),$p("x"))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(Map(Function(RegularizeSubst(Slot1,$s("x"),Plus($s("a"),Times($s("b"),$s("x"))))),Apart(Times(Power($s("x"),$s("m")),Power(Plus(Plus($s("c"),Times(CN1,Times($s("a"),Times($s("d"),Power($s("b"),CN1))))),Times(Times($s("d"),Power($s("b"),CN1)),$s("x"))),$s("n"))),$s("x"))),And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),IntegerQ(List($s("m"),$s("n")))),Greater($s("m"),C0)),Less($s("n"),C0)))),
SetDelayed(ExpandExpression(Times(Times(Power($p("x"),$p("m",true)),Power(Plus($p("a",true),Times($p("b",true),$p("x"))),$p("n"))),Power(Plus($p("c"),Times($p("d",true),$p("x"))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(ExpandExpression(Times(Power($s("x"),$s("m")),Power(Plus(Plus(Times($s("a"),$s("c")),Times(Plus(Times($s("b"),$s("c")),Times($s("a"),$s("d"))),$s("x"))),Times(Times($s("b"),$s("d")),Power($s("x"),C2))),$s("n"))),$s("x")),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),IntegerQ(List($s("m"),$s("n")))),Less($s("n"),C0)))),
SetDelayed(ExpandExpression(Power(Sin($p("v")),$p("n")),$p("x",$s("Symbol"))),
    Condition(Expand(TrigReduce(Power(Sin($s("v")),$s("n"))),$s("x")),And(IntegerQ($s("n")),Greater($s("n"),C1)))),
SetDelayed(ExpandExpression(Power(Cos($p("v")),$p("n")),$p("x",$s("Symbol"))),
    Condition(Expand(TrigReduce(Power(Cos($s("v")),$s("n"))),$s("x")),And(IntegerQ($s("n")),Greater($s("n"),C1)))),
SetDelayed(ExpandExpression(Times(Power(Sin($p("v")),$p("n")),Power(Plus($p("c",true),Times($p("d",true),Power(Cos($p("v")),C2))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Times(CN1,Power(Sin($s("v")),Plus($s("n"),Times(CN1,C2)))),Power($s("d"),CN1)),Dist(Times(Plus($s("c"),$s("d")),Power($s("d"),CN1)),ExpandExpression(Times(Power(Sin($s("v")),Plus($s("n"),Times(CN1,C2))),Power(Plus($s("c"),Times($s("d"),Power(Cos($s("v")),C2))),CN1)),$s("x")))),And(And(FreeQ(List($s("c"),$s("d")),$s("x")),EvenQ($s("n"))),Greater($s("n"),C1)))),
SetDelayed(ExpandExpression(Times(Power(Cos($p("v")),$p("n")),Power(Plus($p("c",true),Times($p("d",true),Power(Sin($p("v")),C2))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Times(CN1,Power(Cos($s("v")),Plus($s("n"),Times(CN1,C2)))),Power($s("d"),CN1)),Dist(Times(Plus($s("c"),$s("d")),Power($s("d"),CN1)),ExpandExpression(Times(Power(Cos($s("v")),Plus($s("n"),Times(CN1,C2))),Power(Plus($s("c"),Times($s("d"),Power(Sin($s("v")),C2))),CN1)),$s("x")))),And(And(FreeQ(List($s("c"),$s("d")),$s("x")),EvenQ($s("n"))),Greater($s("n"),C1)))),
SetDelayed(ExpandExpression(Times(Plus($p("a"),Times($p("b",true),Power(Sin($p("v")),C2))),Power(Plus($p("c",true),Times($p("d",true),Power(Cos($p("v")),C2))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Times(CN1,$s("b")),Power($s("d"),CN1)),Times(Plus(Times($s("b"),$s("c")),Times(Plus($s("a"),$s("b")),$s("d"))),Power(Times($s("d"),Plus($s("c"),Times($s("d"),Power(Cos($s("v")),C2)))),CN1))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")))),
SetDelayed(ExpandExpression(Times(Plus($p("a"),Times($p("b",true),Power(Cos($p("v")),C2))),Power(Plus($p("c",true),Times($p("d",true),Power(Sin($p("v")),C2))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Times(CN1,$s("b")),Power($s("d"),CN1)),Times(Plus(Times($s("b"),$s("c")),Times(Plus($s("a"),$s("b")),$s("d"))),Power(Times($s("d"),Plus($s("c"),Times($s("d"),Power(Sin($s("v")),C2)))),CN1))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")))),
SetDelayed(ExpandExpression(Times(Power(Tan($p("v")),$p("n",true)),Power(Plus(Times($p("a",true),Power(Sin($p("v")),$p("n",true))),Times($p("b",true),Power(Cos($p("v")),$p("n",true)))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(Sec($s("v")),$s("n")),Power($s("a"),CN1)),Times(CN1,Times($s("b"),Power(Times($s("a"),Plus(Times($s("a"),Power(Sin($s("v")),$s("n"))),Times($s("b"),Power(Cos($s("v")),$s("n"))))),CN1)))),And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))))),
SetDelayed(ExpandExpression(Times(Power(Cot($p("v")),$p("n",true)),Power(Plus(Times($p("a",true),Power(Sin($p("v")),$p("n",true))),Times($p("b",true),Power(Cos($p("v")),$p("n",true)))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(Csc($s("v")),$s("n")),Power($s("b"),CN1)),Times(CN1,Times($s("a"),Power(Times($s("b"),Plus(Times($s("a"),Power(Sin($s("v")),$s("n"))),Times($s("b"),Power(Cos($s("v")),$s("n"))))),CN1)))),And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))))),
SetDelayed(ExpandExpression(Times(Power(Sec($p("v")),$p("n",true)),Power(Plus($p("a"),Times($p("b",true),Power(Cot($p("v")),$p("n",true)))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(Sec($s("v")),$s("n")),Power($s("a"),CN1)),Times(CN1,Times($s("b"),Power(Times($s("a"),Plus(Times($s("a"),Power(Sin($s("v")),$s("n"))),Times($s("b"),Power(Cos($s("v")),$s("n"))))),CN1)))),And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))))),
SetDelayed(ExpandExpression(Times(Power(Csc($p("v")),$p("n",true)),Power(Plus($p("a"),Times($p("b",true),Power(Tan($p("v")),$p("n",true)))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(Csc($s("v")),$s("n")),Power($s("a"),CN1)),Times(CN1,Times($s("b"),Power(Times($s("a"),Plus(Times($s("b"),Power(Sin($s("v")),$s("n"))),Times($s("a"),Power(Cos($s("v")),$s("n"))))),CN1)))),And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))))),
SetDelayed(ExpandExpression(Times($p("u"),Power(Plus($p("a"),Times($p("b",true),Tan($p("v")))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(ExpandExpression(Times($s("u"),Power(Plus(Times(Power(Cos($s("v")),C2),Power($s("a"),CN1)),Times(Cos($s("v")),Times(Sin($s("v")),Power($s("b"),CN1)))),Times(CN1,$s("n")))),$s("x")),And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))),Less($s("n"),C0)),ZeroQ(Plus(Power($s("a"),C2),Power($s("b"),C2)))))),
SetDelayed(ExpandExpression(Times($p("u"),Power(Plus($p("a"),Times($p("b",true),Cot($p("v")))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(ExpandExpression(Times($s("u"),Power(Plus(Times(Power(Sin($s("v")),C2),Power($s("a"),CN1)),Times(Cos($s("v")),Times(Sin($s("v")),Power($s("b"),CN1)))),Times(CN1,$s("n")))),$s("x")),And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))),Less($s("n"),C0)),ZeroQ(Plus(Power($s("a"),C2),Power($s("b"),C2)))))),
SetDelayed(ExpandExpression(Times(Times($p("v"),Cos($p("u"))),Sin($p("u"))),$p("x",$s("Symbol"))),
    Condition(ExpandExpression(Times($s("v"),Times(Sin(Dist(C2,$s("u"))),C1D2)),$s("x")),Or(MatchQ($s("v"),Condition(Power($s("x"),$p("m")),RationalQ($s("m")))),MatchQ($s("v"),Condition(Power($p("f"),$p("w")),And(FreeQ($s("f"),$s("x")),LinearQ($s("w"),$s("x")))))))),
SetDelayed(ExpandExpression(Power(Sinh($p("v")),$p("n")),$p("x",$s("Symbol"))),
    Condition(Module(List($s("z")),Expand(NormalForm(Subst(TrigReduce(Power(Sinh($s("z")),$s("n"))),$s("z"),$s("v")),$s("x")),$s("x"))),And(IntegerQ($s("n")),Greater($s("n"),C1)))),
SetDelayed(ExpandExpression(Times(Power($p("x"),$p("m",true)),Power(Sinh($p("v")),$p("n",true))),$p("x",$s("Symbol"))),
    Condition(Module(List($s("z")),Expand(Times(Power($s("x"),$s("m")),NormalForm(Subst(TrigReduce(Power(Sinh($s("z")),$s("n"))),$s("z"),$s("v")),$s("x"))),$s("x"))),And(And(FreeQ($s("m"),$s("x")),IntegerQ($s("n"))),Greater($s("n"),C0)))),
SetDelayed(ExpandExpression(Power(Cosh($p("v")),$p("n")),$p("x",$s("Symbol"))),
    Condition(Module(List($s("z")),Expand(NormalForm(Subst(TrigReduce(Power(Cosh($s("z")),$s("n"))),$s("z"),$s("v")),$s("x")),$s("x"))),And(IntegerQ($s("n")),Greater($s("n"),C1)))),
SetDelayed(ExpandExpression(Times(Power($p("x"),$p("m",true)),Power(Cosh($p("v")),$p("n",true))),$p("x",$s("Symbol"))),
    Condition(Module(List($s("z")),Expand(Times(Power($s("x"),$s("m")),NormalForm(Subst(TrigReduce(Power(Cosh($s("z")),$s("n"))),$s("z"),$s("v")),$s("x"))),$s("x"))),And(And(FreeQ($s("m"),$s("x")),IntegerQ($s("n"))),Greater($s("n"),C0)))),
SetDelayed(ExpandExpression(Times(Power(Sinh($p("v")),$p("n")),Power(Plus($p("c",true),Times($p("d",true),Power(Cosh($p("v")),C2))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(Sinh($s("v")),Plus($s("n"),Times(CN1,C2))),Power($s("d"),CN1)),Times(CN1,Dist(Times(Plus($s("c"),$s("d")),Power($s("d"),CN1)),ExpandExpression(Times(Power(Sinh($s("v")),Plus($s("n"),Times(CN1,C2))),Power(Plus($s("c"),Times($s("d"),Power(Cosh($s("v")),C2))),CN1)),$s("x"))))),And(And(FreeQ(List($s("c"),$s("d")),$s("x")),EvenQ($s("n"))),Greater($s("n"),C1)))),
SetDelayed(ExpandExpression(Times(Power(Cosh($p("v")),$p("n")),Power(Plus($p("c",true),Times($p("d",true),Power(Sinh($p("v")),C2))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(Cosh($s("v")),Plus($s("n"),Times(CN1,C2))),Power($s("d"),CN1)),Times(CN1,Dist(Times(Plus($s("c"),Times(CN1,$s("d"))),Power($s("d"),CN1)),ExpandExpression(Times(Power(Cosh($s("v")),Plus($s("n"),Times(CN1,C2))),Power(Plus($s("c"),Times($s("d"),Power(Sinh($s("v")),C2))),CN1)),$s("x"))))),And(And(FreeQ(List($s("c"),$s("d")),$s("x")),EvenQ($s("n"))),Greater($s("n"),C1)))),
SetDelayed(ExpandExpression(Times(Plus($p("a"),Times($p("b",true),Power(Sinh($p("v")),C2))),Power(Plus($p("c",true),Times($p("d",true),Power(Cosh($p("v")),C2))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times($s("b"),Power($s("d"),CN1)),Times(CN1,Times(Plus(Times($s("b"),$s("c")),Times(CN1,Times(Plus($s("a"),Times(CN1,$s("b"))),$s("d")))),Power(Times($s("d"),Plus($s("c"),Times($s("d"),Power(Cosh($s("v")),C2)))),CN1)))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")))),
SetDelayed(ExpandExpression(Times(Plus($p("a"),Times($p("b",true),Power(Cosh($p("v")),C2))),Power(Plus($p("c",true),Times($p("d",true),Power(Sinh($p("v")),C2))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times($s("b"),Power($s("d"),CN1)),Times(CN1,Times(Plus(Times($s("b"),$s("c")),Times(CN1,Times(Plus($s("a"),$s("b")),$s("d")))),Power(Times($s("d"),Plus($s("c"),Times($s("d"),Power(Sinh($s("v")),C2)))),CN1)))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")))),
SetDelayed(ExpandExpression(Times(Power(Tanh($p("v")),$p("n",true)),Power(Plus(Times($p("a",true),Power(Sinh($p("v")),$p("n",true))),Times($p("b",true),Power(Cosh($p("v")),$p("n",true)))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(Sech($s("v")),$s("n")),Power($s("a"),CN1)),Times(CN1,Times($s("b"),Power(Times($s("a"),Plus(Times($s("a"),Power(Sinh($s("v")),$s("n"))),Times($s("b"),Power(Cosh($s("v")),$s("n"))))),CN1)))),And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))))),
SetDelayed(ExpandExpression(Times(Power(Coth($p("v")),$p("n",true)),Power(Plus(Times($p("a",true),Power(Sinh($p("v")),$p("n",true))),Times($p("b",true),Power(Cosh($p("v")),$p("n",true)))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(Csch($s("v")),$s("n")),Power($s("b"),CN1)),Times(CN1,Times($s("a"),Power(Times($s("b"),Plus(Times($s("a"),Power(Sinh($s("v")),$s("n"))),Times($s("b"),Power(Cosh($s("v")),$s("n"))))),CN1)))),And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))))),
SetDelayed(ExpandExpression(Times(Power(Sech($p("v")),$p("n",true)),Power(Plus($p("a"),Times($p("b",true),Power(Coth($p("v")),$p("n",true)))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(Sech($s("v")),$s("n")),Power($s("a"),CN1)),Times(CN1,Times($s("b"),Power(Times($s("a"),Plus(Times($s("a"),Power(Sinh($s("v")),$s("n"))),Times($s("b"),Power(Cosh($s("v")),$s("n"))))),CN1)))),And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))))),
SetDelayed(ExpandExpression(Times(Power(Csch($p("v")),$p("n",true)),Power(Plus($p("a"),Times($p("b",true),Power(Tanh($p("v")),$p("n",true)))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(Csch($s("v")),$s("n")),Power($s("a"),CN1)),Times(CN1,Times($s("b"),Power(Times($s("a"),Plus(Times($s("b"),Power(Sinh($s("v")),$s("n"))),Times($s("a"),Power(Cosh($s("v")),$s("n"))))),CN1)))),And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))))),
SetDelayed(ExpandExpression(Times($p("u"),Power(Plus($p("a"),Times($p("b",true),Tanh($p("v")))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(ExpandExpression(Times($s("u"),Power(Plus(Times(Power(Cosh($s("v")),C2),Power($s("a"),CN1)),Times(CN1,Times(Cosh($s("v")),Times(Sinh($s("v")),Power($s("b"),CN1))))),Times(CN1,$s("n")))),$s("x")),And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))),Less($s("n"),C0)),ZeroQ(Plus(Power($s("a"),C2),Times(CN1,Power($s("b"),C2))))))),
SetDelayed(ExpandExpression(Times($p("u"),Power(Plus($p("a"),Times($p("b",true),Coth($p("v")))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(ExpandExpression(Times($s("u"),Power(Plus(Times(Times(CN1,Power(Sinh($s("v")),C2)),Power($s("a"),CN1)),Times(Cosh($s("v")),Times(Sinh($s("v")),Power($s("b"),CN1)))),Times(CN1,$s("n")))),$s("x")),And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))),Less($s("n"),C0)),ZeroQ(Plus(Power($s("a"),C2),Times(CN1,Power($s("b"),C2))))))),
SetDelayed(ExpandExpression(Times(Times($p("v"),Cosh($p("u"))),Sinh($p("u"))),$p("x",$s("Symbol"))),
    Condition(ExpandExpression(Times($s("v"),Times(Sinh(Dist(C2,$s("u"))),C1D2)),$s("x")),Or(MatchQ($s("v"),Condition(Power($s("x"),$p("m")),RationalQ($s("m")))),MatchQ($s("v"),Condition(Power($p("f"),$p("w")),And(FreeQ($s("f"),$s("x")),LinearQ($s("w"),$s("x")))))))),
SetDelayed(ExpandExpression(Times($p("u",true),Power($p("v"),$p("n"))),$p("x",$s("Symbol"))),
    Condition(Module(List(Set($s("w"),ExpandExpression($s("u"),$s("x")))),If(SumQ($s("w")),Map(Function(RegularizeTerm(Times(Slot1,Power($s("v"),$s("n"))),$s("x"))),$s("w")),Times($s("w"),Power($s("v"),$s("n"))))),And(FractionQ($s("n")),Or(Less($s("n"),C0),Greater($s("n"),C1))))),
SetDelayed(SumFreeQ($p("u")),
    If(AtomQ($s("u")),True,If(SumQ($s("u")),False,Catch(CompoundExpression(Scan(Function(If(SumFreeQ(Slot1),$s("Null"),Throw(False))),$s("u")),True))))),
SetDelayed(ExpandExpression($p("u"),$p("x",$s("Symbol"))),
    If(Or(Or(Or(Or(Or(Or(SumQ($s("u")),MatchQ($s("u"),Condition(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),Power($s("x"),$p("n",true)))),CN1)),And(FreeQ(List($s("a"),$s("b"),$s("c")),$s("x")),IntegerQ($s("n")))))),MatchQ($s("u"),Condition(Times(Times($p("c",true),Power($s("x"),$p("m",true))),Power(Plus($p("a"),Times($p("b",true),Power($s("x"),$p("n",true)))),$p("p",true))),And(And(FreeQ(List($s("a"),$s("b"),$s("c")),$s("x")),IntegerQ(List($s("m"),$s("n"),$s("p")))),ZeroQ(Plus(Plus($s("m"),Times(CN1,$s("n"))),C1)))))),MatchQ($s("u"),Condition(Times(Times($p("c",true),Power($s("x"),$p("m",true))),Power(Plus($p("v"),Times($p("b",true),Power($s("x"),$p("n",true)))),$p("p",true))),And(And(And(And(FreeQ(List($s("b"),$s("c")),$s("x")),Not(AlgebraicFunctionQ($s("v"),$s("x")))),IntegerQ(List($s("m"),$s("n"),$s("p")))),Less($s("p"),C0)),And(Less(C0,$s("n")),LessEqual($s("n"),$s("m"))))))),MatchQ($s("u"),Condition(Times(Power($s("x"),$p("m")),Power($($p("f"),Plus($p("a",true),Times($p("b",true),$s("x")))),$p("n",true))),And(And(FreeQ(List($s("a"),$s("b"),$s("m"),$s("n")),$s("x")),MemberQ(List($s("Tan"),$s("Cot"),$s("Tanh"),$s("Coth")),$s("f"))),Not(And(IntegerQ($s("m")),Greater($s("m"),C0))))))),MatchQ($s("u"),Condition(Times($($p("f"),Plus(Plus($p("a",true),Times($p("b",true),$s("x"))),Times($p("c",true),Power($s("x"),C2)))),Power($s("x"),CN1)),And(FreeQ(List($s("a"),$s("b"),$s("c")),$s("x")),MemberQ(List($s("Sin"),$s("Cos"),$s("Sinh"),$s("Cosh")),$s("f")))))),MatchQ($s("u"),Condition(Power(Plus($p("a"),Times($p("b",true),Power($($p("f"),Plus($p("c",true),Times($p("d",true),$s("x")))),C2))),$p("n")),And(And(FreeQ(List($s("a"),$s("b")),$s("x")),MemberQ(List($s("Sin"),$s("Cos"),$s("Sinh"),$s("Cosh")),$s("f"))),IntegerQ($s("n")))))),$s("u"),Module(List($s("tmp"),$s("lst")),CompoundExpression(Set($s("tmp"),FindKernel($s("u"),$s("x"))),If(And(NotFalseQ($s("tmp")),FunctionOfKernelQ($s("u"),$s("tmp"),$s("x"))),Subst(ExpandExpression(Subst($s("u"),$s("tmp"),$s("x")),$s("x")),$s("x"),$s("tmp")),CompoundExpression(Set($s("tmp"),FunctionOfTrigQ($s("u"),$s("x"),$s("x"))),If(And(And($s("tmp"),Or(ContainsQ($s("u"),$s("Sin"),$s("x")),ContainsQ($s("u"),$s("Csc"),$s("x")))),FunctionOfQ(Sin($s("x")),$s("u"),$s("x"))),ExpandTrigExpression($s("u"),Sin($s("x")),$s("x")),If(And(And($s("tmp"),Or(ContainsQ($s("u"),$s("Cos"),$s("x")),ContainsQ($s("u"),$s("Sec"),$s("x")))),FunctionOfQ(Cos($s("x")),$s("u"),$s("x"))),ExpandTrigExpression($s("u"),Cos($s("x")),$s("x")),If(And(And($s("tmp"),Or(ContainsQ($s("u"),$s("Tan"),$s("x")),ContainsQ($s("u"),$s("Cot"),$s("x")))),FunctionOfQ(Tan($s("x")),$s("u"),$s("x"))),ExpandTrigExpression($s("u"),Tan($s("x")),$s("x")),CompoundExpression(Set($s("tmp"),FunctionOfHyperbolicQ($s("u"),$s("x"),$s("x"))),If(And(And($s("tmp"),Or(ContainsQ($s("u"),$s("Sinh"),$s("x")),ContainsQ($s("u"),$s("Csch"),$s("x")))),FunctionOfQ(Sinh($s("x")),$s("u"),$s("x"))),ExpandTrigExpression($s("u"),Sinh($s("x")),$s("x")),If(And(And($s("tmp"),Or(ContainsQ($s("u"),$s("Cosh"),$s("x")),ContainsQ($s("u"),$s("Sech"),$s("x")))),FunctionOfQ(Cosh($s("x")),$s("u"),$s("x"))),ExpandTrigExpression($s("u"),Cosh($s("x")),$s("x")),If(And(And($s("tmp"),Or(ContainsQ($s("u"),$s("Tanh"),$s("x")),ContainsQ($s("u"),$s("Coth"),$s("x")))),FunctionOfQ(Tanh($s("x")),$s("u"),$s("x"))),ExpandTrigExpression($s("u"),Tanh($s("x")),$s("x")),Module(List($s("v")),If(And(AlgebraicFunctionQ($s("u"),$s("x")),Not(RationalFunctionQ($s("u"),$s("x")))),If(GoodExpansionQ($s("u"),Set($s("v"),ExpandExpressionAux($s("u"),$s("x"))),$s("x")),RegularizeTerm($s("v"),$s("x")),$s("u")),If(GoodExpansionQ($s("u"),Set($s("v"),Apart($s("u"),$s("x"))),$s("x")),RegularizeTerm($s("v"),$s("x")),If(GoodExpansionQ($s("u"),Set($s("v"),Apart($s("u"))),$s("x")),RegularizeTerm($s("v"),$s("x")),If(GoodExpansionQ($s("u"),Set($s("v"),ExpandExpressionAux($s("u"),$s("x"))),$s("x")),RegularizeTerm($s("v"),$s("x")),If(TrigHyperbolicFreeQ($s("u"),$s("x")),CompoundExpression(Set($s("v"),Apart($s("u"),$s("x"))),RegularizeTerm(If(GoodExpansionQ($s("u"),$s("v"),$s("x")),$s("v"),$s("u")),$s("x"))),CompoundExpression(CompoundExpression(Set($s("tmp"),TryTrigReduceQ($s("u"))),If($s("tmp"),Set($s("lst"),SplitFreeFactors(TrigReduce($s("u")),$s("x"))))),If(And($s("tmp"),GoodExpansionQ($s("u"),Set($s("v"),Apart(Part($s("lst"),C2),$s("x"))),$s("x"))),Map(Function(RegularizeTerm(Times(Part($s("lst"),C1),Slot1),$s("x"))),$s("v")),If(And($s("tmp"),GoodExpansionQ($s("u"),Set($s("v"),Apart(Part($s("lst"),C2))),$s("x"))),Map(Function(RegularizeTerm(Times(Part($s("lst"),C1),Slot1),$s("x"))),$s("v")),If(And($s("tmp"),GoodExpansionQ($s("u"),Set($s("v"),ExpandExpressionAux(Part($s("lst"),C2),$s("x"))),$s("x"))),Map(Function(RegularizeTerm(Times(Part($s("lst"),C1),Slot1),$s("x"))),$s("v")),CompoundExpression(Set($s("v"),SmartTrigExpand($s("u"),$s("x"))),If(SumQ($s("v")),RegularizeTerm($s("v"),$s("x")),CompoundExpression(Set($s("v"),TrigExpand($s("u"))),If(And(SumQ($s("v")),SumQ(Set($s("v"),Simplify($s("v"))))),RegularizeTerm($s("v"),$s("x")),CompoundExpression(Set($s("v"),Apart($s("u"),$s("x"))),RegularizeTerm(If(GoodExpansionQ($s("u"),$s("v"),$s("x")),$s("v"),$s("u")),$s("x")))))))))))))))))))))))))))))),
SetDelayed(TryTrigReduceQ($p("u")),
    MatchQ($s("u"),Condition(Times($p("v"),Power($($p("f"),$p("w")),$p("n"))),And(IntegerQ($s("n")),MemberQ(List($s("Sin"),$s("Cos"),$s("Sinh"),$s("Cosh")),$s("f")))))),
SetDelayed(ExpandTrigExpression($p("u"),$p("v"),$p("x",$s("Symbol"))),
    Module(List(Set($s("w"),TrigSimplify(Subst(ExpandExpression(SubstFor($s("v"),$s("u"),$s("x")),$s("x")),$s("x"),$s("v"))))),If(SumQ($s("w")),Map(Function(ExpandTrigExpressionAux(Slot1,$s("x"))),$s("w")),$s("w")))),
SetDelayed(ExpandTrigExpressionAux(Times($p("u"),$p("v")),$p("x",$s("Symbol"))),
    Condition(Map(Function(Times($s("u"),Slot1)),$s("v")),And(And(SumQ($s("v")),Not(FreeQ($s("u"),$s("x")))),Not(FreeQ($s("v"),$s("x")))))),
SetDelayed(ExpandTrigExpressionAux($p("u"),$p("x",$s("Symbol"))),
    $s("u")),
SetDelayed(GoodExpansionQ($p("u"),$p("v"),$p("x",$s("Symbol"))),
    If(SumQ($s("v")),If(PolynomialQ($s("u"),$s("x")),True,If(RationalFunctionQ($s("u"),$s("x")),If(RationalFunctionQ($s("v"),$s("x")),Module(List(Set($s("lst"),RationalFunctionExponents($s("u"),$s("x")))),Catch(CompoundExpression(Scan(Function(If(SimplerRationalFunctionQ($s("lst"),RationalFunctionExponents(Slot1,$s("x"))),Throw(False))),$s("v")),True))),False),True)),False)),
SetDelayed(SimplerRationalFunctionQ($p("lst1"),$p("lst2")),
    And(And(LessEqual(Part($s("lst1"),C1),Part($s("lst2"),C1)),LessEqual(Part($s("lst1"),C2),Part($s("lst2"),C2))),Or(Less(Part($s("lst1"),C1),Part($s("lst2"),C1)),Less(Part($s("lst1"),C2),Part($s("lst2"),C2))))),
SetDelayed(ExpandExpressionAux(Times(Plus($p("d"),Times($p("e",true),Power($p("x"),$p("k",true)))),Power(Plus(Plus($p("a"),Times($p("b",true),Power($p("x"),$p("k")))),Times($p("c",true),Power($p("x"),$p("j")))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(Plus(Times($s("d"),Power(Plus(Plus($s("a"),Times($s("b"),Power($s("x"),$s("k")))),Times($s("c"),Power($s("x"),$s("j")))),$s("n"))),Times(Times($s("e"),Power($s("x"),$s("k"))),Power(Plus(Plus($s("a"),Times($s("b"),Power($s("x"),$s("k")))),Times($s("c"),Power($s("x"),$s("j")))),$s("n")))),And(And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("e")),$s("x")),IntegerQ(List($s("n"),$s("k"),$s("j")))),EvenQ($s("k"))),Equal($s("j"),Times(C2,$s("k")))),Less($s("n"),CN1)))),
SetDelayed(ExpandExpressionAux(Times($p("u"),$p("v")),$p("x",$s("Symbol"))),
    Condition(Map(Function(Times(Slot1,$s("v"))),$s("u")),And(SumQ($s("u")),Not(FreeQ($s("u"),$s("x")))))),
SetDelayed(ExpandExpressionAux(Times(Power($p("u"),$p("n")),$p("v",true)),$p("x",$s("Symbol"))),
    Condition(Module(List(Set($s("w"),Expand(Power($s("u"),$s("n")),$s("x")))),Condition(Map(Function(Times(Slot1,$s("v"))),$s("w")),SumQ($s("w")))),And(And(And(SumQ($s("u")),IntegerQ($s("n"))),Greater($s("n"),C0)),Not(FreeQ($s("u"),$s("x")))))),
SetDelayed(RegularizeTerm(Times($p("c",true),Times($p("u"),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),$p("n")))),CN1))),$p("x",$s("Symbol"))),
    Condition(If(PolynomialQ($s("u"),$s("x")),Module(List($s("k")),Sum(RegularizeTerm(Times(Times($s("c"),Coefficient($s("u"),$s("x"),$s("k"))),Times(Power($s("x"),$s("k")),Power(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))),CN1))),$s("x")),List($s("k"),C0,Exponent($s("u"),$s("x"))))),Map(Function(RegularizeTerm(Times($s("c"),Times(Slot1,Power(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))),CN1))),$s("x"))),$s("u"))),And(And(And(And(FreeQ(List($s("a"),$s("b"),$s("c")),$s("x")),IntegerQ($s("n"))),Greater($s("n"),C0)),SumQ($s("u"))),Not(FreeQ($s("u"),$s("x")))))),
SetDelayed(RegularizeTerm($p("u"),$p("x",$s("Symbol"))),
    If(SumQ($s("u")),Map(Function(RegularizeTerm(Slot1,$s("x"))),$s("u")),Module(List($s("lst1"),$s("lst2")),CompoundExpression(CompoundExpression(CompoundExpression(CompoundExpression(Set($s("lst1"),SplitFreeFactors(NormalForm($s("u"),$s("x")),$s("x"))),Set($s("lst2"),SplitFreeFactors(Regularize(Part($s("lst1"),C2),$s("x")),$s("x")))),Set($s("lst2"),List(Times(Part($s("lst1"),C1),Part($s("lst2"),C1)),Part($s("lst2"),C2)))),Set($s("lst2"),List(Times(Simplify(Numerator(Part($s("lst2"),C1))),Power(Simplify(Denominator(Part($s("lst2"),C1))),CN1)),Part($s("lst2"),C2)))),If(SumQ(Part($s("lst2"),C2)),Map(Function(Times(Part($s("lst2"),C1),Slot1)),Part($s("lst2"),C2)),Times(Part($s("lst2"),C1),Part($s("lst2"),C2))))))),
SetDelayed(ContainsQ($p("u"),$p("f"),$p("x")),
    Greater(Count($s("u"),Condition($($s("f"),Times($p("n",true),$s("x"))),IntegerQ($s("n"))),CInfinity),C0)),
SetDelayed(FunctionOfKernelQ($p("u"),$p("v"),$p("x")),
    If(SameQ($s("u"),$s("v")),True,If(AtomQ($s("u")),UnsameQ($s("u"),$s("x")),Catch(CompoundExpression(Scan(Function(If(Not(FunctionOfKernelQ(Slot1,$s("v"),$s("x"))),Throw(False))),$s("u")),True))))),
SetDelayed(FindKernel($p("u"),$p("x")),
    If(AlgebraicFunctionQ($s("u"),$s("x")),False,If(And(SameQ(Length($s("u")),C1),AlgebraicFunctionQ(Part($s("u"),C1),$s("x"))),$s("u"),If(And(And(SameQ(Length($s("u")),C2),AlgebraicFunctionQ(Part($s("u"),C1),$s("x"))),FreeQ(Part($s("u"),C2),$s("x"))),$s("u"),If(And(And(SameQ(Length($s("u")),C2),AlgebraicFunctionQ(Part($s("u"),C2),$s("x"))),FreeQ(Part($s("u"),C1),$s("x"))),$s("u"),Module(List($s("tmp")),Catch(CompoundExpression(Scan(Function(If(NotFalseQ(Set($s("tmp"),FindKernel(Slot1,$s("x")))),Throw($s("tmp")))),$s("u")),False)))))))),
SetDelayed(CommonNumericFactors($p("lst")),
    Module(List(Set($s("num"),Apply($s("GCD"),Map($s("Integrate::NumericFactor"),$s("lst"))))),Prepend(Map(Function(Times(Slot1,Power($s("num"),CN1))),$s("lst")),$s("num")))),
SetDelayed(NumericFactor($p("u")),
    If(NumberQ($s("u")),If(ZeroQ(Im($s("u"))),$s("u"),If(ZeroQ(Re($s("u"))),Im($s("u")),C1)),If(PowerQ($s("u")),If(And(RationalQ(Part($s("u"),C1)),FractionQ(Part($s("u"),C2))),If(Greater(Part($s("u"),C2),C0),Times(C1,Power(Denominator(Part($s("u"),C1)),CN1)),Times(C1,Power(Denominator(Times(C1,Power(Part($s("u"),C1),CN1))),CN1))),C1),If(ProductQ($s("u")),Times(NumericFactor(First($s("u"))),NumericFactor(Rest($s("u")))),C1)))),
SetDelayed(NonnumericFactors($p("u")),
    If(NumberQ($s("u")),If(ZeroQ(Im($s("u"))),C1,If(ZeroQ(Re($s("u"))),CI,$s("u"))),If(PowerQ($s("u")),If(And(RationalQ(Part($s("u"),C1)),FractionQ(Part($s("u"),C2))),Times($s("u"),Power(NumericFactor($s("u")),CN1)),$s("u")),If(ProductQ($s("u")),Times(NonnumericFactors(First($s("u"))),NonnumericFactors(Rest($s("u")))),$s("u"))))),
SetDelayed(ContentFactor($p("expn")),
    If(AtomQ($s("expn")),$s("expn"),If(ProductQ($s("expn")),Map($s("Integrate::ContentFactor"),$s("expn")),If(SumQ($s("expn")),Module(List(Set($s("lst"),CommonFactors(Apply($s("List"),$s("expn"))))),If(Or(SameQ(Part($s("lst"),C1),C1),SameQ(Part($s("lst"),C1),CN1)),$s("expn"),Times(Part($s("lst"),C1),Apply($s("Plus"),Rest($s("lst")))))),$s("expn"))))),
SetDelayed(CommonFactors($p("lst")),
    Module(List($s("lst1"),$s("lst2"),$s("lst3"),$s("lst4"),$s("common"),$s("base"),$s("num")),CompoundExpression(CompoundExpression(CompoundExpression(CompoundExpression(CompoundExpression(CompoundExpression(Set($s("lst1"),Map($s("Integrate::NonnumericFactors"),$s("lst"))),Set($s("lst2"),Map($s("Integrate::NumericFactor"),$s("lst")))),Set($s("num"),Apply($s("GCD"),$s("lst2")))),If(MapAnd(Function(Less(Slot1,C0)),$s("lst2")),Set($s("num"),Times(CN1,$s("num"))))),Set($s("common"),$s("num"))),Set($s("lst2"),Map(Function(Times(Slot1,Power($s("num"),CN1))),$s("lst2")))),While(True,CompoundExpression(CompoundExpression(Set($s("lst3"),Map($s("Integrate::LeadFactor"),$s("lst1"))),If(Apply($s("SameQ"),$s("lst3")),CompoundExpression(Set($s("common"),Times($s("common"),Part($s("lst3"),C1))),Set($s("lst1"),Map($s("Integrate::RemainingFactors"),$s("lst1")))),If(And(MapAnd(Function(And(And(LogQ(Slot1),IntegerQ(First(Slot1))),Greater(First(Slot1),C0))),$s("lst3")),MapAnd($s("Integrate::RationalQ"),Set($s("lst4"),Map(Function(FullSimplify(Times(Slot1,Power(First($s("lst3")),CN1)))),$s("lst3"))))),CompoundExpression(CompoundExpression(CompoundExpression(Set($s("num"),Apply($s("GCD"),$s("lst4"))),Set($s("common"),Times($s("common"),Log(Power(Part(First($s("lst3")),C1),$s("num")))))),Set($s("lst2"),Map2(Function(Times(Slot1,Times(Slot2,Power($s("num"),CN1)))),$s("lst2"),$s("lst4")))),Set($s("lst1"),Map($s("Integrate::RemainingFactors"),$s("lst1")))),If(And(Apply($s("SameQ"),Map($s("Integrate::LeadBase"),$s("lst1"))),MapAnd($s("Integrate::RationalQ"),Set($s("lst4"),Map($s("Integrate::LeadDegree"),$s("lst1"))))),CompoundExpression(CompoundExpression(CompoundExpression(CompoundExpression(Set($s("num"),Smallest($s("lst4"))),Set($s("base"),LeadBase(Part($s("lst1"),C1)))),If(Unequal($s("num"),C0),Set($s("common"),Times($s("common"),Power($s("base"),$s("num")))))),Set($s("lst2"),Map2(Function(Times(Slot1,Power($s("base"),Plus(Slot2,Times(CN1,$s("num")))))),$s("lst2"),$s("lst4")))),Set($s("lst1"),Map($s("Integrate::RemainingFactors"),$s("lst1")))),CompoundExpression(CompoundExpression(Set($s("num"),MostMainFactorPosition($s("lst3"))),Set($s("lst2"),ReplacePart($s("lst2"),Times(Part($s("lst3"),$s("num")),Part($s("lst2"),$s("num"))),$s("num")))),Set($s("lst1"),ReplacePart($s("lst1"),RemainingFactors(Part($s("lst1"),$s("num"))),$s("num")))))))),If(MapAnd(Function(SameQ(Slot1,C1)),$s("lst1")),Return(Prepend($s("lst2"),$s("common"))))))))),
SetDelayed(MostMainFactorPosition($p("lst",$s("List"))),
    Module(List(Set($s("factor"),C1),Set($s("num"),C1)),CompoundExpression(Do(If(Greater(FactorOrder(Part($s("lst"),$s("i")),$s("factor")),C0),CompoundExpression(Set($s("factor"),Part($s("lst"),$s("i"))),Set($s("num"),$s("i")))),List($s("i"),Length($s("lst")))),$s("num")))),
SetDelayed(FactorOrder($p("u"),$p("v")),
    If(SameQ($s("u"),C1),If(SameQ($s("v"),C1),C0,CN1),If(SameQ($s("v"),C1),C1,Order($s("u"),$s("v"))))),
SetDelayed(Smallest($p("num1"),$p("num2")),
    If(Greater($s("num1"),C0),If(Greater($s("num2"),C0),Min($s("num1"),$s("num2")),C0),If(Greater($s("num2"),C0),C0,Max($s("num1"),$s("num2"))))),
SetDelayed(Smallest($p("lst",$s("List"))),
    Module(List(Set($s("num"),Part($s("lst"),C1))),CompoundExpression(Scan(Function(Set($s("num"),Smallest($s("num"),Slot1))),Rest($s("lst"))),$s("num")))),
SetDelayed(MonomialFactor($p("u"),$p("x",$s("Symbol"))),
    If(AtomQ($s("u")),If(SameQ($s("u"),$s("x")),List(C1,C1),List(C0,$s("u"))),If(PowerQ($s("u")),If(IntegerQ(Part($s("u"),C2)),Module(List(Set($s("lst"),MonomialFactor(Part($s("u"),C1),$s("x")))),List(Times(Part($s("lst"),C1),Part($s("u"),C2)),Power(Part($s("lst"),C2),Part($s("u"),C2)))),If(And(SameQ(Part($s("u"),C1),$s("x")),FreeQ(Part($s("u"),C2),$s("x"))),List(Part($s("u"),C2),C1),List(C0,$s("u")))),If(ProductQ($s("u")),Module(List(Set($s("lst1"),MonomialFactor(First($s("u")),$s("x"))),Set($s("lst2"),MonomialFactor(Rest($s("u")),$s("x")))),List(Plus(Part($s("lst1"),C1),Part($s("lst2"),C1)),Times(Part($s("lst1"),C2),Part($s("lst2"),C2)))),If(SumQ($s("u")),Module(List($s("lst"),$s("deg")),CompoundExpression(CompoundExpression(CompoundExpression(Set($s("lst"),Map(Function(MonomialFactor(Slot1,$s("x"))),Apply($s("List"),$s("u")))),Set($s("deg"),Part(Part($s("lst"),C1),C1))),Scan(Function(Set($s("deg"),MinimumDegree($s("deg"),Part(Slot1,C1)))),Rest($s("lst")))),If(Or(ZeroQ($s("deg")),And(RationalQ($s("deg")),Less($s("deg"),C0))),List(C0,$s("u")),List($s("deg"),Apply($s("Plus"),Map(Function(Times(Power($s("x"),Plus(Part(Slot1,C1),Times(CN1,$s("deg")))),Part(Slot1,C2))),$s("lst"))))))),List(C0,$s("u"))))))),
SetDelayed(MinimumDegree($p("deg1"),$p("deg2")),
    If(RationalQ($s("deg1")),If(RationalQ($s("deg2")),Min($s("deg1"),$s("deg2")),$s("deg1")),If(RationalQ($s("deg2")),$s("deg2"),Module(List(Set($s("deg"),Simplify(Plus($s("deg1"),Times(CN1,$s("deg2")))))),If(RationalQ($s("deg")),If(Greater($s("deg"),C0),$s("deg2"),$s("deg1")),If(OrderedQ(List($s("deg1"),$s("deg2"))),$s("deg1"),$s("deg2"))))))),
SetDelayed(ConstantFactor($p("u"),$p("x",$s("Symbol"))),
    If(FreeQ($s("u"),$s("x")),List($s("u"),C1),If(AtomQ($s("u")),List(C1,$s("u")),If(And(PowerQ($s("u")),FreeQ(Part($s("u"),C2),$s("x"))),Module(List(Set($s("lst"),ConstantFactor(Part($s("u"),C1),$s("x")))),If(IntegerQ(Part($s("u"),C2)),List(Power(Part($s("lst"),C1),Part($s("u"),C2)),Power(Part($s("lst"),C2),Part($s("u"),C2))),List(Power(PositiveFactors(Part($s("lst"),C1)),Part($s("u"),C2)),Power(Times(NonpositiveFactors(Part($s("lst"),C1)),Part($s("lst"),C2)),Part($s("u"),C2))))),If(ProductQ($s("u")),Module(List(Set($s("lst"),Map(Function(ConstantFactor(Slot1,$s("x"))),Apply($s("List"),$s("u"))))),List(Apply($s("Times"),Map($s("First"),$s("lst"))),Apply($s("Times"),Map($s("Integrate::Second"),$s("lst"))))),If(SumQ($s("u")),Module(List(Set($s("lst1"),Map(Function(ConstantFactor(Slot1,$s("x"))),Apply($s("List"),$s("u"))))),If(Apply($s("SameQ"),Map($s("Integrate::Second"),$s("lst1"))),List(Apply($s("Plus"),Map($s("First"),$s("lst1"))),Part(Part($s("lst1"),C1),C2)),Module(List(Set($s("lst2"),CommonFactors(Map($s("First"),$s("lst1"))))),List(First($s("lst2")),Apply($s("Plus"),Map2($s("Times"),Rest($s("lst2")),Map($s("Integrate::Second"),$s("lst1")))))))),List(C1,$s("u")))))))),
SetDelayed(PositiveFactors($p("u")),
    If(ZeroQ($s("u")),C1,If(RationalQ($s("u")),Abs($s("u")),If(PositiveQ($s("u")),$s("u"),If(ProductQ($s("u")),Map($s("Integrate::PositiveFactors"),$s("u")),C1))))),
SetDelayed(NonpositiveFactors($p("u")),
    If(ZeroQ($s("u")),$s("u"),If(RationalQ($s("u")),Sign($s("u")),If(PositiveQ($s("u")),C1,If(ProductQ($s("u")),Map($s("Integrate::NonpositiveFactors"),$s("u")),$s("u")))))),
SetDelayed(FunctionOfLinear($p("u"),$p("x",$s("Symbol"))),
    Module(List(Set($s("lst"),FunctionOfLinear($s("u"),False,False,$s("x"),False))),If(Or(Or(FalseQ($s("lst")),FalseQ(Part($s("lst"),C1))),And(SameQ(Part($s("lst"),C1),C0),SameQ(Part($s("lst"),C2),C1))),False,List(FunctionOfLinearSubst($s("u"),Part($s("lst"),C1),Part($s("lst"),C2),$s("x")),Part($s("lst"),C1),Part($s("lst"),C2))))),
SetDelayed(FunctionOfLinear($p("u"),$p("a"),$p("b"),$p("x"),$p("flag")),
    If(FreeQ($s("u"),$s("x")),List($s("a"),$s("b")),If(CalculusQ($s("u")),False,If(LinearQ($s("u"),$s("x")),If(FalseQ($s("a")),List(Coefficient($s("u"),$s("x"),C0),Coefficient($s("u"),$s("x"),C1)),Module(List(Set($s("lst"),CommonFactors(List($s("b"),Coefficient($s("u"),$s("x"),C1))))),If(And(ZeroQ(Coefficient($s("u"),$s("x"),C0)),Not($s("flag"))),List(C0,Part($s("lst"),C1)),If(ZeroQ(Plus(Times($s("b"),Coefficient($s("u"),$s("x"),C0)),Times(CN1,Times($s("a"),Coefficient($s("u"),$s("x"),C1))))),List(Times($s("a"),Power(Part($s("lst"),C2),CN1)),Part($s("lst"),C1)),List(C0,C1))))),If(And(PowerQ($s("u")),FreeQ(Part($s("u"),C1),$s("x"))),FunctionOfLinear(Times(Log(Part($s("u"),C1)),Part($s("u"),C2)),$s("a"),$s("b"),$s("x"),False),Module(List($s("lst")),If(And(ProductQ($s("u")),NonzeroQ(Part(Set($s("lst"),MonomialFactor($s("u"),$s("x"))),C1))),If(And(And(And(False,IntegerQ(Part($s("lst"),C1))),Unequal(Part($s("lst"),C1),CN1)),FreeQ(Part($s("lst"),C2),$s("x"))),If(And(RationalQ(LeadFactor(Part($s("lst"),C2))),Less(LeadFactor(Part($s("lst"),C2)),C0)),FunctionOfLinear(Times(DivideDegreesOfFactors(Times(CN1,Part($s("lst"),C2)),Part($s("lst"),C1)),$s("x")),$s("a"),$s("b"),$s("x"),False),FunctionOfLinear(Times(DivideDegreesOfFactors(Part($s("lst"),C2),Part($s("lst"),C1)),$s("x")),$s("a"),$s("b"),$s("x"),False)),False),CompoundExpression(Set($s("lst"),List($s("a"),$s("b"))),Catch(CompoundExpression(Scan(Function(CompoundExpression(Set($s("lst"),FunctionOfLinear(Slot1,Part($s("lst"),C1),Part($s("lst"),C2),$s("x"),SumQ($s("u")))),If(SameQ($s("lst"),False),Throw(False)))),$s("u")),$s("lst"))))))))))),
SetDelayed(FunctionOfLinearSubst($p("u"),$p("a"),$p("b"),$p("x")),
    If(FreeQ($s("u"),$s("x")),$s("u"),If(LinearQ($s("u"),$s("x")),Module(List(Set($s("tmp"),Coefficient($s("u"),$s("x"),C1))),CompoundExpression(Set($s("tmp"),If(SameQ($s("tmp"),$s("b")),C1,Times($s("tmp"),Power($s("b"),CN1)))),Plus(Plus(Coefficient($s("u"),$s("x"),C0),Times(CN1,Times($s("a"),$s("tmp")))),Times($s("tmp"),$s("x"))))),If(And(PowerQ($s("u")),FreeQ(Part($s("u"),C1),$s("x"))),Power(E,FullSimplify(FunctionOfLinearSubst(Times(Log(Part($s("u"),C1)),Part($s("u"),C2)),$s("a"),$s("b"),$s("x")))),Module(List($s("lst")),If(And(ProductQ($s("u")),NonzeroQ(Part(Set($s("lst"),MonomialFactor($s("u"),$s("x"))),C1))),If(And(RationalQ(LeadFactor(Part($s("lst"),C2))),Less(LeadFactor(Part($s("lst"),C2)),C0)),Times(CN1,Power(FunctionOfLinearSubst(Times(DivideDegreesOfFactors(Times(CN1,Part($s("lst"),C2)),Part($s("lst"),C1)),$s("x")),$s("a"),$s("b"),$s("x")),Part($s("lst"),C1))),Power(FunctionOfLinearSubst(Times(DivideDegreesOfFactors(Part($s("lst"),C2),Part($s("lst"),C1)),$s("x")),$s("a"),$s("b"),$s("x")),Part($s("lst"),C1))),Map(Function(FunctionOfLinearSubst(Slot1,$s("a"),$s("b"),$s("x"))),$s("u")))))))),
SetDelayed(DivideDegreesOfFactors($p("u"),$p("n")),
    If(ProductQ($s("u")),Map(Function(Power(LeadBase(Slot1),Times(LeadDegree(Slot1),Power($s("n"),CN1)))),$s("u")),Power(LeadBase($s("u")),Times(LeadDegree($s("u")),Power($s("n"),CN1))))),
SetDelayed(FunctionOfInverseLinear($p("u"),$p("x",$s("Symbol"))),
    FunctionOfInverseLinear($s("u"),$s("Null"),$s("x"))),
SetDelayed(FunctionOfInverseLinear($p("u"),$p("lst"),$p("x")),
    If(FreeQ($s("u"),$s("x")),$s("lst"),If(SameQ($s("u"),$s("x")),False,If(QuotientOfLinearsQ($s("u"),$s("x")),Module(List(Set($s("tmp"),Drop(QuotientOfLinearsParts($s("u"),$s("x")),C2))),If(SameQ(Part($s("tmp"),C2),C0),False,If(SameQ($s("lst"),$s("Null")),$s("tmp"),If(ZeroQ(Plus(Times(Part($s("lst"),C1),Part($s("tmp"),C2)),Times(CN1,Times(Part($s("lst"),C2),Part($s("tmp"),C1))))),$s("lst"),False)))),If(CalculusQ($s("u")),False,Module(List(Set($s("tmp"),$s("lst"))),Catch(CompoundExpression(Scan(Function(If(FalseQ(Set($s("tmp"),FunctionOfInverseLinear(Slot1,$s("tmp"),$s("x")))),Throw(False))),$s("u")),$s("tmp")))))))))
  );
}
