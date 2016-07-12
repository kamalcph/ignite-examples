/** CassandraCacheStoreExample.java -
* @version      $Name$
* @module       my.apache.ignite.examples.cache.store.cassandra
* 
* @purpose
* @see
*
* @author   Kamal (kamal@nmsworks.co.in)
*
* @created  Jul 12, 2016
* $Id$
*
* @bugs
*
* Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
* NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
*/ 

package my.apache.ignite.examples.cache.store.cassandra;

import java.util.Iterator;

import javax.cache.Cache.Entry;

import my.apache.ignite.examples.collocation.Person;
import my.apache.ignite.examples.collocation.PersonKey;
import my.apache.ignite.examples.utils.TestsHelper;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;

public class CassandraCacheStoreExample {

	public CassandraCacheStoreExample() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		try (Ignite ignite = Ignition.start("examples/config/example-cassandra.xml"))
		{
			int cnt = 10_000;
			System.out.println("*********Starting Cassandra Primitive Cache Test******");
			IgniteCache<String, String> cache = ignite.getOrCreateCache("primitive_csndra_cache");
			cache.putAll(TestsHelper.generateStringsEntries(cnt));
			
			for (int i = 0; i < cnt; i++) {
				System.out.printf("[Key : %d], [Value : %s]%n", i, cache.get(Integer.toString(i)));
			}
			
			int size=0;
			Iterator<Entry<String, String>> iterator = cache.iterator();
			while (iterator.hasNext()) {
				iterator.next();
				size++;
			}
			System.out.println("Total entries available in the cache :: " + size);
			System.out.println("LocalSize of the cache : " + cache.localSize(CachePeekMode.ALL));
			System.out.println("Size of the cache : " + cache.size(CachePeekMode.ALL));
			System.out.println("**********Completed Cassandra Primitive Cache Test**************\n");
			
			
			System.out.println("*********Starting Cassandra Blob Cache Test******");
			IgniteCache<PersonKey, Person> blobCache = ignite.getOrCreateCache("blob_csndra_cache");
			blobCache.putAll(TestsHelper.generatePersonKeyPersonsMap(cnt));
			System.out.println("**********Completed Cassandra Blob Cache Test**************\n");
			
			/*System.out.println("*********Starting Cassandra Pojo Cache Test******");
			IgniteCache<PersonKey, Person> pojoCache = ignite.getOrCreateCache("pojo_csndra_cache");
			pojoCache.putAll(TestsHelper.generatePersonKeyPersonsMap(cnt));
			System.out.println("**********Completed Cassandra Pojo Cache Test**************\n");*/
		}
	}
}


/**
 * $Log$
 *  
 */
