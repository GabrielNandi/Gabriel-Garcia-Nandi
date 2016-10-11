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
from scipy.interpolate import NearestNDInterpolator
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

N_PROFILE_POINTS = 100

__all__ = ['get_teslamax_class_file_path',
           'get_comsol_parameters_series']

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
    """Read and parse 'filename' as exported by COMSOL.
    Export the numerical data as a numpy array containing only the numerical
    data; the first two columns are x and y values. All values are in SI.
    
    Keyword Arguments:
    filename -- str
    """

    return np.loadtxt(filename,skiprows=9)

    

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
    column_names = ["phi[deg]","B[T]"]
    column_header = " ".join(column_names)
    
    # load data from the high and low field regions
    B_h = read_comsol_data_file(B_HIGH_FILENAME)
    B_l = read_comsol_data_file(B_LOW_FILENAME)
    
    B_1q = np.concatenate((B_h,B_l),axis=0)
    
    # calcualte vector of angles for the first quadrant
    case_series = get_comsol_parameters_series()
    
    n_phi_points = 100
    
    R_g = case_series['R_g']
    R_o = case_series['R_o']
    
    
    # create ranges for phi and r
    phi_min = 0.0
    phi_max = np.pi/2
    
    phi_vector_1q = np.linspace(phi_min,phi_max,N_PROFILE_POINTS)
    
    # slightly offset the boundaries to avoid numerical problems at the interfaces
    r_min = 1.001*R_o 
    r_max = 0.999*R_g
    n_r_points = 5
    
    r_vector = np.linspace(r_min,r_max,n_r_points)
    
    r_grid, phi_grid = np.meshgrid(r_vector,phi_vector_1q)
    
    # calcualte the points (x,y) distributed along
    # radial lines
    x_grid = r_grid * np.cos(phi_grid)
    y_grid = r_grid * np.sin(phi_grid)
    
    # create a interpolation function over the 1st quadrant grid
    # we use the nearest interpolation to avoid negative values
    # when fitting a spline near points where B = 0
    
    fB = NearestNDInterpolator(x=B_1q[:,0:2],y=B_1q[:,2])

    # because both x_grid and y_grid have shape (n_r_points, N_PROFILE_POINTS),
    # when we apply the above created function we will get an array with the
    # same shape. We then take the average value along each row,
    # resuting in an array (N_PROFILE_POINTS)
    B_profile_1q = np.mean(fB(x_grid,y_grid),axis=1)

    # extrapolate data to the full circle
    phi_vector = np.concatenate((phi_vector_1q,
                                 phi_vector_1q+np.pi/2,
                                 phi_vector_1q+np.pi,
                                 phi_vector_1q+(3/2)*np.pi))

    B_profile = np.concatenate((B_profile_1q,
                                B_profile_1q[::-1],
                                B_profile_1q,
                                B_profile_1q[::-1]))

    profile_data = np.array((np.rad2deg(phi_vector),B_profile)).T
    
    np.savetxt(str(p),
               profile_data,
               fmt=("%.2f","%.5f"),
               delimiter=" ",
               header=column_header,
               comments='')
    
    

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
