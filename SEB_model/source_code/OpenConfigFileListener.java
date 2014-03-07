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

public class OpenConfigFileListener implements ActionListener {
   
  private GUIPanel panel = null;
  private Storage store = null;
  
  OpenConfigFileListener (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }
  
   @Override
  public void actionPerformed(ActionEvent e)
  {
      
      //ResourceBundle MeltModelConfig = ResourceBundle.getBundle("Model_props_20072008_oct_dec");
      //ResourceBundle MeltModelConfig = ResourceBundle.getBundle("Model_props_20082009_oct_dec");
      //ResourceBundle MeltModelConfig = ResourceBundle.getBundle("Model_props_20092010_oct_dec");
      ResourceBundle MeltModelConfig = ResourceBundle.getBundle("Model_props_20072010_oct_dec");
      //ResourceBundle MeltModelConfig = ResourceBundle.getBundle("Model_props_20092010_oct_dec_m1"); // SENSITIVITY (SNOW THICKNESS)
      
      
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
      
      String key_3 = MeltModelConfig.getString("JanHS_midnight");
      String key_4 = MeltModelConfig.getString("FebHS_midnight");
      String key_5 = MeltModelConfig.getString("MarHS_midnight");
      String key_6 = MeltModelConfig.getString("AprHS_midnight");
      String key_7 = MeltModelConfig.getString("MayHS_midnight");
      String key_8 = MeltModelConfig.getString("JunHS_midnight");
      String key_9 = MeltModelConfig.getString("JulHS_midnight");
      String key_10 = MeltModelConfig.getString("AugHS_midnight");
      String key_11 = MeltModelConfig.getString("SepHS_midnight");
      String key_12 = MeltModelConfig.getString("OctHS_midnight");
      String key_13 = MeltModelConfig.getString("NovHS_midnight");
      String key_14 = MeltModelConfig.getString("DecHS_midnight");
      
      String key_3b = MeltModelConfig.getString("JanHS_6am");
      String key_4b = MeltModelConfig.getString("FebHS_6am");
      String key_5b = MeltModelConfig.getString("MarHS_6am");
      String key_6b = MeltModelConfig.getString("AprHS_6am");
      String key_7b = MeltModelConfig.getString("MayHS_6am");
      String key_8b = MeltModelConfig.getString("JunHS_6am");
      String key_9b = MeltModelConfig.getString("JulHS_6am");
      String key_10b = MeltModelConfig.getString("AugHS_6am");
      String key_11b = MeltModelConfig.getString("SepHS_6am");
      String key_12b = MeltModelConfig.getString("OctHS_6am");
      String key_13b = MeltModelConfig.getString("NovHS_6am");
      String key_14b = MeltModelConfig.getString("DecHS_6am");
      
      String key_3c = MeltModelConfig.getString("JanHS_noon");
      String key_4c = MeltModelConfig.getString("FebHS_noon");
      String key_5c = MeltModelConfig.getString("MarHS_noon");
      String key_6c = MeltModelConfig.getString("AprHS_noon");
      String key_7c = MeltModelConfig.getString("MayHS_noon");
      String key_8c = MeltModelConfig.getString("JunHS_noon");
      String key_9c = MeltModelConfig.getString("JulHS_noon");
      String key_10c = MeltModelConfig.getString("AugHS_noon");
      String key_11c = MeltModelConfig.getString("SepHS_noon");
      String key_12c = MeltModelConfig.getString("OctHS_noon");
      String key_13c = MeltModelConfig.getString("NovHS_noon");
      String key_14c = MeltModelConfig.getString("DecHS_noon");
            
      String key_3d = MeltModelConfig.getString("JanHS_6pm");
      String key_4d = MeltModelConfig.getString("FebHS_6pm");
      String key_5d = MeltModelConfig.getString("MarHS_6pm");
      String key_6d = MeltModelConfig.getString("AprHS_6pm");
      String key_7d = MeltModelConfig.getString("MayHS_6pm");
      String key_8d = MeltModelConfig.getString("JunHS_6pm");
      String key_9d = MeltModelConfig.getString("JulHS_6pm");
      String key_10d = MeltModelConfig.getString("AugHS_6pm");
      String key_11d = MeltModelConfig.getString("SepHS_6pm");
      String key_12d = MeltModelConfig.getString("OctHS_6pm");
      String key_13d = MeltModelConfig.getString("NovHS_6pm");
      String key_14d = MeltModelConfig.getString("DecHS_6pm");
      
      String key_16 = MeltModelConfig.getString("MetData");
      String key_17 = MeltModelConfig.getString("Snow0708");
      String key_18 = MeltModelConfig.getString("Snow0809");
      String key_19 = MeltModelConfig.getString("Snow0910");
      String key_20 = MeltModelConfig.getString("Snow1011");
      
      // Elevation
      File f_elev = new File(key_1);
      GRIDJava2 gj = new GRIDJava2(f_elev, true, store);
      double elev_data[][] = gj.getTwoDdoubleArray();
      store.setElevation_INITIAL(elev_data);
      store.setElevation(elev_data);
      System.out.println("Elevation surface stored");
      
      //Thickness
      File f_thick = new File(key_2);
      GRIDJava2 gj2 = new GRIDJava2(f_thick, true, store);
      double thick_data[][] = gj2.getTwoDdoubleArray();
      store.setThickness_INITIAL(thick_data);
      store.setThickness(thick_data);
      System.out.println("Thickness surface stored");
      
      //*********************************************** HILLSHADE *************
      
      //***** MIDNIGHT
      
      //January HS midnight
      
      File f_jan_HS = new File(key_3);
      GRIDJava2 gj3 = new GRIDJava2(f_jan_HS, true, store);
      double jan_HS_data[][] = gj3.getTwoDdoubleArray();
      store.setHillshade_Jan_midnight(jan_HS_data);
      System.out.println("January hillshade surface stored");
      
      //February HS midnight
      
      File f_feb_HS = new File(key_4);
      GRIDJava2 gj4 = new GRIDJava2(f_feb_HS, true, store);
      double feb_HS_data[][] = gj4.getTwoDdoubleArray();
      store.setHillshade_Feb_midnight(feb_HS_data);
      System.out.println("February hillshade surface stored");
      
      //March HS midnight
            
      File f_mar_HS = new File(key_5);
      GRIDJava2 gj5 = new GRIDJava2(f_mar_HS, true, store);
      double mar_HS_data[][] = gj5.getTwoDdoubleArray();
      store.setHillshade_March_midnight(mar_HS_data);
      System.out.println("March hillshade surface stored");
      
      //April HS midnight
            
      File f_april_HS = new File(key_6);
      GRIDJava2 gj6 = new GRIDJava2(f_april_HS, true, store);
      double april_HS_data[][] = gj6.getTwoDdoubleArray();
      store.setHillshade_April_midnight(april_HS_data);
      System.out.println("April hillshade surface stored");
      
      //May HS midnight
            
      File f_may_HS = new File(key_7);
      GRIDJava2 gj7 = new GRIDJava2(f_may_HS, true, store);
      double may_HS_data[][] = gj7.getTwoDdoubleArray();
      store.setHillshade_May_midnight(may_HS_data);
      System.out.println("May hillshade surface stored");
      
      //June HS midnight
            
      File f_june_HS = new File(key_8);
      GRIDJava2 gj8 = new GRIDJava2(f_june_HS, true, store);
      double june_HS_data[][] = gj8.getTwoDdoubleArray();
      store.setHillshade_June_midnight(june_HS_data);
      System.out.println("June hillshade surface stored");
      
      //July HS midnight
            
      File f_july_HS = new File(key_9);
      GRIDJava2 gj9 = new GRIDJava2(f_july_HS, true, store);
      double july_HS_data[][] = gj9.getTwoDdoubleArray();
      store.setHillshade_July_midnight(july_HS_data);
      System.out.println("July hillshade surface stored");
      
      //August HS midnight
            
      File f_aug_HS = new File(key_10);
      GRIDJava2 gj10 = new GRIDJava2(f_aug_HS, true, store);
      double aug_HS_data[][] = gj10.getTwoDdoubleArray();
      store.setHillshade_August_midnight(aug_HS_data);
      System.out.println("August hillshade surface stored");
      
      //September HS midnight
            
      File f_sept_HS = new File(key_11);
      GRIDJava2 gj11 = new GRIDJava2(f_sept_HS, true, store);
      double sept_HS_data[][] = gj11.getTwoDdoubleArray();
      store.setHillshade_Sept_midnight(sept_HS_data);
      System.out.println("September hillshade surface stored");
      
      //October HS midnight
            
      File f_oct_HS = new File(key_12);
      GRIDJava2 gj12 = new GRIDJava2(f_oct_HS, true, store);
      double oct_HS_data[][] = gj12.getTwoDdoubleArray();
      store.setHillshade_Oct_midnight(oct_HS_data);
      System.out.println("October hillshade surface stored");
      
      //November HS midnight
            
      File f_nov_HS = new File(key_13);
      GRIDJava2 gj13 = new GRIDJava2(f_nov_HS, true, store);
      double nov_HS_data[][] = gj13.getTwoDdoubleArray();
      store.setHillshade_Nov_midnight(nov_HS_data);
      System.out.println("November hillshade surface stored");
      
      //December HS midnight
            
      File f_dec_HS = new File(key_14);
      GRIDJava2 gj14 = new GRIDJava2(f_dec_HS, true, store);
      double dec_HS_data[][] = gj14.getTwoDdoubleArray();
      store.setHillshade_Dec_midnight(dec_HS_data);
      System.out.println("December hillshade surface stored");
      
      //*** 6AM
      
      //January HS_6am 
      
      File f_jan_HS_6am = new File(key_3b);
      GRIDJava2 gj3b = new GRIDJava2(f_jan_HS_6am, true, store);
      double jan_HS_6am_data[][] = gj3b.getTwoDdoubleArray();
      store.setHillshade_Jan_6am(jan_HS_6am_data);
      System.out.println("January hillshade 6am surface stored");
      
      //February HS_6am 6am
      
      File f_feb_HS_6am = new File(key_4b);
      GRIDJava2 gj4b = new GRIDJava2(f_feb_HS_6am, true, store);
      double feb_HS_6am_data[][] = gj4b.getTwoDdoubleArray();
      store.setHillshade_Feb_6am(feb_HS_6am_data);
      System.out.println("February hillshade 6am surface stored");
      
      //March HS_6am 6am
            
      File f_mar_HS_6am = new File(key_5b);
      GRIDJava2 gj5b = new GRIDJava2(f_mar_HS_6am, true, store);
      double mar_HS_6am_data[][] = gj5b.getTwoDdoubleArray();
      store.setHillshade_March_6am(mar_HS_6am_data);
      System.out.println("March hillshade 6am surface stored");
      
      //April HS_6am 6am
            
      File f_april_HS_6am = new File(key_6b);
      GRIDJava2 gj6b = new GRIDJava2(f_april_HS_6am, true, store);
      double april_HS_6am_data[][] = gj6b.getTwoDdoubleArray();
      store.setHillshade_April_6am(april_HS_6am_data);
      System.out.println("April hillshade 6am surface stored");
      
      //May HS_6am 6am
            
      File f_may_HS_6am = new File(key_7b);
      GRIDJava2 gj7b = new GRIDJava2(f_may_HS_6am, true, store);
      double may_HS_6am_data[][] = gj7b.getTwoDdoubleArray();
      store.setHillshade_May_6am(may_HS_6am_data);
      System.out.println("May hillshade 6am surface stored");
      
      //June HS_6am 6am
            
      File f_june_HS_6am = new File(key_8b);
      GRIDJava2 gj8b = new GRIDJava2(f_june_HS_6am, true, store);
      double june_HS_6am_data[][] = gj8b.getTwoDdoubleArray();
      store.setHillshade_June_6am(june_HS_6am_data);
      System.out.println("June hillshade 6am surface stored");
      
      //July HS_6am 6am
            
      File f_july_HS_6am = new File(key_9b);
      GRIDJava2 gj9b = new GRIDJava2(f_july_HS_6am, true, store);
      double july_HS_6am_data[][] = gj9b.getTwoDdoubleArray();
      store.setHillshade_July_6am(july_HS_6am_data);
      System.out.println("July hillshade 6am surface stored");
      
      //August HS_6am 6am
            
      File f_aug_HS_6am = new File(key_10b);
      GRIDJava2 gj10b = new GRIDJava2(f_aug_HS_6am, true, store);
      double aug_HS_6am_data[][] = gj10b.getTwoDdoubleArray();
      store.setHillshade_August_6am(aug_HS_6am_data);
      System.out.println("August hillshade 6am surface stored");
      
      //September HS_6am 6am
            
      File f_sept_HS_6am = new File(key_11b);
      GRIDJava2 gj11b = new GRIDJava2(f_sept_HS_6am, true, store);
      double sept_HS_6am_data[][] = gj11b.getTwoDdoubleArray();
      store.setHillshade_Sept_6am(sept_HS_6am_data);
      System.out.println("September hillshade 6am surface stored");
      
      //October HS_6am 6am
            
      File f_oct_HS_6am = new File(key_12b);
      GRIDJava2 gj12b = new GRIDJava2(f_oct_HS_6am, true, store);
      double oct_HS_6am_data[][] = gj12b.getTwoDdoubleArray();
      store.setHillshade_Oct_6am(oct_HS_6am_data);
      System.out.println("October hillshade 6am surface stored");
      
      //November HS_6am 6am
            
      File f_nov_HS_6am = new File(key_13b);
      GRIDJava2 gj13b = new GRIDJava2(f_nov_HS_6am, true, store);
      double nov_HS_6am_data[][] = gj13b.getTwoDdoubleArray();
      store.setHillshade_Nov_6am(nov_HS_6am_data);
      System.out.println("November hillshade 6am surface stored");
      
      //December HS_6am 6am
            
      File f_dec_HS_6am = new File(key_14b);
      GRIDJava2 gj14b = new GRIDJava2(f_dec_HS_6am, true, store);
      double dec_HS_6am_data[][] = gj14b.getTwoDdoubleArray();
      store.setHillshade_Dec_6am(dec_HS_6am_data);
      System.out.println("December hillshade 6am surface stored");
     
      //**** Noon
            
      //January HS_noon 
      
      File f_jan_HS_noon = new File(key_3c);
      GRIDJava2 gj3c = new GRIDJava2(f_jan_HS_noon, true, store);
      double jan_HS_noon_data[][] = gj3c.getTwoDdoubleArray();
      store.setHillshade_Jan_noon(jan_HS_noon_data);
      System.out.println("January hillshade noon surface stored");
      
      //February HS_noon
      
      File f_fec_HS_noon = new File(key_4c);
      GRIDJava2 gj4c = new GRIDJava2(f_fec_HS_noon, true, store);
      double feb_HS_noon_data[][] = gj4c.getTwoDdoubleArray();
      store.setHillshade_Feb_noon(feb_HS_noon_data);
      System.out.println("February hillshade noon surface stored");
      
      //March HS_noon
            
      File f_mar_HS_noon = new File(key_5c);
      GRIDJava2 gj5c = new GRIDJava2(f_mar_HS_noon, true, store);
      double mar_HS_noon_data[][] = gj5c.getTwoDdoubleArray();
      store.setHillshade_March_noon(mar_HS_noon_data);
      System.out.println("March hillshade noon surface stored");
      
      //April HS_noon
            
      File f_april_HS_noon = new File(key_6c);
      GRIDJava2 gj6c = new GRIDJava2(f_april_HS_noon, true, store);
      double april_HS_noon_data[][] = gj6c.getTwoDdoubleArray();
      store.setHillshade_April_noon(april_HS_noon_data);
      System.out.println("April hillshade noon surface stored");
      
      //May HS_noon
            
      File f_may_HS_noon = new File(key_7c);
      GRIDJava2 gj7c = new GRIDJava2(f_may_HS_noon, true, store);
      double may_HS_noon_data[][] = gj7c.getTwoDdoubleArray();
      store.setHillshade_May_noon(may_HS_noon_data);
      System.out.println("May hillshade noon surface stored");
      
      //June HS_noon
            
      File f_june_HS_noon = new File(key_8c);
      GRIDJava2 gj8c = new GRIDJava2(f_june_HS_noon, true, store);
      double june_HS_noon_data[][] = gj8c.getTwoDdoubleArray();
      store.setHillshade_June_noon(june_HS_noon_data);
      System.out.println("June hillshade noon surface stored");
      
      //July HS_noon
            
      File f_july_HS_noon = new File(key_9c);
      GRIDJava2 gj9c = new GRIDJava2(f_july_HS_noon, true, store);
      double july_HS_noon_data[][] = gj9c.getTwoDdoubleArray();
      store.setHillshade_July_noon(july_HS_noon_data);
      System.out.println("July hillshade noon surface stored");
      
      //August HS_noon 
            
      File f_aug_HS_noon = new File(key_10c);
      GRIDJava2 gj10c = new GRIDJava2(f_aug_HS_noon, true, store);
      double aug_HS_noon_data[][] = gj10c.getTwoDdoubleArray();
      store.setHillshade_August_noon(aug_HS_noon_data);
      System.out.println("August hillshade noon surface stored");
      
      //September HS_noon 
            
      File f_sept_HS_noon = new File(key_11c);
      GRIDJava2 gj11c = new GRIDJava2(f_sept_HS_noon, true, store);
      double sept_HS_noon_data[][] = gj11c.getTwoDdoubleArray();
      store.setHillshade_Sept_noon(sept_HS_noon_data);
      System.out.println("September hillshade noon surface stored");
      
      //October HS_noon 
            
      File f_oct_HS_noon = new File(key_12c);
      GRIDJava2 gj12c = new GRIDJava2(f_oct_HS_noon, true, store);
      double oct_HS_noon_data[][] = gj12c.getTwoDdoubleArray();
      store.setHillshade_Oct_noon(oct_HS_noon_data);
      System.out.println("October hillshade noon surface stored");
      
      //November HS_noon 
            
      File f_nov_HS_noon = new File(key_13c);
      GRIDJava2 gj13c = new GRIDJava2(f_nov_HS_noon, true, store);
      double nov_HS_noon_data[][] = gj13c.getTwoDdoubleArray();
      store.setHillshade_Nov_noon(nov_HS_noon_data);
      System.out.println("November hillshade noon surface stored");
      
      //December HS_noon 
            
      File f_dec_HS_noon = new File(key_14c);
      GRIDJava2 gj14c = new GRIDJava2(f_dec_HS_noon, true, store);
      double dec_HS_noon_data[][] = gj14c.getTwoDdoubleArray();
      store.setHillshade_Dec_noon(dec_HS_noon_data);
      System.out.println("December hillshade noon surface stored");
      
      //**** 6PM
      
      //January HS_6pm 
      
      File f_jan_HS_6pm = new File(key_3d);
      GRIDJava2 gj3d = new GRIDJava2(f_jan_HS_6pm, true, store);
      double jan_HS_6pm_data[][] = gj3d.getTwoDdoubleArray();
      store.setHillshade_Jan_6pm(jan_HS_6pm_data);
      System.out.println("January hillshade 6pm surface stored");
      
      //February HS_6pm
      
      File f_fed_HS_6pm = new File(key_4d);
      GRIDJava2 gj4d = new GRIDJava2(f_fed_HS_6pm, true, store);
      double feb_HS_6pm_data[][] = gj4d.getTwoDdoubleArray();
      store.setHillshade_Feb_6pm(feb_HS_6pm_data);
      System.out.println("Fedruary hillshade 6pm surface stored");
      
      //March HS_6pm
            
      File f_mar_HS_6pm = new File(key_5d);
      GRIDJava2 gj5d = new GRIDJava2(f_mar_HS_6pm, true, store);
      double mar_HS_6pm_data[][] = gj5d.getTwoDdoubleArray();
      store.setHillshade_March_6pm(mar_HS_6pm_data);
      System.out.println("Mardh hillshade 6pm surface stored");
      
      //April HS_6pm
            
      File f_april_HS_6pm = new File(key_6d);
      GRIDJava2 gj6d = new GRIDJava2(f_april_HS_6pm, true, store);
      double april_HS_6pm_data[][] = gj6d.getTwoDdoubleArray();
      store.setHillshade_April_6pm(april_HS_6pm_data);
      System.out.println("April hillshade 6pm surface stored");
      
      //May HS_6pm
            
      File f_may_HS_6pm = new File(key_7d);
      GRIDJava2 gj7d = new GRIDJava2(f_may_HS_6pm, true, store);
      double may_HS_6pm_data[][] = gj7d.getTwoDdoubleArray();
      store.setHillshade_May_6pm(may_HS_6pm_data);
      System.out.println("May hillshade 6pm surface stored");
      
      //June HS_6pm
            
      File f_june_HS_6pm = new File(key_8d);
      GRIDJava2 gj8d = new GRIDJava2(f_june_HS_6pm, true, store);
      double june_HS_6pm_data[][] = gj8d.getTwoDdoubleArray();
      store.setHillshade_June_6pm(june_HS_6pm_data);
      System.out.println("June hillshade 6pm surface stored");
      
      //July HS_6pm
            
      File f_july_HS_6pm = new File(key_9d);
      GRIDJava2 gj9d = new GRIDJava2(f_july_HS_6pm, true, store);
      double july_HS_6pm_data[][] = gj9d.getTwoDdoubleArray();
      store.setHillshade_July_6pm(july_HS_6pm_data);
      System.out.println("July hillshade 6pm surface stored");
      
      //August HS_6pm 
            
      File f_aug_HS_6pm = new File(key_10d);
      GRIDJava2 gj10d = new GRIDJava2(f_aug_HS_6pm, true, store);
      double aug_HS_6pm_data[][] = gj10d.getTwoDdoubleArray();
      store.setHillshade_August_6pm(aug_HS_6pm_data);
      System.out.println("August hillshade 6pm surface stored");
      
      //September HS_6pm 
            
      File f_sept_HS_6pm = new File(key_11d);
      GRIDJava2 gj11d = new GRIDJava2(f_sept_HS_6pm, true, store);
      double sept_HS_6pm_data[][] = gj11d.getTwoDdoubleArray();
      store.setHillshade_Sept_6pm(sept_HS_6pm_data);
      System.out.println("September hillshade 6pm surface stored");
      
      //October HS_6pm 
            
      File f_odt_HS_6pm = new File(key_12d);
      GRIDJava2 gj12d = new GRIDJava2(f_odt_HS_6pm, true, store);
      double oct_HS_6pm_data[][] = gj12d.getTwoDdoubleArray();
      store.setHillshade_Oct_6pm(oct_HS_6pm_data);
      System.out.println("October hillshade 6pm surface stored");
      
      //November HS_6pm 
            
      File f_nov_HS_6pm = new File(key_13d);
      GRIDJava2 gj13d = new GRIDJava2(f_nov_HS_6pm, true, store);
      double nov_HS_6pm_data[][] = gj13d.getTwoDdoubleArray();
      store.setHillshade_Nov_6pm(nov_HS_6pm_data);
      System.out.println("November hillshade 6pm surface stored");
      
      //December HS_6pm 
            
      File f_ded_HS_6pm = new File(key_14d);
      GRIDJava2 gj14d = new GRIDJava2(f_ded_HS_6pm, true, store);
      double dec_HS_6pm_data[][] = gj14d.getTwoDdoubleArray();
      store.setHillshade_Dec_6pm(dec_HS_6pm_data);
      System.out.println("December hillshade 6pm surface stored");
      
      //Met data
      //********************************
      
      File f_met_data = new File(key_16);
      
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

      fr = new FileReader(f_met_data);

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
                         
                      System.out.println(dayStore + "/" + monthStore + "/" + yearStore);
//                    System.out.println("Day: " + dayStore);
//                    System.out.println("Year: " + yearStore);
//                    System.out.println("Month: " + monthStore);
//                    System.out.println("Temp: " + tempStore); 
                      System.out.println("Precipitation: " + precipStore);
//                    System.out.println("Precipitation season : " + precipSeasonStore);
//                    
//                    System.out.println("Tau estimate: " + tauEstimateStore);
//                    
//                    System.out.println("TOA midnight: " + TOA_midnight_store);
//                    System.out.println("TOA 6am: " + TOA_6am_store);
//                    System.out.println("TOA noon: " + TOA_noon_store);
//                    System.out.println("TOA 6pm: " + TOA_6pm_store);
//                            
//                    System.out.println("Zenith midnight: " + Zenith_midnight_store);
//                    System.out.println("Zenith 6am: " + Zenith_6am_store);
//                    System.out.println("Zenith noon: " + Zenith_noon_store);
//                    System.out.println("Zenith 6pm: " + Zenith_6pm_store);
//                          
//                    System.out.println("Azimuth midnight: " + Azimuth_midnight_store);
//                    System.out.println("Azimuth 6am: " + Azimuth_6am_store);
//                    System.out.println("Azimuth noon: " + Azimuth_noon_store);
//                    System.out.println("Azimuth 6pm: " + Azimuth_6pm_store);
                              
		   }
                   
		}
 
    } catch(Exception e_br){
        
        System.out.println("Met file read in fail");
    
    }
    
      //Snow surface 2007-2008
            
      File f_snow_0708 = new File(key_17);
      GRIDJava2 gj17 = new GRIDJava2(f_snow_0708, true, store);
      double snow_0708_data[][] = gj17.getTwoDdoubleArray();
      store.set_snow_surface_0708(snow_0708_data);
      System.out.println("snow_0708_data surface stored");
      
      //Snow surface 2008-2009
            
      File f_snow_0809 = new File(key_18);
      GRIDJava2 gj18 = new GRIDJava2(f_snow_0809, true, store);
      double snow_0809_data[][] = gj18.getTwoDdoubleArray();
      store.set_snow_surface_0809(snow_0809_data);
      System.out.println("snow_0809_data surface stored");
      
      //Snow surface 2009-2010
            
      File f_snow_0910 = new File(key_19);
      GRIDJava2 gj19 = new GRIDJava2(f_snow_0910, true, store);
      double snow_0910_data[][] = gj19.getTwoDdoubleArray();
      store.set_snow_surface_0910(snow_0910_data);
      System.out.println("snow_0910_data surface stored");
      
      //Snow surface 2010-2011
            
      File f_snow_1011 = new File(key_20);
      GRIDJava2 gj20 = new GRIDJava2(f_snow_1011, true, store);
      double snow_1011_data[][] = gj20.getTwoDdoubleArray();
      store.set_snow_surface_1011(snow_1011_data);
      System.out.println("snow_1011_data surface stored");
      
      //********************************
      
      System.out.println("Elevation path: " + key_1);
      System.out.println("Thickness path: " + key_2);
      //
      System.out.println("Jan HS path: " + key_3);
      System.out.println("Feb HS path: " + key_4);
      System.out.println("Mar HS path: " + key_5);
      System.out.println("Apr HS path: " + key_6);
      System.out.println("May HS path: " + key_7);
      System.out.println("Jun HS path: " + key_8);
      System.out.println("Jul HS path: " + key_9);
      System.out.println("Aug HS path: " + key_10);
      System.out.println("Sep HS path: " + key_11);
      System.out.println("Oct HS path: " + key_12);
      System.out.println("Nov HS path: " + key_13);
      System.out.println("Dec HS path: " + key_14);
      //b
      System.out.println("Jan HS path: " + key_3b);
      System.out.println("Feb HS path: " + key_4b);
      System.out.println("Mar HS path: " + key_5b);
      System.out.println("Apr HS path: " + key_6b);
      System.out.println("May HS path: " + key_7b);
      System.out.println("Jun HS path: " + key_8b);
      System.out.println("Jul HS path: " + key_9b);
      System.out.println("Aug HS path: " + key_10b);
      System.out.println("Sep HS path: " + key_11b);
      System.out.println("Oct HS path: " + key_12b);
      System.out.println("Nov HS path: " + key_13b);
      System.out.println("Dec HS path: " + key_14b);
      //c
      System.out.println("Jan HS path: " + key_3c);
      System.out.println("Feb HS path: " + key_4c);
      System.out.println("Mar HS path: " + key_5c);
      System.out.println("Apr HS path: " + key_6c);
      System.out.println("May HS path: " + key_7c);
      System.out.println("Jun HS path: " + key_8c);
      System.out.println("Jul HS path: " + key_9c);
      System.out.println("Aug HS path: " + key_10c);
      System.out.println("Sep HS path: " + key_11c);
      System.out.println("Oct HS path: " + key_12c);
      System.out.println("Nov HS path: " + key_13c);
      System.out.println("Dec HS path: " + key_14c);
      //d
      System.out.println("Jan HS path: " + key_3d);
      System.out.println("Feb HS path: " + key_4d);
      System.out.println("Mar HS path: " + key_5d);
      System.out.println("Apr HS path: " + key_6d);
      System.out.println("May HS path: " + key_7d);
      System.out.println("Jun HS path: " + key_8d);
      System.out.println("Jul HS path: " + key_9d);
      System.out.println("Aug HS path: " + key_10d);
      System.out.println("Sep HS path: " + key_11d);
      System.out.println("Oct HS path: " + key_12d);
      System.out.println("Nov HS path: " + key_13d);
      System.out.println("Dec HS path: " + key_14d);
      //
      System.out.println("Met data path: " + key_16);
      //
      System.out.println("Snow 07-08 path: " + key_17);
      System.out.println("Snow 08-09 path: " + key_18);
      System.out.println("Snow 09-10 path: " + key_19);
      System.out.println("Snow 10-11 path: " + key_20);
      
            
  }
}

