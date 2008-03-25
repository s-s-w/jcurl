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
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Dispatch {@link Task}s to the according {@link Executor}.
 * 
 * <ul>
 * <li>http://java.sun.com/javase/6/docs/api/javax/swing/SwingWorker.html</li>
 * <li>Fowler: http://martinfowler.com/eaaDev/EventCollaboration.html</li>
 * <li>EventBus https://eventbus.dev.java.net/
 * https://aptframework.dev.java.net/article/edtMessageBus.html</li>
 * <li><a href="https://beansbinding.dev.java.net/">JSR-295 Beans Binding</a></li>
 * <li><a href="https://appframework.dev.java.net/">JSR-296 Swing Application
 * Framework</a></li>
 * </ul>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class TaskExecutor implements Executor {

	/** Execute in the current Thread */
	public static class Current implements Executor {
		public void execute(final Runnable command) {
			command.run();
		}
	}

	public static class ExecutorDelegate implements Executor {
		private final Executor base;

		public ExecutorDelegate(final Executor base) {
			this.base = base;
		}

		public void execute(final Runnable command) {
			base.execute(command);
		}
	}

	/**
	 * Similar to {@link ForkableFlex} but early-bound to an {@link Executor}.
	 * 
	 * @param T
	 *            see {@link TaskExecutor#execute(Runnable, Class)}
	 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
	 * @version $Id$
	 */
	public static abstract class ForkableFixed<T extends Executor> implements
			Task<T> {
		private final Executor ex;

		/**
		 * Delegate to {@link ForkableFixed#ForkableFixed(Executor)} with
		 * {@link TaskExecutor#getInstance()}.
		 */
		public ForkableFixed() {
			this(TaskExecutor.getInstance());
		}

		/**
		 * Enable dependency injaction for testing purposes.
		 * 
		 * @param ex
		 */
		public ForkableFixed(final Executor ex) {
			this.ex = ex;
		}

		/** Delegate <code>this</code> to {@link Executor#execute(Runnable)} */
		public void fork() {
			ex.execute(this);
		}
	}

	/**
	 * Similar to {@link ForkableFixed} but late-bound to an {@link Executor}.
	 * 
	 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
	 * @version $Id$
	 */
	public static abstract class ForkableFlex implements Runnable {
		private final TaskExecutor ex;

		/**
		 * Delegate to {@link ForkableFlex#ForkableFlex(TaskExecutor)} with
		 * {@link TaskExecutor#getInstance()}.
		 */
		public ForkableFlex() {
			this(TaskExecutor.getInstance());
		}

		/**
		 * Enable dependency injaction for testing purposes.
		 * 
		 * @param ex
		 */
		public ForkableFlex(final TaskExecutor ex) {
			this.ex = ex;
		}

		/**
		 * Delegate <code>this</code> to
		 * {@link TaskExecutor#execute(Runnable, Class)}
		 */
		public void fork(final Class<? extends Executor> dst) {
			ex.execute(this, dst);
		}
	}

	/** Execute in a multi threaded pool executor. */
	public static class Parallel extends ExecutorDelegate {
		public Parallel() {
			super(Executors.newCachedThreadPool());
		}
	}

	/** Execute in a single threaded executor. */
	public static class Single extends ExecutorDelegate {
		public Single() {
			super(Executors.newSingleThreadExecutor());
		}
	}

	/**
	 * TODO Execute in a single threaded executor but remove duplicates from the
	 * queue.
	 */
	public static class SmartQueue extends ExecutorDelegate {
		public SmartQueue() {
			super(Executors.newSingleThreadExecutor());
		}
	}

	/** Execute in the Swing/AWT Event Queue Thread */
	public static class SwingEDT implements Executor {
		public void execute(final Runnable command) {
			SwingUtilities.invokeLater(command);
		}
	}

	static interface Task<T extends Executor> extends Runnable {}

	private static final TaskExecutor instance = new TaskExecutor();

	private static final Log log = JCLoggerFactory
			.getLogger(TaskExecutor.class);

	/** Find the presence of a generic type parameter. */
	@SuppressWarnings("unchecked")
	static Class<Executor> findMessageTypeParam(final Class<? extends Task> clz) {
		if (Object.class.equals(clz.getGenericSuperclass())) {
			final ParameterizedType pt = (ParameterizedType) clz
					.getGenericInterfaces()[0];
			return (Class<Executor>) pt.getActualTypeArguments()[0];
		}
		final ParameterizedType pt = (ParameterizedType) clz
				.getGenericSuperclass();
		return (Class<Executor>) pt.getActualTypeArguments()[0];
	}

	static TaskExecutor getInstance() {
		return instance;
	}

	private final Map<Class<? extends Executor>, Executor> map = new WeakHashMap<Class<? extends Executor>, Executor>();

	private TaskExecutor() {}

	/**
	 * Cast down to {@link Task} and delegate to
	 * {@link #execute(org.jcurl.core.ui.TaskExecutor.Task)}.
	 * 
	 * @throws RejectedExecutionException
	 *             if the downcast fails.
	 */
	@SuppressWarnings("unchecked")
	public void execute(final Runnable command) {
		try {
			execute((Task<? extends Executor>) command);
		} catch (final ClassCastException e) {
			throw new RejectedExecutionException(e);
		}
	}

	/**
	 * Delegate to the {@link Executor#execute(Runnable)} of the type parameter.
	 * 
	 * Keeps a {@link Map} class-&gt;instance.
	 * 
	 * @param msg
	 * @param et
	 *            executor to delegate to.
	 * @throws RejectedExecutionException
	 *             {@link Class#newInstance()} of the type parameter failed.
	 */
	public void execute(final Runnable msg, final Class<? extends Executor> et) {
		log.debug(et);
		Executor ex;
		try {
			synchronized (et) {
				ex = map.get(et);
				if (ex == null)
					map.put(et, ex = et.newInstance());
			}
		} catch (final InstantiationException e) {
			throw new RejectedExecutionException(e);
		} catch (final IllegalAccessException e) {
			throw new RejectedExecutionException(e);
		}
		ex.execute(msg);
	}

	/**
	 * Delegate to the {@link #execute(Runnable, Class)}.
	 * 
	 * Uses {@link #findMessageTypeParam(Class)} to find the {@link Executor}
	 * type parameter
	 * 
	 * @param msg
	 */
	void execute(final Task<? extends Executor> msg) {
		execute(msg, findMessageTypeParam(msg.getClass()));
	}
}
