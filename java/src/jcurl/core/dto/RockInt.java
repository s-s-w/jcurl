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
package jcurl.core.dto;

import java.io.Serializable;

import jcurl.core.Rock;

/**
 * Either location or speed of a rock.
 * 
 * @see jcurl.core.RockSet
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockInt extends Rock implements Serializable {
    static final float UNITS_PER_METER = 1.0e6F;

    static final float UNITS_PER_METER_PER_MILLI = 1.0e3F;

    final float scale;

    int x;

    int y;

    int alpha;

    public RockInt() {
        this(0, 0, 0, UNITS_PER_METER);
    }

    public RockInt(float x, float y, float alpha, final float scale) {
        this.scale = scale;
        setX(x);
        setY(y);
        setZ(alpha);
    }

    private RockInt(int x, int y, int alpha, final float scale) {
        this.scale = scale;
        this.x = x;
        this.y = y;
        this.alpha = alpha;
    }

    public Object clone() {
        return new RockInt(x, y, alpha, scale);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RockInt))
            return false;
        final RockInt b = (RockInt) obj;
        return x == b.x && y == b.y && alpha == b.alpha;
    }

    public double getX() {
        return x / scale;
    }

    public double getY() {
        return y / scale;
    }

    public double getZ() {
        return alpha / scale;
    }

    public boolean nonzero() {
        return x * x + y * y > 0;
    }

    public void setLocation(double x, double y) {
        setX(x);
        setY(y);
    }

    public void setX(double x) {
        this.x = (int) (x * scale);
    }

    public void setY(double y) {
        this.y = (int) (y * scale);
    }

    public void setZ(double alpha) {
        this.alpha = (int) (alpha * scale);
    }
}