/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005 M. Rohrmoser
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
package org.jcurl.core.io;

import junit.framework.TestCase;

import org.jcurl.core.helpers.Dim;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:ConfigTest.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class ConfigTest extends TestCase {

    public void test010_ToDim() {
        assertEquals(Dim.METER_PER_SEC, Config.getDim("-1.4e-3m/s").dim);
        assertEquals(Dim.METER_PER_SEC, Config.getDim("-1.4e-3 m/s").dim);
    }
}