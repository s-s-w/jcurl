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
package jcurl.core;

import jcurl.core.dto.RockSetProps;
import jcurl.model.PositionSet;
import jcurl.model.SpeedSet;
import jcurl.sim.model.CollissionSimple;
import jcurl.sim.model.SlideStraight;
import junit.framework.TestCase;

/**
 * JUnit test for {@link jcurl.core.RockSetInterpolator}.
 * 
 * @see jcurl.core.RockSetInterpolator
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: RockSetInterpolatorTest.java 147 2005-10-23 12:48:46Z
 *          mrohrmoser $
 */
public class RockSetInterpolatorTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(RockSetInterpolatorTest.class);
    }

    public void test010_feed() {
        final long t0 = 0;
        PositionSet rPos = PositionSet.allHome();
        rPos.getLight(0).setLocation(0, 0);
        SpeedSet rSpeed = new SpeedSet();
        rSpeed.getLight(0).setX(0);
        rSpeed.getLight(0).setY(1);
        SlideStraight slid = new SlideStraight();
        assertTrue(slid.isDiscrete());

        slid.reset(t0, rPos, rSpeed, RockSetProps.DEFAULT);
        assertEquals("", t0, slid.getMinT(), 1e-6);

        final RockSetInterpolator ip = new RockSetInterpolator();

        final int loop = 5000;
        for (int i = 0; i < loop; i++) {
            long t1 = t0 + i * 2;
            PositionSet p1 = slid.getPos(t1, null);
            ip.setPos(t1, p1);
        }
        for (int i = loop - 2; i >= 0; i--) {
            long t1 = t0 + i * 2 + 1;
            ip.getPos(t1, null);
        }
    }
}