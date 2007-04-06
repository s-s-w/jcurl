/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.swing;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jcurl.core.helpers.Version;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:AboutDialog.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class AboutDialog extends JDialog {

    private static final long serialVersionUID = -6876982740130350850L;

    /**
     * @param owner
     * @throws java.awt.HeadlessException
     */
    public AboutDialog(final Frame owner) throws HeadlessException {
        super(owner, "About", true);
        setResizable(false);
        final Box b = Box.createVerticalBox();
        b.add(Box.createGlue());
        b.add(new JLabel(owner.getClass().getName()));
        b.add(new JLabel("by M. Rohrmoser"));
        b.add(Box.createGlue());
        final Version v = Version.find(owner.getClass());
        if (v != null) {
            b.add(new JLabel("Version: " + v.toString()));
            b.add(new JLabel("Build time: " + v.getTime()));
            b.add(Box.createGlue());
        }
        getContentPane().add(b, "Center");

        final JPanel p2 = new JPanel();
        final JButton ok = new JButton("OK");
        p2.add(ok);
        getContentPane().add(p2, "South");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                AboutDialog.this.setVisible(false);
            }
        });
        this.setSize(250, 150);
    }

}