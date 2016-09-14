/** ComputeFailOverNodeStartup.java -
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

import java.util.Arrays;
import java.util.Collections;

import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.events.EventType;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.checkpoint.sharedfs.SharedFsCheckpointSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.spi.failover.FailoverSpi;
import org.apache.ignite.spi.failover.always.AlwaysFailoverSpi;

/**
 * Starts up an empty node with checkpoint-enabled configuration.
 */
public class ComputeFailOverNodeStartup {

	public ComputeFailOverNodeStartup() {
	}

	/**
	 * Starts up an empty node with specified configuration.
	 * 
	 * @param args Command line arguments, none required
	 */
	public static void main(String[] args) {
		Ignition.start(configuration());
		System.out.println(Arrays.toString(Ignition.ignite().configuration().getFailoverSpi()));
	}
	
	/**
	 * Create Ignite configuration with configured checkpoints.
	 * 
	 * @return Ignite configuration
	 */
	public static IgniteConfiguration configuration() {
		IgniteConfiguration cfg = new IgniteConfiguration();
		
		cfg.setLocalHost("127.0.0.1");
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setGridLogger(new Slf4jLogger());
		
		// default FailoverSpi
		AlwaysFailoverSpi spi = new AlwaysFailoverSpi();
		spi.setMaximumFailoverAttempts(5);
		cfg.setFailoverSpi(spi);
		
		// Configure checkpoint SPI
		SharedFsCheckpointSpi checkpointSpi = new SharedFsCheckpointSpi();
		checkpointSpi.setDirectoryPaths(Collections.singletonList("work/checkpoing/sharedfs"));
		cfg.setCheckpointSpi(checkpointSpi);
		
		// Configure discovery SPI
		TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
		
		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
		ipFinder.setAddresses(Arrays.asList("127.0.0.1:48500..48509"));
		discoverySpi.setIpFinder(ipFinder);
		discoverySpi.setLocalPort(48500);
		
		cfg.setDiscoverySpi(discoverySpi);
		
		cfg.setIncludeEventTypes(EventType.EVT_CACHE_REBALANCE_STOPPED);
		return cfg;
	}

}


/**
 * $Log$
 *  
 */
