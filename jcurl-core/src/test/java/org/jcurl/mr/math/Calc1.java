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
package org.jcurl.mr.math;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:Calc1.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class Calc1 {

    private static class Worker {

        public double assign(final String name, final double arg) {
            return arg;
        }

        public double binaryOp(final int op, final double left,
                final double right) {
            switch (op) {
            case '*':
                return left * right;
            case '/':
                return left / right;
            case '+':
                return left + right;
            case '-':
                return left - right;
            default:
                throw new IllegalArgumentException("Unknown operator [" + op
                        + "]");
            }
        }

        public double block(final double arg) {
            return arg;
        }

        public double function(final String name, final double arg) {
            return Math.abs(arg);
        }

        public double getNameVal(final String p) {
            return 0;
        }

        public double name(final String name) {
            return 0;
        }

        public double unaryOp(final int op, final double arg) {
            switch (op) {
            case '-':
                return -arg;
            default:
                throw new IllegalArgumentException("Unknown operator [" + op
                        + "]");
            }
        }
    }

    private static final int ASSIGN = '=';

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

    private final PushbackReader cin;

    private int curr_tok = NONE;

    private double number_value = 0;

    private final StringBuffer string_value = new StringBuffer();

    private final Worker w = new Worker();

    public Calc1(final Reader cin) {
        this.cin = new PushbackReader(cin);
    }

    public Calc1(final String cin) {
        this(new StringReader(cin));
    }

    public double compute() throws IOException {
        get_token();
        if (curr_tok == END)
            return 0;
        return expr(false);
    }

    private double error(String s) {
        System.err.println(s);
        return 1;
    }

    private double expr(boolean get) throws IOException {
        double left = term(get);
        for (;;)
            switch (curr_tok) {
            case PLUS:
            case MINUS:
                left = w.binaryOp(curr_tok, left, term(true));
                break;
            default:
                return left;
            }
    }

    private int get_token() throws IOException {
        int ch = 0;
        do
            if (-1 == (ch = cin.read()))
                return curr_tok = END;
        while (ch != '\n' && Character.isWhitespace((char) ch));
        switch (ch) {
        case 0:
            return curr_tok = END;
        case '*':
        case '/':
        case '+':
        case '-':
        case '(':
        case ')':
        case '=':
            return curr_tok = ch;
        case ';':
        case '\n':
            return curr_tok = END;
        default:
            if (parse(ch))
                return curr_tok;
            error("bad token");
            return curr_tok = END;
        }
    }

    /**
     * read a number or name.
     * 
     * @param ch
     * @return false: neither found.
     * @throws IOException
     */
    private boolean parse(int ch) throws IOException {
        if (Character.isLetterOrDigit((char) ch)) {
            string_value.setLength(0);
            string_value.append((char) ch);
            while (-1 != (ch = cin.read())
                    && Character.isLetterOrDigit((char) ch))
                string_value.append((char) ch);
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

    private double prim(boolean get) throws IOException {
        if (get)
            get_token();
        switch (curr_tok) {
        case NUMBER:
            final double v = number_value;
            get_token();
            return v;
        case NAME:
            final String p = string_value.toString();
            get_token();
            switch (curr_tok) {
            case ASSIGN:
                return w.assign(p, expr(true));
            case LP:
                double v3 = w.function(p, expr(true));
                if (curr_tok != RP)
                    return error(") expected");
                get_token();
                return v3;
            }
            return w.getNameVal(p);
        case MINUS:
            return w.unaryOp(curr_tok, prim(true));
        case LP:
            final double e = w.block(expr(true));
            if (curr_tok != RP)
                return error(") expected");
            get_token();
            return e;
        default:
            return error("primary expected");
        }
    }

    double term(boolean get) throws IOException {
        double left = prim(get);
        for (;;)
            switch (curr_tok) {
            case MUL:
            case DIV:
                left = w.binaryOp(curr_tok, left, prim(true));
                break;
            default:
                return left;
            }
    }
}