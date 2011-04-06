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
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Defines a rounded rectangle, each corner may have a different roundness factor.<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class MultiRoundRectangle implements Shape, Cloneable {
   public static void validateBottomFactors( float bottomLeftWidth, float bottomRightWidth, float width ) {
      if( bottomLeftWidth + bottomRightWidth > width ) {
         throw new IllegalArgumentException( "bottom rounding factors are invalid: " + bottomLeftWidth + " + "
               + bottomRightWidth + " > " + width );
      }
   }

   public static void validateLeftFactors( float topLeftHeight, float bottomLeftHeight, float height ) {
      if( topLeftHeight + bottomLeftHeight > height ) {
         throw new IllegalArgumentException( "left rounding factors are invalid: " + topLeftHeight + " + "
               + bottomLeftHeight + " > " + height );
      }
   }

   public static void validateRightFactors( float topRightHeight, float bottomRightHeight, float height ) {
      if( topRightHeight + bottomRightHeight > height ) {
         throw new IllegalArgumentException( "right rounding factors are invalid: " + topRightHeight + " + "
               + bottomRightHeight + " > " + height );
      }
   }

   public static void validateTopFactors( float topLeftWidth, float topRightWidth, float width ) {
      if( topLeftWidth + topRightWidth > width ) {
         throw new IllegalArgumentException( "top rounding factors are invalid: " + topLeftWidth + " + "
               + topRightWidth + " > " + width );
      }
   }

   private float bottomLeftHeight;
   private float bottomLeftWidth;
   private float bottomRightHeight;
   private float bottomRightWidth;
   private float height;
   private GeneralPath rectangle;
   private float topLeftHeight;
   private float topLeftWidth;
   private float topRightHeight;
   private float topRightWidth;
   private float width;
   private float x;
   private float y;

   public MultiRoundRectangle() {
      this( 0, 0, 10, 10, 0, 0, 0, 0, 0, 0, 0, 0 );
   }

   public MultiRoundRectangle( float x, float y, float width, float height, float topLeft, float topRight,
         float bottomLeft, float bottomRight ) {
      this( x, y, width, height, topLeft, topLeft, topRight, topRight, bottomLeft, bottomLeft, bottomRight, bottomRight );
   }

   public MultiRoundRectangle( float x, float y, float width, float height, float topLeftWidth, float topLeftHeight,
         float topRightWidth, float topRightHeight, float bottomLeftWidth, float bottomLeftHeight,
         float bottomRightWidth, float bottomRightHeight ) {
      validateTopFactors( topLeftWidth, topRightWidth, width );
      validateBottomFactors( bottomLeftWidth, bottomRightWidth, width );
      validateLeftFactors( topLeftHeight, bottomLeftHeight, height );
      validateRightFactors( topRightHeight, bottomRightHeight, height );

      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.topLeftWidth = topLeftWidth;
      this.topLeftHeight = topLeftHeight;
      this.topRightWidth = topRightWidth;
      this.topRightHeight = topRightHeight;
      this.bottomLeftWidth = bottomLeftWidth;
      this.bottomLeftHeight = bottomLeftHeight;
      this.bottomRightWidth = bottomRightWidth;
      this.bottomRightHeight = bottomRightHeight;
      calculateShape();
   }

   public Object clone() {
      return new MultiRoundRectangle( x, y, width, height, topLeftWidth, topLeftHeight, topRightWidth, topRightHeight,
            bottomLeftWidth, bottomLeftHeight, bottomRightWidth, bottomRightHeight );
   }

   public boolean contains( double x, double y ) {
      return rectangle.contains( x, y );
   }

   public boolean contains( double x, double y, double w, double h ) {
      return rectangle.contains( x, y, w, h );
   }

   public boolean contains( Point2D p ) {
      return rectangle.contains( p );
   }

   public boolean contains( Rectangle2D r ) {
      return rectangle.contains( r );
   }

   public float getBottomLeft() {
      return bottomLeftWidth;
   }

   public float getBottomLeftHeight() {
      return bottomLeftHeight;
   }

   public float getBottomLeftWidth() {
      return bottomLeftWidth;
   }

   public float getBottomRight() {
      return bottomRightWidth;
   }

   public float getBottomRightHeight() {
      return bottomLeftHeight;
   }

   public float getBottomRightWidth() {
      return bottomRightWidth;
   }

   public Rectangle getBounds() {
      return rectangle.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return rectangle.getBounds2D();
   }

   public float getHeight() {
      return height;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return rectangle.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return rectangle.getPathIterator( at, flatness );
   }

   public float getTopLeft() {
      return topLeftWidth;
   }

   public float getTopLeftHeight() {
      return topLeftHeight;
   }

   public float getTopLeftWidth() {
      return topLeftWidth;
   }

   public float getTopRight() {
      return topRightWidth;
   }

   public float getTopRightHeight() {
      return topLeftHeight;
   }

   public float getTopRightWidth() {
      return topRightWidth;
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
      return rectangle.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return rectangle.intersects( r );
   }

   /**
    * WARNING: change the value of 'height' before calling this method.
    */
   public void setBottomLeftHeight( float bottomLeftHeight ) {
      if( this.bottomLeftHeight != bottomLeftHeight ) {
         validateLeftFactors( topLeftHeight, bottomLeftHeight, height );
         this.bottomLeftHeight = bottomLeftHeight;
         calculateShape();
      }
   }

   /**
    * WARNING: change the value of 'width' before calling this method.
    */
   public void setBottomLeftWidth( float bottomLeftWidth ) {
      if( this.bottomLeftWidth != bottomLeftWidth ) {
         validateBottomFactors( bottomLeftWidth, bottomRightWidth, width );
         this.bottomLeftWidth = bottomLeftWidth;
         calculateShape();
      }
   }

   /**
    * WARNING: change the value of 'height' before calling this method.
    */
   public void setBottomRightHeight( float bottomRightHeight ) {
      if( this.bottomRightHeight != bottomRightHeight ) {
         validateRightFactors( topRightHeight, bottomRightHeight, height );
         this.bottomRightHeight = bottomRightHeight;
         calculateShape();
      }
   }

   /**
    * WARNING: change the value of 'width' before calling this method.
    */
   public void setBottomRightWidth( float bottomRightWidth ) {
      if( this.bottomRightWidth != bottomRightWidth ) {
         validateBottomFactors( bottomLeftWidth, bottomRightWidth, width );
         this.bottomRightWidth = bottomRightWidth;
         calculateShape();
      }
   }

   public void setHeight( float height ) {
      if( this.height != height ) {
         this.height = height;
         calculateShape();
      }
   }

   /**
    * WARNING: change the value of 'height' before calling this method.
    */
   public void setTopLeftHeight( float topLeftHeight ) {
      if( this.topLeftHeight != topLeftHeight ) {
         validateLeftFactors( topLeftHeight, bottomLeftHeight, height );
         this.topLeftHeight = topLeftHeight;
         calculateShape();
      }
   }

   /**
    * WARNING: change the value of 'width' before calling this method.
    */
   public void setTopLeftWidth( float topLeftWidth ) {
      if( this.topLeftWidth != topLeftWidth ) {
         validateTopFactors( topLeftWidth, topRightWidth, width );
         this.topLeftWidth = topLeftWidth;
         calculateShape();
      }
   }

   /**
    * WARNING: change the value of 'height' before calling this method.
    */
   public void setTopRightHeight( float topRightHeight ) {
      if( this.topRightHeight != topRightHeight ) {
         validateRightFactors( topRightHeight, bottomRightHeight, height );
         this.topRightHeight = topRightHeight;
         calculateShape();
      }
   }

   /**
    * WARNING: change the value of 'width' before calling this method.
    */
   public void setTopRightWidth( float topRightWidth ) {
      if( this.topRightWidth != topRightWidth ) {
         validateTopFactors( topLeftWidth, topRightWidth, width );
         this.topRightWidth = topRightWidth;
         calculateShape();
      }
   }
   
   /**
    * WARNING: change the value of 'width' before calling this method.<br/>
    * WARNING: change the value of 'height' before calling this method.
    */
   public void setTopLeft( float topLeft ) {
      if( topLeftWidth != topLeft && topLeftHeight != topLeft ){
         validateTopFactors( topLeft, topRightWidth, width );
         validateLeftFactors( topLeft, bottomLeftHeight, height );
         this.topLeftWidth = topLeft;
         this.topLeftHeight = topLeft;
         calculateShape();
      }
   }
   
   /**
    * WARNING: change the value of 'width' before calling this method.<br/>
    * WARNING: change the value of 'height' before calling this method.
    */
   public void setTopRight( float topRight ) {
      if( topRightWidth != topRight && topRightHeight != topRight ){
         validateTopFactors( topLeftWidth, topRight, width );
         validateRightFactors( topRight, bottomRightHeight, height );
         this.topRightWidth = topRight;
         this.topRightHeight = topRight;
         calculateShape();
      }
   }
   
   /**
    * WARNING: change the value of 'width' before calling this method.<br/>
    * WARNING: change the value of 'height' before calling this method.
    */
   public void setBottomLeft( float bottomLeft ) {
      if( bottomLeftWidth != bottomLeft && bottomLeftHeight != bottomLeft ){
         validateBottomFactors( bottomLeft, bottomRightWidth, width );
         validateLeftFactors( bottomLeft, topLeftHeight, height );
         this.bottomLeftWidth = bottomLeft;
         this.bottomLeftHeight = bottomLeft;
         calculateShape();
      }
   }

   /**
    * WARNING: change the value of 'width' before calling this method.<br/>
    * WARNING: change the value of 'height' before calling this method.
    */
   public void setBottomRight( float bottomRight ) {
      if( bottomRightWidth != bottomRight && bottomRightHeight != bottomRight ){
         validateBottomFactors( bottomLeftWidth, bottomRight, width );
         validateRightFactors( bottomRight, topRightHeight, height );
         this.bottomRightWidth = bottomRight;
         this.bottomRightHeight = bottomRight;
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

   private void calculateShape() {
      rectangle = new GeneralPath();
      if( topLeftWidth > 0 ) {
         rectangle.moveTo( x + topLeftWidth, y );
         rectangle.append( new Arc2D.Float( x, y, topLeftWidth * 2, topLeftHeight * 2, 90, 90, Arc2D.OPEN ), true );
      } else {
         rectangle.moveTo( x, y );
         rectangle.lineTo( x, y + height - bottomLeftHeight );
      }

      if( bottomLeftWidth > 0 ) {
         rectangle.append( new Arc2D.Float( x, y + height - (bottomLeftHeight * 2), bottomLeftWidth * 2,
               bottomLeftHeight * 2, 180, 90, Arc2D.OPEN ), true );
      } else {
         rectangle.lineTo( x, y + height );
      }

      if( bottomRightWidth > 0 ) {
         rectangle.append( new Arc2D.Float( x + width - (bottomRightWidth * 2), y + height - (bottomRightHeight * 2),
               bottomRightWidth * 2, bottomRightHeight * 2, 270, 90, Arc2D.OPEN ), true );
      } else {
         rectangle.lineTo( x + width, y + height );
      }

      if( topRightWidth > 0 ) {
         rectangle.append( new Arc2D.Float( x + width - (topRightWidth * 2), y, topRightWidth * 2, topRightHeight * 2,
               0, 90, Arc2D.OPEN ), true );
      } else {
         rectangle.lineTo( x + width, y );
      }

      rectangle.closePath();
   }
}
