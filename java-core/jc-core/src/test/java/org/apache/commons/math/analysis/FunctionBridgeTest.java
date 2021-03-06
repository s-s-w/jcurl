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
package org.apache.commons.math.analysis;

import junit.framework.TestCase;

import org.apache.commons.math.FunctionEvaluationException;
import org.jcurl.math.Polynome;
import org.jcurl.math.R1R1Function;

/**
 * Demonstrate how to interact with <a
 * href="http://commons.apache.org/math/">Jakarta Commons Math</a>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class FunctionBridgeTest extends TestCase {

	/**
	 * Enable usage of {@link DifferentiableUnivariateRealFunction}s without
	 * implanting the dependency into the jcurl core.
	 */
	public static abstract class FunctionBridge extends R1R1Function implements
			DifferentiableUnivariateRealFunction {
		private static class Curl2Jakarta extends FunctionBridge {

			private static final long serialVersionUID = -4049534201469372830L;

			private final int diff;

			private final R1R1Function root;

			public Curl2Jakarta(final R1R1Function f, final int diff) {
				root = f;
				this.diff = diff;
			}

			@Override
			public double at(final double x) {
				return root.at(x, diff);
			}

			@Override
			public double at(final double x, final int derivative) {
				return root.at(x, diff + derivative);
			}

			public UnivariateRealFunction derivative() {
				return new Curl2Jakarta(root, diff + 1);
			}

			public double value(final double x)
					throws FunctionEvaluationException {
				try {
					return root.at(x, diff);
				} catch (final RuntimeException e) {
					final FunctionEvaluationException fe = new FunctionEvaluationException(
							x, e);
					fe.initCause(e);
					throw fe;
				}
			}
		}

		private static class Jakarta2Curl extends FunctionBridge {

			private static final long serialVersionUID = -6473767870800545411L;

			private final UnivariateRealFunction base;

			private Jakarta2Curl deriv = null;

			public Jakarta2Curl(final UnivariateRealFunction f) {
				base = f;
			}

			@Override
			public double at(final double x) {
				try {
					return base.value(x);
				} catch (final org.apache.commons.math.MathException e) {
					// new org.jcurl.core.math.FunctionEvaluationException();
					throw new RuntimeException(e);
				}
			}

			@Override
			public double at(final double x, final int derivative) {
				if (derivative < 0)
					// TODO throw new DifferentiationException();
					throw new RuntimeException();
				if (derivative == 0)
					return at(x);
				if (deriv == null)
					deriv = (Jakarta2Curl) derivative();
				if (deriv == null)
					// TODO throw new DifferentiationException();
					throw new RuntimeException();
				return deriv.at(x, derivative - 1);
			}

			public UnivariateRealFunction derivative() {
				if (deriv == null
						&& base instanceof DifferentiableUnivariateRealFunction)
					deriv = new Jakarta2Curl(
							((DifferentiableUnivariateRealFunction) base)
									.derivative());
				return deriv;
			}

			public double value(final double x)
					throws FunctionEvaluationException {
				return base.value(x);
			}
		}

		public static FunctionBridge newInstance(final Object o) {
			if (o instanceof FunctionBridge)
				return (FunctionBridge) o;
			if (o instanceof R1R1Function)
				return new Curl2Jakarta((R1R1Function) o, 0);
			if (o instanceof UnivariateRealFunction)
				return new Jakarta2Curl((UnivariateRealFunction) o);
			return null;
		}
	}

	public void testPoly() throws FunctionEvaluationException {
		final PolynomialFunction p0 = new PolynomialFunction(new double[] { 1,
				0.1, 0.01, 0.001 });
		final Polynome p1 = new Polynome(p0.getCoefficients());
		final FunctionBridge b0 = FunctionBridge.newInstance(p0);
		final FunctionBridge b1 = FunctionBridge.newInstance(p1);
		for (double x = -100; x < 100; x += 0.9) {
			assertEquals(b0.at(x), b0.value(x), 1e-9);
			assertEquals(b1.at(x), b1.value(x), 1e-9);
			assertEquals(b0.at(x), b1.at(x), 1e-9);
			for (int i = 0; i < 100; i++)
				assertEquals(b0.at(x, i), b1.at(x, i), 1e-9);
		}
	}
}
