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
package org.jcurl.math;

import junit.framework.TestCase;

public class R1RNFunctionTest extends TestCase {

    public void testIsInside() {
        assertTrue(R1RNFunction.isInside(0, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, true));
        assertTrue(R1RNFunction.isInside(0, Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY, true));
        assertFalse(R1RNFunction.isInside(Double.NaN, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, true));
        assertFalse(R1RNFunction.isInside(Double.NaN, Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY, true));
        assertFalse(R1RNFunction.isInside(Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true));
        assertFalse(R1RNFunction.isInside(Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, true));

        assertTrue(R1RNFunction.isInside(0, Double.NEGATIVE_INFINITY,
                Double.NaN, true));
        assertTrue(R1RNFunction.isInside(0, Double.POSITIVE_INFINITY,
                Double.NaN, true));
        assertTrue(R1RNFunction.isInside(0, Double.NaN, Double.NaN, true));

        assertTrue(R1RNFunction.isInside(0, 0, Double.NEGATIVE_INFINITY, true));
        assertTrue(R1RNFunction.isInside(-1, 0, Double.NEGATIVE_INFINITY, true));
        assertFalse(R1RNFunction.isInside(1, 0, Double.NEGATIVE_INFINITY, true));

        assertFalse(R1RNFunction.isInside(0, 3, 1, true));
        assertTrue(R1RNFunction.isInside(1, 3, 1, true));
        assertTrue(R1RNFunction.isInside(2, 3, 1, true));
        assertTrue(R1RNFunction.isInside(3, 3, 1, true));
        assertFalse(R1RNFunction.isInside(4, 3, 1, true));

        assertTrue(R1RNFunction.isInside(1, 1, 1, true));
    }
}
