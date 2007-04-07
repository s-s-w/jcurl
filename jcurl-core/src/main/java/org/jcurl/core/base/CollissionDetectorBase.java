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

import org.jcurl.math.MathVec;
import org.jcurl.math.R1RNFunction;
import org.jcurl.math.R1RNFunctionImpl;

/**
 * Find Collissions of two spheres moving along curves.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CollissionDetectorBase implements Strategy {

    public static final double RR2 = MathVec.sqr(2 * RockProps.DEFAULT
            .getRadius());

    /**
     * Find the smallest <code>t</code> with
     * <code>t0 &lt;= t &lt;= tmax</code> when the two spheres <code>ra</code>
     * moving along <code>fa</code> and <code>rb</code> moving along
     * <code>fb</code> touch.
     * <p>
     * Assumes a curling rock's radius for the radii of the both spheres.
     * </p>
     * <p>
     * Delegates to
     * {@link #compute(double, double, R1RNFunctionImpl, R1RNFunctionImpl, double)}.
     * </p>
     * 
     * @param t0
     *            start time
     * @param tstop
     *            max. time
     * @param fa
     *            location of sphere <code>a</code>
     * @param fb
     *            location of sphere <code>b</code>
     * @return time of next collission or {@link Double#NaN} if none.
     */
    public double compute(final double t0, final double tstop,
            final R1RNFunction fa, final R1RNFunction fb) {
        return compute(t0, tstop, fa, fb, RR2);
    }

    /**
     * Find the smallest <code>t</code> with
     * <code>t0 &lt;= t &lt;= tmax</code> when the two spheres <code>ra</code>
     * moving along <code>fa</code> and <code>rb</code> moving along
     * <code>fb</code> touch.
     * 
     * @param t0
     *            start time
     * @param tstop
     *            max. time
     * @param fa
     *            location of sphere <code>a</code>
     * @param fb
     *            location of sphere <code>b</code>
     * @param distSq
     *            square of sum of both spheres radii
     * @return time of next collission or {@link Double#NaN} if none.
     */
    public abstract double compute(final double t0, final double tstop,
            final R1RNFunction fa, final R1RNFunction fb, final double distSq);

    /**
     * Find the smallest <code>t</code> with
     * <code>t0 &lt;= t &lt;= tmax</code> when the two spheres <code>ra</code>
     * moving along <code>fa</code> and <code>rb</code> moving along
     * <code>fb</code> touch.
     * <p>
     * Delegates to
     * {@link #compute(double, double, R1RNFunctionImpl, R1RNFunctionImpl, double)}.
     * </p>
     * 
     * @param t0
     *            start time
     * @param tstop
     *            max. time
     * @param fa
     *            location of sphere <code>a</code>
     * @param ra
     *            radius of sphere <code>a</code>
     * @param fb
     *            location of sphere <code>b</code>
     * @param rb
     *            radius of sphere <code>b</code>
     * @return time of next collission or {@link Double#NaN} if none.
     */
    public double compute(final double t0, final double tstop,
            final R1RNFunction fa, final double ra, final R1RNFunction fb,
            final double rb) {
        return compute(t0, tstop, fa, fb, MathVec.sqr(ra + rb));
    }
}
