/*
 * jcurl curling simulation framework 
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
package jcurl.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import junit.framework.TestCase;

/**
 * JUnit Test
 * 
 * @see jcurl.core.io.OldConfigReader
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class OldConfigReaderTest extends TestCase {

    private static final File base = new File(
            "/home/m/eclipse/berlios/jcurl/old");

    public static void main(String[] args) {
        junit.textui.TestRunner.run(OldConfigReaderTest.class);
    }

    public void test010_readToken() throws IOException {
        final Reader read = new FileReader(new File(base, "hammy.ini"));
        assertEquals("curliniV2", OldConfigReader.readToken(read));
        assertEquals("ICE", OldConfigReader.readToken(read));
        assertEquals("comment", OldConfigReader.readToken(read));
        assertEquals("", OldConfigReader.readToken(read));
    }

    public void test040_load() throws FileNotFoundException, IOException {
        OldConfigReader r = OldConfigReader.load(new File(base, "hammy.ini"));
    }
}