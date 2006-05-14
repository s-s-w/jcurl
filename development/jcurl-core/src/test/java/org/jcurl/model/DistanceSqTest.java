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
import org.jcurl.math.analysis.DifferentiableCurve;

public class DistanceSqTest extends TestCase {

    public void testStraightLine() throws ConvergenceException,
            FunctionEvaluationException {
        final DifferentiableCurve g1 = DifferentiableCurve.straightLine(1, 0.5);
        final DifferentiableCurve g2 = DifferentiableCurve.straightLine(-6, 3);
        final DifferentiableUnivariateRealFunction distSq = new DistanceSq(g1,
                g2);
        assertEquals("", 49, distSq.value(0), 1e-11);
        assertEquals("", 20.25, distSq.value(1), 1e-11);
        assertEquals("", 4, distSq.value(2), 1e-11);
        assertEquals("", 0.25, distSq.value(3), 1e-11);
        assertEquals("", 9, distSq.value(4), 1e-11);
    }
}
