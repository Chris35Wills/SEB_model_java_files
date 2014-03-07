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
 * Summary (8/3/13) Reads in the winter precipitation file for the historical 
 * model run.
 * 
 * Input file format: 
 * |Year|WinterSnowThickness|WinterMonthCount|
 * 
 * @author Chris (9/1/13)
 */

public class OpenHistoricInputDataListener_WinterPrecip implements ActionListener {
  private GUIPanel panel = null;
  private Storage store = null;
  
  OpenHistoricInputDataListener_WinterPrecip(GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    
      System.out.println("Historic input data (winter precipitation) button working");
      
          FileDialog fd = new FileDialog(new Frame(), 
            "Open historic model input variables (.csv file) [winter precipitation]", FileDialog.LOAD);
    fd.setVisible(true);
    File f = new File (fd.getDirectory() + fd.getFile());
    
    if((fd.getDirectory()== null)||(fd.getFile() == null))
    {
    System.out.println("Main historical met. input file not uploaded");
    return;
    }
    else{
    
    FileReader fr = null;
    int year_WinterPrecip;
    double winterPrecip, winterMonthCount;
    
    ArrayList <Integer> year_list_WinterPrecip = new ArrayList<Integer>();
    ArrayList <Double> precip_list = new ArrayList<Double>();
    ArrayList <Double> winterMonth_list = new ArrayList<Double>();
         
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
                                                
                         year_WinterPrecip = Integer.parseInt(stringTokenizer.nextElement().toString());
                         winterPrecip = Double.parseDouble(stringTokenizer.nextElement().toString());
                         winterMonthCount = Double.parseDouble(stringTokenizer.nextElement().toString());                       
                         //************************Array List Integration *************
                         
                         year_list_WinterPrecip.add(year_WinterPrecip);
                         precip_list.add(winterPrecip);
                         winterMonth_list.add(winterMonthCount);
                                                  
                         //************************ Array List Integration *************
                       
                         //************************ Set In Storage *************
                         store.setYearFile_WinterPrecip(year_list_WinterPrecip);
                         store.set_Winter_PrecipFile(precip_list);
                         store.set_Winter_MonthFile(winterMonth_list);
                         
                         // Just for running a single line for testing the radiation on slope listener
                         store.setYear_WinterPrecip(year_WinterPrecip);
                         store.setWinterPrecip(winterPrecip);
                         store.setWinterMonthCount(winterMonthCount);
                                                  
                         // Just for running a single line for testing the radiation on slope listener
                         int yearStore_WinterPrecip = store.getYear_WinterPrecip();
                         double winterPrecipStore = store.getWinterPrecip();
                         double winterMonthCountStore = store.getWinterMonthCount();
                         
                         //************************ Set In Storage *************
                                   
                    System.out.println("Year: " + yearStore_WinterPrecip);
                    System.out.println("Precipitation total: " + winterPrecipStore);
                    System.out.println("Winter month count: " + winterMonthCountStore);
                             
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
             
    
   File f2 = new File("F:/Melt_modelling/Historical_model_outputs//Historical_Winter_precip_FILE_upload.txt");
   
    
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
      
      bw.write("Year, Winter_snow_thickness");
     
      bw.newLine();
      

      Iterator<Integer> iterator1 = store.getYearFile_WinterPrecip().iterator();
      Iterator<Double> iterator2 =  store.get_Winter_PrecipFile().iterator();     
            
      
      while(iterator1.hasNext()){

                        String year_print = Double.toString(iterator1.next());
                        String winterSnowThickness_print = Double.toString(iterator2.next());
                        

                        bw.write(year_print);
                        bw.write(",");
                        bw.write(winterSnowThickness_print);
                        bw.newLine();
	     }
              
      System.out.println("Historical Input (Winter_precip) .csv test file written");            
      bw.close();
      fw.close();

    }
    catch (IOException ioe)
    {
      System.out.println(ioe.toString());
    }
  
    //*****************************FILE WRITING ROUTINE********************************************   
    System.out.println("Copy of historical (Winter_precip) model input data: " + f2.getAbsolutePath());
        
     }
    }
      
  }
}
