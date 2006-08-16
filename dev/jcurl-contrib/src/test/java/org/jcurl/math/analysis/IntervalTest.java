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
package org.jcurl.math.analysis;

import java.util.Arrays;

import junit.framework.TestCase;

public class IntervalTest extends TestCase {

    public void testBasics() {
        assertTrue(Double.isInfinite(Double.NEGATIVE_INFINITY));
        assertTrue(Double.isInfinite(Double.POSITIVE_INFINITY));
        assertTrue(Double.NEGATIVE_INFINITY < Double.MIN_VALUE);
        assertTrue(Double.NEGATIVE_INFINITY == Double.NEGATIVE_INFINITY);
        assertFalse(Double.NEGATIVE_INFINITY < Double.NEGATIVE_INFINITY);
        assertTrue(Double.NEGATIVE_INFINITY < Double.POSITIVE_INFINITY);
        assertTrue(Double.POSITIVE_INFINITY == Double.POSITIVE_INFINITY);
        assertFalse(Double.POSITIVE_INFINITY < Double.POSITIVE_INFINITY);
        assertTrue(Double.POSITIVE_INFINITY > Double.MAX_VALUE);
        assertFalse(Double.isInfinite(Double.NaN));
        assertFalse(Double.isInfinite(-1));
        assertFalse(Double.isInfinite(0));
        assertFalse(Double.isInfinite(1));
        assertTrue(Double.isNaN(Double.NaN));
        assertFalse(Double.isNaN(Double.NEGATIVE_INFINITY));
        assertFalse(Double.isNaN(Double.POSITIVE_INFINITY));
        assertFalse(Double.isNaN(-1));
        assertFalse(Double.isNaN(0));
        assertFalse(Double.isNaN(1));
        assertFalse(Double.NaN < 0);
        assertFalse(Double.NaN > 0);
        assertFalse(Double.NaN == 0);
        assertFalse(Double.NaN == Double.NaN);
        assertFalse(Double.NaN > Double.NaN);
        assertFalse(Double.NaN < Double.NaN);
    }

    public void testCompareTo() {
        final Interval[] v = new Interval[3];
        v[0] = new Interval(Double.NEGATIVE_INFINITY, false, -1, true);
        v[1] = new Interval(-1, false, 2, true);
        v[2] = new Interval(-1, true, 3, true);
        Arrays.sort(v);
        assertEquals("", Double.NEGATIVE_INFINITY, v[0].min, 1e-10);
        assertEquals("", -1, v[0].max, 1e-10);
        assertEquals("", -1, v[1].min, 1e-10);
        assertEquals("", 3, v[1].max, 1e-10);
        assertEquals("", -1, v[2].min, 1e-10);
        assertEquals("", 2, v[2].max, 1e-10);
    }

    public void testCtor() {
        Interval a = new Interval(Double.NEGATIVE_INFINITY, false,
                Double.POSITIVE_INFINITY, false);
        a = new Interval(Double.NEGATIVE_INFINITY, false, -1, true);
        a = new Interval(1, true, 1, true);
        a = new Interval(1, false, 2, false);
        try {
            a = new Interval(2, false, 1, false);
            fail("min > max");
        } catch (IllegalArgumentException e) {
            ;
        }
        try {
            a = new Interval(1, true, 1, false);
            fail("min == max and min_included != max_included");
        } catch (IllegalArgumentException e) {
            ;
        }
        try {
            a = new Interval(1, false, 1, false);
            fail("min == max and min_included != max_included");
        } catch (IllegalArgumentException e) {
            ;
        }
        try {
            a = new Interval(1, true, Double.NaN, false);
            fail("");
        } catch (IllegalArgumentException e) {
            ;
        }
        try {
            a = new Interval(Double.NaN, true, Double.NaN, false);
            fail("");
        } catch (IllegalArgumentException e) {
            ;
        }
        try {
            a = new Interval(Double.NaN, true, 1, false);
            fail("");
        } catch (IllegalArgumentException e) {
            ;
        }
    }

    public void testIsWithin() {
        Interval a = new Interval(Double.NEGATIVE_INFINITY, false, -1, true);
        assertFalse(Interval.isWithin(a, Double.NEGATIVE_INFINITY));
        assertFalse(Interval.isWithin(a, Double.NaN));
        assertFalse(Interval.isWithin(a, Double.POSITIVE_INFINITY));
        assertTrue(Interval.isWithin(a, -2));
        assertTrue(Interval.isWithin(a, -1));
        assertFalse(Interval.isWithin(a, 0));

        a = new Interval(-1, true, -1, true);
        assertFalse(Interval.isWithin(a, -2));
        assertTrue(Interval.isWithin(a, -1));
        assertFalse(Interval.isWithin(a, 0));

        a = new Interval(-1, true, 1, true);
        assertFalse(Interval.isWithin(a, -2));
        assertTrue(Interval.isWithin(a, -1));
        assertTrue(Interval.isWithin(a, 0));
        assertTrue(Interval.isWithin(a, 1));
        assertFalse(Interval.isWithin(a, 2));

        a = new Interval(-1, false, 1, true);
        assertFalse(Interval.isWithin(a, -2));
        assertFalse(Interval.isWithin(a, -1));
        assertTrue(Interval.isWithin(a, 0));
        assertTrue(Interval.isWithin(a, 1));
        assertFalse(Interval.isWithin(a, 2));

        a = new Interval(-1, true, 1, false);
        assertFalse(Interval.isWithin(a, -2));
        assertTrue(Interval.isWithin(a, -1));
        assertTrue(Interval.isWithin(a, 0));
        assertFalse(Interval.isWithin(a, 1));
        assertFalse(Interval.isWithin(a, 2));

        a = new Interval(-1, false, 1, false);
        assertFalse(Interval.isWithin(a, -2));
        assertFalse(Interval.isWithin(a, -1));
        assertTrue(Interval.isWithin(a, 0));
        assertFalse(Interval.isWithin(a, 1));
        assertFalse(Interval.isWithin(a, 2));
    }

    public void testNoOverlap() {
        Interval a = new Interval(-1, false, 1, true);
        Interval b = new Interval(2, false, 3, true);
        assertEquals(null, Interval.overlap(a, b));
        assertEquals(null, Interval.overlap(b, a));

        a = new Interval(-1, false, 2, true);
        b = new Interval(2, false, 3, true);
        assertEquals(null, Interval.overlap(a, b));
        assertEquals(null, Interval.overlap(b, a));
    }

    public void testPartialOverlap() {
        Interval a = new Interval(-1, true, 2, false);
        Interval b = new Interval(1, false, 3, true);
        Interval o = Interval.overlap(a, b);
        assertEquals("", 1, o.min, 1e-10);
        assertEquals(false, o.min_included);
        assertEquals("", 2, o.max, 1e-10);
        assertEquals(false, o.max_included);
        o = Interval.overlap(b, a);
        assertEquals("", 1, o.min, 1e-10);
        assertEquals(false, o.min_included);
        assertEquals("", 2, o.max, 1e-10);
        assertEquals(false, o.max_included);

        a = new Interval(-1, true, 2, true);
        b = new Interval(1, false, 3, true);
        o = Interval.overlap(a, b);
        assertEquals("", 1, o.min, 1e-10);
        assertEquals(false, o.min_included);
        assertEquals("", 2, o.max, 1e-10);
        assertEquals(true, o.max_included);
        o = Interval.overlap(b, a);
        assertEquals("", 1, o.min, 1e-10);
        assertEquals(false, o.min_included);
        assertEquals("", 2, o.max, 1e-10);
        assertEquals(true, o.max_included);

        a = new Interval(-1, true, 2, false);
        b = new Interval(1, true, 3, true);
        o = Interval.overlap(a, b);
        assertEquals("", 1, o.min, 1e-10);
        assertEquals(true, o.min_included);
        assertEquals("", 2, o.max, 1e-10);
        assertEquals(false, o.max_included);
        o = Interval.overlap(b, a);
        assertEquals("", 1, o.min, 1e-10);
        assertEquals(true, o.min_included);
        assertEquals("", 2, o.max, 1e-10);
        assertEquals(false, o.max_included);
    }

    public void testSingularOverlap() {
        Interval a = new Interval(-1, false, 1, true);
        Interval b = new Interval(1, true, 3, true);
        Interval o = Interval.overlap(a, b);
        assertEquals("", 1, o.min, 1e-10);
        assertEquals(true, o.min_included);
        assertEquals("", 1, o.max, 1e-10);
        assertEquals(true, o.max_included);
        o = Interval.overlap(b, a);
        assertEquals("", 1, o.min, 1e-10);
        assertEquals(true, o.min_included);
        assertEquals("", 1, o.max, 1e-10);
        assertEquals(true, o.max_included);
    }

    public void testTotalOverlap() {
        Interval a = new Interval(-1, false, 3, true);
        Interval b = new Interval(1, true, 2, true);
        Interval o = Interval.overlap(a, b);
        assertEquals("", 1, o.min, 1e-10);
        assertEquals(true, o.min_included);
        assertEquals("", 2, o.max, 1e-10);
        assertEquals(true, o.max_included);
        o = Interval.overlap(b, a);
        assertEquals("", 1, o.min, 1e-10);
        assertEquals(true, o.min_included);
        assertEquals("", 2, o.max, 1e-10);
        assertEquals(true, o.max_included);
    }
}
