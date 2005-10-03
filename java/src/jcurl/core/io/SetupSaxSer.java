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

import jcurl.core.PositionSet;
import jcurl.core.Rock;
import jcurl.core.RockSet;

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
            internal(pos.getDark(i), i, true);
            internal(pos.getLight(i), i, false);
        }
        xml.endElement(NS, null, "positions");
    }

    /**
     * @param r
     * @param i
     * @param isDark
     * @throws SAXException
     */
    void internal(final Rock r, int i, final boolean isDark)
            throws SAXException {
        final Dim len = Dim.FOOT;

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
}