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

import org.jcurl.core.dto.Rock;

/**
 * Interface for interpolators of single {@link org.jcurl.core.dto.Rock}s.
 * 
 * @see jcurl.core.RockSetInterpolator
 * @see jcurl.core.CSplineRockInterpolator
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public interface IRockInterpolator {
    public abstract void add(final double t, final Rock rock);

    public abstract void add(final double t, final Rock rock,
            final boolean discontinuous);

    public abstract double getMaxT();

    public abstract double getMinT();

    public abstract Rock getPos(final double t, final Rock rock);

    public abstract Rock getSpeed(final double t, final Rock rock);

    public abstract void reset();
}