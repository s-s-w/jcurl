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
package org.jcurl.model;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.NewtonSolver;
import org.apache.commons.math.analysis.UnivariateRealSolver;
import org.jcurl.math.analysis.DifferentiableCurve;

public class HitFinder extends CurveSolver {

    private final DifferentiableUnivariateRealFunction distSq;

    private final UnivariateRealSolver solver;

    public HitFinder(DifferentiableCurve c1,
            DifferentiableCurve c2, double r) {
        this(c1, r, c2, r);
    }

    public HitFinder(DifferentiableCurve c1, double r1,
            DifferentiableCurve c2, double r2) {
        this.distSq = new DistanceSq(c1, c2);
        this.solver = new NewtonSolver(distSq);
    }

    public double solve(double min, double max) throws ConvergenceException,
            FunctionEvaluationException {
        return solver.solve(min, max);
    }

    public double solve(double min, double max, double start)
            throws ConvergenceException, FunctionEvaluationException {
        return solver.solve(min, max, start);
    }
}
