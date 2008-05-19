/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
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

package org.jcurl.batik;

import java.awt.Container;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;

/**
 * Hide all <a href="http://xmlgraphics.apache.org/batik/">Batik SVG</a> behind
 * an interface, that's easy to use per reflection.
 * 
 * <h3>Typical usage</h3>
 * <h4>Determine the existence of batik libs in the classloader</h4>
 * 
 * <pre>
 * // null if batik is not available
 * private static final Method renderSvg;
 * 
 * static {
 * 	Method m = null;
 * 	try {
 * 		final Class&lt;?&gt; b = Class.forName(&quot;org.jcurl.batik.BatikWrapper&quot;);
 * 		if (b != null)
 * 			m = b.getMethod(&quot;renderSvg&quot;, new Class[] { Container.class,
 * 					OutputStream.class });
 * 	} catch (final Exception e) {
 * 		m = null;
 * 	}
 * 	renderSvg = m;
 * }
 * </pre>
 * 
 * <h4>Export any given {@link Container} to svg</h4>
 * 
 * <pre>
 * Container src = ...;
 * File dst = new File(&quot;/tmp/demo.svgz&quot;)
 * OutputStream str = new FileOutputStream(dst);
 * if (dst.getName().endsWith(&quot;.svgz&quot;))
 * 	str = new GZIPOutputStream(str);
 * renderSvg.invoke(null, new Object[] { src, str });
 * str.close();
 * </pre>
 * 
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
