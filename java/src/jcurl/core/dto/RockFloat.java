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
 * Either location or speed of a rock. This class is mostly for display and
 * storage means. For computational correctness better use special classes.
 * 
 * @see jcurl.core.RockSet
 * @see jcurl.core.dto.RockInt
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockFloat extends Rock implements Serializable {

    float x;

    float y;

    float alpha;

    public RockFloat() {
        this(0, 0, 0);
    }

    public RockFloat(float x, float y, float alpha) {
        this.x = x;
        this.y = y;
        this.alpha = alpha;
    }

    public Object clone() {
        return new RockFloat(this.x, this.y, this.alpha);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Rock))
            return false;
        final Rock b = (Rock) obj;
        return getX() == b.getX() && getY() == b.getY() && getZ() == b.getZ();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return alpha;
    }

    public boolean nonzero() {
        return x * x + y * y > 1e-4;
    }

    public void setLocation(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public void setX(double x) {
        this.x = (float) x;
    }

    public void setY(double y) {
        this.y = (float) y;
    }

    public void setZ(double alpha) {
        this.alpha = (float) alpha;
    }
}