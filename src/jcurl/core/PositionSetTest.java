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

import junit.framework.TestCase;

import org.apache.ugli.LoggerFactory;
import org.apache.ugli.ULogger;

/**
 * JUnit Test
 * 
 * @see jcurl.core.PositionSet
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PositionSetTest extends TestCase {

    private static final ULogger log = LoggerFactory
            .getLogger(PositionSetTest.class);

    public static void main(String[] args) {
        junit.textui.TestRunner.run(PositionSetTest.class);
    }

    public void test010_CountBits() {
        log.info("Dark : " + Integer.toBinaryString(RockSet.DARK_MASK));
        System.out.println("Dark : "
                + Integer.toBinaryString(RockSet.DARK_MASK));
        System.out.println("Light: "
                + Integer.toBinaryString(RockSet.LIGHT_MASK));
        assertEquals(8, PositionSet.countBits(RockSet.DARK_MASK));
        assertEquals(8, PositionSet.countBits(RockSet.LIGHT_MASK));
    }

    public void test010_getShotRocks() {
        log.info("Dark : " + Integer.toBinaryString(RockSet.DARK_MASK));
        PositionSet a = new PositionSet();
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            a.getRock(i).setLocation(0, i * 0.5);
        int shot = PositionSet.getShotRocks(a);
        assertEquals(8, PositionSet.countBits(RockSet.DARK_MASK));
        assertEquals(8, PositionSet.countBits(RockSet.LIGHT_MASK));
    }
}