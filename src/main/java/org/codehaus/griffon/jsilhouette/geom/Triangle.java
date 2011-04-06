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
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Defines a triangular shape [equilateral | isosceles | right].<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class Triangle implements Shape, Cloneable, Centered {
   public static final int NONE = -1;
   public static final int ANGLE_AT_END = 1;
   public static final int ANGLE_AT_START = 0;

   public static final int EQUILATERAL = 0;
   public static final int ISOSCELES = 1;
   public static final int RIGHT = 2;

   private float angle;
   private int anglePosition = NONE;
   private float cx;
   private float cy;
   private float height = Float.NaN;
   private boolean rotateAtCenter;
   private Shape triangle;
   private int type = EQUILATERAL;
   private float width;
   private float x;
   private float y;

   public Triangle() {
      this( 10, 10, 10, 0, false );
   }

   public Triangle( float x, float y, float width, float angle ) {
      this( x, y, width, angle, false );
   }

   public Triangle( float x, float y, float width, float angle, boolean rotateAtCenter ) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.angle = ShapeUtils.normalizeAngle( angle );
      this.rotateAtCenter = rotateAtCenter;
      calculateShape();
   }

   public Triangle( float x, float y, float width, float angle, float height ) {
      this( x, y, width, angle, height, false );
   }

   public Triangle( float x, float y, float width, float angle, float height, boolean rotateAtCenter ) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.angle = ShapeUtils.normalizeAngle( angle );
      this.height = height;
      this.rotateAtCenter = rotateAtCenter;
      calculateShape();
   }

   public Triangle( float x, float y, float width, float angle, int anglePosition ) {
      this( x, y, width, angle, anglePosition, false );
   }

   public Triangle( float x, float y, float width, float angle, int anglePosition, boolean rotateAtCenter ) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.angle = ShapeUtils.normalizeAngle( angle );
      this.anglePosition = anglePosition;
      this.rotateAtCenter = rotateAtCenter;
      calculateShape();
   }

   public Triangle( float x, float y, float width, float angle, int anglePosition, float height ) {
      this( x, y, width, angle, anglePosition, height, false );
   }

   public Triangle( float x, float y, float width, float angle, int anglePosition, float height, boolean rotateAtCenter ) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.angle = ShapeUtils.normalizeAngle( angle );
      this.anglePosition = anglePosition;
      this.height = height;
      this.rotateAtCenter = rotateAtCenter;
      calculateShape();
   }

   public Object clone() {
      switch( type ) {
         case ISOSCELES:
            return new Triangle( x, y, width, angle, height, rotateAtCenter );
         case RIGHT:
            return new Triangle( x, y, width, angle, anglePosition, height, rotateAtCenter );
         default:
            return new Triangle( x, y, width, angle, rotateAtCenter );
      }
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

   public int getAnglePosition() {
      return anglePosition;
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

   public float getHeight() {
      return height;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return triangle.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return triangle.getPathIterator( at, flatness );
   }

   public int getType() {
      return type;
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

   public void setHeight( float height ) {
      if( this.height != height ) {
         this.height = height;
         calculateShape();
      }
   }

   public void setAnglePosition( int anglePosition ) {
      if( this.anglePosition != anglePosition ) {
         this.anglePosition = anglePosition;
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
      if( anglePosition != NONE ) {
         this.type = RIGHT;
         calculateRightTriangle();
      } else if( !Double.isNaN( height ) && !Double.isInfinite( height ) ) {
         this.type = ISOSCELES;
         calculateIsoscelesTriangle();
      } else {
         this.type = EQUILATERAL;
         calculateEquilateralTriangle();
      }
   }

   private void calculateEquilateralTriangle() {
      this.height = (float) Math.abs( Math.sqrt( 3 ) / 2 * width );
      GeneralPath t = new GeneralPath();
      t.moveTo( x, y );
      t.lineTo( x + width, y );
      t.lineTo( x + (width / 2), y - height );
      t.closePath();
      triangle = t;

      rotate( new float[] { width, width, width }, new float[][] { { x, y }, { x + width, y },
            { x + (width / 2), y - height } } );
   }

   private void calculateIsoscelesTriangle() {
      this.height = (float) (!Double.isNaN( height ) && !Double.isInfinite( height ) ? Math.abs( height ) : Math
            .abs( Math.sqrt( 3 ) / 2 * width ));
      GeneralPath t = new GeneralPath();
      t.moveTo( x, y );
      t.lineTo( x + width, y );
      t.lineTo( x + (width / 2), y - height );
      t.closePath();
      triangle = t;

      float d = (float) Math.sqrt( Math.pow( width / 2, 2 ) + Math.pow( height, 2 ) );
      rotate( new float[] { width, d, d },
            new float[][] { { x, y }, { x + width, y }, { x + (width / 2), y - height } } );
   }

   private void calculateRightTriangle() {
      this.anglePosition = anglePosition > ANGLE_AT_END ? ANGLE_AT_START : anglePosition;
      this.height = (float) (!Double.isNaN( height ) && !Double.isInfinite( height ) ? Math.abs( height ) : Math
            .abs( Math.sqrt( 3 ) / 2 * width ));
      GeneralPath t = new GeneralPath();
      t.moveTo( x, y );
      if( anglePosition == ANGLE_AT_START ) {
         t.lineTo( x, y - height );
         t.lineTo( x + width, y );
      } else {
         t.lineTo( x + width, y );
         t.lineTo( x + width, y - height );
      }
      t.closePath();
      triangle = t;

      float d = (float) Math.sqrt( Math.pow( width, 2 ) + Math.pow( height, 2 ) );
      if( anglePosition == ANGLE_AT_END ) {
         rotate( new float[] { width, height, d }, new float[][] { { x, y }, { x + width, y },
               { x + width, y - height } } );
      } else {
         rotate( new float[] { width, height, d }, new float[][] { { x + width, y }, { x, y }, { x, y - height } } );
      }
   }

   private void rotate( float[] sides, float[][] points ) {
      float perimeter = sides[0] + sides[1] + sides[2];
      cx = ((sides[0] * points[2][0]) + (sides[1] * points[0][0]) + (sides[2] * points[1][0])) / perimeter;
      cy = ((sides[0] * points[2][1]) + (sides[1] * points[0][1]) + (sides[2] * points[1][1])) / perimeter;
      if( rotateAtCenter ) {
         triangle = ShapeUtils.rotate( triangle, angle, cx, cy );
      } else {
         triangle = ShapeUtils.rotate( triangle, angle, x, y );
      }
   }
}
