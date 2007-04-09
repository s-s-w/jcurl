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
package org.jcurl.math;

/**
 * Multidimensional curves of polynomes.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: Polynome.java 483 2007-03-30 17:56:46Z mrohrmoser $
 */
public class PolynomeCurve extends R1RNFunctionImpl {

    private static final long serialVersionUID = 7503158531478359260L;

    private final double[][] params;

    public PolynomeCurve(final double[][] params) {
        super(params.length);
        this.params = params;
    }

    public PolynomeCurve(final Polynome[] polys) {
        super(polys.length);
        params = new double[polys.length][];
        for (int i = params.length - 1; i >= 0; i--)
            params[i] = polys[i].params;
    }

    @Override
    public double at(final int dim, final int c, final double t) {
        return Polynome.poly(c, t, params[dim]);
    }

}