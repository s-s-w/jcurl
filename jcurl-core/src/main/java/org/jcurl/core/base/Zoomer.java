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

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import org.jcurl.core.helpers.MutableObject;

/**
 * Smart handler for creating wc to dc transformations.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CenteredZoomer.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public abstract class Zoomer extends MutableObject {

    /**
     * Scale WC a bit to avoid int rounding errors. This is relevant only for
     * int based wc drawing operations e.g. fonts. WC objects (rocks etc.)
     * remain unaffected by this.
     */
    public static final int SCALE = 1000;

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
    public abstract AffineTransform computeWctoDcTrafo(final Rectangle dc,
            AffineTransform mat);
}