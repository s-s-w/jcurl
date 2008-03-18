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
package org.jcurl.core.impl;

import java.awt.geom.Point2D;
import java.util.Map;

import org.jcurl.core.api.CurlerBase;
import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.CurveRockAnalytic;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.PropModelHelper;
import org.jcurl.core.helpers.Measure;
import org.jcurl.core.helpers.Physics;
import org.jcurl.math.Polynome;
import org.jcurl.math.PolynomeCurve;

/**
 * This is not a realistic curl model but rather a baseline for the development
 * and implementation of others.
 * 
 * <p>
 * <code>x(t) := 0</code><br />
 * <code>y(t) := v0 * t - mu * g * t * t / 2</code><br />
 * <code>w(t) := w0</code><br />
 * <br />
 * <code>v(t) := v0 - g * mu * t</code><br/>
 * </p>
 * <p>
 * with
 * <ul>
 * <li><code>t [s]</code> time</li>
 * <li><code>x [m]</code> curl</li>
 * <li><code>y [m]</code> distance along the track</li>
 * <li><code>mu [1]</code> friction coefficient rock/ice</li>
 * <li><code>g [N/kg]</code> gravitation 9.81</li>
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:SlideDenny.java 378 2007-01-24 01:18:35Z mrohrmoser $
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CurlerNoCurl.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public class CurlerNoCurl extends CurlerBase {

	private static final long serialVersionUID = -1818914036355517122L;
	protected static final double g = Physics.g;
	transient double mu;

	public CurlerNoCurl() {}

	/**
	 * @see #init(Map)
	 */
	public CurlerNoCurl(final double drawToTeeTime, final double drawToTeeCurl) {
		final Map<CharSequence, Measure> t = PropModelHelper.create();
		PropModelHelper.setDrawToTeeTime(t, drawToTeeTime);
		PropModelHelper.setDrawToTeeCurl(t, drawToTeeCurl);
		init(t);
	}

	/**
	 * @see #init(Map)
	 */
	public CurlerNoCurl(final Map<CharSequence, Measure> ice) {
		init(ice);
	}

	/**
	 * Compute the (virtual) speed at the hack for a rock released with split
	 * time <code>ti</code> towards <code>broom</code>.
	 * <p>
	 * <a href="http://maxima.sourceforge.net">Maxima</a> Solution:
	 * 
	 * <pre>
	 * y(t) := v0 * t - mu * g * t * t / 2;
	 * solve([y(tb)=back, y(tb+ti)=hog], [v0, tb]);
	 * 
	 * v0=sqrt(g&circ;2*mu&circ;2*ti&circ;4+4*g*hog*mu*ti&circ;2+4*back*g*mu*ti&circ;2+4*hog&circ;2-8*back*hog+4*back&circ;2)/(2*ti)
	 * </pre>
	 * 
	 * @param ti
	 *            interval- or split-time
	 * @param broom
	 *            broom location (WC)
	 * @return initial absolute speed at the hack.
	 */
	public double computeHackSpeed(final double ti, final Point2D broom) {
		final double x = broom.getX() / (IceSize.FAR_HACK_2_TEE - broom.getY());
		final double f = Math.sqrt(1 + x * x);
		final double back = IceSize.HACK_2_BACK * f;
		final double hog = IceSize.HACK_2_HOG * f;

		final double g_2 = g * g;
		final double mu_2 = mu * mu;
		final double ti_2 = ti * ti;
		final double ti_4 = ti_2 * ti_2;
		final double hog_2 = hog * hog;
		final double back_2 = back * back;

		return Math.sqrt(g_2 * mu_2 * ti_4 + 4 * g * hog * mu * ti_2 + 4 * back
				* g * mu * ti_2 + 4 * hog_2 - 8 * back * hog + 4 * back_2)
				/ (2 * ti);
	}

	public CurveRock computeRc(final double a0, final double v0,
			final double omega0, final double sweepFactor) {
		return new CurveRockAnalytic(new PolynomeCurve(computeRcPoly(a0, v0,
				omega0, sweepFactor)));
	}

	Polynome[] computeRcPoly(final double alpha0, final double v0,
			final double omega0, final double sweepFactor) {
		final double[] x = { 0 };
		final double[] y = { 0, v0, -mu * g * 0.5 };
		final double[] a = { alpha0, omega0 };
		final Polynome[] ret = { new Polynome(x), new Polynome(y),
				new Polynome(a) };
		return ret;
	}

	public double getDrawToTeeCurl() {
		return 0;
	}

	public double getDrawToTeeTime() {
		if (mu <= 0)
			return Double.POSITIVE_INFINITY;
		return Math.sqrt(2 * (IceSize.FAR_HACK_2_TEE - IceSize.HACK_2_HOG)
				/ (g * mu));
	}

	/**
	 * @see #setDrawToTeeCurl(double)
	 * @see #setDrawToTeeTime(double)
	 */
	void init(final double drawToTeeTime, final double drawToTeeCurl) {
		setDrawToTeeCurl(drawToTeeCurl);
		setDrawToTeeTime(drawToTeeTime);
	}

	/**
	 * @see #internalInit(Map)
	 * @see #init(double, double)
	 */
	public void init(final Map<CharSequence, Measure> ice) {
		internalInit(ice);
		init(PropModelHelper.getDrawToTeeTime(params), PropModelHelper
				.getDrawToTeeCurl(params));
	}

	public void setDrawToTeeCurl(final double drawToTeeCurl) {
		if (drawToTeeCurl != 0)
			throw new IllegalArgumentException("Curl must be zero!");
		PropModelHelper.setDrawToTeeCurl(params, drawToTeeCurl);
	}

	/**
	 * Compute the friction coefficient from a draw-to-tee time. <a
	 * href="http://maxima.sourceforge.net">Maxima</a> Solution:
	 * 
	 * <pre>
	 * y(t) := v0 * t - mu * g * t * t / 2
	 * diff(y(t),t);
	 * v(t) := v0-g*mu*t
	 * solve([y(th)=HACK_2_HOG, y(th+td)=HACK_2_TEE, v(th+td)=0], [th,v0,mu]);
	 * </pre>
	 * 
	 * gives:
	 * 
	 * <pre>
	 * mu = (2 * HACK_2_TEE - 2 * HACK_2_HOG) / (g * td * td)
	 * </pre>
	 */
	public void setDrawToTeeTime(final double drawToTeeTime) {
		if (Double.isInfinite(drawToTeeTime) && drawToTeeTime > 0)
			mu = 0;
		else
			mu = 2.0 * (IceSize.FAR_HACK_2_TEE - IceSize.HACK_2_HOG)
					/ (g * drawToTeeTime * drawToTeeTime);
		PropModelHelper.setDrawToTeeTime(params, drawToTeeTime);
	}
}
