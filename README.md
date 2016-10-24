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
* `H_II_1Q.txt`: values of the magnetic fields in the segments of magnet II in the first quadrant. Columns: $x$, $y$, $B_x$, $B_y$, $H_x$, $H_y$, $B_rem_x$, $B_rem_y$.
* `H_IV_1Q.txt`: values of the magnetic fields in the segments of magnet IV in the first quadrant. Columns: $x$, $y$, $B_x$, $B_y$, $H_x$, $H_y$, $B_rem_x$, $B_rem_y$.
* `COMSOL Main Results.txt`: values of global results for the simulation, in a table-like fashion

## Python interface

An alternative way to interact with TeslaMax is via the Python interface.

Clone the repository and compile the Java file. Then run

	pip install -e .

The `-e` swith installs the package in develop mode, without copying all files to the system PATH. Because the Python script must call the Java class, I haven't figured out a way to make this finding process independent of the actual path, so the normal installation mode does not work (as the Java file is not copied). This "develop" mode should work, though (just be aware that any changes to the package will be instantaneously applied to the `teslamax` script and to the package, as the `-e` flag creates a link for this directory in the Python path).

Now you can run a `teslamax` command in any directory with a correct `params.txt` and the `comsolbatch` command will be automatically called. The Python program is also responsible for creating another set of files:

* `COMSOL Magnetic Profile.txt`, values of the average magnetic induction at a given angular position over the air gap. Columns: $\phi$[degress], $B$.


