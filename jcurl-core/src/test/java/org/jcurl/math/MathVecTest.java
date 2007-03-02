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
public class MathVecTest extends TestCase {

    public void test001() {
        int[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        assertEquals(2, a.length);
        assertEquals(3, a[0].length);

        assertEquals(1, a[0][0]);
        assertEquals(4, a[1][0]);
        assertEquals(2, a[0][1]);
        assertEquals(5, a[1][1]);
        assertEquals(3, a[0][2]);
        assertEquals(6, a[1][2]);
    }

    /**
     * Testdata from Numerische Mathematik, H. R. Schwarz, B. G. Teubner Verlag,
     * 1998, S.21f.
     * 
     * @see MathVec#gauss(double[][], double[], double[])
     */
    public void test010_gauss() {
        final double[][] a = { { 2.1, 2512, -2516 }, { -1.3, 8.8, -7.6 },
                { 0.9, -6.2, 4.6 } };
        final double[] b = { -6.5, 5.3, -2.9 };
        final double[] x = { 0, 0, 0 };
        MathVec.gauss(a, b, x);
        assertEquals(1.0, x[2], 1e-4);
        assertEquals(1.0, x[1], 3e-3);
        assertEquals(5.0, x[0], 2e-2);
    }
}