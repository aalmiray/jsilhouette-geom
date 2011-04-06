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
 * Defines a fan shape whose blades can vary in shape.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class Fan implements Shape, Cloneable, Centered {
   public static void validateBladeCx( float bladeCx ) {
      if( bladeCx < 0 || bladeCx > 1 ) {
         throw new IllegalArgumentException( "bladeCx must be inside the range [0..1]" );
      }
   }

   public static void validateBlades( float blades ) {
      if( blades < 2 ) {
         throw new IllegalArgumentException( "blades can not be less than 2" );
      }
   }

   private float angle;
   private Shape fan;
   private Shape blade;
   private int blades;
   private float cx;
   private float cy;
   private float bladeCx;

   public Fan() {
      this( 10, 10, new Rectangle2D.Float(0,0,4,8), 2, 0f, 0.5f );
   }

   public Fan( Shape blade ) {
      this( 10, 10, blade, 2, 0f, 0.5f );
   }

   public Fan( float cx, float cy, Shape blade ) {
      this( cx, cy, blade, 2, 0f, 0.5f );
   }

   public Fan( float cx, float cy, Shape blade, int blades ) {
      this( cx, cy, blade, blades, 0f, 0.5f );
   }

   public Fan( float cx, float cy, Shape blade, int blades, float angle ) {
      this( cx, cy, blade, blades, angle, 0.5f );
   }

   public Fan( float cx, float cy, Shape blade, int blades, float angle, float bladeCx ) {
      validateBladeCx( bladeCx );
      validateBlades( blades );
      this.cx = cx;
      this.cy = cy;
      this.blade = blade;
      this.angle = ShapeUtils.normalizeAngle( angle );
      this.bladeCx = bladeCx;
      this.blades = blades;
      calculateShape();
   }

   public Object clone() {
      return new Fan( cx, cy, blade, blades, angle, bladeCx );
   }

   public boolean contains( double x, double y ) {
      return fan.contains( x, y );
   }

   public boolean contains( double x, double y, double w, double h ) {
      return fan.contains( x, y, w, h );
   }

   public boolean contains( Point2D p ) {
      return fan.contains( p );
   }

   public boolean contains( Rectangle2D r ) {
      return fan.contains( r );
   }

   public float getAngle() {
      return angle;
   }

   public Shape getBlade() {
      return blade;
   }

   public int getBlades() {
      return blades;
   }

   public Rectangle getBounds() {
      return fan.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return fan.getBounds2D();
   }

   public float getCx() {
      return cx;
   }

   public float getCy() {
      return cy;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return fan.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return fan.getPathIterator( at, flatness );
   }

   public float getBladeCx() {
      return bladeCx;
   }

   public boolean intersects( double x, double y, double w, double h ) {
      return fan.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return fan.intersects( r );
   }

   public void setAngle( float angle ) {
      float a = ShapeUtils.normalizeAngle( angle );
      if( this.angle != a ) {
         this.angle = a;
         calculateShape();
      }
   }

   public void setBlade( Shape blade ) {
      if( this.blade != blade ) {
         this.blade = blade;
         calculateShape();
      }
   }

   public void setBlades( int blades ) {
      if( this.blades != blades ) {
         validateBlades( blades );
         this.blades = blades;
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

   public void setBladeCx( float bladeCx ) {
      if( this.bladeCx != bladeCx ) {
         validateBladeCx( bladeCx );
         this.bladeCx = bladeCx;
         calculateShape();
      }
   }

   /**
    * Signals the fan to recalculate itself.<br/>
    * this is a convenience method to let a fan update
    * itself when the properties of its prototype blade
    * have been changed, it must be done manually as
    * shapes are not guaranteed to be observable.
    */
   public void updateBlades() {
      calculateShape();
   }

   private void calculateShape() {
      float t = 360 / blades;
      float a = angle;
      a = a > 360 ? a - 360 : a;
      fan = new Area();
      for( int i = 0; i < blades; i++ ) {
         Shape b = new Area(blade);
         Rectangle2D bounds = b.getBounds2D();
         b = AffineTransform.getTranslateInstance( cx - bounds.getX() - bounds.getWidth()/2, cy - bounds.getY() ).createTransformedShape(b);
         bounds = b.getBounds2D();
         float bcx = (float) ((bounds.getWidth() * bladeCx) + bounds.getX());
         b = ShapeUtils.rotate( b, a, bcx, cy );
         ((Area) fan).add( new Area(b) );

         a += t;
         a = a > 360 ? a - 360 : a;
      }
   }
}
