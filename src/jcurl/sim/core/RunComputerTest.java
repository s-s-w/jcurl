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
package jcurl.sim.core;

import jcurl.core.RockSet;
import jcurl.core.RockSetInterpolator;
import jcurl.core.Source;
import jcurl.core.dto.RockSetProps;
import jcurl.sim.model.CollissionSimple;
import jcurl.sim.model.SlideSimple;
import junit.framework.TestCase;

/**
 * @see jcurl.sim.core.RunComputer
 * @see jcurl.sim.core.model.SlideSimpleTest
 * @see jcurl.sim.core.model.CollissionSimpleTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RunComputerTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(RunComputerTest.class);
    }

    public void test010_hit() {
        final RockSet pos = RockSet.allHome();
        pos.getDark(0).setLocation(0, 5);
        pos.getLight(0).setLocation(0.2, 4.5);
        final RockSet speed = new RockSet();
        speed.getDark(0).setLocation(0, -1.0);

        // dynamics engines
        final Source src = new RunComputer(new SlideSimple(),
                new CollissionSimple(), new RockSetInterpolator(),
                RockSetProps.DEFAULT, 0, pos, speed);
        final RockSet rs = RockSet.allHome();
        src.getPos(100, rs);
        assertEquals("", 0, rs.getDark(0).getX(), 1e-6);
        assertEquals("", 4.899999961, rs.getDark(0).getY(), 1e-6);

        src.getPos(200, rs);
        assertEquals("", 0, rs.getDark(0).getX(), 1e-6);
        assertEquals("", 4.799999923, rs.getDark(0).getY(), 1e-6);

        src.getPos(300, rs);
        assertEquals("", -0.015340462, rs.getDark(0).getX(), 1e-9);
        assertEquals("", 4.717716693, rs.getDark(0).getY(), 1e-6);
        assertEquals("", 0.215340465, rs.getLight(0).getX(), 1e-9);
        assertEquals("", 4.482281684, rs.getLight(0).getY(), 1e-6);

        src.getSpeed(300, rs);
        assertEquals("", -0.494853615, rs.getDark(0).getX(), 1e-9);
        assertEquals("", -0.428446561, rs.getDark(0).getY(), 1e-6);
        assertEquals("", 0.494853615, rs.getLight(0).getX(), 1e-9);
        assertEquals("", -0.571553409, rs.getLight(0).getY(), 1e-6);

        src.getSpeed(1000, rs);
        assertEquals("", -0.494853615, rs.getDark(0).getX(), 1e-9);
        assertEquals("", -0.428446561, rs.getDark(0).getY(), 1e-6);
        assertEquals("", 0.494853615, rs.getLight(0).getX(), 1e-9);
        assertEquals("", -0.571553409, rs.getLight(0).getY(), 1e-6);
    }
}