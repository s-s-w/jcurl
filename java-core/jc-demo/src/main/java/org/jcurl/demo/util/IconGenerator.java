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
package org.jcurl.demo.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jcurl.core.api.RockSetUtils;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.ui.Zoomer;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:IconGenerator.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class IconGenerator {

	/**
	 * @param loc
	 * @param zoom
	 * @param dst
	 * @throws IOException
	 */
	public static void savePng(final RockSetUtils loc, final Zoomer zoom,
			final File dst) throws IOException {
		// Create image and graphics.
		final BufferedImage img = new BufferedImage(1024, 768,
				BufferedImage.TYPE_INT_ARGB);
		final Graphics g = img.getGraphics();
		// FIXME !!!
		// final PositionDisplay jp = new PositionDisplay();
		// jp.setPos(loc);
		// jp.setZoom(zoom);
		// jp.setSize(img.getWidth(), img.getHeight());
		// jp.paint(g);
		g.dispose();
		ImageIO.write(img, "png", dst);
		throw new NotImplementedYetException();
	}
}