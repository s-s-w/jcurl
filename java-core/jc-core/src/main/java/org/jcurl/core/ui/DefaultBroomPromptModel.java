/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2009 M. Rohrmoser
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
package org.jcurl.core.ui;

import java.awt.geom.Point2D;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.event.ChangeListener;

import org.jcurl.core.api.MutableObject;
import org.jcurl.core.api.RockSet;

/**
 * Data model for broom location, rock index, rotation (counter-/clockwise) and
 * initial speed.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:BroomPromptModel.java 780 2008-03-18 11:06:30Z mrohrmoser $
 */
public class DefaultBroomPromptModel extends MutableObject implements
		BroomPromptModel {
	private static final long serialVersionUID = 4808528753885429987L;
	private Point2D broom = new Point2D.Float(0, 0);
	private int idx16 = -1;
	private boolean outTurn = true;
	private BoundedRangeModel splitTimeMillis = null;

	private boolean valueIsAdjusting = false;

	public DefaultBroomPromptModel() {
		setSplitTimeMillis(new DefaultBoundedRangeModel(2000, 0, 1000, 5000));
	}

	public void addChangeListener(final ChangeListener x) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DefaultBroomPromptModel other = (DefaultBroomPromptModel) obj;
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

	public boolean getValueIsAdjusting() {
		return valueIsAdjusting;
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

	public void removeChangeListener(final ChangeListener x) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	public void setBroom(final Point2D broom) {
		final Point2D old = this.broom;
		this.broom = broom;
		firePropertyChange("broom", old, this.broom);
	}

	public void setIdx16(int idx16) {
		if (idx16 <= -1 || idx16 >= RockSet.ROCKS_PER_SET)
			idx16 = -1;
		if (this.idx16 == idx16)
			return;
		final int old = this.idx16;
		this.idx16 = idx16;
		firePropertyChange("idx16", old, this.idx16);
	}

	public void setOutTurn(final boolean outTurn) {
		final boolean old = this.outTurn;
		if (old == outTurn)
			return;
		this.outTurn = outTurn;
		firePropertyChange("outTurn", old, this.outTurn);
	}

	public void setSplitTimeMillis(final BoundedRangeModel splitTimeMillis) {
		final BoundedRangeModel old = this.splitTimeMillis;
		if (old == splitTimeMillis)
			return;
		this.splitTimeMillis = splitTimeMillis;
		firePropertyChange("splitTimeMillis", old, this.splitTimeMillis);
	}

	public void setValueIsAdjusting(final boolean valueIsAdjusting) {
		final boolean old = this.valueIsAdjusting;
		if (old == valueIsAdjusting)
			return;
		this.valueIsAdjusting = valueIsAdjusting;
		firePropertyChange("valueIsAdjusting", old, this.valueIsAdjusting);
	}
}