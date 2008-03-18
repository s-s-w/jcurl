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

import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.Measure;
import org.jcurl.core.api.Physics;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.helpers.PropModelHelper;
import org.jcurl.math.MathVec;
import org.jcurl.math.Polynome;
import org.jcurl.math.R1R1Function;

/**
 * Mark Denny's curl-model. Motion of a curling rock acc. to "Curling rock
 * dynamics", Mark Denny, Canadian Journal of Physics, 1988, P. 295-304:
 * <p>
 * <code>tau = v0 * (mu * g)</code><br />
 * <code>x(t) := - (b * v0^2 / (4 * e * R * tau)) * (t^3 / 3 - t^4 / (4 * tau))</code><br />
 * <code>y(t) := v0 * (t - t^2 / (2 * tau))</code><br />
 * <code>w(t) := w0 * (1 - t / tau)^(1 / e)</code>
 * </p>
 * <p>
 * with
 * <ul>
 * <li><code>t [s]</code> time</li>
 * <li><code>x [m]</code> curl</li>
 * <li><code>y [m]</code> distance along the track</li>
 * <li><code>b [1]</code> parameter for the curl's magnitude</li>
 * <li><code>e [1]</code> parameter <code>I / (mR^2))</code></li>
 * <li><code>tau [s]</code> total time until <code>v=0</code></li>
 * <li><code>R [m]</code> radius of the touching area rock/ice, ca.
 * <code>6.3e-2</code></li>
 * <li><code>mu [1]</code> friction coefficient rock/ice</li>
 * <li><code>I [m^2*Kg]</code> moment of inertia
 * {@link RockProps#getInertia()}</li>
 * <li><code>g [N/kg]</code> gravitation 9.81</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Integrating <code>w(t)</code> leads to<br />
 * <code>a(t) = - w0 * e * (tau * ((tau-t)/tau)^(1/e) - t * ((tau-t)/tau)^(1/e) - tau) / (e + 1)</code>
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:SlideDenny.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CurlerDenny extends CurlerBase {

	private static final double _R = RockProps.DEFAULT.getRadius();

	private static final double eps = RockProps.DEFAULT.getInertia()
			/ (RockProps.DEFAULT.getMass() * MathVec.sqr(_R));

	private static final double g = Physics.g;

	private static final long serialVersionUID = 9048729754646886751L;

	private transient double b;

	private transient double mu;

	public CurlerDenny() {}

	/**
	 * Computes the internal ice friction coefficient as:
	 * <p>
	 * <code>beta = {@link IceSize#FAR_HOG_2_TEE} / drawToTeeTime^2</code>
	 * </p>
	 * 
	 * @param drawToTeeTime
	 *            may be {@link Double#POSITIVE_INFINITY}.
	 * @param drawToTeeCurl
	 *            should be &gt; 0
	 */
	public CurlerDenny(final double drawToTeeTime, final double drawToTeeCurl) {
		final Map<CharSequence, Measure> t = PropModelHelper.create();
		PropModelHelper.setDrawToTeeTime(t, drawToTeeTime);
		PropModelHelper.setDrawToTeeCurl(t, drawToTeeCurl);
		init(t);
	}

	/**
	 * Compute the (virtual) speed at the hack for a rock released with split
	 * time <code>t</code> towards <code>broom</code>.
	 * <p>
	 * <a href="http://maxima.sourceforge.net">Maxima</a> Solution:
	 * 
	 * <pre>
	 * y(t) := (t-t*t/(2*tau))*v0
	 * ratsubst(v0/(mu*g), tau, %);
	 * remfunction(all);
	 * y(t):=-((g*mu*t*t-2*v0*t)*v0)/(2*v0)
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
		final double f = -Math.signum(omega0) * b * g * mu / (48 * eps * _R);
		final Polynome x = new Polynome(new double[] { 0, 0, 0, -4 * v0 * f,
				3 * g * mu * f });
		final Polynome y = new Polynome(new double[] { 0, v0, -g * mu / 2 });
		final R1R1Function a = new R1R1Function() {
			private static final long serialVersionUID = -4138559036279095873L;

			@Override
			public double at(int c, double t) {
				final double cc = Math.pow(1 - g * mu * t / v0, 1 / eps);
				switch (c) {
				case 0:
					return omega0 * eps / ((eps + 1) * g * mu)
							* (g * mu * t * cc - v0 * cc + v0);
				case 1:
					return omega0 * cc;
				case 2:
					return omega0 * g * mu * cc / (eps * (g * mu * t - v0));
				default:
					throw new IllegalArgumentException();
				}
			}

			@Override
			public String toString() {
				return "";
			}
		};
		return new CurveRockAnalytic(x, y, a);
	}

	public double getDrawToTeeCurl() {
		return PropModelHelper.getDrawToTeeCurl(params);
	}

	public double getDrawToTeeTime() {
		return PropModelHelper.getDrawToTeeTime(params);
	}

	public void init(final Map<CharSequence, Measure> ice) {
		internalInit(ice);
		setDrawToTeeCurl(PropModelHelper.getDrawToTeeCurl(params));
		setDrawToTeeTime(PropModelHelper.getDrawToTeeTime(params));
	}

	public void setDrawToTeeCurl(final double drawToTeeCurl) {
		b = -drawToTeeCurl * 12.0 * eps * _R
				/ MathVec.sqr(IceSize.FAR_HOG_2_TEE);
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