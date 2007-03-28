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
package org.jcurl.core.base;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.swing.PositionDisplay;
import org.jcurl.core.swing.Zoomer;

public abstract class TestShowBase extends TestBase {

    public static interface TimeRunnable {
        public void run(double t) throws InterruptedException;
    }

    protected final int show;

    public TestShowBase(final int millis) {
        final StackTraceElement[] se = new RuntimeException().getStackTrace();
        boolean inEclipse = false;
        for (int i = se.length - 1; i >= 0; i--)
            if (se[i].getClassName().startsWith("org.eclipse.jdt")) {
                inEclipse = true;
                break;
            }
        show = inEclipse ? millis : -1;
    }

    public void doShowPositionDisplay(final PositionSet p, final TimeRunnable r) {
        if (show > 0) {
            final JFrame frame = new JFrame();
            frame.setBounds(0, 0, 800, 600);
            frame.setTitle(getClass().getName());
            final PositionDisplay display = new PositionDisplay();
            display.setZoom(Zoomer.HOG2HACK);
            display.setPos(p);
            frame.getContentPane().add(display);
            frame.show();

            final long t0 = System.currentTimeMillis();
            long loop = 0;
            try {
                while (System.currentTimeMillis() - t0 < show) {
                    r.run(1e-3 * (System.currentTimeMillis() - t0));
                    loop++;
                }
            } catch (final InterruptedException e) {
                ;
            }
            log.info("Loops of " + r.getClass().getName() + ".run(double t): "
                    + loop);
            frame.hide();
        }
    }

    private static final Log log = JCLoggerFactory
            .getLogger(TestShowBase.class);

}
