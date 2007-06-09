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
package org.jcurl.core.zui;

import java.awt.Color;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.base.TestShowBase;
import org.jcurl.core.helpers.Dim;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.util.PPaintContext;

public class PiccoloBasicTest extends TestShowBase {

    static class HouseZoom extends PBasicInputEventHandler {
        private final PCamera cam;

        public HouseZoom(final PCamera c) {
            cam = c;
        }

        @Override
        public void keyPressed(final PInputEvent event) {
            switch (event.getKeyCode()) {
            case KeyEvent.VK_HOME:
            case KeyEvent.VK_H:
                event.setHandled(true);
                if (event.isControlDown())
                    cam.animateViewToCenterBounds(twelveP, true, 500);
                else
                    cam.animateViewToCenterBounds(houseP, true, 500);
                break;
            case KeyEvent.VK_END:
                event.setHandled(true);
                cam.animateViewToCenterBounds(sheetP, true, 500);
                break;
            default:
                ;
            }
        }
    }

    static class PPositionSetDrag extends PBasicInputEventHandler {
        private final PPositionSet pos;

        public PPositionSetDrag(final PPositionSet pos) {
            setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK));
            this.pos = pos;
            pos.addInputEventListener(this);
        }

        @Override
        public void mouseDragged(final PInputEvent event) {
            final PNode aNode = event.getPickedNode();
            if (!(aNode instanceof PRock))
                return;
            event.setHandled(true);
            final PRock n = (PRock) aNode;
            // TODO Add overlap/collission detection!
            n.r.setLocation(n.getParent().globalToLocal(event.getPosition()));
            pos.sync(n);
        }

        @Override
        public void mouseReleased(final PInputEvent arg0) {
            pos.p.notifyChange();
            super.mouseReleased(arg0);
        }
    }

    /** House area plus 1 rock margin plus "out" rock space. */
    static final Rectangle2D houseP;

    private static final long serialVersionUID = -8485372274509187133L;

    /**
     * Inter-hog area area plus house area plus 1 rock margin plus "out" rock
     * space.
     */
    static final Rectangle2D sheetP;

    static final Rectangle2D twelveP;

    static {
        final double r2 = 2 * RockProps.DEFAULT.getRadius();
        final double x = IceSize.SIDE_2_CENTER + r2;
        houseP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE + r2), 2 * x,
                IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
        final double c12 = r2 + Dim.f2m(6.0);
        twelveP = new Rectangle2D.Double(-c12, -c12, 2 * c12, 2 * c12);
        sheetP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_HOG
                + IceSize.HOG_2_TEE + r2), 2 * x, IceSize.HOG_2_HOG
                + IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
    }

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
        final PCamera cam = pico.getCamera();
        // // Shift origin to center of the frame:
        // cam.setViewOffset(0.5 * frame.getWidth(), 0.5 * frame.getHeight());
        // cam.setViewScale(50);
        frame.getContentPane().add(pico);

        // some curling:
        final PNode ice = IcePainter.create();
        pico.getLayer().addChild(ice);
        final PPositionSet pos = new PPositionSet(PositionSet.allOut());
        ice.addChild(pos);
        new PPositionSetDrag(pos);

        pico.getRoot().getDefaultInputManager().setKeyboardFocus(
                new HouseZoom(pico.getCamera()));
        frame.setVisible(true);
        cam.animateViewToCenterBounds(houseP, true, 1);
        while (frame.isVisible())
            Thread.sleep(100);
    }
}
