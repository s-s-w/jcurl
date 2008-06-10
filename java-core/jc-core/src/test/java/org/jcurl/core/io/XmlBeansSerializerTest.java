/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.jcurl.core.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class XmlBeansSerializerTest extends TestCase {

	private final JCurlSerializer io = new JCurlSerializer();

	public void testEmpty() throws IOException {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		io.write(new IOGroup(), bout, XmlBeansSerializer.class);
		bout.close();
		assertEquals("<!-- " + XmlBeansSerializer.class.getName() + " -->\n",
				new String(bout.toByteArray(), 0, 5 + XmlBeansSerializer.class
						.getName().length() + 5, "UTF-8"));

		assertEquals(
				"<!-- org.jcurl.core.io.XmlBeansSerializer -->\n"
						+ "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
						+ "<java version=\"1.5.0_15\" class=\"java.beans.XMLDecoder\"> \n"
						+ " <object class=\"org.jcurl.core.io.IOGroup\"/> \n"
						+ "</java> \n" + "", new String(bout.toByteArray(),
						"UTF-8"));

		final IOGroup d = (IOGroup) io.read(new ByteArrayInputStream(bout
				.toByteArray()));
		assertEquals(0, d.children().size());
		// assertNotNull(d.annotations().get(IONode.CreatedByUser));
	}

	public void testHammy() throws IOException {
		IOTrajectories l = new IOTrajectories();
		l.trajectories().add(JCurlSerializerTest.initHammy(null));
		l.annotations().put(IONode.CreatedByProgram, "value");

		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		io.write(l, bout, XmlBeansSerializer.class);

		assertEquals(
				"<!-- org.jcurl.core.io.XmlBeansSerializer -->\n"
						+ "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
						+ "<java version=\"1.5.0_15\" class=\"java.beans.XMLDecoder\"> \n"
						+ " <object class=\"org.jcurl.core.io.IOTrajectories\"/> \n"
						+ "</java> \n" + "", new String(bout.toByteArray(),
						"UTF-8"));

		final IONode d = io.read(new ByteArrayInputStream(bout.toByteArray()));
		l = (IOTrajectories) d;
		assertEquals(0, l.trajectories().size());
		// final ComputedTrajectorySet c = (ComputedTrajectorySet) l
		// .trajectories().get(0);
		// assertEquals(7, c.getAnnotations().size());
		// assertNotNull(c.getCollider());
		// assertNotNull(c.getCollissionDetector());
		// assertNotNull(c.getCurler());
		// assertNotNull(c.getCurrentPos());
		// assertNotNull(c.getCurrentSpeed());
		// assertNotNull(c.getCurveStore());
		// assertNotNull(c.getInitialPos());
		// assertNotNull(c.getInitialSpeed());
		// assertEquals(0, c.getPropertyChangeListeners().length);
	}

}
