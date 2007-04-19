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

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.swing.FileDialogService.Contents;
import org.jcurl.core.swing.FileDialogService.OpenService;
import org.jcurl.core.swing.FileDialogService.SaveService;

/**
 * http://www.koders.com/java/fid88589626EC469B9526AB5888794D34914322B7A9.aspx
 * http://ajax.sourceforge.net/apollo/crossref/apollo/dev/DevFileOpenService.java.html
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
class FileDialogSwing implements OpenService, SaveService {

    private static class ContentsSwing implements Contents {

        private final File file;

        ContentsSwing(final File file) {
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

    }

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
                    if (f.getName().endsWith(element))
                        return true;
                return false;
            }

            @Override
            public String getDescription() {
                final StringBuffer b = new StringBuffer();
                for (final String element : extensions)
                    b.append(element).append(", ");
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
            return new ContentsSwing(fc.getSelectedFile());
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
            return new ContentsSwing(fc.getSelectedFile());
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
        final JFileChooser fc = createFileChooser(pathHint, extensions, false);
        final int ret = fc.showSaveDialog(parent);
        switch (ret) {
        case 0:
            return new ContentsSwing(fc.getSelectedFile());
        case 1:
            return null;
        default:
            log.warn("Ignored Dialog Result " + ret);
            return null;
        }
    }
}
