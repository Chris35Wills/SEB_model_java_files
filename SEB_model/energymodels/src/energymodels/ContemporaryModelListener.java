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
* This is the compiled contemporary model.
* 
* Model Inputs;
* 
* Elevation surfaces: 
* F:\GIS_temp_back_up\Surfaces [MODEL INPUTS]
* 
* Thickness: 
* F:\GIS_temp_back_up\IceThickness\Contemp_thick
* F:\GIS_temp_back_up\Contemp_glacier_thickness
* F:\GIS_temp_back_up\ALL_glacier_thickness_(5m_resolution) [MODEL INPUTS] <--- instructions also on how to fix resolution
* 
* Snow depth: 
* F:\GIS_with_knowledge\Sweden\Field data\SnowProbe_data\.....
* 
* Hillshade:
* F:\GIS_temp_back_up\AreaGlacier_maps_for_hillshade_layers <-- Still being developed - see notes (average surfaces considering shadows)
* 
* Ensure that on repeat, the updated elevation and ice thickness surfaces are used
 */

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
import java.util.Iterator;

/**
 *
 * @author Chris
 */
public class ContemporaryModelListener implements ActionListener {
   
  private GUIPanel panel = null;
  private Storage store = null;
  
  ContemporaryModelListener (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
   
      //System.out.println("working");
      
      Iterator<Double> iterator0 = store.getDayList().iterator();
      Iterator<Double> iterator1 = store.getMonthFile().iterator();
      Iterator<Double> iterator2 = store.getYearFile().iterator();
      Iterator<Double> iterator3 = store.getTemperatureFile().iterator();
      Iterator<Double> iterator4 =  store.getPrecipFile().iterator();
      Iterator<Double> iterator4b =  store.getPrecipSeasonList().iterator();
      
      Iterator<Double> iterator5 = store.getTOA_midnight_list().iterator();
      Iterator<Double> iterator6 = store.getTOA_6am_list().iterator();
      Iterator<Double> iterator7 = store.getTOA_noon_list().iterator();
      Iterator<Double> iterator8 = store.getTOA_6pm_list().iterator();
      
      Iterator<Double> iterator9 = store.getAzimuth_midnight_list().iterator();
      Iterator<Double> iterator10 = store.getAzimuth_6am_list().iterator();
      Iterator<Double> iterator11 = store.getAzimuth_noon_list().iterator();
      Iterator<Double> iterator12 = store.getAzimuth_6pm_list().iterator();
            
      Iterator<Double> iterator13 = store.getZenith_midnight_list().iterator();
      Iterator<Double> iterator14 = store.getZenith_6am_list().iterator();
      Iterator<Double> iterator15 = store.getZenith_noon_list().iterator();
      Iterator<Double> iterator16 = store.getZenith_6pm_list().iterator();
      
      Iterator<Double> iterator17 = store.getTauEstimateList().iterator();
      
      int NODATA_value = -9999;
      
      // Loop counters 
      int Loop_counter = 0; //Must be initialized outside of loop
          
      System.out.println("Let the model.....begin!");
      
      File f_stats = new File("F:/Melt_modelling/Model_outputs/Model_run_mean_stats.txt"); 
      
      FileWriter fw_stats;
      DecimalFormat df_stats = new DecimalFormat("#.####");
    
      try
      {
      BufferedWriter bw_stats = new BufferedWriter(new FileWriter(f_stats));
      bw_stats.write("Loop number" + ", " + "Mean Radiation (Wm^-2)" + ", " + 
              "Mean Psi (Wm^-2)" + ", " + "Mean Q (Wm^-2)" + ", " + 
              "Mean glacier air temp. (deg C)" + ", " + "Mean snowfall (m)" + 
              ", " + "Mean surface melt (m)" + ", " 
              + "Mean volume change (m^3)" );
      
      bw_stats.newLine();
	 	  	  
	  // Beginning of the main model looping
	  
	  //bw_stats.write("Loop number" + ", " + "Mean Radiation (Wm^-2)" + ", " + "Mean Psi (Wm^-2)" + ", " + "Mean Q (Wm^-2)" + ", " + "Mean glacier air temp. (deg C)" + ", " + "Mean snowfall (m)" + ", " + "Mean surface melt (m)" );
	  
	  // End of model looping
	  	  
      while(iterator0.hasNext()){
      //if(iterator0.hasNext()){

    double elevation[][] = store.getElevation();
    store.setElevationSize(elevation); 
    //double Elevation_size[][] = null;
    double Elevation_size[][] = store.getElevationSize();

    double glacier_thickness[][] = store.getThickness(); //Only called once; following this, loops must use the updated thickness and elevation variables
    double summer_snow_thickness[][]; // Internally calculated as a function of wet precip. and laspe rate - winter snow will be added to this layer 
                    // (so that summer snow is not erradicated upon initiation of the winter precip. algorithm)
                    //double wintersnow_thickness[][] = store.getSnowThickness(); //User uploaded and accessed by winter precip. algorithm

    double glacier_thickness_temp[][] = glacier_thickness; // This gets a copy of glacier_thickness 
    store.setThickness_intermediary(glacier_thickness_temp); // This sets the copy of thickness, making a clone of it (i.e. it can't be affcted if you alter the "glacier_thickness" object
    double glacier_thickness_intermediary[][] = store.getThickness_intermediary(); //This gets the "clone" - so this will remain an unaltered thickness layer throughout the loop (unlike "glacierThickness[][]")

    double day = iterator0.next();
    double month = iterator1.next();
    double year = iterator2.next();
    double temp = iterator3.next();
    store.setTemp(temp);
    double precip =iterator4.next();
    double precip_season = iterator4b.next();

    double TOA_midnight = iterator5.next();
    double TOA_6am = iterator6.next();
    double TOA_noon = iterator7.next();
    double TOA_6pm = iterator8.next();

    double azimuth_midnight = iterator9.next();
    double azimuth_6am = iterator10.next();
    double azimuth_noon = iterator11.next();
    double azimuth_6pm = iterator12.next();

    double zenith_midnight = iterator13.next();
    double zenith_6am = iterator14.next();
    double zenith_noon = iterator15.next();
    double zenith_6pm = iterator16.next();

    double Tau_estimate = iterator17.next();

    System.out.println(day + "/" + month + "/" + year);
    //System.out.println("Everything set and good to go");
                        
    //Surface variables
    
    // double winter_snow[][] = store.getWinterSnow(); // See precipitation (contemp.) module for info - set/get methods for snow interpolations EXIST
    
    //Slope variables
    double Slope_Surface[][] = new double[Elevation_size.length][Elevation_size[0].length];
    double slopeA,slopeB,slopeC,slopeD,slopeE,slopeF,slopeG,slopeH,slopeI;
    double slope_dzdy, slope_dzdx;
    double slopeDegree;
    
    //Aspect variables
    double aspectSurface[][] = new double[Elevation_size.length][Elevation_size[0].length];
    double aspectA,aspectB,aspectC,aspectD,aspectE,aspectF,aspectG,aspectH,aspectI;
    double aspect_dzdy, aspect_dzdx;
    double aspect_pre;
    double aspectDegree;
    
    //Radiation variables
    double slope[][], aspect[][], slope_RAD[][], aspect_RAD[][], 
           surfaceRadiationSurface[][];
    double hillshade[][] = null; 
    double hillshade_midnight[][] = null; 
    double hillshade_6am[][] = null; 
    double hillshade_noon[][] = null; 
    double hillshade_6pm[][] = null; 
    double tau;
    double azimuth_midnight_radians, zenith_midnight_radians; //Midnight variables
    double azimuth_6am_radians, zenith_6am_radians; //6am variables
    double azimuth_noon_radians, zenith_noon_radians; //noon variables
    double azimuth_6pm_radians, zenith_6pm_radians; //6pm variables
    double surface_Radiation;
    double surface_Radiation_midnight;
    double surface_Radiation_6am;
    double surface_Radiation_noon;
    double surface_Radiation_6pm;
    
    //Bulk flux variables
    double psi_min = -25;
    double c = 8.7;
    double T_tip = -1.5;
    double cTa;
    double psi = 0;
    double lapsed_Ta;
    double lapsed_temp[][] = new double[Elevation_size.length][Elevation_size[0].length];
    double psi_surface[][] = new double[Elevation_size.length][Elevation_size[0].length];
    
    //Q calculation variables
    double albedo = 0.39;  //<<<<<------------- Currently Hardwired - this is dependednt on the snow layer thickness
    double Q_daily_grid[][] = new double[Elevation_size.length][Elevation_size[0].length];
    double I_for_Q;
    double psi_for_Q;
    double Q, Q_daily;
    double Q_counter = 0;
    double Q_total = 0;
    //double Q_value = 0;
    double mean_Q_m2;
    double mean_Q_5_x_m2;
    //double Q_per_cell_area; 
    double cell_area = 25.0;
    
    //Melt calculation variables
    
    double adjustment_counter = 0;
    double thickness_adjustment_total = 0;
    double volume_adjustment_total = 0;
    //double ice_thickness_change_counter = 0;
    //double ice_volume_change_counter = 0;
    double ice_thickness_surface_adjustment[][] = new double[Elevation_size.length][Elevation_size[0].length];
    double ice_thickness_change_surface[][] = new double[Elevation_size.length][Elevation_size[0].length];
    //double ice_volume_change_surface[][] = new double[Elevation_size.length][Elevation_size[0].length];
    //double ice_lowering[][] = new double[Elevation_size.length][Elevation_size[0].length];
    double latent_heat_of_fusion = 334000;
    double ice_density = 900.0;
    double snow_density = 400.0; // Check and change this value
    double ice_mass;
    double snow_mass;
    double energy_for_total_melt;
    double Q_available;
    double ice_thickness_change;
    //double thickness_change_base_level = 0;
    //double ice_thickness_change_total = 0;
    //double ice_volume_change_total = 0;
    //double ice_volume_change;
    
    //Counters (zeroed at the beginning of each loop)
    
    double Radiation_counter = 0;
    double Radiation_total = 0;
    double Psi_counter = 0;    
    double Psi_total = 0;
    double Lapsed_temp_counter = 0;    
    double Lapsed_temp_total = 0;
    double Snowfall_counter = 0;    
    double Snowfall_total = 0;
    
    /*
     * Slope calculation
     */
    
      for(int i =0; i<elevation.length; i++){
        for(int j = 0; j<elevation[i].length; j++){

          if(elevation[i][j] != NODATA_value){
               
              // Try/catch blocks exist in case of "out of bounds exceptions" 
              // which are likely when running the neighborhood searches at the 
              // array edges - this will be called upon less when using the 
              // larger arrays populated with No_Data (i.e. -9999.0) values
              //
              // These assign the values to letters A-I, according to positions 
              // within the array assuming the structure of:
              //   A   B   C
              //   D   E   F
              //   G   H   I
              
              try{    
              if((elevation[i-1][j-1] != NODATA_value)){
                  slopeA = elevation[i-1][j-1];
              } else{
                  slopeA = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe0){
                  slopeA = elevation[i][j];
              }   
                  
              try{
              if((elevation[i-1][j] != NODATA_value)){
                  slopeB = elevation[i-1][j];           
              } else {
                  slopeB = elevation[i][j];  
              }
              } catch(ArrayIndexOutOfBoundsException aiobe1){
                  slopeB = elevation[i][j];
              }   
              
              try{
              if((elevation[i-1][j+1] != NODATA_value)){
                  slopeC = elevation[i-1][j+1];
              } else {
                  //error1 = ("C fail");//
                  slopeC = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe2){
                  //error1 = ("C fail catch"); //
                  slopeC = elevation[i][j];
              } 
              
              try{
              if((elevation[i][j-1] != NODATA_value)) {
                  slopeD = elevation[i][j-1];
              } else {
                  slopeD = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe3){
                  slopeD = elevation[i][j];
              } 
              
              try{
              if((elevation[i][j] != NODATA_value)){
                  slopeE = elevation[i][j];
              } else {
                  slopeE = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe4){
                  slopeE = elevation[i][j];
              }
              
              try{
              if((elevation[i][j+1] != NODATA_value)) {
                  slopeF = elevation[i][j+1];
              } else {
                  slopeF = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe5){
                  slopeF = elevation[i][j];
              }
             
              try{
              if((elevation[i+1][j-1] != NODATA_value)){
                  slopeG = elevation[i+1][j-1];
              } else {
                  slopeG = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe6){
                  slopeG = elevation[i][j];
              }
    
              try{
              if((elevation[i+1][j] != NODATA_value)){
                  slopeH = elevation[i+1][j];
              } else {
                  slopeH = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe7){
                  slopeH = elevation[i][j];
              }
                
              try{
              if((elevation[i+1][j+1] != NODATA_value)){
                  slopeI = elevation[i+1][j+1];
              } else {
                  slopeI = elevation[i][j];
              }        
              } catch(ArrayIndexOutOfBoundsException aiobe8){
                  slopeI = elevation[i][j];
              }
              
          slope_dzdx = (((slopeC + (2*slopeF) + slopeI)-(slopeA + (2*slopeD)+slopeG))/(8*5));
          slope_dzdy = (((slopeG + (2*slopeH) + slopeI)-(slopeA + (2 * slopeB) + slopeC))/(8*5));
          slopeDegree = Math.atan(Math.sqrt((Math.pow(slope_dzdx,2) 
                                + Math.pow(slope_dzdy,2))))*(180/Math.PI);
          
          Slope_Surface[i][j] = slopeDegree;       
              
          } else if(elevation[i][j] == NODATA_value){

              Slope_Surface[i][j] = NODATA_value;
             
       }
              
      }       
      
  }
      
      store.setSlope(Slope_Surface);
      //System.out.println("Slope calculated");
                  
      /*
       * Aspect calculation
       */
      
      for(int i =0; i<elevation.length; i++){
      for(int j = 0; j<elevation[i].length; j++){

          if(elevation[i][j] != NODATA_value){
               
              // Try/catch blocks exist in case of "out of bounds exceptions" 
              // which are likely when running the neighborhood searches at the 
              // array edges - this will be called upon less when using the 
              // larger arrays populated with No_Data (i.e. -9999.0) values
              //
              // These assign the values to letters A-I, according to positions 
              // within the array assuming the structure of:
              //   A   B   C
              //   D   E   F
              //   G   H   I
              
              try{    
              if((elevation[i-1][j-1] != NODATA_value)){
                  aspectA = elevation[i-1][j-1];
              } else{
                  aspectA = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe0){
                  aspectA = elevation[i][j];
              }   
                  
              try{
              if((elevation[i-1][j] != NODATA_value)){
                  aspectB = elevation[i-1][j];           
              } else {
                  aspectB = elevation[i][j];  
              }
              } catch(ArrayIndexOutOfBoundsException aiobe1){
                  aspectB = elevation[i][j];
              }   
              
              try{
              if((elevation[i-1][j+1] != NODATA_value)){
                  aspectC = elevation[i-1][j+1];
              } else {
                  //error1 = ("C fail");//
                  aspectC = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe2){
                  //error1 = ("C fail catch"); //
                  aspectC = elevation[i][j];
              } 
              
              try{
              if((elevation[i][j-1] != NODATA_value)) {
                  aspectD = elevation[i][j-1];
              } else {
                  aspectD = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe3){
                  aspectD = elevation[i][j];
              } 
              
              try{
              if((elevation[i][j] != NODATA_value)){
                  aspectE = elevation[i][j];
              } else {
                  aspectE = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe4){
                  aspectE = elevation[i][j];
              }
              
              try{
              if((elevation[i][j+1] != NODATA_value)) {
                  aspectF = elevation[i][j+1];
              } else {
                  aspectF = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe5){
                  aspectF = elevation[i][j];
              }
             
              try{
              if((elevation[i+1][j-1] != NODATA_value)){
                  aspectG = elevation[i+1][j-1];
              } else {
                  aspectG = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe6){
                  aspectG = elevation[i][j];
              }
    
              try{
              if((elevation[i+1][j] != NODATA_value)){
                  aspectH = elevation[i+1][j];
              } else {
                  aspectH = elevation[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe7){
                  aspectH = elevation[i][j];
              }
                
              try{
              if((elevation[i+1][j+1] != NODATA_value)){
                  aspectI = elevation[i+1][j+1];
              } else {
                  aspectI = elevation[i][j];
              }        
              } catch(ArrayIndexOutOfBoundsException aiobe8){
                  aspectI = elevation[i][j];
              }
              
         // Breaks down the aspect algorithm into a few different steps
          
          aspect_dzdx = (((aspectC + (2*aspectF) + aspectI)-(aspectA + (2*aspectD) + aspectG))/(8));
          aspect_dzdy = (((aspectG + (2*aspectH) + aspectI)-(aspectA + (2 * aspectB) + aspectC))/(8));
          
          aspect_pre = ((180/Math.PI) * Math.atan2(aspect_dzdy, -aspect_dzdx));
          
          // Insert if loop to convert aspect to compass direction (degrees)
          
          if(aspect_pre < 0){
           aspectDegree = (90.0 - aspect_pre);
          } else if(aspect_pre > 90.0){
           aspectDegree = (360.0 - aspect_pre + 90.0);
          }else{
           aspectDegree = (90.0 - aspect_pre);
          }
                   
          // Sets the aspect value in the appropriate position in the 
          // aspectSurface array
          
          aspectSurface[i][j] = aspectDegree;   
              
          } else {

               aspectSurface[i][j] = NODATA_value;
       }
             
       }
              
      }       
      
      store.setAspect(aspectSurface);
      //System.out.println("Aspect calculated");
            
      /*
       * Radiation Algorithm
       */
      
      //Gets the TOA, solar azimuth and solar zenith values for the different time 
   //itervals (if using this as a stand alone module then it will only access 
   //the last looped variables in the read in table - needs to be fully 
   //integrated within a loop of the model inout file

//   TOA_midnight = store.getTOA_midnight() ;
//   azimuth_midnight = store.getAzimuth_midnight() ;
//   zenith_midnight = store.getZenith_midnight();
      
//   TOA_6am = store.getTOA_6am() ;
//   azimuth_6am = store.getAzimuth_6am() ;
//   zenith_6am = store.getZenith_6am();
   
//   TOA_noon = store.getTOA_noon() ;
//   azimuth_noon = store.getAzimuth_noon() ;
//   zenith_noon = store.getZenith_noon();
   
//   TOA_6pm = store.getTOA_6pm() ;
//   azimuth_6pm = store.getAzimuth_6pm() ;
//   zenith_6pm = store.getZenith_6pm();
      
   //Gets the calculated slope and aspect surfaces   
   
   slope = store.getSlope(); // slope surface array
   aspect = store.getAspect(); // aspect surface array
   
   
//    Will ned to test for which month is uploaded <- possible once this is 
//    installed within the overall model inputs list loop:
//   
//    1) Try to get all surfaces first....
//    2) Print out which surfaces have been uploaded
//    3) Run algorithm below according 
//       to the month list file
   
      if(month == 1.0){
       
   //hillshade = store.getHillshade_Jan();
   hillshade_midnight = store.getHillshade_Jan_midnight();
   hillshade_6am = store.getHillshade_Jan_6am();
   hillshade_noon = store.getHillshade_Jan_noon();
   hillshade_6pm = store.getHillshade_Jan_6pm();
     
   if(hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null){
   //System.out.println("January hillshade uploaded into model");
   //System.out.println("Month: " + month);
   } 
   else if(hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null){
   System.out.println("January hillshade not uploaded");
   }
   
   } else if(month == 2.0){
       
   //hillshade = store.getHillshade_Feb();
   hillshade_midnight = store.getHillshade_Feb_midnight();
   hillshade_6am = store.getHillshade_Feb_6am();
   hillshade_noon = store.getHillshade_Feb_noon();
   hillshade_6pm = store.getHillshade_Feb_6pm();
  
   if(hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null){
   //System.out.println("February hillshade uploaded into model");
   //System.out.println("Month: " + month);
   } 
   else if(hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null){
   System.out.println("February hillshade not uploaded");
   }
   } else if(month == 3.0){
       
   //hillshade = store.getHillshade_March();
   hillshade_midnight = store.getHillshade_March_midnight();
   hillshade_6am = store.getHillshade_March_6am();
   hillshade_noon = store.getHillshade_March_noon();
   hillshade_6pm = store.getHillshade_March_6pm();
           
   if(hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null){
//   System.out.println("March hillshade uploaded into model");
//   System.out.println("Month: " + month);
   } 
   else if(hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null){
   System.out.println("March hillshade not uploaded");
   }
   
   } else if(month == 4.0){
       
   //hillshade = store.getHillshade_April();
   hillshade_midnight = store.getHillshade_April_midnight();
   hillshade_6am = store.getHillshade_April_6am();
   hillshade_noon = store.getHillshade_April_noon();
   hillshade_6pm = store.getHillshade_April_6pm();
  
   if(hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null){
//   System.out.println("April hillshade uploaded into model");
//   System.out.println("Month: " + month);
   } 
   else if(hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null){
   System.out.println("April hillshade not uploaded");
   }
   
   } else if(month == 5.0){
       
   //hillshade = store.getHillshade_May();
   hillshade_midnight = store.getHillshade_May_midnight();
   hillshade_6am = store.getHillshade_May_6am();
   hillshade_noon = store.getHillshade_May_noon();
   hillshade_6pm = store.getHillshade_May_6pm();
  
    if(hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null){
//   System.out.println("May hillshade uploaded into model");
//   System.out.println("Month: " + month);
   } 
   else if(hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null){
   System.out.println("May hillshade not uploaded");
   }
    
   } else if(month == 6.0){
       
   //hillshade = store.getHillshade_June();
   hillshade_midnight = store.getHillshade_June_midnight();
   hillshade_6am = store.getHillshade_June_6am();
   hillshade_noon = store.getHillshade_June_noon();
   hillshade_6pm = store.getHillshade_June_6pm();
  
    if(hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null){
//   System.out.println("June hillshade uploaded into model");
//   System.out.println("Month: " + month);
   } 
   else if(hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null){
   System.out.println("June hillshade not uploaded");
   }
    
   } else if(month == 7.0){
       
   //hillshade = store.getHillshade_July();
   hillshade_midnight = store.getHillshade_July_midnight();
   hillshade_6am = store.getHillshade_July_6am();
   hillshade_noon = store.getHillshade_July_noon();
   hillshade_6pm = store.getHillshade_July_6pm();
  
   if(hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null){
//   System.out.println("July hillshade uploaded into model");
//   System.out.println("Month: " + month);
   } 
   else if(hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null){
   System.out.println("July hillshade not uploaded");
   }
   
   } else if(month == 8.0){
       
   //hillshade = store.getHillshade_August();
   hillshade_midnight = store.getHillshade_August_midnight();
   hillshade_6am = store.getHillshade_August_6am();
   hillshade_noon = store.getHillshade_August_noon();
   hillshade_6pm = store.getHillshade_August_6pm();
  
   if(hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null){
//   System.out.println("August hillshade uploaded into model");
//   System.out.println("Month: " + month);
   } 
   else if(hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null){
   System.out.println("August hillshade not uploaded");
   }
   
   } else if(month == 9.0){
       
   //hillshade = store.getHillshade_Sept();
   hillshade_midnight = store.getHillshade_Sept_midnight();
   hillshade_6am = store.getHillshade_Sept_6am();
   hillshade_noon = store.getHillshade_Sept_noon();
   hillshade_6pm = store.getHillshade_Sept_6pm();
  
   if(hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null){
//   System.out.println("September hillshade uploaded into model");
//   System.out.println("Month: " + month);
   } 
   else if(hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null){
   System.out.println("September hillshade not uploaded");
   }
   
   } else if(month == 10.0){
       
   //hillshade = store.getHillshade_Oct();
   hillshade_midnight = store.getHillshade_Oct_midnight();
   hillshade_6am = store.getHillshade_Oct_6am();
   hillshade_noon = store.getHillshade_Oct_noon();
   hillshade_6pm = store.getHillshade_Oct_6pm();
  
   if(hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null){
//   System.out.println("October hillshade uploaded into model");
//   System.out.println("Month: " + month);
   } 
   else if(hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null){
   System.out.println("October hillshade not uploaded");
   }
   
   } else if(month == 11.0){
       
   //hillshade = store.getHillshade_Nov();
   hillshade_midnight = store.getHillshade_Nov_midnight();
   hillshade_6am = store.getHillshade_Nov_6am();
   hillshade_noon = store.getHillshade_Nov_noon();
   hillshade_6pm = store.getHillshade_Nov_6pm();
  
   if(hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null){
//   System.out.println("November hillshade uploaded into model");
//   System.out.println("Month: " + month);
   } 
   else if(hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null){
   System.out.println("November hillshade not uploaded");
   }
   
   } else if(month == 12.0){
       
   //hillshade = store.getHillshade_Dec();
   hillshade_midnight = store.getHillshade_Dec_midnight();
   hillshade_6am = store.getHillshade_Dec_6am();
   hillshade_noon = store.getHillshade_Dec_noon();
   hillshade_6pm = store.getHillshade_Dec_6pm();
  
   if(hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null){
//   System.out.println("December hillshade uploaded into model");
//   System.out.println("Month: " + month);
   } 
   else if(hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null){
   System.out.println("December hillshade not uploaded");
   }
   }
   
   slope_RAD = new double[slope.length][slope[0].length];
   aspect_RAD = new double[slope.length][slope[0].length];
         
   //Converts the slope and aspect surfaces to radians
   
   for(int a = 0; a< Slope_Surface.length; a++){
      for (int b = 0; b< Slope_Surface[a].length; b++){
          
          if(slope[a][b] != NODATA_value){
          
          double slope_initial_value = Slope_Surface[a][b];
          double slope_radians = Math.toRadians(slope_initial_value);
          slope_RAD[a][b] = slope_radians;
          
          } else {
          
          slope_RAD[a][b] = NODATA_value;
                    
          }
          
          if(aspectSurface[a][b] != NODATA_value){
          
          double aspect_initial_value = aspectSurface[a][b];
          double aspect_radians = Math.toRadians(aspect_initial_value);
          aspect_RAD[a][b] = aspect_radians;
          
          } else {
          
          aspect_RAD[a][b] = NODATA_value;
              
          }
      }
   }    
    
    // Calculate surface radiation surface considering aspect/slope/hillshade for interval time steps
   
   surfaceRadiationSurface = new double[elevation.length][elevation[0].length];
      
   tau = store.getTauEstimate();   
   
      for(int i = 0; i<slope_RAD.length; i++){
       for(int j = 0; j<slope_RAD[i].length; j++){
           
            surface_Radiation_midnight = 0;
            surface_Radiation_6am = 0;
            surface_Radiation_noon = 0;
            surface_Radiation_6pm = 0;
   
   // Midnight radiation calculation
   //******************************************************************
   
                                                                // These would be taken from lists in the full
   azimuth_midnight_radians = Math.toRadians(azimuth_midnight);  // model but for this run just use a single 
   zenith_midnight_radians = Math.toRadians(zenith_midnight);    // value i.e. read in a one line input met  
                                                                // data file  
   
   // Start looping through surfaces and implement equation - this would be 
   // nested in an array list loop eventually
              
   //        slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];
           
           
           double loop_slope1 = slope_RAD[i][j];
           double loop_aspect1 = aspect_RAD[i][j];
           double loop_hillshade_midnight = hillshade_midnight[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Use to set clearsky conditions (i.e. no topog. shading)
           
           if(((loop_slope1 != NODATA_value) & (loop_aspect1 != NODATA_value)) & (loop_hillshade_midnight != NODATA_value)){
           
           //************** MODIFIED SURFACE RADIATION EQUATION
             //  surface_Radiation_midnight = ((100-loop_hillshade1)/100) * (((Math.cos(zenith_midnight_radians)*Math.cos(loop_slope1))
             //surface_Radiation_midnight = (1) * (((Math.cos(zenith_midnight_radians)*Math.cos(loop_slope1))
               surface_Radiation_midnight = (loop_hillshade_midnight/255.0) * (((Math.cos(zenith_midnight_radians)*Math.cos(loop_slope1))
                   +(Math.sin(zenith_midnight_radians)*Math.sin(loop_slope1)*Math.cos(azimuth_midnight_radians - loop_aspect1)))*
                        TOA_midnight * Math.exp(-tau/Math.cos(zenith_midnight_radians)));
             
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
                      
           //slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];
                      
           double loop_slope2 = slope_RAD[i][j];
           double loop_aspect2 = aspect_RAD[i][j];
           double loop_hillshade_6am = hillshade_6am[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Use to set clearsky conditions (i.e. no topog. shading)
           
           if(((loop_slope2 != NODATA_value) & (loop_aspect2 != NODATA_value)) & (loop_hillshade_6am != NODATA_value)){
           
           //************** MODIFIED SURFACE RADIATION EQUATION
             //  surface_Radiation_6am = ((100-loop_hillshade2)/100) * (((Math.cos(zenith_6am_radians)*Math.cos(loop_slope2))
            //surface_Radiation_6am = (1) * (((Math.cos(zenith_6am_radians)*Math.cos(loop_slope2))
             surface_Radiation_6am = (loop_hillshade_6am/255.0) * (((Math.cos(zenith_6am_radians)*Math.cos(loop_slope2))
                     +(Math.sin(zenith_6am_radians)*Math.sin(loop_slope2)*Math.cos(azimuth_6am_radians - loop_aspect2)))*
                        TOA_6am * Math.exp(-tau/Math.cos(zenith_6am_radians)));
           
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
                         
           //slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];
                      
           double loop_slope3 = slope_RAD[i][j];
           double loop_aspect3 = aspect_RAD[i][j];
           double loop_hillshade_noon = hillshade_noon[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Use to set clearsky conditions (i.e. no topog. shading)
           
          if(((loop_slope3 != NODATA_value) & (loop_aspect3 != NODATA_value)) & (loop_hillshade_noon != NODATA_value)){
              
           //if(zenith_noon <= 84.0){ // Prevents development of rogue values from discontinuity between zenith/azimuth/TOA at low sun altitudes - note that this referes to the zenith in degrees
               
           //************** MODIFIED SURFACE RADIATION EQUATION
//             surface_Radiation_noon = ((100-loop_hillshade3)/100) * (((Math.cos(zenith_noon_radians)*Math.cos(loop_slope3))   
             //surface_Radiation_noon = (1) * (((Math.cos(zenith_noon_radians)*Math.cos(loop_slope3))
              surface_Radiation_noon = (loop_hillshade_noon/255.0) * (((Math.cos(zenith_noon_radians)*Math.cos(loop_slope3))   
                   +(Math.sin(zenith_noon_radians)*Math.sin(loop_slope3)*Math.cos(azimuth_noon_radians - loop_aspect3)))*
                        TOA_noon * Math.exp(-tau/Math.cos(zenith_noon_radians)));
             
//              } else if (zenith_noon > 84.0)
//          
//          {
//          surface_Radiation_noon = 0.0;
//          }
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
   
   //        slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];
   
           
           double loop_slope4 = slope_RAD[i][j];
           double loop_aspect4 = aspect_RAD[i][j];
           double loop_hillshade_6pm = hillshade_6pm[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Use to set clearsky conditions (i.e. no topog. shading)
           
           if(((loop_slope4 != NODATA_value) & (loop_aspect4 != NODATA_value)) & (loop_hillshade_6pm != NODATA_value)){
           
           //************** MODIFIED SURFACE RADIATION EQUATION
             
             //surface_Radiation_6pm = (1) * (((Math.cos(zenith_6pm_radians)*Math.cos(loop_slope4))
               surface_Radiation_6pm = (loop_hillshade_6pm/255.0) * (((Math.cos(zenith_6pm_radians)*Math.cos(loop_slope4))  
                   +(Math.sin(zenith_6pm_radians)*Math.sin(loop_slope4)*Math.cos(azimuth_6pm_radians - loop_aspect4)))*
                        TOA_6pm * Math.exp(-tau/Math.cos(zenith_6pm_radians)));
             
             }

           //****************************************************************** 
         // System.out.println("Just before the if rad loops");
          
           if((elevation[i][j] == NODATA_value))
			{
	//		System.out.println("No data populated for rad");
			surfaceRadiationSurface[i][j] = NODATA_value;
			store.setSurfaceRadiationSurface(surfaceRadiationSurface);
	//		System.out.println("surface_rad NODATA_value populated");
			}
       
           else if((elevation[i][j] != NODATA_value) &((surface_Radiation_midnight != NODATA_value) & (surface_Radiation_6am != NODATA_value)) & ((surface_Radiation_noon != NODATA_value) & (surface_Radiation_6pm != NODATA_value))){
        
               surface_Radiation = (surface_Radiation_midnight + surface_Radiation_6am + surface_Radiation_noon + surface_Radiation_6pm)/4.0;
               
					if(surface_Radiation >= 0.000001){  
          //                                      System.out.println("Rad >= 0.000001 populated");
						surfaceRadiationSurface[i][j] = surface_Radiation;
						store.setSurfaceRadiationSurface(surfaceRadiationSurface);
           
					}
					else if((surface_Radiation < 0.000001) & (surface_Radiation != NODATA_value))
					{
            //                                    System.out.println("Rad < 0.000001 populated");
						surfaceRadiationSurface[i][j] = 0;
						store.setSurfaceRadiationSurface(surfaceRadiationSurface);
					}
			}
       }
      }
      
      //System.out.println("Surface radiation calculated and radiation surface set");
      
       /*
       * Bulk flux calculation
       */
 
      for(int i = 0; i<elevation.length; i++){
         for(int j = 0; j<elevation[i].length; j++){
             
            double Elevation_for_bulk_flux = elevation[i][j];

                if (Elevation_for_bulk_flux != NODATA_value)
                {
                lapsed_temp[i][j] = (double) LapseRate(Elevation_for_bulk_flux, temp);
                lapsed_Ta = lapsed_temp[i][j];
                
                     // Calculate value of psi [INTEGRATE THIS INTO ABOVE LOOP]
                        if(lapsed_Ta >= T_tip){
                            cTa = (c * lapsed_Ta);
                            psi = psi_min + cTa;
                            psi_surface[i][j] = psi;
                                     
                        } else if (lapsed_Ta < T_tip){
                            psi = psi_min;
                            psi_surface[i][j] = psi;
                        } 
                } 
                
                else if (Elevation_for_bulk_flux == NODATA_value)
                {
                lapsed_temp[i][j] = (int) NODATA_value;
                psi = NODATA_value;
                psi_surface[i][j] = NODATA_value;
                }
                            
                            store.setPsi(psi);
                            store.setPsiSurface(psi_surface);
                            store.setLapsedTempSurface(lapsed_temp);
       
         }
     }
      
      //System.out.println("Bulk flux calculated");
      
      /*
       * Q calculation
       */
      
//    double[][] Q_daily_grid = new double[elevation.length][elevation[0].length];
//    double I_for_Q;
//    double psi_for_Q;
//    double Q, Q_daily;
//    double Q, Q_daily, mean_Q;
//    double Q_counter = 0;
      
       for(int i = 0; i<elevation.length; i++){
        for(int j = 0; j<elevation[i].length; j++){
   
            I_for_Q = store.getSurfaceRadiationSurface()[i][j];
            psi_for_Q = store.getPsiSurface()[i][j];
                        
            if((I_for_Q != NODATA_value) & (psi_for_Q != NODATA_value)){
                            
            Q = ((1-albedo)*I_for_Q) + psi_for_Q; //<<<----- This gives a value of Q per m^2 - the actual cell area (5m x 5m) or full days time has not been considered yet 
            Q_daily = (Q * 86400) * cell_area; // Multiplies Q value by the number of seconds in a day and scales it up to the cell area (so if a 5m x 5m cell, multiply Q by 25)
            //System.out.println("Q daily: "  + Q_daily);
            Q_daily_grid[i][j] = Q_daily; // Sets the Q_daily value in the Q_daily_grid
            Q_counter++; // Counts instances of Q where != NODATA_value
            Q_total += Q_daily; // Total value of scaled up Q for a day through each full model loop
            
            } else if((I_for_Q == NODATA_value) | (psi_for_Q == NODATA_value)) {

            Q_daily_grid[i][j] = NODATA_value;

            }

            store.setQdailySurface(Q_daily_grid);
            
        }
      }
     
     mean_Q_5_x_m2 = Q_total/Q_counter; //<<---- Mean energy for a 5m x 5m cell (so units are W x 5m^-2)
     mean_Q_m2 = (Q_total/Q_counter)/25.0; //<<<---- Mean energy for a 1m x 1m cell (so units are Wm^-2)
     
     System.out.println("Q calculated: " + mean_Q_5_x_m2 + " W x 5m^-2"); //<<---- Mean energy for a 5m x 5m cell (so units are W x 5m^-2)
     System.out.println("Q calculated: " + mean_Q_m2 + " W x m^-2"); //<<<---- Mean energy for a 1m x 1m cell (so units are Wm^-2)
     
     /**
       * Surface melting
       * 30/1/13: Currently disregards precipitation ergo snow melt 
       **/  
     
     for(int i = 0; i<glacier_thickness.length; i++){
        for(int j = 0; j<glacier_thickness[i].length; j++){
            
            double Q_for_melting[][] = store.getQdailySurface();
      
// *******Must loop through the summer snow layer first before you consider ice - this has implications for density change*******
            
    if((((glacier_thickness[i][j] != NODATA_value) & (Q_for_melting[i][j] != NODATA_value))) & (((glacier_thickness[i][j] > 0)) & ((Q_for_melting[i][j] > 0)))){
                
            double thickness = glacier_thickness[i][j];
            //System.out.println("Thickness: " + thickness + "m");
            double ice_volume = thickness * cell_area;
            //ice_mass = thickness * ice_density; //<---- Whoops.... BAD MISTAKE TO MAKE!!!
            ice_mass = ice_volume * ice_density;
            energy_for_total_melt = ice_mass * latent_heat_of_fusion;
            //System.out.println("Total energy for melt: " + energy_for_total_melt);
            
            Q_available = Q_for_melting[i][j]; 
            //System.out.println("Q available: " + Q_available);
            
            //if(Q_available > 0)
            //{ 
            ice_thickness_change = (Q_available/energy_for_total_melt) * thickness; // Calculates a ratio of available melt to required melt and multiplies by thickness to make the appropriate reduction
            ice_thickness_change_surface[i][j] = ice_thickness_change; // Populates thickness change grid
            //}
            
            //else
            //{
            //ice_thickness_change = 0;
            //}           
            //ice_thickness_change_surface[i][j] = ice_thickness_change;
            }
    
            else if ((glacier_thickness[i][j] == 0) | (Q_for_melting[i][j] <= 0)) // Required so that if glacier_thickness is already zero or there is no energy, then nothing is done
            {
            ice_thickness_change = 0;
            ice_thickness_change_surface[i][j] = 0;
            }
            
            else if((glacier_thickness[i][j] == NODATA_value) & (Q_for_melting[i][j] != NODATA_value))
            {   
            ice_thickness_change_surface[i][j] = 0;
            } 
            
            else if((glacier_thickness[i][j] == NODATA_value) | (Q_for_melting[i][j] == NODATA_value))
            {   
            ice_thickness_change_surface[i][j] = NODATA_value;
            } 
            else 
            {
            }
        }
      }
      
      store.setIce_thickness_change_surface(ice_thickness_change_surface); // Surface set of how much has melted
 
      /**
       * Updates thickness surface and re-sets it for use at the top of the loop. 
       * Programmed so the thickness surface can never be less than 0.
       **/
              
      for(int i = 0; i<glacier_thickness.length; i++){
        for(int j = 0; j<glacier_thickness[i].length; j++){
            
            if(glacier_thickness[i][j] == 0)
            {
            glacier_thickness[i][j] = 0;
            }
            else if(((glacier_thickness[i][j] != NODATA_value) & (ice_thickness_change_surface[i][j] != NODATA_value)) & ((glacier_thickness[i][j] - ice_thickness_change_surface[i][j] >= 0)))
            {
            glacier_thickness[i][j] = glacier_thickness[i][j] - ice_thickness_change_surface[i][j];
                        
            } else if (((glacier_thickness[i][j] != NODATA_value) & (ice_thickness_change_surface[i][j] != NODATA_value)) & ((glacier_thickness[i][j] - ice_thickness_change_surface[i][j] < 0))) // Prevents negative ice depths from being calculated
            {
            glacier_thickness[i][j] = 0;
            } 
            else if ((glacier_thickness[i][j] == NODATA_value) | (ice_thickness_change_surface[i][j] == NODATA_value))
            {
            glacier_thickness[i][j] = NODATA_value;
            }
         }
        }
      
      store.setThickness(glacier_thickness);
      glacier_thickness = store.getThickness();
      
      /**
       * Loops through ice_thickness_surface_adjustment and populates it with 
       * values equal to glacier_thickness_intermediary - glacier_thickness. 
       * The output of this is a correct calculation of actual surface melt).
       **/
      
      for(int i = 0; i<ice_thickness_surface_adjustment.length; i++){
          for(int j = 0; j<ice_thickness_surface_adjustment[i].length; j++){
          
              if((glacier_thickness[i][j] == NODATA_value) | (glacier_thickness_intermediary[i][j] == NODATA_value))
              {
              ice_thickness_surface_adjustment[i][j] = NODATA_value;
              }
              else if((glacier_thickness[i][j] != NODATA_value) & (glacier_thickness_intermediary[i][j] != NODATA_value))
              {
              ice_thickness_surface_adjustment[i][j] =  glacier_thickness_intermediary[i][j] - glacier_thickness[i][j]; 
              
              // Accumulation of adjustment values to give daily mean melt
              double thickness_adjustment_instance = ice_thickness_surface_adjustment[i][j];
              thickness_adjustment_total += thickness_adjustment_instance; // Accumulates all thickness change
              adjustment_counter++; // Counts instances of glacier thickness where != NODATA_value
              
              double ice_volume_adjustment = thickness_adjustment_instance * cell_area;
              volume_adjustment_total += ice_volume_adjustment; // Accumulates all thickness change
              
              }
          }
      }
      
      DecimalFormat df_mean_melt = new DecimalFormat("0.00");
      double Daily_mean_ice_thickness_change = thickness_adjustment_total/adjustment_counter;
      double Daily_mean_ice_volume_change = volume_adjustment_total/adjustment_counter;
      //System.out.println("Daily mean thickness change: " + df_mean_melt.format(Daily_mean_ice_thickness_change) + "m");
      //System.out.println("Daily mean volume change: " + Daily_mean_ice_volume_change + "m^3");
      
//**
/* This bit will open a save box to save the surfaceRadiationSurface + the loop number 
/* as an ASCII, using the coordinates of the originally opened up elevation surface
**/    
      
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//    
      
      //if((Loop_counter == 1)){

//     File f = new File("F:/Melt_modelling/Model_outputs/surfaceRadiationSurface" + (int)day + "." + (int)month + "." + (int)year + ".txt"); 
//     //File f = new File("F:/Melt_modelling/Model_outputs/Q_daily_grid" + Loop_counter + ".txt"); 
//      
////      
//    FileWriter fw;
//    DecimalFormat df = new DecimalFormat("#.####");
////
//    try
//    {
//     BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//      bw.write("ncols" + "         " + store.getOriginalNcols()); 
//      bw.write(System.getProperty("line.separator"));
//      bw.write("nrows" + "         " + store.getOriginalNrows()); 
//      bw.write(System.getProperty("line.separator"));
//      bw.write("xllcorner" + "     " + store.getOriginalXllcorner());
//      bw.write(System.getProperty("line.separator"));
//      bw.write("yllcorner" + "     " + store.getOriginalYllcorner());
//      bw.write(System.getProperty("line.separator"));
//      bw.write("cellsize" + "      " + store.getOriginalCellsize()); 
//      bw.write(System.getProperty("line.separator"));
//      bw.write("NODATA_value" + "  " + "-9999");
//      bw.write(System.getProperty("line.separator"));
//
//      /**
//       *   Write out the array data
//       **/
//    
//      String tempStr = "";
//
//      for (int a = 0; a < surfaceRadiationSurface.length; a++)
//      {
//        for (int b = 0; b < surfaceRadiationSurface[a].length; b++)
//        {
//
//          if (surfaceRadiationSurface[a][b] == NODATA_value)
//          {
//
//            bw.write("-9999 ");
//          }
//          else
//          {
//
//            bw.write(df.format(surfaceRadiationSurface[a][b]) + " ");
//
//          }
//
//        }
//        bw.newLine();
//      }
//
//      bw.close();
//
//    }
//    catch (IOException ioe)
//    {
////
//      ioe.printStackTrace();
////
//    }
//    
//        System.out.println("surfaceRadiationSurface: " + f.getAbsolutePath());
     //System.out.println("Q surface: " + f.getAbsolutePath());

      //} else{}
//    
   ////System.out.println("Updated glacier thickness surface stored and saved: " + f.getAbsolutePath());
  //// System.out.println("Updated ice_thickness_surface_adjustment stored and saved: " + f.getAbsolutePath());
 //// System.out.println("ice_thickness_change_surface stored and saved: " + f.getAbsolutePath());
 //  System.out.println("surfaceRadiationSurface stored and saved: " + f.getAbsolutePath());

        /**
        * Updates elevation
        * surface and re-sets it for use at the top of the loop
        **/
      
      for(int i = 0; i<elevation.length; i++){
        for(int j = 0; j<elevation[i].length; j++){
            
            if((elevation[i][j] != NODATA_value) & (ice_thickness_surface_adjustment[i][j] != NODATA_value))
            {
            elevation[i][j] = elevation[i][j] - ice_thickness_surface_adjustment[i][j];        
            } 
            else if ((elevation[i][j] == NODATA_value) | (ice_thickness_surface_adjustment[i][j] == NODATA_value))
            {
            elevation[i][j] = NODATA_value;
            }
        }
      }
      
      store.setElevation(elevation);
      elevation = store.getElevation();
      
/**
* This bit will open a save box to save the updated thickness surface 
* as an ASCII, using the coordinates of the originally opened up elevation 
* surface
**/    
      
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
    //File f2 = new File("F:/Melt_modelling/Model_outputs/glacier_thickness_update.txt");
    File f2 = new File("F:/Melt_modelling/Model_outputs/surfaceRadiationSurface_" + (int)day + "." + (int)month + "." + (int)year + ".txt"); 
       
    FileWriter fw2;
    DecimalFormat df2 = new DecimalFormat("#.####");

    try
    {
      BufferedWriter bw2 = new BufferedWriter(new FileWriter(f2));
      bw2.write("ncols" + "         " + store.getOriginalNcols()); 
      bw2.write(System.getProperty("line.separator"));
      bw2.write("nrows" + "         " + store.getOriginalNrows()); 
      bw2.write(System.getProperty("line.separator"));
      bw2.write("xllcorner" + "     " + store.getOriginalXllcorner());
      bw2.write(System.getProperty("line.separator"));
      bw2.write("yllcorner" + "     " + store.getOriginalYllcorner());
      bw2.write(System.getProperty("line.separator"));
      bw2.write("cellsize" + "      " + store.getOriginalCellsize()); 
      bw2.write(System.getProperty("line.separator"));
      bw2.write("NODATA_value" + "  " + "-9999");
      bw2.write(System.getProperty("line.separator"));

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

            bw2.write("-9999 ");
          }
          else
          {

            bw2.write(df2.format(surfaceRadiationSurface[a][b]) + " ");

          }

        }
        bw2.newLine();
      }

      bw2.close();

    }
    catch (IOException ioe)
    {

      ioe.printStackTrace();

    }
    
   //System.out.println("Updated elevation surface stored and saved: " + f2.getAbsolutePath());
    //System.out.println("glacier_thickness surface stored and saved: " + f2.getAbsolutePath());
    System.out.println("surfaceRadiationSurface: " + f2.getAbsolutePath());

///**
//* This bit will open a save box to save the updated elevation layer as an 
//* ASCII, using the coordinates of the originally opened up elevation surface
//**/    
//      
////// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//    
    
//    File f = new File("F:/Melt_modelling/Model_outputs/updated_elevation.txt");
    File f = new File("F:/Melt_modelling/Model_outputs/psi_" + (int)day + "." + (int)month + "." + (int)year + ".txt"); 
//       
    FileWriter fw;
    DecimalFormat df = new DecimalFormat("#.####");
//
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
    
      //String tempStr = "";

      for (int a = 0; a < psi_surface.length; a++)
      {
        for (int b = 0; b < psi_surface[a].length; b++)
        {

          if (psi_surface[a][b] == NODATA_value)
          {

            bw.write("-9999 ");
          }
          else
          {

            bw.write(df.format(psi_surface[a][b]) + " ");

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
    
   //System.out.println("Updated elevation: " + f.getAbsolutePath());
   System.out.println("Psi surface: " + f.getAbsolutePath());
   
   ///**
//* This bit will open a save box to save the updated elevation layer as an 
//* ASCII, using the coordinates of the originally opened up elevation surface
//**/    
//      
////// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//    
    
//    File f = new File("F:/Melt_modelling/Model_outputs/updated_elevation.txt");
    File f3 = new File("F:/Melt_modelling/Model_outputs/Q_daily_grid_" + (int)day + "." + (int)month + "." + (int)year + ".txt"); 
//       
    FileWriter fw3;
    DecimalFormat df3 = new DecimalFormat("#.####");
//
    try
    {
      BufferedWriter bw3 = new BufferedWriter(new FileWriter(f3));
      bw3.write("ncols" + "         " + store.getOriginalNcols()); 
      bw3.write(System.getProperty("line.separator"));
      bw3.write("nrows" + "         " + store.getOriginalNrows()); 
      bw3.write(System.getProperty("line.separator"));
      bw3.write("xllcorner" + "     " + store.getOriginalXllcorner());
      bw3.write(System.getProperty("line.separator"));
      bw3.write("yllcorner" + "     " + store.getOriginalYllcorner());
      bw3.write(System.getProperty("line.separator"));
      bw3.write("cellsize" + "      " + store.getOriginalCellsize()); 
      bw3.write(System.getProperty("line.separator"));
      bw3.write("NODATA_value" + "  " + "-9999");
      bw3.write(System.getProperty("line.separator"));

      /**
       *   Write out the array data
       **/
    
      //String tempStr = "";

      for (int a = 0; a < Q_daily_grid.length; a++)
      {
        for (int b = 0; b < Q_daily_grid[a].length; b++)
        {

          if (Q_daily_grid[a][b] == NODATA_value)
          {

            bw3.write("-9999 ");
          }
          else
          {

            bw3.write(df3.format(Q_daily_grid[a][b]) + " ");

          }

        }
        bw3.newLine();
      }

      bw3.close();

    }
    catch (IOException ioe)
    {

      ioe.printStackTrace();

    }
    
   //System.out.println("Updated elevation: " + f.getAbsolutePath());
   System.out.println("Q_daily_grid: " + f3.getAbsolutePath());
      
      //***********************************************
     
      // All other model code goes in here  
                        
//              1. Get elevation,ice thickness, hillshade etc. - DONE
//              2. Establish other variables
//              3. Calc. slope and aspect  - DONE SLOPE|DONE ASPECT
//              4. Calc. radiation <<<--- Variables added, methods not yet copied over....
//              5. Radiation algorithm - DONE
//                        - bring in hillshade surfaces - DONE
//                        - integrate saesonal hillshade component - DONE 
//              6. Bulk flux algorithm - DONE
//              7. Calculation of Q - DONE
//              [8. Precipitation algorithm and development of snow layer] - NOT DONE
//              9a. Melting without precip. - DONE
//              9b. Melting with precip. - NOT DONE
//              10. Outputs (this should be somebasic stats written to a file  - NOT DONE
//                  giving the day/month/year/mean area/mean volume/mean 
//                  thickness/mean elevation)
//              11. Update elevation and thickness surfaces (ensure that these are used in the loops, not those from store!)  - DONE
//              12. Loop back to the top and continue until end of all lists - DONE
            
   /*
   * Deals with inputs for the stats file. Sorts out loop counting and also 
   * loops through the radiation, psi and lapse rate surfaces to calculate mean 
   * values for the stats file
   */
   
   Loop_counter++;
   
   for(int i = 0; i<surfaceRadiationSurface.length; i++){
       for(int j = 0; j<surfaceRadiationSurface[i].length; j++){
           
           if(surfaceRadiationSurface[i][j] != NODATA_value)
           {
           double Radiation_instance = surfaceRadiationSurface[i][j];     
           Radiation_total += Radiation_instance;    
           Radiation_counter++;    
           }
           else{}
           
           if(psi_surface[i][j] != NODATA_value)
           {
           double Psi_instance = psi_surface[i][j];     
           Psi_total += Psi_instance;    
           Psi_counter++;    
           }
           else{}
           
           if(lapsed_temp[i][j] != NODATA_value)
           {
           double Lapsed_temp_instance = lapsed_temp[i][j];     
           Lapsed_temp_total += Lapsed_temp_instance;    
           Lapsed_temp_counter++;    
           }
           else{}
       }
   }
   //Sort other values
   
   bw_stats.write(Loop_counter + ", " + Radiation_total/Radiation_counter + 
           ", " + Psi_total/Psi_counter + ", " + mean_Q_m2/86400 + ", " + Lapsed_temp_total/Lapsed_temp_counter + ", " + "N/A" 
           + ", " + thickness_adjustment_total/adjustment_counter + ", " 
           + volume_adjustment_total/adjustment_counter);
   
   bw_stats.newLine();
   
   System.out.println("Working for now....");

  }
   
    bw_stats.close();
    } 
      
    catch (IOException ioe)
    {
    ioe.printStackTrace();
    }
    
    System.out.println("Model statistics file stored and saved: " + f_stats.getAbsolutePath());
    
}
  
  
   /**
   * This calculates the temperature at a point as a function of elevation relative
   * to the temperature from a meteorological station of known elevation - the lapse
   * rate value used is 0.0065 which is between the generic environmental lapse rate
   * values of 0.006 and 0.007°C/m (Ballantyne, 2002)
   **/
  
    public double LapseRate(double elevation, double BaseTemp)
  {

    double BaseElevation = 800.0; // Elevation of meteorological station
    //double BaseTemp = store.getTemp(); // Temperature at meteorological station (needs to come from within the loop)
    double equalizedElevation = elevation - BaseElevation; // Calculates height difference between met station and pooint of interest
    double pseudoTemp = equalizedElevation * 0.0065; // Calculates temperature (celcius - increase/decrease) lapse difference
                                                     //	according to equalizedElevation
    double lapseRate = BaseTemp - pseudoTemp; // Calculates temperature at point of interested with a lapse rate correction

    return lapseRate;
    
  }
   



}
