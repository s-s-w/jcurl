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

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.MyUndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
abstract class UndoManagerBase extends MyUndoManager {
	private static final long serialVersionUID = 1L;
	
	@Override
	public abstract boolean addEdit(final UndoableEdit anEdit);

	@Override
	public abstract boolean canRedo();

	@Override
	public abstract boolean canUndo();

	@Override
	public abstract boolean canUndoOrRedo();

	@Override
	public abstract void discardAllEdits();

	@Override
	protected abstract UndoableEdit editToBeRedone();

	@Override
	protected abstract UndoableEdit editToBeUndone();

	@Override
	public abstract void end();

	@Override
	public abstract int getLimit();

	@Override
	public abstract String getRedoPresentationName();

	@Override
	public abstract String getUndoOrRedoPresentationName();

	@Override
	public abstract String getUndoPresentationName();

	@Override
	public abstract void redo() throws CannotRedoException;

	@Override
	protected abstract void redoTo(final UndoableEdit edit)
			throws CannotRedoException;

	@Override
	public abstract void setLimit(final int l);

	@Override
	protected abstract void trimEdits(final int from, final int to);

	@Override
	protected abstract void trimForLimit();

	@Override
	public abstract void undo() throws CannotUndoException;

	@Override
	public abstract void undoableEditHappened(final UndoableEditEvent e);

	@Override
	public abstract void undoOrRedo() throws CannotRedoException,
			CannotUndoException;

	@Override
	protected abstract void undoTo(final UndoableEdit edit)
			throws CannotUndoException;
}
