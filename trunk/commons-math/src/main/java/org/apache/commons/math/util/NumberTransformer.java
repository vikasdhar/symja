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
package org.apache.commons.math.util;

import org.apache.commons.math.MathException;

/**
 * Subclasses implementing this interface can transform Objects to doubles.
 * @version $Id: NumberTransformer.java 1131229 2011-06-03 20:49:25Z luc $
 *
 * No longer extends Serializable since 2.0
 *
 */
public interface NumberTransformer {

    /**
     * Implementing this interface provides a facility to transform
     * from Object to Double.
     *
     * @param o the Object to be transformed.
     * @return the double value of the Object.
     * @throws MathException if the Object can not be transformed into a Double.
     */
    double transform(Object o) throws MathException;
}
