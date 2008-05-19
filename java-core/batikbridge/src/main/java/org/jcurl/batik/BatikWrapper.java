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

package org.jcurl.batik;

import java.awt.Container;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class BatikWrapper {

	/**
	 * http://xmlgraphics.apache.org/batik/using/svg-generator.html
	 * 
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public static void renderSvg(final Container src, final OutputStream dst)
			throws IOException {
		// Get a DOMImplementation.
		final DOMImplementation domImpl = GenericDOMImplementation
				.getDOMImplementation();
		// Create an instance of the SVG Generator.
		final SVGGraphics2D svgGenerator = new SVGGraphics2D(domImpl
				.createDocument("http://www.w3.org/2000/svg", "svg", null));
		// Ask the test to render into the SVG Graphics2D implementation.
		src.paint(svgGenerator);
		// Finally, stream out SVG
		svgGenerator.stream(new OutputStreamWriter(dst, "UTF-8"), true);
	}
}
