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

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Types;

@BindingAnnotation(TimelineTimesBinder.SomethingBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface TimelineTimesBinder
{
    public static class SomethingBinderFactory implements BinderFactory
    {
        private static final int MAX_IN_ROW_BLOB_SIZE = 400;

        public Binder build(final Annotation annotation)
        {
            return new Binder<TimelineTimesBinder, TimelineTimes>()
            {
                public void bind(final SQLStatement query, final TimelineTimesBinder binder, final TimelineTimes timelineTimes)
                {
                    query.bind("hostId", timelineTimes.getHostId())
                        .bind("eventCategory", timelineTimes.getEventCategory())
                        .bind("startTime", TimelineTimes.unixSeconds(timelineTimes.getStartTime()))
                        .bind("endTime", TimelineTimes.unixSeconds(timelineTimes.getEndTime()))
                        .bind("count", timelineTimes.getSampleCount());

                    // Use the in-row field if the blob is small enough
                    if (timelineTimes.getCompressedTimes().length > MAX_IN_ROW_BLOB_SIZE) {
                        query
                            .bindNull("inRowTimes", Types.VARBINARY)
                            .bind("blobTimes", timelineTimes.getCompressedTimes());
                    }
                    else {
                        query
                            .bind("inRowTimes", timelineTimes.getCompressedTimes())
                            .bindNull("blobTimes", Types.BLOB);
                    }
                }
            };
        }
    }
}