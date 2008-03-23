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
package org.jcurl.core.impl;

import junit.framework.TestCase;

import org.jcurl.core.api.CollissionDetector;
import org.jcurl.core.api.CurveRock;
import org.jcurl.math.Distance2DSq;
import org.jcurl.math.NewtonSimpleSolver;
import org.jcurl.math.Polynome;
import org.jcurl.math.R1RNFunction;

public class CollissionNewtonTest extends TestCase {

    public void testCompute() {
        final CurlerNoCurl s = new CurlerNoCurl(1000, 0);
        final NewtonCollissionDetector co = new NewtonCollissionDetector();
        final R1RNFunction c0 = new CurveRockAnalytic(s.computeRcPoly(0, 1,
                Math.PI / 2, 0));
        // System.out.println(c0.toString());
        final R1RNFunction c1 = CurveStill.newInstance(0, 2, 0);
        // System.out.println(c0.at(2, null));
        // System.out.println(c1.at(2, new RockDouble()));
        // System.out.println(co.compute(0, Double.NaN, c0, c1));
        assertEquals("", 1.6952814631961088, co.compute(0, 5, c0, c1), 1e-9);

        // System.out.println(new DistanceSq(c0, c1,
        // CollissionDetector.RR2).at(1.6952814631961088));
    }

    public void testPre() {
        final CurveRock c0 = new CurveRockAnalytic(new Polynome[] {
                new Polynome(new double[] { 0 }),
                new Polynome(new double[] { 0, 1, -2.8346399307250977E-5 }),
                new Polynome(new double[] { 0, 1.5707963267948966 }) });
        R1RNFunction c1 = new CurveRockAnalytic(new Polynome[] {
                new Polynome(new double[] { 0 }),
                new Polynome(new double[] { 2 }),
                new Polynome(new double[] { 3.141592653589793 }) });

        assertEquals("", 1.5690174844768945, NewtonSimpleSolver
                .computeNewtonValue(new Distance2DSq(c0, c1,
                        CollissionDetector.RR2), 0, 0, CollissionDetector.RR2,
                        0, 5), 1e-9);
        c1 = CurveStill.newInstance(0, 2, 0);
        assertEquals("", 1.5690174844768945, NewtonSimpleSolver
                .computeNewtonValue(new Distance2DSq(c0, c1,
                        CollissionDetector.RR2), 0, 0, CollissionDetector.RR2,
                        0, 5), 1e-9);
    }

    public void testStill() {
        final NewtonCollissionDetector co = new NewtonCollissionDetector();
        final R1RNFunction c0 = CurveStill.newInstance(0, 1, 0);
        final R1RNFunction c1 = CurveStill.newInstance(0, 2, 0);
        assertEquals("", Double.NaN, co.compute(0, 5, c0, c1), 1e-9);
    }
}
