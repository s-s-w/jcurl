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

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PieceWiseRealFunction implements
        DifferentiableUnivariateRealFunction {

    private static class IntervalFunction extends Interval {

        private static final long serialVersionUID = -8971107137384039167L;

        final UnivariateRealFunction f;

        public IntervalFunction(final double min, final boolean min_included,
                final double max, final boolean max_included,
                UnivariateRealFunction f) {
            super(min, min_included, max, max_included);
            this.f = f;
        }

        public IntervalFunction(Interval i, UnivariateRealFunction f) {
            this(i.min, i.min_included, i.max, i.max_included, f);
        }
    }

    private final ArrayList intervals = new ArrayList();

    public PieceWiseRealFunction() {

    }

    public void add(final double min, final boolean min_included,
            final double max, final boolean max_included,
            final UnivariateRealFunction f) {
        final IntervalFunction i = new IntervalFunction(min, min_included, max,
                max_included, f);
        // TODO Check overlap!
        intervals.add(i);
        Collections.sort(intervals);
    }

    public void add(final Interval i, final UnivariateRealFunction f) {
        add(i.min, i.min_included, i.max, i.max_included, f);
    }

    public UnivariateRealFunction derivative() {
        return null;
    }

    public double value(double arg0) throws FunctionEvaluationException {
        return 0;
    }

}
