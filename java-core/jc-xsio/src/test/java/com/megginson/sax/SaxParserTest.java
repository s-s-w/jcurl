/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
package com.megginson.sax;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParserTest extends TestCase {

	public void test001() throws ParserConfigurationException, SAXException,
			IOException {
		final SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		spf.setValidating(false);
		final SAXParser p = spf.newSAXParser();
		final DefaultHandler h = new DefaultHandler() {

			@Override
			public void characters(char[] arg0, int arg1, int arg2)
					throws SAXException {
				super.characters(arg0, arg1, arg2);
			}

			@Override
			public void endDocument() throws SAXException {
				super.endDocument();
			}

			@Override
			public void endElement(String arg0, String arg1, String arg2)
					throws SAXException {
				super.endElement(arg0, arg1, arg2);
			}

			@Override
			public void endPrefixMapping(String arg0) throws SAXException {
				super.endPrefixMapping(arg0);
			}

			@Override
			public void error(SAXParseException arg0) throws SAXException {
				super.error(arg0);
			}

			@Override
			public void fatalError(SAXParseException arg0) throws SAXException {
				super.fatalError(arg0);
			}

			@Override
			public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
					throws SAXException {
				super.ignorableWhitespace(arg0, arg1, arg2);
			}

			@Override
			public void notationDecl(String arg0, String arg1, String arg2)
					throws SAXException {
				super.notationDecl(arg0, arg1, arg2);
			}

			@Override
			public void processingInstruction(String arg0, String arg1)
					throws SAXException {
				assertEquals("pi", arg0);
				assertEquals("a b c", arg1);
			}

			// public InputSource resolveEntity(String arg0, String arg1)
			// throws SAXException {
			// return super.resolveEntity(arg0, arg1);
			// }

			@Override
			public void setDocumentLocator(Locator arg0) {
				super.setDocumentLocator(arg0);
			}

			@Override
			public void skippedEntity(String arg0) throws SAXException {
				super.skippedEntity(arg0);
			}

			@Override
			public void startDocument() throws SAXException {
				super.startDocument();
			}

			@Override
			public void startElement(String arg0, String arg1, String arg2,
					Attributes arg3) throws SAXException {
				super.startElement(arg0, arg1, arg2, arg3);
			}

			@Override
			public void startPrefixMapping(String arg0, String arg1)
					throws SAXException {
				super.startPrefixMapping(arg0, arg1);
			}

			@Override
			public void unparsedEntityDecl(String arg0, String arg1,
					String arg2, String arg3) throws SAXException {
				super.unparsedEntityDecl(arg0, arg1, arg2, arg3);
			}

			@Override
			public void warning(SAXParseException arg0) throws SAXException {
				super.warning(arg0);
			}

		};

		p
				.parse(
						new InputSource(
								new StringReader(
										"<?xml version='1.0'?><?pi a b c?><root xmlns:a='nsa' xmlns='nsdefault' att1='val1' a:att1='a:val1'>hello, world!</root>")),
						h);
	}

	public void test002() throws ParserConfigurationException, SAXException,
			IOException {
		final SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		spf.setValidating(false);
		final SAXParser p = spf.newSAXParser();
		final DefaultHandler h = new DefaultHandler() {

			@Override
			public void characters(char[] arg0, int arg1, int arg2)
					throws SAXException {
				super.characters(arg0, arg1, arg2);
			}

			@Override
			public void endDocument() throws SAXException {
				super.endDocument();
			}

			@Override
			public void endElement(String arg0, String arg1, String arg2)
					throws SAXException {
				super.endElement(arg0, arg1, arg2);
			}

			@Override
			public void endPrefixMapping(String arg0) throws SAXException {
				super.endPrefixMapping(arg0);
			}

			@Override
			public void error(SAXParseException arg0) throws SAXException {
				super.error(arg0);
			}

			@Override
			public void fatalError(SAXParseException arg0) throws SAXException {
				super.fatalError(arg0);
			}

			@Override
			public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
					throws SAXException {
				super.ignorableWhitespace(arg0, arg1, arg2);
			}

			@Override
			public void notationDecl(String arg0, String arg1, String arg2)
					throws SAXException {
				super.notationDecl(arg0, arg1, arg2);
			}

			@Override
			public void processingInstruction(String arg0, String arg1)
					throws SAXException {
				assertEquals("pi", arg0);
				assertEquals("a b c", arg1);
			}

			// public InputSource resolveEntity(String arg0, String arg1)
			// throws SAXException {
			// return super.resolveEntity(arg0, arg1);
			// }

			@Override
			public void setDocumentLocator(Locator arg0) {
				super.setDocumentLocator(arg0);
			}

			@Override
			public void skippedEntity(String arg0) throws SAXException {
				super.skippedEntity(arg0);
			}

			@Override
			public void startDocument() throws SAXException {
				super.startDocument();
			}

			@Override
			public void startElement(String arg0, String arg1, String arg2,
					Attributes arg3) throws SAXException {
				super.startElement(arg0, arg1, arg2, arg3);
			}

			@Override
			public void startPrefixMapping(String arg0, String arg1)
					throws SAXException {
				super.startPrefixMapping(arg0, arg1);
			}

			@Override
			public void unparsedEntityDecl(String arg0, String arg1,
					String arg2, String arg3) throws SAXException {
				super.unparsedEntityDecl(arg0, arg1, arg2, arg3);
			}

			@Override
			public void warning(SAXParseException arg0) throws SAXException {
				super.warning(arg0);
			}

		};

		p
				.parse(
						new InputSource(
								new StringReader(
										"<?xml version='1.0'?><?pi a b c?><root xmlns:a='nsa' att1='val1' a:att1='a:val1'>hello, world!</root>")),
						h);
	}
}
