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
package jcurl.math.dom;

/**
 * Simple DOM for arithmetic expressions. Capable of literals, parameters (R1),
 * functions, parenthesises, unary and binary operators.
 * 
 * @see jcurl.math.dom.ParserInfix
 * @see jcurl.math.dom.DomWalker
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class MathDom {

    public static class BinaryOp extends Operator {

        public final Node left;

        public final Node right;

        public BinaryOp(final char op, final Node left, final Node right) {
            super(op);
            this.left = left;
            this.right = right;
        }
    }

    public static class Block extends Node {

        public final Node arg;

        public Block(final Node arg) {
            this.arg = arg;
        }
    }

    public static class Function extends Node {

        public final Node arg;

        public final String name;

        public Function(final String name, final Node arg) {
            this.name = name;
            this.arg = arg;
        }
    }

    public static class Integer extends Literal {
        public final int ival;

        public Integer(final int val) {
            super(val);
            this.ival = val;
        }
    }

    public static class Literal extends Value {
        public final double val;

        public Literal(final double val) {
            this.val = val;
        }
    }

    public static abstract class Node {
    }

    public abstract static class Operator extends Node {
        public final char op;

        public Operator(final char op) {
            this.op = op;
        }
    }

    public static class Parameter extends Value {
        public final String name;

        public Parameter(final String name) {
            this.name = name;
        }
    }

    public static class UnaryOp extends Operator {

        public final Node arg;

        public UnaryOp(final char op, final Node arg) {
            super(op);
            this.arg = arg;
        }
    }

    public static abstract class Value extends Node {
    }
}