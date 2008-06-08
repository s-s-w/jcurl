/*
 * jcurl java curling software framework http://www.jcurl.orgCopyright (C)
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

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Helper for convenient approximated Java2D drawing of arbitratry curves with
 * at least 2 dimensions.
 * 
 * @see Shaper
 * @see Shapeable
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class ShaperUtils {

	private static final Log log = JCLoggerFactory.getLogger(ShaperUtils.class);

	/**
	 * Turn a {@link R1RNFunction} (of at least 2 dimensions) into a
	 * {@link Shape} with the given number of samples and cubic Bezier curves
	 * {@link GeneralPath#quadTo(float, float, float, float)} to interpolate
	 * between.
	 * 
	 * @param src
	 *            the (2-dimensional) curve. Higher dimensions are ignored.
	 * @param min
	 *            the min input <code>t</code> to
	 *            {@link R1RNFunction#at(int, int, double)}
	 * @param max
	 *            the max input <code>t</code> to
	 *            {@link R1RNFunction#at(int, int, double)}
	 * @param samples
	 *            the number of samples (start + stop + intermediate) - must be
	 *            &gt;= 2 (start + stop + intermediate).
	 * @param zoom
	 *            graphics zoom factor (typically 1)
	 * @param ip
	 *            the {@link Interpolator} to get the intermediate sample
	 *            <code>t</code> values.
	 * @see #curveTo(R1RNFunction, double, double, GeneralPath, float)
	 */
	public static Shape approximateCubic(final R1RNFunction src,
			final double min, final double max, final int samples,
			final float zoom, final Interpolator ip) {
		// setup
		if (samples < 2)
			throw new IllegalArgumentException(
					"Give me at least 2 (start + stop)");
		final float d = (float) (max - min);
		final GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO,
				3 * (samples + 1)); // +1 just to be sure...
		// start
		final float x = (float) src.at(0, 0, min);
		final float y = (float) src.at(1, 0, min);
		gp.moveTo(zoom * x, zoom * y);

		double told = min;
		// intermediate
		final int n = samples - 1;
		for (int i = 1; i < n; i++) {
			final double t = min + d * ip.interpolate((float) i / n);
			curveTo(src, told, t, gp, zoom);
			told = t;
		}

		// stop
		curveTo(src, told, max, gp, zoom);
		return gp;
	}

	/**
	 * Turn a {@link R1RNFunction} (of at least 2 dimensions) into a
	 * {@link Shape} with the given number of samples and straight lines
	 * {@link GeneralPath#lineTo(float, float)} to interpolate between.
	 * 
	 * @param src
	 *            the (2-dimensional) curve. Higher dimensions are ignored.
	 * @param min
	 *            the min input <code>t</code> to
	 *            {@link R1RNFunction#at(int, int, double)}
	 * @param max
	 *            the max input <code>t</code> to
	 *            {@link R1RNFunction#at(int, int, double)}
	 * @param samples
	 *            the number of samples (start + stop + intermediate) - must be
	 *            &gt;= 2 (start + stop + intermediate).
	 * @param zoom
	 *            graphics zoom factor (typically 1)
	 * @param ip
	 *            the {@link Interpolator} to get the intermediate sample
	 *            <code>t</code> values.
	 */
	public static Shape approximateLinear(final R1RNFunction src,
			final double min, final double max, final int samples,
			final float zoom, final Interpolator ip) {
		// setup
		if (samples < 2)
			throw new IllegalArgumentException(
					"Give me at least 2 (start + stop)");
		final float d = (float) (max - min);
		final GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO,
				samples + 1); // +1 just to be sure...
		// start
		final float x = (float) src.at(0, 0, min);
		final float y = (float) src.at(1, 0, min);
		gp.moveTo(zoom * x, zoom * y);

		// intermediate
		final int n = samples - 1;
		for (int i = 1; i < n; i++) {
			final double t = min + d * ip.interpolate((float) i / n);
			lineTo(src, t, gp, zoom);
		}

		// stop
		lineTo(src, max, gp, zoom);
		return gp;
	}

	/**
	 * Turn a {@link R1RNFunction} (of at least 2 dimensions) into a
	 * {@link Shape} with the given number of samples and quadratic Bezier
	 * curves {@link GeneralPath#quadTo(float, float, float, float)} to
	 * interpolate between.
	 * 
	 * @param src
	 *            the (2-dimensional) curve. Higher dimensions are ignored.
	 * @param min
	 *            the min input <code>t</code> to
	 *            {@link R1RNFunction#at(int, int, double)}
	 * @param max
	 *            the max input <code>t</code> to
	 *            {@link R1RNFunction#at(int, int, double)}
	 * @param samples
	 *            the number of samples (start + stop + intermediate) - must be
	 *            &gt;= 2 (start + stop + intermediate).
	 * @param zoom
	 *            graphics zoom factor (typically 1)
	 * @param ip
	 *            the {@link Interpolator} to get the intermediate sample
	 *            <code>t</code> values.
	 */
	public static Shape approximateQuadratic(final R1RNFunction src,
			final double min, final double max, final int samples,
			final float zoom, final Interpolator ip) {
		// setup
		if (samples < 2)
			throw new IllegalArgumentException(
					"Give me at least 2 (start + stop)");
		final float d = (float) (max - min);
		final GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO,
				2 * (samples + 1)); // +1 just to be sure...
		// start
		final float x = (float) src.at(0, 0, min);
		final float y = (float) src.at(1, 0, min);
		gp.moveTo(zoom * x, zoom * y);

		double told = min;
		// intermediate
		final int n = samples - 1;
		for (int i = 1; i < n; i++) {
			final double t = min + d * ip.interpolate((float) i / n);
			quadTo(src, told, t, gp, zoom);
			told = t;
		}

		// stop
		quadTo(src, told, max, gp, zoom);
		return gp;
	}

	/**
	 * Cubic Bezier Curve.
	 * 
	 * TODO re-use endpoint location and velocity.
	 * 
	 * Maxima Solution:
	 * 
	 * <pre>
	 * 	radsubstflag: true$
	 * 	k1_0 = k0_0 + l * v0_0;
	 * 	k1_1 = k0_1 + l * v0_1;
	 * 	k2_0 = k3_0 - n * v3_0;
	 * 	k2_1 = k3_1 - n * v3_1;
	 * 	l/n=a/c;
	 * 	((k2_0 - k1_0)*(k2_0 - k1_0) + (k2_1 - k1_1)*(k2_1 - k1_1)) / (n*n) = b*b / (c*c);
	 * 	solve([%o2, %o3, %o4, %o5, %o6, %o7],[k1_0, k1_1, k2_0, k2_1, l, n]);
	 * 	factor(%);
	 * 	ratsimp(%);
	 * 	ratsubst(V0, v0_1&circ;2+v0_0&circ;2, %);
	 * 	ratsubst(V3, v3_1&circ;2+v3_0&circ;2, %);
	 * 	ratsubst(A, k0_1-k3_1, %);
	 * 	ratsubst(B, k0_0-k3_0, %);
	 * 	ratsubst(T, 2*a*c*v0_0*v3_0+a&circ;2*v0_1&circ;2+a&circ;2*v0_0&circ;2-b&circ;2, %);
	 * 	ratsubst(Q, c&circ;2*V3+a&circ;2*V0+T+2*a*c*v0_1*v3_1-a&circ;2*v0_1&circ;2-a&circ;2*v0_0&circ;2, %);
	 * 	ratsubst(W, B&circ;2*T+B&circ;2*(b&circ;2-Q)+c&circ;2*(v3_0&circ;2*B&circ;2-v3_0&circ;2*A&circ;2)-a&circ;2*v0_1&circ;2*B&circ;2+v3_1*(2*c&circ;2*v3_0*A*B
	 * 	+2*a*c*v0_0*A*B)+v0_1*(2*a*c*v3_0*A*B+2*a&circ;2*v0_0*A*B)-2*a*c*v0_0*v3_0*A&circ;2-a&circ;2*v0_0&circ;2*A&circ;2
	 * 	+b&circ;2*A&circ;2, %);
	 * 	expand(%);
	 * 	factor(%);
	 * 	ratsubst(R, c*v3_0*B+a*v0_0*B+c*v3_1*A+a*v0_1*A, %);
	 * 	
	 * </pre>
	 */
	private static void curveTo(final R1RNFunction f, final double tmin,
			final double tmax, final GeneralPath gp, final float zoom) {
		final double eps = 1e-6;

		// first control point (startpoint). The same as gp.getCurrentPoint()
		final double k0_0 = f.at(0, 0, tmin);
		final double k0_1 = f.at(1, 0, tmin);
		// normalized startpoint velocity
		double v0_0 = f.at(0, 1, tmin);
		double v0_1 = f.at(1, 1, tmin);
		if (v0_0 * v0_0 + v0_1 * v0_1 < eps) {
			v0_0 = f.at(0, 1, tmin + eps);
			v0_1 = f.at(1, 1, tmin + eps);
		}
		double v = Math.sqrt(v0_0 * v0_0 + v0_1 * v0_1);
		v0_0 /= v;
		v0_1 /= v;

		// 4th control point (endpoint).
		final double k3_0 = f.at(0, 0, tmax);
		final double k3_1 = f.at(1, 0, tmax);
		// normalized endpoint velocity
		double v3_0 = f.at(0, 1, tmax);
		double v3_1 = f.at(1, 1, tmax);
		if (v3_0 * v3_0 + v3_1 * v3_1 < eps) {
			v3_0 = f.at(0, 1, tmax - eps);
			v3_1 = f.at(1, 1, tmax - eps);
		}
		v = Math.sqrt(v3_0 * v3_0 + v3_1 * v3_1);
		v3_0 /= v;
		v3_1 /= v;

		final double a = 3;
		final double b = 2;
		final double c = 1;
		final double V0 = v0_1 * v0_1 + v0_0 * v0_0;
		final double V3 = v3_1 * v3_1 + v3_0 * v3_0;
		final double A = k0_1 - k3_1;
		final double B = k0_0 - k3_0;
		final double T = 2 * a * c * v0_0 * v3_0 + a * a * v0_1 * v0_1 + a * a
				* v0_0 * v0_0 - b * b;
		final double Q = c * c * V3 + a * a * V0 + T + 2 * a * c * v0_1 * v3_1
				- a * a * v0_1 * v0_1 - a * a * v0_0 * v0_0;
		final double W = B * B * T + B * B * (b * b - Q) + c * c
				* (v3_0 * v3_0 * B * B - v3_0 * v3_0 * A * A) - a * a * v0_1
				* v0_1 * B * B + v3_1 * 2 * c * c * v3_0 * A * B * +2 * a * c
				* v0_0 * A * B + v0_1
				* (2 * a * c * v3_0 * A * B + 2 * a * a * v0_0 * A * B) - 2 * a
				* c * v0_0 * v3_0 * A * A - a * a * v0_0 * v0_0 * A * A + b * b
				* A * A;
		final double R = c * v3_0 * B + a * v0_0 * B + c * v3_1 * A + a * v0_1
				* A;

		if (W < 0) {
			if (log.isDebugEnabled()) {
				log.debug("Arithmetic trouble:");
				log.debug("v0=(" + v0_0 + ", " + v0_1 + ")");
				log.debug("v3=(" + v3_0 + ", " + v3_1 + ")");
				log.debug("V0=" + V0);
				log.debug("V3=" + V3);
				log.debug("A=" + A);
				log.debug("B=" + B);
				log.debug("T=" + T);
				log.debug("Q=" + Q);
				log.debug("W=" + W);
				log.debug("R=" + R);
			}
			gp.moveTo((float) k3_0, (float) k3_1);
			return;
		}

		final double l, n;
		if (true) {
			l = -a * (Math.sqrt(W) + R) / Q;
			n = -c * (Math.sqrt(W) + R) / Q;
		} else {
			l = a * (Math.sqrt(W) - R) / Q;
			n = c * (Math.sqrt(W) - R) / Q;
		}
		if (Double.isNaN(l) || Double.isNaN(n)) {
			log.warn("v0=(" + v0_0 + ", " + v0_1 + ")");
			log.warn("v3=(" + v3_0 + ", " + v3_1 + ")");
			log.warn("V0=" + V0);
			log.warn("V3=" + V3);
			log.warn("A=" + A);
			log.warn("B=" + B);
			log.warn("T=" + T);
			log.warn("Q=" + Q);
			log.warn("W=" + W);
			log.warn("R=" + R);
		}

		final float k1_0 = (float) (k0_0 + l * v0_0);
		final float k1_1 = (float) (k0_1 + l * v0_1);
		final float k2_0 = (float) (k3_0 - n * v3_0);
		final float k2_1 = (float) (k3_1 - n * v3_1);
		// TODO zoom
		if (log.isDebugEnabled())
			log.debug("(" + k1_0 + ", " + k1_1 + "), (" + k2_0 + ", " + k2_1
					+ "), (" + (float) k3_0 + ", " + (float) k3_1 + ")");
		gp.curveTo(k1_0, k1_1, k2_0, k2_1, (float) k3_0, (float) k3_1);
	}

	private static float interpolate(final float min, final float max,
			final float t, final Interpolator ip) {
		final float d = max - min;
		return min + d * ip.interpolate((t - min) / d);
	}

	private static final void lineTo(final R1RNFunction f, final double t,
			final GeneralPath gp, final float zoom) {
		final float x = (float) f.at(0, 0, t);
		final float y = (float) f.at(1, 0, t);
		gp.lineTo(zoom * x, zoom * y);
	}

	/**
	 * TODO re-use endpoint location and velocity.
	 * 
	 * Maxima solution:
	 * 
	 * <pre>
	 * k0_0 + l * v0_0 = k2_0 + m * v2_0;
	 * k0_1 + l * v0_1 = k2_1 + m * v2_1;
	 * solve([%o1,%o2],[k,l]);
	 * subst(q, v0_1 * v2_0 - v0_0 * v2_1, %);
	 * subst(dx_0 + k0_0, k2_0, %);
	 * subst(dx_1 + k0_1, k2_1, %);
	 * ratsimp(%);
	 * </pre>
	 * 
	 * @param f
	 * @param tmin
	 * @param tmax
	 * @param gp
	 * @param zoom
	 */
	private static final void quadTo(final R1RNFunction f, final double tmin,
			final double tmax, final GeneralPath gp, final float zoom) {
		final double eps = 1e-6;

		// first control point (startpoint). The same as gp.getCurrentPoint()
		final double k0_0 = f.at(0, 0, tmin);
		final double k0_1 = f.at(1, 0, tmin);
		// startpoint velocity
		double v0_0 = f.at(0, 1, tmin);
		double v0_1 = f.at(1, 1, tmin);
		if (v0_0 * v0_0 + v0_1 * v0_1 < eps) {
			v0_0 = f.at(0, 1, tmin + eps);
			v0_1 = f.at(1, 1, tmin + eps);
		}

		// 3rd control point (endpoint).
		final double k2_0 = f.at(0, 0, tmax);
		final double k2_1 = f.at(1, 0, tmax);
		// endpoint velocity
		double v2_0 = f.at(0, 1, tmax);
		double v2_1 = f.at(1, 1, tmax);
		if (v2_0 * v2_0 + v2_1 * v2_1 < eps) {
			v2_0 = f.at(0, 1, tmax - eps);
			v2_1 = f.at(1, 1, tmax - eps);
		}

		// compute the 2nd control point
		final double dx_0 = k2_0 - k0_0;
		final double dx_1 = k2_1 - k0_1;
		final double q = v0_1 * v2_0 - v0_0 * v2_1;
		final double m = -(dx_0 * v0_1 - dx_1 * v0_0) / q;

		// 2nd control point is
		final float k1_0 = (float) (k2_0 + m * v2_0);
		final float k1_1 = (float) (k2_1 + m * v2_1);

		if (true)
			gp.quadTo(zoom * k1_0, zoom * k1_1, zoom * (float) k2_0, zoom
					* (float) k2_1);
		else {
			gp.lineTo(zoom * k1_0, zoom * k1_1);
			gp.lineTo(zoom * (float) k2_0, zoom * (float) k2_1);
		}
	}

	static String toString(final double[] arr) {
		final StringBuilder w = new StringBuilder();
		if (arr == null)
			w.append("null");
		else {
			boolean start = true;
			w.append("[");
			for (final double element : arr) {
				if (!start)
					w.append(" ");
				w.append(Double.toString(element));
				start = false;
			}
			w.append("]");
		}
		return w.toString();
	}
}
