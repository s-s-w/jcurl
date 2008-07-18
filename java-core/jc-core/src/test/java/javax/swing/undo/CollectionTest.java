package javax.swing.undo;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import junit.framework.TestCase;

/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
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

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class CollectionTest extends TestCase {

	public void testUnmodifiableCollection() {
		final Collection<String> rw = new LinkedList<String>();
		rw.add("1");
		final Collection<String> ro = Collections.unmodifiableCollection(rw);
		assertEquals(1, rw.size());
		assertEquals(1, ro.size());

		rw.add("2");
		assertEquals(2, rw.size());
		assertEquals(2, ro.size());
	}
}
