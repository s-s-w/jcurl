/*
 * Created on 14.11.2004
 *
 */
package jcurl.core.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * JUnit Test
 * 
 * @see jcurl.core.util.XmlSimpleWriter
 * @author m
 *  
 */
public class XmlSimpleWriterTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(XmlSimpleWriterTest.class);
    }

    private static String xmlEncode(final String s) {
        return xmlEncode(s, null).toString();
    }

    private static StringBuffer xmlEncode(final String s,
            final StringBuffer buf0) {
        final StringBuffer buf = buf0 != null ? buf0 : new StringBuffer(s
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
    private StringBuffer dumpStream(final InputStream stream,
            final String encoding, StringBuffer buf)
            throws UnsupportedEncodingException, IOException {
        if (buf == null)
            buf = new StringBuffer();
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

    public void test008_TrivialDefaultEncoding() throws SAXException {
        final StringBuffer exp = new StringBuffer();
        final StringWriter writ = new StringWriter();
        final XmlSimpleWriter.Content dst = new XmlSimpleWriter.Content(writ,
                false);

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
            final StringBuffer exp = new StringBuffer();
            final ByteArrayOutputStream outStr = new ByteArrayOutputStream();
            final XmlSimpleWriter.Content dst = new XmlSimpleWriter.Content(
                    outStr, enc, false);

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
            final StringBuffer res = dumpStream(iStream, enc, null);
            assertEquals(exp.toString(), res.toString());
        }
    }

    public void test010_NoNamespace() throws IOException, SAXException {
        final StringBuffer exp = new StringBuffer();
        final StringWriter writ = new StringWriter();
        final XmlSimpleWriter.Content dst = new XmlSimpleWriter.Content(writ,
                false);

        dst.startDocument();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(null, null, "att1", null, "Attribute 1" + UGLY);
        dst.startElement(null, null, "root", atts);

        atts = new AttributesImpl();
        atts.addAttribute(null, null, "att1", null, "Attribute 1" + UGLY);
        dst.startElement(null, null, "sub1", atts);
        String txt = UGLY;
        dst.characters(txt.toCharArray(), 0, txt.length());
        dst.endElement(null, null, "sub1");

        //		atts = new AttributesImpl();
        //		atts.addAttribute(null, null, "att1", null, "Attribute 1" + ugly);
        //		dst.startElement(null, null, "sub2", atts);
        //		txt = "Attribute 1" + ugly;
        //		dst.characters(txt.toCharArray(), 0, txt.length());
        //		dst.endElement(null, null, "sub2");

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

        //		exp.append("<sub2 att1=\"Attribute 1");
        //		xmlEncode(ugly, exp);
        //		exp.append("\"><![CDATA[ ");
        //		exp.append(ugly);
        //		exp.append(" ]]></sub2>");

        exp.append("</root>");

        final String res = writ.getBuffer().toString();
        assertEquals(exp.toString(), res);
    }

    public void test015_NonWellFormed() throws SAXException {
        XmlSimpleWriter.Content dst = new XmlSimpleWriter.Content(
                new StringWriter(), true);

        dst.startDocument();
        dst.startElement(null, null, "root", null);
        try {
            dst.endDocument();
            fail("Exception expected");
        } catch (SAXException e) {
        }

        dst.startDocument();
        dst.startElement(null, null, "root", null);
        try {
            dst.endElement(null, null, "other");
            fail("Exception expected");
        } catch (SAXException e) {
        }

        dst.startDocument();
        dst.startElement(null, null, "root", null);
        dst.startElement(null, null, "sub1", null);
        dst.endElement(null, null, "sub1");
        dst.startElement(null, null, "sub2", null);
        try {
            dst.endElement(null, null, "other");
            fail("Exception expected");
        } catch (SAXException e) {
        }
    }

    public void test030_DefaultNamespaces() throws SAXException {
        final String NS1 = "myNamespace1";
        final String NS2 = "myOtherNamespace2";
        final StringBuffer exp = new StringBuffer();
        final StringWriter writ = new StringWriter();
        final XmlSimpleWriter.Content dst = new XmlSimpleWriter.Content(writ,
                false);

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

        //		atts = new AttributesImpl();
        //		atts.addAttribute(null, null, "att1", null, "Attribute 1" + UGLY);
        //		dst.startElement(null, null, "sub2", atts);
        //		txt = "Attribute 1" + UGLY;
        //		dst.characters(txt.toCharArray(), 0, txt.length());
        //		dst.endElement(null, null, "sub2");

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
        final StringBuffer exp = new StringBuffer();
        final StringWriter writ = new StringWriter();
        final XmlSimpleWriter.Content dst = new XmlSimpleWriter.Content(writ,
                false);

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

        //		atts = new AttributesImpl();
        //		atts.addAttribute(null, null, "att1", null, "Attribute 1" + UGLY);
        //		dst.startElement(null, null, "sub2", atts);
        //		txt = "Attribute 1" + UGLY;
        //		dst.characters(txt.toCharArray(), 0, txt.length());
        //		dst.endElement(null, null, "sub2");

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

        //		exp.append("<sub2 att1=\"Attribute 1");
        //		xmlEncode(UGLY, exp);
        //		exp.append("\"><![CDATA[ ");
        //		exp.append(UGLY);
        //		exp.append(" ]]></sub2>");

        exp.append("</root>\n");

        final String res = writ.getBuffer().toString();
        //assertEquals(exp.toString(), res);
        assertNotNull(res);
    }
}