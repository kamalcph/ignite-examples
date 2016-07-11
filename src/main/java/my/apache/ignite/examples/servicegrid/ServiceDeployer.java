/** ServiceDeployer.java -
* @version      $Name$
* @module       my.apache.ignite.examples.servicegrid
* 
* @purpose
* @see
*
* @author   Kamal (kamal@nmsworks.co.in)
*
* @created  Jul 8, 2016
* $Id$
*
* @bugs
*
* Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
* NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
*/ 

package my.apache.ignite.examples.servicegrid;

import java.io.IOException;

import my.apache.ignite.examples.NodeStartup;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;

/**
 * Start one or more node using {@link NodeStartup} and run this
 * example so that each node runs a service.
 * 
 * @author kamal
 */
public class ServiceDeployer {

	private final Ignite ignite;
	private final String calcService = "CalcService"; 
	
	public ServiceDeployer() {
		ignite = Ignition.start("examples/config/example-ignite.xml");
	}
	
	public void deployNodeSingletonService() {
		CalcService calculator = new CalcServiceImpl();
		ignite.services().deployNodeSingleton(calcService, calculator);
	}
	
	public void deployClusterSingletonService() {
		CalcService calculator = new CalcServiceImpl();
		ignite.services().deployClusterSingleton(calcService, calculator);
	}
	
	public CalcService getCalcService(boolean sticky) {
		return ignite.services().serviceProxy(calcService, CalcService.class, sticky);
	}
	
	public void useService(boolean cancelService) throws InterruptedException {
		for (int i=0; i<100; i++) {
			
			try {
				CalcService calculator = getCalcService(false);
				System.out.printf("%d + %d = %d%n", i, i+1, calculator.add(i, i+1));
				System.out.printf("%d - %d = %d%n", i, i+1, calculator.sub(i, i+1));
				System.out.println();
				
				// Try to get the service from the remote server node
				if (cancelService && i == 50) {
					close();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(500);
		}
	}
	
	// Canceling a service will remove it's deployment from the grid. So, don't cancel it.
	// Internally, Ignition.stop(true) cancels the local running Ignite Services.
	public void cancel() {
		ClusterGroup grp = ignite.cluster().forNode(ignite.cluster().localNode());
		ignite.services(grp).cancel(calcService);
	}
	
	public void close() {
		Ignition.stop(false);
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		final ServiceDeployer deployer = new ServiceDeployer();
		deployer.deployNodeSingletonService();
		// deployer.useService(true);
		deployer.useService(false);
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(">>> Stopping the node");
				Ignition.stop(false);
			}
		}));
		
		while (true) {
			System.in.read();
		}
	}

}


/**
 * $Log$
 *  
 */
