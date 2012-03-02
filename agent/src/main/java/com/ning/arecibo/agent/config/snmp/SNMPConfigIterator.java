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

package com.ning.arecibo.agent.config.snmp;

import com.ning.arecibo.agent.config.Config;
import com.ning.arecibo.agent.config.ConfigIterator;
import com.ning.arecibo.agent.datasource.DataSourceException;
import com.ning.arecibo.agent.datasource.snmp.SNMPDataSource;
import com.ning.arecibo.util.Logger;

public class SNMPConfigIterator implements ConfigIterator {
	private static final Logger log = Logger.getLogger(SNMPConfigIterator.class);
	
	private final Config baseConfig;
	private final SNMPDataSource snmpDataSource;
	private final String oidName;
	private final int currRow;
	private final boolean isValid;
	private volatile boolean allocated = false;
	
	public SNMPConfigIterator(SNMPConfig baseConfig,SNMPDataSource snmpDataSource)
		throws DataSourceException
	{
		try {
			this.baseConfig = baseConfig;
		
			this.snmpDataSource = snmpDataSource;
			this.snmpDataSource.initialize();
			this.snmpDataSource.loadMib(baseConfig.getMib());
			this.oidName = baseConfig.getOidName();
			
			this.currRow = Integer.parseInt(baseConfig.getOidRow());
			
			this.isValid = isRowPollable(snmpDataSource,oidName,currRow);
		}
		catch(RuntimeException ruEx) {
			throw new DataSourceException("RuntimeException:",ruEx);
		}
	}
		
	private boolean isRowPollable(SNMPDataSource snmpDataSource,String oidName,int currRow)
		throws DataSourceException
	{
		
		// verify that the OID actually has data
		String oid = oidName + "." + currRow;
		
		log.debug("checking if oid '" + oid + "' is pollable for '" + baseConfig.getHost() + "'");
		
		Object value = snmpDataSource.getTableColumnValueViaCache(oidName, currRow);
		
		log.debug("Polled: '" + oid + "' = '" + value);
		
		if(value == null) {
			log.debug("table row not pollable for " + oid);
			return false;
		}
		
		return true;
	}
		
	@Override
	public synchronized Config getNextConfig() {
		
		if(!allocated && isValid) {
			allocated = true;
			return baseConfig;
		}
		else
			return null;
	}
}
