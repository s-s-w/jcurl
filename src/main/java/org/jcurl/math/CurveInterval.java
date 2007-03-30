/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.math;

/**
 * A curve thats <code>0</code> for everything outside the interval
 * <code>[tmin, tmax]</code>.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveInterval extends R1RNFunction {

    private final R1RNFunction curve;

    private final double tmax;

    private final double tmin;

    public CurveInterval(final double tmin, final double tmax,
            final R1RNFunction curve) {
        super(curve.dim());
        this.curve = curve;
        this.tmin = tmin;
        this.tmax = tmax;
    }

    /**
     * @param dim
     *            dimension
     * @param c
     *            c'th derivative
     * @param t
     *            parameter
     * @return value
     */
    @Override
    public double at(final int dim, final int c, double t) {
        if (t < tmin) {
            if (c > 0)
                return 0;
            t = tmin;
        } else if (t > tmax) {
            if (c > 0)
                return 0;
            t = tmax;
        }
        return curve.at(dim, c, t);
    }
}