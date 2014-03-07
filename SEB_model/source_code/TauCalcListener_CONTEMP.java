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
 * Tau calculator
 * 
 * Expected file format:
 * Header in columns of |Month|Year|Sw (surface)|delta|omega| as 
 * (int|int|double|double|double|). Gaps in dataset to be given -9999 (No Data 
 * values).
 * 
 * Calculates TOA radiation then sorts through uploaded file estimating tau 
 * using Sw (surface)/TOA radiation. Monthly means are then calculated. Output 
 * file created is in format: |Month|Year|TOA|Tau estimate|
 * 
 * @author Chris 31/10/12 v 1.0
 */

public class TauCalcListener_CONTEMP implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;
  
  TauCalcListener_CONTEMP (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    
    double SinTOA, SinTOA_value, I0, Rm, R, eta, delta, omega, etarad, deltarad, omegarad, measured_Sw; 
    double tau_no_sun;
    int month, year;
       
    FileDialog fd = new FileDialog(new Frame(), "Open file for ta estimation(.txt/.csv file)", FileDialog.LOAD);
    fd.setVisible(true);
    File f = new File (fd.getDirectory() + fd.getFile());
    
    System.out.println("File opened");

    FileReader fr = null;
    
    ArrayList<Double> SinTOA_list = new ArrayList<Double>();
    ArrayList <Integer> month_list = new ArrayList<Integer>();
    //ArrayList <Integer> month_list_2 = new ArrayList<Integer>();
    ArrayList <Integer> year_list = new ArrayList<Integer>();
    ArrayList <Double> measured_Sw_list = new ArrayList<Double>();
    ArrayList <Double> tau1_list = new ArrayList<Double>();// the daily tau value
    ArrayList <Double> tau2_list = new ArrayList<Double>();// the monthly mean tau value

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
                       
                         //FID = Double.parseDouble(stringTokenizer.nextElement().toString());
                         month = Integer.parseInt(stringTokenizer.nextElement().toString());
                         year = Integer.parseInt(stringTokenizer.nextElement().toString());
                         measured_Sw = Double.parseDouble(stringTokenizer.nextElement().toString());
                         SinTOA = Double.parseDouble(stringTokenizer.nextElement().toString());
                         //delta = Double.parseDouble(stringTokenizer.nextElement().toString());
                         //omega = Double.parseDouble(stringTokenizer.nextElement().toString());
                         
                         //System.out.println("delta = " + delta);
                         //System.out.println("omega = " + omega);
                         System.out.println("Year = " + year);
                         //*****************************************************
                        
                         
                         //**************************** Sin_TOA CALCULATION 
//                         etarad = Math.toRadians(eta);
//                         deltarad = Math.toRadians(delta);
//                         omegarad = Math.toRadians(omega);
//    
//                         SinTOA = I0 * Math.sqrt(Rm/R) * 
//                              ((Math.cos(etarad)* Math.cos(deltarad)* 
//                                  Math.cos(omegarad))+(Math.sin(etarad)
//                                      * Math.sin(deltarad)));   
//                         
//                         if(SinTOA >= 0){
//                             SinTOA_value = SinTOA;
//                         } else {
//                             SinTOA_value = 0; 
//                         }
                         //**************** Array List ************************* 
                         SinTOA_list.add(SinTOA);
                         month_list.add(month);
                         year_list.add(year);
                         measured_Sw_list.add(measured_Sw);
                         //System.out.println("SinTOA array list created");
                         //**************** Array List ************************* 
                         
                         //**************************** Sin_TOA CALCULATION    
                         
                   }
                   
		}
                                                   
	}  
        
        catch (IOException ex) {
            
        } 
        
                        //**************************** Tau estimation
                         
                        Iterator<Integer> iterator1 = month_list.iterator();
                        Iterator<Double> iterator2 = SinTOA_list.iterator();
                        Iterator<Double> iterator3 = measured_Sw_list.iterator();
                      //  Iterator<Double> iterator4 = tau1_list.iterator();
                        
                        while ((iterator1.hasNext())&&(iterator2.hasNext())&&(iterator3.hasNext())){
                            
                            //int Month_list_value = iterator1.next();
                            double SinTOA_list_value = iterator2.next();
                            double measured_Sw_list_value = iterator3.next();
                            double tau_1;
                            
                            if ((measured_Sw_list_value != -9999)&&(SinTOA_list_value != 0)){
                                
                                tau_1 = measured_Sw_list_value/SinTOA_list_value;
                                                                
                                tau1_list.add(tau_1);                             
                                
                            } else if ((measured_Sw_list_value != -9999)&&(SinTOA_list_value == 0)){
                                
                                tau_1 = 0;
                                
                                tau1_list.add(tau_1);                             
                            
                            } 
                            
                            else if (measured_Sw_list_value == -9999){
                                
                                tau_1 = -9999;
                                
                                tau1_list.add(tau_1);
                            
                            }
                            
                        }
                        
                        //**************************** Tau estimation
                        
                        //**************************** Monthly Tau estimation
        
         // Now need to calculate mean monthly tau
          
           //Iterator<Integer> iterator1 = month_list.iterator();
         
//           ArrayList <Double> tau3_list = new ArrayList<Double>();
//              
//          while ((iterator1.hasNext())&&(iterator4.hasNext())){
//           double sum = 0;          
//           
//                 //while (iterator4.hasNext()){
// 
//                       for (double v: tau1_list ){
//                       
//                       sum = v;
//                               
//                       }
//                         
//                  //}
//           
//            System.out.println("Sum of tau list: " + sum);
                     
////                        
//               int Month_list_value = iterator1.next();
//               double tau_1_list_value = iterator4.next();                    
//
////               System.out.println("Should be about to print out a January estimate");
//                while((Month_list_value == 1)&&(tau_1_list_value != -9999)){
//
//                    double tau_3 = tau_1_list_value;                     
//                    tau3_list.add(tau_3);
//                    
//                 }
//                                               
//          }                 
//                      
//            for (double v: tau3_list ){
//                double sum = 0;
//                sum += v;
//                double Jan_tau = sum/tau3_list.size();
//                System.out.println("January tau estimate: " + Jan_tau);
//            }
                        
                        //**************************** Monthly Tau estimation
        
    //*****************************FILE WRITING ROUTINE********************************************   
    //File f2 = new File("F:/Melt_modelling/Model_outputs/Tau_calculator_output_(to_check_3).txt");
    FileDialog fd2 = new FileDialog(new Frame(), 
                                            "Save Tau calculation", FileDialog.SAVE);
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
    {}

    BufferedWriter bw = new BufferedWriter(fw);
    
    try
    {
         
      bw.write("Month, Tau estimate (I/I0), Sin TOA (Wm^-2), Sw measured (Wm^-2)");
      bw.newLine();
      
      //Iterator<Integer> iterator0 = year_list.iterator();
      Iterator<Integer> iteratorA = month_list.iterator();
      Iterator<Double> iteratorB = SinTOA_list.iterator();
      Iterator<Double> iteratorC = measured_Sw_list.iterator();
      Iterator<Double> iteratorD = tau1_list.iterator();
            while ((iteratorA.hasNext())&&(iteratorB.hasNext())&&(iteratorC.hasNext())&&(iteratorD.hasNext())) {
//			System.out.println(iterator1.next());
//                        System.out.println(iterator2.next());
//                        System.out.println(iterator3.next());              
                
                        String SinTOA_print = Double.toString(iteratorB.next());
                        String month_print = Integer.toString(iteratorA.next());
                        String tau1_print = Double.toString(iteratorD.next());
                        String Sw_measured = Double.toString(iteratorC.next());
                        //String tau_print = Double.toString(iterator2.next());
                        //String albedo_print = Double.toString(iterator3.next());
                        bw.write(month_print);
                        bw.write(",");                   
                        bw.write(tau1_print);
                        bw.write(",");
                        bw.write(SinTOA_print);
                        bw.write(",");
                        bw.write(Sw_measured);
                        //bw.write(",");
                        //bw.write(albedo_print);
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

        
        System.out.println("Tau calculator run successful");
        System.out.println("File saved at: " + f2.getAbsolutePath());
        
    }
  
  
}

