package my.apache.ignite.examples.collocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.cache.Cache;
import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.IgniteInstanceResource;

/**
 * Demonstrates the usage of data collocation in
 * {@link IgniteCache}.
 * 
 * <p>
 * Remote nodes should always be started with special configuration file which
 * enables P2P class loading: {@code 'ignite.{sh|bat} examples/config/example-ignite.xml'}.
 * <p>
 * Alternatively you can run {@link ExampleNodeStartup} in another JVM which will start Ignite node
 * with {@code examples/config/example-ignite.xml} configuration.
 * @author kamal
 *
 */
public class CacheCollocationExample {

	public CacheCollocationExample() {

	}

	public static void main(String[] args) {
		
		try (Ignite ignite = Ignition.start("examples/config/example-ignite.xml")) {
			
			if (ignite.cluster().nodes().size() < 2) {
				System.out.println("The example requires atleast 2 Ignite nodes");
				return;
			}

			Cache<Integer, Company> companiesCache = ignite.getOrCreateCache("companiesCache");
			Cache<AffinityKey<String>, Person> personCache = ignite.getOrCreateCache("personCache");
			
			for (Company company : companies()) {
				companiesCache.put(company.getId(), company);
			}
			
			for (Person person : persons()) {
				AffinityKey<String> key = new AffinityKey<>(person.getName(), person.getCompanyId());
				personCache.put(key, person);
			}
			
			scanCaches(ignite, companiesCache.getName(), personCache.getName());
			
			Person p1 = personCache.get(new AffinityKey<>("Neha", 2));
			System.out.println("Person1 : " + p1);
			
			// How to retrieve data from cache with simple key ?
			Person p2 = personCache.get(new AffinityKey<>("Peter"));
			System.out.println("Person2 : " + p2);
		}
	}
	
	private static List<Company> companies() {
		List<Company> companies = new ArrayList<>();
		companies.add(new Company(1, "Detective Comics"));
		companies.add(new Company(2, "Marvel Comics"));
		return companies;
	}

	private static List<Person> persons() {

		List<Person> persons = new ArrayList<>();
		
		// persons work for company 1
		persons.add(new Person("Ebord Thawne", 80, 100, 1));
		persons.add(new Person("Barry Allen", 20, 85, 1));
		persons.add(new Person("Joe Stein", 30, 56, 1));
		persons.add(new Person("Fire Storm", 35, 90, 1));
		
		// persons work for company 2
		persons.add(new Person("Hulk", 75, 77, 2));
		persons.add(new Person("Peter", 30, 88, 2));
		persons.add(new Person("Neha", 27, 92, 2));
		return persons;
	}
	
	private static void scanCaches(Ignite ignite, final String companyCache, final String personCache) {
		
		ignite.compute().broadcast(new IgniteRunnable() {
			
			private static final long serialVersionUID = 1L;
			
			@IgniteInstanceResource
			private Ignite ignite;

			@Override
			public void run() {
				
				String id8 = ignite.cluster().localNode().id().toString().substring(0, 8);

				IgniteCache<Integer, Company> cmpCache = ignite.cache(companyCache);
				Iterator<Entry<Integer, Company>> iterator = cmpCache.localEntries(CachePeekMode.PRIMARY).iterator();
				
				while (iterator.hasNext()) {
					Entry<Integer, Company> entry = iterator.next();
					System.out.println("Company [Key : " + entry.getKey() + ", Val : " + entry.getValue() + "] located in Node : " + id8);
				}
				
				IgniteCache<AffinityKey<String>, Person> psnCache = ignite.cache(personCache);
				Iterator<Entry<AffinityKey<String>, Person>> iterator1 = psnCache.localEntries(CachePeekMode.PRIMARY).iterator();
				
				while (iterator1.hasNext()) {
					Entry<AffinityKey<String>, Person> entry = iterator1.next();
					System.out.println("Person [Key : " + entry.getKey().key() + ", Val : " + entry.getValue() + "] located in Node : " + id8);
				}
			}
		});
		
	}

}


/**
 * $Log$
 *  
 */
