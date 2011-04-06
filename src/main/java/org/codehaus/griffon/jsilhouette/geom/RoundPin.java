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
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Defines a rounded pushpin shape.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class RoundPin implements Shape, Cloneable, Centered {
   private float angle;
   private float cx;
   private float cy;
   private float height;
   private Shape pin;
   private float radius;

   public RoundPin() {
      this( 5, 5, 5, 10, 0 );
   }

   public RoundPin( float cx, float cy, float radius ) {
      this( cx, cy, radius, radius * 2, 0 );
   }

   public RoundPin( float cx, float cy, float radius, float height, float angle ) {
      this.cx = cx;
      this.cy = cy;
      this.radius = radius;
      this.height = height;
      this.angle = ShapeUtils.normalizeAngle( angle );
      calculateShape();
   }

   public Object clone() {
      return new RoundPin( cx, cy, radius, height, angle );
   }

   public boolean contains( double x, double y ) {
      return pin.contains( x, y );
   }

   public boolean contains( double x, double y, double w, double h ) {
      return pin.contains( x, y, w, h );
   }

   public boolean contains( Point2D p ) {
      return pin.contains( p );
   }

   public boolean contains( Rectangle2D r ) {
      return pin.contains( r );
   }

   public float getAngle() {
      return angle;
   }

   public Rectangle getBounds() {
      return pin.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return pin.getBounds2D();
   }

   public float getCx() {
      return cx;
   }

   public float getCy() {
      return cy;
   }

   public float getHeight() {
      return height;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return pin.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return pin.getPathIterator( at, flatness );
   }

   public float getRadius() {
      return radius;
   }

   public boolean intersects( double x, double y, double w, double h ) {
      return pin.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return pin.intersects( r );
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

   public void setHeight( float height ) {
      if( this.height != height ) {
         this.height = height;
         calculateShape();
      }
   }

   public void setRadius( float radius ) {
      if( this.radius != radius ) {
         this.radius = radius;
         calculateShape();
      }
   }

   private void calculateShape() {
      Arc2D.Float head = new Arc2D.Float( cx - (radius * 1), cy - (radius * 1), radius * 2, radius * 2, 0, 181,
            Arc2D.PIE );
      GeneralPath body = new GeneralPath();
      body.moveTo( cx - radius, cy );
      body.lineTo( cx, cy + height );
      body.lineTo( cx + radius, cy );
      body.closePath();
      pin = new Area( head );
      ((Area) pin).add( new Area( body ) );
      pin = ShapeUtils.rotate( pin, angle, cx, cy );
   }
}
