/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
abstract class Point3D extends Point2D {

    public double distanceSq(final Point3D b) {
        return MathVec.sqr(getX() - b.getX()) + MathVec.sqr(getY() - b.getY())
                + MathVec.sqr(getZ() - b.getZ());
    }

    @Override
    public boolean equals(final Object b) {
        if (this == b)
            return true;
        // TODO getClass
        // http://www.angelikalanger.com/Articles/JavaSpektrum/02.Equals-Part2/02.Equals2.html
        if (b == null || !(b instanceof Point3D))
            return false;
        return equals((Point3D) b);
    }

    public boolean equals(final Point3D b) {
        if (this == b)
            return true;
        if (b == null)
            return false;
        return getX() == b.getX() && getY() == b.getY() && getZ() == b.getZ();
    }

    @Override
    public abstract double getX();

    @Override
    public abstract double getY();

    public abstract double getZ();

    @Override
    public abstract int hashCode();

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
    public void setLocation(final double x, final double y, final double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public void setLocation(final double[] pt) {
        if (pt.length != 3)
            throw new IllegalArgumentException();
        this.setLocation(pt[0], pt[1], pt[2]);
    }

    public void setLocation(final Point3D pt) {
        this.setLocation(pt.getX(), pt.getY(), pt.getZ());
    }

    public abstract void setX(double x);

    public abstract void setY(double y);

    public abstract void setZ(double z);
}