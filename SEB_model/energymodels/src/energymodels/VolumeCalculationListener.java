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
public class VolumeCalculationListener implements ActionListener {
   
  private GUIPanel panel = null;
  private Storage store = null;
  
  VolumeCalculationListener (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }
  
   @Override
  public void actionPerformed(ActionEvent e)
  {
  System.out.println("Volume calculator button ON");
  
  double thickness_initial[][]= store.getThickness_INITIAL();
  double thickness_end[][] = store.getThickness();
  
  if((thickness_initial == null)||(thickness_end == null)){
    System.out.println("Requires a thickness surface to be uploaded");
    return;
  }
  else
  {
    
  double volume_INITIAL[][] = new double[thickness_initial.length][thickness_initial[0].length];
  double volume_END[][] = new double[thickness_initial.length][thickness_initial[0].length];
  double volume_difference[][] = new double[thickness_initial.length][thickness_initial[0].length];
  
  double cell_area = 25; // cell area assuming 5m x 5m -> value is in m^2  
  
  double initial_cell_thickness;
  double initial_cell_volume = 0;
  
  double end_cell_volume = 0;
  double end_cell_thickness;
  
  double initial_volume_total = 0.0;
  double end_volume_total = 0.0;
  
  double total_volume_change = 0;
  
  int NODATA_value = -9999;
  
  // Creates a layer where each cell value is the volume of that cell according 
  // to the thickness of the initially uploaded thickness layer
  
  for(int i = 0; i<thickness_initial.length;i++){
      for(int j = 0; j<thickness_initial[i].length; j++){
          
          // Volume calc.
          if(thickness_initial[i][j] != NODATA_value){
          
          initial_cell_thickness = thickness_initial[i][j];
          initial_cell_volume = initial_cell_thickness * cell_area;
          volume_INITIAL[i][j] = initial_cell_volume;
          //System.out.println("Individ. cell volumes = " + volume_INITIAL[i][j]);
          initial_volume_total += initial_cell_volume;
          }
          
          else
          {
          volume_INITIAL[i][j] = NODATA_value;
          }
          
       }
  }
  
  System.out.println("Initial total glacier volume: " + initial_volume_total + " m^3");
  
  double initial_volume_total_km_3 = initial_volume_total/1000000000.0;
  
  System.out.println("Initial total glacier volume: " + initial_volume_total_km_3 + " km^3");
  
  // Creates a layer where each cell value is the volume of that cell according 
  // to the thickness of the thickness layer following a model run
  
  for(int i = 0; i<thickness_end.length;i++){
      for(int j = 0; j<thickness_end[i].length; j++){
          
          // Volume calc.
          if(thickness_end[i][j] != NODATA_value){
              
          end_cell_thickness = thickness_end[i][j];
          end_cell_volume = end_cell_thickness * cell_area;
          volume_END[i][j] = end_cell_volume;
          end_volume_total += end_cell_volume;
          
          }
          
          else
          {
          volume_END[i][j] = NODATA_value;
          }
          
       }
  }
  
  System.out.println("Total glacier volume following model run: " + end_volume_total + " m^3");
    
  double end_volume_total_km_3 = end_volume_total/1000000000.0;
  
  System.out.println("Total glacier volume following model run: " + end_volume_total_km_3 + " km^3");
  
  
  /*
   * Create a distributed volume change map (initial - output) and gives total volume change
   */
  
  for(int i = 0; i<thickness_initial.length;i++){
      for(int j = 0; j<thickness_initial[i].length; j++){
          
          if(volume_difference[i][j]!= NODATA_value)
          
          if(volume_difference[i][j] >= 0){
                volume_difference[i][j] = volume_INITIAL[i][j] - volume_END[i][j];
                total_volume_change += volume_difference[i][j];
          }
          
          else {
          volume_difference[i][j] = NODATA_value;
          }
  
       }

    }
  
  store.setVolumeDifference(volume_difference);  // Sets a volume difference surface (this could be saved for output if so desired)
  
  System.out.println("Total volume change: " + total_volume_change + " m^3");

  /**
* This bit will open a save box to save the slope layer as an 
* ASCII, using the coordinates of the opened up elevation surface
**/    
      
//// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
   
    FileDialog fd = new FileDialog(new Frame(), 
                                        "Volume change surface saved", FileDialog.SAVE);
    fd.setVisible(true);

    File f = new File(fd.getDirectory() + fd.getFile());
    //File f = new File("F:/Melt_modelling/Model_outputs/Hillshade_experiments/Dynamic_2010.txt");
    //File f = new File("F:/Melt_modelling/Model_outputs/Hillshade_experiments/ClearSky_2010.txt");
        
    if((fd.getDirectory()== null)||(fd.getFile() == null))
    {
    System.out.println("Glacier volume change not saved");
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

      for (int a = 0; a < volume_difference.length; a++)
      {
        for (int b = 0; b < volume_difference[a].length; b++)
        {


          if (volume_difference[a][b] == NODATA_value)
          {

            bw.write("-9999 ");
          }
          else
          {

            bw.write(df.format(volume_difference[a][b]) + " ");

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
    
   System.out.println("Volume change surface stored and saved: " + f.getAbsolutePath());
    
    }
   }
  }
}