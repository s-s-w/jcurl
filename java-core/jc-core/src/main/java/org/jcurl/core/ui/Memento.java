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

package org.jcurl.core.ui;

import java.util.concurrent.Executor;

/**
 * A single, atomic unit of change usually concerning a data model.
 * <p>
 * Implements {@link Runnable} to simplify execution by a different
 * {@link Thread} or {@link Executor} if desired.
 * </p>
 * 
 * @see UndoableMemento
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class Memento<E> implements Runnable {

	final E context;

	protected Memento(final E context) {
		this.context = context;
	}

	public E apply() {
		return apply(context);
	}

	public abstract E apply(E dst);

	public void run() {
		apply();
	}
}
