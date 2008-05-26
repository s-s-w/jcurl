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
package org.jcurl.core.xsio;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.log.JCLoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;

public class XppMiniTest extends TestCase {
	private static final Log log = JCLoggerFactory.getLogger(XppMiniTest.class);

	public void testTrivial() {
		final XStream xs = new XStream();
		assertEquals("<string>Hello, world</string>", xs.toXML("Hello, world"));

		final Rock r = new RockDouble(1, 2, 3);
		assertEquals("<org.jcurl.core.api.RockDouble>\n" + "  <a>3.0</a>\n"
				+ "  <x>1.0</x>\n" + "  <y>2.0</y>\n"
				+ "</org.jcurl.core.api.RockDouble>", xs.toXML(r));
	}

	public void testTrivial2() {
		final XStream xs = new XStream() {
			final String rootElem = "wrap";

			final String rootNS = "mynamespace";

			private HierarchicalStreamReader checkRoot(
					HierarchicalStreamReader arg0) {
				if (rootElem.equals(arg0.getNodeName())
						&& rootNS.equals(arg0.getAttribute("xmlns"))) {
					log.debug("Found korrekt root!");
					arg0.moveDown();
				}
				return arg0;
			}

			@Override
			public String toXML(Object arg0) {
				try {
					StringWriter w = new StringWriter();
					toXML(arg0, w);
					w.close();
					return w.getBuffer().toString();
				} catch (IOException e) {
					throw new RuntimeException("Unhandled", e);
				}
			}

			@Override
			public void toXML(Object arg0, OutputStream arg1) {
				throw new NotImplementedYetException();
			}

			@Override
			public void toXML(Object arg0, Writer arg1) {
				try {
					arg1.write("<?xml version='1.0'?>\n<");
					arg1.write(rootElem);
					arg1.write(" xmlns='");
					arg1.write(rootNS);
					arg1.write("'>\n");
					super.toXML(arg0, arg1);
					arg1.write("<");
					arg1.write(rootElem);
					arg1.write(">");
				} catch (IOException e) {
					throw new RuntimeException("Unhandled", e);
				}
			}

			@Override
			public Object unmarshal(HierarchicalStreamReader arg0) {
				return super.unmarshal(checkRoot(arg0));
			}

			@Override
			public Object unmarshal(HierarchicalStreamReader arg0, Object arg1) {
				return super.unmarshal(checkRoot(arg0), arg1);
			}

			@Override
			public Object unmarshal(HierarchicalStreamReader arg0, Object arg1,
					DataHolder arg2) {
				return super.unmarshal(checkRoot(arg0), arg1, arg2);
			}
		};
		assertEquals(
				"Hello, world",
				xs
						.fromXML("<?xml version='1.0'?>\n<wrap xmlns='mynamespace'><string>Hello, world</string></wrap>"));

		assertEquals("<?xml version='1.0'?>\n" + "<wrap xmlns='mynamespace'>\n"
				+ "<string>Hallo, Welt!</string><wrap>", xs
				.toXML("Hallo, Welt!"));

		assertEquals("Hallo, Welt!!", xs.fromXML(xs.toXML("Hallo, Welt!!")));
	}
}
