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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.zip.GZIPOutputStream;

import jcurl.model.PositionSet;
import jcurl.model.Rock;
import jcurl.model.RockSet;
import jcurl.model.SpeedSet;
import jcurl.sim.core.SlideStrategy;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SetupSaxSer {

    private static final String NS = null;

    private final XmlSerializer xml;

    public SetupSaxSer(final File dst) throws IOException {
        OutputStream o = new FileOutputStream(dst, false);
        if (dst.getName().endsWith("z"))
            o = new GZIPOutputStream(o);
        try {
            this.xml = new XmlSerializer(o, "UTF-8", false);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public SetupSaxSer(final OutputStream dst) {
        try {
            this.xml = new XmlSerializer(dst, "UTF-8", false);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public SetupSaxSer(final Writer dst) {
        this.xml = new XmlSerializer(dst, false);
    }

    public SetupSaxSer(final XmlSerializer dst) {
        this.xml = dst;
    }

    void internal(final PositionSet pos) throws SAXException {
        xml.startElement(NS, null, "positions", null);
        for (int i = RockSet.ROCKS_PER_COLOR - 1; i >= 0; i--) {
            internalLoc(pos.getDark(i), i, true);
            internalLoc(pos.getLight(i), i, false);
        }
        xml.endElement(NS, null, "positions");
    }

    void internal(final SlideStrategy slid) throws SAXException {
        xml.startElement(NS, null, "collission", null);
        {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(NS, null, "val", null, "0.5");
            atts.addAttribute(NS, null, "dim", null, Dim.METER.toString());
            xml.startElement(NS, null, "loss", atts);
            xml.endElement(NS, null, "loss");
            atts = new AttributesImpl();
            atts.addAttribute(NS, null, "name", null,
                    "jcurl.sim.model.CollissionSpin");
            xml.startElement(NS, null, "model", atts);
            {
                atts = new AttributesImpl();
                atts.addAttribute(NS, null, "name", null, "friction");
                atts.addAttribute(NS, null, "val", null, "0.5");
                xml.startElement(NS, null, "param", atts);
                xml.endElement(NS, null, "param");
            }
            xml.endElement(NS, null, "model");
        }
        xml.endElement(NS, null, "collission");
        xml.startElement(NS, null, "ice", null);
        {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(NS, null, "val", null, "24");
            atts.addAttribute(NS, null, "dim", null, "sht");
            xml.startElement(NS, null, "drawtotee", atts);
            xml.endElement(NS, null, "drawtotee");
            atts = new AttributesImpl();
            atts.addAttribute(NS, null, "val", null, "2");
            atts.addAttribute(NS, null, "dim", null, "m");
            xml.startElement(NS, null, "curl", atts);
            xml.endElement(NS, null, "curl");
            atts = new AttributesImpl();
            atts.addAttribute(NS, null, "name", null,
                    "jcurl.sim.model.SlideStraight");
            xml.startElement(NS, null, "model", atts);
            xml.endElement(NS, null, "model");
        }
        xml.endElement(NS, null, "ice");
    }

    void internal(final SpeedSet pos) throws SAXException {
        xml.startElement(NS, null, "speeds", null);
        for (int i = RockSet.ROCKS_PER_COLOR - 1; i >= 0; i--) {
            internalSpeed(pos.getDark(i), i, true);
            internalSpeed(pos.getLight(i), i, false);
        }
        xml.endElement(NS, null, "speeds");
    }

    /**
     * @param r
     * @param i
     * @param isDark
     * @throws SAXException
     */
    void internalLoc(final Rock r, int i, final boolean isDark)
            throws SAXException {
        final Dim len = Dim.FOOT;
        final Dim angle = Dim.DEGREE;

        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(NS, null, "color", null, isDark ? "dark" : "light");
        atts.addAttribute(NS, null, "no", null, Integer.toString(1 + i));
        xml.startElement(NS, null, "rock", atts);

        atts = new AttributesImpl();
        final DimVal x = new DimVal(r.getX(), Dim.METER).to(len);
        atts.addAttribute(NS, null, "val", null, Double.toString(x.val));
        atts.addAttribute(NS, null, "dim", null, x.dim.toString());
        xml.startElement(NS, null, "x", atts);
        xml.endElement(NS, null, "x");

        atts = new AttributesImpl();
        final DimVal y = new DimVal(r.getY(), Dim.METER).to(len);
        atts.addAttribute(NS, null, "val", null, Double.toString(y.val));
        atts.addAttribute(NS, null, "dim", null, y.dim.toString());
        xml.startElement(NS, null, "y", atts);
        xml.endElement(NS, null, "y");

        atts = new AttributesImpl();
        final DimVal a = new DimVal(r.getY(), Dim.RADIANT).to(angle);
        atts.addAttribute(NS, null, "val", null, Double.toString(a.val));
        atts.addAttribute(NS, null, "dim", null, a.dim.toString());
        xml.startElement(NS, null, "a", atts);
        xml.endElement(NS, null, "a");

        xml.endElement(NS, null, "rock");
    }

    /**
     * @param r
     * @param i
     * @param isDark
     * @throws SAXException
     */
    void internalSpeed(final Rock r, int i, final boolean isDark)
            throws SAXException {
        final Dim len = Dim.METER_PER_SEC;
        final Dim angle = Dim.HERTZ;

        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(NS, null, "color", null, isDark ? "dark" : "light");
        atts.addAttribute(NS, null, "no", null, Integer.toString(1 + i));
        xml.startElement(NS, null, "rock", atts);

        atts = new AttributesImpl();
        final DimVal x = new DimVal(r.getX(), Dim.METER_PER_SEC).to(len);
        atts.addAttribute(NS, null, "val", null, Double.toString(x.val));
        atts.addAttribute(NS, null, "dim", null, x.dim.toString());
        xml.startElement(NS, null, "x", atts);
        xml.endElement(NS, null, "x");

        atts = new AttributesImpl();
        final DimVal y = new DimVal(r.getY(), Dim.METER_PER_SEC).to(len);
        atts.addAttribute(NS, null, "val", null, Double.toString(y.val));
        atts.addAttribute(NS, null, "dim", null, y.dim.toString());
        xml.startElement(NS, null, "y", atts);
        xml.endElement(NS, null, "y");

        atts = new AttributesImpl();
        final DimVal a = new DimVal(r.getY(), Dim.RAD_PER_SEC).to(angle);
        atts.addAttribute(NS, null, "val", null, Double.toString(a.val));
        atts.addAttribute(NS, null, "dim", null, a.dim.toString());
        xml.startElement(NS, null, "spin", atts);
        xml.endElement(NS, null, "spin");

        xml.endElement(NS, null, "rock");
    }

    public void write(final PositionSet pos) throws SAXException {
        xml.startDocument();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(NS, null, "xmlns", null,
                "http://jcurl.berlios.de/schema/setup/2005/1.0");
        xml.startElement(NS, null, "jcurl", atts);
        xml.startElement(NS, null, "setup", null);
        xml.startElement(NS, null, "meta", null);
        xml.startElement(NS, null, "event", null);
        xml.characters("???");
        xml.endElement(NS, null, "event");
        xml.startElement(NS, null, "game", null);
        xml.characters("???");
        xml.endElement(NS, null, "game");
        xml.endElement(NS, null, "meta");
        internal(pos);
        xml.endElement(NS, null, "setup");
        xml.endElement(NS, null, "jcurl");
        xml.endDocument();
    }

    public void write(final SetupBuilder setup) throws SAXException {
        xml.startDocument();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(NS, null, "xmlns", null,
                "http://jcurl.berlios.de/schema/setup/2005/1.0");
        xml.startElement(NS, null, "jcurl", atts);
        xml.startElement(NS, null, "setup", null);
        xml.startElement(NS, null, "meta", null);
        xml.startElement(NS, null, "event", null);
        xml.characters("???");
        xml.endElement(NS, null, "event");
        xml.startElement(NS, null, "game", null);
        xml.characters("???");
        xml.endElement(NS, null, "game");
        xml.endElement(NS, null, "meta");
        internal(setup.getSlide());
        internal(setup.getPos());
        internal(setup.getSpeed());
        xml.endElement(NS, null, "setup");
        xml.endElement(NS, null, "jcurl");
        xml.endDocument();
    }
}