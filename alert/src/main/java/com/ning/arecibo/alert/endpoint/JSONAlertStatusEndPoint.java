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

package com.ning.arecibo.alert.endpoint;

import com.google.inject.Inject;
import com.ning.arecibo.alert.client.AlertStatus;
import com.ning.arecibo.alert.client.AlertStatusJSONConverter;
import com.ning.arecibo.alert.conf.ConfigManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

import static com.ning.arecibo.alert.client.AlertActivationStatus.ERROR;

@Path("/xn/rest/1.0/JSONAlertStatus")
public class JSONAlertStatusEndPoint
{
    private final ConfigManager confStatusManager;

    @Inject
    public JSONAlertStatusEndPoint(final ConfigManager confStatusManager)
    {
        this.confStatusManager = confStatusManager;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAlertStatusJSON() throws IOException
    {
        final List<AlertStatus> alertStatii = confStatusManager.getAlertStatus(ERROR);

        return AlertStatusJSONConverter.serializeStatusListToJSON(alertStatii);
    }
}
