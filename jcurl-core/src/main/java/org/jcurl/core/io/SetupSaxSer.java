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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

import org.jcurl.core.base.Collider;
import org.jcurl.core.base.PropModel;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.Curler;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.helpers.Dim;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.helpers.XmlSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:SetupSaxSer.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class SetupSaxSer {

    static final String NS = "http://www.jcurl.org/schemas/2006/basic";

    private static ContentHandler getCH(final File dst) throws IOException {
        OutputStream o = new FileOutputStream(dst, false);
        if (dst.getName().endsWith("z"))
            o = new GZIPOutputStream(o);
        try {
            return new XmlSerializer(o, "UTF-8", false);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static ContentHandler getCH(final OutputStream dst) {
        try {
            return new XmlSerializer(dst, "UTF-8", false);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part(final ContentHandler xml, final double val,
            final Dim src, final String label, final Dim dst)
            throws SAXException {
        final AttributesImpl atts = new AttributesImpl();
        final DimVal tmp = new DimVal(val, src).to(dst);
        atts.addAttribute(NS, null, "val", null, Double.toString(tmp.val));
        atts.addAttribute(NS, null, "dim", null, tmp.dim.toString());
        xml.startElement(NS, null, label, atts);
        xml.endElement(NS, null, label);
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

    void internal(final PropModel model) throws SAXException {
        if (model == null)
            return;
        {
            final AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(NS, null, "engine", null, model.getClass()
                    .getName());
            xml.startElement(NS, null, "model", atts);
        }
        for (final Entry<CharSequence, DimVal> element : model) {
            final AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(NS, null, "name", null, element.getKey()
                    .toString());
            {
                atts.addAttribute(NS, null, "val", null, Double
                        .toString(element.getValue().val));
                atts.addAttribute(NS, null, "dim", null, element.getValue().dim
                        .toString());
            }
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
    void internalLoc(final Rock r, final int i, final boolean isDark)
            throws SAXException {
        final AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(NS, null, "color", null, isDark ? "dark" : "light");
        atts.addAttribute(NS, null, "no", null, Integer.toString(1 + i));
        xml.startElement(NS, null, "rock", atts);

        part(xml, r.getX(), Dim.METER, "x", Dim.FOOT);
        part(xml, r.getY(), Dim.METER, "y", Dim.FOOT);
        part(xml, r.getZ(), Dim.RADIANT, "a", Dim.DEGREE);

        xml.endElement(NS, null, "rock");
    }

    /**
     * @param r
     * @param i
     * @param isDark
     * @throws SAXException
     */
    void internalSpeed(final Rock r, final int i, final boolean isDark)
            throws SAXException {
        if (!r.nonZero())
            return;
        final AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(NS, null, "color", null, isDark ? "dark" : "light");
        atts.addAttribute(NS, null, "no", null, Integer.toString(1 + i));
        xml.startElement(NS, null, "rock", atts);

        part(xml, r.getX(), Dim.METER_PER_SEC, "x", Dim.METER_PER_SEC);
        part(xml, r.getY(), Dim.METER_PER_SEC, "y", Dim.METER_PER_SEC);
        part(xml, r.getZ(), Dim.RAD_PER_SEC, "spin", Dim.HERTZ);

        xml.endElement(NS, null, "rock");
    }

    public void write(final PositionSet pos) throws SAXException {
        xml.startDocument();
        final AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(NS, null, "xmlns", null,
                "http://jcurl.berlios.de/schema/setup/2005/1.0");
        xml.startElement(NS, null, "jcurl", atts);
        xml.startElement(NS, null, "setup", null);
        this.internal(pos);
        xml.endElement(NS, null, "setup");
        xml.endElement(NS, null, "jcurl");
        xml.endDocument();
    }

    public void write(final PositionSet pos, final SpeedSet speed,
            final Collider coll, final Curler slide) throws SAXException {
        xml.startDocument();
        xml.startPrefixMapping(null, NS);
        final AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(NS, null, "xmlns", null,
                "http://jcurl.berlios.de/schema/setup/2005/1.0");
        xml.startElement(NS, null, "jcurl", atts);
        xml.startElement(NS, null, "setup", null);
        if (slide != null)
            throw new NotImplementedException();
        this.internal(coll);
        this.internal(slide);
        this.internal(pos);
        this.internal(speed);
        xml.endElement(NS, null, "setup");
        xml.endElement(NS, null, "jcurl");
        xml.endDocument();
    }
    //
    // public void write(final SetupBuilder setup) throws SAXException {
    // this.write(setup.getPos(), setup.getSpeed(), setup.getSlide());
    // }
}