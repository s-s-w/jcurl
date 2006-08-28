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
package org.jcurl.core.base;

import java.io.Serializable;

/**
 * Either location or speed of a rock. This class is mostly for display and
 * storage means.
 * 
 * @see org.jcurl.core.base.PositionSet
 * @see org.jcurl.core.base.RockDouble
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockFloat extends Rock implements Serializable {

    private static final long serialVersionUID = 3219049101239057245L;

    private final float[] x = new float[3];

    public RockFloat() {
        this(0, 0, 0);
    }

    public RockFloat(float x, float y, float alpha) {
        this.x[0] = x;
        this.x[1] = y;
        this.x[2] = alpha;
    }

    public Object clone() {
        return new RockFloat(this.x[0], this.x[1], this.x[2]);
    }

    public double getX() {
        return x[0];
    }

    public double getY() {
        return x[1];
    }

    public double getZ() {
        return x[2];
    }

    public void setLocation(double x, double y) {
        this.x[0] = (float) x;
        this.x[1] = (float) y;
    }

    public void setLocation(double x, double y, double z) {
        this.x[0] = (float) x;
        this.x[1] = (float) y;
        this.x[2] = (float) z;
    }

    public void setX(double x) {
        this.x[0] = (float) x;
    }

    public void setY(double y) {
        this.x[1] = (float) y;
    }

    public void setZ(double alpha) {
        this.x[2] = (float) alpha;
    }
}