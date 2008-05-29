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

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.WeakHashSet;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.TaskExecutor.SwingEDT;

/**
 * 
 * Relation to {@link SwingWorker}?
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class ChangeManager {

	private static final Log log = JCLoggerFactory
			.getLogger(ChangeManager.class);

	private static final ChangeManager trivial = new ChangeManager() {

		@Override
		public void addUndoableEditListener(UndoableEditListener l) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean canRedo() {
			return false;
		}

		@Override
		public boolean canUndo() {
			return false;
		}

		@Override
		public void discardAllEdits() {}

		@Override
		public Iterable<UndoableEditListener> getUndoableEditListeners() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void redo() {}

		@Override
		public void removeUndoableEditListener(UndoableEditListener l) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void temporary(Memento<?> m) {
			m.run();
		}

		@Override
		public void undo() {}

		@Override
		public <E> boolean undoable(Memento<E> pre, Memento<E> post) {
			post.run();
			return false;
		}

	};

	public static final ChangeManager getTrivial() {
		return trivial;
	}

	public static final ChangeManager getTrivial(final ChangeManager changer) {
		return changer == null ? trivial : changer;
	}

	private final Executor executor;
	private final WeakHashSet<UndoableEditListener> listeners = new WeakHashSet<UndoableEditListener>();
	private final UndoManager undoer = new UndoManager();

	public ChangeManager() {
		// this(Executors.newSingleThreadExecutor());
		// this(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
		// new HFBlockingQueue<Runnable>()));
		this(new SwingEDT());
	}

	public ChangeManager(final Executor executor) {
		this.executor = executor;
	}

	public boolean addEdit(final CompoundEdit anEdit) {
		return addEdit((UndoableEdit) anEdit);
	}

	/**
	 * 
	 * @param <E>
	 * @param anEdit
	 * @return {@link UndoManager#addEdit(UndoableEdit)}
	 */
	private boolean addEdit(final UndoableEdit anEdit) {
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

	/** Delegate to {@link UndoManager#discardAllEdits()}. */
	public void discardAllEdits() {
		undoer.discardAllEdits();
		for (final UndoableEditListener elem : listeners)
			elem.undoableEditHappened(new UndoableEditEvent(undoer, null));
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

	public void temporary(final Memento<?> m) {		
		log.debug(m);
		executor.execute(m);
	}

	public void undo() {
		undoer.undo();
		for (final UndoableEditListener elem : listeners)
			elem.undoableEditHappened(new UndoableEditEvent(undoer, null));
	}

	/**
	 * Create an {@link UndoableMemento} and push to the {@link UndoManager} and
	 * {@link Executor}.
	 * <p>
	 * Does nothing if either <code>pre</code> or <code>post</code> is
	 * <code>null</code> or the two are equal.
	 * </p>
	 * 
	 * @param <E>
	 * @param pre
	 *            "undo" state.
	 * @param post
	 *            "redo" state.
	 * @return {@link UndoManager#addEdit(UndoableEdit)}
	 */
	public <E> boolean undoable(final Memento<E> pre, final Memento<E> post) {
		if (pre == null || post == null || pre.equals(post))
			return false;
		if (log.isDebugEnabled())
			log.debug(pre + " -> " + post);
		final boolean ret = addEdit(new UndoableMemento<E>(pre, post));
		executor.execute(post);
		return ret;
	}
}
