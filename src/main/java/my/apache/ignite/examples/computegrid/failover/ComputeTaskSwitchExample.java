/** ComputeTaskSwitchExample.java -
* @version      $Name$
* @module       my.apache.ignite.examples.computegrid.failover
* 
* @purpose
* @see
*
* @author   Kamal (kamal@nmsworks.co.in)
*
* @created  Aug 12, 2016
* $Id$
*
* @bugs
*
* Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
* NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
*/ 

package my.apache.ignite.examples.computegrid.failover;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.events.CacheRebalancingEvent;
import org.apache.ignite.events.EventType;
import org.apache.ignite.lang.IgnitePredicate;
import org.apache.ignite.lang.IgniteRunnable;

/**
 * Demonstrates whenever a Ignite compute task triggered over
 * a Cache key. On rebalance, the task never gets interrupted /
 * notified even if it's respective Cache key moved to the 
 * another node.
 * 
 * <p>Start a remote node using {@link ComputeFailOverNodeStartup}<p>
 * 
 * Refer: 
 * 1. https://gist.github.com/Kamal15/0a4066de152b8ebc856fc264f7b4037d
 * 2. http://apache-ignite-users.70518.x6.nabble.com/Ignite-Compute-Rebalance-Scenario-td7004.html
 */
public class ComputeTaskSwitchExample {


	/**
	 * Executes example.
	 * @param args Command line arguments, none required.
	 */
	public static void main(String[] args) {
		
		Map<Character, Character> alphabetMap = new HashMap<>();
		for (char c='a'; c<='z'; c++) {
			alphabetMap.put(c, c);
		}
		
		try (Ignite ignite = Ignition.start(ComputeFailOverNodeStartup.configuration())) {
			
			final String CACHE_NAME = "mycache";
			IgniteCache<Character, Character> alphabetCache = ignite.getOrCreateCache(CACHE_NAME);
			alphabetCache.putAll(alphabetMap);
			
			ignite.events().localListen(new IgnitePredicate<CacheRebalancingEvent>() {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean apply(CacheRebalancingEvent e) {
					Thread printer = new Thread(new Runnable() {
						
						@Override
						public void run() {
							
							try {
								Thread.sleep(3000); // Delay added to make sure that cache rebalance gets completed. As REBALANCE_MODE is asynchronous.
								Map<ClusterNode, Collection<Character>> mapKeysToNodes = Ignition.ignite().<Character>affinity(CACHE_NAME)
										.mapKeysToNodes(alphabetMap.keySet());
								
								for (Map.Entry<ClusterNode, Collection<Character>> entry : mapKeysToNodes.entrySet()) {
									System.out.println(entry.getKey().id() + " " + entry.getValue());
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					printer.start();
					return true;
				}
			}, EventType.EVT_CACHE_REBALANCE_STOPPED);
			
			ignite.compute().affinityRun(CACHE_NAME, 'a', new IgniteRunnable() {
				
				private static final long serialVersionUID = -6798955561514308638L;

				@Override
				public void run() {
					int i=0;
					while (true) { // I've used while(true) to reproduce the problem
						try {
							System.out.println("Hello I'm running here : " + i++ + ", is Interrupted : " + Thread.currentThread().isInterrupted());
							Thread.sleep(1000);
						} catch (Exception e) {
							System.out.println("Task got interrupted" +  e);
						}
					}
				}
			});
		}
	}
	
}


/**
 * $Log$
 *  
 */
