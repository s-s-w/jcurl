/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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

import org.jcurl.math.CurveFkt;
import org.jcurl.math.R1R1Function;
import org.jcurl.math.R1RNFunction;

public class CurveRockAnalytic extends CurveRockBase {

    private final R1RNFunction f;

    public CurveRockAnalytic(final R1R1Function x, final R1R1Function y,
            final R1R1Function a) {
        this(new R1R1Function[] { x, y, a });
    }

    public CurveRockAnalytic(final R1R1Function[] x) {
        this(new CurveFkt(x));
    }

    public CurveRockAnalytic(final R1RNFunction f) {
        if (f.dim != 3)
            throw new IllegalArgumentException("Function must be 3-dimensional");
        this.f = f;
    }

    public double at(final int dim, final int c, final double t) {
        return f.at(dim, c, t);
    }

}
