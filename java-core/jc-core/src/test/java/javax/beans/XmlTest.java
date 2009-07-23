/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2009 M. Rohrmoser
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

package javax.beans;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockSetUtils;
import org.jcurl.core.api.RockType.Pos;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class XmlTest extends TestCase {

	public void testRock() throws UnsupportedEncodingException {
		final String xml;
		{
			final ByteArrayOutputStream bout = new ByteArrayOutputStream();
			final XMLEncoder ec = new XMLEncoder(bout);
			ec.writeObject(new RockDouble<Pos>(1, 2, 3));
			ec.close();
			xml = new String(bout.toByteArray(), "UTF-8");
			assertEquals(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
							+ "<java version=\"1.5.0_19\" class=\"java.beans.XMLDecoder\"> \n"
							+ " <object class=\"org.jcurl.core.api.RockDouble\"> \n"
							+ "  <void property=\"a\"> \n"
							+ "   <double>3.0</double> \n" + "  </void> \n"
							+ " </object> \n" + "</java> \n" + "", xml);
		}
		final XMLDecoder de = new XMLDecoder(new ByteArrayInputStream(xml
				.getBytes("UTF-8")));
		final Rock<Pos> r = (Rock<Pos>) de.readObject();
		assertEquals("[0.0, 0.0, 3.0]", r.toString());
	}

	public void testRockSet() throws IOException {
		final String xml;
		{
			final ByteArrayOutputStream bout = new ByteArrayOutputStream();
			final XMLEncoder ec = new XMLEncoder(bout);
			ec.writeObject(RockSetUtils.allHome());
			ec.close();
			bout.close();
			xml = new String(bout.toByteArray(), "UTF-8");
			assertEquals(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
							+ "<java version=\"1.5.0_19\" class=\"java.beans.XMLDecoder\"> \n"
							+ "</java> \n", xml);
		}
		final XMLDecoder de = new XMLDecoder(new ByteArrayInputStream(xml
				.getBytes("UTF-8")));
		try {
			de.readObject();
			fail();
		} catch (final Exception e) {
			assertEquals("java.lang.ArrayIndexOutOfBoundsException", e
					.getClass().getName());
		}
	}
}
