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

import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.dto.RockProps;
import jcurl.math.MathVec;
import jcurl.sim.core.CollissionStrategy;

/**
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

    public int computeHit(RockSet pos, RockSet speed) {
        final float Rad = RockProps.DEFAULT.getRadius();
        final float HIT_MAX_DIST = 1e-3F;
        final double RR = sqr(Rad + Rad + HIT_MAX_DIST);
        int hits = 0;
        for (int B = 0; B < RockSet.ROCKS_PER_SET; B++) {
            for (int A = 0; A < B; A++) {
                final Rock xa = pos.getRock(A);
                final Rock xb = pos.getRock(B);
                final Rock va = speed.getRock(A);
                final Rock vb = speed.getRock(B);
                // vector from A's center to B's:
                final Point2D xr = MathVec.sub(xb, xa, new Point2D.Double());
                final double xrxr = MathVec.scal(xr, xr);
                if (xrxr > RR || !(va.nonzero() || vb.nonzero()))
                    continue;

                // get the speed of approach (A -> B):
                final Point2D vr = MathVec.sub(vb, va, new Point2D.Double());
                // get the (speed) component along xr
                double scal = MathVec.scal(xr, vr);
                double vrabs = MathVec.abs(vr);
                MathVec.mult(scal / xrxr, xr); //r *= r * dv;

                // exchange speed
                MathVec.add(va, xr, va);
                MathVec.sub(vb, xr, vb);

                // mark the rocks' bits hit
                hits |= (1 << A);
                hits |= (1 << B);
            }
        }
        return hits;
    }
}