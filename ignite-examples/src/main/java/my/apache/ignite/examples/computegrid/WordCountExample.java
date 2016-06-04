/** WordCountExample.java -
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

package my.apache.ignite.examples.computegrid;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;

public class WordCountExample {

	public WordCountExample() {
	}

	public static void main(String[] args) {
		
		try (Ignite ignite = Ignition.start())
		{
			Collection<IgniteCallable<Integer>> jobs = new ArrayList<>();
			
			for (final String word : "count characters using callable".split(" ")) {
				
				jobs.add(new IgniteCallable<Integer>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Integer call() throws Exception {
						System.out.println("Computing the length of the word " + word);
						return word.length();
					}
				});
			}
			
			Collection<Integer> result = ignite.compute().call(jobs);
			int sum = 0;
			for (Integer len : result) {
				sum += len;
			}
			System.out.println(">> Total number of characters in the phrase : " + sum);
		}
	}

}


/**
 * $Log$
 *  
 */
