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
package org.jcurl.demo.viewer;

import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Event;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.KeyStroke;
import java.awt.Point;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JSlider;
import org.jcurl.core.swing.PositionDisplay;

public class ViewerVE {

    private JFrame jFrame = null;

    private JPanel jContentPane = null;

    private JMenuBar jJMenuBar = null;

    private JMenu fileMenu = null;

    private JMenu runMenu = null;

    private JMenu helpMenu = null;

    private JMenuItem exitMenuItem = null;

    private JMenuItem aboutMenuItem = null;

    private JMenuItem startMenuItem = null;

    private JMenuItem stopMenuItem = null;

    private JMenuItem pauseMenuItem = null;

    private JMenuItem openMenuItem = null;

    private JDialog aboutDialog = null;

    private JPanel aboutContentPane = null;

    private JLabel aboutVersionLabel = null;

    private JPanel controlPanel = null;

    private JButton pauseButton = null;

    private JButton startButton = null;

    private JButton stopButton = null;

    private JSlider timeSlider = null;

    private PositionDisplay positionDisplay = null;

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
            jFrame.setSize(300, 200);
            jFrame.setContentPane(getJContentPane());
            jFrame.setTitle("Application");
        }
        return jFrame;
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
            jContentPane.add(getControlPanel(), BorderLayout.SOUTH);
            jContentPane.add(getPositionDisplay(), BorderLayout.CENTER);
        }
        return jContentPane;
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
            jJMenuBar.add(getRunMenu());
            jJMenuBar.add(getHelpMenu());
        }
        return jJMenuBar;
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
            fileMenu.add(getExitMenuItem());
        }
        return fileMenu;
    }

    /**
     * This method initializes jMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getRunMenu() {
        if (runMenu == null) {
            runMenu = new JMenu();
            runMenu.setText("Run");
            runMenu.add(getStartMenuItem());
            runMenu.add(getStopMenuItem());
            runMenu.add(getPauseMenuItem());
        }
        return runMenu;
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
     * This method initializes aboutDialog	
     * 	
     * @return javax.swing.JDialog
     */
    private JDialog getAboutDialog() {
        if (aboutDialog == null) {
            aboutDialog = new JDialog(getJFrame(), true);
            aboutDialog.setTitle("About");
            aboutDialog.setContentPane(getAboutContentPane());
        }
        return aboutDialog;
    }

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
    private JMenuItem getStartMenuItem() {
        if (startMenuItem == null) {
            startMenuItem = new JMenuItem();
            startMenuItem.setText("Start");
            startMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                    Event.CTRL_MASK, true));
            startMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return startMenuItem;
    }

    /**
     * This method initializes jMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getStopMenuItem() {
        if (stopMenuItem == null) {
            stopMenuItem = new JMenuItem();
            stopMenuItem.setText("Stop");
            stopMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                    Event.CTRL_MASK, true));
            stopMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return stopMenuItem;
    }

    /**
     * This method initializes jMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getPauseMenuItem() {
        if (pauseMenuItem == null) {
            pauseMenuItem = new JMenuItem();
            pauseMenuItem.setText("Pause");
            pauseMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                    Event.CTRL_MASK, true));
            pauseMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return pauseMenuItem;
    }

    /**
     * This method initializes jMenuItem	
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
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return openMenuItem;
    }

    /**
     * This method initializes controlPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getControlPanel() {
        if (controlPanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridy = 2;
            gridBagConstraints3.gridx = 3;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridy = 2;
            gridBagConstraints2.gridx = 2;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridy = 2;
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints.anchor = GridBagConstraints.NORTH;
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridwidth = 4;
            gridBagConstraints.weightx = 1.0;
            controlPanel = new JPanel();
            controlPanel.setLayout(new GridBagLayout());
            controlPanel.add(getTimeSlider(), gridBagConstraints);
            controlPanel.add(getStartButton(), gridBagConstraints1);
            controlPanel.add(getStopButton(), gridBagConstraints2);
            controlPanel.add(getPauseButton(), gridBagConstraints3);
        }
        return controlPanel;
    }

    /**
     * This method initializes pauseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getPauseButton() {
        if (pauseButton == null) {
            pauseButton = new JButton();
            pauseButton.setText("Pause");
            pauseButton.setEnabled(false);
        }
        return pauseButton;
    }

    /**
     * This method initializes startButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getStartButton() {
        if (startButton == null) {
            startButton = new JButton();
            startButton.setText("Start");
            startButton.setSelected(true);
        }
        return startButton;
    }

    /**
     * This method initializes stopButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getStopButton() {
        if (stopButton == null) {
            stopButton = new JButton();
            stopButton.setText("Stop");
            stopButton.setEnabled(false);
        }
        return stopButton;
    }

    /**
     * This method initializes timeSlider	
     * 	
     * @return javax.swing.JSlider	
     */
    private JSlider getTimeSlider() {
        if (timeSlider == null) {
            timeSlider = new JSlider();
        }
        return timeSlider;
    }

    /**
     * This method initializes positionDisplay	
     * 	
     * @return org.jcurl.core.swing.PositionDisplay	
     */
    private PositionDisplay getPositionDisplay() {
        if (positionDisplay == null) {
            positionDisplay = new PositionDisplay();
        }
        return positionDisplay;
    }

    /**
     * Launches this application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ViewerVE application = new ViewerVE();
                application.getJFrame().setVisible(true);
            }
        });
    }

}
