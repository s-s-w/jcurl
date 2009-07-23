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
package org.jcurl.math.dom;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple parser for arithmetic expressions. Capable of literals, parameters,
 * functions, parenthesises and the operators
 * <p>
 * Binary:
 * <ul>
 * <li>=
 * <li>+
 * <li>-
 * <li>*
 * <li>/
 * </ul>
 * <p>
 * Unary:
 * <ul>
 * <li>-
 * </ul>
 * <p>
 * Inspired by the calc example in "The C++ Programming Language, Bjarne
 * Stroustup, Addison Wesley, 1997".
 * 
 * @see org.jcurl.math.dom.MathDom
 * @see org.jcurl.math.dom.ParserInfix
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:ParserInfixRegExp.java 473 2007-03-26 12:24:21Z mrohrmoser $
 */
class ParserInfixRegExp {

	private static final int ASSIGN = '=';

	private static final Pattern binaryPat = Pattern.compile("[-+/*^=]");

	private static final int DIV = '/';

	private static final int END = 3;

	private static final Pattern floatPat = Pattern
			.compile("-?[0-9]+(.[0-9]*(e-?[0-9]+)?)?");

	private static final int LP = '(';

	private static final int MINUS = '-';

	private static final int MUL = '*';

	private static final int NAME = 0;

	private static final int NONE = 2;

	private static final int NUMBER = 1;

	private static final int PLUS = '+';

	private static final int RP = ')';

	private static final Pattern unaryPat = Pattern.compile("-");

	public static MathDom.Node parse(final PushbackReader cin)
			throws IOException, ParseException {
		return new ParserInfixRegExp(cin).parseInfix();
	}

	public static MathDom.Node parse(final Reader cin) throws IOException,
			ParseException {
		return parse(new PushbackReader(cin));
	}

	public static MathDom.Node parse(final String cin) throws ParseException {
		try {
			return parse(new StringReader(cin));
		} catch (final IOException e) {
			throw new RuntimeException("Couldn't read from string.", e);
		}
	}

	private final PushbackReader cin;

	private int curr_tok = NONE;

	private double number_value = 0;

	private int pos = 0;

	private final StringBuilder string_value = new StringBuilder();

	public ParserInfixRegExp(final PushbackReader cin) {
		this.cin = cin;
	}

	private MathDom.Node expr(final boolean get) throws IOException,
			ParseException {
		MathDom.Node left = term(get);
		for (;;)
			switch (curr_tok) {
			case PLUS:
			case MINUS:
				left = new MathDom.BinaryOp((char) curr_tok, left, term(true));
				break;
			default:
				return left;
			}
	}

	private int get_token() throws IOException, ParseException {
		int ch = 0;
		do {
			ch = cin.read();
			pos++;
			if (-1 == ch)
				return curr_tok = END;
		}
		while (Character.isWhitespace((char) ch));
		switch (ch) {
		case 65535:
		case -1:
			return curr_tok = END;
		case '*':
		case '/':
		case '+':
		case '-':
		case '(':
		case ')':
		case '=':
			return curr_tok = ch;
		default:
			if (parseNameOrLiteral(ch))
				return curr_tok;
			throw new ParseException("bad token", pos);
		}
	}

	public MathDom.Node parseInfix() throws IOException, ParseException {
		get_token();
		if (curr_tok == END)
			return null;
		return expr(false);
	}

	/**
	 * read a number or name.
	 * 
	 * @param ch
	 * @return false: neither found.
	 * @throws IOException
	 */
	private boolean parseNameOrLiteral(int ch) throws IOException {
		if (Character.isLetterOrDigit((char) ch)) {
			string_value.setLength(0);
			string_value.append((char) ch);
			for (;;) {
				ch = cin.read();
				pos++;
				if (-1 == ch || !Character.isLetterOrDigit((char) ch))
					break;
				string_value.append((char) ch);
			}
			cin.unread(ch);
			final String s = string_value.toString();
			final Matcher mat = floatPat.matcher(s);
			if (mat.matches()) {
				number_value = Double.parseDouble(s);
				curr_tok = NUMBER;
			} else
				curr_tok = NAME;
			return true;
		}
		return false;
	}

	private MathDom.Node prim(final boolean get) throws IOException,
			ParseException {
		if (get)
			get_token();
		switch (curr_tok) {
		case NUMBER:
			get_token();
			return new MathDom.Literal(number_value);
		case NAME:
			final String p = string_value.toString();
			get_token();
			switch (curr_tok) {
			case ASSIGN:
				return new MathDom.BinaryOp((char) curr_tok,
						new MathDom.Parameter(p), expr(true));
			case LP:
				final MathDom.Node v3 = new MathDom.Function(p, expr(true));
				if (curr_tok != RP)
					throw new ParseException(") expected", pos);
				get_token();
				return v3;
			}
			return new MathDom.Parameter(p);
		case MINUS:
			return new MathDom.UnaryOp((char) curr_tok, prim(true));
		case LP:
			final MathDom.Node e = new MathDom.Block(expr(true));
			if (curr_tok != RP)
				throw new ParseException(") expected", pos);
			get_token();
			return e;
		default:
			throw new ParseException("primary expected", pos);
		}
	}

	private MathDom.Node term(final boolean get) throws IOException,
			ParseException {
		MathDom.Node left = prim(get);
		for (;;)
			switch (curr_tok) {
			case MUL:
			case DIV:
				left = new MathDom.BinaryOp((char) curr_tok, left, prim(true));
				break;
			default:
				return left;
			}
	}
}