/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
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
package jcurl.sim.core;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import jcurl.core.JCLoggerFactory;
import jcurl.core.dto.RockProps;
import jcurl.model.PositionSet;
import jcurl.model.Rock;
import jcurl.model.RockSet;
import jcurl.model.SpeedSet;

import org.apache.ugli.ULogger;

/**
 * Abstract base class for collission models.
 * 
 * @see jcurl.sim.core.SlideStrategy
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CollissionStrategy extends ModelBase {

    private static final float _Rad = RockProps.DEFAULT.getRadius();

    private static final float HIT_MAX_DIST = 1e-6F;

    private static final ULogger log = JCLoggerFactory
            .getLogger(CollissionStrategy.class);

    /** Maximum distance [m] of two rocks to consider them touching */
    public static final double MaxDistSq = sqr(_Rad + _Rad + HIT_MAX_DIST);

    protected static double abs(final double a) {
        return Math.abs(a);
    }

    /**
     * Compute the trafo to the right handed coordinate-system with origin
     * <code>orig</code> and positive y-axis pointing from <code>a</code> to
     * <code>b</code>.
     * 
     * @param orig
     * @param a
     * @param b
     * @param mat
     * @return the given matrix
     */
    public static AffineTransform getInverseTrafo(final Point2D orig,
            final Point2D a, final Point2D b, final AffineTransform mat) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        final double d = Math.sqrt(dx * dx + dy * dy);
        dx /= d;
        dy /= d;
        mat.setTransform(dy, -dx, dx, dy, 0, 0);
        mat.translate(-orig.getX(), -orig.getY());
        return mat;
    }

    public static CollissionStrategy newInstance(final Class clz) {
        final Class parent = CollissionStrategy.class;
        if (!parent.isAssignableFrom(clz))
            throw new IllegalArgumentException("Class [" + clz.getName()
                    + "] is no descendant of [" + parent.getName() + "]");
        try {
            return (CollissionStrategy) clz.newInstance();
        } catch (InstantiationException e) {
            final IllegalArgumentException ex = new IllegalArgumentException();
            ex.initCause(e);
            throw ex;
        } catch (IllegalAccessException e) {
            final IllegalArgumentException ex = new IllegalArgumentException();
            ex.initCause(e);
            throw ex;
        }
    }

    protected static byte sgn(final double a) {
        if (a < 0)
            return -1;
        if (a > 0)
            return 1;
        return 0;
    }

    protected static final double sqr(final double a) {
        return a * a;
    }

    protected static double sqrt(final double a) {
        return Math.sqrt(a);
    }

    /**
     * Iterate over all rocks and call
     * {@link CollissionStrategy#computeWC(Rock, Rock, Rock, Rock, AffineTransform)}
     * for each pair.
     * 
     * @see CollissionStrategy#computeWC(Rock, Rock, Rock, Rock,
     *      AffineTransform)
     * @param pos
     * @param speed
     * @return bitmask of the changed rocks
     */
    public int compute(PositionSet pos, SpeedSet speed) {
        if (log.isDebugEnabled())
            log.debug("compute()");
        int hits = 0;
        final AffineTransform mat = new AffineTransform();
        for (int B = 0; B < RockSet.ROCKS_PER_SET; B++) {
            for (int A = 0; A < B; A++) {
                if (log.isDebugEnabled())
                    log.debug("Compute hit " + A + "<->" + B);
                if (computeWC(pos.getRock(A), pos.getRock(B), speed.getRock(A),
                        speed.getRock(B), mat)) {
                    // mark the rocks' bits hit
                    hits |= (1 << A);
                    hits |= (1 << B);
                }
            }
        }
        if (log.isDebugEnabled())
            log.debug("hit rocks: " + Integer.toBinaryString(hits));
        return hits;
    }

    /**
     * Compute a collision in rock-coordinates.
     * 
     * @param va
     *            speed of rock a
     * @param vb
     *            speed of rock b (zero before the hit)
     */
    public abstract void computeRC(final Rock va, final Rock vb);

    /**
     * Check distance, speed of approach, transform speeds to rock-coordinates,
     * call {@link #computeRC(Rock, Rock)}and transform back to wc afterwards.
     * 
     * @param xa
     * @param xb
     * @param va
     * @param vb
     * @param mat
     *            may be null. If not avoids frequent instanciations
     * @return <code>true</code> hit, <code>false</code> no hit.
     */
    protected boolean computeWC(final Rock xa, final Rock xb, final Rock va,
            final Rock vb, AffineTransform mat) {
        if (mat == null)
            mat = new AffineTransform();
        // check distance
        if (xa.distanceSq(xb) > MaxDistSq) {
            if (log.isDebugEnabled())
                log.debug("Too far away distance="
                        + (xa.distance(xb) - (_Rad + _Rad)));
            return false;
        }
        // change the coordinate system to rock-coordinates
        final Rock _va = (Rock) va.clone();
        final Rock _vb = (Rock) vb.clone();
        getInverseTrafo(vb, xa, xb, mat);
        try { // transform
            mat.inverseTransform(va, _va);
            mat.inverseTransform(vb, _vb);
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException("Matrix must be invertible", e);
        }
        // check speed of approach
        if (!_va.nonZero())
            return false;
        if (log.isDebugEnabled())
            log.debug("hit!");

        // physical model
        computeRC(_va, _vb);

        // re-transform
        mat.transform(_va, va);
        va.setZ(_va.getZ());
        mat.transform(_vb, vb);
        vb.setZ(_vb.getZ());
        return true;
    }
}