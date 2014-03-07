package energymodels;

import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.util.List;

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
 * This listener creates an OpenDialog box from which the user selects a .txt 
 * file containing a list of temperatures (in Kelvin). This file is required to 
 * be a list of values. On selecting the "Open Temperature File" option on the 
 * Model's GUI, and then opening the appropriate file, the file will be saved in
 * Storage.class (using a set method for which a get method is also available) 
 * as a 2D array and also, a copy of the read in file will be saved as 
 * "temptest.txt" within a (currently hard-wired) folder
 *
 * Note that this adds the temperatures from the opened text file to an Array 
 * List [implemented by Ant back in January] (10/10/12)
 * 
 * File to test this with is: 
 * "Chris H1:\Model_Test_FilesTemp_test_file_(Celsius).txt"
 * 
 * @author Chris Williams 9/11/11
 **/
public class OpenTemperatureFileListener implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;
  
  OpenTemperatureFileListener(GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {

    FileDialog fd = new FileDialog(new Frame(), "Open Monthly Temperature .txt file", FileDialog.LOAD);
    fd.setVisible(true);
    File f = new File (fd.getDirectory() + fd.getFile());

    //File f = new File("F:\\Model_Test_Files\\1920-1930_temps(K).txt");

    
    if((fd.getDirectory()== null)||(fd.getFile() == null))
    {
    System.out.println("Temp. file not uploaded");
    return;
    }
    
    else{
	
    FileReader fr = null;

    ArrayList<Double> tempArray = new ArrayList<Double>();

    try
    {

      fr = new FileReader(f);

    }
    catch (FileNotFoundException fnfe)
    {

      fnfe.printStackTrace();

    }

    BufferedReader br = new BufferedReader(fr);

    String textIn = " ";
    String[] file = null;

    try
    {

      while (textIn != null)
      {
        textIn = br.readLine();
        if (textIn != null)
        {
          //System.out.printf("textIn = '%s'\n", textIn);
          double temp = Double.parseDouble(textIn);
          tempArray.add(temp);
          System.out.println(temp); // Added 10/10/12. Print statement - remove
          
        }
      }

      br.close();
      fr.close();
      
      this.store.setTemperatureFile(tempArray);
      
      try
      {

        fr = new FileReader(f);

      }
      catch (FileNotFoundException fnfe)
      {

        fnfe.printStackTrace();

      }

    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }

    /**
     *  This next bit will simply save the code to a hardwired file so it can be checked to see if it worked
     **/
    File f2 = new File("F:\\Model_Test_Files\\tempTest_check.txt");
    FileWriter fw = null;

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

      for (Double temp : tempArray)
      {
        bw.write(String.valueOf(temp));
        bw.newLine();
      }

      bw.close();

    }
    catch (IOException ioe)
    {
      System.out.println(ioe.toString());
    }
    
//    for (Double temp : tempArray)
//    {
//      System.out.println(temp);
//    }

    System.out.println("Temperature file uploaded (check reference file)");
    } 
  }
}
