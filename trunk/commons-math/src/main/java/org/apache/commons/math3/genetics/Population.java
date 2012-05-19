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
package org.apache.commons.math3.genetics;


/**
 * A collection of chromosomes that facilitates generational evolution.
 *
 * @since 2.0
 * @version $Id: Population.java 1310103 2012-04-05 22:08:45Z tn $
 */
public interface Population extends Iterable<Chromosome> {
    /**
     * Access the current population size.
     * @return the current population size.
     */
    int getPopulationSize();

    /**
     * Access the maximum population size.
     * @return the maximum population size.
     */
    int getPopulationLimit();

    /**
     * Start the population for the next generation.
     * @return the beginnings of the next generation.
     */
    Population nextGeneration();

    /**
     * Add the given chromosome to the population.
     * @param chromosome the chromosome to add.
     * @throws org.apache.commons.math3.exception.NumberIsTooLargeException if the population would exceed
     * the population limit when adding this chromosome
     */
    void addChromosome(Chromosome chromosome);

    /**
     * Access the fittest chromosome in this population.
     * @return the fittest chromosome.
     */
    Chromosome getFittestChromosome();
}