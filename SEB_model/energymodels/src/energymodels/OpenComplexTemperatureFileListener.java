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
 * New summary (26/10/12): Reads in a columned data file of a fixed format and 
 * integrates readings into separate array lists. 
 * 
 * [Historical] Input file format (if saved, format is retained):
 * |Year|Month|Temp|Precip|Zenith|Azimuth|Tau|TOA|
 * 
 * [Contemporary]
 * |Year|Month|***Day***|Temp|Precip|Zenith|Azimuth|Tau|TOA|
 *    
 *      *** Note yet coded for
 * 
 * Once running the model, these lists will be looped and within each loop, 
 * variables will be set from the appropriate lists. The set/get methods 
 * already exist in Storage.java.
 * 
 * Test file to be used with this: 
 * "F:\Model_Test_Files\Input_met_data_TEST2"
 *
 * To do:
 * 1) Create final input structure of file to be read in (i.e. all values 
 * required for model)
 * 
 * @author Chris (26/10/12)
 */

public class OpenComplexTemperatureFileListener implements ActionListener {
  private GUIPanel panel = null;
  private Storage store = null;
  
  OpenComplexTemperatureFileListener(GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
      
    FileDialog fd = new FileDialog(new Frame(), 
            "Open model input variables (.csv file)", FileDialog.LOAD);
    fd.setVisible(true);
    File f = new File (fd.getDirectory() + fd.getFile());
    
    if((fd.getDirectory()== null)||(fd.getFile() == null))
    {
    System.out.println("Complex temperature file not uploaded");
    return;
    }
    
    else{
	

    FileReader fr = null;
    double year, Month;
    double temp,Precip,SolarRad,Zenith,Azimuth,Declination,SolarHour, 
            Tau_estimate, TOA;
    String MonthText = null;
    
    ArrayList <Double> year_list = new ArrayList<Double>();
    ArrayList <Double> month_list = new ArrayList<Double>();
    ArrayList <Double> temp_list = new ArrayList<Double>();
    ArrayList <Double> precip_list = new ArrayList<Double>();
    //ArrayList <Double> solarDec_list = new ArrayList<Double>();
    //ArrayList <Double> solarHour_list = new ArrayList<Double>();
    ArrayList <Double> zenith_list = new ArrayList<Double>();
    ArrayList <Double> azimuth_list = new ArrayList<Double>();
    ArrayList <Double> tau_estimate_list = new ArrayList<Double>();
    ArrayList <Double> TOA_list = new ArrayList<Double>();
    
    
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
                         
                         year = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Month = Integer.parseInt(stringTokenizer.nextElement().toString());
                         temp = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Precip = Double.parseDouble(stringTokenizer.nextElement().toString());
                         //Declination = Double.parseDouble(stringTokenizer.nextElement().toString());
                         //SolarHour = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Zenith = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Azimuth = Double.parseDouble(stringTokenizer.nextElement().toString());
                         Tau_estimate = Double.parseDouble(stringTokenizer.nextElement().toString());
                         TOA = Double.parseDouble(stringTokenizer.nextElement().toString());
                         
                         //************************Array List Integration *************
                         
                         year_list.add(year);
                         month_list.add(Month);
                         temp_list.add(temp);
                         precip_list.add(Precip);
                         //solarDec_list.add(Declination);
                         //solarHour_list.add(SolarHour);
                         zenith_list.add(Zenith);
                         azimuth_list.add(Azimuth);
                         tau_estimate_list.add(Tau_estimate);
                         TOA_list.add(TOA);
                                 
                         //************************ Array List Integration *************
                       
                         //************************ Set In Storage *************
                         store.setYearFile(year_list);
                         store.setMonthFile(month_list);
                         store.setTemperatureFile(temp_list);
                         store.setPrecipFile(precip_list);
                         //store.setSolarDeclinationFile(solarDec_list);
                         //store.setSolarHourFile(solarHour_list);
                         store.setZenithFile(zenith_list);
                         store.setAzimuthFile(azimuth_list);
                         store.setTauFile(tau_estimate_list);
                         store.setTOAFile(TOA_list);
                            
                         
                         // Just for running a single line for testing the radiation on slope listener
                         store.setYear(year);
                         store.setMonth(Month);
                         store.setTemp(temp);
                         store.setPrecip(Precip);
                         //store.setSolarDeclination(Declination);
                         //store.setSolarHour(SolarHour);
                         store.setZenith(Zenith);
                         store.setAzimuth(Azimuth);
                         store.setTau(Tau_estimate);
                         store.setTOA(TOA);
                         // Just for running a single line for testing the radiation on slope listener
                         double yearStore = store.getYear();
                         double monthStore = store.getMonth();
                         double tempStore = store.getTemp();
                         double precipStore = store.getPrecip();
                         double solarDecStore = store.getSolarDeclination();
                         double solarHourStore = store.getSolarHour();
                         double zenithStore = store.getZenith();
                         double azimuthStore = store.getAzimuth();
                         double tauStore = store.getTau();
                         double toaStore = store.getTOA();
                         
                         //************************ Set In Storage *************
                         
                         // Numeric month to text conversion (potentially superfluous)
                         
//                         if(Month == 1){
//                             MonthText = "January";
//                         } else if (Month == 2){
//                             MonthText = "February";
//                         } else if (Month == 3){
//                             MonthText = "March";
//                         } else if (Month == 4){
//                             MonthText = "April";
//                         } else if (Month == 5){
//                             MonthText = "May";
//                         } else if (Month == 6){
//                             MonthText = "June";
//                         } else if (Month == 7){
//                             MonthText = "July";
//                         } else if (Month == 8){
//                             MonthText = "August";
//                         } else if (Month == 9){
//                             MonthText = "September";
//                         } else if (Month == 10){
//                             MonthText = "October";
//                         } else if (Month == 11){
//                             MonthText = "November";
//                         } else if (Month == 12){
//                             MonthText = "December";
//                         }                                                                 
                    System.out.println("Year: " + yearStore);
                    System.out.println("Month: " + monthStore);
                    System.out.println("Temp: " + tempStore); 
                    System.out.println("Precip: " + precipStore);
//                    System.out.println("Solar Declination: " + solarDecStore); 
//                    System.out.println("Solar Hour: " + solarHourStore);
                    System.out.println("Zenith: " + zenithStore);
                    System.out.println("Azimuth: " + azimuthStore); 
                    System.out.println("Tau: " + tauStore); 
                    System.out.println("Sin_TOA: " + toaStore); 
                                             
//                    System.out.println("Month: " + MonthText);
//                    System.out.println("Temp: " + temp); 
//                    System.out.println("Precip: " + Precip);
//                    System.out.println("Solar Declination: " + Declination); 
//                    System.out.println("Solar Hour: " + SolarHour);
//                    System.out.println("Zenith: " + Zenith);
//                    System.out.println("Azimuth: " + Azimuth); 
                    
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
             
    File f2 = new File("F://Melt_Modelling//Model_outputs//Contemp_met_input_file_12.11.12.txt");
   
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
      
       
      //bw.write("Month, Temp., Precip., Solar.Dec, Solar.Hour, Zenith, Azimuth, Tau, TOA");
      bw.write("Year, Month, Temp., Precip., Zenith, Azimuth, Tau, TOA");
      bw.newLine();

//      Iterator<Integer> iterator1 = month_list.iterator();
//      Iterator<Double> iterator2 = temp_list.iterator();
//      Iterator<Double> iterator3 = precip_list.iterator();
//      Iterator<Double> iterator4 = solarDec_list.iterator();
//      Iterator<Double> iterator5 = solarHour_list.iterator();
//      Iterator<Double> iterator6 = zenith_list.iterator();
//      Iterator<Double> iterator7 = azimuth_list.iterator();
      Iterator<Double> iterator0 = store.getYearFile().iterator();
      Iterator<Double> iterator1 = store.getMonthFile().iterator();
      Iterator<Double> iterator2 = store.getTemperatureFile().iterator();
      Iterator<Double> iterator3 =store.getPrecipFile().iterator();
      //Iterator<Double> iterator4 = store.getSolarDeclinationFile().iterator();
      //Iterator<Double> iterator5 = store.getSolarHourFile().iterator();
      Iterator<Double> iterator6 = store.getZenithFile().iterator();
      Iterator<Double> iterator7 = store.getAzimuthFile().iterator();
      Iterator<Double> iterator8 = store.getTauFile().iterator();
      Iterator<Double> iterator9 = store.getTOAFile().iterator();
      
            
               while (iterator0.hasNext()) {

                        String year_print = Double.toString(iterator0.next());
                        String month_print = Double.toString(iterator1.next());
                        String temp_print = Double.toString(iterator2.next());
                        String precip_print = Double.toString(iterator3.next());
                        //String solarDec_print = Double.toString(iterator4.next());
                        //String solarHour_print = Double.toString(iterator5.next());
                        String zenith_print = Double.toString(iterator6.next());
                        String azimuth_print = Double.toString(iterator7.next());
                        String tau_print = Double.toString(iterator8.next());
                        String TOA_print = Double.toString(iterator9.next());
                        
                        bw.write(year_print);
                        bw.write(",");
                        bw.write(month_print);
                        bw.write(",");
                        bw.write(temp_print);
                        bw.write(",");
                        bw.write(precip_print);
                        bw.write(",");
//                        bw.write(solarDec_print);
//                        bw.write(",");
//                        bw.write(solarHour_print);
//                        bw.write(",");
                        bw.write(zenith_print);
                        bw.write(",");
                        bw.write(azimuth_print);
                        bw.write(",");
                        bw.write(tau_print);
                        bw.write(",");
                        bw.write(TOA_print);
                        bw.newLine();
	     }
              
      System.out.println("Input met .csv test file written");            
      bw.close();
      fw.close();

    }
    catch (IOException ioe)
    {
      System.out.println(ioe.toString());
    }
  
    //*****************************FILE WRITING ROUTINE********************************************   
    }
    }     
  }
}
