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
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Base class for all value Objects. Provides a generic toString.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 * 
 */
public abstract class TransferObject implements Serializable {

    /**
     * Generic toString method.
     * 
     * @return all properties' values
     */
    public String toString() { // inspired by Hardcore Java (O'reilly, page
        // 228)
        try {
            final BeanInfo info = Introspector.getBeanInfo(getClass(),
                    Object.class);
            final PropertyDescriptor[] props = info.getPropertyDescriptors();
            final StringBuffer buf = new StringBuffer(500);
            Object value = null;
            buf.append(getClass().getName());
            buf.append("@");
            buf.append(hashCode());
            buf.append("={");
            for (int idx = 0; idx < props.length; idx++) {
                if (idx != 0)
                    buf.append(", ");
                buf.append(props[idx].getName());
                buf.append("=");
                if (props[idx].getReadMethod() != null) {
                    value = props[idx].getReadMethod().invoke(this, null);
                    if (value instanceof TransferObject) {
                        buf.append("@");
                        buf.append(value.hashCode());
                    } else if (value instanceof Collection) {
                        buf.append("{");
                        for (final Iterator iter = ((Collection) value)
                                .iterator(); iter.hasNext();) {
                            final Object element = iter.next();
                            if (element instanceof TransferObject) {
                                buf.append("@");
                                buf.append(element.hashCode());
                            } else
                                buf.append(element.toString());
                        }
                        buf.append("}");
                    } else if (value instanceof Map) {
                        buf.append("{");
                        final Map map = (Map) value;
                        for (final Iterator iter = map.keySet().iterator(); iter
                                .hasNext();) {
                            final Object key = iter.next();
                            final Object element = map.get(key);
                            buf.append(key.toString() + "=");
                            if (element instanceof TransferObject) {
                                buf.append("@");
                                buf.append(element.hashCode());
                            } else
                                buf.append(element.toString());
                        }
                        buf.append("}");
                    } else
                        buf.append(value);
                }
            }
            buf.append("}");
            return buf.toString();
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}