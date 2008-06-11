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
package org.jcurl.core.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.jcurl.core.api.RockSet;

public class CollissionStoreTest extends TestCase {

	public void testAdd() {
		final CollissionStore s = new CollissionStore();
		s.add(2, 3, 4);
		assertEquals(1, s.m.size());
		s.add(1, 1, 2);
		assertEquals(2, s.m.size());
		// s.put(2, 3, 4);
		assertEquals(2, s.m.size());
		assertEquals(2, s.first().a);
	}

	public void testAddNaNLast() {
		final CollissionStore s = new CollissionStore();
		s.add(2, 3, 4);
		s.add(Double.NaN, 5, 6);
		s.add(1, 1, 2);
		assertEquals(3, s.m.size());
		assertEquals(2, s.first().a);
		// assertEquals(6, ((Tupel) s.m.lastKey()).a);

		s.replace(1, 2, Double.NaN);
		assertEquals(3, s.m.size());
		assertEquals(4, s.first().a);

		s.replace(3, 4, Double.NaN);
		assertEquals(3, s.m.size());
		assertEquals("", Double.NaN, s.first().t, 1e-9);
	}

	public void testCount() {
		int k = 0;
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
			for (int j = i - 1; j >= 0; j--)
				k++;
		assertEquals(120, k);
	}

	public void testSortedList() {
		final Comparator<CollissionStore.Tupel> co = new CollissionStore.TupelComp();
		final LinkedList<CollissionStore.Tupel> s = new LinkedList<CollissionStore.Tupel>();

		s.add(new CollissionStore.Tupel(2, 3, 4));
		s.add(new CollissionStore.Tupel(1, 1, 2));
		assertEquals(2, s.size());
		Collections.sort(s, co);
		assertEquals(2, (s.getFirst()).a);

		final CollissionStore.Tupel o = new CollissionStore.Tupel(Double.NaN,
				1, 2);
		s.removeFirst();
		s.add(o);
		Collections.sort(s, co);

		assertEquals(2, s.size());
		assertEquals(4, (s.getFirst()).a);
	}

	public void testSortedSet() {
		final SortedSet<CollissionStore.Tupel> s = new TreeSet<CollissionStore.Tupel>(
				new CollissionStore.TupelComp());
		s.add(new CollissionStore.Tupel(2, 3, 4));
		s.add(new CollissionStore.Tupel(1, 1, 2));
		assertEquals(2, s.size());
		assertEquals(2, (s.first()).a);

		assertTrue(s.remove(s.first()));
		assertTrue(s.add(new CollissionStore.Tupel(Double.NaN, 1, 2)));
		// assertFalse(s.add(new CollissionStore.Tupel(Double.NaN, 1, 2)));
		assertEquals(2, s.size());
		assertEquals(4, (s.first()).a, 1e-9);
	}

	public void testTupel() {
		final CollissionStore.Tupel[] a = new CollissionStore.Tupel[2];
		a[0] = new CollissionStore.Tupel(2, 3, 4);
		a[1] = new CollissionStore.Tupel(1, 1, 2);
		Arrays.sort(a, new CollissionStore.TupelComp());
		assertEquals(2, a[0].a);
		assertEquals(4, a[1].a);

		final SortedMap<CollissionStore.Tupel, CollissionStore.Tupel> m = new TreeMap<CollissionStore.Tupel, CollissionStore.Tupel>(
				new CollissionStore.TupelComp());
		m.put(a[1], a[1]);
		m.put(a[0], a[0]);
		m.put(a[1], a[1]);
		assertEquals(2, m.size());
		assertEquals(a[0], m.firstKey());
	}
}
