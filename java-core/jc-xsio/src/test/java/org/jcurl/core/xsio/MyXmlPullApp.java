package org.jcurl.core.xsio;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * http://www.xmlpull.org/v1/download/unpacked/src/java/samples/MyXmlPullApp.java
 * An example of an application that uses XMLPULL V1 API.
 * 
 * @author <a href="http://www.extreme.indiana.edu/~aslom/">Aleksander Slominski</a>
 */
public class MyXmlPullApp {

    public final static String SAMPLE_XML = "<?xml version=\"1.0\"?>\n" + "\n"
            + "<poem xmlns=\"http://www.megginson.com/ns/exp/poetry\">\n"
            + "<title>Roses are Red</title>\n" + "<l>Roses are red,</l>\n"
            + "<l>Violets are blue;</l>\n" + "<l>Sugar is sweet,</l>\n"
            + "<l>And I love you.</l>\n" + "</poem>";

    public static void main(final String args[]) throws XmlPullParserException,
            IOException {
        // XmlPullParserFactory factory = XmlPullParserFactory.newInstance(
        // System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
        // //factory.setNamespaceAware(true);
        // factory.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);

        final XmlPullParser xpp = new MXParser();
        xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        System.out.println("parser implementation class is " + xpp.getClass());

        final MyXmlPullApp app = new MyXmlPullApp();

        if (args.length == 0) {
            System.out.println("Parsing simple sample XML");// :\n"+
            // SAMPLE_XML);
            xpp.setInput(new StringReader(SAMPLE_XML));
            app.processDocument(xpp);
        } else
            for (final String element : args) {
                System.out.println("Parsing file: " + element);
                xpp.setInput(new FileReader(element));
                // xpp.setInput ( new InputStreamReader( new FileInputStream (
                // args [i] ) ) );
                // xpp.setInput ( new FileInputStream ( args [i] ), "UTF8" );
                app.processDocument(xpp);
            }
    }

    int holderForStartAndLength[] = new int[2];

    public void processDocument(final XmlPullParser xpp)
            throws XmlPullParserException, IOException {
        int eventType = xpp.getEventType();
        do {
            if (eventType == XmlPullParser.START_DOCUMENT)
                System.out.println("Start document");
            else if (eventType == XmlPullParser.END_DOCUMENT)
                System.out.println("End document");
            else if (eventType == XmlPullParser.START_TAG)
                processStartElement(xpp);
            else if (eventType == XmlPullParser.END_TAG)
                processEndElement(xpp);
            else if (eventType == XmlPullParser.TEXT)
                processText(xpp);
            eventType = xpp.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);
    }

    public void processEndElement(final XmlPullParser xpp) {
        final String name = xpp.getName();
        final String uri = xpp.getNamespace();
        if ("".equals(uri))
            System.out.println("End element: " + name);
        else
            System.out.println("End element:   {" + uri + "}" + name);
    }

    public void processStartElement(final XmlPullParser xpp) {
        final String name = xpp.getName();
        final String uri = xpp.getNamespace();
        if ("".equals(uri))
            System.out.println("Start element: " + name);
        else
            System.out.println("Start element: {" + uri + "}" + name);
    }

    public void processText(final XmlPullParser xpp)
            throws XmlPullParserException {
        final char ch[] = xpp.getTextCharacters(holderForStartAndLength);
        final int start = holderForStartAndLength[0];
        final int length = holderForStartAndLength[1];
        System.out.print("Characters:    \"");
        for (int i = start; i < start + length; i++)
            switch (ch[i]) {
            case '\\':
                System.out.print("\\\\");
                break;
            case '"':
                System.out.print("\\\"");
                break;
            case '\n':
                System.out.print("\\n");
                break;
            case '\r':
                System.out.print("\\r");
                break;
            case '\t':
                System.out.print("\\t");
                break;
            default:
                System.out.print(ch[i]);
                break;
            }
        System.out.print("\"\n");
    }
}
