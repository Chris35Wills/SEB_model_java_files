package energymodels;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

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
 * Calculates the value of Q on a daily basis using values taken from the 
 * Radiation on a slope and bulk flux calculators. 
 * 
 * Q = (1 - alpha)I + psi  --- This is in seconds (Wm^-2) 
 * Q daily = Q * 86400     --- Multiplied by the number of seconds in a day 
 * 
 * NB/ Should now account for a surfce of psi (where psi is calculated using 
 * lapse rate corrected temperature values from Bulk_Flux_Calc)Listener.java)
 * 
 * @author Chris 25/10/12 v 1.0
 */

public class Q_Daily_Listener implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;
  
  Q_Daily_Listener (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
      
      int NODATA_value = -9999;
      double[][] I_grid = store.getSurfaceRadiationSurface();
      double[][] psi_grid = store.getPsiSurface();
      double[][] Q_daily_grid = new double[I_grid.length][I_grid[0].length];
      double I_for_Q;
      double psi_for_Q;
      //double Q_i, Q_ii, Q_iii;
      double Q, Q_daily;
      
      
      
      // Albedo value is dependent on snow thickness and a true value for the 
      // presence of ice (HARDWIRED HERE FOR TESTING)
      double albedo = 0.5;
           
      for(int i = 0; i<I_grid.length; i++){
        for(int j = 0; j<I_grid[i].length; j++){
   
            I_for_Q = I_grid[i][j];
            psi_for_Q = psi_grid[i][j];
                        
            if(I_for_Q != NODATA_value){
                            
            Q = ((1-albedo)*I_for_Q) + psi_for_Q;
            Q_daily = Q * 86400;
            System.out.println(Q_daily);
            Q_daily_grid[i][j] = Q_daily;
                      
            } else if(I_for_Q == NODATA_value) {

            Q_daily_grid[i][j] = NODATA_value;

            }

            store.setQdailySurface(Q_daily_grid);
            
        }
      }
      
      System.out.println("Q daily surface calculated and stored");
      
/**
* This bit will open a save box to save the slope layer as an 
* ASCII, using the coordinates of the opened up elevation surface
**/    
      
//// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
      
    FileDialog fd = new FileDialog(new Frame(), 
                                        "Save Daily Q surface", FileDialog.SAVE);
    fd.setVisible(true);

    File f = new File(fd.getDirectory() + fd.getFile());
   
    //File f = new File("F://Melt_Modelling//Model_outputs//Daily_Q_13.11.12.txt");
    
    if((fd.getDirectory()== null)||(fd.getFile() == null))
    {
    System.out.println("Q daily file not saved");
    return;
    }
    
    else{  
        
    FileWriter fw;
    DecimalFormat df = new DecimalFormat("#.####");

    try
    {

      BufferedWriter bw = new BufferedWriter(new FileWriter(f));
      bw.write("ncols" + "         " + store.getOriginalNcols()); 
      bw.write(System.getProperty("line.separator"));
      bw.write("nrows" + "         " + store.getOriginalNrows()); 
      bw.write(System.getProperty("line.separator"));
      bw.write("xllcorner" + "     " + store.getOriginalXllcorner());
      bw.write(System.getProperty("line.separator"));
      bw.write("yllcorner" + "     " + store.getOriginalYllcorner());
      bw.write(System.getProperty("line.separator"));
      bw.write("cellsize" + "      " + store.getOriginalCellsize()); 
      bw.write(System.getProperty("line.separator"));
      bw.write("NODATA_value" + "  " + "-9999");
      bw.write(System.getProperty("line.separator"));

      /**
       *   Write out the array data
       **/
    
      String tempStr = "";
      
      for (int a = 0; a < Q_daily_grid.length; a++)
      {
        for (int b = 0; b < Q_daily_grid[a].length; b++)
        {


          if (Q_daily_grid[a][b] == NODATA_value)
          {

            bw.write("-9999 ");
          }
          else
          {

            bw.write(df.format(Q_daily_grid[a][b]) + " ");
            

          }

        }
        bw.newLine();
      }

      bw.close();

    }
    catch (IOException ioe)
    {

      ioe.printStackTrace();

    }
    
    }
    System.out.println("Q surface saved: " + f.getAbsolutePath());
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

      
      
  }
}