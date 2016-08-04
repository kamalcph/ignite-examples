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
