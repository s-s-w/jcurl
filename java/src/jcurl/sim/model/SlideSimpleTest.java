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
package jcurl.sim.model;

import jcurl.core.RockSet;
import jcurl.core.dto.RockSetProps;
import junit.framework.TestCase;

/**
 * JUnit test.
 * 
 * @see jcurl.sim.model.SlideSimple
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SlideSimpleTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SlideSimpleTest.class);
    }

    public void test010_init() {
        final long t0 = 0;
        RockSet rPos = RockSet.allHome();
        rPos.getLight(0).setLocation(0, 0);
        RockSet rSpeed = new RockSet();
        rSpeed.getLight(0).setX(0);
        rSpeed.getLight(0).setY(1);
        SlideSimple slid = new SlideSimple(new CollissionSimple());
        assertTrue(slid.isDiscrete());

        slid.reset(t0, rPos, rSpeed, RockSetProps.DEFAULT);
        assertEquals("", t0, slid.getMinT(), 1e-6);

        final double nextH = slid.estimateNextHit(t0);
        assertEquals("", 9.223372e15, nextH, 1e9);
    }

    public void test020_loop() {
        final long t0 = 0;
        RockSet rPos = RockSet.allHome();
        rPos.getLight(0).setLocation(0, 0);
        RockSet rSpeed = new RockSet();
        rSpeed.getLight(0).setX(0);
        rSpeed.getLight(0).setY(1);
        SlideSimple slid = new SlideSimple(new CollissionSimple());
        assertTrue(slid.isDiscrete());

        slid.reset(t0, rPos, rSpeed, RockSetProps.DEFAULT);
        assertEquals("", t0, slid.getMinT(), 1e-6);

        for (int i = 0; i < 30000; i++) {
            long t1 = t0 + i;
            //assertEquals("", 40.0, slid.estimateNextHit(t1), 1e-6);
            RockSet p1 = slid.getPos(t1, null);
            RockSet v1 = slid.getSpeed(t1, null);
            //assertEquals(t1, p1.getTime());
            assertEquals("", 0, p1.getLight(0).getX(), 1e-6);
            assertEquals("", (t1 - t0) / 1e-3, p1.getLight(0).getY(), 1e-2);
            //assertEquals(t1, v1.getTime());
        }
    }
}