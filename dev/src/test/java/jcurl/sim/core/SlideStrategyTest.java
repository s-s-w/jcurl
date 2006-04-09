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

import junit.framework.TestCase;

import org.jcurl.core.PositionSet;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SlideStrategyTest extends TestCase {

    public void test005_nextHit() {
        PositionSet pos = PositionSet.allHome();
        pos.getDark(0).setLocation(0, 5);
        pos.getLight(0).setLocation(0.2, 4.0);
        PositionSet speed = new PositionSet();
        speed.getDark(0).setLocation(0, -1);

        double dt = SlideStrategy.tst_timetilhit(0, pos.getDark(0), speed
                .getDark(0), 8, pos.getLight(0), speed.getLight(0));
        assertEquals("", 0.7699933889987538, dt, 1e-9);
    }
}