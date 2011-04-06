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

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Defines a <a href="http://en.wikipedia.org/wiki/Reuleaux_triangle">Reuleaux triangular</a> shape.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class ReuleauxTriangle implements Shape, Cloneable, Centered {
   private float angle;
   private float cx;
   private float cy;
   private boolean rotateAtCenter;
   private Shape triangle;
   private float width;
   private float x;
   private float y;

   public ReuleauxTriangle() {
      this( 10, 10, 10, 0, false );
   }

   public ReuleauxTriangle( float x, float y, float width, float angle ) {
      this( x, y, width, angle, false );
   }

   public ReuleauxTriangle( float x, float y, float width, float angle, boolean rotateAtCenter ) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.angle = ShapeUtils.normalizeAngle( angle );
      this.rotateAtCenter = rotateAtCenter;
      calculateShape();
   }

   public Object clone() {
      return new Triangle( x, y, width, angle, rotateAtCenter );
   }

   public boolean contains( double x, double y ) {
      return triangle.contains( x, y );
   }

   public boolean contains( double x, double y, double w, double h ) {
      return triangle.contains( x, y, w, h );
   }

   public boolean contains( Point2D p ) {
      return triangle.contains( p );
   }

   public boolean contains( Rectangle2D r ) {
      return triangle.contains( r );
   }

   public float getAngle() {
      return angle;
   }

   public Rectangle getBounds() {
      return triangle.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return triangle.getBounds2D();
   }

   public float getCx() {
      return cx;
   }

   public float getCy() {
      return cy;
   }

   public float getX() {
      return x;
   }

   public float getY() {
      return y;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return triangle.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return triangle.getPathIterator( at, flatness );
   }

   public float getWidth() {
      return width;
   }

   public boolean intersects( double x, double y, double w, double h ) {
      return triangle.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return triangle.intersects( r );
   }

   public boolean isRotateAtCenter() {
      return rotateAtCenter;
   }

   public void setAngle( float angle ) {
      float a = ShapeUtils.normalizeAngle( angle );
      if( this.angle != a ) {
         this.angle = a;
         calculateShape();
      }
   }

   public void setX( float x ) {
      if( this.x != x ) {
         this.x = x;
         calculateShape();
      }
   }

   public void setY( float y ) {
      if( this.y != y ) {
         this.y = y;
         calculateShape();
      }
   }

   public void setWidth( float width ) {
      if( this.width != width ) {
         this.width = width;
         calculateShape();
      }
   }

   public void setRotateAtCenter( boolean rotateAtCenter ) {
      if( this.rotateAtCenter != rotateAtCenter ) {
         this.rotateAtCenter = rotateAtCenter;
         calculateShape();
      }
   }

   private void calculateShape() {
      float height = (float) Math.abs( Math.sqrt( 3 ) / 2 * width );
      float[] p1 = {x, y};
      float[] p2 = {x + width, y};
      float[] p3 = {x + (width / 2), y - height};

      float perimeter = width * 3;
      cx = ((width * p3[0]) + (width * p1[0]) + (width * p2[0])) / perimeter;
      cy = ((width * p3[1]) + (width * p1[1]) + (width * p2[1])) / perimeter;

      Ellipse2D c1 = new Ellipse2D.Float(
         p1[0] - width,
         p1[1] - width,
         width * 2,
         width * 2
      );
      Ellipse2D c2 = new Ellipse2D.Float(
         p2[0] - width,
         p2[1] - width,
         width * 2,
         width * 2
      );
      Ellipse2D c3 = new Ellipse2D.Float(
         p3[0] - width,
         p3[1] - width,
         width * 2,
         width * 2
      );

      triangle = new Area(c1);
      ((Area)triangle).intersect( new Area(c2) );
      ((Area)triangle).intersect( new Area(c3) );

      if( rotateAtCenter ) {
         triangle = ShapeUtils.rotate( triangle, angle, cx, cy );
      } else {
         triangle = ShapeUtils.rotate( triangle, angle, x, y );
      }
   }
}
