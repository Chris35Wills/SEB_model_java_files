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
 * Radiation on a slope module. Requires a slope, aspect and hillshade surface 
 * to be in storage as well as values for tau and TOA.
 * 
 * Required data:
 * 
 * Historical:          Year| Month| Tau| TOA| + aforementioned surfaces
 * Contemporary:        Year| Month| Day| Tau| TOA| + aforementioned surfaces
 * 
 * The first test run will use a 1959 surface (therefore slope and aspect) and 
 * a 1959 June mean hillshade surface. The solar geometry and other values being
 * used will be for 21st June 2010 (as tau is not available pre-2007 yet)
 * 
 * NB1/ Everything has to be in radians when working with Java.Math
 * 
 * @author Chris 25/10/12 v 1.0
 */

public class Radiation_on_slope_Listnr implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;
  
  Radiation_on_slope_Listnr (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
      
   double slope[][], aspect[][], slope_RAD[][], aspect_RAD[][], hillshade[][], surfaceRadiationSurface[][];
   double tau, azimuth_radians, zenith_radians, TOA, azimuth, zenith;
   double surface_Radiation = 0;
   double NODATA_value = -9999;
   ArrayList <Double> Surface_Radiation_List = new ArrayList<Double>();
   
   slope = store.getSlope();
   aspect = store.getAspect();
   hillshade = store.getHillshade(); 
   azimuth = store.getAzimuth();
   System.out.println("Azimuth (deg) = " + azimuth);
   zenith = store.getZenith();
   System.out.println("Zenith (deg) = " + zenith);
   slope_RAD = new double[hillshade.length][hillshade[0].length];
   aspect_RAD = new double[hillshade.length][hillshade[0].length];
   //Converts the slope and aspect surfaces to radians
   
   for(int a = 0; a< slope.length; a++){
      for (int b = 0; b< slope[a].length; b++){
          
          slope[a][b] = aspect[a][b];
          
          if(slope[a][b] != NODATA_value){
          
          double slope_initial_value = slope[a][b];
          double slope_radians = Math.toRadians(slope_initial_value);
          slope_RAD[a][b] = slope_radians;
          
          } else {
          
          slope_RAD[a][b] = NODATA_value;
                    
          }
          
          if(aspect[a][b] != NODATA_value){
          
          double aspect_initial_value = aspect[a][b];
          double aspect_radians = Math.toRadians(aspect_initial_value);
          aspect_RAD[a][b] = aspect_radians;
          
          }else{
          
          aspect_RAD[a][b] = NODATA_value;
              
          }
          
      }
   }
   
   // Converts hillshade % values from being between 0 and 100 to being between 
   // 0 and 1
   
    for(int i = 0; i<hillshade.length; i++){
       for(int j = 0; j<hillshade[i].length; j++){
           
           if((hillshade[i][j] != NODATA_value)&&(hillshade[i][j] != 0)){
               
               double hs_0_1_value = hillshade[i][j]/100;
               hillshade[i][j] = hs_0_1_value;
           }
            else if(hillshade[i][j] == 0){
               
               hillshade[i][j] = 0;
           
           } 
                else{
           
               hillshade[i][j] = NODATA_value;
           
           }           
       }
    }                                
   
   surfaceRadiationSurface = new double[slope.length][slope[0].length];
   
   tau = store.getTau();                                   // These would be taken from lists in the full
   azimuth_radians = (Math.toRadians(store.getAzimuth())); // model but for this run just use a single 
   zenith_radians = (Math.toRadians(store.getZenith()));   // value i.e. read in a one line input met  
   TOA = store.getTOA();                                   // data file  
   
   System.out.println("Azimuth (rad) = " + azimuth_radians);
   System.out.println("Zenith (rad) = " + zenith_radians);
   // Start looping through surfaces and implement equation - this would be 
   // nested in an array list loop eventually
   
   for(int i = 0; i<slope_RAD.length; i++){
       for(int j = 0; j<slope_RAD[i].length; j++){
           
           slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];
           //slope_RAD[i][j] = hillshade[i][j];
           
           double loop_slope = slope_RAD[i][j];
           double loop_aspect = aspect_RAD[i][j];
           double loop_hillshade = hillshade[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Experiment to see radiation under clearsky conditions
           
           if(loop_slope != NODATA_value && loop_aspect != NODATA_value && loop_hillshade != NODATA_value){
           
           //double loop_slope_radians = (Math.toRadians(slope[i][j]));
           //double loop_aspect_radians = (Math.toRadians(aspect[i][j]));
           //double loop_hillshade_percentage = hillshade[i][j]; // Gives hillshade as a %
 
           //************** ORIGINAL SURFACE RADIATION EQUATION
//           surface_Radiation = (Math.cos(zenith_radians)*Math.cos(loop_slope_radians)
//                   +Math.sin(zenith_radians)*Math.sin(loop_slope_radians)*Math.cos(azimuth_radians - loop_aspect_radians))*
//                        TOA * Math.exp(-tau/Math.cos(zenith_radians));
           
           
           //************** MODIFIED SURFACE RADIATION EQUATION
             surface_Radiation = loop_hillshade * (((Math.cos(zenith_radians)*Math.cos(loop_slope))
                   +(Math.sin(zenith_radians)*Math.sin(loop_slope)*Math.cos(azimuth_radians - loop_aspect)))*
                        TOA * Math.exp(-tau/Math.cos(zenith_radians)));
           
//           Surface_Radiation_List.add(surface_Radiation);           
//           store.setSurfaceRadiation(surface_Radiation);
//           store.setSurfaceRadiationFile(Surface_Radiation_List);
           
           if(surface_Radiation >= 0.000001){  
           
               surfaceRadiationSurface[i][j] = surface_Radiation;
           
           }
           else if(surface_Radiation < 0.000001){
           
               surfaceRadiationSurface[i][j] = 0.0;
           
           }
           
           } else if (loop_slope == NODATA_value || loop_aspect == NODATA_value || loop_hillshade == NODATA_value){
            
               //surface_Radiation = -9999;
               surfaceRadiationSurface[i][j] = NODATA_value;
           
           }
           
           store.setSurfaceRadiationSurface(surfaceRadiationSurface);
           
         }
   }
   
/**
* This bit will open a save box to save the slope layer as an 
* ASCII, using the coordinates of the opened up elevation surface
**/    
      
//// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
   
    FileDialog fd = new FileDialog(new Frame(), 
                                        "Save Surface Radiation (calc. on slope) surface", FileDialog.SAVE);
    fd.setVisible(true);

    File f = new File(fd.getDirectory() + fd.getFile());
    //File f = new File("F:/Melt_modelling/Model_outputs/Hillshade_experiments/Dynamic_2010.txt");
    //File f = new File("F:/Melt_modelling/Model_outputs/Hillshade_experiments/ClearSky_2010.txt");
    //System.out.println("File saved at : " + f.getAbsolutePath());
    
    FileWriter fw;
    //double[][] surfaceRadiationSurface_toSAVE = store.getSurfaceRadiationSurface();
    DecimalFormat df = new DecimalFormat("#.####");

    

    try
    {

      BufferedWriter bw = new BufferedWriter(new FileWriter(f));
      bw.write("ncols" + "         " + store.getOriginalNcols()); 
      bw.write(System.getProperty("line.separator"));
      bw.write("nrows" + "         " + store.getOriginalNrows()); 
      bw.write(System.getProperty("line.separator"));
      bw.write("xllcorner" + "     " + store.getOriginalXllcorner());
      bw.write(System.getProperty("line.separator"));
      bw.write("yllcorner" + "     " + store.getOriginalYllcorner());
      bw.write(System.getProperty("line.separator"));
      bw.write("cellsize" + "      " + store.getOriginalCellsize()); 
      bw.write(System.getProperty("line.separator"));
      bw.write("NODATA_value" + "  " + "-9999");
      bw.write(System.getProperty("line.separator"));

      /**
       *   Write out the array data
       **/
    
      String tempStr = "";

      for (int a = 0; a < surfaceRadiationSurface.length; a++)
      {
        for (int b = 0; b < surfaceRadiationSurface[a].length; b++)
        {


          if (surfaceRadiationSurface[a][b] == NODATA_value)
          {

            bw.write("-9999 ");
          }
          else
          {

            bw.write(df.format(surfaceRadiationSurface[a][b]) + " ");

          }

        }
        bw.newLine();
      }

      bw.close();

    }
    catch (IOException ioe)
    {

      ioe.printStackTrace();

    }
    
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
   System.out.println("Surface radiation surface stored and saved: " + f.getAbsolutePath());
         
  }
    
}


