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

import org.jcurl.core.math.MathException;

/**
 * Rock standing still.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurlerStill extends CurlerCurved {

    private static final CurveRock zero = new CurveRockBase() {
        protected double value(double t, int derivative, int component)
                throws MathException {
            return 0;
        }
    };

    private double teeCurl;

    private double teeTime;

    public CurveRock computeRC(double v, double omega) {
        return zero;
    }

    public double getTeeCurl() {
        return teeCurl;
    }

    public double getTeeTime() {
        return teeTime;
    }

    public void setTeeCurl(double teeCurl) {
        this.teeCurl = teeCurl;
    }

    public void setTeeTime(double teeTime) {
        this.teeTime = teeTime;
    }

}
