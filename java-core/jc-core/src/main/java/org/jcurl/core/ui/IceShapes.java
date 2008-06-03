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

package org.jcurl.core.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.Unit;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class IceShapes {

	/** IceSize colors */
	public static class IceColors {
		public final Paint backGround = new Color(0xF0F0FF);
		public final Paint c1 = new Color(0xFFFFFF);
		public final Paint c12 = new Color(0xFF3131);
		public final Paint c4 = new Color(0x3131FF);
		public final Paint c8 = new Color(0xFFFFFF);
		public final Paint contours = Color.BLACK;
		public final Paint hog2hog = new Color(0xF8F8F8);
		public final Paint hog2tee = new Color(0xFFFFFF);
		/** (wc) millimiters */
		public final Stroke stroke = new BasicStroke(0.005F);
		public final Paint tee2back = new Color(0xFFFFFF);
	}

	/** Rock colors */
	public static class RockColors {
		public final Color contour = Color.BLACK;
		public final Color dark = new Color(0xFF0000);
		public final Color granite = new Color(0x565755);
		public final Color label = Color.BLACK;
		public final Color light = new Color(0xFFFF00);
	}

	public static final Line2D back;
	public static final Arc2D C1;
	public static final Arc2D C12;
	public static final Arc2D C4;
	public static final Arc2D C8;
	public static final Line2D center;
	public static final Line2D centerLe;
	public static final Line2D centerLeft;
	public static final Line2D centerRi;
	public static final Line2D centerRight;
	public static final Line2D farHog;
	public static final Rectangle2D hog2hog;
	public static final Rectangle2D hog2tee;
	public static final Line2D nearHog;
	
	public static final Arc2D ROCK_INNER;
	public static final Font ROCK_LABEL;
	public static final String[] ROCK_LABELS = { "1", "2", "3", "4", "5", "6",
			"7", "8" };
	public static final Arc2D ROCK_OUTER;
	
	public static final Line2D tee;
	public static final Rectangle2D tee2back;

	/** Define colors and the shapes to be filled and drawn */
	static {
		// ice dimensions
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

		// rock stuff
		final float ro = RockProps.DEFAULT.getRadius();
		final float ri = 0.7F * ro;
		ROCK_OUTER = new Arc2D.Float(-ro, -ro, 2 * ro, 2 * ro, 0, 360,
				Arc2D.CHORD);
		ROCK_INNER = new Arc2D.Float(-ri, -ri, 2 * ri, 2 * ri, 0, 360,
				Arc2D.CHORD);
		ROCK_LABEL = new Font("SansSerif", Font.BOLD, 1);
	}

	public static Color alpha(final Color base, final int alpha) {
		return new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha);
	}

	public static Color trace(final Color base, final int alpha) {
		final float[] hsb = Color.RGBtoHSB(base.getRed(), base.getGreen(), base
				.getBlue(), null);
		return alpha(new Color(Color.HSBtoRGB(hsb[0], 0.5F * hsb[1], hsb[2])),
				alpha);
	}
}
