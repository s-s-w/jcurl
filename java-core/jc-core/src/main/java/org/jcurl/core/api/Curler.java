/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.core.api;

import java.awt.geom.Point2D;

import org.jcurl.core.api.RockType.Pos;

/**
 * Create rock-coordinate curves for running rocks.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:Curler.java 780 2008-03-18 11:06:30Z mrohrmoser $
 */
public interface Curler extends Factory, Strategy, PropModel {

	/**
	 * Compute the (absolute) speed at the hack for a rock released with given
	 * interval time.
	 * 
	 * @param splitTime
	 * @param broom
	 *            location (WC)
	 * @return the hack speed.
	 */
	double computeHackSpeed(double splitTime, Point2D broom);

	double computeIntervalTime(final CurveRock<Pos> wc);

	/**
	 * Release a rock.
	 * 
	 * @param broom
	 *            location (WC)
	 * @param splitTime
	 * @param a0
	 *            initial handle angle
	 * @param omega0
	 *            angular handle speed
	 * @param sweepFactor
	 * @return world coordinate curve.
	 */
	CurveRock<Pos> computeWc(Point2D broom, double splitTime, double a0,
			double omega0, double sweepFactor);

	/**
	 * Create rock-coordinate curves for running rocks.
	 * 
	 * @param a0
	 *            Initial handle angle (RC).
	 * @param v0
	 *            Initial Speed (RC or WC absolute)
	 * @param omega0
	 *            Initial angular handle speed (either WC or RC)
	 * @param sweepFactor
	 * @return trajectory (RC)
	 */
	CurveRock<Pos> computeRc(double a0, double v0, double omega0, double sweepFactor);

	double getDrawToTeeCurl();

	double getDrawToTeeTime();

	void setDrawToTeeCurl(double drawToTeeCurl);

	void setDrawToTeeTime(double drawToTeeTime);

}