package edu.mccc.cos210.fp2014.col.client.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.JComponent;

public class ColorButtonUI extends BasicButtonUI {

    private Color backgroundColor;
    private Color SELECT_COLOR = backgroundColor;

    public ColorButtonUI(Color background) {
    	backgroundColor = background;
    }

    @Override
    protected void paintText(Graphics g, AbstractButton b, Rectangle r, String t) {
        super.paintText(g, b, r, t);
        g.setColor(SELECT_COLOR);
        g.drawRect(r.x, r.y, r.width, r.height);
    }

    @Override
    protected void paintFocus(Graphics g, AbstractButton b,
        Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
        super.paintFocus(g, b, viewRect, textRect, iconRect);
        g.setColor(Color.blue.darker());
        g.drawRect(viewRect.x, viewRect.y, viewRect.width, viewRect.height);
    }

    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        if (b.isContentAreaFilled()) {
            g.setColor(SELECT_COLOR);
            g.fillRect(0, 0, b.getWidth(), b.getHeight());
        }
    }

    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        b.setFont(b.getFont().deriveFont(11f));
        b.setBackground(backgroundColor);
    }

    public ColorButtonUI() {
        super();
    }
}