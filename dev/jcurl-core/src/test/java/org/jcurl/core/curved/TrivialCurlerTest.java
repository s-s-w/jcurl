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
package org.jcurl.core.curved;

import junit.framework.TestCase;

import org.jcurl.core.dto.Rock;
import org.jcurl.core.dto.RockDouble;
import org.jcurl.core.math.Point3D;

public class TrivialCurlerTest extends TestCase {

    public static void assertEquals(double x, double y, double z, Point3D p,
            double delta) {
        final String txt = "";
        assertEquals(txt, x, p.getX(), delta);
        assertEquals(txt, y, p.getY(), delta);
        assertEquals(txt, z, p.getZ(), delta);
    }

    public void testComputeRaw() {
        Rock p = new RockDouble();
        final TrivialCurler curl = new TrivialCurler();
        CurveRock cr = curl.compute(1.5, 0.5);
        assertEquals(0, 0, 0, cr.value(0, p), 1e-9);
        assertEquals(0, 1.5, 0.5, cr.value(1, p), 1e-9);
        assertEquals(0, 3, 1, cr.value(2, p), 1e-9);
    }
}
