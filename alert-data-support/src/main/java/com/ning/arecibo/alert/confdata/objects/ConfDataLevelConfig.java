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

public class ConfDataLevelConfig extends ConfDataObject
{
    private final static Logger log = Logger.getLogger(ConfDataLevelConfig.class);

    public final static String TYPE_NAME = "level_config";
    public final static String INSERT_TEMPLATE_NAME = ":insert_level_config";
    public final static String UPDATE_TEMPLATE_NAME = ":update_level_config";

    private final static String COLOR_FIELD = "color";
    private final static String DEFAULT_NOTIF_GROUP_ID_FIELD = "default_notif_group_id";

    protected volatile String color = null;                 // HTML Color String
    protected volatile Long defaultNotifGroupId = null;

    public ConfDataLevelConfig() {}

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
        setColor(getString(map,COLOR_FIELD));
        setDefaultNotifGroupId(getLong(map, DEFAULT_NOTIF_GROUP_ID_FIELD));
    }

    @Override
    public Map<String,Object> toPropertiesMap() {
        Map<String,Object> map = super.toPropertiesMap();
        setString(map,COLOR_FIELD,getColor());
        setLong(map, DEFAULT_NOTIF_GROUP_ID_FIELD, getDefaultNotifGroupId());

        return map;
    }

    @Override
    public void toStringBuilder(StringBuilder sb) {
        super.toStringBuilder(sb);
        sb.append(String.format("   %s -> %s\n",COLOR_FIELD,getColor()));
        sb.append(String.format("   %s -> %s\n", DEFAULT_NOTIF_GROUP_ID_FIELD, getDefaultNotifGroupId()));
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getDefaultNotifGroupId() {
        return defaultNotifGroupId;
    }

    public void setDefaultNotifGroupId(Long defaultNotifGroupId) {
        this.defaultNotifGroupId = defaultNotifGroupId;
    }
}
