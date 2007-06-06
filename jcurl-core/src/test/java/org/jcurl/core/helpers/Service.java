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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author $LastChangedBy$
 * @version $Id$
 */
class Service {

    private static final Pattern pat = Pattern.compile("\\s*([^# ]+).*");

    public static Iterator providers(final Class clz) {
        return providers(clz, clz.getClassLoader());
    }

    public static Iterator providers(final Class clz, final ClassLoader cl) {
        try {
            final Collection ret = new ArrayList();
            // scan all
            for (final Enumeration e = cl.getResources("META-INF/services/"
                    + clz.getName()); e.hasMoreElements();) {
                final URL o = (URL) e.nextElement();
                final BufferedReader r = new BufferedReader(
                        new InputStreamReader(o.openStream(), "UTF-8"));
                for (CharSequence line = r.readLine(); line != null; line = r.readLine()) {
                    final Matcher m = pat.matcher(line);
                    if (!m.matches())
                        continue;
                    try {
                        final Class c = Class.forName(m.group(1), true, cl);
                        if (clz.isAssignableFrom(c))
                            ret.add(c.newInstance());
                    } catch (final ClassNotFoundException e1) {
                        // Na - dann ignorieren wir den halt.
                    } catch (final InstantiationException e1) {
                        // Na - dann ignorieren wir den halt.
                    } catch (final IllegalAccessException e1) {
                        // Na - dann ignorieren wir den halt.
                    }
                }
            }
            return ret.iterator();
        } catch (final IOException e) {
            throw new RuntimeException("Uncaught Exception", e);
        }
    }
}
