/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.core.model;

/**
 * Convenience wrapper for at least 1x differentiable curves
 * <code>f : R1 -&gt; Rn</code>.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: R1RnCurveBase.java 336 2006-06-05 21:30:35Z mrohrmoser $
 */
public class R1RnCurveBase implements R1RnCurve {

    public double[] value(double t, int derivative, double[] ret) {
        if (ret == null)
            ret = new double[dimension()];
        else if (ret.length != dimension())
            throw new IllegalArgumentException("Dimension mismatch: "
                    + dimension() + "!=" + ret.length);
        for (int i = dimension() - 1; i >= 0; i--)
            ret[i] = component(i).value(t, derivative);
        return ret;
    }

    public static R1RnCurve straightLine(double y0, double incline) {
        return new R1RnCurveBase(new PolynomeBase[] {
                new PolynomeBase(new double[] { 0, 1 }),
                new PolynomeBase(new double[] { y0, incline }) });
    }

    private final R1R1Function[] c;

    public R1RnCurveBase(R1R1Function[] c) {
        this.c = new R1R1Function[c.length];
        System.arraycopy(c, 0, this.c, 0, c.length);
    }

    public R1R1Function component(int dim) {
        return this.c[dim];
    }

    public R1R1Function[] components() {
        final R1R1Function[] ret = new R1R1Function[c.length];
        System.arraycopy(c, 0, ret, 0, c.length);
        return ret;
    }

    public int dimension() {
        return c.length;
    }

    public double[] value(double t, double[] ret) {
        return value(t, 0, ret);
    }
}
