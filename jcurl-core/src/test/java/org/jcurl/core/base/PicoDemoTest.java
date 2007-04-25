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
package org.jcurl.core.base;

import junit.framework.TestCase;

import org.jcurl.core.model.CurlerDenny;
import org.jcurl.core.model.CurlerNoCurl;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class PicoDemoTest extends TestCase {

    public static class A {
        public final B b;

        public A(final B b) {
            this.b = b;
        }
    }

    public static class B {
        public final String c;

        public B(final String c) {
            this.c = c;
        }
    }

    public void test0() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentImplementation(A.class);
        pico.registerComponentImplementation(B.class);
        pico.registerComponentImplementation(String.class);
        A a = (A) pico.getComponentInstance(A.class);
        assertEquals("", a.b.c);

        pico = new DefaultPicoContainer();
        pico.registerComponentImplementation(A.class);
        pico.registerComponentImplementation(B.class);
        pico.registerComponentInstance("Hello, world!");
        a = (A) pico.getComponentInstance(A.class);
        assertEquals("Hello, world!", a.b.c);
        pico.dispose();
        assertNotNull(pico.getComponentInstance(A.class));
    }

    public void test1() {
        final PicoContainer pico;
        {
            final MutablePicoContainer paco = new DefaultPicoContainer();
            pico = paco;
            paco.registerComponentImplementation(CurlerNoCurl.class);
            paco.registerComponentImplementation(CurlerDenny.class);
            paco.unregisterComponent(CurlerNoCurl.class);
        }
        assertNull(pico.getComponentInstance(Curler.class));
        Object o = pico.getComponentInstance(CurlerDenny.class);
        assertNotNull(o);
        o = pico.getComponentInstanceOfType(Curler.class);
        assertNotNull(o);
        pico.dispose();
    }
}
