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
 * Defines a star shape.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class Star implements Shape, Cloneable, Centered {
   public static void validateCount( int count ) {
      if( count < 2 ) {
         throw new IllegalArgumentException( "'count' can not be less than 2 [count=" + count + "]" );
      }
   }

   public static void validateRadii( float ir, float or ) {
      if( ir >= or ) {
         throw new IllegalArgumentException( "'ir' can not be equal greater than 'or' [ir=" + ir + ", or=" + or + "]" );
      }
      if( ir < 0 || or < 0 ) {
         throw new IllegalArgumentException( "radii can not be less than zero [ir=" + ir + ", or=" + or + "]" );
      }
   }

   private float angle;
   private int count;
   private float cx;
   private float cy;
   private float ir;
   private float or;
   private GeneralPath path;
   private Point2D[] points;

   public Star() {
      this( 5, 5, 8, 3, 5, 0 );
   }

   public Star( float cx, float cy, float or, float ir, int count ) {
      this( cx, cy, or, ir, count, 0 );
   }

   public Star( float cx, float cy, float or, float ir, int count, float angle ) {
      validateRadii( ir, or );
      validateCount( count );
      this.cx = cx;
      this.cy = cy;
      this.or = or;
      this.ir = ir;
      this.count = count;
      this.angle = ShapeUtils.normalizeAngle( angle );
      calculateShape();
   }

   public Object clone() {
      return new Star( cx, cy, or, ir, count, angle );
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

   public int getCount() {
      return count;
   }

   public float getCx() {
      return cx;
   }

   public float getCy() {
      return cy;
   }

   public float getIr() {
      return ir;
   }

   public float getOr() {
      return or;
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
      return or;
   }

   public boolean intersects( double x, double y, double w, double h ) {
      return path.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return path.intersects( r );
   }

   public void setAngle( float angle ) {
      float a = ShapeUtils.normalizeAngle( angle );
      if( this.angle != a ) {
         this.angle = a;
         calculateShape();
      }
   }

   public void setCount( int count ) {
      if( this.count != count ) {
         validateCount( count );
         this.count = count;
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

   /**
    * WARNING: change the value of 'or' before calling this method.
    */
   public void setIr( float ir ) {
      if( this.ir != ir ) {
         validateRadii( ir, or );
         this.ir = ir;
         calculateShape();
      }
   }

   public void setOr( float or ) {
      if( this.or != or ) {
         validateRadii( ir, or );
         this.or = or;
         calculateShape();
      }
   }

   private void calculateShape() {
      float t = 360 / count;
      float a = angle + 90;
      float b = angle + 90 + (t / 2);
      a = a > 360 ? a - 360 : a;
      b = b > 360 ? b - 360 : b;
      points = new Point2D[count * 2];
      path = new GeneralPath();
      for( int i = 0; i < count; i++ ){
         float ra = (float) Math.toRadians( a );
         float ox = (float) Math.abs( or * Math.cos( ra ) );
         float oy = (float) Math.abs( or * Math.sin( ra ) );
         if( a <= 90 ){
            ox = cx + ox;
            oy = cy - oy;
         }else if( a <= 180 ){
            ox = cx - ox;
            oy = cy - oy;
         }else if( a <= 270 ){
            ox = cx - ox;
            oy = cy + oy;
         }else if( a <= 360 ){
            ox = cx + ox;
            oy = cy + oy;
         }

         float rb = (float) Math.toRadians( b );
         float ix = (float) Math.abs( ir * Math.cos( rb ) );
         float iy = (float) Math.abs( ir * Math.sin( rb ) );
         if( b <= 90 ){
            ix = cx + ix;
            iy = cy - iy;
         }else if( b <= 180 ){
            ix = cx - ix;
            iy = cy - iy;
         }else if( b <= 270 ){
            ix = cx - ix;
            iy = cy + iy;
         }else if( b <= 360 ){
            ix = cx + ix;
            iy = cy + iy;
         }

         if( i == 0 ){
            path.moveTo( ox, oy );
            path.lineTo( ix, iy );
         }else{
            path.lineTo( ox, oy );
            path.lineTo( ix, iy );
         }
         points[2 * i] = new Point2D.Float( ox, oy );
         points[(2 * i) + 1] = new Point2D.Float( ix, iy );
         a += t;
         a = a > 360 ? a - 360 : a;
         b += t;
         b = b > 360 ? b - 360 : b;
      }
      path.closePath();
   }
}
