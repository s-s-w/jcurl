package org.jcurl.demo.tactics;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import org.jcurl.core.base.Curler;

/**
 * Set draw-to-tee time and curl via {@link JSlider}s.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: MainApp.java 685 2007-08-18 20:18:00Z mrohrmoser $
 */
class IceControl extends JComponent {
    private static final long serialVersionUID = -3381158569292004932L;

    public IceControl(final Curler model) {
        setLayout(new BorderLayout());
        final JLabel l = new JLabel("Ice");
        this.add(l, BorderLayout.NORTH);
        JSlider s = new JSlider();
        s.setOrientation(SwingConstants.VERTICAL);
        this.add(s, BorderLayout.WEST);
        s = new JSlider();
        s.setOrientation(SwingConstants.VERTICAL);
        this.add(s, BorderLayout.CENTER);
    }
}