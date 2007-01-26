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
package org.jcurl.core.base;

/**
 * Interface for classes providing {@link org.jcurl.core.base.Rock} location and
 * speed data.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:Source.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public interface Source {
    /**
     * Get the end time.
     * 
     * @return the max yet known time.
     */
    public abstract double getMaxT();

    /**
     * Get the start time.
     * 
     * @return the start time
     */
    public abstract double getMinT();

    /**
     * Get the rocks' positions.
     * 
     * @return the rocks' positions
     */
    public abstract PositionSet getPos();

    /**
     * Get the rocks' speeds - optional.
     * 
     * @return the rocks' speeds
     */
    public abstract SpeedSet getSpeed();

    /**
     * Get the current time.
     * 
     * @return the start time
     */
    public abstract double getTime();

    public abstract boolean isDiscrete();

    public abstract boolean isForwardOnly();

    public abstract boolean isWithSpeed();

    /**
     * Set the current time.
     * 
     * @param t
     */
    public abstract void setTime(final double t);
}