package controller;

import viewPanel.FractalViewPanel;
import viewPanel.MandelbrotFractalViewPanel;
import viewPanel.SnowflakeFractalViewPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class FractalController {

    private static FractalViewPanel fractalViewPanel;
    public static void main(final String[] args) {

        Runnable r = () -> {
            boolean invalid;
            do {
                final String fractalType = JOptionPane.showInputDialog(
                    "Enter your choice \n" +
                        "Koch Snowflake : (s) \n" +
                        "Mandelbrot     : (m) \n"
                );
                invalid = false;
                switch (fractalType) {
                    case "s" : {
                        fractalViewPanel = getSnowflakeFractalViewPanel();
                        break;
                    } case "m" : {
                        fractalViewPanel = getMandlebrotFractalViewPanel();
                        break;
                    } default: {
                        invalid = true;
                    }
                }
            } while (invalid);

            final JButton jButton = new JButton("Iterate");
            jButton.addActionListener(e -> fractalViewPanel.fractalIterate());
            final JFrame jFrame = new JFrame();
            jFrame.setContentPane(fractalViewPanel);
            jFrame.getContentPane().add(jButton);
            jFrame.setPreferredSize(new Dimension(1366, 768));
            jFrame.setVisible(true);
            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jFrame.setResizable(false);
            jFrame.pack();
        };
        SwingUtilities.invokeLater(r);
    }
    private static SnowflakeFractalViewPanel getSnowflakeFractalViewPanel() {
        final List<Point2D.Double> points = new ArrayList<>();
        final String triangleType = JOptionPane.showInputDialog(
            "Enter your choice \n" +
                "Provide Triangle : (p) \n" +
                "Random Triangle  : (r) \n" +
                "Default Triangle : (anything else) \n"
        );
        if(triangleType.equalsIgnoreCase("r")) {
            points.add(getRandomPointInRange(499,867, 100, 468));
            points.add(getRandomPointInRange(499,867, 100, 468));
            points.add(getRandomPointInRange(499,867, 100, 468));
        } else if(triangleType.equalsIgnoreCase("p")) {
            IntStream
                .range(0,3)
                .forEach( i-> points.add(
                    new Point2D.Double(
                        Integer.parseInt(JOptionPane.showInputDialog("Point "+(i+1)+" : X")),
                        Integer.parseInt(JOptionPane.showInputDialog("Point "+(i+1)+" : Y"))
                    )
                ));
        } else {
            points.add(new Point2D.Double(499, 468));
            points.add(new Point2D.Double(867, 468));
            points.add(new Point2D.Double(683, 100));
        }
        return SnowflakeFractalViewPanel
            .builder
            .buildInitTriangle(
                points.get(0),
                points.get(1),
                points.get(2)
            ).build();
    }
    private static MandelbrotFractalViewPanel getMandlebrotFractalViewPanel() {
        return MandelbrotFractalViewPanel
            .builder
            .buildWithFractalSize(668)
            .build();
    }

    private static Point2D.Double getRandomPointInRange(final int minX, final int maxX, final int minY, final int maxY) {
        return new Point2D.Double(
            getRandomIntInRange(minX, maxX),
            getRandomIntInRange(minY, maxY)
        );
    }
    private static int getRandomIntInRange(final int min, final int max) {
        return new Random()
            .nextInt((max - min) + 1) + min;
    }
}
