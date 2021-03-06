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

package org.jcurl.core.helpers;

import java.awt.Container;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.zip.GZIPOutputStream;

/**
 * "Client-side" of <code>org.jcurl.batik.BatikWrapper</code>. Introduces
 * <b>NO</b> compile time dependency but checks the availability at runtime
 * (static initializer) per reflection ({@link Class#forName(String)}).
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class BatikButler {
	/** <code>null</code> if batik is not available */
	private static final Method renderSvg;

	static {
		Method m = null;
		try {
			final Class<?> b = Class.forName("org.jcurl.batik.BatikWrapper");
			if (b != null)
				m = b.getMethod("renderSvg", new Class[] { Container.class,
						OutputStream.class });
		} catch (final Exception e) {
			m = null;
		}
		renderSvg = m;
	}

	public boolean isBatikAvailable() {
		return renderSvg != null;
	}

	/**
	 * Convenience wrapper for {@link #renderSvg(Container, OutputStream)}. If
	 * the filename ends with "<code>.svgz</code>" the output is gzipped.
	 * 
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public void renderSvg(final Container src, final File dst)
			throws IOException {
		OutputStream str = new FileOutputStream(dst);
		try {
			if (dst.getName().endsWith(".svgz"))
				str = new GZIPOutputStream(str);
			renderSvg(src, str);
		} finally {
			str.close();
		}
	}

	/**
	 * Delegate to the the <code>renderSvg</code> method of
	 * <code>org.jcurl.batik.BatikWrapper</code>.
	 * 
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public void renderSvg(final Container src, final OutputStream dst)
			throws IOException {
		try {
			renderSvg.invoke(null, new Object[] { src, dst });
		} catch (final IllegalArgumentException e) {
			throw new RuntimeException("Unhandled", e);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Unhandled", e);
		} catch (final InvocationTargetException e) {
			if (e.getCause() instanceof IOException)
				throw (IOException) e.getCause();
			throw new RuntimeException("Unhandled", e);
		}
	}
}
