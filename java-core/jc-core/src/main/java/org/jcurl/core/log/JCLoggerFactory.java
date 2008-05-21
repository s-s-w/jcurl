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
package org.jcurl.core.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Jdk14Logger;

/**
 * Factory to have all logging in hand.
 * <p>
 * Does some tricks to be useable in unsigned Webstart Applications (a.k.a.
 * sandbox).
 * </p>
 * <p>
 * {@link LogFactory#getLog(Class)} seems to always access system properties and
 * therefore cannot be used inside a sandbox.
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public final class JCLoggerFactory {

	private static final boolean fallback;
	static {
		boolean t = false;
		try {
			LogFactory.getLog(JCLoggerFactory.class);
		} catch (final ExceptionInInitializerError e) {
			t = true;
		}
		fallback = t;
	}

	/**
	 * Delegate to {@link LogFactory#getLog(Class)}.
	 * 
	 * @param clz
	 * @return the logger.
	 */
	public static Log getLogger(final Class<?> clz) {
		if (!fallback)
			return LogFactory.getLog(clz);
		// if (false) {
		final Jdk14Logger l = new Jdk14Logger(clz.getName());
		return l;
		// } else {
		// final SimpleLog l = new SimpleLog(clz.getName());
		// l.setLevel(SimpleLog.LOG_LEVEL_ALL);
		// return l;
		// }
	}

	private JCLoggerFactory() {}
}