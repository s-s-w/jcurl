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
package org.jcurl.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.jcurl.core.helpers.NotImplementedYetException;

/**
 * A n-dimensional, continuous curve <code>R -&gt; R^n</code> based on
 * {@link org.jcurl.math.analysis.Function1D}s.
 * 
 * @see org.jcurl.math.analysis.Function1D
 * @see org.jcurl.math.analysis.CurveTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveFkt extends CurveGhost {

    private final DifferentiableUnivariateRealFunction[] fkt;

    public CurveFkt(final DifferentiableUnivariateRealFunction[] fkt) {
        super(fkt.length);
        this.fkt = new DifferentiableUnivariateRealFunction[dim];
        System.arraycopy(fkt, 0, this.fkt, 0, fkt.length);
    }

    public CurveFkt(final Polynome x, final Polynome y) {
        this(new Polynome[] { x, y });
    }

    public double getC(int dim, int c, double t)
            throws FunctionEvaluationException {
        DifferentiableUnivariateRealFunction f = fkt[dim];
        while (c-- > 0)
            f = (DifferentiableUnivariateRealFunction) f.derivative();
        return f.value(t);
    }

}