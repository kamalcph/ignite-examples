package my.apache.ignite.examples;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class NodeStartup {

	public NodeStartup() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Ignite ignite = Ignition.start("examples/config/example-ignite.xml");
		
		IgniteConfiguration config = ignite.configuration();
		System.out.println("Ignite configuration : " + config + ", attributes : " + config.getUserAttributes());
	}
}


/**
 * $Log$
 *  
 */
