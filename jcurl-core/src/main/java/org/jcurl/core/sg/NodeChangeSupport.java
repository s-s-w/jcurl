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
package org.jcurl.core.sg;

import java.lang.ref.WeakReference;
import java.util.Set;

import org.jcurl.core.helpers.WeakHashSet;
import org.jcurl.core.sg.SGNode.NodeChangeEvent;
import org.jcurl.core.sg.SGNode.NodeChangeListener;

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
 * This object is not serializable because it uses a {@link WeakHashSet} to hold
 * the listeners. Refer to that class for reasons for this decision. Users must
 * declare this attribute <tt>transient</tt>.
 * </p>
 * <p>
 * This class is thread safe.
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: PropertyChangeSupport.java 548 2007-04-16 20:43:08Z mrohrmoser $
 * @see java.lang.ref.WeakReference
 */
public class NodeChangeSupport {

    /**
     * Holds the Listeners. This is a {@link WeakHashSet}.
     */
    private final Set<NodeChangeListener> listenerSet = new WeakHashSet<NodeChangeListener>();

    private final Object source;

    public NodeChangeSupport(final Object source) {
        this.source = source;
    }

    /**
     * Add a {@link NodeChangeListener}.
     * 
     * @param pcl
     *            The {@link NodeChangeListener} add.
     */
    public boolean addNodeChangeListener(final NodeChangeListener pcl) {
        synchronized (listenerSet) {
            return listenerSet.add(pcl);
        }
    }

    /**
     * Fire a node change event to all of the listeners.
     * 
     * @param event
     *            The event to fire to the listeners.
     */
    public void fireNodeChange(final NodeChangeEvent event) {
        synchronized (listenerSet) {
            for (final NodeChangeListener element : listenerSet)
                element.nodeChange(event);
        }
    }

    /**
     * Returns an array of all the listeners that were added to the
     * {@link NodeChangeSupport} object with addNodeChangeListener().
     * 
     * @return An array of all listeners.
     */
    public NodeChangeListener[] getNodeChangeListeners() {
        synchronized (listenerSet) {
            return listenerSet.toArray(new NodeChangeListener[listenerSet
                    .size()]);
        }
    }

    /**
     * Remove a NodeChangeListener from the listener list.
     * 
     * @param pcl
     *            The {@link NodeChangeListener} to be removed
     */
    public boolean removeNodeChangeListener(final NodeChangeListener pcl) {
        synchronized (listenerSet) {
            return listenerSet.remove(pcl);
        }
    }
}