/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
package org.jcurl.core.swing;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import junit.framework.TestCase;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class BatikDemoTest extends TestCase {

    public void test010() throws UnsupportedEncodingException,
            SVGGraphics2DIOException {
        // Get a DOMImplementation.
        final DOMImplementation domImpl = GenericDOMImplementation
                .getDOMImplementation();
        // Create an instance of org.w3c.dom.Document.
        final Document document = domImpl.createDocument(
                "http://www.w3.org/2000/svg", "svg", null);

        // Create an instance of the SVG Generator.
        final SVGGraphics2D g2 = new SVGGraphics2D(document);
        // g2.scale(-1, 1);

        // Ask the test to render into the SVG Graphics2D implementation.
        final PositionDisplay test = new PositionDisplay();
        test.setBounds(0, 0, 100, 100);
        test.paint2(g2, false);

        // Finally, stream out SVG to the standard output using
        // UTF-8 encoding.
        final Writer out = new OutputStreamWriter(System.out, "UTF-8");
        g2.stream(out, true);
        g2.dispose();
    }
}
