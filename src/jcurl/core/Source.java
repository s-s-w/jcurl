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
package jcurl.core;

import jcurl.core.dto.RockSetProps;

/**
 * Interface for classes providing {@link jcurl.core.Rock}location and speed
 * data.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
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
     * @param time
     *            [sec]
     * @param rocks
     * @return
     */
    public abstract RockSet getPos(final double time, RockSet rocks);

    /**
     * Get the rocks' speeds - optional.
     * 
     * @param time
     *            [sec]
     * @param rocks
     * @return
     */
    public abstract RockSet getSpeed(final double time, RockSet rocks);

    public abstract boolean isDiscrete();

    public abstract boolean isForwardOnly();

    public abstract boolean isWithSpeed();

    /**
     * Clear all internal state and set the given initial state.
     * 
     * @param startTime [msec]
     * @param startPos
     * @param startSpeed
     * @param props
     */
    public abstract void reset(double startTime, RockSet startPos,
            RockSet startSpeed, RockSetProps props);
}