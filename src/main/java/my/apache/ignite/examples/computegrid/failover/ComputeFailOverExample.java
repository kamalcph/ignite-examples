/** ComputeFailOverExample.java -
* @version      $Name$
* @module       my.apache.ignite.examples.computegrid.failover
* 
* @purpose
* @see
*
* @author   Kamal (kamal@nmsworks.co.in)
*
* @created  Aug 12, 2016
* $Id$
*
* @bugs
*
* Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
* NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
*/ 

package my.apache.ignite.examples.computegrid.failover;

import java.util.Arrays;
import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeJobFailoverException;
import org.apache.ignite.compute.ComputeTaskSession;
import org.apache.ignite.compute.ComputeTaskSessionFullSupport;
import org.apache.ignite.lang.IgniteBiTuple;
import org.apache.ignite.lang.IgniteClosure;
import org.apache.ignite.resources.LoggerResource;
import org.apache.ignite.resources.TaskSessionResource;

/**
 * Demonstrates the usage of checkpoints in Ignite
 * <p>
 * This examples tries to compute the phrase length. In order to mitigate possible node failures, intermediate
 * result is saved as checkpoint after each job step. 
 * <p>
 * Remote nodes must be started using {@link ComputeFailOverNodeStartup}.
 */
public class ComputeFailOverExample {

	/**
	 * Executes example.
	 * 
	 * @param args Command line arguments, none required.
	 */
	public static void main(String[] args) {
		
		try (Ignite ignite = Ignition.start(ComputeFailOverNodeStartup.configuration())) {
			
			System.out.println();
			System.out.println("Compute failover example started");
			
			// Number of letters.
            int charCnt = ignite.compute().apply(new CheckPointJob(), "Stage1 Stage2");

            System.out.println();
            System.out.println(">>> Finished executing fail-over example with checkpoints.");
            System.out.println(">>> Total number of characters in the phrase is '" + charCnt + "'.");
            System.out.println(">>> You should see exception stack trace from failed job on some node.");
            System.out.println(">>> Failed job will be failed over to another node.");
		}
	}
	
	@ComputeTaskSessionFullSupport
	private static final class CheckPointJob implements IgniteClosure<String, Integer> {

		/** Injected distributed task session */
		@TaskSessionResource
		private ComputeTaskSession jobSes;
		
		/** Injected Ignite Logger */
		@LoggerResource
		private IgniteLogger log;
		
		private IgniteBiTuple<Integer, Integer> state;
		
		private String phrase;

		/**
         * The job will check the checkpoint with key '{@code fail}' and if
         * it's {@code true} it will throw exception to simulate a failure.
         * Otherwise, it will execute enabled method.
         */
        @Override public Integer apply(String phrase) {
            System.out.println();
            System.out.println(">>> Executing fail-over example job.");

            this.phrase = phrase;

            List<String> words = Arrays.asList(phrase.split(" "));

            final String cpKey = checkpointKey();

            IgniteBiTuple<Integer, Integer> state = jobSes.loadCheckpoint(cpKey);

            int idx = 0;
            int sum = 0;

            if (state != null) {
                this.state = state;

                // Last processed word index and total length.
                idx = state.get1();
                sum = state.get2();
            }

            for (int i = idx; i < words.size(); i++) {
                sum += words.get(i).length();

                this.state = new IgniteBiTuple<>(i + 1, sum);

                // Save checkpoint with scope of task execution.
                // It will be automatically removed when task completes.
                jobSes.saveCheckpoint(cpKey, this.state);

                // For example purposes, we fail on purpose after first stage.
                // This exception will cause job to be failed over to another node.
                if (i == 0) {
                    System.out.println();
                    System.out.println(">>> Job will be failed over to another node.");

                    throw new ComputeJobFailoverException("Expected example job exception.");
                }
            }

            return sum;
        }

        /**
         * Make reasonably unique checkpoint key.
         *
         * @return Checkpoint key.
         */
        private String checkpointKey() {
            return getClass().getName() + '-' + phrase;
        }
		
	}

}


/**
 * $Log$
 *  
 */
