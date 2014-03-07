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
 * Listener which distributes a temperature value over an elevation surface,
 * varying the temperature according to a standard 0.0065°C environmental lapse
 * rate, as a function of elevation
 **/
public class LapseRateMethodListener implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;

  LapseRateMethodListener(GUIPanel pn, Storage s)
  {

    panel = pn;
    store = s;

  }

  // 1. Takes in an elevation surface (opened previously using "Open Elevation Surface" button and therefore on the panel)
  // A problem here will be that there are numerous surfaces added to the panel - possible to divide the panel up
  // into different labelled quaters and acces each part separately?
  // 2. Loops through surface and gets elevation values
  // 3. Creates a new surface of lapse rate corrected temperature values
  public void actionPerformed(ActionEvent e)
  {

    // Gets the GRIDJava object from the panel (currently this could be any surface
    // added using any of the Open File methods currently constructed

    GRIDJava2 newerGJ = panel.getGRIDJavaMethod();

    // Creates a new array object from the GRIDJava object newerGJ created above

    double newArray[][] = newerGJ.getTwoDdoubleArray();

    double ActualTemperature;
    int NODATA_value = newerGJ.getNODATA_values();


    // Implements a loop through the newArray[][] object, filling it with temperature
    // values that are a function of the surface elevation using the lapse rate method

    for (int i = 0; i < newArray.length; i++)
    {
      for (int j = 0; j < newArray[i].length; j++)
      {
        double Elevation = newArray[i][j];

        if (Elevation != NODATA_value)
        {
          newArray[i][j] = (double) LapseRate(Elevation);
        }

        if (Elevation == (double) NODATA_value)
        {
          newArray[i][j] = (int) NODATA_value;
        }


      }
    }


    store.setLapseRate(newArray);
    System.out.println("Lapse rate methods working");

  }

  /**
   * This calculates the temperature at a point as a function of elevation relative
   * to the temperature from a meterological station of known elevation - the lapse
   * rate value used is 0.0065 which is between the generic environmental lapse rate
   * values of 0.006 and 0.007°C/metre (Ballantyne, 2002)
   **/
  public double LapseRate(double elevation)
  {

    double BaseElevation = 800.0; // Elevation of meteorological station
    double BaseTemp = 14.0; // Temperature at meteorological station
    double equalizedElevation = elevation - BaseElevation; // Calculates height difference between met station and pooint of interest
    double pseudoTemp = equalizedElevation * 0.0065; // Calculates temperature (celcius - increase/decrease) lapse difference
    //	according to equalizedElevation
    double lapseRate = BaseTemp - pseudoTemp; // Calculates temperature at point of interested with a lapse rate correction

    return lapseRate;

  }
}