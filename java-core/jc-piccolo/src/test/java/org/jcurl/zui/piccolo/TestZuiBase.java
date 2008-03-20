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
package org.jcurl.zui.piccolo;

import java.awt.Color;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.zui.piccolo.KeyboardZoom;
import org.jcurl.zui.piccolo.PIceFactory;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PPaintContext;

public abstract class TestZuiBase extends TestShowBase {

    private static final Log log = JCLoggerFactory.getLogger(TestZuiBase.class);

    private static final long serialVersionUID = -8485372274509187133L;

    protected final PNode ice;

    protected final PCanvas pico;

    public TestZuiBase() {
        this(800, 600);
    }

    public TestZuiBase(final int dx, final int dy) {
        super(dx, dy);
        if (frame != null) {
            frame.getContentPane().add(pico = new PCanvas());
            pico.setBackground(new Color(0xE8E8FF));
            pico
                    .setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
            pico
                    .setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
            final PCamera cam = pico.getCamera();
            pico.getRoot().getDefaultInputManager().setKeyboardFocus(
                    new KeyboardZoom(cam));

            pico.getLayer().addChild(
                    ice = new PIceFactory.Fancy().newInstance());

            frame.setVisible(true);
            // start with a sensible viewport:
            cam.animateViewToCenterBounds(KeyboardZoom.HousePlus, true, 1);
            frame.setVisible(false);
        } else {
            pico = null;
            ice = null;
        }
    }

    public int show(final long millis, final TimeRunnable r) {
        if (frame == null)
            return -1;
        frame.setVisible(true);

        final long t0 = System.currentTimeMillis();
        int loop = 0;
        try {
            while (System.currentTimeMillis() - t0 < millis) {
                r.run(1e-3 * (System.currentTimeMillis() - t0), null);
                loop++;
            }
        } catch (final InterruptedException e) {
            log.warn("Oops", e);
        }
        frame.setVisible(false);
        return loop;
    }
}
