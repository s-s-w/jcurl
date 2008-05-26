/*
 * jcurl java curling software framework http://www.jcurl.orgCopyright (C) 2005-2008 M. Rohrmoser
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
	 * Split the given interval [min,max] into equidistant sections.
	 * 
	 * @param min
	 * @param max
	 * @param sections
	 * @return filled <code>sections</code> array.
	 */
	public static double[] aequidistantSections(double min, double max,
			final double[] sections) {
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

	public static Shape approximate(final R1RNFunction c,
			final double[] sections) {
		// return approximateLinear(c, sections);
		return approximateQuadratic(c, sections);
	}

	public static Shape approximateLinear(final R1RNFunction c,
			final double[] sections) {
		return approximateLinear(c, sections, 1, null);
	}

	/**
	 * Turns the first 2 dimensions of a {@link R1RNFunction} into a drawable
	 * {@link Shape}.
	 * 
	 * @param c
	 * @param sections
	 * @param zoom
	 *            factor - typically 1
	 * @param tmp
	 *            save instanciations calling
	 *            {@link R1RNFunction#at(int, double, double[])}.
	 * @return the shape.
	 */
	public static Shape approximateLinear(final R1RNFunction c,
			final double[] sections, final float zoom, double[] tmp) {
		if (tmp == null)
			tmp = new double[c.dim()];
		c.at(0, sections[0], tmp);
		final GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO,
				sections.length + 1);
		gp.moveTo((float) (zoom * tmp[0]), (float) (zoom * tmp[1]));
		for (int i = 1; i < sections.length; i++) {
			c.at(0, sections[i], tmp);
			gp.lineTo((float) (zoom * tmp[0]), (float) (zoom * tmp[1]));
		}
		return gp;
	}

	public static Shape approximateQuadratic(final R1RNFunction c,
			final double[] sections) {
		return approximateQuadratic(c, sections, 1, null, null, null, null);
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

	public static double[] exponentialSections(double min, double max,
			final double[] sections) {
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
		long j = 0;
		for (int i = n; i > 0; i--)
			j += i;
		int k = 0;
		for (int i = 1; i < n; i++) {
			k += n - i + 1;
			log.debug(k + "/" + j);
			sections[i] = min + (max - min) * k / j;
		}
		return null;
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