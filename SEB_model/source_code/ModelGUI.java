package energymodels;

import java.awt.*;
import java.awt.Graphics.*;
import java.awt.event.*;
import javax.swing.JComponent.*;

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
 *  * @author Chris 2011-2013
 */
public class ModelGUI extends Frame implements WindowListener
{

  public ModelGUI()
  {

    super("Temperature Index v1.0");
    super.setSize(700, 700);
    super.setVisible(true);
    addWindowListener(this);

    GUIPanel panel = new GUIPanel();
    Storage store = new Storage();

    panel.setSize(700, 700);
    add(panel, BorderLayout.CENTER);

    MenuBar Menu = new MenuBar();
    setMenuBar(Menu);

    Menu File = new Menu("File");
    Menu.add(File);
    
    MenuItem OpenConfigFile = new MenuItem("Open configuration file");
    File.add(OpenConfigFile);
    OpenConfigFileListener OpenConfigFile_Lst = new OpenConfigFileListener(panel, store);
    OpenConfigFile.addActionListener(OpenConfigFile_Lst);
    
    MenuItem OpenConfigFile_historical = new MenuItem("Open configuration file [historical model run]");
    File.add(OpenConfigFile_historical);
    OpenConfigFile_historical_Listener OpenConfigFile_Historical_list = new OpenConfigFile_historical_Listener(panel, store);
    OpenConfigFile_historical.addActionListener(OpenConfigFile_Historical_list);

    MenuItem OpenConfig_historical_largeFiles_button = new MenuItem("Open configuration file [historical: large file sizes]");
    File.add(OpenConfig_historical_largeFiles_button);
    OpenConfig_historical_largeFiles OpenConfig_historical_largeFiles = new OpenConfig_historical_largeFiles(panel, store);
    OpenConfig_historical_largeFiles_button.addActionListener(OpenConfig_historical_largeFiles);
            
    Menu openSurfaces = new Menu("Open Surfaces");

    MenuItem OpenElevation = new MenuItem("Open Elevation surface");
    //File.add(OpenElevation);
    openSurfaces.add(OpenElevation);
    OpenElevationListener el = new OpenElevationListener(panel, store);
    OpenElevation.addActionListener(el);  
    
    MenuItem OpenIceThickness = new MenuItem("Open Ice Thickness surface");
    //File.add(OpenIceThickness);
    openSurfaces.add(OpenIceThickness);
    OpenIceThicknessListener itl = new OpenIceThicknessListener(panel, store);
    OpenIceThickness.addActionListener(itl);
    
    Menu OpenContempWinterSnowSurface = new Menu("Open contemporary winter snow surface");
    openSurfaces.add(OpenContempWinterSnowSurface);
    
    MenuItem OpenContempWinterSnowSurface_0708 = new MenuItem("2007 - 2008 winter snow surface");
    OpenContempWinterSnowSurface.add(OpenContempWinterSnowSurface_0708);
    OpenContempWinterSnowSurface_0708_Listener ocwssl0708 = new OpenContempWinterSnowSurface_0708_Listener(panel, store);
    OpenContempWinterSnowSurface_0708.addActionListener(ocwssl0708);
    
//    OpenContempWinterSnowSurfaceListener ocwssl = new OpenContempWinterSnowSurfaceListener(panel, store);
//    OpenContempWinterSnowSurface.addActionListener(ocwssl);

//    MenuItem OpenSurfaceType = new MenuItem("Open Surface Type surface");
//    //File.add(OpenSurfaceType);
//    openSurfaces.add(OpenSurfaceType);
//    OpenSurfaceTypeListener stl = new OpenSurfaceTypeListener(panel, store);
//    OpenSurfaceType.addActionListener(stl);

    File.add(openSurfaces);
        
    Menu openHillshade_all = new Menu("Open monthly hillshade surfaces");
    openSurfaces.add(openHillshade_all);
    
    MenuItem OpenHillShade = new MenuItem("Open Hill Shade surface");
    openHillshade_all.add(OpenHillShade);
    OpenHillShadeListener hsl = new OpenHillShadeListener(panel, store);
    OpenHillShade.addActionListener(hsl);
    
    //January
    MenuItem OpenHillShade_Jan = new MenuItem("January hillshade");
    openHillshade_all.add(OpenHillShade_Jan);
    OpenHillShadeListener_Jan hslj = new OpenHillShadeListener_Jan(panel, store);
    OpenHillShade_Jan.addActionListener(hslj);
    
            //January midnight

            MenuItem OpenHillShade_Jan_midnight = new MenuItem("January midnight hillshade");
            openHillshade_all.add(OpenHillShade_Jan_midnight);
            OpenHillShade_Jan_midnight_Listener hsljmidnight = new OpenHillShade_Jan_midnight_Listener(panel, store);
            OpenHillShade_Jan_midnight.addActionListener(hsljmidnight);
            
            //January 6am
            
            MenuItem OpenHillShade_Jan_6am = new MenuItem("January 6am hillshade");
            openHillshade_all.add(OpenHillShade_Jan_6am);
            OpenHillShade_Jan_6am_Listener hslj6am = new OpenHillShade_Jan_6am_Listener(panel, store);
            OpenHillShade_Jan_6am.addActionListener(hslj6am);
                        
            //January noon
            
            MenuItem OpenHillShade_Jan_noon = new MenuItem("January noon hillshade");
            openHillshade_all.add(OpenHillShade_Jan_noon);
            OpenHillShade_Jan_noon_Listener hsljnoon = new OpenHillShade_Jan_noon_Listener(panel, store);
            OpenHillShade_Jan_noon.addActionListener(hsljnoon);
                        
            //January 6pm
            
            MenuItem OpenHillShade_Jan_6pm = new MenuItem("January 6pm hillshade");
            openHillshade_all.add(OpenHillShade_Jan_6pm);
            OpenHillShade_Jan_6pm_Listener hslj6pm = new OpenHillShade_Jan_6pm_Listener(panel, store);
            OpenHillShade_Jan_6pm.addActionListener(hslj6pm);
    
    //February 
        MenuItem OpenHillShade_Feb = new MenuItem("February hillshade");
    openHillshade_all.add(OpenHillShade_Feb);
    OpenHillShadeListener_Feb hslf = new OpenHillShadeListener_Feb(panel, store);
    OpenHillShade_Feb.addActionListener(hslf);
        
            //February  midnight

            MenuItem OpenHillShade_Feb_midnight = new MenuItem("February midnight hillshade");
            openHillshade_all.add(OpenHillShade_Feb_midnight);
            OpenHillShade_Feb_midnight_Listener hslfmidnight = new OpenHillShade_Feb_midnight_Listener(panel, store);
            OpenHillShade_Feb_midnight.addActionListener(hslfmidnight);
            
            //February  6am
            
            MenuItem OpenHillShade_Feb_6am = new MenuItem("February 6am hillshade");
            openHillshade_all.add(OpenHillShade_Feb_6am);
            OpenHillShade_Feb_6am_Listener hslf6am = new OpenHillShade_Feb_6am_Listener(panel, store);
            OpenHillShade_Feb_6am.addActionListener(hslf6am);
                        
            //February  noon
            
            MenuItem OpenHillShade_Feb_noon = new MenuItem("February noon hillshade");
            openHillshade_all.add(OpenHillShade_Feb_noon);
            OpenHillShade_Feb_noon_Listener hslfnoon = new OpenHillShade_Feb_noon_Listener(panel, store);
            OpenHillShade_Feb_noon.addActionListener(hslfnoon);
                        
            //February  6pm
            
            MenuItem OpenHillShade_Feb_6pm = new MenuItem("February 6pm hillshade");
            openHillshade_all.add(OpenHillShade_Feb_6pm);
            OpenHillShade_Feb_6pm_Listener hslf6pm = new OpenHillShade_Feb_6pm_Listener(panel, store);
            OpenHillShade_Feb_6pm.addActionListener(hslf6pm);
    
    //March
        MenuItem OpenHillShade_March = new MenuItem("March hillshade");
    openHillshade_all.add(OpenHillShade_March);
    OpenHillShadeListener_March hslmarch = new OpenHillShadeListener_March(panel, store);
    OpenHillShade_March.addActionListener(hslmarch);
    
            //March  midnight

            MenuItem OpenHillShade_March_midnight = new MenuItem("March midnight hillshade");
            openHillshade_all.add(OpenHillShade_March_midnight);
            OpenHillShade_March_midnight_Listener hslmmidnight = new OpenHillShade_March_midnight_Listener(panel, store);
            OpenHillShade_March_midnight.addActionListener(hslmmidnight);
            
            //March  6am
            
            MenuItem OpenHillShade_March_6am = new MenuItem("March 6am hillshade");
            openHillshade_all.add(OpenHillShade_March_6am);
            OpenHillShade_March_6am_Listener hslm6am = new OpenHillShade_March_6am_Listener(panel, store);
            OpenHillShade_March_6am.addActionListener(hslm6am);
                        
            //March  noon
            
            MenuItem OpenHillShade_March_noon = new MenuItem("March noon hillshade");
            openHillshade_all.add(OpenHillShade_March_noon);
            OpenHillShade_March_noon_Listener hslmnoon = new OpenHillShade_March_noon_Listener(panel, store);
            OpenHillShade_March_noon.addActionListener(hslmnoon);
                        
            //March  6pm
            
            MenuItem OpenHillShade_March_6pm = new MenuItem("March 6pm hillshade");
            openHillshade_all.add(OpenHillShade_March_6pm);
            OpenHillShade_March_6pm_Listener hslm6pm = new OpenHillShade_March_6pm_Listener(panel, store);
            OpenHillShade_March_6pm.addActionListener(hslm6pm);
    
    //April
        MenuItem OpenHillShade_April = new MenuItem("April hillshade");
    openHillshade_all.add(OpenHillShade_April);
    OpenHillShadeListener_April hslapril = new OpenHillShadeListener_April(panel, store);
    OpenHillShade_April.addActionListener(hslapril);
    
            //April  midnight

            MenuItem OpenHillShade_April_midnight = new MenuItem("April midnight hillshade");
            openHillshade_all.add(OpenHillShade_April_midnight);
            OpenHillShade_April_midnight_Listener hslamidnight = new OpenHillShade_April_midnight_Listener(panel, store);
            OpenHillShade_April_midnight.addActionListener(hslamidnight);
            
            //April  6am
            
            MenuItem OpenHillShade_April_6am = new MenuItem("April 6am hillshade");
            openHillshade_all.add(OpenHillShade_April_6am);
            OpenHillShade_April_6am_Listener hsla6am = new OpenHillShade_April_6am_Listener(panel, store);
            OpenHillShade_April_6am.addActionListener(hsla6am);
                        
            //April  noon
            
            MenuItem OpenHillShade_April_noon = new MenuItem("April noon hillshade");
            openHillshade_all.add(OpenHillShade_April_noon);
            OpenHillShade_April_noon_Listener hslanoon = new OpenHillShade_April_noon_Listener(panel, store);
            OpenHillShade_April_noon.addActionListener(hslanoon);
                        
            //April  6pm
            
            MenuItem OpenHillShade_April_6pm = new MenuItem("April 6pm hillshade");
            openHillshade_all.add(OpenHillShade_April_6pm);
            OpenHillShade_April_6pm_Listener hsla6pm = new OpenHillShade_April_6pm_Listener(panel, store);
            OpenHillShade_April_6pm.addActionListener(hsla6pm);
    
    //May
        MenuItem OpenHillShade_May = new MenuItem("May hillshade");
    openHillshade_all.add(OpenHillShade_May);
    OpenHillShadeListener_May hslmay = new OpenHillShadeListener_May(panel, store);
    OpenHillShade_May.addActionListener(hslmay);
    
            //May  midnight

            MenuItem OpenHillShade_May_midnight = new MenuItem("May midnight hillshade");
            openHillshade_all.add(OpenHillShade_May_midnight);
            OpenHillShade_May_midnight_Listener hslmMAYmidnight = new OpenHillShade_May_midnight_Listener(panel, store);
            OpenHillShade_May_midnight.addActionListener(hslmMAYmidnight);
            
            //May  6am
            
            MenuItem OpenHillShade_May_6am = new MenuItem("May 6am hillshade");
            openHillshade_all.add(OpenHillShade_May_6am);
            OpenHillShade_May_6am_Listener hslmMAY6am = new OpenHillShade_May_6am_Listener(panel, store);
            OpenHillShade_May_6am.addActionListener(hslmMAY6am);
                        
            //May  noon
            
            MenuItem OpenHillShade_May_noon = new MenuItem("May noon hillshade");
            openHillshade_all.add(OpenHillShade_May_noon);
            OpenHillShade_May_noon_Listener hslmMAYnoon = new OpenHillShade_May_noon_Listener(panel, store);
            OpenHillShade_May_noon.addActionListener(hslmMAYnoon);
                        
            //May  6pm
            
            MenuItem OpenHillShade_May_6pm = new MenuItem("May 6pm hillshade");
            openHillshade_all.add(OpenHillShade_May_6pm);
            OpenHillShade_May_6pm_Listener hslmMAY6pm = new OpenHillShade_May_6pm_Listener(panel, store);
            OpenHillShade_May_6pm.addActionListener(hslmMAY6pm);
    
    //June
        MenuItem OpenHillShade_June = new MenuItem("June hillshade");
    openHillshade_all.add(OpenHillShade_June);
    OpenHillShadeListener_June hsljune = new OpenHillShadeListener_June(panel, store);
    OpenHillShade_June.addActionListener(hsljune);
    
            //June  midnight

            MenuItem OpenHillShade_June_midnight = new MenuItem("June midnight hillshade");
            openHillshade_all.add(OpenHillShade_June_midnight);
            OpenHillShade_June_midnight_Listener hslJUNEmidnight = new OpenHillShade_June_midnight_Listener(panel, store);
            OpenHillShade_June_midnight.addActionListener(hslJUNEmidnight);
            
            //June  6am
            
            MenuItem OpenHillShade_June_6am = new MenuItem("June 6am hillshade");
            openHillshade_all.add(OpenHillShade_June_6am);
            OpenHillShade_June_6am_Listener hslJUNE6am = new OpenHillShade_June_6am_Listener(panel, store);
            OpenHillShade_June_6am.addActionListener(hslJUNE6am);
                        
            //June  noon
            
            MenuItem OpenHillShade_June_noon = new MenuItem("June noon hillshade");
            openHillshade_all.add(OpenHillShade_June_noon);
            OpenHillShade_June_noon_Listener hslJUNEnoon = new OpenHillShade_June_noon_Listener(panel, store);
            OpenHillShade_June_noon.addActionListener(hslJUNEnoon);
                        
            //June  6pm
            
            MenuItem OpenHillShade_June_6pm = new MenuItem("June 6pm hillshade");
            openHillshade_all.add(OpenHillShade_June_6pm);
            OpenHillShade_June_6pm_Listener hslJUNE6pm = new OpenHillShade_June_6pm_Listener(panel, store);
            OpenHillShade_June_6pm.addActionListener(hslJUNE6pm);
    
    //July
        MenuItem OpenHillShade_July = new MenuItem("July hillshade");
    openHillshade_all.add(OpenHillShade_July);
    OpenHillShadeListener_July hsljuly = new OpenHillShadeListener_July(panel, store);
    OpenHillShade_July.addActionListener(hsljuly);
    
            //July  midnight

            MenuItem OpenHillShade_July_midnight = new MenuItem("July midnight hillshade");
            openHillshade_all.add(OpenHillShade_July_midnight);
            OpenHillShade_July_midnight_Listener hslJULYmidnight = new OpenHillShade_July_midnight_Listener(panel, store);
            OpenHillShade_July_midnight.addActionListener(hslJULYmidnight);
            
            //July  6am
            
            MenuItem OpenHillShade_July_6am = new MenuItem("July 6am hillshade");
            openHillshade_all.add(OpenHillShade_July_6am);
            OpenHillShade_July_6am_Listener hslJULY6am = new OpenHillShade_July_6am_Listener(panel, store);
            OpenHillShade_July_6am.addActionListener(hslJULY6am);
                        
            //July  noon
            
            MenuItem OpenHillShade_July_noon = new MenuItem("July noon hillshade");
            openHillshade_all.add(OpenHillShade_July_noon);
            OpenHillShade_July_noon_Listener hslJULYnoon = new OpenHillShade_July_noon_Listener(panel, store);
            OpenHillShade_July_noon.addActionListener(hslJULYnoon);
                        
            //July  6pm
            
            MenuItem OpenHillShade_July_6pm = new MenuItem("July 6pm hillshade");
            openHillshade_all.add(OpenHillShade_July_6pm);
            OpenHillShade_July_6pm_Listener hslJULY6pm = new OpenHillShade_July_6pm_Listener(panel, store);
            OpenHillShade_July_6pm.addActionListener(hslJULY6pm);
    
    //August
        MenuItem OpenHillShade_Aug = new MenuItem("August hillshade");
    openHillshade_all.add(OpenHillShade_Aug);
    OpenHillShadeListener_Aug hslaug = new OpenHillShadeListener_Aug(panel, store);
    OpenHillShade_Aug.addActionListener(hslaug);
    
            //August  midnight

            MenuItem OpenHillShade_August_midnight = new MenuItem("August midnight hillshade");
            openHillshade_all.add(OpenHillShade_August_midnight);
            OpenHillShade_August_midnight_Listener hslAUGmidnight = new OpenHillShade_August_midnight_Listener(panel, store);
            OpenHillShade_August_midnight.addActionListener(hslAUGmidnight);
            
            //August  6am
            
            MenuItem OpenHillShade_August_6am = new MenuItem("August 6am hillshade");
            openHillshade_all.add(OpenHillShade_August_6am);
            OpenHillShade_August_6am_Listener hslAUG6am = new OpenHillShade_August_6am_Listener(panel, store);
            OpenHillShade_August_6am.addActionListener(hslAUG6am);
                        
            //August  noon
            
            MenuItem OpenHillShade_August_noon = new MenuItem("August noon hillshade");
            openHillshade_all.add(OpenHillShade_August_noon);
            OpenHillShade_August_noon_Listener hslAUGnoon = new OpenHillShade_August_noon_Listener(panel, store);
            OpenHillShade_August_noon.addActionListener(hslAUGnoon);
                        
            //August  6pm
            
            MenuItem OpenHillShade_August_6pm = new MenuItem("August 6pm hillshade");
            openHillshade_all.add(OpenHillShade_August_6pm);
            OpenHillShade_August_6pm_Listener hslAUG6pm = new OpenHillShade_August_6pm_Listener(panel, store);
            OpenHillShade_August_6pm.addActionListener(hslAUG6pm);
    
    //September
        MenuItem OpenHillShade_Sept = new MenuItem("September hillshade");
    openHillshade_all.add(OpenHillShade_Sept);
    OpenHillShadeListener_Sept hslsept = new OpenHillShadeListener_Sept(panel, store);
    OpenHillShade_Sept.addActionListener(hslsept);
    
            //September  midnight

            MenuItem OpenHillShade_September_midnight = new MenuItem("September midnight hillshade");
            openHillshade_all.add(OpenHillShade_September_midnight);
            OpenHillShade_September_midnight_Listener hslSEPTmidnight = new OpenHillShade_September_midnight_Listener(panel, store);
            OpenHillShade_September_midnight.addActionListener(hslSEPTmidnight);
            
            //September  6am
            
            MenuItem OpenHillShade_September_6am = new MenuItem("September 6am hillshade");
            openHillshade_all.add(OpenHillShade_September_6am);
            OpenHillShade_September_6am_Listener hslSEPT6am = new OpenHillShade_September_6am_Listener(panel, store);
            OpenHillShade_September_6am.addActionListener(hslSEPT6am);
                        
            //September  noon
            
            MenuItem OpenHillShade_September_noon = new MenuItem("September noon hillshade");
            openHillshade_all.add(OpenHillShade_September_noon);
            OpenHillShade_September_noon_Listener hslSEPTnoon = new OpenHillShade_September_noon_Listener(panel, store);
            OpenHillShade_September_noon.addActionListener(hslSEPTnoon);
                        
            //September  6pm
            
            MenuItem OpenHillShade_September_6pm = new MenuItem("September 6pm hillshade");
            openHillshade_all.add(OpenHillShade_September_6pm);
            OpenHillShade_September_6pm_Listener hslSEPT6pm = new OpenHillShade_September_6pm_Listener(panel, store);
            OpenHillShade_September_6pm.addActionListener(hslSEPT6pm);
    
    //October
        MenuItem OpenHillShade_Oct = new MenuItem("October hillshade");
    openHillshade_all.add(OpenHillShade_Oct);
    OpenHillShadeListener_Oct hsloct = new OpenHillShadeListener_Oct(panel, store);
    OpenHillShade_Oct.addActionListener(hsloct);
    
            //October  midnight

            MenuItem OpenHillShade_October_midnight = new MenuItem("October midnight hillshade");
            openHillshade_all.add(OpenHillShade_October_midnight);
            OpenHillShade_October_midnight_Listener hslOCTmidnight = new OpenHillShade_October_midnight_Listener(panel, store);
            OpenHillShade_October_midnight.addActionListener(hslOCTmidnight);
            
            //October  6am
            
            MenuItem OpenHillShade_October_6am = new MenuItem("October 6am hillshade");
            openHillshade_all.add(OpenHillShade_October_6am);
            OpenHillShade_October_6am_Listener hslOCT6am = new OpenHillShade_October_6am_Listener(panel, store);
            OpenHillShade_October_6am.addActionListener(hslOCT6am);
                        
            //October  noon
            
            MenuItem OpenHillShade_October_noon = new MenuItem("October noon hillshade");
            openHillshade_all.add(OpenHillShade_October_noon);
            OpenHillShade_October_noon_Listener hslOCTnoon = new OpenHillShade_October_noon_Listener(panel, store);
            OpenHillShade_October_noon.addActionListener(hslOCTnoon);
                        
            //October  6pm
            
            MenuItem OpenHillShade_October_6pm = new MenuItem("October 6pm hillshade");
            openHillshade_all.add(OpenHillShade_October_6pm);
            OpenHillShade_October_6pm_Listener hslOCT6pm = new OpenHillShade_October_6pm_Listener(panel, store);
            OpenHillShade_October_6pm.addActionListener(hslOCT6pm);
    
    //November
        MenuItem OpenHillShade_Nov = new MenuItem("November hillshade");
    openHillshade_all.add(OpenHillShade_Nov);
    OpenHillShadeListener_Nov hslnov = new OpenHillShadeListener_Nov(panel, store);
    OpenHillShade_Nov.addActionListener(hslnov);
    
            //November  midnight

            MenuItem OpenHillShade_November_midnight = new MenuItem("November midnight hillshade");
            openHillshade_all.add(OpenHillShade_November_midnight);
            OpenHillShade_November_midnight_Listener hslNOVmidnight = new OpenHillShade_November_midnight_Listener(panel, store);
            OpenHillShade_November_midnight.addActionListener(hslNOVmidnight);
            
            //November  6am
            
            MenuItem OpenHillShade_November_6am = new MenuItem("November 6am hillshade");
            openHillshade_all.add(OpenHillShade_November_6am);
            OpenHillShade_November_6am_Listener hslNOV6am = new OpenHillShade_November_6am_Listener(panel, store);
            OpenHillShade_November_6am.addActionListener(hslNOV6am);
                        
            //November  noon
            
            MenuItem OpenHillShade_November_noon = new MenuItem("November noon hillshade");
            openHillshade_all.add(OpenHillShade_November_noon);
            OpenHillShade_November_noon_Listener hslNOVnoon = new OpenHillShade_November_noon_Listener(panel, store);
            OpenHillShade_November_noon.addActionListener(hslNOVnoon);
                        
            //November  6pm
            
            MenuItem OpenHillShade_November_6pm = new MenuItem("November 6pm hillshade");
            openHillshade_all.add(OpenHillShade_November_6pm);
            OpenHillShade_November_6pm_Listener hslNOV6pm = new OpenHillShade_November_6pm_Listener(panel, store);
            OpenHillShade_November_6pm.addActionListener(hslNOV6pm);
    
    //December
        MenuItem OpenHillShade_Dec = new MenuItem("December hillshade");
    openHillshade_all.add(OpenHillShade_Dec);
    OpenHillShadeListener_Dec hsldec = new OpenHillShadeListener_Dec(panel, store);
    OpenHillShade_Dec.addActionListener(hsldec);
    
            //December  midnight

            MenuItem OpenHillShade_December_midnight = new MenuItem("December midnight hillshade");
            openHillshade_all.add(OpenHillShade_December_midnight);
            OpenHillShade_December_midnight_Listener hslDECmidnight = new OpenHillShade_December_midnight_Listener(panel, store);
            OpenHillShade_December_midnight.addActionListener(hslDECmidnight);
            
            //December  6am
            
            MenuItem OpenHillShade_December_6am = new MenuItem("December 6am hillshade");
            openHillshade_all.add(OpenHillShade_December_6am);
            OpenHillShade_December_6am_Listener hslDEC6am = new OpenHillShade_December_6am_Listener(panel, store);
            OpenHillShade_December_6am.addActionListener(hslDEC6am);
                        
            //December  noon
            
            MenuItem OpenHillShade_December_noon = new MenuItem("December noon hillshade");
            openHillshade_all.add(OpenHillShade_December_noon);
            OpenHillShade_December_noon_Listener hslDECnoon = new OpenHillShade_December_noon_Listener(panel, store);
            OpenHillShade_December_noon.addActionListener(hslDECnoon);
                        
            //December  6pm
            
            MenuItem OpenHillShade_December_6pm = new MenuItem("December 6pm hillshade");
            openHillshade_all.add(OpenHillShade_December_6pm);
            OpenHillShade_December_6pm_Listener hslDEC6pm = new OpenHillShade_December_6pm_Listener(panel, store);
            OpenHillShade_December_6pm.addActionListener(hslDEC6pm);
    
    
    MenuItem TestButton = new MenuItem("Test button");
    File.add(TestButton);
    TestButtonListener testbl = new TestButtonListener(panel, store);
    TestButton.addActionListener(testbl);

    Menu openMetData = new Menu("Open Meteorological Data");

    MenuItem OpenTemperatureFile = new MenuItem("Open Monthly Temperature File");
    openMetData.add(OpenTemperatureFile);
    OpenTemperatureFileListener otfl = new OpenTemperatureFileListener(panel, store);
    OpenTemperatureFile.addActionListener(otfl);
    
    MenuItem OpenComplexTemperatureFile = new MenuItem("Open Complex Temperature File (multiple colums)");
    openMetData.add(OpenComplexTemperatureFile);
    OpenComplexTemperatureFileListener octfl = new OpenComplexTemperatureFileListener(panel, store);
    OpenComplexTemperatureFile.addActionListener(octfl);
 
    MenuItem OpenContempInputData = new MenuItem("Open Contemporary model input data (multiple colums)");
    openMetData.add(OpenContempInputData);
    OpenContempInputDataListener openContempfl = new OpenContempInputDataListener(panel, store);
    OpenContempInputData.addActionListener(openContempfl);
    
    openMetData.addSeparator();
    
    MenuItem OpenHistoricInputData_Main = new MenuItem("Open Historical model input data (temp, precip season and precip total (m))");
    openMetData.add(OpenHistoricInputData_Main);
    OpenHistoricInputDataListener_Main openHistoricID_main = new OpenHistoricInputDataListener_Main(panel, store);
    OpenHistoricInputData_Main.addActionListener(openHistoricID_main);

    MenuItem OpenHistoricInputData_WinterPrecip = new MenuItem("Open Historical model input data (winter precipitation)");
    openMetData.add(OpenHistoricInputData_WinterPrecip);
    OpenHistoricInputDataListener_WinterPrecip openHistoricID_WinterPrecip = new OpenHistoricInputDataListener_WinterPrecip(panel, store);
    OpenHistoricInputData_WinterPrecip.addActionListener(openHistoricID_WinterPrecip);
        
    MenuItem OpenHistoricInputData_Radiation = new MenuItem("Open Historical model input data (radiation)");
    openMetData.add(OpenHistoricInputData_Radiation);
    OpenHistoricInputDataListener_Radiation openHistoricID_Radiation = new OpenHistoricInputDataListener_Radiation(panel, store);
    OpenHistoricInputData_Radiation.addActionListener(openHistoricID_Radiation);
    
    File.add(openMetData);

    File.addSeparator();

    Menu save = new Menu("Save");
    File.add(save);

    MenuItem saveNewElevation = new MenuItem("Save new elevation surface");
    save.add(saveNewElevation);
    SaveNewElevationListener snel = new SaveNewElevationListener(store);
    saveNewElevation.addActionListener(snel);

    MenuItem saveNewThickness = new MenuItem("Save new thickness surface");
    save.add(saveNewThickness);
    SaveNewThicknessListener sntl = new SaveNewThicknessListener(store);
    saveNewThickness.addActionListener(sntl);

    /**
     * Want to put in a subMenu extending from Save button
     **/
    //JMenu subMenu = new JMenu("test");
    //Save.add(submenu);
    Menu Methods = new Menu("Methods");
    Menu.add(Methods);

    MenuItem LapseRateMethod = new MenuItem("Lapse Rate");
    Methods.add(LapseRateMethod);
    LapseRateMethodListener lrl = new LapseRateMethodListener(panel, store);
    LapseRateMethod.addActionListener(lrl);
    // Create listener (contains method)

    //MenuItem MeltFunctionMethod = new MenuItem("Calculate Melt Function");
    //Methods.add(MeltFunctionMethod);
    //MeltFunctionListener mfl = new MeltFunctionListener(panel);
    //MeltFunctionMethod.addActionListener(mfl);
    // Create listener (contains method)

    MenuItem MeltSurfaceMethod = new MenuItem("Melt Ice Surface");
    Methods.add(MeltSurfaceMethod);
    MeltSurfaceListener msl = new MeltSurfaceListener(panel, store);
    MeltSurfaceMethod.addActionListener(msl);
    
    MenuItem Slope = new MenuItem("Surface slope calculator");
    Methods.add(Slope);
    SlopeListener sl = new SlopeListener(panel, store);
    Slope.addActionListener(sl);
    
    MenuItem Aspect = new MenuItem("Surface aspect calculator");
    Methods.add(Aspect);
    AspectListener al = new AspectListener(panel, store);
    Aspect.addActionListener(al);
    
    MenuItem TOA_Calc = new MenuItem("TOA calculator");
    Methods.add(TOA_Calc);
    TOAcalcListener toal = new TOAcalcListener(panel, store);
    TOA_Calc.addActionListener(toal);

    MenuItem TOA_Calc_experimental = new MenuItem("TOA calculator (experimental)");
    Methods.add(TOA_Calc_experimental);
    TOAcalcListenerExperimental toalEX = new TOAcalcListenerExperimental(panel, store);
    TOA_Calc_experimental.addActionListener(toalEX);
    
    MenuItem Tau_Calc = new MenuItem("Tau calculator");
    Methods.add(Tau_Calc);
    TauCalcListener_CONTEMP TAUal_contemp = new TauCalcListener_CONTEMP(panel, store);
    Tau_Calc.addActionListener(TAUal_contemp);
    
    MenuItem Bulk_Flux_Calc = new MenuItem("Bulk flux calculator");
    Methods.add(Bulk_Flux_Calc);
    Bulk_Flux_Calc_Listener bf_clc_list = new Bulk_Flux_Calc_Listener(panel, store);
    Bulk_Flux_Calc.addActionListener(bf_clc_list);
    
    MenuItem Radiation_on_slope = new MenuItem("Radiation on a slope");
    Methods.add(Radiation_on_slope);
    Radiation_on_slope_Listnr rsl = new Radiation_on_slope_Listnr(panel, store);
    Radiation_on_slope.addActionListener(rsl);
    
    MenuItem Radiation_on_slope_interval = new MenuItem("Radiation on a slope (interval) - DEFECTIVE 8.2.13 AND NEEDS FIXING");
    Methods.add(Radiation_on_slope_interval);
    Radiation_on_slope_interval_Listnr rsl_int = new Radiation_on_slope_interval_Listnr(panel, store);
    Radiation_on_slope_interval.addActionListener(rsl_int);
    
    MenuItem Q_Daily = new MenuItem("Q (daily)");
    Methods.add(Q_Daily);
    Q_Daily_Listener qdl = new Q_Daily_Listener(panel, store);
    Q_Daily.addActionListener(qdl);
    
    MenuItem Contemporary_precipitation = new MenuItem("Contemporary precipitation");
    Methods.add(Contemporary_precipitation);
    Contemporary_precipitation_Listener cpl = new Contemporary_precipitation_Listener(panel, store);
    Contemporary_precipitation.addActionListener(cpl);
    
    Menu MainModelCompilations =new Menu("Main model compilations");
    Menu.add(MainModelCompilations);
    
    MenuItem ContemporaryModel = new MenuItem("Contemporary model (no precipitation)");
    MainModelCompilations.add(ContemporaryModel);
    ContemporaryModelListener cml = new ContemporaryModelListener(panel, store);
    ContemporaryModel.addActionListener(cml);
    
    MenuItem ContemporaryModel_Precip = new MenuItem("Contemporary model (+ precipitation)");
    MainModelCompilations.add(ContemporaryModel_Precip);
    ContemporaryModel_Precip_Listener cmpl = new ContemporaryModel_Precip_Listener(panel, store);
    ContemporaryModel_Precip.addActionListener(cmpl);
    
    MenuItem ContemporaryModel_Precip_winter_snow_limit = new MenuItem("Contemporary model (+ precipitation with winter snow limit)");
    MainModelCompilations.add(ContemporaryModel_Precip_winter_snow_limit);
    ContemporaryModel_Precip_winter_snow_limit cmpwsll = new ContemporaryModel_Precip_winter_snow_limit(panel, store);
    ContemporaryModel_Precip_winter_snow_limit.addActionListener(cmpwsll);
    
    MenuItem ContemporaryModel_Precip_slope_fixed = new MenuItem("Contemporary model (slope fixed)");
    MainModelCompilations.add(ContemporaryModel_Precip_slope_fixed);
    ContemporaryModel_Precip_slope_fixed cmpslope_fixed = new ContemporaryModel_Precip_slope_fixed(panel, store);
    ContemporaryModel_Precip_slope_fixed.addActionListener(cmpslope_fixed);
    
    MenuItem ContemporaryModel_Precip_aspect_fixed = new MenuItem("Contemporary model (aspect fixed)");
    MainModelCompilations.add(ContemporaryModel_Precip_aspect_fixed);
    ContemporaryModel_Precip_aspect_fixed cmpaspect_fixed = new ContemporaryModel_Precip_aspect_fixed(panel, store);
    ContemporaryModel_Precip_aspect_fixed.addActionListener(cmpaspect_fixed);
    
    MenuItem ContemporaryModel_Precip_aspect_and_slope_fixed = new MenuItem("Contemporary model (aspect and slope fixed)");
    MainModelCompilations.add(ContemporaryModel_Precip_aspect_and_slope_fixed);
    ContemporaryModel_Precip_aspect_and_slope_fixed cmpaspect_slope_fixed = new ContemporaryModel_Precip_aspect_and_slope_fixed(panel, store);
    ContemporaryModel_Precip_aspect_and_slope_fixed.addActionListener(cmpaspect_slope_fixed);
    
    MenuItem ContemporaryModel_Precip_elev_fixed = new MenuItem("Contemporary model (elevation fixed) - NEEDS CHECKING");
    MainModelCompilations.add(ContemporaryModel_Precip_elev_fixed);
    ContemporaryModel_Precip_elev_fixed cmpwef = new ContemporaryModel_Precip_elev_fixed(panel, store);
    ContemporaryModel_Precip_winter_snow_limit.addActionListener(cmpwef);
    
    MainModelCompilations.addSeparator();
    
    MenuItem Historical_Model_Precip_winter_snow_limit = new MenuItem("Historical model (+ precipitation with winter snow limit)");
    MainModelCompilations.add(Historical_Model_Precip_winter_snow_limit);
    Historical_Model_Precip_winter_snow_limit hmpwsll = new Historical_Model_Precip_winter_snow_limit(panel, store);
    Historical_Model_Precip_winter_snow_limit.addActionListener(hmpwsll);
    
    MenuItem Historical_Model_Slope_fixed = new MenuItem("Historical model (slope fixed)");
    MainModelCompilations.add(Historical_Model_Slope_fixed);
    Historical_Model_Slope_fixed hmsf = new Historical_Model_Slope_fixed(panel, store);
    Historical_Model_Slope_fixed.addActionListener(hmsf);
    
    MenuItem Historical_Model_Aspect_Fixed = new MenuItem("Historical model (aspect fixed)");
    MainModelCompilations.add(Historical_Model_Aspect_Fixed);
    Historical_Model_Aspect_Fixed hmaf = new Historical_Model_Aspect_Fixed(panel, store);
    Historical_Model_Aspect_Fixed.addActionListener(hmaf);
    
    MenuItem Historical_Model_Slope_and_Aspect_fixed = new MenuItem("Historical model (slope and aspect fixed)");
    MainModelCompilations.add(Historical_Model_Slope_and_Aspect_fixed);
    Historical_Model_Slope_and_Aspect_fixed hmsaf = new Historical_Model_Slope_and_Aspect_fixed(panel, store);
    Historical_Model_Slope_and_Aspect_fixed.addActionListener(hmsaf);
    
    MenuItem Historical_Model_Precip_Elev_Fixed = new MenuItem("Historical model (elevation fixed) - NEEDS CHECKING");
    MainModelCompilations.add(Historical_Model_Precip_Elev_Fixed);
    Historical_Model_Precip_Elev_Fixed hmp_ElevFx = new Historical_Model_Precip_Elev_Fixed(panel, store);
    Historical_Model_Precip_winter_snow_limit.addActionListener(hmp_ElevFx);
    
    MainModelCompilations.addSeparator();
    
    MenuItem Historical_model_dynamic_LARGE_FILES_button = new MenuItem("Historical model (dynamic): [LARGE FILES])");
    MainModelCompilations.add(Historical_model_dynamic_LARGE_FILES_button);
    Historical_model_dynamic_LARGE_FILES hmd_LF = new Historical_model_dynamic_LARGE_FILES(panel, store);
    Historical_model_dynamic_LARGE_FILES_button.addActionListener(hmd_LF); 
    
    MenuItem Historical_model_slope_fixed_LARGE_FILES_button = new MenuItem("Historical model (slope fixed): [LARGE FILES])");
    MainModelCompilations.add(Historical_model_slope_fixed_LARGE_FILES_button);
    Historical_model_slope_fixed_LARGE_FILES hmsf_LF = new Historical_model_slope_fixed_LARGE_FILES(panel, store);
    Historical_model_slope_fixed_LARGE_FILES_button.addActionListener(hmsf_LF);
    
    MenuItem Historical_Model_aspect_fixed_LARGE_FILES_button = new MenuItem("Historical model (aspect fixed): [LARGE FILES])");
    MainModelCompilations.add(Historical_Model_aspect_fixed_LARGE_FILES_button);
    Historical_Model_Aspect_Fixed_LARGE_FILES hmaf_LF = new Historical_Model_Aspect_Fixed_LARGE_FILES(panel, store);
    Historical_Model_aspect_fixed_LARGE_FILES_button.addActionListener(hmaf_LF);
    
    MenuItem Historical_Model_Slope_and_Aspect_fixed_LARGE_FILES_button = new MenuItem("Historical model (slope and aspect fixed): [LARGE FILES]");
    MainModelCompilations.add(Historical_Model_Slope_and_Aspect_fixed_LARGE_FILES_button);
    Historical_Model_Slope_and_Aspect_fixed_LARGE_FILES hmsaf_LF = new Historical_Model_Slope_and_Aspect_fixed_LARGE_FILES(panel, store);
    Historical_Model_Slope_and_Aspect_fixed_LARGE_FILES_button.addActionListener(hmsaf_LF);
    
    MenuItem Historical_model_elev_fixed_LARGE_FILES_button = new MenuItem("Historical model (elevation fixed): [LARGE FILES]) - NEEDS CHECKING");
    MainModelCompilations.add(Historical_model_elev_fixed_LARGE_FILES_button);
    Historical_model_elev_fixed_LARGE_FILES hmd_ElevFX_LF = new Historical_model_elev_fixed_LARGE_FILES(panel, store);
    Historical_model_elev_fixed_LARGE_FILES_button.addActionListener(hmd_ElevFX_LF); 
            
    /////////////////////////////////////////////////
            
    Menu AnalysisTools =new Menu("Analysis tools");
    Menu.add(AnalysisTools);
    
    MenuItem AreaCalculation = new MenuItem("Area Calculator");
    AnalysisTools.add(AreaCalculation);
    AreaCalculationListener acl = new AreaCalculationListener(panel, store);
    AreaCalculation.addActionListener(acl);
    
    MenuItem AreaChangeCalculation = new MenuItem("Area change Calculator");
    AnalysisTools.add(AreaChangeCalculation);
    AreaChangeCalculationListener accl = new AreaChangeCalculationListener(panel, store);
    AreaChangeCalculation.addActionListener(accl);
    
    MenuItem VolumeCalculation = new MenuItem("Volume Calculator");
    AnalysisTools.add(VolumeCalculation);
    VolumeCalculationListener vcl = new VolumeCalculationListener(panel, store);
    VolumeCalculation.addActionListener(vcl);
    
    MenuItem ElevationChangeCalculation = new MenuItem("Elevation change Calculator");
    AnalysisTools.add(ElevationChangeCalculation);
    ElevationChangeCalculationListener eccl = new ElevationChangeCalculationListener(panel, store);
    ElevationChangeCalculation.addActionListener(eccl);
    
    MenuItem ThicknessChangeCalculation = new MenuItem("Thickness change Calculator");
    AnalysisTools.add(ThicknessChangeCalculation);
    ThicknessChangeCalculationListener tccl = new ThicknessChangeCalculationListener(panel, store);
    ThicknessChangeCalculation.addActionListener(tccl);
    
    MenuItem GeneralStats = new MenuItem("General stats");
    AnalysisTools.add(GeneralStats);
    GeneralStatsListener gsl = new GeneralStatsListener(panel, store);
    GeneralStats.addActionListener(gsl);
     
    Menu PrintArray = new Menu("Print arrays");
    Menu.add(PrintArray);

    MenuItem PrintElevationArray = new MenuItem("Print elevation surface array");
    PrintArray.add(PrintElevationArray);
    PrintElevationArrayListener peal = new PrintElevationArrayListener(panel, store);
    PrintElevationArray.addActionListener(peal);

    MenuItem PrintThicknessArray = new MenuItem("Print ice thickness array");
    PrintArray.add(PrintThicknessArray);
    PrintThicknessArrayListener ptal = new PrintThicknessArrayListener(panel, store);
    PrintThicknessArray.addActionListener(ptal);

    MenuItem PrintSurfaceTypeArray = new MenuItem("Print surface type array");
    PrintArray.add(PrintSurfaceTypeArray);
    PrintSurfaceTypeArrayListener pstal = new PrintSurfaceTypeArrayListener(panel, store);
    PrintSurfaceTypeArray.addActionListener(pstal);


  }

  public void windowActivated(WindowEvent e)
  {
  }

  public void windowClosed(WindowEvent e)
  {
  }

  public void windowClosing(WindowEvent e)
  {

    System.exit(0);

  }

  @Override
  public void windowDeactivated(WindowEvent e)
  {
  }

  @Override
  public void windowDeiconified(WindowEvent e)
  {
  }

  @Override
  public void windowIconified(WindowEvent e)
  {
  }

  @Override
  public void windowOpened(WindowEvent e)
  {
  }

  @Override
  public void paint(Graphics g)
  {
    g.drawString("Hello World", 100, 100);
  }

  public static void main(String args[])
  {

    new ModelGUI();

  }
}
