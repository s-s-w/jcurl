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
package org.jcurl.math.analysis;

import junit.framework.TestCase;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class PieceWiseRealFunctionTest extends TestCase {

    public void test010() {
        final UnivariateRealFunction sine = new UnivariateRealFunction() {
            public double value(double t) throws FunctionEvaluationException {
                return Math.sin(t);
            }
        };
        final PieceWiseRealFunction f = new PieceWiseRealFunction();
        f.add(Double.NEGATIVE_INFINITY, false, 1.0, false, sine);
        f.add(-1, false, 1.0, false, null);
        f.add(-3, false, -2, false, sine);
    }
}
