/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
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
package jcurl.exp.mr.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Iterator;

import javax.swing.JPanel;

import jcurl.exp.mr.math.PointList;

import org.apache.log4j.Logger;


/**
 * Draw lines if the "hot" key is pressed.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class MouseSketchPanel extends JPanel implements KeyListener {

    private static final Logger log = Logger.getLogger(MouseSketchPanel.class);

    /**
     * Paint the given curve.
     * 
     * @param g
     * @param c
     */
    private static void paint(final Graphics g, final PointList c) {
        final Iterator it = c.points();
        if (!it.hasNext())
            return;
        Point p = (Point) it.next();
        while (it.hasNext()) {
            final Point p2 = (Point) it.next();
            g.drawLine(p.x, p.y, p2.x, p2.y);
            p = p2;
        }
    }

    private Point current = null;

    private final PointList curve = new PointList();

    private final char hotKey;

    private boolean isHot = false;

    public MouseSketchPanel(final char hotKey) {
        this.hotKey = hotKey;
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                if (isHot)
                    lineTo(e.getPoint());
            }
        });
        addKeyListener(this);
    }

    public PointList getCurve() {
        return curve;
    }

    public boolean isFocusTraversable() {
        return true;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == hotKey) {
            log.debug("HotKey pressed");
            isHot = true;
        }
        switch (e.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
            this.current = null;
            this.curve.clear();
            this.repaint();
            break;
        case KeyEvent.VK_F1:
            final Graphics g = getGraphics();
            paint(g, PointList.getLine(curve));
            g.dispose();
            break;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == hotKey) {
            log.debug("HotKey released");
            isHot = false;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    private void lineTo(final Point p) {
        if (current != null) {
            log.debug("draw line");
            final Graphics g = getGraphics();
            g.drawLine(current.x, current.y, p.x, p.y);
            g.dispose();
        }
        curve.add(current = p);
    }
}