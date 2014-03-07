package energymodels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

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
 * Open configuration file - uploads files automatically, allowing avoidance 
 * of use of buttons.
 * 
 * @author Chris 10/2/13
 */

public class OpenConfigFile_historical_Listener implements ActionListener {
   
  private GUIPanel panel = null;
  private Storage store = null;
  
  OpenConfigFile_historical_Listener (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }
  
   @Override
  public void actionPerformed(ActionEvent e)
  {
      int Memory_usage = 0; // set to 0: no memory usage printout
                               // set to 1: memory usage printout (total available and total used)
      Runtime runtime = Runtime.getRuntime();
      long memory;
      
      //ResourceBundle MeltModelConfig = ResourceBundle.getBundle("Model_props_winter_19261943_historic");
      ResourceBundle MeltModelConfig = ResourceBundle.getBundle("Model_props_winter_19431959_historic");
      //ResourceBundle MeltModelConfig = ResourceBundle.getBundle("Model_props_winter_19591978_historic");
      //ResourceBundle MeltModelConfig = ResourceBundle.getBundle("Model_props_winter_19781991_historic");
      //ResourceBundle MeltModelConfig = ResourceBundle.getBundle("Model_props_winter_19912010_historic");
      
            if(MeltModelConfig == null)
      {
      System.out.println("Config file not uploaded");
      }
      else
      {
      System.out.println("Config file uploaded");
      }
            
      String key_1 = MeltModelConfig.getString("Elevation");
      String key_2 = MeltModelConfig.getString("Thickness");
      String key_3 = MeltModelConfig.getString("Main_data_input");
      String key_4 = MeltModelConfig.getString("WinterSnow_data_input");
      String key_5 = MeltModelConfig.getString("Radiation_data_input");
      
      String key_hsm1 = MeltModelConfig.getString("JanHS_midnight");
      String key_hsm2 = MeltModelConfig.getString("FebHS_midnight");
      String key_hsm3 = MeltModelConfig.getString("MarHS_midnight");
      String key_hsm4 = MeltModelConfig.getString("AprHS_midnight");
      String key_hsm5 = MeltModelConfig.getString("MayHS_midnight");
      String key_hsm6 = MeltModelConfig.getString("JunHS_midnight");
      String key_hsm7 = MeltModelConfig.getString("JulHS_midnight");
      String key_hsm8 = MeltModelConfig.getString("AugHS_midnight");
      String key_hsm9 = MeltModelConfig.getString("SepHS_midnight");
      String key_hsm10 = MeltModelConfig.getString("OctHS_midnight");
      String key_hsm11 = MeltModelConfig.getString("NovHS_midnight");
      String key_hsm12 = MeltModelConfig.getString("DecHS_midnight");
      
      String key_hs6am1 = MeltModelConfig.getString("JanHS_6am");
      String key_hs6am2 = MeltModelConfig.getString("FebHS_6am");
      String key_hs6am3 = MeltModelConfig.getString("MarHS_6am");
      String key_hs6am4 = MeltModelConfig.getString("AprHS_6am");
      String key_hs6am5 = MeltModelConfig.getString("MayHS_6am");
      String key_hs6am6 = MeltModelConfig.getString("JunHS_6am");
      String key_hs6am7 = MeltModelConfig.getString("JulHS_6am");
      String key_hs6am8 = MeltModelConfig.getString("AugHS_6am");
      String key_hs6am9 = MeltModelConfig.getString("SepHS_6am");
      String key_hs6am10 = MeltModelConfig.getString("OctHS_6am");
      String key_hs6am11 = MeltModelConfig.getString("NovHS_6am");
      String key_hs6am12 = MeltModelConfig.getString("DecHS_6am");
      
      String key_hsnoon1 = MeltModelConfig.getString("JanHS_noon");
      String key_hsnoon2 = MeltModelConfig.getString("FebHS_noon");
      String key_hsnoon3 = MeltModelConfig.getString("MarHS_noon");
      String key_hsnoon4 = MeltModelConfig.getString("AprHS_noon");
      String key_hsnoon5 = MeltModelConfig.getString("MayHS_noon");
      String key_hsnoon6 = MeltModelConfig.getString("JunHS_noon");
      String key_hsnoon7 = MeltModelConfig.getString("JulHS_noon");
      String key_hsnoon8 = MeltModelConfig.getString("AugHS_noon");
      String key_hsnoon9 = MeltModelConfig.getString("SepHS_noon");
      String key_hsnoon10 = MeltModelConfig.getString("OctHS_noon");
      String key_hsnoon11 = MeltModelConfig.getString("NovHS_noon");
      String key_hsnoon12 = MeltModelConfig.getString("DecHS_noon");
            
      String key_hs6pm1 = MeltModelConfig.getString("JanHS_6pm");
      String key_hs6pm2 = MeltModelConfig.getString("FebHS_6pm");
      String key_hs6pm3 = MeltModelConfig.getString("MarHS_6pm");
      String key_hs6pm4 = MeltModelConfig.getString("AprHS_6pm");
      String key_hs6pm5 = MeltModelConfig.getString("MayHS_6pm");
      String key_hs6pm6 = MeltModelConfig.getString("JunHS_6pm");
      String key_hs6pm7 = MeltModelConfig.getString("JulHS_6pm");
      String key_hs6pm8 = MeltModelConfig.getString("AugHS_6pm");
      String key_hs6pm9 = MeltModelConfig.getString("SepHS_6pm");
      String key_hs6pm10 = MeltModelConfig.getString("OctHS_6pm");
      String key_hs6pm11 = MeltModelConfig.getString("NovHS_6pm");
      String key_hs6pm12 = MeltModelConfig.getString("DecHS_6pm");
      

//      String key_17 = MeltModelConfig.getString("Snow0708");
//      String key_18 = MeltModelConfig.getString("Snow0809");
//      String key_19 = MeltModelConfig.getString("Snow0910");
//      String key_20 = MeltModelConfig.getString("Snow1011");
      
      // Elevation
      File f_elev = new File(key_1);
      GRIDJava2 gj = new GRIDJava2(f_elev, true, store);
      double elev_data[][] = gj.getTwoDdoubleArray();
      store.setElevation_INITIAL(elev_data);
      store.setElevation(elev_data);
      System.out.println("Elevation surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //Thickness
      File f_thick = new File(key_2);
      GRIDJava2 gj2 = new GRIDJava2(f_thick, true, store);
      double thick_data[][] = gj2.getTwoDdoubleArray();
      store.setThickness_INITIAL(thick_data);
      store.setThickness(thick_data);
      System.out.println("Thickness surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //Met data
      //********************************
      
    File f_main_data = new File(key_3);
      
    FileReader fr = null;
    int year_main, month_main;
    double temp, PrecipTotal, Precip_season, Tau_estimate;
        
    ArrayList <Integer> year_list_MAIN = new ArrayList<Integer>();
    ArrayList <Integer> month_list_MAIN = new ArrayList<Integer>();
    ArrayList <Double> temp_list = new ArrayList<Double>();
    ArrayList <Double> precip_list = new ArrayList<Double>();
    ArrayList <Double> precip_season_list = new ArrayList<Double>();
    ArrayList <Double> tau_estimate_list = new ArrayList<Double>();
    
    try
    {

      fr = new FileReader(f_main_data);

    }
    catch (FileNotFoundException fnfe)
    {

      fnfe.printStackTrace();

    }

    BufferedReader br = new BufferedReader(fr);

    try
    {
        
                String line;
 
		//br = new BufferedReader(new FileReader("C:/Users/Chris/Desktop/Java_module_tests/Input_met_data_TEST.csv"));
                
                br.readLine(); // Skips first line in file (useful if you have a header)
                
		while ((line = br.readLine()) != null) {
		   //System.out.println(line);
 
		   StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
 
		   while (stringTokenizer.hasMoreElements()) {
                                                
                         year_main = Integer.parseInt(stringTokenizer.nextElement().toString());
                         month_main = Integer.parseInt(stringTokenizer.nextElement().toString());
                         temp = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Precip_season = Double.parseDouble(stringTokenizer.nextElement().toString());
                         PrecipTotal = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Tau_estimate = Double.parseDouble(stringTokenizer.nextElement().toString());
                         
                         //************************Array List Integration *************
                         
                         year_list_MAIN.add(year_main);
                         month_list_MAIN.add(month_main);
                         temp_list.add(temp);
                         precip_list.add(PrecipTotal);
                         precip_season_list.add(Precip_season);
                         tau_estimate_list.add(Tau_estimate);
                         
                         //************************ Array List Integration *************
                       
                         //************************ Set In Storage *************
                         store.setYearFile_MAIN(year_list_MAIN);
                         store.setMonthFile_MAIN(month_list_MAIN);
                         store.setTemperatureFile(temp_list);
                         store.setPrecipFile(precip_list);
                         store.setPrecipSeasonList_MAIN(precip_season_list);
                         store.setTauEstimateList(tau_estimate_list);
                         
                         // Just for running a single line for testing the radiation on slope listener
//                         store.setYear_MAIN(year_main);
//                         store.setMonth_MAIN(month_main);
//                         store.setTemp(temp);
//                         store.setPrecipTotal(PrecipTotal);
//                         store.setPrecipSeason(Precip_season);                 
//                         store.setTauEstimate(Tau_estimate);
//
//                        // Just for running a single line for testing the radiation on slope listener
//                         int yearStore = store.getYear_MAIN();
//                         double monthStore = store.getMonth_MAIN();
//                         double tempStore = store.getTemp();
//                         double precipTotalStore = store.getPrecipTotal();
//                         double precipSeasonStore = store.getPrecipSeason();
//                                                  
//                         double tauEstimateStore = store.getTauEstimate();

                                                  
                         //************************ Set In Storage *************
                    
                    //System.out.println(monthStore + "/" + yearStore + " [Main input file (temp & precip)]");
//                    System.out.println("Year: " + yearStore);
//                    System.out.println("Month: " + monthStore);
//                    System.out.println("Temp: " + tempStore); 
//                    System.out.println(monthStore + "/" + yearStore);
//                    System.out.println("Precipitation total: " + precipTotalStore);
//                    System.out.println("Precipitation season : " + precipSeasonStore);
//                    System.out.println("Tau estimate: " + tauEstimateStore);
                    
       
                         
		   }
                   
		}
                
                  
    } catch(Exception e_br){
        
        System.out.println("Met (main) file read in fail");
    
    }
    
    System.out.println("Met data stored");
                         
                         if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
                         
    //WinterSnow data
      //********************************
      
    File f_winterSnow_data = new File(key_4);
      
    FileReader fr_winterSnow = null;
    int year_WinterPrecip;
    double winterPrecip, winterMonthCount;
    
    ArrayList <Integer> year_list_WinterPrecip = new ArrayList<Integer>();
    ArrayList <Double> precip_list_winterSnow = new ArrayList<Double>();
    ArrayList <Double> winterMonth_list = new ArrayList<Double>();
    
    try
    {

      fr_winterSnow = new FileReader(f_winterSnow_data);

    }
    catch (FileNotFoundException fnfe2)
    {

      fnfe2.printStackTrace();

    }

    BufferedReader br_winterSnow = new BufferedReader(fr_winterSnow);

    try
    {
        
                String line;
 
		//br = new BufferedReader(new FileReader("C:/Users/Chris/Desktop/Java_module_tests/Input_met_data_TEST.csv"));
                
                br_winterSnow.readLine(); // Skips first line in file (useful if you have a header)
                
		while ((line = br_winterSnow.readLine()) != null) {
		   //System.out.println(line);
 
		   StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
 
		   while (stringTokenizer.hasMoreElements()) {
                                                
                         year_WinterPrecip = Integer.parseInt(stringTokenizer.nextElement().toString());
                         winterPrecip = Double.parseDouble(stringTokenizer.nextElement().toString());
                         winterMonthCount = Double.parseDouble(stringTokenizer.nextElement().toString());                       
                         //************************Array List Integration *************
                         
                         year_list_WinterPrecip.add(year_WinterPrecip);
                         precip_list_winterSnow.add(winterPrecip);
                         winterMonth_list.add(winterMonthCount);
                                                  
                         //************************ Array List Integration *************
                       
                         //************************ Set In Storage *************
                         store.setYearFile_WinterPrecip(year_list_WinterPrecip);
                         store.set_Winter_PrecipFile(precip_list_winterSnow);
                         store.set_Winter_MonthFile(winterMonth_list);
                         
                         // Just for running a single line for testing the radiation on slope listener
//                         store.setYear_WinterPrecip(year_WinterPrecip);
//                         store.setWinterPrecip(winterPrecip);
//                         store.setWinterMonthCount(winterMonthCount);
//                                                  
//                         // Just for running a single line for testing the radiation on slope listener
//                         int yearStore_WinterPrecip = store.getYear_WinterPrecip();
//                         double winterPrecipStore = store.getWinterPrecip();
//                         double winterMonthCountStore = store.getWinterMonthCount();
                         
                         //************************ Set In Storage *************
                                   
//                    System.out.println("Year: " + yearStore_WinterPrecip);
//                    System.out.println("Winter precipitation total: " + winterPrecipStore);
//                    System.out.println("Winter month count: " + winterMonthCountStore);
       
                         System.out.println("Winter precipitation data stored");
                         
                         if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
//                    
		   }
                   
		}
 
    } catch(Exception e_br_winterSnow){
        
        System.out.println("Met (WinterSnow) file read in fail");
    
    }
    
      //Radiation data
      //********************************
      
      File f_radiation_data = new File(key_5);
      
      FileReader fr_radiation = null;
    int year_Radiation, month_Radiation, day_Radiation;
    double TOA_midnight, Zenith_midnight, Azimuth_midnight, TOA_6am, Zenith_6am, 
                Azimuth_6am, TOA_noon, Zenith_noon, Azimuth_noon, TOA_6pm, Zenith_6pm, Azimuth_6pm;
    
    ArrayList <Integer> day_list_Radiation = new ArrayList<Integer>();
    ArrayList <Integer> year_list_Radiation = new ArrayList<Integer>();
    ArrayList <Integer> month_list_Radiation = new ArrayList<Integer>();
    
    ArrayList <Double> TOA_midnight_list = new ArrayList<Double>();
    ArrayList <Double> TOA_6am_list = new ArrayList<Double>();
    ArrayList <Double> TOA_noon_list = new ArrayList<Double>();
    ArrayList <Double> TOA_6pm_list = new ArrayList<Double>();
    
    ArrayList <Double> zenith_midnight_list = new ArrayList<Double>();
    ArrayList <Double> zenith_6am_list = new ArrayList<Double>();
    ArrayList <Double> zenith_noon_list = new ArrayList<Double>();
    ArrayList <Double> zenith_6pm_list = new ArrayList<Double>();
    
    ArrayList <Double> azimuth_midnight_list = new ArrayList<Double>();
    ArrayList <Double> azimuth_6am_list = new ArrayList<Double>();
    ArrayList <Double> azimuth_noon_list = new ArrayList<Double>();
    ArrayList <Double> azimuth_6pm_list = new ArrayList<Double>();
    
    try
    {

      fr_radiation = new FileReader(f_radiation_data);

    }
    catch (FileNotFoundException fnfe3)
    {

      fnfe3.printStackTrace();

    }

    BufferedReader br_radiation = new BufferedReader(fr_radiation);

    try
    {
        
                String line;
 
		//br = new BufferedReader(new FileReader("C:/Users/Chris/Desktop/Java_module_tests/Input_met_data_TEST.csv"));
                
                br_radiation.readLine(); // Skips first line in file (useful if you have a header)
                
		while ((line = br_radiation.readLine()) != null) {
		   //System.out.println(line);
 
		   StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
 
		   while (stringTokenizer.hasMoreElements()) {
                                                
                          day_Radiation = Integer.parseInt(stringTokenizer.nextElement().toString());
                         month_Radiation = Integer.parseInt(stringTokenizer.nextElement().toString());
                         year_Radiation = Integer.parseInt(stringTokenizer.nextElement().toString());
                         TOA_midnight = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Zenith_midnight = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Azimuth_midnight = Double.parseDouble(stringTokenizer.nextElement().toString());
                         TOA_6am = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Zenith_6am = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Azimuth_6am = Double.parseDouble(stringTokenizer.nextElement().toString());
                         TOA_noon = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Zenith_noon = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Azimuth_noon = Double.parseDouble(stringTokenizer.nextElement().toString());
                         TOA_6pm = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Zenith_6pm = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Azimuth_6pm = Double.parseDouble(stringTokenizer.nextElement().toString());
                         
                         //************************Array List Integration *************
                         
                         day_list_Radiation.add(day_Radiation);
                         year_list_Radiation.add(year_Radiation);
                         month_list_Radiation.add(month_Radiation);
                         
                         TOA_midnight_list.add(TOA_midnight);
                         TOA_6am_list.add(TOA_6am);
                         TOA_noon_list.add(TOA_noon);
                         TOA_6pm_list.add(TOA_6pm);
                         zenith_midnight_list.add(Zenith_midnight);
                         zenith_6am_list.add(Zenith_6am);
                         zenith_noon_list.add(Zenith_noon);
                         zenith_6pm_list.add(Zenith_6pm);
                         azimuth_midnight_list.add(Azimuth_midnight);
                         azimuth_6am_list.add(Azimuth_6am);
                         azimuth_noon_list.add(Azimuth_noon);
                         azimuth_6pm_list.add(Azimuth_6pm);
                         
                         //************************ Array List Integration *************
                       
                         //************************ Set In Storage *************
                         store.setDayFile_Radiation(day_list_Radiation);
                         store.setYearFile_Radiation(year_list_Radiation);
                         store.setMonthFile_Radiation(month_list_Radiation);
                         
                         store.setTOA_midnight_list(TOA_midnight_list);
                         store.setTOA_6am_list(TOA_6am_list);
                         store.setTOA_noon_list(TOA_noon_list);
                         store.setTOA_6pm_list(TOA_6pm_list);
                         
                         store.setZenith_midnight_list(zenith_midnight_list);
                         store.setZenith_6am_list(zenith_6am_list);
                         store.setZenith_noon_list(zenith_noon_list);
                         store.setZenith_6pm_list(zenith_6pm_list);
                                                 
                         store.setAzimuth_midnight_list(azimuth_midnight_list);
                         store.setAzimuth_6am_list(azimuth_6am_list);
                         store.setAzimuth_noon_list(azimuth_noon_list);
                         store.setAzimuth_6pm_list(azimuth_6pm_list);
                                                  
                         // Just for running a single line for testing the radiation on slope listener
//                         store.setDay_Radiation(day_Radiation);
//                         store.setMonth_Radiation(month_Radiation);
//                         store.setYear_Radiation(year_Radiation);
//                         
//                         store.setTOA_midnight(TOA_midnight);
//                         store.setTOA_6am(TOA_6am);
//                         store.setTOA_noon(TOA_noon);
//                         store.setTOA_6pm(TOA_6pm);
//                                                  
//                         store.setZenith_midnight(Zenith_midnight);
//                         store.setZenith_6am(Zenith_6am);
//                         store.setZenith_noon(Zenith_noon);
//                         store.setZenith_6pm(Zenith_6pm);
//                         
//                         store.setAzimuth_midnight(Azimuth_midnight);
//                         store.setAzimuth_6am(Azimuth_6am);
//                         store.setAzimuth_noon(Azimuth_noon);
//                         store.setAzimuth_6pm(Azimuth_6pm);
//                                                  
//                         // Just for running a single line for testing the radiation on slope listener
//                         
//                         int dayStore = store.getDay_Radiation();
//                         double monthStore = store.getMonth_Radiation();
//                         int yearStore = store.getYear_Radiation();
//                         
//                         double TOA_midnight_store = store.getTOA_midnight();
//                         double TOA_6am_store = store.getTOA_6am();
//                         double TOA_noon_store = store.getTOA_noon();
//                         double TOA_6pm_store = store.getTOA_6pm();
//                                                  
//                         double Zenith_midnight_store = store.getZenith_midnight();
//                         double Zenith_6am_store = store.getZenith_6am();
//                         double Zenith_noon_store = store.getZenith_noon();
//                         double Zenith_6pm_store = store.getZenith_6pm();
//                         
//                         double Azimuth_midnight_store = store.getAzimuth_midnight();
//                         double Azimuth_6am_store = store.getAzimuth_6am();
//                         double Azimuth_noon_store = store.getAzimuth_noon();
//                         double Azimuth_6pm_store = store.getAzimuth_6pm();
                         
                         //************************ Set In Storage *************

                        //System.out.println(dayStore + "/" + monthStore + "/" + yearStore);  
                    
//                        System.out.println("TOA midnight: " + TOA_midnight_store);
//                        System.out.println("TOA 6am: " + TOA_6am_store);
//                        System.out.println("TOA noon: " + TOA_noon_store);
//                        System.out.println("TOA 6pm: " + TOA_6pm_store);
//                            
//                        System.out.println("Zenith midnight: " + Zenith_midnight_store);
//                        System.out.println("Zenith 6am: " + Zenith_6am_store);
//                        System.out.println("Zenith noon: " + Zenith_noon_store);
//                        System.out.println("Zenith 6pm: " + Zenith_6pm_store);
//                          
//                        System.out.println("Azimuth midnight: " + Azimuth_midnight_store);
//                        System.out.println("Azimuth 6am: " + Azimuth_6am_store);
//                        System.out.println("Azimuth noon: " + Azimuth_noon_store);
//                        System.out.println("Azimuth 6pm: " + Azimuth_6pm_store);
                              
                         
                         
		   }
                   
		}
 
    } catch(Exception e_br_Radiation){
        
        System.out.println("Met (Radiation) file read in fail");
    
    }
      
    System.out.println("Radiation data uploaded");
                         
                         if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
                         
      //*********************************************** HILLSHADE *************
      
      //***** MIDNIGHT
      
      //January HS midnight
      
      File f_jan_HS = new File(key_hsm1);
      GRIDJava2 gj3 = new GRIDJava2(f_jan_HS, true, store);
      double jan_HS_data[][] = gj3.getTwoDdoubleArray();
      store.setHillshade_Jan_midnight(jan_HS_data);
      System.out.println("January hillshade surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //February HS midnight
      
      File f_feb_HS = new File(key_hsm2);
      GRIDJava2 gj4 = new GRIDJava2(f_feb_HS, true, store);
      double feb_HS_data[][] = gj4.getTwoDdoubleArray();
      store.setHillshade_Feb_midnight(feb_HS_data);
      System.out.println("February hillshade surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //March HS midnight
            
      File f_mar_HS = new File(key_hsm3);
      GRIDJava2 gj5 = new GRIDJava2(f_mar_HS, true, store);
      double mar_HS_data[][] = gj5.getTwoDdoubleArray();
      store.setHillshade_March_midnight(mar_HS_data);
      System.out.println("March hillshade surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //April HS midnight
            
      File f_april_HS = new File(key_hsm4);
      GRIDJava2 gj6 = new GRIDJava2(f_april_HS, true, store);
      double april_HS_data[][] = gj6.getTwoDdoubleArray();
      store.setHillshade_April_midnight(april_HS_data);
      System.out.println("April hillshade surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //May HS midnight
            
      File f_may_HS = new File(key_hsm5);
      GRIDJava2 gj7 = new GRIDJava2(f_may_HS, true, store);
      double may_HS_data[][] = gj7.getTwoDdoubleArray();
      store.setHillshade_May_midnight(may_HS_data);
      System.out.println("May hillshade surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //June HS midnight
            
      File f_june_HS = new File(key_hsm6);
      GRIDJava2 gj8 = new GRIDJava2(f_june_HS, true, store);
      double june_HS_data[][] = gj8.getTwoDdoubleArray();
      store.setHillshade_June_midnight(june_HS_data);
      System.out.println("June hillshade surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //July HS midnight
            
      File f_july_HS = new File(key_hsm7);
      GRIDJava2 gj9 = new GRIDJava2(f_july_HS, true, store);
      double july_HS_data[][] = gj9.getTwoDdoubleArray();
      store.setHillshade_July_midnight(july_HS_data);
      System.out.println("July hillshade surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //August HS midnight
            
      File f_aug_HS = new File(key_hsm8);
      GRIDJava2 gj10 = new GRIDJava2(f_aug_HS, true, store);
      double aug_HS_data[][] = gj10.getTwoDdoubleArray();
      store.setHillshade_August_midnight(aug_HS_data);
      System.out.println("August hillshade surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //September HS midnight
            
      File f_sept_HS = new File(key_hsm9);
      GRIDJava2 gj11 = new GRIDJava2(f_sept_HS, true, store);
      double sept_HS_data[][] = gj11.getTwoDdoubleArray();
      store.setHillshade_Sept_midnight(sept_HS_data);
      System.out.println("September hillshade surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //October HS midnight
            
      File f_oct_HS = new File(key_hsm10);
      GRIDJava2 gj12 = new GRIDJava2(f_oct_HS, true, store);
      double oct_HS_data[][] = gj12.getTwoDdoubleArray();
      store.setHillshade_Oct_midnight(oct_HS_data);
      System.out.println("October hillshade surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      
      //November HS midnight
            
      File f_nov_HS = new File(key_hsm11);
      GRIDJava2 gj13 = new GRIDJava2(f_nov_HS, true, store);
      double nov_HS_data[][] = gj13.getTwoDdoubleArray();
      store.setHillshade_Nov_midnight(nov_HS_data);
      System.out.println("November hillshade surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //December HS midnight
            
      File f_dec_HS = new File(key_hsm12);
      GRIDJava2 gj14 = new GRIDJava2(f_dec_HS, true, store);
      double dec_HS_data[][] = gj14.getTwoDdoubleArray();
      store.setHillshade_Dec_midnight(dec_HS_data);
      System.out.println("December hillshade surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //*** 6AM
      
      //January HS_6am 
      
      File f_jan_HS_6am = new File(key_hs6am1);
      GRIDJava2 gj3b = new GRIDJava2(f_jan_HS_6am, true, store);
      double jan_HS_6am_data[][] = gj3b.getTwoDdoubleArray();
      store.setHillshade_Jan_6am(jan_HS_6am_data);
      System.out.println("January hillshade 6am surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //February HS_6am 6am
      
      File f_feb_HS_6am = new File(key_hs6am2);
      GRIDJava2 gj4b = new GRIDJava2(f_feb_HS_6am, true, store);
      double feb_HS_6am_data[][] = gj4b.getTwoDdoubleArray();
      store.setHillshade_Feb_6am(feb_HS_6am_data);
      System.out.println("February hillshade 6am surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //March HS_6am 6am
            
      File f_mar_HS_6am = new File(key_hs6am3);
      GRIDJava2 gj5b = new GRIDJava2(f_mar_HS_6am, true, store);
      double mar_HS_6am_data[][] = gj5b.getTwoDdoubleArray();
      store.setHillshade_March_6am(mar_HS_6am_data);
      System.out.println("March hillshade 6am surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //April HS_6am 6am
            
      File f_april_HS_6am = new File(key_hs6am4);
      GRIDJava2 gj6b = new GRIDJava2(f_april_HS_6am, true, store);
      double april_HS_6am_data[][] = gj6b.getTwoDdoubleArray();
      store.setHillshade_April_6am(april_HS_6am_data);
      System.out.println("April hillshade 6am surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //May HS_6am 6am
            
      File f_may_HS_6am = new File(key_hs6am5);
      GRIDJava2 gj7b = new GRIDJava2(f_may_HS_6am, true, store);
      double may_HS_6am_data[][] = gj7b.getTwoDdoubleArray();
      store.setHillshade_May_6am(may_HS_6am_data);
      System.out.println("May hillshade 6am surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //June HS_6am 6am
            
      File f_june_HS_6am = new File(key_hs6am6);
      GRIDJava2 gj8b = new GRIDJava2(f_june_HS_6am, true, store);
      double june_HS_6am_data[][] = gj8b.getTwoDdoubleArray();
      store.setHillshade_June_6am(june_HS_6am_data);
      System.out.println("June hillshade 6am surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //July HS_6am 6am
            
      File f_july_HS_6am = new File(key_hs6am7);
      GRIDJava2 gj9b = new GRIDJava2(f_july_HS_6am, true, store);
      double july_HS_6am_data[][] = gj9b.getTwoDdoubleArray();
      store.setHillshade_July_6am(july_HS_6am_data);
      System.out.println("July hillshade 6am surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //August HS_6am 6am
            
      File f_aug_HS_6am = new File(key_hs6am8);
      GRIDJava2 gj10b = new GRIDJava2(f_aug_HS_6am, true, store);
      double aug_HS_6am_data[][] = gj10b.getTwoDdoubleArray();
      store.setHillshade_August_6am(aug_HS_6am_data);
      System.out.println("August hillshade 6am surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //September HS_6am 6am
            
      File f_sept_HS_6am = new File(key_hs6am9);
      GRIDJava2 gj11b = new GRIDJava2(f_sept_HS_6am, true, store);
      double sept_HS_6am_data[][] = gj11b.getTwoDdoubleArray();
      store.setHillshade_Sept_6am(sept_HS_6am_data);
      System.out.println("September hillshade 6am surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //October HS_6am 6am
            
      File f_oct_HS_6am = new File(key_hs6am10);
      GRIDJava2 gj12b = new GRIDJava2(f_oct_HS_6am, true, store);
      double oct_HS_6am_data[][] = gj12b.getTwoDdoubleArray();
      store.setHillshade_Oct_6am(oct_HS_6am_data);
      System.out.println("October hillshade 6am surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //November HS_6am 6am
            
      File f_nov_HS_6am = new File(key_hs6am11);
      GRIDJava2 gj13b = new GRIDJava2(f_nov_HS_6am, true, store);
      double nov_HS_6am_data[][] = gj13b.getTwoDdoubleArray();
      store.setHillshade_Nov_6am(nov_HS_6am_data);
      System.out.println("November hillshade 6am surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //December HS_6am 6am
            
      File f_dec_HS_6am = new File(key_hs6am12);
      GRIDJava2 gj14b = new GRIDJava2(f_dec_HS_6am, true, store);
      double dec_HS_6am_data[][] = gj14b.getTwoDdoubleArray();
      store.setHillshade_Dec_6am(dec_HS_6am_data);
      System.out.println("December hillshade 6am surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
     
      //**** Noon
            
      //January HS_noon 
      
      File f_jan_HS_noon = new File(key_hsnoon1);
      GRIDJava2 gj3c = new GRIDJava2(f_jan_HS_noon, true, store);
      double jan_HS_noon_data[][] = gj3c.getTwoDdoubleArray();
      store.setHillshade_Jan_noon(jan_HS_noon_data);
      System.out.println("January hillshade noon surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //February HS_noon
      
      File f_fec_HS_noon = new File(key_hsnoon2);
      GRIDJava2 gj4c = new GRIDJava2(f_fec_HS_noon, true, store);
      double feb_HS_noon_data[][] = gj4c.getTwoDdoubleArray();
      store.setHillshade_Feb_noon(feb_HS_noon_data);
      System.out.println("February hillshade noon surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //March HS_noon
            
      File f_mar_HS_noon = new File(key_hsnoon3);
      GRIDJava2 gj5c = new GRIDJava2(f_mar_HS_noon, true, store);
      double mar_HS_noon_data[][] = gj5c.getTwoDdoubleArray();
      store.setHillshade_March_noon(mar_HS_noon_data);
      System.out.println("March hillshade noon surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //April HS_noon
            
      File f_april_HS_noon = new File(key_hsnoon4);
      GRIDJava2 gj6c = new GRIDJava2(f_april_HS_noon, true, store);
      double april_HS_noon_data[][] = gj6c.getTwoDdoubleArray();
      store.setHillshade_April_noon(april_HS_noon_data);
      System.out.println("April hillshade noon surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //May HS_noon
            
      File f_may_HS_noon = new File(key_hsnoon5);
      GRIDJava2 gj7c = new GRIDJava2(f_may_HS_noon, true, store);
      double may_HS_noon_data[][] = gj7c.getTwoDdoubleArray();
      store.setHillshade_May_noon(may_HS_noon_data);
      System.out.println("May hillshade noon surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //June HS_noon
            
      File f_june_HS_noon = new File(key_hsnoon6);
      GRIDJava2 gj8c = new GRIDJava2(f_june_HS_noon, true, store);
      double june_HS_noon_data[][] = gj8c.getTwoDdoubleArray();
      store.setHillshade_June_noon(june_HS_noon_data);
      System.out.println("June hillshade noon surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //July HS_noon
            
      File f_july_HS_noon = new File(key_hsnoon7);
      GRIDJava2 gj9c = new GRIDJava2(f_july_HS_noon, true, store);
      double july_HS_noon_data[][] = gj9c.getTwoDdoubleArray();
      store.setHillshade_July_noon(july_HS_noon_data);
      System.out.println("July hillshade noon surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //August HS_noon 
            
      File f_aug_HS_noon = new File(key_hsnoon8);
      GRIDJava2 gj10c = new GRIDJava2(f_aug_HS_noon, true, store);
      double aug_HS_noon_data[][] = gj10c.getTwoDdoubleArray();
      store.setHillshade_August_noon(aug_HS_noon_data);
      System.out.println("August hillshade noon surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //September HS_noon 
            
      File f_sept_HS_noon = new File(key_hsnoon9);
      GRIDJava2 gj11c = new GRIDJava2(f_sept_HS_noon, true, store);
      double sept_HS_noon_data[][] = gj11c.getTwoDdoubleArray();
      store.setHillshade_Sept_noon(sept_HS_noon_data);
      System.out.println("September hillshade noon surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //October HS_noon 
            
      File f_oct_HS_noon = new File(key_hsnoon10);
      GRIDJava2 gj12c = new GRIDJava2(f_oct_HS_noon, true, store);
      double oct_HS_noon_data[][] = gj12c.getTwoDdoubleArray();
      store.setHillshade_Oct_noon(oct_HS_noon_data);
      System.out.println("October hillshade noon surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //November HS_noon 
            
      File f_nov_HS_noon = new File(key_hsnoon11);
      GRIDJava2 gj13c = new GRIDJava2(f_nov_HS_noon, true, store);
      double nov_HS_noon_data[][] = gj13c.getTwoDdoubleArray();
      store.setHillshade_Nov_noon(nov_HS_noon_data);
      System.out.println("November hillshade noon surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //December HS_noon 
            
      File f_dec_HS_noon = new File(key_hsnoon12);
      GRIDJava2 gj14c = new GRIDJava2(f_dec_HS_noon, true, store);
      double dec_HS_noon_data[][] = gj14c.getTwoDdoubleArray();
      store.setHillshade_Dec_noon(dec_HS_noon_data);
      System.out.println("December hillshade noon surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //**** 6PM
      
      //January HS_6pm 
      
      File f_jan_HS_6pm = new File(key_hs6pm1);
      GRIDJava2 gj3d = new GRIDJava2(f_jan_HS_6pm, true, store);
      double jan_HS_6pm_data[][] = gj3d.getTwoDdoubleArray();
      store.setHillshade_Jan_6pm(jan_HS_6pm_data);
      System.out.println("January hillshade 6pm surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //February HS_6pm
      
      File f_fed_HS_6pm = new File(key_hs6pm2);
      GRIDJava2 gj4d = new GRIDJava2(f_fed_HS_6pm, true, store);
      double feb_HS_6pm_data[][] = gj4d.getTwoDdoubleArray();
      store.setHillshade_Feb_6pm(feb_HS_6pm_data);
      System.out.println("Fedruary hillshade 6pm surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //March HS_6pm
            
      File f_mar_HS_6pm = new File(key_hs6pm3);
      GRIDJava2 gj5d = new GRIDJava2(f_mar_HS_6pm, true, store);
      double mar_HS_6pm_data[][] = gj5d.getTwoDdoubleArray();
      store.setHillshade_March_6pm(mar_HS_6pm_data);
      System.out.println("March hillshade 6pm surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //April HS_6pm
            
      File f_april_HS_6pm = new File(key_hs6pm4);
      GRIDJava2 gj6d = new GRIDJava2(f_april_HS_6pm, true, store);
      double april_HS_6pm_data[][] = gj6d.getTwoDdoubleArray();
      store.setHillshade_April_6pm(april_HS_6pm_data);
      System.out.println("April hillshade 6pm surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //May HS_6pm
            
      File f_may_HS_6pm = new File(key_hs6pm5);
      GRIDJava2 gj7d = new GRIDJava2(f_may_HS_6pm, true, store);
      double may_HS_6pm_data[][] = gj7d.getTwoDdoubleArray();
      store.setHillshade_May_6pm(may_HS_6pm_data);
      System.out.println("May hillshade 6pm surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //June HS_6pm
            
      File f_june_HS_6pm = new File(key_hs6pm6);
      GRIDJava2 gj8d = new GRIDJava2(f_june_HS_6pm, true, store);
      double june_HS_6pm_data[][] = gj8d.getTwoDdoubleArray();
      store.setHillshade_June_6pm(june_HS_6pm_data);
      System.out.println("June hillshade 6pm surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //July HS_6pm
            
      File f_july_HS_6pm = new File(key_hs6pm7);
      GRIDJava2 gj9d = new GRIDJava2(f_july_HS_6pm, true, store);
      double july_HS_6pm_data[][] = gj9d.getTwoDdoubleArray();
      store.setHillshade_July_6pm(july_HS_6pm_data);
      System.out.println("July hillshade 6pm surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //August HS_6pm 
            
      File f_aug_HS_6pm = new File(key_hs6pm8);
      GRIDJava2 gj10d = new GRIDJava2(f_aug_HS_6pm, true, store);
      double aug_HS_6pm_data[][] = gj10d.getTwoDdoubleArray();
      store.setHillshade_August_6pm(aug_HS_6pm_data);
      System.out.println("August hillshade 6pm surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //September HS_6pm 
            
      File f_sept_HS_6pm = new File(key_hs6pm9);
      GRIDJava2 gj11d = new GRIDJava2(f_sept_HS_6pm, true, store);
      double sept_HS_6pm_data[][] = gj11d.getTwoDdoubleArray();
      store.setHillshade_Sept_6pm(sept_HS_6pm_data);
      System.out.println("September hillshade 6pm surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //October HS_6pm 
            
      File f_odt_HS_6pm = new File(key_hs6pm10);
      GRIDJava2 gj12d = new GRIDJava2(f_odt_HS_6pm, true, store);
      double oct_HS_6pm_data[][] = gj12d.getTwoDdoubleArray();
      store.setHillshade_Oct_6pm(oct_HS_6pm_data);
      System.out.println("October hillshade 6pm surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //November HS_6pm 
            
      File f_nov_HS_6pm = new File(key_hs6pm11);
      GRIDJava2 gj13d = new GRIDJava2(f_nov_HS_6pm, true, store);
      double nov_HS_6pm_data[][] = gj13d.getTwoDdoubleArray();
      store.setHillshade_Nov_6pm(nov_HS_6pm_data);
      System.out.println("November hillshade 6pm surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
      //December HS_6pm 
            
      File f_dec_HS_6pm = new File(key_hs6pm12);
      GRIDJava2 gj14d = new GRIDJava2(f_dec_HS_6pm, true, store);
      double dec_HS_6pm_data[][] = gj14d.getTwoDdoubleArray();
      store.setHillshade_Dec_6pm(dec_HS_6pm_data);
      System.out.println("December hillshade 6pm surface stored");
      
      if(Memory_usage == 1){
      
      memory = runtime.totalMemory() - runtime.freeMemory();
      System.out.println("Total memory available: " + runtime.totalMemory() + " (bytes)");
      //System.out.println("Maximum memory JVM will attempt to use: " + runtime.maxMemory() + " (bytes)");
      System.out.println("Used memory is bytes: " + memory + " (bytes)");
      
      }
      
//      //Snow surface 2007-2008
//            
//      File f_snow_0708 = new File(key_17);
//      GRIDJava2 gj17 = new GRIDJava2(f_snow_0708, true, store);
//      double snow_0708_data[][] = gj17.getTwoDdoubleArray();
//      store.set_snow_surface_0708(snow_0708_data);
//      System.out.println("snow_0708_data surface stored");
//      
//      //Snow surface 2008-2009
//            
//      File f_snow_0809 = new File(key_18);
//      GRIDJava2 gj18 = new GRIDJava2(f_snow_0809, true, store);
//      double snow_0809_data[][] = gj18.getTwoDdoubleArray();
//      store.set_snow_surface_0809(snow_0809_data);
//      System.out.println("snow_0809_data surface stored");
//      
//      //Snow surface 2009-2010
//            
//      File f_snow_0910 = new File(key_19);
//      GRIDJava2 gj19 = new GRIDJava2(f_snow_0910, true, store);
//      double snow_0910_data[][] = gj19.getTwoDdoubleArray();
//      store.set_snow_surface_0910(snow_0910_data);
//      System.out.println("snow_0910_data surface stored");
//      
//      //Snow surface 2010-2011
//            
//      File f_snow_1011 = new File(key_20);
//      GRIDJava2 gj20 = new GRIDJava2(f_snow_1011, true, store);
//      double snow_1011_data[][] = gj20.getTwoDdoubleArray();
//      store.set_snow_surface_1011(snow_1011_data);
//      System.out.println("snow_1011_data surface stored");
      
      //********************************
      
      System.out.println("Elevation path: " + key_1);
      System.out.println("Thickness path: " + key_2);
      System.out.println("Met data path: " + key_3);
      System.out.println("WinterSnow data path: " + key_4);
      System.out.println("Radiation data path: " + key_5);
      
//      System.out.println("Jan HS path: " + key_3);
//      System.out.println("Feb HS path: " + key_4);
//      System.out.println("Mar HS path: " + key_5);
//      System.out.println("Apr HS path: " + key_6);
//      System.out.println("May HS path: " + key_7);
//      System.out.println("Jun HS path: " + key_8);
//      System.out.println("Jul HS path: " + key_9);
//      System.out.println("Aug HS path: " + key_10);
//      System.out.println("Sep HS path: " + key_11);
//      System.out.println("Oct HS path: " + key_12);
//      System.out.println("Nov HS path: " + key_13);
//      System.out.println("Dec HS path: " + key_14);
//      //b
//      System.out.println("Jan HS path: " + key_3b);
//      System.out.println("Feb HS path: " + key_4b);
//      System.out.println("Mar HS path: " + key_5b);
//      System.out.println("Apr HS path: " + key_6b);
//      System.out.println("May HS path: " + key_7b);
//      System.out.println("Jun HS path: " + key_8b);
//      System.out.println("Jul HS path: " + key_9b);
//      System.out.println("Aug HS path: " + key_10b);
//      System.out.println("Sep HS path: " + key_11b);
//      System.out.println("Oct HS path: " + key_12b);
//      System.out.println("Nov HS path: " + key_13b);
//      System.out.println("Dec HS path: " + key_14b);
//      //c
//      System.out.println("Jan HS path: " + key_3c);
//      System.out.println("Feb HS path: " + key_4c);
//      System.out.println("Mar HS path: " + key_5c);
//      System.out.println("Apr HS path: " + key_6c);
//      System.out.println("May HS path: " + key_7c);
//      System.out.println("Jun HS path: " + key_8c);
//      System.out.println("Jul HS path: " + key_9c);
//      System.out.println("Aug HS path: " + key_10c);
//      System.out.println("Sep HS path: " + key_11c);
//      System.out.println("Oct HS path: " + key_12c);
//      System.out.println("Nov HS path: " + key_13c);
//      System.out.println("Dec HS path: " + key_14c);
//      //d
//      System.out.println("Jan HS path: " + key_3d);
//      System.out.println("Feb HS path: " + key_4d);
//      System.out.println("Mar HS path: " + key_5d);
//      System.out.println("Apr HS path: " + key_6d);
//      System.out.println("May HS path: " + key_7d);
//      System.out.println("Jun HS path: " + key_8d);
//      System.out.println("Jul HS path: " + key_9d);
//      System.out.println("Aug HS path: " + key_10d);
//      System.out.println("Sep HS path: " + key_11d);
//      System.out.println("Oct HS path: " + key_12d);
//      System.out.println("Nov HS path: " + key_13d);
//      System.out.println("Dec HS path: " + key_14d);
      //
      
      //
//      System.out.println("Snow 07-08 path: " + key_17);
//      System.out.println("Snow 08-09 path: " + key_18);
//      System.out.println("Snow 09-10 path: " + key_19);
//      System.out.println("Snow 10-11 path: " + key_20);
      
            
  }
}

