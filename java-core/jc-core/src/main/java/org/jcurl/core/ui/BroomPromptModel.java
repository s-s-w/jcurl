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

	Point2D getBroom();

	int getIdx16();

	boolean getOutTurn();

	BoundedRangeModel getSplitTimeMillis();

	boolean getValueIsAdjusting();

	void setBroom(final Point2D broom);

	void setIdx16(int idx16);

	void setOutTurn(final boolean outTurn);

	void setSplitTimeMillis(final BoundedRangeModel splitTimeMillis);

	void setValueIsAdjusting(final boolean valueIsAdjusting);

}