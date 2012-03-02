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

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.google.inject.Inject;
import com.ning.arecibo.alert.conf.ConfigManager;

@Path("/xn/rest/1.0/AlertStatus")
public class AlertStatusEndPoint
{
    private final ConfigManager confStatusManager;

    @Inject
    public AlertStatusEndPoint(ConfigManager confStatusManager) 
    {
        this.confStatusManager = confStatusManager;
    }

    @GET
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus()
    {
        return confStatusManager.getAlertStatusString();
    }
}
