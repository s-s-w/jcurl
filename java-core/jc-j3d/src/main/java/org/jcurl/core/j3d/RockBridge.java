/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
package org.jcurl.core.j3d;

import javax.vecmath.Point4d;

import org.jcurl.core.base.Rock;

public class RockBridge extends Point4d implements Cloneable {

    private static final long serialVersionUID = -5623298143038016036L;

    public final Rock rock = new Rock() {

        @Override
        public Object clone() {
            return ((RockBridge) RockBridge.this.clone()).rock;
        }

        @Override
        public double getA() {
            return RockBridge.this.w;
        }

        @Override
        public double getX() {
            return RockBridge.this.x;
        }

        @Override
        public double getY() {
            return RockBridge.this.y;
        }

        @Override
        public int hashCode() {
            return RockBridge.this.hashCode();
        }

        @Override
        public boolean nonZero() {
            return RockBridge.this.x * RockBridge.this.x + RockBridge.this.y
                    * RockBridge.this.y > 1e-4;
        }

        @Override
        public void setA(final double a) {
            RockBridge.this.w = a;
        }

        @Override
        public void setLocation(final double x, final double y) {
            setX(x);
            setY(y);
        }

        @Override
        public void setLocation(final double x, final double y, final double a) {
            setLocation(x, y);
            setA(a);
        }

        @Override
        public void setLocation(final double[] pt) {
            if (pt.length != 3)
                throw new IllegalArgumentException();
            setLocation(pt[0], pt[1], pt[2]);
        }

        @Override
        public void setX(final double x) {
            RockBridge.this.x = x;
        }

        @Override
        public void setY(final double y) {
            RockBridge.this.y = y;
        }
    };

    public Object clone() {
        final RockBridge r = new RockBridge();
        r.set(this);
        return r;
    }
}
