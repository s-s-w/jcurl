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

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * Drop, move and erase rectangles on a panel.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class MouseRectPanel extends JPanel implements MouseMotionListener {

    private static final int MAXNSQUARES = 10;

    private static final int SQUARELENGTH = 10;

    private int current = -1;

    private int nsquares = 0;

    private Point[] squares = new Point[MAXNSQUARES];

    public MouseRectPanel() {
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() >= 2) {
                    remove(current);
                }
            }

            public void mousePressed(MouseEvent evt) {
                int x = evt.getX();
                int y = evt.getY();
                current = find(x, y);
                if (current < 0) // not inside a square
                    add(x, y);
            }
        });
        addMouseMotionListener(this);
    }

    public void add(int x, int y) {
        if (nsquares < MAXNSQUARES) {
            squares[nsquares] = new Point(x, y);
            current = nsquares;
            nsquares++;
            repaint();
        }
    }

    public void draw(Graphics g, int i) {
        g.drawRect(squares[i].x - SQUARELENGTH / 2, squares[i].y - SQUARELENGTH
                / 2, SQUARELENGTH, SQUARELENGTH);
    }

    public int find(int x, int y) {
        for (int i = 0; i < nsquares; i++) {
            if (squares[i].x - SQUARELENGTH / 2 <= x
                    && x <= squares[i].x + SQUARELENGTH / 2
                    && squares[i].y - SQUARELENGTH / 2 <= y
                    && y <= squares[i].y + SQUARELENGTH / 2

            )
                return i;
        }
        return -1;
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (current >= 0) {
            Graphics g = getGraphics();
            g.setXORMode(getBackground());
            draw(g, current);
            squares[current].x = x;
            squares[current].y = y;
            draw(g, current);
            g.dispose();
        }
    }

    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (find(x, y) >= 0)
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        else
            setCursor(Cursor.getDefaultCursor());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < nsquares; i++) {
            draw(g, i);
        }
    }

    public void remove(int n) {
        if (n < 0 || n >= nsquares)
            return;
        nsquares--;
        squares[n] = squares[nsquares];
        if (current == n)
            current = -1;
        repaint();
    }
}