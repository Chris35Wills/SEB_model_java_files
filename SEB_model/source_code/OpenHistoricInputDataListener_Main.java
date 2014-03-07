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
 * Summary (8/3/13) Reads in the Main input file for the historical 
 * model run.
 * 
 * Input file format: 
 *|Year|Month|Temp(corrected)|PrecipSeason|PrecipTotal|Tau|
 * 
 * @author Chris (9/1/13)
 **/

public class OpenHistoricInputDataListener_Main implements ActionListener {
  private GUIPanel panel = null;
  private Storage store = null;
  
  OpenHistoricInputDataListener_Main(GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    
      System.out.println("Historic input data (main) button working");
      
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
    int year_main, month_main;
    double temp, PrecipTotal, Precip_season, Tau_estimate;
    String MonthText = null;
    
    ArrayList <Integer> year_list_MAIN = new ArrayList<Integer>();
    ArrayList <Integer> month_list_MAIN = new ArrayList<Integer>();
    ArrayList <Double> temp_list = new ArrayList<Double>();
    ArrayList <Double> precip_list = new ArrayList<Double>();
    ArrayList <Double> precip_season_list = new ArrayList<Double>();
    ArrayList <Double> tau_estimate_list = new ArrayList<Double>();
 
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
                         store.setYear_MAIN(year_main);
                         store.setMonth_MAIN(month_main);
                         store.setTemp(temp);
                         store.setPrecipTotal(PrecipTotal);
                         store.setPrecipSeason(Precip_season);                 
                         store.setTauEstimate(Tau_estimate);
                                                  
                         // Just for running a single line for testing the radiation on slope listener
                         int yearStore = store.getYear_MAIN();
                         double monthStore = store.getMonth_MAIN();
                         double tempStore = store.getTemp();
                         double precipTotalStore = store.getPrecipTotal();
                         double precipSeasonStore = store.getPrecipSeason();
                                                  
                         double tauEstimateStore = store.getTauEstimate();
                         
                         //************************ Set In Storage *************
                                   
                    System.out.println("Year: " + yearStore);
                    System.out.println("Month: " + monthStore);
                    System.out.println("Temp: " + tempStore); 
                    System.out.println("Precipitation total: " + precipTotalStore);
                    System.out.println("Precipitation season : " + precipSeasonStore);
                    System.out.println("Tau estimate: " + tauEstimateStore);
                              
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
             
    
   File f2 = new File("F:/Melt_modelling/Historical_model_outputs/Historical_MAIN_MET_FILE_upload.txt");
   
    
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
      
      bw.write("Year, Month, Corrected temp, PrecipSeason, Precip_m_RAW, Tau");
     
      bw.newLine();
      

      Iterator<Integer> iterator1 = store.getMonthFile_MAIN().iterator();
      Iterator<Integer> iterator2 = store.getYearFile_MAIN().iterator();
      Iterator<Double> iterator3 = store.getTemperatureFile().iterator();
      Iterator<Double> iterator4b =  store.getPrecipSeasonList_MAIN().iterator();
      Iterator<Double> iterator4 =  store.getPrecipFile().iterator();     
      Iterator<Double> iterator17 = store.getTauEstimateList().iterator();
            
      
      while(iterator1.hasNext()){

                        String year_print = Double.toString(iterator2.next());
                        String month_print = Double.toString(iterator1.next());
                        String temp_print = Double.toString(iterator3.next());
                        String precip_season_print = Double.toString(iterator4b.next());
                        String precip_print = Double.toString(iterator4.next());
                        String Tau_estimate_print = Double.toString(iterator17.next());

                        bw.write(year_print);
                        bw.write(",");
                        bw.write(month_print);
                        bw.write(",");
                        bw.write(temp_print);
                        bw.write(",");
                        bw.write(precip_season_print);
                        bw.write(",");
                        bw.write(precip_print);
                        bw.write(",");
                        bw.write(Tau_estimate_print);
                        bw.newLine();
	     }
              
      System.out.println("Historical Input (MAIN) .csv test file written");            
      bw.close();
      fw.close();

    }
    catch (IOException ioe)
    {
      System.out.println(ioe.toString());
    }
  
    //*****************************FILE WRITING ROUTINE********************************************   
    System.out.println("Copy of historical (MAIN) model input data: " + f2.getAbsolutePath());
        
     }
    }
  }
}
