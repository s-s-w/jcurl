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
 * Interface for n-dimensional curves <code>f : R^1 -&gt; R^n</code>.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public interface R1RNFunction {

	/**
	 * Compute the c'th derivative of all dimensions at <code>t</code>.
	 * 
	 * Default implementation via iteration over {@link #at(int, int, double)}.
	 * 
	 * @param c
	 *            derivative (0=location, 1:speed, ...)
	 * @param t
	 *            t-value (input)
	 * @param ret
	 *            return value container
	 * @return the c'th derivative at <code>t</code>
	 */
	public abstract double[] at(final int c, final double t, double[] ret);

	/**
	 * Compute the c'th derivative of all dimensions at <code>t</code>.
	 * 
	 * @param c
	 *            derivative (0=location, 1:speed, ...)
	 * @param t
	 *            t-value (input)
	 * @param ret
	 *            return value container
	 * @return the c'th derivative at <code>t</code>
	 */
	public abstract float[] at(final int c, final double t, float[] ret);

	/**
	 * Compute the c'th derivative of the given dimension at <code>t</code>.
	 * 
	 * @param dim
	 *            dimension (0,1,2,...)
	 * @param c
	 *            derivative (0=location, 1:speed, ...)
	 * @param t
	 *            t-value
	 * @return the c'th derivative at <code>t</code>
	 */
	public abstract double at(int dim, int c, double t);

	public abstract int dim();

}