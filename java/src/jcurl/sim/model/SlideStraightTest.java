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

import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.dto.RockDouble;
import jcurl.core.dto.RockSetProps;
import jcurl.math.CurveBase;
import junit.framework.TestCase;

/**
 * JUnit test.
 * 
 * @see jcurl.sim.model.SlideStraight
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SlideStraightTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SlideStraightTest.class);
    }

    private final SlideStraight s = new SlideStraight(new CollissionSimple());

    private final RockSet pos = RockSet.allOut();

    private final RockSet speed = new RockSet();

    public void setUp() {
        // initial state
        RockSet.allOut(pos);
        pos.getDark(0).setLocation(0, 5, 0);
        pos.getLight(0).setLocation(0.2, 2.5);
        pos.getLight(1).setLocation(1.0, 1.5);
        RockSet.allZero(speed);
        speed.getDark(0).setLocation(0, -1.0, 0.75);
        s.reset(0, pos, speed, RockSetProps.DEFAULT);
    }

    public void test010_createCurve() {
        Rock x0 = new RockDouble(0, 0, 0);
        Rock v0 = new RockDouble(0, 0, 0);
        CurveBase c = s.createCurve(0, x0, v0);
        assertEquals("", 0, c.getC(0, 0, 0), 1e-9);
        assertEquals("", 0, c.getC(1, 0, 0), 1e-9);
        assertEquals("", 0, c.getC(2, 0, 0), 1e-9);

        assertEquals("", 0, c.getC(0, 1, 0), 1e-9);
        assertEquals("", 0, c.getC(1, 1, 0), 1e-9);
        assertEquals("", 0, c.getC(2, 1, 0), 1e-9);

        assertEquals("", 0, c.getC(0, 0, 1), 1e-9);
        assertEquals("", 0, c.getC(1, 0, 1), 1e-9);
        assertEquals("", 0, c.getC(2, 0, 1), 1e-9);

        assertEquals("", 0, c.getC(0, 1, 1), 1e-9);
        assertEquals("", 0, c.getC(1, 1, 1), 1e-9);
        assertEquals("", 0, c.getC(2, 1, 1), 1e-9);

        x0 = new RockDouble(1, 2, 0);
        v0 = new RockDouble(0, 0, 0);
        c = s.createCurve(0, x0, v0);
        assertEquals("", 1, c.getC(0, 0, 0), 1e-9);
        assertEquals("", 2, c.getC(1, 0, 0), 1e-9);
        assertEquals("", 0, c.getC(2, 0, 0), 1e-9);

        assertEquals("", 0, c.getC(0, 1, 0), 1e-9);
        assertEquals("", 0, c.getC(1, 1, 0), 1e-9);
        assertEquals("", 0, c.getC(2, 1, 0), 1e-9);

        assertEquals("", 1, c.getC(0, 0, 1), 1e-9);
        assertEquals("", 2, c.getC(1, 0, 1), 1e-9);
        assertEquals("", 0, c.getC(2, 0, 1), 1e-9);

        assertEquals("", 0, c.getC(0, 1, 1), 1e-9);
        assertEquals("", 0, c.getC(1, 1, 1), 1e-9);
        assertEquals("", 0, c.getC(2, 1, 1), 1e-9);

        x0 = new RockDouble(1, 2, 0);
        v0 = new RockDouble(0.1, 0.2, 0);
        c = s.createCurve(0, x0, v0);
        assertEquals("", 1, c.getC(0, 0, 0), 1e-9);
        assertEquals("", 2, c.getC(1, 0, 0), 1e-9);
        assertEquals("", 0, c.getC(2, 0, 0), 1e-9);

        assertEquals("", 0.1, c.getC(0, 1, 0), 1e-9);
        assertEquals("", 0.2, c.getC(1, 1, 0), 1e-9);
        assertEquals("", 0, c.getC(2, 1, 0), 1e-9);

        assertEquals("", 1.07603611502, c.getC(0, 0, 1), 1e-9);
        assertEquals("", 2.15207223004, c.getC(1, 0, 1), 1e-9);
        assertEquals("", 0, c.getC(2, 0, 1), 1e-9);

        assertEquals("", 0.052072230042, c.getC(0, 1, 1), 1e-9);
        assertEquals("", 0.104144460085, c.getC(1, 1, 1), 1e-9);
        assertEquals("", 0, c.getC(2, 1, 1), 1e-9);
    }

    public void test100() {
        double t = 0;
        assertEquals("", 2.26999338899, t = s.estimateNextHit(pos, speed), 1e-6);
        s.getPos(t, pos);
        assertEquals("", 0, pos.getDark(0).getX(), 1e-6);
        assertEquals("", 3.006122589111328, pos.getDark(0).getY(), 1e-6);
        assertEquals("", 0, speed.getDark(0).getX(), 1e-6);
        assertEquals("", -1, speed.getDark(0).getY(), 1e-6);
        assertEquals("", 0.2, pos.getLight(0).getX(), 1e-6);
        assertEquals("", 2.5, pos.getLight(0).getY(), 1e-6);
        assertEquals("", 1.0, pos.getLight(1).getX(), 1e-6);
        assertEquals("", 1.5, pos.getLight(1).getY(), 1e-6);

        s.getSpeed(t, speed);
        assertEquals("", 00.36488267571, t = s.estimateNextHit(pos, speed),
                1e-6);
    }
}