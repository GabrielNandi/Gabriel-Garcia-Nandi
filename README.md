# TeslaMax

Design and simulate nested Halbach cylinders with COMSOL

The main Java file contains a description of a COMSOL model, from building the geometry to running the results.

The COMSOL (e.g. for version 5.2a) command line executables (for Windows) are found on:

	C:\Program Files\COMSOL\COMSOL52a\Multiphysics\bin\win64

Add this directory to your PATH.

To compile the Java source file:

	comsolcompile <filename>.java

This will result into a Java `.class` file, which you can open normally with COMSOL (as if you are opening a `.mph` file). Alternatively, you can run:

	comsolbatch -inputfile <filename>.class

This will run COMSOL in *batch* mode, without the graphical interface. If everything is successful, you have a `<filename>_Model.mph` file that you can load into COMSOL to see the results.
