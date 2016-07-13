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

import org.junit.Assert;
import my.apache.ignite.examples.collocation.Person;
import my.apache.ignite.examples.collocation.PersonKey;
import my.apache.ignite.examples.utils.TestsHelper;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraCacheStoreExample {

	private static Logger logger = LoggerFactory.getLogger(CassandraCacheStoreExample.class.getName());
	private static Ignite ignite;
	
	@BeforeClass
	public static void beforeClass() {
		logger.info("Hello");
		ignite = Ignition.start("examples/config/example-cassandra.xml");
		logger.info("Ignite started");
	}
	
	@AfterClass
	public static void afterClass() {
		Ignition.stop(true);
		logger.info("Ignite Stopped");
	}
	
	@Before
	public void beforeMethod() {
		logger.info("****** Test Started ******");
	}
	
	@After
	public void afterMethod() {
		logger.info("****** Test Completed ******");
	}
	
	public CassandraCacheStoreExample() {
	}

	@Test
	public void testPojoCache() {
		
		IgniteCache<PersonKey, Person> cache = ignite.getOrCreateCache("pojo_csndra_cache");
		cache.putAll(TestsHelper.generatePersonKeyPersonsMap(100));
		printSize(cache);
	}
	
	/*@Test
	public void testPojoCacheClear() {
		IgniteCache<PersonKey, Person> cache = ignite.getOrCreateCache("pojo_csndra_cache");
		cache.clear();
		cache.loadCache(null, new Object[] {"Select * from hello.pojo_persons"});
		Assert.assertEquals(100, cache.size());
	}*/
	
	@Test
	public void testPojoCacheRemoveAll() {
		IgniteCache<PersonKey, Person> cache = ignite.getOrCreateCache("pojo_csndra_cache");
		cache.removeAll();
		cache.loadCache(null, new Object[] {"Select * from hello.pojo_persons"});
		Assert.assertEquals(0, cache.size());
	}

	@Test
	public void testBlobCache() {
		int cnt = 10;
		IgniteCache<PersonKey, Person> cache = ignite.getOrCreateCache("blob_csndra_cache");
		cache.loadCache(null, new Object[] {"Select * from hello.blob_persons"});
		cache.putAll(TestsHelper.generatePersonKeyPersonsMap(cnt));
		printSize(cache);
		Assert.assertEquals(cnt, cache.size());
	}

	@Test
	public void testPrimitiveCache() {
		int cnt = 5;
		IgniteCache<String, String> cache = ignite.getOrCreateCache("primitive_csndra_cache");
		cache.loadCache(null, new Object[] {"Select * from hello.primitive_xyz"});
		cache.putAll(TestsHelper.generateStringsEntries(cnt));
		
		printSize(cache);
		Assert.assertEquals(cnt, cache.size());
	}

	@SuppressWarnings("rawtypes")
	private void printSize(IgniteCache cache) {
		
		int size=0;
		@SuppressWarnings("unchecked")
		Iterator<Entry> iterator = cache.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			size++;
		}
		
		System.out.println(">> Iterable No. of entries in the cache :: " + size);
		System.out.println(">> No. of primary entries available in the cache : " + cache.localSize(CachePeekMode.PRIMARY));
		System.out.println(">> Total No. of entries available in the cache across nodes : " + cache.size(CachePeekMode.PRIMARY));
	}
}


/**
 * $Log$
 *  
 */
