package energymodels;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.BufferedReader.*;
import java.io.CharArrayReader.*;
import java.io.FileInputStream.*;
import java.awt.image.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
 * @author Chris
**/

public class TestButtonListener implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;

  TestButtonListener(GUIPanel pn, Storage s)
  {
    panel = pn;
    store = s;
  }
  
  public void actionPerformed(ActionEvent e)
  {
   
   System.out.println("Test button pressed");

      Iterator<Double> iterator0 = store.getDayList().iterator();
      Iterator<Double> iterator1 = store.getMonthFile().iterator();
      Iterator<Double> iterator2 = store.getYearFile().iterator();
      Iterator<Double> iterator3 = store.getTemperatureFile().iterator();
      Iterator<Double> iterator4 = store.getPrecipFile().iterator();
      Iterator<Double> iterator4b = store.getPrecipSeasonList().iterator();
      
        
      
      double winter_days_0708_counter = 0;
      double winter_days_0809_counter = 0;
      double winter_days_0910_counter = 0;
      double winter_days_1011_counter = 0;
      
	        
      // Main loop
       
      while(iterator0.hasNext()){
          
        double day = iterator0.next();
        double month = iterator1.next();
        double year = iterator2.next();
        double temp = iterator3.next();
        double precip = iterator4.next();
        double precip_season = iterator4b.next();
          
        System.out.println("While loop 2 reached");  
        System.out.println("Month: " + month);
          
        System.out.println(day + "/" + month + "/" + year);
       
       //**************Summer precipitation************** 
       if(precip_season == 1.0)
       {
           System.out.println("Precip season = 1.0");
           System.out.println("If statement reached because it's summer");
       }
  
      //**************Winter precipitation************** 
      else if(precip_season == 0.0)
       {
           System.out.println("Precip season = 0.0");
           System.out.println("Else if statement reached because it's winter");
       }
    }
      
      Iterator<Double> iterator0_second_run = store.getDayList().iterator();
      Iterator<Double> iterator1_second_run = store.getMonthFile().iterator();
      Iterator<Double> iterator2_second_run = store.getYearFile().iterator();
      Iterator<Double> iterator3_second_run = store.getTemperatureFile().iterator();
      Iterator<Double> iterator4_second_run = store.getPrecipFile().iterator();
      Iterator<Double> iterator4b_second_run = store.getPrecipSeasonList().iterator();
      
      while(iterator0_second_run.hasNext()){
          
        double day = iterator0_second_run.next();
        double month = iterator1_second_run.next();
        double year = iterator2_second_run.next();
        double temp = iterator3_second_run.next();
        double precip = iterator4_second_run.next();
        double precip_season = iterator4b_second_run.next();
          
          System.out.println("While loop 1 reached");
          System.out.println((int)day + "/" + (int)month + "/" + (int)year);
          winter_days_0708_counter++;
          winter_days_0809_counter++;
          winter_days_0910_counter++;
          winter_days_1011_counter++;
          
      }
            
      System.out.println("Winter days 07-08:" + winter_days_0708_counter);
      System.out.println("Winter days 08-09:" + winter_days_0809_counter);
      System.out.println("Winter days 09-10:" + winter_days_0910_counter);
      System.out.println("Winter days 10-11:" + winter_days_1011_counter);

      
           
  }
}

