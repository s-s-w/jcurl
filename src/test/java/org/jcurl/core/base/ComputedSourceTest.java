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
package org.jcurl.core.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.jcurl.core.base.ComputedSource;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.SlideStraight;
import org.xml.sax.SAXException;

/**
 * @see org.jcurl.core.base.ComputedSource
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class ComputedSourceTest extends TestCase {

    public void test010_init() {
        final ComputedSource m = new ComputedSource();
        m.init(PositionSet.allHome(), new SpeedSet(), new SlideStraight(),
                new CollissionSpin());
        assertEquals(0, 0, m.getMinT());
        assertEquals(0, 0, m.getT());
        assertEquals(0, 35, m.getMaxT());
    }

    public void test100_save() throws SAXException,
            UnsupportedEncodingException {
        final ComputedSource m = new ComputedSource();
        m.init(PositionSet.allHome(), new SpeedSet(), new SlideStraight(),
                new CollissionSpin());

        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        m.saveStart(bout);
        final byte[] data = bout.toByteArray();
        assertEquals(2854, data.length);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><jcurl xmlns=\"http://jcurl.berlios.de/schema/setup/2005/1.0\"><setup><model engine=\"jcurl.sim.model.CollissionSpin\"><description>Collissions with spin</description><param name=\"friction rock-rock\" val=\"0.5\"></param><param name=\"loss\" val=\"0.0\" dim=\"J\"></param></model><model engine=\"jcurl.sim.model.SlideStraight\"><description>Straight movement</description><param name=\"curl\" val=\"1.0\" dim=\"m\"></param><param name=\"drawtotee\" val=\"25.0\" dim=\"sht\"></param></model><positions><rock color=\"dark\" no=\"8\"><x val=\"-5.199999946934657\" dim=\"ft\"></x><y val=\"121.80000465373041\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"8\"><x val=\"5.199999946934657\" dim=\"ft\"></x><y val=\"121.80000465373041\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"7\"><x val=\"-6.399999844433441\" dim=\"ft\"></x><y val=\"121.80000465373041\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"7\"><x val=\"6.399999844433441\" dim=\"ft\"></x><y val=\"121.80000465373041\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"6\"><x val=\"-5.199999946934657\" dim=\"ft\"></x><y val=\"123.00000728897534\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"6\"><x val=\"5.199999946934657\" dim=\"ft\"></x><y val=\"123.00000728897534\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"5\"><x val=\"-6.399999844433441\" dim=\"ft\"></x><y val=\"123.00000728897534\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"5\"><x val=\"6.399999844433441\" dim=\"ft\"></x><y val=\"123.00000728897534\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"4\"><x val=\"-5.199999946934657\" dim=\"ft\"></x><y val=\"124.1999974088093\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"4\"><x val=\"5.199999946934657\" dim=\"ft\"></x><y val=\"124.1999974088093\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"3\"><x val=\"-6.399999844433441\" dim=\"ft\"></x><y val=\"124.1999974088093\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"3\"><x val=\"6.399999844433441\" dim=\"ft\"></x><y val=\"124.1999974088093\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"2\"><x val=\"-5.199999946934657\" dim=\"ft\"></x><y val=\"125.40000004405424\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"2\"><x val=\"5.199999946934657\" dim=\"ft\"></x><y val=\"125.40000004405424\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"1\"><x val=\"-6.399999844433441\" dim=\"ft\"></x><y val=\"125.40000004405424\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"1\"><x val=\"6.399999844433441\" dim=\"ft\"></x><y val=\"125.40000004405424\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock></positions><speeds></speeds></setup></jcurl>",
                new String(data, "UTF-8"));
    }

    public void test110_save_load() throws SAXException, IOException {
        final ComputedSource m0 = new ComputedSource();
        m0.init(PositionSet.allHome(), new SpeedSet(), new SlideStraight(),
                new CollissionSpin());

        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final PositionSet pos = PositionSet.allOut();
        m0.saveStart(bout);
        final byte[] data = bout.toByteArray();
        assertEquals(2854, data.length);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><jcurl xmlns=\"http://jcurl.berlios.de/schema/setup/2005/1.0\"><setup><model engine=\"jcurl.sim.model.CollissionSpin\"><description>Collissions with spin</description><param name=\"friction rock-rock\" val=\"0.5\"></param><param name=\"loss\" val=\"0.0\" dim=\"J\"></param></model><model engine=\"jcurl.sim.model.SlideStraight\"><description>Straight movement</description><param name=\"curl\" val=\"1.0\" dim=\"m\"></param><param name=\"drawtotee\" val=\"25.0\" dim=\"sht\"></param></model><positions><rock color=\"dark\" no=\"8\"><x val=\"-5.199999946934657\" dim=\"ft\"></x><y val=\"121.80000465373041\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"8\"><x val=\"5.199999946934657\" dim=\"ft\"></x><y val=\"121.80000465373041\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"7\"><x val=\"-6.399999844433441\" dim=\"ft\"></x><y val=\"121.80000465373041\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"7\"><x val=\"6.399999844433441\" dim=\"ft\"></x><y val=\"121.80000465373041\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"6\"><x val=\"-5.199999946934657\" dim=\"ft\"></x><y val=\"123.00000728897534\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"6\"><x val=\"5.199999946934657\" dim=\"ft\"></x><y val=\"123.00000728897534\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"5\"><x val=\"-6.399999844433441\" dim=\"ft\"></x><y val=\"123.00000728897534\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"5\"><x val=\"6.399999844433441\" dim=\"ft\"></x><y val=\"123.00000728897534\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"4\"><x val=\"-5.199999946934657\" dim=\"ft\"></x><y val=\"124.1999974088093\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"4\"><x val=\"5.199999946934657\" dim=\"ft\"></x><y val=\"124.1999974088093\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"3\"><x val=\"-6.399999844433441\" dim=\"ft\"></x><y val=\"124.1999974088093\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"3\"><x val=\"6.399999844433441\" dim=\"ft\"></x><y val=\"124.1999974088093\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"2\"><x val=\"-5.199999946934657\" dim=\"ft\"></x><y val=\"125.40000004405424\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"2\"><x val=\"5.199999946934657\" dim=\"ft\"></x><y val=\"125.40000004405424\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"dark\" no=\"1\"><x val=\"-6.399999844433441\" dim=\"ft\"></x><y val=\"125.40000004405424\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock><rock color=\"light\" no=\"1\"><x val=\"6.399999844433441\" dim=\"ft\"></x><y val=\"125.40000004405424\" dim=\"ft\"></y><a val=\"0.0\" dim=\"deg\"></a></rock></positions><speeds></speeds></setup></jcurl>",
                new String(data, "UTF-8"));

        final ComputedSource m1 = new ComputedSource();
        m1.loadStart(new ByteArrayInputStream(data));
        assertEquals(SlideStraight.class, m1.getSlide().getClass());
        assertEquals(CollissionSpin.class, m1.getSlide().getColl().getClass());
        assertEquals(PositionSet.class, m1.getPos().getClass());
        assertEquals(SpeedSet.class, m1.getSpeed().getClass());
    }
}