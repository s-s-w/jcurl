/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.core.impl;

import java.awt.geom.Point2D;

import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.Physics;
import org.jcurl.core.helpers.PropModelHelper;

/**
 * Coulomb-friction (along y-axis) based curl models.
 * 
 * <pre>
 * y(t) := v0 * t - mu * g * t * t / 2
 * </pre>
 * 
 * <p>
 * with
 * <ul>
 * <li><code>t [s]</code> time</li>
 * <li><code>y [m]</code> distance along the track</li>
 * <li><code>mu [1]</code> friction coefficient rock/ice</li>
 * <li><code>g [N/kg]</code> gravitation 9.81</li>
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CoulombCurler extends CurlerBase {

	protected static final double g = Physics.g;

	private static final long serialVersionUID = 4753259656811782206L;

	protected transient double mu;

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

	public double getDrawToTeeCurl() {
		return PropModelHelper.getDrawToTeeCurl(params);
	}

	public double getDrawToTeeTime() {
		return PropModelHelper.getDrawToTeeTime(params);
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
