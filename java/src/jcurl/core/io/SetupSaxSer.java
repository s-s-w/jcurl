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
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;

import jcurl.model.PositionSet;
import jcurl.model.Rock;
import jcurl.model.RockSet;
import jcurl.model.SpeedSet;
import jcurl.sim.core.ModelBase;
import jcurl.sim.core.SlideStrategy;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SetupSaxSer {

    private static final String NS = null;

    private static ContentHandler getCH(File dst) throws IOException {
        OutputStream o = new FileOutputStream(dst, false);
        if (dst.getName().endsWith("z"))
            o = new GZIPOutputStream(o);
        try {
            return new XmlSerializer(o, "UTF-8", false);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static ContentHandler getCH(OutputStream dst) {
        try {
            return new XmlSerializer(dst, "UTF-8", false);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private final ContentHandler xml;

    public SetupSaxSer(final ContentHandler xml) {
        this.xml = xml;
    }

    public SetupSaxSer(final File dst) throws IOException {
        this(getCH(dst));
    }

    public SetupSaxSer(final OutputStream dst) {
        this(getCH(dst));
    }

    public SetupSaxSer(final Writer dst) {
        this(new XmlSerializer(dst, false));
    }

    private void characters(final ContentHandler dst, final String str)
            throws SAXException {
        final char[] ch = str.toCharArray();
        dst.characters(ch, 0, ch.length);
    }

    void internal(final ModelBase model) throws SAXException {
        if (model == null)
            return;
        {
            final AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(NS, null, "engine", null, model.getClass()
                    .getName());
            xml.startElement(NS, null, "model", atts);
        }
        xml.startElement(NS, null, "description", null);
        characters(xml, model.description());
        xml.endElement(NS, null, "description");
        for (Iterator it = model.properties(); it.hasNext();) {
            final String key = (String) it.next();
            Object val = model.getProp(key);
            final AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(NS, null, "name", null, key);
            if (val instanceof DimVal) {
                final DimVal dv = (DimVal) val;
                atts.addAttribute(NS, null, "val", null, Double
                        .toString(dv.val));
                atts.addAttribute(NS, null, "dim", null, dv.dim.toString());
            } else
                atts.addAttribute(NS, null, "val", null, val.toString());
            xml.startElement(NS, null, "param", atts);
            xml.endElement(NS, null, "param");
        }
        xml.endElement(NS, null, "model");
    }

    void internal(final PositionSet pos) throws SAXException {
        if (pos == null)
            return;
        xml.startElement(NS, null, "positions", null);
        for (int i = RockSet.ROCKS_PER_COLOR - 1; i >= 0; i--) {
            internalLoc(pos.getDark(i), i, true);
            internalLoc(pos.getLight(i), i, false);
        }
        xml.endElement(NS, null, "positions");
    }

    void internal(final SpeedSet pos) throws SAXException {
        if (pos == null)
            return;
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
        internal(pos);
        xml.endElement(NS, null, "setup");
        xml.endElement(NS, null, "jcurl");
        xml.endDocument();
    }

    public void write(final PositionSet pos, final SpeedSet speed,
            final SlideStrategy slide) throws SAXException {
        xml.startDocument();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(NS, null, "xmlns", null,
                "http://jcurl.berlios.de/schema/setup/2005/1.0");
        xml.startElement(NS, null, "jcurl", atts);
        xml.startElement(NS, null, "setup", null);
        if (slide != null)
            internal(slide.getColl());
        internal(slide);
        internal(pos);
        internal(speed);
        xml.endElement(NS, null, "setup");
        xml.endElement(NS, null, "jcurl");
        xml.endDocument();
    }

    public void write(final SetupBuilder setup) throws SAXException {
        write(setup.getPos(), setup.getSpeed(), setup.getSlide());
    }
}