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
package org.jcurl.core.swing;

import java.awt.Component;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.TestBase;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.Zoomer;

public abstract class TestShowBase extends TestBase {

    public static abstract class TimeRunnable {
        public abstract void run(double t) throws InterruptedException;

        public void run(final double t, final Component p)
                throws InterruptedException {
            run(t);
        }
    }

    private static final Log log = JCLoggerFactory
            .getLogger(TestShowBase.class);

    private static final boolean showGui;

    static {
        final StackTraceElement[] se = new RuntimeException().getStackTrace();
        boolean inEclipse = false;
        for (int i = se.length - 1; i >= 0; i--)
            if (se[i].getClassName().startsWith("org.eclipse.jdt")) {
                inEclipse = true;
                break;
            }
        showGui = inEclipse;
    }

    protected final PositionDisplay display;

    protected final JFrame frame;

    public TestShowBase() {
        this(800, 600);
    }

    public TestShowBase(final int dx, final int dy) {
        frame = showGui ? new JFrame() : null;
        if (frame != null) {
            display = new PositionDisplay();
            frame.setBounds(0, 0, dx, dy);
            frame.setTitle(getClass().getName());
            frame.getContentPane().add(display);
        } else
            display = null;
    }

    public int showPositionDisplay(final RockSet<Pos> p, final Zoomer zoom,
            final long millis, final TimeRunnable r) {
        if (frame == null)
            return -1;
        display.setZoom(zoom);
        display.setPos(p);
        frame.setVisible(true);

        final long t0 = System.currentTimeMillis();
        int loop = 0;
        try {
            while (System.currentTimeMillis() - t0 < millis) {
                r.run(1e-3 * (System.currentTimeMillis() - t0), display);
                loop++;
            }
        } catch (final InterruptedException e) {
            log.warn("Oops", e);
        }
        frame.setVisible(false);
        return loop;
    }

    @Override
    public void tearDown() {
        if (frame != null)
            frame.setVisible(false);
    }

}
