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

import org.apache.commons.logging.Log;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.math.analysis.DifferentiableCurve;
import org.jcurl.math.linalg.MathVec;

public class DistanceSq implements DifferentiableUnivariateRealFunction {

    private final DifferentiableCurve c1;

    private final DifferentiableCurve c2;

    transient private DistanceSq derived = null;

    transient private final double[] tmp1;

    transient private final double[] tmp2;

    public DistanceSq(DifferentiableCurve c1, DifferentiableCurve c2) {
        this(c1, c2, null, null);
    }

    public DistanceSq(DifferentiableCurve c1, DifferentiableCurve c2,
            final double[] tmp1, final double[] tmp2) {
        this.tmp1 = tmp1;
        this.tmp2 = tmp2;
        this.c1 = c1;
        this.c2 = c2;
        if (c1.getDimension() != c2.getDimension())
            throw new IllegalArgumentException("Dimension mismatch: "
                    + c1.getDimension() + "!=" + c2.getDimension());
    }

    public UnivariateRealFunction derivative() {
        if (derived == null)
            this.derived = new DistanceSq(this.c1.derivative(), this.c2
                    .derivative());
        return derived;
    }

    public double value(double t) throws FunctionEvaluationException {
        return value(t, tmp1, tmp2);
    }

    private static final Log log = JCLoggerFactory.getLogger(DistanceSq.class);

    public double value(double t, double[] tmp1, double[] tmp2)
            throws FunctionEvaluationException {
        final double[] x1 = c1.value(t, tmp1);
        final double[] x2 = c2.value(t, tmp2);
        final double ret = MathVec.distSq(x1, x2, x2);
        if (log.isInfoEnabled())
            log.info("t=" + t + ", distsq(" + MathVec.toString(x1) + ", "
                    + MathVec.toString(x2) + "=" + ret);
        return ret;
    }
}