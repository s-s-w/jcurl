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
package org.jcurl.math;

import java.awt.geom.Point2D;

/**
 * Base class for 3D coordinates.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class Point3D extends Point2D {

    public abstract double getX();

    public abstract double getY();

    public abstract double getZ();

    /**
     * Sets the location of this Point3D to the specified double coordinates.
     * 
     * @param x
     *            the coordinates of this Point3D
     * @param y
     *            the coordinates of this Point3D
     * @param z
     *            (angle) the coordinates of this Point3D
     */
    public void setLocation(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public void setLocation(final Point3D pt) {
        this.setLocation(pt.getX(), pt.getY(), pt.getZ());
    }

    public abstract void setX(double x);

    public abstract void setY(double y);

    public abstract void setZ(double z);
}