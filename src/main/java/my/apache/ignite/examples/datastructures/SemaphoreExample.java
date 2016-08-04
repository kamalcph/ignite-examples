package my.apache.ignite.examples.datastructures;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteSemaphore;
import org.apache.ignite.Ignition;

/**
 * This examples demonstrates the usage of {@link IgniteSemaphore}.
 * Start two or more {@link SemaphoreExample} nodes and see whether
 * each node able to acquire the permits and the task gets executed
 * sequentially
 *   
 * @author kamal
 */
public class SemaphoreExample {

	public SemaphoreExample() {
	}

	public static void main(String[] args) throws InterruptedException {
		
		try (Ignite ignite = Ignition.start("examples/config/example-ignite.xml")) {
			
			String id8 = ignite.cluster().localNode().id().toString().substring(0 , 8);
			IgniteSemaphore semaphore = ignite.semaphore("Hello", 1, true, true);
			semaphore.acquire();
			
			for (int i=0; i<20; i++) {
				System.out.println(id8 + ", " + i + " Hello");
				Thread.sleep(1000);
			}
			semaphore.release();
			Thread.sleep(1000);
		}
	}

}


/**
 * $Log$
 *  
 */
