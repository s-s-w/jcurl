/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
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
package org.jcurl.zui.piccolo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

import org.jcurl.core.api.Factory;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.RockType.Pos;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * Creates a pickable node displaying one rock, assuming a <b>RIGHT HANDED</b>
 * parent coordinate system.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:PRockFactory.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public abstract class PRockFactory implements Factory {

	public static class Fancy extends PRockFactory {
		/** Rock colors */
		protected static class ColorSet {
			public Color contour = Color.BLACK;

			public Color dark = new Color(0xFF0000);

			public Color granite = new Color(0x565755);

			public Color label = Color.BLACK;

			public Color light = new Color(0xFFFF00);
		}

		protected static final ColorSet colors = new ColorSet();

		protected static final Font fo;

		protected static final Arc2D.Float inner;

		protected static final Arc2D.Float outer;

		static {
			final float ro = RockProps.DEFAULT.getRadius();
			final float ri = 0.7F * ro;
			outer = new Arc2D.Float(-ro, -ro, 2 * ro, 2 * ro, 0, 360,
					Arc2D.CHORD);
			inner = new Arc2D.Float(-ri, -ri, 2 * ri, 2 * ri, 0, 360,
					Arc2D.CHORD);
			fo = new Font("SansSerif", Font.BOLD, 1);
		}

		static Color alpha(final Color base, final int alpha) {
			return new Color(base.getRed(), base.getGreen(), base.getBlue(),
					alpha);
		}

		private final int alpha;

		public Fancy(final int alpha) {
			this.alpha = alpha;
		}

		@Override
		public PRockNode newInstance(final int i8, final boolean isDark,
				final Rock<Pos> rock) {
			final PRockNode r = new PRockNode(i8, isDark, rock);
			r.addChild(node(outer, alpha(colors.granite, alpha), null, null));
			r.addChild(node(inner, alpha(isDark ? colors.dark : colors.light,
					alpha), null, null));
			{
				final PText t = new PText(labels[i8]);
				t.setFont(fo);
				t.setTextPaint(alpha(colors.label, alpha));
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
		public PRockNode newInstance(final int i8, final boolean isDark,
				final Rock<Pos> rock) {
			final PRockNode r = new PRockNode(i8, isDark, rock);
			// fill to also make the body, not only the edge
			// pickable:
			r.addChild(node(outer, isDark ? colors.dark : colors.light, stroke,
					Color.BLACK));
			r.getChild(0).setPickable(true);
			return r;
		}
	}

	protected static final String[] labels = { "1", "2", "3", "4", "5", "6",
			"7", "8" };

	protected static PNode node(final Shape s, final Paint p, final Stroke str,
			final Paint sp) {
		final PPath n = new PPath(s, str);
		n.setStrokePaint(sp);
		n.setPaint(p);
		n.setPickable(false);
		return n;
	}

	protected static boolean sync(final Rock<Pos> src, final PRockNode dst) {
		// check if it's changed either location or angle:
		if (src.p().distanceSq(dst.getOffset()) < PPositionSet.eps
				&& Math.abs(src.getA() - dst.getRotation()) < PPositionSet.eps)
			return false;
		if (true)
			dst.setTransform(src.getAffineTransform());
		else {
			dst.setRotation(src.getA());
			dst.setOffset(src.getX(), src.getY());
		}
		dst.invalidatePaint();
		// Why is this necessary?
		dst.repaint();
		return true;
	}

	public abstract PRockNode newInstance(final int i8, final boolean isDark,
			Rock<Pos> rock);

	public PRockNode newInstance(final int i16, final Rock<Pos> rock) {
		return newInstance(i16 / 2, i16 % 2 == 0, rock);
	}
}
