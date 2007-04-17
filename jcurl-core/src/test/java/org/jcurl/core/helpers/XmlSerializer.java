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
package org.jcurl.core.helpers;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:XmlSerializer.java 542 2007-04-12 00:12:53Z mrohrmoser $
 */
public class XmlSerializer extends XmlSimpleWriter {

    /**
     * @param stream
     * @param encoding
     * @param indent
     * @throws UnsupportedEncodingException
     */
    public XmlSerializer(final OutputStream stream, final String encoding,
            final boolean indent) throws UnsupportedEncodingException {
        super(new OutputStreamWriter(stream, encoding));
    }

    /**
     * @param target
     * @param indent
     */
    public XmlSerializer(final Writer target, final boolean indent) {
        super(target);
    }

    /**
     * Write a bunch of characters (encoded) to the stream.
     * 
     * @param ch
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     * @throws SAXException
     */
    public void characters(final char[] ch) throws SAXException {
        this.characters(ch, 0, ch.length);
    }

    /**
     * Write a bunch of characters (encoded) to the stream.
     * 
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     * @param ch
     * @throws SAXException
     */
    public void characters(final String ch) throws SAXException {
        this.characters(ch.toCharArray());
    }

}