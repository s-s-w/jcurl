package org.jcurl.demo.tactics;

import java.awt.BorderLayout;

import javax.swing.JComponent;

/**
 * Aggregate {@link IceControl}, {@link ZuiPanel}, {@link RockControl}.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
class MainPanel extends JComponent {
    private static final long serialVersionUID = -3700593329890044115L;

    private final IceControl i;

    private final RockControl r;

    public MainPanel(final MainMod model, final ZuiPanel p) {
        setLayout(new BorderLayout());
        this.add(p, BorderLayout.CENTER);
        this.add(r = new RockControl(model), BorderLayout.EAST);
        this.add(i = new IceControl(model.getCurler()), BorderLayout.WEST);
    }
}