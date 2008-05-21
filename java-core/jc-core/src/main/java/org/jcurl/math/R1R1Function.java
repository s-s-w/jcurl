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
package org.jcurl.math;

/**
 * A one-dimensional function <code>f : R^1 -&gt; R^1</code>. Because this is
 * the same as a 1-dimensional curve it extends
 * {@link org.jcurl.math.R1RNFunctionImpl}.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class R1R1Function extends R1RNFunctionImpl {

	protected R1R1Function() {
		super(1);
	}

	/**
	 * Compute the value <code>x</code>.
	 * 
	 * @param x
	 *            x-value
	 * @return the value at <code>x</code>
	 * @see R1R1Function#at(int, double)
	 */
	public double at(final double x) {
		return at(0, x);
	}

	/**
	 * Compute the c'th derivative at <code>x</code>.
	 * 
	 * @param c
	 *            derivative (0=location, 1:speed, ...)
	 * @param x
	 *            x-value
	 * @return the c'th derivative at <code>x</code>
	 * @see R1R1Function#at(int, int, double)
	 */
	public abstract double at(int c, double x);

	/**
	 * Compute the c'th derivative at <code>x</code>.
	 * 
	 * @param dim
	 *            must be 0
	 * @param c
	 *            derivative (0=location, 1:speed, ...)
	 * @param x
	 *            x-value
	 * @return the c'th derivative at <code>x</code>
	 * @see R1R1Function#at(int, double)
	 * @throws IllegalArgumentException
	 *             if <code>dim != 0</code>
	 */
	@Override
	public double at(final int dim, final int c, final double x) {
		if (dim != 0)
			throw new IllegalArgumentException("Dimension must be 0");
		return this.at(c, x);
	}

}