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

import jcurl.model.Rock;
import jcurl.sim.core.CollissionStrategy;

/**
 * A very simple hit-model using conservation of energy and momentum.
 * <p>
 * Compute collissions without bothering about inertia. Only exchanges the
 * speed-components along the hit-direction of the two involved rocks. Only
 * conservation of momentum is obeyed, e.g. spin is neglected.
 * 
 * @see jcurl.sim.model.SlideStraight
 * @see jcurl.sim.model.CollissionSimpleTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CollissionSimple extends CollissionStrategy {

    public void computeRC(final Rock va, final Rock vb) {
        final double tmp = va.getY();
        va.setLocation(va.getX(), vb.getY());
        vb.setLocation(vb.getX(), tmp);
    }
}