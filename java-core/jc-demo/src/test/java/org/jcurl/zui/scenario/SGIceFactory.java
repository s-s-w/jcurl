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
package org.jcurl.zui.scenario;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jcurl.core.api.Factory;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.Unit;

import com.sun.scenario.scenegraph.SGAbstractShape;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGParent;
import com.sun.scenario.scenegraph.SGShape;
import com.sun.scenario.scenegraph.SGTransform;

/**
 * Creates {@link SGParent} displaying a sheet of ice with a <b>RIGHT HANDED</b>
 * coordinate system.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PIceFactory.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
abstract class SGIceFactory implements Factory {

	public static class Fancy extends SGIceFactory {

		/** IceSize colors */
		private static class ColorSet {

			public Paint backGround = new Color(0xF0F0FF);

			public Paint c1 = new Color(0xFFFFFF);

			public Paint c12 = new Color(0xFF3131);

			public Paint c4 = new Color(0x3131FF);

			public Paint c8 = new Color(0xFFFFFF);

			public Paint contours = Color.BLACK;

			public Paint hog2hog = new Color(0xF8F8F8);

			public Paint hog2tee = new Color(0xFFFFFF);

			/** (wc) millimiters */
			public Stroke stroke = new BasicStroke(0.005F);

			public Paint tee2back = new Color(0xFFFFFF);
		}

		private static final ColorSet colors = new ColorSet();

		/**
		 * Do it!
		 * 
		 * @return the new SGNode
		 */
		@Override
		public SGNode newInstance(final SGNode content) {
			final SGGroup ice = new SGGroup();
			ice.add(node(hog2hog, colors.hog2hog, colors.stroke));
			ice.add(node(hog2tee, colors.hog2tee, colors.stroke));
			ice.add(node(centerLe, colors.contours, colors.stroke));
			ice.add(node(centerRi, colors.contours, colors.stroke));
			ice.add(node(tee2back, colors.tee2back, colors.stroke));
			ice.add(node(C12, colors.c12, null));
			ice.add(node(C8, colors.c8, null));
			ice.add(node(C4, colors.c4, null));
			ice.add(node(C1, colors.c1, null));
			ice.add(node(center, colors.contours, colors.stroke));
			ice.add(node(centerLeft, colors.contours, colors.stroke));
			ice.add(node(centerRight, colors.contours, colors.stroke));
			ice.add(node(back, colors.contours, colors.stroke));
			ice.add(node(tee, colors.contours, colors.stroke));
			ice.add(node(nearHog, colors.contours, colors.stroke));
			ice.add(node(farHog, colors.contours, colors.stroke));
			ice.setMouseBlocker(true);
			final SGGroup tmp = new SGGroup();
			tmp.add(ice);
			if (content != null)
				tmp.add(content);
			// Make coord-sys right-handed:
			return SGTransform.createAffine(AffineTransform.getScaleInstance(1,
					-1), tmp);
		}
	}

	protected static final Line2D.Float back;

	protected static final Arc2D.Float C1;

	protected static final Arc2D.Float C12;

	protected static final Arc2D.Float C4;

	protected static final Arc2D.Float C8;

	protected static final Line2D.Float center;

	protected static final Line2D.Float centerLe;

	protected static final Line2D.Float centerLeft;

	protected static final Line2D.Float centerRi;

	protected static final Line2D.Float centerRight;

	protected static final Line2D.Float farHog;

	protected static final Rectangle2D.Float hog2hog;

	protected static final Rectangle2D.Float hog2tee;

	protected static final Line2D.Float nearHog;

	protected static final Line2D.Float tee;

	protected static final Rectangle2D.Float tee2back;

	/** Define colors and the shapes to be filled and drawn */
	static {
		final float fhy = IceSize.FAR_HOG_2_TEE;
		final float nhy = IceSize.HOG_2_TEE;
		final float hy = IceSize.FAR_HACK_2_TEE;
		final float dx = IceSize.SIDE_2_CENTER;
		final float by = IceSize.BACK_2_TEE;
		final float c1 = Unit.f2m(0.5);
		final float c4 = Unit.f2m(2.0);
		final float c8 = Unit.f2m(4.0);
		final float c12 = Unit.f2m(6.0);

		hog2hog = new Rectangle2D.Float(-dx, nhy, 2 * dx, fhy - nhy);
		hog2tee = new Rectangle2D.Float(-dx, 0, 2 * dx, nhy);
		tee2back = new Rectangle2D.Float(-dx, -by, 2 * dx, by);
		C12 = new Arc2D.Float(-c12, -c12, 2 * c12, 2 * c12, 0, 360, Arc2D.CHORD);
		C8 = new Arc2D.Float(-c8, -c8, 2 * c8, 2 * c8, 0, 360, Arc2D.CHORD);
		C4 = new Arc2D.Float(-c4, -c4, 2 * c4, 2 * c4, 0, 360, Arc2D.CHORD);
		C1 = new Arc2D.Float(-c1, -c1, 2 * c1, 2 * c1, 0, 360, Arc2D.CHORD);
		back = new Line2D.Float(-dx, -by, dx, -by);
		tee = new Line2D.Float(-dx, 0, dx, 0);
		nearHog = new Line2D.Float(-dx, nhy, dx, nhy);
		farHog = new Line2D.Float(-dx, fhy, dx, fhy);
		center = new Line2D.Float(0, hy, 0, -by);
		final float RR = 4 * RockProps.DEFAULT.getRadius();
		centerLe = new Line2D.Float(-RR, fhy, -RR, -by);
		centerRi = new Line2D.Float(RR, fhy, RR, -by);
		centerLeft = new Line2D.Float(-dx, fhy, -dx, -by);
		centerRight = new Line2D.Float(dx, fhy, dx, -by);
	}

	protected static SGNode node(final Shape s, final Paint p, final Stroke l) {
		final SGShape n = new SGShape();
		n.setShape(s);
		if (l != null) {
			n.setDrawStroke(l);
			n.setDrawPaint(Color.BLACK);
			n.setFillPaint(p);
			n.setMode(SGAbstractShape.Mode.STROKE_FILL);
		} else {
			n.setFillPaint(p);
			n.setMode(SGAbstractShape.Mode.FILL);
		}
		n.setAntialiasingHint(RenderingHints.VALUE_ANTIALIAS_ON);
		return n;
	}

	public abstract SGNode newInstance(SGNode content);
}