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
package org.jcurl.core.impl;

import java.awt.geom.Point2D;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.helpers.MutableObject;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Data model for broom location, rock index, rotation (counter-/clockwise) and
 * initial speed.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class BroomPromptModel extends MutableObject {
	private static final Log log = JCLoggerFactory
			.getLogger(BroomPromptModel.class);
	private static final long serialVersionUID = 4808528753885429987L;
	private Point2D broom = new Point2D.Float(0, 0);
	private int idx16 = -1;
	private boolean outTurn = true;
	private BoundedRangeModel splitTimeMillis = null;

	public BroomPromptModel() {
		setSplitTimeMillis(new DefaultBoundedRangeModel(2000, 0, 1000, 3000));
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BroomPromptModel other = (BroomPromptModel) obj;
		if (broom == null) {
			if (other.broom != null)
				return false;
		} else if (!broom.equals(other.broom))
			return false;
		if (idx16 != other.idx16)
			return false;
		if (outTurn != other.outTurn)
			return false;
		if (splitTimeMillis == null) {
			if (other.splitTimeMillis != null)
				return false;
		} else if (!splitTimeMillis.equals(other.splitTimeMillis))
			return false;
		return true;
	}

	public Point2D getBroom() {
		return broom;
	}

	public int getIdx16() {
		return idx16;
	}

	public boolean getOutTurn() {
		return outTurn;
	}

	public BoundedRangeModel getSplitTimeMillis() {
		return splitTimeMillis;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (broom == null ? 0 : broom.hashCode());
		result = prime * result + idx16;
		result = prime * result + (outTurn ? 1231 : 1237);
		result = prime * result
				+ (splitTimeMillis == null ? 0 : splitTimeMillis.hashCode());
		return result;
	}

	public void setBroom(final Point2D broom) {
		final Point2D old = this.broom;
		this.broom = broom;
		firePropertyChange("broom", old, this.broom);
	}

	public void setIdx16(int idx16) {
		if (idx16 <= -1 || idx16 >= RockSet.ROCKS_PER_SET)
			idx16 = -1;
		final int old = this.idx16;
		this.idx16 = idx16;
		firePropertyChange("idx16", old, this.idx16);
	}

	public void setOutTurn(final boolean outTurn) {
		final boolean old = this.outTurn;
		this.outTurn = outTurn;
		firePropertyChange("outTurn", old, this.outTurn);
	}

	public void setSplitTimeMillis(final BoundedRangeModel splitTimeMillis) {
		final BoundedRangeModel old = this.splitTimeMillis;
		this.splitTimeMillis = splitTimeMillis;
		firePropertyChange("splitTimeMillis", old, this.splitTimeMillis);
	}
}