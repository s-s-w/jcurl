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
package jcurl.sim.model;

import java.awt.geom.Point2D;

import org.apache.log4j.Logger;

import jcurl.core.Rock;
import jcurl.core.dto.RockProps;
import jcurl.math.MathVec;
import jcurl.sim.core.CollissionStrategy;

/**
 * A very simple hit-model using conservation of energy and momentum.
 * <p>
 * Compute collissions without bothering about inertia. A very simple hit-model
 * only exchanging the speed-components along the hit-direction of the first two
 * involved rocks. Only conservation of momentum is obeyed, e.g. spin is
 * neglected.
 * 
 * @see jcurl.sim.core.model.SlideSimple
 * @see jcurl.sim.core.model.CollissionSimpleTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CollissionSimple extends CollissionStrategy {

    private static final float HIT_MAX_DIST = 1e-3F;

    private static final Logger log = Logger.getLogger(CollissionSimple.class);

    private static final float Rad = RockProps.DEFAULT.getRadius();

    private static final double RR = sqr(Rad + Rad + HIT_MAX_DIST);

    public boolean compute(final Rock xa, final Rock xb, final Rock va,
            final Rock vb) {
        // vector from A's center to B's:
        final Point2D xr = MathVec.sub(xb, xa, new Point2D.Double());
        final double xrxr = MathVec.scal(xr, xr);
        if (xrxr > RR || !(va.nonzero() || vb.nonzero()))
            return false;
        if (log.isDebugEnabled())
            log.debug("hit!");
        // get the speed of approach (A -> B):
        final Point2D vr = MathVec.sub(vb, va, new Point2D.Double());
        // get the (speed) component along xr
        double scal = MathVec.scal(xr, vr);
        double vrabs = MathVec.abs(vr);
        MathVec.mult(scal / xrxr, xr, xr); //r *= r * dv;

        // exchange speed
        MathVec.add(va, xr, va);
        MathVec.sub(vb, xr, vb);
        return true;
    }
}