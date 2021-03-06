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
package org.jcurl.core.impl;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.ObjectStreamException;

import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockType;
import org.jcurl.math.MathVec;
import org.jcurl.math.R1RNFunction;

/**
 * Decorator to apply an rc -&gt; wc {@link AffineTransform} and a time-shift to
 * a {@link CurveRock}.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:CurveTransformed.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public class CurveTransformed<T extends RockType> extends CurveRock<T> {

	private static final long serialVersionUID = -665772521427597014L;

	/**
	 * Create the transformation from a (Rock Coordinates - rc) System at p0_wc
	 * with positive y axis along v0_wc into World Coordinates (wc).
	 * 
	 * @param ret
	 *            <code>null</code> creates a new one.
	 * @param x0
	 *            (world coordinates)
	 * @param y0
	 *            (world coordinates)
	 * @param vx
	 *            (world coordinates)
	 * @param vy
	 *            (world coordinates)
	 * @return trafo rc -&gt; wc
	 */
	public static AffineTransform createRc2Wc(AffineTransform ret,
			final double x0, final double y0, final double vx, final double vy) {
		if (ret == null)
			ret = new AffineTransform();
		else
			ret.setToIdentity();
		ret.translate(x0, y0);
		MathVec.rotate(ret, vx, vy);
		MathVec.rotate(ret, 0, -1);
		return ret;
	}

	/**
	 * Delegate to
	 * {@link #createRc2Wc(AffineTransform, double, double, double, double)}
	 * 
	 * @param p0_wc
	 * @param v0_wc
	 * @param ret
	 *            <code>null</code> creates a new one.
	 * @return trafo rc -&gt; wc
	 */
	public static AffineTransform createRc2Wc(final Point2D p0_wc,
			final Point2D v0_wc, final AffineTransform ret) {
		return createRc2Wc(ret, p0_wc.getX(), p0_wc.getY(), v0_wc.getX(), v0_wc
				.getY());
	}

	private final R1RNFunction base;

	private final transient double rot;

	private final double t0;

	private final double[] trafo = new double[6];

	private CurveTransformed(final AffineTransform trafo,
			final R1RNFunction base, final double t0) {
		this.t0 = t0;
		if (base instanceof CurveRockAnalytic)
			this.base = ((CurveRockAnalytic) base).getCurve();
		else
			this.base = base;
		trafo.getMatrix(this.trafo);
		rot = Math.atan2(trafo.getShearY(), trafo.getScaleY());
	}

	/**
	 * 
	 * @param base
	 * @param trafo
	 *            See {@link #createRc2Wc(Point2D, Point2D, AffineTransform)}
	 * @param t0
	 */
	public CurveTransformed(final CurveRock<T> base,
			final AffineTransform trafo, final double t0) {
		this(trafo, base, t0);
	}

	@Override
	public double[] at(double t, final int derivative, double[] ret) {
		t -= t0;
		ret = base.at(t, derivative, ret);
		final double x;
		final double y;
		final double a;
		if (derivative < 1) {
			x = trafo[0] * ret[0] + trafo[2] * ret[1] + trafo[4];
			y = trafo[1] * ret[0] + trafo[3] * ret[1] + trafo[5];
			a = ret[2] + rot;
		} else {
			x = trafo[0] * ret[0] + trafo[2] * ret[1];
			y = trafo[1] * ret[0] + trafo[3] * ret[1];
			a = ret[2];
		}
		ret[0] = x;
		ret[1] = y;
		ret[2] = a;
		return ret;
	}

	@Override
	public double at(final double t, final int derivative, final int component) {
		if (false)
			throw new UnsupportedOperationException("Not supported.");
		final double[] tmp = { 0, 0, 0 };
		at(t, derivative, tmp);
		return tmp[component];
	}

	@Override
	public Rock<T> at(double t, final int derivative, Rock<T> ret) {
		t -= t0;
		if (ret == null)
			ret = new RockDouble<T>();
		ret.setLocation(base.at(t, derivative, 0), base.at(t, derivative, 1),
				base.at(t, derivative, 2));
		if (derivative < 1) {
			final double x = trafo[0] * ret.getX() + trafo[2] * ret.getY()
					+ trafo[4];
			final double y = trafo[1] * ret.getX() + trafo[3] * ret.getY()
					+ trafo[5];
			final double z = ret.getA() + rot;
			ret.setLocation(x, y, z);
		} else {
			final double x = trafo[0] * ret.getX() + trafo[2] * ret.getY();
			final double y = trafo[1] * ret.getX() + trafo[3] * ret.getY();
			ret.setLocation(x, y, ret.getA());
		}
		return ret;
	}

	/** Clone the untransformed base curve for testing purposes */
	R1RNFunction getBase() {
		return base;
	}

	/** Clone the trafo for testing purposes */
	AffineTransform getTrafo() {
		return new AffineTransform(trafo);
	}

	protected Object readResolve() throws ObjectStreamException {
		return new CurveTransformed<T>(new AffineTransform(trafo), base, t0);
	}

	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("[");
		for (final double element : trafo)
			b.append(element).append(", ");
		b.setLength(b.length() - 2);
		b.append("] ");
		b.append(base.toString());
		return b.toString();
	}
}
