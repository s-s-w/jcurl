/*
 * jcurl curling simulation framework Copyright (C) 2005 M. Rohrmoser
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

/**
 * Enable convenient approximated Java2D drawing of arbitratry curves.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CurveShape {

    public static Shape approximate(final R1RNFunction c,
            final double[] sections) {
        // return approximateLinear(c, sections);
        return approximateQuadratic(c, sections);
    }

    public static Shape approximateLinear(final R1RNFunction c,
            final double[] sections) {
        final double[] x_1 = { c.at(0, 0, sections[0]), c.at(1, 0, sections[0]) };
        final double[] x_2 = { 0, 0 };
        final GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO,
                sections.length + 1);
        gp.moveTo((float) x_1[0], (float) x_1[1]);
        for (int i = 1; i < sections.length; i++) {
            x_2[0] = c.at(0, 0, sections[i]);
            x_2[1] = c.at(1, 0, sections[i]);
            gp.lineTo((float) x_2[0], (float) x_2[1]);
            x_1[0] = x_2[0];
            x_1[1] = x_2[1];
        }
        return gp;
    }

    public static Shape approximateQuadratic(final R1RNFunction c,
            final double[] sections) {
        final double[] p0 = { c.at(0, 0, sections[0]), c.at(1, 0, sections[0]) };
        final double[] v0 = { c.at(0, 1, sections[0]), c.at(1, 1, sections[0]) };
        final double[] p1 = { 0, 0 };
        final double[] v1 = { 0, 0 };
        final GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO,
                sections.length + 1);
        gp.moveTo((float) p0[0], (float) p0[1]);
        final double tmp_a[][] = { { 0, 0 }, { 0, 0 } };
        final double tmp_b[] = { 0, 0 };
        final double pc[] = { 0, 0 };
        for (int i = 1; i < sections.length; i++) {
            p1[0] = c.at(0, 0, sections[i]);
            p1[1] = c.at(1, 0, sections[i]);
            v1[0] = c.at(0, 1, sections[i]);
            v1[1] = c.at(1, 1, sections[i]);
            computeControlPoint(p0, v0, p1, v1, tmp_a, tmp_b, pc);
            gp.quadTo((float) pc[0], (float) pc[1], (float) p1[0],
                    (float) p1[1]);
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
     *       NEXTLAYERFACTOR(TRUE)$
     *       DEBUGMODE(TRUE)$ 
     *        
     *       pa[0] + k * va[0] = pb[0] + l * vb[0];
     *       pa[1] + k * va[1] = pb[1] + l * vb[1];
     *        
     *       LINSOLVE([%i4, %i5],[k, l]),GLOBALSOLVE:TRUE,BACKSUBST:TRUE$
     *        
     *       SCSIMP(PART(%o6,1,2)); 
     *        
     *       quit$
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
     * @see CurveShapeTest#test020_computeControlPoint()
     */
    static double[] computeControlPoint(final double[] pa, final double[] va,
            final double[] pb, final double[] vb, final double[][] tmp_matrix,
            final double[] tmp_right, final double[] pc) {
        // tmp_matrix[0][0] = va[0];
        // tmp_matrix[1][0] = va[1];
        // tmp_matrix[0][1] = vb[0];
        // tmp_matrix[1][1] = vb[1];
        // tmp_right[0] = pb[0] - pa[0];
        // tmp_right[1] = pb[1] - pa[1];
        // MathVec.gauss(tmp_matrix, tmp_right, pc);
        // final double f = pc[0];
        final double f = (-pb[0] * vb[1] + pa[0] * vb[1] + vb[0]
                * (pb[1] - pa[1]))
                / (vb[0] * va[1] - va[0] * vb[1]);
        pc[0] = pa[0] + f * va[0];
        pc[1] = pa[1] + f * va[1];
        return pc;
    }

    /**
     * Split the given interval [min,max] into equidistant sections.
     * 
     * @param min
     * @param max
     * @param sections
     * @return filled <code>sections</code> array.
     * @see CurveShapeTest#test010_sections()
     */
    public static double[] sections(final double min, final double max,
            final double[] sections) {
        final int n = sections.length;
        if (n == 0)
            return sections;
        sections[0] = min;
        sections[n - 1] = max;
        if (n <= 2)
            return sections;
        final double d = (max - min) / (n - 1);
        for (int i = n - 2; i > 0; i--)
            sections[i] = min + i * d;
        return sections;
    }

}