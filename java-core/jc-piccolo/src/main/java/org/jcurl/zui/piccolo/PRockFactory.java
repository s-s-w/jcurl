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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import org.jcurl.core.api.Factory;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.ui.IceShapes;
import org.jcurl.core.ui.IceShapes.RockColors;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * Creates a pickable node displaying one rock, assuming a <b>RIGHT HANDED</b>
 * parent coordinate system.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PRockFactory.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public abstract class PRockFactory implements Factory {

	public static class Fancy extends PRockFactory {

		protected static final RockColors colors = new RockColors();

		private final int alpha;

		public Fancy(final int alpha) {
			this.alpha = alpha;
		}

		@Override
		public PNode newInstance(final int i8, final boolean isDark) {
			final PNode r = new PNode();
			r.addChild(node(IceShapes.ROCK_OUTER, IceShapes.alpha(
					colors.granite, alpha), null, null));
			r.addChild(node(IceShapes.ROCK_INNER, IceShapes.alpha(
					isDark ? colors.dark : colors.light, alpha), null, null));
			{
				final PText t = new PText(IceShapes.ROCK_LABELS[i8]);
				t.setFont(IceShapes.ROCK_LABEL);
				t.setTextPaint(IceShapes.alpha(colors.label, alpha));
				// Make coord-sys left-handed again, as the ice is assumed to be
				// right-handed:
				t.setTransform(AffineTransform.getScaleInstance(1, -1));
				t.scale(1.0 / 5.0); // implicit FontSize
				t.translate(-0.5 * t.getWidth(), -0.5 * t.getHeight());
				t.setPickable(false);
				r.addChild(t);
			}
			// r.setChildrenPickable(false);
			// r.setPickable(true);
			r.getChild(0).setPickable(true);
			return r;
		}
	}

	public static class Simple extends Fancy {
		private static final Stroke stroke = new BasicStroke(0.010F);

		public Simple() {
			super(128);
		}

		@Override
		public PNode newInstance(final int i8, final boolean isDark) {
			final PNode r = new PNode();
			// fill to also make the body, not only the edge
			// pickable:
			r.addChild(node(IceShapes.ROCK_OUTER, isDark ? colors.dark
					: colors.light, stroke, Color.BLACK));
			r.getChild(0).setPickable(true);
			return r;
		}
	}

	protected static PNode node(final Shape s, final Paint p, final Stroke str,
			final Paint sp) {
		final PPath n = new PPath(s, str);
		n.setStrokePaint(sp);
		n.setPaint(p);
		n.setPickable(false);
		return n;
	}

	public PNode newInstance(final int i16) {
		return newInstance(RockSet.toIdx8(i16), RockSet.isDark(i16));
	}

	public abstract PNode newInstance(final int i8, final boolean isDark);
}
