/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
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

		public R1RNFunction setValue(final R1RNFunction value) {
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

	Map<Integer, String> m;

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
	 * @see java.util.Arrays#binarySearch(Object[], int, int, Object,
	 *      java.util.Comparator)
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
	 * @see java.util.Arrays#binarySearch(Object[], int, int, Object,
	 *      java.util.Comparator)
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
		} else
			for (;;) {
				if (key == a.get(fromIndex).getKey().doubleValue())
					return fromIndex;
				if (key == a.get(toIndex).getKey().doubleValue())
					return toIndex;
				final int m = (toIndex + fromIndex) / 2;
				if (key == a.get(m).getKey().doubleValue())
					return m;
				if (fromIndex + 1 >= toIndex) {
					if (a.get(fromIndex).getKey().doubleValue() < key
							&& key < a.get(toIndex).getKey().doubleValue())
						return -1 - toIndex;
					return -1;
				}
				if (key < a.get(m).getKey().doubleValue()) {
					toIndex = m;
					continue;
				} else if (key > a.get(m).getKey().doubleValue()) {
					fromIndex = m;
					continue;
				}
			}
	}

	private final List<Entry<Double, T>> parts = new ArrayList<Entry<Double, T>>();

	public CurveCombined(final int dim) {
		super(dim);
	}

	public void add(final double t0, final T fkt, final boolean dropTail) {
		log.debug("");
		if (fkt.dim() != dim())
			throw new IllegalArgumentException();
		if (dropTail) {
			final int idx = findFktIdx_BS(t0);
			for (int i = parts.size() - 1; i > idx; i--)
				parts.remove(i);
		}
		parts.add(new Part<T>(t0, fkt));
	}

	/**
	 * Get the n-th derivative of all dimensions.
	 * 
	 * @param c
	 *            derivative
	 * @param t
	 * @param ret
	 *            <code>null</code> creates a new instance
	 * @return the value
	 * @see R1RNFunctionImpl#at(int, double, double[])
	 */
	@Override
	public double[] at(final int c, final double t, final double[] ret) {
		return parts.get(findFktIdx_BS(t)).getValue().at(c, t, ret);
	}

	@Override
	public double at(final int dim, final int c, final double t) {
		return parts.get(findFktIdx_BS(t)).getValue().at(dim, c, t);
	}

	public void clear() {
		parts.clear();
	}

	public T first() {
		return parts.get(0).getValue();
	}

	/**
	 * Binary search. Could be more general operating with {@link Comparable}
	 * and {@link Object}s.
	 * 
	 * @param t
	 * @return the curve index
	 */
	private int findFktIdx_BS(final double t) {
		if (parts.size() == 0)
			return -1;
		if (t < parts.get(0).getKey().doubleValue())
			throw new IllegalArgumentException("t < tmin");
		// find the correct index
		final int idx = binarySearch(parts, 0, parts.size() - 1, t);
		if (idx >= 0)
			return idx;
		if (idx == -1)
			return parts.size() - 1;
		return -2 - idx;
	}

	public Iterator<Entry<Double, T>> iterator() {
		return parts.iterator();
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append('[');
		for (Entry<Double, T> elem : parts)
			b.append("x>=").append(elem.getKey()).append(" f(x)=").append(
					elem.getValue()).append(", ");
		if (b.length() > 2)
			b.setLength(b.length() - 2);
		b.append(']');
		return b.toString();
	}
}