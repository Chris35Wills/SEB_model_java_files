package energymodels;

import java.awt.event.*;
import java.util.List;

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
 * A listener for the Melt Surface Method in ModelGUI.java - imports user uploaded files from the storage
 * class, manipulating them and eventually creating a set of new arrays, altered according to melt processes
 * defined below (these will then be saved to a hard-wired folder and extractable as ASCII files (converted
 * using methods inherited from GRIDJava.java
 *
 * ISSUES: The new surface elevation and ice thickness files that are being created (saved through the SaveNewElevation and
 * SaveNewThickness listeners) are being distorted somehow and it is happening due to something within the code below.....
 *
 * CURRENT TASK LIST: 1) Create an image of the new elevation surface (eventually an image of
 * all surfaces that can be displayed in different frames within the GUI, with labels (requires use of GridBag
 * layout) 2) Create a text file with details of new glacier area/ice volume/maximum thickness
 *
 * Fix: Do not use Kelvin as an input - the lapse rate method is dependent on Celsius and all compare clauses now assume Celcius as an input
 *
 * @author Chris Williams 16/11/11
 *
 * v.1.2 (March 2012)
 **/
public class MeltSurfaceListener implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;

  MeltSurfaceListener(GUIPanel pn, Storage s)
  {

    panel = pn;
    store = s;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {


    // Need to smack all of the below into a loop which works its way through the temperature file
    // Each iteration will require a re-setting of the elevation and ice thickness surfaces
    // Each iteration will then recalculate the new lapse rate surface

    System.out.println("Melt surface function being calculated....");

    //This gets the temperature file opened by the user

        List<Double> tempFile = store.getTemperatureFile();

    //Loops through the temp file

//    if (tempFile.isEmpty())
//    {
//      System.out.println("No data!!!");
//    }
//    else
//    {
//      System.out.println("Store:");
//      System.out.println(store.toString());
//    }

    for (Double temp : tempFile)
    {
      //Create a double object which holds the temperature read out of the temp file for the
      //specific loop iteration

      System.out.println("New Temperature loop");

      GRIDJava2 newerGJ = panel.getGRIDJavaMethod();

      double surfaceElevation[][] = store.getElevation();
      double lapserateSurface[][] = new double[surfaceElevation.length][surfaceElevation[0].length];

      int NODATA_value = newerGJ.getNODATA_values();


      //Implements a loop through surfaceElevation, filling lapserateSurface with
      //temperature values that are a function of the surface elevation and the
      //temperature of the current loop iteration using the lapse rate method
      //declared within this class

      for (int i = 0; i < surfaceElevation.length; i++)
      {
        for (int j = 0; j < surfaceElevation[i].length; j++)
        {
          double elevation = surfaceElevation[i][j];
          //System.out.printf("Surface Elevation = %.5f\n", elevation);

          if (compare(elevation, NODATA_value) != 0)
          {
            //System.out.println("!= LapseRate = " + LapseRate(elevation, temp));
            //surfaceElevation[i][j] = (double) LapseRate(elevation, temp)
            lapserateSurface[i][j] = (double) LapseRate(elevation, temp);
            
            if (compare(lapserateSurface[i][j], 0) == -1)
            {
              System.out.println(lapserateSurface[i][j]);  
            }
          }
          else if (compare(elevation, NODATA_value) == 0)
          {
            //System.out.println("== LapseRate = " + NODATA_value);
            //surfaceElevation[i][j] = (int) NODATA_value;
            lapserateSurface[i][j] = (int) NODATA_value;
          }

          //System.out.println(surfaceElevation[i][j] + " ");

        }

        //System.out.println("");
      }

      System.out.println("New lapse rate surface created");

      //Sets a surface of temperatures fixed using the LapseRate method
      //store.setLapseRate(surfaceElevation);

      //Gets all of the files required for calculating surface melt

      //double[][] lapserateSurface = store.getLapseRate();
      double[][] iceThickness = store.getThickness();
      double[][] surfaceType = store.getSurfaceType();

      for (int i = 0; i < surfaceElevation.length; i++)
      {
        for (int j = 0; j < surfaceElevation.length; j++)
        {
          if ((compare(surfaceElevation[i][j], 0) == -1) && (compare(surfaceElevation[i][j], -9999.0) == 1))
          {
            System.out.println(surfaceElevation[i][j]);
          }
        }
      }

      //Loop through each array now to check array dimensions (there is currently an OutOfBoundsException and its stacking up the modelling loop

      //System.out.println("lapserateSurface.length = " + lapserateSurface.length + " lapserateSurface[0].length = " + lapserateSurface[0].length);
      //System.out.println("iceThickness.length = " + iceThickness.length + " iceThickness[0].length = " + iceThickness[0].length);
      //System.out.println("surfaceType.length = " + surfaceType.length + " surfaceType[0].length = " + surfaceType[0].length);
      //System.out.println("surfaceElevation.length = " + surfaceElevation.length + " surfaceElevation[0].length = " + surfaceElevation[0].length);

      /**
       * The below loop is all that you should need - its loops through the lapserateSurface array and then looks at the data in the same positions of
       * the other arrays (without separately looping through them)
       **/
      for (int c = 0; c < lapserateSurface.length; c++)
      {
        for (int d = 0; d < lapserateSurface[c].length; d++)
        {
          //System.out.printf("c = %4d, d = %4d\n", c, d);
          // This statement is conditional on the surface type being that of ice

          if ((compare(lapserateSurface[c][d], 0.0) == 1) && surfaceType[c][d] == 0)
          {
            // The sum below to calculate the value of the meltFactor-Ice object
            // is taken from Hock (2003) - the value of 5.93 is a mean DDF for ice
            // calculated from 3 DDF values calculated on Storglaciaren in 1993

            double meltFactor_ICE = (5.93 * (lapserateSurface[c][d]))/ 1000;// - 273)); //* 1000; // (converts melt factor from mm to metres!);
            System.out.println("Ice melt = " + meltFactor_ICE);
           
            //insert if loop - if meltFactor_ICE >= 0.... else... do nothing
                    
                //double surfaceElevationChange = surfaceElevation[c][d] - meltFactor_ICE; - NOT NEEDED!

            //System.out.printf("lapserateSurface[%d][%d] = %.5f, surfaceElevation[%d][%d] = %.5f\n", c, d, lapserateSurface[c][d], c, d, surfaceElevation[c][d]);
            //System.out.printf("meltFactor_ICE = %.5f, surfaceElevation = %.5f\n", meltFactor_ICE, surfaceElevationChange);

            //System.out.println("Melt function over Ice method working");

            // Changes surface elevation only if the meltFactor_ICE is >= 0.0 (i.e. not negative)change to
            //surface elevation is greater than the ice thickness (otherwise rock is being melted)

            if ((meltFactor_ICE >= 0.0) & (compare(meltFactor_ICE, iceThickness[c][d]) == -1))
            {
              surfaceElevation[c][d] = surfaceElevation[c][d] - meltFactor_ICE;
            }
            else
            {
              // Do nothing...
            }

            if (meltFactor_ICE >= 0.0){

            iceThickness[c][d] = iceThickness[c][d] - meltFactor_ICE;

            }

            // This statement is conditional on the surface type being that of snow

          }
          else if ((compare(lapserateSurface[c][d], 0.0) == 1) && surfaceType[c][d] == 1)
          {


            // The sum below to calculate the value of the meltFactor-Snow object
            // is taken from Hock (2003) - the value of 3.2 is a DDF value for
            // snow calculated from Storglaciaren in 1993

            double meltFactor_SNOW = (3.2 * (lapserateSurface[c][d]))/ 1000;//- 273)); //* 1000;  // (converts melt factor from mm to metres!);
            System.out.println("Snow melt = " + meltFactor_SNOW);
                //double surfaceElevationChange = surfaceElevation[c][d] - meltFactor_SNOW;

            //System.out.println("Melt function over Snow method working");

            // Changes surface elevation only if the change to surface elevation is greater than the ice thickness
            // (otherwise rock is being melted)

            if ((meltFactor_SNOW >= 0.0) & (compare(meltFactor_SNOW, iceThickness[c][d]) == -1))
            {
              surfaceElevation[c][d] = surfaceElevation[c][d] - meltFactor_SNOW;
            }
            else
            {
                // Do nothing...
            }

            if (meltFactor_SNOW >= 0.0){

                iceThickness[c][d] = iceThickness[c][d] - meltFactor_SNOW;
            }
            


          }
          else
          {
          }



        }
      }
      store.setElevation(surfaceElevation);
      store.setThickness(iceThickness);


    }

    System.out.println("Melt function method complete");
  }

  /**
   * This calculates the temperature at a point as a function of elevation relative
   * to the temperature from a meteorological station of known elevation - the lapse
   * rate value used is 0.0065 which is between the generic environmental lapse rate
   * values of 0.006 and 0.007°C/metre (Ballantyne, 2002) (regardless of whether the
   * temperature input into the method is in Kelvin or degrees Celcius, this method
   * will work as in increments, 1K is equal to 1°C)
   **/
  public double LapseRate(double elevation, double temperature)
  {

    double BaseElevation = 800.0; //Elevation of meteorological station
    double BaseTemp = temperature; //Temperature at meteorological station
    double equalizedElevation = elevation - BaseElevation; //Calculates height difference between met station and pooint of interest
    double pseudoTemp = equalizedElevation * 0.0065; //Calculates temperature (temp - increase/decrease) lapse difference
    //according to equalizedElevation
    double lapseRate = BaseTemp - pseudoTemp; //Calculates temperature at point of interested with a lapse rate correction

    return lapseRate;

  }

  private int compare(double d, int i)
  {
    return new Double(d).compareTo(new Double(i));
  }

  private int compare(double a, double b)
  {
    return new Double(a).compareTo(new Double(b));
  }
}
