package org.jcurl.core.api;

import junit.framework.TestCase;

public class BoxingTest extends TestCase {

	public void testDoubleBoxing() {
		final int loops = 100000000;
		{
			long start = System.currentTimeMillis();
			for (int i = loops - 1; i >= 0; i--) {
				final double d = (double) i;
			}
			long stop = System.currentTimeMillis() - start;
			System.out.println(loops + " loops took " + stop
					+ " millis (double assignment)");
		}
		{
			long start = System.currentTimeMillis();
			for (int i = loops - 1; i >= 0; i--) {
				final Double d = (double) i;
			}
			long stop = System.currentTimeMillis() - start;
			System.out.println(loops + " loops took " + stop
					+ " millis (boxing assignment)");
		}
	}

	public void testDoubleUnBoxing() {
		final Double PI = Double.valueOf(Math.PI);
		final int loops = 100000000;
		{
			long start = System.currentTimeMillis();
			for (int i = loops - 1; i >= 0; i--) {
				final double d = (double) i;
			}
			long stop = System.currentTimeMillis() - start;
			System.out.println(loops + " loops took " + stop
					+ " millis (double assignment)");
		}
		{
			long start = System.currentTimeMillis();
			for (int i = loops - 1; i >= 0; i--) {
				final double d = PI;
			}
			long stop = System.currentTimeMillis() - start;
			System.out.println(loops + " loops took " + stop
					+ " millis (boxing assignment)");
		}
	}
}
