// XMLWriter.java - serialize an XML document.
// Written by David Megginson, david@megginson.com
// NO WARRANTY!  This class is in the public domain.

// $Id:XMLWriter.java 378 2007-01-24 01:18:35Z mrohrmoser $

package com.megginson.sax;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Filter to write an XML document from a SAX event stream.
 * 
 * <p>
 * This class can be used by itself or as part of a SAX event stream: it takes
 * as input a series of SAX2 ContentHandler events and uses the information in
 * those events to write an XML document. Since this class is a filter, it can
 * also pass the events on down a filter chain for further processing (you can
 * use the XMLWriter to take a snapshot of the current state at any point in a
 * filter chain), and it can be used directly as a ContentHandler for a SAX2
 * XMLReader.
 * </p>
 * 
 * <p>
 * The client creates a document by invoking the methods for standard SAX2
 * events, always beginning with the {@link #startDocument startDocument} method
 * and ending with the {@link #endDocument endDocument} method. There are
 * convenience methods provided so that clients to not have to create empty
 * attribute lists or provide empty strings as parameters; for example, the
 * method invocation
 * </p>
 * 
 * <pre>
 * w.startElement(&quot;foo&quot;);
 * </pre>
 * 
 * <p>
 * is equivalent to the regular SAX2 ContentHandler method
 * </p>
 * 
 * <pre>
 * w.startElement(&quot;&quot;, &quot;foo&quot;, &quot;&quot;, new AttributesImpl());
 * </pre>
 * 
 * <p>
 * Except that it is more efficient because it does not allocate a new empty
 * attribute list each time. The following code will send a simple XML document
 * to standard output:
 * </p>
 * 
 * <pre>
 * XMLWriter w = new XMLWriter();
 * 
 * w.startDocument();
 * w.startElement(&quot;greeting&quot;);
 * w.characters(&quot;Hello, world!&quot;);
 * w.endElement(&quot;greeting&quot;);
 * w.endDocument();
 * </pre>
 * 
 * <p>
 * The resulting document will look like this:
 * </p>
 * 
 * <pre>
 *           &lt;?xml version=&quot;1.0&quot; standalone=&quot;yes&quot;?&gt;
 *          
 *           &lt;greeting&gt;Hello, world!&lt;/greeting&gt;
 * </pre>
 * 
 * <p>
 * In fact, there is an even simpler convenience method, <var>dataElement</var>,
 * designed for writing elements that contain only character data, so the code
 * to generate the document could be shortened to
 * </p>
 * 
 * <pre>
 * XMLWriter w = new XMLWriter();
 * 
 * w.startDocument();
 * w.dataElement(&quot;greeting&quot;, &quot;Hello, world!&quot;);
 * w.endDocument();
 * </pre>
 * 
 * <h2>Whitespace</h2>
 * 
 * <p>
 * According to the XML Recommendation, <em>all</em> whitespace in an XML
 * document is potentially significant to an application, so this class never
 * adds newlines or indentation. If you insert three elements in a row, as in
 * </p>
 * 
 * <pre>
 * w.dataElement(&quot;item&quot;, &quot;1&quot;);
 * w.dataElement(&quot;item&quot;, &quot;2&quot;);
 * w.dataElement(&quot;item&quot;, &quot;3&quot;);
 * </pre>
 * 
 * <p>
 * you will end up with
 * </p>
 * 
 * <pre>
 *           &lt;item&gt;1&lt;/item&gt;&lt;item&gt;3&lt;/item&gt;&lt;item&gt;3&lt;/item&gt;
 * </pre>
 * 
 * <p>
 * You need to invoke one of the <var>characters</var> methods explicitly to
 * add newlines or indentation. Alternatively, you can use
 * {@link com.megginson.sax.DataWriter DataWriter}, which is derived from this
 * class -- it is optimized for writing purely data-oriented (or field-oriented)
 * XML, and does automatic linebreaks and indentation (but does not support
 * mixed content properly).
 * </p>
 * 
 * 
 * <h2>Namespace Support</h2>
 * 
 * <p>
 * The writer contains extensive support for XML Namespaces, so that a client
 * application does not have to keep track of prefixes and supply <var>xmlns</var>
 * attributes. By default, the XML writer will generate Namespace declarations
 * in the form _NS1, _NS2, etc., wherever they are needed, as in the following
 * example:
 * </p>
 * 
 * <pre>
 * w.startDocument();
 * w.emptyElement(&quot;http://www.foo.com/ns/&quot;, &quot;foo&quot;);
 * w.endDocument();
 * </pre>
 * 
 * <p>
 * The resulting document will look like this:
 * </p>
 * 
 * <pre>
 *           &lt;?xml version=&quot;1.0&quot; standalone=&quot;yes&quot;?&gt;
 *          
 *           &lt;_NS1:foo xmlns:_NS1=&quot;http://www.foo.com/ns/&quot;/&gt;
 * </pre>
 * 
 * <p>
 * In many cases, document authors will prefer to choose their own prefixes
 * rather than using the (ugly) default names. The XML writer allows two methods
 * for selecting prefixes:
 * </p>
 * 
 * <ol>
 * <li>the qualified name</li>
 * <li>the {@link #setPrefix setPrefix} method.</li>
 * </ol>
 * 
 * <p>
 * Whenever the XML writer finds a new Namespace URI, it checks to see if a
 * qualified (prefixed) name is also available; if so it attempts to use the
 * name's prefix (as long as the prefix is not already in use for another
 * Namespace URI).
 * </p>
 * 
 * <p>
 * Before writing a document, the client can also pre-map a prefix to a
 * Namespace URI with the setPrefix method:
 * </p>
 * 
 * <pre>
 * w.setPrefix(&quot;http://www.foo.com/ns/&quot;, &quot;foo&quot;);
 * w.startDocument();
 * w.emptyElement(&quot;http://www.foo.com/ns/&quot;, &quot;foo&quot;);
 * w.endDocument();
 * </pre>
 * 
 * <p>
 * The resulting document will look like this:
 * </p>
 * 
 * <pre>
 *           &lt;?xml version=&quot;1.0&quot; standalone=&quot;yes&quot;?&gt;
 *          
 *           &lt;foo:foo xmlns:foo=&quot;http://www.foo.com/ns/&quot;/&gt;
 * </pre>
 * 
 * <p>
 * The default Namespace simply uses an empty string as the prefix:
 * </p>
 * 
 * <pre>
 * w.setPrefix(&quot;http://www.foo.com/ns/&quot;, &quot;&quot;);
 * w.startDocument();
 * w.emptyElement(&quot;http://www.foo.com/ns/&quot;, &quot;foo&quot;);
 * w.endDocument();
 * </pre>
 * 
 * <p>
 * The resulting document will look like this:
 * </p>
 * 
 * <pre>
 *           &lt;?xml version=&quot;1.0&quot; standalone=&quot;yes&quot;?&gt;
 *          
 *           &lt;foo xmlns=&quot;http://www.foo.com/ns/&quot;/&gt;
 * </pre>
 * 
 * <p>
 * By default, the XML writer will not declare a Namespace until it is actually
 * used. Sometimes, this approach will create a large number of Namespace
 * declarations, as in the following example:
 * </p>
 * 
 * <pre>
 *           &lt;xml version=&quot;1.0&quot; standalone=&quot;yes&quot;?&gt;
 *          
 *           &lt;rdf:RDF xmlns:rdf=&quot;http://www.w3.org/1999/02/22-rdf-syntax-ns#&quot;&gt;
 *            &lt;rdf:Description about=&quot;http://www.foo.com/ids/books/12345&quot;&gt;
 *             &lt;dc:title xmlns:dc=&quot;http://www.purl.org/dc/&quot;&gt;A Dark Night&lt;/dc:title&gt;
 *             &lt;dc:creator xmlns:dc=&quot;http://www.purl.org/dc/&quot;&gt;Jane Smith&lt;/dc:title&gt;
 *             &lt;dc:date xmlns:dc=&quot;http://www.purl.org/dc/&quot;&gt;2000-09-09&lt;/dc:title&gt;
 *            &lt;/rdf:Description&gt;
 *           &lt;/rdf:RDF&gt;
 * </pre>
 * 
 * <p>
 * The "rdf" prefix is declared only once, because the RDF Namespace is used by
 * the root element and can be inherited by all of its descendants; the "dc"
 * prefix, on the other hand, is declared three times, because no higher element
 * uses the Namespace. To solve this problem, you can instruct the XML writer to
 * predeclare Namespaces on the root element even if they are not used there:
 * </p>
 * 
 * <pre>
 * w.forceNSDecl(&quot;http://www.purl.org/dc/&quot;);
 * </pre>
 * 
 * <p>
 * Now, the "dc" prefix will be declared on the root element even though it's
 * not needed there, and can be inherited by its descendants:
 * </p>
 * 
 * <pre>
 *           &lt;xml version=&quot;1.0&quot; standalone=&quot;yes&quot;?&gt;
 *          
 *           &lt;rdf:RDF xmlns:rdf=&quot;http://www.w3.org/1999/02/22-rdf-syntax-ns#&quot;
 *                       xmlns:dc=&quot;http://www.purl.org/dc/&quot;&gt;
 *            &lt;rdf:Description about=&quot;http://www.foo.com/ids/books/12345&quot;&gt;
 *             &lt;dc:title&gt;A Dark Night&lt;/dc:title&gt;
 *             &lt;dc:creator&gt;Jane Smith&lt;/dc:title&gt;
 *             &lt;dc:date&gt;2000-09-09&lt;/dc:title&gt;
 *            &lt;/rdf:Description&gt;
 *           &lt;/rdf:RDF&gt;
 * </pre>
 * 
 * <p>
 * This approach is also useful for declaring Namespace prefixes that be used by
 * qualified names appearing in attribute values or character data.
 * </p>
 * 
 * @author David Megginson, david@megginson.com
 * @version 0.2
 * @see org.xml.sax.XMLFilter
 * @see org.xml.sax.ContentHandler
 */
class XMLWriter extends XMLFilterImpl {

    // //////////////////////////////////////////////////////////////////
    // Constructors.
    // //////////////////////////////////////////////////////////////////

    private Hashtable doneDeclTable;

    private int elementLevel = 0;

    private final Attributes EMPTY_ATTS = new AttributesImpl();

    private Hashtable forcedDeclTable;

    private NamespaceSupport nsSupport;

    // //////////////////////////////////////////////////////////////////
    // Public methods.
    // //////////////////////////////////////////////////////////////////

    private Writer output;

    private int prefixCounter = 0;

    private Hashtable prefixTable;

    /**
     * Create a new XML writer.
     * 
     * <p>
     * Write to standard output.
     * </p>
     */
    public XMLWriter() {
        init(null);
    }

    /**
     * Create a new XML writer.
     * 
     * <p>
     * Write to the writer provided.
     * </p>
     * 
     * @param writer
     *            The output destination, or null to use standard output.
     */
    public XMLWriter(Writer writer) {
        init(writer);
    }

    /**
     * Create a new XML writer.
     * 
     * <p>
     * Use the specified XML reader as the parent.
     * </p>
     * 
     * @param xmlreader
     *            The parent in the filter chain, or null for no parent.
     */
    public XMLWriter(XMLReader xmlreader) {
        super(xmlreader);
        init(null);
    }

    /**
     * Create a new XML writer.
     * 
     * <p>
     * Use the specified XML reader as the parent, and write to the specified
     * writer.
     * </p>
     * 
     * @param xmlreader
     *            The parent in the filter chain, or null for no parent.
     * @param writer
     *            The output destination, or null to use standard output.
     */
    public XMLWriter(XMLReader xmlreader, Writer writer) {
        super(xmlreader);
        init(writer);
    }

    // //////////////////////////////////////////////////////////////////
    // Methods from org.xml.sax.ContentHandler.
    // //////////////////////////////////////////////////////////////////

    /**
     * Write character data.
     * 
     * Pass the event on down the filter chain for further processing.
     * 
     * @param ch
     *            The array of characters to write.
     * @param start
     *            The starting position in the array.
     * @param len
     *            The number of characters to write.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the characters, or if a
     *                handler further down the filter chain raises an exception.
     * @see org.xml.sax.ContentHandler#characters
     */
    public void characters(char ch[], int start, int len) throws SAXException {
        writeEsc(ch, start, len, false);
        super.characters(ch, start, len);
    }

    /**
     * Write a string of character data, with XML escaping.
     * 
     * <p>
     * This is a convenience method that takes an XML String, converts it to a
     * character array, then invokes {@link #characters(char[], int, int)}.
     * </p>
     * 
     * @param data
     *            The character data.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the string, or if a handler
     *                further down the filter chain raises an exception.
     * @see #characters(char[], int, int)
     */
    public void characters(String data) throws SAXException {
        char ch[] = data.toCharArray();
        this.characters(ch, 0, ch.length);
    }

    /**
     * Write an element with character data content but no attributes or
     * Namespace URI.
     * 
     * <p>
     * This is a convenience method to write a complete element with character
     * data content, including the start tag and end tag. The method provides an
     * empty string for the Namespace URI, and empty string for the qualified
     * name, and an empty attribute list.
     * </p>
     * 
     * <p>
     * This method invokes
     * {@link #startElement(String, String, String, Attributes)}, followed by
     * {@link #characters(String)}, followed by
     * {@link #endElement(String, String, String)}.
     * </p>
     * 
     * @param localName
     *            The element's local name.
     * @param content
     *            The character data content.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the empty tag, or if a
     *                handler further down the filter chain raises an exception.
     * @see #startElement(String, String, String, Attributes)
     * @see #characters(String)
     * @see #endElement(String, String, String)
     */
    public void dataElement(String localName, String content)
            throws SAXException {
        this.dataElement("", localName, "", EMPTY_ATTS, content);
    }

    /**
     * Write an element with character data content but no attributes.
     * 
     * <p>
     * This is a convenience method to write a complete element with character
     * data content, including the start tag and end tag. This method provides
     * an empty string for the qname and an empty attribute list.
     * </p>
     * 
     * <p>
     * This method invokes
     * {@link #startElement(String, String, String, Attributes)}, followed by
     * {@link #characters(String)}, followed by
     * {@link #endElement(String, String, String)}.
     * </p>
     * 
     * @param uri
     *            The element's Namespace URI.
     * @param localName
     *            The element's local name.
     * @param content
     *            The character data content.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the empty tag, or if a
     *                handler further down the filter chain raises an exception.
     * @see #startElement(String, String, String, Attributes)
     * @see #characters(String)
     * @see #endElement(String, String, String)
     */
    public void dataElement(String uri, String localName, String content)
            throws SAXException {
        this.dataElement(uri, localName, "", EMPTY_ATTS, content);
    }

    /**
     * Write an element with character data content.
     * 
     * <p>
     * This is a convenience method to write a complete element with character
     * data content, including the start tag and end tag.
     * </p>
     * 
     * <p>
     * This method invokes
     * {@link #startElement(String, String, String, Attributes)}, followed by
     * {@link #characters(String)}, followed by
     * {@link #endElement(String, String, String)}.
     * </p>
     * 
     * @param uri
     *            The element's Namespace URI.
     * @param localName
     *            The element's local name.
     * @param qName
     *            The element's default qualified name.
     * @param atts
     *            The element's attributes.
     * @param content
     *            The character data content.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the empty tag, or if a
     *                handler further down the filter chain raises an exception.
     * @see #startElement(String, String, String, Attributes)
     * @see #characters(String)
     * @see #endElement(String, String, String)
     */
    public void dataElement(String uri, String localName, String qName,
            Attributes atts, String content) throws SAXException {
        this.startElement(uri, localName, qName, atts);
        this.characters(content);
        this.endElement(uri, localName, qName);
    }

    /**
     * Determine the prefix for an element or attribute name.
     * 
     * TODO: this method probably needs some cleanup.
     * 
     * @param uri
     *            The Namespace URI.
     * @param qName
     *            The qualified name (optional); this will be used to indicate
     *            the preferred prefix if none is currently bound.
     * @param isElement
     *            true if this is an element name, false if it is an attribute
     *            name (which cannot use the default Namespace).
     * @return ???
     */
    private String doPrefix(String uri, String qName, boolean isElement) {
        String defaultNS = nsSupport.getURI("");
        if ("".equals(uri)) {
            if (isElement && defaultNS != null)
                nsSupport.declarePrefix("", "");
            return null;
        }
        String prefix;
        if (isElement && defaultNS != null && uri.equals(defaultNS))
            prefix = "";
        else
            prefix = nsSupport.getPrefix(uri);
        if (prefix != null)
            return prefix;
        prefix = (String) doneDeclTable.get(uri);
        if (prefix != null
                && ((!isElement || defaultNS != null) && "".equals(prefix) || nsSupport
                        .getURI(prefix) != null))
            prefix = null;
        if (prefix == null) {
            prefix = (String) prefixTable.get(uri);
            if (prefix != null
                    && ((!isElement || defaultNS != null) && "".equals(prefix) || nsSupport
                            .getURI(prefix) != null))
                prefix = null;
        }
        if (prefix == null && qName != null && !"".equals(qName)) {
            int i = qName.indexOf(':');
            if (i == -1) {
                if (isElement && defaultNS == null)
                    prefix = "";
            } else
                prefix = qName.substring(0, i);
        }
        for (; prefix == null || nsSupport.getURI(prefix) != null; prefix = "__NS"
                + ++prefixCounter)
            ;
        nsSupport.declarePrefix(prefix, uri);
        doneDeclTable.put(uri, prefix);
        return prefix;
    }

    /**
     * Add an empty element without a Namespace URI, qname or attributes.
     * 
     * <p>
     * This method will supply an empty string for the qname, and empty string
     * for the Namespace URI, and an empty attribute list. It invokes
     * {@link #emptyElement(String, String, String, Attributes)} directly.
     * </p>
     * 
     * @param localName
     *            The element's local name.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the empty tag, or if a
     *                handler further down the filter chain raises an exception.
     * @see #emptyElement(String, String, String, Attributes)
     */
    public void emptyElement(String localName) throws SAXException {
        this.emptyElement("", localName, "", EMPTY_ATTS);
    }

    // //////////////////////////////////////////////////////////////////
    // Additional markup.
    // //////////////////////////////////////////////////////////////////

    /**
     * Add an empty element without a qname or attributes.
     * 
     * <p>
     * This method will supply an empty string for the qname and an empty
     * attribute list. It invokes
     * {@link #emptyElement(String, String, String, Attributes)} directly.
     * </p>
     * 
     * @param uri
     *            The element's Namespace URI.
     * @param localName
     *            The element's local name.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the empty tag, or if a
     *                handler further down the filter chain raises an exception.
     * @see #emptyElement(String, String, String, Attributes)
     */
    public void emptyElement(String uri, String localName) throws SAXException {
        this.emptyElement(uri, localName, "", EMPTY_ATTS);
    }

    // //////////////////////////////////////////////////////////////////
    // Convenience methods.
    // //////////////////////////////////////////////////////////////////

    /**
     * Write an empty element.
     * 
     * This method writes an empty element tag rather than a start tag followed
     * by an end tag. Both a {@link #startElement(String)} and an
     * {@link #endElement(String) } event will be passed on down the filter
     * chain.
     * 
     * @param uri
     *            The element's Namespace URI, or the empty string if the
     *            element has no Namespace or if Namespace processing is not
     *            being performed.
     * @param localName
     *            The element's local name (without prefix). This parameter must
     *            be provided.
     * @param qName
     *            The element's qualified name (with prefix), or the empty
     *            string if none is available. This parameter is strictly
     *            advisory: the writer may or may not use the prefix attached.
     * @param atts
     *            The element's attribute list.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the empty tag, or if a
     *                handler further down the filter chain raises an exception.
     * @see #startElement(String, String, String, Attributes)
     * @see #endElement(String, String, String)
     */
    public void emptyElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        nsSupport.pushContext();
        this.write('<');
        writeName(uri, localName, qName, true);
        writeAttributes(atts);
        if (elementLevel == 1)
            forceNSDecls();
        writeNSDecls();
        this.write("/>");
        super.startElement(uri, localName, qName, atts);
        super.endElement(uri, localName, qName);
    }

    /**
     * Write a newline at the end of the document.
     * 
     * Pass the event on down the filter chain for further processing.
     * 
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the newline, or if a handler
     *                further down the filter chain raises an exception.
     * @see org.xml.sax.ContentHandler#endDocument
     */
    public void endDocument() throws SAXException {
        this.write('\n');
        super.endDocument();
        try {
            flush();
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    /**
     * End an element without a Namespace URI or qname.
     * 
     * <p>
     * This method will supply an empty string for the qName and an empty string
     * for the Namespace URI. It invokes
     * {@link #endElement(String, String, String)} directly.
     * </p>
     * 
     * @param localName
     *            The element's local name.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the end tag, or if a handler
     *                further down the filter chain raises an exception.
     * @see #endElement(String, String, String)
     */
    public void endElement(String localName) throws SAXException {
        this.endElement("", localName, "");
    }

    /**
     * End an element without a qname.
     * 
     * <p>
     * This method will supply an empty string for the qName. It invokes
     * {@link #endElement(String, String, String)} directly.
     * </p>
     * 
     * @param uri
     *            The element's Namespace URI.
     * @param localName
     *            The element's local name.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the end tag, or if a handler
     *                further down the filter chain raises an exception.
     * @see #endElement(String, String, String)
     */
    public void endElement(String uri, String localName) throws SAXException {
        this.endElement(uri, localName, "");
    }

    /**
     * Write an end tag.
     * 
     * Pass the event on down the filter chain for further processing.
     * 
     * @param uri
     *            The Namespace URI, or the empty string if none is available.
     * @param localName
     *            The element's local (unprefixed) name (required).
     * @param qName
     *            The element's qualified (prefixed) name, or the empty string
     *            is none is available. This method will use the qName as a
     *            template for generating a prefix if necessary, but it is not
     *            guaranteed to use the same qName.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the end tag, or if a handler
     *                further down the filter chain raises an exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        this.write("</");
        writeName(uri, localName, qName, true);
        this.write('>');
        if (elementLevel == 1)
            this.write('\n');
        super.endElement(uri, localName, qName);
        nsSupport.popContext();
        elementLevel--;
    }

    /**
     * Flush the output.
     * 
     * <p>
     * This method flushes the output stream. It is especially useful when you
     * need to make certain that the entire document has been written to output
     * but do not want to close the output stream.
     * </p>
     * 
     * <p>
     * This method is invoked automatically by the
     * {@link #endDocument endDocument} method after writing a document.
     * </p>
     * 
     * @throws IOException
     * @see #reset
     */
    public void flush() throws IOException {
        output.flush();
    }

    /**
     * Force a Namespace to be declared on the root element.
     * 
     * <p>
     * By default, the XMLWriter will declare only the Namespaces needed for an
     * element; as a result, a Namespace may be declared many places in a
     * document if it is not used on the root element.
     * </p>
     * 
     * <p>
     * This method forces a Namespace to be declared on the root element even if
     * it is not used there, and reduces the number of xmlns attributes in the
     * document.
     * </p>
     * 
     * @param uri
     *            The Namespace URI to declare.
     * @see #forceNSDecl(java.lang.String,java.lang.String)
     * @see #setPrefix
     */
    public void forceNSDecl(String uri) {
        forcedDeclTable.put(uri, Boolean.TRUE);
    }

    /**
     * Force a Namespace declaration with a preferred prefix.
     * 
     * <p>
     * This is a convenience method that invokes {@link #setPrefix setPrefix}
     * then {@link #forceNSDecl(java.lang.String) forceNSDecl}.
     * </p>
     * 
     * @param uri
     *            The Namespace URI to declare on the root element.
     * @param prefix
     *            The preferred prefix for the Namespace, or "" for the default
     *            Namespace.
     * @see #setPrefix
     * @see #forceNSDecl(java.lang.String)
     */
    public void forceNSDecl(String uri, String prefix) {
        setPrefix(uri, prefix);
        this.forceNSDecl(uri);
    }

    /**
     * Force all Namespaces to be declared.
     * 
     * This method is used on the root element to ensure that the predeclared
     * Namespaces all appear.
     */
    private void forceNSDecls() {
        Enumeration prefixes = forcedDeclTable.keys();
        while (prefixes.hasMoreElements()) {
            String prefix = (String) prefixes.nextElement();
            doPrefix(prefix, null, true);
        }
    }

    /**
     * Get the current or preferred prefix for a Namespace URI.
     * 
     * @param uri
     *            The Namespace URI.
     * @return The preferred prefix, or "" for the default Namespace.
     * @see #setPrefix
     */
    public String getPrefix(String uri) {
        return (String) prefixTable.get(uri);
    }

    // //////////////////////////////////////////////////////////////////
    // Internal methods.
    // //////////////////////////////////////////////////////////////////

    /**
     * Write ignorable whitespace.
     * 
     * Pass the event on down the filter chain for further processing.
     * 
     * @param ch
     *            The array of characters to write.
     * @param start
     *            The starting position in the array.
     * @param length
     *            The number of characters to write.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the whitespace, or if a
     *                handler further down the filter chain raises an exception.
     * @see org.xml.sax.ContentHandler#ignorableWhitespace
     */
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
        writeEsc(ch, start, length, false);
        super.ignorableWhitespace(ch, start, length);
    }

    /**
     * Internal initialization method.
     * 
     * <p>
     * All of the public constructors invoke this method.
     * 
     * @param writer
     *            The output destination, or null to use standard output.
     */
    private void init(Writer writer) {
        setOutput(writer);
        nsSupport = new NamespaceSupport();
        prefixTable = new Hashtable();
        forcedDeclTable = new Hashtable();
        doneDeclTable = new Hashtable();
    }

    /**
     * Write a processing instruction.
     * 
     * Pass the event on down the filter chain for further processing.
     * 
     * @param target
     *            The PI target.
     * @param data
     *            The PI data.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the PI, or if a handler
     *                further down the filter chain raises an exception.
     * @see org.xml.sax.ContentHandler#processingInstruction
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
        this.write("<?");
        this.write(target);
        this.write(' ');
        this.write(data);
        this.write("?>");
        if (elementLevel < 1)
            this.write('\n');
        super.processingInstruction(target, data);
    }

    /**
     * Reset the writer.
     * 
     * <p>
     * This method is especially useful if the writer throws an exception before
     * it is finished, and you want to reuse the writer for a new document. It
     * is usually a good idea to invoke {@link #flush flush} before resetting
     * the writer, to make sure that no output is lost.
     * </p>
     * 
     * <p>
     * This method is invoked automatically by the
     * {@link #startDocument startDocument} method before writing a new
     * document.
     * </p>
     * 
     * <p>
     * <strong>Note:</strong> this method will <em>not</em> clear the prefix
     * or URI information in the writer or the selected output writer.
     * </p>
     * 
     * @see #flush
     */
    public void reset() {
        elementLevel = 0;
        prefixCounter = 0;
        nsSupport.reset();
    }

    /**
     * Set a new output destination for the document.
     * 
     * @param writer
     *            The output destination, or null to use standard output.
     * @see #flush
     */
    public void setOutput(Writer writer) {
        if (writer == null)
            output = new OutputStreamWriter(System.out);
        else
            output = writer;
    }

    /**
     * Specify a preferred prefix for a Namespace URI.
     * 
     * <p>
     * Note that this method does not actually force the Namespace to be
     * declared; to do that, use the {@link  #forceNSDecl(java.lang.String)
     * forceNSDecl} method as well.
     * </p>
     * 
     * @param uri
     *            The Namespace URI.
     * @param prefix
     *            The preferred prefix, or "" to select the default Namespace.
     * @see #getPrefix
     * @see #forceNSDecl(java.lang.String)
     * @see #forceNSDecl(java.lang.String,java.lang.String)
     */
    public void setPrefix(String uri, String prefix) {
        prefixTable.put(uri, prefix);
    }

    /**
     * Write the XML declaration at the beginning of the document.
     * 
     * Pass the event on down the filter chain for further processing.
     * 
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the XML declaration, or if a
     *                handler further down the filter chain raises an exception.
     * @see org.xml.sax.ContentHandler#startDocument
     */
    public void startDocument() throws SAXException {
        reset();
        this.write("<?xml version=\"1.0\" standalone=\"yes\"?>\n\n");
        super.startDocument();
    }

    /**
     * Start a new element without a qname, attributes or a Namespace URI.
     * 
     * <p>
     * This method will provide an empty string for the Namespace URI, and empty
     * string for the qualified name, and a default empty attribute list. It
     * invokes #startElement(String, String, String, Attributes)} directly.
     * </p>
     * 
     * @param localName
     *            The element's local name.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the start tag, or if a
     *                handler further down the filter chain raises an exception.
     * @see #startElement(String, String, String, Attributes)
     */
    public void startElement(String localName) throws SAXException {
        this.startElement("", localName, "", EMPTY_ATTS);
    }

    // //////////////////////////////////////////////////////////////////
    // Constants.
    // //////////////////////////////////////////////////////////////////

    /**
     * Start a new element without a qname or attributes.
     * 
     * <p>
     * This method will provide a default empty attribute list and an empty
     * string for the qualified name. It invokes {@link  #startElement(String,
     * String, String, Attributes)} directly.
     * </p>
     * 
     * @param uri
     *            The element's Namespace URI.
     * @param localName
     *            The element's local name.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the start tag, or if a
     *                handler further down the filter chain raises an exception.
     * @see #startElement(String, String, String, Attributes)
     */
    public void startElement(String uri, String localName) throws SAXException {
        this.startElement(uri, localName, "", EMPTY_ATTS);
    }

    // //////////////////////////////////////////////////////////////////
    // Internal state.
    // //////////////////////////////////////////////////////////////////

    /**
     * Write a start tag.
     * 
     * Pass the event on down the filter chain for further processing.
     * 
     * @param uri
     *            The Namespace URI, or the empty string if none is available.
     * @param localName
     *            The element's local (unprefixed) name (required).
     * @param qName
     *            The element's qualified (prefixed) name, or the empty string
     *            is none is available. This method will use the qName as a
     *            template for generating a prefix if necessary, but it is not
     *            guaranteed to use the same qName.
     * @param atts
     *            The element's attribute list (must not be null).
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the start tag, or if a
     *                handler further down the filter chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        elementLevel++;
        nsSupport.pushContext();
        this.write('<');
        writeName(uri, localName, qName, true);
        writeAttributes(atts);
        if (elementLevel == 1)
            forceNSDecls();
        writeNSDecls();
        this.write('>');
        super.startElement(uri, localName, qName, atts);
    }

    /**
     * Write a raw character.
     * 
     * @param c
     *            The character to write.
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the character, this method
     *                will throw an IOException wrapped in a SAXException.
     */
    private void write(char c) throws SAXException {
        try {
            output.write(c);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    /**
     * Write a raw string.
     * 
     * @param s
     * @exception org.xml.sax.SAXException
     *                If there is an error writing the string, this method will
     *                throw an IOException wrapped in a SAXException
     */
    private void write(String s) throws SAXException {
        try {
            output.write(s);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    /**
     * Write out an attribute list, escaping values.
     * 
     * The names will have prefixes added to them.
     * 
     * @param atts
     *            The attribute list to write.
     * @exception SAXException
     *                If there is an error writing the attribute list, this
     *                method will throw an IOException wrapped in a
     *                SAXException.
     */
    private void writeAttributes(Attributes atts) throws SAXException {
        int len = atts.getLength();
        for (int i = 0; i < len; i++) {
            char ch[] = atts.getValue(i).toCharArray();
            this.write(' ');
            writeName(atts.getURI(i), atts.getLocalName(i), atts.getQName(i),
                    false);
            this.write("=\"");
            writeEsc(ch, 0, ch.length, true);
            this.write('"');
        }
    }

    /**
     * Write an array of data characters with escaping.
     * 
     * @param ch
     *            The array of characters.
     * @param start
     *            The starting position.
     * @param length
     *            The number of characters to use.
     * @param isAttVal
     *            true if this is an attribute value literal.
     * @exception SAXException
     *                If there is an error writing the characters, this method
     *                will throw an IOException wrapped in a SAXException.
     */
    private void writeEsc(char ch[], int start, int length, boolean isAttVal)
            throws SAXException {
        for (int i = start; i < start + length; i++)
            switch (ch[i]) {
            case '&':
                this.write("&amp;");
                break;
            case '<':
                this.write("&lt;");
                break;
            case '>':
                this.write("&gt;");
                break;
            case '\"':
                if (isAttVal)
                    this.write("&quot;");
                else
                    this.write('\"');
                break;
            default:
                if (ch[i] > '\u007f') {
                    this.write("&#");
                    this.write(Integer.toString(ch[i]));
                    this.write(';');
                } else
                    this.write(ch[i]);
            }
    }

    /**
     * Write an element or attribute name.
     * 
     * @param uri
     *            The Namespace URI.
     * @param localName
     *            The local name.
     * @param qName
     *            The prefixed name, if available, or the empty string.
     * @param isElement
     *            true if this is an element name, false if it is an attribute
     *            name.
     * @exception org.xml.sax.SAXException
     *                This method will throw an IOException wrapped in a
     *                SAXException if there is an error writing the name.
     */
    private void writeName(String uri, String localName, String qName,
            boolean isElement) throws SAXException {
        String prefix = doPrefix(uri, qName, isElement);
        if (prefix != null && !"".equals(prefix)) {
            this.write(prefix);
            this.write(':');
        }
        this.write(localName);
    }

    /**
     * Write out the list of Namespace declarations.
     * 
     * @exception org.xml.sax.SAXException
     *                This method will throw an IOException wrapped in a
     *                SAXException if there is an error writing the Namespace
     *                declarations.
     */
    private void writeNSDecls() throws SAXException {
        Enumeration prefixes = nsSupport.getDeclaredPrefixes();
        while (prefixes.hasMoreElements()) {
            String prefix = (String) prefixes.nextElement();
            String uri = nsSupport.getURI(prefix);
            if (uri == null)
                uri = "";
            char ch[] = uri.toCharArray();
            this.write(' ');
            if ("".equals(prefix))
                this.write("xmlns=\"");
            else {
                this.write("xmlns:");
                this.write(prefix);
                this.write("=\"");
            }
            writeEsc(ch, 0, ch.length, true);
            this.write('\"');
        }
    }

}

// end of XMLWriter.java
