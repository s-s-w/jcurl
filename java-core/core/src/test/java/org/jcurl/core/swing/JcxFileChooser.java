/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005 M. Rohrmoser
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

class JcxFileChooser extends JFileChooser {

    private static final long serialVersionUID = -1903818463336848079L;

    public JcxFileChooser() {
        this(null);
    }

    public JcxFileChooser(final File currentFile) {
        super(currentFile == null ? new File(".") : currentFile);
        setMultiSelectionEnabled(false);
        setAcceptAllFileFilterUsed(true);
        setFileFilter(new FileFilter() {
            @Override
            public boolean accept(final File f) {
                if (f == null)
                    return false;
                return f.isDirectory() || f.getName().endsWith(".jcx")
                        || f.getName().endsWith(".jcz");
            }

            @Override
            public String getDescription() {
                return "JCurl Setup Files (.jcx) (.jcz)";
            }
        });
    }
}