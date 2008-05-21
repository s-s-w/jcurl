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
package org.jcurl.core.helpers;

import java.util.Comparator;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.jcurl.core.helpers.AgeCacheMap.AgeItem;

public class AgeCacheMapTest extends TestCase {
	public void testExpirer4h() {
		final AgeCacheMap<Integer, String> m = new AgeCacheMap<Integer, String>(
				4 * 60 * 60 * 1000L);
		m.put(1, "1", 1);
		m.put(2, "2", 2);
		m.put(3, "3", 2);
		assertEquals(3, m.size(0));
		assertEquals(3, m.size(1));
		assertEquals(3, m.size(2));
		assertEquals(3, m.size(3));
		assertEquals(3, m.size(0 + 4 * 60 * 60 * 1000L));
		assertEquals(2, m.size(1 + 4 * 60 * 60 * 1000L));
		assertEquals(0, m.size(2 + 4 * 60 * 60 * 1000L));
		assertEquals(0, m.size(3 + 4 * 60 * 60 * 1000L));
	}

	public void testExpirer4hComparator() {
		final Comparator<? super AgeItem<Integer>> comp = new AgeCacheMap<Integer, String>(
				new TreeMap<Integer, String>(), 4 * 60 * 60 * 1000L)
				.comparator();
		assertEquals(-1, comp.compare(new AgeItem<Integer>(1, 1),
				new AgeItem<Integer>(1, 2)));
		assertEquals(1, comp.compare(new AgeItem<Integer>(1, 2),
				new AgeItem<Integer>(1, 1)));
		assertEquals(0, comp.compare(new AgeItem<Integer>(1, 1),
				new AgeItem<Integer>(2, 1)));
	}
}
