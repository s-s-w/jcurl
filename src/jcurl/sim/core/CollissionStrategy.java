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

import org.apache.log4j.Logger;

import jcurl.core.Rock;
import jcurl.core.RockSet;

/**
 * Abstract base class for collission models.
 * 
 * @see jcurl.sim.core.RunComputer
 * @see jcurl.sim.core.SlideStrategy
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CollissionStrategy {

    private static final Logger log = Logger
            .getLogger(CollissionStrategy.class);

    protected static final double sqr(final double a) {
        return a * a;
    }

    /**
     * 
     * @param xa
     *            location of rock a
     * @param xb
     *            location of rock b
     * @param va
     *            speed of rock a
     * @param vb
     *            speed of rock b
     * @return true: hit, false: no hit
     */
    public abstract boolean compute(final Rock xa, final Rock xb,
            final Rock va, final Rock vb);

    /**
     * Iterate over all rocks and call
     * {@link CollissionStrategy#compute(Rock, Rock, Rock, Rock)}for each pair.
     * 
     * @see CollissionStrategy#compute(Rock, Rock, Rock, Rock)
     * @param pos
     * @param speed
     * @return bitmask of the changed rocks
     */
    public int compute(RockSet pos, RockSet speed) {
        if (log.isDebugEnabled())
            log.debug("compute()");
        int hits = 0;
        for (int B = 0; B < RockSet.ROCKS_PER_SET; B++) {
            for (int A = 0; A < B; A++) {
                final Rock xa = pos.getRock(A);
                final Rock xb = pos.getRock(B);
                final Rock va = speed.getRock(A);
                final Rock vb = speed.getRock(B);
                if (compute(xa, xb, va, vb)) {
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
}