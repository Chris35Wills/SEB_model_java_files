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
 * This is the compiled contemporary model, incluidng summer and winter
 * precipitation algorithms. THIS FIXES THE ASPECT CALCULATION. To facilitate a
 * fixed aspect surface, aspect is calculated outside of the loop once, is then 
 * set and then not recalculated again.
 *  * 
 * DIFFERS TO ContemporaryModel_Precip_Listener.java AS IT ZEROS SUMMER SNOW AT 
 * THE BGINNING OF WINTER AND PREVENTS WINTER SNOWFALL EXCEED THAT MEASURED AT 
 * THE END OF THE SEASON
 * 
 * Line details:
 * 
 * Winter days loop:                                         L121 - L198 
 * Main model loop:                                          L249 - L1936
 *   Slope calc:
 *   Aspect calc:
 *   Radiation calc (including hillshade acquisition):
 *   Psi (bulk flux) calc: 
 *   Q calc:
 *   Melt calc:
 *   Thickness update
 *   Elevation update:
 * 
 * Model Inputs:
 * 
 * Elevation surfaces: 
 * F:\GIS_temp_back_up\Surfaces [MODEL INPUTS]
 * 
 * Thickness: 
 * F:\GIS_temp_back_up\IceThickness
 * F:\GIS_temp_back_up\Contemp_glacier_thickness
 * F:\GIS_temp_back_up\ALL_glacier_thickness_(5m_resolution) [MODEL INPUTS] <--- instructions also on how to fix resolution
 * 
 * Snow depth: 
 * F:\GIS_with_knowledge\Sweden\Field data\SnowProbe_data\.....
 * 
 * Hillshade:
 * F:\GIS_temp_back_up\AreaGlacier_maps_for_hillshade_layers <-- Still being developed - see notes (average surfaces **considering MODELLED SHADOWS**)
 *
 */
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
import java.util.Iterator;

/**
 *
 * @author Chris
 */
public class ContemporaryModel_Precip_aspect_fixed implements ActionListener {

    private GUIPanel panel = null;
    private Storage store = null;

    ContemporaryModel_Precip_aspect_fixed(GUIPanel panel, Storage store) {

        this.panel = panel;
        this.store = store;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

         double save_arrays = 4; // Set to 1, array saving code will be turned on 
                                // | Set to 0, array saving code will be turned 
                                // off 
                                // | Set to 3 and slope surface will be saved
                                // | Set to 4 and Q surface will be saved

        //Melt algorithm check counters
        int summersnow_hit = 0;
        int nosummersnow_hit = 0;

        int summersnow_wintersnow_hit = 0;
        int summersnow_no_wintersnow_hit = 0;
        int summersnow_wintersnow_ice = 0;
        //int summersnow_no_wintersnow_ice = 0;

        int no_summersnow_wintersnow_hit = 0;
        int no_summersnow_no_wintersnow_hit = 0;
        int no_summersnow_wintersnow_ice = 0;
        //int no_summersnow_no_wintersnow_ice = 0;

        /**
         * Calculate the number of winter days
         *
         */
        //***********************************
        Iterator<Double> iterator0_winter_days = store.getDayList().iterator();
        Iterator<Double> iterator1_winter_days = store.getMonthFile().iterator();
        Iterator<Double> iterator2_winter_days = store.getYearFile().iterator();
        Iterator<Double> iterator3_winter_days = store.getTemperatureFile().iterator();
        Iterator<Double> iterator4_winter_days = store.getPrecipFile().iterator();
        Iterator<Double> iterator4b_winter_days = store.getPrecipSeasonList().iterator();

        //Variables
        double summer_snow_thickness_total; //= 0;
        double summer_snow_thickness_counter; // = 0;
        double winter_snow_thickness_total; // = 0;
        double winter_snow_thickness_counter; // = 0;
        int NODATA_value = -9999;

        //Constants (subject to change) 
//        double snow_threshold = 1.5; // Temperature below which rain falls as snow
//        double fresh_snow_density = 0.400; // Relative to 1.0 for water

        //Arrays
        double elevation1[][] = store.getElevation();
        double elev_initial_size[][] = store.getElevation();
        double summer_snow_thickness_INITIAL[][] = new double[elev_initial_size.length][elev_initial_size[0].length];
        double winter_snow_thickness_INITIAL[][] = new double[elev_initial_size.length][elev_initial_size[0].length];

        for (int i = 0; i < elevation1.length; i++) {
            for (int j = 0; j < elevation1[i].length; j++) {
                if (elevation1[i][j] != NODATA_value) {
                    summer_snow_thickness_INITIAL[i][j] = 0.0;
                    winter_snow_thickness_INITIAL[i][j] = 0.0;
                } else {
                    summer_snow_thickness_INITIAL[i][j] = NODATA_value;
                    winter_snow_thickness_INITIAL[i][j] = NODATA_value;
                }

            }
        }

        store.setSummerSnowSurface(summer_snow_thickness_INITIAL);
        store.setWinterSnowSurface(winter_snow_thickness_INITIAL);

        /**
         * Calculate the total number of winter days for each season (07/08
         * etc)) for use inside the model with the winter precipitation
         * algorithm
         *
         */
        double winter_days_0708_counter = 0;
        double winter_days_0809_counter = 0;
        double winter_days_0910_counter = 0;
        double winter_days_1011_counter = 0;

        while (iterator1_winter_days.hasNext()) {

            double Day_temporary = iterator0_winter_days.next();
            double Year_temporary = iterator2_winter_days.next();
            double Month_temporary = iterator1_winter_days.next();
            double Precip_season_temporary = iterator4b_winter_days.next();
            System.out.println("Loop 1: " + (int) Day_temporary + "/" + (int) Month_temporary + "/" + (int) Year_temporary);

            if (((Year_temporary == 2007.0)
                    && ((Month_temporary == 8.0) || (Month_temporary == 9.0) || (Month_temporary == 10.0) || (Month_temporary == 11.0) || (Month_temporary == 12.0))
                    && (Precip_season_temporary == 0.0))
                    || ((Year_temporary == 2008.0) && ((Month_temporary == 1.0) || (Month_temporary == 2.0) || (Month_temporary == 3.0) || (Month_temporary == 4.0) || (Month_temporary == 5.0) || (Month_temporary == 6.0))
                    && (Precip_season_temporary == 0.0))) {
                //System.out.println("BbbbBbBrrrrrrrrRrRRRRRRR it's cold - must be winter 2007-2008 :^D");
                winter_days_0708_counter++;
            } else if (((Year_temporary == 2008.0)
                    && ((Month_temporary == 8.0) || (Month_temporary == 9.0) || (Month_temporary == 10.0) || (Month_temporary == 11.0) || (Month_temporary == 12.0))
                    && (Precip_season_temporary == 0.0))
                    || ((Year_temporary == 2009.0) && ((Month_temporary == 1.0) || (Month_temporary == 2.0) || (Month_temporary == 3.0) || (Month_temporary == 4.0) || (Month_temporary == 5.0) || (Month_temporary == 6.0))
                    && (Precip_season_temporary == 0.0))) {
                //System.out.println("BbbbBbBrrrrrrrrRrRRRRRRR it's cold - must be winter 2008-2009 :^D");
                winter_days_0809_counter++;
            } else if (((Year_temporary == 2009.0)
                    && ((Month_temporary == 8.0) || (Month_temporary == 9.0) || (Month_temporary == 10.0) || (Month_temporary == 11.0) || (Month_temporary == 12.0))
                    && (Precip_season_temporary == 0.0))
                    || ((Year_temporary == 2010.0) && ((Month_temporary == 1.0) || (Month_temporary == 2.0) || (Month_temporary == 3.0) || (Month_temporary == 4.0) || (Month_temporary == 5.0) || (Month_temporary == 6.0))
                    && (Precip_season_temporary == 0.0))) {
                //System.out.println("BbbbBbBrrrrrrrrRrRRRRRRR it's cold - must be winter 2009-2010 :^D");
                winter_days_0910_counter++;
            } else if (((Year_temporary == 2010.0)
                    && ((Month_temporary == 8.0) || (Month_temporary == 9.0) || (Month_temporary == 10.0) || (Month_temporary == 11.0) || (Month_temporary == 12.0))
                    && (Precip_season_temporary == 0.0))
                    || ((Year_temporary == 2011.0) && ((Month_temporary == 1.0) || (Month_temporary == 2.0) || (Month_temporary == 3.0) || (Month_temporary == 4.0) || (Month_temporary == 5.0) || (Month_temporary == 6.0))
                    && (Precip_season_temporary == 0.0))) {
                //System.out.println("BbbbBbBrrrrrrrrRrRRRRRRR it's cold - must be winter 2010-2011 :^D");
                winter_days_1011_counter++;
            } else {
                //System.out.println("Nice and warm - must be summer :^)");
                // Macht nichts
            }
        }

//        System.out.println("Winter days 07-08:" + winter_days_0708_counter);
//        System.out.println("Winter days 08-09:" + winter_days_0809_counter);
//        System.out.println("Winter days 09-10:" + winter_days_0910_counter);
//        System.out.println("Winter days 10-11:" + winter_days_1011_counter);

        //***********************************

        Iterator<Double> iterator0 = store.getDayList().iterator();
        Iterator<Double> iterator1 = store.getMonthFile().iterator();
        Iterator<Double> iterator2 = store.getYearFile().iterator();
        Iterator<Double> iterator3 = store.getTemperatureFile().iterator();
        Iterator<Double> iterator4 = store.getPrecipFile().iterator();
        Iterator<Double> iterator4b = store.getPrecipSeasonList().iterator();

        Iterator<Double> iterator5 = store.getTOA_midnight_list().iterator();
        Iterator<Double> iterator6 = store.getTOA_6am_list().iterator();
        Iterator<Double> iterator7 = store.getTOA_noon_list().iterator();
        Iterator<Double> iterator8 = store.getTOA_6pm_list().iterator();

        Iterator<Double> iterator9 = store.getAzimuth_midnight_list().iterator();
        Iterator<Double> iterator10 = store.getAzimuth_6am_list().iterator();
        Iterator<Double> iterator11 = store.getAzimuth_noon_list().iterator();
        Iterator<Double> iterator12 = store.getAzimuth_6pm_list().iterator();

        Iterator<Double> iterator13 = store.getZenith_midnight_list().iterator();
        Iterator<Double> iterator14 = store.getZenith_6am_list().iterator();
        Iterator<Double> iterator15 = store.getZenith_noon_list().iterator();
        Iterator<Double> iterator16 = store.getZenith_6pm_list().iterator();

        Iterator<Double> iterator17 = store.getTauEstimateList().iterator();

        // Loop counters 
        int Loop_counter = 0; //Must be initialized outside of loop

        System.out.println("ASPECT FIXED model commencing");

        File f_stats = new File("F:/Melt_modelling/Model_outputs_aspect_fixed/Model_run_mean_stats.txt");

        FileWriter fw_stats;
        DecimalFormat df_stats = new DecimalFormat("#.####");

        try {
            BufferedWriter bw_stats = new BufferedWriter(new FileWriter(f_stats));
            bw_stats.write("Loop number" + ", " + "Day" + ", " + "Month" + ", " + "Year" + ", " + "Mean Radiation (Wm^-2)" + ", "
                    + "Mean Psi (Wm^-2)" + ", " + "Mean Q (Wm^-2)" + ", "
                    + "Mean glacier air temp. (deg C)" + ", " + "Mean snowfall (m)"
                    + ", " + "Mean surface melt (m)" + ", "
                    + "Mean volume change (m^3)");

            bw_stats.newLine();

            // Beginning of the main model looping (ends at about Ln 1936)

            // VARIABLES TO CHANGE FOR SENSITIVITY ANALYSIS

            double ice_density = 900.0;
            double summer_snow_density = 200.0; // Taken from Paterson (1994) (p9, table 2.1)
            double winter_snow_density = 407.13; // Average density value from 2008 - 2011 snow pit analysis
            double snow_wind_stripping_factor = 0.5; // 0.5
            double psi_min = -19;
            double c = 8.4;
            double T_tip = +0.2;
            double ice_albedo = 0.39;//0.39
            double snow_albedo = 0.70;//0.70
            //double threshold_snow_thickness_for_albedo = 0.05; // Subject to change - threshold for acquiring albedo from layer beneath
            double snow_threshold = 1.5; // Temperature below which rain falls as snow
            //double fresh_snow_density = 0.400; // Relative to 1.0 for water

            System.out.println("Ice density: " + ice_density);
            System.out.println("Summer snow density: " + summer_snow_density);
            System.out.println("Winter snow density: " + winter_snow_density);
            System.out.println("Snow stripping [wind] factor : " + snow_wind_stripping_factor);
            System.out.println("Psi min: " + psi_min);
            System.out.println("Variable c: " + c);
            System.out.println("T_tip: " + T_tip);
            System.out.println("Ice albedo: " + ice_albedo);
            System.out.println("Snow_albedo: " + snow_albedo);
            //System.out.println("Threshold for degradation of albedo according to snow thickness:" + threshold_snow_thickness_for_albedo);
            
            /*
             * Aspect calculation
             */
            
                double aspectSurface[][] = new double[elev_initial_size.length][elev_initial_size[0].length];
                double aspectA, aspectB, aspectC, aspectD, aspectE, aspectF, aspectG, aspectH, aspectI;
                double aspect_dzdy, aspect_dzdx;
                double aspect_pre;
                double aspectDegree;
            

                for (int i = 0; i < elevation1.length; i++) {
                    for (int j = 0; j < elevation1[i].length; j++) {

                        if (elevation1[i][j] != NODATA_value) {

                            // Try/catch blocks exist in case of "out of bounds exceptions" 
                            // which are likely when running the neighborhood searches at the 
                            // array edges - this will be called upon less when using the 
                            // larger arrays populated with No_Data (i.e. -9999.0) values
                            //
                            // These assign the values to letters A-I, according to positions 
                            // within the array assuming the structure of:
                            //   A   B   C
                            //   D   E   F
                            //   G   H   I

                            try {
                                if ((elevation1[i - 1][j - 1] != NODATA_value)) {
                                    aspectA = elevation1[i - 1][j - 1];
                                } else {
                                    aspectA = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe0) {
                                aspectA = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i - 1][j] != NODATA_value)) {
                                    aspectB = elevation1[i - 1][j];
                                } else {
                                    aspectB = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe1) {
                                aspectB = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i - 1][j + 1] != NODATA_value)) {
                                    aspectC = elevation1[i - 1][j + 1];
                                } else {
                                    //error1 = ("C fail");//
                                    aspectC = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe2) {
                                //error1 = ("C fail catch"); //
                                aspectC = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i][j - 1] != NODATA_value)) {
                                    aspectD = elevation1[i][j - 1];
                                } else {
                                    aspectD = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe3) {
                                aspectD = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i][j] != NODATA_value)) {
                                    aspectE = elevation1[i][j];
                                } else {
                                    aspectE = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe4) {
                                aspectE = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i][j + 1] != NODATA_value)) {
                                    aspectF = elevation1[i][j + 1];
                                } else {
                                    aspectF = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe5) {
                                aspectF = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i + 1][j - 1] != NODATA_value)) {
                                    aspectG = elevation1[i + 1][j - 1];
                                } else {
                                    aspectG = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe6) {
                                aspectG = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i + 1][j] != NODATA_value)) {
                                    aspectH = elevation1[i + 1][j];
                                } else {
                                    aspectH = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe7) {
                                aspectH = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i + 1][j + 1] != NODATA_value)) {
                                    aspectI = elevation1[i + 1][j + 1];
                                } else {
                                    aspectI = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe8) {
                                aspectI = elevation1[i][j];
                            }

                            // Breaks down the aspect algorithm into a few different steps

                            aspect_dzdx = (((aspectC + (2 * aspectF) + aspectI) - (aspectA + (2 * aspectD) + aspectG)) / (8));
                            aspect_dzdy = (((aspectG + (2 * aspectH) + aspectI) - (aspectA + (2 * aspectB) + aspectC)) / (8));

                            aspect_pre = ((180 / Math.PI) * Math.atan2(aspect_dzdy, -aspect_dzdx));

                            // Insert if loop to convert aspect to compass direction (degrees)

                            if (aspect_pre < 0) {
                                aspectDegree = (90.0 - aspect_pre);
                            } else if (aspect_pre > 90.0) {
                                aspectDegree = (360.0 - aspect_pre + 90.0);
                            } else {
                                aspectDegree = (90.0 - aspect_pre);
                            }

                            // Sets the aspect value in the appropriate position in the 
                            // aspectSurface array

                            aspectSurface[i][j] = aspectDegree;

                        } else {

                            aspectSurface[i][j] = NODATA_value;
                        }

                    }

                }

                store.setAspect(aspectSurface);
                //System.out.println("Aspect calculated");
                
            // Beginning of main model loop
            while (iterator0.hasNext()) {

                double elevation[][] = store.getElevation();
                store.setElevationSize(elevation);
                //double Elevation_size[][] = null;
                double Elevation_size[][] = store.getElevationSize();

                double glacier_thickness[][] = store.getThickness(); //Only called once; following this, loops must use the updated thickness and elevation variables

                double summer_snow_thickness[][] = store.getSummerSnowSurface(); // An empty array in the first iteration of this loop
                double winter_snow_thickness[][] = store.getWinterSnowSurface(); // An empty array in the first iteration of this loop
                double WinterSnow_interpolation[][];
                double temp_winter_snow_layer[][] = new double[elevation.length][elevation[0].length];

                double glacier_thickness_temp[][] = glacier_thickness; // This gets a copy of glacier_thickness 
                store.setThickness_intermediary(glacier_thickness_temp); // This sets the copy of thickness, making a clone of it (i.e. it can't be affcted if you alter the "glacier_thickness" object
                double glacier_thickness_intermediary[][] = store.getThickness_intermediary(); //This gets the "clone" - so this will remain an unaltered thickness layer throughout the loop (unlike "glacierThickness[][]")

                double day = iterator0.next();
                double month = iterator1.next();
                double year = iterator2.next();
                double temp = iterator3.next();
                store.setTemp(temp);
                double precip = iterator4.next(); // This is the daily total precipitation in metres - check the input data file with the raw if you are unsure
                double precip_season = iterator4b.next();

                double TOA_midnight = iterator5.next();
                double TOA_6am = iterator6.next();
                double TOA_noon = iterator7.next();
                double TOA_6pm = iterator8.next();

                double azimuth_midnight = iterator9.next();
                double azimuth_6am = iterator10.next();
                double azimuth_noon = iterator11.next();
                double azimuth_6pm = iterator12.next();

                double zenith_midnight = iterator13.next();
                double zenith_6am = iterator14.next();
                double zenith_noon = iterator15.next();
                double zenith_6pm = iterator16.next();

                double Tau_estimate = iterator17.next();

                System.out.println((int) day + "/" + (int) month + "/" + (int) year);
                //System.out.println("Everything set and good to go");

                //Surface variables

                // double winter_snow[][] = store.getWinterSnow(); // See precipitation (contemp.) module for info - set/get methods for snow interpolations EXIST

                //Slope variables
                double Slope_Surface[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double slopeA, slopeB, slopeC, slopeD, slopeE, slopeF, slopeG, slopeH, slopeI;
                double slope_dzdy, slope_dzdx;
                double slopeDegree;

                //Aspect variables
//                double aspectSurface[][] = new double[Elevation_size.length][Elevation_size[0].length];
//                double aspectA, aspectB, aspectC, aspectD, aspectE, aspectF, aspectG, aspectH, aspectI;
//                double aspect_dzdy, aspect_dzdx;
//                double aspect_pre;
//                double aspectDegree;

                //Radiation variables
                double slope[][], aspect[][], slope_RAD[][], aspect_RAD[][],
                        surfaceRadiationSurface[][];
                //double hillshade[][] = null; 
                double hillshade_midnight[][] = null;
                double hillshade_6am[][] = null;
                double hillshade_noon[][] = null;
                double hillshade_6pm[][] = null;
                double tau;
                double azimuth_midnight_radians, zenith_midnight_radians; //Midnight variables
                double azimuth_6am_radians, zenith_6am_radians; //6am variables
                double azimuth_noon_radians, zenith_noon_radians; //noon variables
                double azimuth_6pm_radians, zenith_6pm_radians; //6pm variables
                double surface_Radiation;
                double surface_Radiation_midnight;
                double surface_Radiation_6am;
                double surface_Radiation_noon;
                double surface_Radiation_6pm;

                //Bulk flux variables
//                double psi_min = -25;
//                double c = 8.7;
//                double T_tip = -1.5;
                double cTa;
                double psi = 0;
                double lapsed_Ta;
                double lapsed_temp[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double psi_surface[][] = new double[Elevation_size.length][Elevation_size[0].length];

                //Q calculation variables
//                double ice_albedo = 0.39;
//                double snow_albedo = 0.70;
                double albedo_instance = 0.0;
                //double threshold_snow_thickness_for_albedo = 0.05; // Subject to change - threshold for acquiring albedo from layer beneath

                double Q_daily_grid[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double I_for_Q;
                double psi_for_Q;
                double Q, Q_daily;
                double Q_counter = 0;
                double Q_total = 0;
                //double Q_value = 0;
                double mean_Q_m2;
                double mean_Q_5_x_m2;
                //double Q_per_cell_area; 
                double cell_area = 25.0;

                //Melt calculation variables

                double adjustment_counter = 0;
                double thickness_adjustment_total = 0;
                double volume_adjustment_total = 0;
                //double ice_thickness_change_counter = 0;
                //double ice_volume_change_counter = 0;
                double ice_thickness_surface_adjustment[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double ice_thickness_change_surface[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double summer_snow_surface_change[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double winter_snow_surface_change[][] = new double[Elevation_size.length][Elevation_size[0].length];
                //double ice_volume_change_surface[][] = new double[Elevation_size.length][Elevation_size[0].length];
                //double ice_lowering[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double latent_heat_of_fusion = 334000;
                double energy_for_total_summer_snow_melt;
                double energy_for_total_winter_snow_melt;
                //double ice_density = 900.0;
                //double summer_snow_density = 300.0; // Check and change this value
                //double winter_snow_density = 200.0; // Check and change this value
                double ice_mass;
                double snow_mass;
                double energy_for_total_melt;
                double Q_available;
//    double ice_thickness_change;
                //double thickness_change_base_level = 0;
                //double ice_thickness_change_total = 0;
                //double ice_volume_change_total = 0;
                //double ice_volume_change;

                //Counters (zeroed at the beginning of each loop)

                double Radiation_counter = 0;
                double Radiation_total = 0;
                double Psi_counter = 0;
                double Psi_total = 0;
                double Lapsed_temp_counter = 0;
                double Lapsed_temp_total = 0;
                double Snowfall_counter = 0;
                double Snowfall_total = 0;

                /*
                 * Precipitation algorithm
                 */

                //**************Summer precipitation**************

                // REMEMBER: when dividing precipitation by snow density, do not 
                // use the value in kg m^3 (e.g. 900.0 kg m^3). You must instead 
                // use that value divided by 100 (e.g. 0.9). In this way, where 
                // the threshold is not met rainfall at the cell is equal to the 
                // AWS (i.e. measured rainfall/1) To divide the emasured 
                // rainfall by water density (1000.0 kg m^3) would give very 
                // warped results! 

                if (precip_season == 1.0) {
                    //System.out.println("Summer precipitation");

                    /*
                     * Lapse rate calculater
                     */

                    for (int i = 0; i < elevation.length; i++) {
                        for (int j = 0; j < elevation[i].length; j++) {

                            double lapse_intermediary = elevation[i][j];

                            if (lapse_intermediary != NODATA_value) {
                                lapsed_temp[i][j] = (double) LapseRate(lapse_intermediary, temp);
                            } else if (lapse_intermediary == NODATA_value) {
                                lapsed_temp[i][j] = (int) NODATA_value;
                            }
                        }
                    }

                    store.setLapsedTempSurface(lapsed_temp);

                    /*
                     * Populate summer snow thickness layer
                     */

                    for (int i = 0; i < summer_snow_thickness.length; i++) {
                        for (int j = 0; j < summer_snow_thickness[i].length; j++) {

                            if (((lapsed_temp[i][j] != NODATA_value) & (precip != NODATA_value)) & ((precip != 0) & (lapsed_temp[i][j] <= snow_threshold))) // If less than or equal to threshold, snow amount is equal to precip * fresh snow density
                            {
                                summer_snow_thickness[i][j] += (precip / (summer_snow_density / 100)); // Increase the value of the existing cell by new snow fall
                            } else if (((lapsed_temp[i][j] != NODATA_value) & (precip != NODATA_value)) & (lapsed_temp[i][j] > snow_threshold)) // This simulates rain which flows off the surface
                            {
                                summer_snow_thickness[i][j] += 0.0;  // Increase the value of the existing cell by nothing (could just do nothing...)
                            } else if ((lapsed_temp[i][j] != NODATA_value) & (precip == NODATA_value)) // There are some instances where the AWS didn't work so this is pertinent
                            {
                                summer_snow_thickness[i][j] += 0.0;
                            } else if (lapsed_temp[i][j] == NODATA_value) {
                                summer_snow_thickness[i][j] = NODATA_value;
                            }
                        }
                    }

                    store.setSummerSnowSurface(summer_snow_thickness);

                    /**
                     * Loops through the SummerSnowSurface layer - this just
                     * gives the mean thickness over the summer grid which is
                     * continuously increasing (so long as melt is not
                     * occurring)
                     *
                     */
                    summer_snow_thickness_total = 0; // Has to be done here. If set as 0 outide the loop the total will grow forever
                    summer_snow_thickness_counter = 0; // Has to be done here. If set as 0 outide the loop the total will grow forever

                    for (int i = 0; i < summer_snow_thickness.length; i++) {
                        for (int j = 0; j < summer_snow_thickness[i].length; j++) {

                            if (summer_snow_thickness[i][j] != NODATA_value) {
                                //System.out.println("Summer snow cells: " + summer_snow_thickness[i][j]);
                                double summer_snow_thickness_instance = summer_snow_thickness[i][j];
                                summer_snow_thickness_total += summer_snow_thickness_instance; // Accumulates all summer snow thickness changes
                                summer_snow_thickness_counter++; // Counts instances of summer snow thickness where != NODATA_value
                            } else if (summer_snow_thickness[i][j] == NODATA_value) {
                                //do nothing
                            }
                        }
                    }

                    double mean_daily_summer_snowfall = summer_snow_thickness_total / summer_snow_thickness_counter;
                    //System.out.println("Mean accumulated snow thickness (of summer layer) at this point: " + mean_daily_summer_snowfall + "m");
                    //System.out.println("Don't be alarmed if the above value doesn't go up in tandem with the measured precipitation value - don't forget it only snows as a function of lapse corrected temperature.");

                } //**************Winter precipitation************** 
                else if (precip_season == 0.0) {
                    //System.out.println("Winter precipitation");

                    // This bit does:

                    //(1) Zeros the snow thickness surface (summer snow at the end of 
                    //summer is assumed to be a part of the winetr snow pack that was 
                    //measured in the field at the end of march and is tehrefore already 
                    //accounted for in the daily winter snowfall algorithm

                    for (int i = 0; i < summer_snow_thickness.length; i++) {
                        for (int j = 0; j < summer_snow_thickness.length; j++) {

                            if (summer_snow_thickness[i][j] != NODATA_value) {

                                summer_snow_thickness[i][j] = 0;
                                store.setSummerSnowSurface(summer_snow_thickness);

                            } else if (summer_snow_thickness[i][j] == NODATA_value) {

                                summer_snow_thickness[i][j] = NODATA_value;
                                store.setSummerSnowSurface(summer_snow_thickness);
                            }
                        }
                    }

                    // (2) Select correct snow layer according to month and year
                    // (3) Add to each cell in winter_snow_thickness the value of the 
                    //     imported snow layer divided by the number of days in winter 
                    //     (calculated outside of the loop)

                    if (((year == 2007.0)
                            && ((month == 9.0) || (month == 10.0) || (month == 11.0) || (month == 12.0)))
                            | ((year == 2008.0) && ((month == 1.0) || (month == 2.0) || (month == 3.0) || (month == 4.0) || (month == 5.0)))) {
                        WinterSnow_interpolation = store.get_snow_surface_0708();

                        for (int i = 0; i < WinterSnow_interpolation.length; i++) {
                            for (int j = 0; j < WinterSnow_interpolation[i].length; j++) {

                                if ((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] != 0)) {
                                    temp_winter_snow_layer[i][j] = WinterSnow_interpolation[i][j] / winter_days_0708_counter;

                                } else if ((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] == 0)) {
                                    temp_winter_snow_layer[i][j] = 0;
                                } else if ((elevation[i][j] == NODATA_value) || (WinterSnow_interpolation[i][j] == NODATA_value)) {
                                    temp_winter_snow_layer[i][j] = NODATA_value;
                                }
                            }
                        }

                        // Update thickness

                        for (int i = 0; i < winter_snow_thickness.length; i++) {
                            for (int j = 0; j < winter_snow_thickness[i].length; j++) {

                                if (temp_winter_snow_layer[i][j] != NODATA_value) {

                                    winter_snow_thickness[i][j] += temp_winter_snow_layer[i][j];

                                    // Prevent winter snow thickness exceeding the measured end of winter season snow thickness
                                    if (winter_snow_thickness[i][j] > WinterSnow_interpolation[i][j]) {
                                        winter_snow_thickness[i][j] = WinterSnow_interpolation[i][j];
                                    } else {
                                    }

                                } else if (temp_winter_snow_layer[i][j] == NODATA_value) {

                                    winter_snow_thickness[i][j] = NODATA_value;

                                }

                            }

                            store.setWinterSnowThickness(winter_snow_thickness);
                        }
                    } else if (((year == 2008.0)
                            && ((month == 9.0) || (month == 10.0) || (month == 11.0) || (month == 12.0)))
                            | ((year == 2009.0) && ((month == 1.0) || (month == 2.0) || (month == 3.0) || (month == 4.0) || (month == 5.0)))) {
                        WinterSnow_interpolation = store.get_snow_surface_0809();

                        for (int i = 0; i < WinterSnow_interpolation.length; i++) {
                            for (int j = 0; j < WinterSnow_interpolation[i].length; j++) {

                                if ((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] != 0)) {
                                    temp_winter_snow_layer[i][j] = WinterSnow_interpolation[i][j] / winter_days_0809_counter;
                                } else if ((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] == 0)) {
                                    temp_winter_snow_layer[i][j] = 0;
                                } else if ((elevation[i][j] == NODATA_value) || (WinterSnow_interpolation[i][j] == NODATA_value)) {
                                    temp_winter_snow_layer[i][j] = NODATA_value;
                                }
                            }
                        }


                        // Update thickness

                        for (int i = 0; i < winter_snow_thickness.length; i++) {
                            for (int j = 0; j < winter_snow_thickness[i].length; j++) {

                                if (temp_winter_snow_layer[i][j] != NODATA_value) {

                                    winter_snow_thickness[i][j] += temp_winter_snow_layer[i][j];

                                    // Prevent winter snow thickness exceeding the measured end of winter season snow thickness
                                    if (winter_snow_thickness[i][j] > WinterSnow_interpolation[i][j]) {
                                        winter_snow_thickness[i][j] = WinterSnow_interpolation[i][j];
                                    } else {
                                    }
                                } else if (temp_winter_snow_layer[i][j] == NODATA_value) {

                                    winter_snow_thickness[i][j] = NODATA_value;

                                }

                            }

                            store.setWinterSnowThickness(winter_snow_thickness);
                        }

                    } else if (((year == 2009.0)
                            && ((month == 9.0) || (month == 10.0) || (month == 11.0) || (month == 12.0)))
                            | ((year == 2010.0) && ((month == 1.0) || (month == 2.0) || (month == 3.0) || (month == 4.0) || (month == 5.0)))) {
                        WinterSnow_interpolation = store.get_snow_surface_0910();

                        for (int i = 0; i < WinterSnow_interpolation.length; i++) {
                            for (int j = 0; j < WinterSnow_interpolation[i].length; j++) {

                                if ((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] != 0)) {
                                    temp_winter_snow_layer[i][j] = WinterSnow_interpolation[i][j] / winter_days_0910_counter;
                                } else if ((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] == 0)) {
                                    temp_winter_snow_layer[i][j] = 0;
                                } else if ((elevation[i][j] == NODATA_value) || (WinterSnow_interpolation[i][j] == NODATA_value)) {
                                    temp_winter_snow_layer[i][j] = NODATA_value;
                                }
                            }
                        }

                        // Update thickness

                        for (int i = 0; i < winter_snow_thickness.length; i++) {
                            for (int j = 0; j < winter_snow_thickness[i].length; j++) {

                                if (temp_winter_snow_layer[i][j] != NODATA_value) {

                                    winter_snow_thickness[i][j] += temp_winter_snow_layer[i][j];

                                    // Prevent winter snow thickness exceeding the measured end of winter season snow thickness
                                    if (winter_snow_thickness[i][j] > WinterSnow_interpolation[i][j]) {
                                        winter_snow_thickness[i][j] = WinterSnow_interpolation[i][j];
                                    } else {
                                    }
                                } else if (temp_winter_snow_layer[i][j] == NODATA_value) {
                                    winter_snow_thickness[i][j] = NODATA_value;
                                }

                            }

                            store.setWinterSnowThickness(winter_snow_thickness);

                        }
                    } else if (((year == 2010.0)
                            && ((month == 9.0) || (month == 10.0) || (month == 11.0) || (month == 12.0)))
                            | ((year == 2011.0) && ((month == 1.0) || (month == 2.0) || (month == 3.0) || (month == 4.0) || (month == 5.0)))) {
                        WinterSnow_interpolation = store.get_snow_surface_1011();

                        for (int i = 0; i < WinterSnow_interpolation.length; i++) {
                            for (int j = 0; j < WinterSnow_interpolation[i].length; j++) {

                                if ((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] != 0)) {
                                    temp_winter_snow_layer[i][j] = WinterSnow_interpolation[i][j] / winter_days_1011_counter;
                                } else if ((elevation[i][j] != NODATA_value) && (WinterSnow_interpolation[i][j] == 0)) {
                                    temp_winter_snow_layer[i][j] = 0;
                                } else if ((elevation[i][j] == NODATA_value) || (WinterSnow_interpolation[i][j] == NODATA_value)) {
                                    temp_winter_snow_layer[i][j] = NODATA_value;
                                }
                            }
                        }


                        // Update thickness

                        for (int i = 0; i < winter_snow_thickness.length; i++) {
                            for (int j = 0; j < winter_snow_thickness[i].length; j++) {

                                if (temp_winter_snow_layer[i][j] != NODATA_value) {

                                    winter_snow_thickness[i][j] += temp_winter_snow_layer[i][j];

                                    // Prevent winter snow thickness exceeding the measured end of winter season snow thickness
                                    if (winter_snow_thickness[i][j] > WinterSnow_interpolation[i][j]) {
                                        winter_snow_thickness[i][j] = WinterSnow_interpolation[i][j];
                                    } else {
                                    }

                                } else if (temp_winter_snow_layer[i][j] == NODATA_value) {
                                    winter_snow_thickness[i][j] = NODATA_value;
                                }

                            }
                            store.setWinterSnowThickness(winter_snow_thickness);
                        }
                    }

                    /**
                     * Loops through the WinterSnowSurface layer and calculates
                     * mean total accumulated thickness on a given day
                     *
                     */
                    winter_snow_thickness_total = 0; // Has to be done here. If set as 0 outide the loop the total will grow forever
                    winter_snow_thickness_counter = 0; // Has to be done here. If set as 0 outide the loop the total will grow forever

                    for (int i = 0; i < winter_snow_thickness.length; i++) {
                        for (int j = 0; j < winter_snow_thickness[i].length; j++) {

                            if (winter_snow_thickness[i][j] != NODATA_value) {
                                //System.out.println("Winter snow cells: " + winter_snow_thickness[i][j]);
                                double winter_snow_thickness_instance = winter_snow_thickness[i][j];
                                winter_snow_thickness_total += winter_snow_thickness_instance; // Accumulates all summer snow thickness changes
                                winter_snow_thickness_counter++; // Counts instances of summer snow thickness where != NODATA_value
                            } else if (winter_snow_thickness[i][j] == NODATA_value) {
                                //do nothing
                            }
                        }
                    }
                    //System.out.println("Mean accumulated winter snowfall at this point: " + winter_snow_thickness_total / winter_snow_thickness_counter + "m");
                    store.setWinterSnowThickness(winter_snow_thickness);
                }

                //*************************************** End of precipitation algorithms

                /*
                 * Slope calculation
                 */

                for (int i = 0; i < elevation.length; i++) {
                    for (int j = 0; j < elevation[i].length; j++) {

                        if (elevation[i][j] != NODATA_value) {

                            // Try/catch blocks exist in case of "out of bounds exceptions" 
                            // which are likely when running the neighborhood searches at the 
                            // array edges - this will be called upon less when using the 
                            // larger arrays populated with No_Data (i.e. -9999.0) values
                            //
                            // These assign the values to letters A-I, according to positions 
                            // within the array assuming the structure of:
                            //   A   B   C
                            //   D   E   F
                            //   G   H   I

                            try {
                                if ((elevation[i - 1][j - 1] != NODATA_value)) {
                                    slopeA = elevation[i - 1][j - 1];
                                } else {
                                    slopeA = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe0) {
                                slopeA = elevation[i][j];
                            }

                            try {
                                if ((elevation[i - 1][j] != NODATA_value)) {
                                    slopeB = elevation[i - 1][j];
                                } else {
                                    slopeB = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe1) {
                                slopeB = elevation[i][j];
                            }

                            try {
                                if ((elevation[i - 1][j + 1] != NODATA_value)) {
                                    slopeC = elevation[i - 1][j + 1];
                                } else {
                                    //error1 = ("C fail");//
                                    slopeC = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe2) {
                                //error1 = ("C fail catch"); //
                                slopeC = elevation[i][j];
                            }

                            try {
                                if ((elevation[i][j - 1] != NODATA_value)) {
                                    slopeD = elevation[i][j - 1];
                                } else {
                                    slopeD = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe3) {
                                slopeD = elevation[i][j];
                            }

                            try {
                                if ((elevation[i][j] != NODATA_value)) {
                                    slopeE = elevation[i][j];
                                } else {
                                    slopeE = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe4) {
                                slopeE = elevation[i][j];
                            }

                            try {
                                if ((elevation[i][j + 1] != NODATA_value)) {
                                    slopeF = elevation[i][j + 1];
                                } else {
                                    slopeF = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe5) {
                                slopeF = elevation[i][j];
                            }

                            try {
                                if ((elevation[i + 1][j - 1] != NODATA_value)) {
                                    slopeG = elevation[i + 1][j - 1];
                                } else {
                                    slopeG = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe6) {
                                slopeG = elevation[i][j];
                            }

                            try {
                                if ((elevation[i + 1][j] != NODATA_value)) {
                                    slopeH = elevation[i + 1][j];
                                } else {
                                    slopeH = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe7) {
                                slopeH = elevation[i][j];
                            }

                            try {
                                if ((elevation[i + 1][j + 1] != NODATA_value)) {
                                    slopeI = elevation[i + 1][j + 1];
                                } else {
                                    slopeI = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe8) {
                                slopeI = elevation[i][j];
                            }

                            slope_dzdx = (((slopeC + (2 * slopeF) + slopeI) - (slopeA + (2 * slopeD) + slopeG)) / (8 * 5));
                            slope_dzdy = (((slopeG + (2 * slopeH) + slopeI) - (slopeA + (2 * slopeB) + slopeC)) / (8 * 5));
                            slopeDegree = Math.atan(Math.sqrt((Math.pow(slope_dzdx, 2)
                                    + Math.pow(slope_dzdy, 2)))) * (180 / Math.PI);

                            Slope_Surface[i][j] = slopeDegree;

                        } else if (elevation[i][j] == NODATA_value) {

                            Slope_Surface[i][j] = NODATA_value;
                        }
                    }
                }

                store.setSlope(Slope_Surface);
                //System.out.println("Slope calculated");

                
                /*
                 * Radiation Algorithm
                 */

                //Gets the TOA, solar azimuth and solar zenith values for the different time 
                //itervals (if using this as a stand alone module then it will only access 
                //the last looped variables in the read in table - needs to be fully 
                //integrated within a loop of the model inout file

//   TOA_midnight = store.getTOA_midnight() ;
//   azimuth_midnight = store.getAzimuth_midnight() ;
//   zenith_midnight = store.getZenith_midnight();

//   TOA_6am = store.getTOA_6am() ;
//   azimuth_6am = store.getAzimuth_6am() ;
//   zenith_6am = store.getZenith_6am();

//   TOA_noon = store.getTOA_noon() ;
//   azimuth_noon = store.getAzimuth_noon() ;
//   zenith_noon = store.getZenith_noon();

//   TOA_6pm = store.getTOA_6pm() ;
//   azimuth_6pm = store.getAzimuth_6pm() ;
//   zenith_6pm = store.getZenith_6pm();

                //Gets the calculated slope and aspect surfaces   

                slope = store.getSlope(); // slope surface array
                aspect = store.getAspect(); // aspect surface arrays
//    Will ned to test for which month is uploaded <- possible once this is 
//    installed within the overall model inputs list loop:
//   
//    1) Try to get all surfaces first....
//    2) Print out which surfaces have been uploaded
//    3) Run algorithm below according 
//       to the month list file

                if (month == 1.0) {

                    //hillshade = store.getHillshade_Jan();
                    hillshade_midnight = store.getHillshade_Jan_midnight();
                    hillshade_6am = store.getHillshade_Jan_6am();
                    hillshade_noon = store.getHillshade_Jan_noon();
                    hillshade_6pm = store.getHillshade_Jan_6pm();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
                        //System.out.println("January hillshade uploaded into model");
                        //System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("January hillshade not uploaded");
                    }

                } else if (month == 2.0) {

                    //hillshade = store.getHillshade_Feb();
                    hillshade_midnight = store.getHillshade_Feb_midnight();
                    hillshade_6am = store.getHillshade_Feb_6am();
                    hillshade_noon = store.getHillshade_Feb_noon();
                    hillshade_6pm = store.getHillshade_Feb_6pm();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
                        //System.out.println("February hillshade uploaded into model");
                        //System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("February hillshade not uploaded");
                    }
                } else if (month == 3.0) {

                    //hillshade = store.getHillshade_March();
                    hillshade_midnight = store.getHillshade_March_midnight();
                    hillshade_6am = store.getHillshade_March_6am();
                    hillshade_noon = store.getHillshade_March_noon();
                    hillshade_6pm = store.getHillshade_March_6pm();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("March hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("March hillshade not uploaded");
                    }

                } else if (month == 4.0) {

                    //hillshade = store.getHillshade_April();
                    hillshade_midnight = store.getHillshade_April_midnight();
                    hillshade_6am = store.getHillshade_April_6am();
                    hillshade_noon = store.getHillshade_April_noon();
                    hillshade_6pm = store.getHillshade_April_6pm();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("April hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("April hillshade not uploaded");
                    }

                } else if (month == 5.0) {

                    //hillshade = store.getHillshade_May();
                    hillshade_midnight = store.getHillshade_May_midnight();
                    hillshade_6am = store.getHillshade_May_6am();
                    hillshade_noon = store.getHillshade_May_noon();
                    hillshade_6pm = store.getHillshade_May_6pm();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("May hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("May hillshade not uploaded");
                    }

                } else if (month == 6.0) {

                    //hillshade = store.getHillshade_June();
                    hillshade_midnight = store.getHillshade_June_midnight();
                    hillshade_6am = store.getHillshade_June_6am();
                    hillshade_noon = store.getHillshade_June_noon();
                    hillshade_6pm = store.getHillshade_June_6pm();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("June hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("June hillshade not uploaded");
                    }

                } else if (month == 7.0) {

                    //hillshade = store.getHillshade_July();
                    hillshade_midnight = store.getHillshade_July_midnight();
                    hillshade_6am = store.getHillshade_July_6am();
                    hillshade_noon = store.getHillshade_July_noon();
                    hillshade_6pm = store.getHillshade_July_6pm();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("July hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("July hillshade not uploaded");
                    }

                } else if (month == 8.0) {

                    //hillshade = store.getHillshade_August();
                    hillshade_midnight = store.getHillshade_August_midnight();
                    hillshade_6am = store.getHillshade_August_6am();
                    hillshade_noon = store.getHillshade_August_noon();
                    hillshade_6pm = store.getHillshade_August_6pm();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("August hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("August hillshade not uploaded");
                    }

                } else if (month == 9.0) {

                    //hillshade = store.getHillshade_Sept();
                    hillshade_midnight = store.getHillshade_Sept_midnight();
                    hillshade_6am = store.getHillshade_Sept_6am();
                    hillshade_noon = store.getHillshade_Sept_noon();
                    hillshade_6pm = store.getHillshade_Sept_6pm();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("September hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("September hillshade not uploaded");
                    }

                } else if (month == 10.0) {

                    //hillshade = store.getHillshade_Oct();
                    hillshade_midnight = store.getHillshade_Oct_midnight();
                    hillshade_6am = store.getHillshade_Oct_6am();
                    hillshade_noon = store.getHillshade_Oct_noon();
                    hillshade_6pm = store.getHillshade_Oct_6pm();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("October hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("October hillshade not uploaded");
                    }

                } else if (month == 11.0) {

                    //hillshade = store.getHillshade_Nov();
                    hillshade_midnight = store.getHillshade_Nov_midnight();
                    hillshade_6am = store.getHillshade_Nov_6am();
                    hillshade_noon = store.getHillshade_Nov_noon();
                    hillshade_6pm = store.getHillshade_Nov_6pm();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("November hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("November hillshade not uploaded");
                    }

                } else if (month == 12.0) {

                    //hillshade = store.getHillshade_Dec();
                    hillshade_midnight = store.getHillshade_Dec_midnight();
                    hillshade_6am = store.getHillshade_Dec_6am();
                    hillshade_noon = store.getHillshade_Dec_noon();
                    hillshade_6pm = store.getHillshade_Dec_6pm();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("December hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("December hillshade not uploaded");
                    }
                }

                slope_RAD = new double[slope.length][slope[0].length];
                aspect_RAD = new double[slope.length][slope[0].length];

                //Converts the slope and aspect surfaces to radians

                for (int a = 0; a < Slope_Surface.length; a++) {
                    for (int b = 0; b < Slope_Surface[a].length; b++) {

                        if (slope[a][b] != NODATA_value) {

                            double slope_initial_value = Slope_Surface[a][b];
                            double slope_radians = Math.toRadians(slope_initial_value);
                            slope_RAD[a][b] = slope_radians;

                        } else {

                            slope_RAD[a][b] = NODATA_value;

                        }

                        if (aspectSurface[a][b] != NODATA_value) {

                            double aspect_initial_value = aspectSurface[a][b];
                            double aspect_radians = Math.toRadians(aspect_initial_value);
                            aspect_RAD[a][b] = aspect_radians;

                        } else {

                            aspect_RAD[a][b] = NODATA_value;

                        }
                    }
                }

                // Calculate surface radiation surface considering aspect/slope/hillshade for interval time steps

                surfaceRadiationSurface = new double[elevation.length][elevation[0].length];

                tau = store.getTauEstimate();

                for (int i = 0; i < slope_RAD.length; i++) {
                    for (int j = 0; j < slope_RAD[i].length; j++) {

                        surface_Radiation_midnight = 0;
                        surface_Radiation_6am = 0;
                        surface_Radiation_noon = 0;
                        surface_Radiation_6pm = 0;

                        //******************************************************************            
                        // Midnight radiation calculation
                        //******************************************************************

                        // These would be taken from lists in the full
                        azimuth_midnight_radians = Math.toRadians(azimuth_midnight);  // model but for this run just use a single 
                        zenith_midnight_radians = Math.toRadians(zenith_midnight);    // value i.e. read in a one line input met  
                        // data file  

                        // Start looping through surfaces and implement equation - this would be 
                        // nested in an array list loop eventually

                        //        slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];


                        double loop_slope1 = slope_RAD[i][j];
                        double loop_aspect1 = aspect_RAD[i][j];
                        double loop_hillshade_midnight = hillshade_midnight[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Use to set clear sky conditions (i.e. no topog. shading)

                        if (((loop_slope1 != NODATA_value) & (loop_aspect1 != NODATA_value)) & (loop_hillshade_midnight != NODATA_value)) {

                            //************** MODIFIED SURFACE RADIATION EQUATION
                            //  surface_Radiation_midnight = ((100-loop_hillshade1)/100) * (((Math.cos(zenith_midnight_radians)*Math.cos(loop_slope1))
                            //surface_Radiation_midnight = (1) * (((Math.cos(zenith_midnight_radians)*Math.cos(loop_slope1))
                            surface_Radiation_midnight = (loop_hillshade_midnight / 255.0) * (((Math.cos(zenith_midnight_radians) * Math.cos(loop_slope1))
                                    + (Math.sin(zenith_midnight_radians) * Math.sin(loop_slope1) * Math.cos(azimuth_midnight_radians - loop_aspect1)))
                                    * TOA_midnight * Math.exp(-tau / Math.cos(zenith_midnight_radians)));

                        }

                        //******************************************************************
                        // 6am radiation calculation
                        //******************************************************************

                        // These would be taken from lists in the full
                        azimuth_6am_radians = Math.toRadians(azimuth_6am);  // model but for this run just use a single 
                        zenith_6am_radians = Math.toRadians(zenith_6am);    // value i.e. read in a one line input met  
                        // data file  

                        //System.out.println("Azimuth 6am (rad) = " + azimuth_6am_radians);
                        //System.out.println("Zenith 6am (rad) = " + zenith_6am_radians);

                        //slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];

                        double loop_slope2 = slope_RAD[i][j];
                        double loop_aspect2 = aspect_RAD[i][j];
                        double loop_hillshade_6am = hillshade_6am[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Use to set clearsky conditions (i.e. no topog. shading)

                        if (((loop_slope2 != NODATA_value) & (loop_aspect2 != NODATA_value)) & (loop_hillshade_6am != NODATA_value)) {

                            //************** MODIFIED SURFACE RADIATION EQUATION
                            //  surface_Radiation_6am = ((100-loop_hillshade2)/100) * (((Math.cos(zenith_6am_radians)*Math.cos(loop_slope2))
                            //surface_Radiation_6am = (1) * (((Math.cos(zenith_6am_radians)*Math.cos(loop_slope2))
                            surface_Radiation_6am = (loop_hillshade_6am / 255.0) * (((Math.cos(zenith_6am_radians) * Math.cos(loop_slope2))
                                    + (Math.sin(zenith_6am_radians) * Math.sin(loop_slope2) * Math.cos(azimuth_6am_radians - loop_aspect2)))
                                    * TOA_6am * Math.exp(-tau / Math.cos(zenith_6am_radians)));

                        }

                        //****************************************************************** 
                        // noon radiation calculation  
                        //****************************************************************** 

                        // These would be taken from lists in the full
                        azimuth_noon_radians = Math.toRadians(azimuth_noon);  // model but for this run just use a single 
                        zenith_noon_radians = Math.toRadians(zenith_noon);    // value i.e. read in a one line input met  
                        // data file  

                        //System.out.println("Azimuth noon (rad) = " + azimuth_noon_radians);
                        //System.out.println("Zenith noon (rad) = " + zenith_noon_radians);

                        //slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];

                        double loop_slope3 = slope_RAD[i][j];
                        double loop_aspect3 = aspect_RAD[i][j];
                        double loop_hillshade_noon = hillshade_noon[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Use to set clearsky conditions (i.e. no topog. shading)

                        if (((loop_slope3 != NODATA_value) & (loop_aspect3 != NODATA_value)) & (loop_hillshade_noon != NODATA_value)) {

                            if (zenith_noon <= 84.0) { // Prevents development of rogue values from discontinuity between zenith/azimuth/TOA at low sun altitudes - note that this referes to the zenith in degrees

                                //************** MODIFIED SURFACE RADIATION EQUATION
//             surface_Radiation_noon = ((100-loop_hillshade3)/100) * (((Math.cos(zenith_noon_radians)*Math.cos(loop_slope3))   
                                //surface_Radiation_noon = (1) * (((Math.cos(zenith_noon_radians)*Math.cos(loop_slope3))
                                surface_Radiation_noon = (loop_hillshade_noon / 255.0) * (((Math.cos(zenith_noon_radians) * Math.cos(loop_slope3))
                                        + (Math.sin(zenith_noon_radians) * Math.sin(loop_slope3) * Math.cos(azimuth_noon_radians - loop_aspect3)))
                                        * TOA_noon * Math.exp(-tau / Math.cos(zenith_noon_radians)));

                            } else if (zenith_noon > 84.0) {
                                surface_Radiation_noon = 0.0;
                            }
                        }

                        //****************************************************************** 
                        // 6pm radiation calculation
                        //****************************************************************** 

                        // These would be taken from lists in the full
                        azimuth_6pm_radians = Math.toRadians(azimuth_6pm);  // model but for this run just use a single 
                        zenith_6pm_radians = Math.toRadians(zenith_6pm);    // value i.e. read in a one line input met  
                        // data file  

                        //System.out.println("Azimuth 6pm (rad) = " + azimuth_6pm_radians);
                        //System.out.println("Zenith 6pm (rad) = " + zenith_6pm_radians);

                        //        slope_RAD[i][j] = aspect_RAD[i][j] = hillshade[i][j];


                        double loop_slope4 = slope_RAD[i][j];
                        double loop_aspect4 = aspect_RAD[i][j];
                        double loop_hillshade_6pm = hillshade_6pm[i][j]; // If you want to account for topog. shading
//           double loop_hillshade = 1.0; // Use to set clearsky conditions (i.e. no topog. shading)

                        if (((loop_slope4 != NODATA_value) & (loop_aspect4 != NODATA_value)) & (loop_hillshade_6pm != NODATA_value)) {

                            //************** MODIFIED SURFACE RADIATION EQUATION

                            //surface_Radiation_6pm = (1) * (((Math.cos(zenith_6pm_radians)*Math.cos(loop_slope4))
                            surface_Radiation_6pm = (loop_hillshade_6pm / 255.0) * (((Math.cos(zenith_6pm_radians) * Math.cos(loop_slope4))
                                    + (Math.sin(zenith_6pm_radians) * Math.sin(loop_slope4) * Math.cos(azimuth_6pm_radians - loop_aspect4)))
                                    * TOA_6pm * Math.exp(-tau / Math.cos(zenith_6pm_radians)));
                        }

                        //****************************************************************** 
                        // System.out.println("Just before the if rad loops");

                        if ((elevation[i][j] == NODATA_value)) {
                            //		System.out.println("No data populated for rad");
                            surfaceRadiationSurface[i][j] = NODATA_value;
                            store.setSurfaceRadiationSurface(surfaceRadiationSurface);
                            //		System.out.println("surface_rad NODATA_value populated");
                        } else if ((elevation[i][j] != NODATA_value) & ((surface_Radiation_midnight != NODATA_value) & (surface_Radiation_6am != NODATA_value)) & ((surface_Radiation_noon != NODATA_value) & (surface_Radiation_6pm != NODATA_value))) {

                            surface_Radiation = (surface_Radiation_midnight + surface_Radiation_6am + surface_Radiation_noon + surface_Radiation_6pm) / 4.0;

                            if (surface_Radiation >= 0.000001) {
                                //                                      System.out.println("Rad >= 0.000001 populated");
                                surfaceRadiationSurface[i][j] = surface_Radiation;
                                store.setSurfaceRadiationSurface(surfaceRadiationSurface);

                            } else if ((surface_Radiation < 0.000001) & (surface_Radiation != NODATA_value)) {
                                //                                    System.out.println("Rad < 0.000001 populated");
                                surfaceRadiationSurface[i][j] = 0;
                                store.setSurfaceRadiationSurface(surfaceRadiationSurface);
                            }
                        }
                    }
                }

                //System.out.println("Surface radiation calculated and radiation surface set");

                /*
                 * Bulk flux calculation
                 */

                for (int i = 0; i < elevation.length; i++) {
                    for (int j = 0; j < elevation[i].length; j++) {

                        double Elevation_for_bulk_flux = elevation[i][j];

                        if (Elevation_for_bulk_flux != NODATA_value) {
                            lapsed_temp[i][j] = (double) LapseRate(Elevation_for_bulk_flux, temp);
                            lapsed_Ta = lapsed_temp[i][j];

                            // Calculate value of psi [INTEGRATE THIS INTO ABOVE LOOP]
                            if (lapsed_Ta >= T_tip) {
                                cTa = (c * lapsed_Ta);
                                psi = psi_min + cTa;
                                psi_surface[i][j] = psi;

                            } else if (lapsed_Ta < T_tip) {
                                psi = psi_min;
                                psi_surface[i][j] = psi;
                            }
                        } else if (Elevation_for_bulk_flux == NODATA_value) {
                            lapsed_temp[i][j] = (int) NODATA_value;
                            psi = NODATA_value;
                            psi_surface[i][j] = NODATA_value;
                        }

                        store.setPsi(psi);
                        store.setPsiSurface(psi_surface);
                        store.setLapsedTempSurface(lapsed_temp);

                    }
                }

                //System.out.println("Bulk flux calculated");

                /*
                 * Q calculation
                 */

//    double[][] Q_daily_grid = new double[elevation.length][elevation[0].length];
//    double I_for_Q;
//    double psi_for_Q;
//    double Q, Q_daily;
//    double Q, Q_daily, mean_Q;
//    double Q_counter = 0;

                for (int i = 0; i < elevation.length; i++) {
                    for (int j = 0; j < elevation[i].length; j++) {

                        // Change albedo according to surface (i.e. it changes if there is snow or ice, according to a thickness threshold

//                        if ((summer_snow_thickness[i][j] != NODATA_value) && ((summer_snow_thickness[i][j] + winter_snow_thickness[i][j]) >= threshold_snow_thickness_for_albedo)) {
//                            albedo_instance = snow_albedo;
//                            //System.out.println("Albedo set to that of snow (summer)");
//                        } else if ((summer_snow_thickness[i][j] != NODATA_value) && ((summer_snow_thickness[i][j] > 0) | (winter_snow_thickness[i][j] > 0)) && ((summer_snow_thickness[i][j] + winter_snow_thickness[i][j]) < threshold_snow_thickness_for_albedo)) {
//                            albedo_instance = snow_albedo; // This can be multiplied by a factor as in Giesen and Oerlemans 2012 or Leclercq et al. 2012 if you want to reduce the albedo
//                            //System.out.println("Albedo set to that of snow (winter)");
//                        } else if ((summer_snow_thickness[i][j] != NODATA_value) && (summer_snow_thickness[i][j] < threshold_snow_thickness_for_albedo) && (winter_snow_thickness[i][j] != NODATA_value) && (winter_snow_thickness[i][j] < threshold_snow_thickness_for_albedo) && (glacier_thickness[i][j] != NODATA_value) && (glacier_thickness[i][j] > 0)) {
//                            albedo_instance = ice_albedo;
//                            //System.out.println("Albedo set to that of ice");
//                        }

                        if ((summer_snow_thickness[i][j] != NODATA_value) && (summer_snow_thickness[i][j] > 0.0)) {

                            albedo_instance = snow_albedo;

                        } else if ((winter_snow_thickness[i][j] != NODATA_value) && (winter_snow_thickness[i][j] > 0.0)) {

                            albedo_instance = snow_albedo;

                        } else {

                            albedo_instance = ice_albedo;

                        }


                        I_for_Q = store.getSurfaceRadiationSurface()[i][j];
                        psi_for_Q = store.getPsiSurface()[i][j];

                        if ((I_for_Q != NODATA_value) & (psi_for_Q != NODATA_value)) {

                            Q = ((1 - albedo_instance) * I_for_Q) + psi_for_Q; //<<<----- This gives a value of Q per m^2 - the actual cell area (5m x 5m) or full days time has not been considered yet 
                            Q_daily = (Q * 86400) * cell_area; // Multiplies Q value by the number of seconds in a day and scales it up to the cell area (so if a 5m x 5m cell, multiply Q by 25)
                            //System.out.println("Q daily: "  + Q_daily);
                            Q_daily_grid[i][j] = Q_daily; // Sets the Q_daily value in the Q_daily_grid
                            Q_counter++; // Counts instances of Q where != NODATA_value
                            Q_total += Q_daily; // Total value of scaled up Q for a day through each full model loop

                        } else if ((I_for_Q == NODATA_value) | (psi_for_Q == NODATA_value)) {

                            Q_daily_grid[i][j] = NODATA_value;

                        }

                        store.setQdailySurface(Q_daily_grid);

                    }
                }

                mean_Q_5_x_m2 = Q_total / Q_counter; //<<---- Mean energy for a 5m x 5m cell (so units are W x 5m^-2)
                mean_Q_m2 = (Q_total / Q_counter) / 25.0; //<<<---- Mean energy for a 1m x 1m cell (so units are Wm^-2)

                //System.out.println("Q calculated: " + mean_Q_5_x_m2 + " W x 5m^-2"); //<<---- Mean energy for a 5m x 5m cell (so units are W x 5m^-2)
                // System.out.println("Q calculated: " + mean_Q_m2 + " W x m^-2"); //<<<---- Mean energy for a 1m x 1m cell (so units are Wm^-2)

                /**
                 * Surface melting.
                 *
                 * Melts through the summer snow surface, then the winter
                 * surface and then the ice surface. Accounts for remaining Q,
                 * where Q available > Q required to melt. In this way, energy
                 * is conserved and used on the next layer that can be melted.
                 */
                for (int i = 0; i < summer_snow_thickness.length; i++) {
                    for (int j = 0; j < summer_snow_thickness[i].length; j++) {

                        double Q_for_melt = Q_daily_grid[i][j];
                        double glacier_thickness_instance = glacier_thickness[i][j];
                        double winter_snow_surface_instance = winter_snow_thickness[i][j];
                        double summer_snow_surface_instance = summer_snow_thickness[i][j];

                        double summer_snow_surface_change_instance = summer_snow_surface_change[i][j];
                        double winter_snow_surface_change_instance = winter_snow_surface_change[i][j];
                        //double ice_thickness_change_surface_instance = ice_thickness_change_surface[i][j];
                        double ice_thickness_change = ice_thickness_change_surface[i][j];

                        // First loop - when there is energy, positive thickness values and Q is a positive number

                        if (((summer_snow_surface_instance != NODATA_value) && (Q_for_melt != NODATA_value)) && ((summer_snow_surface_instance > 0.0) && (Q_for_melt > 0.0))) {

                            summersnow_hit++;
                            double summer_snow_volume = summer_snow_surface_instance * cell_area;
                            double summer_snow_mass = summer_snow_volume * summer_snow_density;
                            energy_for_total_summer_snow_melt = summer_snow_mass * latent_heat_of_fusion;

                            summer_snow_surface_change_instance = (Q_for_melt / energy_for_total_summer_snow_melt) * summer_snow_surface_instance; // Calculates a ratio of available melt to required melt and multiplies by thickness to make the appropriate reduction

                            summer_snow_surface_change[i][j] = summer_snow_surface_change_instance; // Populates thickness change grid - this may allow for more change than is possible considering the actual thickness surface hence the next step

                            // Calculates the remaining energy available (i.e. used in instances where there is more energy available than there is snow to melt)

                            if ((energy_for_total_summer_snow_melt - Q_for_melt) < 0.0) // A negative value is indicative of Q_for_melt > Q required so gives you the remainding energy
                            {
                                Q_for_melt = (Q_for_melt - energy_for_total_summer_snow_melt); // Inverts the negative value to make the remainding energy positive
                            } else {
                                Q_for_melt = 0.0;
                            }

                            // Now deal with WINTER snow
                            // If snow thickness is positive and there is energy

                            if (((winter_snow_surface_instance != NODATA_value) && (Q_for_melt != NODATA_value)) && ((winter_snow_surface_instance > 0.0) && (Q_for_melt > 0.0))) {

                                summersnow_wintersnow_hit++;
                                double winter_snow_volume = winter_snow_surface_instance * cell_area;
                                double winter_snow_mass = winter_snow_volume * (winter_snow_density * snow_wind_stripping_factor);
                                    // The snow stripping factor in effect 
                                    // reduces the amount of snow present - this 
                                    // replicates the effect wind would have in 
                                    // removing the snow, not asa function of 
                                    // melting
                                energy_for_total_winter_snow_melt = winter_snow_mass * latent_heat_of_fusion;

                                winter_snow_surface_change_instance = (Q_for_melt / energy_for_total_winter_snow_melt) * winter_snow_surface_instance; // Calculates a ratio of available melt to required melt and multiplies by thickness to make the appropriate reduction

                                winter_snow_surface_change[i][j] = winter_snow_surface_change_instance; // Populates thickness change grid - this may allow for more change than is possible considering the actual thickness surface hence the next step

                                // Calculates the remaining energy available (i.e. used in instances where there is more energy available than there is snow to melt)

                                if ((energy_for_total_winter_snow_melt - Q_for_melt) < 0.0) // A negative value is indicative of Q_for_melt > Q required so gives you the remainding energy
                                {
                                    Q_for_melt = (Q_for_melt - energy_for_total_winter_snow_melt); // Inverts the negative value to make the remainding energy positive
                                } else {
                                    Q_for_melt = 0.0;
                                }

                            } // If snow thickness is equal to 0 or there is no energy
                            else if ((winter_snow_surface_instance == 0.0) | (Q_for_melt <= 0.0)) // Required so that if winter_snow_surface is already zero or there is no energy, then nothing is done
                            {
                                summersnow_no_wintersnow_hit++;
                                winter_snow_surface_change_instance = 0.0;
                                winter_snow_surface_change[i][j] = 0.0;
                            } // If snow thickness is equal to NODATA_value or there is no energy
                            else if ((winter_snow_surface_instance == NODATA_value) | (Q_for_melt == NODATA_value)) {
                                winter_snow_surface_change[i][j] = NODATA_value;
                            }

                            // Deal with ICE 

                            if (((glacier_thickness_instance != NODATA_value) && (Q_for_melt != NODATA_value)) && ((glacier_thickness_instance > 0.0) && (Q_for_melt > 0.0))) {

                                //double thickness = glacier_thickness_instance;
                                summersnow_wintersnow_ice++;
                                double ice_volume = glacier_thickness_instance * cell_area;
                                ice_mass = ice_volume * ice_density;
                                energy_for_total_melt = ice_mass * latent_heat_of_fusion;

                                ice_thickness_change = (Q_for_melt / energy_for_total_melt) * glacier_thickness_instance; // Calculates a ratio of available melt to required melt and multiplies by thickness to make the appropriate reduction


                                ice_thickness_change_surface[i][j] = ice_thickness_change; // Populates thickness change grid - this may allow for more change than is possible considering the actual thickness surface hence the next step

                                // Calculates the remaining energy available (i.e. used in instances where there is more energy available than there is snow to melt)

                                if ((energy_for_total_melt - Q_for_melt) < 0.0) // A negative value is indicative of Q_for_melt > Q required so gives you the remainding energy
                                {
                                    Q_for_melt = (Q_for_melt - energy_for_total_melt); // Inverts the negative value to make the remainding energy positive
                                } else {
                                    Q_for_melt = 0.0;
                                }
                            } // If ice thickness is equal to 0 or there is no energy
                            else if ((glacier_thickness_instance == 0.0) | (Q_for_melt <= 0.0)) // Required so that if winter_snow_surface is already zero or there is no energy, then nothing is done
                            {
                                ice_thickness_change = 0.0;
                                ice_thickness_change_surface[i][j] = 0.0;
                            } // If ice thickness is equal to NODATA_value or there is no energy
                            else if ((glacier_thickness_instance == NODATA_value) | (Q_for_melt == NODATA_value)) {
                                ice_thickness_change_surface[i][j] = NODATA_value;
                            }
                        } // Second loop - when there is energy, but summer snow thickness values are 0 -> for winter snow, thickness values > 0 and energy is positive
                        else if (((summer_snow_surface_instance != NODATA_value) && (Q_for_melt != NODATA_value)) && ((summer_snow_surface_instance == 0.0) && (Q_for_melt > 0.0))) {

                            nosummersnow_hit++;
                            //summer_snow_surface_instance = 0.0;
                            summer_snow_surface_change_instance = 0.0;
                            summer_snow_surface_change[i][j] = summer_snow_surface_change_instance;

                            // Now deal with WINTER snow
                            // If snow thickness is positive and there is energy

                            if ((winter_snow_surface_instance != NODATA_value) && (Q_for_melt != NODATA_value) && (winter_snow_surface_instance > 0.0) && (Q_for_melt > 0.0)) {

                                no_summersnow_wintersnow_hit++;
                                double winter_snow_volume = winter_snow_surface_instance * cell_area;
                                double winter_snow_mass = winter_snow_volume * (winter_snow_density * snow_wind_stripping_factor);
                                    // The snow stripping factor in effect 
                                    // reduces the amount of snow present - this 
                                    // replicates the effect wind would have in 
                                    // removing the snow, not asa function of 
                                    // melting
                                energy_for_total_winter_snow_melt = winter_snow_mass * latent_heat_of_fusion;

                                winter_snow_surface_change_instance = (Q_for_melt / energy_for_total_winter_snow_melt) * winter_snow_surface_instance; // Calculates a ratio of available melt to required melt and multiplies by thickness to make the appropriate reduction

                                winter_snow_surface_change[i][j] = winter_snow_surface_change_instance; // Populates thickness change grid - this may allow for more change than is possible considering the actual thickness surface hence the next step

                                // Calculates the remaining energy available (i.e. used in instances where there is more energy available than there is snow to melt)

                                if ((energy_for_total_winter_snow_melt - Q_for_melt) < 0.0) // A negative value is indicative of Q_for_melt > Q required so gives you the remainding energy
                                {
                                    Q_for_melt = (Q_for_melt - energy_for_total_winter_snow_melt); // Inverts the negative value to make the remainding energy positive
                                } else {
                                    Q_for_melt = 0.0;
                                }

                            } // If snow thickness is equal to 0 or there is no energy
                            else if ((winter_snow_surface_instance == 0.0) | (Q_for_melt <= 0.0)) // Required so that if winter_snow_surface is already zero or there is no energy, then nothing is done
                            {
                                no_summersnow_no_wintersnow_hit++;
                                winter_snow_surface_change_instance = 0.0;
                                winter_snow_surface_change[i][j] = 0.0;
                            } // If snow thickness is equal to NODATA_value or there is no energy
                            else if ((winter_snow_surface_instance == NODATA_value) | (Q_for_melt == NODATA_value)) {
                                winter_snow_surface_change[i][j] = NODATA_value;
                            }

                            // Deal with ICE 

                            if (((glacier_thickness_instance != NODATA_value) && (Q_for_melt != NODATA_value)) && ((glacier_thickness_instance > 0.0) && (Q_for_melt > 0.0))) {

                                //double thickness = glacier_thickness_instance;
                                no_summersnow_wintersnow_ice++;
                                double ice_volume = glacier_thickness_instance * cell_area;
                                ice_mass = ice_volume * ice_density;
                                energy_for_total_melt = ice_mass * latent_heat_of_fusion;

                                ice_thickness_change = (Q_for_melt / energy_for_total_melt) * glacier_thickness_instance; // Calculates a ratio of available melt to required melt and multiplies by thickness to make the appropriate reduction


                                ice_thickness_change_surface[i][j] = ice_thickness_change; // Populates thickness change grid - this may allow for more change than is possible considering the actual thickness surface hence the next step

                                // Calculates the remaining energy available (i.e. used in instances where there is more energy available than there is snow to melt)

                                if ((energy_for_total_melt - Q_for_melt) < 0) // A negative value is indicative of Q_for_melt > Q required so gives you the remainding energy
                                {
                                    Q_for_melt = (energy_for_total_melt - Q_for_melt) * -1; // Inverts the negative value to make the remainding energy positive
                                } else {
                                    Q_for_melt = 0.0;
                                }
                            } // If ice thickness is equal to 0 or there is no energy
                            else if ((glacier_thickness_instance == 0) | (Q_for_melt <= 0)) // Required so that if winter_snow_surface is already zero or there is no energy, then nothing is done
                            {
                                ice_thickness_change = 0.0;
                                ice_thickness_change_surface[i][j] = 0.0;
                            } // If ice thickness is equal to NODATA_value or there is no energy
                            else if ((glacier_thickness_instance == NODATA_value) | (Q_for_melt == NODATA_value)) {
                                ice_thickness_change_surface[i][j] = NODATA_value;
                            }
                        }

                        // If there is no summer or winter snow but there is ice
//                        
//                        else if(((summer_snow_surface_instance == 0.0)&&(winter_snow_surface_instance == 0.0))&&((summer_snow_surface_instance != NODATA_value)&&(winter_snow_surface_instance != NODATA_value)) && ((glacier_thickness_instance > 0.0) && (Q_for_melt > 0.0))){
//                        
//                             //double thickness = glacier_thickness_instance;
//                                double ice_volume = glacier_thickness_instance * cell_area;
//                                ice_mass = ice_volume * ice_density;
//                                energy_for_total_melt = ice_mass * latent_heat_of_fusion;
//
//                                ice_thickness_change = (Q_for_melt / energy_for_total_melt) * glacier_thickness_instance; // Calculates a ratio of available melt to required melt and multiplies by thickness to make the appropriate reduction
//
//
//                                ice_thickness_change_surface[i][j] = ice_thickness_change; // Populates thickness change grid - this may allow for more change than is possible considering the actual thickness surface hence the next step
//
//                        
//                        }
//                        
//                        
//                        // If summer snow is equal to zero
//                        
//                        else if (((summer_snow_surface_instance == NODATA_value) | (Q_for_melt == NODATA_value))) {
//
//                            summer_snow_surface_change_instance = NODATA_value;
//                            summer_snow_surface_change[i][j] = NODATA_value;
//
//                        }


                        // Update surfaces

                        //Update summer snow thickness

                        if ((summer_snow_surface_change[i][j] != NODATA_value) && (summer_snow_thickness[i][j] != NODATA_value)) {

                            if ((summer_snow_thickness[i][j] - summer_snow_surface_change[i][j]) >= 0.0) {

                                summer_snow_thickness[i][j] = summer_snow_thickness[i][j] - summer_snow_surface_change[i][j];

                            } else if ((summer_snow_thickness[i][j] - summer_snow_surface_change[i][j]) < 0.0) {

                                summer_snow_thickness[i][j] = 0.0;

                            } else if ((summer_snow_surface_change[i][j] == NODATA_value) | (summer_snow_thickness[i][j] == NODATA_value)) {

                                summer_snow_thickness[i][j] = NODATA_value;

                            }

                        }

                        store.setSummerSnowSurface(summer_snow_thickness);

                        //Update winter snow thickness

                        if ((winter_snow_surface_change[i][j] != NODATA_value) && (winter_snow_thickness[i][j] != NODATA_value)) {

                            if ((winter_snow_thickness[i][j] - winter_snow_surface_change[i][j]) >= 0.0) {

                                winter_snow_thickness[i][j] = winter_snow_thickness[i][j] - winter_snow_surface_change[i][j];

                            } else if ((winter_snow_thickness[i][j] - winter_snow_surface_change[i][j]) < 0.0) {

                                winter_snow_thickness[i][j] = 0.0;

                            } else if ((winter_snow_surface_change[i][j] == NODATA_value) | (winter_snow_thickness[i][j] == NODATA_value)) {

                                winter_snow_thickness[i][j] = NODATA_value;

                            }

                        }

                        store.setWinterSnowSurface(winter_snow_thickness);

                        //Update glacier ice thickness

                        if ((ice_thickness_change_surface[i][j] != NODATA_value) && (glacier_thickness[i][j] != NODATA_value)) {

                            if ((glacier_thickness[i][j] - ice_thickness_change_surface[i][j]) >= 0.0) {

                                glacier_thickness[i][j] = glacier_thickness[i][j] - ice_thickness_change_surface[i][j];

                            } else if ((glacier_thickness[i][j] - ice_thickness_change_surface[i][j]) < 0.0) {

                                glacier_thickness[i][j] = 0.0;

                            } else if ((ice_thickness_change_surface[i][j] == NODATA_value) | (glacier_thickness[i][j] == NODATA_value)) {

                                glacier_thickness[i][j] = NODATA_value;

                            }

                        }

                        //store.setThickness(glacier_thickness);
//                        summer_snow_thickness[i][j] = summer_snow_thickness[i][j] - summer_snow_surface_change[i][j];
//                        winter_snow_thickness[i][j] = winter_snow_thickness[i][j] - winter_snow_surface_change[i][j];
//                        glacier_thickness[i][j] = glacier_thickness[i][j] - ice_thickness_change_surface[i][j];

                        store.setSummerSnowSurface(summer_snow_thickness);
                        store.setWinterSnowSurface(winter_snow_thickness);
                        store.setThickness(glacier_thickness);

                    }
                }
                //*********************************************************************************************************************************************************	

                /**
                 * Loops through ice_thickness_surface_adjustment and populates
                 * it with values equal to glacier_thickness_intermediary -
                 * glacier_thickness. The output of this is a correct
                 * calculation of actual surface melt).
                 *
                 */
                for (int i = 0; i < ice_thickness_surface_adjustment.length; i++) {
                    for (int j = 0; j < ice_thickness_surface_adjustment[i].length; j++) {

                        if ((glacier_thickness[i][j] == NODATA_value) | (glacier_thickness_intermediary[i][j] == NODATA_value)) {
                            ice_thickness_surface_adjustment[i][j] = NODATA_value;
                        } else if ((glacier_thickness[i][j] != NODATA_value) & (glacier_thickness_intermediary[i][j] != NODATA_value)) {
                            ice_thickness_surface_adjustment[i][j] = glacier_thickness_intermediary[i][j] - glacier_thickness[i][j];

                            // Accumulation of adjustment values to give daily mean melt
                            double thickness_adjustment_instance = ice_thickness_surface_adjustment[i][j];
                            thickness_adjustment_total += thickness_adjustment_instance; // Accumulates all thickness change
                            adjustment_counter++; // Counts instances of glacier thickness where != NODATA_value

                            double ice_volume_adjustment = thickness_adjustment_instance * cell_area;
                            volume_adjustment_total += ice_volume_adjustment; // Accumulates all thickness change

                        }
                    }
                }

                DecimalFormat df_mean_melt = new DecimalFormat("0.00");
                double Daily_mean_ice_thickness_change = thickness_adjustment_total / adjustment_counter;
                double Daily_mean_ice_volume_change = volume_adjustment_total / adjustment_counter;
                //System.out.println("Daily mean thickness change: " + df_mean_melt.format(Daily_mean_ice_thickness_change) + "m");
                //System.out.println("Daily mean volume change: " + Daily_mean_ice_volume_change + "m^3");


                /**
                 * Updates elevation surface and re-sets it for use at the top
                 * of the loop
                 *
                 */
                for (int i = 0; i < elevation.length; i++) {
                    for (int j = 0; j < elevation[i].length; j++) {

                        if ((elevation[i][j] != NODATA_value) & (ice_thickness_surface_adjustment[i][j] != NODATA_value)) {
                            elevation[i][j] = elevation[i][j] - ice_thickness_surface_adjustment[i][j];
                        } else if ((elevation[i][j] == NODATA_value) | (ice_thickness_surface_adjustment[i][j] == NODATA_value)) {
                            elevation[i][j] = NODATA_value;
                        }
                    }
                }

                store.setElevation(elevation);
                elevation = store.getElevation();

                /**
                 * This bit will open a save box to save the surface radiation
                 * surface as an ASCII, using the coordinates of the originally
                 * opened up elevation surface
                 *
                 */
                if (save_arrays == 1) {

                    File f2 = new File("F:/Melt_modelling/Model_outputs_aspect_fixed/surfaceRadiationSurface_" + (int) day + "." + (int) month + "." + (int) year + ".txt");

                    FileWriter fw2;
                    DecimalFormat df2 = new DecimalFormat("#.####");

                    try {
                        BufferedWriter bw2 = new BufferedWriter(new FileWriter(f2));
                        bw2.write("ncols" + "         " + store.getOriginalNcols());
                        bw2.write(System.getProperty("line.separator"));
                        bw2.write("nrows" + "         " + store.getOriginalNrows());
                        bw2.write(System.getProperty("line.separator"));
                        bw2.write("xllcorner" + "     " + store.getOriginalXllcorner());
                        bw2.write(System.getProperty("line.separator"));
                        bw2.write("yllcorner" + "     " + store.getOriginalYllcorner());
                        bw2.write(System.getProperty("line.separator"));
                        bw2.write("cellsize" + "      " + store.getOriginalCellsize());
                        bw2.write(System.getProperty("line.separator"));
                        bw2.write("NODATA_value" + "  " + "-9999");
                        bw2.write(System.getProperty("line.separator"));

                        /**
                         * Write out the array data
                         *
                         */
                        String tempStr = "";

                        for (int a = 0; a < surfaceRadiationSurface.length; a++) {
                            for (int b = 0; b < surfaceRadiationSurface[a].length; b++) {

                                if (surfaceRadiationSurface[a][b] == NODATA_value) {

                                    bw2.write("-9999 ");
                                } else {

                                    bw2.write(df2.format(surfaceRadiationSurface[a][b]) + " ");

                                }

                            }
                            bw2.newLine();
                        }

                        bw2.close();

                    } catch (IOException ioe) {

                        ioe.printStackTrace();

                    }

                    System.out.println("surfaceRadiationSurface: " + f2.getAbsolutePath());
                } else {
                    //Do not save array
                }
                /**
                 * This bit will open a save box to save the Psi surface layer
                 * as an ASCII, using the coordinates of the originally opened
                 * up elevation surface
*
                 */
                if (save_arrays == 1) {
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


                    File f_psi_surface = new File("F:/Melt_modelling/Model_outputs_aspect_fixed/psi_" + (int) day + "." + (int) month + "." + (int) year + ".txt");

                    DecimalFormat df_psi_surface = new DecimalFormat("#.####");
//
                    try {
                        BufferedWriter bw_psi_surface = new BufferedWriter(new FileWriter(f_psi_surface));
                        bw_psi_surface.write("ncols" + "         " + store.getOriginalNcols());
                        bw_psi_surface.write(System.getProperty("line.separator"));
                        bw_psi_surface.write("nrows" + "         " + store.getOriginalNrows());
                        bw_psi_surface.write(System.getProperty("line.separator"));
                        bw_psi_surface.write("xllcorner" + "     " + store.getOriginalXllcorner());
                        bw_psi_surface.write(System.getProperty("line.separator"));
                        bw_psi_surface.write("yllcorner" + "     " + store.getOriginalYllcorner());
                        bw_psi_surface.write(System.getProperty("line.separator"));
                        bw_psi_surface.write("cellsize" + "      " + store.getOriginalCellsize());
                        bw_psi_surface.write(System.getProperty("line.separator"));
                        bw_psi_surface.write("NODATA_value" + "  " + "-9999");
                        bw_psi_surface.write(System.getProperty("line.separator"));

                        /**
                         * Write out the array data
                         *
                         */
                        //String tempStr = "";
                        for (int a = 0; a < psi_surface.length; a++) {
                            for (int b = 0; b < psi_surface[a].length; b++) {

                                if (psi_surface[a][b] == NODATA_value) {

                                    bw_psi_surface.write("-9999 ");
                                } else {

                                    bw_psi_surface.write(df_psi_surface.format(psi_surface[a][b]) + " ");

                                }

                            }
                            bw_psi_surface.newLine();
                        }

                        bw_psi_surface.close();

                    } catch (IOException ioe) {

                        ioe.printStackTrace();

                    }

                    System.out.println("Psi surface: " + f_psi_surface.getAbsolutePath());
                } else {
                    // Do not save array
                }
                /**
                 * This bit will open a save box to save the Q daily grid layer
                 * as an ASCII, using the coordinates of the originally opened
                 * up elevation surface
                 *
                 */
                if ((save_arrays == 1)|(save_arrays == 4)) {
                    File f_q_daily = new File("F:/Melt_modelling/Model_outputs_aspect_fixed/Q_daily_grid_" + (int) day + "." + (int) month + "." + (int) year + ".txt");
                    
                    DecimalFormat df_q_daily = new DecimalFormat("#.####");
//
                    try {
                        BufferedWriter bw_q_daily = new BufferedWriter(new FileWriter(f_q_daily));
                        bw_q_daily.write("ncols" + "         " + store.getOriginalNcols());
                        bw_q_daily.write(System.getProperty("line.separator"));
                        bw_q_daily.write("nrows" + "         " + store.getOriginalNrows());
                        bw_q_daily.write(System.getProperty("line.separator"));
                        bw_q_daily.write("xllcorner" + "     " + store.getOriginalXllcorner());
                        bw_q_daily.write(System.getProperty("line.separator"));
                        bw_q_daily.write("yllcorner" + "     " + store.getOriginalYllcorner());
                        bw_q_daily.write(System.getProperty("line.separator"));
                        bw_q_daily.write("cellsize" + "      " + store.getOriginalCellsize());
                        bw_q_daily.write(System.getProperty("line.separator"));
                        bw_q_daily.write("NODATA_value" + "  " + "-9999");
                        bw_q_daily.write(System.getProperty("line.separator"));

                        /**
                         * Write out the array data
                         *
                         */
                        //String tempStr = "";
                        for (int a = 0; a < Q_daily_grid.length; a++) {
                            for (int b = 0; b < Q_daily_grid[a].length; b++) {

                                if (Q_daily_grid[a][b] == NODATA_value) {

                                    bw_q_daily.write("-9999 ");
                                } else {

                                    bw_q_daily.write(df_q_daily.format(Q_daily_grid[a][b]) + " ");

                                }

                            }
                            bw_q_daily.newLine();
                        }

                        bw_q_daily.close();

                    } catch (IOException ioe) {

                        ioe.printStackTrace();

                    }

                    System.out.println("Q_daily_grid: " + f_q_daily.getAbsolutePath());
                } else {
                    // Do not save array
                }

                /**
                 * This bit will open a save box to save the updated thickness
                 * layer as an ASCII, using the coordinates of the originally
                 * opened up elevation surface
                 *
                 */
                if (save_arrays == 1) {
                    File f_glacier_thickness = new File("F:/Melt_modelling/Model_outputs_aspect_fixed/updated_thickness_" + (int) day + "." + (int) month + "." + (int) year + ".txt");

                    DecimalFormat df_glacier_thickness = new DecimalFormat("#.####");

                    try {
                        BufferedWriter bw_glacier_thickness = new BufferedWriter(new FileWriter(f_glacier_thickness));
                        bw_glacier_thickness.write("ncols" + "         " + store.getOriginalNcols());
                        bw_glacier_thickness.write(System.getProperty("line.separator"));
                        bw_glacier_thickness.write("nrows" + "         " + store.getOriginalNrows());
                        bw_glacier_thickness.write(System.getProperty("line.separator"));
                        bw_glacier_thickness.write("xllcorner" + "     " + store.getOriginalXllcorner());
                        bw_glacier_thickness.write(System.getProperty("line.separator"));
                        bw_glacier_thickness.write("yllcorner" + "     " + store.getOriginalYllcorner());
                        bw_glacier_thickness.write(System.getProperty("line.separator"));
                        bw_glacier_thickness.write("cellsize" + "      " + store.getOriginalCellsize());
                        bw_glacier_thickness.write(System.getProperty("line.separator"));
                        bw_glacier_thickness.write("NODATA_value" + "  " + "-9999");
                        bw_glacier_thickness.write(System.getProperty("line.separator"));

                        /**
                         * Write out the array data
                         *
                         */
                        //String tempStr = "";
                        for (int a = 0; a < glacier_thickness.length; a++) {
                            for (int b = 0; b < glacier_thickness[a].length; b++) {

                                if (glacier_thickness[a][b] == NODATA_value) {

                                    bw_glacier_thickness.write("-9999 ");
                                } else {

                                    bw_glacier_thickness.write(df_glacier_thickness.format(glacier_thickness[a][b]) + " ");

                                }

                            }
                            bw_glacier_thickness.newLine();
                        }

                        bw_glacier_thickness.close();

                    } catch (IOException ioe) {

                        ioe.printStackTrace();

                    }

                    System.out.println("Updated elevation: " + f_glacier_thickness.getAbsolutePath());
                    System.out.println("Glacier_thickness: " + f_glacier_thickness.getAbsolutePath());
                } else {
                    // Do not save array    
                }

                /**
                 * This bit will open a save box to save the updated elevation
                 * layer as an ASCII, using the coordinates of the originally
                 * opened up elevation surface
                 *
                 */
                if (save_arrays == 1) {
                    File f_elevation_update = new File("F:/Melt_modelling/Model_outputs_aspect_fixed/updated_elevation_" + (int) day + "." + (int) month + "." + (int) year + ".txt");

                    DecimalFormat df_elevation_update = new DecimalFormat("#.####");

                    try {
                        BufferedWriter bw_elevation_update = new BufferedWriter(new FileWriter(f_elevation_update));
                        bw_elevation_update.write("ncols" + "         " + store.getOriginalNcols());
                        bw_elevation_update.write(System.getProperty("line.separator"));
                        bw_elevation_update.write("nrows" + "         " + store.getOriginalNrows());
                        bw_elevation_update.write(System.getProperty("line.separator"));
                        bw_elevation_update.write("xllcorner" + "     " + store.getOriginalXllcorner());
                        bw_elevation_update.write(System.getProperty("line.separator"));
                        bw_elevation_update.write("yllcorner" + "     " + store.getOriginalYllcorner());
                        bw_elevation_update.write(System.getProperty("line.separator"));
                        bw_elevation_update.write("cellsize" + "      " + store.getOriginalCellsize());
                        bw_elevation_update.write(System.getProperty("line.separator"));
                        bw_elevation_update.write("NODATA_value" + "  " + "-9999");
                        bw_elevation_update.write(System.getProperty("line.separator"));

                        /**
                         * Write out the array data
                         *
                         */
                        //String tempStr = "";
                        for (int a = 0; a < elevation.length; a++) {
                            for (int b = 0; b < elevation[a].length; b++) {

                                if (Q_daily_grid[a][b] == NODATA_value) {

                                    bw_elevation_update.write("-9999 ");
                                } else {

                                    bw_elevation_update.write(df_elevation_update.format(elevation[a][b]) + " ");

                                }

                            }
                            bw_elevation_update.newLine();
                        }

                        bw_elevation_update.close();

                    } catch (IOException ioe) {

                        ioe.printStackTrace();

                    }

                    System.out.println("Updated elevation: " + f_elevation_update.getAbsolutePath());
                } else {
                    // Do not save array
                }
                
                /**
                 * This bit will open a save box to save the aspect
                 * layer as an ASCII, using the coordinates of the originally
                 * opened up elevation surface
                 *
                 */
                if (save_arrays == 3) {
                    File f_aspectSurface = new File("F:/Melt_modelling/Model_outputs_aspect_fixed/aspect_" + (int) day + "." + (int) month + "." + (int) year + ".txt");

                    DecimalFormat df_aspectSurface = new DecimalFormat("#.####");

                    try {
                        BufferedWriter bw_aspectSurface = new BufferedWriter(new FileWriter(f_aspectSurface));
                        bw_aspectSurface.write("ncols" + "         " + store.getOriginalNcols());
                        bw_aspectSurface.write(System.getProperty("line.separator"));
                        bw_aspectSurface.write("nrows" + "         " + store.getOriginalNrows());
                        bw_aspectSurface.write(System.getProperty("line.separator"));
                        bw_aspectSurface.write("xllcorner" + "     " + store.getOriginalXllcorner());
                        bw_aspectSurface.write(System.getProperty("line.separator"));
                        bw_aspectSurface.write("yllcorner" + "     " + store.getOriginalYllcorner());
                        bw_aspectSurface.write(System.getProperty("line.separator"));
                        bw_aspectSurface.write("cellsize" + "      " + store.getOriginalCellsize());
                        bw_aspectSurface.write(System.getProperty("line.separator"));
                        bw_aspectSurface.write("NODATA_value" + "  " + "-9999");
                        bw_aspectSurface.write(System.getProperty("line.separator"));

                        /**
                         * Write out the array data
                         *
                         */
                        //String tempStr = "";
                        for (int a = 0; a < aspectSurface.length; a++) {
                            for (int b = 0; b < aspectSurface[a].length; b++) {

                                if (aspectSurface[a][b] == NODATA_value) {

                                    bw_aspectSurface.write("-9999 ");
                                } else {

                                    bw_aspectSurface.write(df_aspectSurface.format(aspectSurface[a][b]) + " ");

                                }

                            }
                            bw_aspectSurface.newLine();
                        }

                        bw_aspectSurface.close();

                    } catch (IOException ioe) {

                        ioe.printStackTrace();

                    }

                    System.out.println("Aspect check: " + f_aspectSurface.getAbsolutePath());
                    
                } else {
                    // Do not save array    
                }
                
                //***********************************************

                // All other model code goes in here  

//              1. Get elevation,ice thickness, hillshade etc. - DONE
//              2. Establish other variables
//              3. Calc. slope and aspect  - DONE SLOPE|DONE ASPECT
//              4. Calc. radiation <<<--- Variables added, methods not yet copied over....
//              5. Radiation algorithm - DONE
//                        - bring in hillshade surfaces - DONE
//                        - integrate saesonal hillshade component - DONE 
//              6. Bulk flux algorithm - DONE
//              7. Calculation of Q - DONE
//              [8. Precipitation algorithm and development of snow layer] - DONE
//              9a. Melting without precip. - DONE
//              9b. Melting with precip. - DONE
//              10. Outputs (this should be somebasic stats written to a file  - DONE
//                  giving the day/month/year/mean area/mean volume/mean 
//                  thickness/mean elevation)
//              11. Update elevation and thickness surfaces (ensure that these are used in the loops, not those from store!)  - DONE
//              12. Loop back to the top and continue until end of all lists - DONE

                /*
                 * Deals with inputs for the stats file. Sorts out loop counting
                 * and also loops through the radiation, psi and lapse rate
                 * surfaces to calculate mean values for the stats file
                 */

                Loop_counter++;

                for (int i = 0; i < surfaceRadiationSurface.length; i++) {
                    for (int j = 0; j < surfaceRadiationSurface[i].length; j++) {

                        if (surfaceRadiationSurface[i][j] != NODATA_value) {
                            double Radiation_instance = surfaceRadiationSurface[i][j];
                            Radiation_total += Radiation_instance;
                            Radiation_counter++;
                        } else {
                        }

                        if (psi_surface[i][j] != NODATA_value) {
                            double Psi_instance = psi_surface[i][j];
                            Psi_total += Psi_instance;
                            Psi_counter++;
                        } else {
                        }

                        if (lapsed_temp[i][j] != NODATA_value) {
                            double Lapsed_temp_instance = lapsed_temp[i][j];
                            Lapsed_temp_total += Lapsed_temp_instance;
                            Lapsed_temp_counter++;
                        } else {
                        }
                    }
                }
                //Sort other values

                bw_stats.write(Loop_counter + ", "  + day + ", "  + month + ", "  + year + ", " + Radiation_total / Radiation_counter
                        + ", " + Psi_total / Psi_counter + ", " + mean_Q_m2 / 86400 + ", " + Lapsed_temp_total / Lapsed_temp_counter + ", " + "N/A"
                        + ", " + thickness_adjustment_total / adjustment_counter + ", "
                        + volume_adjustment_total / adjustment_counter);

                bw_stats.newLine();

                //System.out.println("Working for now....");

            }//End of while loop

            bw_stats.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        System.out.println("Model statistics file stored and saved: " + f_stats.getAbsolutePath());

        double summer_snow_thickness_after_the_snow[][] = store.getSummerSnowSurface();
        double winter_snow_thickness_after_the_snow[][] = store.getWinterSnowThickness();

        System.out.println("summersnow_hit: " + summersnow_hit);
        System.out.println("nosummersnow_hit: " + nosummersnow_hit);

        System.out.println("summersnow_wintersnow_hit: " + summersnow_wintersnow_hit);
        System.out.println("summersnow_no_wintersnow_hit: " + summersnow_no_wintersnow_hit);
        System.out.println("summersnow_wintersnow_ice: " + summersnow_wintersnow_ice);

        System.out.println("no_summersnow_wintersnow_hit: " + no_summersnow_wintersnow_hit);
        System.out.println("no_summersnow_no_wintersnow_hit: " + no_summersnow_no_wintersnow_hit);
        System.out.println("no_summersnow_wintersnow_ice: " + no_summersnow_wintersnow_ice);

        /**
         * This bit will open a save box to save the
         * summer_snow_thickness_after_the_snow layer as an ASCII, using the
         * coordinates of the originally opened up elevation surface
         *
         */
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        File f_summer_snow = new File("F:/Melt_modelling/Model_outputs_aspect_fixed/summer_snow.txt");

        DecimalFormat df_summer_snow = new DecimalFormat("#.####");

        try {
            BufferedWriter bw_summer_snow = new BufferedWriter(new FileWriter(f_summer_snow));
            bw_summer_snow.write("ncols" + "         " + store.getOriginalNcols());
            bw_summer_snow.write(System.getProperty("line.separator"));
            bw_summer_snow.write("nrows" + "         " + store.getOriginalNrows());
            bw_summer_snow.write(System.getProperty("line.separator"));
            bw_summer_snow.write("xllcorner" + "     " + store.getOriginalXllcorner());
            bw_summer_snow.write(System.getProperty("line.separator"));
            bw_summer_snow.write("yllcorner" + "     " + store.getOriginalYllcorner());
            bw_summer_snow.write(System.getProperty("line.separator"));
            bw_summer_snow.write("cellsize" + "      " + store.getOriginalCellsize());
            bw_summer_snow.write(System.getProperty("line.separator"));
            bw_summer_snow.write("NODATA_value" + "  " + "-9999");
            bw_summer_snow.write(System.getProperty("line.separator"));

            /**
             * Write out the array data
             *
             */
            String tempStr = "";

            for (int a = 0; a < summer_snow_thickness_after_the_snow.length; a++) {
                for (int b = 0; b < summer_snow_thickness_after_the_snow[a].length; b++) {

                    if (summer_snow_thickness_after_the_snow[a][b] == NODATA_value) {

                        bw_summer_snow.write("-9999 ");
                    } else {

                        bw_summer_snow.write(df_summer_snow.format(summer_snow_thickness_after_the_snow[a][b]) + " ");

                    }

                }
                bw_summer_snow.newLine();
            }

            bw_summer_snow.close();

        } catch (IOException ioe) {

            ioe.printStackTrace();

        }

        System.out.println("Summer snow accumulation surface saved: " + f_summer_snow.getAbsolutePath());

        /**
         * This bit will open a save box to save the
         * winter_snow_thickness_after_the_snow layer as an ASCII, using the
         * coordinates of the originally opened up elevation surface
         *
         */
        if (winter_snow_thickness_after_the_snow != null) {

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

            File f_winter_snow = new File("F:/Melt_modelling/Model_outputs_aspect_fixed/winter_snow.txt");
//  
            DecimalFormat df_winter_snow = new DecimalFormat("#.####");
//
            try {
                BufferedWriter bw_winter_snow = new BufferedWriter(new FileWriter(f_winter_snow));
                bw_winter_snow.write("ncols" + "         " + store.getOriginalNcols());
                bw_winter_snow.write(System.getProperty("line.separator"));
                bw_winter_snow.write("nrows" + "         " + store.getOriginalNrows());
                bw_winter_snow.write(System.getProperty("line.separator"));
                bw_winter_snow.write("xllcorner" + "     " + store.getOriginalXllcorner());
                bw_winter_snow.write(System.getProperty("line.separator"));
                bw_winter_snow.write("yllcorner" + "     " + store.getOriginalYllcorner());
                bw_winter_snow.write(System.getProperty("line.separator"));
                bw_winter_snow.write("cellsize" + "      " + store.getOriginalCellsize());
                bw_winter_snow.write(System.getProperty("line.separator"));
                bw_winter_snow.write("NODATA_value" + "  " + "-9999");
                bw_winter_snow.write(System.getProperty("line.separator"));

                /**
                 * Write out the array data
                 *
                 */
                String tempStr = "";

                for (int a = 0; a < winter_snow_thickness_after_the_snow.length; a++) {
                    for (int b = 0; b < winter_snow_thickness_after_the_snow[a].length; b++) {

                        if (winter_snow_thickness_after_the_snow[a][b] == NODATA_value) {

                            bw_winter_snow.write("-9999 ");
                        } else {

                            bw_winter_snow.write(df_winter_snow.format(winter_snow_thickness_after_the_snow[a][b]) + " ");

                        }

                    }
                    bw_winter_snow.newLine();
                }

                bw_winter_snow.close();

            } catch (IOException ioe) {

                ioe.printStackTrace();

            }

            System.out.println("Winter snow accumulation surface saved: " + f_winter_snow.getAbsolutePath());

        } else {
            System.out.println("Ordinarilly, the winter snow surface would be printed out - the input data must be summer season only");
        }
    }

    /**
     * This calculates the temperature at a point as a function of elevation
     * relative to the temperature from a meteorological station of known
     * elevation - the lapse rate value used is 0.0065 which is between the
     * generic environmental lapse rate values of 0.006 and 0.007°C/m
     * (Ballantyne, 2002)
     *
     */
    public double LapseRate(double elevation, double BaseTemp) {

        double BaseElevation = 800.0; // Elevation of meteorological station
        //double BaseTemp = store.getTemp(); // Temperature at meteorological station (needs to come from within the loop)
        double equalizedElevation = elevation - BaseElevation; // Calculates height difference between met station and pooint of interest
        double pseudoTemp = equalizedElevation * 0.0065; // Calculates temperature (celcius - increase/decrease) lapse difference
        //	according to equalizedElevation
        double lapseRate = BaseTemp - pseudoTemp; // Calculates temperature at point of interested with a lapse rate correction

        return lapseRate;

    }
}
