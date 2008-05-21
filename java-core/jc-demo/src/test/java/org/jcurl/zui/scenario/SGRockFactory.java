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
package org.jcurl.zui.scenario;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

import org.jcurl.core.api.Factory;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType.Pos;

import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGShape;
import com.sun.scenario.scenegraph.SGText;
import com.sun.scenario.scenegraph.SGTransform;
import com.sun.scenario.scenegraph.SGAbstractShape.Mode;
import com.sun.scenario.scenegraph.SGTransform.Affine;

/**
 * Creates a pickable node displaying one rock, assuming a <b>RIGHT HANDED</b>
 * parent coordinate system.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PRockFactory.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public abstract class SGRockFactory implements Factory {

	public static class Fancy extends SGRockFactory {
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

		private final int alpha;

		public Fancy(final int alpha) {
			this.alpha = alpha;
		}

		Color alpha(final Color base) {
			return new Color(base.getRed(), base.getGreen(), base.getBlue(),
					alpha);
		}

		@Override
		public Affine newInstance(final int i8, final boolean isDark,
				final Rock<Pos> rock) {
			final SGGroup r = new SGGroup();
			r.add(node(outer, alpha(colors.granite), null, null));
			r.add(node(inner, alpha(isDark ? colors.dark : colors.light), null,
					null));
			{
				final SGText t = new SGText();
				t.setAntialiasingHint(RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				t.setText(labels[i8]);
				t.setFont(fo);
				t.setFillPaint(alpha(colors.label));
				// Make coord-sys left-handed again, as the ice is assumed to be
				// right-handed:
				final AffineTransform at = AffineTransform.getScaleInstance(1,
						-1);
				at.scale(1.0 / 5.0, 1.0 / 5.0); // implicit FontSize
				final Rectangle2D re = t.getBounds();
				at.translate(-0.075 * re.getWidth(), 0.075 * re.getHeight());
				// t.setPickable(false);
				r.add(SGTransform.createAffine(at, t));
			}
			// r.setMouseBlocker(true);
			final Affine n = SGTransform.createAffine(
					rock.getAffineTransform(), r);
			n.putAttribute(rock.getClass().getName(), rock);
			return n;
		}
	}

	public static class Simple extends Fancy {
		private static final Stroke stroke = new BasicStroke(0.010F);

		public Simple(final int alpha) {
			super(alpha);
		}

		@Override
		public Affine newInstance(final int i8, final boolean isDark,
				final Rock<Pos> rock) {
			final Affine n = SGTransform.createAffine(
					rock.getAffineTransform(), node(outer,
							alpha(isDark ? colors.dark : colors.light), stroke,
							alpha(Color.BLACK)));
			n.putAttribute(rock.getClass().getName(), rock);
			return n;
		}
	}

	protected static final String[] labels = { "1", "2", "3", "4", "5", "6",
			"7", "8" };

	protected static SGNode node(final Shape s, final Paint p,
			final Stroke str, final Paint sp) {
		final SGShape n = new SGShape();
		n.setShape(s);
		n.setFillPaint(p);
		if (str != null) {
			n.setMode(Mode.STROKE_FILL);
			n.setDrawPaint(sp);
			n.setDrawStroke(str);
		}
		n.setAntialiasingHint(RenderingHints.VALUE_ANTIALIAS_ON);
		return n;
	}

	public abstract Affine newInstance(final int i8, final boolean isDark,
			Rock<Pos> rock);

	public SGNode newInstance(final int i16, final Rock<Pos> rock) {
		return newInstance(RockSet.toIdx8(i16), RockSet.isDark(i16), rock);
	}
}
