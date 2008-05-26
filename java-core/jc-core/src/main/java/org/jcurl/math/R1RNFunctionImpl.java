/*
 * jcurl java curling software framework http://www.jcurl.org
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

import java.io.Serializable;

/**
 * Abstract base class for n-dimensional curves <code>f : R^1 -&gt; R^n</code>.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class R1RNFunctionImpl implements R1RNFunction, Serializable {

	private final transient int dim;

	protected R1RNFunctionImpl(final int dim) {
		this.dim = dim;
	}

	public double[] at(final int c, final double t, double[] ret) {
		if (ret == null)
			ret = new double[dim];
		for (int i = dim - 1; i >= 0; i--)
			ret[i] = this.at(i, c, t);
		return ret;
	}

	public float[] at(final int c, final double t, float[] ret) {
		if (ret == null)
			ret = new float[dim];
		for (int i = dim - 1; i >= 0; i--)
			ret[i] = (float) this.at(i, c, t);
		return ret;
	}

	public abstract double at(int dim, int c, double t);

	public final int dim() {
		return dim;
	}
}