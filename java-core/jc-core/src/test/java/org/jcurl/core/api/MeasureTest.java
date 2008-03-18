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
package org.jcurl.core.api;

import org.jcurl.core.api.Measure;
import org.jcurl.core.api.Unit;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class MeasureTest extends TestCase {
    public void testConvert() {
        final Measure in = new Measure(12, Unit.INCH);
        final Measure ft = new Measure(1, Unit.FOOT);
        final Measure m = new Measure(0.3048, Unit.METER);

        assertEquals(m, m.to(Unit.METER));
        assertEquals(m, in.to(Unit.METER));
        assertEquals(m, ft.to(Unit.METER));
    }
}