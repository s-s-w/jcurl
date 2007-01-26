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
package org.jcurl.mr.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.mr.math.PointList;

/**
 * Draw lines if the "hot" key is pressed.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:MouseSketchPanel.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class MouseSketchPanel extends JPanel implements KeyListener {

    private static final long serialVersionUID = -4606015027709328552L;

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#printComponent(java.awt.Graphics)
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(100, 110, 100, 90);
        g.drawLine(110, 100, 90, 100);
        circle(g, 100, 100, 10, 10);
        g.drawArc(100, 100, 20, 20, 0, 360);
        g.drawArc(100, 100, -20, -20, 0, 360);
    }

    private static void circle(Graphics g, int x, int y, int rx, int ry) {
        g.drawArc(x - rx, y - ry, 2 * rx, 2 * ry, 0, 360);
    }

    private static void circle(Graphics g, Point2D c, int r) {
        final int x = (int) c.getX();
        final int y = (int) c.getY();
        final int d = 2 * r;
        g.drawArc(x - r, y - r, d, d, 0, 360);
    }

    private static final Log log = JCLoggerFactory
            .getLogger(MouseSketchPanel.class);

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