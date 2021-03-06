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
package org.jcurl.core.impl;

import java.util.Map;

import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.Measure;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.helpers.PropModelHelper;
import org.jcurl.math.MathVec;
import org.jcurl.math.Polynome;
import org.jcurl.math.R1R1Function;

/**
 * Mark Denny's curl-model. Motion of a curling rock acc. to "Curling rock
 * dynamics", Mark Denny, Canadian Journal of Physics, 1988, P. 295-304:
 * 
 * <pre>
 * tau = v0 * (mu * g)
 * x(t) := - (b * v0&circ;2 / (4 * e * R * tau)) * (t&circ;3 / 3 - t&circ;4 / (4 * tau))
 * y(t) := v0 * (t - t&circ;2 / (2 * tau))
 * w(t) := w0 * (1 - t / tau)&circ;(1 / e)
 * </pre>
 * 
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
 * <p>
 * Integrating <code>w(t)</code> leads to<br />
 * <code>a(t) = - w0 * e * (tau * ((tau-t)/tau)^(1/e) - t * ((tau-t)/tau)^(1/e) - tau) / (e + 1)</code>
 * </p>
 * <p>
 * <a href="http://maxima.sourceforge.net">Maxima</a> Solution for the friction
 * coefficients: Starting with the basic equations we first substitute
 * <code>tau</tau>:<pre>
 * tau = v0 * (mu * g)
 * x(t) := - (b * v0&circ;2 / (4 * e * R * tau)) * (t&circ;3 / 3 - t&circ;4 / (4 * tau))
 * ratsubst(v0/(mu*g), tau, %);
 * y(t) := v0 * (t - t&circ;2 / (2 * tau))
 * ratsubst(v0/(mu*g), tau, %);
 * </pre>
 * which results in<pre>
 * x(t):=-(4*g*mu*b*t&circ;3*v0-3*g&circ;2*mu&circ;2*b*t&circ;4)/(48*e*R)
 * y(t):=(2*t*v0-g*mu*t&circ;2)/2
 * </pre>
 * Now as we're interested in the frictions coefficients:
 * <pre>
 * solve([y(tt)=tee, diff(y(tt),tt)=0, x(tt)=curl], [mu, v0, b]);
 * </pre>
 * we get them:
 * <pre>
 * mu=(2*tee)/(g*tt&circ;2)
 * v0=(2*tee)/tt
 * b=-(12*curl*e*R)/tee&circ;2
 * </pre>
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:SlideDenny.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CurlerDenny extends CoulombCurler {

	private static final double _R = RockProps.DEFAULT.getRadius();

	private static final double eps = RockProps.DEFAULT.getInertia()
			/ (RockProps.DEFAULT.getMass() * MathVec.sqr(_R));

	private static final long serialVersionUID = 9048729754646886751L;

	private transient double b;

	public CurlerDenny() {}

	/**
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

	public CurveRock<Pos> computeRc(final double a0, final double v0,
			final double omega0, final double sweepFactor) {
		final Polynome y = new Polynome(new double[] { 0, v0, -g * mu / 2 });
		final double f = Math.signum(omega0) * b * g * mu / (48 * eps * _R);
		final Polynome x = new Polynome(new double[] { 0, 0, 0, 4 * v0 * f,
				-3 * g * mu * f });
		final R1R1Function a = new R1R1Function() {
			private static final long serialVersionUID = -4138559036279095873L;

			@Override
			public double at(double t, int c) {
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
		return new CurveRockAnalytic<Pos>(x, y, a);
	}

	public void init(final Map<CharSequence, Measure> ice) {
		internalInit(ice);
		setDrawToTeeCurl(PropModelHelper.getDrawToTeeCurl(params));
		setDrawToTeeTime(PropModelHelper.getDrawToTeeTime(params));
	}

	public void setDrawToTeeCurl(final double drawToTeeCurl) {
		b = -drawToTeeCurl * 12.0 * eps * _R
				/ MathVec.sqr(IceSize.FAR_HACK_2_TEE);
		PropModelHelper.setDrawToTeeCurl(params, drawToTeeCurl);
	}
}