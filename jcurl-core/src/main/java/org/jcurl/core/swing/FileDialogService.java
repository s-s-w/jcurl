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
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Abstraction layer to be able to use <a
 * href="http://java.sun.com/javase/6/docs/jre/api/javaws/jnlp/javax/jnlp/FileOpenService.html">FileOpenService</a>
 * and <a
 * href="http://java.sun.com/javase/6/docs/jre/api/javaws/jnlp/javax/jnlp/FileSaveService.html">FileSaveService</a>
 * under the hood if available.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class FileDialogService {

    public interface Contents {
        boolean canRead();

        boolean canWrite();

        InputStream getInputStream();

        String getName();

        OutputStream getOutputStream(boolean overwrite);
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

    private OpenService open;

    private SaveService save;

    public FileDialogService() {
        try {
            // http://java.sun.com/docs/books/tutorial/uiswing/components/examples/JWSFileChooserDemo.java
            final FileDialogWebstart ws = new FileDialogWebstart();
            open = ws;
            save = ws;
        } catch (final Exception e) {
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
