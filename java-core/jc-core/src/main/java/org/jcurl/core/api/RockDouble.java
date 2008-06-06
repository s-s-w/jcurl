/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
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

import java.io.Serializable;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:RockDouble.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class RockDouble<R extends RockType> extends Rock<R> implements
		Serializable {
	private static final long serialVersionUID = 2337028316325540776L;
	private volatile double a;
	private volatile double x;
	private volatile double y;

	public RockDouble() {
		this(0, 0, 0);
	}

	public RockDouble(final double x, final double y, final double alpha) {
		this.x = x;
		this.y = y;
		this.a = alpha;
	}

	public RockDouble(final Rock<R> r) {
		this(r.getX(), r.getY(), r.getA());
	}

	@Override
	public Object clone() {
		return new RockDouble<R>(x, y, a);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RockDouble other = (RockDouble) obj;
		if (Double.doubleToLongBits(a) != Double.doubleToLongBits(other.a))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public double getA() {
		return a;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(a);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ temp >>> 32);
		return result;
	}

	@Override
	public boolean isNotZero() {
		return x * x + y * y > 1e-4;
	}

	@Override
	public void setA(final double alpha) {
		if (alpha == a)
			return;
		a = alpha;
		fireStateChanged();
	}

	@Override
	public void setLocation(final double x, final double y, final double a) {
		if (x == this.x && y == this.y && a == this.a)
			return;
		this.x = x;
		this.y = y;
		this.a = a;
		fireStateChanged();
	}

	@Override
	public void setLocation(final double[] pt) {
		if (pt.length != 3)
			throw new IllegalArgumentException();
		setLocation(pt[0], pt[1], pt[2]);
	}

	@Override
	public void setX(final double x) {
		if (x == this.x)
			return;
		this.x = x;
		fireStateChanged();
	}

	@Override
	public void setY(final double y) {
		if (y == this.y)
			return;
		this.y = y;
		fireStateChanged();
	}
}