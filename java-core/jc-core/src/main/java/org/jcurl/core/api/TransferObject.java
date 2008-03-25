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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Base class for all value Objects. Provides a generic toString.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 * 
 */
public abstract class TransferObject implements Serializable {

    /**
     * Generic toString method.
     * 
     * @return all properties' values
     */
    @SuppressWarnings("unchecked")
    @Override
    public String toString() { // inspired by Hardcore Java (O'reilly, page
        // 228)
        try {
            final BeanInfo info = Introspector.getBeanInfo(getClass(),
                    Object.class);
            final PropertyDescriptor[] props = info.getPropertyDescriptors();
            final StringBuilder buf = new StringBuilder(500);
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
                    value = props[idx].getReadMethod().invoke(this,
                            (Object[]) null);
                    if (value instanceof TransferObject) {
                        buf.append("@");
                        buf.append(value.hashCode());
                    } else if (value instanceof Collection) {
                        buf.append("{");
                        for (final Object element : (Collection) value)
                            if (element instanceof TransferObject) {
                                buf.append("@");
                                buf.append(element.hashCode());
                            } else
                                buf.append(element.toString());
                        buf.append("}");
                    } else if (value instanceof Map) {
                        buf.append("{");
                        for (final Iterator it = ((Map) value).entrySet()
                                .iterator(); it.hasNext();) {
                            final Entry element = (Entry) it.next();
                            buf.append(element.getKey() + "=");
                            if (element.getValue() instanceof TransferObject) {
                                buf.append("@");
                                buf.append(element.getValue().hashCode());
                            } else
                                buf.append(element.getValue().toString());
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