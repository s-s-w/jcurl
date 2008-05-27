/*
 * jcurl java curling software framework http://www.jcurl.org
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
package org.jcurl.core.swing;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.Strategy;
import org.jcurl.core.ui.IceShapes;

/**
 * Strategy to paint one single rock at (0,0) with the handle pointing along the
 * negative y-axis. This coordinate system is also called
 * <em>Rock Coordinates (RC)</em>.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:RockPainter.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
class RockPainter implements Strategy {

	private static final IceShapes.RockColors colors = new IceShapes.RockColors();
	private static final int FONT_SCALE = 5 * 200;
	private static final char[] labels = { '1', '2', '3', '4', '5', '6', '7',
			'8' };
	private FontMetrics fm = null;
	private int[] txtXoff = null;
	private int txtYoff = 0;

	/**
	 * Basic rock drawing method: draw one single rock at (0,0,0). Assumes the
	 * correct transformation display- to rock-coodinates is set already.
	 * 
	 * @param g
	 * @param isDark
	 * @param idx
	 */
	public void paintRockRC(final Graphics2D g, final boolean isDark,
			final int idx) {
		// body
		g.setPaint(colors.granite);
		g.fill(IceShapes.ROCK_OUTER);
		g.setPaint(isDark ? colors.dark : colors.light);
		g.fill(IceShapes.ROCK_INNER);
		{
			g.scale(1.0 / FONT_SCALE, 1.0 / FONT_SCALE);
			// label
			if (fm == null)
				fm = g.getFontMetrics(IceShapes.ROCK_LABEL);
			if (txtXoff == null) {
				txtYoff = (int) (0.6F * 0.5F * fm.getHeight());
				txtXoff = new int[RockSet.ROCKS_PER_COLOR];
				for (int i = RockSet.ROCKS_PER_COLOR - 1; i >= 0; i--)
					txtXoff[i] = -fm.charWidth(labels[i]) / 2;
			}
			g.setFont(IceShapes.ROCK_LABEL);
			g.setPaint(colors.label);
			g.drawChars(labels, idx, 1, txtXoff[idx], txtYoff);
		}
		// contours
		// g.setPaint(colors.contour);
		// // handle
		// //g.fillOval(-p, -p, 2 * p, ri + p);
		// g.draw(inner);
		// g.draw(outer);
	}
}