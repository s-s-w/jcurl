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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Stack;

import jcurl.core.JCLoggerFactory;

import org.apache.ugli.ULogger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A very simple xml serializer implementing a SAX content-handler. Can handle
 * changing default namespaces. Checks well-formedness. Does not support smart
 * namespace prefix handling - which it should.
 * 
 * @see XmlSerializerTest
 * @author m
 */
public class XmlSerializer implements ContentHandler {
    /**
     * Helper class to make xml validation errors catastrophic.
     * 
     * @author mr
     * @version $Id: XmlSimpleWriter.java 92 2005-03-30 14:41:20Z mrohrmoser $
     */
    public static class ErrHandler implements ErrorHandler {
        private static final ULogger log = JCLoggerFactory
                .getLogger(ErrHandler.class);

        public void error(final SAXParseException e) throws SAXParseException {
            log.error("parse exception", e);
            throw e;
        }

        public void fatalError(final SAXParseException e)
                throws SAXParseException {
            log.error("parse exception", e);
            throw e;
        }

        public void warning(final SAXParseException e) {
            log.debug("parse exception", e);
        }
    }

    private static final char NEWLINE = '\n';

    private static final char TABULATOR = '\t';

    private final String encoding;

    private final boolean indent;

    private final Stack currentElement = new Stack();

    private String pendingPrefixStart = null;

    private String pendingUriStart = null;

    private final Writer target;

    public XmlSerializer(final OutputStream stream, final String encoding,
            final boolean indent) throws UnsupportedEncodingException {
        this.target = new OutputStreamWriter(stream, encoding);
        this.encoding = encoding;
        this.indent = indent;
    }

    public XmlSerializer(final Writer target, final boolean indent) {
        this.target = target;
        this.encoding = null;
        this.indent = indent;
    }

    /**
     * Write a bunch of characters (encoded) to the stream.
     * 
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(final char[] ch, final int start, final int length)
            throws SAXException {
        for (int i = start; i < start + length; i++)
            writeEncoded(ch[i]);
    }

    /**
     * Close the stream.
     * 
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() throws SAXException {
        if (currentElement.size() != 0)
            throw new SAXException("Unclosed elements pending.");
        try {
            target.close();
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    /**
     * Write the element end tag. Checks for a matching qname, but should check
     * the namespace as well.
     * 
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        final String recent = (String) currentElement.pop();
        if (!recent.equals(qName))
            throw new SAXException("Cannot write non-wellformed stuff.");
        if (indent) {
            writePlain(NEWLINE);
            for (int i = currentElement.size(); i > 0; i--)
                writePlain(TABULATOR);
        }
        writePlain("</");
        writePlain(qName);
        writePlain(">");
        if (indent)
            writePlain(NEWLINE);
    }

    /**
     * Should remove a prefix mapping for following child nodes. Does nothing
     * right now.
     * 
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(String prefix) throws SAXException {
        // TODO keep track of current mappings
        // TODO allow multiple changes at once
        ;
    }

    /**
     * Write whitespace that could be omitted (and is if in "indent" mode).
     * 
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        if (!indent)
            characters(ch, start, length);
    }

    /**
     * Write a processing instruction.
     * 
     * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String,
     *      java.lang.String)
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
        if (indent) {
            writePlain(NEWLINE);
            for (int i = currentElement.size(); i > 0; i--)
                writePlain(TABULATOR);
        }
        writePlain("<?");
        writePlain(target);
        if (data != null) {
            writePlain(' ');
            writePlain(data);
        }
        writePlain("?>");
    }

    /**
     * Not supported.
     * 
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    public void setDocumentLocator(Locator locator) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    /**
     * Not supported.
     * 
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    public void skippedEntity(String name) throws SAXException {
        throw new UnsupportedOperationException("Not implemented.");
    }

    /**
     * Reset the internal state and write the xml processing instruction
     * (including encoding if known).
     * 
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException {
        currentElement.clear();
        String data = "version=\"1.0\"";
        if (encoding != null) {
            data += " encoding=\"" + encoding + "\"";
        }
        processingInstruction("xml", data);
    }

    /**
     * Write an element start tag and it's attributes. Correctly handles
     * changing default namespaces.
     * 
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
        if (indent) {
            writePlain(NEWLINE);
            for (int i = currentElement.size(); i > 0; i--)
                writePlain(TABULATOR);
        }
        writePlain("<");
        writePlain(qName);
        // handle namespace changes
        if (pendingPrefixStart != null || pendingUriStart != null) {
            writePlain(" xmlns");
            if (pendingPrefixStart != null) {
                writePlain(':');
                writePlain(pendingPrefixStart);
            }
            writePlain("=\"");
            if (pendingUriStart != null) {
                writeEncoded(pendingUriStart);
            }
            writePlain("\"");
            pendingPrefixStart = pendingUriStart = null;
        }
        // TODO keep track of changing namespace prefixes
        // TODO handle attribute namespaces right

        if (atts != null) {
            int len = atts.getLength();
            for (int i = 0; i < len; i++) {
                writePlain(" ");
                writePlain(atts.getQName(i));
                writePlain("=\"");
                writeEncoded(atts.getValue(i));
                writePlain("\"");
            }
        }
        writePlain(">");
        currentElement.push(qName);
    }

    /**
     * Begin a prefix mapping. Only emtpy prefixes are supported right now.
     * 
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
     *      java.lang.String)
     */
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        if (prefix != null)
            throw new UnsupportedOperationException("Not implemented yet.");
        // TODO allow multiple changes at once
        pendingPrefixStart = prefix;
        pendingUriStart = uri;
    }

    private void writeEncoded(final char ch) throws SAXException {
        switch (ch) {
        case '&':
            writePlain("&amp;");
            break;
        case '<':
            writePlain("&lt;");
            break;
        case '>':
            writePlain("&gt;");
            break;
        case '"':
            writePlain("&quot;");
            break;
        case '\'':
            writePlain("&apos;");
            break;
        default:
            writePlain(ch);
            break;
        }
    }

    private void writeEncoded(final String s) throws SAXException {
        final int len = s.length();
        for (int i = 0; i < len; i++) {
            writeEncoded(s.charAt(i));
        }
    }

    private void writePlain(final char s) throws SAXException {
        try {
            target.write(s);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    private void writePlain(final String s) throws SAXException {
        try {
            target.write(s);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }
}