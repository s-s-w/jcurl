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
package org.jcurl.core.model;

import junit.framework.TestCase;

public class PolynomeBaseTest extends TestCase {

    public void test010_fak() {
        assertEquals(1, PolynomeBase.factorial(0, 0));
        assertEquals(1, PolynomeBase.factorial(1, 1));
        assertEquals(2, PolynomeBase.factorial(2, 1));
        assertEquals(6, PolynomeBase.factorial(3, 1));
        assertEquals(3, PolynomeBase.factorial(3, 2));
        assertEquals(24, PolynomeBase.factorial(4, 1));
        assertEquals(12, PolynomeBase.factorial(4, 2));

        assertEquals(1, PolynomeBase.factorial(3, 3));
        assertEquals(1, PolynomeBase.factorial(2, 2));
        assertEquals(1, PolynomeBase.factorial(1, 1));
        assertEquals(1, PolynomeBase.factorial(0, 0));
    }

    public void test020_value() {
        final double[] a = { 1.1, 1.2, 1.3, 1.4 };
        Polynome po = new PolynomeBase(a);

        // C0
        double x = 1.5;
        double y = a[0] + a[1] * x + a[2] * x * x + a[3] * x * x * x;
        assertEquals("", y, po.value(x, 0), 1e-9);
        x = 2.5;
        y = a[0] + a[1] * x + a[2] * x * x + a[3] * x * x * x;
        assertEquals("", y, po.value(x, 0), 1e-9);

        // C1
        x = 1.5;
        y = 1 * a[1] + 2 * a[2] * x + 3 * a[3] * x * x;
        assertEquals("", y, po.value(x, 1), 1e-9);
        x = 2.5;
        y = 1 * a[1] + 2 * a[2] * x + 3 * a[3] * x * x;
        assertEquals("", y, po.value(x, 1), 1e-9);

        // C2
        x = 1.5;
        y = 1 * 2 * a[2] + 2 * 3 * a[3] * x;
        assertEquals("", y, po.value(x, 2), 1e-9);
        x = 2.5;
        y = 1 * 2 * a[2] + 2 * 3 * a[3] * x;
        assertEquals("", y, po.value(x, 2), 1e-9);

        // C3
        x = 1.5;
        y = 1 * 2 * 3 * a[3];
        assertEquals("", y, po.value(x, 3), 1e-9);
        x = 2.5;
        y = 1 * 2 * 3 * a[3];
        assertEquals("", y, po.value(x, 3), 1e-9);

        // C4
        x = 1.5;
        y = 0;
        assertEquals("", y, po.value(x, 4), 1e-9);
        x = 2.5;
        y = 0;
        assertEquals("", y, po.value(x, 4), 1e-9);
    }

    public void test029_getPolyParams() {
        double t0 = 2.894295921183459;
        double dt = 0;

        double v = 0.11913533326608741;
        double a = -0.1071697516342192;
        double[] par = PolynomeBase.getPolyParams(t0, 0, v, a);

        assertEquals("", 0, PolynomeBase.evaluate(par, t0 + dt, 0), 1e-9);
        assertEquals("", v, PolynomeBase.evaluate(par, t0 + dt, 1), 1e-9);
        assertEquals("", a, PolynomeBase.evaluate(par, t0 + dt, 2), 1e-9);

        dt = 0.1;
        assertEquals("", 0.0113776845, PolynomeBase.evaluate(par, t0 + dt, 0),
                1e-9);
        assertEquals("", 0.1084183581, PolynomeBase.evaluate(par, t0 + dt, 1),
                1e-9);
        assertEquals("", a, PolynomeBase.evaluate(par, t0 + dt, 2), 1e-9);

    }

    public void test030_getPoly() {
        Polynome po = PolynomeBase.getPoly(1.0, 2.0, 3.0, 4.0);
        assertEquals("", 2.0, po.value(1.0, 0), 1e-9);
        assertEquals("", 4.0, po.value(1.5, 0), 1e-9);
        assertEquals("", 7.0, po.value(2.0, 0), 1e-9);

        assertEquals("", 3.0, po.value(1.0, 1), 1e-9);
        assertEquals("", 5.0, po.value(1.5, 1), 1e-9);
        assertEquals("", 7.0, po.value(2.0, 1), 1e-9);

        assertEquals("", 4.0, po.value(1.0, 2), 1e-9);
        assertEquals("", 4.0, po.value(1.5, 2), 1e-9);
        assertEquals("", 4.0, po.value(2.0, 2), 1e-9);
    }

    public void test040_load() {
        final double[] a = { 1.1, 1.2, 1.3, 1.4 };
        Polynome po = new PolynomeBase(a);
        final int count = 500000;
        final long start = System.currentTimeMillis();
        for (int i = count - 1; i >= 0; i--)
            po.value(1.1, 0);
        double cps = count / (1e-3 * (System.currentTimeMillis() - start));
        assertTrue(cps > 1000000);
    }

    // public void testCompare() {
    // final double[] coeff = { 1.1, 1.2, 1.3 };
    // final double[] points = { -1.1, -1, 0, 1, 1.1, 100 };
    // final PolynomeBase pa = new PolynomeBase(coeff);
    // final PolynomeBase pj = new PolynomeBase(coeff);
    // for (int i = points.length - 1; i >= 0; i--)
    // assertEquals("", pa.value(points[i]), pj.value(points[i]), 1e-11);
    //
    // final PolynomialFunction pa1 = pa.polynomialDerivative();
    // for (int i = points.length - 1; i >= 0; i--)
    // assertEquals("", pa1.value(points[i]), pj.value(1, points[i]), 1e-11);
    //
    // final PolynomialFunction pa2 = pa1.polynomialDerivative();
    // for (int i = points.length - 1; i >= 0; i--)
    // assertEquals("", pa2.value(points[i]), pj.value(2, points[i]), 1e-11);
    //
    // final PolynomialFunction pj1 = pj.polynomialDerivative();
    // for (int i = points.length - 1; i >= 0; i--)
    // assertEquals("", pj1.value(points[i]), pj.value(1, points[i]), 1e-11);
    //
    // final PolynomialFunction pj2 = pj1.polynomialDerivative();
    // for (int i = points.length - 1; i >= 0; i--)
    // assertEquals("", pj2.value(points[i]), pj.value(2, points[i]), 1e-11);
    // }

    public void testToString() {
        assertEquals("p(x) = 2.0*x^2 + 1.0*x^0", new PolynomeBase(new double[] {
                1, 0, 2 }).toString());
        assertEquals("p(x) = -2.0*x^2 + 1.0*x^0", new PolynomeBase(
                new double[] { 1, 0, -2 }).toString());
        assertEquals("p(x) = -2.0*x^2 - 1.0*x^0", new PolynomeBase(
                new double[] { -1, 0, -2 }).toString());
    }

}
