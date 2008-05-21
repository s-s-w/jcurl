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

package org.jcurl.demo.tactics.old;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.TaskExecutor.ForkableFlex;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class ActionRegistry {
	/**
	 * Mark a method that should be wrapped into a contextless {@link Action}.
	 * 
	 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
	 * @version $Id$
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface JCAction {
		/**
		 * Accepts either strings in the form
		 * {@link KeyStroke#getKeyStroke(String)} or a simplified form, e.g.
		 * <code>CTRL-SHIFT-ALT-F1</code>.
		 */
		String accelerator() default "";

		/** Sorting order for menu entries. &lt; 0 hides. */
		int idx();

		/** add a separator before this menu entry. */
		boolean separated() default false;

		/** Prefix the mnemonic with '&'. */
		String title();
	}

	/**
	 * Mark a class which {@link JCAction}-marked methods will become entries.
	 * 
	 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
	 * @version $Id$
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface JCMenu {
		/** Prefix the mnemonic with '&'. */
		String value();
	}

	private static final ActionRegistry instance = new ActionRegistry();
	private static final Pattern KeyPat = Pattern
			.compile("((?:(?:CTRL|ALT|SHIFT)-)*)([1-4a-zA-Z]+)");
	private static final Log log = JCLoggerFactory
			.getLogger(ActionRegistry.class);
	private static final Pattern MneFinder = Pattern.compile(".*[&]([^&]).*");
	private static final Pattern MneStripper = Pattern.compile("[&](.)");
	private static final Map<String, Integer> str2key = new TreeMap<String, Integer>();

	static {
		str2key.put("UP", KeyEvent.VK_UP);
		str2key.put("DOWN", KeyEvent.VK_DOWN);
		str2key.put("LEFT", KeyEvent.VK_LEFT);
		str2key.put("RIGHT", KeyEvent.VK_RIGHT);
		str2key.put("ADD", KeyEvent.VK_ADD);
		str2key.put("SUBTRACT", KeyEvent.VK_SUBTRACT);
		str2key.put("PLUS", KeyEvent.VK_PLUS);
		str2key.put("MINUS", KeyEvent.VK_MINUS);
		str2key.put("HOME", KeyEvent.VK_HOME);
		str2key.put("END", KeyEvent.VK_END);
		str2key.put("PGUP", KeyEvent.VK_PAGE_UP);
		str2key.put("PGDN", KeyEvent.VK_PAGE_DOWN);
		str2key.put("F1", KeyEvent.VK_F1);
		str2key.put("F2", KeyEvent.VK_F2);
		str2key.put("F3", KeyEvent.VK_F3);
		str2key.put("F4", KeyEvent.VK_F4);
		str2key.put("F5", KeyEvent.VK_F5);
		str2key.put("F6", KeyEvent.VK_F6);
		str2key.put("F7", KeyEvent.VK_F7);
		str2key.put("F8", KeyEvent.VK_F8);
		str2key.put("F9", KeyEvent.VK_F9);
		str2key.put("F10", KeyEvent.VK_F10);
		str2key.put("F11", KeyEvent.VK_F11);
		str2key.put("F12", KeyEvent.VK_F12);
	}

	public static ActionRegistry getInstance() {
		return instance;
	}

	public static void invoke(final Class<?> controller, final String method) {
		getInstance().findAction(controller, method).actionPerformed(null);
	}

	public static void invoke(final Class<?> controller, final String method,
			final Class<? extends Executor> exec) {
		new ForkableFlex() {
			public void run() {
				getInstance().findAction(controller, method).actionPerformed(
						null);
			}
		}.fork(exec);
	}

	/** Resolve class (controller) to action. */
	private final Map<Class<?>, Map<Method, Action>> c2a = new HashMap<Class<?>, Map<Method, Action>>();
	/** Resolve instance (controller) to action. */
	private final Map<Object, Map<Method, Action>> i2a = new HashMap<Object, Map<Method, Action>>();

	private void addMenuItem(final Object controller, final JMenu ret,
			final Method m) {
		final JCAction a = m.getAnnotation(JCAction.class);
		if (a.separated())
			ret.addSeparator();
		final JMenuItem mi = new JMenuItem();
		mi.setAction(findAction(controller, m));
		{
			// TODO move to createAction
			final Character mne = findMnemonic(a.title());
			if (mne != null)
				mi.setMnemonic(mne);
		}
		ret.add(mi);
	}

	/** Create a disabled {@link Action}. */
	private Action createAction(final Object controller, final Method m,
			final JCAction a) {
		final Action ac = new AbstractAction() {
			private static final long serialVersionUID = 2349356576661476730L;

			public void actionPerformed(final ActionEvent e) {
				try {
					m.invoke(controller, (Object[]) null);
				} catch (final IllegalArgumentException e1) {
					throw new RuntimeException("Unhandled", e1);
				} catch (final IllegalAccessException e1) {
					throw new RuntimeException("Unhandled", e1);
				} catch (final InvocationTargetException e1) {
					throw new RuntimeException("Unhandled", e1);
				}
			}
		};
		ac.putValue(Action.NAME, stripMnemonic(a.title()));
		ac.putValue(Action.ACCELERATOR_KEY, findAccelerator(a.accelerator()));
		if (false) {
			final Character mne = findMnemonic(a.title());
			if (mne != null)
				ac.putValue(Action.MNEMONIC_KEY, KeyStroke.getKeyStroke(mne));
		}
		ac.setEnabled(false);
		return ac;
	}

	public JMenu createJMenu(final Object controller) {
		final Class<?> clz = controller.getClass();
		final JCMenu ca = clz.getAnnotation(JCMenu.class);
		if (ca == null)
			return null;
		// first get all candidates
		final Map<Integer, Method> sorted = new TreeMap<Integer, Method>();
		for (final Method me : clz.getMethods()) {
			final JCAction ma = me.getAnnotation(JCAction.class);
			if (ma == null || ma.idx() < 0)
				continue;
			if (sorted.put(ma.idx(), me) != null)
				throw new IllegalStateException(ma.idx() + ": " + me.toString());
		}
		// create the menu
		final JMenu ret = new JMenu(stripMnemonic(ca.value()));
		{
			final Character mne = findMnemonic(ca.value());
			if (mne != null)
				ret.setMnemonic(mne);
		}
		// add the menu items in natural order
		for (final Entry<Integer, Method> elem : sorted.entrySet())
			addMenuItem(controller, ret, elem.getValue());
		return ret;
	}

	KeyStroke findAccelerator(final String acc) {
		if (acc == null || "".equals(acc))
			return null;
		final Matcher m = KeyPat.matcher(acc);
		if (m.matches()) {
			int modifiers = 0;
			{
				final String gr = m.group(1);
				if (!"".equals(gr)) {
					final String[] mod = gr.split("-");
					for (final String mm : mod)
						if ("CTRL".equals(mm))
							modifiers |= InputEvent.CTRL_MASK;
						else if ("ALT".equals(mm))
							modifiers |= InputEvent.ALT_MASK;
						else if ("SHIFT".equals(mm))
							modifiers |= InputEvent.SHIFT_MASK;
						else
							throw new IllegalStateException(mm);
				}
			}
			if (m.group(2).length() == 1)
				return KeyStroke.getKeyStroke(m.group(2).charAt(0), modifiers);
			final Integer kc = str2key.get(m.group(2));
			if (kc == null)
				throw new IllegalStateException(m.group(2));
			return KeyStroke.getKeyStroke(kc.intValue(), modifiers);
		} else {
			// swing syntax
			final KeyStroke k = KeyStroke.getKeyStroke(acc);
			if (k == null)
				throw new IllegalArgumentException(acc);
			return k;
		}
	}

	/**
	 * @return never <code>null</code>
	 * @throws IllegalArgumentException
	 *             no such action found
	 */
	public Action findAction(final Class<?> controller, final String method) {
		try {
			return findAction(controller.getMethod(method, (Class<?>[]) null));
		} catch (final Exception e) {
			throw new IllegalArgumentException(controller.getName() + "::"
					+ method, e);
		}
	}

	private Action findAction(final Map<Method, Action> m, final Method method) {
		if (m == null)
			throw new IllegalArgumentException(method.getDeclaringClass()
					.getName());
		final Action ret = m.get(method);
		if (ret == null)
			throw new IllegalArgumentException(method.toString());
		return ret;
	}

	/**
	 * @return never <code>null</code>
	 * @throws IllegalArgumentException
	 *             no such action found
	 */
	public Action findAction(final Method method) {
		return findAction(c2a.get(method.getDeclaringClass()), method);
	}

	/**
	 * @return never <code>null</code>
	 * @throws IllegalArgumentException
	 *             no such action found
	 */
	public Action findAction(final Object controller, final Method method) {
		return findAction(i2a.get(controller), method);
	}

	/**
	 * @return never <code>null</code>
	 * @throws IllegalArgumentException
	 *             no such action found
	 */
	public Action findAction(final Object controller, final String method)
			throws IllegalArgumentException {
		try {
			return findAction(controller, controller.getClass().getMethod(
					method, (Class<?>[]) null));
		} catch (final Exception e) {
			throw new IllegalArgumentException(controller.getClass().getName()
					+ "::" + method, e);
		}
	}

	Character findMnemonic(final CharSequence acc) {
		final Matcher m = MneFinder.matcher(acc);
		if (!m.matches())
			return null;
		return m.group(1).charAt(0);
	}

	public Object registerController(final Object controller) {
		final Class<?> clz = controller.getClass();
		for (final Method me : clz.getMethods()) {
			final JCAction ma = me.getAnnotation(JCAction.class);
			if (ma == null)
				continue;
			Map<Method, Action> m = i2a.get(controller);
			if (m == null) {
				i2a.put(controller, m = new HashMap<Method, Action>());
				if (c2a.put(clz, m) != null)
					throw new IllegalStateException(clz.getName());
			}
			if (m.put(me, createAction(controller, me, ma)) != null)
				throw new IllegalStateException(me.toString());
		}
		return controller;
	}

	/** De-escape "mnemocicced" titles. */
	String stripMnemonic(final CharSequence a) {
		return MneStripper.matcher(a).replaceAll("$1");
	}
}
