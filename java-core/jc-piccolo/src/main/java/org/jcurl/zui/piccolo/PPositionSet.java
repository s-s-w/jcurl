/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.zui.piccolo;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.PositionSet;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.log.JCLoggerFactory;

import edu.umd.cs.piccolo.PNode;

/**
 * Piccolo {@link PNode} backed with a {@link PositionSet}.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PPositionSet.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public class PPositionSet extends PNode implements ChangeListener {
	static final double eps = PRockNode.EPSILON;
	private static final Log log = JCLoggerFactory
			.getLogger(PPositionSet.class);
	private static final long serialVersionUID = 6564103045992326633L;
	private final PRockFactory f;
	private RockSet<Pos> model = null;

	/**
	 * Create a pickable child node for each rock and set it's attributes
	 * {@link PRockNode#INDEX16} and {@link PositionSet#getClass()} - yes the
	 * class object is the key.
	 * 
	 * @param f
	 */
	public PPositionSet(final PRockFactory f) {
		this.f = f;
		for (int i = 0; i < RockSet.ROCKS_PER_SET; i++)
			addChild(new PNode());
	}

	public RockSet<Pos> getModel() {
		return model;
	}

	public void setModel(final RockSet<Pos> positionSet) {
		if(model == positionSet)
			return;
		if (model != null)
			throw new UnsupportedOperationException();
		model = positionSet;
		setVisible(model != null);
		if (model != null) {
			for (int i = 0; i < RockSet.ROCKS_PER_SET; i++)
				getChild(i).replaceWith(
						f.newInstance(i, positionSet.getRock(i)));
		}
	}

	public void stateChanged(final ChangeEvent evt) {
		log.warn("Set update: " + evt);
		sync(model, this);
	}

	private void sync(final RockSet<Pos> src, final PPositionSet dst) {
		if (src == null)
			return;
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
			PNode c = dst.getChild(i);
			if (c instanceof PRockNode)
				PRockFactory.sync(src.getRock(i), (PRockNode) dst.getChild(i));
		}
	}
}
