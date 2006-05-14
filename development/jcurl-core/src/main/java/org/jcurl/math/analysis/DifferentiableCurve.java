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
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;

public class DifferentiableCurve {

    private final DifferentiableUnivariateRealFunction[] c;

    transient private DifferentiableCurve derived = null;

    public DifferentiableCurve(DifferentiableUnivariateRealFunction[] c) {
        this.c = new DifferentiableUnivariateRealFunction[c.length];
        System.arraycopy(c, 0, this.c, 0, c.length);
    }

    public DifferentiableUnivariateRealFunction component(int dim) {
        return this.c[dim];
    }

    public DifferentiableUnivariateRealFunction[] components() {
        final DifferentiableUnivariateRealFunction[] ret = new DifferentiableUnivariateRealFunction[c.length];
        System.arraycopy(c, 0, ret, 0, c.length);
        return ret;
    }

    public DifferentiableCurve derivative() {
        if (derived == null) {
            final DifferentiableUnivariateRealFunction[] tmp = new DifferentiableUnivariateRealFunction[dimension()];
            for (int i = dimension() - 1; i >= 0; i--)
                tmp[i] = (DifferentiableUnivariateRealFunction) component(i)
                        .derivative();
            derived = new DifferentiableCurve(tmp);
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
            ret[i] = value(i, t);
        return ret;
    }

    public double value(int dim, double t) throws FunctionEvaluationException {
        return component(dim).value(t);
    }
}
