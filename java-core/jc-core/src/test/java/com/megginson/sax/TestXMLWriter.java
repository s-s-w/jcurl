package com.megginson.sax;

// TestXMLWriter.java - Test harness for the XML writer.
// Usage: java -Dorg.xml.sax.driver="..." TestXMLWriter [files...]

// $Id$

import java.io.FileReader;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Simple test harness for XMLWriter.
 */
public class TestXMLWriter {
    public static void main(final String args[]) throws Exception // yech!
    {
        final XMLWriter w = new XMLWriter(XMLReaderFactory.createXMLReader());

        if (args.length == 0) {
            System.err
                    .println("Usage java -Dorg.xml.sax.driver=<driver> TestXMLWriter file...");
            System.exit(1);
        }

        for (final String element : args)
            w.parse(new InputSource(new FileReader(element)));
    }
}

// end of TestXMLWriter.java
