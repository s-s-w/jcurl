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
package jcurl.math;

import junit.framework.TestCase;

/**
 * @see jcurl.math.CSplineInterpolator
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CSplineInterpolatorTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(CSplineInterpolatorTest.class);
    }

    public void test005_binarySearch() {
        double[] a = { 0, 1, 2, 3, 4 };
        assertEquals("", 0, CurveParts.binarySearch(a, 0, 0,
                a.length - 1));
        assertEquals("", 1, CurveParts.binarySearch(a, 1, 0,
                a.length - 1));
        assertEquals("", 4, CurveParts.binarySearch(a, 4, 0,
                a.length - 1));
        assertEquals("", -1, CurveParts.binarySearch(a, 1, 2,
                a.length - 1));
        assertEquals("", -2, CurveParts.binarySearch(a, 0.5, 0,
                a.length - 1));
    }

    public void test010() {
        final CSplineInterpolator ip = new CSplineInterpolator();
        ip.add(0, 0);
        ip.add(1, 1);
        ip.add(2, 0);
        ip.add(3, 1);
        ip.add(4, 0);
        ip.add(5, 1);
        ip.add(6, 0);
        ip.add(7, 1);
        ip.add(8, 0);
        assertEquals("", 0.0, ip.getC0(0), 1e-6);
        assertEquals("", 0.774484, ip.getC0(0.5), 1e-6);
        assertEquals("", 1.0, ip.getC0(1), 1e-6);
        assertEquals("", 0.426546, ip.getC0(1.5), 1e-6);
        assertEquals("", 0.0, ip.getC0(2), 1e-6);
        assertEquals("", 0.519329, ip.getC0(2.5), 1e-6);
        assertEquals("", 1.0, ip.getC0(3), 1e-6);
        assertEquals("", 0.496134, ip.getC0(3.5), 1e-6);
        assertEquals("", 0.0, ip.getC0(4), 1e-6);
        assertEquals("", 0.496134, ip.getC0(4.5), 1e-6);
        assertEquals("", 1.0, ip.getC0(5), 1e-6);
        assertEquals("", 0.519329, ip.getC0(5.5), 1e-6);
        assertEquals("", 0.0, ip.getC0(6), 1e-6);
        assertEquals("", 0.426546, ip.getC0(6.5), 1e-6);
        assertEquals("", 1.0, ip.getC0(7), 1e-6);
        assertEquals("", 0.774484, ip.getC0(7.5), 1e-6);
        assertEquals("", 0.0, ip.getC0(8), 1e-6);
        try {
            ip.getC0(-0.1);
            fail("Too small");
        } catch (ArrayIndexOutOfBoundsException e) {
            ;
        }
        try {
            ip.getC0(8.1);
            fail("Too large");
        } catch (ArrayIndexOutOfBoundsException e) {
            ;
        }
        for (int i = 1000000; i >= 0; i--)
            ip.getC0(2);
    }
}