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
package org.jcurl.core.base;

import java.io.Serializable;

/**
 * Either location or speed of a rock. This class is mostly for display and
 * storage means. The value array is accessible for direct use with e.g.
 * {@link org.jcurl.math.R1RNFunction#at(int, double, float[])}.
 * 
 * @see org.jcurl.core.base.PositionSet
 * @see org.jcurl.core.base.RockDouble
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:RockFloat.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
class RockFloat extends Rock implements Serializable {

    private static final long serialVersionUID = 3219049101239057245L;

    private final float[] x = new float[3];

    public RockFloat() {
        this(0, 0, 0);
    }

    public RockFloat(final float x, final float y, final float alpha) {
        this.x[0] = x;
        this.x[1] = y;
        this.x[2] = alpha;
        dirty = true;
    }

    @Override
    public Object clone() {
        return new RockFloat(x[0], x[1], x[2]);
    }

    @Override
    public double getX() {
        return x[0];
    }

    @Override
    public double getY() {
        return x[1];
    }

    @Override
    public double getZ() {
        return x[2];
    }

    @Override
    public int hashCode() {
        // http://www.angelikalanger.com/Articles/JavaSpektrum/03.HashCode/03.HashCode.html
        // hashcode N = hashcode N-1 * multiplikator + feldwert N
        int hash = 17;
        final int fact = 59;
        for (int i = 0; i < 3; i++) {
            hash *= fact;
            hash += x[0] == 0.0 ? 0 : java.lang.Float.floatToIntBits(x[0]);
        }
        return hash;
    }

    @Override
    public boolean nonZero() {
        return x[0] * x[0] + x[1] * x[1] > 1e-12;
    }

    @Override
    public void setLocation(final double x, final double y) {
        this.x[0] = (float) x;
        this.x[1] = (float) y;
        dirty = true;
    }

    @Override
    public void setLocation(final double x, final double y, final double z) {
        this.x[0] = (float) x;
        this.x[1] = (float) y;
        this.x[2] = (float) z;
        dirty = true;
    }

    @Override
    public void setX(final double x) {
        this.x[0] = (float) x;
        dirty = true;
    }

    @Override
    public void setY(final double y) {
        x[1] = (float) y;
        dirty = true;
    }

    @Override
    public void setZ(final double alpha) {
        x[2] = (float) alpha;
        dirty = true;
    }
}