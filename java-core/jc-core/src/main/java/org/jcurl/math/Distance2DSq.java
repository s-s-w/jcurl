/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
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
package org.jcurl.math;

/**
 * The distance between two {@link R1RNFunction}s - only 2 dimensions used.
 * 
 * Differentiable min. 1x.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class Distance2DSq extends R1R1Function {

	private static final long serialVersionUID = 1239814260738123868L;
	private static final boolean threadSafe = true;
	private final int c;
	private final R1RNFunction c1;
	private final R1RNFunction c2;
	/** (r1+r2)^2 */
	private final double r2;
	private final double[] tmpA;
	private final double[] tmpB;

	/**
	 * Distance between two (2-dimensional) spheres moving along curve
	 * <code>c1</code> and curve <code>c2</code>, having radii
	 * <code>r1</code> and <code>r2</code>.
	 * 
	 * @param c1
	 * @param r1
	 * @param c2
	 * @param r2
	 */
	Distance2DSq(final R1RNFunction c1, final double r1, final R1RNFunction c2,
			final double r2) {
		this(c1, c2, MathVec.sqr(r1 + r2));
	}

	/**
	 * Distance between two (2-dimensional) spheres moving along curve
	 * <code>c1</code> and curve <code>c2</code>, having the square sum of
	 * radii <code>r12Sqr</code>.
	 * 
	 * @param c1
	 * @param c2
	 * @param r12Sqr
	 *            <code>(r1+r2)^2</code>
	 */
	public Distance2DSq(final R1RNFunction c1, final R1RNFunction c2,
			final double r12Sqr) {
		this(c1, c2, r12Sqr, 0);
	}

	/**
	 * Distance between two (2-dimensional) spheres moving along curve
	 * <code>c1</code> and curve <code>c2</code>, having the square sum of
	 * radii <code>r12Sqr</code>.
	 * 
	 * @param c1
	 * @param c2
	 * @param r12Sqr
	 *            <code>(r1+r2)^2</code>
	 * @param c
	 *            derivative of c1 and c2 to operate on. Usually <code>0</code>
	 */
	public Distance2DSq(final R1RNFunction c1, final R1RNFunction c2,
			final double r12Sqr, final int c) {
		if (c1.dim() != c2.dim())
			throw new IllegalArgumentException("Dimension mismatch: "
					+ c1.dim() + "!=" + c2.dim());
		this.c1 = c1;
		this.c2 = c2;
		r2 = r12Sqr;
		this.c = c;
		if (threadSafe)
			tmpA = tmpB = null;
		else {
			tmpA = new double[3];
			tmpB = new double[3];
		}

	}

	/**
	 * <code>(c1(t) - c2(t))^2 - (r1 + r2)^2</code>.
	 * 
	 * @param t
	 * @return the value
	 */
	@Override
	public double at(final double t) {
		if (false) {
			// TUNE don't use double[] at all.
			final double[] a = c1.at(c, t, tmpA);
			final double[] b = c2.at(c, t, tmpB);
			a[0] -= b[0];
			a[1] -= b[1];
			a[2] = 0;
			return MathVec.scal(a, a) - r2;
		} else {
			final double x = c1.at(0, c, t) - c2.at(0, c, t);
			final double y = c1.at(1, c, t) - c2.at(1, c, t);
			return x * x + y * y - r2;
		}
	}

	/**
	 * @param derivative
	 * @param t
	 * @return the value
	 * @see #at(double)
	 * @see #atC1(double)
	 */
	@Override
	public double at(final int derivative, final double t) {
		if (derivative == 0)
			return at(t);
		if (derivative == 1)
			return atC1(t);
		throw new UnsupportedOperationException();
	}

	/**
	 * <code>2 * (c1 - c2) * (c1' - c2')</code> Feed into maxima:
	 * 
	 * <pre>
	 *       a(t) := [ ax(t), ay(t) ];
	 *       b(t) := [ bx(t), by(t) ];
	 *       d(t) := (a(t) - b(t)) . (a(t) - b(t));
	 *       diff(d(t), t);
	 *       quit$
	 * </pre>
	 */
	double atC1(final double t) {
		// TUNE don't use double[] at all.
		double ret = 0.0;
		if (false) {
			// TUNE Thread safety at the cost of 4 instanciations
			final double[] a = c1.at(c, t, new double[3]);
			final double[] b = c2.at(c, t, new double[3]);
			final double[] da = c1.at(c + 1, t, new double[3]);
			final double[] db = c2.at(c + 1, t, new double[3]);
			ret += (a[0] - b[0]) * (da[0] - db[0]);
			ret += (a[1] - b[1]) * (da[1] - db[1]);
		} else if (false) {
			// TUNE Thread safety at the cost of 2 instanciations
			final double[] a = c1.at(c, t, tmpA);
			final double[] b = c2.at(c, t, tmpB);
			final double a_minus_b_0 = a[0] - b[0];
			final double a_minus_b_1 = a[1] - b[1];
			final double[] da = c1.at(c + 1, t, a);
			final double[] db = c2.at(c + 1, t, b);
			final double da_minus_db_0 = da[0] - db[0];
			final double da_minus_db_1 = da[1] - db[1];

			ret += a_minus_b_0 * da_minus_db_0;
			ret += a_minus_b_1 * da_minus_db_1;
		} else {
			final double a_minus_b_0 = c1.at(0, c, t) - c2.at(0, c, t);
			final double a_minus_b_1 = c1.at(1, c, t) - c2.at(1, c, t);
			final double da_minus_db_0 = c1.at(0, c + 1, t)
					- c2.at(0, c + 1, t);
			final double da_minus_db_1 = c1.at(1, c + 1, t)
					- c2.at(1, c + 1, t);

			ret += a_minus_b_0 * da_minus_db_0;
			ret += a_minus_b_1 * da_minus_db_1;
		}
		return 2.0 * ret;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "(" + c1 + ", " + c2 + ")";
	}
}