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
package org.jcurl.core.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockSetUtils;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.ui.FixpointZoomer;

public class PositionDisplayTest extends TestShowBase {

	public void testThroughPut() {
		final int dt = 5000;
		final Graphics g = new BufferedImage(1024 * 2, 768 * 2,
				BufferedImage.TYPE_INT_ARGB).getGraphics();
		final RockSet<Pos> p = RockSetUtils.allHome();
		final int frames = showPositionDisplay(p, FixpointZoomer.HOG2HACK, dt,
				new TimeRunnable() {
					@Override
					public void run(final double t) throws InterruptedException {
						throw new UnsupportedOperationException();
					}

					@Override
					public void run(final double t, final Component jp)
							throws InterruptedException {
						jp.paint(g);
					}
				});
		if (frame != null)
			System.out.println(getClass().getName() + " frequency: " + frames
					* 1000L / (double) dt + " frames per second");
		// System.out.println(frames + " computations took " + dt
		// + " millis, i.e. " + frames * 1000L / dt + " per second.");
	}
}
