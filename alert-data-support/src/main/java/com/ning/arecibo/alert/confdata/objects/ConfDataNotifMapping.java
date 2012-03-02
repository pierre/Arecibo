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

package com.ning.arecibo.alert.confdata.objects;

import java.util.Map;

import com.ning.arecibo.util.Logger;

public class ConfDataNotifMapping extends ConfDataObject
{
    private final static Logger log = Logger.getLogger(ConfDataNotifMapping.class);

    public final static String TYPE_NAME = "notif_mapping";
    public final static String INSERT_TEMPLATE_NAME = ":insert_notif_mapping";
    public final static String UPDATE_TEMPLATE_NAME = ":update_notif_mapping";

    private final static String NOTIF_GROUP_ID_FIELD = "notif_group_id";
    private final static String NOTIF_CONFIG_ID_FIELD = "notif_config_id";

    protected volatile Long notifGroupId = null;
    protected volatile Long notifConfigId = null;

    public ConfDataNotifMapping() {}

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public String getInsertSqlTemplateName() {
        return INSERT_TEMPLATE_NAME;
    }

    @Override
    public String getUpdateSqlTemplateName() {
        return UPDATE_TEMPLATE_NAME;
    }

    @Override
    public void setPropertiesFromMap(Map<String,Object> map) {
        super.setPropertiesFromMap(map);
        setNotifGroupId(getLong(map, NOTIF_GROUP_ID_FIELD));
        setNotifConfigId(getLong(map, NOTIF_CONFIG_ID_FIELD));
    }

    @Override
    public Map<String,Object> toPropertiesMap() {
        Map<String,Object> map = super.toPropertiesMap();
        setLong(map, NOTIF_GROUP_ID_FIELD, getNotifGroupId());
        setLong(map, NOTIF_CONFIG_ID_FIELD, getNotifConfigId());

        return map;
    }

    @Override
    public void toStringBuilder(StringBuilder sb) {
        super.toStringBuilder(sb);
        sb.append(String.format("   %s -> %s\n", NOTIF_GROUP_ID_FIELD, getNotifGroupId()));
        sb.append(String.format("   %s -> %s\n", NOTIF_CONFIG_ID_FIELD, getNotifConfigId()));
    }

    public Long getNotifGroupId() {
        return notifGroupId;
    }

    public void setNotifGroupId(Long notifGroupId) {
        this.notifGroupId = notifGroupId;
    }

    public Long getNotifConfigId() {
        return notifConfigId;
    }

    public void setNotifConfigId(Long notifConfigId) {
        this.notifConfigId = notifConfigId;
    }
}
