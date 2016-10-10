"""Usage: teslamax [--help]

Read 'comsol-parameters.txt' from the current directory and execute the
TeslaMax COMSOL model

Options:

-h, --help    Print this help message and exit
"""

import subprocess
import os.path
import sys
from pathlib import Path
import numpy as np
import pandas as pd
from docopt import docopt

B_HIGH_FILENAME = "B_high.txt"
B_LOW_FILENAME = "B_low.txt"

MAIN_RESULTS_FILENAME = "COMSOL Main Results.txt"

MAGNETIC_PROFILE_FILENAME = "COMSOL Magnetic Profile.txt"

COMSOL_PARAMETER_FILENAME = "params.txt"

A_MAGNET_INDEX = "A_magnet[m2]"
A_GAP_INDEX = "A_gap[m2]"
B_HIGH_INDEX = "B_high[T]"
B_LOW_INDEX = "B_low[T]"

def get_teslamax_class_file_path():
    """Return the path string to the main TeslaMax java class file.
    
    """

    # the class file is located in a 'java' directory,
    # in the parent directory of the present file

    java_dir = os.path.join(os.path.dirname(__file__),
                            os.pardir,
                            "java")
    class_file = os.path.join(java_dir,"TeslaMax.class")
    return os.path.realpath(class_file)

def get_comsol_parameters_series():
    """Parse the COMSOL parameters file in the current directory and
    return a pandas Series from it.
    
    """
    param_comsol_file = Path('.') / COMSOL_PARAMETER_FILENAME

    param_comsol_series = pd.read_table(str(param_comsol_file),
                                        squeeze=True,
                                        sep=" ",
                                        index_col=0,
                                        header=None)

    param_comsol_series.name = "COMSOL Parameters"
    param_comsol_series.index.name = None

    return param_comsol_series



def read_comsol_data_file(filename):
    """Read and parse 'filename' as exported by COMSOL. Assume
    the header rows are preceded by '%'. Export the numerical data as a
    numpy array containing only the numerical data; the first two columns
    are x and y values. All values are in SI.
    
    Keyword Arguments:
    filename -- str
    """
    
def calculate_area_average(data):
    """For a 'data' array, assuming the first two columns are x and y values
    and the third column is a scalar field over that domain, return the area
    average of that field over that domain
    
    Keyword Arguments:
    data -- array
    """
    
    

def process_main_results_file():
    """Take the file "COMSOL Main Results.txt" as exported by COMSOL and
    clean the header data.
    
    """
    
    p = Path('.') / MAIN_RESULTS_FILENAME

    param_comsol_series = get_comsol_parameters_series()

    results = pd.read_table(MAIN_RESULTS_FILENAME,
                            sep="\s+",
                            skiprows=5,
                            index_col=None,
                            header=None,
                            names=["B_high[T]",
                                   "B_low[T]",
                                   "A_gap[m2]",
                                   "A_magnet[m2]"])
    
   
    results.to_csv(str(p),
                   float_format="%.6f",
                   sep=" ",
                   index=False)
    

def write_magnetic_profile_file():
    """Create a file "COMSOL Magnetic Profile.txt" in the current directory,
    assuming the teslamax command was already ran
    
    """
    
    p = Path('.') / MAGNETIC_PROFILE_FILENAME
    p.touch()
    
    

def run_comsol_mode():
    """Run the TeslaMax COMSOL Java model from the current directory
    
    """

    # locate the .class file to be executed
    teslamax_class_file_path = get_teslamax_class_file_path()

    # call the comsol batch command from it
    comsol_cmd_args = ["comsolbatch", "-inputfile", teslamax_class_file_path]
    
    comsol_cmd_process = subprocess.run(comsol_cmd_args,
                                        shell=True,
                                        stdout=subprocess.PIPE,
                                        stderr=subprocess.STDOUT,
                                        universal_newlines=True)

    print(comsol_cmd_process.stdout,end='')

    process_main_results_file()
    write_magnetic_profile_file()
    
    sys.exit(comsol_cmd_process.returncode)

def main():

    arguments = docopt(__doc__)

    help_mode_q = arguments['--help']

    if not help_mode_q:

        run_comsol_mode()


if __name__ == '__main__':
    main()
