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
 * A "normal" one-dimensional function. Because this is the same as a
 * 1-dimensional curve it extends {@link jcurl.math.CurveBase}.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class Function1D extends CurveBase {

    protected Function1D() {
        super(1);
    }

    /**
     * Compute the c'th derivative at <code>x</code>.
     * 
     * @param c
     *            derivative (0=location, 1:speed, ...)
     * @param x
     *            x-value
     * @return the c'th derivative at <code>x</code>
     * @see Function1D#getC(int, int, double)
     */
    public abstract double getC(int c, double x);

    /**
     * Compute the c'th derivative at <code>x</code>.
     * 
     * @param dim
     *            must be 0
     * @param c
     *            derivative (0=location, 1:speed, ...)
     * @param x
     *            x-value
     * @return the c'th derivative at <code>x</code>
     * @see Function1D#getC(int, double)
     * @throws IllegalArgumentException
     *             if <code>dim != 0</code>
     */
    public double getC(int dim, int c, double x) {
        if (dim != 0)
            throw new IllegalArgumentException("Dimension must be 0");
        return getC(c, x);
    }

    /**
     * Compute <code>x where f(x) = 0</code> using Newton's algorithm.
     * 
     * @param x0
     * @return
     */
    public double computeNewtonZero(final int c, double x) {
        return computeNewtonZero(0, c, x);
    }

}