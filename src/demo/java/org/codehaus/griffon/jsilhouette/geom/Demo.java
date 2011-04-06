/*
 * Copyright (c) 2008-2011, Andres Almiray
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of jSilhouette nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.codehaus.griffon.jsilhouette.geom;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Andres Almiray
 */
public class Demo {
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable(){
         public void run() {
            JFrame frame = new JFrame("jSilhouette Shapes (Java)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(500,400));
            frame.getContentPane().add(createDemoPanel());
            frame.setVisible(true);
         }
      });
   }

   private static JPanel createDemoPanel() {
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout(5,5));

      final CanvasPanel canvas = new CanvasPanel();
      JPanel controls = new JPanel();
      controls.setLayout(new GridLayout(10,1));
      panel.add(controls, BorderLayout.WEST);
      panel.add(canvas, BorderLayout.CENTER);

      controls.add(newButton("Arrow", new ActionListener(){
         public void actionPerformed(ActionEvent event) {
            drawArrows(canvas);
         }
      }));
      controls.add(newButton("Balloon", new ActionListener(){
         public void actionPerformed(ActionEvent event) {
            drawBalloons(canvas);
         }
      }));
      controls.add(newButton("Cross", new ActionListener(){
         public void actionPerformed(ActionEvent event) {
            drawCrosses(canvas);
         }
      }));
      controls.add(newButton("Donut", new ActionListener(){
         public void actionPerformed(ActionEvent event) {
            drawDonuts(canvas);
         }
      }));
      controls.add(newButton("MultiRoundRectangle", new ActionListener(){
         public void actionPerformed(ActionEvent event) {
            drawMultiRoundRectangles(canvas);
         }
      }));
      controls.add(newButton("Rays", new ActionListener(){
         public void actionPerformed(ActionEvent event) {
            drawRays(canvas);
         }
      }));
      controls.add(newButton("RegularPolygon", new ActionListener(){
         public void actionPerformed(ActionEvent event) {
            drawRegularPolygons(canvas);
         }
      }));
      controls.add(newButton("RoundPin", new ActionListener(){
         public void actionPerformed(ActionEvent event) {
            drawRoundPins(canvas);
         }
      }));
      controls.add(newButton("Star", new ActionListener(){
         public void actionPerformed(ActionEvent event) {
            drawStars(canvas);
         }
      }));
      controls.add(newButton("Triangle", new ActionListener(){
         public void actionPerformed(ActionEvent event) {
            drawTriangles(canvas);
         }
      }));

      return panel;
   }

   private static JButton newButton(String text, ActionListener listener) {
      JButton button = new JButton(text);
      button.addActionListener(listener);
      return button;
   }

   private static void drawArrows(CanvasPanel canvas) {
      canvas.setPaintOperation(new PaintOperation() {
         public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; 

            drawShape(g2, new Arrow(20,20,100,60), Color.RED);
            drawShape(g2, new Arrow(140,20,100,60,0.5f,0.2f), Color.GREEN);
            drawShape(g2, new Arrow(20,100,100,60,0.75f,0.5f), Color.BLUE);
            drawShape(g2, new Arrow(140,100,100,60,0.25f,0.5f), Color.ORANGE);
            drawShape(g2, new Arrow(20,200,100,60,0.5f,0.5f,45), Color.MAGENTA);
            drawShape(g2, new Arrow(140,200,100,60,0.5f,0.5f,180), Color.CYAN);
         }
      });
   }

   private static void drawBalloons(CanvasPanel canvas) {
      canvas.setPaintOperation(new PaintOperation() {
         public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; 

            drawShape(g2, new Balloon(20,20,50,50,0,20,10,Balloon.TAB_AT_BOTTOM,0.5f), Color.RED);
            drawShape(g2, new Balloon(90,20,50,50,0,20,10,Balloon.TAB_AT_LEFT,0.5f), Color.RED);
            drawShape(g2, new Balloon(160,20,50,50,0,20,10,Balloon.TAB_AT_TOP,0.5f), Color.RED);
            drawShape(g2, new Balloon(230,20,50,50,0,20,10,Balloon.TAB_AT_RIGHT,0.5f), Color.RED);

            drawShape(g2, new Balloon(20,90,50,50,10,20,10,Balloon.TAB_AT_BOTTOM,0.5f,Balloon.ANGLE_AT_END), Color.GREEN);
            drawShape(g2, new Balloon(90,90,50,50,10,20,10,Balloon.TAB_AT_LEFT,0.5f,Balloon.ANGLE_AT_END), Color.GREEN);
            drawShape(g2, new Balloon(160,90,50,50,10,20,10,Balloon.TAB_AT_TOP,0.5f,Balloon.ANGLE_AT_END), Color.GREEN);
            drawShape(g2, new Balloon(230,90,50,50,10,20,10,Balloon.TAB_AT_RIGHT,0.5f,Balloon.ANGLE_AT_END), Color.GREEN);

            drawShape(g2, new Balloon(20,160,50,50,10,20,10,Balloon.TAB_AT_BOTTOM,0.5f,Balloon.ANGLE_AT_START), Color.BLUE);
            drawShape(g2, new Balloon(90,160,50,50,10,20,10,Balloon.TAB_AT_LEFT,0.5f,Balloon.ANGLE_AT_START), Color.BLUE);
            drawShape(g2, new Balloon(160,160,50,50,10,20,10,Balloon.TAB_AT_TOP,0.5f,Balloon.ANGLE_AT_START), Color.BLUE);
            drawShape(g2, new Balloon(230,160,50,50,10,20,10,Balloon.TAB_AT_RIGHT,0.5f,Balloon.ANGLE_AT_START), Color.BLUE);

            drawShape(g2, new Balloon(20,230,50,50,10,20,10,Balloon.TAB_AT_BOTTOM,0), Color.ORANGE);
            drawShape(g2, new Balloon(90,230,50,50,10,20,10,Balloon.TAB_AT_LEFT,0), Color.ORANGE);
            drawShape(g2, new Balloon(160,230,50,50,10,20,10,Balloon.TAB_AT_TOP,0), Color.ORANGE);
            drawShape(g2, new Balloon(230,230,50,50,10,20,10,Balloon.TAB_AT_RIGHT,0), Color.ORANGE);

            drawShape(g2, new Balloon(20,300,50,50,10,20,10,Balloon.TAB_AT_BOTTOM,1), Color.MAGENTA);
            drawShape(g2, new Balloon(90,300,50,50,10,20,10,Balloon.TAB_AT_LEFT,1), Color.MAGENTA);
            drawShape(g2, new Balloon(160,300,50,50,10,20,10,Balloon.TAB_AT_TOP,1), Color.MAGENTA);
            drawShape(g2, new Balloon(230,300,50,50,10,20,10,Balloon.TAB_AT_RIGHT,1), Color.MAGENTA);
         }
      });
   }

   private static void drawCrosses(CanvasPanel canvas) {
      canvas.setPaintOperation(new PaintOperation() {
         public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; 

            drawShape(g2, new Cross(60,60,40,30), Color.RED);
            drawShape(g2, new Cross(160,60,40,10), Color.GREEN);
            drawShape(g2, new Cross(60,160,40,30,0,0.25f), Color.BLUE);
            drawShape(g2, new Cross(160,160,40,30,0,0.75f), Color.ORANGE);
            drawShape(g2, new Cross(60,260,40,30,45), Color.MAGENTA);
            drawShape(g2, new Cross(160,260,40,30,45,0.5f), Color.CYAN);
         }
      });
   }

   private static void drawDonuts(CanvasPanel canvas) {
      canvas.setPaintOperation(new PaintOperation() {
         public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; 

            drawShape(g2, new Donut(40,40,30,5,2), Color.RED);
            drawShape(g2, new Donut(120,40,30,5,3), Color.RED);
            drawShape(g2, new Donut(200,40,30,5,4), Color.RED);
            drawShape(g2, new Donut(280,40,30,5,5), Color.RED);

            drawShape(g2, new Donut(40,120,30,10,2), Color.GREEN);
            drawShape(g2, new Donut(120,120,30,10,3), Color.GREEN);
            drawShape(g2, new Donut(200,120,30,10,4), Color.GREEN);
            drawShape(g2, new Donut(280,120,30,10,5), Color.GREEN);

            drawShape(g2, new Donut(40,200,30,20,2), Color.BLUE);
            drawShape(g2, new Donut(120,200,30,20,3), Color.BLUE);
            drawShape(g2, new Donut(200,200,30,20,4), Color.BLUE);
            drawShape(g2, new Donut(280,200,30,20,5), Color.BLUE);

            drawShape(g2, new Donut(40,280,30,10,2,45), Color.ORANGE);
            drawShape(g2, new Donut(120,280,30,10,3,45), Color.ORANGE);
            drawShape(g2, new Donut(200,280,30,10,4,45), Color.ORANGE);
            drawShape(g2, new Donut(280,280,30,10,5,45), Color.ORANGE);
         }
      });
   }

   private static void drawMultiRoundRectangles(CanvasPanel canvas) {
      canvas.setPaintOperation(new PaintOperation() {
         public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; 

            drawShape(g2, new MultiRoundRectangle(20,20,120,60,30,30,0,0), Color.RED);
            drawShape(g2, new MultiRoundRectangle(160,20,120,60,0,0,30,30), Color.GREEN);
            drawShape(g2, new MultiRoundRectangle(20,100,120,60,30,0,30,0), Color.BLUE);
            drawShape(g2, new MultiRoundRectangle(160,100,120,60,0,30,0,30), Color.ORANGE);
         }
      });
   }

   private static void drawRays(CanvasPanel canvas) {
      canvas.setPaintOperation(new PaintOperation() {
         public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; 

            drawShape(g2, new Rays(40,40,30,2), Color.RED);
            drawShape(g2, new Rays(120,40,30,3), Color.RED);
            drawShape(g2, new Rays(200,40,30,4), Color.RED);
            drawShape(g2, new Rays(280,40,30,5), Color.RED);

            drawShape(g2, new Rays(40,120,30,2,0.25f), Color.GREEN);
            drawShape(g2, new Rays(120,120,30,3,0.25f), Color.GREEN);
            drawShape(g2, new Rays(200,120,30,4,0.25f), Color.GREEN);
            drawShape(g2, new Rays(280,120,30,5,0.25f), Color.GREEN);

            drawShape(g2, new Rays(40,200,30,2,0.75f), Color.BLUE);
            drawShape(g2, new Rays(120,200,30,3,0.75f), Color.BLUE);
            drawShape(g2, new Rays(200,200,30,4,0.75f), Color.BLUE);
            drawShape(g2, new Rays(280,200,30,5,0.75f), Color.BLUE);

            drawShape(g2, new Rays(40,280,30,2,0,0.5f,true), Color.ORANGE);
            drawShape(g2, new Rays(120,280,30,3,0,0.5f,true), Color.ORANGE);
            drawShape(g2, new Rays(200,280,30,4,0,0.5f,true), Color.ORANGE);
            drawShape(g2, new Rays(280,280,30,5,0,0.5f,true), Color.ORANGE);
         }
      });
   }

   private static void drawRegularPolygons(CanvasPanel canvas) {
      canvas.setPaintOperation(new PaintOperation() {
         public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; 

            drawShape(g2, new RegularPolygon(40,40,30,3), Color.RED);
            drawShape(g2, new RegularPolygon(120,40,30,4), Color.RED);
            drawShape(g2, new RegularPolygon(200,40,30,5), Color.RED);
            drawShape(g2, new RegularPolygon(280,40,30,6), Color.RED);

            drawShape(g2, new RegularPolygon(40,120,30,3,45), Color.GREEN);
            drawShape(g2, new RegularPolygon(120,120,30,4,45), Color.GREEN);
            drawShape(g2, new RegularPolygon(200,120,30,5,45), Color.GREEN);
            drawShape(g2, new RegularPolygon(280,120,30,6,45), Color.GREEN);

            drawShape(g2, new RegularPolygon(40,200,30,3,115), Color.BLUE);
            drawShape(g2, new RegularPolygon(120,200,30,4,115), Color.BLUE);
            drawShape(g2, new RegularPolygon(200,200,30,5,115), Color.BLUE);
            drawShape(g2, new RegularPolygon(280,200,30,6,115), Color.BLUE);
         }
      });
   }

   private static void drawRoundPins(CanvasPanel canvas) {
      canvas.setPaintOperation(new PaintOperation() {
         public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; 

            drawShape(g2, new RoundPin(50,50,20,40,0), Color.RED);
            drawShape(g2, new RoundPin(120,50,20,30,0), Color.RED);
            drawShape(g2, new RoundPin(190,50,20,20,0), Color.RED);
            drawShape(g2, new RoundPin(260,50,20,10,0), Color.RED);

            drawShape(g2, new RoundPin(50,120,20,40,45), Color.GREEN);
            drawShape(g2, new RoundPin(120,120,20,30,45), Color.GREEN);
            drawShape(g2, new RoundPin(190,120,20,20,45), Color.GREEN);
            drawShape(g2, new RoundPin(260,120,20,10,45), Color.GREEN);

            drawShape(g2, new RoundPin(50,190,20,40,90), Color.BLUE);
            drawShape(g2, new RoundPin(120,190,20,30,90), Color.BLUE);
            drawShape(g2, new RoundPin(190,190,20,20,90), Color.BLUE);
            drawShape(g2, new RoundPin(260,190,20,10,90), Color.BLUE);
         }
      });
   }

   private static void drawStars(CanvasPanel canvas) {
      canvas.setPaintOperation(new PaintOperation() {
         public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; 

            drawShape(g2, new Star(40,40,30,5,2), Color.RED);
            drawShape(g2, new Star(120,40,30,5,3), Color.RED);
            drawShape(g2, new Star(200,40,30,5,4), Color.RED);
            drawShape(g2, new Star(280,40,30,5,5), Color.RED);

            drawShape(g2, new Star(40,120,30,10,2), Color.GREEN);
            drawShape(g2, new Star(120,120,30,10,3), Color.GREEN);
            drawShape(g2, new Star(200,120,30,10,4), Color.GREEN);
            drawShape(g2, new Star(280,120,30,10,5), Color.GREEN);

            drawShape(g2, new Star(40,200,30,20,2), Color.BLUE);
            drawShape(g2, new Star(120,200,30,20,3), Color.BLUE);
            drawShape(g2, new Star(200,200,30,20,4), Color.BLUE);
            drawShape(g2, new Star(280,200,30,20,5), Color.BLUE);

            drawShape(g2, new Star(40,280,30,10,2,45), Color.ORANGE);
            drawShape(g2, new Star(120,280,30,10,3,45), Color.ORANGE);
            drawShape(g2, new Star(200,280,30,10,4,45), Color.ORANGE);
            drawShape(g2, new Star(280,280,30,10,5,45), Color.ORANGE);
         }
      });
   }

   private static void drawTriangles(CanvasPanel canvas) {
      canvas.setPaintOperation(new PaintOperation() {
         public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; 

            drawShape(g2, new Triangle(20,90,80, 0), Color.RED);
            drawShape(g2, new Triangle(140,90,80,45), Color.RED);
            drawShape(g2, new Triangle(300,90,80,90), Color.RED);

            drawShape(g2, new Triangle(20,190,80,0,30f), Color.BLUE);
            drawShape(g2, new Triangle(140,190,80,45,30f), Color.BLUE);
            drawShape(g2, new Triangle(300,190,80,90,30f), Color.BLUE);

            drawShape(g2, new Triangle(20,300,80,0,Triangle.ANGLE_AT_END,30), Color.ORANGE);
            drawShape(g2, new Triangle(140,300,80,45,Triangle.ANGLE_AT_END,30), Color.ORANGE);
            drawShape(g2, new Triangle(300,300,80,90,Triangle.ANGLE_AT_END,30), Color.ORANGE);
         }
      });
   }

   private static void drawShape(Graphics2D g, Shape shape, Color color) {
      g.setColor(color);
      g.fill(shape);
      g.setColor(Color.BLACK);
      g.draw(shape);
   }

   private static class CanvasPanel extends JPanel {
      private PaintOperation paintOperation;

      public void setPaintOperation(PaintOperation paintOperation) {
         this.paintOperation = paintOperation;
         if(isVisible()) {
            repaint();
         }
      }

      protected void paintComponent(Graphics g) {
         if(paintOperation != null) {
            Dimension size = getSize();
            g.clearRect(0, 0, size.width, size.height);
            Graphics2D g2 = (Graphics2D) g; 
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);
            paintOperation.paint(g);
         }else{
            super.paintComponent(g);
         }
      }
   }

   private interface PaintOperation {
      void paint(Graphics g);
   }
}
