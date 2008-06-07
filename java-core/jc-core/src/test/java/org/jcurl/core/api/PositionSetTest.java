/*
 * jcurl java curling software framework http://www.jcurl.org
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
package org.jcurl.core.api;

import java.awt.geom.Point2D;

import junit.framework.TestCase;

import org.jcurl.core.api.RockType.Pos;

/**
 * JUnit Test
 * 
 * @see org.jcurl.core.api.RockSetUtils
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PositionSetTest.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public class PositionSetTest extends TestCase {

	public void testFindRockAtPos() {
		final RockSet<Pos> a = RockSetUtils.allHome();
		a.getRock(1).setLocation(0, 0, 0);
		assertEquals(1, RockSetUtils.findRockIndexAtPos(a, new Point2D.Float(0,
				0)));
		assertEquals(1, RockSetUtils.findRockIndexAtPos(a, new Point2D.Float(
				Unit.f2m(0.4), 0)));
	}

	public void testGetShotRocks() {
		final RockSet<Pos> a = RockSetUtils.allHome();
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
			a.getRock(i).setLocation(0, i * 0.5, 0);
		assertEquals(1, RockSetUtils.getShotRocks(a));
	}
}