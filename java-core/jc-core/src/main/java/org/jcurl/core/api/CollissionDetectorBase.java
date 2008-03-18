/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.api;

import org.jcurl.math.MathVec;
import org.jcurl.math.R1RNFunction;

/**
 * Find Collissions of two spheres moving along curves.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CollissionDetectorBase.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public abstract class CollissionDetectorBase implements CollissionDetector {

    public double compute(final double t0, final double tstop,
            final R1RNFunction fa, final double ra, final R1RNFunction fb,
            final double rb) {
        return compute(t0, tstop, fa, fb, MathVec.sqr(ra + rb));
    }

    public double compute(final double t0, final double tstop,
            final R1RNFunction fa, final R1RNFunction fb) {
        return compute(t0, tstop, fa, fb, CollissionDetector.RR2);
    }
}
