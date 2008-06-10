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
	private final int c;
	private final R1RNFunction c1;
	private final R1RNFunction c2;
	/** (r1+r2)^2 */
	private final double r2;

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
	}

	/**
	 * @param c
	 * @param t
	 * @return the value
	 * @see #atC0(double)
	 * @see #atC1(double)
	 */
	@Override
	public double at(final double t, final int c) {
		if (c == 0)
			return atC0(t);
		if (c == 1)
			return atC1(t);
		throw new UnsupportedOperationException();
	}

	/**
	 * <code>(c1(t) - c2(t))^2 - (r1 + r2)^2</code>.
	 * 
	 * @param t
	 * @return the value
	 */
	protected double atC0(final double t) {
		final double a_minus_b_0 = c1.at(t, c, 0) - c2.at(t, c, 0);
		final double a_minus_b_1 = c1.at(t, c, 1) - c2.at(t, c, 1);
		return a_minus_b_0 * a_minus_b_0 + a_minus_b_1 * a_minus_b_1 - r2;
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
	protected double atC1(final double t) {
		double ret = 0.0;
		final double a_minus_b_0 = c1.at(t, c, 0) - c2.at(t, c, 0);
		final double a_minus_b_1 = c1.at(t, c, 1) - c2.at(t, c, 1);
		final double da_minus_db_0 = c1.at(t, c + 1, 0) - c2.at(t, c + 1, 0);
		final double da_minus_db_1 = c1.at(t, c + 1, 1) - c2.at(t, c + 1, 1);

		ret += a_minus_b_0 * da_minus_db_0;
		ret += a_minus_b_1 * da_minus_db_1;
		return 2.0 * ret;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "(" + c1 + ", " + c2 + ")";
	}
}