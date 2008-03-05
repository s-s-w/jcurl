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
import java.util.Iterator;

import junit.framework.TestCase;

public class FilterIteratorTest extends TestCase {

    private static final class MyFilter<E> extends FilterIterator<E> {
        protected MyFilter(final Iterator<E> base) {
            super(base);
        }

        @Override
        protected boolean matches(final E item) {
            return "2".equals(item) || "4".equals(item);
        }
    }

    public void testEmpty() {
        final Iterable<String> i = new ArrayList<String>();
        Iterator<String> it = new MyFilter<String>(i.iterator());
        assertFalse(it.hasNext());
        assertNull(it.next());

        it = new MyFilter<String>(i.iterator());
        assertNull(it.next());
        assertFalse(it.hasNext());
    }

    public void testFilteredEmpty() {
        final Collection<String> i = new ArrayList<String>();
        i.add("1");
        i.add("3");
        i.add("3");
        i.add("5");

        Iterator<String> it = new MyFilter<String>(i.iterator());
        assertFalse(it.hasNext());
        assertNull(it.next());

        it = new MyFilter<String>(i.iterator());
        assertNull(it.next());
        assertFalse(it.hasNext());
    }
    
    public void testNonEmpty() {
        final Collection<String> i = new ArrayList<String>();
        i.add("1");
        i.add("2");
        i.add("3");
        i.add("3");
        i.add("4");
        i.add("4");
        i.add("5");

        final Iterator<String> it = new MyFilter<String>(i.iterator());
        assertTrue(it.hasNext());
        assertEquals("2", it.next());
        assertTrue(it.hasNext());
        assertEquals("4", it.next());
        assertTrue(it.hasNext());
        assertEquals("4", it.next());
        assertFalse(it.hasNext());
        assertEquals(null, it.next());
    }
}
