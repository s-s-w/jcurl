/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
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
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Compatible to <code>sun.misc.Service</code>.
 * 
 * @author $LastChangedBy$
 * @version $Id$
 */
public class Service {

	private static final Log log = JCLoggerFactory.getLogger(Service.class);
	private static final String META_INF_SERVICES = "META-INF/services/";
	private static final Pattern pat = Pattern.compile("\\s*([^# ]+).*");
	private static final String UTF_8 = "UTF-8";

	@SuppressWarnings("unchecked")
	public static <E> Iterable<Class<E>> providerClasses(final Class<E> clz,
			ClassLoader cl) {
		try {
			if (cl == null)
				cl = clz.getClassLoader();
			final Collection<Class<E>> ret = new LinkedHashSet<Class<E>>();
			// scan all
			for (final Enumeration<URL> e = cl.getResources(META_INF_SERVICES
					+ clz.getName()); e.hasMoreElements();) {
				final URL o = e.nextElement();
				final BufferedReader r = new BufferedReader(
						new InputStreamReader(o.openStream(), UTF_8));
				for (CharSequence line = r.readLine(); line != null; line = r
						.readLine()) {
					final Matcher m = pat.matcher(line);
					if (!m.matches())
						continue;
					try {
						final Class<?> c = Class.forName(m.group(1), true, cl);
						if (clz.isAssignableFrom(c)) {
							final Class<E> clz_ = (Class<E>) c;
							if (ret.contains(clz_))
								log.warn("Duplicate class " + clz_.getName()
										+ " in " + o.toString());
							else
								ret.add(clz_);
						}
					} catch (final ClassCastException e1) {
						// ignore.
					} catch (final ClassNotFoundException e1) {
						// ignore.
					}
				}
			}
			return ret;
		} catch (final IOException e) {
			throw new RuntimeException("Uncaught Exception", e);
		}
	}

	public static <E> Iterable<E> providerInstances(final Class<E> clz,
			final ClassLoader cl) {
		final Collection<E> ret = new ArrayList<E>();
		for (final Class<E> elem : providerClasses(clz, cl))
			try {
				ret.add(elem.newInstance());
			} catch (final InstantiationException e1) {
				// ignore.
			} catch (final IllegalAccessException e1) {
				// ignore.
			}
		return ret;
	}

	/** Compatible to <code>sun.misc.Service#providers</code>. */
	public static <E> Iterator<E> providers(final Class<E> clz) {
		return providers(clz, clz.getClassLoader());
	}

	/** Compatible to <code>sun.misc.Service#providers</code>. */
	public static <E> Iterator<E> providers(final Class<E> clz,
			final ClassLoader cl) {
		return providerInstances(clz, cl).iterator();
	}
}
