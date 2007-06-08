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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.jcurl.core.base.Zoomer;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PPaintContext;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PiccoloExample {

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
        pico.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        pico.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        frame.getContentPane().add(pico);
        
        // some text:
        final PText a = new PText("Hello");
        a.setTextPaint(Color.BLUE);
        final PText b = new PText("World!");
        b.setTextPaint(Color.RED);
        pico.getLayer().addChild(a);
        a.addChild(b);
        b.translate(10, 20);

        // some curling:
        final PNode t = new PPath();
        t.addChild(IcePainter.create());
        final PNode r = RockPainter.create(0, true);
        r.translate(1 * Zoomer.SCALE, 1 * Zoomer.SCALE);
        t.addChild(r);
        pico.getLayer().addChild(t);
        
        frame.setVisible(true);
    }
}