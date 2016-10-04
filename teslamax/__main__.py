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

def calculate_magnet_area(params):
    """Calculate the magnet area from the fields of 'params'
    
    Keyword Arguments:
    params -- dict-like, must contain the fields 'R_i','R_o',
    'R_g' and 'R_s'
    """

    area_magnet_II = np.pi/4 * (params['R_o']**2 - params['R_i']**2)
    area_magnet_IV = np.pi/4 * (params['R_s']**2 - params['R_g']**2)

    return (area_magnet_II + area_magnet_IV)

def calculate_air_gap_area(params):
    """Calculate the air gap area from the fields of 'params'
    
    Keyword Arguments:
    params -- dict-like, must contain the fields 'R_i','R_o',
    'R_g' and 'R_s'
    """

    area_gap = np.pi/4 * (params['R_g']**2 - params['R_o']**2)

    return area_gap

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
    
    

def write_main_results_file():
    """Create a file "COMSOL Main Results.txt" in the current directory,
    assuming the teslamax command was already ran
    
    """
    
    p = Path('.') / MAIN_RESULTS_FILENAME

    param_comsol_series = get_comsol_parameters_series()
    
    A_magnet = calculate_magnet_area(param_comsol_series)
    A_gap = calculate_air_gap_area(param_comsol_series)

    B_high_data = read_comsol_data_file(B_HIGH_FILENAME)
    B_low_data = read_comsol_data_file(B_LOW_FILENAME)

    B_high_avg = calculate_area_average(B_high_data)
    B_low_avg = calculate_area_average(B_low_data)

    results_series = pd.Series()
    results_series.name = "COMSOL Main Results"

    results_series[A_MAGNET_INDEX] = A_magnet
    results_series[A_GAP_INDEX] = A_gap
    results_series[B_HIGH_INDEX] = B_high_avg
    results_series[B_LOW_INDEX] = B_low_index

    results_series.to_csv(str(p),
                          float_format="%.6f",
                          sep=" ")
    

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

    write_main_results_file()
    write_magnetic_profile_file()
    
    sys.exit(comsol_cmd_process.returncode)

def main():

    arguments = docopt(__doc__)

    help_mode_q = arguments['--help']

    if not help_mode_q:

        run_comsol_mode()


if __name__ == '__main__':
    main()
