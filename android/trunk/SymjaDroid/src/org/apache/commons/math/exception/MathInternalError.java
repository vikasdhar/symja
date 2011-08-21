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

import org.apache.commons.math.exception.util.LocalizedFormats;

/**
 * Exception triggered when something that shouldn't happen does happen.
 *
 * @since 2.2
 * @version $Revision: 1061498 $ $Date: 2011-01-20 21:32:54 +0100 (Do, 20 Jan 2011) $
 */
public class MathInternalError extends MathIllegalStateException {

    /** Serializable version Id. */
    private static final long serialVersionUID = -6276776513966934846L;

    /** URL for reporting problems. */
    private static final String REPORT_URL = "https://issues.apache.org/jira/browse/MATH";

    /**
     * Simple constructor.
     */
    public MathInternalError() {
        super(LocalizedFormats.INTERNAL_ERROR, REPORT_URL);
    }

    /**
     * Simple constructor.
     * @param cause root cause
     */
    public MathInternalError(final Throwable cause) {
        super(LocalizedFormats.INTERNAL_ERROR, REPORT_URL);
    }

}
