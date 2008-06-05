/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
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

package org.jcurl.math;

/**
 * Re-implementation of <a
 * href="http://download.java.net/javadesktop/scenario/releases/0.5/javadoc/com/sun/scenario/animation/Interpolator.html">Scenario
 * Interpolator</a>.
 * 
 * @see Interpolators
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public interface Interpolator {

	/**
	 * This function takes an input value between 0 and 1 and returns another
	 * value, also between 0 and 1. The purpose of the function is to define how
	 * time (represented as a (0-1) fraction of the duration of an animation) is
	 * altered to derive different value calculations during an animation.
	 * 
	 * @param fraction
	 *            a value between 0 and 1, representing the elapsed fraction of
	 *            a time interval (either an entire animation cycle or an
	 *            interval between two KeyFrames, depending on where this
	 *            Interpolator is used)
	 * @return a value between 0 and 1. Values outside of this boundary may be
	 *         clamped to the interval [0,1] and cause undefined results.
	 */
	float interpolate(float fraction);

}
