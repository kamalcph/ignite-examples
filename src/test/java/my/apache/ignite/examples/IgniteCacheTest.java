package my.apache.ignite.examples;
/** IgniteCacheTest.java -
* @version      $Name$
* @module       org.apache.ignite.examples
* 
* @purpose
* @see
*
* @author   Kamal (kamal@nmsworks.co.in)
*
* @created  Apr 15, 2016
* $Id$
*
* @bugs
*
* Copyright 2014-2015 NMSWorks Software Pvt Ltd. All rights reserved.
* NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
*/ 

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.spi.failover.always.AlwaysFailoverSpi;

public class IgniteCacheTest
{
	public void populateCache()
	{
		String cacheName = "sample";
		Ignite ignite = startServerIgnite();
		sleep(5000);
		
		int nItems = 100010;
		int batchSize = 1000;
		createData(ignite, nItems, batchSize, cacheName);
		iterateData(ignite, cacheName);
		System.out.println("Cache populated in node :" + ignite.cluster().localNode().id());
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void iterateData(Ignite ignite, String cacheName) {
		IgniteCache<Long, Long> cache = ignite.cache(cacheName);
		Iterator<Entry<Long, Long>> iterator = cache.iterator();
		
		int i=0;
		while(iterator.hasNext()) {
			Entry<Long, Long> entry = iterator.next();
			// System.out.println(entry.getKey());
			i++;
		}
		System.out.println("Total number of entries iterated :" + i);
	} 

	private void createData(Ignite ignite, int nItems, int batchSize, String cacheName) {
		
		IgniteCache<Long, Long> cache = ignite.cache(cacheName);
		
		if (cache != null) 
		{
			System.out.println("Cache already populated!!");
			System.out.println("Size of the cache : " + cache.size());
			System.out.println("Local entries, Size of the cache : " + cache.localSize());
			return;
		}
		
		cache = ignite.getOrCreateCache(cacheName);
		
		int remainder = nItems % batchSize;
		int nBatches = (nItems / batchSize) + (remainder > 0 ? 1 : 0);
		
		for (int b=0; b<nBatches; b++) {
			int start = b * batchSize;
			int end = Math.min(nItems, (b+1) * batchSize);
			
			System.out.println("Populating the batch " + b + " with batchSize " + batchSize + " no. of items completed : " + end);
			Map<Long, Long> map = new HashMap<>();
			for (long k=start; k<end; k++) {
				map.put(k, k);
			}
			cache.putAll(map);
		}
		System.out.println("Size of the cache : " + cache.size());
	}

	private Ignite startServerIgnite()
	{
		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
		ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47509"));

		TcpDiscoverySpi discoSpi = new TcpDiscoverySpi();
		discoSpi.setIpFinder(ipFinder);
		
		Map<String, String> attrs = Collections.singletonMap("ROLE", "server");

		AlwaysFailoverSpi failOverSpi = new AlwaysFailoverSpi();
		failOverSpi.setMaximumFailoverAttempts(5);
		
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setLocalHost("127.0.0.1");
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setDiscoverySpi(discoSpi);
		cfg.setUserAttributes(attrs);
		cfg.setFailoverSpi(failOverSpi);
		return Ignition.start(cfg);
	}
	
	public static void main(String[] args) {
		
		IgniteCacheTest test = new IgniteCacheTest();
		test.populateCache();
	}
}


/**
 * $Log$
 *  
 */
