/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.core.curved;

import java.awt.geom.AffineTransform;

import org.jcurl.core.dto.Rock;

public class CurveWorld implements CurveRock {

    private final CurveRock c;

    private final AffineTransform t;

    public CurveWorld(CurveRock c, AffineTransform t) {
        this.c = c;
        this.t = t;
    }

    public int dimension() {
        return c.dimension();
    }

    public double[] value(double t, double[] ret) {
        return null;
    }

    public double[] value(double t, int derivative, double[] ret) {
        return null;
    }

    public Rock value(double t, int derivative, Rock ret) {
        return null;
    }

    public Rock value(double t, Rock ret) {
        return null;
    }

}
