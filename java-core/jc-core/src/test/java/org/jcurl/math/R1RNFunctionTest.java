/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2009 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.math;

import junit.framework.TestCase;

import org.jscience.mathematics.function.Polynomial;
import org.jscience.mathematics.function.Variable;
import org.jscience.mathematics.number.Float64;

public class R1RNFunctionTest extends TestCase {

	private static interface FunctionGeneric<X, Y> {
		Y at(X t);
	}

	public void testGeneric() {
		final R1R1Function trad = new R1R1Function() {
			private static final long serialVersionUID = -2622077818516775396L;

			@Override
			public double at(double x, int c) {
				return x * x;
			}
		};
		final FunctionGeneric<Double, Double> gene = new FunctionGeneric<Double, Double>() {
			public Double at(Double t) {
				return t * t;
			}
		};
		final Variable.Local<Float64> vx = new Variable.Local<Float64>("x");
		final Polynomial<Float64> jsci = Polynomial.valueOf(Float64.ONE, vx)
				.pow(2);

		final long loops = 1000000;
		double x;
		// warm up
		for (long i = loops / 10 - 1; i >= 0; i--) {
			x = trad.at(i * 1.0);
			x = gene.at(i * 1.0);
			x = jsci.evaluate(Float64.valueOf(i * 1.0)).doubleValue();
		}
		long start = System.currentTimeMillis();
		for (long i = loops - 1; i >= 0; i--)
			x = trad.at(i * 1.0);
		System.out.println(loops + " traditional loops: "
				+ (System.currentTimeMillis() - start) + " millis");

		start = System.currentTimeMillis();
		for (long i = loops - 1; i >= 0; i--)
			x = gene.at(i * 1.0);
		System.out.println(loops + " generic     loops: "
				+ (System.currentTimeMillis() - start) + " millis");

		start = System.currentTimeMillis();
		for (long i = loops - 1; i >= 0; i--)
			x = jsci.evaluate(Float64.valueOf(i)).doubleValue();
		System.out.println(loops + " jscience    loops: "
				+ (System.currentTimeMillis() - start) + " millis");
	}

	public void testIsInside() {
		assertTrue(MathVec.isInside(0, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY, true));
		assertTrue(MathVec.isInside(0, Double.POSITIVE_INFINITY,
				Double.NEGATIVE_INFINITY, true));
		assertFalse(MathVec.isInside(Double.NaN, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY, true));
		assertFalse(MathVec.isInside(Double.NaN, Double.POSITIVE_INFINITY,
				Double.NEGATIVE_INFINITY, true));
		assertFalse(MathVec.isInside(Double.POSITIVE_INFINITY,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true));
		assertFalse(MathVec.isInside(Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, true));

		assertTrue(MathVec.isInside(0, Double.NEGATIVE_INFINITY, Double.NaN,
				true));
		assertTrue(MathVec.isInside(0, Double.POSITIVE_INFINITY, Double.NaN,
				true));
		assertTrue(MathVec.isInside(0, Double.NaN, Double.NaN, true));

		assertTrue(MathVec.isInside(0, 0, Double.NEGATIVE_INFINITY, true));
		assertTrue(MathVec.isInside(-1, 0, Double.NEGATIVE_INFINITY, true));
		assertFalse(MathVec.isInside(1, 0, Double.NEGATIVE_INFINITY, true));

		assertFalse(MathVec.isInside(0, 3, 1, true));
		assertTrue(MathVec.isInside(1, 3, 1, true));
		assertTrue(MathVec.isInside(2, 3, 1, true));
		assertTrue(MathVec.isInside(3, 3, 1, true));
		assertFalse(MathVec.isInside(4, 3, 1, true));

		assertTrue(MathVec.isInside(1, 1, 1, true));
	}
}
