/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2009 M. Rohrmoser
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

import java.awt.Shape;

/**
 * Indicate that the class may support a native {@link #toShape(double, double)}
 * method.
 * 
 * @see Shaper
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public interface Shapeable {

	/**
	 * If not <code>null</code> the resulting shape should be exact - neiter
	 * interpolation nor approximation.
	 * 
	 * @param tmin
	 * @param tmax
	 * @return <code>null</code> if there's no natural, exact shape.
	 */
	Shape toShape(double tmin, double tmax);
}
