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

import org.jcurl.core.PositionSet;
import org.jcurl.core.SpeedSet;

/**
 * Interface for classes consuming discrete {@link org.jcurl.core.Rock}location
 * data.
 * 
 * @see jcurl.core.RockSetInterpolator
 * @see jcurl.core.gui.JCurlDisplay
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public interface TargetDiscrete extends Target {

    public void setPos(final double t, final PositionSet rocks);

    /**
     * 
     * @param t
     * @param rocks
     * @param discontinuous
     *            bitmask of discontinuous rocks as returned by
     *            {@link org.jcurl.model.CollissionModel#compute(PositionSet, SpeedSet)}.
     */
    public void setPos(final double t, final PositionSet rocks,
            final int discontinuous);
}