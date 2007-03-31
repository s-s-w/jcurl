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

abstract class SlideBase implements Slide, Strategy, Factory {

    public static CurveRock still(final double x, final double y, final double a) {
        return new CurveRock() {

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
        };
    }

    public static CurveRock still(final Rock x) {
        return still(x.getX(), x.getY(), x.getZ());
    }

}
