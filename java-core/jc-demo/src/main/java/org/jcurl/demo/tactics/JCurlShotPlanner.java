/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
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

package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.EventObject;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.TrajectorySet;
import org.jcurl.core.api.Unit;
import org.jcurl.core.helpers.BatikButler;
import org.jcurl.core.io.IONode;
import org.jcurl.core.io.IOTrajectories;
import org.jcurl.core.io.JCurlSerializer;
import org.jcurl.core.io.JDKSerializer;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.FileNameExtensionFilter;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationAction;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;

/**
 * Makes heavy use of the <a
 * href="https://appframework.dev.java.net/intro/index.html">Swing Application
 * Framework</a>.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class JCurlShotPlanner extends SingleFrameApplication {
	static class ChangeManager implements ChangeListener {
		private final JCurlShotPlanner host;

		public ChangeManager(final JCurlShotPlanner host) {
			this.host = host;
		}

		public void deregister(final ComputedTrajectorySet cts) {
			if (cts == null)
				return;
			cts.getInitialPos().removeChangeListener(this);
			cts.getInitialSpeed().removeChangeListener(this);
			cts.getCurrentPos().removeChangeListener(this);
			cts.getCurrentSpeed().removeChangeListener(this);
		}

		public void register(final ComputedTrajectorySet cts) {
			if (cts == null)
				return;
			cts.getInitialPos().addChangeListener(this);
			cts.getInitialSpeed().addChangeListener(this);
			cts.getCurrentPos().addChangeListener(this);
			cts.getCurrentSpeed().addChangeListener(this);
		}

		public void stateChanged(final ChangeEvent e) {
			host.setModified(true);
		}
	}

	static class GuiUtil {

		private static final Insets zeroInsets = new Insets(0, 0, 0, 0);

		private final ApplicationContext act;;

		public GuiUtil(final ApplicationContext act) {
			this.act = act;
		}

		/**
		 * Create a simple about box JDialog that displays the standard
		 * Application resources, like {@code Application.title} and
		 * {@code Application.description}. The about box's labels and fields
		 * are configured by resources that are injected when the about box is
		 * shown (see SingleFrameApplication#show). The resources are defined in
		 * the application resource file: resources/DocumentExample.properties.
		 * 
		 * From:
		 * https://appframework.dev.java.net/downloads/AppFramework-1.03-src.zip
		 * DocumentExample
		 */
		private JDialog createAboutBox(final Frame owner) {
			final JPanel panel = new JPanel(new GridBagLayout());
			panel.setBorder(new EmptyBorder(0, 28, 16, 28)); // top, left,
			// bottom, right
			final JLabel titleLabel = new JLabel();
			titleLabel.setName("aboutTitleLabel");
			final GridBagConstraints c = new GridBagConstraints();
			initGridBagConstraints(c);
			c.anchor = GridBagConstraints.WEST;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.ipady = 32;
			c.weightx = 1.0;
			panel.add(titleLabel, c);
			final String[] fields = { "description", "version", "vendor",
					"home" };
			for (final String field : fields) {
				final JLabel label = new JLabel();
				label.setName(field + "Label");
				final JTextField textField = new JTextField();
				textField.setName(field + "TextField");
				textField.setEditable(false);
				textField.setBorder(null);
				initGridBagConstraints(c);
				// c.anchor = GridBagConstraints.BASELINE_TRAILING; 1.6 ONLY
				c.anchor = GridBagConstraints.EAST;
				panel.add(label, c);
				initGridBagConstraints(c);
				c.weightx = 1.0;
				c.gridwidth = GridBagConstraints.REMAINDER;
				c.fill = GridBagConstraints.HORIZONTAL;
				panel.add(textField, c);
			}
			final JButton closeAboutButton = new JButton();
			closeAboutButton.setAction(findAction("closeAboutBox"));
			initGridBagConstraints(c);
			c.anchor = GridBagConstraints.EAST;
			c.gridx = 1;
			panel.add(closeAboutButton, c);
			final JDialog dialog = new JDialog(owner);
			dialog.setName("aboutDialog");
			dialog.add(panel, BorderLayout.CENTER);
			return dialog;
		}

		private JFileChooser createFileChooser(final File base,
				final String resourceName, final FileFilter filter) {
			final JFileChooser fc = new JFileChooser(base);
			fc.setName(resourceName);
			fc.setMultiSelectionEnabled(false);
			fc.setAcceptAllFileFilterUsed(true);
			fc.setFileFilter(filter);
			getContext().getResourceMap().injectComponents(fc);
			return fc;
		}

		private FileNameExtensionFilter createFileFilter(
				final String resourceName, final String... extensions) {
			final ResourceMap appResourceMap = getContext().getResourceMap();
			final String key = resourceName + ".description";
			final String desc = appResourceMap.getString(key);
			return new FileNameExtensionFilter(desc == null ? key : desc,
					extensions);
		}

		private JMenu createMenu(final String menuName,
				final String[] actionNames) {
			final JMenu menu = new JMenu();
			menu.setName(menuName);
			for (final String actionName : actionNames)
				if (actionName.equals("---"))
					menu.add(new JSeparator());
				else {
					final JMenuItem menuItem = new JMenuItem();
					menuItem.setAction(findAction(actionName));
					menuItem.setIcon(null);
					menu.add(menuItem);
				}
			return menu;
		}

		private File ensureSuffix(final File dst,
				final FileNameExtensionFilter pat) {
			if (pat.accept(dst))
				return dst;
			return new File(dst.getAbsoluteFile() + "."
					+ pat.getExtensions()[0]);
		}

		private javax.swing.Action findAction(final String actionName) {
			return getContext().getActionMap().get(actionName);
		};

		public ApplicationContext getContext() {
			return act;
		};

		private void initGridBagConstraints(final GridBagConstraints c) {
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.NONE;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.gridx = GridBagConstraints.RELATIVE;
			c.gridy = GridBagConstraints.RELATIVE;
			c.insets = zeroInsets;
			c.ipadx = 4; // not the usual default
			c.ipady = 4; // not the usual default
			c.weightx = 0.0;
			c.weighty = 0.0;
		};
	}

	abstract static class WaitCursorTask<T, V> extends Task<T, V> {
		private final SingleFrameApplication app;

		public WaitCursorTask(final SingleFrameApplication app) {
			super(app);
			this.app = app;
		}

		protected abstract T doCursor() throws Exception;

		@Override
		protected T doInBackground() throws Exception {
			final Cursor cu = app.getMainFrame().getCursor();
			try {
				app.getMainFrame().setCursor(waitc);
				Thread.yield();
				return doCursor();
			} finally {
				app.getMainFrame().setCursor(cu);
				Thread.yield();
			}
		}
	}

	static class ZoomHelper {
		/**
		 * Inter-hog area area plus house area plus 1 rock margin plus "out"
		 * rock space.
		 */
		public static final Rectangle2D ActivePlus;
		/** All from back to back */
		public static final Rectangle2D CompletePlus;
		/** House area plus 1 rock margin plus "out" rock space. */
		public static final Rectangle2D HousePlus;
		/** 12-foot circle plus 1 rock */
		public static final Rectangle2D TwelvePlus;
		static {
			final double r2 = 2 * RockProps.DEFAULT.getRadius();
			final double x = IceSize.SIDE_2_CENTER + r2;
			HousePlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE + r2),
					2 * x, IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2
							* r2);
			final double c12 = r2 + Unit.f2m(6.0);
			TwelvePlus = new Rectangle2D.Double(-c12, -c12, 2 * c12, 2 * c12);
			ActivePlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_HOG
					+ IceSize.HOG_2_TEE + r2), 2 * x, IceSize.HOG_2_HOG
					+ IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
			CompletePlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE
					+ IceSize.HOG_2_HOG + IceSize.HACK_2_HOG + r2), 2 * x,
					IceSize.HOG_2_HOG + 2 * IceSize.HACK_2_HOG);
		}

		private void pan(final Zoomable dst, final double rx, final double ry,
				final int dt) {
			if (dst == null)
				return;
			final RectangularShape src = dst.getZoom();
			zoom(dst, new Rectangle2D.Double(src.getX() + src.getWidth() * rx,
					src.getY() + src.getHeight() * ry, src.getWidth(), src
							.getHeight()), dt);
		}

		private void zoom(final Zoomable dst, final Point2D center,
				final double ratio, final int dt) {
			if (dst == null)
				return;
			final RectangularShape src = dst.getZoom();
			final double w = src.getWidth() * ratio;
			final double h = src.getHeight() * ratio;
			final double cx, cy;
			if (center == null) {
				cx = src.getCenterX();
				cy = src.getCenterY();
			} else {
				cx = center.getX();
				cy = center.getY();
			}
			zoom(dst, new Rectangle2D.Double(cx - w / 2, cy - h / 2, Math
					.abs(w), Math.abs(h)), dt);
		}

		private void zoom(final Zoomable dst, final RectangularShape viewport,
				final int dt) {
			if (dst == null)
				return;
			dst.setZoom(viewport, dt);
		}
	}

	private static final BatikButler batik = new BatikButler();
	private static final double currentTime = 30;
	private static final int FAST = 200;
	private static URL initialScene = null;
	private static final Log log = JCLoggerFactory
			.getLogger(JCurlShotPlanner.class);
	private static final int SLOW = 333;
	private static URL templateScene = null;
	private static final Cursor waitc = Cursor
			.getPredefinedCursor(Cursor.WAIT_CURSOR);
	private static final ZoomHelper zh = new ZoomHelper();

	public static void main(final String[] args) {
		// for debugging reasons only:
		Locale.setDefault(Locale.CANADA);
		launch(JCurlShotPlanner.class, args);
	}

	private static void renderPng(final Container src, final File dst)
			throws IOException {
		final BufferedImage img = new BufferedImage(src.getWidth(), src
				.getHeight(), BufferedImage.TYPE_INT_ARGB);
		final Graphics g = img.getGraphics();
		try {
			src.paintAll(g);
		} finally {
			g.dispose();
		}
		ImageIO.write(img, "png", dst);
	}

	private JDialog aboutBox = null;
	private final ChangeManager cm = new ChangeManager(this);

	// static void bind(final Object src, final String src_p, final Object dst,
	// final String dst_p) {
	// Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, src,
	// BeanProperty.create(src_p), dst, BeanProperty.create(dst_p))
	// .bind();
	// }

	private URL document;

	private File file;

	private final GuiUtil gui = new GuiUtil(getContext());

	private FileNameExtensionFilter jcxzPat;

	private boolean modified = false;

	private FileNameExtensionFilter pngPat;

	private FileNameExtensionFilter svgPat;

	private final TrajectoryPiccoloBean tactics = new TrajectoryPiccoloBean();

	private final JLabel url = new JLabel();

	private JCurlShotPlanner() {
		// tactics.setName("tactics");
		url.setName("urlLabel");
	};

	public boolean askDiscardUnsaved(final javax.swing.Action action) {
		if (!isModified())
			return true;
		final String title, msg;
		if (true) {
			final ResourceMap r = getContext().getResourceMap();
			title = r.getString("discard" + ".Dialog" + ".title", action
					.getValue(javax.swing.Action.NAME));
			msg = r.getString("discard" + ".Dialog" + ".message");
		} else if (action instanceof ApplicationAction) {
			final ApplicationAction aa = (ApplicationAction) action;
			final ResourceMap r = getContext().getResourceMap();
			title = action == null ? null : r.getString(aa.getName()
					+ ".Dialog" + ".title", aa
					.getValue(javax.swing.Action.NAME));
			msg = r.getString(aa.getName() + ".Dialog" + ".message");
		} else {
			title = null;
			msg = "Discard unsaved changes?";
		}
		return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
				getMainFrame(), msg, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
	}

	public boolean askOverwrite(final File f) {
		if (!f.exists())
			return true;
		final String title, msg;
		{
			final ResourceMap r = getContext().getResourceMap();
			title = r.getString("overwrite" + ".Dialog" + ".title");
			msg = r.getString("overwrite" + ".Dialog" + ".message", f
					.toString());
		}
		return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
				getMainFrame(), msg, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
	}

	@Action
	public void closeAboutBox() {
		if (aboutBox == null)
			return;
		aboutBox.setVisible(false);
		aboutBox = null;
	}

	private JFileChooser createJcxChooser(final File base, final String name) {
		return gui.createFileChooser(base, name, jcxzPat);
	}

	private JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();

		final String[] fileMenuActionNames = { /*"fileClear",*/
		"fileNewDoc", "fileHammy", "---", "fileOpen", "fileOpenURL", "---",
				"fileReset", "fileSave", "fileSaveAs", "fileSaveCopyAs", "---",
				"fileExportPng", "fileExportSvg", "---", "quit" };
		menuBar.add(gui.createMenu("fileMenu", fileMenuActionNames));

		final String[] editMenuActionNames = { "editUndo", "editRedo", "---",
				"editProperties", "---", "editPreferences" };
		menuBar.add(gui.createMenu("editMenu", editMenuActionNames));

		final String[] viewMenuActionNames = { "viewHouse", "view12Foot",
				"viewComplete", "viewActive", "---", "viewZoomIn",
				"viewZoomOut", "---", "viewPanNorth", "viewPanSouth",
				"viewPanEast", "viewPanWest" };
		menuBar.add(gui.createMenu("viewMenu", viewMenuActionNames));

		final String[] helpMenuActionNames = { "showAboutBox" };
		menuBar.add(gui.createMenu("helpMenu", helpMenuActionNames));

		return menuBar;
	};

	private JFileChooser createPngChooser(final File base, final String name) {
		return gui.createFileChooser(base, name, pngPat);
	}

	private JFileChooser createSvgChooser(final File base, final String name) {
		return gui.createFileChooser(base, name, svgPat);
	};

	private JComponent createToolBar() {
		final String[] toolbarActionNames = { "cut", "copy", "paste" };
		final JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		for (final String actionName : toolbarActionNames) {
			final JButton button = new JButton();
			button.setAction(gui.findAction(actionName));
			button.setFocusable(false);
			toolBar.add(button);
		}
		return toolBar;
	}

	/** Edit Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void editPreferences() {}

	/** Edit Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void editProperties() {}

	/** Edit Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void editRedo() {}

	/** Edit Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void editUndo() {}

	/** File Menu Action */
	@Action
	private void fileClear() {
		if (!askDiscardUnsaved(gui.findAction("fileClear")))
			return;
		try {
			setDocument(null);
		} catch (final IOException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	/** File Menu Action */
	@Action(block = BlockingScope.ACTION)
	public Task<Void, Void> fileExportPng() {
		final JFileChooser fcPng = createPngChooser(getFile(),
				"exportPngFileChooser");
		for (;;) {
			if (JFileChooser.APPROVE_OPTION != fcPng
					.showSaveDialog(getMainFrame()))
				return null;
			final File dst = gui.ensureSuffix(fcPng.getSelectedFile(), pngPat);
			if (!askOverwrite(dst))
				continue;

			return new WaitCursorTask<Void, Void>(this) {
				@Override
				protected Void doCursor() throws Exception {
					renderPng(tactics, dst);
					return null;
				}
			};
		}
	}

	/** File Menu Action */
	@Action(enabledProperty = "renderSvgAvailable", block = BlockingScope.ACTION)
	public Task<Void, Void> fileExportSvg() {
		final JFileChooser fcSvg = createSvgChooser(getFile(),
				"exportSvgFileChooser");
		for (;;) {
			if (JFileChooser.APPROVE_OPTION != fcSvg
					.showSaveDialog(getMainFrame()))
				return null;
			final File dst = gui.ensureSuffix(fcSvg.getSelectedFile(), svgPat);
			if (!askOverwrite(dst))
				continue;

			return new WaitCursorTask<Void, Void>(this) {
				@Override
				protected Void doCursor() throws Exception {
					batik.renderSvg(tactics, dst);
					return null;
				}
			};
		}
	}

	/** File Menu Action */
	@Action(block = BlockingScope.APPLICATION)
	public void fileHammy() {
		if (!askDiscardUnsaved(gui.findAction("fileHammy")))
			return;
		try {
			setDocument(initialScene);
		} catch (final IOException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	/** File Menu Action */
	@Action
	public void fileNewDoc() {
		if (!askDiscardUnsaved(gui.findAction("fileNewDoc")))
			return;
		try {
			setDocument(templateScene);
		} catch (final IOException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	/** File Menu Action */
	@Action
	public void fileOpen() {
		if (!askDiscardUnsaved(gui.findAction("fileOpen")))
			return;
		final JFileChooser chooser = createJcxChooser(getFile(),
				"openFileChooser");
		if (chooser.showOpenDialog(getMainFrame()) == JFileChooser.APPROVE_OPTION) {
			final File file = chooser.getSelectedFile();
			try {
				setDocument(file.toURI().toURL());
			} catch (final MalformedURLException e) {
				// shouldn't happen unless the JRE fails
				log.warn("File.toURI().toURL() failed", e);
			} catch (final IOException e) {
				showErrorDialog("can't open \"" + file + "\"", e);
			}
		}
	}

	/** File Menu Action */
	@Action
	public void fileOpenURL() {
		final String a = "fileOpenURL";
		if (!askDiscardUnsaved(gui.findAction(a)))
			return;
		final ResourceMap r = getContext().getResourceMap();
		final String title = r.getString(a + ".Dialog" + ".title");
		final String msg = r.getString(a + ".Dialog" + ".message");
		for (;;) {
			final String url = JOptionPane.showInputDialog(getMainFrame(), msg,
					title, JOptionPane.QUESTION_MESSAGE);
			if (url == null)
				return;
			try {
				setDocument(new URL(url));
				return;
			} catch (final IOException e) {
				showErrorDialog(r.getString(a + ".Dialog" + ".error", url), e);
			}
		}
	}

	/**
	 * File Menu Action
	 * 
	 * @throws IOException
	 */
	@Action(enabledProperty = "modified")
	public void fileReset() throws IOException {
		if (!askDiscardUnsaved(gui.findAction("fileReset")))
			return;
		final URL tmp = getDocument();
		setDocument(null);
		setDocument(tmp);
	};

	/** File Menu Action */
	@Action(enabledProperty = "modified")
	public void fileSave() {
		if (!isModified())
			return;
		final File f = saveHelper(getFile(), getFile(), "saveFileChooser", true);
		log.info(f);
		if (f != null) {
			try {
				setDocument(f.toURL(), false);
			} catch (final IOException e) {
				throw new RuntimeException("Unhandled", e);
			}
			setModified(false);
		}
	}

	/** File Menu Action */
	@Action
	public void fileSaveAs() {
		final File f = saveHelper(null, getFile(), "saveAsFileChooser", false);
		log.info(f);
		if (f != null) {
			try {
				setDocument(f.toURL(), false);
			} catch (final IOException e) {
				throw new RuntimeException("Unhandled", e);
			}
			setModified(false);
		}
	};

	/** File Menu Action */
	@Action
	public void fileSaveCopyAs() {
		log.info(saveHelper(null, getFile(), "saveCopyAsFileChooser", false));
	};

	private URL getDocument() {
		return document;
	}

	private File getFile() {
		return file;
	}

	/**
	 * Setting the internal field {@link #document} directly (bypassing
	 * {@link #setDocument(URL)}) is used to deplay the document loading until
	 * {@link #ready()}.
	 */
	@Override
	protected void initialize(final String[] as) {
		if ("Linux".equals(System.getProperty("os.name")))
			getContext().getResourceManager().setPlatform("linux");

		final Class<?> mc = this.getClass();
		{
			final String res;
			if (true) {
				final ResourceMap r = Application.getInstance().getContext()
						.getResourceMap();
				res = "/" + r.getResourcesDir()
						+ r.getString("Application.defaultDocument");
			} else
				res = "/" + mc.getPackage().getName().replace('.', '/')
						+ "/resources" + "/" + "default.jcz";
			initialScene = mc.getResource(res);
		}

		// schedule the document to load in #ready()
		document = initialScene;
		for (final String p : as) {
			// ignore javaws parameters
			if ("-open".equals(p) || "-print".equals(p))
				continue;
			try {
				document = new URL(p);
				break;
			} catch (final MalformedURLException e) {
				final File f = new File(p);
				if (f.canRead())
					try {
						document = f.toURL();
						break;
					} catch (final MalformedURLException e2) {
						log.warn("Cannot load '" + p + "'.", e);
					}
				else
					log.warn("Cannot load '" + p + "'.", e);
			}
		}
	}

	public boolean isAlwaysFalse() {
		return false;
	}

	public boolean isModified() {
		return modified;
	}

	public boolean isRenderSvgAvailable() {
		return batik.isBatikAvailable();
	}

	@Override
	protected void ready() {
		final URL tmp = document;
		document = null;
		try {
			setDocument(tmp);
		} catch (final IOException e) {
			log.warn("Couldn't load '" + tmp + "'.", e);
		}
		addExitListener(new Application.ExitListener() {
			public boolean canExit(final EventObject e) {
				return askDiscardUnsaved(gui.findAction("quit"));
			}

			public void willExit(final EventObject e) {
				log.info("Good bye!");
			}
		});
	}

	private final void save(final TrajectorySet cts, final File dst)
			throws IOException {
		final Cursor cu = switchCursor(waitc);
		try {
			final IOTrajectories t = new IOTrajectories();
			// TODO add annotations
			t.trajectories().add(cts);
			new JCurlSerializer().write(t, dst, JDKSerializer.class);
		} finally {
			switchCursor(cu);
		}
	}

	private File saveHelper(File dst, final File base, final String name,
			final boolean forceOverwrite) {
		JFileChooser fcJcx = null;
		for (;;) {
			if (fcJcx == null)
				fcJcx = createJcxChooser(base, name);
			if (dst == null) {
				if (JFileChooser.APPROVE_OPTION != fcJcx
						.showSaveDialog(getMainFrame()))
					return null;
				dst = fcJcx.getSelectedFile();
			}
			if (dst == null)
				continue;
			dst = gui.ensureSuffix(dst, jcxzPat);
			if (forceOverwrite || askOverwrite(dst))
				try {
					save(tactics.getCurves(), dst);
					return dst;
				} catch (final Exception e) {
					showErrorDialog("Couldn't save to '" + dst + "'", e);
				}
		}
	}

	private void setDocument(final URL document) throws IOException {
		this.setDocument(document, true);
	}

	private void setDocument(final URL document, boolean load)
			throws IOException {
		final Cursor cu = switchCursor(waitc);
		try {
			log.info(document);
			final URL old = this.document;
			this.firePropertyChange("document", old, this.document = document);
			setFile(this.document);
			url.setText(this.document == null ? "{null}" : this.document
					.toString());
			if (!load)
				return;

			cm.deregister(tactics.getCurves());
			final ComputedTrajectorySet cts;
			if (this.document == null)
				cts = null;
			else {
				final IONode n = new JCurlSerializer().read(this.document);
				final IOTrajectories it = (IOTrajectories) n;
				final TrajectorySet ts = it.trajectories().get(0);
				cts = (ComputedTrajectorySet) ts;
			}
			if (cts != null)
				cts.setCurrentTime(currentTime);
			tactics.setCurves(cts);
			// TODO just push to the Swing Trajectory Bean
			// bpm = tp.getBroom();
			cm.register(tactics.getCurves());
			setModified(false);
		} finally {
			switchCursor(cu);
		}
	}

	private void setFile(final URL url) {
		File file;
		if (url != null && "file".equals(url.getProtocol()))
			try {
				file = new File(url.toURI());
			} catch (final URISyntaxException e) {
				file = null;
			}
		else
			file = null;
		final File old = this.file;
		this.firePropertyChange("file", old, this.file = file);
	};

	public void setModified(final boolean modified) {
		final boolean old = this.modified;
		firePropertyChange("modified", old, this.modified = modified);
	}

	/**
	 * Show the about box dialog.
	 */
	@Action(block = BlockingScope.COMPONENT)
	public void showAboutBox() {
		if (aboutBox == null)
			aboutBox = gui.createAboutBox(getMainFrame());
		show(aboutBox);
	}

	private void showErrorDialog(final String message, final Exception e) {
		JOptionPane.showMessageDialog(getMainFrame(), "Error: " + message,
				"Error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	protected void startup() {
		// set the window icon:
		{
			final Image img;
			if (true)
				img = getContext().getResourceMap().getImageIcon(
						"Application.icon").getImage();
			else {
				final ResourceMap r = getContext().getResourceMap();
				if (true)
					try {
						img = ImageIO.read(this.getClass().getResource(
								"/" + r.getResourcesDir() + "/"
										+ r.getString("Application.icon")));
					} catch (final IOException e) {
						throw new RuntimeException("Unhandled", e);
					}
				else
					img = Toolkit.getDefaultToolkit().createImage(
							this.getClass().getResource(
									"/" + r.getResourcesDir() + "/"
											+ r.getString("Application.icon")));
			}
			getMainFrame().setIconImage(img);
			// SystemTray tray = SystemTray.getSystemTray();
		}

		// File Filter
		jcxzPat = gui.createFileFilter("fileFilterJcxz", "jcz", "jcx");
		pngPat = gui.createFileFilter("fileFilterPng", "png");
		svgPat = gui.createFileFilter("fileFilterSvg", "svgz", "svg");

		getMainFrame().setJMenuBar(createMenuBar());

		final JComponent c = new JPanel();
		c.setLayout(new BorderLayout());
		tactics.setPreferredSize(new Dimension(400, 600));
		c.add(tactics, BorderLayout.CENTER);
		c.add(url, BorderLayout.NORTH);
		final BroomSwingBean swing = new BroomSwingBean();
		swing.setBroom(tactics.getBroom());
		{
			final JPanel b = new JPanel();
			b.setLayout(new BorderLayout());
			final JTabbedPane t = new JTabbedPane(SwingConstants.TOP,
					JTabbedPane.SCROLL_TAB_LAYOUT);
			t.add("Rock", swing);
			t.setMnemonicAt(0, 'R');
			t.add("Ice", new JLabel("TODO: Ice settings"));
			t.setMnemonicAt(1, 'I');
			t.add("Collission", new JLabel("TODO: Collission settings"));
			t.setMnemonicAt(2, 'C');
			b.add(t, BorderLayout.NORTH);
			b.add(new JLabel("TODO: Bird's eye view"), BorderLayout.CENTER);
			c.add(b, BorderLayout.EAST);
		}

		show(c);

		viewHouse();
	}

	private Cursor switchCursor(final Cursor neo) {
		final Cursor cu = getMainFrame().getCursor();
		getMainFrame().setCursor(neo);
		Thread.yield();
		return cu;
	}

	/** View Menu Action */
	@Action
	public void view12Foot() {
		zh.zoom(tactics, ZoomHelper.TwelvePlus, SLOW);
	}

	/** View Menu Action */
	@Action
	public void viewActive() {
		zh.zoom(tactics, ZoomHelper.ActivePlus, SLOW);
	}

	/** View Menu Action */
	@Action
	public void viewComplete() {
		zh.zoom(tactics, ZoomHelper.CompletePlus, SLOW);
	}

	/** View Menu Action */
	@Action
	public void viewHouse() {
		zh.zoom(tactics, ZoomHelper.HousePlus, SLOW);
	}

	/** View Menu Action */
	@Action
	public void viewPanEast() {
		zh.pan(tactics, 0.2, 0, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewPanNorth() {
		zh.pan(tactics, 0, -0.2, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewPanSouth() {
		zh.pan(tactics, 0, 0.2, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewPanWest() {
		zh.pan(tactics, -0.2, 0, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewZoomIn() {
		zh.zoom(tactics, null, 0.75, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewZoomOut() {
		zh.zoom(tactics, null, 1.25, FAST);
	}
}
