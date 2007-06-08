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
package org.jcurl.core.jazz;

import java.awt.Color;

import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.TestShowBase;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PPaintContext;

public class PiccoloBasicTest extends TestShowBase {

    private final PCanvas pico;

    public PiccoloBasicTest() {
        super();
        if (frame != null) {
            frame.getContentPane().remove(display);
            frame.getContentPane().add(pico = new PCanvas());
        } else
            pico = null;
    }

    public void testThroughPut() throws InterruptedException {
        if (frame == null)
            return;

        pico.setBackground(new Color(0xE8E8FF));
        pico.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        pico.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        // Shift origin to center of the frame:
        pico.getLayer().offset(0.5 * frame.getWidth(), 0.5 * frame.getHeight());
        pico.getLayer().scale(50);
        frame.getContentPane().add(pico);

        // some curling:
        final PNode ice = IcePainter.create();
        pico.getLayer().addChild(ice);
        ice.addChild(new PPositionSet(PositionSet.allOut()));
        
        frame.setVisible(true);
        while (frame.isVisible())
            Thread.sleep(100);
    }
}
