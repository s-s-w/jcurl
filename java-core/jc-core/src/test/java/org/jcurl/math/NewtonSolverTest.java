/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
package org.jcurl.math;

import junit.framework.TestCase;

public class NewtonSolverTest extends TestCase {

    public void testParabolic() throws MathException {
        final Polynome p = new Polynome(new double[] { 0, 0, 1 });
        assertEquals("", 1.0, p.at(-1), 1e-9);
        assertEquals("", 0, p.at(0), 1e-9);
        assertEquals("", 1.0, p.at(1.0), 1e-9);

        final R1R1Solver s = new NewtonSolver(p);
        s.setAbsoluteAccuracy(1e-6);
        assertEquals("", 0.0, s.solve(0, -1, 3, -0.99), 1e-6);
    }

    public void testStraightLine() throws MathException {
        final Polynome p = new Polynome(new double[] { -1, 0.5 });
        assertEquals("", -1.0, p.at(0), 1e-9);
        assertEquals("", -0.5, p.at(1.0), 1e-9);
        assertEquals("", 0, p.at(2.0), 1e-9);
        assertEquals("", 0.5, p.at(3.0), 1e-9);

        final R1R1Solver s = new NewtonSolver(p);
        assertEquals("", 2.0, s.solve(0, -1, 3, -0.99), 1e-9);
    }
}
