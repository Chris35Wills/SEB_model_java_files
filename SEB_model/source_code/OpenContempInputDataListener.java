package energymodels;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

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
 * Summary (8/1/12) Specifically used for reading model input variables for 
 * contemporary model runs - Historical reader is separate as it comprises of 
 * separate meteorological read ins and solar radiation treatment.
 * 
 * Once running the model, these lists will be looped and within each loop, 
 * variables will be set from the appropriate lists. The set/get methods 
 * exist in Storage.java.
 * 
 * Test file to be used with this: 
 * " "
 * 
 * @author Chris (9/1/13)
 */

public class OpenContempInputDataListener implements ActionListener {
  private GUIPanel panel = null;
  private Storage store = null;
  
  OpenContempInputDataListener(GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {

    System.out.println("Have you plugged in a precipitation season value (1/2) yet? If not, then this might not work....");
    
    
    FileDialog fd = new FileDialog(new Frame(), 
            "Open model input variables (.csv file)", FileDialog.LOAD);
    fd.setVisible(true);
    File f = new File (fd.getDirectory() + fd.getFile());
    
    if((fd.getDirectory()== null)||(fd.getFile() == null))
    {
    System.out.println("Contemporary met. input file not uploaded");
    return;
    }
    
    else{
	
    FileReader fr = null;
    double year, Month, day, Precip_season;
    double temp,Precip, Tau_estimate, TOA_midnight, Zenith_midnight, Azimuth_midnight, TOA_6am, Zenith_6am, 
                Azimuth_6am, TOA_noon, Zenith_noon, Azimuth_noon, TOA_6pm, Zenith_6pm, Azimuth_6pm;
    String MonthText = null;
    
    ArrayList <Double> day_list = new ArrayList<Double>();
    ArrayList <Double> year_list = new ArrayList<Double>();
    ArrayList <Double> month_list = new ArrayList<Double>();
    ArrayList <Double> temp_list = new ArrayList<Double>();
    ArrayList <Double> precip_list = new ArrayList<Double>();
    ArrayList <Double> precip_season_list = new ArrayList<Double>();
    ArrayList <Double> tau_estimate_list = new ArrayList<Double>();
    
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

      fr = new FileReader(f);

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
                                                
                         day = Integer.parseInt(stringTokenizer.nextElement().toString());
                         Month = Integer.parseInt(stringTokenizer.nextElement().toString());
                         year = Integer.parseInt(stringTokenizer.nextElement().toString());
                         temp = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Precip_season = Integer.parseInt(stringTokenizer.nextElement().toString());
                         Precip = Double.parseDouble(stringTokenizer.nextElement().toString());
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
                         Tau_estimate = Double.parseDouble(stringTokenizer.nextElement().toString());
                         
                         //************************Array List Integration *************
                         
                         day_list.add(day);
                         year_list.add(year);
                         month_list.add(Month);
                         temp_list.add(temp);
                         precip_list.add(Precip);
                         precip_season_list.add(Precip_season);
                         tau_estimate_list.add(Tau_estimate);
                         
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
                         store.setDayList(day_list);
                         store.setYearFile(year_list);
                         store.setMonthFile(month_list);
                         store.setTemperatureFile(temp_list);
                         store.setPrecipFile(precip_list);
                         store.setPrecipSeasonList(precip_season_list);
                         store.setTauEstimateList(tau_estimate_list);
                         
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
                         store.setDay(day);
                         store.setYear(year);
                         store.setMonth(Month);
                         store.setTemp(temp);
                         store.setPrecip(Precip);
                         store.setPrecipSeason(Precip_season);
                                              
                         store.setTauEstimate(Tau_estimate);
                         
                         store.setTOA_midnight(TOA_midnight);
                         store.setTOA_6am(TOA_6am);
                         store.setTOA_noon(TOA_noon);
                         store.setTOA_6pm(TOA_6pm);
                                                  
                         store.setZenith_midnight(Zenith_midnight);
                         store.setZenith_6am(Zenith_6am);
                         store.setZenith_noon(Zenith_noon);
                         store.setZenith_6pm(Zenith_6pm);
                         
                         store.setAzimuth_midnight(Azimuth_midnight);
                         store.setAzimuth_6am(Azimuth_6am);
                         store.setAzimuth_noon(Azimuth_noon);
                         store.setAzimuth_6pm(Azimuth_6pm);

                         
                         // Just for running a single line for testing the radiation on slope listener
                         Double dayStore = store.getDay();
                         Double yearStore = store.getYear();
                         Double monthStore = store.getMonth();
                         double tempStore = store.getTemp();
                         double precipStore = store.getPrecip();
                         Double precipSeasonStore = store.getPrecipSeason();
                                                  
                         double tauEstimateStore = store.getTauEstimate();
                         
                         double TOA_midnight_store = store.getTOA_midnight();
                         double TOA_6am_store = store.getTOA_6am();
                         double TOA_noon_store = store.getTOA_noon();
                         double TOA_6pm_store = store.getTOA_6pm();
                                                  
                         double Zenith_midnight_store = store.getZenith_midnight();
                         double Zenith_6am_store = store.getZenith_6am();
                         double Zenith_noon_store = store.getZenith_noon();
                         double Zenith_6pm_store = store.getZenith_6pm();
                         
                         double Azimuth_midnight_store = store.getAzimuth_midnight();
                         double Azimuth_6am_store = store.getAzimuth_6am();
                         double Azimuth_noon_store = store.getAzimuth_noon();
                         double Azimuth_6pm_store = store.getAzimuth_6pm();
                                                  
                         //************************ Set In Storage *************
                                   
                    System.out.println("Day: " + dayStore);
                    System.out.println("Year: " + yearStore);
                    System.out.println("Month: " + monthStore);
                    System.out.println("Temp: " + tempStore); 
                    System.out.println("Precipitation: " + precipStore);
                    System.out.println("Precipitation season : " + precipSeasonStore);
                    
                    System.out.println("Tau estimate: " + tauEstimateStore);
                    
                    System.out.println("TOA midnight: " + TOA_midnight_store);
                    System.out.println("TOA 6am: " + TOA_6am_store);
                    System.out.println("TOA noon: " + TOA_noon_store);
                    System.out.println("TOA 6pm: " + TOA_6pm_store);
                            
                    System.out.println("Zenith midnight: " + Zenith_midnight_store);
                    System.out.println("Zenith 6am: " + Zenith_6am_store);
                    System.out.println("Zenith noon: " + Zenith_noon_store);
                    System.out.println("Zenith 6pm: " + Zenith_6pm_store);
                          
                    System.out.println("Azimuth midnight: " + Azimuth_midnight_store);
                    System.out.println("Azimuth 6am: " + Azimuth_6am_store);
                    System.out.println("Azimuth noon: " + Azimuth_noon_store);
                    System.out.println("Azimuth 6pm: " + Azimuth_6pm_store);
                              
		   }
                   
		}
 
      }
    
    catch (IOException d) {
		d.printStackTrace();
	}
        finally {
		try {
                        if (br != null)
				br.close();
 		} catch (IOException ex) {
			ex.printStackTrace();
		} try {
			if (fr != null)
				fr.close();
 
		} catch (IOException ex) {
			ex.printStackTrace();       
                
	}
      
      try
      {

        fr = new FileReader(f);

      }
      catch (FileNotFoundException fnfe)
      {

        fnfe.printStackTrace();

      }

     //*****************************FILE WRITING ROUTINE********************************************   
      
    /**
     *  This next bit will simply save the code to a hardwired file so it can be checked to see if it worked
     **/     
             
    //File f2 = new File("F:/Melt_Modelling/Model_outputs/TEST_Contemp_met_input_file_10.1.12.txt");
    File f2 = new File("C:/Users/Chris/Desktop/TEST_CONTEMP_MET_FILE__DELETE_ME.txt");
    
    
    FileWriter fw = null;
  
    try
    {

      fw = new FileWriter(f2, true);

    }
    catch (IOException ioe)
    {

      ioe.printStackTrace();

    }

    BufferedWriter bw = new BufferedWriter(fw);
    
    try
    {  
      
      bw.write("Day, Month, Year, Temp., Precip. Season, Precip., TOA_midnight, Zenith_midnight, Azimuth_midnight, TOA_6am, Zenith_6am, Azimuth_6am, TOA_noon, Zenith_noon, Azimuth_noon, TOA_6pm, Zenith_6pm, Azimuth_6pm, Tau estimate");
     
      bw.newLine();
      
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
            
      
      while(iterator0.hasNext()){

                        String day_print = Double.toString(iterator0.next());
                        String month_print = Double.toString(iterator1.next());
                        String year_print = Double.toString(iterator2.next());
                        String temp_print = Double.toString(iterator3.next());
                        String precip_print = Double.toString(iterator4.next());
                        String precip_season_print = Double.toString(iterator4b.next());
                        
                        String TOA_midnight_print = Double.toString(iterator5.next());
                        String TOA_6am_print = Double.toString(iterator6.next());
                        String TOA_noon_print = Double.toString(iterator7.next());
                        String TOA_6pm_print = Double.toString(iterator8.next());
                        
                        String azimuth_midnight_print = Double.toString(iterator9.next());
                        String azimuth_6am_print = Double.toString(iterator10.next());
                        String azimuth_noon_print = Double.toString(iterator11.next());
                        String azimuth_6pm_print = Double.toString(iterator12.next());
                        
                        String zenith_midnight_print = Double.toString(iterator13.next());
                        String zenith_6am_print = Double.toString(iterator14.next());
                        String zenith_noon_print = Double.toString(iterator15.next());
                        String zenith_6pm_print = Double.toString(iterator16.next());
                        
                        String Tau_estimate_print = Double.toString(iterator17.next());
                                              
                        bw.write(day_print);
                        bw.write(",");
                        bw.write(month_print);
                        bw.write(",");
                        bw.write(year_print);
                        bw.write(",");
                        bw.write(temp_print);
                        bw.write(",");
                        bw.write(precip_print);
                        bw.write(",");
                        bw.write(precip_season_print);
                        bw.write(",");
                        bw.write(TOA_midnight_print);
                        bw.write(",");
                        bw.write(zenith_midnight_print);
                        bw.write(",");
                        bw.write(azimuth_midnight_print);
                        bw.write(",");
                        bw.write(TOA_6am_print);
                        bw.write(",");
                        bw.write(zenith_6am_print);
                        bw.write(",");
                        bw.write(azimuth_6am_print);
                        bw.write(",");
                        bw.write(TOA_noon_print);
                        bw.write(",");
                        bw.write(zenith_noon_print);
                        bw.write(",");
                        bw.write(azimuth_noon_print);
                        bw.write(",");
                        bw.write(TOA_6pm_print);
                        bw.write(",");
                        bw.write(zenith_6pm_print);
                        bw.write(",");
                        bw.write(azimuth_6pm_print);
                        bw.write(",");
                        bw.write(Tau_estimate_print);
                        bw.newLine();
	     }
              
      System.out.println("Contemporary Input met .csv test file written");            
      bw.close();
      fw.close();

    }
    catch (IOException ioe)
    {
      System.out.println(ioe.toString());
    }
  
    //*****************************FILE WRITING ROUTINE********************************************   
    System.out.println("Copy of contemporary model input data: " + f2.getAbsolutePath());
    }
   }     
  }
}

