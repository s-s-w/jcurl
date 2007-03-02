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

/**
 * Interface for classes consuming discrete {@link org.jcurl.core.base.Rock}location
 * data.
 * 
 * @see org.jcurl.core.base.RockSetInterpolator
 * @see org.jcurl.core.swing.JCurlDisplay
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:TargetDiscrete.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public interface TargetDiscrete extends Target {

    public void setPos(final PositionSet rocks);

    /**
     * 
     * @param rocks
     * @param discontinuous
     *            bitmask of discontinuous rocks as returned by
     *            {@link org.jcurl.core.base.CollissionStrategy#compute(PositionSet, SpeedSet)}.
     */
    public void setPos(final PositionSet rocks, final int discontinuous);
}