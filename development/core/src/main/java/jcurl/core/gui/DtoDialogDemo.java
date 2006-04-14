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

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import jcurl.sim.model.SlideDenny;

import org.apache.ugli.ULogger;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.core.helpers.Version;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class DtoDialogDemo extends JFrame {

    private static final long serialVersionUID = 7619830975686009904L;

    private static final ULogger log = JCLoggerFactory
            .getLogger(DtoDialogDemo.class);

    public static void main(String[] args) {
        log.info("Version: " + Version.find());
        final DtoDialogDemo frame = new DtoDialogDemo();
        frame.setSize(500, 400);
        frame.setVisible(true);
    }

    private Object data = new SlideDenny();

    private JDialog dialog = null;

    public DtoDialogDemo() {
        final Container con = getContentPane();
        final JFrame fr = this;
        con.add(new JButton(new AbstractAction("Edit") {

            private static final long serialVersionUID = -508686443887297017L;

            public void actionPerformed(final ActionEvent evt) {
                if (dialog == null)
                    dialog = new DtoDialog(data, fr);
                dialog.setVisible(true);
            }
        }), "Center");
    }

}