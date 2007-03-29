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


/**
 * Find Collissions of two curves.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CollissionDetector implements Strategy {

    public static class NoCollission extends Exception {

        private static final long serialVersionUID = -8458724406611218446L;

    }

    /**
     * Find the smallest <code>t</code> with
     * <code>t0 &lt;= t &lt;= tmax</code> when the two spheres <code>ra</code>
     * moving along <code>fa</code> and <code>rb</code> moving along
     * <code>fb</code> touch.
     * 
     * @param t0
     * @param tmax
     * @param fa
     * @param ra
     * @param fb
     * @param rb
     * @return time of next collission
     * @throws NoCollission
     *             they don't collide in the given interval.
     */
    public abstract double compute(double t0, double tmax, CurveRock fa,
            double ra, CurveRock fb, double rb) throws NoCollission;
}
