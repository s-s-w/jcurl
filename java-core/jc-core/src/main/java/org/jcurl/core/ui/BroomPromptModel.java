/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
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

package org.jcurl.core.ui;

import java.awt.geom.Point2D;

import javax.swing.BoundedRangeModel;

import org.jcurl.core.api.IChangeSupport;
import org.jcurl.core.api.IPropertyChangeSupport;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public interface BroomPromptModel extends IPropertyChangeSupport,
		IChangeSupport {

	public class HandleMemento extends Memento<BroomPromptModel> {
		private final boolean outTurn;

		public HandleMemento(final BroomPromptModel context,
				final boolean outTurn) {
			super(context);
			this.outTurn = outTurn;
		}

		@Override
		public BroomPromptModel apply(final BroomPromptModel dst) {
			dst.setOutTurn(outTurn);
			return dst;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			final HandleMemento other = (HandleMemento) obj;
			if (outTurn != other.outTurn)
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + (outTurn ? 1231 : 1237);
			return result;
		}

		@Override
		public String toString() {
			return this.getClass().getName() + ": " + outTurn;
		}
	}

	public class IndexMemento extends Memento<BroomPromptModel> {
		private final int idx16;

		public IndexMemento(final BroomPromptModel context, final int idx16) {
			super(context);
			this.idx16 = idx16;
		}

		@Override
		public BroomPromptModel apply(final BroomPromptModel dst) {
			dst.setIdx16(idx16);
			return dst;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			final IndexMemento other = (IndexMemento) obj;
			if (idx16 != other.idx16)
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + idx16;
			return result;
		}

		@Override
		public String toString() {
			return this.getClass().getName() + ": " + idx16;
		}
	}

	public class SplitMemento extends Memento<BroomPromptModel> {
		private final int splitTime;

		public SplitMemento(final BroomPromptModel context, final int splitTime) {
			super(context);
			this.splitTime = splitTime;
		}

		@Override
		public BroomPromptModel apply(final BroomPromptModel dst) {
			dst.getSplitTimeMillis().setValue(splitTime);
			return dst;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			final SplitMemento other = (SplitMemento) obj;
			if (splitTime != other.splitTime)
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + splitTime;
			return result;
		}

		@Override
		public String toString() {
			return this.getClass().getName() + ": " + splitTime;
		}
	}

	public class XYMemento extends P2DMemento<BroomPromptModel> {
		public XYMemento(final BroomPromptModel context, final double x,
				final double y) {
			super(context, x, y);
		}

		public XYMemento(final BroomPromptModel context, final Point2D p) {
			super(context, p);
		}

		@Override
		public BroomPromptModel apply(final BroomPromptModel dst) {
			dst.setBroom(p);
			return dst;
		}
	}

	Point2D getBroom();

	int getIdx16();

	boolean getOutTurn();

	BoundedRangeModel getSplitTimeMillis();

	boolean getValueIsAdjusting();

	// void setBroom(double x, double y);

	void setBroom(Point2D broom);

	void setIdx16(int idx16);

	void setOutTurn(boolean outTurn);

	void setSplitTimeMillis(BoundedRangeModel splitTimeMillis);

	void setValueIsAdjusting(boolean valueIsAdjusting);

}