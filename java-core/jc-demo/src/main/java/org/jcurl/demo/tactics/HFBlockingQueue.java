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

package org.jcurl.demo.tactics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class HFBlockingQueue<E> extends LinkedBlockingQueue<E> {

	private static final Log log = JCLoggerFactory
			.getLogger(HFBlockingQueue.class);
	private static final long serialVersionUID = 8529261423710640827L;

	private final Map<Class<?>, E> recent = new HashMap<Class<?>, E>();

	public HFBlockingQueue() {}

	@Override
	synchronized public boolean add(final E o) {
		recent.put(o.getClass(), o);
		return super.add(o);
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(final Object o) {
		return super.contains(o) && recent.get(o.getClass()) == o;
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int drainTo(final Collection<? super E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int drainTo(final Collection<? super E> c, final int maxElements) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E element() {
		for (;;) {
			final E o = super.element();
			if (o == null || recent.get(o.getClass()) == o)
				return o;
			log.debug("discarded");
		}
	}

	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	synchronized public boolean offer(final E o) {
		recent.put(o.getClass(), o);
		return super.offer(o);
	}

	@Override
	synchronized public boolean offer(final E o, final long timeout,
			final TimeUnit unit) throws InterruptedException {
		recent.put(o.getClass(), o);
		return super.offer(o, timeout, unit);
	}

	@Override
	public E peek() {
		throw new UnsupportedOperationException();
	}

	@Override
	public E poll() {
		for (;;) {
			final E o = super.poll();
			if (o == null || recent.get(o.getClass()) == o)
				return o;
			log.debug("discarded");
		}
	}

	@Override
	public E poll(final long timeout, final TimeUnit unit)
			throws InterruptedException {
		for (;;) {
			final E o = super.poll(timeout, unit);
			if (o == null || recent.get(o.getClass()) == o)
				return o;
			log.debug("discarded");
		}
	}

	@Override
	synchronized public void put(final E o) throws InterruptedException {
		recent.put(o.getClass(), o);
		super.put(o);
	}

	@Override
	public boolean remove(final Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E take() throws InterruptedException {
		for (;;) {
			final E o = super.take();
			if (o == null || recent.get(o.getClass()) == o)
				return o;
			log.debug("discarded");
		}
	}
}
