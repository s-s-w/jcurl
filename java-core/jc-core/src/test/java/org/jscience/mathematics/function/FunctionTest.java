/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
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

package org.jscience.mathematics.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javolution.context.ConcurrentContext;
import javolution.text.Text;
import junit.framework.TestCase;

import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockType;
import org.jcurl.core.api.RockType.Pos;
import org.jscience.mathematics.number.Float64;

/**
 * Demonstrate how to interact with <a href="http://www.jscience.org">JScience</a>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class FunctionTest extends TestCase {

	/**
	 * Wrap a {@link RockFunction} into a {@link CurveRock}.
	 * 
	 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
	 * @version $Id$
	 */
	static class CurveRockJScience<T extends RockType> extends CurveRock<T> {
		private static final Variable.Local<Float64> localT = new Variable.Local<Float64>(
				VAR_T);

		private static final long serialVersionUID = -1938366869381933449L;
		private transient Function<Float64, Rock<T>> c1 = null;
		private transient Function<Float64, Rock<T>> c2 = null;
		private final RockFunction<Float64, T> f;

		public CurveRockJScience(final RockFunction<Float64, T> f) {
			this.f = f;
		}

		@Override
		public Rock<T> at(final int c, final double t, final Rock<T> ret) {
			final Float64 tt = Float64.valueOf(t);
			final Rock<T> r;
			switch (c) {
			case 0:
				r = f.evaluate(tt);
				break;
			case 1:
				if (c1 == null)
					c1 = f.differentiate(localT);
				r = c1.evaluate(tt);
				break;
			case 2:
				if (c1 == null)
					c1 = f.differentiate(localT);
				if (c2 == null)
					c2 = c1.differentiate(localT);
				r = c2.evaluate(tt);
				break;
			default:
				throw new IllegalArgumentException();
			}
			if (ret == null)
				return r;
			ret.setLocation(r);
			return ret;
		}

		@Override
		public double at(final int dim, final int c, final double t) {
			final Rock<T> r = at(c, t, (Rock<T>) null);
			switch (dim) {
			case 0:
				return r.getX();
			case 1:
				return r.getY();
			case 2:
				return r.getA();
			}
			throw new IllegalArgumentException();
		}

		@Override
		public String toString() {
			return f.toString();
		}
	}

	/**
	 * A JScience compliant function providing {@link Rock} results. Simple demo
	 * requiring the 3 dimensions to be functions of {@link FunctionTest#VAR_T}
	 * and being independent from each other.
	 */
	static class RockFunction<T extends Number, R extends RockType> extends
			Function<T, Rock<R>> {
		private class Differentiator implements Runnable {
			private final Function<T, T> f;
			Function<T, T> result = null;

			Differentiator(final Function<T, T> f) {
				this.f = f;
			}

			public void run() {
				result = f.differentiate(locT);
			}
		}

		private class Evaluator implements Runnable {
			private final Function<T, T> f;
			T result = null;

			Evaluator(final Function<T, T> f) {
				this.f = f;
			}

			public void run() {
				result = f.evaluate(getVariable(VAR_T).get());
			}
		}

		private static final long serialVersionUID = -5371903330578728822L;

		private final Function<T, T> angle;

		private transient RockFunction<T, R> c1 = null;
		private transient final Differentiator d_angle;
		private transient final Differentiator d_x;
		private transient final Differentiator d_y;
		private transient final Evaluator e_angle;
		private transient final Evaluator e_x;
		private transient final Evaluator e_y;
		private final Variable.Local<T> locT = new Variable.Local<T>(VAR_T);
		private transient final List<Variable<T>> vars;
		private final Function<T, T> x;
		private final Function<T, T> y;

		public RockFunction(final Function<T, T> x, final Function<T, T> y,
				final Function<T, T> angle) {
			if (false)
				if (x.getVariable(VAR_T) == null
						|| y.getVariable(VAR_T) == null
						|| angle.getVariable(VAR_T) == null)
					throw new IllegalArgumentException("need variable \""
							+ VAR_T + "\"");
			this.e_x = new Evaluator(this.x = x);
			this.e_y = new Evaluator(this.y = y);
			this.e_angle = new Evaluator(this.angle = angle);
			// TODO do the differentiation once and for all?
			this.d_x = new Differentiator(this.x);
			this.d_y = new Differentiator(this.y);
			this.d_angle = new Differentiator(this.angle);
			// TODO rather inspect the really existing Variables?
			final Variable<T> t = new Variable.Local<T>(VAR_T);
			final List<Variable<T>> l = new ArrayList<Variable<T>>();
			l.add(t);
			vars = Collections.unmodifiableList(l);
		}

		/**
		 * Use a {@link ConcurrentContext} to parallelize the 3 dimensional
		 * differentiation. Maybe this is not worth while as it's only done once
		 * and buffered for later use.
		 */
		@Override
		public Function<T, Rock<R>> differentiate(final Variable<T> v) {
			if (c1 == null) {
				if (!"t".equals(v.getSymbol()))
					throw new IllegalArgumentException();
				ConcurrentContext.enter();
				try {
					ConcurrentContext.execute(d_x);
					ConcurrentContext.execute(d_y);
					ConcurrentContext.execute(d_angle);
				} finally {
					ConcurrentContext.exit(); // Waits for all concurrent
					// threads
					// to complete.
				}
				c1 = new RockFunction<T, R>(d_x.result, d_y.result,
						d_angle.result);
			}
			return c1;
		}

		/**
		 * Use a {@link ConcurrentContext} to parallelize the 3 dimensional
		 * computation and create a new {@link RockDouble} instance to return.
		 */
		@Override
		public Rock<R> evaluate() {
			ConcurrentContext.enter();
			try {
				ConcurrentContext.execute(e_x);
				ConcurrentContext.execute(e_y);
				ConcurrentContext.execute(e_angle);
			} finally {
				ConcurrentContext.exit(); // Waits for all concurrent threads
				// to complete.
			}
			return new RockDouble<R>(e_x.result.doubleValue(), e_y.result
					.doubleValue(), e_angle.result.doubleValue());
		}

		@Override
		public List<Variable<T>> getVariables() {
			return vars;
		}

		@Override
		public Text toText() {
			return new Text("[" + x + ", " + y + ", " + angle + "]");
		}
	}

	private static class SimpleFunction<T extends RockType> extends
			Function<Double, Rock<T>> {
		private static final long serialVersionUID = -3416921495780500242L;
		private final List<Variable<Double>> vars;

		public SimpleFunction() {
			final Variable<Double> v = new Variable.Local<Double>(VAR_T);
			final List<Variable<Double>> l = new ArrayList<Variable<Double>>();
			l.add(v);
			vars = Collections.unmodifiableList(l);
		}

		@Override
		public Rock<T> evaluate() {
			final Variable<Double> tv = getVariable(VAR_T);
			final double t = tv.get();
			return new RockDouble<T>(1 * t, 2 * t, 3 * t);
		}

		@Override
		public List<Variable<Double>> getVariables() {
			return vars;
		}

		@Override
		public Text toText() {
			return new Text("Hello, world!");
		}
	}

	private static final String VAR_T = "t";

	public void testCurveRockJScience() {
		final Variable<Float64> varT = new Variable.Local<Float64>("t");
		final Polynomial<Float64> x = Polynomial.valueOf(Float64.ONE, varT);
		final CurveRockJScience<Pos> f = new CurveRockJScience<Pos>(
				new RockFunction<Float64, Pos>(x, x, x));

		assertEquals("[[1.0]t, [1.0]t, [1.0]t]", f.toString());
		final Rock<Pos> r = f.at(0, 2, (Rock<Pos>) null);
		assertEquals("[2.0, 2.0, 2.0]", r.toString());

		final int loops = 100000;
		{
			final long start = System.currentTimeMillis();
			for (int i = loops - 1; i >= 0; i--)
				f.at(0, 2, (Rock<Pos>) null);
			final long stop = System.currentTimeMillis() - start;
			System.out.println(loops + " loops took " + stop
					+ " millis (wrapped 3D jscience function evaluation)");
		}
	}

	/**
	 * That's amazing! Symbolic differentiation AND integration! (Ok,
	 * polynomials are an easy start, but anyway!) Jean-Marie, you're a stud!
	 */
	public void testDifferentiate() {
		final Variable<Float64> t = new Variable.Local<Float64>(VAR_T);
		final Polynomial<Float64> p = Polynomial.valueOf(Float64.valueOf(1.5),
				t);
		assertEquals("[1.5]t", p.toString());
		final Function<Float64, Float64> f = p.pow(3);
		assertEquals("[3.375]t³", f.toString());
		final Function<Float64, Float64> c1 = f.differentiate(t);
		assertEquals("[10.125]t²", c1.toString());
		final Function<Float64, Float64> ic1 = c1.integrate(t);
		assertEquals("[3.375]t³", ic1.toString());
	}

	public void testRock() {
		final Variable<Float64> varT = new Variable.Local<Float64>(VAR_T);
		final Polynomial<Float64> x = Polynomial.valueOf(Float64.ONE, varT);
		final RockFunction<Float64, Pos> f = new RockFunction<Float64, Pos>(x, x,
				x);
		assertEquals("[[1.0]t, [1.0]t, [1.0]t]", f.toString());
		final Rock<Pos> r = f.evaluate(Float64.valueOf(2));
		assertEquals("[2.0, 2.0, 2.0]", r.toString());

		final int loops = 100000;
		{
			final long start = System.currentTimeMillis();
			for (int i = loops - 1; i >= 0; i--)
				f.evaluate(Float64.valueOf(i));
			final long stop = System.currentTimeMillis() - start;
			System.out.println(loops + " loops took " + stop
					+ " millis (3D jscience function evaluation)");
		}
	}

	public void testSimple() {
		final Function<Double, Rock<Pos>> f = new SimpleFunction<Pos>();
		assertEquals("Hello, world!", f.toString());
		final Rock<Pos> r = f.evaluate(-1.0);
		assertEquals("[-1.0, -2.0, -3.0]", r.toString());
		assertEquals("d[Hello, world!]/dt", f.differentiate(
				new Variable.Local<Double>(VAR_T)).toString());

		final Function<Double, Rock<Pos>> f2 = f.pow(2);
		assertEquals("(Hello, world!)·(Hello, world!)", f2.toString());
		try {
			final Rock<Pos> r2 = f2.evaluate(-1.0);
			fail();
		} catch (final FunctionException e) {}
	}
}
