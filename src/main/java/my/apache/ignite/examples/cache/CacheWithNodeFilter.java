package my.apache.ignite.examples.cache;

import javax.cache.Cache;

import my.apache.ignite.examples.NodeStartup;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgnitePredicate;

/**
 * Start two or more nodes using {@link NodeStartup} 
 * with / without attribute as 'workerGroup - sample'. 
 * Then see, the data is collocated only between
 * the nodes which have same attributes.
 * 
 * @author kamal
 *
 */
public class CacheWithNodeFilter {

	public CacheWithNodeFilter() {
	}

	public static void main(String[] args) {
		
		try (Ignite ignite = Ignition.start("examples/config/example-cache.xml")) {
			
			IgniteCache<String, String> fmevents = ignite.getOrCreateCache("alarmcache");
			
			for (Character c='a'; c<='z'; c++) {
				String ch = c.toString();
				fmevents.put(ch, ch);
			}
			
			int i=1;
			for (Cache.Entry<String, String> entry : fmevents.localEntries(CachePeekMode.PRIMARY)) {
				System.out.println("i : " + i++ + ", key : " + entry.getKey() + ", val : " + entry.getValue());
			}
		}
	}
	
	public static class NodePredicate implements IgnitePredicate<ClusterNode> {

		private static final long serialVersionUID = 1L;

		@Override
		public boolean apply(ClusterNode node) {
			System.out.println("ClusterNode : " + node + ", attributes : " + node.attributes());
			return node.attribute("workerGroup").equals("sample");
		}
	}

}


/**
 * $Log$
 *  
 */
