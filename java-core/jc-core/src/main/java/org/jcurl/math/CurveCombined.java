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
package org.jcurl.math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Combined curve. Becomes more and more similar to {@link List} with some
 * restrictions and additions.
 * 
 * @param <T>
 *            type of the parts to be combined.
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveCombined<T extends R1RNFunction> extends R1RNFunctionImpl
		implements Iterable<Entry<Double, T>>, Serializable {
	/**
	 * One segment of the combined curve.
	 * 
	 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
	 * @version $Id$
	 */
	public static class Part<U extends R1RNFunction> extends Number implements
			Entry<Double, U>, Comparable<Number> {
		private static final long serialVersionUID = 2799393238450079381L;
		private final U curve;
		private final Double t0;

		public Part(final double t0, final U f) {
			this.t0 = new Double(t0);
			curve = f;
		}

		public int compareTo(final Number o2) {
			if (doubleValue() < o2.doubleValue())
				return -1;
			if (doubleValue() > o2.doubleValue())
				return 1;
			return 0;
		}

		@Override
		public double doubleValue() {
			return getKey();
		}

		@Override
		public float floatValue() {
			return (float) doubleValue();
		}

		public Double getKey() {
			return t0;
		}

		public U getValue() {
			return curve;
		}

		@Override
		public int intValue() {
			return (int) doubleValue();
		}

		@Override
		public long longValue() {
			return (long) doubleValue();
		}

		public U setValue(final U value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toString() {
			return new StringBuilder().append("[").append(getKey()).append(
					" : ").append(getValue()).append("]").toString();
		}
	}

	private static final Log log = JCLoggerFactory
			.getLogger(CurveCombined.class);

	private static final long serialVersionUID = 5955065096153576747L;

	/**
	 * Search only part of an array.
	 * 
	 * @see java.util.Arrays#binarySearch(double[], double)
	 * @param a
	 * @param min
	 * @param max
	 * @param key
	 * @return found index
	 */
	static int binarySearch(final double[] a, int min, int max, final double key) {
		// if(true) {
		// return Arrays.binarySearch(a, min, max, key);
		// } else
		for (;;) {
			if (key == a[min])
				return min;
			if (key == a[max])
				return max;
			final int m = (max + min) / 2;
			if (key == a[m])
				return m;
			if (min + 1 >= max) {
				if (a[min] < key && key < a[max])
					return -1 - max;
				return -1;
			}
			if (key < a[m]) {
				max = m;
				continue;
			} else if (key > a[m]) {
				min = m;
				continue;
			}
		}
	}

	/**
	 * Search only part of a list.
	 * 
	 * @param a
	 * @param fromIndex
	 * @param toIndex
	 * @param key
	 * @param comp
	 * 
	 * @return found index
	 */
	static <E> int binarySearch(final List<E> a, final int fromIndex,
			final int toIndex, final E key, final Comparator<? super E> comp) {
		if (fromIndex > toIndex)
			throw new IllegalArgumentException("fromIndex(" + fromIndex
					+ ") > toIndex(" + toIndex + ")");
		if (fromIndex < 0)
			throw new ArrayIndexOutOfBoundsException(fromIndex);
		if (toIndex > a.size())
			throw new ArrayIndexOutOfBoundsException(toIndex);

		int low = fromIndex;
		int high = toIndex - 1;
		while (low <= high) {
			final int mid = low + high >>> 1;
			final E midVal = a.get(mid);
			final int cmp = comp.compare(midVal, key);
			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // done
		}
		return -(low + 1); // no such key
	}

	/**
	 * Search only part of an array. Could be more general operating with
	 * {@link Comparable} and {@link Object}s.
	 * 
	 * @param a
	 * @param fromIndex
	 * @param toIndex
	 * @param key
	 * 
	 * @return found index
	 */
	static <V extends R1RNFunction> int binarySearch(
			final List<Entry<Double, V>> a, int fromIndex, int toIndex,
			final double key) {
		if (false) {
			if (fromIndex > toIndex)
				throw new IllegalArgumentException("fromIndex(" + fromIndex
						+ ") > toIndex(" + toIndex + ")");
			if (fromIndex < 0)
				throw new ArrayIndexOutOfBoundsException(fromIndex);
			if (toIndex > a.size())
				throw new ArrayIndexOutOfBoundsException(toIndex);

			int low = fromIndex;
			int high = toIndex - 1;
			while (low <= high) {
				final int mid = low + high >>> 1;
				final double midVal = a.get(mid).getKey().doubleValue();
				final int cmp = Double.compare(midVal, key);
				if (cmp < 0)
					low = mid + 1;
				else if (cmp > 0)
					high = mid - 1;
				else
					return mid; // done
			}
			return -(low + 1); // no such key
		} else {
			double fromKey = a.get(fromIndex).getKey().doubleValue();
			double toKey = a.get(toIndex).getKey().doubleValue();
			for (;;) {
				if (key == fromKey)
					return fromIndex;
				if (key == toKey)
					return toIndex;
				final int midIndex = (toIndex + fromIndex) / 2;
				final double midKey = a.get(midIndex).getKey().doubleValue();
				if (key == midKey)
					return midIndex;
				if (fromIndex + 1 >= toIndex) {
					if (fromKey < key && key < toKey)
						return -1 - toIndex;
					return -1;
				}
				if (key < midKey) {
					toIndex = midIndex;
					toKey = midKey;
					continue;
				} else if (key > midKey) {
					fromIndex = midIndex;
					fromKey = midKey;
					continue;
				}
			}
		}
	}

	Map<Integer, String> m;
	private final List<Entry<Double, T>> parts = new ArrayList<Entry<Double, T>>();

	public CurveCombined(final int dim) {
		super(dim);
	}

	public void add(final double t0, final T fkt, final boolean dropTail) {
		log.debug("");
		if (fkt.dim() != dim())
			throw new IllegalArgumentException();
		if (dropTail) {
			final int idx = findFunctionIndex(t0);
			for (int i = parts.size() - 1; i > idx; i--)
				parts.remove(i);
		}
		parts.add(new Part<T>(t0, fkt));
	}

	/**
	 * Get the n-th derivative of all dimensions.
	 * 
	 * @param t
	 * @param c
	 *            derivative
	 * @param ret
	 *            <code>null</code> creates a new instance
	 * 
	 * @return the value
	 * @see R1RNFunctionImpl#at(double, int, double[])
	 */
	@Override
	public double[] at(final double t, final int c, final double[] ret) {
		return parts.get(findFunctionIndex(t)).getValue().at(t, c, ret);
	}

	@Override
	public double at(final double t, final int c, final int dim) {
		return parts.get(findFunctionIndex(t)).getValue().at(t, c, dim);
	}

	public void clear() {
		parts.clear();
	}

	/**
	 * Drop all segments with t0 &gt;= t.
	 * 
	 * @param t
	 * @return <code>false</code> nothing dropped.
	 */
	public boolean dropTail(final double t) {
		if (Double.isNaN(t))
			return false;
		final int n = parts.size() - 1;
		if (n < 0)
			return false;
		int j = findFunctionIndex(t);
		if (parts.get(j).getKey() == t)
			j += 1;
		if (n < j)
			return false;
		for (int i = n; i >= j; i--)
			parts.remove(i);
		return true;
	}

	private int findFunctionIndex(final double t) {
		int highIdx = parts.size() - 1;
		if (highIdx < 0)
			return -1;
		double highKey = parts.get(highIdx).getKey();
		if (t >= highKey)
			return highIdx;

		int lowIdx = 0;
		double lowKey = parts.get(lowIdx).getKey();
		if (t < lowKey)
			return -1;

		while (lowIdx + 1 < highIdx) {
			final int midIdx = (highIdx + lowIdx) / 2;
			final double midKey = parts.get(midIdx).getKey();

			if (t >= midKey) {
				// Throw away left half
				lowIdx = midIdx;
				lowKey = midKey;
			} else {
				// Throw away right half
				highIdx = midIdx;
				highKey = midKey;
			}
		}
		return lowIdx;
	}

	public T first() {
		if (parts.size() <= 0)
			return null;
		return parts.get(0).getValue();
	}

	public Iterator<Entry<Double, T>> iterator() {
		return parts.iterator();
	}

	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append('[');
		for (final Entry<Double, T> elem : parts)
			b.append("x>=").append(elem.getKey()).append(" f(x)=").append(
					elem.getValue()).append(", ");
		if (b.length() > 2)
			b.setLength(b.length() - 2);
		b.append(']');
		return b.toString();
	}
}