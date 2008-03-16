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
package org.jcurl.core.base;

import java.util.Iterator;

import junit.framework.TestCase;

import org.jcurl.core.helpers.Service;

public class PluginTest extends TestCase {

    public void testCollider() {
        final Iterator it = Service.providers(Collider.class);
        assertNotNull(it.next());
    }

    public void testCollissionDetector() {
        final Iterator it = Service.providers(CollissionDetector.class);
        assertNotNull(it.next());
    }

    public void testSlider() {
        final Iterator it = Service.providers(Curler.class);
        assertNotNull(it.next());
    }
}
