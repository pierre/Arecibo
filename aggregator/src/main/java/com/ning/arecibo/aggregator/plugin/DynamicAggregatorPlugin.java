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

package com.ning.arecibo.aggregator.plugin;

import java.util.Map;
import com.ning.arecibo.aggregator.dictionary.EventDefinition;
import com.ning.arecibo.lang.Aggregator;


public interface DynamicAggregatorPlugin
{
	Aggregator getDynamicAggregator(EventDefinition definition);
    EventDefinition preProcessEventDefinition(EventDefinition definition);
    void postProcessEvent(Map<String,Object> map);
    boolean isInterestedIn(EventDefinition definition);
}
