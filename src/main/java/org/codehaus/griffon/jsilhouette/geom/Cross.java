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
import java.awt.geom.RoundRectangle2D;

/**
 * Defines a cross shape that may have round corners.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class Cross implements Shape, Cloneable, Centered {
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
   private Shape cross;
   private float cx;
   private float cy;
   private float radius;
   private float roundness;
   private float width;

   public Cross() {
      this( 5, 5, 5, 3, 0, 0 );
   }

   public Cross( float cx, float cy, float radius, float width ) {
      this( cx, cy, radius, width, 0, 0 );
   }

   public Cross( float cx, float cy, float radius, float width, float angle ) {
      this( cx, cy, radius, width, angle, 0 );
   }

   public Cross( float cx, float cy, float radius, float width, float angle, float roundness ) {
      validateWidth( width, radius );
      validateRoundness( roundness );
      this.cx = cx;
      this.cy = cy;
      this.radius = radius;
      this.width = width;
      this.angle = ShapeUtils.normalizeAngle( angle );
      this.roundness = roundness;
      calculateShape();
   }

   public Object clone() {
      return new Cross( cx, cy, radius, width, angle, roundness );
   }

   public boolean contains( double x, double y ) {
      return cross.contains( x, y );
   }

   public boolean contains( double x, double y, double w, double h ) {
      return cross.contains( x, y, w, h );
   }

   public boolean contains( Point2D p ) {
      return cross.contains( p );
   }

   public boolean contains( Rectangle2D r ) {
      return cross.contains( r );
   }

   public float getAngle() {
      return angle;
   }

   public Rectangle getBounds() {
      return cross.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return cross.getBounds2D();
   }

   public float getCx() {
      return cx;
   }

   public float getCy() {
      return cy;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return cross.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return cross.getPathIterator( at, flatness );
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
      return cross.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return cross.intersects( r );
   }

   public void setAngle( float angle ) {
      float a = ShapeUtils.normalizeAngle( angle );
      if( this.angle != a ) {
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
      float arcwh = width * roundness;
      Shape beam1 = new RoundRectangle2D.Float( cx - radius, cy - (width / 2), radius * 2, width, arcwh, arcwh );
      Shape beam2 = new RoundRectangle2D.Float( cx - (width / 2), cy - radius, width, radius * 2, arcwh, arcwh );
      cross = new Area( beam1 );
      ((Area) cross).add( new Area( beam2 ) );
      cross = ShapeUtils.rotate( cross, angle, cx, cy );
   }
}
