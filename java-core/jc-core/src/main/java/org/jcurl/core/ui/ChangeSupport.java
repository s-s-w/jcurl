package org.jcurl.core.ui;

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
	protected EventListenerList listenerList = new EventListenerList();
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

}
