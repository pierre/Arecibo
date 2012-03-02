/*
 * Copyright 2010-2012 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.arecibo.util.timeline;

public interface SampleProcessor {

    /**
     * Process sampleCount sequential samples with identical values.  sampleCount will usually be 1,
     * but may be larger than 1.  Implementors may just loop processing identical values, but some
     * implementations may optimize adding a bunch of repeated values
     * @param timestamps a TimelineTimestamps instance, indexed by sample number to get the time at which the sample was captured.
     * @param sampleNumber the number of the sample within the timeline, used to index timestamps
     * @param sampleCount the count of sequential, identical values
     * @param opcode the opcode of the sample value, which may not be a REPEAT opcode
     * @param value the value of this kind of sample over the count of samples starting at the time
     * given by the sampleNumber indexing the TimelineTimestamps.
     */
    public void processSamples(final TimelineTimes timestamps,
                               final int sampleNumber,
                               final int sampleCount,
                               final SampleOpcode opcode,
                               final Object value);

}
