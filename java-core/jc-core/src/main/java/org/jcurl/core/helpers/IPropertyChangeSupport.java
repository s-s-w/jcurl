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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public interface IPropertyChangeSupport {

    /**
     * Add a non-specific {@link PropertyChangeListener}. Adds a listener that
     * will recieve events on all properties.
     * 
     * @param pcl
     *            The {@link PropertyChangeListener} add.
     */
    public abstract void addPropertyChangeListener(
            final PropertyChangeListener pcl);

    /**
     * Add a {@link PropertyChangeListener} for a specific property.
     * 
     * @param property
     *            The name of the relevant property.
     * @param pcl
     *            The listener to add.
     */
    public abstract void addPropertyChangeListener(final String property,
            final PropertyChangeListener pcl);

    public abstract void fireIndexedPropertyChange(final String property,
            final int index, final Object old, final Object neo);

    /**
     * Fire a property change event to all of the listeners.
     * <p>
     * This method is called by all the fire methods to perform the firing of
     * the events.
     * </p>
     * <p>
     * The firing will go to the listeners that are registered for the specific
     * property as well as general purpose listeners.
     * </p>
     * <p>
     * If the old and new values for the event are the same, by the
     * <tt>equals()</tt> method, the event will not be fired.
     * </p>
     * 
     * @param event
     *            The event to fire to the listeners.
     */
    public abstract void firePropertyChange(final PropertyChangeEvent event);

    /**
     * Notify listeners that an object type property has changed
     * 
     * @param property
     *            the name of the property which changed.
     * @param old
     *            The old value.
     * @param neo
     *            The new value.
     */
    public abstract void firePropertyChange(final String property,
            final Object old, final Object neo);

    /**
     * Returns an array of all the listeners that were added to the
     * {@link PropertyChangeSupport} object with addPropertyChangeListener().
     * The array is computed as late as possible to give as much cleanout time
     * as neded.
     * 
     * @return An array of all listeners.
     */
    public abstract PropertyChangeListener[] getPropertyChangeListeners();

    /**
     * Returns an array of all the listeners which have been associated with the
     * named property.
     * 
     * @param property
     *            The name of the relevant property.
     * @return An array of listeners listening to the specified property.
     */
    public abstract PropertyChangeListener[] getPropertyChangeListeners(
            final String property);

    /**
     * Check if there are any listeners interested in a specific property.
     * 
     * @param property
     *            The relvant property name.
     * @return true If there are listeners for the given property
     */
    public abstract boolean hasListeners(final String property);

    /**
     * Remove a PropertyChangeListener from the listener list. This removes a
     * PropertyChangeListener that was registered for all properties.
     * 
     * @param pcl
     *            The {@link PropertyChangeListener} to be removed
     */
    public abstract void removePropertyChangeListener(
            final PropertyChangeListener pcl);

    /**
     * Remove a {@link PropertyChangeListener} for a specific property.
     * 
     * @param property
     *            The name of the relevant property.
     * @param pcl
     *            The listener to remove.
     */
    public abstract void removePropertyChangeListener(final String property,
            final PropertyChangeListener pcl);

}