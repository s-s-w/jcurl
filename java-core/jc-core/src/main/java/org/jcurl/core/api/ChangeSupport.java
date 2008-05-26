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
package org.jcurl.core.api;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Inspired by {@link BoundedRangeModel}.
 */
public class ChangeSupport implements IChangeSupport {

	/**
	 * Only one <code>ChangeEvent</code> is needed per model instance since
	 * the event's only (read-only) state is the source property.
	 */
	protected transient ChangeEvent changeEvent = null;

	/** The listeners waiting for model changes. */
	protected transient EventListenerList listenerList = new EventListenerList();
	private final IChangeSupport source;

	protected ChangeSupport() {
		source = this;
	}

	public ChangeSupport(final IChangeSupport source) {
		this.source = source;
	}

	/**
	 * Adds a <code>ChangeListener</code>. The change listeners are run each
	 * time any one of the Bounded Range model properties changes.
	 * 
	 * @param l
	 *            the ChangeListener to add
	 * @see #removeChangeListener
	 * @see BoundedRangeModel#addChangeListener
	 */
	public void addChangeListener(final ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	/**
	 * Runs each <code>ChangeListener</code>'s <code>stateChanged</code>
	 * method.
	 * 
	 * @see EventListenerList
	 */
	public void fireStateChanged() {
		final Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null)
					changeEvent = new ChangeEvent(source);
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
	}

	/**
	 * Returns an array of all the change listeners registered on this
	 * <code>DefaultBoundedRangeModel</code>.
	 * 
	 * @return all of this model's <code>ChangeListener</code>s or an empty
	 *         array if no change listeners are currently registered
	 * 
	 * @see #addChangeListener
	 * @see #removeChangeListener
	 * 
	 * @since 1.4
	 */
	public ChangeListener[] getChangeListeners() {
		return listenerList.getListeners(ChangeListener.class);
	}

	public boolean getValueIsAdjusting() {
		return source.getValueIsAdjusting();
	}

	/**
	 * Removes a <code>ChangeListener</code>.
	 * 
	 * @param l
	 *            the <code>ChangeListener</code> to remove
	 * @see #addChangeListener
	 * @see BoundedRangeModel#removeChangeListener
	 */
	public void removeChangeListener(final ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	public void setValueIsAdjusting(final boolean valueIsAdjusting) {
		source.setValueIsAdjusting(valueIsAdjusting);
	}

}
