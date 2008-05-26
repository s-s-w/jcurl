/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
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

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.Collider;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.MathVec;

/**
 * Abstract base class for collission models computed in collission-coordinates.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:CollissionStrategy.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public abstract class ColliderBase extends PropModelImpl implements Collider {

	private static final double _Rad = RockProps.DEFAULT.getRadius();

	private static final double HIT_MAX_DIST = 1e-6F;

	private static final Log log = JCLoggerFactory
			.getLogger(ColliderBase.class);

	/** Maximum distance square [m] of two rocks to consider them touching */
	public static final double MaxDistSq = MathVec.sqr(_Rad + _Rad
			+ HIT_MAX_DIST);

	protected static double abs(final double a) {
		return Math.abs(a);
	}

	/**
	 * Compute the inverse speed transformation to the right handed, moving
	 * coordinate-system with relative speed <code>v0</code> and positive
	 * y-axis pointing from <code>a</code> to <code>b</code>.
	 * 
	 * TODO Maybe thios should operate on {@link Rock}s rather than
	 * {@link Point2D}s?
	 * 
	 * @param v0
	 * @param a
	 * @param b
	 * @param mat
	 * @return the given matrix
	 */
	public static AffineTransform getInverseTrafo(final Point2D v0,
			final Point2D a, final Point2D b, final AffineTransform mat) {
		double dx = b.getX() - a.getX();
		double dy = b.getY() - a.getY();
		final double d = Math.sqrt(dx * dx + dy * dy);
		dx /= d;
		dy /= d;
		mat.setTransform(dy, -dx, dx, dy, 0, 0);
		mat.translate(-v0.getX(), -v0.getY());
		return mat;
	}

	/**
	 * Iterate over all rocks and call
	 * {@link ColliderBase#computeWC(Rock, Rock, Rock, Rock, AffineTransform)}
	 * for each pair.
	 * <p>
	 * Does not change <code>pos</code>!
	 * </p>
	 * <p>
	 * Does not fire {@link RockSet#fireStateChanged()}!
	 * </p>
	 * 
	 * @see ColliderBase#computeWC(Rock, Rock, Rock, Rock, AffineTransform)
	 * @param pos
	 * @param speed
	 * @param tr
	 *            <code>null</code> creates a new instance.
	 * @return bitmask of the changed rocks
	 */
	public int compute(final RockSet<Pos> pos, final RockSet<Vel> speed,
			AffineTransform tr) {
		if (log.isDebugEnabled())
			log.debug("compute()");
		int hits = 0;
		if (tr == null)
			tr = new AffineTransform();
		else
			tr.setToIdentity();
		// TUNE Parallel
		for (int B = 0; B < RockSet.ROCKS_PER_SET; B++)
			for (int A = 0; A < B; A++) {
				if (log.isDebugEnabled())
					log.debug("Compute hit " + A + "<->" + B);
				if (computeWC(pos.getRock(A), pos.getRock(B), speed.getRock(A),
						speed.getRock(B), tr)) {
					// mark the rocks' bits hit
					hits |= 1 << A;
					hits |= 1 << B;
				}
			}
		if (log.isDebugEnabled())
			log.debug("hit rocks: " + Integer.toBinaryString(hits));
		return hits;
	}

	/**
	 * Compute a collision in collission-coordinates.
	 * 
	 * @param va
	 *            speed of rock a
	 * @param vb
	 *            speed of rock b (zero before the hit)
	 */
	public abstract void computeCC(final Rock<Vel> va, final Rock<Vel> vb);

	/**
	 * Check distance, speed of approach, transform speeds to
	 * collission-coordinates, call {@link #computeCC(Rock, Rock)} and transform
	 * back to wc afterwards.
	 * <p>
	 * The angle (not the angular speed!) remains in WC and untouched!
	 * </p>
	 * 
	 * @param xa
	 *            position before the hit (WC)
	 * @param xb
	 *            position before the hit (WC)
	 * @param va
	 *            speed before the hit (CC)
	 * @param vb
	 *            speed before the hit (usually zero)
	 * @param mat
	 *            may be null. If not avoids frequent instanciations
	 * @return <code>true</code> hit, <code>false</code> no hit.
	 */
	protected boolean computeWC(final Rock<Pos> xa, final Rock<Pos> xb,
			final Rock<Vel> va, final Rock<Vel> vb, AffineTransform mat) {
		if (mat == null)
			mat = new AffineTransform();
		// check distance
		if (xa.p().distanceSq(xb.p()) > MaxDistSq) {
			if (log.isDebugEnabled())
				log.debug("Too far away distance="
						+ (xa.p().distance(xb.p()) - (_Rad + _Rad)));
			return false;
		}
		// change the coordinate system to collission-coordinates
		final Rock<Vel> _va = (Rock<Vel>) va.clone();
		final Rock<Vel> _vb = (Rock<Vel>) vb.clone();
		getInverseTrafo(vb.p(), xa.p(), xb.p(), mat);
		try { // transform
			mat.inverseTransform(va.p(), _va.p());
			mat.inverseTransform(vb.p(), _vb.p());
		} catch (final NoninvertibleTransformException e) {
			throw new RuntimeException("Matrix must be invertible", e);
		}
		// check speed of approach
		if (!_va.isNotZero())
			return false;
		if (log.isDebugEnabled())
			log.debug("hit!");

		// physical model
		computeCC(_va, _vb);

		// re-transform
		mat.transform(_va.p(), va.p());
		va.setA(_va.getA());
		mat.transform(_vb.p(), vb.p());
		vb.setA(_vb.getA());
		return true;
	}
}
