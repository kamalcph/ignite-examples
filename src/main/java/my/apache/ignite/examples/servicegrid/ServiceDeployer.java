package my.apache.ignite.examples.servicegrid;

import java.io.IOException;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;

/**
 * Run this class one or many times and see how
 * many services are deployed in the grid.
 * 
 * @author kamal
 */
public class ServiceDeployer {

	public static final String calcService = "CalcService";
	
	public ServiceDeployer() {
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		Ignite ignite = Ignition.start("examples/config/example-ignite.xml");
		IgniteServices services = ignite.services();
		
		CalcService calculator = new CalcServiceImpl();
		services.deployNodeSingleton(calcService, calculator);
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(">>> Stopping the node");
				Ignition.stop(true);
			}
		}));
		
		while (true) {
			System.out.println("Press Enter to see the total no. of deployed services");
			System.in.read();
			System.out.println("Total no. of deployed services: " + services.services(calcService).size() + " " + services.serviceDescriptors());
		}
	}

}


/**
 * $Log$
 *  
 */
