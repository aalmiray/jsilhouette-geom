package org.codehaus.griffon.jsilhouette.geom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

public class TriangleExample {
   public static JPanel canvas() {
      return new JPanel() {
         public void paint( Graphics g ) {
            Triangle triangle = new Triangle(10, 80, 80, 0);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
               RenderingHints.KEY_ANTIALIASING,
               RenderingHints.VALUE_ANTIALIAS_ON
            );
            Rectangle bounds = getBounds();
            g2d.setBackground(Color.WHITE);
            g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g2d.setColor(Color.BLUE);
            g2d.fill(triangle);
            g2d.setColor(Color.BLACK);
            g2d.draw(triangle);
            g2d.drawRect(bounds.x, bounds.y, bounds.width-1, bounds.height-1);
         }
      };
   }

   public static JFrame buildUI() {
      JFrame frame = new JFrame("Triangle");
      frame.getContentPane().add(canvas());
      frame.setSize(new Dimension(110,120));
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      return frame;
   }

   public static void main( String[] args ) {
      SwingUtilities.invokeLater( new Runnable() {
         public void run() {
            buildUI().setVisible(true);
         }
      });
   }
}
