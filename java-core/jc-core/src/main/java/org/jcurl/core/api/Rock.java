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

import javax.swing.event.ChangeListener;

/**
 * Base class for rock information (either location or speed). The 3rd component (<code>a</code>)
 * is the handle angle in radians.
 * <h3>Why is this type not generic, e.g. Rock&lt;XY extends Number, A extends
 * Number&gt;</h3>
 * <p>
 * Yes, this would be preferable, especially for a neat integration into <a
 * href="http://www.jscience.org">JScience</a> calculus.
 * </p>
 * <p>
 * As the function and curve classes take the order of the desired derivative as
 * an input parameter to {@link CurveRock#at(double, int, Rock)} there's a
 * pardigm shift necessary to use jscience-like typing.
 * </p>
 * <p>
 * And - at least for the moment - I hesitate to add a 700K dependency to this
 * lib.
 * </p>
 * <p>
 * But it looks like this absolutely central class is subject to some more
 * refactoring.
 * </p>
 * <h2>Possible ways out</h2>
 * <ul>
 * <li>understand and adopt the <a
 * href="http://www.jscience.org/api/org/jscience/mathematics/function/Function.html#differentiate(org.jscience.mathematics.function.Variable)">jscience
 * function concept</a>.</li>
 * </ul>
 * 
 * @param <R>
 *            indicate position, velocity or acceleration semantics.
 * @see org.jcurl.core.api.RockSet
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:Rock.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public abstract class Rock<R extends RockType> implements IChangeSupport,
		Cloneable {
	private static class ImmutableRock<X extends RockType> extends Rock<X> {
		private final Rock<X> base;

		ImmutableRock(final Rock<X> base) {
			this.base = base;
		}

		@Override
		public Object clone() {
			return this;
		}

		@Override
		public double getA() {
			return base.getA();
		}

		@Override
		public boolean getValueIsAdjusting() {
			return false;
		}

		@Override
		public double getX() {
			return base.getX();
		}

		@Override
		public double getY() {
			return base.getY();
		}

		@Override
		public boolean isNotZero() {
			return base.isNotZero();
		}

		@Override
		public void setA(final double a) {
			throw new UnsupportedOperationException("Not supported.");
		}

		@Override
		public void setLocation(final double x, final double y, final double a) {
			throw new UnsupportedOperationException("Not supported.");
		}

		@Override
		public void setValueIsAdjusting(final boolean valueIsAdjusting) {
			throw new UnsupportedOperationException("Not supported.");
		}
	}

	private class RockPoint extends Point2D {
		@Override
		public double getX() {
			return Rock.this.getX();
		}

		@Override
		public double getY() {
			return Rock.this.getY();
		}

		@Override
		public void setLocation(final double x, final double y) {
			Rock.this.setLocation(x, y, getA());
		}
	}

	public static <Y extends RockType> Rock<Y> immutable(final Rock<Y> r) {
		return new ImmutableRock<Y>(r);
	}

	protected final transient ChangeSupport change = new ChangeSupport(this);
	private transient volatile boolean dirty = true;
	private transient final Point2D p = new RockPoint();
	private transient volatile AffineTransform trafo;
	private transient boolean valueIsAdjusting = false;

	public void addChangeListener(final ChangeListener l) {
		change.addChangeListener(l);
	}

	@Override
	public abstract Object clone();

	protected void fireStateChanged() {
		dirty = true;
		change.fireStateChanged();
	}

	/** Angle in radians */
	public abstract double getA();

	/**
	 * Location and angle as a transformation wc -&gt; rock coordinates.
	 * 
	 * @return reference to the internal (uncloned!) trafo.
	 */
	public AffineTransform getAffineTransform() {
		if (trafo != null && !dirty)
			return trafo;
		synchronized (this) {
			if (trafo == null)
				trafo = new AffineTransform();
			else
				trafo.setToIdentity();
			trafo.translate(getX(), getY());
			// TUNE jdk 1.6 brings a non-trigonometric rotate!
			trafo.rotate(getA());
			dirty = false;
		}
		return trafo;
	}

	public ChangeListener[] getChangeListeners() {
		return change.getChangeListeners();
	}

	public boolean getValueIsAdjusting() {
		return valueIsAdjusting;
	}

	/**
	 * WC location x in meters
	 * 
	 * @see #p()
	 */
	public abstract double getX();

	/**
	 * WC location y in meters
	 * 
	 * @see #p()
	 */
	public abstract double getY();

	/**
	 * Convenience method to check if zero or not.
	 * 
	 * @return whether x or y are non-zero
	 */
	public abstract boolean isNotZero();

	/**
	 * The current location as {@link Point2D}. This links directly to the
	 * internal representation, so it's read/write!
	 */
	public Point2D p() {
		return p;
	}

	public void removeChangeListener(final ChangeListener l) {
		change.removeChangeListener(l);
	}

	/** The current angle in radians */
	@Deprecated
	public abstract void setA(double a);

	public abstract void setLocation(double x, double y, double a);

	public void setLocation(final Rock<R> r) {
		this.setLocation(r.getX(), r.getY(), r.getA());
	}

	public void setValueIsAdjusting(final boolean valueIsAdjusting) {
		final boolean old = this.valueIsAdjusting;
		if (old == valueIsAdjusting)
			return;
		this.valueIsAdjusting = valueIsAdjusting;
		fireStateChanged();
	}

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		buf.append('[');
		buf.append(getX());
		buf.append(", ");
		buf.append(getY());
		buf.append(", ");
		buf.append(getA());
		buf.append(']');
		return buf.toString();
	}
}