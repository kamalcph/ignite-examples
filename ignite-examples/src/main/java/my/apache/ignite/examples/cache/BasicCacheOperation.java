/** BasicCacheOperation.java -
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

package my.apache.ignite.examples.cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

public class BasicCacheOperation {

	public BasicCacheOperation() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		try (Ignite ignite = Ignition.start("examples/config/example-ignite.xml")) {
			
			IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myCacheName");
			
			// Basic Put and Get Operation
			for (int i=0; i<10; i++) {
				cache.put(Integer.toString(i), i);
			}
			
			for (Integer i=0; i<10; i++) {
				System.out.println("Got [key = " + i + ", val = " + cache.get(i.toString()) + "]");
			}
			
			// Atomic operations
			boolean success = cache.replace("Hello", 25);
			System.out.println("[Expecting false ] Success : " + success);
			
			Integer oldVal = cache.getAndPutIfAbsent("Hello", 15);
			System.out.println("[Expecting null]  oldVal : " + oldVal);
			
			oldVal = cache.getAndPut("Hello", 17);
			System.out.println("[Expecting non-null] Old val : " + oldVal);
			
			oldVal = cache.getAndReplace("Hello", 19);
			System.out.println("Old val : " + oldVal);
			
		}
	}
}


/**
 * $Log$
 *  
 */
