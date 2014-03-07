package energymodels;

import java.awt.event.*;
import java.io.BufferedReader.*;
import java.io.CharArrayReader.*;
import java.io.FileInputStream.*;

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
 * A listener which prints out a 2D array in command line of the surface type surface
 * opened via the OpenSurfaceTypeListener, using methods declared in Storage.java
 * @author Chris Williams 2/11/10
 **/
public class PrintSurfaceTypeArrayListener implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;

  PrintSurfaceTypeArrayListener(GUIPanel pn, Storage s)
  {

    panel = pn;
    store = s;

  }

  /**
   * Creates an "Open a File" dialog box from which the user can open a file. Methods from GRIDJava.java
   * are then invoked to create 1D and 2D arrays of the opened file - the 1D array being used to display an
   * image version of the file via the development of a MemoryImageSource object
   **/
  public void actionPerformed(ActionEvent e)
  {

    try
    {

      // This bit below isn't working at the moment - I wonder if the get/set methods are working over in the Storage.java class......
      // Each time it is run, it throws a java.lang.NullPointerException - check out the API to sort this out

      double[][] surfaceTypeArray = null;
      surfaceTypeArray = store.getSurfaceType();


      for (int i = 0; i < surfaceTypeArray.length; i++)
      {
        for (int j = 0; j < surfaceTypeArray[i].length; j++)
        {
          System.out.print(surfaceTypeArray[i][j] + " ");
        }
        System.out.print(" ");
      }

    }
    catch (Exception oe)
    {

      oe.printStackTrace();

    }

    System.out.println(" ");
    System.out.println("Array visible above? If yes then listener operating correctly. If no then listener not operating correctly.");

  }
}
