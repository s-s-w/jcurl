package com.megginson.sax;

public class TestDataWriter {

    public static void main(String args[]) throws Exception {
        DataWriter w = new DataWriter();

        w.setIndentStep(2);
        w.startDocument();
        w.startElement("foo");
        w.dataElement("bar", "1");
        w.dataElement("bar", "2");
        w.startElement("hack");
        w.dataElement("fubar", "zing");
        w.endElement("hack");
        w.dataElement("bar", "3");
        w.endElement("foo");
        w.endDocument();
    }

}
