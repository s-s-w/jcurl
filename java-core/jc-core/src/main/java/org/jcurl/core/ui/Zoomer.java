/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
package org.jcurl.core.ui;

import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;

import org.jcurl.core.api.MutableObject;

/**
 * Smart handler for creating wc to dc transformations.
 * 
 * @deprecated Can we get rid of this?
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:CenteredZoomer.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
@Deprecated
public abstract class Zoomer extends MutableObject {

	/**
	 * Map the zoomer's wc viewport to the given dc viewport.
	 * 
	 * @param dc
	 *            Viewport on Screen
	 * @param mat
	 *            Matrix to add the transformation to, usually call yourself a
	 *            {@link AffineTransform#setToIdentity()}&nbsp;before.
	 * @return the transformation
	 */
	public abstract AffineTransform computeWctoDcTrafo(
			final RectangularShape dc, AffineTransform mat);
}