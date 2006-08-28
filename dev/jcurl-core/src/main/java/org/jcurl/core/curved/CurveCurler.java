/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.core.curved;

import org.jcurl.core.base.Curler;
import org.jcurl.core.base.Rock;

/**
 * Create {@link CurveRock}s for freely running rocks. (No collissions)
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CurveCurler implements Curler {

    /**
     * Calls {@link #computeRC(double, double)} and
     * {@link CurveTransformed#CurveTransformed(CurveRock, java.awt.geom.Point2D, java.awt.geom.Point2D, double)}.
     * 
     * @param t0
     * @param x0
     * @param v0
     * @return the trajectory in wc
     */
    public CurveTransformed computeWC(double t0, final Rock x0, final Rock v0) {
        final CurveRock c = computeRC(v0.distance(0, 0), v0.getZ());
        return new CurveTransformed(c, x0, v0, t0);
    }

    /**
     * Create a trajectory in rock-coordinates.
     * 
     * @param v
     * @param omega
     * @return the trajectory in rc
     */
    public abstract CurveRock computeRC(final double v, final double omega);
}
