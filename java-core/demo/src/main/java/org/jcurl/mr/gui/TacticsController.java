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
package org.jcurl.mr.gui;

import java.awt.Component;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.ComputedTrajectorySet;
import org.jcurl.core.base.JCurlSerializer;
import org.jcurl.core.base.JCurlSerializer.Payload;
import org.jcurl.core.io.XStreamSerializer;
import org.jcurl.core.jnlp.FileDialogService.Contents;
import org.jcurl.core.log.JCLoggerFactory;

public class TacticsController {
    private static final String[] jcxExt = { "jcx", "jcz" };

    private static final Log log = JCLoggerFactory
            .getLogger(TacticsController.class);

    private static final String[] pngExt = { "png" };

    final Model m;

    final DetailPanel p;

    private final Component parent;

    private Thread running = null;

    private URL url;

    private final Cursor wc;

    public TacticsController(final Component parent, final DetailPanel p,
            final Model m) {
        this.parent = parent;
        this.p = p;
        this.m = m;
        new RockPositionMouseController(m.getInitialPos(), p.ice, false);
        wc = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    }

    public URL exportPng(File f) {
        log.info(f);
        if (f.isDirectory())
            throw new IllegalArgumentException(
                    "Is not a file but a directory: " + f);
        if (!f.getName().endsWith(".png"))
            f = new File(f.getAbsoluteFile() + ".png");
        final Cursor c = parent.getCursor();
        try {
            parent.setCursor(wc);
            p.exportPng(f, "created by " + parent.getClass().getName());
            return f.toURL();
        } catch (final MalformedURLException e) {
            throw new RuntimeException("Unhandled", e);
        } catch (final IOException e) {
            throw new RuntimeException("Unhandled", e);
        } finally {
            parent.setCursor(c);
        }
    }

    public void exportPng(final OutputStream f) {
        log.info(f);
        final Cursor c = parent.getCursor();
        try {
            parent.setCursor(wc);
            p.exportPng(f, "created by " + parent.getClass().getName());
        } catch (final MalformedURLException e) {
            throw new RuntimeException("Unhandled", e);
        } catch (final IOException e) {
            throw new RuntimeException("Unhandled", e);
        } finally {
            parent.setCursor(c);
        }
    }

    public File getFile() {
        if (url == null || !"file".equals(url.getProtocol()))
            return null;
        try {
            return new File(url.toURI());
        } catch (final URISyntaxException e) {
            throw new RuntimeException("Unhandled", e);
        }
    }

    public String[] getJcxExt() {
        return jcxExt;
    }

    public String getPathHint() {
        return url == null ? "" : url.getFile();
    }

    public String[] getPngExt() {
        return pngExt;
    }

    public URL getUrl() {
        return url;
    }

    public URL open(final Contents co) {
        log.info(co.getName());
        final Cursor c = parent.getCursor();
        try {
            parent.setCursor(wc);
            final JCurlSerializer io = new XStreamSerializer();
            return url = open(null, io.read(co.getInputStream(), co.getName(),
                    null));
        } catch (final IOException e) {
            log.error("Failed opening [" + co.getName() + "]", e);
            return null;
        } finally {
            parent.setCursor(c);
        }
    }

    public URL open(final File f) {
        if (f.isDirectory())
            throw new IllegalArgumentException(
                    "Is not a file but a directory: " + f);
        try {
            return open(f.toURL());
        } catch (final IOException e) {
            throw new RuntimeException("Unhandled", e);
        }
    }

    public URL open(final URL url) throws IOException {
        log.info(url);
        final Cursor c = parent.getCursor();
        try {
            parent.setCursor(wc);
            final JCurlSerializer io = new XStreamSerializer();
            return open(url, io.read(url, null));
        } finally {
            parent.setCursor(c);
        }
    }

    URL open(final URL url, final Payload co) {
        switch (co.getTrajectories().length) {
        case 0:
            return url;
        case 1:
            m.setTrajectory((ComputedTrajectorySet) co.getTrajectories()[0]);
            return url;
        default:
            // TODO show a list? Keep them all?
            m.setTrajectory((ComputedTrajectorySet) co.getTrajectories()[0]);
            return url;
        }
    }

    public synchronized void pause() {
        if (running != null) {
            running.interrupt();
            running = null;
        } else
            play((long) (1e3 * m.getTrajectory().getCurrentTime()));
    }

    public synchronized void play(final long offMillis) {
        log.debug("Play!!");
        if (running != null)
            running.interrupt();
        running = new Thread() {
            @Override
            public void run() {
                log.debug("run");
                try {
                    final long t0 = System.currentTimeMillis() - offMillis;
                    for (;;) {
                        final long dt = System.currentTimeMillis() - t0;
                        setTime(dt);
                        if (dt > 25000)
                            return;
                        sleep(20);
                    }
                } catch (final InterruptedException e) {
                    log.debug("Interrupt");
                    return;
                } finally {
                    log.debug("stop");
                    running = null;
                }
            }
        };
        running.start();
    }

    public URL save(File f) {
        log.info(f);
        if (f.isDirectory())
            throw new IllegalArgumentException(
                    "Is not a file but a directory: " + f);
        if (!(f.getName().endsWith(".jcx") || f.getName().endsWith(".jcz")))
            f = new File(f.getAbsoluteFile() + ".jcz");
        final Cursor c = parent.getCursor();
        try {
            parent.setCursor(wc);
            return f.toURL();
        } catch (final MalformedURLException e) {
            throw new RuntimeException("Unhandled", e);
        } finally {
            parent.setCursor(c);
        }
    }

    private void setTime(final long millis) {
        m.getTrajectory().setCurrentTime(1e-3 * millis);
    }

    public synchronized void stop() {
        if (running != null) {
            running.interrupt();
            running = null;
        }
        setTime(0);
    }
}