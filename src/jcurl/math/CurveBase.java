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
package jcurl.math;

/**
 * Abstract base class for n-dimensional curves (R -&gt; R^n).
 * 
 * @see jcurl.math.CurveTest 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CurveBase {
    protected final int dim;

    protected CurveBase(final int dim) {
        this.dim = dim;
    }

    public double[] getC(int c, double t, double[] ret) {
        if (ret == null)
            ret = new double[dim];
        for (int i = dim - 1; i >= 0; i--)
            ret[i] = getC(i, c, t);
        return ret;
    }

    public float[] getC(int c, double t, float[] ret) {
        if (ret == null)
            ret = new float[dim];
        for (int i = dim - 1; i >= 0; i--)
            ret[i] = (float) getC(i, c, t);
        return ret;
    }

    public abstract double getC(int dim, int c, double t);
}