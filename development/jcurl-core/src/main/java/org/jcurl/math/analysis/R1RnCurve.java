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
package org.jcurl.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;

/**
 * Convenience wrapper for at least 1x differentiable curves
 * <code>f : R1 -&gt; Rn</code>.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class R1RnCurve {

    public static R1RnCurve straightLine(double y0, double incline) {
        return new R1RnCurve(new Polynome[] {
                new Polynome(new double[] { 0, 1 }),
                new Polynome(new double[] { y0, incline }) });
    }

    private final R1R1Function[] c;

    transient private R1RnCurve derived = null;

    public R1RnCurve(R1R1Function[] c) {
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

    public R1RnCurve derivative() {
        if (derived == null) {
            final R1R1Function[] tmp = new R1R1Function[dimension()];
            for (int i = dimension() - 1; i >= 0; i--)
                tmp[i] = (R1R1Function) component(i).derivative();
            derived = new R1RnCurve(tmp);
        }
        return derived;
    }

    public int dimension() {
        return c.length;
    }

    public double[] value(double t, double[] ret)
            throws FunctionEvaluationException {
        if (ret == null)
            ret = new double[dimension()];
        else if (ret.length != dimension())
            throw new IllegalArgumentException("Dimension mismatch: "
                    + dimension() + "!=" + ret.length);
        for (int i = dimension() - 1; i >= 0; i--)
            ret[i] = component(i).value(t);//value(i, t);
        return ret;
    }

    /**
     * Return the raw, untransformed value!
     * 
     * @param dim
     * @param t
     * @throws FunctionEvaluationException
     */
    private double value(int dim, double t) throws FunctionEvaluationException {
        return component(dim).value(t);
    }
}
