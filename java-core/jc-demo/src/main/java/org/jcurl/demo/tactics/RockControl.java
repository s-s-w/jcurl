package org.jcurl.demo.tactics;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

/**
 * Set played rock, broom position, split time and handle via {@link JSlider}s.
 * etc.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: MainApp.java 685 2007-08-18 20:18:00Z mrohrmoser $
 */
class RockControl extends JComponent {

    private static final long serialVersionUID = 1286753237795097988L;

    public RockControl(final RockMod model) {
        setLayout(new BorderLayout());
        final JLabel l = new JLabel("Rock");
        this.add(l, BorderLayout.NORTH);
        JSlider s = new JSlider();
        s.setOrientation(SwingConstants.VERTICAL);
        this.add(s, BorderLayout.WEST);
        s = new JSlider();
        s.setOrientation(SwingConstants.VERTICAL);
        this.add(s, BorderLayout.CENTER);
        s = new JSlider();
        s.setOrientation(SwingConstants.VERTICAL);
        this.add(s, BorderLayout.EAST);
    }
}