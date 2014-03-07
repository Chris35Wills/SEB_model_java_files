package energymodels;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.BufferedReader.*;
import java.io.CharArrayReader.*;
import java.io.FileInputStream.*;
import java.awt.image.*;
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
 * 
 * ****************************************************************************
 * 
 * @author Chris
 */
public class OpenHillShadeListener_March implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;

  OpenHillShadeListener_March(GUIPanel pn, Storage s)
  {

    panel = pn;
    store = s;

  }
  
  public void actionPerformed(ActionEvent e)
  {
  System.out.println("March working");
  
  // Creates the "Open a File" dialog box

    FileDialog fd = new FileDialog(new Frame (), "Open March hillshade ASCII", FileDialog.LOAD);
    fd.setVisible(true);

    // Creates a new File object from the file that is opened

    File file = new File(fd.getDirectory() + fd.getFile());

    
    if((fd.getDirectory()== null)||(fd.getFile() == null))
    {
    System.out.println("March hillshade file not uploaded");
    return;
    }
    
    else{
	
    // Creates a GRIDJava object from the opened file

    GRIDJava2 gj = new GRIDJava2(file, true, store);

    // Adds the GRIDJava object to the panel

    panel.addGRIDJavaMethod(gj);

    // Creates a 2D array from the GRIDJava object

    double hillshade_March[][] = gj.getTwoDdoubleArray();

    // Converts hillshade values (0-255) to %
          
       for(int i = 0; i<hillshade_March.length; i++){
        for(int j = 0; j<hillshade_March[i].length; j++){
           
           if ((hillshade_March[i][j] != -9999)&& (hillshade_March[i][j] != 0)){
           double percentage1 = (hillshade_March[i][j]/255.0); //half the way.... (* 100)
           double percentage2 = percentage1 * 100.0;
           hillshade_March[i][j] = percentage2;
           }
           
           else if (hillshade_March[i][j] == 0){
           hillshade_March[i][j] = 0;
           }
           
           else {
           hillshade_March[i][j] = -9999;          
           }
           
        }
       }
           
    store.setHillshade_March(hillshade_March);
    
    // ****************** End of hillshade conversion **********************

    // Creates a 1D array from the GRIDJava object

    int data1d[] = gj.getOneDintArray();

    // Creates an Image object

    Image temp = null;

    // Creates a MemoryImageSource object which uses methods from GRIDJava.java and takes in the 1D array created above

    MemoryImageSource mis = new MemoryImageSource(gj.getNumberOfColumns(), gj.getNumberOfRows(), data1d, 0, gj.getNumberOfColumns());

    // Using a toolkit, this creates the image and assigns it to the Image object created earlier (previously set to null)

    temp = panel.getToolkit().createImage(mis);

    // Displays the image on the panel

    panel.displayImage(temp);

    System.out.println("March hillshade uploaded");
    
    }
  }
  
}
