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

package org.jcurl.core.ui;

import java.io.Serializable;
import java.util.concurrent.Executor;

/**
 * A single, atomic unit of change usually concerning a data model.
 * <p>
 * Implements {@link Runnable} to simplify execution by a different
 * {@link Thread} or {@link Executor} if desired - typically a
 * {@link ChangeManager}.
 * </p>
 * <h3>Caution!</h3>
 * <p>
 * The {@link #setContext(Object)} exists <b>only</b> to be able to
 * (re-)connect deserialised mementos to an existing data model i.e. implant a
 * context (immediately after deserialisation). This is the only case when it's
 * legitime to call {@link #setContext(Object)} if and only if the existing
 * internal context is <code>null</code>. And that's also the reason why the
 * {@link #context} field is transient, while all other fields of derived
 * mememto classes <b>must be</b> immutable as well as non-transient.
 * </p>
 * <p>
 * Maybe a clone method with the new context as a parameter would do as well in
 * a secure, encapsulated manner at the cost of one object instanciation.
 * </p>
 * 
 * @see UndoableMemento
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class Memento<E> implements Runnable, Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private static final void pushStat(final Class<?> c, final long millis) {
		;// System.out.println(c.getName() + " " + millis);
	}

	// private static final Collection<Integer> stats = new
	// LinkedList<Integer>();

	private transient E context;

	protected Memento(final E context) {
		this.context = context;
	}

	public E apply() {
		return apply(context);
	}

	public abstract E apply(E dst);

	@Override
	protected abstract Object clone();

	public Memento<E> clone(final E context) {
		final Memento<E> r = (Memento<E>) this.clone();
		r.context = context;
		return r;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Memento other = (Memento) obj;
		return this.context == other.context;
	}

	public E getContext() {
		return context;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (context == null ? 0 : context.hashCode());
		return result;
	}

	public void run() {
		final long start = System.currentTimeMillis();
		try {
			apply();
		} finally {
			pushStat(this.getClass(), System.currentTimeMillis() - start);
		}
	}

	/**
	 * 
	 * @param context
	 * @throws IllegalStateException
	 *             the internal {@link #context} wasn't <code>null</code>
	 */
	public void setContext(final E context) throws IllegalStateException {
		if (this.context != null)
			throw new IllegalStateException("Context already set.");
		this.context = context;
	}
}
