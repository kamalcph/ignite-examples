/** MyLifeCycleBean.java -
* @version      $Name$
* @module       my.examples.ignite.lifecycle
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

package my.apache.ignite.examples.lifecycle;

import org.apache.ignite.IgniteException;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;

public class MyLifeCycleBean implements LifecycleBean {

	public MyLifeCycleBean() {
	}

	@Override
	public void onLifecycleEvent(LifecycleEventType evt) throws IgniteException {

		switch (evt) {
		
		case AFTER_NODE_START:
			System.out.println(">> Ignite Node Started");
			break;
		case AFTER_NODE_STOP:
			System.out.println(">> Ignite Node Stopped");
			break;
		case BEFORE_NODE_START:
			System.out.println(">> Starting Ignite Node");
			break;
		case BEFORE_NODE_STOP:
			System.out.println(">> Stopping Ignite Node");
			break;
		default:
			break;
		}
	}

}


/**
 * $Log$
 *  
 */
