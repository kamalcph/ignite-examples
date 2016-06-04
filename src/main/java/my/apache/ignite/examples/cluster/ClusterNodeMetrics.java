/** ClusterNodeMetrics.java -
* @version      $Name$
* @module       my.examples.ignite.cluster
* 
* @purpose
* @see
*
* @author   Kamal (kamal@nmsworks.co.in)
*
* @created  Jun 3, 2016
* $Id$
*
* @bugs
*
* Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
* NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
*/ 

package my.apache.ignite.examples.cluster;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterMetrics;
import org.apache.ignite.cluster.ClusterNode;

public class ClusterNodeMetrics {

	public ClusterNodeMetrics() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		try (Ignite ignite = Ignition.start("examples/config/example-ignite.xml")) {
			
			IgniteCluster cluster = ignite.cluster();
			ClusterNode localNode = cluster.localNode();
			
			ClusterMetrics metrics = localNode.metrics();
			
			System.out.println("Current CPU Load : " + metrics.getCurrentCpuLoad());
			System.out.println("Heap Memory Used : " + metrics.getHeapMemoryUsed() / (1024 * 1024) + " MB");
			System.out.println("Total Heap Memory : " + metrics.getHeapMemoryTotal() / (1024 * 1024) + " MB");
		}
	}
}


/**
 * $Log$
 *  
 */
