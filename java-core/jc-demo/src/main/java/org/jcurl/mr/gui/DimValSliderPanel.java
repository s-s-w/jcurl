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
package org.jcurl.mr.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.helpers.Unit;
import org.jcurl.core.helpers.Measure;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * @see BoundedRangeModel
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class DimValSliderPanel extends JPanel implements ChangeListener,
        ActionListener, PropertyChangeListener {

    private static final Log log = JCLoggerFactory
            .getLogger(DimValSliderPanel.class);

    private static final long serialVersionUID = 9008976409239381440L;

    static PropertyDescriptor findProperty(final Class clz,
            final String property) {
        try {
            final BeanInfo b = Introspector.getBeanInfo(clz);
            final PropertyDescriptor[] all = b.getPropertyDescriptors();
            for (int i = all.length - 1; i >= 0; i--)
                if (property.equals(all[i].getName()))
                    return all[i];
            throw new IllegalArgumentException("Property [" + property
                    + "] not found in [" + clz.getClass() + "]");
        } catch (final IntrospectionException e) {
            throw new IllegalStateException("Unhandled", e);
        }
    }

    private final Unit dim;

    private final int Granularity = 1000;

    private final Model model;

    private final PropertyDescriptor prop;

    private final JSlider slider;

    private final JTextField text;

    public DimValSliderPanel(final Model m, final String title,
            final String property, final Unit dim) {
        setVisible(false);
        model = m == null ? new Model() : m;
        model.addPropertyChangeListener(this);
        this.dim = dim;
        prop = findProperty(model.getClass(), property);

        final JLabel label = new JLabel();
        label.setText(title);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        slider = new JSlider();
        slider.setOrientation(SwingConstants.VERTICAL);

        final int max = (int) (new Measure(IceSize.SIDE_2_CENTER, dim.BaseUnit)
                .to(dim).value * Granularity);
        slider.setMaximum((int) (Granularity * Math.ceil((double) max
                / Granularity)));
        // slider.setMaximum(2500);
        slider.setMinimum(-slider.getMaximum());
        slider.setMajorTickSpacing(Granularity);
        slider.setMinorTickSpacing(Granularity / 10);
        slider.setAlignmentX(10);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setPaintTrack(true);
        slider.addChangeListener(this);

        text = new JTextField();
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setEditable(true);
        text.addActionListener(this);
        text.selectAll();

        setLayout(new BorderLayout());
        this.add(label, "North");
        this.add(slider, "Center");
        this.add(text, "South");
        this.setSize(50, 100);
        setValue(new Measure(0, dim));
        setVisible(true);
    }

    public void actionPerformed(final ActionEvent arg0) {
        if (arg0.getSource() == text)
            try {
                setValue(Measure.parse(text.getText()));
            } catch (final RuntimeException e) {
                ;// model. setBroomX(getBroomX());
            }
    }

    public int getMajorTickSpacing() {
        return slider.getMajorTickSpacing();
    }

    public int getMaximum() {
        return slider.getMaximum();
    }

    public int getMinimum() {
        return slider.getMinimum();
    }

    public int getMinorTickSpacing() {
        return slider.getMinorTickSpacing();
    }

    public void propertyChange(final PropertyChangeEvent arg0) {
        log.debug(arg0);
        if (model == arg0.getSource())
            if (prop.getName().equals(arg0.getPropertyName())) {
                final Measure raw = (Measure) arg0.getNewValue();
                final Measure val;
                if (raw.unit == Unit.NONE)
                    val = new Measure(raw.value, dim);
                else
                    val = raw.to(dim);
                slider.setValue((int) (val.value * Granularity));
                text.setText(val.toString());
            }
    }

    public void setMajorTickSpacing(final int arg0) {
        slider.setMajorTickSpacing(arg0);
    }

    public void setMaximum(final int arg0) {
        slider.setMaximum(arg0);
    }

    public void setMinimum(final int arg0) {
        slider.setMinimum(arg0);
    }

    public void setMinorTickSpacing(final int arg0) {
        slider.setMinorTickSpacing(arg0);
    }

    public void setValue(final Measure value) {
        try {
            prop.getWriteMethod().invoke(model, new Object[] { value });
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException("Unhandled", e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("Unhandled", e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException("Unhandled", e);
        }
    }

    public void stateChanged(final ChangeEvent arg0) {
        if (arg0.getSource() == slider)
            setValue(new Measure((double) slider.getValue() / Granularity, dim));
    }
}
