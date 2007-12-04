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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;

import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockProps;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PPaintContext;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PiccoloExample {

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
        final PPositionSet pos = new PPositionSet(new PRockFactory.Fancy(100));
        pos.setPositionSet(PositionSet.allOut());
        ice.addChild(pos);
        pos.addInputEventListener(new PPositionSetDrag());

        // some helpers:
        // pico.getLayer().addChild(new PPath(house));

        pico.getRoot().getDefaultInputManager().setKeyboardFocus(
                new KeyboardZoom(cam));
        frame.setVisible(true);
    }
}