/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
package org.jcurl.core.io;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jcurl.core.base.ComputedTrajectorySet;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.TrajectorySet;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class ComputedTrajectorySetXml {

    protected void endElement(final ContentHandler dst, final String elem)
            throws SAXException {
        dst.endElement(SetupSaxSer.NS, null, elem);
    }

    public ComputedTrajectorySet read(final ComputedTrajectorySet s,
            final InputStream src) {
        return s;
    }

    protected void startElement(final ContentHandler dst, final String elem,
            final Attributes atts) throws SAXException {
        dst.startElement(SetupSaxSer.NS, null, elem, atts);
    }

    protected void startElement(final ContentHandler dst, final String elem,
            final String[] atts) throws SAXException {
        final AttributesImpl a = new AttributesImpl();
        for (int i = 0; i < atts.length; i += 2)
            a.addAttribute(SetupSaxSer.NS, null, atts[i], null, atts[i + 1]);
        startElement(dst, elem, a);
    }

    Object val(final PropertyDescriptor p, final Object context)
            throws IllegalAccessException, InvocationTargetException {
        final Method read = p.getReadMethod();
        final Class clz = read.getDeclaringClass();
        if (TrajectorySet.class.isAssignableFrom(clz))
            if ("currentTime".equals(p.getName()))
                return null;
        if (ComputedTrajectorySet.class.isAssignableFrom(clz)) {
            if ("curveStore".equals(p.getName()))
                return null;
            if ("currentPos".equals(p.getName()))
                return null;
            if ("currentSpeed".equals(p.getName()))
                return null;
        }
        if (RockSet.class.isAssignableFrom(clz)) {
            if ("lastChanged".equals(p.getName()))
                return null;
            if ("currentSpeed".equals(p.getName()))
                return null;
        }
        return p.getReadMethod().invoke(context, (Object[]) null);
    }

    public void write(final ComputedTrajectorySet s, final ContentHandler dst)
            throws SAXException {
        startElement(dst, "trajectory", new String[] { "class",
                s.getClass().getName() });
        try {
            final BeanInfo bi = Introspector.getBeanInfo(s.getClass(),
                    Object.class);
            final PropertyDescriptor[] pd = bi.getPropertyDescriptors();
            for (PropertyDescriptor element : pd)
                write(dst, element, s);
        } catch (final IntrospectionException e) {
            throw new RuntimeException(e);
        }
        endElement(dst, "trajectory");
    }

    protected void write(final ContentHandler dst, final PropertyDescriptor p,
            final Object context) throws SAXException {
        try {
            final Object ctx = val(p, context);
            if (ctx == null)
                return;
            final BeanInfo bi = Introspector.getBeanInfo(ctx.getClass(),
                    Object.class);
            final String name = p.getName();
            startElement(dst, p.getName(), new String[] { "class",
                    ctx.getClass().getName() });
            final PropertyDescriptor[] pd = bi.getPropertyDescriptors();
            for (PropertyDescriptor element : pd)
                write(dst, element, ctx);
            endElement(dst, name);
        } catch (final NullPointerException e) {
            // throw new RuntimeException(e);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (final IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }
}
