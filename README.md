A user friendly distributed glacier surface energy balance model
================================================================

The code provided here makes up a distributed surface energy balance (SEB) model, designed for small Arctic glaciers. 

The model was developed specifically for Kårsaglaciären (N. Sweden) using methods described fully in Williams (2014), 
however no local scaling has been carried out other than 3 constants taken from Giesen and Oerlemans (2012) which is 
discussed in Williams (2013).

The source code provided when compiled provides a user friendly platform from which SEB calculations can be facilitated.
Model outputs are provided in the form of ESRI ASCII files - i.e. they can easily be converted using ArcGIS to create 
georeferenced rasters (all georeferencing of the surfaces here use the WGS 1984 34N projected coordinate system).

The model was developed to assess the sensitivity of changing glacier geometry on surface energy balance, with a 
particular focus on the changing contribution of radiation incident at the glacier surface, using a 
reference balance approach (see Elsberg et al. (2001) and Huss et al. (2012)). Consequently, the model was designed 
to enable scenario based model runs of four types:

* slope fixed (surface slope of the glacier does not change with elevation)
* aspect fixed (surface aspect of the glacier does not change with elevation)
* slope and aspect fixed (surface slope and aspect of the glacier do not change with elevation)
* fully dynamic surface  (surface slope and aspect change with elevation, as elevation changes as a function of ice melt)

Getting to the core
===================

If you are just interested in looking at the key files to understand how the main model processing goes are:

SEB_model\source_code\

* ContemporaryModel_Precip_winter_snow_limit.java (dynamic version of the contemporary model)
* Historical_Model_Precip_winter_snow_limit.java (dynamic version of the historical model)
* Historical_model_dynamic_LARGE_FILES.java (dynamic version of the historical (LARGE FILES) model)

For the programmers and those wanting to use the model
======================================================

******************************************
The contents of all properties files is set to the directory of: 

C:\Users\ggwillc\Desktop\ 

This needs to be changed before the model can be run on the user's system to the directory that the user saves the programme files to - this applies to all properties files. The individual model code contains hard-wired paths for model outputs - these are usually written as: 

F:/Melt_modelling/ 

This must be changed prior to model compilation to ensure that a suitable directory is chosen by the user - the specific code affected is highlighted below.
******************************************

* All source files can be found in:
	SEB_model\source_code

* The code is also packaged up as "energymodels": 
	SEB_model\energymodels

	- This can be opened directly into an IDE such as netbeans - the main class is ModelGUI.java

* Properties files are also available:
	SEB_model\energymodels\Model_props_20092010_oct_dec.properties **
  SEB_model\energymodels\Model_props_winter_19431959_historic.properties **
	SEB_model\energymodels\Model_props_winter_19262010_historic_LARGE_FILES **

	-- and duplicated  here --

	SEB_model\energymodels\build\classes

		** These config files are provided to give an introduction to the model and a sample of data with which to introduce the user to the model. The files are all available at \SEB_model\sample_data_files
	
	User specific config files can easily be developed with the appropriate files - see above for the format of the files required. If a new config file is created, the call to this file (i.e. the file name) should be altered in:

			> OpenConfigFileListener				- for contemporary config files
			> OpenConfigFile_historical_Listener	- for historical config files
			> OpenConfig_historical_largeFiles		- for historical (large) config files 

			The config. file must also be available in both SEB_model\energymodels\ and SEB_model\energymodels\build\classes for it to be available in the model

		- these can be called in the model by:
			> File
				> Open configuration file				   <- accesses the contemporary properties file
						- or -
				> Open configuration file [historical run] <- accesses the historical properties file
						- or -
				> Open configuration file [historical model run] <- accesses the historical properties file
						- or -
				> Open configuration file [historical: large file sizes] <- accesses the historical properties file

* Some sample files are also available in:
	SEB_model\sample_data_files\

		59surfacez.txt (digital elevation model (DEM) ASCII)*
		59_hillshade.txt (hillshade ASCII)*
		59snow1_ice_0.txt (albedo ASCII)*
		AWS_MODEL_INPUT_FILE_1.10.09_31.12.10 RAIN NEW.csv (simple met data input file)*

			* the sample surfaces and met data do not match in time but are provided here just for experimentation and an introduction to the features of the model. The met data file and surfaces can be altered by the user and other ASCII surfaces and met data can be uploaded as required. 

		- the historical/ and contemp/ folders contain the data called by the properties files

		- ASCII files can be opened through
			> File
				> Open surfaces 
					> .... make sure you open the correct surface with the correct button (i.e. only  open the elevation surface if using the elevation button - despite being able to open up any ASCII file)

		- the met data .csv file can be opened through
			> File
				> Open Meteorological Data
					> Open Contemporary model input data (multiple columns)

		- opening these files also allows for testing of some of the basic methods functions
			> Methods
				> Surface slope calculator - always use this tool once you have an elevation surface uploaded
				> Surface aspect calculator - always use this tool once you have an elevation surface uploaded

				- if using tools such as the lapse rate calculator, a lapse rate surface will be calculated for all days in the met. data input file but the only surface that can be saved will be the last to be calculated. If a lapse rate surface for a specific day is required, alter the met data file (or create a sub-sampled file) to include data for a single time step (1 day in the case of the file provided), re-upload the file and the required surfaces and re-run the tool.			

* On opening the properties files, model runs (contemporary/historical/historical large files) can be implemented - output paths are hard-wired (as mentioned above) and these will need to be configured manually in the code to suit your system - see the following files:

======================
Contemporary model run
======================

ContemporaryModel_Precip_aspect_and_slope_fixed.java 
ContemporaryModel_Precip_aspect_fixed.java
ContemporaryModel_Precip_elev_fixed.java
ContemporaryModel_Precip_slope_fixed.java
ContemporaryModel_Precip_winter_snow_limit.java <- this is the dynamic surface model

=====================
Historical model runs
=====================

Historical_Model_Aspect_Fixed.java
Historical_Model_Precip_Elev_Fixed.java
Historical_Model_Precip_winter_snow_limit.java <- this is the dynamic surface model
Historical_Model_Slope_and_Aspect_fixed.java
Historical_Model_Slope_fixed.java

===================================
Historical (large files) model runs***
===================================

Historical_Model_Aspect_Fixed_LARGE_FILES.java
Historical_Model_Slope_and_Aspect_fixed_LARGE_FILES.java
Historical_model_dynamic_LARGE_FILES.java <- this is the dynamic surface model
Historical_model_elev_fixed_LARGE_FILES.java
Historical_model_slope_fixed_LARGE_FILES.java

*** Each of these files contains a preamble explaining how "Historical (large files)" differs to the normal "Historical"

Things to be aware of
=====================

* There are a number of hard-wired path directories that will need altering - sorry! These should be altered to suit your system. If you encounter errors when using the model, this should be the first thing to investigate.
* The code available here contains the basis for a variety of buttons and methods - some of which are obsolete.
* If using the open surface buttons, these will allow any ASCII of the correct format to be opened - for example, if using the "Open Elevation surface" button, you are not stopped from opening any ASCII e.g.  59_hillshade.txt (the hillshade ASCII) from the sample_data_files folder. This will be read in as it is of the correct format but the values will be representative of hillshade and not elevation. If then using the surface for calculating slope or aspect through the tools available in "Methods", this will result in the calculation of incorrect values. A more complex file opening dialogue could be developed to restrict which files can be opened by which buttons.
      
References
==========

Elsberg, D.H., Harrison, W.D., Echelmeyer, K.A. and Krimmel, R.M. 2001 Quantifying the effects of climate and surface 
change on glacier mass balance. Journal of Glaciology, 47(159), pp649–658 (doi: 10.3189/172756501781831783)

Giesen, R. and Oerlemans, J. 2012. Global application of a surface mass balance model using gridded climate data. The 
Cryosphere Discuss. 6(2), pp1445-1490.

Huss, M., Hock, R., Bauder, A. and Funk, M. Conventional versus reference-surface mass balance. Journal of Glaciology,
38(208), pp278-286.

Williams, C. 2013. Geometric and surface energy balance change affecting Kårsaglaciären, northern Sweden, over 
the past century. University of Leeds PhD Thesis.
Status API Training Shop Blog About © 2014 GitHub, Inc. Terms Privacy Security Contact 
