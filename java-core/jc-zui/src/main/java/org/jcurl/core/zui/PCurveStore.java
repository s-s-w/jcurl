/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
package org.jcurl.core.zui;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.CurveStore;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.R1RNFunction;

import edu.umd.cs.piccolo.PNode;

/**
 * Manage a set of trajectories.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PCurveStore extends PNode implements PropertyChangeListener {

    private static final Log log = JCLoggerFactory.getLogger(PCurveStore.class);
    private static final long serialVersionUID = -7570887991507166006L;
    private final CurveStore cs;
    private final PTrajectoryFactory f;

    private final double tmax;

    public PCurveStore(final CurveStore cs, final PTrajectoryFactory f,
            final double tmax) {
        this.tmax = tmax;
        this.cs = cs;
        this.f = f;
        int i = 0;
        for (final Iterable<Entry<Double, R1RNFunction>> element : cs) {
            addChild(i, new PNode());
            // ensure initial rendering
            sync(i++, tmax);
        }
        cs.addPropertyChangeListener("curve", this);
    }

    public void propertyChange(final PropertyChangeEvent evt) {
        log.debug(evt);
        if (evt instanceof IndexedPropertyChangeEvent) {
            final IndexedPropertyChangeEvent e = (IndexedPropertyChangeEvent) evt;
            sync(e.getIndex(), tmax);
            return;
        }
    }

    private void sync(final int i16, final double tmax) {
        final PNode c = f.newInstance(i16, cs.iterator(i16), tmax);
        c.addAttribute(PPositionSet.index16, i16);
        getChild(i16).replaceWith(c);
    }
}
