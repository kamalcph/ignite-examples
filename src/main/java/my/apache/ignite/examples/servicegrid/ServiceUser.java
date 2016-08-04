package my.apache.ignite.examples.servicegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

/**
 * Client node which access the Ignite service
 * continuously.
 * 
 * @author kamal
 *
 */
public class ServiceUser {

	public ServiceUser() {
	}

	public static void main(String[] args) throws InterruptedException {

		Ignition.setClientMode(true);
		final Ignite ignite = Ignition.start("examples/config/example-ignite.xml");
		
		CalcService calculator = ignite.services().serviceProxy(ServiceDeployer.calcService, CalcService.class, false);
		
		int i = 0;
		while (true) {
			System.out.println(i++ + ". 2 + 2 = " + calculator.add(2, 2));
			Thread.sleep(100);
		}
	}
}


/**
 * $Log$
 *  
 */
