/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
import java.util.Iterator;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Draw lines if the "hot" key is pressed.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:MouseSketchPanel.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class MouseSketchPanel extends JPanel implements KeyListener {

	private static final Log log = JCLoggerFactory
			.getLogger(MouseSketchPanel.class);

	private static final long serialVersionUID = -4606015027709328552L;

	private static void circle(final Graphics g, final int x, final int y,
			final int rx, final int ry) {
		g.drawArc(x - rx, y - ry, 2 * rx, 2 * ry, 0, 360);
	}

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
			@Override
			public void mouseMoved(final MouseEvent e) {
				if (isHot)
					MouseSketchPanel.this.lineTo(e.getPoint());
			}
		});
		addKeyListener(this);
	}

	public PointList getCurve() {
		return curve;
	}

	@Override
	public boolean isFocusTraversable() {
		return true;
	}

	public void keyPressed(final KeyEvent e) {
		if (e.getKeyChar() == hotKey) {
			log.debug("HotKey pressed");
			isHot = true;
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			current = null;
			curve.clear();
			this.repaint();
			break;
		case KeyEvent.VK_F1:
			final Graphics g = getGraphics();
			paint(g, PointList.getLine(curve));
			g.dispose();
			break;
		}
	}

	public void keyReleased(final KeyEvent e) {
		if (e.getKeyChar() == hotKey) {
			log.debug("HotKey released");
			isHot = false;
		}
	}

	public void keyTyped(final KeyEvent e) {}

	private void lineTo(final Point p) {
		if (current != null) {
			log.debug("draw line");
			final Graphics g = getGraphics();
			g.drawLine(current.x, current.y, p.x, p.y);
			g.dispose();
		}
		curve.add(current = p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#printComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.drawLine(100, 110, 100, 90);
		g.drawLine(110, 100, 90, 100);
		circle(g, 100, 100, 10, 10);
		g.drawArc(100, 100, 20, 20, 0, 360);
		g.drawArc(100, 100, -20, -20, 0, 360);
	}
}