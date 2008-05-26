/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2008  M. Rohrmoser
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

public class MergedIteratorTest extends TestCase {

	public void testNonEmpty() {
		final Collection<Iterator<String>> c = new ArrayList<Iterator<String>>();
		{
			final Collection<String> a = new ArrayList<String>();
			a.add("1");
			a.add("3");
			a.add("3");
			a.add("5");
			c.add(a.iterator());

			c.add(new ArrayList<String>().iterator());

			final Collection<String> b = new ArrayList<String>();
			b.add("2");
			b.add("3");
			b.add("4");
			c.add(b.iterator());

			c.add(new ArrayList<String>().iterator());
		}
		final MergedIterator<String> it = new MergedIterator<String>(c);
		assertTrue(it.hasNext());
		assertEquals("1", it.next());
		assertEquals("2", it.next());
		assertEquals("3", it.next());
		assertEquals("3", it.next());
		assertEquals("3", it.next());
		assertEquals("4", it.next());
		assertEquals("5", it.next());
		assertFalse(it.hasNext());
		assertEquals(null, it.next());
	}

}
