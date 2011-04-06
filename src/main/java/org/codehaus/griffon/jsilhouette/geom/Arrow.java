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
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Defines an arrow shape.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class Arrow implements Shape, Cloneable, Centered {
   public static void validateDepth( float depth ) {
      if( depth < 0 || depth > 1 ) {
         throw new IllegalArgumentException( "depth must be inside the range [0..1]" );
      }
   }

   public static void validateRise( float rise ) {
      if( rise < 0 || rise > 1 ) {
         throw new IllegalArgumentException( "rise must be inside the range [0..1]" );
      }
   }

   private float angle;
   private Shape arrow;
   private float depth;
   private float height;
   private float rise;
   private float width;
   private float x;
   private float y;
   private float cx;
   private float cy;

   public Arrow() {
      this( 0, 0, 10, 6, 0.5f, 0.5f, 0 );
   }

   public Arrow( float x, float y, float width, float height ) {
      this( x, y, width, height, 0.5f, 0.5f, 0 );
   }

   public Arrow( float x, float y, float width, float height, float angle ) {
      this( x, y, width, height, 0.5f, 0.5f, angle );
   }

   public Arrow( float x, float y, float width, float height, float rise, float depth ) {
      this( x, y, width, height, rise, depth, 0 );
   }

   public Arrow( float x, float y, float width, float height, float rise, float depth, float angle ) {
      validateDepth( depth );
      validateRise( rise );
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.rise = rise;
      this.depth = depth;
      this.angle = ShapeUtils.normalizeAngle( angle );
      calculateShape();
   }

   public Object clone() {
      return new Arrow( x, y, width, height, rise, depth, angle );
   }

   public boolean contains( double x, double y ) {
      return arrow.contains( x, y );
   }

   public boolean contains( double x, double y, double w, double h ) {
      return arrow.contains( x, y, w, h );
   }

   public boolean contains( Point2D p ) {
      return arrow.contains( p );
   }

   public boolean contains( Rectangle2D r ) {
      return arrow.contains( r );
   }

   public float getAngle() {
      return angle;
   }

   public Rectangle getBounds() {
      return arrow.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return arrow.getBounds2D();
   }

   public float getDepth() {
      return depth;
   }

   public float getHeight() {
      return height;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return arrow.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return arrow.getPathIterator( at, flatness );
   }

   public float getRise() {
      return rise;
   }

   public float getWidth() {
      return width;
   }

   public float getX() {
      return x;
   }

   public float getY() {
      return y;
   }

   public boolean intersects( double x, double y, double w, double h ) {
      return arrow.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return arrow.intersects( r );
   }

   public void setAngle( float angle ) {
      float a = ShapeUtils.normalizeAngle( angle );
      if( this.angle != a ) {
         this.angle = a;
         calculateShape();
      }
   }

   public void setDepth( float depth ) {
      if( this.depth != depth ) {
         validateDepth( depth );
         this.depth = depth;
         calculateShape();
      }
   }

   public void setHeight( float height ) {
      if( this.height != height ) {
         this.height = height;
         calculateShape();
      }
   }

   public void setRise( float rise ) {
      if( this.rise != rise ) {
         validateRise( rise );
         this.rise = rise;
         calculateShape();
      }
   }

   public void setWidth( float width ) {
      if( this.width != width ) {
         this.width = width;
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

   public float getCx() {
      return cx;
   }

   public float getCy() {
      return cy;
   }

   private void calculateShape() {
      float d = width * depth;
      float r = height * rise / 2;
      GeneralPath head = new GeneralPath();
      head.moveTo( x + d, y );
      head.lineTo( x + d, y + height );
      head.lineTo( x + width, y + (height / 2) );
      head.closePath();
      arrow = new Area( new Rectangle2D.Float( x, y + (height / 2) - r, d, r * 2 ) );
      ((Area) arrow).add( new Area( head ) );

      cx = x + (width / 2);
      cy = y + (height / 2);

      arrow = ShapeUtils.rotate( arrow, angle, cx, cy );
   }
}
