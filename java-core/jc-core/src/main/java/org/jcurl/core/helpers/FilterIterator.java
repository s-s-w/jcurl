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

import java.util.Iterator;

/**
 * Abstract Decorator: Filter iterators with 1-step lookahead.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:EnumBase.java 682 2007-08-12 21:25:04Z mrohrmoser $
 * @param <E>
 *            Iterator Element Type.
 */
public abstract class FilterIterator<E> implements Iterator<E> {

    private final Iterator<E> base;

    private E next = null;

    protected FilterIterator(final Iterator<E> base) {
        this.base = base;
        doLookAhead();
    }

    private void doLookAhead() {
        while (base.hasNext()) {
            next = base.next();
            if (matches(next))
                return;
        }
        next = null;
    }

    public boolean hasNext() {
        return next != null || base.hasNext();
    }

    /**
     * Overload.
     * 
     * @param item
     * @return true: item passes the filter, false: item will be filtered out.
     */
    protected abstract boolean matches(final E item);

    public E next() {
        try {
            return next;
        } finally {
            doLookAhead();
        }
    }

    protected E peek() {
        return next;
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }
}