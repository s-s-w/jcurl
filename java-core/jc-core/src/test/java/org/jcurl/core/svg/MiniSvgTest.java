/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
package org.jcurl.core.svg;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.jcurl.core.helpers.XmlSerializer;

public class MiniSvgTest extends TestCase {

	public void testCircle() {
		final StringWriter str = new StringWriter();
		final XmlSerializer dst = new XmlSerializer(str, false);
		final MiniSvg g = new MiniSvg(dst);
		g.drawArc(0, 1, 50, 100, 45, 270);
		g.dispose();
		assertEquals(
				"<?xml version=\"1.0\"?><svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.0\"><g stroke=\"black\" stroke-width=\"1.0\"><path d=\"M300,200 h-150 a150,150 0 1,0 150,-150 z\" fill=\"none\"></path></g></svg>",
				str.getBuffer().toString());
	}

	public void testEmpty() {
		final StringWriter str = new StringWriter();
		final XmlSerializer dst = new XmlSerializer(str, false);
		final MiniSvg g = new MiniSvg(dst);
		// g.drawLine(0, 0, 1, 1);
		g.dispose();
		assertEquals(
				"<?xml version=\"1.0\"?><svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.0\"><g stroke=\"black\" stroke-width=\"1.0\"></g></svg>",
				str.getBuffer().toString());
	}

	public void testLine() {
		final StringWriter str = new StringWriter();
		final XmlSerializer dst = new XmlSerializer(str, false);
		final MiniSvg g = new MiniSvg(dst);
		g.drawLine(0, 0, 100, -100);
		g.dispose();
		assertEquals(
				"<?xml version=\"1.0\"?><svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.0\"><g stroke=\"black\" stroke-width=\"1.0\"><line x1=\"0\" y1=\"0\" x2=\"100\" y2=\"100\" stroke-width=\"1\"></line></g></svg>",
				str.getBuffer().toString());
	}
}
