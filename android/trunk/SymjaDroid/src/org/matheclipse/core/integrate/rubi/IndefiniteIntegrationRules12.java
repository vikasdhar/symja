package org.matheclipse.core.integrate.rubi;


import static org.matheclipse.core.expression.F.$p;
import static org.matheclipse.core.expression.F.$s;
import static org.matheclipse.core.expression.F.And;
import static org.matheclipse.core.expression.F.ArcCos;
import static org.matheclipse.core.expression.F.ArcCosh;
import static org.matheclipse.core.expression.F.ArcCot;
import static org.matheclipse.core.expression.F.ArcSin;
import static org.matheclipse.core.expression.F.ArcSinh;
import static org.matheclipse.core.expression.F.ArcTanh;
import static org.matheclipse.core.expression.F.Block;
import static org.matheclipse.core.expression.F.C0;
import static org.matheclipse.core.expression.F.C1;
import static org.matheclipse.core.expression.F.C1D2;
import static org.matheclipse.core.expression.F.C1D4;
import static org.matheclipse.core.expression.F.C2;
import static org.matheclipse.core.expression.F.C4;
import static org.matheclipse.core.expression.F.CI;
import static org.matheclipse.core.expression.F.CN1;
import static org.matheclipse.core.expression.F.CN1D2;
import static org.matheclipse.core.expression.F.Condition;
import static org.matheclipse.core.expression.F.Cosh;
import static org.matheclipse.core.expression.F.Cot;
import static org.matheclipse.core.expression.F.D;
import static org.matheclipse.core.expression.F.E;
import static org.matheclipse.core.expression.F.Equal;
import static org.matheclipse.core.expression.F.EvenQ;
import static org.matheclipse.core.expression.F.Expand;
import static org.matheclipse.core.expression.F.False;
import static org.matheclipse.core.expression.F.FreeQ;
import static org.matheclipse.core.expression.F.Greater;
import static org.matheclipse.core.expression.F.IntegerQ;
import static org.matheclipse.core.expression.F.Less;
import static org.matheclipse.core.expression.F.LessEqual;
import static org.matheclipse.core.expression.F.List;
import static org.matheclipse.core.expression.F.Log;
import static org.matheclipse.core.expression.F.MatchQ;
import static org.matheclipse.core.expression.F.Module;
import static org.matheclipse.core.expression.F.Not;
import static org.matheclipse.core.expression.F.OddQ;
import static org.matheclipse.core.expression.F.Or;
import static org.matheclipse.core.expression.F.Part;
import static org.matheclipse.core.expression.F.Pi;
import static org.matheclipse.core.expression.F.Plus;
import static org.matheclipse.core.expression.F.Power;
import static org.matheclipse.core.expression.F.Set;
import static org.matheclipse.core.expression.F.SetDelayed;
import static org.matheclipse.core.expression.F.Sin;
import static org.matheclipse.core.expression.F.Sqrt;
import static org.matheclipse.core.expression.F.Tanh;
import static org.matheclipse.core.expression.F.Times;
import static org.matheclipse.core.expression.F.integer;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Dist;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FalseQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FunctionOfLinear;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.HalfIntegerQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.InverseFunctionFreeQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.InverseFunctionOfLinear;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NegQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NonzeroQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NotFalseQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.PosQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.PositiveQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.PowerVariableExpn;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.QuadraticQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RationalQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Regularize;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Rt;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Subst;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SubstFor;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SubstForInverseFunction;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ZeroQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.ArcCoth;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.ArcCsc;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.ArcCsch;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.ArcSec;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.ArcSech;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Coefficient;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.CoshIntegral;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Discriminant;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Erf;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Erfi;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.FunctionOfQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Gamma;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Head;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Int;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.PolyLog;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.PolynomialQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.SameQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Sech;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.SinhIntegral;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Unequal;

import org.matheclipse.core.interfaces.IAST;

/** 
 * IndefiniteIntegrationRules from the <a href="http://www.apmaths.uwo.ca/~arich/">Rubi -
 * rule-based integrator</a>.
 *  
 */
public class IndefiniteIntegrationRules12 { 
  public static IAST RULES = List( 
SetDelayed(Int(Times(Times(Power($p("x"),$p("m",true)),$p("u")),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),C2))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(Dist(Times(CN1,Power($s("a"),$s("n"))),Subst(Int(Regularize(Times(Times(Power(Cot($s("x")),$s("m")),Power(Sin($s("x")),Times(integer(-2L),Plus($s("n"),C1)))),SubstFor(ArcCot($s("x")),$s("u"),$s("x"))),$s("x")),$s("x")),$s("x"),ArcCot($s("x")))),And(And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),FunctionOfQ(ArcCot($s("x")),$s("u"),$s("x"))),ZeroQ(Plus($s("a"),Times(CN1,$s("b"))))),IntegerQ(List($s("m"),$s("n")))),Less($s("n"),C0)))),
SetDelayed(Int(Times(Times(Power($p("x"),$p("m",true)),$p("u")),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),C2))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(Dist(Times(CN1,Power($s("a"),$s("n"))),Subst(Int(Regularize(Times(Times(Power(Cot($s("x")),$s("m")),Power(Sin($s("x")),Times(integer(-2L),Plus($s("n"),C1)))),SubstFor(ArcCot($s("x")),$s("u"),$s("x"))),$s("x")),$s("x")),$s("x"),ArcCot($s("x")))),And(And(And(And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),FunctionOfQ(ArcCot($s("x")),$s("u"),$s("x"))),ZeroQ(Plus($s("a"),Times(CN1,$s("b"))))),HalfIntegerQ($s("n"))),Less($s("n"),CN1)),PositiveQ($s("a"))),IntegerQ($s("m"))))),
SetDelayed(Int(ArcCot($p("u")),$p("x",$s("Symbol"))),
    Condition(Plus(Times($s("x"),ArcCot($s("u"))),Int(Regularize(Times($s("x"),Times(D($s("u"),$s("x")),Power(Plus(C1,Power($s("u"),C2)),CN1))),$s("x")),$s("x"))),And(InverseFunctionFreeQ($s("u"),$s("x")),Not(MatchQ($s("u"),Condition(Plus($p("c",true),Times($p("d",true),Power($p("f"),Plus($p("a",true),Times($p("b",true),$s("x")))))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("f")),$s("x")))))))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),ArcCot($p("u"))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(ArcCot($s("u")),Power(Plus($s("m"),C1),CN1))),Dist(Times(C1,Power(Plus($s("m"),C1),CN1)),Int(Regularize(Times(Power($s("x"),Plus($s("m"),C1)),Times(D($s("u"),$s("x")),Power(Plus(C1,Power($s("u"),C2)),CN1))),$s("x")),$s("x")))),And(And(And(And(FreeQ($s("m"),$s("x")),NonzeroQ(Plus($s("m"),C1))),InverseFunctionFreeQ($s("u"),$s("x"))),Not(FunctionOfQ(Power($s("x"),Plus($s("m"),C1)),$s("u"),$s("x")))),FalseQ(PowerVariableExpn($s("u"),Plus($s("m"),C1),$s("x")))))),
SetDelayed(Int(Times($p("v"),ArcCot($p("u"))),$p("x",$s("Symbol"))),
    Condition(Module(List(Set($s("w"),Block(List(Set($s("ShowSteps"),False),Set($s("StepCounter"),$s("Null"))),Int($s("v"),$s("x"))))),Condition(Plus(Times($s("w"),ArcCot($s("u"))),Int(Regularize(Times($s("w"),Times(D($s("u"),$s("x")),Power(Plus(C1,Power($s("u"),C2)),CN1))),$s("x")),$s("x"))),InverseFunctionFreeQ($s("w"),$s("x")))),And(And(InverseFunctionFreeQ($s("u"),$s("x")),Not(MatchQ($s("v"),Condition(Power($s("x"),$p("m",true)),FreeQ($s("m"),$s("x")))))),FalseQ(FunctionOfLinear(Times($s("v"),ArcCot($s("u"))),$s("x")))))),
SetDelayed(Int(Times(ArcCot(Times($p("b",true),$p("x"))),Power(Plus($p("c"),Times($p("d",true),Power($p("x"),$p("n",true)))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Dist(Times(CI,C1D2),Int(Times(Log(Plus(C1,Times(CN1,Times(CI,Power(Times($s("b"),$s("x")),CN1))))),Power(Plus($s("c"),Times($s("d"),Power($s("x"),$s("n")))),CN1)),$s("x"))),Times(CN1,Dist(Times(CI,C1D2),Int(Times(Log(Plus(C1,Times(CI,Power(Times($s("b"),$s("x")),CN1)))),Power(Plus($s("c"),Times($s("d"),Power($s("x"),$s("n")))),CN1)),$s("x"))))),And(And(FreeQ(List($s("b"),$s("c"),$s("d")),$s("x")),IntegerQ($s("n"))),Not(And(Equal($s("n"),C2),ZeroQ(Plus(Times(Power($s("b"),C2),$s("c")),Times(CN1,$s("d"))))))))),
SetDelayed(Int(Times(ArcCot(Plus($p("a"),Times($p("b",true),$p("x")))),Power(Plus($p("c"),Times($p("d",true),Power($p("x"),$p("n",true)))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Dist(Times(CI,C1D2),Int(Times(Log(Plus(C1,Times(CN1,Times(CI,Power(Plus($s("a"),Times($s("b"),$s("x"))),CN1))))),Power(Plus($s("c"),Times($s("d"),Power($s("x"),$s("n")))),CN1)),$s("x"))),Times(CN1,Dist(Times(CI,C1D2),Int(Times(Log(Plus(C1,Times(CI,Power(Plus($s("a"),Times($s("b"),$s("x"))),CN1)))),Power(Plus($s("c"),Times($s("d"),Power($s("x"),$s("n")))),CN1)),$s("x"))))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),IntegerQ($s("n"))),Not(And(Equal($s("n"),C1),ZeroQ(Plus(Times($s("a"),$s("d")),Times(CN1,Times($s("b"),$s("c")))))))))),
SetDelayed(Int(ArcSec(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Plus($s("a"),Times($s("b"),$s("x"))),Times(ArcSec(Plus($s("a"),Times($s("b"),$s("x")))),Power($s("b"),CN1))),Times(CN1,Int(Times(C1,Power(Times(Plus($s("a"),Times($s("b"),$s("x"))),Sqrt(Plus(C1,Times(CN1,Times(C1,Power(Power(Plus($s("a"),Times($s("b"),$s("x"))),C2),CN1)))))),CN1)),$s("x")))),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(Times(ArcSec(Times($p("a",true),Power($p("x"),$p("n",true)))),Power($p("x"),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Plus(Times(CI,Times(Power(ArcSec(Times($s("a"),Power($s("x"),$s("n")))),C2),Power(Times(C2,$s("n")),CN1))),Times(CN1,Times(ArcSec(Times($s("a"),Power($s("x"),$s("n")))),Times(Log(Plus(C1,Times(CN1,Times(C1,Power(Power(Plus(Times(CI,Power(Times(Power($s("x"),$s("n")),$s("a")),CN1)),Sqrt(Plus(C1,Times(CN1,Times(C1,Power(Times(Power($s("x"),Times(C2,$s("n"))),Power($s("a"),C2)),CN1)))))),C2),CN1))))),Power($s("n"),CN1))))),Times(CI,Times(PolyLog(C2,Times(C1,Power(Power(Plus(Times(CI,Power(Times(Power($s("x"),$s("n")),$s("a")),CN1)),Sqrt(Plus(C1,Times(CN1,Times(C1,Power(Times(Power($s("x"),Times(C2,$s("n"))),Power($s("a"),C2)),CN1)))))),C2),CN1))),Power(Times(C2,$s("n")),CN1)))),FreeQ(List($s("a"),$s("n")),$s("x")))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),ArcSec(Plus($p("a"),Times($p("b",true),$p("x"))))),$p("x",$s("Symbol"))),
    Condition(Dist(Times(C1,Power($s("b"),CN1)),Subst(Int(Times(Power(Plus(Times(Times(CN1,$s("a")),Power($s("b"),CN1)),Times($s("x"),Power($s("b"),CN1))),$s("m")),ArcSec($s("x"))),$s("x")),$s("x"),Plus($s("a"),Times($s("b"),$s("x"))))),And(And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("m"))),Greater($s("m"),C0)))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),ArcSec(Times($p("a",true),$p("x")))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(ArcSec(Times($s("a"),$s("x"))),Power(Plus($s("m"),C1),CN1))),Times(CN1,Dist(Times(C1,Power(Times($s("a"),Plus($s("m"),C1)),CN1)),Int(Times(Power($s("x"),Plus($s("m"),Times(CN1,C1))),Power(Sqrt(Plus(C1,Times(CN1,Times(C1,Power(Power(Times($s("a"),$s("x")),C2),CN1))))),CN1)),$s("x"))))),And(FreeQ(List($s("a"),$s("m")),$s("x")),NonzeroQ(Plus($s("m"),C1))))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),ArcSec(Plus($p("a",true),Times($p("b",true),$p("x"))))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(ArcSec(Plus($s("a"),Times($s("b"),$s("x")))),Power(Plus($s("m"),C1),CN1))),Times(CN1,Dist(Times($s("b"),Power(Plus($s("m"),C1),CN1)),Int(Times(Power($s("x"),Plus($s("m"),C1)),Power(Times(Sqrt(Plus(C1,Times(CN1,Times(C1,Power(Power(Plus($s("a"),Times($s("b"),$s("x"))),C2),CN1))))),Power(Plus($s("a"),Times($s("b"),$s("x"))),C2)),CN1)),$s("x"))))),And(FreeQ(List($s("a"),$s("b"),$s("m")),$s("x")),NonzeroQ(Plus($s("m"),C1))))),
SetDelayed(Int(Times($p("u",true),Power(ArcSec(Times($p("c",true),Power(Plus($p("a",true),Times($p("b",true),Power($p("x"),$p("n",true)))),CN1))),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Int(Times($s("u"),Power(ArcCos(Plus(Times($s("a"),Power($s("c"),CN1)),Times($s("b"),Times(Power($s("x"),$s("n")),Power($s("c"),CN1))))),$s("m"))),$s("x")),FreeQ(List($s("a"),$s("b"),$s("c"),$s("n"),$s("m")),$s("x")))),
SetDelayed(Int(ArcSec($p("u")),$p("x",$s("Symbol"))),
    Condition(Plus(Times($s("x"),ArcSec($s("u"))),Times(CN1,Int(Regularize(Times($s("x"),Times(D($s("u"),$s("x")),Power(Times(Power($s("u"),C2),Sqrt(Plus(C1,Times(CN1,Times(C1,Power(Power($s("u"),C2),CN1)))))),CN1))),$s("x")),$s("x")))),And(InverseFunctionFreeQ($s("u"),$s("x")),Not(MatchQ($s("u"),Condition(Plus($p("c",true),Times($p("d",true),Power($p("f"),Plus($p("a",true),Times($p("b",true),$s("x")))))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("f")),$s("x")))))))),
SetDelayed(Int(ArcCsc(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Plus($s("a"),Times($s("b"),$s("x"))),Times(ArcCsc(Plus($s("a"),Times($s("b"),$s("x")))),Power($s("b"),CN1))),Int(Times(C1,Power(Times(Plus($s("a"),Times($s("b"),$s("x"))),Sqrt(Plus(C1,Times(CN1,Times(C1,Power(Power(Plus($s("a"),Times($s("b"),$s("x"))),C2),CN1)))))),CN1)),$s("x"))),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(Times(ArcCsc(Times($p("a",true),Power($p("x"),$p("n",true)))),Power($p("x"),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Plus(Times(CI,Times(Power(ArcCsc(Times($s("a"),Power($s("x"),$s("n")))),C2),Power(Times(C2,$s("n")),CN1))),Times(CN1,Times(ArcCsc(Times($s("a"),Power($s("x"),$s("n")))),Times(Log(Plus(C1,Times(CN1,Power(Plus(Times(CI,Power(Times(Power($s("x"),$s("n")),$s("a")),CN1)),Sqrt(Plus(C1,Times(CN1,Times(C1,Power(Times(Power($s("x"),Times(C2,$s("n"))),Power($s("a"),C2)),CN1)))))),C2)))),Power($s("n"),CN1))))),Times(CI,Times(PolyLog(C2,Power(Plus(Times(CI,Power(Times(Power($s("x"),$s("n")),$s("a")),CN1)),Sqrt(Plus(C1,Times(CN1,Times(C1,Power(Times(Power($s("x"),Times(C2,$s("n"))),Power($s("a"),C2)),CN1)))))),C2)),Power(Times(C2,$s("n")),CN1)))),FreeQ(List($s("a"),$s("n")),$s("x")))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),ArcCsc(Plus($p("a"),Times($p("b",true),$p("x"))))),$p("x",$s("Symbol"))),
    Condition(Dist(Times(C1,Power($s("b"),CN1)),Subst(Int(Times(Power(Plus(Times(Times(CN1,$s("a")),Power($s("b"),CN1)),Times($s("x"),Power($s("b"),CN1))),$s("m")),ArcCsc($s("x"))),$s("x")),$s("x"),Plus($s("a"),Times($s("b"),$s("x"))))),And(And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("m"))),Greater($s("m"),C0)))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),ArcCsc(Times($p("a",true),$p("x")))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(ArcCsc(Times($s("a"),$s("x"))),Power(Plus($s("m"),C1),CN1))),Dist(Times(C1,Power(Times($s("a"),Plus($s("m"),C1)),CN1)),Int(Times(Power($s("x"),Plus($s("m"),Times(CN1,C1))),Power(Sqrt(Plus(C1,Times(CN1,Times(C1,Power(Power(Times($s("a"),$s("x")),C2),CN1))))),CN1)),$s("x")))),And(FreeQ(List($s("a"),$s("m")),$s("x")),NonzeroQ(Plus($s("m"),C1))))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),ArcCsc(Plus($p("a",true),Times($p("b",true),$p("x"))))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(ArcCsc(Plus($s("a"),Times($s("b"),$s("x")))),Power(Plus($s("m"),C1),CN1))),Dist(Times($s("b"),Power(Plus($s("m"),C1),CN1)),Int(Times(Power($s("x"),Plus($s("m"),C1)),Power(Times(Sqrt(Plus(C1,Times(CN1,Times(C1,Power(Power(Plus($s("a"),Times($s("b"),$s("x"))),C2),CN1))))),Power(Plus($s("a"),Times($s("b"),$s("x"))),C2)),CN1)),$s("x")))),And(FreeQ(List($s("a"),$s("b"),$s("m")),$s("x")),NonzeroQ(Plus($s("m"),C1))))),
SetDelayed(Int(Times($p("u",true),Power(ArcCsc(Times($p("c",true),Power(Plus($p("a",true),Times($p("b",true),Power($p("x"),$p("n",true)))),CN1))),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Int(Times($s("u"),Power(ArcSin(Plus(Times($s("a"),Power($s("c"),CN1)),Times($s("b"),Times(Power($s("x"),$s("n")),Power($s("c"),CN1))))),$s("m"))),$s("x")),FreeQ(List($s("a"),$s("b"),$s("c"),$s("n"),$s("m")),$s("x")))),
SetDelayed(Int(ArcCsc($p("u")),$p("x",$s("Symbol"))),
    Condition(Plus(Times($s("x"),ArcCsc($s("u"))),Int(Regularize(Times($s("x"),Times(D($s("u"),$s("x")),Power(Times(Power($s("u"),C2),Sqrt(Plus(C1,Times(CN1,Times(C1,Power(Power($s("u"),C2),CN1)))))),CN1))),$s("x")),$s("x"))),And(InverseFunctionFreeQ($s("u"),$s("x")),Not(MatchQ($s("u"),Condition(Plus($p("c",true),Times($p("d",true),Power($p("f"),Plus($p("a",true),Times($p("b",true),$s("x")))))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("f")),$s("x")))))))),
SetDelayed(Int(ArcSinh(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Plus($s("a"),Times($s("b"),$s("x"))),Times(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))),Power($s("b"),CN1))),Times(CN1,Times(Sqrt(Plus(C1,Power(Plus($s("a"),Times($s("b"),$s("x"))),C2))),Power($s("b"),CN1)))),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(Times(C1,Power(ArcSinh(Plus($p("a",true),Times($p("b",true),$p("x")))),CN1)),$p("x",$s("Symbol"))),
    Condition(Times(CoshIntegral(ArcSinh(Plus($s("a"),Times($s("b"),$s("x"))))),Power($s("b"),CN1)),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(Times(C1,Power(Power(ArcSinh(Plus($p("a",true),Times($p("b",true),$p("x")))),C2),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Times(CN1,Sqrt(Plus(C1,Power(Plus($s("a"),Times($s("b"),$s("x"))),C2)))),Power(Times($s("b"),ArcSinh(Plus($s("a"),Times($s("b"),$s("x"))))),CN1)),Times(SinhIntegral(ArcSinh(Plus($s("a"),Times($s("b"),$s("x"))))),Power($s("b"),CN1))),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(Times(C1,Power(Sqrt(ArcSinh(Plus($p("a",true),Times($p("b",true),$p("x"))))),CN1)),$p("x",$s("Symbol"))),
    Condition(Times(Times(Sqrt(Pi),C1D2),Times(Plus(Erf(Sqrt(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))))),Erfi(Sqrt(ArcSinh(Plus($s("a"),Times($s("b"),$s("x"))))))),Power($s("b"),CN1))),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(Sqrt(ArcSinh(Plus($p("a",true),Times($p("b",true),$p("x"))))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Plus($s("a"),Times($s("b"),$s("x"))),Times(Sqrt(ArcSinh(Plus($s("a"),Times($s("b"),$s("x"))))),Power($s("b"),CN1))),Times(CN1,Times(Times(Sqrt(Pi),C1D4),Times(Plus(Times(CN1,Erf(Sqrt(ArcSinh(Plus($s("a"),Times($s("b"),$s("x"))))))),Erfi(Sqrt(ArcSinh(Plus($s("a"),Times($s("b"),$s("x"))))))),Power($s("b"),CN1))))),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(Power(ArcSinh(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("n")),$p("x",$s("Symbol"))),
    Condition(Plus(Plus(Times(Plus($s("a"),Times($s("b"),$s("x"))),Times(Power(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))),$s("n")),Power($s("b"),CN1))),Times(CN1,Times(Times($s("n"),Sqrt(Plus(C1,Power(Plus($s("a"),Times($s("b"),$s("x"))),C2)))),Times(Power(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),Times(CN1,C1))),Power($s("b"),CN1))))),Dist(Times($s("n"),Plus($s("n"),Times(CN1,C1))),Int(Power(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),Times(CN1,C2))),$s("x")))),And(And(FreeQ(List($s("a"),$s("b")),$s("x")),RationalQ($s("n"))),Greater($s("n"),C1)))),
SetDelayed(Int(Power(ArcSinh(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("n")),$p("x",$s("Symbol"))),
    Condition(Plus(Plus(Times(Times(CN1,Plus($s("a"),Times($s("b"),$s("x")))),Times(Power(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),C2)),Power(Times(Times($s("b"),Plus($s("n"),C1)),Plus($s("n"),C2)),CN1))),Times(Sqrt(Plus(C1,Power(Plus($s("a"),Times($s("b"),$s("x"))),C2))),Times(Power(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),C1)),Power(Times($s("b"),Plus($s("n"),C1)),CN1)))),Dist(Times(C1,Power(Times(Plus($s("n"),C1),Plus($s("n"),C2)),CN1)),Int(Power(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),C2)),$s("x")))),And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),RationalQ($s("n"))),Less($s("n"),CN1)),Unequal($s("n"),integer(-2L))))),
SetDelayed(Int(Power(ArcSinh(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("n")),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))),$s("n")),Times(Gamma(Plus($s("n"),C1),Times(CN1,ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))))),Power(Times(Times(C2,$s("b")),Power(Times(CN1,ArcSinh(Plus($s("a"),Times($s("b"),$s("x"))))),$s("n"))),CN1))),Times(CN1,Times(Gamma(Plus($s("n"),C1),ArcSinh(Plus($s("a"),Times($s("b"),$s("x"))))),Power(Times(C2,$s("b")),CN1)))),And(FreeQ(List($s("a"),$s("b"),$s("n")),$s("x")),Or(Not(RationalQ($s("n"))),Less(Less(CN1,$s("n")),C1))))),
SetDelayed(Int(Times(ArcSinh(Times($p("a",true),Power($p("x"),$p("n",true)))),Power($p("x"),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Plus(Times(Power(ArcSinh(Times($s("a"),Power($s("x"),$s("n")))),C2),Power(Times(C2,$s("n")),CN1)),Times(ArcSinh(Times($s("a"),Power($s("x"),$s("n")))),Times(Log(Plus(C1,Times(CN1,Power(E,Times(integer(-2L),ArcSinh(Times($s("a"),Power($s("x"),$s("n"))))))))),Power($s("n"),CN1)))),Times(CN1,Times(PolyLog(C2,Power(E,Times(integer(-2L),ArcSinh(Times($s("a"),Power($s("x"),$s("n"))))))),Power(Times(C2,$s("n")),CN1)))),FreeQ(List($s("a"),$s("n")),$s("x")))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),ArcSinh(Plus($p("a",true),Times($p("b",true),$p("x"))))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))),Power(Plus($s("m"),C1),CN1))),Times(CN1,Dist(Times($s("b"),Power(Plus($s("m"),C1),CN1)),Int(Times(Power($s("x"),Plus($s("m"),C1)),Power(Sqrt(Plus(Plus(Plus(C1,Power($s("a"),C2)),Times(Times(Times(C2,$s("a")),$s("b")),$s("x"))),Times(Power($s("b"),C2),Power($s("x"),C2)))),CN1)),$s("x"))))),And(FreeQ(List($s("a"),$s("b"),$s("m")),$s("x")),NonzeroQ(Plus($s("m"),C1))))),
SetDelayed(Int(Times(C1,Power(Sqrt(Plus($p("a"),Times($p("b",true),ArcSinh(Plus($p("c",true),Times($p("d",true),$p("x"))))))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Times(Sqrt(Pi),Power(E,Times($s("a"),Power($s("b"),CN1)))),Times(Erf(Times(Sqrt(Plus($s("a"),Times($s("b"),ArcSinh(Plus($s("c"),Times($s("d"),$s("x"))))))),Power(Rt($s("b"),C2),CN1))),Power(Times(Times(C2,Rt($s("b"),C2)),$s("d")),CN1))),Times(Times(Sqrt(Pi),Power(E,Times(Times(CN1,$s("a")),Power($s("b"),CN1)))),Times(Erfi(Times(Sqrt(Plus($s("a"),Times($s("b"),ArcSinh(Plus($s("c"),Times($s("d"),$s("x"))))))),Power(Rt($s("b"),C2),CN1))),Power(Times(Times(C2,Rt($s("b"),C2)),$s("d")),CN1)))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),PosQ($s("b"))))),
SetDelayed(Int(Times(C1,Power(Sqrt(Plus($p("a"),Times($p("b",true),ArcSinh(Plus($p("c",true),Times($p("d",true),$p("x"))))))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Times(Times(CN1,Sqrt(Pi)),Power(E,Times(Times(CN1,$s("a")),Power($s("b"),CN1)))),Times(Erf(Times(Sqrt(Plus($s("a"),Times($s("b"),ArcSinh(Plus($s("c"),Times($s("d"),$s("x"))))))),Power(Rt(Times(CN1,$s("b")),C2),CN1))),Power(Times(Times(C2,Rt(Times(CN1,$s("b")),C2)),$s("d")),CN1))),Times(CN1,Times(Times(Sqrt(Pi),Power(E,Times($s("a"),Power($s("b"),CN1)))),Times(Erfi(Times(Sqrt(Plus($s("a"),Times($s("b"),ArcSinh(Plus($s("c"),Times($s("d"),$s("x"))))))),Power(Rt(Times(CN1,$s("b")),C2),CN1))),Power(Times(Times(C2,Rt(Times(CN1,$s("b")),C2)),$s("d")),CN1))))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),NegQ($s("b"))))),
SetDelayed(Int(Times(Power(Plus(C1,Power($p("x"),C2)),$p("m",true)),Power(ArcSinh($p("x")),$p("n",true))),$p("x",$s("Symbol"))),
    Condition(Module(List(Set($s("u"),Block(List(Set($s("ShowSteps"),False),Set($s("StepCounter"),$s("Null"))),Int(Power(Plus(C1,Power($s("x"),C2)),$s("m")),$s("x"))))),Plus(Times($s("u"),Power(ArcSinh($s("x")),$s("n"))),Times(CN1,Dist($s("n"),Int(Expand(Times($s("u"),Times(Power(ArcSinh($s("x")),Plus($s("n"),Times(CN1,C1))),Power(Sqrt(Plus(C1,Power($s("x"),C2))),CN1)))),$s("x")))))),And(And(And(HalfIntegerQ($s("m")),Unequal($s("m"),CN1D2)),IntegerQ($s("n"))),Greater($s("n"),C0)))),
SetDelayed(Int(Times($p("x"),Times(Power(ArcSinh(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("n")),Power(Sqrt($p("u")),CN1))),$p("x",$s("Symbol"))),
    Condition(Plus(Plus(Times(Sqrt($s("u")),Times(Power(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))),$s("n")),Power(Power($s("b"),C2),CN1))),Times(CN1,Dist(Times($s("n"),Power($s("b"),CN1)),Int(Power(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),Times(CN1,C1))),$s("x"))))),Times(CN1,Dist(Times($s("a"),Power($s("b"),CN1)),Int(Times(Power(ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))),$s("n")),Power(Sqrt($s("u")),CN1)),$s("x"))))),And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),ZeroQ(Plus(Plus($s("u"),Times(CN1,C1)),Times(CN1,Power(Plus($s("a"),Times($s("b"),$s("x"))),C2))))),RationalQ($s("n"))),Greater($s("n"),C1)))),
SetDelayed(Int(Times($p("u",true),Power(ArcSinh(Times($p("c",true),Power(Plus($p("a",true),Times($p("b",true),Power($p("x"),$p("n",true)))),CN1))),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Int(Times($s("u"),Power(ArcCsch(Plus(Times($s("a"),Power($s("c"),CN1)),Times($s("b"),Times(Power($s("x"),$s("n")),Power($s("c"),CN1))))),$s("m"))),$s("x")),FreeQ(List($s("a"),$s("b"),$s("c"),$s("n"),$s("m")),$s("x")))),
SetDelayed(Int(ArcSinh($p("u")),$p("x",$s("Symbol"))),
    Condition(Plus(Times($s("x"),ArcSinh($s("u"))),Times(CN1,Int(Regularize(Times($s("x"),Times(D($s("u"),$s("x")),Power(Sqrt(Plus(C1,Power($s("u"),C2))),CN1))),$s("x")),$s("x")))),And(InverseFunctionFreeQ($s("u"),$s("x")),Not(MatchQ($s("u"),Condition(Plus($p("c",true),Times($p("d",true),Power($p("f"),Plus($p("a",true),Times($p("b",true),$s("x")))))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("f")),$s("x")))))))),
SetDelayed(Int(Power($p("f"),Times($p("c",true),ArcSinh(Plus($p("a",true),Times($p("b",true),$p("x")))))),$p("x",$s("Symbol"))),
    Condition(Times(Power($s("f"),Times($s("c"),ArcSinh(Plus($s("a"),Times($s("b"),$s("x")))))),Times(Plus(Plus($s("a"),Times($s("b"),$s("x"))),Times(CN1,Times(Times($s("c"),Sqrt(Plus(C1,Power(Plus($s("a"),Times($s("b"),$s("x"))),C2)))),Log($s("f"))))),Power(Times($s("b"),Plus(C1,Times(CN1,Times(Power($s("c"),C2),Power(Log($s("f")),C2))))),CN1))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("f")),$s("x")),NonzeroQ(Plus(C1,Times(CN1,Times(Power($s("c"),C2),Power(Log($s("f")),C2)))))))),
SetDelayed(Int(Power(E,Times($p("n",true),ArcSinh($p("v")))),$p("x",$s("Symbol"))),
    Condition(Int(Power(Plus($s("v"),Sqrt(Plus(C1,Power($s("v"),C2)))),$s("n")),$s("x")),And(IntegerQ($s("n")),PolynomialQ($s("v"),$s("x"))))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(E,Times($p("n",true),ArcSinh($p("v"))))),$p("x",$s("Symbol"))),
    Condition(Int(Times(Power($s("x"),$s("m")),Power(Plus($s("v"),Sqrt(Plus(C1,Power($s("v"),C2)))),$s("n"))),$s("x")),And(And(RationalQ($s("m")),IntegerQ($s("n"))),PolynomialQ($s("v"),$s("x"))))),
SetDelayed(Int(ArcCosh(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Plus($s("a"),Times($s("b"),$s("x"))),Times(ArcCosh(Plus($s("a"),Times($s("b"),$s("x")))),Power($s("b"),CN1))),Times(CN1,Times(Sqrt(Plus(Plus(CN1,$s("a")),Times($s("b"),$s("x")))),Times(Sqrt(Plus(Plus(C1,$s("a")),Times($s("b"),$s("x")))),Power($s("b"),CN1))))),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(Times(C1,Power(ArcCosh(Plus($p("a",true),Times($p("b",true),$p("x")))),CN1)),$p("x",$s("Symbol"))),
    Condition(Times(SinhIntegral(ArcCosh(Plus($s("a"),Times($s("b"),$s("x"))))),Power($s("b"),CN1)),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(Times(C1,Power(Power(ArcCosh(Plus($p("a",true),Times($p("b",true),$p("x")))),C2),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Times(CN1,Sqrt(Plus(Plus(CN1,$s("a")),Times($s("b"),$s("x"))))),Times(Sqrt(Plus(Plus(C1,$s("a")),Times($s("b"),$s("x")))),Power(Times($s("b"),ArcCosh(Plus($s("a"),Times($s("b"),$s("x"))))),CN1))),Times(CoshIntegral(ArcCosh(Plus($s("a"),Times($s("b"),$s("x"))))),Power($s("b"),CN1))),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(Times(C1,Power(Sqrt(ArcCosh(Plus($p("a",true),Times($p("b",true),$p("x"))))),CN1)),$p("x",$s("Symbol"))),
    Condition(Times(Times(Sqrt(Pi),C1D2),Times(Plus(Times(CN1,Erf(Sqrt(ArcCosh(Plus($s("a"),Times($s("b"),$s("x"))))))),Erfi(Sqrt(ArcCosh(Plus($s("a"),Times($s("b"),$s("x"))))))),Power($s("b"),CN1))),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(Sqrt(ArcCosh(Plus($p("a",true),Times($p("b",true),$p("x"))))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Plus($s("a"),Times($s("b"),$s("x"))),Times(Sqrt(ArcCosh(Plus($s("a"),Times($s("b"),$s("x"))))),Power($s("b"),CN1))),Times(CN1,Times(Times(Sqrt(Pi),C1D4),Times(Plus(Erf(Sqrt(ArcCosh(Plus($s("a"),Times($s("b"),$s("x")))))),Erfi(Sqrt(ArcCosh(Plus($s("a"),Times($s("b"),$s("x"))))))),Power($s("b"),CN1))))),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(Power(ArcCosh(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("n")),$p("x",$s("Symbol"))),
    Condition(Plus(Plus(Times(Plus($s("a"),Times($s("b"),$s("x"))),Times(Power(ArcCosh(Plus($s("a"),Times($s("b"),$s("x")))),$s("n")),Power($s("b"),CN1))),Times(CN1,Times(Times(Times($s("n"),Sqrt(Plus(Plus(CN1,$s("a")),Times($s("b"),$s("x"))))),Sqrt(Plus(Plus(C1,$s("a")),Times($s("b"),$s("x"))))),Times(Power(ArcCosh(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),Times(CN1,C1))),Power($s("b"),CN1))))),Dist(Times($s("n"),Plus($s("n"),Times(CN1,C1))),Int(Power(ArcCosh(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),Times(CN1,C2))),$s("x")))),And(And(FreeQ(List($s("a"),$s("b")),$s("x")),RationalQ($s("n"))),Greater($s("n"),C1)))),
SetDelayed(Int(Power(ArcCosh(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("n")),$p("x",$s("Symbol"))),
    Condition(Plus(Plus(Times(Times(CN1,Plus($s("a"),Times($s("b"),$s("x")))),Times(Power(ArcCosh(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),C2)),Power(Times(Times($s("b"),Plus($s("n"),C1)),Plus($s("n"),C2)),CN1))),Times(Times(Sqrt(Plus(Plus(CN1,$s("a")),Times($s("b"),$s("x")))),Sqrt(Plus(Plus(C1,$s("a")),Times($s("b"),$s("x"))))),Times(Power(ArcCosh(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),C1)),Power(Times($s("b"),Plus($s("n"),C1)),CN1)))),Dist(Times(C1,Power(Times(Plus($s("n"),C1),Plus($s("n"),C2)),CN1)),Int(Power(ArcCosh(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),C2)),$s("x")))),And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),RationalQ($s("n"))),Less($s("n"),CN1)),Unequal($s("n"),integer(-2L))))),
SetDelayed(Int(Power(ArcCosh(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("n")),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(ArcCosh(Plus($s("a"),Times($s("b"),$s("x")))),$s("n")),Times(Gamma(Plus($s("n"),C1),Times(CN1,ArcCosh(Plus($s("a"),Times($s("b"),$s("x")))))),Power(Times(Times(C2,$s("b")),Power(Times(CN1,ArcCosh(Plus($s("a"),Times($s("b"),$s("x"))))),$s("n"))),CN1))),Times(Gamma(Plus($s("n"),C1),ArcCosh(Plus($s("a"),Times($s("b"),$s("x"))))),Power(Times(C2,$s("b")),CN1))),And(FreeQ(List($s("a"),$s("b"),$s("n")),$s("x")),Or(Not(RationalQ($s("n"))),Less(Less(CN1,$s("n")),C1))))),
SetDelayed(Int(Times(ArcCosh(Times($p("a",true),Power($p("x"),$p("n",true)))),Power($p("x"),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Plus(Times(Power(ArcCosh(Times($s("a"),Power($s("x"),$s("n")))),C2),Power(Times(C2,$s("n")),CN1)),Times(ArcCosh(Times($s("a"),Power($s("x"),$s("n")))),Times(Log(Plus(C1,Power(E,Times(integer(-2L),ArcCosh(Times($s("a"),Power($s("x"),$s("n")))))))),Power($s("n"),CN1)))),Times(CN1,Times(PolyLog(C2,Times(CN1,Power(E,Times(integer(-2L),ArcCosh(Times($s("a"),Power($s("x"),$s("n")))))))),Power(Times(C2,$s("n")),CN1)))),FreeQ(List($s("a"),$s("n")),$s("x")))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),ArcCosh(Plus($p("a",true),Times($p("b",true),$p("x"))))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(ArcCosh(Plus($s("a"),Times($s("b"),$s("x")))),Power(Plus($s("m"),C1),CN1))),Times(CN1,Dist(Times($s("b"),Power(Plus($s("m"),C1),CN1)),Int(Times(Power($s("x"),Plus($s("m"),C1)),Power(Times(Sqrt(Plus(Plus(CN1,$s("a")),Times($s("b"),$s("x")))),Sqrt(Plus(Plus(C1,$s("a")),Times($s("b"),$s("x"))))),CN1)),$s("x"))))),And(FreeQ(List($s("a"),$s("b"),$s("m")),$s("x")),NonzeroQ(Plus($s("m"),C1))))),
SetDelayed(Int(Times(C1,Power(Sqrt(Plus($p("a"),Times($p("b",true),ArcCosh(Plus($p("c",true),Times($p("d",true),$p("x"))))))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Times(Sqrt(Pi),Power(E,Times(Times(CN1,$s("a")),Power($s("b"),CN1)))),Times(Erfi(Times(Sqrt(Plus($s("a"),Times($s("b"),ArcCosh(Plus($s("c"),Times($s("d"),$s("x"))))))),Power(Rt($s("b"),C2),CN1))),Power(Times(Times(C2,Rt($s("b"),C2)),$s("d")),CN1))),Times(CN1,Times(Times(Sqrt(Pi),Power(E,Times($s("a"),Power($s("b"),CN1)))),Times(Erf(Times(Sqrt(Plus($s("a"),Times($s("b"),ArcCosh(Plus($s("c"),Times($s("d"),$s("x"))))))),Power(Rt($s("b"),C2),CN1))),Power(Times(Times(C2,Rt($s("b"),C2)),$s("d")),CN1))))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),PosQ($s("b"))))),
SetDelayed(Int(Times(C1,Power(Sqrt(Plus($p("a"),Times($p("b",true),ArcCosh(Plus($p("c",true),Times($p("d",true),$p("x"))))))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Times(Times(CN1,Sqrt(Pi)),Power(E,Times(Times(CN1,$s("a")),Power($s("b"),CN1)))),Times(Erf(Times(Sqrt(Plus($s("a"),Times($s("b"),ArcCosh(Plus($s("c"),Times($s("d"),$s("x"))))))),Power(Rt(Times(CN1,$s("b")),C2),CN1))),Power(Times(Times(C2,Rt(Times(CN1,$s("b")),C2)),$s("d")),CN1))),Times(Times(Sqrt(Pi),Power(E,Times($s("a"),Power($s("b"),CN1)))),Times(Erfi(Times(Sqrt(Plus($s("a"),Times($s("b"),ArcCosh(Plus($s("c"),Times($s("d"),$s("x"))))))),Power(Rt(Times(CN1,$s("b")),C2),CN1))),Power(Times(Times(C2,Rt(Times(CN1,$s("b")),C2)),$s("d")),CN1)))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),NegQ($s("b"))))),
SetDelayed(Int(Times(Times(Power(Plus(C1,$p("x")),$p("m",true)),Power(Plus(CN1,$p("x")),$p("m",true))),Power(ArcCosh($p("x")),$p("n",true))),$p("x",$s("Symbol"))),
    Condition(Module(List(Set($s("u"),Block(List(Set($s("ShowSteps"),False),Set($s("StepCounter"),$s("Null"))),Int(Times(Power(Plus(C1,$s("x")),$s("m")),Power(Plus(CN1,$s("x")),$s("m"))),$s("x"))))),Plus(Times($s("u"),Power(ArcCosh($s("x")),$s("n"))),Times(CN1,Dist($s("n"),Int(Expand(Times($s("u"),Times(Power(ArcCosh($s("x")),Plus($s("n"),Times(CN1,C1))),Power(Times(Sqrt(Plus(C1,$s("x"))),Sqrt(Plus(CN1,$s("x")))),CN1)))),$s("x")))))),And(And(And(HalfIntegerQ($s("m")),Unequal($s("m"),CN1D2)),IntegerQ($s("n"))),Greater($s("n"),C0)))),
SetDelayed(Int(Times($p("u",true),Power(ArcCosh(Times($p("c",true),Power(Plus($p("a",true),Times($p("b",true),Power($p("x"),$p("n",true)))),CN1))),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Int(Times($s("u"),Power(ArcSech(Plus(Times($s("a"),Power($s("c"),CN1)),Times($s("b"),Times(Power($s("x"),$s("n")),Power($s("c"),CN1))))),$s("m"))),$s("x")),FreeQ(List($s("a"),$s("b"),$s("c"),$s("n"),$s("m")),$s("x")))),
SetDelayed(Int(ArcCosh($p("u")),$p("x",$s("Symbol"))),
    Condition(Plus(Times($s("x"),ArcCosh($s("u"))),Times(CN1,Int(Regularize(Times($s("x"),Times(D($s("u"),$s("x")),Power(Times(Sqrt(Plus(CN1,$s("u"))),Sqrt(Plus(C1,$s("u")))),CN1))),$s("x")),$s("x")))),And(InverseFunctionFreeQ($s("u"),$s("x")),Not(MatchQ($s("u"),Condition(Plus($p("c",true),Times($p("d",true),Power($p("f"),Plus($p("a",true),Times($p("b",true),$s("x")))))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("f")),$s("x")))))))),
SetDelayed(Int(Power($p("f"),Times($p("c",true),ArcCosh(Plus($p("a",true),Times($p("b",true),$p("x")))))),$p("x",$s("Symbol"))),
    Condition(Times(Power($s("f"),Times($s("c"),ArcCosh(Plus($s("a"),Times($s("b"),$s("x")))))),Times(Plus(Plus($s("a"),Times($s("b"),$s("x"))),Times(CN1,Times(Times(Times($s("c"),Sqrt(Times(Plus(Plus(CN1,$s("a")),Times($s("b"),$s("x"))),Power(Plus(Plus(C1,$s("a")),Times($s("b"),$s("x"))),CN1)))),Plus(Plus(C1,$s("a")),Times($s("b"),$s("x")))),Log($s("f"))))),Power(Times($s("b"),Plus(C1,Times(CN1,Times(Power($s("c"),C2),Power(Log($s("f")),C2))))),CN1))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("f")),$s("x")),NonzeroQ(Plus(C1,Times(CN1,Times(Power($s("c"),C2),Power(Log($s("f")),C2)))))))),
SetDelayed(Int(Power(E,Times($p("n",true),ArcCosh($p("v")))),$p("x",$s("Symbol"))),
    Condition(Int(Power(Plus($s("v"),Times(Sqrt(Plus(CN1,$s("v"))),Sqrt(Plus(C1,$s("v"))))),$s("n")),$s("x")),And(IntegerQ($s("n")),PolynomialQ($s("v"),$s("x"))))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(E,Times($p("n",true),ArcCosh($p("v"))))),$p("x",$s("Symbol"))),
    Condition(Int(Times(Power($s("x"),$s("m")),Power(Plus($s("v"),Times(Sqrt(Plus(CN1,$s("v"))),Sqrt(Plus(C1,$s("v"))))),$s("n"))),$s("x")),And(And(RationalQ($s("m")),IntegerQ($s("n"))),PolynomialQ($s("v"),$s("x"))))),
SetDelayed(Int(ArcTanh(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Plus($s("a"),Times($s("b"),$s("x"))),Times(ArcTanh(Plus($s("a"),Times($s("b"),$s("x")))),Power($s("b"),CN1))),Times(Log(Plus(C1,Times(CN1,Power(Plus($s("a"),Times($s("b"),$s("x"))),C2)))),Power(Times(C2,$s("b")),CN1))),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(ArcTanh(Plus($p("a",true),Times($p("b",true),Power($p("x"),$p("n"))))),$p("x",$s("Symbol"))),
    Condition(Plus(Times($s("x"),ArcTanh(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))))),Times(CN1,Dist(Times($s("b"),$s("n")),Int(Times(Power($s("x"),$s("n")),Power(Plus(Plus(Plus(C1,Times(CN1,Power($s("a"),C2))),Times(CN1,Times(Times(Times(C2,$s("a")),$s("b")),Power($s("x"),$s("n"))))),Times(CN1,Times(Power($s("b"),C2),Power($s("x"),Times(C2,$s("n")))))),CN1)),$s("x"))))),And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))))),
SetDelayed(Int(Times(ArcTanh(Plus($p("a",true),Times($p("b",true),Power($p("x"),$p("n",true))))),Power($p("x"),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Dist(C1D2,Int(Times(Log(Plus(Plus(C1,$s("a")),Times($s("b"),Power($s("x"),$s("n"))))),Power($s("x"),CN1)),$s("x"))),Times(CN1,Dist(C1D2,Int(Times(Log(Plus(Plus(C1,Times(CN1,$s("a"))),Times(CN1,Times($s("b"),Power($s("x"),$s("n")))))),Power($s("x"),CN1)),$s("x"))))),FreeQ(List($s("a"),$s("b"),$s("n")),$s("x")))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),ArcTanh(Plus($p("a",true),Times($p("b",true),Power($p("x"),$p("n",true)))))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(ArcTanh(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n"))))),Power(Plus($s("m"),C1),CN1))),Times(CN1,Dist(Times($s("b"),Times($s("n"),Power(Plus($s("m"),C1),CN1))),Int(Times(Power($s("x"),Plus($s("m"),$s("n"))),Power(Plus(Plus(Plus(C1,Times(CN1,Power($s("a"),C2))),Times(CN1,Times(Times(Times(C2,$s("a")),$s("b")),Power($s("x"),$s("n"))))),Times(CN1,Times(Power($s("b"),C2),Power($s("x"),Times(C2,$s("n")))))),CN1)),$s("x"))))),And(And(And(FreeQ(List($s("a"),$s("b"),$s("m")),$s("x")),IntegerQ($s("n"))),NonzeroQ(Plus($s("m"),C1))),NonzeroQ(Plus(Plus($s("m"),Times(CN1,$s("n"))),C1))))),
SetDelayed(Int(Times(Power(Plus(C1,Times(CN1,Power($p("x"),C2))),$p("m")),Power(ArcTanh($p("x")),$p("n",true))),$p("x",$s("Symbol"))),
    Condition(Module(List(Set($s("u"),Block(List(Set($s("ShowSteps"),False),Set($s("StepCounter"),$s("Null"))),Int(Power(Plus(C1,Times(CN1,Power($s("x"),C2))),$s("m")),$s("x"))))),Plus(Times($s("u"),Power(ArcTanh($s("x")),$s("n"))),Times(CN1,Dist($s("n"),Int(Expand(Times($s("u"),Times(Power(ArcTanh($s("x")),Plus($s("n"),Times(CN1,C1))),Power(Plus(C1,Times(CN1,Power($s("x"),C2))),CN1)))),$s("x")))))),And(And(IntegerQ(List($s("m"),$s("n"))),Less($s("m"),CN1)),Greater($s("n"),C0)))),
SetDelayed(Int(Times(Power(Plus(CN1,Power($p("x"),C2)),$p("m")),Power(ArcTanh($p("x")),$p("n",true))),$p("x",$s("Symbol"))),
    Condition(Dist(Power(CN1,$s("m")),Int(Times(Power(Plus(C1,Times(CN1,Power($s("x"),C2))),$s("m")),Power(ArcTanh($s("x")),$s("n"))),$s("x"))),And(And(IntegerQ(List($s("m"),$s("n"))),Less($s("m"),CN1)),Greater($s("n"),C0)))),
SetDelayed(Int(Times(C1,Power(Times(Times(Plus(C1,Times(CN1,Power($p("x"),C2))),ArcCoth($p("x"))),ArcTanh($p("x"))),CN1)),$p("x",$s("Symbol"))),
    Times(Plus(Times(CN1,Log(ArcCoth($s("x")))),Log(ArcTanh($s("x")))),Power(Plus(ArcCoth($s("x")),Times(CN1,ArcTanh($s("x")))),CN1))),
SetDelayed(Int(Times(Power(ArcCoth($p("x")),$p("n",true)),Times(Power(ArcTanh($p("x")),$p("p",true)),Power(Plus(C1,Times(CN1,Power($p("x"),C2))),CN1))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(ArcCoth($s("x")),Plus($s("n"),C1)),Times(Power(ArcTanh($s("x")),$s("p")),Power(Plus($s("n"),C1),CN1))),Times(CN1,Dist(Times($s("p"),Power(Plus($s("n"),C1),CN1)),Int(Times(Power(ArcCoth($s("x")),Plus($s("n"),C1)),Times(Power(ArcTanh($s("x")),Plus($s("p"),Times(CN1,C1))),Power(Plus(C1,Times(CN1,Power($s("x"),C2))),CN1))),$s("x"))))),And(IntegerQ(List($s("n"),$s("p"))),And(Less(C0,$s("p")),LessEqual($s("p"),$s("n")))))),
SetDelayed(Int(Times($p("x"),Power(ArcTanh(Times($p("a",true),$p("x"))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Plus(CN1,Times(Power($s("a"),C2),Power($s("x"),C2))),Times(Power(ArcTanh(Times($s("a"),$s("x"))),$s("n")),Power(Times(C2,Power($s("a"),C2)),CN1))),Dist(Times($s("n"),Power(Times(C2,$s("a")),CN1)),Int(Power(ArcTanh(Times($s("a"),$s("x"))),Plus($s("n"),Times(CN1,C1))),$s("x")))),And(And(FreeQ($s("a"),$s("x")),RationalQ($s("n"))),Greater($s("n"),C1)))),
SetDelayed(Int(Times($p("x"),Power(ArcTanh(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(Plus(Plus(Times(Times(CN1,Plus(C1,Times(CN1,Power(Plus($s("a"),Times($s("b"),$s("x"))),C2)))),Times(Power(ArcTanh(Plus($s("a"),Times($s("b"),$s("x")))),$s("n")),Power(Times(C2,Power($s("b"),C2)),CN1))),Dist(Times($s("n"),Power(Times(C2,$s("b")),CN1)),Int(Power(ArcTanh(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),Times(CN1,C1))),$s("x")))),Times(CN1,Dist(Times($s("a"),Power($s("b"),CN1)),Int(Power(ArcTanh(Plus($s("a"),Times($s("b"),$s("x")))),$s("n")),$s("x"))))),And(And(FreeQ(List($s("a"),$s("b")),$s("x")),RationalQ($s("n"))),Greater($s("n"),C1)))),
SetDelayed(Int(Times($p("u",true),Power(ArcTanh(Times($p("c",true),Power(Plus($p("a",true),Times($p("b",true),Power($p("x"),$p("n",true)))),CN1))),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Int(Times($s("u"),Power(ArcCoth(Plus(Times($s("a"),Power($s("c"),CN1)),Times($s("b"),Times(Power($s("x"),$s("n")),Power($s("c"),CN1))))),$s("m"))),$s("x")),FreeQ(List($s("a"),$s("b"),$s("c"),$s("n"),$s("m")),$s("x")))),
SetDelayed(Int(Times($p("u"),Power($p("v"),$p("n",true))),$p("x",$s("Symbol"))),
    Condition(Module(List(Set($s("tmp"),InverseFunctionOfLinear($s("u"),$s("x")))),Condition(Dist(Times(Power(Times(Times(CN1,Discriminant($s("v"),$s("x"))),Power(Times(C4,Coefficient($s("v"),$s("x"),C2)),CN1)),$s("n")),Power(Coefficient(Part($s("tmp"),C1),$s("x"),C1),CN1)),Subst(Int(Regularize(Times(SubstForInverseFunction($s("u"),$s("tmp"),$s("x")),Power(Sech($s("x")),Times(C2,Plus($s("n"),C1)))),$s("x")),$s("x")),$s("x"),$s("tmp"))),And(And(NotFalseQ($s("tmp")),SameQ(Head($s("tmp")),$s("ArcTanh"))),ZeroQ(Plus(Times(Discriminant($s("v"),$s("x")),Power(Part($s("tmp"),C1),C2)),Times(CN1,Power(D($s("v"),$s("x")),C2))))))),And(And(And(QuadraticQ($s("v"),$s("x")),IntegerQ($s("n"))),Less($s("n"),C0)),PosQ(Discriminant($s("v"),$s("x")))))),
SetDelayed(Int(Times($p("u",true),Power(E,Times($p("n",true),ArcTanh($p("v"))))),$p("x",$s("Symbol"))),
    Condition(Int(Times($s("u"),Times(Power(Plus(C1,$s("v")),Times($s("n"),C1D2)),Power(Power(Plus(C1,Times(CN1,$s("v"))),Times($s("n"),C1D2)),CN1))),$s("x")),EvenQ($s("n")))),
SetDelayed(Int(Power(E,Times($p("n",true),ArcTanh($p("v")))),$p("x",$s("Symbol"))),
    Condition(Int(Times(Power(Plus(C1,$s("v")),Times($s("n"),C1D2)),Power(Power(Plus(C1,Times(CN1,$s("v"))),Times($s("n"),C1D2)),CN1)),$s("x")),RationalQ($s("n")))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(E,Times($p("n",true),ArcTanh($p("v"))))),$p("x",$s("Symbol"))),
    Condition(Int(Times(Power($s("x"),$s("m")),Times(Power(Plus(C1,$s("v")),$s("n")),Power(Power(Plus(C1,Times(CN1,Power($s("v"),C2))),Times($s("n"),C1D2)),CN1))),$s("x")),And(And(RationalQ($s("m")),OddQ($s("n"))),PolynomialQ($s("v"),$s("x"))))),
SetDelayed(Int(Times(Times($p("u",true),Power(E,Times($p("n",true),ArcTanh($p("v"))))),Power(Plus(C1,Times(CN1,Power($p("v"),C2))),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Int(Times(Times($s("u"),Power(Plus(C1,Times(CN1,$s("v"))),Plus($s("m"),Times(CN1,Times($s("n"),C1D2))))),Power(Plus(C1,$s("v")),Plus($s("m"),Times($s("n"),C1D2)))),$s("x")),And(And(RationalQ(List($s("m"),$s("n"))),IntegerQ(Plus($s("m"),Times(CN1,Times($s("n"),C1D2))))),IntegerQ(Plus($s("m"),Times($s("n"),C1D2)))))),
SetDelayed(Int(Times(Times($p("u",true),Power(E,Times($p("n",true),ArcTanh($p("v"))))),Power(Plus($p("a"),Times($p("b",true),Power($p("v"),C2))),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Times(Times(Power(Plus($s("a"),Times($s("b"),Power($s("v"),C2))),$s("m")),Power(Power(Plus(C1,Times(CN1,Power($s("v"),C2))),$s("m")),CN1)),Int(Times(Times($s("u"),Power(Plus(C1,Times(CN1,$s("v"))),Plus($s("m"),Times(CN1,Times($s("n"),C1D2))))),Power(Plus(C1,$s("v")),Plus($s("m"),Times($s("n"),C1D2)))),$s("x"))),And(And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),ZeroQ(Plus($s("a"),$s("b")))),RationalQ(List($s("m"),$s("n")))),IntegerQ(Plus($s("m"),Times(CN1,Times($s("n"),C1D2))))),IntegerQ(Plus($s("m"),Times($s("n"),C1D2)))))),
SetDelayed(Int(Times(Times($p("u",true),Power(E,Times($p("n",true),ArcTanh($p("v"))))),Power(Plus(C1,Times(CN1,Power($p("v"),C2))),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Int(Times(Times($s("u"),Power(Plus(C1,$s("v")),$s("n"))),Power(Plus(C1,Times(CN1,Power($s("v"),C2))),Plus($s("m"),Times(CN1,Times($s("n"),C1D2))))),$s("x")),And(And(RationalQ($s("n")),IntegerQ($s("m"))),Greater($s("m"),C0)))),
SetDelayed(Int(Times(Times($p("u",true),Power(E,Times($p("n",true),ArcTanh($p("v"))))),Power(Plus(C1,$p("v")),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Int(Times($s("u"),Times(Power(Plus(C1,$s("v")),Plus($s("m"),$s("n"))),Power(Power(Plus(C1,Times(CN1,Power($s("v"),C2))),Times($s("n"),C1D2)),CN1))),$s("x")),And(RationalQ(List($s("m"),$s("n"))),IntegerQ(Plus($s("m"),$s("n")))))),
SetDelayed(Int(Times(Times($p("u",true),Power(E,Times($p("n",true),ArcTanh($p("v"))))),Power(Plus(C1,$p("v")),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Int(Times($s("u"),Times(Power(Plus(C1,$s("v")),Plus($s("m"),Times($s("n"),C1D2))),Power(Power(Plus(C1,Times(CN1,$s("v"))),Times($s("n"),C1D2)),CN1))),$s("x")),RationalQ(List($s("m"),$s("n"))))),
SetDelayed(Int(Times(Times($p("u",true),Power(E,Times($p("n",true),ArcTanh($p("v"))))),Power(Plus(C1,Times(CN1,$p("v"))),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Int(Times(Times($s("u"),Power(Plus(C1,$s("v")),Times($s("n"),C1D2))),Power(Plus(C1,Times(CN1,$s("v"))),Plus($s("m"),Times(CN1,Times($s("n"),C1D2))))),$s("x")),RationalQ(List($s("m"),$s("n"))))),
SetDelayed(Int(Times(Times($p("u",true),Power(E,Times($p("n",true),ArcTanh($p("v"))))),Power(Plus($p("a"),Times($p("b",true),$p("v"))),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Dist(Power($s("a"),$s("m")),Int(Times(Times($s("u"),Power(E,Times($s("n"),ArcTanh($s("v"))))),Power(Plus(C1,Times(Times($s("b"),Power($s("a"),CN1)),$s("v"))),$s("m"))),$s("x"))),And(And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("m"))),RationalQ($s("n"))),NonzeroQ(Plus($s("a"),Times(CN1,C1)))),ZeroQ(Plus(Power($s("a"),C2),Times(CN1,Power($s("b"),C2))))))),
SetDelayed(Int(Times(Times($p("u",true),Power(E,ArcTanh($p("v")))),Power(Plus($p("a"),Times($p("b",true),Power(Power($p("v"),C2),CN1))),$p("m",true))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power($s("b"),$s("m")),Int(Times($s("u"),Times(Power(Plus(C1,Times(CN1,Power($s("v"),C2))),Plus($s("m"),Times(CN1,C1D2))),Power(Power($s("v"),Times(C2,$s("m"))),CN1))),$s("x"))),Times(Power($s("b"),$s("m")),Int(Times($s("u"),Times(Power(Plus(C1,Times(CN1,Power($s("v"),C2))),Plus($s("m"),Times(CN1,C1D2))),Power(Power($s("v"),Plus(Times(C2,$s("m")),Times(CN1,C1))),CN1))),$s("x")))),And(And(FreeQ(List($s("a"),$s("b")),$s("x")),ZeroQ(Plus($s("a"),$s("b")))),IntegerQ($s("m"))))),
SetDelayed(Int(Times($p("u"),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),C2))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(Dist(Power($s("a"),$s("n")),Subst(Int(Regularize(Times(Power(Cosh($s("x")),Times(integer(-2L),Plus($s("n"),C1))),SubstFor(ArcTanh($s("x")),$s("u"),$s("x"))),$s("x")),$s("x")),$s("x"),ArcTanh($s("x")))),And(And(And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),FunctionOfQ(ArcTanh($s("x")),$s("u"),$s("x"))),ZeroQ(Plus($s("a"),$s("b")))),HalfIntegerQ($s("n"))),Less($s("n"),CN1)),PositiveQ($s("a"))))),
SetDelayed(Int(Times($p("u"),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),C2))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(Dist(Times(C1,Power($s("a"),CN1)),Subst(Int(Regularize(Times(Power(Times($s("a"),Power(Sech($s("x")),C2)),Plus($s("n"),C1)),SubstFor(ArcTanh($s("x")),$s("u"),$s("x"))),$s("x")),$s("x")),$s("x"),ArcTanh($s("x")))),And(And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),FunctionOfQ(ArcTanh($s("x")),$s("u"),$s("x"))),ZeroQ(Plus($s("a"),$s("b")))),HalfIntegerQ($s("n"))),Less($s("n"),CN1)))),
SetDelayed(Int(Times(Times(Power($p("x"),$p("m",true)),$p("u")),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),C2))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(Dist(Power($s("a"),$s("n")),Subst(Int(Regularize(Times(Times(Power(Tanh($s("x")),$s("m")),Power(Cosh($s("x")),Times(integer(-2L),Plus($s("n"),C1)))),SubstFor(ArcTanh($s("x")),$s("u"),$s("x"))),$s("x")),$s("x")),$s("x"),ArcTanh($s("x")))),And(And(And(And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),FunctionOfQ(ArcTanh($s("x")),$s("u"),$s("x"))),ZeroQ(Plus($s("a"),$s("b")))),HalfIntegerQ($s("n"))),Less($s("n"),CN1)),PositiveQ($s("a"))),IntegerQ($s("m"))))),
SetDelayed(Int(ArcTanh($p("u")),$p("x",$s("Symbol"))),
    Condition(Plus(Times($s("x"),ArcTanh($s("u"))),Times(CN1,Int(Regularize(Times($s("x"),Times(D($s("u"),$s("x")),Power(Plus(C1,Times(CN1,Power($s("u"),C2))),CN1))),$s("x")),$s("x")))),InverseFunctionFreeQ($s("u"),$s("x")))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),ArcTanh($p("u"))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(ArcTanh($s("u")),Power(Plus($s("m"),C1),CN1))),Times(CN1,Dist(Times(C1,Power(Plus($s("m"),C1),CN1)),Int(Regularize(Times(Power($s("x"),Plus($s("m"),C1)),Times(D($s("u"),$s("x")),Power(Plus(C1,Times(CN1,Power($s("u"),C2))),CN1))),$s("x")),$s("x"))))),And(And(And(And(FreeQ($s("m"),$s("x")),NonzeroQ(Plus($s("m"),C1))),InverseFunctionFreeQ($s("u"),$s("x"))),Not(FunctionOfQ(Power($s("x"),Plus($s("m"),C1)),$s("u"),$s("x")))),FalseQ(PowerVariableExpn($s("u"),Plus($s("m"),C1),$s("x")))))),
SetDelayed(Int(Times($p("v"),ArcTanh($p("u"))),$p("x",$s("Symbol"))),
    Condition(Module(List(Set($s("w"),Block(List(Set($s("ShowSteps"),False),Set($s("StepCounter"),$s("Null"))),Int($s("v"),$s("x"))))),Condition(Plus(Times($s("w"),ArcTanh($s("u"))),Times(CN1,Int(Regularize(Times($s("w"),Times(D($s("u"),$s("x")),Power(Plus(C1,Times(CN1,Power($s("u"),C2))),CN1))),$s("x")),$s("x")))),InverseFunctionFreeQ($s("w"),$s("x")))),And(And(InverseFunctionFreeQ($s("u"),$s("x")),Not(MatchQ($s("v"),Condition(Power($s("x"),$p("m",true)),FreeQ($s("m"),$s("x")))))),FalseQ(FunctionOfLinear(Times($s("v"),ArcTanh($s("u"))),$s("x")))))),
SetDelayed(Int(Times(ArcTanh(Times($p("b",true),$p("x"))),Power(Plus($p("c"),Times($p("d",true),Power($p("x"),$p("n",true)))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Dist(C1D2,Int(Times(Log(Plus(C1,Times($s("b"),$s("x")))),Power(Plus($s("c"),Times($s("d"),Power($s("x"),$s("n")))),CN1)),$s("x"))),Times(CN1,Dist(C1D2,Int(Times(Log(Plus(C1,Times(CN1,Times($s("b"),$s("x"))))),Power(Plus($s("c"),Times($s("d"),Power($s("x"),$s("n")))),CN1)),$s("x"))))),And(And(FreeQ(List($s("b"),$s("c"),$s("d")),$s("x")),IntegerQ($s("n"))),Not(And(Equal($s("n"),C2),ZeroQ(Plus(Times(Power($s("b"),C2),$s("c")),$s("d")))))))),
SetDelayed(Int(Times(ArcTanh(Plus($p("a"),Times($p("b",true),$p("x")))),Power(Plus($p("c"),Times($p("d",true),Power($p("x"),$p("n",true)))),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Dist(C1D2,Int(Times(Log(Plus(Plus(C1,$s("a")),Times($s("b"),$s("x")))),Power(Plus($s("c"),Times($s("d"),Power($s("x"),$s("n")))),CN1)),$s("x"))),Times(CN1,Dist(C1D2,Int(Times(Log(Plus(Plus(C1,Times(CN1,$s("a"))),Times(CN1,Times($s("b"),$s("x"))))),Power(Plus($s("c"),Times($s("d"),Power($s("x"),$s("n")))),CN1)),$s("x"))))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),IntegerQ($s("n"))),Not(And(Equal($s("n"),C1),ZeroQ(Plus(Times($s("a"),$s("d")),Times(CN1,Times($s("b"),$s("c")))))))))),
SetDelayed(Int(ArcCoth(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Plus($s("a"),Times($s("b"),$s("x"))),Times(ArcCoth(Plus($s("a"),Times($s("b"),$s("x")))),Power($s("b"),CN1))),Times(Log(Plus(C1,Times(CN1,Power(Plus($s("a"),Times($s("b"),$s("x"))),C2)))),Power(Times(C2,$s("b")),CN1))),FreeQ(List($s("a"),$s("b")),$s("x")))),
SetDelayed(Int(ArcCoth(Plus($p("a",true),Times($p("b",true),Power($p("x"),$p("n"))))),$p("x",$s("Symbol"))),
    Condition(Plus(Times($s("x"),ArcCoth(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))))),Times(CN1,Dist(Times($s("b"),$s("n")),Int(Times(Power($s("x"),$s("n")),Power(Plus(Plus(Plus(C1,Times(CN1,Power($s("a"),C2))),Times(CN1,Times(Times(Times(C2,$s("a")),$s("b")),Power($s("x"),$s("n"))))),Times(CN1,Times(Power($s("b"),C2),Power($s("x"),Times(C2,$s("n")))))),CN1)),$s("x"))))),And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("n"))))),
SetDelayed(Int(Times(ArcCoth(Plus($p("a",true),Times($p("b",true),Power($p("x"),$p("n",true))))),Power($p("x"),CN1)),$p("x",$s("Symbol"))),
    Condition(Plus(Dist(C1D2,Int(Times(Log(Plus(C1,Times(C1,Power(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))),CN1)))),Power($s("x"),CN1)),$s("x"))),Times(CN1,Dist(C1D2,Int(Times(Log(Plus(C1,Times(CN1,Times(C1,Power(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))),CN1))))),Power($s("x"),CN1)),$s("x"))))),FreeQ(List($s("a"),$s("b"),$s("n")),$s("x")))),
SetDelayed(Int(Times(Power($p("x"),$p("m",true)),ArcCoth(Plus($p("a",true),Times($p("b",true),Power($p("x"),$p("n",true)))))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(ArcCoth(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n"))))),Power(Plus($s("m"),C1),CN1))),Times(CN1,Dist(Times($s("b"),Times($s("n"),Power(Plus($s("m"),C1),CN1))),Int(Times(Power($s("x"),Plus($s("m"),$s("n"))),Power(Plus(Plus(Plus(C1,Times(CN1,Power($s("a"),C2))),Times(CN1,Times(Times(Times(C2,$s("a")),$s("b")),Power($s("x"),$s("n"))))),Times(CN1,Times(Power($s("b"),C2),Power($s("x"),Times(C2,$s("n")))))),CN1)),$s("x"))))),And(And(And(FreeQ(List($s("a"),$s("b"),$s("m")),$s("x")),IntegerQ($s("n"))),NonzeroQ(Plus($s("m"),C1))),NonzeroQ(Plus(Plus($s("m"),Times(CN1,$s("n"))),C1))))),
SetDelayed(Int(Times(Power(Plus(C1,Times(CN1,Power($p("x"),C2))),$p("m")),Power(ArcCoth($p("x")),$p("n",true))),$p("x",$s("Symbol"))),
    Condition(Module(List(Set($s("u"),Block(List(Set($s("ShowSteps"),False),Set($s("StepCounter"),$s("Null"))),Int(Power(Plus(C1,Times(CN1,Power($s("x"),C2))),$s("m")),$s("x"))))),Plus(Times($s("u"),Power(ArcCoth($s("x")),$s("n"))),Times(CN1,Dist($s("n"),Int(Expand(Times($s("u"),Times(Power(ArcCoth($s("x")),Plus($s("n"),Times(CN1,C1))),Power(Plus(C1,Times(CN1,Power($s("x"),C2))),CN1)))),$s("x")))))),And(And(IntegerQ(List($s("m"),$s("n"))),Less($s("m"),CN1)),Greater($s("n"),C0)))),
SetDelayed(Int(Times(Power(Plus(CN1,Power($p("x"),C2)),$p("m")),Power(ArcCoth($p("x")),$p("n",true))),$p("x",$s("Symbol"))),
    Condition(Dist(Power(CN1,$s("m")),Int(Times(Power(Plus(C1,Times(CN1,Power($s("x"),C2))),$s("m")),Power(ArcCoth($s("x")),$s("n"))),$s("x"))),And(And(IntegerQ(List($s("m"),$s("n"))),Less($s("m"),CN1)),Greater($s("n"),C0)))),
SetDelayed(Int(Times(Power(ArcCoth($p("x")),$p("n",true)),Times(Power(ArcTanh($p("x")),$p("p")),Power(Plus(C1,Times(CN1,Power($p("x"),C2))),CN1))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Power(ArcCoth($s("x")),$s("n")),Times(Power(ArcTanh($s("x")),Plus($s("p"),C1)),Power(Plus($s("p"),C1),CN1))),Times(CN1,Dist(Times($s("n"),Power(Plus($s("p"),C1),CN1)),Int(Times(Power(ArcCoth($s("x")),Plus($s("n"),Times(CN1,C1))),Times(Power(ArcTanh($s("x")),Plus($s("p"),C1)),Power(Plus(C1,Times(CN1,Power($s("x"),C2))),CN1))),$s("x"))))),And(IntegerQ(List($s("n"),$s("p"))),Less(Less(C0,$s("n")),$s("p"))))),
SetDelayed(Int(Times(Times(Power(Plus(C1,Times(CN1,Power($p("x"),C2))),$p("m",true)),Power(ArcCoth($p("x")),$p("n",true))),Power(ArcTanh($p("x")),$p("p",true))),$p("x",$s("Symbol"))),
    Condition(Module(List(Set($s("u"),Block(List(Set($s("ShowSteps"),False),Set($s("StepCounter"),$s("Null"))),Int(Power(Plus(C1,Times(CN1,Power($s("x"),C2))),$s("m")),$s("x"))))),Plus(Plus(Times(Times($s("u"),Power(ArcCoth($s("x")),$s("n"))),Power(ArcTanh($s("x")),$s("p"))),Times(CN1,Dist($s("p"),Int(Times(Times($s("u"),Power(ArcCoth($s("x")),$s("n"))),Times(Power(ArcTanh($s("x")),Plus($s("p"),Times(CN1,C1))),Power(Plus(C1,Times(CN1,Power($s("x"),C2))),CN1))),$s("x"))))),Times(CN1,Dist($s("n"),Int(Times(Times($s("u"),Power(ArcCoth($s("x")),Plus($s("n"),Times(CN1,C1)))),Times(Power(ArcTanh($s("x")),$s("p")),Power(Plus(C1,Times(CN1,Power($s("x"),C2))),CN1))),$s("x")))))),And(And(And(IntegerQ(List($s("m"),$s("p"),$s("n"))),Less($s("m"),CN1)),Greater($s("p"),C1)),Greater($s("n"),C1)))),
SetDelayed(Int(Times(Times(Power(Plus(CN1,Power($p("x"),C2)),$p("m",true)),Power(ArcCoth($p("x")),$p("n",true))),Power(ArcTanh($p("x")),$p("p",true))),$p("x",$s("Symbol"))),
    Condition(Dist(Power(CN1,$s("m")),Int(Times(Times(Power(Plus(C1,Times(CN1,Power($s("x"),C2))),$s("m")),Power(ArcCoth($s("x")),$s("n"))),Power(ArcTanh($s("x")),$s("p"))),$s("x"))),And(And(IntegerQ(List($s("m"),$s("n"),$s("p"))),Less($s("m"),CN1)),Greater($s("n"),C0)))),
SetDelayed(Int(Times($p("x"),Power(ArcCoth(Times($p("a",true),$p("x"))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(Plus(Times(Plus(CN1,Times(Power($s("a"),C2),Power($s("x"),C2))),Times(Power(ArcCoth(Times($s("a"),$s("x"))),$s("n")),Power(Times(C2,Power($s("a"),C2)),CN1))),Dist(Times($s("n"),Power(Times(C2,$s("a")),CN1)),Int(Power(ArcCoth(Times($s("a"),$s("x"))),Plus($s("n"),Times(CN1,C1))),$s("x")))),And(And(FreeQ($s("a"),$s("x")),RationalQ($s("n"))),Greater($s("n"),C1)))),
SetDelayed(Int(Times($p("x"),Power(ArcCoth(Plus($p("a",true),Times($p("b",true),$p("x")))),$p("n"))),$p("x",$s("Symbol"))),
    Condition(Plus(Plus(Times(Plus(CN1,Power(Plus($s("a"),Times($s("b"),$s("x"))),C2)),Times(Power(ArcCoth(Plus($s("a"),Times($s("b"),$s("x")))),$s("n")),Power(Times(C2,Power($s("b"),C2)),CN1))),Dist(Times($s("n"),Power(Times(C2,$s("b")),CN1)),Int(Power(ArcCoth(Plus($s("a"),Times($s("b"),$s("x")))),Plus($s("n"),Times(CN1,C1))),$s("x")))),Times(CN1,Dist(Times($s("a"),Power($s("b"),CN1)),Int(Power(ArcCoth(Plus($s("a"),Times($s("b"),$s("x")))),$s("n")),$s("x"))))),And(And(FreeQ(List($s("a"),$s("b")),$s("x")),RationalQ($s("n"))),Greater($s("n"),C1))))
  );
}
