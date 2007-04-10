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

import java.util.Iterator;

import junit.framework.TestCase;

import org.jcurl.core.helpers.Dim;

public class RockSetTest extends TestCase {
    public void testCountBits() {
        assertEquals(8, RockSet.countBits(RockSet.DARK_MASK));
        assertEquals(8, RockSet.countBits(RockSet.LIGHT_MASK));
    }

    public void testToIdx16() {
        for (int i = RockSet.ROCKS_PER_COLOR - 1; i >= 0; i--) {
            assertEquals(2 * i, RockSet.toIdx16(true, i));
            assertEquals(2 * i + 1, RockSet.toIdx16(false, i));
        }
    }

    public void testIterators() throws InterruptedException {
        final PositionSet p = PositionSet.allHome();
        final int loops = 500000;
        long t0 = 0;
        long v = 0;
        String txt = null;
        // calm down a bit...
        Thread.sleep(100);

        txt = "plain for loop (downwards)";
        t0 = System.currentTimeMillis();
        for (int l = 0; l < loops; l++)
            for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
                v++;
        System.out.println(txt + ": " + (System.currentTimeMillis() - t0));

        txt = "plain for loop (upwards)";
        t0 = System.currentTimeMillis();
        for (int l = 0; l < loops; l++)
            for (int i = 0; i < RockSet.ROCKS_PER_SET; i++)
                v++;
        System.out.println(txt + ": " + (System.currentTimeMillis() - t0));

        txt = "itIdx Iterator";
        t0 = System.currentTimeMillis();
        for (int l = 0; l < loops; l++)
            for (final Iterator it = p.itIdx(); it.hasNext();) {
                it.next();
                v++;
            }
        System.out.println(txt + ": " + (System.currentTimeMillis() - t0));

        txt = "itRocks Iterator";
        t0 = System.currentTimeMillis();
        for (int l = 0; l < loops; l++)
            for (final Iterator it = p.itRocks(); it.hasNext();) {
                it.next();
                v++;
            }
        System.out.println(txt + ": " + (System.currentTimeMillis() - t0));

        txt = "itEntries Iterator";
        t0 = System.currentTimeMillis();
        for (int l = 0; l < loops; l++)
            for (final Iterator it = p.itEntries(); it.hasNext();) {
                it.next();
                v++;
            }
        System.out.println(txt + ": " + (System.currentTimeMillis() - t0));
        assertTrue(v != 0);
    }

    public static void initHammy(final PositionSet p, final SpeedSet s) {
        PositionSet.allOut(p);
        // te.getInitialPos().getLight(1-1).setLocation(
        p.getLight(2 - 1)
                .setLocation(Dim.f2m(-1.170732), Dim.f2m(15.365854), 0);
        p.getLight(3 - 1).setLocation(Dim.f2m(0.292683), Dim.f2m(8.780488), 0);
        p.getLight(4 - 1).setLocation(Dim.f2m(2.195122), Dim.f2m(12), 0);
        p.getLight(5 - 1).setLocation(Dim.f2m(1.463415), Dim.f2m(5.707317), 0);
        p.getLight(6 - 1).setLocation(Dim.f2m(1.463415), Dim.f2m(-2.780488), 0);
        p.getLight(7 - 1)
                .setLocation(Dim.f2m(-0.439024), Dim.f2m(-5.560976), 0);
        p.getLight(8 - 1)
                .setLocation(Dim.f2m(-1.756098), Dim.f2m(-1.609756), 0);
        // p.getDark(1-1).setLocation(
        // p.getDark(2-1).setLocation(
        p.getDark(3 - 1).setLocation(Dim.f2m(0.878049), Dim.f2m(14.341463), 0);
        p.getDark(4 - 1).setLocation(Dim.f2m(-2.634146), Dim.f2m(13.170732), 0);
        p.getDark(5 - 1).setLocation(Dim.f2m(4.536585), Dim.f2m(-0.439024), 0);
        p.getDark(6 - 1).setLocation(Dim.f2m(0.731707), Dim.f2m(-3.95122), 0);
        p.getDark(7 - 1).setLocation(Dim.f2m(-2.780488), Dim.f2m(-4.390244), 0);
        p.getDark(8 - 1).setLocation(Dim.f2m(3.89991), IceSize.HOG_2_TEE, 0);
        RockSet.allZero(s);
        s.getDark(7).setLocation(0, -3, 100 * Math.PI / 180);
    }
}
