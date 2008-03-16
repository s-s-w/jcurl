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
package org.jcurl.demo.tactics;

import java.awt.event.ActionEvent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * Try building Menues via Annotations.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
abstract class Menu {
    @Retention(RetentionPolicy.RUNTIME)
    @interface JCAction {
        String accelerator() default "";

        int idx();

        int mnemonic();

        String name();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface JCMenu {
        String value();
    }

    private static JMenu compile(final Menu o, final Map<Method, Action> m2a,
            final Map<String, Method> n2m) {
        final JCMenu jcm = o.getClass().getAnnotation(JCMenu.class);
        if (jcm == null)
            throw new UnsupportedOperationException("No annotation "
                    + JCMenu.class.getName() + " found.");
        final SortedMap<Integer, Action> actions = new TreeMap<Integer, Action>();
        final int max = compile(o, actions, m2a, n2m);
        final JMenu ret = new JMenu(jcm.value());
        for (int i = 0; i <= max; i++) {
            final Action a = actions.get(i);
            if (a == null)
                ret.addSeparator();
            else
                ret.add(new JMenuItem(a));
        }
        return ret;
    }

    private static int compile(final Menu o,
            final SortedMap<Integer, Action> actions,
            final Map<Method, Action> m2a, final Map<String, Method> n2m) {
        if (n2m != null)
            n2m.clear();
        if (m2a != null)
            m2a.clear();
        final Class<? extends Menu> c = o.getClass();
        int max = 0;
        for (final Method m : c.getMethods()) {
            if (m.getParameterTypes().length != 1)
                continue;
            if (!ActionEvent.class.isAssignableFrom(m.getParameterTypes()[0]))
                continue;
            final JCAction aa = m.getAnnotation(JCAction.class);
            if (aa == null)
                continue;
            final Action a = new AbstractAction() {
                private static final long serialVersionUID = 1776964891211913808L;

                public void actionPerformed(final ActionEvent e) {
                    try {
                        final Object[] p = { e };
                        m.invoke(o, p);
                    } catch (IllegalArgumentException e1) {
                        throw new RuntimeException("Unhandled", e1);
                    } catch (IllegalAccessException e1) {
                        throw new RuntimeException("Unhandled", e1);
                    } catch (InvocationTargetException e1) {
                        throw new RuntimeException("Unhandled", e1);
                    }
                }
            };
            a.putValue(Action.NAME, aa.name());
            a.putValue(Action.MNEMONIC_KEY, aa.mnemonic());
            a.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(aa
                    .accelerator()));
            actions.put(aa.idx(), a);
            if (aa.idx() > max)
                max = aa.idx();
            if (n2m != null)
                n2m.put(m.getName(), m);
            if (m2a != null)
                m2a.put(m, a);
        }
        return max;
    }

    protected final Map<Method, Action> m2a = new HashMap<Method, Action>();

    private transient JMenu menu = null;

    protected final Map<String, Method> n2m = new TreeMap<String, Method>();

    public Action findAction(final Method name) {
        return m2a.get(name);
    }

    public Action findAction(final String name) {
        return findAction(n2m.get(name));
    }

    public JMenu menu() {
        if (menu == null)
            menu = compile(this, m2a, n2m);
        return menu;
    }
}
