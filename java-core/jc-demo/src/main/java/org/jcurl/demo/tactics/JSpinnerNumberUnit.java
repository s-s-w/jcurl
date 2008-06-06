package org.jcurl.demo.tactics;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.ChangeSupport;
import org.jcurl.core.api.IChangeSupport;
import org.jcurl.core.api.Unit;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Combine a numerical {@link JSpinner} and a {@link Unit} {@link JComboBox}.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class JSpinnerNumberUnit extends JComponent implements ItemListener,
		FocusListener, ChangeListener, IChangeSupport {

	private static final Log log = JCLoggerFactory
			.getLogger(JSpinnerNumberUnit.class);

	private static final long serialVersionUID = 7309659176999252671L;

	private static double convert(final double v, final Unit src, final Unit dst) {
		if (src == dst || src == null || src == Unit.NONE || dst == null
				|| dst == Unit.NONE)
			return v;
		return src.convert(dst, v);
	}

	private static SpinnerNumberModel rescale(final Unit src, final Unit dst,
			final SpinnerNumberModel m) {
		if (m == null || dst == null || src == null || src == dst)
			return m;
		if (log.isDebugEnabled())
			log.debug("Convert " + src + " -> " + dst + ":" + m.getValue()
					+ " -> " + src.convert(dst, m.getNumber().doubleValue()));
		m.setValue(src.convert(dst, m.getNumber().doubleValue()));
		m.setMinimum(src.convert(dst, ((Number) m.getMinimum()).doubleValue()));
		m.setMaximum(src.convert(dst, ((Number) m.getMaximum()).doubleValue()));
		return m;
	}

	private Unit base = null;
	private final transient ChangeSupport change = new ChangeSupport(this);
	private Unit[] choose = new Unit[0];
	private final JComboBox combo;
	private final JLabel label;
	transient private double oldValueBase = Double.NaN;
	private Unit unit = null;

	private final JSpinner value;

	private boolean valueIsAdjusting;

	public JSpinnerNumberUnit() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(label = new JLabel("xxx"));
		add(value = new JSpinner());
		add(combo = new JComboBox(choose));
		combo.addItemListener(this);
		setBase(Unit.NONE);
		setChoose();
	}

	/** Register for {@link #getValue()} changes. */
	public void addChangeListener(final ChangeListener l) {
		change.addChangeListener(l);
	}

	/** internal use only! */
	public void focusGained(final FocusEvent e) {
		setValueIsAdjusting(true);
	}

	/** internal use only! */
	public void focusLost(final FocusEvent e) {
		setValueIsAdjusting(false);
	}

	/** {@link #getValue()} listeners. */
	public ChangeListener[] getChangeListeners() {
		return change.getChangeListeners();
	}

	private DefaultEditor getEd() {
		return (JSpinner.DefaultEditor) value.getEditor();
	}

	/** Allow custom layouting */
	JComponent getLabel() {
		return label;
	}

	/** The data model according to {@link #getUnit()}. */
	public SpinnerNumberModel getModel() {
		return (SpinnerNumberModel) value.getModel();
	}

	/** Allow custom layouting */
	JComponent getSpinner() {
		return value;
	}

	/** Currently active {@link Unit} */
	public Unit getUnit() {
		if (true)
			return unit;
		else {
			if (!combo.isVisible())
				return Unit.NONE;
			final Unit old = (Unit) combo.getSelectedItem();
			if (old == null)
				return Unit.NONE;
			return old;
		}
	}

	/** Allow custom layouting */
	JComponent getUnitCombo() {
		return combo;
	}

	/** The current value in according to the {@link #setBase(Unit)} unit. */
	public double getValue() {
		return convert(getModel().getNumber().doubleValue(), getUnit(), base);
	}

	/**
	 * Is {@link #getValue()} currently changing? ({@link JSpinner} has the
	 * focus)
	 */
	public boolean getValueIsAdjusting() {
		return change.getValueIsAdjusting();
	}

	/** internal use only. */
	public void itemStateChanged(final ItemEvent e) {
		if (e.getSource() == combo)
			setUnit((Unit) e.getItem());
	}

	/** Deregister for {@link #getValue()} changes. */
	public void removeChangeListener(final ChangeListener l) {
		change.removeChangeListener(l);
	}

	/** The unit for {@link #getValue()}. TODO Check compatibility with choose. */
	public void setBase(final Unit base) {
		final Unit old = this.base;
		if (old == base)
			return;
		if (choose == null || choose.length == 0)
			setChoose(base);
		firePropertyChange("base", old, this.base = base);
	}

	/**
	 * Alternative units.TODO Check compatibility with base.
	 * 
	 * @param choose
	 *            An empty list makes the {@link Unit} {@link JComboBox}
	 *            invisible.
	 */
	public void setChoose(final Unit... choose) {
		this.choose = choose;
		combo.setModel(new DefaultComboBoxModel(this.choose));
		combo.setVisible(this.choose != null && this.choose.length > 0);
		combo.setEnabled(combo.isVisible() && choose.length > 1);
		unit = (Unit) combo.getSelectedItem();
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (!enabled) {
			label.setEnabled(enabled);
			value.setEnabled(enabled);
			combo.setEnabled(enabled);
		} else {
			label.setEnabled(label.isVisible());
			value.setEnabled(value.isVisible());
			combo.setEnabled(combo.isVisible());
		}
		super.setEnabled(enabled);
	}

	/**
	 * @param label
	 *            <code>null</code> makes the text {@link JLabel} to
	 *            invisible.
	 */
	public void setLabel(final String label) {
		final String old = this.label.getText();
		if (old == label)
			return;
		this.label.setText(label);
		this.label.setVisible(label != null);
		firePropertyChange("label", old, label);
	}

	/**
	 * Mostly to set minimum and maximum values. Must be given in
	 * {@link #setBase(Unit)} units and is immediately converted to
	 * {@link #getUnit()}.
	 * 
	 * @see #rescale(Unit, Unit, SpinnerNumberModel)
	 * @param model
	 *            <code>null</code> makes the {@link JSpinner} invisible.
	 */
	public void setModel(final SpinnerNumberModel model) {
		final SpinnerNumberModel old = getModel();
		if (old == model)
			return;
		if (old != null) {
			// wire down the listener to set valueIsAdjusting
			getEd().getTextField().removeFocusListener(this);
			// model changes
			old.removeChangeListener(this);
		}
		oldValueBase = Double.NaN;
		if (model != null) {
			value.setModel(rescale(base, getUnit(), model));
			oldValueBase = getValue();
			// wire up the listener to set valueIsAdjusting
			getEd().getTextField().addFocusListener(this);
			// model changes
			model.addChangeListener(this);
		}
		value.setVisible(model != null);
		firePropertyChange("model", old, model);
	}

	/** Set the currently active (display) {@link Unit} */
	public void setUnit(final Unit current) {
		final Unit old = unit;
		if (current == old)
			return;
		combo.setSelectedItem(unit = current);
		// re-adjust the model
		rescale(old, unit, getModel());
		firePropertyChange("unit", old, unit);
	}

	public void setValue(double value) {
		if (oldValueBase == value)
			return;
		// convert to model:
		value = convert(value, base, getUnit());
		// re-convert to base to avoid rounding errors:
		oldValueBase = convert(value, getUnit(), base);
		if (value == getModel().getNumber().doubleValue())
			return;
		this.value.setValue(value);
	}

	/** Internal use only! */
	public void setValueIsAdjusting(final boolean valueIsAdjusting) {
		final boolean old = this.valueIsAdjusting;
		if (old == valueIsAdjusting)
			return;
		firePropertyChange("valueIsAdjusting", old,
				this.valueIsAdjusting = valueIsAdjusting);
	}

	/** internal use only! */
	public void stateChanged(final ChangeEvent e) {
		if (e.getSource() == getModel()) {
			final double v = getValue();
			if (oldValueBase == v)
				return;
			oldValueBase = v;
			change.fireStateChanged();
		}
	}
}