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

package org.jcurl.core.api;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * EXPERIMENTAL - A generic, abstract base.
 * 
 * @param <XY>
 *            location data. Must be convertible to and from {@link Double}
 *            (meter).
 * @param <A>
 *            angular data. Must be convertible to and from {@link Double}
 *            (radians).
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public interface GenericRock<XY, A> extends IChangeSupport, Cloneable,
		Serializable {
	A getA();

	/**
	 * Convenience method for display purposes.
	 */
	AffineTransform getAffineTransform();

	/**
	 * Convenience.
	 * 
	 * @see #getAffineTransform()
	 * @return {@link AffineTransform#getTranslateX()}
	 */
	double getDoubleX();

	/**
	 * Convenience.
	 * 
	 * @see #getAffineTransform()
	 * @return {@link AffineTransform#getTranslateY()}
	 */
	double getDoubleY();

	/** Convenience method for display purposes. */
	Point2D getPoint2D();

	XY getX();

	XY getY();

	/**
	 * Convenience method to check if zero or not.
	 * 
	 * @return whether x or y are non-zero
	 */
	boolean isNotZero(boolean checkAngle);

	void setLocation(double x, double y, double a);

	/** Convenience method for display feedback. */
	void setLocation(Point2D p);

	void setLocation(XY x, XY y, A a);
}
