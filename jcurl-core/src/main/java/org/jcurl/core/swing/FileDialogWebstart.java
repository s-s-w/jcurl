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
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.swing.FileDialogService.Contents;
import org.jcurl.core.swing.FileDialogService.OpenService;
import org.jcurl.core.swing.FileDialogService.SaveService;

/**
 * Fully reflection based implementation to avoid dependency.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
class FileDialogWebstart implements OpenService, SaveService {

    static class ContentsWS implements Contents {
        final Object[] empty = {};

        private String name;

        final Class[] nop = {};

        private final Object o;

        private boolean read;

        private boolean write;

        ContentsWS(final Object o) {
            this.o = o;
            try {
                read = ((Boolean) o.getClass().getMethod("canRead", nop)
                        .invoke(o, empty)).booleanValue();
                write = ((Boolean) o.getClass().getMethod("canWrite", nop)
                        .invoke(o, empty)).booleanValue();
                name = (String) o.getClass().getMethod("getName", nop).invoke(
                        o, empty);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        public boolean canRead() {
            return read;
        }

        public boolean canWrite() {
            return write;
        }

        public InputStream getInputStream() {
            try {
                return (InputStream) o.getClass().getMethod("getInputStream",
                        nop).invoke(o, empty);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        public String getName() {
            return name;
        }

        public OutputStream getOutputStream(final boolean b) {
            try {
                return (OutputStream) o.getClass().getMethod("getOutputStream",
                        new Class[] { Boolean.class }).invoke(o,
                        new Object[] { b });
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final Log log = JCLoggerFactory
            .getLogger(FileDialogWebstart.class);

    private static Contents wrap(final Object o) {
        if (o == null)
            return null;
        log.info(o);
        return new ContentsWS(o);
    }

    private final Object fos;

    private final Method open;

    private final Method save;

    private final Method saveAs;

    FileDialogWebstart() {
        try {
            fos = Class.forName("javax.jnlp.ServiceManager").getMethod(
                    "lookup", new Class[] { String.class }).invoke(null,
                    new Object[] { "javax.jnlp.FileOpenService" });
            log.debug(fos);
            open = fos.getClass().getMethod("openFileDialog",
                    new Class[] { String.class, String[].class });
            log.debug(open);
            saveAs = null;
            log.debug(saveAs);
            save = fos.getClass().getMethod(
                    "saveFileDialog",
                    new Class[] { String.class, String[].class,
                            InputStream.class, String.class });
            log.debug(saveAs);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Contents openFileDialog(final String pathHint,
            final String[] extensions, Component parent) {
        try {
            return wrap(open.invoke(fos, new Object[] { pathHint, extensions }));
        } catch (final Exception e) {
            throw new RuntimeException("Unhandled", e);
        }
    }

    public Contents saveAsFileDialog(final String pathHint,
            final String[] extensions, final Contents contents, Component parent) {
        try {
            return wrap(saveAs.invoke(fos,
                    new Object[] { pathHint, extensions, contents }));
        } catch (final Exception e) {
            throw new RuntimeException("Unhandled", e);
        }
    }

    public Contents saveFileDialog(final String pathHint,
            final String[] extensions, final InputStream stream,
            final String name, Component parent) {
        try {
            return wrap(save.invoke(fos, new Object[] { pathHint, extensions,
                    stream, name }));
        } catch (final Exception e) {
            throw new RuntimeException("Unhandled", e);
        }
    }
}
