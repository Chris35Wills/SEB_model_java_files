package energymodels;

import java.util.ArrayList;
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
 * Storage.class holds numerous Accessor/Mutator (get/set) methods that can be
 * used to store copies of user imported/created files. Thus store acts as a
 * depository box from which the user can add and take things
 *
 * @author Chris Williams 9/11/11
 **/

public class Storage
{

  double elevation[][] = null;
  double elevation_initial[][] = null; // So a clean initially uploaded surface is stored
  double elevation_size[][] = null; // Used for sizing all arrays
  double ElevationFixedSurface[][] = null; // For fixed elev. experiments
  double hillshade[][] = null;
    
  double thickness[][] = null;
  double thickness_intermediary[][] = null; // Used within the melt component of the model for adjusting other arrays
  double thickness_initial[][] = null; // So a clean initially uploaded surface is stored

  
  double surfaceType[][] = null;
  double lapseRate[][] = null;
  double slope[][] = null;
  double aspect[][] = null;
  double surfaceRadiationSurface[][] = null;
  double hillshadeConversionSurface[][] = null;
  double ice_thickness_change[][] = null;
  
  double hillshade_jan[][] = null;
  double hillshade_jan_6am[][] = null;
  double hillshade_jan_noon[][] = null;
  double hillshade_jan_6pm[][] = null;
  double hillshade_jan_midnight[][] = null;
  
  double hillshade_feb[][] = null;
  double hillshade_feb_6am[][] = null;
  double hillshade_feb_noon[][] = null;
  double hillshade_feb_6pm[][] = null;
  double hillshade_feb_midnight[][] = null;
  
  double hillshade_march[][] = null;
  double hillshade_march_6am[][] = null;
  double hillshade_march_noon[][] = null;
  double hillshade_march_6pm[][] = null;
  double hillshade_march_midnight[][] = null;
  
  double hillshade_april[][] = null;
  double hillshade_april_6am[][] = null;
  double hillshade_april_noon[][] = null;
  double hillshade_april_6pm[][] = null;
  double hillshade_april_midnight[][] = null;
  
  double hillshade_may[][] = null;
  double hillshade_may_6am[][] = null;
  double hillshade_may_noon[][] = null;
  double hillshade_may_6pm[][] = null;
  double hillshade_may_midnight[][] = null;
  
  double hillshade_june[][] = null;
  double hillshade_june_6am[][] = null;
  double hillshade_june_noon[][] = null;
  double hillshade_june_6pm[][] = null;
  double hillshade_june_midnight[][] = null;
  
  double hillshade_july[][] = null;
  double hillshade_july_6am[][] = null;
  double hillshade_july_noon[][] = null;
  double hillshade_july_6pm[][] = null;
  double hillshade_july_midnight[][] = null;
  
  double hillshade_aug[][] = null;
  double hillshade_aug_6am[][] = null;
  double hillshade_aug_noon[][] = null;
  double hillshade_aug_6pm[][] = null;
  double hillshade_aug_midnight[][] = null;
  
  double hillshade_sept[][] = null;
  double hillshade_sept_6am[][] = null;
  double hillshade_sept_noon[][] = null;
  double hillshade_sept_6pm[][] = null;
  double hillshade_sept_midnight[][] = null;
  
  double hillshade_oct[][] = null;
  double hillshade_oct_6am[][] = null;
  double hillshade_oct_noon[][] = null;
  double hillshade_oct_6pm[][] = null;
  double hillshade_oct_midnight[][] = null;
  
  double hillshade_nov[][] = null;
  double hillshade_nov_6am[][] = null;
  double hillshade_nov_noon[][] = null;
  double hillshade_nov_6pm[][] = null;
  double hillshade_nov_midnight[][] = null;
  
  double hillshade_dec[][] = null;
  double hillshade_dec_6am[][] = null;
  double hillshade_dec_noon[][] = null;
  double hillshade_dec_6pm[][] = null;
  double hillshade_dec_midnight[][] = null;
  
  double surfaceQ_dailySurface[][] = null;
  double surfaceQ_monthlySurface[][] = null;
  
  double psi_Surface[][] = null;
  double LapsedTemp_Surface2[][] = null;
  double volume_difference[][] = null;
  double SummerSnowSurface[][] = null;
  double WinterSnowSurface[][] = null;
  double WinterSnowSurface_0708[][];
  double WinterSnowSurface_0809[][];
  double WinterSnowSurface_0910[][];
  double WinterSnowSurface_1011[][];
  double winter_and_summer_snow_thickness[][];
  double WinterSnowThickness[][];
  double ContempSnowSurface_0708[][];
  double ContempSnowSurface_0809[][];
  double ContempSnowSurface_0910[][];
  double ContempSnowSurface_1011[][];
          
  ArrayList<Double> Day_list = new ArrayList<Double>();
  ArrayList<Double> yearList = new ArrayList<Double>();
  ArrayList<Double> monthList = new ArrayList<Double>();
  ArrayList<Double> tempList = new ArrayList<Double>();
  ArrayList<Double> precipList = new ArrayList<Double>();
  ArrayList<Double> precipSeasonList = new ArrayList<Double>();
  ArrayList<Double> solarDeclinationList = new ArrayList<Double>();
  ArrayList<Double> solarHourList = new ArrayList<Double>();
  ArrayList<Double> zenithList = new ArrayList<Double>();
  ArrayList<Double> azimuthList = new ArrayList<Double>();
  ArrayList<Double> tauList = new ArrayList<Double>();
  ArrayList<Double> toaList = new ArrayList<Double>();
  ArrayList<Double> surfaceRadiationList = new ArrayList<Double>();
  int originalNcols, originalNrows, originalCellsize;
  double originalXllcorner, originalYllcorner;
  double temp, precip, solarDeclination, solarHour, 
          zenith, azimuth, tau, TOA, surfaceRadiation, psi; 
 Double day, year, month, precipSeason; 
  double Tau_estimate, TOA_midnight, Zenith_midnight, Azimuth_midnight, TOA_6am, Zenith_6am, 
                Azimuth_6am, TOA_noon, Zenith_noon, Azimuth_noon, TOA_6pm, Zenith_6pm, Azimuth_6pm;
    
  double Radiation_Midnight, Radiation_Noon, Radiation_6am, Radiation_6pm;
      
    ArrayList<Double> TOA_midnight_list = new ArrayList<Double>();
    ArrayList<Double> TOA_6am_list = new ArrayList<Double>();
    ArrayList<Double> TOA_noon_list = new ArrayList<Double>();
    ArrayList<Double> TOA_6pm_list = new ArrayList<Double>();

    ArrayList<Double> Zenith_midnight_list = new ArrayList<Double>();
    ArrayList<Double> Zenith_6am_list = new ArrayList<Double>();
    ArrayList<Double> Zenith_noon_list = new ArrayList<Double>();
    ArrayList<Double> Zenith_6pm_list = new ArrayList<Double>();

    ArrayList<Double> Azimuth_midnight_list = new ArrayList<Double>();
    ArrayList<Double> Azimuth_6am_list = new ArrayList<Double>();
    ArrayList<Double> Azimuth_noon_list = new ArrayList<Double>();
    ArrayList<Double> Azimuth_6pm_list = new ArrayList<Double>();

    ArrayList<Double> TauEstimateList = new ArrayList<Double>();
    
    ////////////////////////// Historical variables
    ArrayList<Integer> dayList_Radiation = new ArrayList<Integer>();
    ArrayList<Integer> yearList_MAIN = new ArrayList<Integer>();
    ArrayList<Integer> yearList_WinterPrecip = new ArrayList<Integer>();
    ArrayList<Integer> yearList_Radiation = new ArrayList<Integer>();
    ArrayList<Integer> monthList_MAIN = new ArrayList<Integer>();
    ArrayList<Integer> monthList_Radiation = new ArrayList<Integer>();
    ArrayList<Double> PrecipSeasonList_MAIN = new ArrayList<Double>();
    ArrayList<Double> WinterPrecipList = new ArrayList<Double>();
    ArrayList<Double> WinterMonthCountList = new ArrayList<Double>();
        
    int day_MAIN_instance, month_MAIN_instance, year_MAIN_instance, year_WinterPrecip_instance;
    int day_Radiation_instance, month_Radiation_instance, year_Radiation_instance;
    double precipTotal_summer, PrecipSeason_MAIN, winterPrecip, winterMonthCount;
    
  //Set method creates a copy of a user imported elevation surface which
  //is now held within Storage.class
  //Get method allows access to the copy of the elevation surface from
  //Storage.class
  public void setElevation(double[][] e)
  {
    elevation = e;
  }

  public double[][] getElevation()
  {
    return elevation;
  }
  
  public void setElevation_INITIAL(double[][] eI)
  {
    //elevation_initial = eI;
    elevation_initial = new double[eI.length][eI[0].length];
    for (int i = 0; i < eI.length; i++) {
        for (int j = 0; j < eI[i].length; j++) {
            elevation_initial[i][j] = eI[i][j];
        }
    }
    
  }

  public double[][] getElevation_INITIAL()
  {
    return elevation_initial;
  }
  
  
  public void setElevationSize(double[][] e_size)
  {
    //elevation_initial = eI;
    elevation_size = new double[e_size.length][e_size[0].length];
    for (int i = 0; i < e_size.length; i++) {
        for (int j = 0; j < e_size[i].length; j++) {
            elevation_size[i][j] = e_size[i][j];
        }
    }
    
  }

  public double[][] getElevationSize()
  {
    return elevation_size;
  }
  
  //Set method creates a copy of a user imported hillshade surface which
  //is now held within Storage.class
  //Get method allows access to the copy of the hillshade surface from
  //Storage.class
  public void setHillshade(double[][] e)
  {
    hillshade = e;
  }

  public double[][] getHillshade()
  {
    return hillshade;
  }
  
  //Set method creates a copy of a user imported ice thickness surface
  //which is now held within Storage.class
  //Get method allows access to the copy of the ice thickness surface
  //from Storage.class
  public void setThickness(double[][] t)
  {
    thickness = t;
  }

  public double[][] getThickness()
  {
    return thickness;
  }
  
  public void setThickness_INITIAL(double[][] ti)
  {
    thickness_initial = ti;
  }

  public double[][] getThickness_INITIAL()
  {
    return thickness_initial;
  }
  
  
  /**
   * This bit of code is used within the melt model function - it is used
   * for getting/setting the thickness layer of the glacier at the beginning of 
   * a melt loop
   */
         
  public void setThickness_intermediary(double[][] t)
  {
    
    thickness_intermediary = new double[t.length][t[0].length];
    for (int i = 0; i < t.length; i++) {
        for (int j = 0; j < t[i].length; j++) {
            thickness_intermediary[i][j] = t[i][j];
        }
    }
    
  }

  public double[][] getThickness_intermediary()
  {
    return thickness_intermediary;
  }
  
    public void setVolumeDifference(double[][] vd)
  {
    volume_difference = vd;
  }

  public double[][] getVolumeDifference()
  {
    return volume_difference;
  }

  //Set method creates a copy of a user imported surface type surface
  //which is now held within Storage.class
  //Get method allows access to the copy of the surface type surface
  //from Storage.class
  public void setSurfaceType(double[][] st)
  {
    surfaceType = st;
  }

  public double[][] getSurfaceType()
  {
    return surfaceType;
  }
  
  //Set method creates a copy of the internally developed surface slope
  //which is now held within Storage.class
  //Get method allows access to the copy of the surface slope from
  //Storage.class
  public void setSlope(double[][] slp)
  {
    slope = slp;
  }

  public double[][] getSlope()
  {
    return slope;
  }
  
  //Set method creates a copy of the internally developed surface aspect
  //which is now held within Storage.class
  //Get method allows access to the copy of the surface aspect from
  //Storage.class
  
  public void setAspect(double[][] asp)
  {
    aspect = asp;
  }

  public double[][] getAspect()
  {
    return aspect;
  }

  //Set method creates a copy of a user created LapseRate method created
  //surface which is now held within Storage.class
  //Get method allows access to the copy of the LapseRate method created
  //surface from Storage.class
  public void setLapseRate(double[][] lr)
  {
    lapseRate = lr;
  }

  public double[][] getLapseRate()
  {
    return lapseRate;
  }

  // All of the methods below could be created in the individual file
  // opener listeners, thus storing details relative to specific files
  //(This currently only allows 1 value for each to apply to all files...)
  /**
   * Sets the number of columns
   **/
  public void setOriginalNcols(int ncols)
  {
    originalNcols = ncols;
  }

  /**
   * Gets the original number of columns
   */
  public int getOriginalNcols()
  {
    return originalNcols;
  }

  /**
   * Sets the number of rows
   **/
  public void setOriginalNrows(int org_rows)
  {
    originalNrows = org_rows;
  }

  /**
   * Gets the original number of rows
   */
  public int getOriginalNrows()
  {
    return originalNrows;
  }

  /**
   * Sets the x co-ordinate of the upper left hand corner of the file for positioning
   **/
  public void setOriginalXllcorner(double e)
  {
    originalXllcorner = e;
  }

  /**
   * Gets the x co-ordinate of the upper left hand corner of the file for positioning
   */
  public double getOriginalXllcorner()
  {
    return originalXllcorner;
  }

  /**
   * Sets the y co-ordinate of the upper left hand corner of the file for positioning
   **/
  public void setOriginalYllcorner(double e)
  {
    originalYllcorner = e;
  }

  /**
   * Gets the y co-ordinate of the upper left hand corner of the file for positioning
   */
  public double getOriginalYllcorner()
  {
    return originalYllcorner;
  }

  /**
   * Sets the cell size of the GRIDJava file
   **/
  public void setOriginalCellsize(int e)
  {
    originalCellsize = e;
  }

  /**
   * Gets the cell size of the GRIDJava file
   */
  public double getOriginalCellsize()
  {
    return originalCellsize;
  }
  
  //********************** Bulk flux methods **********************************
  
  public void setPsi(double p)
  {
      psi = p;
  }
  
  public double getPsi()
  {
      return psi;
  }
  
  
  public void setPsiSurface(double[][] psi_s)
  {
    psi_Surface = psi_s;
  }

  public double[][] getPsiSurface()
  {
    return psi_Surface;
  }
  
 
  public void  setLapsedTempSurface(double[][] lts_2)
  {
    LapsedTemp_Surface2 = lts_2;
  }

  public double[][] getLapsedTempSurface()
  {
    return LapsedTemp_Surface2;
  }
//********************** Daily Q methods *************************************
          
    public void setQdailySurface(double[][] qds)
  {
    surfaceQ_dailySurface = qds;
  }

  public double[][] getQdailySurface()
  {
    return surfaceQ_dailySurface;
  }
  
  //********************** Complex file reader methods ************************
  
    /**
   * Set/get methods for day integer
   * 
   **/
  
  public void setDay(Double d)
  {
    day = d;
  }

  public Double getDay()
  {
    return day;
  }
  
  /**
   * Set/get methods for year integer
   * 
   **/
  
  public void setYear(Double y)
  {
    year = y;
  }

  public Double getYear()
  {
    return year;
  }
    
   /**
   * Sets the temperature from the [complex] met file (.csv)
   **/
  public void setTemp(double t)
  {
    temp = t;
  }

  /**
   * Gets the temperature from the [complex] met file (.csv)
   */
  public double getTemp()
  {
    return temp;
  }
  
  /**
   * Sets the TOA from the [complex] met file (.csv)
   **/
  public void setTOA(double ta)
  {
    TOA = ta;
  }

  /**
   * Gets the TOA from the [complex] met file (.csv)
   */
  public double getTOA()
  {
    return TOA;
  }
  
   /**
   * Sets the Month from the [complex] met file (.csv)
   **/
  public void setMonth(Double m)
  {
    month = m; 
  }

  /**
   * Gets the Month from the [complex] met file (.csv)
   */
  public Double getMonth()
  {
    return month;
  }
  
   /**
   * Sets the Precipitation from the [complex] met file (.csv)
   **/
  public void setPrecip(double p)
  {
    precip = p;
  }

  /**
   * Gets the Precipitation from the [complex] met file (.csv)
   */
  public double getPrecip()
  {
    return precip;
  }
  
  /**
   * Gets the Precipitation Season (1 or a 2)from the [complex] met file (.csv)
   */
    
  public void setPrecipSeason(Double pS)
  {
    precipSeason = pS;
  }

  /**
   * Gets the Precipitation Season (1 or a 2) from the [complex] met file (.csv)
   */
  
  public Double getPrecipSeason()
  {
    return precipSeason;
  }
  
  /**
   * Sets the Solar Declination from the [complex] met file (.csv)
   **/
  public void setSolarDeclination(double sd)
  {
    solarDeclination = sd;
  }

  /**
   * Gets the Solar Declination from the [complex] met file (.csv)
   */
  public double getSolarDeclination()
  {
    return solarDeclination;
  }
  
    /**
   * Sets the Solar Hour from the [complex] met file (.csv)
   **/
  public void setSolarHour(double sh)
  {
    solarHour = sh;
  }

  /**
   * Gets the Solar Hour from the [complex] met file (.csv)
   */
  public double getSolarHour()
  {
    return solarHour;
  }       
  
      /**
   * Sets Tau from the [complex] met file (.csv)
   **/
  public void setTau(double t)
  {
    tau = t;
  }

  /**
   * Gets Tau from the [complex] met file (.csv)
   */
  public double getTau()
  {
    return tau;
  }  
          
  /**
   * Sets the Zenith from the [complex] met file (.csv)
   **/
  public void setZenith(double z)
  {
    zenith = z;
  }

  /**
   * Gets the Zenith from the [complex] met file (.csv)
   */
  public double getZenith()
  {
    return zenith;
  }
  
  /**
   * Sets the Zenith from the [complex] met file (.csv)
   **/
  public void setAzimuth(double az)
  {
    azimuth = az;
  }

  /**
   * Gets the Zenith from the [complex] met file (.csv)
   */
  public double getAzimuth()
  {
    return azimuth;
  }
  
    
 // Get/set methods used for the contemporary reader and the historical reader
    
  public void setTauEstimate(double tauEst){
      Tau_estimate = tauEst;
  }
  public double getTauEstimate ()
  {
  return Tau_estimate;
  }
  
  public void setTOA_midnight(double TOAmid){
      TOA_midnight = TOAmid;
  }
  public double getTOA_midnight ()
  {
  return TOA_midnight;
  }
  
  public void setTOA_6am(double TOA6am){
      TOA_6am = TOA6am;
  }
  public double getTOA_6am ()
  {
  return TOA_6am;
  }
  
  public void setTOA_noon(double TOAnoon){
      TOA_noon = TOAnoon;
  }
  public double getTOA_noon ()
  {
  return TOA_noon;
  }
  
  public void setTOA_6pm(double TOA6pm){
      TOA_6pm = TOA6pm;
  }
  public double getTOA_6pm ()
  {
  return TOA_6pm;
  }
    
  public void setZenith_midnight(double Zenithmid){
      Zenith_midnight = Zenithmid;
  }
  public double getZenith_midnight ()
  {
  return Zenith_midnight;
  }
  
  public void setZenith_6am(double Zenith6am){
      Zenith_6am = Zenith6am;
  }
  public double getZenith_6am ()
  {
  return Zenith_6am;
  }
  
  public void setZenith_noon(double Zenithnoon){
      Zenith_noon = Zenithnoon;
  }
  public double getZenith_noon ()
  {
  return Zenith_noon;
  }
  
  public void setZenith_6pm(double Zenith6pm){
      Zenith_6pm = Zenith6pm;
  }
  public double getZenith_6pm ()
  {
  return Zenith_6pm;
  }
  
  public void setAzimuth_midnight(double Azimuthmid){
      Azimuth_midnight = Azimuthmid;
  }
  public double getAzimuth_midnight ()
  {
  return Azimuth_midnight;
  }
  
  public void setAzimuth_6am(double Azimuth6am){
      Azimuth_6am = Azimuth6am;
  }
  public double getAzimuth_6am ()
  {
  return Azimuth_6am;
  }
  
  public void setAzimuth_noon(double Azimuthnoon){
      Azimuth_noon = Azimuthnoon;
  }
  public double getAzimuth_noon ()
  {
  return Azimuth_noon;
  }
  
  public void setAzimuth_6pm(double Azimuth6pm){
      Azimuth_6pm = Azimuth6pm;
  }
  public double getAzimuth_6pm ()
  {
  return Azimuth_6pm;
  }

  // Getters&Setters of ArrayLists from the complex file reader
  
  /**
  *Sets and gets the month list from the [complex] met file (.csv)
  **/
  
  public void setDayList (ArrayList<Double> dayList)  {
    Day_list = dayList;
}
public List<Double> getDayList()  
  {
    return Day_list;
  }


  public void setYearFile(ArrayList<Double> yf)
  {
    yearList = yf;
  }

  public List<Double> getYearFile()
  {
    return yearList;
  }
  
  /**
  *Sets and gets the month list from the [complex] met file (.csv)
  **/
  public void setMonthFile(ArrayList<Double> mf)
  {
    monthList = mf;
  }

  public List<Double> getMonthFile()
  {
    return monthList;
  }
  
  /**
  *Sets and gets the tau list from the [complex] met file (.csv)
  **/
  public void setTauFile(ArrayList<Double> tf)
  {
    tauList = tf;
  }

  public List<Double> getTauFile()
  {
    return tauList;
  }
  
    /**
  *Sets and gets the TOA list from the [complex] met file (.csv)
  **/
  public void setTOAFile(ArrayList<Double> toal)
  {
    toaList = toal;
  }

  public List<Double> getTOAFile()
  {
    return toaList;
  }
  
  /**
  *Sets and gets the temperature list from the [complex] met file (.csv)
  **/
  public void setTemperatureFile(ArrayList<Double> tf)
  {
    tempList = tf;
  }

  public List<Double> getTemperatureFile()
  {
    return tempList;
  }
  
   /**
  *Sets and gets the precipitation list from the [complex] met file (.csv)
  **/
  public void setPrecipFile(ArrayList<Double> pf)
  {
    precipList = pf;
  }

  public List<Double> getPrecipFile()
  {
    return precipList;
  }
  
  public void setPrecipSeasonList(ArrayList<Double> psl)
  {
    precipSeasonList = psl;
  }

  public List<Double> getPrecipSeasonList()
  {
    return precipSeasonList;
  }
  
 /**
  *Sets and gets the solar declination list from the [complex] met file (.csv)
  **/
  public void setSolarDeclinationFile(ArrayList<Double> sdf)
  {
    solarDeclinationList = sdf;
  }

  public List<Double> getSolarDeclinationFile()
  {
    return solarDeclinationList;
  }
  
 /**
  *Sets and gets the solar hour list from the [complex] met file (.csv)
  **/
  public void setSolarHourFile(ArrayList<Double> shf)
  {
    solarHourList = shf;
  }

  public List<Double> getSolarHourFile()
  {
    return solarHourList;
  }

 /**
  *Sets and gets the zenith list from the [complex] met file (.csv)
  **/
  public void setZenithFile(ArrayList<Double> zf)
  {
    zenithList = zf;
  }

  public List<Double> getZenithFile()
  {
    return zenithList;
  }

   /**
  *Sets and gets the azimuth list from the [complex] met file (.csv)
  **/
  public void setAzimuthFile(ArrayList<Double> af)
  {
    azimuthList = af;
  }

  public List<Double> getAzimuthFile()
  {
    return azimuthList;
  }
  
  /**
   * Radiation variable methods within input file readers
   */
  
  // Interval radiation getter and setter methods      
        
public void setTauEstimateList (ArrayList<Double> tauEst)  {
    TauEstimateList = tauEst; 
}
public List<Double> getTauEstimateList()
  {
    return TauEstimateList;
  }
  
public void setTOA_midnight_list (ArrayList<Double> TOAmidnight)  {
    TOA_midnight_list = TOAmidnight; 
}
public List<Double> getTOA_midnight_list()
  {
    return TOA_midnight_list;
  }

public void setTOA_6am_list (ArrayList<Double> TOA6am)  {
    TOA_6am_list = TOA6am;
}
public List<Double> getTOA_6am_list()  
  {
    return TOA_6am_list;
  }

public void setTOA_noon_list (ArrayList<Double> TOAnoon)  {
    TOA_noon_list = TOAnoon;
}
public List<Double> getTOA_noon_list()
  {
    return TOA_noon_list;
  }

public void setTOA_6pm_list (ArrayList<Double> TOA6pm)  {
    TOA_6pm_list = TOA6pm;
}
public List<Double> getTOA_6pm_list()
  {
    return TOA_6pm_list;
  }

// Interval zenith getter and setter methods
                                                  
public void setZenith_midnight_list (ArrayList<Double> ZenMidnight)  {
    Zenith_midnight_list = ZenMidnight; 
}
public List<Double> getZenith_midnight_list()
  {
    return Zenith_midnight_list;
  }

public void setZenith_6am_list (ArrayList<Double> Zen6am)  {
    Zenith_6am_list = Zen6am;
}
public List<Double> getZenith_6am_list()
  {
    return Zenith_6am_list;
  }

public void setZenith_noon_list (ArrayList<Double> ZenNoon)  {
    Zenith_noon_list = ZenNoon;
}
public List<Double> getZenith_noon_list()
  {
    return Zenith_noon_list;
  }

public void setZenith_6pm_list (ArrayList<Double> Zen6pm)  {
    Zenith_6pm_list  = Zen6pm;
}
public List<Double> getZenith_6pm_list()
  {
    return Zenith_6pm_list;
  }

// Interval azimuth getter and setter methods
                                                 
public void setAzimuth_midnight_list (ArrayList<Double> AzimMidnight)  {
    Azimuth_midnight_list = AzimMidnight;
}
public List<Double> getAzimuth_midnight_list()
  {
    return Azimuth_midnight_list;
  }

public void setAzimuth_6am_list (ArrayList<Double> Azim6am)  {
    Azimuth_6am_list = Azim6am;
}
public List<Double> getAzimuth_6am_list()
  {
    return Azimuth_6am_list;
  }

public void setAzimuth_noon_list (ArrayList<Double> AzimNoon)  {
    Azimuth_noon_list = AzimNoon;
}
public List<Double> getAzimuth_noon_list()
  {
    return Azimuth_noon_list;
  }

public void setAzimuth_6pm_list (ArrayList<Double> Azim6pm)  {
    Azimuth_6pm_list = Azim6pm;
}
public List<Double> getAzimuth_6pm_list()
  {
    return Azimuth_6pm_list;
  }

  //************** Radiation Values ******************
  
  public void setSurfaceRadiation (double SR){
      surfaceRadiation = SR;
  }
  
  public double getSurfaceRadiation (){
      return surfaceRadiation;
  }
  
  public void setSurfaceRadiationFile(ArrayList<Double> SR_list)
  {
    surfaceRadiationList = SR_list;
  }

  public List<Double> getSurfaceRadiationFile()
  {
    return surfaceRadiationList;
  }
  
  //Sets and gets a GRID surface of surface radiation valuesa as set in the 
  //"Radiation_on_a_slope" listener 
  
  public void setSurfaceRadiationSurface(double[][] srs)
  {
    surfaceRadiationSurface = srs;
  }

  public double[][] getSurfaceRadiationSurface()
  {
    return surfaceRadiationSurface;
  }
  //surfaceRadiationSurface
  //************** Radiation Values ******************

  //**************Hillshade conversion******************
  
    
  public void setHillshadeConversionSurface(double[][] hcs)
  {
    hillshadeConversionSurface = hcs;
  }

  public double[][] getHillshadeConversionSurface()
  {
    return hillshadeConversionSurface;
  }
    
  //**************Monthly hillshade get/set methods******************
  
  public void setHillshade_Jan(double[][] hs_jan)
  {
    hillshade_jan = hs_jan;
  }

  public double[][] getHillshade_Jan()
  {
    return hillshade_jan;
  }
  
  public void setHillshade_Feb(double[][] hs_feb)
  {
    hillshade_feb = hs_feb;
  }

  public double[][] getHillshade_Feb()
  {
    return hillshade_feb;
  }
  
  public void setHillshade_March(double[][] hs_mar)
  {
    hillshade_march = hs_mar;
  }

  public double[][] getHillshade_March()
  {
    return hillshade_march;
  }
  
    public void setHillshade_April(double[][] hs_april)
  {
    hillshade_april = hs_april;
  }

  public double[][] getHillshade_April()
  {
    return hillshade_april;
  }
  
  public void setHillshade_May(double[][] hs_may)
  {
    hillshade_may = hs_may;
  }

  public double[][] getHillshade_May()
  {
    return hillshade_may;
  }
  
  public void setHillshade_June(double[][] hs_june)
  {
    hillshade_june = hs_june;
  }

  public double[][] getHillshade_June()
  {
    return hillshade_june;
  }
  
  public void setHillshade_July(double[][] hs_july)
  {
    hillshade_july = hs_july;
  }

  public double[][] getHillshade_July()
  {
    return hillshade_july;
  }
  
  public void setHillshade_August(double[][] hs_august)
  {
    hillshade_aug = hs_august;
  }

  public double[][] getHillshade_August()
  {
    return hillshade_aug;
  }
  
  public void setHillshade_Sept(double[][] hs_sept)
  {
    hillshade_sept = hs_sept;
  }

  public double[][] getHillshade_Sept()
  {
    return hillshade_sept;
  }
  
  public void setHillshade_Oct(double[][] hs_oct)
  {
    hillshade_oct = hs_oct;
  }

  public double[][] getHillshade_Oct()
  {
    return hillshade_oct;
  }
  
  public void setHillshade_Nov (double[][] hs_nov)
  {
    hillshade_nov = hs_nov;
  }

  public double[][] getHillshade_Nov()
  {
    return hillshade_nov;
  }
  
  public void setHillshade_Dec (double[][] hs_dec)
  {
    hillshade_dec = hs_dec;
  }

  public double[][] getHillshade_Dec()
  {
    return hillshade_dec;
  }
  
    //**************Monthly hillshade time interval get/set methods******************

  // January 
  
  public void setHillshade_Jan_midnight(double[][] hs_jan_midnight)
  {
    hillshade_jan_midnight = hs_jan_midnight;
  }

  public double[][] getHillshade_Jan_midnight()
  {
    return hillshade_jan_midnight;
  }
  
  public void setHillshade_Jan_6am(double[][] hs_jan_6am)
  {
    hillshade_jan_6am = hs_jan_6am;
  }

  public double[][] getHillshade_Jan_6am()
  {
    return hillshade_jan_6am;
  }
  
  public void setHillshade_Jan_noon(double[][] hs_jan_noon)
  {
    hillshade_jan_noon = hs_jan_noon;
  }

  public double[][] getHillshade_Jan_noon()
  {
    return hillshade_jan_noon;
  }
    
  public void setHillshade_Jan_6pm(double[][] hs_jan_6pm)
  {
    hillshade_jan_6pm = hs_jan_6pm;
  }

  public double[][] getHillshade_Jan_6pm()
  {
    return hillshade_jan_6pm;
  }
      
  // February
  
  public void setHillshade_Feb_midnight(double[][] hs_feb_midnight)
  {
    hillshade_feb_midnight = hs_feb_midnight;
  }

  public double[][] getHillshade_Feb_midnight()
  {
    return hillshade_feb_midnight;
  }
  
  public void setHillshade_Feb_6am(double[][] hs_feb_6am)
  {
    hillshade_feb_6am = hs_feb_6am;
  }

  public double[][] getHillshade_Feb_6am()
  {
    return hillshade_feb_6am;
  }
  
  public void setHillshade_Feb_noon(double[][] hs_feb_noon)
  {
    hillshade_feb_noon = hs_feb_noon;
  }

  public double[][] getHillshade_Feb_noon()
  {
    return hillshade_feb_noon;
  }
    
  public void setHillshade_Feb_6pm(double[][] hs_feb_6pm)
  {
    hillshade_feb_6pm = hs_feb_6pm;
  }

  public double[][] getHillshade_Feb_6pm()
  {
    return hillshade_feb_6pm;
  }
  
  // March
  
  public void setHillshade_March_midnight(double[][] hs_march_midnight)
  {
    hillshade_march_midnight = hs_march_midnight;
  }

  public double[][] getHillshade_March_midnight()
  {
    return hillshade_march_midnight;
  }
  
  public void setHillshade_March_6am(double[][] hs_march_6am)
  {
    hillshade_march_6am = hs_march_6am;
  }

  public double[][] getHillshade_March_6am()
  {
    return hillshade_march_6am;
  }
  
  public void setHillshade_March_noon(double[][] hs_march_noon)
  {
    hillshade_march_noon = hs_march_noon;
  }

  public double[][] getHillshade_March_noon()
  {
    return hillshade_march_noon;
  }
    
  public void setHillshade_March_6pm(double[][] hs_march_6pm)
  {
    hillshade_march_6pm = hs_march_6pm;
  }

  public double[][] getHillshade_March_6pm()
  {
    return hillshade_march_6pm;
  }
  
  // April
  
  public void setHillshade_April_midnight(double[][] hs_april_midnight)
  {
    hillshade_april_midnight = hs_april_midnight;
  }

  public double[][] getHillshade_April_midnight()
  {
    return hillshade_april_midnight;
  }
  
  public void setHillshade_April_6am(double[][] hs_april_6am)
  {
    hillshade_april_6am = hs_april_6am;
  }

  public double[][] getHillshade_April_6am()
  {
    return hillshade_april_6am;
  }
  
  public void setHillshade_April_noon(double[][] hs_april_noon)
  {
    hillshade_april_noon = hs_april_noon;
  }

  public double[][] getHillshade_April_noon()
  {
    return hillshade_april_noon;
  }
    
  public void setHillshade_April_6pm(double[][] hs_april_6pm)
  {
    hillshade_april_6pm = hs_april_6pm;
  }

  public double[][] getHillshade_April_6pm()
  {
    return hillshade_april_6pm;
  }
    
  // May
  
  public void setHillshade_May_midnight(double[][] hs_may_midnight)
  {
    hillshade_may_midnight = hs_may_midnight;
  }

  public double[][] getHillshade_May_midnight()
  {
    return hillshade_may_midnight;
  }
  
  public void setHillshade_May_6am(double[][] hs_may_6am)
  {
    hillshade_may_6am = hs_may_6am;
  }

  public double[][] getHillshade_May_6am()
  {
    return hillshade_may_6am;
  }
  
  public void setHillshade_May_noon(double[][] hs_may_noon)
  {
    hillshade_may_noon = hs_may_noon;
  }

  public double[][] getHillshade_May_noon()
  {
    return hillshade_may_noon;
  }
    
  public void setHillshade_May_6pm(double[][] hs_may_6pm)
  {
    hillshade_may_6pm = hs_may_6pm;
  }

  public double[][] getHillshade_May_6pm()
  {
    return hillshade_may_6pm;
  }
    
  // June
  
  public void setHillshade_June_midnight(double[][] hs_june_midnight)
  {
    hillshade_june_midnight = hs_june_midnight;
  }

  public double[][] getHillshade_June_midnight()
  {
    return hillshade_june_midnight;
  }
  
  public void setHillshade_June_6am(double[][] hs_june_6am)
  {
    hillshade_june_6am = hs_june_6am;
  }

  public double[][] getHillshade_June_6am()
  {
    return hillshade_june_6am;
  }
  
  public void setHillshade_June_noon(double[][] hs_june_noon)
  {
    hillshade_june_noon = hs_june_noon;
  }

  public double[][] getHillshade_June_noon()
  {
    return hillshade_june_noon;
  }
    
  public void setHillshade_June_6pm(double[][] hs_june_6pm)
  {
    hillshade_june_6pm = hs_june_6pm;
  }

  public double[][] getHillshade_June_6pm()
  {
    return hillshade_june_6pm;
  }
      
  // July
  
  public void setHillshade_July_midnight(double[][] hs_july_midnight)
  {
    hillshade_july_midnight = hs_july_midnight;
  }

  public double[][] getHillshade_July_midnight()
  {
    return hillshade_july_midnight;
  }
  
  public void setHillshade_July_6am(double[][] hs_july_6am)
  {
    hillshade_july_6am = hs_july_6am;
  }

  public double[][] getHillshade_July_6am()
  {
    return hillshade_july_6am;
  }
  
  public void setHillshade_July_noon(double[][] hs_july_noon)
  {
    hillshade_july_noon = hs_july_noon;
  }

  public double[][] getHillshade_July_noon()
  {
    return hillshade_july_noon;
  }
    
  public void setHillshade_July_6pm(double[][] hs_july_6pm)
  {
    hillshade_july_6pm = hs_july_6pm;
  }

  public double[][] getHillshade_July_6pm()
  {
    return hillshade_july_6pm;
  }
        
  // August
  
  public void setHillshade_August_midnight(double[][] hs_aug_midnight)
  {
    hillshade_aug_midnight = hs_aug_midnight;
  }

  public double[][] getHillshade_August_midnight()
  {
    return hillshade_aug_midnight;
  }
  
  public void setHillshade_August_6am(double[][] hs_aug_6am)
  {
    hillshade_aug_6am = hs_aug_6am;
  }

  public double[][] getHillshade_August_6am()
  {
    return hillshade_aug_6am;
  }
  
  public void setHillshade_August_noon(double[][] hs_aug_noon)
  {
    hillshade_aug_noon = hs_aug_noon;
  }

  public double[][] getHillshade_August_noon()
  {
    return hillshade_aug_noon;
  }
    
  public void setHillshade_August_6pm(double[][] hs_aug_6pm)
  {
    hillshade_aug_6pm = hs_aug_6pm;
  }

  public double[][] getHillshade_August_6pm()
  {
    return hillshade_aug_6pm;
  }
        
  // September
  
  public void setHillshade_Sept_midnight(double[][] hs_sept_midnight)
  {
    hillshade_sept_midnight = hs_sept_midnight;
  }

  public double[][] getHillshade_Sept_midnight()
  {
    return hillshade_sept_midnight;
  }
  
  public void setHillshade_Sept_6am(double[][] hs_sept_6am)
  {
    hillshade_sept_6am = hs_sept_6am;
  }

  public double[][] getHillshade_Sept_6am()
  {
    return hillshade_sept_6am;
  }
  
  public void setHillshade_Sept_noon(double[][] hs_sept_noon)
  {
    hillshade_sept_noon = hs_sept_noon;
  }

  public double[][] getHillshade_Sept_noon()
  {
    return hillshade_sept_noon;
  }
    
  public void setHillshade_Sept_6pm(double[][] hs_sept_6pm)
  {
    hillshade_sept_6pm = hs_sept_6pm;
  }

  public double[][] getHillshade_Sept_6pm()
  {
    return hillshade_sept_6pm;
  }
        
  // October
  
  public void setHillshade_Oct_midnight(double[][] hs_oct_midnight)
  {
    hillshade_oct_midnight = hs_oct_midnight;
  }

  public double[][] getHillshade_Oct_midnight()
  {
    return hillshade_oct_midnight;
  }
  
  public void setHillshade_Oct_6am(double[][] hs_oct_6am)
  {
    hillshade_oct_6am = hs_oct_6am;
  }

  public double[][] getHillshade_Oct_6am()
  {
    return hillshade_oct_6am;
  }
  
  public void setHillshade_Oct_noon(double[][] hs_oct_noon)
  {
    hillshade_oct_noon = hs_oct_noon;
  }

  public double[][] getHillshade_Oct_noon()
  {
    return hillshade_oct_noon;
  }
    
  public void setHillshade_Oct_6pm(double[][] hs_oct_6pm)
  {
    hillshade_oct_6pm = hs_oct_6pm;
  }

  public double[][] getHillshade_Oct_6pm()
  {
    return hillshade_oct_6pm;
  }
        
  // November
  
  public void setHillshade_Nov_midnight(double[][] hs_nov_midnight)
  {
    hillshade_nov_midnight = hs_nov_midnight;
  }

  public double[][] getHillshade_Nov_midnight()
  {
    return hillshade_nov_midnight;
  }
  
  public void setHillshade_Nov_6am(double[][] hs_nov_6am)
  {
    hillshade_nov_6am = hs_nov_6am;
  }

  public double[][] getHillshade_Nov_6am()
  {
    return hillshade_nov_6am;
  }
  
  public void setHillshade_Nov_noon(double[][] hs_nov_noon)
  {
    hillshade_nov_noon = hs_nov_noon;
  }

  public double[][] getHillshade_Nov_noon()
  {
    return hillshade_nov_noon;
  }
    
  public void setHillshade_Nov_6pm(double[][] hs_nov_6pm)
  {
    hillshade_nov_6pm = hs_nov_6pm;
  }

  public double[][] getHillshade_Nov_6pm()
  {
    return hillshade_nov_6pm;
  }
       
  // December
  
  public void setHillshade_Dec_midnight(double[][] hs_dec_midnight)
  {
    hillshade_dec_midnight = hs_dec_midnight;
  }

  public double[][] getHillshade_Dec_midnight()
  {
    return hillshade_dec_midnight;
  }
  
  public void setHillshade_Dec_6am(double[][] hs_dec_6am)
  {
    hillshade_dec_6am = hs_dec_6am;
  }

  public double[][] getHillshade_Dec_6am()
  {
    return hillshade_dec_6am;
  }
  
  public void setHillshade_Dec_noon(double[][] hs_dec_noon)
  {
    hillshade_dec_noon = hs_dec_noon;
  }

  public double[][] getHillshade_Dec_noon()
  {
    return hillshade_dec_noon;
  }
    
  public void setHillshade_Dec_6pm(double[][] hs_dec_6pm)
  {
    hillshade_dec_6pm = hs_dec_6pm;
  }

  public double[][] getHillshade_Dec_6pm()
  {
    return hillshade_dec_6pm;
  }
  
  //*************** Melt surface tools ********
  
  public void setIce_thickness_change_surface (double[][] itc)
  {
    ice_thickness_change = itc;
  }

  public double[][] getIce_thickness_change_surface()
  {
    return ice_thickness_change;
  }
  
  //*************** Precipitation tools ********
  
    public void setSummerSnowSurface (double[][] sss)
  {
    SummerSnowSurface = sss;
  }

  public double[][] getSummerSnowSurface()
  {
    return SummerSnowSurface;
  }
  
  public void setWinterSnowSurface (double[][] wss)
  {
    WinterSnowSurface = wss;
  }

  public double[][] getWinterSnowSurface()
  {
    return WinterSnowSurface;
  }
            
  public void setWinterSnowSurface_0708 (double[][] wss_0708)
  {
    WinterSnowSurface_0708 = wss_0708;
  }

  public double[][] getWinterSnowSurface_0708()
  {
    return WinterSnowSurface_0708;
  }
          
  public void setWinterSnowSurface_0809 (double[][] wss_0809)
  {
    WinterSnowSurface_0809 = wss_0809;
  }

  public double[][] getWinterSnowSurface_0809()
  {
    return WinterSnowSurface_0809;
  }
          
  public void setWinterSnowSurface_0910 (double[][] wss_0910)
  {
    WinterSnowSurface_0910 = wss_0910;
  }

  public double[][] getWinterSnowSurface_0910()
  {
    return WinterSnowSurface_0910;
  }
    
  public void setWinterSnowSurface_1011 (double[][] wss_1011)
  {
    WinterSnowSurface_1011 = wss_1011;
  }

  public double[][] getWinterSnowSurface_1011()
  {
    return WinterSnowSurface_1011;
  }
  
  public void set_winter_and_summer_snow_thickness (double[][] wsst)
  {
    winter_and_summer_snow_thickness = wsst;
  }

  public double[][] get_winter_and_summer_snow_thickness()
  {
    return winter_and_summer_snow_thickness;
  }
  
  public void setWinterSnowThickness (double[][] wst)
  {
    WinterSnowThickness = wst;
  }

  public double[][] getWinterSnowThickness()
  {
    return WinterSnowThickness;
  }    
  
  /*
   * Open snow surfaces (contemporary)
   */
  
  // 2007 - 2008
  public void set_snow_surface_0708 (double[][] css_0708)
  {
    ContempSnowSurface_0708 = css_0708;
  }

  public double[][] get_snow_surface_0708()
  {
    return ContempSnowSurface_0708;
  }
  
  // 2008 - 2009
  public void set_snow_surface_0809 (double[][] css_0809)
  {
    ContempSnowSurface_0809 = css_0809;
  }

  public double[][] get_snow_surface_0809()
  {
    return ContempSnowSurface_0809;
  }
  
    // 2009 - 2010
  public void set_snow_surface_0910 (double[][] css_0910)
  {
    ContempSnowSurface_0910 = css_0910;
  }

  public double[][] get_snow_surface_0910()
  {
    return ContempSnowSurface_0910;
  }
  
    // 2010 - 2011
  public void set_snow_surface_1011 (double[][] css_1011)
  {
    ContempSnowSurface_1011 = css_1011;
  }

  public double[][] get_snow_surface_1011()
  {
    return ContempSnowSurface_1011;
  }
          
  //********************
  
    public void setRadiation_Midnight (double R_midnight)
  {
    Radiation_Midnight = R_midnight;
  }

  public double getRadiation_Midnight()
  {
    return Radiation_Midnight;
  }
  
    public void setRadiation_Noon (double R_noon)
  {
    Radiation_Noon = R_noon;
  }

  public double getRadiation_Noon()
  {
    return Radiation_Noon;
  }
  
   public void setRadiation_6am (double R_6am)
  {
    Radiation_6am = R_6am;
  }

  public double getRadiation_6am()
  {
    return Radiation_6am;
  }
  
   public void setRadiation_6pm (double R_6pm)
  {
    Radiation_6pm = R_6pm;
  }

  public double getRadiation_6pm()
  {
    return Radiation_6pm;
  }
  
  //********************
  
  //*************** HISTORIC READ IN METHODS *********************//
  
  public void setDay_MAIN (int day_MAIN)
  {
    day_MAIN_instance = day_MAIN;
  }

  public int getDay_MAIN()
  {
    return day_MAIN_instance;
  }
  
  public void setMonth_MAIN (int month_MAIN)
  {
    month_MAIN_instance = month_MAIN;
  }

  public int getMonth_MAIN()
  {
    return month_MAIN_instance;
  }
  
  public void setYear_MAIN (int year_MAIN)
  {
    year_MAIN_instance = year_MAIN;
  }

  public int getYear_MAIN()
  {
    return year_MAIN_instance;
  }
  
   public void setYear_WinterPrecip (int year_WinterPrecip)
  {
    year_WinterPrecip_instance = year_WinterPrecip;
  }

  public int getYear_WinterPrecip()
  {
    return year_WinterPrecip_instance;
  }
  
   public void setDay_Radiation (int day_Radiation)
  {
    day_Radiation_instance = day_Radiation;
  }

  public int getDay_Radiation()
  {
    return day_Radiation_instance;
  }
  
  public void setMonth_Radiation (int month_Radiation)
  {
    month_Radiation_instance = month_Radiation;
  }

  public int getMonth_Radiation()
  {
    return month_Radiation_instance;
  }
  
  public void setYear_Radiation (int year_Radiation)
  {
    year_Radiation_instance = year_Radiation;
  }

  public int getYear_Radiation()
  {
    return year_Radiation_instance;
  }
  
  public void setPrecipTotal (double precipTotal)
  {
    precipTotal_summer = precipTotal;
  }

  public double getPrecipTotal()
  {
    return precipTotal_summer;
  }
  
  public void setPrecipSeasonList_MAIN(ArrayList<Double> psf)
  {
    PrecipSeasonList_MAIN = psf;
  }

  public List<Double> getPrecipSeasonList_MAIN()
  {
    return PrecipSeasonList_MAIN;
  }
  
  public void setYearFile_MAIN(ArrayList<Integer> yf)
  {
    yearList_MAIN = yf;
  }

  public List<Integer> getYearFile_MAIN()
  {
    return yearList_MAIN;
  }
    
  public void setYearFile_WinterPrecip(ArrayList<Integer> yfwp)
  {
    yearList_WinterPrecip = yfwp;
  }

  public List<Integer> getYearFile_WinterPrecip()
  {
    return yearList_WinterPrecip;
  }
  
  public void setMonthFile_MAIN(ArrayList<Integer> mf)
  {
    monthList_MAIN = mf;
  }

  public List<Integer> getMonthFile_MAIN()
  {
    return monthList_MAIN;
  }
   
  public void setDayFile_Radiation(ArrayList<Integer> dlr)
  {
    dayList_Radiation = dlr;
  }

  public List<Integer> getDayFile_Radiation()
  {
    return dayList_Radiation;
  }
  
  public void setYearFile_Radiation(ArrayList<Integer> yfr)
  {
    yearList_Radiation = yfr;
  }

  public List<Integer> getYearFile_Radiation()
  {
    return yearList_Radiation;
  }
  
  public void setMonthFile_Radiation(ArrayList<Integer> mfr)
  {
    monthList_Radiation = mfr;
  }

  public List<Integer> getMonthFile_Radiation()
  {
    return monthList_Radiation;
  }
  
  //winterPrecip
  public void set_Winter_PrecipFile(ArrayList<Double> wpf)
  {
    WinterPrecipList = wpf;
  }

  public List<Double> get_Winter_PrecipFile()
  {
    return WinterPrecipList;
  }
  
  public void setWinterPrecip(double wp)
  {
    winterPrecip = wp;
  }

  public double getWinterPrecip()
  {
    return winterPrecip;
  }
  
    public void set_Winter_MonthFile(ArrayList<Double> wpf)
  {
    WinterMonthCountList = wpf;
  }

  public List<Double> get_Winter_MonthFile()
  {
    return WinterMonthCountList;
  }
  
  public void setWinterMonthCount(double wmc)
  {
    winterMonthCount = wmc;
  }

  public double getWinterMonthCount()
  {
    return winterMonthCount;
  }
  
  //********************** Monthly Q methods *************************************
          
    public void setQmonthlySurface(double[][] qms)
  {
    surfaceQ_monthlySurface = qms;
  }

  public double[][] getQmonthlySurface()
  {
    return surfaceQ_monthlySurface;
  }
  
    //********************** FIXED ELEVATION ARRAYS***************************
          
    public void setElevationFixedSurface(double[][] eFIXEDs)
  {
    ElevationFixedSurface = eFIXEDs;
  }

  public double[][] getElevationFixedSurface()
  {
    return ElevationFixedSurface;
  }
  
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    
    for (Double temp :  tempList)
    {
      sb.append(temp).append("\n");
    }
    
    return sb.toString();
  }
}
