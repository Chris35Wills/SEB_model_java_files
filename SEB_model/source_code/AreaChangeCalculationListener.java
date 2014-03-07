/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * 
 * ****************************************************************************
 * 
 * @author Chris
 */
public class AreaChangeCalculationListener implements ActionListener {
   
  private GUIPanel panel = null;
  private Storage store = null;
  
  AreaChangeCalculationListener (GUIPanel panel, Storage store)
  {

    this.panel = panel;
    this.store = store;

  }
  
   @Override
  public void actionPerformed(ActionEvent e)
  {
  System.out.println("Area change button ON");
  }

}
