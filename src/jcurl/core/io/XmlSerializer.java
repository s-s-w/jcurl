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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.xml.sax.SAXException;

import com.megginson.sax.XMLWriter;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class XmlSerializer extends XMLWriter {

    /**
     * @param stream
     * @param encoding
     * @param indent
     * @throws UnsupportedEncodingException
     */
    public XmlSerializer(OutputStream stream, String encoding, boolean indent)
            throws UnsupportedEncodingException {
        super(new OutputStreamWriter(stream, encoding));
    }

    /**
     * @param target
     * @param indent
     */
    public XmlSerializer(Writer target, boolean indent) {
        super(target);
    }

    /**
     * Write a bunch of characters (encoded) to the stream.
     * 
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(final char[] ch) throws SAXException {
        characters(ch, 0, ch.length);
    }

    /**
     * Write a bunch of characters (encoded) to the stream.
     * 
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(final String ch) throws SAXException {
        characters(ch.toCharArray());
    }

}