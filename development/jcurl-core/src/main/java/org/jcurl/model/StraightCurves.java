/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.model;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.math.analysis.Polynome;
import org.jcurl.math.analysis.R1R1Function;

/**
 * Very simple model - mostly for testing reasons.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: DennyCurves.java 319 2006-05-30 01:29:19Z mrohrmoser $
 */
public class StraightCurves extends CurveFactory {

    /**
     * Angular Handle position.
     */
    public class StraightAlphaDiff0 implements R1R1Function {

        public UnivariateRealFunction derivative() {
            return new StraightAlphaDiff1();
        }

        public boolean isLinear() {
            return false;
        }

        public double value(double t) throws FunctionEvaluationException {
            throw new NotImplementedYetException();
        }
    }

    /**
     * Angular Handle speed.
     */
    public class StraightAlphaDiff1 implements R1R1Function {
        public UnivariateRealFunction derivative() {
            return new StraightAlphaDiff2();
        }

        public boolean isLinear() {
            return false;
        }

        public double value(double t) throws FunctionEvaluationException {
            throw new NotImplementedYetException();
        }
    }

    /**
     * Angular Handle acceleration.
     */
    public class StraightAlphaDiff2 implements R1R1Function {
        public UnivariateRealFunction derivative() {
            return null;
        }

        public boolean isLinear() {
            return false;
        }

        public double value(double t) throws FunctionEvaluationException {
            throw new NotImplementedYetException();
        }
    }

    private static final long serialVersionUID = 3289346496211556915L;

    private double mu = 0;

    private double omega0 = 0;

    private double v0 = 0;

    public StraightCurves() {
        this.mu = 0.0127;
    }

    R1R1Function[] compute(final double v0Square, final double w0) {
        throw new NotImplementedYetException();
    }

    public boolean equals(Object obj) {
        return false;
    }
}
