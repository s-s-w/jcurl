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
package jcurl.mr.exp.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

/**
 * Draw lines using the keyboard.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class KeySketchPanel extends JPanel implements KeyListener {

    private static final long serialVersionUID = -3879989219730493694L;

    private Point end = new Point(0, 0);

    private Point start = new Point(0, 0);

    public KeySketchPanel() {
        addKeyListener(this);
    }

    public void add(int dx, int dy) {
        end.x += dx;
        end.y += dy;
        Graphics g = getGraphics();
        g.drawLine(start.x, start.y, end.x, end.y);
        g.dispose();
        start.x = end.x;
        start.y = end.y;
    }

    public boolean isFocusTraversable() {
        return true;
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int d;
        if (e.isShiftDown())
            d = 5;
        else
            d = 1;
        if (keyCode == KeyEvent.VK_LEFT)
            add(-d, 0);
        else if (keyCode == KeyEvent.VK_RIGHT)
            add(d, 0);
        else if (keyCode == KeyEvent.VK_UP)
            add(0, -d);
        else if (keyCode == KeyEvent.VK_DOWN)
            add(0, d);

    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        int d;
        if (Character.isUpperCase(keyChar)) {
            d = 5;
            keyChar = Character.toLowerCase(keyChar);
        } else
            d = 1;
        if (keyChar == 'h')
            add(-d, 0);
        else if (keyChar == 'l')
            add(d, 0);
        else if (keyChar == 'k')
            add(0, -d);
        else if (keyChar == 'j')
            add(0, d);
    }
}