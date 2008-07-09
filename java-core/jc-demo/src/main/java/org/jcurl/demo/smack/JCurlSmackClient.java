/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
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

package org.jcurl.demo.smack;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.MutableObject;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.FileNameExtensionFilter;
import org.jcurl.demo.tactics.JCurlShotPlanner;
import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task.BlockingScope;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * A very simple Jabber/Xmpp client. This a merely an experiment to gain
 * experience.
 * <p>
 * The goal is to add collaborative editing to and instant messaging e.g.
 * {@link JCurlShotPlanner}. <a
 * href="http://en.wikipedia.org/wiki/J.C.R._Licklider">J. C. R. Licklider</a>
 * suggested this <b>1968</b> in his paper <a
 * href="http://gatekeeper.dec.com/pub/DEC/SRC/publications/taylor/licklider-taylor.pdf">The
 * Computer as a Communication Device</a>. So much about the "fast-lived" IT
 * business.
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class JCurlSmackClient extends SingleFrameApplication {
	static class GuiUtil {

		private static final Pattern check = Pattern.compile("\\[\\](.+)");
		private static final Insets zeroInsets = new Insets(0, 0, 0, 0);
		private final ApplicationContext act;

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
				initGridBagConstraints(c);
				// c.anchor = GridBagConstraints.BASELINE_TRAILING; 1.6 ONLY
				c.anchor = GridBagConstraints.EAST;
				panel.add(label, c);
				initGridBagConstraints(c);
				c.weightx = 1.0;
				c.gridwidth = GridBagConstraints.REMAINDER;
				c.fill = GridBagConstraints.HORIZONTAL;
				final JTextField textField = new JTextField();
				textField.setName(field + "TextField");
				textField.setEditable(false);
				textField.setBorder(null);
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
			return createMenu(menuName, actionNames, null);
		}

		private JMenu createMenu(final String menuName,
				final String[] actionNames, JMenu menu) {
			if (menu == null)
				menu = new JMenu();
			menu.setName(menuName);
			for (String actionName : actionNames)
				if (actionName.equals("---"))
					menu.add(new JSeparator());
				else {
					final JMenuItem menuItem;
					{
						final Matcher m = check.matcher(actionName);
						if (m.matches()) {
							actionName = m.group(1);
							menuItem = new JCheckBoxMenuItem();
						} else
							menuItem = new JMenuItem();
					}
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

	static class XmppAccount extends MutableObject {
		private static final Log lo = JCLoggerFactory
				.getLogger(XmppAccount.class);
		private transient XMPPConnection conn;
		private CharSequence pwd;
		private XmppAddress uid;

		public XMPPConnection getConn() {
			return conn;
		}

		public CharSequence getPwd() {
			return pwd;
		}

		public XmppAddress getUid() {
			return uid;
		}

		public XMPPConnection login(final String resource) throws XMPPException {
			logout();
			final XMPPConnection conn = new XMPPConnection(uid.getHost());
			conn.connect();
			lo.info("connected: " + conn.isConnected() + ", secure: "
					+ conn.isSecureConnection() + ", TLS: " + conn.isUsingTLS()
					+ ", compressed: " + conn.isUsingCompression());
			conn.login(uid.getAccount(), pwd.toString(), resource);
			setConn(conn);
			return getConn();
		}

		public void logout() {
			if (conn != null && conn.isConnected())
				conn.disconnect();
			setConn(null);
		}

		private void setConn(final XMPPConnection conn) {
			final XMPPConnection old = this.conn;
			firePropertyChange("conn", old, this.conn = conn);
		}

		public void setPwd(final CharSequence pwd) {
			this.pwd = pwd;
		}

		public void setUid(final XmppAddress uid) {
			this.uid = uid;
		}
	}

	private static final Log log = JCLoggerFactory
			.getLogger(JCurlSmackClient.class);

	private static Properties loadClassProps(final Class<?> c, Properties p)
			throws IOException {
		if (p == null)
			p = new Properties();
		final String r = "/" + c.getName().replace('.', '/') + ".properties";
		final InputStream i = c.getResourceAsStream(r);
		if (i == null) {
			System.err.println("Add a file '." + r
					+ "' to the classpath containing the properties:");
			System.err
					.println("\nacc_uid = foo@foo.org # a jabber account name");
			System.err.println("\nacc_pwd = .. # a jabber account password");
		}
		p.load(i);
		return p;
	}

	public static void main(final String[] args) {
		launch(JCurlSmackClient.class, args);
	}

	private JDialog aboutBox = null;
	private final XmppAccount acc = new XmppAccount();
	private final GuiUtil gui = new GuiUtil(getContext());
	private JCheckBoxMenuItem miRoster;
	private final String resource;
	private final ChatSimpleSwingBean sca = new ChatSimpleSwingBean();
	private final ChatLogSimpleSwingBean slo = new ChatLogSimpleSwingBean();
	private final RosterSimpleSwingBean sro = new RosterSimpleSwingBean();

	public JCurlSmackClient() {
		resource = this.getClass().getSimpleName();
		sro.setStar(Pattern.compile("[^/]+/" + resource));
	}

	@Action
	public void closeAboutBox() {
		if (aboutBox == null)
			return;
		aboutBox.setVisible(false);
		aboutBox = null;
	}

	private JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();

		final String[] xmppMenuActionNames = { "xmppAccount", "[]xmppSession",
				"[]xmppChat", "xmppPreferences", "---", "[]xmppRoster", "---",
				"quit" };
		final JMenu menu = gui.createMenu("xmppMenu", xmppMenuActionNames);
		miRoster = (JCheckBoxMenuItem) menu.getMenuComponent(5);
		menuBar.add(menu);

		final String[] helpMenuActionNames = { "showAboutBox" };
		menuBar.add(gui.createMenu("helpMenu", helpMenuActionNames));

		return menuBar;
	}

	public boolean isAlwaysFalse() {
		return false;
	}

	/** Show the about box dialog. */
	@Action(block = BlockingScope.COMPONENT)
	public void showAboutBox() {
		if (aboutBox == null)
			aboutBox = gui.createAboutBox(getMainFrame());
		show(aboutBox);
	}

	@Override
	protected void startup() {
		getMainFrame().setJMenuBar(createMenuBar());
		miRoster.setSelected(true);

		final JComponent pv = new JPanel();
		pv.setLayout(new BorderLayout());
		final Box conversation = Box.createVerticalBox();
		pv.add(new JScrollPane(conversation,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
		pv.add(new JScrollPane(sca,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS),
				BorderLayout.SOUTH);

		final JSplitPane ph = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		ph.setResizeWeight(0.8);
		ph.add(pv, JSplitPane.LEFT);
		ph.add(new JScrollPane(sro), JSplitPane.RIGHT);

		xmppRoster();
		show(ph);

		// connect the jabber account
		new Thread(new Runnable() {
			public void run() {
				try {
					// get uid + pwd from a .properties file
					final Properties p = loadClassProps(JCurlSmackClient.class,
							null);
					acc.setUid(XmppAddress.parse(p.getProperty("acc_uid")));
					acc.setPwd(p.getProperty("acc_pwd"));
					// login and get the present buddies
					acc.login(resource);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							sro.setConn(acc.getConn());
							// Wire up xmpp stuff:
							final ChatManager cm = sro.getChatManager();
							cm.addChatListener(sca);
							cm.addChatListener(slo);
						}
					});
				} catch (final IOException e) {
					throw new RuntimeException("Unhandled", e);
				} catch (final XMPPException e) {
					throw new RuntimeException("Unhandled", e);
				}
			}
		}).start();
	}

	/** Xmpp Menu Action */
	@Action
	public void xmppAccount() {
		log.info("");
	}

	/** Xmpp Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void xmppChat() {
		log.info("");
	}

	/** Xmpp Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void xmppPreferences() {
		log.info("");
	}

	/** Xmpp Menu Action */
	@Action
	public void xmppRoster() {
		sro.setEnabled(miRoster.isSelected());
		// sro.getParent().setVisible(miRoster.isSelected());
	}

	/** Xmpp Menu Action */
	@Action
	public void xmppSession() {
		log.info("");
	}
}
