package org.jcurl.zui.scenario;

import java.awt.Font;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.sun.scenario.scenegraph.JSGPanel;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGText;

/**
 * From http://forums.java.net/jive/thread.jspa?threadID=34259&tstart=45
 */
public class HelloWorld {

	public static void main(final String[] args) {
		final JFrame f = new JFrame("Demo");
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		final JSGPanel panel = new JSGPanel();
		final SGGroup rootNode = new SGGroup();
		panel.setScene(rootNode);

		final SGText textNode = new SGText();
		textNode.setText("Hello World");
		textNode.setLocation(new Point(50, 50));
		textNode.setFont(new Font("Sanserif", Font.PLAIN, 12));
		rootNode.add(textNode);

		f.add(panel);
		f.setSize(300, 200);
		f.setVisible(true);
	}
}