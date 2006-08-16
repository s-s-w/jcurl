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
package jcurl.model;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class JCurlModel extends RockModelBase {
    private int focusIdx;

    private int selectedMask;

    public JCurlModel() {
    }

    public int getFocusIdx() {
        return focusIdx;
    }

    public int getSelectedMask() {
        return selectedMask;
    }

    /**
     * Triggers a {@link java.beans.PropertyChangeEvent}.
     * 
     * @param focusIdx
     */
    public void setFocusIdx(int focusIdx) {
        this.propChange.firePropertyChange("focusIdx", this.focusIdx, focusIdx);
        this.focusIdx = focusIdx;
    }

    /**
     * Triggers a {@link java.beans.PropertyChangeEvent}.
     * 
     * @param selectedMask
     */
    public void setSelectedMask(int selectedMask) {
        this.propChange.firePropertyChange("selectedMask", this.selectedMask,
                selectedMask);
        this.selectedMask = selectedMask;
    }
}