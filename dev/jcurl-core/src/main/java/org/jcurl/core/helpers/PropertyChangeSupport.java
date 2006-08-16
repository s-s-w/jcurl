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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;

/**
 * Implements a beans like property change support utility that stores the
 * references to the listeners in a weak set and thus are memory-safe.
 * <p>
 * 
 * By storing the references to the listeners in weak references instead of
 * strong references, we can be sure that if the listener is no longer used by
 * other classes, it will be garbage collected with this class. Since the
 * producer of messages should not have the responsability of maintaining the
 * garbage collection status of its listeners, this is needed.
 * </p>
 * 
 * <p>
 * Note that this class implements the identical interface to
 * <tt>java.lang.PropertyChangeSupport</tt>. This means that it is completely
 * plug in compatible, enabling users to switch out current support in favor of
 * the safe version.
 * </p>
 * <p>
 * This object is not serializable because it uses a <tt>WeakHashSet</tt> to
 * hold the listeners. Refer to that class for reasons for this decision. Users
 * must declare this attribute <tt>transient</tt>.
 * </p>
 * 
 * <p>
 * This class is thread safe.
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: PropertyChangeSupport.java 295 2006-05-14 11:50:09Z mrohrmoser $
 * @see java.lang.ref.WeakReference
 * @see java.beans.PropertyChangeSupport
 */

public class PropertyChangeSupport {

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
     * Holds the Listener map. The map is held as an index of WeakHashSet
     * objects. Each property will appear as a key once in the set and when a
     * property change event is fired on a property, the event will be fired to
     * the union of the specific listeners set and the general listeners set.
     * 
     * The general listeners are also held in the set with the key set by
     * <tt>ALL_PROPERTIES</tt>
     */

    private final Map listenerMap = new HashMap();

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
            final PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (int idx = 0; idx < props.length; idx++) {
                listenerMap.put(props[idx].getName(), new WeakHashSet());
            }
            listenerMap.put(ALL_PROPERTIES, new WeakHashSet());
            this.producer = producer;
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Add a non-specific PropertyChangeListener. Adds a listener that will
     * recieve events on all properties.
     * 
     * @param pcl
     *            The PropertyChangeListener add.
     */
    public void addPropertyChangeListener(final PropertyChangeListener pcl) {
        synchronized (listenerMap) {
            ((WeakHashSet) listenerMap.get(ALL_PROPERTIES)).add(pcl);
        }
    }

    /**
     * Add a PropertyChangeListener for a specific property.
     * 
     * @param property
     *            The name of the relevant property.
     * @param pcl
     *            The listener to add.
     */
    public void addPropertyChangeListener(final String property,
            final PropertyChangeListener pcl) {
        validateNamedProperty(property);
        synchronized (listenerMap) {
            ((WeakHashSet) listenerMap.get(property)).add(pcl);
        }
    }

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
    public void firePropertyChange(final PropertyChangeEvent event) {
        validateNamedProperty(event.getPropertyName());
        {
            final Object a = event.getOldValue();
            final Object b = event.getNewValue();
            if ((a != null && a.equals(b)) || (a == null && b == null)) {
                return;
            }
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
            final Set targets = new HashSet((Set) listenerMap
                    .get(ALL_PROPERTIES));
            targets.addAll((Set) listenerMap.get(event.getPropertyName()));
            // Fire events at the listeners.
            for (Iterator iter = targets.iterator(); iter.hasNext();) {
                ((PropertyChangeListener) iter.next()).propertyChange(event);
            }
            // Release the strong set so that the weak refs take over again.
        }
    }

    /**
     * Shortcut for firing an event on boolean properties.
     * 
     * @param property
     *            the name of the property which changed.
     * @param old
     *            The old value.
     * @param neo
     *            The new value.
     */
    public void firePropertyChange(final String property, final boolean old,
            final boolean neo) {
        PropertyChangeEvent event = new PropertyChangeEvent(producer, property,
                new Boolean(old), new Boolean(neo));
        firePropertyChange(event);
    }

    /**
     * Shortcut for firing an event on double properties.
     * 
     * @param property
     *            the name of the property which changed.
     * @param old
     *            The old value.
     * @param neo
     *            The new value.
     */
    public void firePropertyChange(final String property, final double old,
            final double neo) {
        PropertyChangeEvent event = new PropertyChangeEvent(producer, property,
                new Double(old), new Double(neo));
        firePropertyChange(event);
    }

    /**
     * Shortcut for firing an event on integer properties.
     * 
     * @param property
     *            the name of the property which changed.
     * @param old
     *            The old value.
     * @param neo
     *            The new value.
     */
    public void firePropertyChange(final String property, final int old,
            final int neo) {
        PropertyChangeEvent event = new PropertyChangeEvent(producer, property,
                new Integer(old), new Integer(neo));
        firePropertyChange(event);
    }

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
    public void firePropertyChange(final String property, final Object old,
            final Object neo) {
        final PropertyChangeEvent event = new PropertyChangeEvent(producer,
                property, old, neo);
        firePropertyChange(event);
    }

    /**
     * Returns an array of all the listeners that were added to the
     * PropertyChangeSupport object with addPropertyChangeListener(). The array
     * is computed as late as possible to give as much cleanout time as neded.
     * 
     * @return An array of all listeners.
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        final Set all = new WeakHashSet();
        PropertyChangeListener[] pcls = new PropertyChangeListener[0];
        synchronized (listenerMap) {
            for (Iterator iter = listenerMap.values().iterator(); iter
                    .hasNext();) {
                all.addAll((Set) iter.next());
            }
        }
        return (PropertyChangeListener[]) all.toArray(pcls);
    }

    /**
     * Returns an array of all the listeners which have been associated with the
     * named property.
     * 
     * @param property
     *            The name of the relevant property.
     * @return An array of listeners listening to the specified property.
     */
    public PropertyChangeListener[] getPropertyChangeListeners(
            final String property) {
        validateNamedProperty(property);
        PropertyChangeListener[] pcls = new PropertyChangeListener[0];
        Set namedListeners = null;
        synchronized (listenerMap) {
            namedListeners = new HashSet((Set) listenerMap.get(property));
        }
        return (PropertyChangeListener[]) namedListeners.toArray(pcls);
    }

    /**
     * Check if there are any listeners interested in a specific property.
     * 
     * @param property
     *            The relvant property name.
     * @return true If there are listeners for the given property
     */
    public boolean hasListeners(final String property) {
        validateNamedProperty(property);
        synchronized (listenerMap) {
            return (!((Set) listenerMap.get(property)).isEmpty());
        }
    }

    /**
     * Remove a PropertyChangeListener from the listener list. This removes a
     * PropertyChangeListener that was registered for all properties.
     * 
     * @param pcl
     *            The PropertyChangeListener to be removed
     */
    public void removePropertyChangeListener(final PropertyChangeListener pcl) {
        synchronized (listenerMap) {
            ((WeakHashSet) listenerMap.get(ALL_PROPERTIES)).remove(pcl);
        }
    }

    /**
     * Remove a PropertyChangeListener for a specific property.
     * 
     * @param property
     *            The name of the relevant property.
     * @param pcl
     *            The listener to remove.
     */
    public void removePropertyChangeListener(final String property,
            final PropertyChangeListener pcl) {
        validateNamedProperty(property);
        synchronized (listenerMap) {
            ((WeakHashSet) listenerMap.get(property)).remove(pcl);
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
            if (log.isDebugEnabled()) {
                log.debug("Key Set: " + listenerMap.keySet());
            }
            throw new IllegalArgumentException("The property '" + property
                    + "' is not a valid property of " + producer.getClass()
                    + ". Valid values = " + listenerMap.keySet().toString());
        }
    }
}