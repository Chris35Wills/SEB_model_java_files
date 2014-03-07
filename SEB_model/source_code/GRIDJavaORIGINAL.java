package energymodels;

import java.io.*;
import java.util.*;
import java.awt.Color;

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
 * This Class takes in a ESRI ASCII GRID file object in its constructor and turns it into an 2D array.<P>
 * It also includes a method to get a 1D compressed int array for making images and 1D String arrays.
 * @author Andy Evans (University of Leeds) and Jonathan Gill (GMap).<P>
 * @version 1.3
 * @fixes: 1.3: Switched rows for columns to make reading and writing more straightforward.
 **/
public class GRIDJavaORIGINAL
{

  private double[][] twoDdoubleArray = null;
  private String[][] twoDStringArray = null;
  private int ncols, nrows, NODATA_value = 0;
  private double xllcorner, yllcorner, cellsize = 0.0;
  private boolean debug = false;

  public GRIDJavaORIGINAL(File file, boolean convertToInts)
  {

    FileReader filer = null;
    StringTokenizer st = null;
    String temp = null;
    String line = null;

    try
    {

      BufferedReader buff = new BufferedReader(new FileReader(file));

      try
      {

        ncols = Integer.parseInt(getNextParameter(buff));
        if (debug == true)
        {
          System.out.println(ncols);
        }
      }
      catch (NumberFormatException e)
      {

        System.out.println("Invalid Format: "
          + "ncols");
      }


      try
      {

        nrows = Integer.parseInt(getNextParameter(buff));
        if (debug == true)
        {
          System.out.println(nrows);
        }
      }
      catch (NumberFormatException e)
      {
        System.out.println("Invalid Format: "
          + "nrows");
      }


      try
      {

        xllcorner = Double.parseDouble(getNextParameter(buff));
        if (debug == true)
        {
          System.out.println(xllcorner);
        }
      }
      catch (NumberFormatException e)
      {
        System.out.println("Invalid Format: "
          + "xllcorner");
      }


      try
      {

        yllcorner = Double.parseDouble(getNextParameter(buff));
        if (debug == true)
        {
          System.out.println(yllcorner);
        }
      }
      catch (NumberFormatException e)
      {
        System.out.println("Invalid Format: "
          + "right");
      }


      try
      {

        cellsize = Double.parseDouble(getNextParameter(buff));
        if (debug == true)
        {
          System.out.println(cellsize);
        }
      }
      catch (NumberFormatException e)
      {
        System.out.println("Invalid Format: "
          + "cellsize");
      }


      try
      {

        NODATA_value = Integer.parseInt(getNextParameter(buff));
        if (debug == true)
        {
          System.out.println(NODATA_value);
        }
      }
      catch (NumberFormatException e)
      {
        System.out.println("Invalid Format: "
          + "missing data value");
      }


      twoDdoubleArray = new double[nrows][ncols];
      twoDStringArray = new String[nrows][ncols];

      line = buff.readLine();

      // Get rid of any blank lines.

      if (line != null)
      {

        line.trim();
        if (line.length() == 0)
        {
          line = buff.readLine();
        }


      }


      while (line != null)
      {

        for (int i = 0; i < nrows; i++)
        {

          st = new StringTokenizer(line, ", ");

          for (int j = 0; j < ncols; j++)
          {

            twoDStringArray[i][j] = st.nextToken();

            if (convertToInts == true)
            {

              try
              {
                double z = Double.parseDouble(twoDStringArray[i][j]);
                twoDdoubleArray[i][j] = z;
              }
              catch (NumberFormatException e)
              {
                twoDdoubleArray[i][j] = 0;
              }
            }
          }

          line = buff.readLine();

          if (line != null)
          {
            line.trim();
            if (line.length() == 0)
            {
              line = buff.readLine();
            }
          }

        }

      }

      buff.close();

    }
    catch (IOException e)
    {

      e.printStackTrace();

    }


  }

  /**
   * Reads through a BufferedReader object when passed in
   **/
  private String getNextParameter(BufferedReader br)
  {


    String line = null;
    try
    {
      line = br.readLine();
    }
    catch (IOException ioe)
    {
      System.out.println("File header damaged");
      return "";
    }

    try
    {

      StringTokenizer st = new StringTokenizer(line, " ");
      String temp = st.nextToken();
      return st.nextToken();

    }
    catch (NumberFormatException e)
    {
      System.out.println("Invalid Format");
      return "";
    }

  }

  /**
   * Gets the number of columns from the file that is read in
   **/
  public int getNumberOfColumns()
  {
    return ncols;
  }

  /**
   * Gets the number of rows from the file that is read in
   **/
  public int getNumberOfRows()
  {
    return nrows;
  }

  /**
   * Gets the x co-ordinate of the upper left hand corner of the file for positioning
   **/
  public double getxllcorner()
  {
    return xllcorner;
  }

  /**
   * Gets the y co-ordinate of the upper left hand corner of the GRIDJava file
   **/
  public double getyllcorner()
  {
    return yllcorner;
  }

  /**
   * Gets the cell size of the GRIDJava file
   **/
  public double getCellsize()
  {
    return cellsize;
  }

  /**
   * Gets the NO_DATA value of the GRIDJava file
   **/
  public int getNODATA_values()
  {
    return NODATA_value;
  }

  /**
   * Gets a 2D double array from the GRIDJava file
   **/
  public double[][] getTwoDdoubleArray()
  {

    return twoDdoubleArray;

  }

  /**
   * Gets a 2D string array from the GRIDJava file
   **/
  public String[][] getTwoDStringArray()
  {

    return twoDStringArray;

  }

  /**
   * Gets a 1D int array from the GRIDJava file which can be used for image display
   **/
  public int[] getOneDintArray()
  {

    int[] oneDintArray = new int[ncols * nrows];


    double max = Double.NEGATIVE_INFINITY;
    double min = Double.POSITIVE_INFINITY;

    for (int a = 0; a < nrows; a++)
    {

      for (int b = 0; b < ncols; b++)
      {

        if (twoDdoubleArray[a][b] != NODATA_value)
        {
          if (max < twoDdoubleArray[a][b])
          {
            max = twoDdoubleArray[a][b];
          }
          if (min > twoDdoubleArray[a][b])
          {
            min = twoDdoubleArray[a][b];
          }
        }
      }

    }
    if (debug == true)
    {
      System.out.println("max = " + max + " min = " + min + " oneDintArray.length = " + oneDintArray.length);
    }

    for (int a = 0; a < nrows; a++)
    {

      for (int b = 0; b < ncols; b++)
      {
        if (twoDdoubleArray[a][b] != NODATA_value)
        {
          double temp = ((twoDdoubleArray[a][b] - min) / (max - min)) * 255.0;
          if ((new Double(temp)).isNaN())
          {
            temp = 0.0;
          }
          if (debug == true)
          {
            System.out.println(temp);
          }
          Color tempColor = new Color((int) temp, (int) temp, (int) temp);
          oneDintArray[(a * ncols) + b] = tempColor.getRGB();
        }
        else
        {
          oneDintArray[(a * ncols) + b] = Color.BLUE.getRGB();
        }
      }

    }


    return oneDintArray;
  }

  /**
   * Gets a 1D string array from the GRIDJava file
   **/
  public String[] getOneDStringArray()
  {

    String[] oneDStringArray = new String[ncols * nrows];

    for (int a = 0; a < nrows; a++)
    {

      for (int b = 0; b < ncols; b++)
      {

        oneDStringArray[(b * ncols) + a] = twoDStringArray[a][b];

      }

    }
    return oneDStringArray;
  }
}
