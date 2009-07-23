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

package org.jcurl.demo.tactics.sg;

import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;

import com.sun.scenario.animation.Clip;
import com.sun.scenario.animation.Interpolator;
import com.sun.scenario.animation.Interpolators;
import com.sun.scenario.animation.TimingTargetAdapter;
import com.sun.scenario.scenegraph.SGTransform.Affine;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class AnimateAffine extends TimingTargetAdapter {
	/**
	 * 
	 * @param dst
	 *            the trafo to modify
	 * @param dc
	 *            current display viewport
	 * @param wc
	 *            desired wc viewport
	 * @param millis
	 *            duration of the animation. <code><= 0</code> immediate.
	 */
	public static void animateToCenterBounds(final Affine dst,
			final RectangularShape dc, final RectangularShape wc,
			final long millis) {
		if (millis >= 0) {
			// animate
			final AnimateAffine target = new AnimateAffine(dst, AnimateAffine
					.map(wc, dc, null), Interpolators.getEasingInstance());
			final Clip c = Clip.create(millis, target);
			// c.setResolution(1);
			c.start();
		} else
			dst.setAffine(AnimateAffine.map(wc, dc, dst.getAffine()));
	}

	/** create a transformation that maps wc to dc */
	public static AffineTransform map(final RectangularShape wc,
			final RectangularShape dc, AffineTransform zt) {
		if (zt == null)
			zt = new AffineTransform();
		else
			zt.setToIdentity();
		// 3. move to the center of dc
		zt.translate(dc.getCenterX(), dc.getCenterY());
		// 2. scale to fit
		final double sw = dc.getWidth() / wc.getWidth();
		final double sh = dc.getHeight() / wc.getHeight();
		final double scale = sw > sh ? sh : sw;
		zt.scale(scale, scale);
		// 1. move the center of viewport to (0,0)
		zt.translate(-wc.getCenterX(), -wc.getCenterY());
		return zt;
	}

	private final double[] a = new double[6];
	private final double[] b = new double[6];
	private final Affine from;
	private final Interpolator ip;
	private AffineTransform tmp = null;

	private final double[] tt = new double[6];

	public AnimateAffine(final Affine from, final AffineTransform to,
			final Interpolator ip) {
		this.from = from;
		this.ip = ip;
		to.getMatrix(b);
	}

	@Override
	public void begin() {
		(tmp = from.getAffine()).getMatrix(a);
	}

	@Override
	public void end() {
		tmp.setTransform(b[0], b[1], b[2], b[3], b[4], b[5]);
		from.setAffine(tmp);
	}

	double[] interpolate(final double[] min, final double[] max,
			final double f, final double[] dst) {
		for (int i = min.length - 1; i >= 0; i--)
			dst[i] = min[i] + (max[i] - min[i]) * f;
		return dst;
	}

	@Override
	public void timingEvent(final float f, final long l) {
		interpolate(a, b, ip.interpolate(f), tt);
		tmp.setTransform(tt[0], tt[1], tt[2], tt[3], tt[4], tt[5]);
		from.setAffine(tmp);
	}
}
