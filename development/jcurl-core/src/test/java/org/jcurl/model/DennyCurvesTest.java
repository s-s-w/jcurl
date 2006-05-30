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
package org.jcurl.model;

import junit.framework.TestCase;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.NewtonSolver;

public class DennyCurvesTest extends TestCase {

    public void testStraight() throws FunctionEvaluationException,
            ConvergenceException {
        final DennyCurves m = new DennyCurves();
        final PathSegment f = m.compute(0, 0, 0, 0, 0, 2.5, 0, 1);
        assertEquals("", 0.0, f.value(0, 0), 1e-9);
        assertEquals("", 0.0, f.value(1, 0), 1e-9);
        assertEquals("", 0.0, f.value(2, 0), 1e-9);

        assertEquals(
                "p(x) = 1.7565092079712113E-5*x^4 - 4.699551873981532E-4*x^3",
                f.component(0).toString());
        assertEquals("p(x) = -0.0622935*x^2 + 2.5*x^1", f.component(1)
                .toString());

        // find the stop
        double tStopX = new NewtonSolver(
                (DifferentiableUnivariateRealFunction) f.component(0)
                        .derivative()).solve(0, 100);
        assertEquals("", 20.066299052068032, tStopX, 1e-12);
        assertEquals("", tStopX, new NewtonSolver(
                (DifferentiableUnivariateRealFunction) f.component(1)
                        .derivative()).solve(0, 100, 0.1), 1e-12);
    }
}
