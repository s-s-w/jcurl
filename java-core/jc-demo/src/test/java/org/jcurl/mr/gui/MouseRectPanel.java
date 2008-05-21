/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:MouseRectPanel.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class MouseRectPanel extends JPanel implements MouseMotionListener {

	private static final int MAXNSQUARES = 10;

	private static final long serialVersionUID = 3946069203465871809L;

	private static final int SQUARELENGTH = 10;

	private int current = -1;

	private int nsquares = 0;

	private final Point[] squares = new Point[MAXNSQUARES];

	public MouseRectPanel() {
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent evt) {
				if (evt.getClickCount() >= 2)
					MouseRectPanel.this.remove(current);
			}

			@Override
			public void mousePressed(final MouseEvent evt) {
				final int x = evt.getX();
				final int y = evt.getY();
				current = MouseRectPanel.this.find(x, y);
				if (current < 0) // not inside a square
					MouseRectPanel.this.add(x, y);
			}
		});
		addMouseMotionListener(this);
	}

	public void add(final int x, final int y) {
		if (nsquares < MAXNSQUARES) {
			squares[nsquares] = new Point(x, y);
			current = nsquares;
			nsquares++;
			this.repaint();
		}
	}

	public void draw(final Graphics g, final int i) {
		g.drawRect(squares[i].x - SQUARELENGTH / 2, squares[i].y - SQUARELENGTH
				/ 2, SQUARELENGTH, SQUARELENGTH);
	}

	public int find(final int x, final int y) {
		for (int i = 0; i < nsquares; i++)
			if (squares[i].x - SQUARELENGTH / 2 <= x
					&& x <= squares[i].x + SQUARELENGTH / 2
					&& squares[i].y - SQUARELENGTH / 2 <= y
					&& y <= squares[i].y + SQUARELENGTH / 2

			)
				return i;
		return -1;
	}

	public void mouseDragged(final MouseEvent e) {
		final int x = e.getX();
		final int y = e.getY();
		if (current >= 0) {
			final Graphics g = getGraphics();
			g.setXORMode(getBackground());
			draw(g, current);
			squares[current].x = x;
			squares[current].y = y;
			draw(g, current);
			g.dispose();
		}
	}

	public void mouseMoved(final MouseEvent e) {
		final int x = e.getX();
		final int y = e.getY();
		if (find(x, y) >= 0)
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		else
			setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < nsquares; i++)
			draw(g, i);
	}

	@Override
	public void remove(final int n) {
		if (n < 0 || n >= nsquares)
			return;
		nsquares--;
		squares[n] = squares[nsquares];
		if (current == n)
			current = -1;
		this.repaint();
	}
}