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

import java.util.Map;

import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.Measure;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.helpers.PropModelHelper;
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
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:SlideDenny.java 378 2007-01-24 01:18:35Z mrohrmoser $
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:CurlerNoCurl.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public class CurlerNoCurl extends CoulombCurler {

	private static final long serialVersionUID = -1818914036355517122L;

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

	public CurveRock<Pos> computeRc(final double a0, final double v0,
			final double omega0, final double sweepFactor) {
		return new CurveRockAnalytic<Pos>(new PolynomeCurve(computeRcPoly(a0, v0,
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

	/**
	 * @see #setDrawToTeeCurl(double)
	 * @see #setDrawToTeeTime(double)
	 */
	void init(final double drawToTeeTime, final double drawToTeeCurl) {
		setDrawToTeeCurl(drawToTeeCurl);
		setDrawToTeeTime(drawToTeeTime);
	}

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
}
