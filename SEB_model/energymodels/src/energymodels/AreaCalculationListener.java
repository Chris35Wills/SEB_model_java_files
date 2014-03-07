package energymodels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
   
 * Summary (10/1/13) Uses the ice thickness layer to calculate "real" glacier 
 * area.
 * 
 * A single cell area is 5m x 5m (25m^2). This loops through the thickness layer
 * and counts the cells that are != -9999 and are >0.
 * 
 * Conversion from m^2 to km^2 requires division by 1,000,000(!)
 * 
 * @author Chris (10/1/13)
 */
public class AreaCalculationListener implements ActionListener {
   
  private GUIPanel panel = null;
  private Storage store = null;
  
  AreaCalculationListener (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }
  
   @Override
  public void actionPerformed(ActionEvent e)
  {
      
  //System.out.println("Area calculator button ON");
  
  double thickness_surface[][];
  //int counter1[][] = new int[0][0];
  //int counter2[][] = new int[0][0];
  int AREA_counter1 = 0;
  int AREA_counter2 = 0;
  double Glacier_cells_POSITIVE = 0.0;
  double Glacier_cells_ALL = 0.0;
  int Glacier_cellsize = 25; //m^2 (5m x 5m)
  double Glacier_area_m2;
  double Glacier_area_km2;
  double NODATA_value = -9999;
          
  thickness_surface = store.getThickness_INITIAL();
  
  if(thickness_surface == null){
  
      System.out.println("Requires glacier thickness surface upload");
      return;
  }
  else
  {
            for(int i =0; i<thickness_surface.length; i++){
                for(int j =0; j<thickness_surface[i].length; j++){
                    
                    if(thickness_surface[i][j] != NODATA_value){     
                    Glacier_cells_ALL = AREA_counter2++;
                    }
                    
                    if ((thickness_surface[i][j] != NODATA_value) & (thickness_surface[i][j] > 0)){
                    Glacier_cells_POSITIVE = AREA_counter1++;                        
                    }
                    
                }
   
            }            
            
            Glacier_area_m2 = Glacier_cells_POSITIVE * Glacier_cellsize;
            Glacier_area_km2 = (Glacier_cells_POSITIVE * Glacier_cellsize)/1000000;
            
            System.out.println("Glacier cells (all not including NODATA): " + Glacier_cells_ALL);
            System.out.println("Glacier cells (>0): " + Glacier_cells_POSITIVE);
            System.out.println("Glacier area = " + Glacier_area_m2 + " m^2");
            System.out.println("Glacier area = " + Glacier_area_km2 + " km^2");
  
  }
 } 
}