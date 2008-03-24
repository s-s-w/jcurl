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

package org.jcurl.core.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class JCurlSerializer {
	/**
	 * Read and write JCurl data.
	 * 
	 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
	 * @version $Id:JCurlSerializer.java 682 2007-08-12 21:25:04Z mrohrmoser $
	 */
	public static interface Engine {
		IODocument read(InputStream src) throws IOException;

		void write(IODocument src, OutputStream dst) throws IOException;
	}

	private static final char CHAR = '\n';

	private static final String UTF_8 = "UTF-8";

	protected Engine id2Ser(final byte[] id) throws ClassNotFoundException,
			UnsupportedEncodingException, InstantiationException,
			IllegalAccessException {
		final Class<? extends Engine> deser = (Class<? extends Engine>) Class
				.forName(new String(id, UTF_8));
		return deser.newInstance();
	}

	protected boolean isGzipped(final String name) {
		return name.toString().endsWith(".jcz");
	}

	public IODocument read(final File src) throws IOException {
		return read(src.toURL());
	}

	public IODocument read(final InputStream src) throws IOException {
		final Class<? extends Engine> deser;
		final Engine d;
		try {
			final ByteArrayOutputStream buf = new ByteArrayOutputStream();
			for (;;) {
				final int b = src.read();
				if (b == CHAR)
					break;
				buf.write(b);
			}
			buf.close();
			d = id2Ser(buf.toByteArray());
		} catch (final Exception e) {
			final IOException io = new IOException();
			io.initCause(e);
			throw io;
		}
		return d.read(src);
	}

	public IODocument read(final URL src) throws IOException {
		InputStream is = src.openStream();
		try {
			if (isGzipped(src.getFile()))
				is = new GZIPInputStream(is);
			return read(is);
		} finally {
			is.close();
		}
	}

	protected byte[] ser2Id(final Engine d) throws UnsupportedEncodingException {
		return d.getClass().getName().getBytes(UTF_8);
	}

	public void write(final IODocument src, final File dst,
			final Class<? extends Engine> ser) throws IOException {
		OutputStream os = new FileOutputStream(dst);
		try {
			if (isGzipped(dst.getName()))
				os = new GZIPOutputStream(os);
			write(src, os, ser);
		} finally {
			os.close();
		}
	}

	public void write(final IODocument src, final OutputStream dst,
			final Class<? extends Engine> ser) throws IOException {
		final Engine d;
		try {
			d = ser.newInstance();
		} catch (final Exception e) {
			final IOException io = new IOException();
			io.initCause(e);
			throw io;
		}
		dst.write(ser2Id(d));
		dst.write(CHAR);
		d.write(src, dst);
	}
}