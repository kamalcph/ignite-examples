package my.apache.ignite.examples.servicegrid;

import java.util.concurrent.TimeUnit;

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
		logger.info("Service : {} cancelled", ctx.name());
	}

	@Override
	public void init(ServiceContext ctx) throws Exception {
		logger.info(">>> Service : initing..", ctx.name());
		Thread.sleep(TimeUnit.MINUTES.toMillis(1));
		logger.info("Service : {} inited", ctx.name());
	}

	@Override
	public void execute(ServiceContext ctx) throws Exception {
		logger.info("Service : {} executed", ctx.name());
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
