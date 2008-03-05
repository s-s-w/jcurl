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

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

public class PeekIteratorTest extends TestCase {

    public void testEmpty() {
        final Iterable<String> i = new ArrayList<String>();
        PeekIterator<String> it = new PeekIterator<String>(i.iterator());
        assertFalse(it.hasNext());
        assertNull(it.peek());
        assertNull(it.next());

        it = new PeekIterator<String>(i.iterator());
        assertNull(it.peek());
        assertNull(it.next());
        assertFalse(it.hasNext());

        it = new PeekIterator<String>(i.iterator());
        assertNull(it.next());
        assertFalse(it.hasNext());
        assertNull(it.peek());

        it = new PeekIterator<String>(i.iterator());
        assertNull(it.peek());
        assertFalse(it.hasNext());
        assertNull(it.next());

        it = new PeekIterator<String>(i.iterator());
        assertFalse(it.hasNext());
        assertNull(it.next());
        assertNull(it.peek());
    }

    public void testNonEmpty() {
        final Collection<String> i = new ArrayList<String>();
        i.add("1");
        i.add("2");
        i.add("3");

        final PeekIterator<String> it = new PeekIterator<String>(i.iterator());
        assertTrue(it.hasNext());
        assertEquals("1", it.peek());
        assertEquals("1", it.next());
        assertEquals("2", it.peek());
        assertEquals("2", it.next());
        assertEquals("3", it.peek());
        assertTrue(it.hasNext());
        assertEquals("3", it.next());
        assertFalse(it.hasNext());
        assertEquals(null, it.peek());
        assertEquals(null, it.next());
    }
}
