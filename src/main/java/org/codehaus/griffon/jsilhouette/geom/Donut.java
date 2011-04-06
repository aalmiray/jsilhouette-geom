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
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Defines a donut shape based on circles or regular polygons.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class Donut implements Shape, Cloneable, Centered {
   public static void validateRadii( float ir, float or ) {
      if( ir >= or ) {
         throw new IllegalArgumentException( "'ir' can not be equal greater than 'or' [ir=" + ir + ", or=" + or + "]" );
      }
      if( ir < 0 || or < 0 ) {
         throw new IllegalArgumentException( "radii can not be less than zero [ir=" + ir + ", or=" + or + "]" );
      }
   }

   private float angle;
   private float cx;
   private float cy;
   private Shape donut;
   private float ir;
   private float or;
   private Point2D[] points;
   private int sides;

   /**
    * Creates a donut shape using default values.
    * <p>
    * <ul>
    * <li>cx = 5</li>
    * <li>cy = 5</li>
    * <li>or = 8</li>
    * <li>ir = 3</li>
    * <li>sides = 0</li>
    * <li>angle = 0</li>
    * </ul>
    */
   public Donut() {
      this( 5, 5, 8, 3, 5, 0 );
   }

   /**
    * Creates a donut shape using default values.
    * <p>
    * <ul>
    * <li>sides = 0</li>
    * <li>angle = 0</li>
    * </ul>
    * 
    * @param cx x center coordinate
    * @param cy y center coordinate
    * @param or output radius
    * @param ir inner radius
    */
   public Donut( float cx, float cy, float or, float ir ) {
      this( 5, 5, 8, 3, 5, 0 );
   }

   /**
    * Creates a donut shape using default values.
    * <p>
    * <ul>
    * <li>angle = 0</li>
    * </ul>
    * 
    * @param cx x center coordinate
    * @param cy y center coordinate
    * @param or output radius
    * @param ir inner radius
    * @param sides the number of sides (< 2 for circular donuts)
    */
   public Donut( float cx, float cy, float or, float ir, int sides ) {
      this( cx, cy, or, ir, sides, 0 );
   }

   /**
    * Creates a donut shape with the specified parameters.
    * <p>
    * 
    * @param cx x center coordinate
    * @param cy y center coordinate
    * @param or output radius
    * @param ir inner radius
    * @param sides the number of sides (< 2 for circular donuts)
    * @param angle quick rotation
    */
   public Donut( float cx, float cy, float or, float ir, int sides, float angle ) {
      validateRadii( ir, or );
      this.cx = cx;
      this.cy = cy;
      this.or = or;
      this.ir = ir;
      this.sides = sides;
      this.angle = ShapeUtils.normalizeAngle( angle );
      calculateShape();
   }

   public Object clone() {
      return new Donut( cx, cy, or, ir, sides, angle );
   }

   public boolean contains( double x, double y ) {
      return donut.contains( x, y );
   }

   public boolean contains( double x, double y, double w, double h ) {
      return donut.contains( x, y, w, h );
   }

   public boolean contains( Point2D p ) {
      return donut.contains( p );
   }

   public boolean contains( Rectangle2D r ) {
      return donut.contains( r );
   }

   public float getAngle() {
      return angle;
   }

   public Rectangle getBounds() {
      return donut.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return donut.getBounds2D();
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
      return donut.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return donut.getPathIterator( at, flatness );
   }

   public Point2D[] getPoints() {
      return points;
   }

   public float getRadius() {
      return or;
   }

   public int getSides() {
      return sides;
   }

   public boolean intersects( double x, double y, double w, double h ) {
      return donut.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return donut.intersects( r );
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

   public void setSides( int sides ) {
      if( this.sides != sides ) {
         this.sides = sides;
         calculateShape();
      }
   }

   private void calculateShape() {
      Shape innerShape = null;
      Shape outerShape = null;

      if( sides > 2 ) {
         outerShape = new RegularPolygon( cx, cy, or, sides, angle );
         innerShape = new RegularPolygon( cx, cy, ir, sides, angle );

      } else {
         outerShape = new Ellipse2D.Double( (cx - or), (cy - or), (or * 2), (or * 2) );
         innerShape = new Ellipse2D.Double( (cx - ir), (cy - ir), (ir * 2), (ir * 2) );
      }
      donut = new Area( outerShape );
      ((Area) donut).subtract( new Area( innerShape ) );
   }
}
