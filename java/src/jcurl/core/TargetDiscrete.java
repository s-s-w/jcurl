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

/**
 * Interface for classes consuming discrete {@link jcurl.core.Rock}location
 * data.
 * 
 * @see jcurl.core.RockSetInterpolator
 * @see jcurl.core.gui.JCurlPanel
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public interface TargetDiscrete extends Target {

    public void setPos(final double t, final RockSet rocks);

    /**
     * 
     * @param t
     * @param rocks
     * @param discontinuous
     *            bitmask of discontinuous rocks as returned by
     *            {@link jcurl.sim.core.CollissionStrategy#compute(RockSet, RockSet)}.
     */
    public void setPos(final double t, final RockSet rocks,
            final int discontinuous);
}