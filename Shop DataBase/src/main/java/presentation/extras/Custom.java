package presentation.extras;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class Custom extends JButton {

    private final Color startColor = new Color(0, 125, 12);
    private final Color endColor = new Color(0, 0, 0);

    public Custom(String text) {
        super(text);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradientPaint = new GradientPaint(
                new Point2D.Double(0, 0),
                startColor,
                new Point2D.Double(0, getHeight()),
                endColor);
        g2.setPaint(gradientPaint);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));

        g2.dispose();
        super.paintComponent(g);
    }
}
