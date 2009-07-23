/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
package org.jcurl.core.jnlp;

import java.awt.Component;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.logging.Log;
import org.jcurl.core.jnlp.FileDialogService.Contents;
import org.jcurl.core.jnlp.FileDialogService.OpenService;
import org.jcurl.core.jnlp.FileDialogService.SaveService;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Fully reflection based implementation for use in untrusted environments to
 * avoid compile-time dependency.
 * 
 * https://java.sun.com/products/javawebstart/1.0.1/javadoc/javax/jnlp/FileOpenService.html
 * https://java.sun.com/products/javawebstart/1.0.1/javadoc/javax/jnlp/FileSaveService.html
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
class FileDialogWebstart implements OpenService, SaveService {

	private static final Log log = JCLoggerFactory
			.getLogger(FileDialogWebstart.class);

	/**
	 * Dynamic Interface Wrapper (or dynamic Delegate) using
	 * {@link Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)}
	 * mapping only identical methods.
	 * <p>
	 * The two interfaces must have identical methods!
	 * </p>
	 * 
	 * @param src
	 *            the source instance (the one to wrap)
	 * @param dstT
	 *            destination interface type
	 * @return target interface instance.
	 */
	static Object wrap(final Object src, final Class<?> dstT) {
		if (log.isDebugEnabled())
			log.debug("wrap(" + src + ", " + dstT + ")");
		if (src == null)
			return null;
		return Proxy.newProxyInstance(
				FileDialogWebstart.class.getClassLoader(),
				new Class[] { dstT }, new InvocationHandler() {
					public Object invoke(final Object proxy,
							final Method method, final Object[] args)
							throws Throwable {
						final Class<?> srcT = src.getClass();
						// Wrap ALL Methods (also getClass, equals, toString,
						// ...)
						final Method dstM = srcT.getMethod(method.getName(),
								method.getParameterTypes());
						if (log.isDebugEnabled()) {
							log.debug("proxy: " + proxy.getClass());
							log.debug("method: " + method);
							log.debug("args: " + args);
							log.debug("srcT: " + srcT);
							log.debug("dstM: " + dstM);
						}
						final Object ret = dstM.invoke(src, args);
						if (log.isDebugEnabled())
							log.debug("ret: " + ret);
						return ret;
					}
				});
	}

	private final Object fos;

	private final Object fss;

	private final Method open;

	private final Method save;

	private final Method saveAs;

	private final Class<?> tco;

	FileDialogWebstart() {
		try {
			tco = Class.forName("javax.jnlp.FileContents");
			if (log.isDebugEnabled()) {
				final Method[] ms = tco.getMethods();
				for (int i = ms.length - 1; i >= 0; i--)
					log.debug(ms[i]);
			}
			// FileOpenService
			fos = Class.forName("javax.jnlp.ServiceManager").getMethod(
					"lookup", new Class[] { String.class }).invoke(null,
					new Object[] { "javax.jnlp.FileOpenService" });
			log.debug(fos);
			if (log.isDebugEnabled()) {
				final Method[] ms = fos.getClass().getMethods();
				for (int i = ms.length - 1; i >= 0; i--)
					log.debug(ms[i]);
			}
			open = fos.getClass().getMethod("openFileDialog",
					new Class[] { String.class, String[].class });
			log.debug(open);
			// FileSaveService
			fss = Class.forName("javax.jnlp.ServiceManager").getMethod(
					"lookup", new Class[] { String.class }).invoke(null,
					new Object[] { "javax.jnlp.FileSaveService" });
			log.debug(fss);
			if (log.isDebugEnabled()) {
				final Method[] ms = fss.getClass().getMethods();
				for (int i = ms.length - 1; i >= 0; i--)
					log.debug(ms[i]);
			}
			saveAs = fss.getClass().getMethod("saveAsFileDialog",
					new Class[] { String.class, String[].class, tco });
			log.debug(saveAs);
			save = fss.getClass().getMethod(
					"saveFileDialog",
					new Class[] { String.class, String[].class,
							InputStream.class, String.class });
			log.debug(save);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Contents openFileDialog(final String pathHint,
			final String[] extensions, final Component parent) {
		try {
			if (log.isDebugEnabled())
				log.debug("openFileDialog(" + pathHint + ", " + to(extensions)
						+ ", " + parent + ")");
			return (Contents) wrap(open.invoke(fos, new Object[] { pathHint,
					extensions }), Contents.class);
		} catch (final Exception e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	public Contents saveAsFileDialog(final String pathHint,
			final String[] extensions, final Contents contents,
			final Component parent) {
		try {
			if (log.isDebugEnabled())
				log.debug(pathHint + ", " + to(extensions) + ", " + contents
						+ ", " + parent);
			return (Contents) wrap(saveAs.invoke(fss, new Object[] { pathHint,
					extensions, wrap(contents, tco) }), Contents.class);
		} catch (final Exception e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	public Contents saveFileDialog(final String pathHint,
			final String[] extensions, final InputStream stream,
			final String name, final Component parent) {
		try {
			if (log.isDebugEnabled())
				log.debug(pathHint + ", " + to(extensions) + ", " + stream
						+ ", " + name + ", " + parent);
			return (Contents) wrap(save.invoke(fss, new Object[] { pathHint,
					extensions, stream, name }), Contents.class);
		} catch (final Exception e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	private String to(final String[] a) {
		final StringBuilder b = new StringBuilder();
		for (final String element : a)
			b.append(element).append(" ");
		return b.toString().trim();
	}
}
