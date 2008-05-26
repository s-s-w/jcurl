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
package org.jcurl.core.jnlp;

import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Abstraction layer to be able to use <a
 * href="http://java.sun.com/javase/6/docs/jre/api/javaws/jnlp/javax/jnlp/FileOpenService.html">FileOpenService</a>
 * and <a
 * href="http://java.sun.com/javase/6/docs/jre/api/javaws/jnlp/javax/jnlp/FileSaveService.html">FileSaveService</a>
 * under the hood if available.
 * <p>
 * Does some tricks to be useable in untrusted Webstart Applications.
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class FileDialogService {

	public interface Contents {
		boolean canRead();

		boolean canWrite();

		InputStream getInputStream();

		long getLength();

		long getMaxLength();

		String getName();

		OutputStream getOutputStream(boolean overwrite);

		void setMaxLength(long maxLength);
	}

	public static class ContentsBuffer implements Contents {

		private final ByteArrayOutputStream bout = new ByteArrayOutputStream();

		private final String name;

		public ContentsBuffer(final String name, final byte[] file) {
			try {
				if (file != null)
					bout.write(file);
			} catch (final IOException e) {
				throw new RuntimeException("Unhandled", e);
			}
			this.name = name;
		}

		public boolean canRead() {
			return true;
		}

		public boolean canWrite() {
			return true;
		}

		public InputStream getInputStream() {
			return new ByteArrayInputStream(bout.toByteArray());
		}

		public long getLength() {
			return bout.size();
		}

		public long getMaxLength() {
			return Integer.MAX_VALUE;
		}

		public String getName() {
			return name;
		}

		public OutputStream getOutputStream(final boolean overwrite) {
			return bout;
		}

		public void setMaxLength(final long maxLength) {
		// nop;
		}

	}

	public static class ContentsFile implements Contents {

		private final File file;

		public ContentsFile(final File file) {
			this.file = file;
		}

		public boolean canRead() {
			return file.canRead();
		}

		public boolean canWrite() {
			return file.canWrite();
		}

		public InputStream getInputStream() {
			try {
				return new FileInputStream(file);
			} catch (final FileNotFoundException e) {
				return null;
			}
		}

		public long getLength() {
			return file.length();
		}

		public long getMaxLength() {
			return Long.MAX_VALUE;
		}

		public String getName() {
			return file.getName();
		}

		public OutputStream getOutputStream(final boolean overwrite) {
			try {
				return new FileOutputStream(file);
			} catch (final FileNotFoundException e) {
				return null;
			}
		}

		public void setMaxLength(final long maxLength) {
		// unused
		}

	}

	interface OpenService {
		Contents openFileDialog(String pathHint, String[] extensions,
				Component parent);
	}

	interface SaveService {
		Contents saveAsFileDialog(String pathHint, String[] extensions,
				Contents contents, Component parent);

		Contents saveFileDialog(String pathHint, String[] extensions,
				InputStream stream, String name, Component parent);
	}

	private static final Log log = JCLoggerFactory
			.getLogger(FileDialogService.class);

	private OpenService open;

	private SaveService save;

	public FileDialogService() {
		try {
			// http://java.sun.com/docs/books/tutorial/uiswing/components/examples/JWSFileChooserDemo.java
			final FileDialogWebstart ws = new FileDialogWebstart();
			open = ws;
			save = ws;
		} catch (final Exception e) {
			log.debug("Failed to use Webstart File Services, I use Swing");
			final FileDialogSwing ws = new FileDialogSwing();
			open = ws;
			save = ws;
		}

	}

	public Contents openFileDialog(final String pathHint,
			final String[] extensions, final Component parent) {
		return open.openFileDialog(pathHint, extensions, parent);
	}

	public Contents saveAsFileDialog(final String pathHint,
			final String[] extensions, final Contents contents,
			final Component parent) {
		return save.saveAsFileDialog(pathHint, extensions, contents, parent);
	}

	public Contents saveFileDialog(final String pathHint,
			final String[] extensions, final InputStream stream,
			final String name, final Component parent) {
		return save.saveFileDialog(pathHint, extensions, stream, name, parent);
	}
}
