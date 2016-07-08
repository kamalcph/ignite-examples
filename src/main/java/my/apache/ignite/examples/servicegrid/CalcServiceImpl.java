/** CalcServiceImpl.java -
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

import org.apache.ignite.services.ServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalcServiceImpl implements CalcService {

	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(CalcServiceImpl.class);
	
	public CalcServiceImpl() {
		logger.info("CalcServiceImpl instantiated");
	}

	@Override
	public void cancel(ServiceContext ctx) {
		logger.info("Service : {} got cancelled", ctx == null ? "" : ctx.name());
	}

	@Override
	public void init(ServiceContext ctx) throws Exception {
		logger.info("Service : {} got inited", ctx.name());
	}

	@Override
	public void execute(ServiceContext ctx) throws Exception {
		logger.info("Service : {} got executed", ctx.name());
	}

	@Override
	public int add(int a, int b) {
		return a+b;
	}

	@Override
	public int sub(int a, int b) {
		return a-b;
	}

}


/**
 * $Log$
 *  
 */
