package energymodels;

import java.awt.*;
import java.awt.Graphics.*;

/**
 * 
 *  Distributed Surface Energy Balance (SEB) model source code (developed for my PhD)
 *  Copyright (C) 2013  Chris Williams

 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 * ****************************************************************************
 * 
 * This class creates am ImagePanel object. It contains numerous methods
 * used for image display and accessing/mutating different object to and from the panel.
 *
 * @author Chris Williams 12/10/10
 *
 **/
public class GUIPanel extends Panel
{

  private Image displayImage = null;
  private double a[][] = null;
  private GRIDJava2 b = null;

  /**
   * Creates an ImpagePanel object as an extension of Panel
   **/
  public GUIPanel()
  {
    super();
  }

  /**
   * Sets a GRIDJava object to the panel object
   **/
  public void addGRIDJavaMethod(GRIDJava2 c)
  {
    b = c;
  }

  /**
   * Gets a GRIDJava object from the panel object
   **/
  public GRIDJava2 getGRIDJavaMethod()
  {
    return b;
  }

  /**
   * Gets the DisplayImage from the panel object
   **/
  public Image getDisplayImage()
  {
    return displayImage;
  }

  /**
   * Sets the DisplayImage to the panel object
   **/
  public void displayImage(Image a)
  {
    displayImage = a;
    repaint();
  }

  /**
   * Overwrites the paint method so that if an image is present,
   * it will always be displayed - this is facilitated by using
   * a Graphics object
   **/
  public void paint(Graphics g)
  {
    if (displayImage != null)
    {
      g.drawImage(displayImage, 0, 0, this);

    }
  }
}
