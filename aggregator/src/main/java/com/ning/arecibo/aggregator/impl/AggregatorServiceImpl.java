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

package com.ning.arecibo.aggregator.impl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import com.google.inject.Inject;
import com.ning.arecibo.client.AggregatorService;
import com.ning.arecibo.client.RemoteAggregatorService;
import com.ning.arecibo.event.publisher.EventPublisherConfig;
import com.ning.arecibo.lang.Aggregator;
import com.ning.arecibo.util.Logger;
import com.ning.arecibo.util.service.ServiceDescriptor;
import com.ning.arecibo.util.service.ServiceLocator;
import com.ning.arecibo.util.service.ServiceNotAvailableException;
import com.ning.arecibo.util.service.ServiceSelector;

public class AggregatorServiceImpl extends UnicastRemoteObject implements RemoteAggregatorService
{
	private static final Logger log = Logger.getLogger(AggregatorServiceImpl.class);
	private final ServiceLocator serviceLocator;
	private final AggregatorRegistry registry;
	private final ServiceSelector selector;

	@Inject
	public AggregatorServiceImpl(ServiceLocator serviceLocator,
                                 AggregatorRegistry registry,
                                 EventPublisherConfig eventPublisherConfig) throws RemoteException
	{
		this.serviceLocator = serviceLocator;
		this.registry = registry;
		this.selector = new ServiceSelector(eventPublisherConfig.getEventServiceName());
    }

    public void register(final Aggregator agg,final long leaseTime,final TimeUnit leaseTimeUnit) throws RemoteException
    {
        onAllServers(new ServiceCallback()
        {
            public String getServiceName() {return "register";}
            public void onServer(RemoteAggregatorRegistry service) throws RemoteException
            {
                service.register(Aggregator.overrideNS(RemoteAggregatorService.DEFAULT_NS, agg),leaseTime,leaseTimeUnit);
            }
        });
    }

	public void register(final Aggregator agg) throws RemoteException
	{
        onAllServers(new ServiceCallback()
		{
            public String getServiceName() {return "register";}
			public void onServer(RemoteAggregatorRegistry service) throws RemoteException
			{
				service.register(Aggregator.overrideNS(RemoteAggregatorService.DEFAULT_NS, agg));
			}
		});
	}

	public void unregister(final String fullName) throws RemoteException
	{
		onAllServers(new ServiceCallback()
		{
            public String getServiceName() {return "unregister";}
			public void onServer(RemoteAggregatorRegistry service) throws RemoteException
			{
				service.unregister(fullName);
			}
		});
	}

	public List<String> getAggregatorFullNames(String namespace) throws RemoteException
	{
		return registry.getAggregatorFullNames(namespace);
	}

	public void ping() throws RemoteException
	{
		onAllServers(new ServiceCallback()
		{
            public String getServiceName() {return "ping";}
			public void onServer(RemoteAggregatorRegistry service) throws RemoteException
			{
				service.ping();
			}
		});
	}

	public void softRestart() throws RemoteException
	{
		onAllServers(new ServiceCallback()
		{
            public String getServiceName() {return "softRestart";}
			public void onServer(RemoteAggregatorRegistry service) throws RemoteException
			{
				service.softRestart();
			}
		});
	}

	private void onAllServers(ServiceCallback callback) throws RemoteException
	{
		Set<ServiceDescriptor> list = serviceLocator.selectServices(selector);
		for (ServiceDescriptor sd : list) {
			log.info("locating server %s for multi-node %s", sd.getProperty("host"),callback.getServiceName());
			try {
				callback.onServer(getRemoteAggregatorRegistry(sd));
			}
			catch (ServiceNotAvailableException e) {
				log.warn(e);
			}
		}
	}

	public RemoteAggregatorRegistry getRemoteAggregatorRegistryExcluding(ServiceDescriptor self) throws ServiceNotAvailableException
	{
		log.info("looking for '%s' servers....", selector.getServiceName());
        Set<ServiceDescriptor> list = serviceLocator.selectServices(selector);
        ServiceDescriptor sd = null;
        for ( ServiceDescriptor s : list ) {
	        log.info("checking server %s", s.getProperty("host"));
            if ( ! s.getUuid().equals(self.getUuid()) ) {
                sd = s ;
                break ;
            }
        }        
        if ( sd != null ) {
	        log.info("using server %s" ,sd.getProperty("host"));
            return getRemoteAggregatorRegistry(sd);
        }
        else {
            throw new ServiceNotAvailableException();
        }
    }

	public static RemoteAggregatorRegistry getRemoteAggregatorRegistry(ServiceDescriptor sd) throws ServiceNotAvailableException
	{
		return AggregatorService.getRemoteObject(sd, new AggregatorService.LookupCallback<RemoteAggregatorRegistry>()
		{
			public RemoteAggregatorRegistry lookup(Registry registry) throws NotBoundException, RemoteException
			{
				return (RemoteAggregatorRegistry) registry.lookup(AggregatorRegistry.class.getSimpleName());
			}
		});
	}

	private interface ServiceCallback
	{
        String getServiceName();
		void onServer(RemoteAggregatorRegistry service) throws RemoteException;
	}
}
