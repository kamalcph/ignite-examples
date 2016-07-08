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
	
	public void useService() throws InterruptedException {
		for (int i=0; i<100; i++) {
			CalcService calculator = getCalcService(false);
			System.out.printf("%d + %d = %d%n", i, i+1, calculator.add(i, i+1));
			System.out.printf("%d - %d = %d%n", i, i+1, calculator.sub(i, i+1));
			System.out.println();
			Thread.sleep(500);
			
			// Try to get the service from the remote server node
			if (i == 50) {
			//	cancel();
			}
		}
	}
	
	public void cancel() {
		ignite.services().cancel(calcService);
	}
	
	public void close() {
		ignite.close();
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		final ServiceDeployer deployer = new ServiceDeployer();
		deployer.deployClusterSingletonService();
		deployer.useService();
		
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
