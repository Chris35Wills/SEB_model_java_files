package energymodels;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

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
 * Opens a save dialogue box which enables the user to name and save a copy of the ice thickness 
 * surface (this would be facilitated following manipulation � e.g. �Melt Surface�, of the 
 * originally uploaded ice thickness surface) as an ASCII file. This allows the user to select the 
 * location and name of the file to be saved. The file must have a .txt extension (which is 
 * currently not prompted).
 * 
 * @author gycnw
 */
public class SaveNewThicknessListener implements ActionListener
{

  GRIDJava2 gj2;
  private Storage store = null;

  SaveNewThicknessListener(Storage s)
  {

    store = s;

  }

  public void actionPerformed(ActionEvent e)
  {

    FileDialog fd = new FileDialog(new Frame(), "Save thickness surface", FileDialog.SAVE);
    fd.setVisible(true);

    // Creates a new File object from the file that is opened

    File f = new File(fd.getDirectory() + fd.getFile());

    FileWriter fw;
    double[][] thicknessSurface = store.getThickness();
    DecimalFormat df = new DecimalFormat("#.####");

    //fw = new FileWriter (f, true);

    try
    {

      BufferedWriter bw = new BufferedWriter(new FileWriter(f));
      bw.write("ncols" + "         " + store.getOriginalNcols()); //+ "number");
      bw.write(System.getProperty("line.separator"));
      bw.write("nrows" + "         " + store.getOriginalNrows()); //+ "number");
      bw.write(System.getProperty("line.separator"));
      bw.write("xllcorner" + "     " + store.getOriginalXllcorner()); //"number");
      bw.write(System.getProperty("line.separator"));
      bw.write("yllcorner" + "     " + store.getOriginalYllcorner()); //"number");
      bw.write(System.getProperty("line.separator"));
      bw.write("cellsize" + "      " + store.getOriginalCellsize()); //"number");
      bw.write(System.getProperty("line.separator"));
      bw.write("NODATA_value" + "  " + "-9999");
      bw.write(System.getProperty("line.separator"));

      /**
       *   Write out the array data
       **/
      System.out.println("Thickness received");
      //String tempStr = ""; 

      for (int i = 0; i < thicknessSurface.length; i++)
      {
        for (int j = 0; j < thicknessSurface[i].length; j++)
        {


          if (thicknessSurface[i][j] == -9999.0)
          {

            bw.write("-9999 ");
          }
          else
          {

            bw.write(df.format(thicknessSurface[i][j]) + " ");

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
}
