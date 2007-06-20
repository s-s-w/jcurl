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
package org.jcurl.core.base;

import java.io.ObjectStreamException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jcurl.core.helpers.Measure;

/**
 * Help with post-constructor one-time initialisation.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:ModelBase.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public abstract class PropModelImpl implements PropModel {

    protected Map<CharSequence, Measure> params = null;

    public Measure getProp(final CharSequence key) {
        return params.get(key);
    }

    protected void internalInit(final Map<CharSequence, Measure> props) {
        if (params != null)
            throw new IllegalStateException();
        params = PropModelHelper.create(props);
    }

    public Iterator<Entry<CharSequence, Measure>> iterator() {
        return params.entrySet().iterator();
    }

    protected Object readResolve() throws ObjectStreamException {
        final Map<CharSequence, Measure> params = this.params;
        this.params = null;
        init(params);
        return this;
    }
}