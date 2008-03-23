/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
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

package org.jcurl.core.ui;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Dispatch {@link Message}s to the according {@link Executor}.
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class MessageExecutor implements Executor {

	public static interface Message<T extends ExecutorType> extends Runnable {}

	static interface ExecutorType {}

	public static interface Parallel extends ExecutorType {}

	public static interface Single extends ExecutorType {}

	public static interface SwingEDT extends ExecutorType {}

	private static final MessageExecutor instance = new MessageExecutor();

	private static final Log log = JCLoggerFactory
			.getLogger(MessageExecutor.class);

	/**
	 * Find the presence of a generic type parameter.
	 */
	@SuppressWarnings("unchecked")
	static <T> Class<T> findGenericTypeParam(final Class<?> clz,
			final Class<T> genericParam) {
		for (final Type t : clz.getGenericInterfaces())
			for (final Type ta : ((ParameterizedType) t)
					.getActualTypeArguments())
				if (ta instanceof Class)
					if (genericParam.isAssignableFrom((Class<?>) ta))
						return (Class<T>) ta;
		return null;
	}

	public static MessageExecutor getInstance() {
		return instance;
	}

	private final ExecutorService parallel;

	private final ExecutorService single;

	private MessageExecutor() {
		single = Executors.newSingleThreadExecutor();
		parallel = Executors.newCachedThreadPool();
	}

	public void execute(final Message<? extends ExecutorType> msg) {
		final Class<ExecutorType> et = findGenericTypeParam(msg.getClass(),
				ExecutorType.class);
		log.debug(et);
		if (SwingEDT.class.equals(et))
			SwingUtilities.invokeLater(msg);
		else if (Single.class.equals(et))
			single.execute(msg);
		else if (Parallel.class.equals(et))
			parallel.execute(msg);
		else
			throw new RejectedExecutionException(et.getName());
	}

	@SuppressWarnings("unchecked")
	public void execute(final Runnable command) {
		try {
			execute((Message<? extends ExecutorType>) command);
		} catch (final ClassCastException e) {
			throw new RejectedExecutionException(e);
		}
	}
}
