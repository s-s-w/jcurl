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

import java.awt.Graphics2D;

import org.jcurl.core.api.Strategy;
import org.jcurl.core.ui.IceShapes;

/**
 * Strategy to paint the ice sheet in world coordinates.
 * 
 * @see org.jcurl.core.swing.RockPainter
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:IcePainter.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
class IcePainter implements Strategy {
	static final IceShapes.IceColors colors = new IceShapes.IceColors();
	
	public void paintIceWC(final Graphics2D g) {
		g.setStroke(colors.stroke);
		// filled stuff
		g.setPaint(colors.hog2hog);
		g.fill(IceShapes.hog2hog);
		g.setPaint(colors.hog2tee);
		g.fill(IceShapes.hog2tee);
		g.setPaint(colors.contours);
		g.draw(IceShapes.centerLe);
		g.draw(IceShapes.centerRi);
		g.setPaint(colors.tee2back);
		g.fill(IceShapes.tee2back);
		g.setPaint(colors.c12);
		g.fill(IceShapes.C12);
		g.setPaint(colors.c8);
		g.fill(IceShapes.C8);
		g.setPaint(colors.c4);
		g.fill(IceShapes.C4);
		g.setPaint(colors.c1);
		g.fill(IceShapes.C1);
		// contours
		g.setPaint(colors.contours);
		// g.draw(C12);
		// g.draw(C8);
		// g.draw(C4);
		// g.draw(C1);
		g.draw(IceShapes.back);
		g.draw(IceShapes.tee);
		g.draw(IceShapes.nearHog);
		g.draw(IceShapes.farHog);
		g.draw(IceShapes.center);
		g.draw(IceShapes.centerLeft);
		g.draw(IceShapes.centerRight);
	}
}