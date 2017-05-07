package viewPanel;

import utility.Complex;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MandelbrotFractalViewPanel extends FractalViewPanel {

    private int size;
    private int marginWidth;
    private int marginHeight;

    private static final int max = 50;
    public static Builder builder = new Builder();

    private static int multiplier = 1;

    public static class Builder {

        private MandelbrotFractalViewPanel mandelbrotFractalViewPanel = new MandelbrotFractalViewPanel();

        public MandelbrotFractalViewPanel.Builder buildWithFractalSize(final int size) {
            mandelbrotFractalViewPanel.size = size;
            mandelbrotFractalViewPanel.marginWidth = (1360 - size) / 2;
            mandelbrotFractalViewPanel.marginHeight = (760 - size) / 2;
            return this;
        }

        public MandelbrotFractalViewPanel build() {
            return mandelbrotFractalViewPanel;
        }
    }

    @Override
    public void fractalIterate() {
        double scale = 2;
        double xc = -0.5;
        double yc = 0;
        final Graphics g = getGraphics();

        while (multiplier != Integer.MAX_VALUE) {
            final String colorMultiplier = JOptionPane.showInputDialog("Enter color code multiplier\n");
            if(colorMultiplier.matches("\\d+")) {
                multiplier = Integer.parseInt(colorMultiplier);
            } else {
                continue;
            }

            ExecutorService executorService = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    int finalI = i;
                    int finalJ = j;
                    executorService.execute(
                        () -> {
                            double x0 = xc - scale / 2 + scale * finalI / size;
                            double y0 = yc - scale / 2 + scale * finalJ / size;
                            int color = max - mandelbrotEquation(new Complex(x0, y0), max);
                            g.setColor(new Color(color * multiplier));
                            g.drawOval(finalI + marginWidth, finalJ + marginHeight, 0, 0);
                        }
                    );

                }
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            multiplier++;
        }
    }

    private int mandelbrotEquation(final Complex z0, final int max) {
        Complex z = z0;
        for (int i = 0; i < max; i++) {
            if (z.abs() > 2.0) {
                return i;
            }
            z = z.times(z).plus(z0);
        }
        return max;
    }
}
