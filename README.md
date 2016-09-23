# TeslaMax

Design and simulate nested Halbach cylinders with COMSOL

The main Java file `java/TeslaMax.java` contains a description of a COMSOL model, from building the geometry to running the results.

The COMSOL (e.g. for version 5.2a) command line executables (for Windows) are found on:

	C:\Program Files\COMSOL\COMSOL52a\Multiphysics\bin\win64

Add this directory to your PATH.

## Compiling and running

To compile the Java source file:

	comsolcompile java/TeslaMax.java

This will result into a Java `.class` file, which you can open normally with COMSOL (as if you are opening a `.mph` file). Alternatively, you can run:

	comsolbatch -inputfile java/TeslaMax.class -outputfile <basename>

This will run COMSOL in *batch* mode, without the graphical interface. If everything is successful, you have a `<basename>_Model.mph` file (in the current directory) that you can load into COMSOL to see the results. If you do not specify the `-outputfile` option, the file will be saved as `java/TeslaMax_Model.mph`.

Notice that you can run the above command from any working directory, as long as you specify the full path to the input Java file. The source file is configured to read and write files from/to the current working directory.

You have to provide a `params.txt` file with all required parameters. This repository includes a sample file.

The TeslaMax model will output a list of files to the current directory. All files have SI units and the header lines are always preceded by `%`.

* `B_high.txt`: values of the magnetic flux density at the high field region of the magnetic gap (first quadrant). Columns: $x$, $y$, $B$;
* `B_low.txt`: values of the magnetic flux density at the low field region of the magnetic gap (first quadrant). Columns: $x$, $y$, $B$;


