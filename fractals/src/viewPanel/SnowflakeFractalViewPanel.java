package viewPanel;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class SnowflakeFractalViewPanel extends FractalViewPanel {

    public static Builder builder = new Builder();
    private static final double sin120 = Math.sin(Math.toRadians(-60));
    private ArrayList<Line2D.Double> lines = new ArrayList<>();

    public static class Builder {

        private SnowflakeFractalViewPanel snowflakeFractalViewPanel = new SnowflakeFractalViewPanel();
        public SnowflakeFractalViewPanel.Builder buildInitTriangle(final Point2D pointA, final Point2D pointB, final Point2D pointC) {

            snowflakeFractalViewPanel.lines.add(new Line2D.Double(pointA, pointB));
            snowflakeFractalViewPanel.lines.add(new Line2D.Double(pointB, pointC));
            snowflakeFractalViewPanel.lines.add(new Line2D.Double(pointC, pointA));
            return this;
        }
        public SnowflakeFractalViewPanel build() {
            return snowflakeFractalViewPanel;
        }
    }

    @Override
    public void fractalIterate() {

        ArrayList<Line2D.Double> newLines = new ArrayList<>();
        lines.forEach( line -> {
            final Point2D pOne = line.getP1();
            final Point2D pTwo = line.getP2();

            final Point2D distance = new Point2D.Double (
                (pTwo.getX() - pOne.getX()) / 3,
                (pTwo.getY() - pOne.getY()) / 3
            );
            final Point2D pA = new Point2D.Double (
                pOne.getX() + distance.getX(),
                pOne.getY() + distance.getY()
            );
            final Point2D pB = new Point2D.Double (
                pTwo.getX() - distance.getX(),
                pTwo.getY() - distance.getY()
            );

            final Point2D pTip = new Point2D.Double (
                pA.getX() + (int)(distance.getX() * 0.5 + distance.getY() * sin120),
                pA.getY() + (int)(distance.getY() * 0.5 - distance.getX() * sin120)
            );

            newLines.add(new Line2D.Double(pOne, pA));
            newLines.add(new Line2D.Double(pA, pTip));
            newLines.add(new Line2D.Double(pTip, pB));
            newLines.add(new Line2D.Double(pB, pTwo));
        });
        lines = newLines;
        repaint();
    }

    @Override
    public void paintComponent(final Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.white);
        for (Line2D.Double line : lines) {
            g.drawLine(
                (int) line.getX1(),
                (int) line.getY1(),
                (int) line.getX2(),
                (int) line.getY2()
            );
        }
    }
}
