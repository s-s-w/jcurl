/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005 M. Rohrmoser
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

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveShapeTest extends TestCase {
    
    public void testSections() {
        double[] d = new double[0];
        CurveShape.aequidistantSections(1, 2, d);
        
        d = new double[1];
        CurveShape.aequidistantSections(1, 2, d);
        assertEquals(1, d[0], 1e-9);
        CurveShape.aequidistantSections(2, 1, d);
        assertEquals(1, d[0], 1e-9);

        d = new double[2];
        CurveShape.aequidistantSections(1, 2, d);
        assertEquals(1, d[0], 1e-9);
        assertEquals(2, d[1], 1e-9);
        CurveShape.aequidistantSections(2, 1, d);
        assertEquals(1, d[0], 1e-9);
        assertEquals(2, d[1], 1e-9);

        d = new double[3];
        CurveShape.aequidistantSections(1, 3, d);
        assertEquals(1, d[0], 1e-9);
        assertEquals(2, d[1], 1e-9);
        assertEquals(3, d[2], 1e-9);
        CurveShape.aequidistantSections(3, 1, d);
        assertEquals(1, d[0], 1e-9);
        assertEquals(2, d[1], 1e-9);
        assertEquals(3, d[2], 1e-9);
    }

    public void testComputeControlPoint() {
        final double[] p0 = { 0, 0 };
        final double[] p1 = { 1, 0 };
        final double[] v0 = { 1, 1 };
        final double[] v1 = { 1, -1 };
        final double[][] tmp_a = { { 0, 0 }, { 0, 0 } };
        final double[] tmp_b = { 0, 0 };
        final double[] pc = { 0, 0 };

        CurveShape.computeControlPoint(p0, v0, p1, v1, tmp_a, tmp_b, pc);
        assertEquals(0.5, pc[0], 1e-9);
        assertEquals(0.5, pc[1], 1e-9);
    }

}
