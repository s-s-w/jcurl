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
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.jcurl.math.analysis.Polynome;

/**
 * Curling rock propagation model published by Mark Denny, Can. J. Phys. Vol.
 * 76, 1998
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class DennyModel extends IceModel {

    /**
     * Angular Handle position.
     */
    public class DennyAlphaDiff0 implements
            DifferentiableUnivariateRealFunction {

        public UnivariateRealFunction derivative() {
            return new DennyAlphaDiff1();
        }

        public double value(double t) throws FunctionEvaluationException {

            double tmp = Math.exp(1 - (g * mu * t / v0))
                    / Math.log(1 / epsilon);
            return -epsilon * omega0 * (v0 * tmp - g * mu * t * tmp - v0)
                    / ((epsilon + 1) * g * mu);
        }
    }

    /**
     * Angular Handle speed.
     */
    public class DennyAlphaDiff1 implements
            DifferentiableUnivariateRealFunction {
        public UnivariateRealFunction derivative() {
            return new DennyAlphaDiff2();
        }

        public double value(double t) throws FunctionEvaluationException {
            double tmp = Math.exp(1 - (g * mu * t / v0))
                    / Math.log(1 / epsilon);
            return omega0 * tmp;
        }
    }

    /**
     * Angular Handle acceleration.
     */
    public class DennyAlphaDiff2 implements
            DifferentiableUnivariateRealFunction {
        public UnivariateRealFunction derivative() {
            return null;
        }

        public double value(double t) throws FunctionEvaluationException {
            double tmp = Math.exp(1 - (g * mu * t / v0))
                    / Math.log(1 / epsilon);
            return -g * mu * omega0 * tmp / (epsilon * (v0 - g * mu * t));
        }
    }

    private static final double bLR = 0.003;

    private static final double g = 9.81;

    private static final double R = 6.3e-2;

    private double curl;

    private double epsilon = 0;

    private double mu = 0;

    private double omega0 = 0;

    private double tee;

    private double v0 = 0;

    public JCurlPathSegment computePath(final double t0,
            final double[] x0, final double[] v0) {
        return null;
    }
    
    
    public DifferentiableUnivariateRealFunction[] compute(final double t0,
            final double[] x0, final double[] v0) {

        final double v02 = v0[0] * v0[0] + v0[1] * v0[1];
        this.v0 = Math.sqrt(v02);
        this.omega0 = v0[2];
        final double tau = this.v0 / (mu * g);

        double tmp = -bLR * v02 / (4 * epsilon * R * tau);
        final DifferentiableUnivariateRealFunction[] ret = new DifferentiableUnivariateRealFunction[3];
        ret[0] = new Polynome(
                new double[] { 0, 0, 0, tmp / 3, -tmp / (4 * tau) });
        tmp = Math.sqrt(v02);
        ret[1] = new Polynome(new double[] { 0, tmp, -tmp / (2 * tau) });

        return ret;
    }

    public void init(final double tee, final double curl) {
        this.tee = tee;
        this.curl = curl;
        this.mu = 0.0127;
        this.epsilon = 2.63;
    }

}
