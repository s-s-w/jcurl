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
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CurveShape {

	private static final Log log = JCLoggerFactory.getLogger(CurveShape.class);

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
		float x = (float) src.at(0, 0, min);
		float y = (float) src.at(1, 0, min);
		gp.moveTo(zoom * x, zoom * y);

		// intermediate
		final int n = samples - 1;
		for (int i = 1; i < n; i++) {
			final double t = min + d * ip.interpolate((float) i / n);
			x = (float) src.at(0, 0, t);
			y = (float) src.at(1, 0, t);
			gp.lineTo(zoom * x, zoom * y);
		}

		// stop
		x = (float) src.at(0, 0, max);
		y = (float) src.at(1, 0, max);
		gp.lineTo(zoom * x, zoom * y);
		return gp;
	}

	/**
	 * Turns the first 2 dimensions of a {@link R1RNFunction} into a drawable
	 * {@link Shape} - namely a {@link GeneralPath}.
	 * 
	 * @param src
	 *            the cuve to turn into a shape.
	 * @param sections
	 *            the samples
	 * @param zoom
	 *            graphics zoom factor - typically 1
	 * @param tmp
	 *            save instanciations calling
	 *            {@link R1RNFunction#at(int, double, double[])}. Should have
	 *            the same dimension as the input {@link R1RNFunction}. May be
	 *            <code>null</code>.
	 * @return the curve segment as a {@link Shape}.
	 * @deprecated Rather use
	 *             {@link #approximateLinear(R1RNFunction, double, double, int, float, Interpolator)}
	 */
	@Deprecated
	public static Shape approximateLinear(final R1RNFunction src,
			final double[] sections, final float zoom, double[] tmp) {
		if (tmp == null)
			tmp = new double[src.dim()];
		src.at(0, sections[0], tmp);
		final GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO,
				sections.length + 1);
		gp.moveTo((float) (zoom * tmp[0]), (float) (zoom * tmp[1]));
		for (int i = 1; i < sections.length; i++) {
			src.at(0, sections[i], tmp);
			gp.lineTo((float) (zoom * tmp[0]), (float) (zoom * tmp[1]));
		}
		return gp;
	}

	/**
	 * Turns the first 2 dimensions of a {@link R1RNFunction} into a drawable
	 * {@link Shape}.
	 * 
	 * @param c
	 * @param sections
	 * @param zoom
	 *            factor - typically 1
	 * @param p0
	 *            save instanciations calling
	 *            {@link R1RNFunction#at(int, double, double[])}.
	 * @param v0
	 *            save instanciations calling
	 *            {@link R1RNFunction#at(int, double, double[])}.
	 * @param p1
	 *            save instanciations calling
	 *            {@link R1RNFunction#at(int, double, double[])}.
	 * @param v1
	 *            save instanciations calling
	 *            {@link R1RNFunction#at(int, double, double[])}.
	 * @return the shape
	 */
	@Deprecated
	public static Shape approximateQuadratic(final R1RNFunction c,
			final double[] sections, final float zoom, double[] p0,
			double[] v0, double[] p1, double[] v1) {
		if (p0 == null)
			p0 = new double[c.dim()];
		if (v0 == null)
			v0 = new double[c.dim()];
		if (p1 == null)
			p1 = new double[c.dim()];
		if (v1 == null)
			v1 = new double[c.dim()];
		c.at(0, sections[0], p0);
		c.at(1, sections[0], v0);
		final GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO,
				sections.length + 1);
		gp.moveTo((float) (zoom * p0[0]), (float) (zoom * p0[1]));
		final double tmp_a[][] = { { 0, 0 }, { 0, 0 } };
		final double tmp_b[] = { 0, 0 };
		final double pc[] = { 0, 0 };
		for (int i = 1; i < sections.length; i++) {
			if (log.isDebugEnabled())
				log.debug("t=" + sections[i]);
			c.at(0, sections[i], p1);
			c.at(1, sections[i], v1);
			CurveShape.computeControlPoint(p0, v0, p1, v1, tmp_a, tmp_b, pc);
			gp.quadTo((float) (zoom * pc[0]), (float) (zoom * pc[1]),
					(float) (zoom * p1[0]), (float) (zoom * p1[1]));
			p0[0] = p1[0];
			p0[1] = p1[1];
			v0[0] = v1[0];
			v0[1] = v1[1];
		}
		return gp;
	}

	/**
	 * Compute the control point for a quadratic bezier spline from pa to pb.
	 * Maxima code:
	 * 
	 * <pre>
	 * NEXTLAYERFACTOR(TRUE)$
	 * DEBUGMODE(TRUE)$ 
	 * 
	 * pa[0] + k * va[0] = pb[0] + l * vb[0];
	 * pa[1] + k * va[1] = pb[1] + l * vb[1];
	 * 
	 * LINSOLVE([%i4, %i5],[k, l]),GLOBALSOLVE:TRUE,BACKSUBST:TRUE$
	 * 
	 * SCSIMP(PART(%o6,1,2)); 
	 * 
	 * quit$
	 * </pre>
	 * 
	 * @param pa
	 *            startpoint
	 * @param va
	 *            incline in startpoint
	 * @param pb
	 *            endpoint
	 * @param vb
	 *            incline in endpoint
	 * @param tmp_matrix
	 *            2x2 matrix for temporary use
	 * @param tmp_right
	 *            2-dimensional vector for temporary use
	 * @param pc
	 *            control point coordinates
	 * @return coordinates of the control point (stored in x)
	 * @see MathVec#gauss(double[][], double[], double[])
	 */
	static double[] computeControlPoint(final double[] pa, final double[] va,
			final double[] pb, final double[] vb, final double[][] tmp_matrix,
			final double[] tmp_right, final double[] pc) {
		if (va[0] == 0.0 && va[1] == 0.0 && vb[0] == 0.0 && vb[1] == 0.0) {
			pc[0] = 0.5 * (pa[0] + pb[0]);
			pc[1] = 0.5 * (pa[1] + pb[1]);
			return pc;
		}
		// final double f = pc[0];
		final double f = (vb[1] * (pa[0] - pb[0]) + vb[0] * (pb[1] - pa[1]))
				/ (vb[0] * va[1] - va[0] * vb[1]);
		pc[0] = pa[0] + f * va[0];
		pc[1] = pa[1] + f * va[1];
		if (log.isDebugEnabled()) {
			final StringBuilder b = new StringBuilder();
			b.append("pa=").append(toString(pa));
			b.append(" va=").append(toString(va));
			b.append(" pb=").append(toString(pb));
			b.append(" vb=").append(toString(vb));
			b.append(" pc=").append(toString(pc));
			log.debug(b.toString());
		}
		return pc;
	}

	@Deprecated
	public static double[] exponentialSections(final double min,
			final double max, final double[] sections) {
		return interpolate(min, max, sections, Interpolators
				.getExponentialInstance());
	}

	@Deprecated
	public static double[] interpolate(final double min, final double max,
			final double[] sections, final Interpolator ip) {
		if (min > max)
			throw new IllegalArgumentException();
		final int n = sections.length - 1;
		if (n < 1)
			throw new IllegalArgumentException();
		sections[0] = min;
		sections[n] = max;
		final float _min = (float) min;
		final float _d = (float) (max - min);
		for (int i = 1; i < n; i++)
			sections[i] = _min + _d * ip.interpolate((float) i / n);
		return sections;
	}

	private static float interpolate(final float min, final float max,
			final float t, final Interpolator ip) {
		final float d = max - min;
		return min + d * ip.interpolate((t - min) / d);
	}

	/**
	 * Split the given interval [min,max] into equidistant sections.
	 * 
	 * @param min
	 * @param max
	 * @param sections
	 * @return filled <code>sections</code> array.
	 */
	@Deprecated
	public static double[] linearSections(double min, double max,
			final double[] sections) {
		if (true)
			return interpolate(min, max, sections, Interpolators
					.getLinearInstance());
		else {
			final int n = sections.length - 1;
			if (n < 0)
				return sections;
			if (min > max) {
				final double tmp = min;
				min = max;
				max = tmp;
			}
			sections[0] = min;
			if (n < 1)
				return sections;
			sections[n] = max;
			if (n < 2)
				return sections;
			final double d = (max - min) / n;
			for (int i = n - 1; i > 0; i--)
				sections[i] = min + i * d;
			return sections;
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