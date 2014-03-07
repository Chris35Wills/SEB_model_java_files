package energymodels;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;


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
 * Radiation on a slope interval module. Requires a slope, aspect and hillshade 
 * surface to be in storage as well as values for tau, TOA, solar zenith and 
 * solar azimuth at midnight, 6am, noon and 6pm.
 * 
 * Required data:
 * 
 * Historical:          Year| Month| Day| Tau| TOA| Solar Zenith| Solar Azimuth 
 *                                          + aforementioned surfaces
 * Contemporary:        Year| Month| Day| Tau| TOA| Solar Zenith| Solar Azimuth 
 *                                          + aforementioned surfaces
 * 
 * The first test run will use a 1959 surface (therefore slope and aspect) and 
 * a 1959 June mean hillshade surface. The solar geometry and other values being
 * used will be for 21st June 2010 (as tau is not available pre-2007 yet)
 * 
 * NB1/ Everything has to be in radians when working with Java.Math
 * 
 * @author Chris 10/1/13 v 1.0
 */

public class Radiation_on_slope_interval_Listnr implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;
  
  Radiation_on_slope_interval_Listnr (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
      
   double slope[][], aspect[][], slope_RAD[][], aspect_RAD[][], 
           surfaceRadiationSurface[][];
   double hillshade[][], hillshade_jan[][], hillshade_feb[][], hillshade_mar[][]
           , hillshade_april[][], hillshade_may[][], hillshade_june[][], 
           hillshade_july[][], hillshade_aug[][], hillshade_sept[][], 
           hillshade_oct[][], hillshade_nov[][], hillshade_dec[][]; 
   double tau;
   double azimuth_midnight_radians, zenith_midnight_radians, TOA_midnight, 
           azimuth_midnight, zenith_midnight; //Midnight variables
   double azimuth_6am_radians, zenith_6am_radians, TOA_6am, azimuth_6am, 
           zenith_6am; //6am variables
   double azimuth_noon_radians, zenith_noon_radians, TOA_noon, azimuth_noon, 
           zenith_noon; //noon variables
   double azimuth_6pm_radians, zenith_6pm_radians, TOA_6pm, azimuth_6pm,
           zenith_6pm; //6pm variables
   double surface_Radiation;
   double surface_Radiation_midnight;
   double surface_Radiation_6am;
   double surface_Radiation_noon;
   double surface_Radiation_6pm;
   int NODATA_value = -9999;
                        
   //Gets the TOA, solar azimuth and solar zenith values for the different time 
   //itervals (if using this as a stand alone module then it will only access 
   //the last looped variables in the read in table - needs to be fully 
   //integrated within a loop of the model inout file

   TOA_midnight = store.getTOA_midnight() ;
   azimuth_midnight = store.getAzimuth_midnight() ;
   //System.out.println("Azimuth midnight (deg) = " + azimuth_midnight);
   zenith_midnight = store.getZenith_midnight();
   //System.out.println("Zenith midnight (deg) = " + zenith_midnight);
   
   TOA_6am = store.getTOA_6am() ;
   azimuth_6am = store.getAzimuth_6am() ;
   //System.out.println("Azimuth 6am (deg) = " + azimuth_6am);
   zenith_6am = store.getZenith_6am();
   //System.out.println("Zenith 6am (deg) = " + zenith_6am);
   
   TOA_noon = store.getTOA_noon() ;
   azimuth_noon = store.getAzimuth_noon() ;
   //System.out.println("Azimuth noon (deg) = " + azimuth_noon);
   zenith_noon = store.getZenith_noon();
   //System.out.println("Zenith noon (deg) = " + zenith_noon);
   
   TOA_6pm = store.getTOA_6pm() ;
   azimuth_6pm = store.getAzimuth_6pm() ;
   //System.out.println("Azimuth 6pm (deg) = " + azimuth_6pm);
   zenith_6pm = store.getZenith_6pm();
   //System.out.println("Zenith 6pm (deg) = " + zenith_6pm);
   
   //Gets the calculated slope and aspect surfaces   
   
   slope = store.getSlope(); // slope surface array
   aspect = store.getAspect(); // aspect surface array
   
   
   // Will ned to test for which month is uploaded <- possible once this is 
   // installed within the overall model inputs list loop:
   //
   // 1) Try to get all surfaces first....
   // 2) Print out which surfaces have been uploaded
   // 3) Run algorithm below according 
   //    to the month list file
   
//   try{
//   hillshade_jan = store.getHillshade();
//   } catch (Exception fnfe1){
//   System.out.println("No January Hillshade");
//   }
//   
//   
//   hillshade_feb = store.getHillshade_Feb();
//   hillshade_mar = store.getHillshade_March();
//   hillshade_april = store.getHillshade_April();
//   hillshade_may = store.getHillshade_May();
//   hillshade_june = store.getHillshade_June();
//   hillshade_july =store.getHillshade_July();
//   hillshade_aug = store.getHillshade_August();
//   hillshade_sept = store.getHillshade_Sept();
//   hillshade_oct = store.getHillshade_Oct();
//   hillshade_nov = store.getHillshade_Nov();
//   hillshade_dec = store.getHillshade_Dec();
   
   double elevation[][] = store.getElevation();
   hillshade = store.getHillshade(); // hillshade surface array 
      
   slope_RAD = new double[slope.length][slope[0].length];
   aspect_RAD = new double[slope.length][slope[0].length];
         
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
    
    // Calculate surface radiation surface considering aspect/slope/hillshade for interval time steps
   
   surfaceRadiationSurface = new double[slope.length][slope[0].length];
      
   tau = store.getTauEstimate();   
   
      for(int i = 0; i<slope_RAD.length; i++){
       for(int j = 0; j<slope_RAD[i].length; j++){
           
            surface_Radiation_midnight = 0;
            surface_Radiation_6am = 0;
            surface_Radiation_noon = 0;
            surface_Radiation_6pm = 0;
            surface_Radiation = 0;
   
   // Midnight radiation calculation
   //******************************************************************
   
                                                                // These would be taken from lists in the full
   azimuth_midnight_radians = Math.toRadians(azimuth_midnight);  // model but for this run just use a single 
   zenith_midnight_radians = Math.toRadians(zenith_midnight);    // value i.e. read in a one line input met  
                                                                // data file  
   
   //System.out.println("Azimuth midnight (rad) = " + azimuth_midnight_radians);
   //System.out.println("Zenith midnight (rad) = " + zenith_midnight_radians);
   
   // Start looping through surfaces and implement equation - this would be 
   // nested in an array list loop eventually
              
           //slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];
           //slope_RAD[i][j] = hillshade[i][j];
           
           double loop_slope1 = slope_RAD[i][j];
           double loop_aspect1 = aspect_RAD[i][j];
           double loop_hillshade1 = hillshade[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Use to set clearsky conditions (i.e. no topog. shading)
           
           if(loop_slope1 != NODATA_value && loop_aspect1 != NODATA_value && loop_hillshade1 != NODATA_value){
           
           //************** MODIFIED SURFACE RADIATION EQUATION
             surface_Radiation_midnight = (loop_hillshade1/255) * (((Math.cos(zenith_midnight_radians)*Math.cos(loop_slope1))
                   +(Math.sin(zenith_midnight_radians)*Math.sin(loop_slope1)*Math.cos(azimuth_midnight_radians - loop_aspect1)))*
                        TOA_midnight * Math.exp(-tau/Math.cos(zenith_midnight_radians)));
           
             }else{
               
            surface_Radiation_midnight = NODATA_value;
            }
 
           //******************************************************************
           
           // 6am radiation calculation
           //******************************************************************
   
                                                                // These would be taken from lists in the full
   azimuth_6am_radians = Math.toRadians(azimuth_6am);  // model but for this run just use a single 
   zenith_6am_radians = Math.toRadians(zenith_6am);    // value i.e. read in a one line input met  
                                                                // data file  
   
   //System.out.println("Azimuth 6am (rad) = " + azimuth_6am_radians);
   //System.out.println("Zenith 6am (rad) = " + zenith_6am_radians);
           
   // Start looping through surfaces and implement equation - this would be 
   // nested in an array list loop eventually
           
           //slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];
           //slope_RAD[i][j] = hillshade[i][j];
           
           double loop_slope2 = slope_RAD[i][j];
           double loop_aspect2 = aspect_RAD[i][j];
           double loop_hillshade2 = hillshade[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Use to set clearsky conditions (i.e. no topog. shading)
           
           if(loop_slope2 != NODATA_value && loop_aspect2 != NODATA_value && loop_hillshade2 != NODATA_value){
           
           //************** MODIFIED SURFACE RADIATION EQUATION
             surface_Radiation_6am = (loop_hillshade2/255) * (((Math.cos(zenith_6am_radians)*Math.cos(loop_slope2))
                   +(Math.sin(zenith_6am_radians)*Math.sin(loop_slope2)*Math.cos(azimuth_6am_radians - loop_aspect2)))*
                        TOA_6am * Math.exp(-tau/Math.cos(zenith_6am_radians)));
           
            }
           
           else
           {
           surface_Radiation_6am = NODATA_value;
           }
                
           //****************************************************************** 
           
           // noon radiation calculation  
           //****************************************************************** 
             
                                                        // These would be taken from lists in the full
   azimuth_noon_radians = Math.toRadians(azimuth_noon);  // model but for this run just use a single 
   zenith_noon_radians = Math.toRadians(zenith_noon);    // value i.e. read in a one line input met  
                                                        // data file  
   
   //System.out.println("Azimuth noon (rad) = " + azimuth_noon_radians);
   //System.out.println("Zenith noon (rad) = " + zenith_noon_radians);
           
   // Start looping through surfaces and implement equation - this would be 
   // nested in an array list loop eventually
              
           //slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];
           //slope_RAD[i][j] = hillshade[i][j];
           
           double loop_slope3 = slope_RAD[i][j];
           double loop_aspect3 = aspect_RAD[i][j];
           double loop_hillshade3 = hillshade[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Use to set clearsky conditions (i.e. no topog. shading)
           
           if(loop_slope3 != NODATA_value && loop_aspect3 != NODATA_value && loop_hillshade3 != NODATA_value){
           
           //************** MODIFIED SURFACE RADIATION EQUATION
             surface_Radiation_noon = (loop_hillshade3/255) * (((Math.cos(zenith_noon_radians)*Math.cos(loop_slope3))
                   +(Math.sin(zenith_noon_radians)*Math.sin(loop_slope3)*Math.cos(azimuth_noon_radians - loop_aspect3)))*
                        TOA_noon * Math.exp(-tau/Math.cos(zenith_noon_radians)));
           }
           
           else
           {
           surface_Radiation_noon = NODATA_value;
           }
           //****************************************************************** 
           
           // 6pm radiation calculation
           //****************************************************************** 
                        
                                                      // These would be taken from lists in the full
   azimuth_6pm_radians = Math.toRadians(azimuth_6pm);  // model but for this run just use a single 
   zenith_6pm_radians = Math.toRadians(zenith_6pm);    // value i.e. read in a one line input met  
                                                      // data file  
   
   //System.out.println("Azimuth 6pm (rad) = " + azimuth_6pm_radians);
   //System.out.println("Zenith 6pm (rad) = " + zenith_6pm_radians);
   
   // Start looping through surfaces and implement equation - this would be 
   // nested in an array list loop eventually
              
           //slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];
           //slope_RAD[i][j] = hillshade[i][j];
           
           double loop_slope4 = slope_RAD[i][j];
           double loop_aspect4 = aspect_RAD[i][j];
           double loop_hillshade4 = hillshade[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Use to set clearsky conditions (i.e. no topog. shading)
           
           if(loop_slope4 != NODATA_value && loop_aspect4 != NODATA_value && loop_hillshade4 != NODATA_value){
           
           //************** MODIFIED SURFACE RADIATION EQUATION
             surface_Radiation_6pm = (loop_hillshade4/255) * (((Math.cos(zenith_6pm_radians)*Math.cos(loop_slope4))
                   +(Math.sin(zenith_6pm_radians)*Math.sin(loop_slope4)*Math.cos(azimuth_6pm_radians - loop_aspect4)))*
                        TOA_6pm * Math.exp(-tau/Math.cos(zenith_6pm_radians)));
           
           }
           else
           {
           surface_Radiation_6pm = NODATA_value;
           }

           //****************************************************************** 
                   System.out.println("Just before the if rad loops");
          
           if((elevation[i][j] == NODATA_value))
			{
			System.out.println("No data populated for rad");
			surfaceRadiationSurface[i][j] = NODATA_value;
			store.setSurfaceRadiationSurface(surfaceRadiationSurface);
			System.out.println("surface_rad NODATA_value populated");
			}
       
           else if(elevation[i][j] != NODATA_value &((surface_Radiation_midnight != NODATA_value) & (surface_Radiation_6am != NODATA_value)) & ((surface_Radiation_noon != NODATA_value) & (surface_Radiation_6pm != NODATA_value))){
        
               surface_Radiation = (surface_Radiation_midnight + surface_Radiation_6am + surface_Radiation_noon + surface_Radiation_6pm)/4.0;
               
					if(surface_Radiation >= 0.000001){  
                                                System.out.println("Rad >= 0.000001 populated");
						surfaceRadiationSurface[i][j] = surface_Radiation;
						store.setSurfaceRadiationSurface(surfaceRadiationSurface);
           
					}
					else if((surface_Radiation < 0.000001) & (surface_Radiation != NODATA_value))
					{
                                                System.out.println("Rad < 0.000001 populated");
						surfaceRadiationSurface[i][j] = 0;
						store.setSurfaceRadiationSurface(surfaceRadiationSurface);
					}
				
			}
             
       }
      }
 
   
/**
* This bit will open a save box to save the slope layer as an 
* ASCII, using the coordinates of the opened up elevation surface
**/    
      
//// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
   
    FileDialog fd = new FileDialog(new Frame(), 
                                        "Save Surface Radiation (interval calculation) surface", FileDialog.SAVE);
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
    
   System.out.println("Surface radiation surface stored and saved: " + f.getAbsolutePath());
   
        }
   }
    
