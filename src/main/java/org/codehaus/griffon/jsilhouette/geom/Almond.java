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
 * Defines an almond or <i><a href="http://en.wikipedia.org/wiki/Vesica_piscis">Vesica Piscis</a></i> shape.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class Almond implements Shape, Cloneable, Centered {
   private float angle;
   private Shape almond;
   private float cx;
   private float cy;
   private float width;

   public Almond() {
      this( 5, 5, 2, 0 );
   }

   public Almond( float cx, float cy, float width) {
      this( cx, cy, width, 0 );
   }

   public Almond( float cx, float cy, float width, float angle ) {
      this.cx = cx;
      this.cy = cy;
      this.width = width;
      this.angle = ShapeUtils.normalizeAngle( angle );
      calculateShape();
   }

   public Object clone() {
      return new Almond( cx, cy, width, angle );
   }

   public boolean contains( double x, double y ) {
      return almond.contains( x, y );
   }

   public boolean contains( double x, double y, double w, double h ) {
      return almond.contains( x, y, w, h );
   }

   public boolean contains( Point2D p ) {
      return almond.contains( p );
   }

   public boolean contains( Rectangle2D r ) {
      return almond.contains( r );
   }

   public float getAngle() {
      return angle;
   }

   public Rectangle getBounds() {
      return almond.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return almond.getBounds2D();
   }

   public float getCx() {
      return cx;
   }

   public float getCy() {
      return cy;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return almond.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return almond.getPathIterator( at, flatness );
   }

   public float getWidth() {
      return width;
   }

   public boolean intersects( double x, double y, double w, double h ) {
      return almond.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return almond.intersects( r );
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

   public void setWidth( float width ) {
      if( this.width != width ) {
         this.width = width;
         calculateShape();
      }
   }

   private void calculateShape() {
      Shape c1 = new Ellipse2D.Float( cx - width, cy - width * 2, width * 4, width * 4 );
      almond = new Area( c1 );
      ((Area) almond)
            .intersect( new Area( new Ellipse2D.Float( cx - width * 3, cy - width * 2, width * 4, width * 4 ) ) );
      almond = ShapeUtils.rotate( almond, angle, cx, cy );
   }
}
