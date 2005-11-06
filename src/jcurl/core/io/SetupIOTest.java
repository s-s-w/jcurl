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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import jcurl.core.JCLoggerFactory;
import jcurl.model.PositionSet;
import jcurl.model.SpeedSet;
import jcurl.sim.model.CollissionSpin;
import jcurl.sim.model.SlideStraight;
import junit.framework.TestCase;

import org.apache.ugli.ULogger;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 * @see jcurl.core.io.SetupIO
 */
public class SetupIOTest extends TestCase {

    private static final ULogger log = JCLoggerFactory
            .getLogger(SetupIOTest.class);

    public SetupIOTest() {
        // Set logging to debug
        //Logger.getRootLogger().setLevel(Level.DEBUG);
    }

    public void test010_save() throws SAXException,
            UnsupportedEncodingException {
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final PositionSet pos = PositionSet.allOut();
        SetupIO.save(bout, pos, null, new SlideStraight(), null);
        final byte[] data = bout.toByteArray();
        log.info(new String(data, "UTF-8"));
        assertEquals(2808, data.length);
    }

    public void test020_save_load() throws SAXException, IOException {
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final PositionSet pos = PositionSet.allOut();
        SetupIO.save(bout, pos, null, new SlideStraight(), null);
        final byte[] data = bout.toByteArray();
        assertEquals(2808, data.length);

        SetupBuilder sb = SetupIO.load(new ByteArrayInputStream(data));
        assertEquals(SlideStraight.class, sb.getSlide().getClass());
        assertEquals(CollissionSpin.class, sb.getSlide().getColl().getClass());
        assertEquals(PositionSet.class, sb.getPos().getClass());
        assertEquals(SpeedSet.class, sb.getSpeed().getClass());
        log.info("");
    }
}