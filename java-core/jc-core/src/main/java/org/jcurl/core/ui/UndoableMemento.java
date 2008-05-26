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

package org.jcurl.core.ui;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 * Combine two {@link Memento}s with identical context to one
 * {@link UndoableEdit}.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class UndoableMemento<E> extends AbstractUndoableEdit {

	private static final long serialVersionUID = -5182398471355882831L;
	private final Memento<E> post;
	private final Memento<E> pre;

	public UndoableMemento(final Memento<E> pre, final Memento<E> post) {
		if (pre.context != post.context)
			throw new IllegalArgumentException();
		this.pre = pre;
		this.post = post;
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		post.run();
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		pre.run();
	}
}
