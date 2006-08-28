/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.core.curved;

import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.math.MathException;

public abstract class CurveRockBase implements CurveRock {

    public int dimension() {
        return 3;
    }

    public double[] value(double t, double[] ret) throws MathException {
        return value(t, 0, ret);
    }

    public double[] value(double t, int derivative, double[] ret)
            throws MathException {
        if (ret == null)
            ret = new double[dimension()];
        for (int i = ret.length - 1; i >= 0; i--)
            ret[i] = value(t, derivative, i);
        return ret;
    }

    protected abstract double value(double t, int derivative, int component)
            throws MathException;

    public Rock value(double t, int derivative, Rock ret) throws MathException {
        if (ret == null)
            ret = new RockDouble();
        ret.setLocation(value(t, derivative, 0), value(t, derivative, 1),
                value(t, derivative, 2));
        return ret;
    }

    public Rock value(double t, Rock ret) throws MathException {
        return value(t, 0, ret);
    }
}