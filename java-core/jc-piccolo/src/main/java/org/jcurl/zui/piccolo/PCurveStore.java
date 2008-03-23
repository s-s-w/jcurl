/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
package org.jcurl.zui.piccolo;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.CurveStore;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.R1RNFunction;

import edu.umd.cs.piccolo.PNode;

/**
 * Manage a set of trajectories.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:PCurveStore.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public class PCurveStore extends PNode implements PropertyChangeListener {
    private static final Log log = JCLoggerFactory.getLogger(PCurveStore.class);
    private static final long serialVersionUID = -7570887991507166006L;
    private final PTrajectoryFactory f;
    private CurveStore model;
    private final double tmax;

    public PCurveStore(final PTrajectoryFactory f, final double tmax) {
        this.tmax = tmax;
        this.f = f;
        setModel(model);
    }

    public CurveStore getModel() {
        return model;
    }

    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt instanceof IndexedPropertyChangeEvent)
            sync(((IndexedPropertyChangeEvent) evt).getIndex());
    }

    public void setModel(final CurveStore curveStore) {
        if (model != null)
            model.removePropertyChangeListener(this);
        model = curveStore;
        setVisible(model != null);
        if (model != null) {
            sync();
            curveStore.addPropertyChangeListener("curve", this);
        }
    }

    private void sync() {
        if (model == null)
            return;
        int i = 0;
        for (final Iterable<Entry<Double, R1RNFunction>> element : model) {
            addChild(i, new PNode());
            // ensure initial rendering
            sync(i++);
        }
    }

    private void sync(final int i16) {
        if (model == null)
            return;
        final PNode c = f.newInstance(i16, model.iterator(i16), tmax);
        c.addAttribute(PRockNode.INDEX16, i16);
        getChild(i16).replaceWith(c);
    }
}
