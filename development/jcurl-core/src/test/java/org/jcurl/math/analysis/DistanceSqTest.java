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
package org.jcurl.math.analysis;

import junit.framework.TestCase;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.NewtonSolver;

public class DistanceSqTest extends TestCase {

    public void testIntersectStraightLines() throws ConvergenceException,
            FunctionEvaluationException {
        {
            final double dt = 1e-6;
            final R1RnCurve g1 = R1RnCurve.straightLine(1, 0.5);
            final R1RnCurve g2 = R1RnCurve.straightLine(-6, 4);
            final DifferentiableUnivariateRealFunction distSq = new DistanceSq(
                    g1, g2);
            double t = new NewtonSolver(distSq).solve(2, 4);
            assertEquals("", 2.0, t, dt);
            assertEquals("", 0.0, distSq.value(t), dt);
        }
        {
            final double dt = 1e-6;
            final R1RnCurve g1 = R1RnCurve.straightLine(1, 0.5);
            final R1RnCurve g2 = R1RnCurve.straightLine(-6, 3);
            final DifferentiableUnivariateRealFunction distSq = new DistanceSq(
                    g1, g2);
            double t = new NewtonSolver(distSq).solve(2, 4);
            assertEquals("", 2.8, t, dt);
            assertEquals("", 0.0, distSq.value(t), dt);
        }
    }

    public void testStraightLine() throws ConvergenceException,
            FunctionEvaluationException {
        final R1RnCurve g1 = R1RnCurve.straightLine(1, 0.5);
        final R1RnCurve g2 = R1RnCurve.straightLine(-6, 4);
        final DifferentiableUnivariateRealFunction distSq = new DistanceSq(g1,
                g2);
        assertEquals("", 49, distSq.value(0), 1e-11);
        assertEquals("", 12.25, distSq.value(1), 1e-11);
        assertEquals("", 0, distSq.value(2), 1e-11);
        assertEquals("", 12.25, distSq.value(3), 1e-11);
        assertEquals("", 49, distSq.value(4), 1e-11);
    }

}
