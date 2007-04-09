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
package org.jcurl.core.base;

import java.util.Map;

import org.jcurl.core.helpers.DimVal;

/**
 * Create rock-coordinate curves for running rocks.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public interface Curler extends PropModel, Strategy, Factory {

    public abstract CurveRock computeRc(final Rock x0, final Rock v0,
            double sweepFactor);

    /**
     * Compute the (absolute) speed at the hog line for a rock released with
     * given interval time.
     * <p>
     * <code>v_0 = {@link IceSize#BACK_2_HOG} / t_S - beta t_S</code>
     * </p>
     * 
     * @param intervalTime
     * @return the hog speed.
     */
    public abstract double computeV0(final double intervalTime);

    public abstract double getDrawToTeeCurl();

    public abstract double getDrawToTeeTime();

    public void init(final Map<CharSequence, DimVal> ice);

}