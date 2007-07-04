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
package org.jcurl.core.j3d;

import java.awt.geom.Point2D;

import javax.vecmath.Point3d;

public class Point2DBridge extends Point3d {

    private static final long serialVersionUID = -5623298143038016036L;

    public final Point2D p2d = new Point2D() {

        @Override
        public double getX() {
            return Point2DBridge.this.x;
        }

        @Override
        public double getY() {
            return Point2DBridge.this.y;
        }

        @Override
        public void setLocation(final double x, final double y) {
            Point2DBridge.this.x = x;
            Point2DBridge.this.y = y;
        }
    };

}
