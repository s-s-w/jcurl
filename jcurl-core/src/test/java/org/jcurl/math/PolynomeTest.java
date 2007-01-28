/*
 * jcurl curling simulation framework 
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
 * JUnit Test
 * 
 * @see org.jcurl.math.Polynome
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PolynomeTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(PolynomeTest.class);
    }

    public void test010_fak() {
        assertEquals(1, Polynome.fak(0, 0));
        assertEquals(1, Polynome.fak(1, 1));
        assertEquals(2, Polynome.fak(2, 1));
        assertEquals(6, Polynome.fak(3, 1));
        assertEquals(3, Polynome.fak(3, 2));
        assertEquals(24, Polynome.fak(4, 1));
        assertEquals(12, Polynome.fak(4, 2));

        assertEquals(1, Polynome.fak(3, 3));
        assertEquals(1, Polynome.fak(2, 2));
        assertEquals(1, Polynome.fak(1, 1));
        assertEquals(1, Polynome.fak(0, 0));
    }

    public void test020_getC() {
        final double[] a = { 1.1, 1.2, 1.3, 1.4 };
        Polynome po = new Polynome(a);

        // C0
        double x = 1.5;
        double y = a[0] + a[1] * x + a[2] * x * x + a[3] * x * x * x;
        assertEquals("", y, po.at(0, x), 1e-9);
        x = 2.5;
        y = a[0] + a[1] * x + a[2] * x * x + a[3] * x * x * x;
        assertEquals("", y, po.at(0, x), 1e-9);

        // C1
        x = 1.5;
        y = 1 * a[1] + 2 * a[2] * x + 3 * a[3] * x * x;
        assertEquals("", y, po.at(1, x), 1e-9);
        x = 2.5;
        y = 1 * a[1] + 2 * a[2] * x + 3 * a[3] * x * x;
        assertEquals("", y, po.at(1, x), 1e-9);

        // C2
        x = 1.5;
        y = 1 * 2 * a[2] + 2 * 3 * a[3] * x;
        assertEquals("", y, po.at(2, x), 1e-9);
        x = 2.5;
        y = 1 * 2 * a[2] + 2 * 3 * a[3] * x;
        assertEquals("", y, po.at(2, x), 1e-9);

        // C3
        x = 1.5;
        y = 1 * 2 * 3 * a[3];
        assertEquals("", y, po.at(3, x), 1e-9);
        x = 2.5;
        y = 1 * 2 * 3 * a[3];
        assertEquals("", y, po.at(3, x), 1e-9);

        // C4
        x = 1.5;
        y = 0;
        assertEquals("", y, po.at(4, x), 1e-9);
        x = 2.5;
        y = 0;
        assertEquals("", y, po.at(4, x), 1e-9);
    }

    public void test025_Newton() {
        {
            double[] a = { -1, 1 };
            Polynome po = new Polynome(a);
            assertEquals("", 1, po.computeNewtonZero(0, 0.5), 1e-6);
        }
        {
            double[] a = { -1, 0, 1 };
            Polynome po = new Polynome(a);
            assertEquals("", 0, po.at(0, 1), 1e-6);
            assertEquals("", 1, po.computeNewtonZero(0, 0.5), 1e-6);
        }
        {
            double[] a = { 0, 1, -0.0535848758171096 };
            Polynome po = new Polynome(a);
            assertEquals("", 0.0, po.at(0, 0), 1e-6);
            assertEquals("", 1.0, po.at(1, 0), 1e-6);
            assertEquals("", 9.330991112241236, po.computeNewtonZero(1, 0),
                    1e-6);
        }
    }

    public void test029_getPolyParams() {
        double t0 = 2.894295921183459;
        double dt = 0;

        double v = 0.11913533326608741;
        double a = -0.1071697516342192;
        double[] par = Polynome.getPolyParams(t0, 0, v, a);

        assertEquals("", 0, Polynome.poly(0, t0 + dt, par), 1e-9);
        assertEquals("", v, Polynome.poly(1, t0 + dt, par), 1e-9);
        assertEquals("", a, Polynome.poly(2, t0 + dt, par), 1e-9);

        dt = 0.1;
        assertEquals("", 0.0113776845, Polynome.poly(0, t0 + dt, par), 1e-9);
        assertEquals("", 0.1084183581, Polynome.poly(1, t0 + dt, par), 1e-9);
        assertEquals("", a, Polynome.poly(2, t0 + dt, par), 1e-9);

    }

    public void test030_getPoly() {
        Polynome po = Polynome.getPoly(1.0, 2.0, 3.0, 4.0);
        assertEquals("", 2.0, po.at(0, 1.0), 1e-9);
        assertEquals("", 4.0, po.at(0, 1.5), 1e-9);
        assertEquals("", 7.0, po.at(0, 2.0), 1e-9);

        assertEquals("", 3.0, po.at(1, 1.0), 1e-9);
        assertEquals("", 5.0, po.at(1, 1.5), 1e-9);
        assertEquals("", 7.0, po.at(1, 2.0), 1e-9);

        assertEquals("", 4.0, po.at(2, 1.0), 1e-9);
        assertEquals("", 4.0, po.at(2, 1.5), 1e-9);
        assertEquals("", 4.0, po.at(2, 2.0), 1e-9);
    }

    public void test040_load() {
        final double[] a = { 1.1, 1.2, 1.3, 1.4 };
        Polynome po = new Polynome(a);
        final int count = 500000;
        final long start = System.currentTimeMillis();
        for (int i = count - 1; i >= 0; i--)
            po.at(0, 1.1);
        double cps = count / (1e-3 * (System.currentTimeMillis() - start));
        assertTrue(cps > 2000000);
    }
}