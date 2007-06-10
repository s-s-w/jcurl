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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;

import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockProps;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.util.PPaintContext;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PiccoloExample {

    static class KeyboardZoom extends PBasicInputEventHandler {
        private final PCamera cam;

        public KeyboardZoom(final PCamera c) {
            cam = c;
        }

        @Override
        public void keyPressed(final PInputEvent event) {
            System.out.println("Keypressed");
            switch (event.getKeyCode()) {
            case KeyEvent.VK_HOME:
            case KeyEvent.VK_H:
                event.setHandled(true);
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

        public PPositionSetDrag() {
            setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK));
        }

        @Override
        public void mouseDragged(final PInputEvent event) {
            final PNode node = event.getPickedNode();
            final int i16 = ((Integer) node.getAttribute(PPositionSet.index16))
                    .intValue();
            final PositionSet pp = (PositionSet) node
                    .getAttribute(PositionSet.class);
            final Rock r = pp.getRock(i16);
            // TODO Add overlap/collission detection!
            event.setHandled(true);
            r.setLocation(node.getParent().globalToLocal(event.getPosition()));
            PPositionSet.sync(r, node);
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

    static {
        final double r2 = 2 * RockProps.DEFAULT.getRadius();
        final double x = IceSize.SIDE_2_CENTER + r2;
        houseP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE + r2), 2 * x,
                IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
        sheetP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_HOG
                + IceSize.HOG_2_TEE + r2), 2 * x, IceSize.HOG_2_HOG
                + IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
    }

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
        final PCamera cam = pico.getCamera();
        // Shift origin to center of the frame:
        cam.setViewOffset(0.5 * frame.getWidth(), 0.5 * frame.getHeight());
        cam.setViewScale(50);
        frame.getContentPane().add(pico);

        // some curling:
        final PNode ice = new PIceFactory.Fancy().newInstance();
        pico.getLayer().addChild(ice);
        final PPositionSet pos = new PPositionSet(PositionSet.allOut(),
                new PRockFactory.Fancy());
        ice.addChild(pos);
        pos.addInputEventListener(new PPositionSetDrag());

        // some helpers:
        // pico.getLayer().addChild(new PPath(house));

        pico.getRoot().getDefaultInputManager().setKeyboardFocus(
                new KeyboardZoom(cam));
        frame.setVisible(true);
    }
}