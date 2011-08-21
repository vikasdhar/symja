/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MaxCountExceededException;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.TooManyEvaluationsException;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.util.Incrementor;

/**
 * Provide a default implementation for several functions useful to generic
 * optimizers.
 *
 * @version $Revision: 1043078 $ $Date: 2010-12-07 16:01:37 +0100 (Di, 07 Dez 2010) $
 * @since 2.0
 */
public abstract class AbstractUnivariateRealOptimizer
    implements UnivariateRealOptimizer {
    /** Convergence checker. */
    private ConvergenceChecker<UnivariateRealPointValuePair> checker;
    /** Evaluations counter. */
    private final Incrementor evaluations = new Incrementor();
    /** Optimization type */
    private GoalType goal;
    /** Lower end of search interval. */
    private double searchMin;
    /** Higher end of search interval. */
    private double searchMax;
    /** Initial guess . */
    private double searchStart;
    /** Function to optimize. */
    private UnivariateRealFunction function;

    /** {@inheritDoc} */
    public int getMaxEvaluations() {
        return evaluations.getMaximalCount();
    }

    /** {@inheritDoc} */
    public int getEvaluations() {
        return evaluations.getCount();
    }

    /**
     * @return the optimization type.
     */
    public GoalType getGoalType() {
        return goal;
    }
    /**
     * @return the lower end of the search interval.
     */
    public double getMin() {
        return searchMin;
    }
    /**
     * @return the higher end of the search interval.
     */
    public double getMax() {
        return searchMax;
    }
    /**
     * @return the initial guess.
     */
    public double getStartValue() {
        return searchStart;
    }

    /**
     * Compute the objective function value.
     *
     * @param point Point at which the objective function must be evaluated.
     * @return the objective function value at specified point.
     * @throws TooManyEvaluationsException if the maximal number of evaluations
     * is exceeded.
     * @throws org.apache.commons.math.exception.MathUserException if the
     * objective function throws one.
     */
    protected double computeObjectiveValue(double point) {
        try {
            evaluations.incrementCount();
        } catch (MaxCountExceededException e) {
            throw new TooManyEvaluationsException(e.getMax());
        }
        return function.value(point);
    }

    /** {@inheritDoc} */
    public UnivariateRealPointValuePair optimize(int maxEval, UnivariateRealFunction f,
                                                 GoalType goalType,
                                                 double min, double max,
                                                 double startValue) {
        // Checks.
        if (f == null) {
            throw new NullArgumentException();
        }
        if (goalType == null) {
            throw new NullArgumentException();
        }

        // Reset.
        searchMin = min;
        searchMax = max;
        searchStart = startValue;
        goal = goalType;
        function = f;
        evaluations.setMaximalCount(maxEval);
        evaluations.resetCount();

        // Perform computation.
        return doOptimize();
    }

    /** {@inheritDoc} */
    public UnivariateRealPointValuePair optimize(int maxEval,
                                                 UnivariateRealFunction f,
                                                 GoalType goalType,
                                                 double min, double max){
        return optimize(maxEval, f, goalType, min, max, min + 0.5 * (max - min));
    }

    /**
     * {@inheritDoc}
     */
    public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> c) {
        checker = c;
    }

    /**
     * {@inheritDoc}
     */
    public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
        return checker;
    }

    /**
     * Method for implementing actual optimization algorithms in derived
     * classes.
     *
     * @return the optimum and its corresponding function value.
     * @throws TooManyEvaluationsException if the maximal number of evaluations
     * is exceeded.
     * @throws org.apache.commons.math.exception.MathUserException if the
     * function to optimize throws one during search.
     */
    protected abstract UnivariateRealPointValuePair doOptimize();
}
