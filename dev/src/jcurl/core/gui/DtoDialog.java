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
package jcurl.core.gui;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class DtoDialog extends JDialog {

    public DtoDialog(Object data, Frame owner) throws HeadlessException {
        this(data, owner, true);
    }

    public DtoDialog(Object data, Frame owner, boolean modal)
            throws HeadlessException {
        this(data, owner, null, modal);
    }

    public DtoDialog(Object data, Frame owner, String title)
            throws HeadlessException {
        this(data, owner, title, true);
    }

    public DtoDialog(Object data, Frame owner, String title, boolean modal)
            throws HeadlessException {
        super(owner, title, modal);

        final Box b = Box.createVerticalBox();
        try {
            final BeanInfo bi = Introspector.getBeanInfo(data.getClass(),
                    Object.class);
            final PropertyDescriptor[] pds = bi.getPropertyDescriptors();
            for (int i = 0; i < pds.length; i++) {
                final Box b2 = Box.createHorizontalBox();
                b2.add(new JLabel(pds[i].getDisplayName() + ": "));
                try {
                    JTextField txt = null;
                    if (pds[i].getWriteMethod() == null) {
                        b2.add(new JLabel(pds[i].getDisplayName() + ": "));
                    } else {
                        Class peC = pds[i].getPropertyEditorClass();
                        PropertyEditor pe;
                        if (peC != null)
                            pe = (PropertyEditor) peC.newInstance();
                        else
                            pe = PropertyEditorManager.findEditor(pds[i]
                                    .getPropertyType());
                        txt = new JTextField();
                    }
                    if (txt != null)
                        b2.add(txt);
                    b.add(b2);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        this.getContentPane().add(b);

        {
            final Box b2 = Box.createHorizontalBox();
            b2.add(Box.createHorizontalGlue());
            b2.add(new JButton("OK"));
            b2.add(Box.createHorizontalGlue());
            b2.add(new JButton("CANCEL"));
            b2.add(Box.createHorizontalGlue());
            b.add(b2);
        }
        this.setSize(300, 600);
    }
}