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
package jcurl.sim.model;

import jcurl.core.NotImplementedYetException;
import jcurl.core.Rock;
import jcurl.sim.core.CollissionStrategy;

/**
 * A hitter including spin and loss of energy.
 * <p>
 * This hitter gets the transfer of spin and momentum right. The 'loss of
 * energy'-mechanism bases upon a Hook'sch elasticity-model for the rocks. Only
 * stillstanding rocks cause a loss of energy. For details, mail to <a
 * href="mailto:jcurl@gmx.net">.jcurl@gmx.net </a>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CollissionSpinLoss extends CollissionStrategy {

    //        /*
    //         * (non-Javadoc)
    //         *
    //         * @see jcurl.sim.core.CollissionStrategy#compute(jcurl.core.Rock,
    //         * jcurl.core.Rock, jcurl.core.Rock, jcurl.core.Rock)
    //         */
    //    public boolean compute(Rock xa, Rock xb, Rock va, Rock vb) {
    //        // vector from i[ f]'s center to i[!f]'s:
    //	    double2d r( S.loc(i[!f]).getx() - S.loc(i[ f]).getx(),
    //			  S.loc(i[!f]).gety() - S.loc(i[ f]).gety() );
    //	    double tmp = r.abs();
    //	    if( !(S.speed(i[ f]).nonzero() || S.speed(i[!f]).nonzero()) ||
    //		tmp > RADIUS+RADIUS+HIT_MAX_DIST )
    //		return false;
    //
    //	    if( S.speed(i[ 0]).nonzero() + S.speed(i[ 1]).nonzero() == 1 ) {
    //		if(0!=(f = S.speed(i[ 1]).nonzero()))
    //		    tmp = -tmp;
    //	    }
    //
    //	    // get the coordinate-system:
    //	    final double2d eY( r /= tmp );
    //	    final double2d eX( (eY[1]), -(eY[0]) );
    //
    //	    // from here you can reuse 'r' and 'tmp'
    //
    //	    // do the coordinate-trafo world->new:
    //	    // va[0] = eX[0] * Va[0] + eX[1] * Va[1];
    //	    // va[1] = eY[0] * Va[0] + eY[1] * Va[1];
    //	    double2d va( eX[0] * S.speed(i[ f]).getx() +
    //			   eX[1] * S.speed(i[ f]).gety(),
    //			   eY[0] * S.speed(i[ f]).getx() +
    //			   eY[1] * S.speed(i[ f]).gety() );
    //	    double2d vb( eX[0] * S.speed(i[!f]).getx() +
    //			   eX[1] * S.speed(i[!f]).gety(),
    //			   eY[0] * S.speed(i[!f]).getx() +
    //			   eY[1] * S.speed(i[!f]).gety() );
    //	    double w[2];
    //	    w[0] = S.speed(i[ f]).getangle();
    //	    w[1] = S.speed(i[!f]).getangle();
    //
    //	    if( S.speed(i[ f]).nonzero() + S.speed(i[!f]).nonzero() == 1 )
    //		singleLoss(va, w[0], vb, w[1]);
    //	    singleNoLoss(va, w[0], vb, w[1]);
    //
    //	    S.speed(i[ f]).setangle( w[0] );
    //	    S.speed(i[!f]).setangle( w[1] );
    //
    //	    // re-transformation:
    //	    // do the coordinate-trafo new->world:
    //	    // Va[0] = eX[0] * va[0] + eY[0] * va[1];
    //	    // Va[1] = eX[1] * va[0] + eY[1] * va[1];
    //	    S.speed(i[ f]).setx( eX[0] * va[0] + eY[0] * va[1] );
    //	    S.speed(i[ f]).sety( eX[1] * va[0] + eY[1] * va[1] );
    //	    S.speed(i[!f]).setx( eX[0] * vb[0] + eY[0] * vb[1] );
    //	    S.speed(i[!f]).sety( eX[1] * vb[0] + eY[1] * vb[1] );
    //	    return true;
    //    }
    //
    //    private double U;
    //    private double mu;
    //	    /**
    //         * The friction granite/granite. Set the parameter for friction granite/granite.
    //         *
    //         * @param v
    //         * the value
    //         */
    //void setFricRockRock(final double v) {
    //    mu = v;
    //}
    //
    //	    /**
    //         * The loss of energy on raises. Set the parameter for the loss of
    //         * energy raises suffer.
    //         *
    //         * @param v
    //         * [Joule] the value
    //         */
    //void setLoss(final double v) {
    //    U = v / MASS;
    //}
    //
    //
    //void singleLoss(double2d& va, double& wa, double2d&, double&) {
    //    final double I = 2.0 * (1.0/MASS + sqr(RADIUS) / INERTIA);
    //    final double FHdivOmega = sqrt( 2.0 * MASS * Loss() );
    //
    //    // A's real surface-speed:
    //    final double Veff = va[0] - RADIUS * wa;
    //
    //    // t0 is the time when A's effective surface speed becomes 0 (=B's)
    //    double cost0;
    //    cost0 = 1.0-fabs(Veff / (mu*va[1]* I * MASS));
    //    if( 0.0 > cost0 ) cost0 = 0.0;
    //    assert( cost0 <= 1.0 );
    //
    //    double2d dv( sgn(Veff)*mu, 1.0 );
    //    // sint1 is the time when the Hook-force equals the friction.
    //    double sint1;
    //
    //    sint1 = FHdivOmega / (MASS * va[1] * dv.abs());
    //    if( sint1 > 1.0 ) sint1 = 1.0;
    //    assert( 0.0 <= sint1 );
    //
    //    if( 1.0-sqr(cost0) < sqr(sint1) || (cost0==0.0 && sint1==1.0) ) {
    //	// surface speed becomes 0 before the friction is killed:
    //	// => our force points in || direction only, but the loss of momentum
    //	// does not!
    //	dv[0] = 0; dv[1] = 1.0;
    //
    //	sint1 = FHdivOmega / (MASS * va[1] * dv.abs());
    //	if( sint1 > 1.0 ) sint1 = 1.0;
    //	assert( 0.0 <= sint1 );
    //
    //	// the loss of momentum in perp. direction:
    //	dv[0] = -sgn(Veff) * mu * va[1] * fabs( cost0 - 1.0 );
    //	dv[1] = va[1] * ( sqrt( 1.0 - sqr( sint1 ) ) - 1.0 );
    //    } else {
    //	dv *= va[1] * ( sqrt( 1.0 - sqr( sint1 ) ) - 1.0 );
    //    }
    //    assert( sgn(dv[0]) == -sgn(Veff) );
    //    assert( dv[1] <= 0.0 );
    //
    //    va += dv;
    //    wa -= dv[0] * MASS * RADIUS / INERTIA;
    //
    //    return;
    //}
    //
    //private static final double MASS = RockProps.DEFAULT.getMass();
    //private static final double RADUIS = RockProps.DEFAULT.getRadius();
    //private static final double INERTIA = RockProps.DEFAULT.getInertia();
    //
    //void singleNoLoss(double2d& va, double wa, double2d& vb, double wb) {
    //    final double Veff = - vb[0] - RADIUS * wb
    //			+ va[0] - RADIUS * wa;
    //    final double I = 2.0 * (1.0/MASS +
    //			   sqr(RADIUS)/INERTIA
    //			);
    //    double tmp;
    //    va[1] += (tmp = vb[1] - va[1]);
    //    vb[1] -= tmp;
    //
    //    double X = - Veff / I;
    //    if( fabs(X) > (tmp=mu * MASS * fabs( tmp )) )
    //	X = -sgn(Veff) * tmp;
    //
    //    // apply the x-change:
    //    va[0] += (tmp = X / MASS);
    //    vb[0] -= tmp;
    //
    //    // the spin's change:
    //    wa -= (tmp = X * RADIUS / INERTIA);
    //    wb -= tmp;
    //
    //    return;
    //}

    /*
     * (non-Javadoc)
     * 
     * @see jcurl.sim.core.CollissionStrategy#compute(jcurl.core.Rock,
     *      jcurl.core.Rock, jcurl.core.Rock, jcurl.core.Rock)
     */
    public boolean compute(Rock xa, Rock xb, Rock va, Rock vb) {
        throw new NotImplementedYetException();
    }
}