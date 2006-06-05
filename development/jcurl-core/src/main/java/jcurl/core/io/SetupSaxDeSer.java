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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.jcurl.core.helpers.Dim;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.model.RockSet;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Just do XML parsing and hand values to {@link jcurl.core.io.SetupBuilder}.
 * 
 * @see jcurl.core.io.SetupSaxDeSerTest
 * @see jcurl.core.io.OldConfigReader
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SetupSaxDeSer extends DefaultHandler {
    private static final class RockIdx {
        public final int idx16;

        public final int idx8;

        public final boolean isDark;

        public RockIdx(final Attributes atts) {
            final String col = atts.getValue("color");
            if ("dark".equals(col))
                isDark = true;
            else if ("light".equals(col))
                isDark = false;
            else
                throw new IllegalArgumentException("");
            final String no = atts.getValue("no");
            idx8 = Byte.parseByte(no) - 1;
            if (idx8 < 0 || idx8 > 7)
                throw new IllegalArgumentException("");
            idx16 = RockSet.toIdx16(isDark, idx8);
        }
    }

    private static final Log log = JCLoggerFactory
            .getLogger(SetupSaxDeSer.class);

    private static SAXParserFactory spf = null;

    private static DimVal getDim(final Attributes atts) {
        final String val = atts.getValue("val");
        final Dim dim = Dim.find(atts.getValue("dim"));
        return new DimVal(Double.parseDouble(val), dim);
    }

    private static synchronized SAXParser newParser() throws SAXException {
        if (spf == null) {
            spf = SAXParserFactory.newInstance();
            // http://www.cafeconleche.org/slides/xmlone/london2002/namespaces/36.html
            // http://xml.apache.org/xerces-j/features.html
            // spf.setFeature("http://xml.org/sax/features/namespaces", true);
            // spf.setFeature("http://xml.org/sax/features/namespace-prefixes",
            // true);
            spf.setNamespaceAware(true);
            spf.setValidating(false);
        }
        try {
            final SAXParser sp = spf.newSAXParser();
            if (log.isDebugEnabled())
                log.debug(sp.getClass().getName());
            return sp;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static SetupBuilder parse(final File file) throws SAXException,
            IOException {
        if (file.getName().endsWith("z"))
            return parse(new GZIPInputStream(new FileInputStream(file)));
        return parse(new FileInputStream(file));
    }

    public static SetupBuilder parse(final InputSource in) throws SAXException,
            IOException {
        final SetupBuilder ret = new SetupBuilder();
        newParser().parse(in, new SetupSaxDeSer(ret));
        return ret;
    }

    public static SetupBuilder parse(final InputStream file)
            throws SAXException, IOException {
        return parse(new InputSource(file));
    }

    public static SetupBuilder parse(final Reader file) throws SAXException,
            IOException {
        return parse(new InputSource(file));
    }

    public static SetupBuilder parse(final URL file) throws SAXException,
            IOException {
        if (file.getFile().endsWith("z"))
            return parse(new GZIPInputStream(file.openStream()));
        return parse(file.openStream());
    }

    private final StringBuffer buf = new StringBuffer();

    private RockIdx currRock = null;

    private final Stack elems = new Stack();

    private Locator locator;

    private Class modelClass = null;

    private final Map modelProps = new TreeMap();

    private final SetupBuilder setup;

    private SetupSaxDeSer(final SetupBuilder setup) {
        this.setup = setup;
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        buf.append(ch, start, length);
    }

    public void endDocument() throws SAXException {
        log.debug("-");
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        final String elem = qName;
        elems.pop();
        final String txt = buf.toString().trim();
        switch (elems.size() + 1) {
        case 1:
            if ("jcurl".equals(elem))
                ;
            else
                break;
            return;
        case 2:
            if ("setup".equals(elem))
                setup.freeze();
            else
                break;
            return;
        case 3:
            if ("model".equals(elem))
                try {
                    setup.addModel(modelClass, modelProps);
                } catch (InstantiationException e) {
                    log.warn("error in [" + elems + "]", e);
                    error(new SAXParseException("error in [" + elems + "]",
                            locator, e));
                } catch (IllegalAccessException e) {
                    log.warn("error in [" + elems + "]", e);
                    error(new SAXParseException("error in [" + elems + "]",
                            locator, e));
                }
            else
                break;
            return;
        case 4:
            if ("rock".equals(elem))
                currRock = null;
            else
                break;
            return;
        }
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        log.debug(prefix);
    }

    public void error(SAXParseException e) throws SAXException {
        throw e;
    }

    public void fatalError(SAXParseException e) throws SAXException {
        throw e;
    }

    public void processingInstruction(String target, String data)
            throws SAXException {
        if (log.isDebugEnabled())
            log.debug(target + " " + data);
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    public void startDocument() throws SAXException {
        log.debug("-");
    }

    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        // log.debug("[" + localName + "] [" + qName + "] [" + uri + "]");
        final String elem = qName;
        final String parent = elems.size() > 0 ? (String) elems.peek() : null;
        final String grandParent = elems.size() > 1 ? (String) elems.get(elems
                .size() - 2) : null;
        elems.push(elem);
        if (log.isDebugEnabled())
            log.debug(elems + " " + atts.getLength());
        buf.setLength(0);
        try {
            // check
            switch (elems.size()) {
            case 1:
                if ("jcurl".equals(elem))
                    ;
                else
                    break;
                return;
            case 2:
                if ("setup".equals(elem))
                    ;
                else
                    break;
                return;
            case 3:
                if ("meta".equals(elem))
                    ;
                else if ("model".equals(elem)) {
                    modelClass = Class.forName(atts.getValue("engine"));
                    modelProps.clear();
                } else if ("positions".equals(elem))
                    ;
                else if ("speeds".equals(elem))
                    ;
                else
                    break;
                return;
            case 4:
                if ("event".equals(elem))
                    ;
                else if ("game".equals(elem))
                    ;
                else if ("description".equals(elem) && "model".equals(parent))
                    ;
                else if ("param".equals(elem) && "model".equals(parent)) {
                    final String key = atts.getValue("name");
                    final String val = atts.getValue("val");
                    final String dim = atts.getValue("dim");
                    if (dim != null) {
                        modelProps.put(key, new DimVal(Double.parseDouble(val),
                                Dim.find(dim)));
                    } else {
                        modelProps.put(key, val);
                    }
                } else if ("rock".equals(elem))
                    currRock = new RockIdx(atts);
                else
                    break;
                return;
            case 5:
                if ("a".equals(elem))
                    if ("positions".equals(grandParent))
                        setup.setAngle(currRock.idx16, getDim(atts));
                    else if ("speeds".equals(grandParent))
                        setup.setSpin(currRock.idx16, getDim(atts));
                    else
                        break;
                else if ("x".equals(elem))
                    if ("positions".equals(grandParent))
                        setup.setPosX(currRock.idx16, getDim(atts));
                    else if ("speeds".equals(grandParent))
                        setup.setSpeedX(currRock.idx16, getDim(atts));
                    else
                        break;
                else if ("y".equals(elem))
                    if ("positions".equals(grandParent))
                        setup.setPosY(currRock.idx16, getDim(atts));
                    else if ("speeds".equals(grandParent))
                        setup.setSpeedY(currRock.idx16, getDim(atts));
                    else
                        break;
                else if ("out".equals(elem))
                    if ("positions".equals(grandParent))
                        setup.setPosOut(currRock.idx16);
                    else
                        break;
                else if ("nearhog".equals(elem))
                    if ("positions".equals(grandParent))
                        setup.setPosNHog(currRock.idx16);
                    else
                        break;
                else if ("to_x".equals(elem))
                    if ("speeds".equals(grandParent))
                        setup.setToX(currRock.idx16, getDim(atts));
                    else
                        break;
                else if ("to_y".equals(elem))
                    if ("speeds".equals(grandParent))
                        setup.setToY(currRock.idx16, getDim(atts));
                    else
                        break;
                else if ("speed".equals(elem))
                    if ("speeds".equals(grandParent))
                        setup.setSpeed(currRock.idx16, getDim(atts));
                    else
                        break;
                else if ("spin".equals(elem))
                    if ("speeds".equals(grandParent))
                        setup.setSpin(currRock.idx16, getDim(atts));
                    else
                        break;
                else
                    break;
                return;
            }
            error(new SAXParseException("unexpected element [" + elem + "]",
                    locator));
        } catch (RuntimeException e) {
            log.warn("error in [" + elems + "]", e);
            error(new SAXParseException("error in [" + elems + "]", locator, e));
        } catch (ClassNotFoundException e) {
            log.warn("error in [" + elems + "]", e);
            error(new SAXParseException("error in [" + elems + "]", locator, e));
        }
    }

    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        log.debug("xmlns:" + prefix + "=" + uri);
    }
}