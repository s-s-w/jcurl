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

/**
 * Curve implementation for rocks NOT in motion.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveStill extends CurveRock {

    private static final long serialVersionUID = -8031863193302315171L;

    public static CurveStill newInstance(final double x, final double y,
            final double a) {
        return new CurveStill(x, y, a);
    }

    public static CurveStill newInstance(final double[] x) {
        return CurveStill.newInstance(x[0], x[1], x[2]);
    }

    public static CurveStill newInstance(final Rock x) {
        return CurveStill.newInstance(x.getX(), x.getY(), x.getZ());
    }

    private final double a;

    private final double x;

    private final double y;

    CurveStill(final double x, final double y, final double a) {
        this.x = x;
        this.y = y;
        this.a = a;
    }

    CurveStill(final Rock x) {
        this(x.getX(), x.getY(), x.getZ());
    }

    @Override
    public double at(final int dim, final int c, final double t) {
        if (c > 0)
            return 0;
        switch (dim) {
        case 0:
            return x;
        case 1:
            return y;
        case 2:
            return a;
        default:
            throw new RuntimeException();
        }
    }

    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append('[');
        buf.append(x).append(", ");
        buf.append(y).append(", ");
        buf.append(a);
        buf.append(']');
        return buf.toString();
    }

}
