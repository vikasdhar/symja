package org.matheclipse.core.integrate.rubi;


import static org.matheclipse.core.expression.F.*;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.*;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.*;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
/** 
 * UtilityFunctions rules from the <a href="http://www.apmaths.uwo.ca/~arich/">Rubi -
 * rule-based integrator</a>.
 *  
 */
public class UtilityFunctions0 { 
  public static IAST RULES = List( 
SetDelayed(IndentedPrint($p("n",$s("Integer")),$p("u")),
    CompoundExpression(Print(StringJoin(MakeList($s("n"),stringx(" "))),$s("u")),$s("u"))),
SetDelayed(MakeList($p("n",$s("Integer")),$p("u")),
    If(Equal($s("n"),C0),List(),Append(MakeList(Plus($s("n"),Times(CN1,C1)),$s("u")),$s("u")))),
SetDelayed(Second($p("u")),
    Part($s("u"),C2)),
SetDelayed(ClearDownValues($p("func",$s("Symbol"))),
    CompoundExpression(CompoundExpression(Unprotect($s("func")),Set(DownValues($s("func")),List())),Protect($s("func")))),
SetDelayed(SetDownValues($p("func",$s("Symbol")),$p("lst",$s("List"))),
    CompoundExpression(CompoundExpression(CompoundExpression(Unprotect($s("func")),Set(DownValues($s("func")),Take($s("lst"),Min(integer(529L),Length($s("lst")))))),Scan(Function(ReplacePart(ReplacePart(Slot1,Part(Part(Slot1,C1),C1),C1),$s("SetDelayed"),C0)),Drop($s("lst"),Min(integer(529L),Length($s("lst")))))),Protect($s("func")))),
SetDelayed(MoveDownValues($p("func1",$s("Symbol")),$p("func2",$s("Symbol"))),
    Module(List($s("lst")),CompoundExpression(SetDownValues($s("func2"),ReplaceAll(DownValues($s("func1")),List(Rule($s("func1"),$s("func2"))))),ClearDownValues($s("func1"))))),
SetDelayed(Map2($p("func"),$p("lst1"),$p("lst2")),
    ReapList(Do(Sow($($s("func"),Part($s("lst1"),$s("i")),Part($s("lst2"),$s("i")))),List($s("i"),Length($s("lst1")))))),
SetDelayed(ReapList($p("u")),
    Module(List(Set($s("lst"),Part(Reap($s("u")),C2))),If(SameQ($s("lst"),List()),$s("lst"),Part($s("lst"),C1)))),
SetDelayed(MapAnd($p("f"),$p("lst")),
    Catch(CompoundExpression(Scan(Function(If($($s("f"),Slot1),$s("Null"),Throw(False))),$s("lst")),True))),
SetDelayed(MapAnd($p("f"),$p("lst"),$p("x")),
    Catch(CompoundExpression(Scan(Function(If($($s("f"),Slot1,$s("x")),$s("Null"),Throw(False))),$s("lst")),True))),
SetDelayed(MapOr($p("f"),$p("lst")),
    Catch(CompoundExpression(Scan(Function(If($($s("f"),Slot1),Throw(True),$s("Null"))),$s("lst")),False))),
SetDelayed(NotIntegrableQ($p("u"),$p("x",$s("Symbol"))),
    Or(MatchQ($s("u"),Condition(Times(Power($s("x"),$p("m")),Power(Log(Plus($p("a"),Times($p("b",true),$s("x")))),$p("n"))),And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ(List($s("m"),$s("n")))),Less($s("m"),C0)),Less($s("n"),C0)))),MatchQ($s("u"),Condition($($p("f"),Times(Power($s("x"),$p("m",true)),Log(Plus($p("a",true),Times($p("b",true),$s("x")))))),And(And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("m"))),Or(TrigQ($s("f")),HyperbolicQ($s("f")))))))),
SetDelayed(ZeroQ($p("u")),
    PossibleZeroQ($s("u"))),
SetDelayed(NonzeroQ($p("u")),
    Not(PossibleZeroQ($s("u")))),
SetDelayed(RealNumericQ($p("u")),
    And(NumericQ($s("u")),PossibleZeroQ(Im(N($s("u")))))),
SetDelayed(ImaginaryNumericQ($p("u")),
    And(And(NumericQ($s("u")),PossibleZeroQ(Re(N($s("u"))))),Not(PossibleZeroQ(Im(N($s("u"))))))),
SetDelayed(PositiveQ($p("u")),
    Module(List(Set($s("v"),Simplify($s("u")))),And(RealNumericQ($s("v")),Greater(Re(N($s("v"))),C0)))),
SetDelayed(PositiveOrZeroQ($p("u")),
    Module(List(Set($s("v"),Simplify($s("u")))),And(RealNumericQ($s("v")),GreaterEqual(Re(N($s("v"))),C0)))),
SetDelayed(NegativeQ($p("u")),
    Module(List(Set($s("v"),Simplify($s("u")))),And(RealNumericQ($s("v")),Less(Re(N($s("v"))),C0)))),
SetDelayed(NegativeOrZeroQ($p("u")),
    Module(List(Set($s("v"),Simplify($s("u")))),And(RealNumericQ($s("v")),LessEqual(Re(N($s("v"))),C0)))),
SetDelayed(FractionQ($p("u")),
    If(ListQ($s("u")),MapAnd($s("Integrate::FractionQ"),$s("u")),SameQ(Head($s("u")),$s("Rational")))),
SetDelayed(RationalQ(Plus($p("u"),Times($p("m"),Plus($p("n"),$p("v"))))),
    And(And(RationalQ($s("m")),RationalQ($s("n"))),RationalQ(Plus($s("u"),Times($s("m"),$s("v")))))),
SetDelayed(RationalQ($p("u")),
    If(ListQ($s("u")),MapAnd($s("Integrate::RationalQ"),$s("u")),Or(IntegerQ($s("u")),SameQ(Head($s("u")),$s("Rational"))))),
SetDelayed(HalfIntegerQ($p("u")),
    If(ListQ($s("u")),MapAnd($s("Integrate::HalfIntegerQ"),$s("u")),And(FractionQ($s("u")),Equal(Denominator($s("u")),C2)))),
SetDelayed(FractionOrNegativeQ($p("u")),
    If(ListQ($s("u")),MapAnd($s("Integrate::FractionOrNegativeQ"),$s("u")),Or(FractionQ($s("u")),And(IntegerQ($s("u")),Less($s("u"),C0))))),
SetDelayed(SqrtNumberQ(Power($p("m"),$p("n"))),
    Or(And(IntegerQ($s("n")),SqrtNumberQ($s("m"))),And(HalfIntegerQ($s("n")),RationalQ($s("m"))))),
SetDelayed(SqrtNumberQ(Times($p("u"),$p("v"))),
    And(SqrtNumberQ($s("u")),SqrtNumberQ($s("v")))),
SetDelayed(SqrtNumberQ($p("u")),
    Or(RationalQ($s("u")),SameQ($s("u"),CI))),
SetDelayed(SqrtNumberSumQ($p("u")),
    And(And(SumQ($s("u")),SqrtNumberQ(First($s("u")))),SqrtNumberQ(Rest($s("u"))))),
SetDelayed(FalseQ($p("u")),
    SameQ($s("u"),False)),
SetDelayed(NotFalseQ($p("u")),
    UnsameQ($s("u"),False)),
SetDelayed(SumQ($p("u")),
    SameQ(Head($s("u")),$s("Plus"))),
SetDelayed(NonsumQ($p("u")),
    UnsameQ(Head($s("u")),$s("Plus"))),
SetDelayed(ProductQ($p("u")),
    SameQ(Head($s("u")),$s("Times"))),
SetDelayed(PowerQ($p("u")),
    SameQ(Head($s("u")),$s("Power"))),
SetDelayed(IntegerPowerQ($p("u")),
    And(PowerQ($s("u")),IntegerQ(Part($s("u"),C2)))),
SetDelayed(PositiveIntegerPowerQ($p("u")),
    And(And(PowerQ($s("u")),IntegerQ(Part($s("u"),C2))),Greater(Part($s("u"),C2),C0))),
SetDelayed(FractionalPowerQ($p("u")),
    And(PowerQ($s("u")),FractionQ(Part($s("u"),C2)))),
SetDelayed(RationalPowerQ($p("u")),
    And(PowerQ($s("u")),RationalQ(Part($s("u"),C2)))),
SetDelayed(SqrtQ($p("u")),
    And(PowerQ($s("u")),SameQ(Part($s("u"),C2),C1D2))),
SetDelayed(ExpQ($p("u")),
    And(PowerQ($s("u")),SameQ(Part($s("u"),C1),E))),
SetDelayed(ImaginaryQ($p("u")),
    And(SameQ(Head($s("u")),$s("Complex")),SameQ(Re($s("u")),C0))),
SetDelayed(FractionalPowerFreeQ($p("u")),
    If(AtomQ($s("u")),True,If(And(FractionalPowerQ($s("u")),Not(AtomQ(Part($s("u"),C1)))),False,Catch(CompoundExpression(Scan(Function(If(FractionalPowerFreeQ(Slot1),$s("Null"),Throw(False))),$s("u")),True))))),
SetDelayed(ComplexFreeQ($p("u")),
    If(AtomQ($s("u")),UnsameQ(Head($s("u")),$s("Complex")),Catch(CompoundExpression(Scan(Function(If(ComplexFreeQ(Slot1),$s("Null"),Throw(False))),$s("u")),True)))),
SetDelayed(LogQ($p("u")),
    SameQ(Head($s("u")),$s("Log"))),
SetDelayed(ProductLogQ($p("u")),
    SameQ(Head($s("u")),$s("ProductLog"))),
SetDelayed(SinQ($p("u")),
    SameQ(Head($s("u")),$s("Sin"))),
SetDelayed(CosQ($p("u")),
    SameQ(Head($s("u")),$s("Cos"))),
SetDelayed(TanQ($p("u")),
    SameQ(Head($s("u")),$s("Tan"))),
SetDelayed(CotQ($p("u")),
    SameQ(Head($s("u")),$s("Cot"))),
SetDelayed(SecQ($p("u")),
    SameQ(Head($s("u")),$s("Sec"))),
SetDelayed(CscQ($p("u")),
    SameQ(Head($s("u")),$s("Csc"))),
SetDelayed(SinhQ($p("u")),
    SameQ(Head($s("u")),$s("Sinh"))),
SetDelayed(CoshQ($p("u")),
    SameQ(Head($s("u")),$s("Cosh"))),
SetDelayed(TanhQ($p("u")),
    SameQ(Head($s("u")),$s("Tanh"))),
SetDelayed(CothQ($p("u")),
    SameQ(Head($s("u")),$s("Coth"))),
SetDelayed(SechQ($p("u")),
    SameQ(Head($s("u")),$s("Sech"))),
SetDelayed(CschQ($p("u")),
    SameQ(Head($s("u")),$s("Csch"))),
SetDelayed(TrigQ($p("u")),
    MemberQ(List($s("Sin"),$s("Cos"),$s("Tan"),$s("Cot"),$s("Sec"),$s("Csc")),If(AtomQ($s("u")),$s("u"),Head($s("u"))))),
SetDelayed(InverseTrigQ($p("u")),
    MemberQ(List($s("ArcSin"),$s("ArcCos"),$s("ArcTan"),$s("ArcCot"),$s("ArcSec"),$s("ArcCsc")),If(AtomQ($s("u")),$s("u"),Head($s("u"))))),
SetDelayed(HyperbolicQ($p("u")),
    MemberQ(List($s("Sinh"),$s("Cosh"),$s("Tanh"),$s("Coth"),$s("Sech"),$s("Csch")),If(AtomQ($s("u")),$s("u"),Head($s("u"))))),
SetDelayed(InverseHyperbolicQ($p("u")),
    MemberQ(List($s("ArcSinh"),$s("ArcCosh"),$s("ArcTanh"),$s("ArcCoth"),$s("ArcSech"),$s("ArcCsch")),If(AtomQ($s("u")),$s("u"),Head($s("u"))))),
SetDelayed(SinCosQ($p("f")),
    MemberQ(List($s("Sin"),$s("Cos"),$s("Sec"),$s("Csc")),$s("f"))),
SetDelayed(SinhCoshQ($p("f")),
    MemberQ(List($s("Sinh"),$s("Cosh"),$s("Sech"),$s("Csch")),$s("f"))),
SetDelayed(CalculusQ($p("u")),
    MemberQ(List($s("D"),$s("Integrate"),$s("Sum"),$s("Product"),$s("Int"),$s("Dif"),$s("Integrate::Subst")),Head($s("u")))),
SetDelayed(CalculusFreeQ($p("u"),$p("x")),
    If(AtomQ($s("u")),True,If(Or(Or(And(CalculusQ($s("u")),SameQ(Part($s("u"),C2),$s("x"))),SameQ(Head($s("u")),$s("Pattern"))),SameQ(Head($s("u")),$s("Defer"))),False,Catch(CompoundExpression(Scan(Function(If(CalculusFreeQ(Slot1,$s("x")),$s("Null"),Throw(False))),$s("u")),True))))),
SetDelayed(SubstQ($p("u")),
    SameQ(Head($s("u")),$s("Integrate::Subst"))),
SetDelayed(InverseFunctionQ($p("u")),
    Or(Or(Or(LogQ($s("u")),InverseTrigQ($s("u"))),InverseHyperbolicQ($s("u"))),SameQ(Head($s("u")),$s("Integrate::Mods")))),
SetDelayed(TrigHyperbolicFreeQ($p("u"),$p("x",$s("Symbol"))),
    If(AtomQ($s("u")),True,If(Or(Or(TrigQ($s("u")),HyperbolicQ($s("u"))),CalculusQ($s("u"))),FreeQ($s("u"),$s("x")),Catch(CompoundExpression(Scan(Function(If(TrigHyperbolicFreeQ(Slot1,$s("x")),$s("Null"),Throw(False))),$s("u")),True))))),
SetDelayed(InverseFunctionFreeQ($p("u"),$p("x",$s("Symbol"))),
    If(AtomQ($s("u")),True,If(Or(InverseFunctionQ($s("u")),CalculusQ($s("u"))),FreeQ($s("u"),$s("x")),Catch(CompoundExpression(Scan(Function(If(InverseFunctionFreeQ(Slot1,$s("x")),$s("Null"),Throw(False))),$s("u")),True))))),
SetDelayed(NegativeCoefficientQ($p("u")),
    If(SumQ($s("u")),NegativeCoefficientQ(First($s("u"))),MatchQ($s("u"),Condition(Times($p("m"),$p("v",true)),And(RationalQ($s("m")),Less($s("m"),C0)))))),
SetDelayed(RealQ($p("u")),
    Condition(MapAnd($s("Integrate::RealQ"),$s("u")),ListQ($s("u")))),
SetDelayed(RealQ($p("u")),
    Condition(PossibleZeroQ(Im(N($s("u")))),NumericQ($s("u")))),
SetDelayed(RealQ(Power($p("u"),$p("v"))),
    And(And(RealQ($s("u")),RealQ($s("v"))),Or(IntegerQ($s("v")),PositiveOrZeroQ($s("u"))))),
SetDelayed(RealQ(Times($p("u"),$p("v"))),
    And(RealQ($s("u")),RealQ($s("v")))),
SetDelayed(RealQ(Plus($p("u"),$p("v"))),
    And(RealQ($s("u")),RealQ($s("v")))),
SetDelayed(RealQ($($p("f"),$p("u"))),
    If(MemberQ(List($s("Sin"),$s("Cos"),$s("Tan"),$s("Cot"),$s("Sec"),$s("Csc"),$s("ArcTan"),$s("ArcCot"),$s("Erf")),$s("f")),RealQ($s("u")),If(MemberQ(List($s("ArcSin"),$s("ArcCos")),$s("f")),LE(CN1,$s("u"),C1),If(SameQ($s("f"),$s("Log")),PositiveOrZeroQ($s("u")),False)))),
SetDelayed(RealQ($p("u")),
    False),
SetDelayed(PosQ($p("u")),
    If(PossibleZeroQ($s("u")),False,If(NumericQ($s("u")),If(NumberQ($s("u")),If(PossibleZeroQ(Re($s("u"))),Greater(Im($s("u")),C0),Greater(Re($s("u")),C0)),Module(List(Set($s("v"),N($s("u")))),If(PossibleZeroQ(Re($s("v"))),Greater(Im($s("v")),C0),Greater(Re($s("v")),C0)))),Module(List(Set($s("v"),Simplify($s("u")))),If(NumericQ($s("v")),PosQ($s("v")),If(And(PowerQ($s("v")),IntegerQ(Part($s("v"),C2))),PosQ(Part($s("v"),C1)),If(ProductQ($s("v")),If(RationalQ(First($s("v"))),If(Greater(First($s("v")),C0),PosQ(Rest($s("v"))),NegQ(Rest($s("v")))),PosQ(First($s("v")))),If(SumQ($s("v")),PosQ(First($s("v"))),Not(MatchQ($s("v"),Times(CN1,$p((ISymbol)null)))))))))))),
SetDelayed(NegQ($p("u")),
    If(PossibleZeroQ($s("u")),False,Not(PosQ($s("u"))))),
SetDelayed(LeadTerm($p("u")),
    If(SumQ($s("u")),First($s("u")),$s("u"))),
SetDelayed(RemainingTerms($p("u")),
    If(SumQ($s("u")),Rest($s("u")),C0)),
SetDelayed(LeadFactor($p("u")),
    If(ProductQ($s("u")),LeadFactor(First($s("u"))),If(ImaginaryQ($s("u")),If(SameQ(Im($s("u")),C1),$s("u"),LeadFactor(Im($s("u")))),$s("u")))),
SetDelayed(RemainingFactors($p("u")),
    If(ProductQ($s("u")),Times(RemainingFactors(First($s("u"))),Rest($s("u"))),If(ImaginaryQ($s("u")),If(SameQ(Im($s("u")),C1),C1,Times(CI,RemainingFactors(Im($s("u"))))),C1))),
SetDelayed(LeadBase($p("u")),
    Module(List(Set($s("v"),LeadFactor($s("u")))),If(PowerQ($s("v")),Part($s("v"),C1),$s("v")))),
SetDelayed(LeadDegree($p("u")),
    Module(List(Set($s("v"),LeadFactor($s("u")))),If(PowerQ($s("v")),Part($s("v"),C2),C1))),
SetDelayed(LT($p("u"),$p("v")),
    And(And(RealNumericQ($s("u")),RealNumericQ($s("v"))),Less(Re(N($s("u"))),Re(N($s("v")))))),
SetDelayed(LT($p("u"),$p("v"),$p("w")),
    And(LT($s("u"),$s("v")),LT($s("v"),$s("w")))),
SetDelayed(LE($p("u"),$p("v")),
    And(And(RealNumericQ($s("u")),RealNumericQ($s("v"))),LessEqual(Re(N($s("u"))),Re(N($s("v")))))),
SetDelayed(LE($p("u"),$p("v"),$p("w")),
    And(LE($s("u"),$s("v")),LE($s("v"),$s("w")))),
SetDelayed(GT($p("u"),$p("v")),
    And(And(RealNumericQ($s("u")),RealNumericQ($s("v"))),Greater(Re(N($s("u"))),Re(N($s("v")))))),
SetDelayed(GT($p("u"),$p("v"),$p("w")),
    And(GT($s("u"),$s("v")),GT($s("v"),$s("w")))),
SetDelayed(GE($p("u"),$p("v")),
    And(And(RealNumericQ($s("u")),RealNumericQ($s("v"))),GreaterEqual(Re(N($s("u"))),Re(N($s("v")))))),
SetDelayed(GE($p("u"),$p("v"),$p("w")),
    And(GE($s("u"),$s("v")),GE($s("v"),$s("w")))),
SetDelayed(IndependentQ($p("u"),$p("x",$s("Symbol"))),
    FreeQ($s("u"),$s("x"))),
SetDelayed(SplitFreeFactors($p("u"),$p("x",$s("Symbol"))),
    If(ProductQ($s("u")),Map(Function(If(FreeQ(Slot1,$s("x")),List(Slot1,C1),List(C1,Slot1))),$s("u")),If(FreeQ($s("u"),$s("x")),List($s("u"),C1),List(C1,$s("u"))))),
SetDelayed(SplitFreeTerms($p("u"),$p("x",$s("Symbol"))),
    If(SumQ($s("u")),Map(Function(SplitFreeTerms(Slot1,$s("x"))),$s("u")),If(FreeQ($s("u"),$s("x")),List($s("u"),C0),List(C0,$s("u"))))),
SetDelayed(SplitFactorsOfTerms($p("u"),$p("x",$s("Symbol"))),
    Module(List(Set($s("lst"),SplitFreeTerms($s("u"),$s("x"))),$s("v"),$s("w")),CompoundExpression(CompoundExpression(CompoundExpression(Set($s("v"),Part($s("lst"),C1)),Set($s("w"),Part($s("lst"),C2))),If(ZeroQ($s("w")),Set($s("lst"),List()),If(SumQ($s("w")),CompoundExpression(CompoundExpression(Set($s("lst"),Map(Function(SplitFreeFactors(Slot1,$s("x"))),Apply($s("List"),$s("w")))),Set($s("lst"),Map(Function(Prepend(SplitFreeFactors(Regularize(Part(Slot1,C2),$s("x")),$s("x")),Part(Slot1,C1))),$s("lst")))),Set($s("lst"),Map(Function(List(Times(Part(Slot1,C1),Part(Slot1,C2)),Part(Slot1,C3))),$s("lst")))),CompoundExpression(CompoundExpression(Set($s("lst"),SplitFreeFactors($s("w"),$s("x"))),Set($s("lst"),Prepend(SplitFreeFactors(Regularize(Part($s("lst"),C2),$s("x")),$s("x")),Part($s("lst"),C1)))),Set($s("lst"),List(List(Times(Part($s("lst"),C1),Part($s("lst"),C2)),Part($s("lst"),C3)))))))),If(ZeroQ($s("v")),$s("lst"),Prepend($s("lst"),List(C1,$s("v"))))))),
SetDelayed(SplitMonomialTerms($p("u"),$p("x",$s("Symbol"))),
    Map(Function(If(Or(FreeQ(Slot1,$s("x")),MatchQ(Slot1,Condition(Times($p("a",true),Power($s("x"),$p("n",true))),FreeQ(List($s("a"),$s("n")),$s("x"))))),List(Slot1,C0),List(C0,Slot1))),$s("u")))
  );
}
