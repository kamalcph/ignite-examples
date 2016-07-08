package my.apache.ignite.examples;


import java.util.Arrays;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cache.affinity.rendezvous.RendezvousAffinityFunction;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.events.CacheRebalancingEvent;
import org.apache.ignite.events.EventAdapter;
import org.apache.ignite.events.EventType;
import org.apache.ignite.internal.IgniteNodeAttributes;
import org.apache.ignite.lang.IgnitePredicate;

public class RebalanceTester {
	
	public static final String CACHE_NAME = "ON_HEAP_CACHE"; 
	
    public static void main(String[] args) throws IgniteException {

    	System.setProperty("IGNITE_HOME", "/home/kamal/git/ignite-examples");
    	final Ignite ignite = Ignition.start("examples/config/example-ignite.xml");
        
        CacheConfiguration<String, String> cacheConfig = new CacheConfiguration<String, String>();
        cacheConfig.setAffinity(new RendezvousAffinityFunction(false, 10));
        cacheConfig.setCacheMode(CacheMode.PARTITIONED);
        cacheConfig.setBackups(1);
        cacheConfig.setName(CACHE_NAME);
        IgniteCache<String, String> cache = ignite.getOrCreateCache(cacheConfig);
        		
		IgnitePredicate<EventAdapter> locLsnr = new IgnitePredicate<EventAdapter>() {
			@Override
			public boolean apply(EventAdapter evt) {
				Boolean isClient = null;

				switch (evt.type()) {
				case EventType.EVT_CACHE_REBALANCE_STOPPED:
					if (evt instanceof CacheRebalancingEvent) {
						CacheRebalancingEvent cacheEvt = (CacheRebalancingEvent) evt;
						isClient = cacheEvt.discoveryNode().attribute(IgniteNodeAttributes.ATTR_CLIENT_MODE);
						if (isClient != null && !isClient
								&& cacheEvt.cacheName().equals(CACHE_NAME)
								&& (cacheEvt.discoveryEventType() == EventType.EVT_NODE_JOINED
										|| cacheEvt.discoveryEventType() == EventType.EVT_NODE_LEFT 
										|| cacheEvt.discoveryEventType() == EventType.EVT_NODE_FAILED)) {
							
							//Rebalance has been finished?
							System.out.println("Rebalance stopped.");
							Affinity aff = ignite.affinity(CACHE_NAME);
							int[] partitions = aff.primaryPartitions(ignite.cluster().localNode());
							System.out.println("Current partitions : " + Arrays.toString(partitions));
							
						}
					}
					break;
				default:
					break;
				}

				return true;
			}
        };
        
        ignite.events().localListen(locLsnr, EventType.EVT_CACHE_REBALANCE_STOPPED);
        
        while(true) {
        	try {
        		System.in.read();
        		
        		Affinity aff = ignite.affinity(CACHE_NAME);
				int[] partitions = aff.primaryPartitions(ignite.cluster().localNode());
				System.out.println("Current primary partitions : " + Arrays.toString(partitions));
        		
			} catch (Exception e) {
				e.printStackTrace();
			}
        	
        }
    }
 
}