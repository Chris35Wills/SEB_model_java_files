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
 * @author Chris
 */
public class ThicknessChangeCalculationListener implements ActionListener {
   
  private GUIPanel panel = null;
  private Storage store = null;
  
  ThicknessChangeCalculationListener (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }
  
   @Override
  public void actionPerformed(ActionEvent e)
  {
  //System.out.println("Thickness change button ON");
  
  double thickness_change_counter = 0;
  double thickness_change_additive_counter = 0;
  double thickness_change_additive_log = 0;
  double Mean_thickness_change;
  int NODATA_value = -9999;
  
  //System.out.println("Elevation change button ON");
  
  double thickness_initial[][]= store.getThickness_INITIAL();
  double thickness_end[][] = store.getThickness();
  
  if((thickness_initial == null)||(thickness_end == null)){
  System.out.println("Requires a thickness surface to be uploaded");
  return;
  }
  else
  {
  
  double thickness_change[][] = new double[thickness_initial.length][thickness_initial[0].length];
  
  /*
   * Calculates spatial elevation change by subtracting the model output 
   * surface from the intial input surface
   */
  
  for(int i =0; i<thickness_initial.length; i++){
        for(int j =0; j<thickness_initial[i].length; j++){
          
            if((thickness_initial[i][j] != NODATA_value) & (thickness_end[i][j] != NODATA_value)){
                
                thickness_change[i][j] = thickness_initial[i][j] - thickness_end[i][j];
                
            } else {
                
                thickness_change[i][j] = NODATA_value;
                
            }
        }                
   }
  
  /**
   *    Runs through the thickness change surface to calculate mean overall 
   *    thickness change (a single value)
   */
  for(int i = 0; i<thickness_change.length; i++){
        for(int j =0; j<thickness_change[i].length; j++){
        
            if(thickness_change[i][j] != NODATA_value){
                
                thickness_change_counter++;
                //thickness_change_additive_log = thickness_change_additive_counter + thickness_change[i][j];
                thickness_change_additive_log =+ thickness_change[i][j];
            } else {}
            
        }
            
   }
  
  Mean_thickness_change = thickness_change_additive_log/thickness_change_counter;
  System.out.println("Mean thickness change = " + Mean_thickness_change);
        

   /**
    * This bit will open a save box to save the slope layer as an 
    * ASCII, using the coordinates of the opened up elevation surface
    **/    
      
//// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
   
    FileDialog fd = new FileDialog(new Frame(), 
                                        "Thickness change surface saved", FileDialog.SAVE);
    fd.setVisible(true);

    File f = new File(fd.getDirectory() + fd.getFile());
    //File f = new File("F:/Melt_modelling/Model_outputs/Hillshade_experiments/Dynamic_2010.txt");
    //File f = new File("F:/Melt_modelling/Model_outputs/Hillshade_experiments/ClearSky_2010.txt");
        
    if((fd.getDirectory()== null)||(fd.getFile() == null))
    {
    System.out.println("Thickness change surface not saved");
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

      for (int a = 0; a < thickness_change.length; a++)
      {
        for (int b = 0; b < thickness_change[a].length; b++)
        {


          if (thickness_change[a][b] == NODATA_value)
          {

            bw.write("-9999 ");
          }
          else
          {

            bw.write(df.format(thickness_change[a][b]) + " ");

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
    
    
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
   System.out.println("Thickness change surface stored and saved: " + f.getAbsolutePath());
    }     
  }
  }
}


