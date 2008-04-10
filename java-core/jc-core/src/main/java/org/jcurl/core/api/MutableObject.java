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
package org.jcurl.core.api;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;


/**
 * Base class for all mutable value Objects. Provides a generic toString and
 * means to notify others upon propery changes.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:MutableObject.java 682 2007-08-12 21:25:04Z mrohrmoser $
 * 
 */
public abstract class MutableObject extends TransferObject implements
        IPropertyChangeSupport {
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

    public void fireIndexedPropertyChange(final String property,
            final int index, final Object old, final Object neo) {
        propChange.fireIndexedPropertyChange(property, index, old, neo);
    }

    public void firePropertyChange(final PropertyChangeEvent event) {
        propChange.firePropertyChange(event);
    }

    public void firePropertyChange(final String property, final Object old,
            final Object neo) {
        propChange.firePropertyChange(property, old, neo);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return propChange.getPropertyChangeListeners();
    }

    public PropertyChangeListener[] getPropertyChangeListeners(
            final String property) {
        return propChange.getPropertyChangeListeners(property);
    }

    public boolean hasListeners(final String property) {
        return propChange.hasListeners(property);
    }

    /**
     * Removes a PropertyChangeListener to the listener list.
     */
    public void removePropertyChangeListener(
            final PropertyChangeListener listener) {
        propChange.removePropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener to the listener list for a specific
     * property.
     */
    public void removePropertyChangeListener(final String property,
            final PropertyChangeListener listener) {
        propChange.removePropertyChangeListener(property, listener);
    }

    /** TUNE cache! */
	protected PropertyDescriptor findPropertyDescriptor(final Class<?> bean, final String name) {
		try {
			for (final PropertyDescriptor pd : Introspector.getBeanInfo(bean)
					.getPropertyDescriptors())
				if (name.equals(pd.getName()))
					return pd;
		} catch (final IntrospectionException e) {
			throw new RuntimeException("Unhandled", e);
		}
		throw new IllegalArgumentException("Property \"" + name
				+ "\" not found in class \"" + bean + "\"");
	}

	protected void updateProperty(final String name, final Object old, final Object neo) {
		if (old == neo || (old != null && old.equals(neo)))
			return;
		try {
			final Object[] args = { neo };
			this.getClass().getField(name).set(this, args);
//			findPropertyDescriptor(this.getClass(), name).setgetWriteMethod()
//					.invoke(this, args);
			firePropertyChange(name, old, neo);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Unhandled", e);
		} catch (SecurityException e) {
			throw new RuntimeException("Unhandled", e);
		} catch (NoSuchFieldException e) {
			for(Field f : this.getClass().getFields())
				System.out.println(f.getName());
			throw new RuntimeException("Unhandled", e);
		}
	}
}