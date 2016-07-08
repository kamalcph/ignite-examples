/** CalcService.java -
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

import org.apache.ignite.services.Service;

public interface CalcService extends Service {

	int add(int a, int b);
	int sub(int a, int b);
}


/**
 * $Log$
 *  
 */
