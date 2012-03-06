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

package org.apache.commons.math3.optimization.fitting;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
import org.apache.commons.math3.optimization.PointVectorValuePair;

/** Fitter for parametric univariate real functions y = f(x).
 * <p>When a univariate real function y = f(x) does depend on some
 * unknown parameters p<sub>0</sub>, p<sub>1</sub> ... p<sub>n-1</sub>,
 * this class can be used to find these parameters. It does this
 * by <em>fitting</em> the curve so it remains very close to a set of
 * observed points (x<sub>0</sub>, y<sub>0</sub>), (x<sub>1</sub>,
 * y<sub>1</sub>) ... (x<sub>k-1</sub>, y<sub>k-1</sub>). This fitting
 * is done by finding the parameters values that minimizes the objective
 * function &sum;(y<sub>i</sub>-f(x<sub>i</sub>))<sup>2</sup>. This is
 * really a least squares problem.</p>
 * @version $Id: CurveFitter.java 1244107 2012-02-14 16:17:55Z erans $
 * @since 2.0
 */
public class CurveFitter {
    /** Optimizer to use for the fitting. */
    private final DifferentiableMultivariateVectorOptimizer optimizer;
    /** Observed points. */
    private final List<WeightedObservedPoint> observations;

    /** Simple constructor.
     * @param optimizer optimizer to use for the fitting
     */
    public CurveFitter(final DifferentiableMultivariateVectorOptimizer optimizer) {
        this.optimizer = optimizer;
        observations = new ArrayList<WeightedObservedPoint>();
    }

    /** Add an observed (x,y) point to the sample with unit weight.
     * <p>Calling this method is equivalent to call
     * {@code addObservedPoint(1.0, x, y)}.</p>
     * @param x abscissa of the point
     * @param y observed value of the point at x, after fitting we should
     * have f(x) as close as possible to this value
     * @see #addObservedPoint(double, double, double)
     * @see #addObservedPoint(WeightedObservedPoint)
     * @see #getObservations()
     */
    public void addObservedPoint(double x, double y) {
        addObservedPoint(1.0, x, y);
    }

    /** Add an observed weighted (x,y) point to the sample.
     * @param weight weight of the observed point in the fit
     * @param x abscissa of the point
     * @param y observed value of the point at x, after fitting we should
     * have f(x) as close as possible to this value
     * @see #addObservedPoint(double, double)
     * @see #addObservedPoint(WeightedObservedPoint)
     * @see #getObservations()
     */
    public void addObservedPoint(double weight, double x, double y) {
        observations.add(new WeightedObservedPoint(weight, x, y));
    }

    /** Add an observed weighted (x,y) point to the sample.
     * @param observed observed point to add
     * @see #addObservedPoint(double, double)
     * @see #addObservedPoint(double, double, double)
     * @see #getObservations()
     */
    public void addObservedPoint(WeightedObservedPoint observed) {
        observations.add(observed);
    }

    /** Get the observed points.
     * @return observed points
     * @see #addObservedPoint(double, double)
     * @see #addObservedPoint(double, double, double)
     * @see #addObservedPoint(WeightedObservedPoint)
     */
    public WeightedObservedPoint[] getObservations() {
        return observations.toArray(new WeightedObservedPoint[observations.size()]);
    }

    /**
     * Remove all observations.
     */
    public void clearObservations() {
        observations.clear();
    }

    /**
     * Fit a curve.
     * This method compute the coefficients of the curve that best
     * fit the sample of observed points previously given through calls
     * to the {@link #addObservedPoint(WeightedObservedPoint)
     * addObservedPoint} method.
     *
     * @param f parametric function to fit.
     * @param initialGuess first guess of the function parameters.
     * @return the fitted parameters.
     * @throws org.apache.commons.math3.exception.DimensionMismatchException
     * if the start point dimension is wrong.
     */
    public double[] fit(final ParametricUnivariateFunction f, final double[] initialGuess) {
        return fit(Integer.MAX_VALUE, f, initialGuess);
    }

    /**
     * Fit a curve.
     * This method compute the coefficients of the curve that best
     * fit the sample of observed points previously given through calls
     * to the {@link #addObservedPoint(WeightedObservedPoint)
     * addObservedPoint} method.
     *
     * @param f parametric function to fit.
     * @param initialGuess first guess of the function parameters.
     * @param maxEval Maximum number of function evaluations.
     * @return the fitted parameters.
     * @throws org.apache.commons.math3.exception.TooManyEvaluationsException
     * if the number of allowed evaluations is exceeded.
     * @throws org.apache.commons.math3.exception.DimensionMismatchException
     * if the start point dimension is wrong.
     * @since 3.0
     */
    public double[] fit(int maxEval, final ParametricUnivariateFunction f,
                        final double[] initialGuess) {
        // prepare least squares problem
        double[] target  = new double[observations.size()];
        double[] weights = new double[observations.size()];
        int i = 0;
        for (WeightedObservedPoint point : observations) {
            target[i]  = point.getY();
            weights[i] = point.getWeight();
            ++i;
        }

        // perform the fit
        PointVectorValuePair optimum =
            optimizer.optimize(maxEval, new TheoreticalValuesFunction(f),
                               target, weights, initialGuess);

        // extract the coefficients
        return optimum.getPointRef();
    }

    /** Vectorial function computing function theoretical values. */
    private class TheoreticalValuesFunction
        implements DifferentiableMultivariateVectorFunction {
        /** Function to fit. */
        private final ParametricUnivariateFunction f;

        /** Simple constructor.
         * @param f function to fit.
         */
        public TheoreticalValuesFunction(final ParametricUnivariateFunction f) {
            this.f = f;
        }

        /** {@inheritDoc} */
        public MultivariateMatrixFunction jacobian() {
            return new MultivariateMatrixFunction() {
                public double[][] value(double[] point) {
                    final double[][] jacobian = new double[observations.size()][];

                    int i = 0;
                    for (WeightedObservedPoint observed : observations) {
                        jacobian[i++] = f.gradient(observed.getX(), point);
                    }

                    return jacobian;
                }
            };
        }

        /** {@inheritDoc} */
        public double[] value(double[] point) {
            // compute the residuals
            final double[] values = new double[observations.size()];
            int i = 0;
            for (WeightedObservedPoint observed : observations) {
                values[i++] = f.value(observed.getX(), point);
            }

            return values;
        }
    }
}
