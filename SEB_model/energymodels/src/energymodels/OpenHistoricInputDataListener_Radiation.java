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
 * Summary (8/3/13) Reads in the radiation file for the historical model run.
 * 
 * Input file format: 
 * |Day|Month|Year|MidnightTOA|MidnightZenith|MidnightAzimuth|6amTOA|
 * |6amZenith|6amAzimuth|NoonTOA|NoonZenith|NoonAzimuth|6pmTOA|6pmZenith|
 * |6pmAzimuth|
 * 
 * @author Chris (8/3/13)
 */

public class OpenHistoricInputDataListener_Radiation implements ActionListener {
  private GUIPanel panel = null;
  private Storage store = null;
  
  OpenHistoricInputDataListener_Radiation(GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    
      System.out.println("Historic input data (radiation) button working");
      
       FileDialog fd = new FileDialog(new Frame(), 
            "Open historic model input variables (.csv file) [Main]", FileDialog.LOAD);
    fd.setVisible(true);
    File f = new File (fd.getDirectory() + fd.getFile());
    
    if((fd.getDirectory()== null)||(fd.getFile() == null))
    {
    System.out.println("Main historical met. input file not uploaded");
    return;
    }
    else{
    
    FileReader fr = null;
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
                         store.setDay_Radiation(day_Radiation);
                         store.setMonth_Radiation(month_Radiation);
                         store.setYear_Radiation(year_Radiation);
                         
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
                         
                         int dayStore = store.getDay_Radiation();
                         double monthStore = store.getMonth_Radiation();
                         int yearStore = store.getYear_Radiation();
                         
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

                        System.out.println(dayStore + "/" + monthStore + "/" + yearStore);  
                    
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
             
    
   File f2 = new File("F:/Melt_modelling/Historical_model_outputs/Historical_Radiation_FILE_upload.txt");
   
    
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
      
      bw.write("Day, Month, Year, midnight_TOA, midnight_Zenith, "
              + "midnight_Azimuth, 6am_TOA, 6am_Zenith, 6am_Azimuth, "
              + "noon_TOA, noon_Zenith, noon_Azimuth, 6pm_TOA, 6pm_Zenith, "
              + "6pm_Azimuth ");
     
      bw.newLine();
      
      Iterator<Integer> iterator0 = store.getDayFile_Radiation().iterator();
      Iterator<Integer> iterator1 = store.getMonthFile_Radiation().iterator();
      Iterator<Integer> iterator2 = store.getYearFile_Radiation().iterator();   
      
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
      
      while(iterator1.hasNext()){

                        String day_print = Integer.toString(iterator0.next());
                        String month_print = Double.toString(iterator1.next());
                        String year_print = Double.toString(iterator2.next());
                        
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
                        
                        bw.write(day_print);
                        bw.write(",");
                        bw.write(month_print);
                        bw.write(",");
                        bw.write(year_print);
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
                        bw.newLine();
	     }
              
      System.out.println("Historical Input (Radiation) .csv test file written");            
      bw.close();
      fw.close();

    }
    catch (IOException ioe)
    {
      System.out.println(ioe.toString());
    }
  
    //*****************************FILE WRITING ROUTINE********************************************   
    System.out.println("Copy of historical (Radiation) model input data: " + f2.getAbsolutePath());
        
     }
    }
      
  }
}
