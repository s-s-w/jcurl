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
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RectangularShape;

import javax.swing.JFrame;

import org.jcurl.core.base.PositionSet;
import org.jcurl.core.model.FixpointZoomer;
import org.jcurl.core.zui.IcePainter;
import org.jcurl.core.zui.PPositionSet;
import org.jcurl.core.zui.PRock;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PPaintContext;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PiccoloExample {
    static class HouseZoom extends PBasicInputEventHandler {
        @Override
        public void keyPressed(final PInputEvent event) {
            System.out.println("Keypressed");
            switch (event.getKeyCode()) {
            case KeyEvent.VK_H:
                System.out.println("Keypressed H");
                event.setHandled(true);
                final PCamera cam = event.getCamera();
                final RectangularShape r0 = cam.getBoundsReference();
                final Rectangle r = new Rectangle((int) r0.getX(), (int) r0.getY(),
                        (int) r0.getWidth(), (int) r0.getHeight());
                FixpointZoomer.HOG2HACK.computeWctoDcTrafo(r, cam
                        .getTransformReference(false));
                cam.invalidatePaint();
                break;
            default:
                ;
            }
        }
    }

    static class PPositionSetDrag extends PBasicInputEventHandler {
        private final PPositionSet pos;

        public PPositionSetDrag(final PPositionSet pos) {
            this.pos = pos;
            pos.addInputEventListener(this);
        }

        @Override
        public void mouseDragged(final PInputEvent event) {
            final PNode aNode = event.getPickedNode();
            if (!(aNode instanceof PRock))
                return;
            final PRock n = (PRock) aNode;
            n.r.setLocation(n.getParent().globalToLocal(event.getPosition()));
            event.setHandled(true);
            pos.p.notifyChange();
        }
    }

    private static final long serialVersionUID = -8485372274509187133L;

    public static void main(final String[] args) {
        new PiccoloExample();
    }

    private final JFrame frame;

    private final PCanvas pico;

    public PiccoloExample() {
        frame = new JFrame("Piccolo Example");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                System.exit(0);
            }
        });

        frame.setBounds(0, 0, 800, 600);
        pico = new PCanvas();
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
        final PPositionSet pos = new PPositionSet(PositionSet.allOut());
        ice.addChild(pos);
        new PPositionSetDrag(pos);

        pico.addInputEventListener(new HouseZoom());
        frame.setVisible(true);
    }
}