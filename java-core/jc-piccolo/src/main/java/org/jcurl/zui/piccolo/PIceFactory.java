/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2009 M. Rohrmoser
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
package org.jcurl.zui.piccolo;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import org.jcurl.core.api.Factory;
import org.jcurl.core.ui.IceShapes;
import org.jcurl.core.ui.IceShapes.IceColors;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

/**
 * Creates a unpickable {@link PNode} displaying a sheet of ice assuming a
 * <b>RIGHT HANDED</b> coordinate system.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PIceFactory.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public abstract class PIceFactory implements Factory {

	public static class Fancy extends PIceFactory {

		private static final IceColors colors = new IceColors();

		@Override
		public PNode newInstance() {
			final PNode ice = new PNode();
			ice
					.addChild(node(IceShapes.hog2hog, colors.hog2hog,
							colors.stroke));
			ice
					.addChild(node(IceShapes.hog2tee, colors.hog2tee,
							colors.stroke));
			ice.addChild(node(IceShapes.centerLe, colors.contours,
					colors.stroke));
			ice.addChild(node(IceShapes.centerRi, colors.contours,
					colors.stroke));
			ice.addChild(node(IceShapes.tee2back, colors.tee2back,
					colors.stroke));
			ice.addChild(node(IceShapes.C12, colors.c12, null));
			ice.addChild(node(IceShapes.C8, colors.c8, null));
			ice.addChild(node(IceShapes.C4, colors.c4, null));
			ice.addChild(node(IceShapes.C1, colors.c1, null));
			ice
					.addChild(node(IceShapes.center, colors.contours,
							colors.stroke));
			ice.addChild(node(IceShapes.centerLeft, colors.contours,
					colors.stroke));
			ice.addChild(node(IceShapes.centerRight, colors.contours,
					colors.stroke));
			ice.addChild(node(IceShapes.back, colors.contours, colors.stroke));
			ice.addChild(node(IceShapes.tee, colors.contours, colors.stroke));
			ice
					.addChild(node(IceShapes.nearHog, colors.contours,
							colors.stroke));
			ice
					.addChild(node(IceShapes.farHog, colors.contours,
							colors.stroke));
			ice.setChildrenPickable(false);
			ice.setPickable(false);
			return ice;
		}
	}

	protected static PNode node(final Shape s, final Paint p, final Stroke l) {
		final PNode n = new PPath(s, l);
		n.setPaint(p);
		n.setPickable(false);
		return n;
	}

	public abstract PNode newInstance();
}