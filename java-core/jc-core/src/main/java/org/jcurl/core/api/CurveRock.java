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

import org.jcurl.math.R1RNFunctionImpl;

/**
 * Trajectory of one Rock, in either rock-coordinates or world-coordinates.
 * 
 * <p>
 * Maybe obsolete?
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CurveRock extends R1RNFunctionImpl {
	protected CurveRock() {
		super(3);
	}

	public Rock at(final int c, final double t, Rock ret) {
		if (ret == null)
			ret = new RockDouble();
		ret.setLocation(at(0, c, t), at(1, c, t), at(2, c, t));
		return ret;
	}
}