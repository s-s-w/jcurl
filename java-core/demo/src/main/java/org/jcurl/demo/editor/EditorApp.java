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
package org.jcurl.demo.editor;

import javax.swing.JFrame;

/**
 * A simple editor that brings all together.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:EditorApp.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class EditorApp extends JFrame {

    // private static final Cursor Cdefault = Cursor
    // .getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    //
    // private static final Cursor Cwait = Cursor
    // .getPredefinedCursor(Cursor.WAIT_CURSOR);
    //
    // private static final Log log =
    // JCLoggerFactory.getLogger(EditorApp.class);
    //
    // private static final long serialVersionUID = -5330160383513753742L;
    //
    // /**
    // * @param name
    // * @param icon
    // * @param executor
    // * @param action
    // * @return the generated action
    // */
    // private static AbstractAction createAction(final String name,
    // final Icon icon, final Object executor, final String action) {
    // return new AbstractAction(name, icon) {
    //
    // private static final long serialVersionUID = 1L;
    //
    // private Method m = null;
    //
    // public void actionPerformed(final ActionEvent evt) {
    // if (m == null)
    // try {
    // m = executor.getClass().getMethod(action, null);
    // } catch (Exception e) {
    // try {
    // m = executor.getClass().getDeclaredMethod(action,
    // null);
    // } catch (SecurityException e1) {
    // throw new RuntimeException(e1);
    // } catch (NoSuchMethodException e1) {
    // throw new RuntimeException(e1);
    // }
    // }
    // try {
    // m.invoke(executor, null);
    // } catch (IllegalAccessException e) {
    // throw new RuntimeException(e);
    // } catch (InvocationTargetException e) {
    // throw new RuntimeException(e);
    // }
    // }
    // };
    // }
    //
    // public static void main(String[] args) throws SAXException, IOException {
    // log.info("Version: " + Version.find());
    // final EditorApp frame = new EditorApp();
    // frame.cmdNew();
    // frame.load(args);
    // frame.setVisible(true);
    // }
    //
    // private JDialog about = null;
    //
    // private final JButton bPause;
    //
    // private final JButton bStart;
    //
    // private final JButton bStop;
    //
    // private File currentFile = null;
    //
    // private long lastSaved = 0;
    //
    // private final RockEditDisplay master;
    //
    // private final PositionSet mod_locations = new PositionSet();
    //
    // private final SpeedSet mod_speeds = new SpeedSet();
    //
    // public EditorApp() {
    // addWindowListener(new WindowAdapter() {
    // public void windowClosing(WindowEvent e) {
    // EditorApp.this.cmdExit();
    // }
    // });
    // master = new RockEditDisplay();
    // master.setPos(mod_locations);
    // master.setSpeed(mod_speeds);
    // final PositionDisplay pnl2 = new PositionDisplay();
    // pnl2.setPos(mod_locations);
    // pnl2.setZoom(FixpointZoomer.HOG2HACK);
    //
    // final Container con = getContentPane();
    //
    // con.add(master, "Center");
    // con.add(new SumWaitDisplay(mod_locations), "West");
    // con.add(new SumShotDisplay(mod_locations), "East");
    // {
    // final Box b0 = Box.createVerticalBox();
    // final Box b1 = Box.createHorizontalBox();
    // b1.add(Box.createRigidArea(new Dimension(0, 75)));
    // b1.add(pnl2);
    // b0.add(b1);
    // b0.add(new JSlider(0, 100, 0));
    // final Box b2 = Box.createHorizontalBox();
    // b2.add(Box.createHorizontalGlue());
    // b2.add(bStart = this.newButton("Start", this, "cmdRunStart"));
    // b2.add(bPause = this.newButton("Pause", this, "cmdRunPause"));
    // b2.add(bStop = this.newButton("Stop", this, "cmdRunStop"));
    // b2.add(Box.createHorizontalGlue());
    // b0.add(b2);
    // con.add(b0, "South");
    // }
    // bStop.getAction().actionPerformed(null);
    //
    // setJMenuBar(createMenu());
    // refreshTitle();
    // this.setSize(900, 400);
    //
    // new SpeedController(mod_locations, mod_speeds, master);
    // new LocationController(mod_locations, pnl2);
    // lastSaved = mod_locations.getLastChanged();
    // }
    //
    // boolean chooseLoadFile(final File def) {
    // final JFileChooser fc = new JcxFileChooser(def);
    // if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
    // setCurrentFile(fc.getSelectedFile());
    // return true;
    // }
    // return false;
    // }
    //
    // boolean chooseSaveFile(final File def) {
    // final JFileChooser fc = new JcxFileChooser(def);
    // if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(this)) {
    // setCurrentFile(fc.getSelectedFile());
    // return true;
    // }
    // return false;
    // }
    //
    // void cmdAbout() {
    // log.info("");
    // if (about == null)
    // about = new AboutDialog(this);
    // about.setVisible(true);
    // }
    //
    // void cmdExit() {
    // if (!discardUnsavedChanges())
    // return;
    // System.exit(0);
    // }
    //
    // void cmdExportPng() throws IOException {
    // log.debug("-");
    // final JFileChooser fc = new PngFileChooser(currentFile);
    // if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(this)) {
    // final File dst = fc.getSelectedFile();
    // try {
    // this.setCursor(Cwait);
    // master.exportPng(dst, null);
    // } finally {
    // this.setCursor(Cdefault);
    // }
    // }
    // }
    //
    // void cmdNew() {
    // if (!discardUnsavedChanges())
    // return;
    // try {
    // this.setCursor(Cwait);
    // // initial state
    // final PositionSet pos = PositionSet.allOut();
    // pos.getDark(0).setLocation(0, 5, 0);
    // pos.getDark(1).setLocation(0, 0, 0.5);
    // pos.getLight(0).setLocation(0.2, 2.5);
    // pos.getLight(1).setLocation(1.0, 1.5);
    // final SpeedSet speed = new SpeedSet();
    // speed.getDark(0).setLocation(0.1, -1.325, 0.75);
    // // feed the model
    // PositionSet.allOut(mod_locations);
    // RockSet.allZero(mod_speeds);
    // lastSaved = System.currentTimeMillis();
    // } finally {
    // this.setCursor(Cdefault);
    // }
    // }
    //
    // void cmdOpen() throws FileNotFoundException, SAXException, IOException {
    // if (!discardUnsavedChanges())
    // return;
    // // try {
    // // final FileOpenService fos = (FileOpenService) ServiceManager
    // // .lookup("javax.jnlp.BasicService");
    // // final FileContents fc = fos.openFileDialog("/home/m", new
    // // String[]{".jcx", ".jcz"});
    // // if(fc != null)
    // // log.info(fc.getName());
    // // } catch (UnavailableServiceException e) {
    // // throw new RuntimeException("Uncaught exception", e);
    // // }
    // if (!chooseLoadFile(getCurrentFile() == null ? new File(".")
    // : getCurrentFile()))
    // return;
    // SetupIO.load(getCurrentFile(), mod_locations, mod_speeds, null, null);
    // lastSaved = System.currentTimeMillis();
    // }
    //
    // void cmdRunPause() {
    // JOptionPane.showMessageDialog(this, "Not implemented yet");
    // bStart.getAction().setEnabled(true);
    // bPause.getAction().setEnabled(false);
    // bStop.getAction().setEnabled(false);
    // }
    //
    // void cmdRunStart() {
    // JOptionPane.showMessageDialog(this, "Not implemented yet");
    // bStart.getAction().setEnabled(false);
    // bPause.getAction().setEnabled(true);
    // bStop.getAction().setEnabled(true);
    // }
    //
    // void cmdRunStop() {
    // bStart.getAction().setEnabled(true);
    // bPause.getAction().setEnabled(false);
    // bStop.getAction().setEnabled(false);
    // }
    //
    // void cmdSave() throws SAXException, IOException {
    // if (getCurrentFile() == null)
    // if (!chooseSaveFile(new File(".")))
    // return;
    // save(getCurrentFile());
    // }
    //
    // void cmdSaveAs() throws SAXException, IOException {
    // if (!chooseSaveFile(getCurrentFile() == null ? new File(".")
    // : getCurrentFile()))
    // return;
    // save(getCurrentFile());
    // }
    //
    // void cmdZoom() {
    // log.info("");
    // }
    //
    // private JMenuBar createMenu() {
    // final JMenuBar bar = new JMenuBar();
    // {
    // final JMenu menu = bar.add(new JMenu("File"));
    // menu.setMnemonic('F');
    // menu.add(this.newMI("New", null, 'N', -1, this, "cmdNew"));
    // menu.add(this.newMI("Open", null, 'O', KeyEvent.VK_O, this,
    // "cmdOpen"));
    // menu.addSeparator();
    // menu.add(this.newMI("Export Png", null, 'P', KeyEvent.VK_E, this,
    // "cmdExportPng"));
    // menu.addSeparator();
    // menu.add(this.newMI("Save", null, 'S', KeyEvent.VK_S, this,
    // "cmdSave"));
    // menu.add(this.newMI("Save As", null, 'A', -1, this, "cmdSaveAs"));
    // menu.addSeparator();
    // menu.add(this.newMI("Exit", null, 'x', -1, this, "cmdExit"));
    // }
    // {
    // final JMenu menu = bar.add(new JMenu("View"));
    // menu.setMnemonic('V');
    // menu.add(this.newMI("Zoom", null, 'z', -1, this, "cmdZoom"));
    // }
    // {
    // final JMenu menu = bar.add(new JMenu("Play"));
    // menu.setMnemonic('P');
    // menu.add(this.newMI('a', -1, bStart.getAction()));
    // menu.add(this.newMI('P', -1, bPause.getAction()));
    // menu.add(this.newMI('o', -1, bStop.getAction()));
    // }
    // {
    // final JMenu menu = bar.add(new JMenu("Help"));
    // menu.setMnemonic('H');
    // menu.add(this.newMI("About", null, 'a', -1, this, "cmdAbout"));
    // }
    // return bar;
    // }
    //
    // private boolean discardUnsavedChanges() {
    // if (mod_locations.getLastChanged() <= lastSaved
    // && mod_speeds.getLastChanged() <= lastSaved)
    // return true;
    // if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this,
    // "Discard unsaved changes?", "Warning",
    // JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
    // return true;
    // return false;
    // }
    //
    // public File getCurrentFile() {
    // return currentFile;
    // }
    //
    // private void load(final File f, final PositionSet pos)
    // throws FileNotFoundException, SAXException, IOException {
    // SetupIO.load(f, pos, null, null, null);
    // }
    //
    // private void load(final String[] args) throws SAXException, IOException {
    // if (args.length <= 0)
    // return;
    // final File f = new File(args[0]);
    // if (!f.exists())
    // return;
    // this.load(f, mod_locations);
    // }
    //
    // private JButton newButton(final Action action) {
    // final JButton item = new JButton(action);
    // // item.setMnemonic(mnemonic);
    // // if (ctrlAccel >= 0)
    // // item.setAccelerator(KeyStroke.getKeyStroke(ctrlAccel,
    // // InputEvent.CTRL_MASK));
    // return item;
    // }
    //
    // private JButton newButton(final String name, final Object executor,
    // final String action) {
    // return this.newButton(createAction(name, null, executor, action));
    // }
    //
    // private JMenuItem newMI(final char mnemonic, final int ctrlAccel,
    // final Action action) {
    // final JMenuItem item = new JMenuItem(action);
    // item.setMnemonic(mnemonic);
    // if (ctrlAccel >= 0)
    // item.setAccelerator(KeyStroke.getKeyStroke(ctrlAccel,
    // InputEvent.CTRL_MASK));
    // return item;
    // }
    //
    // private JMenuItem newMI(final String name, final Icon icon,
    // final char mnemonic, final int ctrlAccel, final Object executor,
    // final String action) {
    // return this.newMI(mnemonic, ctrlAccel, createAction(name, icon,
    // executor, action));
    // }
    //
    // private void refreshTitle() {
    // setTitle(getClass().getName() + " - "
    // + (currentFile == null ? "" : currentFile.getAbsolutePath()));
    // }
    //
    // private void save(File f) throws SAXException, IOException {
    // SetupIO.save(f, mod_locations, mod_speeds, null, null);
    // lastSaved = System.currentTimeMillis();
    // refreshTitle();
    // }
    //
    // public void setCurrentFile(File currentFile) {
    // if (currentFile == null || currentFile.getName().endsWith(".jcx")
    // || currentFile.getName().endsWith(".jcz"))
    // this.currentFile = currentFile;
    // else
    // this.currentFile = new File(currentFile.getName() + ".jcx");
    // refreshTitle();
    // }
}