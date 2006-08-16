/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class FileChooser extends JFileChooser {

    protected final String description;

    protected final String[] suffixes;

    /**
     * @param currentFile
     */
    public FileChooser(File currentFile, final String[] suffixes,
            final String description) {
        super(currentFile == null ? new File(".") : currentFile);
        this.suffixes = suffixes;
        final StringBuffer buf = new StringBuffer(description);
        for (int i = 0; i < suffixes.length; i++)
            buf.append(" (").append(suffixes[i]).append(")");
        this.description = buf.toString();

        this.setMultiSelectionEnabled(false);
        this.setAcceptAllFileFilterUsed(true);
        this.setFileFilter(new FileFilter() {
            public boolean accept(final File f) {
                if (f == null)
                    return false;
                if (f.isDirectory())
                    return true;
                for (int i = suffixes.length - 1; i >= 0; i--)
                    if (f.getName().endsWith(suffixes[i]))
                        return true;
                return false;
            }

            public String getDescription() {
                return description;
            }
        });
    }

}
