/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.base;

public abstract class CurveRockBase extends CurveRock {

    public int dimension() {
        return 3;
    }

    public double[] at(final double t, final double[] ret) {
        return at(0, t, ret);
    }

    public double[] at(final int derivative, final double t, double[] ret) {
        if (ret == null)
            ret = new double[dimension()];
        for (int i = ret.length - 1; i >= 0; i--)
            ret[i] = at(i, derivative, t);
        return ret;
    }

    public Rock at(final int derivative, final double t, Rock ret) {
        if (ret == null)
            ret = new RockDouble();
        ret.setLocation(at(0, derivative, t), at(1, derivative, t), at(2,
                derivative, t));
        return ret;
    }

    public Rock at(final double t, final Rock ret) {
        return at(0, t, ret);
    }
}