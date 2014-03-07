package energymodels;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
 * Calculates values for psi according to user input temperature. Constants are 
 * hardwired within this listener and are specific to sites as discussed in 
 * Giesen and Oerlemans (2012).
 * 
 * NB (12/11/12)/ Currently just uses the last temperature value set in 
 * OpenComplexTemperatureFileListener.java - easily altered but best to do this 
 * once the main model run file is implemented as everything will occur within 
 * a single loop so designating a temperature variable should be fine....
 * 
 * NB 2/ CHECK THAT TEMPERATURE IS DISTRIBUTED AS A FUNCTION OF TEMPERATURE 
 *       CORRECTLY
 * 
 * @author Chris 12/11/12 v 1.0
 */

public class Bulk_Flux_Calc_Listener implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;
  
  Bulk_Flux_Calc_Listener (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
      
     // Constants taken from Giesen and Oerlemans (2012) - values here 
     // correspond to those used for Midtalsbreen (Norway) 
      
     // NB/ These could be extracted from an input file or initiated here, 
     // being altered for the different model runs
     int NODATA_value = -9999;
     double psi_min = -25;
     double c = 8.7;
     double T_tip = -1.5;
     double cTa;
     
     double psi = 0;
     
     // Variable values according to user input
     double lapsed_Ta;
          
     double elevation[][] = store.getElevation();
     double lapsed_temp[][] = new double[elevation.length][elevation[0].length];
     double psi_surface[][] = new double[elevation.length][elevation[0].length];
     
      // Create new surface of psi values.
     
     for(int i = 0; i<elevation.length; i++){
         for(int j = 0; j<elevation[i].length; j++){
             
             double Elevation_for_bulk_flux = elevation[i][j];

                if (Elevation_for_bulk_flux != NODATA_value)
                {
                lapsed_temp[i][j] = (double) LapseRate(Elevation_for_bulk_flux);
                lapsed_Ta = lapsed_temp[i][j];
                
                     // Calculate value of psi [INTEGRATE THIS INTO ABOVE LOOP]
                        if(lapsed_Ta >= T_tip){
                            cTa = (c * lapsed_Ta);
                            psi = psi_min + cTa;
                            //store.setPsi(psi);
                            //System.out.println("Psi = " + psi);
                            psi_surface[i][j] = psi;
                            //store.setPsiSurface(psi_surface);
         
                        } else if (lapsed_Ta < T_tip){
                            psi = psi_min;
                            //System.out.println("Psi = " + psi);
                            //store.setPsi(psi);
                            psi_surface[i][j] = psi;
                            //store.setPsiSurface(psi_surface);
                        } 
                }

                if (Elevation_for_bulk_flux == (double) NODATA_value)
                {
                lapsed_temp[i][j] = (int) NODATA_value;
                lapsed_Ta = lapsed_temp[i][j];
                psi = NODATA_value;
                psi_surface[i][j] = psi;
                }
                            
                            store.setPsi(psi);
                            store.setPsiSurface(psi_surface);
                            store.setLapsedTempSurface(lapsed_temp);
          
         }
     }
      System.out.println("Bulk flux grid calculated and stored");
     
     
//     /**
//* This bit will open a save box to save the bulk flux layer as an 
//* ASCII, using the coordinates of the opened up elevation surface
//**/    
      
//// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
   
    FileDialog fd4 = new FileDialog(new Frame(), 
                                        "Save bulk flux surface", FileDialog.SAVE);
    fd4.setVisible(true);

    File f4 = new File(fd4.getDirectory() + fd4.getFile());
    //File f = new File("F:/Melt_modelling/Model_outputs/Hillshade_experiments/Dynamic_2010.txt");
    //File f = new File("F:/Melt_modelling/Model_outputs/Hillshade_experiments/ClearSky_2010.txt");
    //System.out.println("File saved at : " + f.getAbsolutePath());
    
    
    if((fd4.getDirectory()== null)||(fd4.getFile() == null))
    {
    System.out.println("Bulk flux file not saved");
    return;
    }
    
    else{
    
    
    FileWriter fw4;
    double[][] BulkFluxSurface_toSAVE = store.getPsiSurface();
    DecimalFormat df4 = new DecimalFormat("#.####");

    try
    {

      BufferedWriter bw4 = new BufferedWriter(new FileWriter(f4));
      bw4.write("ncols" + "         " + store.getOriginalNcols()); 
      bw4.write(System.getProperty("line.separator"));
      bw4.write("nrows" + "         " + store.getOriginalNrows()); 
      bw4.write(System.getProperty("line.separator"));
      bw4.write("xllcorner" + "     " + store.getOriginalXllcorner());
      bw4.write(System.getProperty("line.separator"));
      bw4.write("yllcorner" + "     " + store.getOriginalYllcorner());
      bw4.write(System.getProperty("line.separator"));
      bw4.write("cellsize" + "      " + store.getOriginalCellsize()); 
      bw4.write(System.getProperty("line.separator"));
      bw4.write("NODATA_value" + "  " + "-9999");
      bw4.write(System.getProperty("line.separator"));

      /**
       *   Write out the array data
       **/
    
      String tempStr = "";

      for (int a = 0; a < BulkFluxSurface_toSAVE.length; a++)
      {
        for (int b = 0; b < BulkFluxSurface_toSAVE[a].length; b++)
        {

          if (BulkFluxSurface_toSAVE[a][b] == NODATA_value)
          {

            bw4.write("-9999 ");
          }
          
          else
          {

            bw4.write(df4.format(BulkFluxSurface_toSAVE[a][b]) + " ");

          }

        }
        bw4.newLine();
      }

      bw4.close();

    }
    catch (IOException ioe)
    {

      ioe.printStackTrace();

    }
     
    }
}

   /**
   * This calculates the temperature at a point as a function of elevation relative
   * to the temperature from a meteorological station of known elevation - the lapse
   * rate value used is 0.0065 which is between the generic environmental lapse rate
   * values of 0.006 and 0.007°C/m (Ballantyne, 2002)
   **/
  
    public double LapseRate(double elevation)
  {

    double BaseElevation = 800.0; // Elevation of meteorological station
    double BaseTemp = store.getTemp(); // Temperature at meteorological station
    double equalizedElevation = elevation - BaseElevation; // Calculates height difference between met station and pooint of interest
    double pseudoTemp = equalizedElevation * 0.0065; // Calculates temperature (celcius - increase/decrease) lapse difference
    //	according to equalizedElevation
    double lapseRate = BaseTemp - pseudoTemp; // Calculates temperature at point of interested with a lapse rate correction

    return lapseRate;

  }

}



