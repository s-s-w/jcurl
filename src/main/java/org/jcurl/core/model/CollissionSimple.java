/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.model;

import org.jcurl.core.base.Collider;
import org.jcurl.core.base.Rock;

/**
 * A very simple hit-model using conservation of energy and momentum.
 * <p>
 * Compute collissions without bothering about inertia. Only exchanges the
 * speed-components along the hit-direction of the two involved rocks. Only
 * conservation of momentum is obeyed, e.g. spin is neglected.
 * 
 * @see org.jcurl.core.model.SlideStraight
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CollissionSimple.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CollissionSimple extends Collider {
    public void computeRC(final Rock va, final Rock vb) {
        final double tmp = va.getY();
        va.setLocation(va.getX(), vb.getY());
        vb.setLocation(vb.getX(), tmp);
    }

    public String description() {
        return "Simple collissions";
    }
}