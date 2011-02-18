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
package org.apache.commons.math.exception;

import org.apache.commons.math.exception.util.Localizable;

/**
 * Base class for all preconditions violation exceptions.
 * In most cases, this class should not be instantiated directly: it should
 * serve as a base class to create all the exceptions that share the semantics
 * of the standard {@link IllegalArgumentException}, but must also provide a
 * localized message.
 *
 * @since 2.2
 * @version $Revision$ $Date$
 */
public class MathIllegalArgumentException extends MathRuntimeException {
    /** Serializable version Id. */
    private static final long serialVersionUID = -6024911025449780478L;

    /**
     * @param specific Message pattern providing the specific context of
     * the error.
     * @param general Message pattern explaining the cause of the error.
     * @param args Arguments.
     */
    public MathIllegalArgumentException(Localizable specific,
                                        Localizable general,
                                        Object ... args) {
        super(null, specific, general, args);
    }
    /**
     * @param general Message pattern explaining the cause of the error.
     * @param args Arguments.
     */
    public MathIllegalArgumentException(Localizable general,
                                        Object ... args) {
        this(null, general, args);
    }
}
