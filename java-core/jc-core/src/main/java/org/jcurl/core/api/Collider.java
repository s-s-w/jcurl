/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
package org.jcurl.core.api;

import java.awt.geom.AffineTransform;
import java.util.Map;

import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;

/**
 * Compute rock collissions.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:Collider.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public interface Collider extends PropModel, Strategy {

	/**
	 * Check for and compute all collissions.
	 * <p>
	 * Does not change <code>pos</code>!
	 * </p>
	 * <p>
	 * Does not fire {@link RockSet#fireStateChanged()}!
	 * </p>
	 * 
	 * @param pos
	 *            the positions (before and after the hit)
	 * @param speed
	 *            (before and after the hit)
	 * @param tr
	 *            Helper reference to avoid internal instanciations.
	 *            <code>null</code> creates a new instance.
	 * @return bitmask of the changed rocks
	 */
	public abstract int compute(RockSet<Pos> pos, RockSet<Vel> speed,
			AffineTransform tr);

	public void init(Map<CharSequence, Measure> ice);
}