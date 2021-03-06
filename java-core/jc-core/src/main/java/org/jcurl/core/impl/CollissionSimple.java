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
package org.jcurl.core.impl;

import java.util.Map;

import org.jcurl.core.api.Measure;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockType.Vel;

/**
 * A very simple hit-model using conservation of energy and momentum.
 * <p>
 * Compute collissions without bothering about inertia. Only exchanges the
 * speed-components along the hit-direction of the two involved rocks. Only
 * conservation of momentum is obeyed, e.g. spin is neglected.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:CollissionSimple.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CollissionSimple extends ColliderBase {

	private static final long serialVersionUID = -5423884872114753286L;

	public CollissionSimple() {}

	public CollissionSimple(final Map<CharSequence, Measure> ice) {
		init(ice);
	}

	@Override
	public void computeCC(final Rock<Vel> va, final Rock<Vel> vb) {
		final double tmp = va.getY();
		va.setLocation(va.getX(), vb.getY(), va.getA());
		vb.setLocation(vb.getX(), tmp, vb.getA());
	}

	public void init(final Map<CharSequence, Measure> params) {
		internalInit(params);
	}
}