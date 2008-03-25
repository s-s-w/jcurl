/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @see XmlSimpleWriter
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:XmlSimpleWriterTest.java 8046 2006-02-27 17:03:05Z rohrmoser $
 * 
 */
public class XmlSimpleWriterTest extends TestCase {

    private static StringBuilder xmlEncode(final String s,
            final StringBuilder buf0) {
        final StringBuilder buf = buf0 != null ? buf0 : new StringBuilder(s
                .length() * 2);
        final int len = s.length();
        for (int i = 0; i < len; i++) {
            final char ch = s.charAt(i);
            switch (ch) {
            case '&':
                buf.append("&amp;");
                break;
            case '<':
                buf.append("&lt;");
                break;
            case '>':
                buf.append("&gt;");
                break;
            case '"':
                buf.append("&quot;");
                break;
            case '\'':
                buf.append("&apos;");
                break;
            default:
                buf.append(ch);
                break;
            }
        }
        return buf;
    }

    private final String UGLY = "&<>'\" ]]> äüö";

    /**
     * @param stream
     * @param encoding
     * @param buf
     * @return -
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private StringBuilder dumpStream(final InputStream stream,
            final String encoding, StringBuilder buf)
            throws UnsupportedEncodingException, IOException {
        if (buf == null)
            buf = new StringBuilder();
        final BufferedReader read = new BufferedReader(new InputStreamReader(
                stream, encoding));
        for (;;) {
            final String line = read.readLine();
            if (line == null)
                break;
            buf.append(line).append('\n');
        }
        return buf;
    }

    public void test001_XMLReader() throws ParserConfigurationException,
            IOException, SAXException {
        final StringWriter writ = new StringWriter(1000);
        final SAXParserFactory fact = SAXParserFactory.newInstance();
        final XMLReader reader = fact.newSAXParser().getXMLReader();
        final DefaultHandler handler = new XmlSimpleWriter(writ);
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);

        String xml = "<?xml version='1.0'?><root />";
        reader.parse(new InputSource(new StringReader(xml)));
        assertEquals("<?xml version=\"1.0\"?><root></root>", writ.toString());

        writ.getBuffer().setLength(0);
        xml = "<?xml version='1.0'?><root att='val'/>";
        reader.parse(new InputSource(new StringReader(xml)));
        assertEquals("<?xml version=\"1.0\"?><root att=\"val\"></root>", writ
                .toString());

        writ.getBuffer().setLength(0);
        xml = "<?xml version=\"1.0\"?><root att=\"val\"></root>";
        reader.parse(new InputSource(new StringReader(xml)));
        assertEquals(xml, writ.toString());

        writ.getBuffer().setLength(0);
        xml = "<?xml version=\"1.0\"?><root xmlns=\"a\" att=\"val\"></root>";
        reader.parse(new InputSource(new StringReader(xml)));
        assertEquals(xml, writ.toString());

        writ.getBuffer().setLength(0);
        xml = "<?xml version=\"1.0\"?><root xmlns=\"a\" att=\"root\"><sub xmlns=\"b\" att=\"sub\"></sub></root>";
        reader.parse(new InputSource(new StringReader(xml)));
        assertEquals(xml, writ.toString());

        writ.getBuffer().setLength(0);
        xml = "<?xml version=\"1.0\"?><root><![CDATA[some ugly &<>\"' characters?]]></root>";
        reader.parse(new InputSource(new StringReader(xml)));
        assertEquals(
                "<?xml version=\"1.0\"?><root>some ugly &amp;&lt;&gt;&quot;&apos; characters?</root>",
                writ.toString());
    }

    public void test002_SAXParser() throws ParserConfigurationException,
            IOException, SAXException {
        final StringWriter writ = new StringWriter(1000);
        final SAXParserFactory fact = SAXParserFactory.newInstance();
        final SAXParser reader = fact.newSAXParser();
        final DefaultHandler handler = new XmlSimpleWriter(writ);

        String xml = "<?xml version='1.0'?><root />";
        reader.parse(new InputSource(new StringReader(xml)), handler);
        assertEquals("<?xml version=\"1.0\"?><root></root>", writ.toString());

        writ.getBuffer().setLength(0);
        xml = "<?xml version='1.0'?><root att='val'/>";
        reader.parse(new InputSource(new StringReader(xml)), handler);
        assertEquals("<?xml version=\"1.0\"?><root att=\"val\"></root>", writ
                .toString());

        writ.getBuffer().setLength(0);
        xml = "<?xml version=\"1.0\"?><root att=\"val\"></root>";
        reader.parse(new InputSource(new StringReader(xml)), handler);
        assertEquals(xml, writ.toString());

        writ.getBuffer().setLength(0);
        xml = "<?xml version=\"1.0\"?><root xmlns=\"a\" att=\"val\"></root>";
        reader.parse(new InputSource(new StringReader(xml)), handler);
        assertEquals(xml, writ.toString());

        writ.getBuffer().setLength(0);
        xml = "<?xml version=\"1.0\"?><root xmlns=\"a\" att=\"root\"><sub xmlns=\"b\" att=\"sub\"></sub></root>";
        reader.parse(new InputSource(new StringReader(xml)), handler);
        assertEquals(xml, writ.toString());

        writ.getBuffer().setLength(0);
        xml = "<?xml version=\"1.0\"?><root><![CDATA[some ugly &<>\"' characters?]]></root>";
        reader.parse(new InputSource(new StringReader(xml)), handler);
        assertEquals(
                "<?xml version=\"1.0\"?><root>some ugly &amp;&lt;&gt;&quot;&apos; characters?</root>",
                writ.toString());
    }

    public void test008_TrivialDefaultEncoding() throws SAXException {
        final StringBuilder exp = new StringBuilder();
        final StringWriter writ = new StringWriter();
        final XmlSimpleWriter dst = new XmlSimpleWriter(writ);

        dst.startDocument();
        dst.startElement(null, null, "root", null);
        dst.characters(UGLY.toCharArray(), 0, UGLY.length());
        dst.endElement(null, null, "root");
        dst.endDocument();

        exp.append("<?xml version=\"1.0\"?>");
        exp.append("<root>");
        xmlEncode(UGLY, exp);
        exp.append("</root>");

        final String res = writ.getBuffer().toString();
        assertEquals(exp.toString(), res);
    }

    public void test009_TrivialEncodingGiven() throws SAXException, IOException {
        final String[] encodings = { "UTF-8", "ISO-8859-1" };
        for (int encIdx = encodings.length - 1; encIdx >= 0; encIdx--) {
            final String enc = encodings[encIdx];
            final StringBuilder exp = new StringBuilder();
            final ByteArrayOutputStream outStr = new ByteArrayOutputStream();
            final XmlSimpleWriter dst = new XmlSimpleWriter(outStr, enc, false);

            dst.startDocument();
            dst.startElement(null, null, "root", null);
            dst.characters(UGLY.toCharArray(), 0, UGLY.length());
            dst.endElement(null, null, "root");
            dst.endDocument();

            exp.append("<?xml version=\"1.0\" encoding=\"" + enc + "\"?>");
            exp.append("<root>");
            xmlEncode(UGLY, exp);
            exp.append("</root>\n");

            final InputStream iStream = new ByteArrayInputStream(outStr
                    .toByteArray());
            final StringBuilder res = dumpStream(iStream, enc, null);
            assertEquals(exp.toString(), res.toString());
        }
    }

    public void test010_NoNamespace() throws IOException, SAXException {
        final StringBuilder exp = new StringBuilder();
        final StringWriter writ = new StringWriter();
        final XmlSimpleWriter dst = new XmlSimpleWriter(writ);

        dst.startDocument();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(null, null, "att1", null, "Attribute 1" + UGLY);
        dst.startElement(null, null, "root", atts);

        atts = new AttributesImpl();
        atts.addAttribute(null, null, "att1", null, "Attribute 1" + UGLY);
        dst.startElement(null, null, "sub1", atts);
        final String txt = UGLY;
        dst.characters(txt.toCharArray(), 0, txt.length());
        dst.endElement(null, null, "sub1");

        // atts = new AttributesImpl();
        // atts.addAttribute(null, null, "att1", null, "Attribute 1" + ugly);
        // dst.startElement(null, null, "sub2", atts);
        // txt = "Attribute 1" + ugly;
        // dst.characters(txt.toCharArray(), 0, txt.length());
        // dst.endElement(null, null, "sub2");

        dst.endElement(null, null, "root");
        dst.endDocument();

        exp.append("<?xml version=\"1.0\"?>");
        exp.append("<root att1=\"Attribute 1");
        xmlEncode(UGLY, exp);
        exp.append("\">");

        exp.append("<sub1 att1=\"Attribute 1");
        xmlEncode(UGLY, exp);
        exp.append("\">");
        xmlEncode(UGLY, exp);
        exp.append("</sub1>");

        // exp.append("<sub2 att1=\"Attribute 1");
        // xmlEncode(ugly, exp);
        // exp.append("\"><![CDATA[ ");
        // exp.append(ugly);
        // exp.append(" ]]></sub2>");

        exp.append("</root>");

        final String res = writ.getBuffer().toString();
        assertEquals(exp.toString(), res);
    }

    public void test015_NonWellFormed() throws SAXException {
        final XmlSimpleWriter dst = new XmlSimpleWriter(new StringWriter());

        dst.startDocument();
        dst.startElement(null, null, "root", null);
        try {
            dst.endDocument();
            fail("Exception expected");
        } catch (final SAXException e) {
        }

        dst.startDocument();
        dst.startElement(null, null, "root", null);
        try {
            dst.endElement(null, null, "other");
            fail("Exception expected");
        } catch (final SAXException e) {
        }

        dst.startDocument();
        dst.startElement(null, null, "root", null);
        dst.startElement(null, null, "sub1", null);
        dst.endElement(null, null, "sub1");
        dst.startElement(null, null, "sub2", null);
        try {
            dst.endElement(null, null, "other");
            fail("Exception expected");
        } catch (final SAXException e) {
        }
    }

    public void test030_DefaultNamespaces() throws SAXException {
        final String NS1 = "myNamespace1";
        final String NS2 = "myOtherNamespace2";
        final StringBuilder exp = new StringBuilder();
        final StringWriter writ = new StringWriter();
        final XmlSimpleWriter dst = new XmlSimpleWriter(writ);

        dst.startDocument();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(null, null, "att1", null, "Attribute 1" + UGLY);
        dst.startPrefixMapping(null, NS1);
        dst.startElement(NS1, null, "root", atts);

        atts = new AttributesImpl();
        atts.addAttribute(null, null, "att1", null, "Attribute 1" + UGLY);
        dst.startPrefixMapping(null, NS2);
        dst.startElement(NS2, null, "sub1", atts);
        dst.characters(UGLY.toCharArray(), 0, UGLY.length());
        dst.endElement(NS2, null, "sub1");
        dst.endPrefixMapping(null);

        dst.startElement(NS1, null, "sub2", atts);
        dst.characters(UGLY.toCharArray(), 0, UGLY.length());
        dst.endElement(NS1, null, "sub2");

        // atts = new AttributesImpl();
        // atts.addAttribute(null, null, "att1", null, "Attribute 1" + UGLY);
        // dst.startElement(null, null, "sub2", atts);
        // txt = "Attribute 1" + UGLY;
        // dst.characters(txt.toCharArray(), 0, txt.length());
        // dst.endElement(null, null, "sub2");

        dst.endElement(NS1, null, "root");
        dst.endDocument();

        exp.append("<?xml version=\"1.0\"?>");
        exp.append("<root xmlns=\"" + NS1 + "\" att1=\"Attribute 1");
        xmlEncode(UGLY, exp);
        exp.append("\">");

        exp.append("<sub1 xmlns=\"" + NS2 + "\" att1=\"Attribute 1");
        xmlEncode(UGLY, exp);
        exp.append("\">");
        xmlEncode(UGLY, exp);
        exp.append("</sub1>");

        exp.append("<sub2 att1=\"Attribute 1");
        xmlEncode(UGLY, exp);
        exp.append("\">");
        xmlEncode(UGLY, exp);
        exp.append("</sub2>");

        exp.append("</root>");

        final String res = writ.getBuffer().toString();
        assertNotNull(res);
        assertEquals(exp.toString(), res);
    }

    public void test040_PrefixedNamespaces() throws SAXException {
        final String UGLY = "&<>'\"";
        final String NS1 = "myNamespace1";
        final String NS2 = "myOtherNamespace2";
        final String P1 = "p1";
        final String P2 = "p2";
        final StringBuilder exp = new StringBuilder();
        final StringWriter writ = new StringWriter();
        final XmlSimpleWriter dst = new XmlSimpleWriter(writ);

        dst.startDocument();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(NS1, "att1", P1 + ":" + "att1", null, "Attribute 1"
                + UGLY);
        atts.addAttribute(NS2, "att2", P2 + ":" + "att2", null, "Attribute 2"
                + UGLY);
        atts.addAttribute(null, null, "att3", null, "Attribute 3" + UGLY);
        dst.startElement(NS1, "root", P1 + ":" + "root", atts);

        atts = new AttributesImpl();
        atts.addAttribute(NS1, "att1", P1 + ":" + "att1", null, "Attribute 1"
                + UGLY);
        atts.addAttribute(NS2, "att2", P2 + ":" + "att2", null, "Attribute 2"
                + UGLY);
        atts.addAttribute(null, null, "att3", null, "Attribute 3" + UGLY);
        dst.startElement(NS1, "sub1", P1 + ":" + "sub1", atts);
        dst.characters(UGLY.toCharArray(), 0, UGLY.length());
        dst.endElement(NS1, "sub1", P1 + ":" + "sub1");

        // atts = new AttributesImpl();
        // atts.addAttribute(null, null, "att1", null, "Attribute 1" + UGLY);
        // dst.startElement(null, null, "sub2", atts);
        // txt = "Attribute 1" + UGLY;
        // dst.characters(txt.toCharArray(), 0, txt.length());
        // dst.endElement(null, null, "sub2");

        dst.endElement(NS1, "root", P1 + ":" + "root");
        dst.endDocument();

        exp.append("<?xml version=\"1.0\"?>\n");
        exp.append("<root att1=\"Attribute 1");
        xmlEncode(UGLY, exp);
        exp.append("\">");

        exp.append("<sub1 att1=\"Attribute 1");
        xmlEncode(UGLY, exp);
        exp.append("\">");
        xmlEncode(UGLY, exp);
        exp.append("</sub1>");

        // exp.append("<sub2 att1=\"Attribute 1");
        // xmlEncode(UGLY, exp);
        // exp.append("\"><![CDATA[ ");
        // exp.append(UGLY);
        // exp.append(" ]]></sub2>");

        exp.append("</root>\n");

        final String res = writ.getBuffer().toString();
        // assertEquals(exp.toString(), res);
        assertNotNull(res);
    }
}