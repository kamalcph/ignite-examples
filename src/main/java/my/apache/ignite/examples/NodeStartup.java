/** NodeStartup.java -
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

import org.apache.ignite.Ignition;

public class NodeStartup {

	public NodeStartup() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Ignition.start("examples/config/example-ignite.xml");
	}
}


/**
 * $Log$
 *  
 */
