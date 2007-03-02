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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A very leightweight, quick and simple xml serializer implementing a SAX
 * content-handler to <b>write to a stream </b> or writer. Can handle changing
 * default namespaces. Checks well-formedness. Does not support smart namespace
 * prefix handling - which it should.
 * 
 * <p>
 * For Namespace support evtl. use
 * http://www.jcurl.org/api/com/megginson/sax/XMLWriter.html
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:XmlSimpleWriter.java 8046 2006-02-27 17:03:05Z rohrmoser $
 */
public class XmlSimpleWriter extends DefaultHandler {

    private static final Log log = JCLoggerFactory.getLogger(XmlSimpleWriter.class);

    private static void checkAttName(final String qName) throws SAXException {
        if (qName == null || "".equals(qName))
            throw new SAXException("empty element name not allowed!");
    }

    /**
     * Check if qName contains only valid characters.
     * 
     * @see <a
     *      href="http://edition-w3c.de/TR/2000/REC-xml-20001006/#NT-Name">XML
     *      Spec </a>
     * @param qName
     * @throws SAXException
     */
    private static void checkQName(final String qName) throws SAXException {
        if (qName == null || "".equals(qName))
            throw new SAXException("empty element name not allowed!");
    }

    public static String getCurrentXPath(final List elemStack) {
        final StringBuffer buf = new StringBuffer();
        for (Iterator it = elemStack.iterator(); it.hasNext();)
            buf.append('/').append((String) it.next());
        return buf.toString();
    }

    private static final void writeEncoded(final char ch, final Writer target)
            throws SAXException {
        switch (ch) {
        case '&':
            writePlain("&amp;", target);
            break;
        case '<':
            writePlain("&lt;", target);
            break;
        case '>':
            writePlain("&gt;", target);
            break;
        case '"':
            writePlain("&quot;", target);
            break;
        case '\'':
            writePlain("&apos;", target);
            break;
        default:
            writePlain(ch, target);
            break;
        }
    }

    public static void writeEncoded(final String src, final Writer target)
            throws SAXException {
        final int len = src.length();
        for (int i = 0; i < len; i++)
            writeEncoded(src.charAt(i), target);
    }

    private static final void writePlain(final char s, final Writer target)
            throws SAXException {
        try {
            target.write(s);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    private static final void writePlain(final String src, final Writer target)
            throws SAXException {
        try {
            target.write(src);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    private final Stack elemStack = new Stack();

    private final String encoding;

    // private Locator locator = null;

    private String pendingPrefixStart = null;

    private String pendingUriStart = null;

    private final Writer target;

    /**
     * Create an xml serializer to a stream + encoding.
     * 
     * @param dst
     *            stream to write to
     * @param encoding
     * @param indent
     *            simple pretty printing
     * @throws UnsupportedEncodingException
     */
    public XmlSimpleWriter(final OutputStream dst, final String encoding,
            final boolean indent) throws UnsupportedEncodingException {
        target = new OutputStreamWriter(dst, encoding);
        this.encoding = encoding;
    }

    /**
     * Create an xml serializer to a writer.
     * 
     * @param target
     */
    public XmlSimpleWriter(final Writer target) {
        this.target = target;
        encoding = null;
    }

    /**
     * Write a bunch of characters (encoded) to the stream.
     * 
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(final char[] ch, final int start, final int length)
            throws SAXException {
        for (int i = start; i < start + length; i++)
            writeEncoded(ch[i], target);
    }

    /**
     * flush the stream.
     * 
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() throws SAXException {
        if (elemStack.size() != 0)
            throw new SAXException("Unclosed elements pending.");
        try {
            target.flush();
        } catch (IOException e) {
            throw new SAXException("Error flushing xml writer.", e);
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
        final String recent = (String) elemStack.pop();
        if (qName == null && localName != null)
            qName = localName;
        if (!recent.equals(qName))
            throw new SAXException("Cannot write non-wellformed stuff.");
        writePlain("</", target);
        writePlain(qName, target);
        writePlain('>', target);
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
     * Make errors catastrophic and throw the given Exception
     */
    public void error(final SAXParseException e) throws SAXParseException {
        log.error("parse exception", e);
        throw e;
    }

    /**
     * Make errors catastrophic and throw the given Exception
     */
    public void fatalError(final SAXParseException e) throws SAXParseException {
        log.error("parse exception", e);
        throw e;
    }

    public Writer getWriter() {
        return target;
    }

    /**
     * Write whitespace that could be omitted (and is if in "indent" mode).
     * 
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
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
        writePlain("<?", this.target);
        writePlain(target, this.target);
        if (data != null) {
            writePlain(' ', this.target);
            writePlain(data, this.target);
        }
        writePlain("?>", this.target);
    }

    /**
     * Not supported.
     * 
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    public void setDocumentLocator(final Locator locator) {
        ;// this.locator = locator;
    }

    /**
     * Not supported.
     * 
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    public void skippedEntity(String name) throws SAXException {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Reset the internal state and write the xml processing instruction
     * (including encoding if known).
     * 
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException {
        elemStack.clear();
        String data = "version=\"1.0\"";
        if (encoding != null)
            data += " encoding=\"" + encoding + "\"";
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
        if (qName == null && localName != null)
            qName = localName;
        checkQName(qName);
        writePlain('<', target);
        writePlain(qName, target);
        // handle namespace changes
        if (pendingPrefixStart != null || pendingUriStart != null) {
            writePlain(" xmlns", target);
            if (pendingPrefixStart != null) {
                writePlain(':', target);
                writePlain(pendingPrefixStart, target);
            }
            writePlain("=\"", target);
            if (pendingUriStart != null)
                writeEncoded(pendingUriStart, target);
            writePlain("\"", target);
            pendingPrefixStart = pendingUriStart = null;
        }
        // TODO keep track of changing namespace prefixes
        // TODO handle attribute namespaces right

        if (atts != null) {
            int len = atts.getLength();
            for (int i = 0; i < len; i++) {
                final String val = atts.getValue(i);
                String name = atts.getQName(i);
                if (name == null && atts.getLocalName(i) != null)
                    name = atts.getLocalName(i);
                checkAttName(name);
                if (val == null)
                    log.warn("Attribute [" + getCurrentXPath(elemStack) + "/"
                            + qName + "/@" + name
                            + "] is skipped because it has the value null");
                else {
                    writePlain(' ', target);
                    writePlain(name, target);
                    writePlain("=\"", target);
                    writeEncoded(val, target);
                    writePlain("\"", target);
                }
            }
        }
        writePlain(">", target);
        elemStack.push(qName);
    }

    /**
     * Begin a prefix mapping. Only emtpy prefixes are supported right now.
     * 
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
     *      java.lang.String)
     */
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        if ("xml".equals(prefix)
                && "http://www.w3.org/XML/1998/namespace".equals(uri))
            return;
        if ("xmlns".equals(prefix)
                && "http://www.w3.org/2000/xmlns/".equals(uri))
            return;
        if (prefix != null)
            throw new UnsupportedOperationException("Not implemented yet.");
        // TODO allow multiple changes at once
        pendingPrefixStart = prefix;
        pendingUriStart = uri;
    }

    /**
     * Do nothing.
     */
    public void warning(final SAXParseException e) {
        log.debug("parse exception", e);
    }
}