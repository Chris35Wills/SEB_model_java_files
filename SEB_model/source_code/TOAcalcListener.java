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
 * Calculates Sin TOA using values for I0, Rm, R, eta, delta and omega (solar 
 * constant, mean and instantaneous sun-earth distances, latitude, solar 
 * declination and solar hour).
 * 
 * Currently provides an open file window - expects a .csv of the format
 * "FID,Solar declination (deg), Solar hour (deg)". 
 * These values are then stored as ArrayLists. All degree units are converted 
 * to radians and Sin TOA is then calculated. This is then added to its own 
 * ArrayList.
 * 
 * DO NOT USE TO CALCULATE MEAN DAILY TOA - THE MEAN SOLAR HOUR ANGLE DOES NOT 
 * PROVIDE CORRECT VALUES (ARTIFICIAL GEOMETRY THAT CANNOT EXIST IF YOU USE ITS 
 * MEAN)
 * 
 * Things to change:
 * 1) Open file window to be removed - all data will be read in via 
 * OpenComplexTemperatureListener.java and then be sent to Storage.java - thus, 
 * the data currently being read in here directly will be accessed using mutator
 * methods
 * 2) Sin_TOA ArrayList to be sent to Storage
 * 
 * NB1/ An output file is currently produced (more as a means of checking 
 * everything is right) - the format of the file is:
 * "Sin TOA, Tau*Sin TOA, Albedo(Tau*Sin TOA)"
 * Both Tau*Sin TOA and Albedo(Tau*Sin TOA) are experiments, assuming a tau 
 * value of 0.48 and an albedo value of 0.5 - this has been done just to see how 
 * values look following these calculations (the values aren't that far from 
 * expected). This can all be removed eventually - tau will be calculated 
 * elsewhere and albedo will depend on snow layer thickness.
 * 
 * NB2/This can currently be run as a stand-alone programme - you'll need to 
 * remove the elements that make it a listener, the package and any imports from 
 * EnergyBalance (i.e. Panel and store)
 * 
 * NB3/ BE AWARE THAT TOA_VALUE (AND TOA_LIST) CONTAIN 0 VALUES. FOLLOWING THIS 
 * CHANGE, THE ALBEDO AND TAU EXPERIMENTS NO LONGER WORK (31/10/12)
 * 
 * @author Chris 25/10/12 v 1.0
 */

public class TOAcalcListener implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;
  
  TOAcalcListener (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    
    double SinTOA, SinTOA_value, I0, Rm, R, eta, delta, omega, etarad, deltarad, omegarad;
    int Year, Month;
    //double trans_Value, albedo_value;
    int FID;
       
    FileDialog fd = new FileDialog(new Frame(), "Open radiation data .txt/.csv file", FileDialog.LOAD);
    fd.setVisible(true);
    File f = new File (fd.getDirectory() + fd.getFile());
    
    System.out.println("File opened");

    FileReader fr = null;
    
    ArrayList<Double> SinTOA_list = new ArrayList<Double>();
    ArrayList<Integer> FID_list = new ArrayList<Integer>();
    ArrayList<Double> delta_list = new ArrayList<Double>();
    ArrayList<Double> omega_list = new ArrayList<Double>();
    ArrayList<Integer> year_list = new ArrayList<Integer>();
    ArrayList<Integer> month_list = new ArrayList<Integer>();
    //ArrayList <Double> trans_Value_list = new ArrayList<Double>();
    //ArrayList <Double> albedo_value_list = new ArrayList<Double>();

    try
    {

      fr = new FileReader(f);

    }
    catch (FileNotFoundException fnfe)
    {
    }

    BufferedReader br = new BufferedReader(fr);
    
        I0 = 1368;  //Watts m^-2
        Rm  = 1;
        R = Rm;
        eta =  68.35; //deg latitude
        //BufferedReader br = null;
        //BufferedWriter bw = null;
 
	try {
 
		String line;
               
                br.readLine(); // Skips first line in file (useful if you have a header)
                
		while ((line = br.readLine()) != null) {
		   //System.out.println(line);
 
		   StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
 
		   while (stringTokenizer.hasMoreElements()) {
                       
                         FID = Integer.parseInt(stringTokenizer.nextElement().toString());
                         System.out.println("FID = " + FID);
                         Year = Integer.parseInt(stringTokenizer.nextElement().toString());
                         System.out.println("Year = " + Year);
                         Month = Integer.parseInt(stringTokenizer.nextElement().toString());
                         System.out.println("Month = " + Month);
                         delta = Double.parseDouble(stringTokenizer.nextElement().toString());
                         System.out.println("delta = " + delta);
                         omega = Double.parseDouble(stringTokenizer.nextElement().toString());
                         System.out.println("omega = " + omega);
                         //System.out.println("delta = " + delta);
                         //System.out.println("omega = " + omega);
                         
                       //*******************************************************************************
                        
                         
                         //**************************** Sin_TOA CALCULATION 
                         etarad = Math.toRadians(eta); // Convert to radians
                         deltarad = Math.toRadians(delta); // Convert to radians
                         omegarad = Math.toRadians(omega); // Convert to radians
                         
                         double cossin_calc = ((Math.cos(etarad)* Math.cos(deltarad)* 
                                  Math.cos(omegarad))+(Math.sin(etarad)
                                      * Math.sin(deltarad)));
                         
                         double cossin_calc_deg = cossin_calc/0.0174532925;
                         
                         SinTOA = I0 * ((Rm/R)*(Rm/R)) * cossin_calc_deg;                      
                                                 
                         if(SinTOA >= 0){
                             SinTOA_value = SinTOA;
                         } else {
                             SinTOA_value = 0; 
                         }
                         //**************** Array List ************************* 
                         SinTOA_list.add(SinTOA_value);
                         FID_list.add(FID);
                         delta_list.add(delta);
                         omega_list.add(omega);
                         year_list.add(Year);
                         month_list.add(Month);
                         
                         //**************** Array List ************************* 
                         //SinTOA_list.add(SinTOA);
                         //System.out.println("SinTOA array list created");
                         //**************** Array List ************************* 
                         
                         //**************************** Sin_TOA CALCULATION 
                         
                         //************************** Mock transmissivity and albedo value
                         //trans_Value = 0.48 * SinTOA_value; // tau is set as 0.48 here just as a test
                         //albedo_value = trans_Value * 0.5;
                                                  
                         //**************** Array List ************************* 
                         //trans_Value_list.add(trans_Value);
                         //System.out.println("Trans value array list created");
                         //albedo_value_list.add(albedo_value);
                         //System.out.println("Albedo array list created");
                         //**************** Array List ************************* 
                         
                         //************************** Mock transmissivity and albedo value
                         
                         
//                         System.out.println("FID = " +FID);
//                         if(SinTOA > 0){
//                                System.out.println("TOA = " + SinTOA);
//                                System.out.println("Transmissivity adjustment = " + trans_Value);
//                                System.out.println("Albedo adjustment: " + albedo_value);
//                                //System.out.println("Must be sunny! Is it summmer?");
//                         } else{
//                                System.out.println("TOA = " + "No incoming solar energy");
//                                //System.out.println("Brrrrrr, no sun? Is it winter or night?");
//                         }
                                   
                       
                         //**************** Array List Test*************************
//                         int SinTOASize = SinTOA_list.size();
//                         int transSize = trans_Value_list.size();
//                         int albedoSize = albedo_value_list.size();
//        
//                         System.out.println("SinTOA list length: " + SinTOASize);
//                         System.out.println("Trans list length: " + transSize);
//                         System.out.println("Albedo list length: " + albedoSize);
                         //**************** Array List Test************************* 
                   }
                   

                   
		}
                         
//                         int SinTOASize = SinTOA_list.size();
//                         int transSize = trans_Value_list.size();
//                         int albedoSize = albedo_value_list.size();
        
//                         System.out.println("SinTOA list length: " + SinTOASize);
//                         System.out.println("Trans list length: " + transSize);
//                         System.out.println("Albedo list length: " + albedoSize);
                         
                                                                
	}  
        
        catch (IOException ex) {
        } 
        
    //*****************************FILE WRITING ROUTINE********************************************   
    //File f2 = new File("F:/Melt_modelling/Model_outputs/TOA_test.txt");
    
    FileDialog fd2 = new FileDialog(new Frame(), 
                                       "Save TOA Radiation file", FileDialog.SAVE);
    fd2.setVisible(true);

    File f2 = new File(fd2.getDirectory() + fd2.getFile());
   
    FileWriter fw = null;
    ArrayList <Double> output = new ArrayList<Double>();
    //output = SinTOA_list;

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
      
       
      bw.write("FID, Year, Month, Delta (Declination), Omega (solar hour), Sin TOA (Wm^-2)");//, Tau corrected Sin TOA (Wm^-2), Albedo mod. of tau correction (Wm^-2)");
      bw.newLine();
      
//      delta_list.add(delta);
//      omega_list.add(omega);
      
      Iterator<Double> iterator1 = SinTOA_list.iterator();
//      Iterator<Double> iterator2 = trans_Value_list.iterator();
//      Iterator<Double> iterator3 = albedo_value_list.iterator();
      Iterator<Integer> iterator2 = year_list.iterator();
      Iterator<Integer> iterator3 = month_list.iterator();
      Iterator<Integer> iterator4 = FID_list.iterator();
      Iterator<Double> iterator5 = delta_list.iterator();
      Iterator<Double> iterator6 = omega_list.iterator();
            //while ((iterator1.hasNext())&&(iterator2.hasNext())&&(iterator3.hasNext())) {
                while(iterator1.hasNext()&& iterator4.hasNext()){
//			System.out.println(iterator1.next());
//                        System.out.println(iterator2.next());
//                        System.out.println(iterator3.next());              
                        
                    String FID_print = Integer.toString(iterator4.next());                  
                    String delta_print = Double.toString(iterator5.next());
                    String omega_print = Double.toString(iterator6.next());
                    String SinTOA_print = Double.toString(iterator1.next());
                    String month_print = Integer.toString(iterator3.next());
                    String year_print = Integer.toString(iterator2.next());
                    
//                        String tau_print = Double.toString(iterator2.next());
//                        String albedo_print = Double.toString(iterator3.next());
                        
                        bw.write(FID_print);
                        bw.write(",");
                        bw.write(year_print);
                        bw.write(",");
                        bw.write(month_print);
                        bw.write(",");
                        bw.write(delta_print);
                        bw.write(",");
                        bw.write(omega_print);
                        bw.write(",");
                        bw.write(SinTOA_print);
//                        bw.write(",");
//                        bw.write(tau_print);
//                        bw.write(",");
//                        bw.write(albedo_print);
                        bw.newLine();
	     }
                  
      bw.close();
      fw.close();

    }
    catch (IOException ioe)
    {
      System.out.println(ioe.toString());
    }
  
    //*****************************FILE WRITING ROUTINE********************************************    
  
  finally {
		try {
			if (br != null)
				br.close();
 
		} catch (IOException ex) {
                  }
               
        }
        

        //System.out.println("File written");

        
        System.out.println("Model run successful");
        
    }
  
  
}

