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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jcurl.core.helpers.DimVal;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:ModelBase.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public abstract class ModelBase implements Model {

    protected ModelProps data = null;

    public DimVal getProp(final CharSequence key) {
        return data.get(key);
    }

    public void init(final Map<CharSequence, DimVal> props) {
        if (this.data != null)
            throw new IllegalStateException();
        if (props instanceof ModelProps)
            this.data = (ModelProps) props;
        else
            this.data = new ModelProps(props);
    }

    public Iterator<Entry<CharSequence, DimVal>> iterator() {
        return data.entrySet().iterator();
    }
}