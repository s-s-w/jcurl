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
package org.jcurl.core.helpers;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Merge multiple iterators over arbitrary elements.
 * 
 * When using the "natural order" form {@link #MergedIterator(Iterable)} the
 * iterator elements must be {@link Comparable} with each other!
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:EnumBase.java 682 2007-08-12 21:25:04Z mrohrmoser $
 * @param <E>
 *            Iterator Element Type.
 */
public class MergedIterator<E> implements Iterator<E> {
	private final LinkedList<PeekIterator<E>> base = new LinkedList<PeekIterator<E>>();
	private final Comparator<PeekIterator<E>> comp;

	/**
	 * Convenience wrapper for {@link #MergedIterator(Iterable, Comparator)}
	 * with natural order - <code>E</code> must be {@link Comparable} in this
	 * case!
	 * 
	 * @param base
	 *            multiple iterators over comparable elements.
	 */
	public MergedIterator(final Iterable<Iterator<E>> base) {
		this(base, new Comparator<E>() {
			@SuppressWarnings("unchecked")
			public int compare(final E o1, final E o2) {
				return ((Comparable<E>) o1).compareTo(o2);
			}
		});
	}

	public MergedIterator(final Iterable<Iterator<E>> base,
			final Comparator<E> comp) {
		for (final Iterator<E> elem : base) {
			final PeekIterator<E> p = new PeekIterator<E>(elem);
			if (p.hasNext())
				this.base.add(p);
		}
		this.comp = new Comparator<PeekIterator<E>>() {
			public int compare(final PeekIterator<E> o1,
					final PeekIterator<E> o2) {
				return comp.compare(o1.peek(), o2.peek());
			}
		};
		Collections.sort(this.base, this.comp);
	}

	public boolean hasNext() {
		return !base.isEmpty() && base.getFirst().hasNext();
	}

	public E next() {
		if (!hasNext())
			return null;
		try {
			return base.getFirst().next();
		} finally {
			if (!base.getFirst().hasNext())
				base.removeFirst();
			Collections.sort(base, comp);
		}
	}

	public void remove() {
		throw new UnsupportedOperationException("Not supported.");
	}
}