/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
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

import java.beans.PropertyChangeListener;

/**
 * Base class for all mutable value Objects. Provides a generic toString and
 * means to notify others upon propery changes.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 * 
 */
public abstract class MutableObject extends TransferObject {
    /** Utility field used by bound properties. */
    protected final transient PropertyChangeSupport propChange = new PropertyChangeSupport(
            this);

    /**
     * Creates a new instance of MutableObject
     */
    protected MutableObject() {
    }

    /**
     * Adds a PropertyChangeListener to the listener list.
     * 
     * @param listener
     *            The listener to add.
     */
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propChange.addPropertyChangeListener(listener);
    }

    /**
     * Adds a PropertyChangeListener to the listener list for a specific
     * property.
     * 
     * @param property
     *            The property to listen to.
     * @param listener
     *            The listener to add.
     */
    public void addPropertyChangeListener(final String property,
            final PropertyChangeListener listener) {
        propChange.addPropertyChangeListener(property, listener);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public abstract boolean equals(Object obj);

    /**
     * @see java.lang.Object#hashCode()
     */
    // public abstract int hashCode();
    /**
     * Removes a PropertyChangeListener to the listener list.
     * 
     * @param listener
     *            The listener to add.
     */
    public void removePropertyChangeListener(
            final PropertyChangeListener listener) {
        propChange.removePropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener to the listener list for a specific
     * property.
     * 
     * @param property
     *            The property to listen to.
     * @param listener
     *            The listener to add.
     */
    public void removePropertyChangeListener(final String property,
            final PropertyChangeListener listener) {
        propChange.removePropertyChangeListener(property, listener);
    }

    protected void fire(final String property) {

    }
}