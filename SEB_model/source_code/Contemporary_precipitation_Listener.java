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
 * The winter season occurs when temperatures at the AWS are below 0 degrees C 
 * for a period of 5 days continuously. After a period of 5 days positive 
 * temperature, seasonality reverts to summer. If a period of 5 days of mean 
 * temp. greater than 0 degrees occurs between colder months (for example, a 
 * warm week in November), it is still assumed that winter conditions prevail.
 * 
 * @author Chris 4/2/13
 */

public class Contemporary_precipitation_Listener implements ActionListener {
   
  private GUIPanel panel = null;
  private Storage store = null;
  
  Contemporary_precipitation_Listener (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }
  
   @Override
  public void actionPerformed(ActionEvent e)
  {
      //Iterators
      Iterator<Double> iterator0_winter_days = store.getDayList().iterator();
      Iterator<Double> iterator1_winter_days = store.getMonthFile().iterator();
      Iterator<Double> iterator2_winter_days = store.getYearFile().iterator();
      Iterator<Double> iterator3_winter_days = store.getTemperatureFile().iterator();
      Iterator<Double> iterator4_winter_days =  store.getPrecipFile().iterator();
      Iterator<Double> iterator4b_winter_days =  store.getPrecipSeasonList().iterator();
      
      //Variables
      double summer_snow_thickness_total; //= 0;
      double summer_snow_thickness_counter; // = 0;
      double winter_snow_thickness_total; // = 0;
      double winter_snow_thickness_counter; // = 0;
      int NODATA_value = -9999;
      
      //Constants (subject to change) 
      double snow_threshold = 1.5; // Temperature below which rain falls as snow
      double fresh_snow_density = 0.400; // Relative to 1.0 for water
      
      //Arrays
      double elevation1[][] = store.getElevation();
      double elev_initial_size[][] = store.getElevation();
      double summer_snow_thickness_INITIAL[][] = new double[elev_initial_size.length][elev_initial_size[0].length];
      double winter_snow_thickness_INITIAL[][] = new double[elev_initial_size.length][elev_initial_size[0].length];
      
      for(int i = 0; i<elevation1.length; i++){
          for(int j = 0; j<elevation1[i].length; j++)
          {
          if(elevation1[i][j] != NODATA_value){
          summer_snow_thickness_INITIAL[i][j] = 0.0;
          winter_snow_thickness_INITIAL[i][j] = 0.0;
          }
          else
          {
          summer_snow_thickness_INITIAL[i][j] = NODATA_value;
          winter_snow_thickness_INITIAL[i][j] = NODATA_value;
          }
          
          }
      }
      
      store.setSummerSnowSurface(summer_snow_thickness_INITIAL);
      store.setWinterSnowSurface(winter_snow_thickness_INITIAL);
      
      /**
       * Calculate the total number of winter days for each season (07/08 etc)) 
       * for use inside the model with the winter precipitation algorithm
      **/
      
      double winter_days_0708_counter = 0;
      double winter_days_0809_counter = 0;
      double winter_days_0910_counter = 0;
      double winter_days_1011_counter = 0;

      while(iterator1_winter_days.hasNext()){
          
          double Day_temporary = iterator0_winter_days.next();
          double Year_temporary = iterator2_winter_days.next();
          double Month_temporary = iterator1_winter_days.next();
          double Precip_season_temporary = iterator4b_winter_days.next();
          System.out.println("Loop 1: " + (int)Day_temporary + "/" + (int)Month_temporary + "/" + (int)Year_temporary);
          
          if((
                  (Year_temporary == 2007.0) && 
                  (
                  (Month_temporary == 8.0) || (Month_temporary == 9.0) || (Month_temporary == 10.0) || (Month_temporary == 11.0) || (Month_temporary == 12.0)) 
                  && 
                  (Precip_season_temporary == 0.0)) 
                  ||
                  ((Year_temporary == 2008.0) && ((Month_temporary == 1.0) || (Month_temporary == 2.0) || (Month_temporary == 3.0) || (Month_temporary == 4.0)||(Month_temporary == 5.0)||(Month_temporary == 6.0)) 
                  && 
                  (Precip_season_temporary == 0.0))
                  )
          {
          System.out.println("BbbbBbBrrrrrrrrRrRRRRRRR it's cold - must be winter 2007-2008 :^D");
          winter_days_0708_counter++;
          }
          
          else if((
                  (Year_temporary == 2008.0) && 
                  (
                  (Month_temporary == 8.0) || (Month_temporary == 9.0) || (Month_temporary == 10.0) || (Month_temporary == 11.0) || (Month_temporary == 12.0)) 
                  && 
                  (Precip_season_temporary == 0.0)) 
                  ||
                  ((Year_temporary == 2009.0) && ((Month_temporary == 1.0) || (Month_temporary == 2.0) || (Month_temporary == 3.0) || (Month_temporary == 4.0)||(Month_temporary == 5.0)||(Month_temporary == 6.0)) 
                  && 
                  (Precip_season_temporary == 0.0))
                  )
          {
          System.out.println("BbbbBbBrrrrrrrrRrRRRRRRR it's cold - must be winter 2008-2009 :^D");
          winter_days_0809_counter++;
          }
          
          else if((
                  (Year_temporary == 2009.0) && 
                  (
                  (Month_temporary == 8.0) || (Month_temporary == 9.0) || (Month_temporary == 10.0) || (Month_temporary == 11.0) || (Month_temporary == 12.0)) 
                  && 
                  (Precip_season_temporary == 0.0)) 
                  ||
                  ((Year_temporary == 2010.0) && ((Month_temporary == 1.0) || (Month_temporary == 2.0) || (Month_temporary == 3.0) || (Month_temporary == 4.0)||(Month_temporary == 5.0)||(Month_temporary == 6.0)) 
                  && 
                  (Precip_season_temporary == 0.0))
                  )
          {
          System.out.println("BbbbBbBrrrrrrrrRrRRRRRRR it's cold - must be winter 2009-2010 :^D");
          winter_days_0910_counter++;
          }
          
          else if((
                  (Year_temporary == 2010.0) && 
                  (
                  (Month_temporary == 8.0) || (Month_temporary == 9.0) || (Month_temporary == 10.0) || (Month_temporary == 11.0) || (Month_temporary == 12.0)) 
                  && 
                  (Precip_season_temporary == 0.0)) 
                  ||
                  ((Year_temporary == 2011.0) && ((Month_temporary == 1.0) || (Month_temporary == 2.0) || (Month_temporary == 3.0) || (Month_temporary == 4.0)||(Month_temporary == 5.0)||(Month_temporary == 6.0)) 
                  && 
                  (Precip_season_temporary == 0.0))
                  )
          {
          System.out.println("BbbbBbBrrrrrrrrRrRRRRRRR it's cold - must be winter 2010-2011 :^D");
          winter_days_1011_counter++;
          }
          
          else
          {
              System.out.println("Nice and warm - must be summer :^)");
              // Macht nichts
          }
      }
            
      System.out.println("Winter days 07-08:" + winter_days_0708_counter);
      System.out.println("Winter days 08-09:" + winter_days_0809_counter);
      System.out.println("Winter days 09-10:" + winter_days_0910_counter);
      System.out.println("Winter days 10-11:" + winter_days_1011_counter);
      
      // Re-establish iterators to enable second while loop to work
      Iterator<Double> iterator0_repeat = store.getDayList().iterator();
      Iterator<Double> iterator1_repeat = store.getMonthFile().iterator();
      Iterator<Double> iterator2_repeat = store.getYearFile().iterator();
      Iterator<Double> iterator3_repeat = store.getTemperatureFile().iterator();
      Iterator<Double> iterator4_repeat =  store.getPrecipFile().iterator();
      Iterator<Double> iterator4b_repeat =  store.getPrecipSeasonList().iterator();
      
      // Main loop
      while(iterator1_repeat.hasNext()){
        
        double elevation[][] = store.getElevation();
        double summer_snow_thickness[][] = store.getSummerSnowSurface(); // An empty array in the first iteration of this loop
        double winter_snow_thickness[][] = store.getWinterSnowSurface(); // An empty array in the first iteration of this loop
        double WinterSnow_interpolation[][];
        double temp_winter_snow_layer[][] = new double[elevation.length][elevation[0].length];
        
        double lapsed_temp[][] = new double[elevation.length][elevation[0].length];
        
        double day = iterator0_repeat.next();
        double month = iterator1_repeat.next();
        double year = iterator2_repeat.next();
        double temp = iterator3_repeat.next();
        store.setTemp(temp);
        double precip = iterator4_repeat.next();
        double precip_season = iterator4b_repeat.next();
        
        System.out.println("Loop 2: " + (int)day + "/" + (int)month + "/" + (int)year);
       
       //**************Summer precipitation************** 
	   
       if(precip_season == 1.0)
       {
       System.out.println("Summer precipitation");
            
       /*
       * Lapse rate calculater
       */
 
      for(int i = 0; i<elevation.length; i++){
         for(int j = 0; j<elevation[i].length; j++){
             
            double lapse_intermediary = elevation[i][j];

            if(lapse_intermediary != NODATA_value)
            {
            lapsed_temp[i][j] = (double) LapseRate(lapse_intermediary, temp);
            } 

            else if (lapse_intermediary == NODATA_value)
            {
            lapsed_temp[i][j] = (int) NODATA_value;
            }
         }
       }
        
        store.setLapsedTempSurface(lapsed_temp);
      
       /*
       * Populate summer snow thickness layer
       */
        
        for(int i = 0; i<summer_snow_thickness.length; i++){
         for(int j = 0; j<summer_snow_thickness[i].length; j++){
             
             if(((lapsed_temp[i][j] != NODATA_value) & (precip != NODATA_value)) & ((precip != 0) & (lapsed_temp[i][j] <= snow_threshold))) // If less than or equal to threshold, snow amount is equal to precip * fresh snow density
             {
             summer_snow_thickness[i][j] += precip/fresh_snow_density ; // Increase the value of the existing cell by new snow fall
             }
             else if (((lapsed_temp[i][j] != NODATA_value) & (precip != NODATA_value)) & (lapsed_temp[i][j] > snow_threshold)) // This simulates rain which flows off the surface
             {
             summer_snow_thickness[i][j] += 0.0;  // Increase the value of the existing cell by nothing (could just do nothing...)
             }
             else if ((lapsed_temp[i][j] != NODATA_value) & (precip == NODATA_value)) // There are some instances where the AWS didn't work so this is pertinent
             {
             summer_snow_thickness[i][j] += 0.0;    
             }
             else if(lapsed_temp[i][j] == NODATA_value)
             {
             summer_snow_thickness[i][j] = NODATA_value;
             }
         }
        }
          
       store.setSummerSnowSurface(summer_snow_thickness);
       
       /**
       * Loops through the SummerSnowSurface layer - this just gives the mean 
       * thickness over the summer grid which is continuously increasing (so 
       * long as melt is not occurring)
       **/
       
      summer_snow_thickness_total = 0; // Has to be done here. If set as 0 outide the loop the total will grow forever
      summer_snow_thickness_counter = 0; // Has to be done here. If set as 0 outide the loop the total will grow forever
      
      for(int i = 0; i<summer_snow_thickness.length; i++){
          for(int j = 0; j<summer_snow_thickness[i].length; j++){
              
              if(summer_snow_thickness[i][j] != NODATA_value)
              {
              //System.out.println("Summer snow cells: " + summer_snow_thickness[i][j]);
              double summer_snow_thickness_instance = summer_snow_thickness[i][j];
              summer_snow_thickness_total += summer_snow_thickness_instance; // Accumulates all summer snow thickness changes
              summer_snow_thickness_counter++; // Counts instances of summer snow thickness where != NODATA_value
              }
              else if(summer_snow_thickness[i][j] == NODATA_value)
              {
              //do nothing
              }
          }
      }
      
      double mean_daily_summer_snowfall = summer_snow_thickness_total/summer_snow_thickness_counter;
      System.out.println("Mean accumulated snow thickness (of summer layer) at this point: " + mean_daily_summer_snowfall +"m");
      System.out.println("Don't be alarmed if the above value doesn't go up in tandem with the measured precipitation value - don't forget it only snows as a function of lapse corrected temperature.");
      
      }
	  
      //**************Winter precipitation************** 
       else if(precip_season == 0.0)
      {
      System.out.println("Winter precipitation");    
          // This bit does:
          // (1) Select correct snow layer according to month and year
          // (2) Add to each cell in winter_snow_thickness the value of the 
          //     imported snow layer divided by the number of days in winter 
          //     (calculated outside of the loop)

          if(
            ((year == 2007.0) && 
            (
            (month == 9.0) || (month == 10.0) || (month == 11.0) || (month == 12.0) 
            )) 
            |
            ((year == 2008.0) && ((month == 1.0) || (month == 2.0) || (month == 3.0) || (month == 4.0) || (month == 5.0))
            ))
          {
          WinterSnow_interpolation = store.get_snow_surface_0708();
          
          for(int i = 0; i<WinterSnow_interpolation.length; i++){
              for(int j = 0; j<WinterSnow_interpolation[i].length; j++){
                  
                  if((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] != 0)){
                    temp_winter_snow_layer[i][j] = WinterSnow_interpolation[i][j]/winter_days_0708_counter;
                  }
                  else if((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] == 0)){
                      temp_winter_snow_layer[i][j] = 0;
                  }
                  else if((elevation[i][j] == NODATA_value) || (WinterSnow_interpolation[i][j] == NODATA_value))
                      temp_winter_snow_layer[i][j] = NODATA_value;
              }
          }
          } 
          else if(
            ((year == 2008.0) && 
            (
            (month == 9.0) || (month == 10.0) || (month == 11.0) || (month == 12.0)
            )) 
            |
            ((year == 2009.0) && ((month == 1.0) || (month == 2.0) || (month == 3.0) || (month == 4.0) || (month == 5.0))
            ))
          {
          WinterSnow_interpolation = store.get_snow_surface_0809();
          
          for(int i = 0; i<WinterSnow_interpolation.length; i++){
              for(int j = 0; j<WinterSnow_interpolation[i].length; j++){
                  
                  if((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] != 0)){
                    temp_winter_snow_layer[i][j] = WinterSnow_interpolation[i][j]/winter_days_0809_counter;
                  }
                  else if((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] == 0)){
                    temp_winter_snow_layer[i][j] = 0;
                  }
                  else if((elevation[i][j] == NODATA_value) || (WinterSnow_interpolation[i][j] == NODATA_value))
                    temp_winter_snow_layer[i][j] = NODATA_value;
              }   
          }
          }
          else if((
            (year == 2009.0) && 
            (
            (month == 9.0) || (month == 10.0) || (month == 11.0) || (month == 12.0) 
            )) 
            |
            ((year == 2010.0) && ((month == 1.0) || (month == 2.0) || (month == 3.0) || (month == 4.0) || (month == 5.0))
            ))
          {
          WinterSnow_interpolation = store.get_snow_surface_0910();
          
          for(int i = 0; i<WinterSnow_interpolation.length; i++){
              for(int j = 0; j<WinterSnow_interpolation[i].length; j++){
                  
                  if((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] != 0)){
                    temp_winter_snow_layer[i][j] = WinterSnow_interpolation[i][j]/winter_days_0910_counter;
                  }
                  else if((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] == 0)){
                    temp_winter_snow_layer[i][j] = 0;
                  }
                  else if((elevation[i][j] == NODATA_value) || (WinterSnow_interpolation[i][j] == NODATA_value))
                  {
                    temp_winter_snow_layer[i][j] = NODATA_value;
              }   
          }
          }
          }
          else if((
            (year == 2010.0) && 
            (
            (month == 9.0) || (month == 10.0) || (month == 11.0) || (month == 12.0) 
            )) 
            |
            ((year == 2011.0) && ((month == 1.0) || (month == 2.0) || (month == 3.0) || (month == 4.0) || (month == 5.0))
            ))
          {
          WinterSnow_interpolation = store.get_snow_surface_1011();
          
          for(int i = 0; i<WinterSnow_interpolation.length; i++){
              for(int j = 0; j<WinterSnow_interpolation[i].length; j++){
                  
                  if((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] != 0)){
                    temp_winter_snow_layer[i][j] = WinterSnow_interpolation[i][j]/winter_days_1011_counter;
                  }
                  else if((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] == 0)){
                    temp_winter_snow_layer[i][j] = 0;
                  }
                  else if((elevation[i][j] == NODATA_value) || (WinterSnow_interpolation[i][j] == NODATA_value))
                    temp_winter_snow_layer[i][j] = NODATA_value;
          }   
          }
          }   
          
          /*
           * Fill up the winter snow thickness layer
           */
                   
      for(int i = 0; i<winter_snow_thickness.length; i++){
         for(int j = 0; j<winter_snow_thickness[i].length; j++){
             
             if(temp_winter_snow_layer[i][j] != NODATA_value){
                winter_snow_thickness[i][j] += temp_winter_snow_layer[i][j];
             }
             else if(temp_winter_snow_layer[i][j] == NODATA_value){
                winter_snow_thickness[i][j] = NODATA_value;
             }
         
         }
      }
                  
      /**
       * Loops through the WinterSnowSurface layer and calculates mean total
       * accumulated thickness on a given day
       **/
          
      winter_snow_thickness_total = 0; // Has to be done here. If set as 0 outide the loop the total will grow forever
      winter_snow_thickness_counter = 0; // Has to be done here. If set as 0 outide the loop the total will grow forever
      
      for(int i = 0; i<winter_snow_thickness.length; i++){
          for(int j = 0; j<winter_snow_thickness[i].length; j++){
              
              if(winter_snow_thickness[i][j] != NODATA_value)
              {
              //System.out.println("Winter snow cells: " + winter_snow_thickness[i][j]);
              double winter_snow_thickness_instance = winter_snow_thickness[i][j];
              winter_snow_thickness_total += winter_snow_thickness_instance; // Accumulates all summer snow thickness changes
              winter_snow_thickness_counter++; // Counts instances of summer snow thickness where != NODATA_value
              }
              else if(winter_snow_thickness[i][j] == NODATA_value)
              {
              //do nothing
              }
          }
      }
          System.out.println("Mean accumulated winter snowfall at this point: " + winter_snow_thickness_total/winter_snow_thickness_counter +"m");
          store.setWinterSnowThickness(winter_snow_thickness);
      }
  }
      
      // End of precipitation algorithms
      
      double summer_snow_thickness_after_the_snow[][] = store.getSummerSnowSurface();
      System.out.println("Summer snow accumulation surface saved");
      double winter_snow_thickness_after_the_snow[][] = store.getWinterSnowThickness();
      System.out.println("Winter snow accumulation surface saved");
      
      // Ultimately, the main model will have the following tiers:
        //1) Melt through winter snow
        //2) Melt through summer snow
        //3) Melt through ice
           
/**
* This bit will open a save box to save the summer_snow_thickness_after_the_snow
* layer as an ASCII, using the coordinates of the originally opened up elevation 
* surface
**/    
      
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
    File f_summer_snow = new File("F:/Melt_modelling/Model_outputs/summer_snow.txt"); 
      
    DecimalFormat df_summer_snow = new DecimalFormat("#.####");
    
    try
    {
      BufferedWriter bw_summer_snow = new BufferedWriter(new FileWriter(f_summer_snow));
      bw_summer_snow.write("ncols" + "         " + store.getOriginalNcols()); 
      bw_summer_snow.write(System.getProperty("line.separator"));
      bw_summer_snow.write("nrows" + "         " + store.getOriginalNrows()); 
      bw_summer_snow.write(System.getProperty("line.separator"));
      bw_summer_snow.write("xllcorner" + "     " + store.getOriginalXllcorner());
      bw_summer_snow.write(System.getProperty("line.separator"));
      bw_summer_snow.write("yllcorner" + "     " + store.getOriginalYllcorner());
      bw_summer_snow.write(System.getProperty("line.separator"));
      bw_summer_snow.write("cellsize" + "      " + store.getOriginalCellsize()); 
      bw_summer_snow.write(System.getProperty("line.separator"));
      bw_summer_snow.write("NODATA_value" + "  " + "-9999");
      bw_summer_snow.write(System.getProperty("line.separator"));

      /**
       *   Write out the array data
       **/
    
      String tempStr = "";

      for (int a = 0; a < summer_snow_thickness_after_the_snow.length; a++)
      {
        for (int b = 0; b < summer_snow_thickness_after_the_snow[a].length; b++)
        {

          if (summer_snow_thickness_after_the_snow[a][b] == NODATA_value)
          {

            bw_summer_snow.write("-9999 ");
          }
          else
          {

            bw_summer_snow.write(df_summer_snow.format(summer_snow_thickness_after_the_snow[a][b]) + " ");

          }

        }
        bw_summer_snow.newLine();
      }

      bw_summer_snow.close();

    }
    catch (IOException ioe)
    {

      ioe.printStackTrace();

    }
    
    System.out.println("Summer snow accumulation surface saved: " + f_summer_snow.getAbsolutePath());
    
/**
* This bit will open a save box to save the winter_snow_thickness_after_the_snow
* layer as an ASCII, using the coordinates of the originally opened up elevation 
* surface
**/    
      
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
    File f_winter_snow = new File("F:/Melt_modelling/Model_outputs/winter_snow.txt"); 
//  
    DecimalFormat df_winter_snow = new DecimalFormat("#.####");
//
    try
    {
      BufferedWriter bw_winter_snow = new BufferedWriter(new FileWriter(f_winter_snow));
      bw_winter_snow.write("ncols" + "         " + store.getOriginalNcols()); 
      bw_winter_snow.write(System.getProperty("line.separator"));
      bw_winter_snow.write("nrows" + "         " + store.getOriginalNrows()); 
      bw_winter_snow.write(System.getProperty("line.separator"));
      bw_winter_snow.write("xllcorner" + "     " + store.getOriginalXllcorner());
      bw_winter_snow.write(System.getProperty("line.separator"));
      bw_winter_snow.write("yllcorner" + "     " + store.getOriginalYllcorner());
      bw_winter_snow.write(System.getProperty("line.separator"));
      bw_winter_snow.write("cellsize" + "      " + store.getOriginalCellsize()); 
      bw_winter_snow.write(System.getProperty("line.separator"));
      bw_winter_snow.write("NODATA_value" + "  " + "-9999");
      bw_winter_snow.write(System.getProperty("line.separator"));

      /**
       *   Write out the array data
       **/
    
      String tempStr = "";

      for (int a = 0; a < winter_snow_thickness_after_the_snow.length; a++)
      {
        for (int b = 0; b < winter_snow_thickness_after_the_snow[a].length; b++)
        {

          if (winter_snow_thickness_after_the_snow[a][b] == NODATA_value)
          {

            bw_winter_snow.write("-9999 ");
          }
          else
          {

            bw_winter_snow.write(df_winter_snow.format(winter_snow_thickness_after_the_snow[a][b]) + " ");

          }

        }
        bw_winter_snow.newLine();
      }

      bw_winter_snow.close();

    }
    catch (IOException ioe)
    {

      ioe.printStackTrace();

   }
    
    System.out.println("Winter snow accumulation surface saved: " + f_winter_snow.getAbsolutePath());
      
  }
  
   
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