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
 * This is the compiled historical model, incluidng summer and winter
 * precipitation algorithms.
 * 
 * This code differs to the fixed slope version of normal 
 * "Historical_Model_Precip_winter_snow_limit.java"
 * code as all instances where hillshade has a value of 0 (midnight for most 
 * months, 6am for winter months etc.) are populate with a hillshade layer using
 * 
 * store.getHillshade_Jan_midnight()
 * 
 * This is reliant on the use of OpenConfig_historical_largeFiles.java which 
 * only sets the January midnight hillshade and doesn;t set the individual month 
 * and time zone layers where this hillshade condition is the case.
 * 
 * NB/ THIS IS A WORK AROUND AND COULD BE DEALT WITH IN A MORE EFFICIENT MANNER!
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
import java.util.ListIterator;

/**
 *
 * @author Chris
 */
public class Historical_model_slope_fixed_LARGE_FILES implements ActionListener {

    private GUIPanel panel = null;
    private Storage store = null;

    Historical_model_slope_fixed_LARGE_FILES(GUIPanel panel, Storage store) {

        this.panel = panel;
        this.store = store;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("Historical model working");
       double save_arrays = 4; // Set to 1, array saving code will be turned on 
                                // | Set to 0, array saving code will be turned 
                                // off 
                                // | Set to 4 and Q surface will be saved
                                

        //***********************************

        Iterator<Integer> iterator0 = store.getMonthFile_MAIN().iterator();
        Iterator<Integer> iterator1 = store.getYearFile_MAIN().iterator();
        Iterator<Double> iterator2 = store.getTemperatureFile().iterator();
        Iterator<Double> iterator3 = store.getPrecipFile().iterator();
        Iterator<Double> iterator4 = store.getPrecipSeasonList_MAIN().iterator();
        Iterator<Double> iterator5 = store.getTauEstimateList().iterator();

        Iterator<Integer> iterator6 = store.getYearFile_WinterPrecip().iterator();
        Iterator<Double> iterator7 = store.get_Winter_PrecipFile().iterator();
        Iterator<Double> iterator8 = store.get_Winter_MonthFile().iterator();

//        ListIterator iterator6b = store.getYearFile_WinterPrecip().listIterator();
//        ListIterator iterator7b = store.get_Winter_PrecipFile().listIterator();
//        ListIterator iterator8b = store.get_Winter_MonthFile().listIterator();

        // Loop counters 
        int Loop_counter = 0; //Must be initialized outside of loop

        System.out.println("Let the historical model.....begin!");

        //File f_stats = new File("F:/Melt_modelling/Historical_model_outputs_slope_fixed/Historical_model_run_mean_stats.txt");
        File f_stats = new File("C:/Users/Chris/Desktop/19262010/Historical_model_outputs_slope_fixed/Historical_model_run_mean_stats.txt");
        FileWriter fw_stats;
        DecimalFormat df_stats = new DecimalFormat("#.####");

        try {
            BufferedWriter bw_stats = new BufferedWriter(new FileWriter(f_stats));
            bw_stats.write("Loop number" + ", " + "Month" + ", " + "Year" + ", " + "Mean Radiation (Wm^-2)" + ", "
                    + "Mean Psi (Wm^-2)" + ", " + "Mean Q (Wm^-2)" + ", "
                    + "Mean glacier air temp. (deg C)" + ", " + "Mean snowfall (m)"
                    + ", " + "Mean surface melt (m)" + ", "
                    + "Mean volume change (m^3)");

            bw_stats.newLine();

            double NODATA_value = -9999;

            // VARIABLES TO CHANGE FOR SENSITIVITY ANALYSIS

            double ice_density = 900.0;
            double summer_snow_density = 200.0; // Taken from Paterson (1994) (p9, table 2.1)
            double winter_snow_density = 407.13; // Average density value from 2008 - 2011 snow pit analysis
            double snow_wind_stripping_factor = 0.5; //
            double psi_min = -19;
            double c = 8.4;
            double T_tip = +0.2;
            double ice_albedo = 0.39;//0.39
            double snow_albedo = 0.70;

            //double threshold_snow_thickness_for_albedo = 0.05; // Subject to change - threshold for acquiring albedo from layer beneath
            double snow_threshold = 1.5; // Temperature below which rain falls as snow
            //double fresh_snow_density = 0.400; // Relative to 1.0 for water

            System.out.println("Ice density: " + ice_density);
            System.out.println("Summer snow density: " + summer_snow_density);
            System.out.println("Winter snow density: " + winter_snow_density);
            System.out.println("Snow stripping [wind] factor : " + snow_wind_stripping_factor);
            System.out.println("Psi min: " + psi_min);
            System.out.println("Variable c: " + c);
            System.out.println("T_tip (Temp at which precip. falls as snow): " + T_tip);
            System.out.println("Ice albedo: " + ice_albedo);
            System.out.println("Snow_albedo: " + snow_albedo);
            //System.out.println("Threshold for degradation of albedo according to snow thickness:" + threshold_snow_thickness_for_albedo);

            /**
             * Create empty surfaces for the summer and winter precipitation
             * (these are populated in the iterator loops)
             */
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
            
            /*
             * Slope calculation
             */
            
                //Slope variables
                double Slope_Surface[][] = new double[elev_initial_size.length][elev_initial_size[0].length];
                double slopeA, slopeB, slopeC, slopeD, slopeE, slopeF, slopeG, slopeH, slopeI;
                double slope_dzdy, slope_dzdx;
                double slopeDegree;

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
                                    slopeA = elevation1[i - 1][j - 1];
                                } else {
                                    slopeA = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe0) {
                                slopeA = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i - 1][j] != NODATA_value)) {
                                    slopeB = elevation1[i - 1][j];
                                } else {
                                    slopeB = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe1) {
                                slopeB = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i - 1][j + 1] != NODATA_value)) {
                                    slopeC = elevation1[i - 1][j + 1];
                                } else {
                                    //error1 = ("C fail");//
                                    slopeC = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe2) {
                                //error1 = ("C fail catch"); //
                                slopeC = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i][j - 1] != NODATA_value)) {
                                    slopeD = elevation1[i][j - 1];
                                } else {
                                    slopeD = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe3) {
                                slopeD = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i][j] != NODATA_value)) {
                                    slopeE = elevation1[i][j];
                                } else {
                                    slopeE = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe4) {
                                slopeE = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i][j + 1] != NODATA_value)) {
                                    slopeF = elevation1[i][j + 1];
                                } else {
                                    slopeF = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe5) {
                                slopeF = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i + 1][j - 1] != NODATA_value)) {
                                    slopeG = elevation1[i + 1][j - 1];
                                } else {
                                    slopeG = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe6) {
                                slopeG = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i + 1][j] != NODATA_value)) {
                                    slopeH = elevation1[i + 1][j];
                                } else {
                                    slopeH = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe7) {
                                slopeH = elevation1[i][j];
                            }

                            try {
                                if ((elevation1[i + 1][j + 1] != NODATA_value)) {
                                    slopeI = elevation1[i + 1][j + 1];
                                } else {
                                    slopeI = elevation1[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe8) {
                                slopeI = elevation1[i][j];
                            }

                            slope_dzdx = (((slopeC + (2 * slopeF) + slopeI) - (slopeA + (2 * slopeD) + slopeG)) / (8 * 5));
                            slope_dzdy = (((slopeG + (2 * slopeH) + slopeI) - (slopeA + (2 * slopeB) + slopeC)) / (8 * 5));
                            slopeDegree = Math.atan(Math.sqrt((Math.pow(slope_dzdx, 2)
                                    + Math.pow(slope_dzdy, 2)))) * (180 / Math.PI);

                            Slope_Surface[i][j] = slopeDegree;

                        } else if (elevation1[i][j] == NODATA_value) {

                            Slope_Surface[i][j] = NODATA_value;
                        }
                    }
                }

                store.setSlope(Slope_Surface);
                //System.out.println("Slope calculated");


            // Begin loop through monthly file   
            while (iterator0.hasNext()) {

                System.out.println("Elevation surface uploaded");
                double elevation[][] = store.getElevation();
                store.setElevationSize(elevation);
                double Elevation_size[][] = store.getElevationSize();

                System.out.println("Thickness surface uploaded");
                double glacier_thickness[][] = store.getThickness(); //Only called once; following this, loops must use the updated thickness and elevation variables
                double glacier_thickness_temp[][] = glacier_thickness; // This gets a copy of glacier_thickness 
                store.setThickness_intermediary(glacier_thickness_temp); // This sets the copy of thickness, making a clone of it (i.e. it can't be affcted if you alter the "glacier_thickness" object
                double glacier_thickness_intermediary[][] = store.getThickness_intermediary(); //This gets the "clone" - so this will remain an unaltered thickness layer throughout the loop (unlike "glacierThickness[][]")

                double summer_snow_thickness[][] = store.getSummerSnowSurface(); // An empty array in the first iteration of this loop
                double winter_snow_thickness[][] = store.getWinterSnowSurface(); // An empty array in the first iteration of this loop
                //double WinterSnow_interpolation[][];
                double temp_winter_snow_layer[][] = new double[elevation.length][elevation[0].length];

//                //Slope variables
//                double Slope_Surface[][] = new double[Elevation_size.length][Elevation_size[0].length];
//                double slopeA, slopeB, slopeC, slopeD, slopeE, slopeF, slopeG, slopeH, slopeI;
//                double slope_dzdy, slope_dzdx;
//                double slopeDegree;

                //Aspect variables
                double aspectSurface[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double aspectA, aspectB, aspectC, aspectD, aspectE, aspectF, aspectG, aspectH, aspectI;
                double aspect_dzdy, aspect_dzdx;
                double aspect_pre;
                double aspectDegree;

                //Bulk flux variables
                double cTa;
                double psi = 0;
                double lapsed_Ta;
                double lapsed_temp[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double psi_surface[][] = new double[Elevation_size.length][Elevation_size[0].length];

                //Q calculation variables
                double albedo_instance = 0.0;
                double Q_daily_grid[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double Q_monthly_grid[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double I_for_Q;
                double psi_for_Q;
                double Q, Q_daily, Q_monthly;
                double Q_counter = 0;
                double Q_total = 0;
                double mean_Q_m2;
                double mean_Q_5_x_m2;
                double cell_area = 25.0;

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
                double surface_Radiation = 0.0;
                double surface_Radiation_midnight;
                double surface_Radiation_6am;
                double surface_Radiation_noon;
                double surface_Radiation_6pm;

                //Melt calculation variables

                double adjustment_counter = 0;
                double thickness_adjustment_total = 0;
                double volume_adjustment_total = 0;
                double ice_thickness_surface_adjustment[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double ice_thickness_change_surface[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double summer_snow_surface_change[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double winter_snow_surface_change[][] = new double[Elevation_size.length][Elevation_size[0].length];
                double latent_heat_of_fusion = 334000;
                double energy_for_total_summer_snow_melt;
                double energy_for_total_winter_snow_melt;
                double ice_mass;
                double snow_mass;
                double energy_for_total_melt;
                double Q_available;

                //Counters (zeroed at the beginning of each loop)

                double Radiation_counter = 0;
                double Radiation_total = 0;
                double Psi_counter = 0;
                double Psi_total = 0;
                double Lapsed_temp_counter = 0;
                double Lapsed_temp_total = 0;
                double Snowfall_counter = 0;
                double Snowfall_total = 0;

                //Winter precip. variables
//                     Iterator<Integer> iterator6 = store.getYearFile_WinterPrecip().iterator();
//        Iterator<Double> iterator7 = store.get_Winter_PrecipFile().iterator();
//        Iterator<Double> iterator8 = store.get_Winter_MonthFile().iterator();
//                int year_winter_precip = iterator6.next();
//                double snow_thickness_winter_precip = iterator7.next();
//                double month_winter_precip = iterator8.next();

                int month = (int) iterator0.next();
                int year = (int) iterator1.next();
                double temp = iterator2.next();
                store.setTemp(temp);
                double precip = iterator3.next(); // This is the daily total precipitation in metres - check the input data file with the raw if you are unsure
                double precip_season = iterator4.next();

                System.out.println(month + "/" + year);
                System.out.println("Temp: " + temp);
                System.out.println("Precipitation (m): " + precip);
                System.out.println("Precipitation season: " + precip_season);

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
                    System.out.println("Summer precipitation");

                    /*
                     * Lapse rate calculator
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
                    System.out.println("Precip. value for use in summer precip. algorithm: " + precip + " m");
                    System.out.println("AWS temp. value for use in summer precip. algorithm: " + temp + "°C");
                    System.out.println("Summer snow density value for use in summer precip. algorithm: " + summer_snow_density);

                    for (int i = 0; i < summer_snow_thickness.length; i++) {
                        for (int j = 0; j < summer_snow_thickness[i].length; j++) {

                            if (((lapsed_temp[i][j] != NODATA_value) && (precip != NODATA_value)) && ((precip != 0) && (lapsed_temp[i][j] <= snow_threshold))) // If less than or equal to threshold, snow amount is equal to precip * fresh snow density
                            {
                                summer_snow_thickness[i][j] += (precip / (summer_snow_density / 100)); // Increase the value of the existing cell by new snow fall
                                store.setSummerSnowSurface(summer_snow_thickness);
                            } else if (((lapsed_temp[i][j] != NODATA_value) && (precip != NODATA_value)) && (lapsed_temp[i][j] > snow_threshold)) // This simulates rain which flows off the surface
                            {
                                summer_snow_thickness[i][j] += 0;  // Increase the value of the existing cell by nothing (could just do nothing...)
                                store.setSummerSnowSurface(summer_snow_thickness);

                            } else if ((lapsed_temp[i][j] != NODATA_value) && (precip == NODATA_value)) // There are some instances where the AWS didn't work so this is pertinent
                            {
                                summer_snow_thickness[i][j] += 0;
                                store.setSummerSnowSurface(summer_snow_thickness);
                            } else if (lapsed_temp[i][j] == NODATA_value) {
                                summer_snow_thickness[i][j] = NODATA_value;
                                store.setSummerSnowSurface(summer_snow_thickness);
                            }
                        }
                    }

                    //store.setSummerSnowSurface(summer_snow_thickness);
                } //**************Winter precipitation**************
                //else if (((precip_season == 0.0) && (year == year_winter_precip)) && (month == month_winter_precip)) {
                else if (precip_season == 0.0) {

                    System.out.println("Winter precipitation");

                    int year_winter_precip = 0;
                    double snow_thickness_winter_precip = 0.0;
                    double month_winter_precip = 0.0;
                    double winter_depth_test = 0.0;
                    
                    //int year_winter_precip = iterator6.next();
                    //double snow_thickness_winter_precip = iterator7.next();
                    //double month_winter_precip = iterator8.next();

                    //int year_winter_precip = 2008;
                    //double snow_thickness_winter_precip = 2.567;
                    //double month_winter_precip = 7;

                    if (((month == 1) | (month == 2)) | ((month == 3) | (month == 4)) | (month == 5)) {
                        System.out.println("Use month count of year");
                        // Loop through winter_precip_list

                        for (int i = 0; i < store.getYearFile_WinterPrecip().size(); i++) {

                            int test_year = store.getYearFile_WinterPrecip().get(i);
                            winter_depth_test = store.get_Winter_PrecipFile().get(i);
                            double test_month = store.get_Winter_MonthFile().get(i);
//                            System.out.println("Start test loop (1)");
//                            System.out.println("test_year: " + test_year);
//                            System.out.println("winter_depth_test: " + winter_depth_test);
//                            System.out.println("test_month: " + test_month);
//                            System.out.println("End test loop (1)");

                            if (year == test_year) {
                                System.out.println("Bazinga years equal");
                                System.out.println("Year of main loop: " + year);
                                System.out.println("Year in winter precip. loop: " + test_year);
                                snow_thickness_winter_precip = winter_depth_test;
                                month_winter_precip = test_month;
                                System.out.println("winter_depth_test: " + winter_depth_test);
                                System.out.println("month_winter_precip: " + month_winter_precip);
                            }
                            else 
                            {
                                // Do nothing so loop again
                            }

                        }

                    } else if (((month == 9) | (month == 10)) | ((month == 11) | (month == 12))) {
                        System.out.println("Use month count of following year");
                        // Loop through winter_precip_list

                        for (int i = 0; i < store.getYearFile_WinterPrecip().size(); i++) {

                            int test_year = store.getYearFile_WinterPrecip().get(i);
                            winter_depth_test = store.get_Winter_PrecipFile().get(i);
                            double test_month = store.get_Winter_MonthFile().get(i);
//                            System.out.println("Start test loop (2)");
//                            System.out.println("test_year: " + test_year);
//                            System.out.println("winter_depth_test: " + winter_depth_test);
//                            System.out.println("test_month: " + test_month);
//                            System.out.println("End test loop (2)");

                            if ((year + 1) == test_year) {
                                System.out.println("Bazinga year +1");
                                System.out.println("Year of main loop: " + year);
                                System.out.println("Year in winter precip. loop: " + test_year);
                                snow_thickness_winter_precip = winter_depth_test;
                                month_winter_precip = test_month;
                                System.out.println("winter_depth_test: " + winter_depth_test);
                                System.out.println("month_winter_precip: " + month_winter_precip);
                            } 
                            else 
                            {
                                // Do nothing so loop again
                            }

                        }

                    }

                    System.out.println("Out of while loops now");
                    System.out.println("snow_thickness_winter_precip: " + snow_thickness_winter_precip);
                    System.out.println("month_winter_precip: " + month_winter_precip);

                    //(1) Zeros the snow thickness surface (summer snow at the end of 
                    //summer is assumed to be a part of the winter snow pack that was 
                    //measured in the field at the end of march and is tehrefore already 
                    //accounted for in the daily winter snowfall algorithm

                    //(1b) [Possible addition] Maybe zero the winter snow pack....

                    for (int i = 0; i < summer_snow_thickness.length; i++) {
                        for (int j = 0; j < summer_snow_thickness[i].length; j++) {

                            if (summer_snow_thickness[i][j] != NODATA_value) {

                                summer_snow_thickness[i][j] = 0.0;
                                store.setSummerSnowSurface(summer_snow_thickness);

                            } else if (summer_snow_thickness[i][j] == NODATA_value) {

                                summer_snow_thickness[i][j] = NODATA_value;
                                store.setSummerSnowSurface(summer_snow_thickness);
                            }
                        }
                    }

                    //(2) Calculate mean monthly winter snowfall
                    //double snow_to_accumulate = (snow_thickness_winter_precip/month_winter_precip);

                    System.out.println("Main model loop: " + month + "/" + year);
                    System.out.println("Winter precip: " + month_winter_precip + "/" + year_winter_precip);
                    System.out.println("Total snow thickness:" + snow_thickness_winter_precip);
                    //System.out.println("Mean snow thickness:" + snow_to_accumulate);

                    //(3) Distribute monthly snow across surface as a function 
                    // of elevation using:
                    // [y = -28.39 + 0.05252x - 0.00002181x^2] * snow_to_accumulate 
                    // where x is elevation
                    //
                    // NOTE: IF ELEVATION DISTRIBUTED SNOW THICKNESS IS LESS 
                    // THAN 1.0M, IT IS SET TO 1.0M

                    for (int i = 0; i < winter_snow_thickness.length; i++) {
                        for (int j = 0; j < winter_snow_thickness[i].length; j++) {
                            
                            double y = 0.0;
                            if (winter_snow_thickness[i][j] != NODATA_value) {

                                double elev_instance_x = elevation[i][j];
                                double regressmean_yearmean_diff = (2.854751 - snow_thickness_winter_precip); 
                                // 2.854751 is the mean on which the regression 
                                // is based - the difference between this mean 
                                // and the year specific mean is required to 
                                // correct the regression to the year to which 
                                // it is applied
                                
                                double snow_thickness_limit = (-28.39 + (0.05252 * 964.331943)) - (0.00002181 * (964.331943 * 964.331943)) - regressmean_yearmean_diff;
                                // where snow thickness values fall below 
                                // predicted snow depth as calculated for the 
                                // minimum elevation (964.3m) from which the 
                                // regression curve was created (with the 
                                // addition/subtraction of the difference in 
                                // means), snow thickness will be set to this 
                                // calculated value. In implementing this 
                                // method, snow thickness values will have a 
                                // finite limit.
                                                 
                                if(regressmean_yearmean_diff >= 0) // i.e. ANS < Glacier mean
                                {
                                    // Add on the mean diffs to correct the regression to the new mean
                                    y = (-28.39 + (0.05252 * elev_instance_x)) - (0.00002181 * (elev_instance_x * elev_instance_x)) - regressmean_yearmean_diff;
                                }
                                else if(regressmean_yearmean_diff < 0) // i.e. ANS > Glacier mean
                                {
                                    // Removes the minus sign
                                    regressmean_yearmean_diff = (regressmean_yearmean_diff * -1); 
                                    
                                    // Add on the mean diffs to correct the regression to the new mean
                                    y = (-28.39 + (0.05252 * elev_instance_x)) - (0.00002181 * (elev_instance_x * elev_instance_x)) + regressmean_yearmean_diff;    
                                }
                                
                                //double elev_dependent_snow_acummulation = y;

                                if (y >= 1.0) {
                                    
                                    // Prevents exceeding winter snow thickness total at a given elevation.
                                    //
                                    // NOTE: the cap is the total measured snowfall lapsed up to the elevation
                                    // i.e. (snow_thickness_winter_precip*y) 
                                    // and NOT "elev_dependent_snow_acummulation" ITSELF (this is just a mean addition)
                                    //
                                    // This caps snow in a similar way to zeroing any previous season 
                                    // winter snow at the beginning of a new winter season.
                                    
                                    if(winter_snow_thickness[i][j] < y)
                                    {
                                    winter_snow_thickness[i][j] += (y/month_winter_precip); // If less than the elevation corrected total, add on a monthly portion of the total
                                    store.setWinterSnowSurface(winter_snow_thickness);
                                    }
                                    else if(winter_snow_thickness[i][j] > y)
                                    {
                                    winter_snow_thickness[i][j] = y; // If it's going to be greater than the elevation corrected total, it equals the total
                                    store.setWinterSnowSurface(winter_snow_thickness);
                                    }
                                    
                                }
                                else if (y < snow_thickness_limit) 
                                {
                                    winter_snow_thickness[i][j] += (snow_thickness_limit/month_winter_precip); // Adds a monthly portion of snow_thickness_limit (according to number of winter months)
                                    store.setWinterSnowSurface(winter_snow_thickness);
                                }
                                else if (y <= 0.0)
                                {
                                    winter_snow_thickness[i][j] += (snow_thickness_limit/month_winter_precip); // Adds a monthly portion of snow_thickness_limit (according to number of winter months)
                                    store.setWinterSnowSurface(winter_snow_thickness);
                                }

                                store.setWinterSnowSurface(winter_snow_thickness);

                            } else if (winter_snow_thickness[i][j] == NODATA_value) {

                                winter_snow_thickness[i][j] = NODATA_value;
                                store.setWinterSnowSurface(winter_snow_thickness);
                            }
                        }
                    }
                }

                /**
                 * Loops through the WinterSnowSurface layer and calculates mean
                 * total accumulated thickness on a given day
                 */
                double winter_snow_thickness_total = 0; // Has to be done here. If set as 0 outide the loop the total will grow forever
                double winter_snow_thickness_counter = 0; // Has to be done here. If set as 0 outide the loop the total will grow forever

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
                System.out.println("Mean accumulated winter snowfall at this point: " + winter_snow_thickness_total / winter_snow_thickness_counter + "m");
                store.setWinterSnowThickness(winter_snow_thickness);


                if (save_arrays == 1) {

                    /**
                     * This bit will open a save box to save the monthly psi
                     * surface as an ASCII, using the coordinates of the
                     * originally opened up elevation surface
                     *
                     */
                    File f_summer_precip = new File("F:/Melt_modelling/Historical_model_outputs_slope_fixed/summer_precip_" + month + "_" + year + ".txt");

                    DecimalFormat df_summer_precip = new DecimalFormat("#.####");

                    try {
                        BufferedWriter bw_summer_precip = new BufferedWriter(new FileWriter(f_summer_precip));
                        bw_summer_precip.write("ncols" + "         " + store.getOriginalNcols());
                        bw_summer_precip.write(System.getProperty("line.separator"));
                        bw_summer_precip.write("nrows" + "         " + store.getOriginalNrows());
                        bw_summer_precip.write(System.getProperty("line.separator"));
                        bw_summer_precip.write("xllcorner" + "     " + store.getOriginalXllcorner());
                        bw_summer_precip.write(System.getProperty("line.separator"));
                        bw_summer_precip.write("yllcorner" + "     " + store.getOriginalYllcorner());
                        bw_summer_precip.write(System.getProperty("line.separator"));
                        bw_summer_precip.write("cellsize" + "      " + store.getOriginalCellsize());
                        bw_summer_precip.write(System.getProperty("line.separator"));
                        bw_summer_precip.write("NODATA_value" + "  " + "-9999");
                        bw_summer_precip.write(System.getProperty("line.separator"));

                        /**
                         * Write out the array data
                         *
                         */
                        String tempStr = "";

                        for (int a = 0; a < summer_snow_thickness.length; a++) {
                            for (int b = 0; b < summer_snow_thickness[a].length; b++) {

                                if (summer_snow_thickness[a][b] == NODATA_value) {

                                    bw_summer_precip.write("-9999 ");
                                } else {

                                    bw_summer_precip.write(df_summer_precip.format(summer_snow_thickness[a][b]) + " ");

                                }

                            }
                            bw_summer_precip.newLine();
                        }

                        bw_summer_precip.close();

                    } catch (IOException ioe) {

                        ioe.printStackTrace();

                    }

                    System.out.println("Summer snow thickness surface for " + month + "/" + year + " saved: " + f_summer_precip.getAbsolutePath());
                } else {
                    // Do not save arrays
                }

                if (save_arrays == 1) {

                    /**
                     * This bit will open a save box to save the monthly psi
                     * surface as an ASCII, using the coordinates of the
                     * originally opened up elevation surface
                     *
                     */
                    File f_winter_precip = new File("F:/Melt_modelling/Historical_model_outputs_slope_fixed/winter_precip_" + month + "_" + year + ".txt");

                    DecimalFormat df_winter_precip = new DecimalFormat("#.####");

                    try {
                        BufferedWriter bw_winter_precip = new BufferedWriter(new FileWriter(f_winter_precip));
                        bw_winter_precip.write("ncols" + "         " + store.getOriginalNcols());
                        bw_winter_precip.write(System.getProperty("line.separator"));
                        bw_winter_precip.write("nrows" + "         " + store.getOriginalNrows());
                        bw_winter_precip.write(System.getProperty("line.separator"));
                        bw_winter_precip.write("xllcorner" + "     " + store.getOriginalXllcorner());
                        bw_winter_precip.write(System.getProperty("line.separator"));
                        bw_winter_precip.write("yllcorner" + "     " + store.getOriginalYllcorner());
                        bw_winter_precip.write(System.getProperty("line.separator"));
                        bw_winter_precip.write("cellsize" + "      " + store.getOriginalCellsize());
                        bw_winter_precip.write(System.getProperty("line.separator"));
                        bw_winter_precip.write("NODATA_value" + "  " + "-9999");
                        bw_winter_precip.write(System.getProperty("line.separator"));

                        /**
                         * Write out the array data
                         *
                         */
                        String tempStr = "";

                        for (int a = 0; a < winter_snow_thickness.length; a++) {
                            for (int b = 0; b < winter_snow_thickness[a].length; b++) {

                                if (winter_snow_thickness[a][b] == NODATA_value) {

                                    bw_winter_precip.write("-9999 ");
                                } else {

                                    bw_winter_precip.write(df_winter_precip.format(winter_snow_thickness[a][b]) + " ");

                                }

                            }
                            bw_winter_precip.newLine();
                        }

                        bw_winter_precip.close();

                    } catch (IOException ioe) {

                        ioe.printStackTrace();

                    }

                    System.out.println("Winter snow thickness surface for " + month + "/" + year + " saved: " + f_winter_precip.getAbsolutePath());
                } else {
                    // Do not save arrays
                }

                /*
                 * Aspect calculation
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
                                    aspectA = elevation[i - 1][j - 1];
                                } else {
                                    aspectA = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe0) {
                                aspectA = elevation[i][j];
                            }

                            try {
                                if ((elevation[i - 1][j] != NODATA_value)) {
                                    aspectB = elevation[i - 1][j];
                                } else {
                                    aspectB = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe1) {
                                aspectB = elevation[i][j];
                            }

                            try {
                                if ((elevation[i - 1][j + 1] != NODATA_value)) {
                                    aspectC = elevation[i - 1][j + 1];
                                } else {
                                    //error1 = ("C fail");//
                                    aspectC = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe2) {
                                //error1 = ("C fail catch"); //
                                aspectC = elevation[i][j];
                            }

                            try {
                                if ((elevation[i][j - 1] != NODATA_value)) {
                                    aspectD = elevation[i][j - 1];
                                } else {
                                    aspectD = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe3) {
                                aspectD = elevation[i][j];
                            }

                            try {
                                if ((elevation[i][j] != NODATA_value)) {
                                    aspectE = elevation[i][j];
                                } else {
                                    aspectE = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe4) {
                                aspectE = elevation[i][j];
                            }

                            try {
                                if ((elevation[i][j + 1] != NODATA_value)) {
                                    aspectF = elevation[i][j + 1];
                                } else {
                                    aspectF = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe5) {
                                aspectF = elevation[i][j];
                            }

                            try {
                                if ((elevation[i + 1][j - 1] != NODATA_value)) {
                                    aspectG = elevation[i + 1][j - 1];
                                } else {
                                    aspectG = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe6) {
                                aspectG = elevation[i][j];
                            }

                            try {
                                if ((elevation[i + 1][j] != NODATA_value)) {
                                    aspectH = elevation[i + 1][j];
                                } else {
                                    aspectH = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe7) {
                                aspectH = elevation[i][j];
                            }

                            try {
                                if ((elevation[i + 1][j + 1] != NODATA_value)) {
                                    aspectI = elevation[i + 1][j + 1];
                                } else {
                                    aspectI = elevation[i][j];
                                }
                            } catch (ArrayIndexOutOfBoundsException aiobe8) {
                                aspectI = elevation[i][j];
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

                /*
                 * Bulk flux calculation
                 */
                System.out.println("Temp value used in bulk flux equation: " + temp);

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

                System.out.println("Bulk flux surfce created for " + month + "/" + year);

                if (save_arrays == 1) {

                    /**
                     * This bit will open a save box to save the monthly psi
                     * surface as an ASCII, using the coordinates of the
                     * originally opened up elevation surface
                     *
                     */
                    File f_monthly_psi = new File("F:/Melt_modelling/Historical_model_outputs_slope_fixed/monthly_psi_" + month + "_" + year + ".txt");

                    DecimalFormat df_monthly_psi = new DecimalFormat("#.####");

                    try {
                        BufferedWriter bw_monthly_psi = new BufferedWriter(new FileWriter(f_monthly_psi));
                        bw_monthly_psi.write("ncols" + "         " + store.getOriginalNcols());
                        bw_monthly_psi.write(System.getProperty("line.separator"));
                        bw_monthly_psi.write("nrows" + "         " + store.getOriginalNrows());
                        bw_monthly_psi.write(System.getProperty("line.separator"));
                        bw_monthly_psi.write("xllcorner" + "     " + store.getOriginalXllcorner());
                        bw_monthly_psi.write(System.getProperty("line.separator"));
                        bw_monthly_psi.write("yllcorner" + "     " + store.getOriginalYllcorner());
                        bw_monthly_psi.write(System.getProperty("line.separator"));
                        bw_monthly_psi.write("cellsize" + "      " + store.getOriginalCellsize());
                        bw_monthly_psi.write(System.getProperty("line.separator"));
                        bw_monthly_psi.write("NODATA_value" + "  " + "-9999");
                        bw_monthly_psi.write(System.getProperty("line.separator"));

                        /**
                         * Write out the array data
                         *
                         */
                        String tempStr = "";

                        for (int a = 0; a < psi_surface.length; a++) {
                            for (int b = 0; b < psi_surface[a].length; b++) {

                                if (psi_surface[a][b] == NODATA_value) {

                                    bw_monthly_psi.write("-9999 ");
                                } else {

                                    bw_monthly_psi.write(df_monthly_psi.format(psi_surface[a][b]) + " ");

                                }

                            }
                            bw_monthly_psi.newLine();
                        }

                        bw_monthly_psi.close();

                    } catch (IOException ioe) {

                        ioe.printStackTrace();

                    }

                    System.out.println("Monthly radiation surface for " + month + "/" + year + " saved: " + f_monthly_psi.getAbsolutePath());
                } else {
                    // Do not save arrays
                }

/**
                 * Hillshade and slope/aspect radian conversion - preliminary to
                 * the radiation algorithm
                 */
                if (month == 1.0) {

                    //hillshade = store.getHillshade_Jan();
                    hillshade_midnight = store.getHillshade_Jan_midnight();
                    hillshade_6am = store.getHillshade_Jan_midnight();
                    hillshade_noon = store.getHillshade_Jan_noon();
                    hillshade_6pm = store.getHillshade_Jan_midnight();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
                        //System.out.println("January hillshade uploaded into model");
                        //System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("January hillshade not uploaded");
                    }

                } else if (month == 2.0) {

                    //hillshade = store.getHillshade_Feb();
                    hillshade_midnight = store.getHillshade_Jan_midnight();
                    hillshade_6am = store.getHillshade_Jan_midnight();
                    hillshade_noon = store.getHillshade_Feb_noon();
                    hillshade_6pm = store.getHillshade_Jan_midnight();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
                        //System.out.println("February hillshade uploaded into model");
                        //System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("February hillshade not uploaded");
                    }
                } else if (month == 3.0) {

                    //hillshade = store.getHillshade_March();
                    hillshade_midnight = store.getHillshade_Jan_midnight();
                    hillshade_6am = store.getHillshade_Jan_midnight();
                    hillshade_noon = store.getHillshade_March_noon();
                    hillshade_6pm = store.getHillshade_Jan_midnight();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("March hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("March hillshade not uploaded");
                    }

                } else if (month == 4.0) {

                    //hillshade = store.getHillshade_April();
                    hillshade_midnight = store.getHillshade_Jan_midnight();
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
                    hillshade_midnight = store.getHillshade_Jan_midnight();
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
                    hillshade_midnight = store.getHillshade_Jan_midnight();
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
                    hillshade_midnight = store.getHillshade_Jan_midnight();
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
                    hillshade_midnight = store.getHillshade_Jan_midnight();
                    hillshade_6am = store.getHillshade_Jan_midnight();
                    hillshade_noon = store.getHillshade_Oct_noon();
                    hillshade_6pm = store.getHillshade_Jan_midnight();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("October hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("October hillshade not uploaded");
                    }

                } else if (month == 11.0) {

                    //hillshade = store.getHillshade_Nov();
                    hillshade_midnight = store.getHillshade_Jan_midnight();
                    hillshade_6am = store.getHillshade_Jan_midnight();
                    hillshade_noon = store.getHillshade_Nov_noon();
                    hillshade_6pm = store.getHillshade_Jan_midnight();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("November hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("November hillshade not uploaded");
                    }

                } else if (month == 12.0) {

                    //hillshade = store.getHillshade_Dec();
                    hillshade_midnight = store.getHillshade_Jan_midnight();
                    hillshade_6am = store.getHillshade_Jan_midnight();
                    hillshade_noon = store.getHillshade_Jan_midnight();
                    hillshade_6pm = store.getHillshade_Jan_midnight();

                    if (hillshade_midnight != null | hillshade_6am != null | hillshade_noon != null | hillshade_6pm != null) {
//   System.out.println("December hillshade uploaded into model");
//   System.out.println("Month: " + month);
                    } else if (hillshade_midnight == null | hillshade_6am == null | hillshade_noon == null | hillshade_6pm == null) {
                        System.out.println("December hillshade not uploaded");
                    }
                }

                //Gets the calculated slope and aspect surfaces   

                slope = store.getSlope(); // slope surface array
                aspect = store.getAspect(); // aspect surface array

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

                System.out.println("Just before radiation loop");

                /**
                 * Internal Radiation loop: during each month loop, this daily
                 * radiation loop is instigated.
                 *
                 * This loop first accesses all of the lists created upon upload
                 * of the radiation input file. Where the month and year values
                 * from the radiation lists match the month and year of the
                 * overall monthly time step loop (starting somewhere up above
                 * and in which this secondary iteration loop exists), radiation
                 * calculations are carried out for each day. The mean daily
                 * radiation amount is calculated and totaled within the array
                 * (i.e. each loop, the current loops mean daily radiation is
                 * summed with the last value present at the same position in
                 * the array). At the end of the radiation loop, the cells of
                 * the array are divided by the number of days (which is
                 * represented by the loop counter of radiation loop, to give a
                 * monthly mean.
                 *
                 * Required prior to loop initiation: - Radiation loop counter
                 * to be set to 0 - MonthlyRadiation total to be set to 0
                 *
                 */
                double radiation_loop_counter = 0;
                double monthlyRadiationTotal = 0;
                surfaceRadiationSurface = new double[elevation.length][elevation[0].length];

                // Read in radiation iterators
                // Implement Rdaiation code
                // When month_radiation == month and year_radiation == year, get next using the day as the iterator

                Iterator<Integer> iteratora = store.getDayFile_Radiation().iterator();
                Iterator<Integer> iteratorb = store.getMonthFile_Radiation().iterator();
                Iterator<Integer> iteratorc = store.getYearFile_Radiation().iterator();

                Iterator<Double> iteratord = store.getTOA_midnight_list().iterator();
                Iterator<Double> iteratore = store.getZenith_midnight_list().iterator();
                Iterator<Double> iteratorf = store.getAzimuth_midnight_list().iterator();

                Iterator<Double> iteratorg = store.getTOA_6am_list().iterator();
                Iterator<Double> iteratorh = store.getZenith_6am_list().iterator();
                Iterator<Double> iteratori = store.getAzimuth_6am_list().iterator();

                Iterator<Double> iteratorj = store.getTOA_noon_list().iterator();
                Iterator<Double> iteratork = store.getZenith_noon_list().iterator();
                Iterator<Double> iteratorl = store.getAzimuth_noon_list().iterator();

                Iterator<Double> iteratorm = store.getTOA_6pm_list().iterator();
                Iterator<Double> iteratorn = store.getZenith_6pm_list().iterator();
                Iterator<Double> iteratoro = store.getAzimuth_6pm_list().iterator();

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

                while (iteratora.hasNext()) {

                    int day_radiation = iteratora.next();
                    int month_radiation = iteratorb.next();
                    int year_radiation = iteratorc.next();

                    double TOA_midnight = iteratord.next();
                    double zenith_midnight = iteratore.next();
                    double azimuth_midnight = iteratorf.next();

                    double TOA_6am = iteratorg.next();
                    double zenith_6am = iteratorh.next();
                    double azimuth_6am = iteratori.next();

                    double TOA_noon = iteratorj.next();
                    double zenith_noon = iteratork.next();
                    double azimuth_noon = iteratorl.next();

                    double TOA_6pm = iteratorm.next();
                    double zenith_6pm = iteratorn.next();
                    double azimuth_6pm = iteratoro.next();


                    if ((month_radiation == month) && (year_radiation == year)) {
                        //System.out.println("Radiation algorithm reached (" + month_radiation + "/" + year_radiation + ")");
                        radiation_loop_counter++;
                        //System.out.println("Rad loop number: " + radiation_loop_counter);

                        //surfaceRadiationSurface = new double[elevation.length][elevation[0].length];

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

                                    if (zenith_midnight <= 84.0) { // Prevents development of rogue values from discontinuity between zenith/azimuth/TOA at low sun altitudes - note that this referes to the zenith in degrees


                                        //************** MODIFIED SURFACE RADIATION EQUATION
                                        //  surface_Radiation_midnight = ((100-loop_hillshade1)/100) * (((Math.cos(zenith_midnight_radians)*Math.cos(loop_slope1))
                                        //surface_Radiation_midnight = (1) * (((Math.cos(zenith_midnight_radians)*Math.cos(loop_slope1))
                                        surface_Radiation_midnight = (loop_hillshade_midnight / 255.0) * (((Math.cos(zenith_midnight_radians) * Math.cos(loop_slope1))
                                                + (Math.sin(zenith_midnight_radians) * Math.sin(loop_slope1) * Math.cos(azimuth_midnight_radians - loop_aspect1)))
                                                * TOA_midnight * Math.exp(-tau / Math.cos(zenith_midnight_radians)));


                                        // System.out.println("Midnight surface rad: " + surface_Radiation_midnight);
                                    } else if (zenith_midnight > 84.0) {

                                        surface_Radiation_midnight = 0.0;

                                        //System.out.println("Noon surface rad: " + surface_Radiation_noon);

                                    }

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

                                    if (zenith_6am <= 84.0) { // Prevents development of rogue values from discontinuity between zenith/azimuth/TOA at low sun altitudes - note that this referes to the zenith in degrees

                                        //************** MODIFIED SURFACE RADIATION EQUATION
                                        //  surface_Radiation_6am = ((100-loop_hillshade2)/100) * (((Math.cos(zenith_6am_radians)*Math.cos(loop_slope2))
                                        //surface_Radiation_6am = (1) * (((Math.cos(zenith_6am_radians)*Math.cos(loop_slope2))
                                        surface_Radiation_6am = (loop_hillshade_6am / 255.0) * (((Math.cos(zenith_6am_radians) * Math.cos(loop_slope2))
                                                + (Math.sin(zenith_6am_radians) * Math.sin(loop_slope2) * Math.cos(azimuth_6am_radians - loop_aspect2)))
                                                * TOA_6am * Math.exp(-tau / Math.cos(zenith_6am_radians)));

                                        //System.out.println("6am surface rad: " + surface_Radiation_6am);
                                    } else if (zenith_6am > 84.0) {
                                        surface_Radiation_6am = 0.0;

                                        //System.out.println("Noon surface rad: " + surface_Radiation_noon);

                                    }

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

                                        //System.out.println("Noon surface rad: " + surface_Radiation_noon);


                                    } else if (zenith_noon > 84.0) {
                                        surface_Radiation_noon = 0.0;

                                        //System.out.println("Noon surface rad: " + surface_Radiation_noon);

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


                                    if (zenith_6pm <= 84.0) { // Prevents development of rogue values from discontinuity between zenith/azimuth/TOA at low sun altitudes - note that this referes to the zenith in degrees


                                        //************** MODIFIED SURFACE RADIATION EQUATION

                                        //surface_Radiation_6pm = (1) * (((Math.cos(zenith_6pm_radians)*Math.cos(loop_slope4))
                                        surface_Radiation_6pm = (loop_hillshade_6pm / 255.0) * (((Math.cos(zenith_6pm_radians) * Math.cos(loop_slope4))
                                                + (Math.sin(zenith_6pm_radians) * Math.sin(loop_slope4) * Math.cos(azimuth_6pm_radians - loop_aspect4)))
                                                * TOA_6pm * Math.exp(-tau / Math.cos(zenith_6pm_radians)));

                                        //System.out.println("6pm surface rad: " + surface_Radiation_6pm);
                                    } else if (zenith_6pm > 84.0) {

                                        surface_Radiation_6pm = 0.0;

                                        //System.out.println("Noon surface rad: " + surface_Radiation_noon);

                                    }

                                    // Need to total surface_radiation_[time_interval] up for each loop and to create daily mean radiation
                                    //monthlyRadiationTotal += dailyRadiationMean;
                                }
                                //***************************** Create radiation surface ****************************************************

                                if ((elevation[i][j] == NODATA_value)) {
                                    //		System.out.println("No data populated for rad");
                                    surfaceRadiationSurface[i][j] = NODATA_value;
                                    store.setSurfaceRadiationSurface(surfaceRadiationSurface);
                                    //		System.out.println("surface_rad NODATA_value populated");
                                } else if ((elevation[i][j] != NODATA_value) & ((surface_Radiation_midnight != NODATA_value) & (surface_Radiation_6am != NODATA_value)) & ((surface_Radiation_noon != NODATA_value) & (surface_Radiation_6pm != NODATA_value))) {

                                    //surface_Radiation += ((surface_Radiation_midnight + surface_Radiation_6am + surface_Radiation_noon + surface_Radiation_6pm) / 4.0);
                                    surfaceRadiationSurface[i][j] += ((surface_Radiation_midnight + surface_Radiation_6am + surface_Radiation_noon + surface_Radiation_6pm) / 4.0);


                                    //System.out.println("Surface radiation [accum] calc: " + surfaceRadiationSurface[i][j]);

                                    //double surfRadinstance =  ((surface_Radiation_midnight + surface_Radiation_6am + surface_Radiation_noon + surface_Radiation_6pm) / 4.0);
                                    //System.out.println("Surface radiation [instance] calc: " + surfRadinstance);


                                    if (surfaceRadiationSurface[i][j] >= 0.000001) {
                                        //                                      System.out.println("Rad >= 0.000001 populated");
                                        //surfaceRaditionSurface[i][j] = surfaceRadiationSurface[i][j];
                                        store.setSurfaceRadiationSurface(surfaceRadiationSurface);

                                    } else if ((surfaceRadiationSurface[i][j] < 0.000001) & (surfaceRadiationSurface[i][j] != NODATA_value)) {
                                        //                                    System.out.println("Rad < 0.000001 populated");
                                        surfaceRadiationSurface[i][j] = 0.0;
                                        store.setSurfaceRadiationSurface(surfaceRadiationSurface);

                                    }
                                }
                            }
                        }
                    }
                }

                System.out.println("Radiation loop counter: " + radiation_loop_counter);

                /**
                 * Calculating monthly mean surface radiation surface. Loops
                 * through set surfaceRadiationSurface, dividing each cell by
                 * the number of days in the prior radiation loop. *
                 *
                 */
                double daily_surfaceRadiationSurface[][] = store.getSurfaceRadiationSurface();// Gets the SurfaceRadiationSurface array which consisits of monthly totals (total of daily mean values)
                double meanMonthlyRadiation = 0;

                if (daily_surfaceRadiationSurface != null) {

                    System.out.println("Calculating monthly mean surface radiation surface");
                    double raw_rad_total = 0;
                    double meanrad_total = 0;
                    double rad_cellcount = 0;

                    //meanMonthlyRadiation = monthlyRadiationTotal / radiation_loop_counter;
                    //double meanMonthlyRadiation_surface [][] = new double[][];

                    for (int i = 0; i < daily_surfaceRadiationSurface.length; i++) {
                        for (int j = 0; j < daily_surfaceRadiationSurface[i].length; j++) {

                            if (daily_surfaceRadiationSurface[i][j] == NODATA_value) {
                                //macht nichts
                            } else if (daily_surfaceRadiationSurface[i][j] != NODATA_value) {

                                raw_rad_total += daily_surfaceRadiationSurface[i][j];
                                double dailyrad_instance = daily_surfaceRadiationSurface[i][j];

                                meanMonthlyRadiation = dailyrad_instance / radiation_loop_counter;
                                daily_surfaceRadiationSurface[i][j] = meanMonthlyRadiation;
                                store.setSurfaceRadiationSurface(daily_surfaceRadiationSurface);

                                meanrad_total += meanMonthlyRadiation; // This mean is for the whole grid (i.e. this is not a mean monthly cell value (requires the result to be divided by number of cells))
                                rad_cellcount++;

                            }

                        }
                    }

                    //System.out.println("cell count: " + rad_cellcount);
                    //System.out.println("Raw raditaion total: " + raw_rad_total);
                    //System.out.println("raditaion total: " + meanrad_total);
                    System.out.println("Mean radiation for " + month + "/" + year + ": " + (meanrad_total / rad_cellcount));
                    //System.out.println("End of radiation loop reached - go back to start of main monthly loop");

                    if (save_arrays == 1) {
                        /**
                         * This bit will open a save box to save the mean
                         * radiation surface as an ASCII, using the coordinates
                         * of the originally opened up elevation surface
                         *
                         */
                        File f_monthly_rad = new File("F:/Melt_modelling/Historical_model_outputs_slope_fixed/mean_monthly_rad_" + month + "_" + year + ".txt");

                        DecimalFormat df_monthly_rad = new DecimalFormat("#.####");

                        try {
                            BufferedWriter bw_monthly_rad = new BufferedWriter(new FileWriter(f_monthly_rad));
                            bw_monthly_rad.write("ncols" + "         " + store.getOriginalNcols());
                            bw_monthly_rad.write(System.getProperty("line.separator"));
                            bw_monthly_rad.write("nrows" + "         " + store.getOriginalNrows());
                            bw_monthly_rad.write(System.getProperty("line.separator"));
                            bw_monthly_rad.write("xllcorner" + "     " + store.getOriginalXllcorner());
                            bw_monthly_rad.write(System.getProperty("line.separator"));
                            bw_monthly_rad.write("yllcorner" + "     " + store.getOriginalYllcorner());
                            bw_monthly_rad.write(System.getProperty("line.separator"));
                            bw_monthly_rad.write("cellsize" + "      " + store.getOriginalCellsize());
                            bw_monthly_rad.write(System.getProperty("line.separator"));
                            bw_monthly_rad.write("NODATA_value" + "  " + "-9999");
                            bw_monthly_rad.write(System.getProperty("line.separator"));

                            /**
                             * Write out the array data
                             *
                             */
                            String tempStr = "";

                            for (int a = 0; a < daily_surfaceRadiationSurface.length; a++) {
                                for (int b = 0; b < daily_surfaceRadiationSurface[a].length; b++) {

                                    if (daily_surfaceRadiationSurface[a][b] == NODATA_value) {

                                        bw_monthly_rad.write("-9999 ");
                                    } else {

                                        bw_monthly_rad.write(df_monthly_rad.format(daily_surfaceRadiationSurface[a][b]) + " ");

                                    }

                                }
                                bw_monthly_rad.newLine();
                            }

                            bw_monthly_rad.close();

                        } catch (IOException ioe) {

                            ioe.printStackTrace();

                        }

                        System.out.println("Monthly radiation surface for " + month + "/" + year + "saved: " + f_monthly_rad.getAbsolutePath());
                    } else {
                        // Do not save arrays
                    }

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

                            if ((summer_snow_thickness[i][j] != NODATA_value) && (summer_snow_thickness[i][j] > 0.0)) {

                                albedo_instance = snow_albedo;

                            } else if ((winter_snow_thickness[i][j] != NODATA_value) && (winter_snow_thickness[i][j] > 0.0)) {

                                albedo_instance = snow_albedo;

                            } else {

                                albedo_instance = ice_albedo;

                            }
                            //albedo_instance = 0.5;


                            I_for_Q = store.getSurfaceRadiationSurface()[i][j];
                            psi_for_Q = store.getPsiSurface()[i][j];

                            if ((I_for_Q != NODATA_value) & (psi_for_Q != NODATA_value)) {

                                Q = ((1 - albedo_instance) * I_for_Q) + psi_for_Q; //<<<----- This gives a value of Q per m^2 - the actual cell area (5m x 5m) or full months time has not been considered yet 
                                Q_daily = (Q * 86400) * cell_area; // Multiplies Q value by the number of seconds in a day and scales it up to the cell area (so if a 5m x 5m cell, multiply Q by 25)
                                Q_monthly = Q_daily * radiation_loop_counter; // The radiation loop counter accounts for the number of days in the month, specific to year and month
                                //System.out.println("Q daily: "  + Q_daily);
                                //Q_daily_grid[i][j] = Q_daily; // Sets the Q_daily value in the Q_daily_grid
                                Q_monthly_grid[i][j] = Q_monthly; // Sets the Q_monthly value in the Q_monthly_grid

                                Q_counter++; // Counts instances of Q where != NODATA_value
                                //Q_total += Q_daily; // Total value of scaled up Q for a day through each full model loop
                                Q_total += Q_monthly; // Total value of scaled up Q for a month through each full model loop

                            } else if ((I_for_Q == NODATA_value) | (psi_for_Q == NODATA_value)) {

                                Q_monthly_grid[i][j] = NODATA_value;

                            }

                            store.setQmonthlySurface(Q_monthly_grid);

                        }
                    }

                    mean_Q_5_x_m2 = Q_total / Q_counter; //<<---- Mean energy for a 5m x 5m cell (so units are W x 5m^-2)
                    mean_Q_m2 = (Q_total / Q_counter) / 25.0; //<<<---- Mean energy for a 1m x 1m cell (so units are Wm^-2)

                    System.out.println("Q calculated: " + mean_Q_5_x_m2 + " W x 5m^-2"); //<<---- Mean energy for a 5m x 5m cell (so units are W x 5m^-2)
                    System.out.println("Q calculated: " + mean_Q_m2 + " W x m^-2"); //<<<---- Mean energy for a 1m x 1m cell (so units are Wm^-2)

                    /**
                     * This bit will open a save box to save the Q surface layer
                     * as an ASCII, using the coordinates of the originally
                     * opened up elevation surface. This is the MONTHLY surface
                     * (i.e. not in Wm^-2 (or per second)
                     */
                    if ((save_arrays == 1)|(save_arrays == 4)) {

                        //File f_Q_monthly = new File("F:/Melt_modelling/Historical_model_outputs_slope_fixed/Q_monthly_" + (int) month + "." + (int) year + ".txt");
                        File f_Q_monthly = new File("C:/Users/Chris/Desktop/19262010/Historical_model_outputs_slope_fixed/Q_monthly_" + (int) month + "." + (int) year + ".txt");
                        DecimalFormat df_Q_monthly = new DecimalFormat("#.####");
//
                        try {
                            BufferedWriter bw_Q_monthly = new BufferedWriter(new FileWriter(f_Q_monthly));
                            bw_Q_monthly.write("ncols" + "         " + store.getOriginalNcols());
                            bw_Q_monthly.write(System.getProperty("line.separator"));
                            bw_Q_monthly.write("nrows" + "         " + store.getOriginalNrows());
                            bw_Q_monthly.write(System.getProperty("line.separator"));
                            bw_Q_monthly.write("xllcorner" + "     " + store.getOriginalXllcorner());
                            bw_Q_monthly.write(System.getProperty("line.separator"));
                            bw_Q_monthly.write("yllcorner" + "     " + store.getOriginalYllcorner());
                            bw_Q_monthly.write(System.getProperty("line.separator"));
                            bw_Q_monthly.write("cellsize" + "      " + store.getOriginalCellsize());
                            bw_Q_monthly.write(System.getProperty("line.separator"));
                            bw_Q_monthly.write("NODATA_value" + "  " + "-9999");
                            bw_Q_monthly.write(System.getProperty("line.separator"));

                            /**
                             * Write out the array data
                             *
                             */
                            //String tempStr = "";
                            for (int a = 0; a < Q_monthly_grid.length; a++) {
                                for (int b = 0; b < Q_monthly_grid[a].length; b++) {

                                    if (Q_monthly_grid[a][b] == NODATA_value) {

                                        bw_Q_monthly.write("-9999 ");
                                    } else {

                                        bw_Q_monthly.write(df_Q_monthly.format(Q_monthly_grid[a][b]) + " ");

                                    }

                                }
                                bw_Q_monthly.newLine();
                            }

                            bw_Q_monthly.close();

                        } catch (IOException ioe) {

                            ioe.printStackTrace();

                        }

                        System.out.println("Q [monthly] surface: " + f_Q_monthly.getAbsolutePath());
                    } else {
                        // Do not save array
                    }

                    /**
                     * Surface melting.
                     *
                     * Melts through the summer snow surface, then the winter
                     * surface and then the ice surface. Accounts for remaining
                     * Q, where Q available > Q required to melt. In this way,
                     * energy is conserved and used on the next layer that can
                     * be melted.
                     */
                    for (int i = 0; i < summer_snow_thickness.length; i++) {
                        for (int j = 0; j < summer_snow_thickness[i].length; j++) {

                            double Q_for_melt = Q_monthly_grid[i][j];
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
                     * Loops through ice_thickness_surface_adjustment and
                     * populates it with values equal to
                     * glacier_thickness_intermediary - glacier_thickness. The
                     * output of this is a correct calculation of actual surface
                     * melt).
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
                     * Updates elevation surface and re-sets it for use at the
                     * top of the loop
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
                     * This bit will open a save box to save the updated
                     * thickness layer as an ASCII, using the coordinates of the
                     * originally opened up elevation surface
                     *
                     */
                    if (save_arrays == 1) {
                        File f_glacier_thickness = new File("F:/Melt_modelling/Historical_model_outputs_slope_fixed/updated_thickness_" + (int) month + "." + (int) year + ".txt");

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

                        System.out.println("Glacier_thickness: " + f_glacier_thickness.getAbsolutePath());
                    } else {
                        // Do not save array    
                    }

                    /**
                     * This bit will open a save box to save the updated
                     * elevation layer as an ASCII, using the coordinates of the
                     * originally opened up elevation surface
                     *
                     */
                    if (save_arrays == 1) {
                        File f_elevation_update = new File("F:/Melt_modelling/Historical_model_outputs_slope_fixed/updated_elevation_" + (int) month + "." + (int) year + ".txt");

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

                    /*
                     * Deals with inputs for the stats file. Sorts out loop
                     * counting and also loops through the radiation, psi and
                     * lapse rate surfaces to calculate mean values for the
                     * stats file
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

                    bw_stats.write(Loop_counter + ", " + month + ", " + year + ", " + Radiation_total / Radiation_counter
                            + ", " + Psi_total / Psi_counter + ", " + mean_Q_m2 / radiation_loop_counter + ", " + Lapsed_temp_total / Lapsed_temp_counter + ", " + "N/A"
                            + ", " + thickness_adjustment_total / adjustment_counter + ", "
                            + volume_adjustment_total / adjustment_counter);

                    bw_stats.newLine();

                } // If there is no surfaceRadiationSurface (i.e. if the month and temp list extends to a period where there are no solar geometry values
                // e.g. if the main met input fle starts march 1920 but the radiation file doesn't start until march 1926, then surfaceRadiationSurface
                // would be null for all runs until march 1926 
                else {
                    System.out.println("No radiation surface - main input and radiation input must be starting at different dates");

                }

                //}

                System.out.println("summersnow_hit: " + summersnow_hit);
                System.out.println("nosummersnow_hit: " + nosummersnow_hit);

                System.out.println("summersnow_wintersnow_hit: " + summersnow_wintersnow_hit);
                System.out.println("summersnow_no_wintersnow_hit: " + summersnow_no_wintersnow_hit);
                System.out.println("summersnow_wintersnow_ice: " + summersnow_wintersnow_ice);

                System.out.println("no_summersnow_wintersnow_hit: " + no_summersnow_wintersnow_hit);
                System.out.println("no_summersnow_no_wintersnow_hit: " + no_summersnow_no_wintersnow_hit);
                System.out.println("no_summersnow_wintersnow_ice: " + no_summersnow_wintersnow_ice);
                
                /**
                 * This bit will open a save box to save the slope
                 * layer as an ASCII, using the coordinates of the originally
                 * opened up elevation surface
                 *
                 */
                if (save_arrays == 3) {
                    File f_Slope_Surface = new File("F:/Melt_modelling/Historical_model_outputs_slope_fixed/slope_" + (int) month + "." + (int) year + ".txt");

                    DecimalFormat df_Slope_Surface = new DecimalFormat("#.####");

                    try {
                        BufferedWriter bw_Slope_Surface = new BufferedWriter(new FileWriter(f_Slope_Surface));
                        bw_Slope_Surface.write("ncols" + "         " + store.getOriginalNcols());
                        bw_Slope_Surface.write(System.getProperty("line.separator"));
                        bw_Slope_Surface.write("nrows" + "         " + store.getOriginalNrows());
                        bw_Slope_Surface.write(System.getProperty("line.separator"));
                        bw_Slope_Surface.write("xllcorner" + "     " + store.getOriginalXllcorner());
                        bw_Slope_Surface.write(System.getProperty("line.separator"));
                        bw_Slope_Surface.write("yllcorner" + "     " + store.getOriginalYllcorner());
                        bw_Slope_Surface.write(System.getProperty("line.separator"));
                        bw_Slope_Surface.write("cellsize" + "      " + store.getOriginalCellsize());
                        bw_Slope_Surface.write(System.getProperty("line.separator"));
                        bw_Slope_Surface.write("NODATA_value" + "  " + "-9999");
                        bw_Slope_Surface.write(System.getProperty("line.separator"));

                        /**
                         * Write out the array data
                         *
                         */
                        //String tempStr = "";
                        for (int a = 0; a < Slope_Surface.length; a++) {
                            for (int b = 0; b < Slope_Surface[a].length; b++) {

                                if (Slope_Surface[a][b] == NODATA_value) {

                                    bw_Slope_Surface.write("-9999 ");
                                } else {

                                    bw_Slope_Surface.write(df_Slope_Surface.format(Slope_Surface[a][b]) + " ");

                                }

                            }
                            bw_Slope_Surface.newLine();
                        }

                        bw_Slope_Surface.close();

                    } catch (IOException ioe) {

                        ioe.printStackTrace();

                    }

                    System.out.println("Slope check: " + f_Slope_Surface.getAbsolutePath());
                    
                } else {
                    // Do not save array    
                }
                
            }

            bw_stats.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
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
