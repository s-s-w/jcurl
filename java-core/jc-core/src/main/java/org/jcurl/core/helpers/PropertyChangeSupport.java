/*
 * jcurl curling simulation framework http://www.jcurl.org
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

import java.beans.BeanInfo;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Implements a beans like property change support utility that stores the
 * references to the listeners in a {@link WeakHashSet} and thus are
 * memory-safe.
 * <p>
 * By storing the references to the listeners in {@link WeakReference}s instead
 * of strong references, we can be sure that if the listener is no longer used
 * by other classes, it will be garbage collected with this class. Since the
 * producer of messages should not have the responsability of maintaining the
 * garbage collection status of its listeners, this is needed.
 * </p>
 * <p>
 * Note that this class implements the identical interface to
 * {@link java.beans.PropertyChangeSupport}. This means that it is completely
 * plug in compatible, enabling users to switch out current support in favor of
 * the safe version.
 * </p>
 * <p>
 * This object is not serializable because it uses a {@link WeakHashSet} to hold
 * the listeners. Refer to that class for reasons for this decision. Users must
 * declare this attribute <tt>transient</tt>.
 * </p>
 * <p>
 * This class is thread safe.
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:PropertyChangeSupport.java 682 2007-08-12 21:25:04Z mrohrmoser $
 * @see java.lang.ref.WeakReference
 * @see java.beans.PropertyChangeSupport
 */
public class PropertyChangeSupport implements IPropertyChangeSupport {

    /**
     * Holds the value used as the key for general listeners in the listener
     * map. This value is chosen because it would be illegal to name a property
     * the same as this value which guarantees that there will be no name
     * clashes.
     */
    private static final String ALL_PROPERTIES = "**GENERAL**";

    private static final Log log = JCLoggerFactory
            .getLogger(PropertyChangeSupport.class);

    /**
     * Holds the Listener map. The map is held as an index of
     * {@link WeakHashSet} objects. Each property will appear as a key once in
     * the set and when a property change event is fired on a property, the
     * event will be fired to the union of the specific listeners set and the
     * general listeners set.
     * 
     * The general listeners are also held in the set with the key set by
     * {@link #ALL_PROPERTIES}
     */

    private final Map<String, Set<PropertyChangeListener>> listenerMap = new HashMap<String, Set<PropertyChangeListener>>();

    /** Stores the producer of the events. */
    private final Object producer;

    /**
     * Creates a new instance of SafePropertyChangeSupport.
     * 
     * @param producer
     *            This is the object that is producing the property change
     *            events.
     * @throws RuntimeException
     *             If there is an introspection problem.
     */
    public PropertyChangeSupport(final Object producer) {
        try {
            final BeanInfo info = Introspector.getBeanInfo(producer.getClass());
            for (final PropertyDescriptor element : info
                    .getPropertyDescriptors())
                listenerMap.put(element.getName(),
                        new WeakHashSet<PropertyChangeListener>());
            listenerMap.put(ALL_PROPERTIES,
                    new WeakHashSet<PropertyChangeListener>());
            this.producer = producer;
        } catch (final IntrospectionException ex) {
            throw new RuntimeException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jcurl.core.helpers.IPropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(final PropertyChangeListener pcl) {
        synchronized (listenerMap) {
            listenerMap.get(ALL_PROPERTIES).add(pcl);
        }
    }

    public void addPropertyChangeListener(final String property,
            final PropertyChangeListener pcl) {
        validateNamedProperty(property);
        synchronized (listenerMap) {
            listenerMap.get(property).add(pcl);
        }
    }

    public void fireIndexedPropertyChange(final String property,
            final int index, final boolean old, final boolean neo) {
        firePropertyChange(new IndexedPropertyChangeEvent(producer, property,
                old, neo, index));
    }

    public void fireIndexedPropertyChange(final String property,
            final int index, final int old, final int neo) {
        firePropertyChange(new IndexedPropertyChangeEvent(producer, property,
                old, neo, index));
    }

    public void fireIndexedPropertyChange(final String property,
            final int index, final Object old, final Object neo) {
        firePropertyChange(new IndexedPropertyChangeEvent(producer, property,
                old, neo, index));
    }

    public void firePropertyChange(final PropertyChangeEvent event) {
        validateNamedProperty(event.getPropertyName());
        {
            final Object a = event.getOldValue();
            final Object b = event.getNewValue();
            if (a != null && a.equals(b) || a == null && b == null)
                return;
        }
        // if (event.getOldValue() == null) {
        // if (event.getOldValue() == null) {
        // return;
        // }
        // } else if (event.getOldValue().equals(event.getNewValue())) {
        // return;
        // }
        // validated that an event must be thrown; now throw it.
        synchronized (listenerMap) {
            // First gets the list of listeners and stores them in strong
            // references by copying them into a new set.
            final Set<PropertyChangeListener> targets = new HashSet<PropertyChangeListener>(
                    listenerMap.get(ALL_PROPERTIES));
            targets.addAll(listenerMap.get(event.getPropertyName()));
            for (final PropertyChangeListener element : targets)
                element.propertyChange(event);
        }
    }

    public void firePropertyChange(final String property, final boolean old,
            final boolean neo) {
        final PropertyChangeEvent event = new PropertyChangeEvent(producer,
                property, Boolean.valueOf(old), Boolean.valueOf(neo));
        this.firePropertyChange(event);
    }

    public void firePropertyChange(final String property, final double old,
            final double neo) {
        final PropertyChangeEvent event = new PropertyChangeEvent(producer,
                property, new Double(old), new Double(neo));
        this.firePropertyChange(event);
    }

    public void firePropertyChange(final String property, final int old,
            final int neo) {
        final PropertyChangeEvent event = new PropertyChangeEvent(producer,
                property, new Integer(old), new Integer(neo));
        this.firePropertyChange(event);
    }

    public void firePropertyChange(final String property, final Object old,
            final Object neo) {
        final PropertyChangeEvent event = new PropertyChangeEvent(producer,
                property, old, neo);
        this.firePropertyChange(event);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        final Set<PropertyChangeListener> all = new WeakHashSet<PropertyChangeListener>();
        synchronized (listenerMap) {
            for (final Set<PropertyChangeListener> element : listenerMap
                    .values())
                all.addAll(element);
        }
        final PropertyChangeListener[] pcls = new PropertyChangeListener[all
                .size()];
        return all.toArray(pcls);
    }

    public PropertyChangeListener[] getPropertyChangeListeners(
            final String property) {
        validateNamedProperty(property);
        final Set<PropertyChangeListener> namedListeners;
        synchronized (listenerMap) {
            namedListeners = new HashSet<PropertyChangeListener>(listenerMap
                    .get(property));
        }
        final PropertyChangeListener[] pcls = new PropertyChangeListener[namedListeners
                .size()];
        return namedListeners.toArray(pcls);
    }

    public boolean hasListeners(final String property) {
        validateNamedProperty(property);
        synchronized (listenerMap) {
            return !listenerMap.get(property).isEmpty();
        }
    }

    public void removePropertyChangeListener(final PropertyChangeListener pcl) {
        synchronized (listenerMap) {
            listenerMap.get(ALL_PROPERTIES).remove(pcl);
        }
    }

    public void removePropertyChangeListener(final String property,
            final PropertyChangeListener pcl) {
        validateNamedProperty(property);
        synchronized (listenerMap) {
            listenerMap.get(property).remove(pcl);
        }
    }

    /**
     * Validate that a property name is a member of the producer object.
     * <p>
     * This is a helper method so that all methods that must validate this need
     * not replicate the code.
     * </p>
     * 
     * @param property
     *            The name of the property to validate.
     * @throws IllegalArgumentException
     */
    private void validateNamedProperty(final String property) {
        if (!listenerMap.containsKey(property)) {
            if (log.isDebugEnabled())
                log.debug("Key Set: " + listenerMap.keySet());
            throw new IllegalArgumentException("The property '" + property
                    + "' is not a valid property of " + producer.getClass()
                    + ". Valid values = " + listenerMap.keySet().toString());
        }
    }
}