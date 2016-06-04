package my.apache.ignite.examples.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;

/**
 * This class demonstrates what happens when cache
 * gets hits Out-Of-Memory Error
 * 
 * @author kamal
 *
 */
public class CacheOverFlowExample {

	public CacheOverFlowExample() {
	}

	private static void readCache(IgniteCache<String, String> cache) {
		List<String> contents = new ArrayList<>();
		Iterator<Entry<String, String>> iterator = cache.iterator();
		while(iterator.hasNext()) {
			contents.add(iterator.next().getKey());
		}
		Collections.sort(contents);
		System.out.println("Cache contents : " + contents + ", Size : " + cache.size());
	}
	
	public static void main(String[] args) {
		
		try (Ignite ignite = Ignition.start("examples/config/example-ignite.xml")) {
			
			String cacheName = "samplecache";
			IgniteCache<String, String> sampleCache = ignite.getOrCreateCache(cacheName);
			
			readCache(sampleCache);

			try (IgniteDataStreamer<String, String> streamer = ignite.dataStreamer(cacheName)) {
				
				streamer.perNodeBufferSize(1024);
				streamer.perNodeParallelOperations(8);
				
				for(Long l=0L; l<Long.MAX_VALUE; l++) {
					streamer.addData(l.toString(), l.toString());
					
					if (l>0 && l%10_000 == 0) {
						System.out.printf("Loaded %d keys, Cache size : %d %n", l, sampleCache.size());
					}
				}
			}
		}
	}

}


/**
 * $Log$
 *  
 */

