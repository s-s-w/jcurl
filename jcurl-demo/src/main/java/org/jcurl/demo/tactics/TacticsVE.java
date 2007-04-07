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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileView;

import org.jcurl.core.swing.PositionDisplay;

public class TacticsVE {

    /**
     * This method initializes jCurlDisplay
     * 
     * @return org.jcurl.core.swing.PositionDisplay
     */
    private PositionDisplay getJCurlDisplay() {
        if (jCurlDisplay == null) {
            jCurlDisplay = new PositionDisplay();
            jCurlDisplay.setName("IceSize");
            jCurlDisplay
                    .setFont(new Font("Bitstream Vera Sans", Font.PLAIN, 12));
            //jCurlDisplay.setMinimumSize(new Dimension(100, 50));
        }
        return jCurlDisplay;
    }

    /**
     * This method initializes controlPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getControlPanel() {
        if (controlPanel == null) {
            controlPanel = new JPanel();
        }
        return controlPanel;
    }

    /**
     * This method initializes exportPngFileChooser
     * 
     * @return javax.swing.JFileChooser
     */
    private JFileChooser getExportPngFileChooser() {
        if (exportPngFileChooser == null) {
            exportPngFileChooser = new JFileChooser();
            exportPngFileChooser.setDialogTitle("Export to Png");
            exportPngFileChooser.setFileView(new FileView() {
            });
            exportPngFileChooser.setInputVerifier(new InputVerifier() {

                public boolean verify(JComponent arg0) {
                    // TODO Auto-generated method stub
                    return false;

                }
            });
            exportPngFileChooser.setName("Export Png");
            exportPngFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        }
        return exportPngFileChooser;
    }

    /**
     * Launches this application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TacticsVE application = new TacticsVE();
                application.getJFrame().setVisible(true);
            }
        });
    }

    private JPanel aboutContentPane = null;

    private JDialog aboutDialog = null;  //  @jve:decl-index=0:visual-constraint="537,163"

    private JMenuItem aboutMenuItem = null;

    private JLabel aboutVersionLabel = null;

    private JMenuItem copyMenuItem = null;

    private JMenuItem cutMenuItem = null;

    private JMenu editMenu = null;

    private JMenuItem exitMenuItem = null;

    private JMenuItem exportPngMenuItem = null;

    private JMenu fileMenu = null;

    private JMenu helpMenu = null;

    private JPanel jContentPane = null;

    private JFrame jFrame = null; // @jve:decl-index=0:visual-constraint="10,10"

    private JMenuBar jJMenuBar = null;

    private JMenuItem openMenuItem = null;

    private JMenuItem pasteMenuItem = null;

    private JMenuItem saveMenuItem = null;

    private PositionDisplay jCurlDisplay = null;

    private JPanel controlPanel = null;

    private JFileChooser exportPngFileChooser = null; // @jve:decl-index=0:visual-constraint="14,234"

    /**
     * This method initializes aboutContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getAboutContentPane() {
        if (aboutContentPane == null) {
            aboutContentPane = new JPanel();
            aboutContentPane.setLayout(new BorderLayout());
            aboutContentPane.add(getAboutVersionLabel(), BorderLayout.CENTER);
        }
        return aboutContentPane;
    }

    /**
     * This method initializes aboutDialog
     * 
     * @return javax.swing.JDialog
     */
    private JDialog getAboutDialog() {
        if (aboutDialog == null) {
            aboutDialog = new JDialog(getJFrame(), true);
            aboutDialog.setTitle("About");
            aboutDialog.setSize(new Dimension(121, 83));
            aboutDialog.setContentPane(getAboutContentPane());
        }
        return aboutDialog;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new JMenuItem();
            aboutMenuItem.setText("About");
            aboutMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JDialog aboutDialog = getAboutDialog();
                    aboutDialog.pack();
                    Point loc = getJFrame().getLocation();
                    loc.translate(20, 20);
                    aboutDialog.setLocation(loc);
                    aboutDialog.setVisible(true);
                }
            });
        }
        return aboutMenuItem;
    }

    /**
     * This method initializes aboutVersionLabel
     * 
     * @return javax.swing.JLabel
     */
    private JLabel getAboutVersionLabel() {
        if (aboutVersionLabel == null) {
            aboutVersionLabel = new JLabel();
            aboutVersionLabel.setText("Version 1.0");
            aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return aboutVersionLabel;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getCopyMenuItem() {
        if (copyMenuItem == null) {
            copyMenuItem = new JMenuItem();
            copyMenuItem.setText("Copy");
            copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                    Event.CTRL_MASK, true));
        }
        return copyMenuItem;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getCutMenuItem() {
        if (cutMenuItem == null) {
            cutMenuItem = new JMenuItem();
            cutMenuItem.setText("Cut");
            cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                    Event.CTRL_MASK, true));
        }
        return cutMenuItem;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getEditMenu() {
        if (editMenu == null) {
            editMenu = new JMenu();
            editMenu.setText("Edit");
            editMenu.add(getCutMenuItem());
            editMenu.add(getCopyMenuItem());
            editMenu.add(getPasteMenuItem());
        }
        return editMenu;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getExitMenuItem() {
        if (exitMenuItem == null) {
            exitMenuItem = new JMenuItem();
            exitMenuItem.setText("Exit");
            exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return exitMenuItem;
    }

    /**
     * This method initializes exportPngMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getExportPngMenuItem() {
        if (exportPngMenuItem == null) {
            exportPngMenuItem = new JMenuItem();
            exportPngMenuItem.setText("Export Png");
            exportPngMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_E, Event.CTRL_MASK, true));
            exportPngMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                            JFileChooser aboutDialog = getExportPngFileChooser();
                            aboutDialog.setVisible(true);
                        }
                    });
        }
        return exportPngMenuItem;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu();
            fileMenu.setText("File");
            fileMenu.add(getOpenMenuItem());
            fileMenu.add(getSaveMenuItem());
            fileMenu.add(getExportPngMenuItem());
            fileMenu.add(getExitMenuItem());
        }
        return fileMenu;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = new JMenu();
            helpMenu.setText("Help");
            helpMenu.add(getAboutMenuItem());
        }
        return helpMenu;
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJCurlDisplay(), BorderLayout.CENTER);
            jContentPane.add(getControlPanel(), BorderLayout.EAST);
        }
        return jContentPane;
    }

    /**
     * This method initializes jFrame
     * 
     * @return javax.swing.JFrame
     */
    private JFrame getJFrame() {
        if (jFrame == null) {
            jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setJMenuBar(getJJMenuBar());
            jFrame.setSize(500, 200);
            jFrame.setContentPane(getJContentPane());
            jFrame.setTitle("Tactics2007");
        }
        return jFrame;
    }

    /**
     * This method initializes jJMenuBar
     * 
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getEditMenu());
            jJMenuBar.add(getHelpMenu());
        }
        return jJMenuBar;
    }

    /**
     * This method initializes openMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getOpenMenuItem() {
        if (openMenuItem == null) {
            openMenuItem = new JMenuItem();
            openMenuItem.setText("Open");
            openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                    Event.CTRL_MASK, true));
            openMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO
                    // Auto-generated
                    // Event stub
                    // actionPerformed()
                }
            });
        }
        return openMenuItem;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getPasteMenuItem() {
        if (pasteMenuItem == null) {
            pasteMenuItem = new JMenuItem();
            pasteMenuItem.setText("Paste");
            pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                    Event.CTRL_MASK, true));
        }
        return pasteMenuItem;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSaveMenuItem() {
        if (saveMenuItem == null) {
            saveMenuItem = new JMenuItem();
            saveMenuItem.setText("Save");
            saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                    Event.CTRL_MASK, true));
        }
        return saveMenuItem;
    }

}
