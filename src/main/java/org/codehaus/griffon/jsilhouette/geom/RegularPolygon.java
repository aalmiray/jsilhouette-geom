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
 * Defines a regular polygon shape.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class RegularPolygon implements Shape, Cloneable, Centered {
   public static void validateSides( int sides ) {
      if( sides < 3 ) {
         throw new IllegalArgumentException( "sides can not be less than 3" );
      }
   }

   private float angle;
   private float cx;
   private float cy;
   private GeneralPath path;
   private Point2D[] points;
   private float radius;
   private int sides;

   public RegularPolygon() {
      this( 5, 5, 5, 3, 0 );
   }

   public RegularPolygon( float cx, float cy, float radius, int sides ) {
      this( cx, cy, radius, sides, 0 );
   }

   public RegularPolygon( float cx, float cy, float radius, int sides, float angle ) {
      validateSides( sides );
      this.cx = cx;
      this.cy = cy;
      this.radius = radius;
      this.sides = sides;
      this.angle = ShapeUtils.normalizeAngle( angle );
      calculateShape();
   }

   public Object clone() {
      return new RegularPolygon( cx, cy, radius, sides, angle );
   }

   public boolean contains( double x, double y ) {
      return path.contains( x, y );
   }

   public boolean contains( double x, double y, double w, double h ) {
      return path.contains( x, y, w, h );
   }

   public boolean contains( Point2D p ) {
      return path.contains( p );
   }

   public boolean contains( Rectangle2D r ) {
      return path.contains( r );
   }

   public float getAngle() {
      return angle;
   }

   public Rectangle getBounds() {
      return path.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return path.getBounds2D();
   }

   public float getCx() {
      return cx;
   }

   public float getCy() {
      return cy;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return path.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return path.getPathIterator( at, flatness );
   }

   public Point2D[] getPoints() {
      return points;
   }

   public float getRadius() {
      return radius;
   }

   public int getSides() {
      return sides;
   }

   public boolean intersects( double x, double y, double w, double h ) {
      return path.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return path.intersects( r );
   }

   public void setAngle( float angle ) {
      float a = ShapeUtils.normalizeAngle( angle );
      if( this.angle != a) {
         this.angle = a;
         calculateShape();
      }
   }

   public void setCx( float cx ) {
      if( this.cx != cx ) {
         this.cx = cx;
         calculateShape();
      }
   }

   public void setCy( float cy ) {
      if( this.cy != cy ) {
         this.cy = cy;
         calculateShape();
      }
   }

   public void setRadius( float radius ) {
      if( this.radius != radius ) {
         this.radius = radius;
         calculateShape();
      }
   }

   public void setSides( int sides ) {
      if( this.sides != sides ) {
         validateSides( sides );
         this.sides = sides;
         calculateShape();
      }
   }

   private void calculateShape() {
      float t = 360 / sides;
      float a = angle;
      a = a > 360 ? a - 360 : a;
      points = new Point2D[sides];
      path = new GeneralPath();
      for( int i = 0; i < sides; i++ ) {
         float ra = (float) Math.toRadians( a );
         float x = (float) Math.abs( radius * Math.cos( ra ) );
         float y = (float) Math.abs( radius * Math.sin( ra ) );
         if( a <= 90 ) {
            x = cx + x;
            y = cy - y;
         } else if( a <= 180 ) {
            x = cx - x;
            y = cy - y;
         } else if( a <= 270 ) {
            x = cx - x;
            y = cy + y;
         } else if( a <= 360 ) {
            x = cx + x;
            y = cy + y;
         }
         if( i == 0 ) {
            path.moveTo( x, y );
         } else {
            path.lineTo( x, y );
         }
         points[i] = new Point2D.Float( x, y );
         a += t;
         a = a > 360 ? a - 360 : a;
      }
      path.closePath();
   }
}
