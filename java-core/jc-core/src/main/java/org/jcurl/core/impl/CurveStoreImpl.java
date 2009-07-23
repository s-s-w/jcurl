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
package org.jcurl.core.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.CurveStore;
import org.jcurl.core.api.MutableObject;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.StopDetector;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.CurveCombined;
import org.jcurl.math.R1RNFunction;

/**
 * @see CurveCombined
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:CurveStoreImpl.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public class CurveStoreImpl extends MutableObject implements Serializable,
		CurveStore {
	private static final byte DIM = 3;
	private static final Log log = JCLoggerFactory
			.getLogger(CurveStoreImpl.class);
	private static final long serialVersionUID = -1485170570756670720L;
	private final CurveCombined<R1RNFunction>[] curve;
	private final StopDetector stopper;

	/**
	 * 
	 * @param stopper
	 *            May be <code>null</code> for no stop detection.
	 * @param capacity
	 *            usually {@link RockSet#ROCKS_PER_SET}
	 */
	public CurveStoreImpl(final StopDetector stopper, final int capacity) {
		this.stopper = stopper;
		curve = new CurveCombined[capacity];
		for (int i = curve.length - 1; i >= 0; i--)
			curve[i] = new CurveCombined<R1RNFunction>(DIM);
	}

	public void add(final int i, final double t, final R1RNFunction f,
			final double tstop) {
		if (log.isDebugEnabled())
			log.debug(i + "  t=" + t + " " + f);
		// unwrap if possible:
		if (f instanceof CurveRockAnalytic)
			curve[i].add(t, ((CurveRockAnalytic) f).getCurve(), true);
		else
			curve[i].add(t, f, true);
		// Stop detection. Either here or in each slider?
		if (stopper != null) {
			final double stop = stopper.compute(f, 0, tstop - t);
			if (stop > 0 && !Double.isNaN(stop)) {
				// TUNE save one instanciation?
				final double[] tmp = { 0, 0, 0 };
				curve[i].add(stop, CurveStill.newInstance(f.at(stop, 0, tmp)),
						true);
				if (log.isDebugEnabled())
					log.debug(i + "  t=" + t + " Still");
			}
		}
		fireIndexedPropertyChange("curve", i, null, curve[i]);
	}

	/**
	 * Clear everything from t on.
	 * 
	 * @see CurveCombined#dropTail(double)
	 */
	public void dropTail(final double t) {
		for (int i = curve.length - 1; i >= 0; i--)
			curve[i].dropTail(t);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CurveStoreImpl other = (CurveStoreImpl) obj;
		if (!Arrays.equals(curve, other.curve))
			return false;
		if (stopper == null) {
			if (other.stopper != null)
				return false;
		} else if (!stopper.equals(other.stopper))
			return false;
		return true;
	}

	public R1RNFunction getCurve(final int i) {
		return curve[i];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(curve);
		result = prime * result + (stopper == null ? 0 : stopper.hashCode());
		return result;
	}

	public Iterator<Iterable<Entry<Double, R1RNFunction>>> iterator() {
		return new Iterator<Iterable<Entry<Double, R1RNFunction>>>() {
			int current = 0;

			public boolean hasNext() {
				return current < curve.length;
			}

			public Iterable<Entry<Double, R1RNFunction>> next() {
				return curve[current++];
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public Iterator<Entry<Double, R1RNFunction>> iterator(final int i) {
		return curve[i].iterator();
	}

	public void reset(final int i) {
		curve[i].clear();
		fireIndexedPropertyChange("curve", i, curve[i], curve[i]);
	}
}
