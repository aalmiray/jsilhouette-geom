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
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Defines an asterisk shape that may have round corners.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class Asterisk implements Shape, Cloneable, Centered {
   public static void validateBeams( float beams ) {
      if( beams < 2 ) {
         throw new IllegalArgumentException( "beams can not be less than 2" );
      }
   }

   public static void validateRoundness( float roundness ) {
      if( roundness < 0 || roundness > 1 ) {
         throw new IllegalArgumentException( "roundness must be inside the range [0..1]" );
      }
   }

   public static void validateWidth( float width, float radius ) {
      if( width > radius * 2 ) {
         throw new IllegalArgumentException( "width can not be greater than radius*2 (" + (radius * 2) + ")" );
      }
   }

   private float angle;
   private Shape asterisk;
   private int beams;
   private float cx;
   private float cy;
   private float radius;
   private float roundness;
   private float width;

   public Asterisk() {
      this( 5, 5, 5, 3, 5, 0, 0 );
   }

   public Asterisk( float cx, float cy, float radius, float width ) {
      this( cx, cy, radius, width,5,  0, 0 );
   }

   public Asterisk( float cx, float cy, float radius, float width, float angle ) {
      this( cx, cy, radius, width, 5, angle, 0 );
   }

   public Asterisk( float cx, float cy, float radius, float width, float angle, float roundness ) {
      this( cx, cy, radius, width, 5, angle, 0 );
   }

   public Asterisk( float cx, float cy, float radius, float width, int beams ) {
      this( cx, cy, radius, width, beams, 0, 0 );
   }

   public Asterisk( float cx, float cy, float radius, float width, int beams, float angle, float roundness ) {
      validateWidth( width, radius );
      validateRoundness( roundness );
      validateBeams( beams );
      this.cx = cx;
      this.cy = cy;
      this.radius = radius;
      this.width = width;
      this.angle = ShapeUtils.normalizeAngle( angle );
      this.roundness = roundness;
      this.beams = beams;
      calculateShape();
   }

   public Object clone() {
      return new Asterisk( cx, cy, radius, width, beams, angle, roundness );
   }

   public boolean contains( double x, double y ) {
      return asterisk.contains( x, y );
   }

   public boolean contains( double x, double y, double w, double h ) {
      return asterisk.contains( x, y, w, h );
   }

   public boolean contains( Point2D p ) {
      return asterisk.contains( p );
   }

   public boolean contains( Rectangle2D r ) {
      return asterisk.contains( r );
   }

   public float getAngle() {
      return angle;
   }

   public int getBeams() {
      return beams;
   }

   public Rectangle getBounds() {
      return asterisk.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return asterisk.getBounds2D();
   }

   public float getCx() {
      return cx;
   }

   public float getCy() {
      return cy;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return asterisk.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return asterisk.getPathIterator( at, flatness );
   }

   public float getRadius() {
      return radius;
   }

   public float getRoundness() {
      return roundness;
   }

   public float getWidth() {
      return width;
   }

   public boolean intersects( double x, double y, double w, double h ) {
      return asterisk.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return asterisk.intersects( r );
   }

   public void setAngle( float angle ) {
      float a = ShapeUtils.normalizeAngle( angle );
      if( this.angle != a ) {
         this.angle = a;
         calculateShape();
      }
   }

   public void setBeams( int beams ) {
      if( this.beams != beams ) {
         validateBeams( beams );
         this.beams = beams;
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

   public void setRoundness( float roundness ) {
      if( this.roundness != roundness ) {
         validateRoundness( roundness );
         this.roundness = roundness;
         calculateShape();
      }
   }

   public void setWidth( float width ) {
      if( this.width != width ) {
         validateWidth( width, radius );
         this.width = width;
         calculateShape();
      }
   }

   private void calculateShape() {
      float t = 360 / beams;
      float a = angle;
      a = a > 360 ? a - 360 : a;
      asterisk = new Area();
      for( int i = 0; i < beams; i++ ) {
         Shape beam = new MultiRoundRectangle( cx, cy - width, radius, width * 2, 0, width * roundness, 0, width
               * roundness );
         beam = ShapeUtils.rotate( beam, a, cx, cy );
         ((Area) asterisk).add( new Area( beam ) );

         a += t;
         a = a > 360 ? a - 360 : a;
      }
   }
}
