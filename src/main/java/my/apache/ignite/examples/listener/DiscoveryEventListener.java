/** DiscoveryEventListener.java -
* @version      $Name$
* @module       in.co.nmsworks.examples.kafka.consumer
* 
* @purpose
* @see
*
* @author   Kamal (kamal@nmsworks.co.in)
*
* @created  Feb 24, 2016
* $Id$
*
* @bugs
*
* Copyright 2014-2015 NMSWorks Software Pvt Ltd. All rights reserved.
* NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
*/ 

package my.apache.ignite.examples.listener;

import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.events.DiscoveryEvent;
import org.apache.ignite.events.EventType;
import org.apache.ignite.lang.IgnitePredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscoveryEventListener {

	final static Logger logger = LoggerFactory.getLogger(DiscoveryEventListener.class);
	
	public static void main(String[] args) throws InterruptedException {
		
		Ignite ignite = Ignition.start("examples/config/example-ignite.xml");
		String cacheName = "sampleCache";
		
		CacheConfiguration<String, Boolean> cfg = new CacheConfiguration<>(cacheName);
		cfg.setBackups(1);
		IgniteCache<String, Boolean> alphaCache = ignite.getOrCreateCache(cfg);
		
		for(final String ems : "A B C D E F".split(" "))
			alphaCache.put(ems, true);
		
		Thread cacheScanner = new Thread(new CacheScanJob(cacheName));
		cacheScanner.start();
		
		registerDiscoveryEvtListener(ignite);
		
		Thread.sleep(TimeUnit.SECONDS.toMillis(60));
		System.exit(0);
	}
	
	private static void registerDiscoveryEvtListener(final Ignite ignite) {
		
		ignite.events().localListen(new IgnitePredicate<DiscoveryEvent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean apply(DiscoveryEvent e) {
				UUID id = ignite.cluster().localNode().id();
				logger.info("S : {} Discovery event received : {}", id, e);
				return true;
			}
		}, EventType.EVTS_DISCOVERY);
	}

	private static class CacheScanJob implements Runnable {

		private String cacheName;
		
		public CacheScanJob(String cacheName) {
			this.cacheName = cacheName;
		}
		
		@Override
		public void run() {
			
			Ignite ignite = Ignition.ignite();
			UUID id = ignite.cluster().localNode().id();
			IgniteCache<String, Boolean> alphaCache  = ignite.cache(cacheName);
			
			while (true) {
				for (CachePeekMode mode : Arrays.asList(CachePeekMode.PRIMARY, CachePeekMode.BACKUP)) {
					
					Iterator<Entry<String, Boolean>> iterator = alphaCache.localEntries(mode).iterator();
					while (iterator.hasNext()) {
						Entry<String, Boolean> next = iterator.next();
						logger.info("S : {} Mode : {}, [Key : {}, Val : {}]", id, mode, next.getKey(), next.getValue());
					}
				}
				sleep(1000);
				logger.info("");
			}
		}
		
		private void sleep(long millis) {
			try {
				Thread.sleep(millis);
			} catch (Exception e) {
				logger.error("Exception while sleeping", e);
			}
		}
	}
}


/**
 * $Log$
 *  
 */
