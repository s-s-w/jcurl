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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.xml.sax.SAXException;

/**
 * JUnit test.
 * 
 * @see org.jcurl.core.io.SetupSaxDeSer
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:SetupSaxDeSerTest.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class SetupSaxDeSerTest extends TestCase {

    // private static final File _base = new File(
    // "/home/m/eclipse/berlios/jcurl/config/jcurl.jar/setup");

    private static final File base = new File("config/jcurl.jar/setup");

    private static final URL baseUrl;

    private static final Log log = JCLoggerFactory
            .getLogger(SetupSaxDeSerTest.class);
    static {
        try {
            baseUrl = base.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("This MUST be a valid url!", e);
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SetupSaxDeSerTest.class);
    }

    public void test010_parse() throws ParserConfigurationException,
            SAXException, IOException {
        SetupBuilder so = SetupSaxDeSer.parse(new File(base, "hammy.jcx"));
        assertNotNull(so);
        so = SetupSaxDeSer.parse(new URL(baseUrl, "hammy.jcx"));
        assertNotNull(so);
    }

    public void test020_parseZ() throws ParserConfigurationException,
            SAXException, IOException {
        SetupBuilder so = SetupSaxDeSer.parse(new File(base, "hammy.jcz"));
        assertNotNull(so);
        so = SetupSaxDeSer.parse(new URL(baseUrl, "hammy.jcz"));
        assertNotNull(so);
    }
}