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
package jcurl.model;

import jcurl.math.Point3D;

/**
 * Base class for rock information (either location or speed). The "Z" component
 * is the handle angle in radians.
 * 
 * @see jcurl.model.PositionSet
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class Rock extends Point3D implements Cloneable {
    public abstract Object clone();

    /**
     * Convenience method to check if zero or not.
     * 
     * @return whether x or y are non-zero
     */
    public abstract boolean nonZero();

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append('[');
        buf.append(this.getX());
        buf.append(", ");
        buf.append(this.getY());
        buf.append(", ");
        buf.append(this.getZ());
        buf.append(']');
        return buf.toString();
    }
}