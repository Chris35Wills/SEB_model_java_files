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
 * @author Chris
 */

public class TOAcalcListenerExperimental implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;
  
  TOAcalcListenerExperimental (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    
    double SinTOA, I0, Rm, R, eta, delta, omega, etarad, deltarad, omegarad; // Do these need to be array lists like the original temp file?
    double trans_Value, albedo_value;
    double FID, meanTAU, meanDELTA, meanOMEGA, meanDELTArad, meanOMEGArad;
    
    //**************************** FOR TAU EXPERIEMNT
    double measured_Sw, tau_estimate, experimental_surface_Sw;
    //**************************** FOR TAU EXPERIEMNT
    
    FileDialog fd = new FileDialog(new Frame(), "Open radiation data .txt/.csv file", FileDialog.LOAD);
    fd.setVisible(true);
    File f = new File (fd.getDirectory() + fd.getFile());
    
    System.out.println("File opened");

    FileReader fr = null;
    
    ArrayList<Double> SinTOA_list = new ArrayList<Double>();
    ArrayList <Double> trans_Value_list = new ArrayList<Double>();
    ArrayList <Double> albedo_value_list = new ArrayList<Double>();
    
    //**************************** FOR TAU EXPERIEMNT
    ArrayList <Double> tau_experiment_list = new ArrayList<Double>();
    ArrayList <Double> experimental_surface_Sw_list = new ArrayList<Double>();
    ArrayList <Double> measured_Sw_list = new ArrayList<Double>();
    //**************************** FOR TAU EXPERIEMNT

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
//                ArrayList<Double> SinTOA_list = new ArrayList<Double>();
//                ArrayList<Double> trans_Value_list = new ArrayList<Double>();
//                ArrayList<Double> albedo_value_list = new ArrayList<Double>();
 
		//br = new BufferedReader(new FileReader("C:/Users/Chris/Desktop/Java_working/6th_Jun_data.csv"));
                
                br.readLine(); // Skips first line in file (useful if you have a header)
                
		while ((line = br.readLine()) != null) {
		   //System.out.println(line);
 
		   StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
 
		   while (stringTokenizer.hasMoreElements()) {
                       
                         FID = Double.parseDouble(stringTokenizer.nextElement().toString());
                         delta = Double.parseDouble(stringTokenizer.nextElement().toString());
                         omega = Double.parseDouble(stringTokenizer.nextElement().toString());
                        // measured_Sw = Double.parseDouble(stringTokenizer.nextElement().toString());
                         
                         //**************************** FOR TAU EXPERIEMNT
                         measured_Sw = Double.parseDouble(stringTokenizer.nextElement().toString());
                         measured_Sw_list.add(measured_Sw);
                         //**************************** FOR TAU EXPERIEMNT
                         
                         //System.out.println("delta = " + delta);
                         //System.out.println("omega = " + omega);
                         
                       //*******************************************************************************
                        
                         
                         //**************************** Sin_TOA CALCULATION 
                         etarad = Math.toRadians(eta); 
                         deltarad = Math.toRadians(delta);
                         omegarad = Math.toRadians(omega);
    
                         SinTOA = I0 * Math.sqrt(Rm/R) * 
                              ((Math.cos(etarad)* Math.cos(deltarad)* 
                                  Math.cos(omegarad))+(Math.sin(etarad)
                                      * Math.sin(deltarad)));
                         //**************************** Sin_TOA CALCULATION 
                         
                         
                         //**************************** FOR TAU EXPERIEMNT
                         tau_estimate = (measured_Sw/SinTOA);
                         tau_experiment_list.add(tau_estimate);
                         experimental_surface_Sw = (tau_estimate*SinTOA);
                         experimental_surface_Sw_list.add(experimental_surface_Sw);
                        //**************************** FOR TAU EXPERIEMNT
                         
                         //**************** Array List ************************* 
                         SinTOA_list.add(SinTOA);
                         //System.out.println("SinTOA array list created");
                         //**************** Array List ************************* 
                         
                         //************************** Mock transmissivity value
                         trans_Value = 0.48 * SinTOA; // tau is set as 0.48 here just as a test
                         albedo_value = trans_Value * 0.5;
                                                  
                         //**************** Array List ************************* 
                         trans_Value_list.add(trans_Value);
                         //System.out.println("Trans value array list created");
                         albedo_value_list.add(albedo_value);
                         //System.out.println("Albedo array list created");
                         //**************** Array List ************************* 
                         
                         //************************** Mock transmissivity value
                         
                         
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
                                   
                       
                         //**************** Array List ************************* // JUST ADDED
//                         int SinTOASize = SinTOA_list.size();
//                         int transSize = trans_Value_list.size();
//                         int albedoSize = albedo_value_list.size();
//        
//                         System.out.println("SinTOA list length: " + SinTOASize);
//                         System.out.println("Trans list length: " + transSize);
//                         System.out.println("Albedo list length: " + albedoSize);
                         //**************** Array List ************************* // JUST ADDED
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
    
    //************************************ WORKING ASSUMING TAU EXPERIMENT CODE IS COMMENTED OUT            
//    File f2 = new File("C:/Users/Chris/Desktop/Java_working/SinTOA_output_test3.txt");
    //************************************ WORKING ASSUMING TAU EXPERIMENT CODE IS COMMENTED OUT    
    
    //**************************** FOR TAU EXPERIEMNT 
    File f2 = new File("C:/Users/Chris/Desktop/Java_working/Tau_experiment_output_EXPERIMENTAL.txt");
    //**************************** FOR TAU EXPERIEMNT 
    FileWriter fw = null;
    ArrayList <Double> output = new ArrayList<Double>();
    output = SinTOA_list;

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
      
      //************************************ WORKING ASSUMING TAU EXPERIMENT CODE IS COMMENTED OUT    
//      bw.write("Sin TOA (Wm^-2), Tau corrected Sin TOA (Wm^-2), Albedo mod. of tau correction (Wm^-2)");
//      bw.newLine();
      //************************************ WORKING ASSUMING TAU EXPERIMENT CODE IS COMMENTED OUT  
      
      //**************************** FOR TAU EXPERIEMNT 
      bw.write("Sin TOA (Wm^-2), Estimated Tau, Sin_TOA*tau (Wm^-2), Measured surface Sw (Wm^-2)");
      bw.newLine();
      
      Iterator<Double> iterator1 = SinTOA_list.iterator(); 
      Iterator<Double> iterator2 = tau_experiment_list.iterator();
      Iterator<Double> iterator3 = experimental_surface_Sw_list.iterator();
      Iterator<Double> iterator4 = measured_Sw_list.iterator();
            while ((iterator1.hasNext())&&(iterator2.hasNext())&&(iterator3.hasNext())) {
//			  System.out.println(iterator1.next());
//                        System.out.println(iterator2.next());
//                        System.out.println(iterator3.next());
                        String SinTOA_print = Double.toString(iterator1.next());
                        String estimated_tau_print = Double.toString(iterator2.next());
                        String tau_x_SinTOA_print = Double.toString(iterator3.next());
                        String surface_Sw_print = Double.toString(iterator4.next());
                        
                        bw.write(SinTOA_print);
                        bw.write(", ");
                        bw.write(estimated_tau_print);
                        bw.write(", ");
                        bw.write(tau_x_SinTOA_print);
                        bw.write(", ");
                        bw.write(surface_Sw_print);
                        bw.newLine();
	     }
      //**************************** FOR TAU EXPERIEMNT
      
      //************************************ WORKING ASSUMING TAU EXPERIMENT CODE IS COMMENTED OUT       
//      Iterator<Double> iterator1 = SinTOA_list.iterator(); // THIS IS THE NEW BIT (25/10/12 - 11:51)
//      Iterator<Double> iterator2 = trans_Value_list.iterator();
//      Iterator<Double> iterator3 = albedo_value_list.iterator();
//            while ((iterator1.hasNext())&&(iterator2.hasNext())&&(iterator3.hasNext())) {
////			System.out.println(iterator1.next());
////                        System.out.println(iterator2.next());
////                        System.out.println(iterator3.next());
//                        String SinTOA_print = Double.toString(iterator1.next());
//                        String tau_print = Double.toString(iterator2.next());
//                        String albedo_print = Double.toString(iterator3.next());
//                        
//                        bw.write(SinTOA_print);
//                        bw.write(",");
//                        bw.write(tau_print);
//                        bw.write(",");
//                        bw.write(albedo_print);
//                        bw.newLine();
//	     }
      //************************************ WORKING ASSUMING TAU EXPERIMENT CODE IS COMMENTED OUT
            
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

