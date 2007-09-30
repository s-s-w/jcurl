/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
package org.jcurl.demo.tactics;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcurl.demo.tactics.MainMultiPanel.Controller;
import org.jcurl.demo.tactics.Menu.JCMenu;

/**
 * Try building Menues via Annotations.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
@JCMenu("File2")
public class FileController extends Menu {

    private static final Log log = LogFactory.getLog(FileController.class);
    private final Controller con;

    public FileController(final Controller con) {
        this.con = con;
    }

    @JCAction(idx = 0, name = "Clear", mnemonic = KeyEvent.VK_C)
    public void doClear(final ActionEvent e) {
        con._fileClear();
    }

    @JCAction(idx = 6, name = "Exit", mnemonic = KeyEvent.VK_X)
    public void doExit(final ActionEvent e) {
        con._fileExit();
    }

    @JCAction(idx = 1, name = "Open", mnemonic = KeyEvent.VK_O, accelerator = "ctrl O")
    public void doOpen(final ActionEvent e) {
        con._fileOpen();
    }

    @JCAction(idx = 3, name = "Save", mnemonic = KeyEvent.VK_S, accelerator = "ctrl S")
    public void doSave(final ActionEvent e) {
        con._fileSave();
    }

    @JCAction(idx = 4, name = "Save As", mnemonic = KeyEvent.VK_A)
    public void doSaveAs(final ActionEvent e) {
        con._fileSaveAs();
    }
}
