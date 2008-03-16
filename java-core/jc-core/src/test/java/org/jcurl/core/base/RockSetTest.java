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
package org.jcurl.core.base;

import java.util.Iterator;

import junit.framework.TestCase;

public class RockSetTest extends TestCase {
    public void testCountBits() {
        assertEquals(8, RockSet.countBits(RockSet.DARK_MASK));
        assertEquals(8, RockSet.countBits(RockSet.LIGHT_MASK));
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

    public void testToIdx16() {
        for (int i = RockSet.ROCKS_PER_COLOR - 1; i >= 0; i--) {
            assertEquals(2 * i, RockSet.toIdx16(true, i));
            assertEquals(2 * i + 1, RockSet.toIdx16(false, i));
        }
    }
}
