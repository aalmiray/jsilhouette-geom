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
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Defines a rays shape.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class Rays implements Shape, Cloneable, Centered {
   public static void validateExtent( float extent ) {
      if( extent < 0 || extent > 1 ) {
         throw new IllegalArgumentException( "extent must be inside the range [0..1]" );
      }
   }

   public static void validateRays( float rays ) {
      if( rays < 2 ) {
         throw new IllegalArgumentException( "rays can not be less than 2" );
      }
   }

   private float angle;
   private float cx;
   private float cy;
   private float extent;
   private GeneralPath path;
   private float radius;
   private int rays;
   private boolean rounded;

   public Rays() {
      this( 5, 5, 5, 2, 0, 0.5f, false );
   }

   public Rays( float cx, float cy, float radius, int rays ) {
      this( cx, cy, radius, rays, 0, 0.5f, false );
   }

   public Rays( float cx, float cy, float radius, int rays, float extent ) {
      this( cx, cy, radius, rays, 0, extent, false );
   }

   public Rays( float cx, float cy, float radius, int rays, float angle, float extent, boolean rounded ) {
      validateRays( rays );
      validateExtent( extent );
      this.cx = cx;
      this.cy = cy;
      this.radius = radius;
      this.rays = rays;
      this.angle = ShapeUtils.normalizeAngle( angle );
      this.extent = extent;
      this.rounded = rounded;
      calculateShape();
   }

   public Object clone() {
      return new Rays( cx, cy, radius, rays, angle, extent, rounded );
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

   public float getExtent() {
      return extent;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return path.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return path.getPathIterator( at, flatness );
   }

   public float getRadius() {
      return radius;
   }

   public int getRays() {
      return rays;
   }

   public boolean intersects( double x, double y, double w, double h ) {
      return path.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return path.intersects( r );
   }

   public boolean isRounded() {
      return rounded;
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

   public void setExtent( float extent ) {
      if( this.extent != extent ) {
         validateExtent( extent );
         this.extent = extent;
         calculateShape();
      }
   }

   public void setRadius( float radius ) {
      if( this.radius != radius ) {
         this.radius = radius;
         calculateShape();
      }
   }

   public void setRays( int rays ) {
      if( this.rays != rays ) {
         validateRays( rays );
         this.rays = rays;
         calculateShape();
      }
   }

   public void setRounded( boolean rounded ) {
      if( this.rounded != rounded ) {
         this.rounded = rounded;
         calculateShape();
      }
   }

   private void calculateShape() {
      float sides = rays * 2;
      float t = 360 / sides;
      float a = angle;
      a = a > 360 ? a - 360 : a;
      float e = (extent * t * 2) - t;
      float[][] points = new float[rays * 2][];
      float[] angles = new float[rays * 2];
      for( int i = 0; i < sides; i++ ) {
         float r = i % 2 == 0 ? a : a + e;
         r = r < 0 ? 360 + r : r;
         // r = r > 360 ? r - 360 : r;
         float ra = (float) Math.toRadians( r );
         float x = (float) Math.abs( radius * Math.cos( ra ) );
         float y = (float) Math.abs( radius * Math.sin( ra ) );
         if( r <= 90 || r > 360 ) {
            x = cx + x;
            y = cy - y;
         } else if( r <= 180 ) {
            x = cx - x;
            y = cy - y;
         } else if( r <= 270 ) {
            x = cx - x;
            y = cy + y;
         } else if( r <= 360 ) {
            x = cx + x;
            y = cy + y;
         }
         points[i] = new float[] { x, y };
         angles[i] = r;
         a += t;
         a = a > 360 ? a - 360 : a;
      }

      path = new GeneralPath();
      for( int i = 0; i < rays; i++ ) {
         path.moveTo( cx, cy );
         path.lineTo( points[(2 * i)][0], points[(2 * i)][1] );
         if( rounded ) {
            path.append( new Arc2D.Float( cx - radius, cy - radius, radius * 2, radius * 2, angles[(2 * i)], t + e,
                  Arc2D.OPEN ), true );
         } else {
            path.lineTo( points[(2 * i) + 1][0], points[(2 * i) + 1][1] );
         }
         path.closePath();
      }
   }
}
