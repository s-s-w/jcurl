/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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

import org.jcurl.math.R1RNFunction;

/**
 * Uses
 * {@link R1RNFunction#computeNewtonValue(int, int, double, double, double)} on
 * {@link DistanceSq} to find the next collission.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CollissionNewton extends CollissionDetector {

    public double compute(final double t0, final double tmax,
            final CurveRock fa, final CurveRock fb, final double distSq) {
        return new DistanceSq(fa, fb, 0).computeNewtonValue(0, 0, distSq, t0,
                tmax);
    }
}
