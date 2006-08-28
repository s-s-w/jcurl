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

/**
 * Constant motion, no curl, no friction.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurlerTrivial extends CurlerCurved {

    public CurveRock computeRC(final double v, final double omega) {
        return new CurveRockBase() {
            protected double value(double t, int derivative, int component) {
                switch (component) {
                case 0:
                    return 0;
                case 1:
                    return t * v;
                case 2:
                    return t * omega;
                default:
                    throw new IllegalArgumentException();
                }
            }
        };
    }

    public double getTeeCurl() {
        return 0;
    }

    public double getTeeTime() {
        return Double.POSITIVE_INFINITY;
    }

    public void setTeeCurl(double teeCurl) {
        // ignored
    }

    public void setTeeTime(double teeTime) {
        // ignored
    }

}
