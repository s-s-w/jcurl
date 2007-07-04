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
package org.jcurl.core.j3d;

import junit.framework.TestCase;

import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;

public class RockBridgeTest extends TestCase {

    public void testClone() {
        Rock r0 = new RockBridge().rock;
        Rock r1 = (Rock) r0.clone();
        // Instances differ:
        assertFalse(r0 == r1);
        // Backing Data also differs:
        r0.setLocation(1, 2, 3);
        assertTrue(r0.getX() > 0 && r0.getY() > 0 && r0.getA() > 0);
        assertFalse(r1.getX() > 0 || r1.getY() > 0 || r1.getA() > 0);
    }

    public void testPositionSet() {
        PositionSet p = new PositionSet(new RockBridge().rock);
        assertEquals(RockBridge.class.getName() + "$1", p.getRock(0).getClass()
                .getName());
    }
}
