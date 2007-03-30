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

import junit.framework.TestCase;

import org.jcurl.math.Polynome;

public class CurveRockTest extends TestCase {

    public void testStill() {
        final CurveRock a = new CurveRockAnalytic(new Polynome[] {
                new Polynome(new double[] { 0 }),
                new Polynome(new double[] { 2 }),
                new Polynome(new double[] { Math.PI }) });
        final CurveRock b = CurveRock.still(0, 2, Math.PI);
        final Rock ra = new RockDouble();
        final Rock rb = new RockDouble();
        for (int c = 0; c < 3; c++)
            for (double t = -10; t <= 10; t += 0.1) {
                for (int dim = 0; dim < 3; dim++)
                    assertEquals("c=" + c + " dim=" + dim + " t=" + t, a.at(
                            dim, c, t), b.at(dim, c, t), 1e-9);
                assertEquals(a.at(0, t, ra), b.at(0, t, rb));
            }
    }
}
