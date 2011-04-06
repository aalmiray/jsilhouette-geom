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
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Defines a <a href="http://en.wikipedia.org/wiki/Lauburu">Lauburu</a> shape.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class Lauburu implements Shape, Cloneable, Centered {
   private float angle;
   private float cx;
   private float cy;
   private Shape lauburu;
   private float radius;

   public Lauburu() {
      this( 6, 6, 6, 0 );
   }

   public Lauburu( float cx, float cy, float radius ) {
      this( cx, cy, radius, 0 );
   }

   public Lauburu( float cx, float cy, float radius, float angle ) {
      this.cx = cx;
      this.cy = cy;
      this.radius = radius;
      this.angle = ShapeUtils.normalizeAngle( angle );
      calculateShape();
   }

   public Object clone() {
      return new Lauburu( cx, cy, radius, angle );
   }

   public boolean contains( double x, double y ) {
      return lauburu.contains( x, y );
   }

   public boolean contains( double x, double y, double w, double h ) {
      return lauburu.contains( x, y, w, h );
   }

   public boolean contains( Point2D p ) {
      return lauburu.contains( p );
   }

   public boolean contains( Rectangle2D r ) {
      return lauburu.contains( r );
   }

   public float getAngle() {
      return angle;
   }

   public Rectangle getBounds() {
      return lauburu.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return lauburu.getBounds2D();
   }

   public float getCx() {
      return cx;
   }

   public float getCy() {
      return cy;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return lauburu.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return lauburu.getPathIterator( at, flatness );
   }

   public float getRadius() {
      return radius;
   }

   public boolean intersects( double x, double y, double w, double h ) {
      return lauburu.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return lauburu.intersects( r );
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

   private void calculateShape() {
      // terrible hack to avoid OOM when cx = 0 & cy = 0
      if( cx == 0f ) cx = 0.0001f;
      if( cy == 0f ) cy = 0.0001f;
      Area l = createArm();
      l.add(createArm().createTransformedArea(AffineTransform.getRotateInstance(Math.toRadians(  90 ), cx, cy)));
      l.add(createArm().createTransformedArea(AffineTransform.getRotateInstance(Math.toRadians( 180 ), cx, cy)));
      l.add(createArm().createTransformedArea(AffineTransform.getRotateInstance(Math.toRadians( 270 ), cx, cy)));
      lauburu = ShapeUtils.rotate( l, angle, cx, cy );
   }

   private Area createArm() {
      Area arm = new Area(new Arc2D.Float(cx, cy - radius/2, radius, radius, 0f, 180f, Arc2D.CHORD));
      arm.add( new Area(new Ellipse2D.Float(cx + radius/2, cy - radius/4, radius/2, radius/2)) );
      arm.subtract( new Area(new Ellipse2D.Float(cx, cy - radius/4, radius/2, radius/2)) );
//       arm.add( new Area(new Arc2D.Float(cx + radius/2, cy - radius/4, radius/2, radius/2, 180f, 360f, Arc2D.CHORD)) );
//       arm.subtract( new Area(new Arc2D.Float(cx, cy - radius/4, radius/2, radius/2, 0f, 180f, Arc2D.CHORD)) );
      return arm;
   }
}
