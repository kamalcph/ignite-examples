/** AutoClientReconnect.java -
* @version      $Name$
* @module       my.examples.ignite
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

package my.apache.ignite.examples;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteClientDisconnectedException;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteRunnable;

public class AutoClientReconnect {

	public AutoClientReconnect() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		Ignition.setClientMode(true);
		try (Ignite ignite = Ignition.start("examples/config/example-ignite.xml")) {
			IgniteCompute compute = ignite.compute();
			while (true) {
				try {
					compute.run(new IgniteRunnable() {
						
						private static final long serialVersionUID = 1L;

						@Override
						public void run() {
							System.out.println("I'm running");
						}
					});
				} catch (IgniteClientDisconnectedException e) {
					System.out.println("Client node disconnected");
					e.reconnectFuture().get();
					System.out.println("Client node reconnected");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}


/**
 * $Log$
 *  
 */
