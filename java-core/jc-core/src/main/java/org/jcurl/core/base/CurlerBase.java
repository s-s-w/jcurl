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
package org.jcurl.core.base;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.jcurl.math.MathVec;

/**
 * Base implementation for {@link Curler}s.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CurlerBase extends PropModelImpl implements Curler {

	public double computeIntervalTime(double v0) {
		final double d = getDrawToTeeTime();
		final double B = IceSize.BACK_2_HOG;
		final double H = IceSize.FAR_HOG_2_TEE;
		// $$t=\frac{d\,\sqrt{4\,B\,H+{d}^{2}\,{v}^{2}}-{d}^{2}\,v}{2\,H}$$
		return (d * Math.sqrt(4 * B * H + d * d * v0 * v0) - d * d * v0) / 2
				* H;
	}

	public double computeHogSpeed(final double intervalTime) {
		return IceSize.BACK_2_HOG / intervalTime - IceSize.FAR_HOG_2_TEE
				/ MathVec.sqr(getDrawToTeeTime()) * intervalTime;
	}

	/**
	 * Compute the RC-&gt;WC transformation for a rock immediately after it's
	 * release (at the hog).
	 * 
	 * @param ret
	 *            <code>null</code> creates a new one.
	 * @param broomX
	 *            (world coordinates)
	 * @param broomY
	 *            (world coordinates)
	 * @return the transformation matrix
	 */
	public AffineTransform releaseRc2Wc(AffineTransform ret,
			final double broomX, final double broomY) {
		if (ret == null)
			ret = new AffineTransform();
		else
			ret.setToIdentity();
		final double dx = (0 - broomX) * IceSize.HACK_2_HOG
				/ (IceSize.FAR_HACK_2_TEE - broomY);
		ret.translate(dx, IceSize.FAR_HOG_2_TEE);
		// TUNE avoid trigonometry
		ret.rotate(Math.atan2(dx, IceSize.HACK_2_HOG) + Math.PI);
		return ret;
	}

	/**
	 * Compute the RC-&gt;WC transformation for a rock immediately after release
	 * it's (at the hog).
	 * <p>
	 * Convenience wrapper for
	 * {@link #releaseRc2Wc(AffineTransform, double, double)}.
	 * </p>
	 * 
	 * @param ret
	 *            <code>null</code> creates a new one.
	 * @param broom
	 *            (world coordinates)
	 * @return the transformation matrix
	 * @see #releaseRc2Wc(AffineTransform, double, double)
	 */
	public AffineTransform releaseRc2Wc(final AffineTransform ret,
			final Point2D broom) {
		return releaseRc2Wc(ret, broom.getX(), broom.getY());
	}
}
