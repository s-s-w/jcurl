/*
 * jcurl java curling software framework http://www.jcurl.org
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
package org.jcurl.core.ui;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import org.jcurl.core.api.WeakHashSet;

/**
 * Abstract base.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class UndoRedoDocumentBase implements UndoRedoDocument {

	private static final WeakHashSet<UndoableEditListener> listeners = new WeakHashSet<UndoableEditListener>();

	private final UndoManager undoer = new UndoManager();

	public boolean addEdit(final UndoableEdit anEdit) {
		final boolean ret = undoer.addEdit(anEdit);
		for (final UndoableEditListener elem : listeners)
			elem.undoableEditHappened(new UndoableEditEvent(undoer, anEdit));
		return ret;
	}

	public void addUndoableEditListener(final UndoableEditListener l) {
		listeners.add(l);
	}

	public boolean canRedo() {
		return undoer.canRedo();
	}

	public boolean canUndo() {
		return undoer.canUndo();
	}

	public Iterable<UndoableEditListener> getUndoableEditListeners() {
		return listeners;
	}

	public void redo() {
		undoer.redo();
		for (final UndoableEditListener elem : listeners)
			elem.undoableEditHappened(new UndoableEditEvent(undoer, null));
	}

	public void removeUndoableEditListener(final UndoableEditListener l) {
		listeners.remove(l);
	}

	public void undo() {
		undoer.undo();
		for (final UndoableEditListener elem : listeners)
			elem.undoableEditHappened(new UndoableEditEvent(undoer, null));
	}
}
