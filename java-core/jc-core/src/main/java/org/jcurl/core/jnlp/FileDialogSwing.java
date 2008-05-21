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
package org.jcurl.core.jnlp;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.jnlp.FileDialogService.Contents;
import org.jcurl.core.jnlp.FileDialogService.ContentsFile;
import org.jcurl.core.jnlp.FileDialogService.OpenService;
import org.jcurl.core.jnlp.FileDialogService.SaveService;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Swing based implementation to be used in trusted environments.
 * 
 * http://www.koders.com/java/fid88589626EC469B9526AB5888794D34914322B7A9.aspx
 * http://ajax.sourceforge.net/apollo/crossref/apollo/dev/DevFileOpenService.java.html
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
class FileDialogSwing implements OpenService, SaveService {
	private static final Log log = JCLoggerFactory
			.getLogger(FileDialogSwing.class);

	private static JFileChooser createFileChooser(final String pathHint,
			final String[] extensions, final boolean showDir) {
		final JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(false);
		fc.setAcceptAllFileFilterUsed(true);
		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(final File f) {
				if (f == null)
					return false;
				if (showDir && f.isDirectory())
					return true;
				for (final String element : extensions)
					if (f.getName().endsWith("." + element))
						return true;
				return false;
			}

			@Override
			public String getDescription() {
				final StringBuilder b = new StringBuilder();
				for (final String element : extensions)
					b.append("*.").append(element).append(", ");
				if (b.length() > 0)
					b.setLength(b.length() - 2);
				return b.toString();
			}
		});
		return fc;
	}

	public Contents openFileDialog(final String pathHint,
			final String[] extensions, final Component parent) {
		final JFileChooser fc = createFileChooser(pathHint, extensions, true);
		final int ret = fc.showOpenDialog(parent);
		switch (ret) {
		case 0:
			throw new NotImplementedYetException();
		case 1:
			return null;
		default:
			log.warn("Ignored Dialog Result " + ret);
			return null;
		}
	}

	public Contents saveAsFileDialog(final String pathHint,
			final String[] extensions, final Contents contents,
			final Component parent) {
		final JFileChooser fc = createFileChooser(pathHint, extensions, false);
		final int ret = fc.showSaveDialog(parent);
		switch (ret) {
		case 0:
			throw new NotImplementedYetException();
		case 1:
			return null;
		default:
			log.warn("Ignored Dialog Result " + ret);
			return null;
		}
	}

	public Contents saveFileDialog(final String pathHint,
			final String[] extensions, final InputStream stream,
			final String name, final Component parent) {
		final JFileChooser fc = createFileChooser(pathHint, extensions, true);
		final int ret = fc.showSaveDialog(parent);
		switch (ret) {
		case 0:
			try {
				final Contents c = new ContentsFile(fc.getSelectedFile());
				final FileOutputStream fo = new FileOutputStream(fc
						.getSelectedFile());
				// TODO fo.write(arg0)
				fo.close();
				return c;
			} catch (final IOException e) {
				throw new RuntimeException("Unhandled", e);
			}
		case 1:
			return null;
		default:
			log.warn("Ignored Dialog Result " + ret);
			return null;
		}
	}
}
