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
package org.jcurl.core.base;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.jcurl.math.MathVec;

/**
 * Base class for rock information (either location or speed). The "Z" component
 * is the handle angle in radians.
 * 
 * @see org.jcurl.core.base.PositionSet
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:Rock.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public abstract class Rock extends Point2D implements Cloneable {

    protected transient boolean dirty = true;

    protected transient AffineTransform trafo;

    public abstract void setLocation(double x, double y, double z);

    public boolean equals(final Rock b) {
        if (this == b)
            return true;
        if (b == null)
            return false;
        return getX() == b.getX() && getY() == b.getY() && getZ() == b.getZ();
    }


    public abstract void setX(double z);

    public abstract void setY(double z);

    public abstract void setZ(double z);

    public abstract void setLocation(double[] l);

    public abstract double getZ();

    @Override
    public abstract Object clone();

    /**
     * Override super to ignore {@link #getZ()}.
     * 
     * @param b
     * @return the distance square.
     */
    public double distanceSq(final Rock b) {
        return MathVec.sqr(getX() - b.getX()) + MathVec.sqr(getY() - b.getY());
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Rock))
            return false;
        return equals((Rock) obj);
    }

    public AffineTransform getTrafo() {
        if (trafo == null || dirty) {
            if (trafo == null)
                trafo = new AffineTransform();
            else
                trafo.setToIdentity();
            trafo.translate(getX(), getY());
            trafo.rotate(getZ());
            dirty = false;
        }
        return trafo;
    }

    @Override
    public abstract int hashCode();

    /**
     * Convenience method to check if zero or not.
     * 
     * @return whether x or y are non-zero
     */
    public abstract boolean nonZero();

    /**
     * Danger: Loss of information as the {@link #getZ()} is restricted to the
     * values of {@link Math#asin(double)}!
     * 
     * @param trafo
     */
    public void setTrafo(final AffineTransform trafo) {
        setLocation(trafo.getTranslateX(), trafo.getTranslateY(), Math
                .asin(trafo.getShearY()));
        // setLocation(trafo.getTranslateX(), trafo.getTranslateY(), Math
        // .acos(trafo.getScaleX()));
        if (this.trafo == null)
            this.trafo = new AffineTransform();
        this.trafo.setTransform(trafo);
        dirty = false;
    }

    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append('[');
        buf.append(getX());
        buf.append(", ");
        buf.append(getY());
        buf.append(", ");
        buf.append(getZ());
        buf.append(']');
        return buf.toString();
    }
}