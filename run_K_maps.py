"""Usage: run_K_maps.py <R_s>

Generates various TeslaMax models, using common paramaters and
the value of the external radius <R_s>, and optimizes them for several
ramp profile.

The results are saved in a file 'map_K_Rs_<R_s>.txt', with the argument
printed in mm. The first lines of this file are a string representation of the
dictionary of common parameters for all simulations; the remainder rows form a
table, with columns for the parameters;
the last columns is the cost function K.
"""

# coding: utf-8

from pathlib import Path
import os

from docopt import docopt
import matplotlib
import matplotlib.pyplot as plt
import numpy as np
from scipy.optimize import minimize, differential_evolution, basinhopping
from pandas import Series, DataFrame

import teslamax
from teslamax import TeslaMaxGeometry, TeslaMaxPreDesign, TeslaMaxModel

OVERWRITE = True 

args = docopt(__doc__,help=True)
print(args)

R_s = float(args["<R_s>"])

os.chdir(str(Path.home() / "code" / "TeslaMax"))

params_optimization_ref = {"R_i": 0.010,
                           "h_fc": 0.005,
                           "R_s": 1e-3*R_s,
                           "R_o": 40e-3,
                           "R_e": 0.3,
                           "n_II": 2,
                           "n_IV": 4,
                           "phi_C_II": 15,
                           "mu_r_II": 1.05,
                           "mu_r_IV": 1.05,
                           "linear_iron": 1,
                           "mu_r_iron": 5e3,
}

B_rem = 1.4

B_min = 0.0
field_fraction = 0.35
params_optimization_ref["F_M[%]"] = field_fraction*100

target_function = teslamax.calculate_ramp_profile

map_file_path = Path("map_K_Rs_%d.txt" %(R_s))

# #### Generate the results file


params_header_str = str(params_optimization_ref).replace(',',',\n') + '\n\n'
print(params_header_str)
if OVERWRITE:
    map_file_path.write_text(params_header_str)


# ### Update the results file

# We define a range of values for the external radius and the
# iron-magnet separating angle and calculate the cost function.


phi_S_values = np.array([35,45,55])


B_max_min = 1.00
B_max_max = 1.20
B_max_step = 0.01

B_max_values =  np.arange(B_max_min,B_max_max+B_max_step,B_max_step)


h_gap_min = 15
h_gap_max = 25
h_gap_step = 1

h_gap_values = 1e-3*np.arange(h_gap_min,h_gap_max + h_gap_step, h_gap_step)

params = params_optimization_ref.copy()

n = params["n_II"] + params["n_IV"]

params = teslamax.expand_parameters_from_remanence_array(
    B_rem*np.ones(n), 
    params, 
    "B_rem")


COLUMNS_NAMES_STR = '\t'.join(['phi_S[deg]',
                               'h_gap[mm]',
                               'B_max[T]',
                               'K[]\n'])


print(COLUMNS_NAMES_STR)
if OVERWRITE:
    with map_file_path.open(mode='a') as f:
        f.write(COLUMNS_NAMES_STR)

for B_max in B_max_values:

    target_args = (B_max,B_min,field_fraction)

    for h_gap in h_gap_values:

        params["h_gap"] = h_gap

        for phi_S in phi_S_values:

            params["phi_S_II"] = phi_S
            params["phi_S_IV"] = phi_S

            try:

                tmpd = TeslaMaxPreDesign(params)
                
                
                alpha_B_rem_g = tmpd.get_optimal_remanence_angles(
                    target_function,
                    target_args)
                
                K = tmpd.calculate_functional_target(alpha_B_rem_g,
                                                     target_function,
                                                     target_args)
                
                results_str = "%.1f\t%.3f\t%.3f\t%.6f" %(
                    phi_S,
                    1e3*h_gap,
                    B_max,
                    K)
                
                
                results_str = results_str + "\n"
                
                print(results_str)
                with map_file_path.open(mode='a',buffering=1) as f:
                    f.write(results_str)
                    f.flush()
                    os.fsync(f.fileno()),
            except:
                continue

