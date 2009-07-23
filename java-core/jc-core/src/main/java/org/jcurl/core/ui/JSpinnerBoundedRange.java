/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

/**
 * Provide a {@link BoundedRangeModel}-based {@link JSpinner} supporting
 * {@link #addFocusListener(FocusListener)} &amp; co.
 * <p>
 * See <a
 * href="http://forum.java.sun.com/thread.jspa?threadID=409748&forumID=57">why
 * this is worth mentioning</a>.
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class JSpinnerBoundedRange extends JSpinner implements FocusListener {
	/**
	 * Wraps a {@link BoundedRangeModel} under the interface of a
	 * {@link SpinnerNumberModel}.
	 * 
	 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
	 * @version $Id$
	 */
	static class SpinnerModelWrapper extends SpinnerNumberModel {
		private static final long serialVersionUID = 2503438304002446476L;
		private final BoundedRangeModel wrap;

		public SpinnerModelWrapper(final BoundedRangeModel wrap) {
			this(wrap, 50);
		}

		public SpinnerModelWrapper(final BoundedRangeModel wrap, final int dt) {
			super(wrap.getValue(), wrap.getMinimum(), wrap.getMaximum(), dt);
			this.wrap = wrap;
		}

		@Override
		public void addChangeListener(final ChangeListener l) {
			wrap.addChangeListener(l);
		}

		@Override
		public Object getValue() {
			return wrap.getValue();
		}

		@Override
		public void removeChangeListener(final ChangeListener l) {
			wrap.removeChangeListener(l);
		}

		@Override
		public void setValue(final Object value) {
			wrap.setValue(((Number) value).intValue());
			super.setValue(wrap.getValue());
		}
	}

	private static final long serialVersionUID = 5372258995350073766L;

	public JSpinnerBoundedRange() {
		super(new SpinnerNumberModel());
	}

	/** internal use only! */
	public void focusGained(final FocusEvent e) {
		final FocusEvent fe = new FocusEvent(this, e.getID(), e.isTemporary(),
				e.getOppositeComponent());
		for (final FocusListener f : getFocusListeners())
			f.focusGained(fe);
	}

	/** internal use only! */
	public void focusLost(final FocusEvent e) {
		final FocusEvent fe = new FocusEvent(this, e.getID(), e.isTemporary(),
				e.getOppositeComponent());
		for (final FocusListener f : getFocusListeners())
			f.focusLost(fe);
	}

	public BoundedRangeModel getBRM() {
		throw new UnsupportedOperationException();
	}

	private DefaultEditor getEd() {
		return (JSpinner.DefaultEditor) super.getEditor();
	}

	/** internal use only! */
	@Override
	public JComponent getEditor() {
		return super.getEditor();
	}

	/** internal use only! */
	@Override
	public SpinnerModel getModel() {
		return super.getModel();
	}

	public void setBRM(final BoundedRangeModel model) {
		getEd().getTextField().removeFocusListener(this);
		super.setModel(new JSpinnerBoundedRange.SpinnerModelWrapper(model));
		getEd().getTextField().addFocusListener(this);
	}

	/** internal use only! */
	@Override
	public void setEditor(final JComponent editor) {
		super.setEditor(editor);
	}

	@Override
	public void setModel(final SpinnerModel model) {
		throw new UnsupportedOperationException("use setBRM");
	}
}