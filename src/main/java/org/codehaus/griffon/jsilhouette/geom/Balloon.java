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
 * Defines a text balloon shape (like the ones used on tooltips &amp; comics).<p>
 *
${img}
 *
${code}
 *
 * @author Andres Almiray
 */
public class Balloon implements Shape, Cloneable {
   public static final int ANGLE_AT_END = Triangle.ANGLE_AT_END;
   public static final int ANGLE_AT_START = Triangle.ANGLE_AT_START;
   public static final int NONE = Triangle.NONE;

   public static final int TAB_AT_BOTTOM = 0;
   public static final int TAB_AT_LEFT = 3;
   public static final int TAB_AT_RIGHT = 1;
   public static final int TAB_AT_TOP = 2;

   private int anglePosition = NONE;
   private float arc;
   private Shape balloon;
   private float height;
   private float tabDisplacement;
   private float tabHeight;
   private int tabLocation;
   private float tabWidth;
   private float width;
   private float x;
   private float y;

   public Balloon() {
      this( 0, 0, 20, 20, 5, 5, 2.5f, TAB_AT_BOTTOM, 0.5f );
   }

   public Balloon( float x, float y, float width, float height, float arc, float tabWidth, float tabHeight,
         int tabLocation, float tabDisplacement ) {
      super();
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.arc = arc;
      this.tabWidth = tabWidth;
      this.tabHeight = tabHeight;
      this.tabLocation = tabLocation;
      this.tabDisplacement = tabDisplacement;
      calculatePath();
   }

   public Balloon( float x, float y, float width, float height, float arc, float tabWidth, float tabHeight,
         int tabLocation, float tabDisplacement, int anglePosition ) {
      super();
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.arc = arc;
      this.tabWidth = tabWidth;
      this.tabHeight = tabHeight;
      this.tabLocation = tabLocation;
      this.tabDisplacement = tabDisplacement;
      this.anglePosition = anglePosition;
      calculatePath();
   }

   public Object clone() {
      return new Balloon( x, y, width, height, arc, tabWidth, tabHeight, tabLocation, tabDisplacement, anglePosition );
   }

   public boolean contains( double x, double y ) {
      return balloon.contains( x, y );
   }

   public boolean contains( double x, double y, double w, double h ) {
      return balloon.contains( x, y, w, h );
   }

   public boolean contains( Point2D p ) {
      return balloon.contains( p );
   }

   public boolean contains( Rectangle2D r ) {
      return balloon.contains( r );
   }

   public int getAnglePosition() {
      return anglePosition;
   }

   public float getArc() {
      return arc;
   }

   public Rectangle getBounds() {
      return balloon.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return balloon.getBounds2D();
   }

   public float getHeight() {
      return height;
   }

   public PathIterator getPathIterator( AffineTransform at ) {
      return balloon.getPathIterator( at );
   }

   public PathIterator getPathIterator( AffineTransform at, double flatness ) {
      return balloon.getPathIterator( at, flatness );
   }

   public float getTabDisplacement() {
      return tabDisplacement;
   }

   public float getTabHeight() {
      return tabHeight;
   }

   public int getTabLocation() {
      return tabLocation;
   }

   public float getTabWidth() {
      return tabWidth;
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
      return balloon.intersects( x, y, w, h );
   }

   public boolean intersects( Rectangle2D r ) {
      return balloon.intersects( r );
   }

   public void setAnglePosition( int anglePosition ) {
      if( this.anglePosition != anglePosition ) {
         this.anglePosition = anglePosition;
         calculatePath();
      }
   }

   public void setArc( float arc ) {
      if( this.arc != arc ) {
         this.arc = arc;
         calculatePath();
      }
   }

   public void setHeight( float height ) {
      if( this.height != height ) {
         this.height = height;
         calculatePath();
      }
   }

   public void setTabDisplacement( float tabDisplacement ) {
      if( this.tabDisplacement != tabDisplacement ) {
         this.tabDisplacement = tabDisplacement;
         calculatePath();
      }
   }

   public void setTabHeight( float tabHeight ) {
      if( this.tabHeight != tabHeight ) {
         this.tabHeight = tabHeight;
         calculatePath();
      }
   }

   public void setTabLocation( int tabLocation ) {
      if( this.tabLocation != tabLocation ) {
         this.tabLocation = tabLocation;
         calculatePath();
      }
   }

   public void setTabWidth( float tabWidth ) {
      if( this.tabWidth != tabWidth ) {
         this.tabWidth = tabWidth;
         calculatePath();
      }
   }

   public void setWidth( float width ) {
      if( this.width != width ) {
         this.width = width;
         calculatePath();
      }
   }

   public void setX( float x ) {
      if( this.x != x ) {
         this.x = x;
         calculatePath();
      }
   }

   public void setY( float y ) {
      if( this.y != y ) {
         this.y = y;
         calculatePath();
      }
   }

   private int calculateAnglePosition() {
      return (int) Math.abs( anglePosition - ANGLE_AT_END );
   }

   private void calculatePath() {
      Shape rectangle = new RoundRectangle2D.Float( x, y, width, height, arc, arc );
      Triangle triangle = null;
      AffineTransform at = new AffineTransform();

      switch( tabLocation ) {
         case TAB_AT_RIGHT:
            if( anglePosition == NONE ) {
               triangle = new Triangle( 0f, 0f, tabWidth, 270f, tabHeight );
            } else {
               triangle = new Triangle( 0f, 0f, tabWidth, 270f, calculateAnglePosition(), tabHeight );
            }
            float a = (tabWidth + arc - height) * tabDisplacement;
            at = AffineTransform.getTranslateInstance( x + width, y + height - tabWidth - (arc / 2) + a );
            break;
         case TAB_AT_LEFT:
            if( anglePosition == NONE ) {
               triangle = new Triangle( 0, 0, tabWidth, 90, tabHeight );
            } else {
               triangle = new Triangle( 0, 0, tabWidth, 90, calculateAnglePosition(), tabHeight );
            }
            float b = (height - arc - tabWidth) * tabDisplacement;
            at = AffineTransform.getTranslateInstance( x, y + tabWidth + (arc / 2) + b );
            break;
         case TAB_AT_TOP:
            if( anglePosition == NONE ) {
               triangle = new Triangle( 0, 0, tabWidth, 0, tabHeight );
            } else {
               triangle = new Triangle( 0, 0, tabWidth, 0, calculateAnglePosition(), tabHeight );
            }
            float c = (tabWidth + arc - width) * tabDisplacement;
            at = AffineTransform.getTranslateInstance( x + width - tabWidth - (arc / 2) + c, y );
            break;
         default:
            if( anglePosition == NONE ) {
               triangle = new Triangle( 0, 0, tabWidth, 180, tabHeight );
            } else {
               triangle = new Triangle( 0, 0, tabWidth, 180, calculateAnglePosition(), tabHeight );
            }
            float d = (width - arc - tabWidth) * tabDisplacement;
            at = AffineTransform.getTranslateInstance( x + tabWidth + (arc / 2) + d, y + height );
      }

      balloon = new Area( rectangle );
      ((Area) balloon).add( new Area( at.createTransformedShape( triangle ) ) );
   }
}
