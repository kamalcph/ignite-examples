package my.apache.ignite.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;

public class CacheEltsPrinter {

	public CacheEltsPrinter() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		String cacheName = "hello";
		Ignition.setClientMode(true);
		Ignite ignite = Ignition.start("examples/config/example-ignite.xml");

		try (Scanner scan = new Scanner(System.in)) {
			while (scan.nextLine() != null) {
				Collection<Map<String, List<Character>>> result = ignite.compute().broadcast(new IgniteCallable<Map<String, List<Character>>>() {
					
					private static final long serialVersionUID = 1L;
					
					@IgniteInstanceResource
					private Ignite ignite;

					@Override
					public Map<String, List<Character>> call() {
						List<Character> characters = new ArrayList<Character>();
						Iterator<Entry<Character, Character>> iterator = ignite.<Character, Character>cache(cacheName).localEntries().iterator();
						while (iterator.hasNext()) {
							characters.add(iterator.next().getKey());
						}
						return Collections.singletonMap(ignite.cluster().localNode().id().toString().substring(0, 8), characters);
					}
				});
				System.out.println(result);
			}
		}
		
	}
}


/**
 * $Log$
 *  
 */
