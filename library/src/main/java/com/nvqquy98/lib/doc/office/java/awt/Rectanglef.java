package com.nvqquy98.lib.doc.office.java.awt;

/*
 * Copyright (c) 2002-2004 LWJGL Project All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: * Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. * Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'LWJGL' nor the names
 * of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written
 * permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

/**
 * A 2D integer Rectangle class which looks remarkably like an lwjgl
 * one.
 * 
 * @author $Author: matzon $
 * @version $Revision: 2286 $ $Id: Rectangle.java 2286 2006-03-23
 *          19:32:21Z matzon $
 */
public class Rectanglef
{
  /** Rectangle's bounds */
  private float x, y, width, height;

  /**
   * Constructor for Rectangle.
   */
  public Rectanglef()
  {
  }

  /**
   * Constructor for Rectangle.
   * 
   * @param x
   * @param y
   * @param w
   * @param h
   */
  public Rectanglef( float x, float y, float w, float h )
  {
    this.x = x;
    this.y = y;
    width = w;
    height = h;
  }

  
  public void setLocation( float x, float y )
  {
    this.x = x;
    this.y = y;
  }
 
  public void setSize( float w, float h )
  {
    width = w;
    height = h;
  }
  
  public void setBounds( float x, float y, float w, float h )
  {
    this.x = x;
    this.y = y;
    width = w;
    height = h;
  }

  /**
   * Translate the rectangle by an amount.
   * 
   * @param newX
   *           The translation amount on the x axis
   * @param newY
   *           The translation amount on the y axis
   */
  public void translate( float newX, float newY )
  {
    x += newX;
    y += newY;
  }

  /**
   * Checks whether or not this <code>Rectangle</code> contains the
   * point at the specified location (<i>x</i>,&nbsp;<i>y</i>).
   * 
   * @param X
   *           the specified x coordinate
   * @param Y
   *           the specified y coordinate
   * @return <code>true</code> if the point (<i>x</i>,&nbsp;<i>y</i>)
   *         is inside this <code>Rectangle</code>;
   *         <code>false</code> otherwise.
   */
  public boolean contains( float X, float Y )
  {
    float w = width;
    float h = height;
    if( w < 0 || h < 0 )
    {
      // At least one of the dimensions is negative...
      return false;
    }
    // Note: if either dimension is zero, tests below must return
    // false...
    if( X < x || Y < y )
    {
      return false;
    }
    w += x;
    h += y;
    // overflow || intersect
    return ( w < x || w > X ) && ( h < y || h > Y );
  }

  /**
   * Checks whether this <code>Rectangle</code> entirely contains the
   * <code>Rectangle</code> at the specified location
   * (<i>X</i>,&nbsp;<i>Y</i>) with the specified dimensions
   * (<i>W</i>,&nbsp;<i>H</i>).
   * 
   * @param X
   *           the specified x coordinate
   * @param Y
   *           the specified y coordinate
   * @param W
   *           the width of the <code>Rectangle</code>
   * @param H
   *           the height of the <code>Rectangle</code>
   * @return <code>true</code> if the <code>Rectangle</code>
   *         specified by
   *         (<i>X</i>,&nbsp;<i>Y</i>,&nbsp;<i>W</i>,&nbsp;<i>H</i>)
   *         is entirely enclosed inside this <code>Rectangle</code>;
   *         <code>false</code> otherwise.
   */
  public boolean contains( float X, float Y, float W, float H )
  {
    float w = width;
    float h = height;
    if( w < 0 || W < 0 || h < 0 || H < 0 )
    {
      // At least one of the dimensions is negative...
      return false;
    }
    // Note: if any dimension is zero, tests below must return
    // false...
    if( X < x || Y < y )
    {
      return false;
    }
    w += x;
    W += X;
    if( W <= X )
    {
      // X+W overflowed or W was zero, return false if...
      // either original w or W was zero or
      // x+w did not overflow or
      // the overflowed x+w is smaller than the overflowed X+W
      if( w >= x || W > w )
      {
        return false;
      }
    }
    else
    {
      // X+W did not overflow and W was not zero, return false
      // if...
      // original w was zero or
      // x+w did not overflow and x+w is smaller than X+W
      if( w >= x && W > w )
      {
        return false;
      }
    }
    h += y;
    H += Y;
    if( H <= Y )
    {
      if( h >= y || H > h )
      {
        return false;
      }
    }
    else
    {
      if( h >= y && H > h )
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Adds a point, specified by the integer arguments
   * <code>newx</code> and <code>newy</code>, to this
   * <code>Rectangle</code>. The resulting <code>Rectangle</code> is
   * the smallest <code>Rectangle</code> that contains both the
   * original <code>Rectangle</code> and the specified point.
   * <p>
   * After adding a point, a call to <code>contains</code> with the
   * added point as an argument does not necessarily return
   * <code>true</code>. The <code>contains</code> method does not
   * return <code>true</code> for points on the right or bottom edges
   * of a <code>Rectangle</code>. Therefore, if the added point falls
   * on the right or bottom edge of the enlarged
   * <code>Rectangle</code>, <code>contains</code> returns
   * <code>false</code> for that point.
   * 
   * @param newx
   *           the x coordinates of the new point
   * @param newy
   *           the y coordinates of the new point
   */
  public void add( float newx, float newy )
  {
    float x1 = Math.min( x, newx );
    float x2 = Math.max( x + width, newx );
    float y1 = Math.min( y, newy );
    float y2 = Math.max( y + height, newy );
    x = x1;
    y = y1;
    width = x2 - x1;
    height = y2 - y1;
  }  

  /**
   * Resizes the <code>Rectangle</code> both horizontally and
   * vertically.
   * <p>
   * This method modifies the <code>Rectangle</code> so that it is
   * <code>h</code> units larger on both the left and right side, and
   * <code>v</code> units larger at both the top and bottom.
   * <p>
   * The new <code>Rectangle</code> has (<code>x&nbsp;-&nbsp;h</code>, <code>y&nbsp;-&nbsp;v</code>) as its top-left corner, a width
   * of <code>width</code>&nbsp;<code>+</code>&nbsp;<code>2h</code>,
   * and a height of <code>height</code>&nbsp;<code>+</code>&nbsp;
   * <code>2v</code>.
   * <p>
   * If negative values are supplied for <code>h</code> and
   * <code>v</code>, the size of the <code>Rectangle</code> decreases
   * accordingly. The <code>grow</code> method does not check whether
   * the resulting values of <code>width</code> and
   * <code>height</code> are non-negative.
   * 
   * @param h
   *           the horizontal expansion
   * @param v
   *           the vertical expansion
   */
  public void grow( float h, float v )
  {
    x -= h;
    y -= v;
    width += h * 2;
    height += v * 2;
  }

  /**
   * Determines whether or not this <code>Rectangle</code> is empty.
   * A <code>Rectangle</code> is empty if its width or its height is
   * less than or equal to zero.
   * 
   * @return <code>true</code> if this <code>Rectangle</code> is
   *         empty; <code>false</code> otherwise.
   */
  public boolean isEmpty()
  {
    return width <= 0 || height <= 0;
  }

  /**
   * Checks whether two rectangles are equal.
   * <p>
   * The result is <code>true</code> if and only if the argument is
   * not <code>null</code> and is a <code>Rectangle</code> object
   * that has the same top-left corner, width, and height as this
   * <code>Rectangle</code>.
   * 
   * @param obj
   *           the <code>Object</code> to compare with this
   *           <code>Rectangle</code>
   * @return <code>true</code> if the objects are equal;
   *         <code>false</code> otherwise.
   */
  @Override
  public boolean equals( Object obj )
  {
    if( obj instanceof Rectanglef )
    {
      Rectanglef r = ( Rectanglef ) obj;
      return x == r.x && y == r.y && width == r.width && height == r.height;
    }
    return super.equals( obj );
  }

  /**
   * Debugging
   * 
   * @return a String
   */
  @Override
  public String toString()
  {
    return getClass().getName() + "[x=" + x + ",y=" + y + ",width=" + width + ",height=" + height
        + "]";
  }

  /**
   * Gets the height.
   * 
   * @return Returns a float
   */
  public float getHeight()
  {
    return height;
  }

  /**
   * Sets the height.
   * 
   * @param height
   *           The height to set
   */
  public void setHeight( float height )
  {
    this.height = height;
  }

  /**
   * Gets the width.
   * 
   * @return Returns a float
   */
  public float getWidth()
  {
    return width;
  }

  /**
   * Sets the width.
   * 
   * @param width
   *           The width to set
   */
  public void setWidth( float width )
  {
    this.width = width;
  }

  /**
   * Gets the x.
   * 
   * @return Returns a float
   */
  public float getX()
  {
    return x;
  }

  /**
   * Sets the x.
   * 
   * @param x
   *           The x to set
   */
  public void setX( float x )
  {
    this.x = x;
  }

  /**
   * Gets the y.
   * 
   * @return Returns a float
   */
  public float getY()
  {
    return y;
  }

  /**
   * Sets the y.
   * 
   * @param y
   *           The y to set
   */
  public void setY( float y )
  {
    this.y = y;
  }
}
