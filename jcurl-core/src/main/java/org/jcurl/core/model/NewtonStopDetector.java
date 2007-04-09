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
package org.jcurl.core.model;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.CurveStill;
import org.jcurl.core.base.StopDetector;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.Distance2DSq;
import org.jcurl.math.NewtonSimpleSolver;
import org.jcurl.math.R1RNFunction;

public class NewtonStopDetector implements StopDetector {

    private static final Log log = JCLoggerFactory
            .getLogger(NewtonStopDetector.class);

    private static final R1RNFunction zero = CurveStill.newInstance(0, 0, 0);

    /**
     * When drops the "speed" (first derivative) of a function to zero?
     * 
     * @param f
     *            first 2 dimensions used, must be 2 x differentiable
     * @param tmin
     * @param tmax
     * @return {@link Double#NaN} if not stopping within [tmin, tmax]
     * @see Distance2DSq#Distance2DSq(R1RNFunction, R1RNFunction, double, int)
     * @see NewtonSimpleSolver#computeNewtonZero(R1RNFunction, int, int, double,
     *      double)
     */
    public double compute(final R1RNFunction f, final double tmin,
            final double tmax) {
        final double stop = NewtonSimpleSolver.computeNewtonZero(
                new Distance2DSq(f, zero, 0, 1), 0, 0, tmin, tmax);
        if (log.isDebugEnabled())
            log.debug("Stop: " + stop);
        return stop;
    }
}
