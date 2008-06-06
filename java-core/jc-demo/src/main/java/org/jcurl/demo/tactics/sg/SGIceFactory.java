/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.demo.tactics.sg;

import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jcurl.core.api.Factory;
import org.jcurl.core.ui.IceShapes;
import org.jcurl.core.ui.IceShapes.IceColors;

import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGShape;
import com.sun.scenario.scenegraph.SGAbstractShape.Mode;

/**
 * Creates a unpickable {@link SGGroup} displaying a sheet of ice assuming a
 * <b>RIGHT HANDED</b> coordinate system.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PIceFactory.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public abstract class SGIceFactory implements Factory {

	public static class Fancy extends SGIceFactory {

		private static final IceColors colors = new IceColors();

		@Override
		public SGNode newInstance() {
			final SGGroup ice = new SGGroup();
			ice.add(node(IceShapes.hog2hog, colors.hog2hog, colors.stroke));
			ice.add(node(IceShapes.hog2tee, colors.hog2tee, colors.stroke));
			ice.add(node(IceShapes.centerLe, colors.contours, colors.stroke));
			ice.add(node(IceShapes.centerRi, colors.contours, colors.stroke));
			ice.add(node(IceShapes.tee2back, colors.tee2back, colors.stroke));
			ice.add(node(IceShapes.C12, colors.c12, null));
			ice.add(node(IceShapes.C8, colors.c8, null));
			ice.add(node(IceShapes.C4, colors.c4, null));
			ice.add(node(IceShapes.C1, colors.c1, null));
			ice.add(node(IceShapes.center, colors.contours, colors.stroke));
			ice.add(node(IceShapes.centerLeft, colors.contours, colors.stroke));
			ice
					.add(node(IceShapes.centerRight, colors.contours,
							colors.stroke));
			ice.add(node(IceShapes.back, colors.contours, colors.stroke));
			ice.add(node(IceShapes.tee, colors.contours, colors.stroke));
			ice.add(node(IceShapes.nearHog, colors.contours, colors.stroke));
			ice.add(node(IceShapes.farHog, colors.contours, colors.stroke));
			// ice.setChildrenPickable(false);
			// ice.setPickable(false);
			ice.setMouseBlocker(true);
			return ice;
		}
	}

	protected static SGShape node(final Shape s, final Paint p, final Stroke l) {
		final SGShape n = new SGShape();
		n.setAntialiasingHint(RenderingHints.VALUE_ANTIALIAS_ON);
		n.setShape(s);
		n.setFillPaint(p);
		n.setDrawPaint(p);
		if (l != null)
			n.setDrawStroke(l);
		if (s instanceof Rectangle2D)
			n.setMode(Mode.STROKE_FILL);
		else if (s instanceof Arc2D)
			n.setMode(Mode.FILL);
		else if (s instanceof Line2D)
			n.setMode(Mode.STROKE);
		// n.setPickable(false);
		return n;
	}

	public abstract SGNode newInstance();
}