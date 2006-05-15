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

public class DifferentiableCurveTest extends TestCase {

    public void testStraightLine() throws ConvergenceException,
            FunctionEvaluationException {
        {
            final DifferentiableCurve g1 = new DifferentiableCurve(
                    new Polynome[] { new Polynome(new double[] { 0, 1 }),
                            new Polynome(new double[] { 1, 0.5 }) });
            final double[] tmp = { 0, 0 };
            g1.value(0, tmp);
            assertEquals("", 0, tmp[0], 1e-11);
            assertEquals("", 1, tmp[1], 1e-11);

            g1.value(1, tmp);
            assertEquals("", 1, tmp[0], 1e-11);
            assertEquals("", 1.5, tmp[1], 1e-11);

            g1.value(2, tmp);
            assertEquals("", 2, tmp[0], 1e-11);
            assertEquals("", 2, tmp[1], 1e-11);

            g1.value(3, tmp);
            assertEquals("", 3, tmp[0], 1e-11);
            assertEquals("", 2.5, tmp[1], 1e-11);
        }
        {
            final DifferentiableCurve g1 = new DifferentiableCurve(
                    new Polynome[] { new Polynome(new double[] { 0, 1 }),
                            new Polynome(new double[] { -6, 3 }) });
            final double[] tmp = { 0, 0 };
            g1.value(0, tmp);
            assertEquals("", 0, tmp[0], 1e-11);
            assertEquals("", -6, tmp[1], 1e-11);

            g1.value(1, tmp);
            assertEquals("", 1, tmp[0], 1e-11);
            assertEquals("", -3, tmp[1], 1e-11);

            g1.value(2, tmp);
            assertEquals("", 2, tmp[0], 1e-11);
            assertEquals("", 0, tmp[1], 1e-11);

            g1.value(3, tmp);
            assertEquals("", 3, tmp[0], 1e-11);
            assertEquals("", 3.0, tmp[1], 1e-11);
        }
    }
}
