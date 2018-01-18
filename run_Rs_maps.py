"""Usage: run_Rs_maps.py <R_o>

Generates various TeslaMax models, from common parameters and <R_o> (in mm),
and calculates the minimum R_s that can be used to optimize the model for a
ramp profile of high field 'B_h' (in T) and field fration 'F_M' (0  < F_M < 1).

The results are saved in a file 'map_Rs_Ro_<R_o>.txt', with the argument
printed in units of mm.  The first lines of this file are a string
representation of the dictionary of common  parameters for all simulations;
the remainder rows form a table, with columns for the parameters;
the last column is the cost function K.
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

K_CRITICAL = 0.002
OVERWRITE = False

args = docopt(__doc__,help=True)
print(args)

R_o = float(args["<R_o>"])

os.chdir(str(Path.home() / "code" / "TeslaMax"))

params_optimization_ref = {"R_i": 0.015,
                "h_fc": 0.005,
                "R_o": 1e-3*R_o,
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


n = params_optimization_ref["n_II"] + params_optimization_ref["n_IV"]

params_optimization_ref = teslamax.expand_parameters_from_remanence_array(
    B_rem*np.ones(n), 
    params_optimization_ref, 
    "B_rem")

B_min = 0.0
field_fraction = 0.35
params_optimization_ref["F_M[%]"] = field_fraction*100

target_function = teslamax.calculate_ramp_profile

map_file_path = Path("map_Rs_Ro_%d.txt" %(R_o))

### Generate the results file


params_header_str = str(params_optimization_ref).replace(',',',\n') + '\n\n'
print(params_header_str)
if OVERWRITE:
    map_file_path.write_text(params_header_str)


### Update the results file

phi_S_values = np.array([45,55])


#B_max_min = 1.00
B_max_min = 1.12
B_max_max = 1.20
B_max_step = 0.01

B_max_values =  np.arange(B_max_min,B_max_max+B_max_step,B_max_step)

h_gap_min = 15
h_gap_max = 25
h_gap_step = 1

h_gap_values = 1e-3*np.arange(h_gap_min,h_gap_max + h_gap_step, h_gap_step)

R_s_min = 90e-3
R_s_max = 150e-3
dRs = 5e-3

params = params_optimization_ref.copy()

COLUMNS_NAMES_STR = '\t'.join(['phi_S[deg]',
                               'h_gap[mm]',
                               'B_max[T]',
                               'R_s[mm]',
                               'K[]\n'])


print(COLUMNS_NAMES_STR)
if OVERWRITE:
    with map_file_path.open(mode='a') as f:
        f.write(COLUMNS_NAMES_STR)

results_str=""
for B_max in B_max_values:

    target_args = (B_max,B_min,field_fraction)
    
    for h_gap in h_gap_values:

        params["h_gap"] = h_gap

        for phi_S in phi_S_values:

            params["phi_S_II"] = phi_S
            params["phi_S_IV"] = phi_S

            R_s = R_s_min - dRs

            K = 1e3

            while (K > K_CRITICAL):

                try:

                    if R_s < R_s_max:
                        R_s = R_s + dRs
                    else:
                        break

                    params["R_s"] = R_s

                    tmpd = TeslaMaxPreDesign(params)


                    alpha_B_rem_g = tmpd.get_optimal_remanence_angles(
                        target_function,
                        target_args)

                    K = tmpd.calculate_functional_target(alpha_B_rem_g,
                                                         target_function,
                                                         target_args)

                    results_str = "%.1f\t%.3f\t%.3f\t%.3f\t%.6f" %(
                        phi_S,
                        1e3*h_gap,
                        B_max,
                        1e3*R_s,
                        K)

                    results_str = results_str + "\n"


                except:
                    continue

            print(results_str)
            with map_file_path.open(mode='a',buffering=1) as f:
                f.write(results_str)
                f.flush()
                os.fsync(f.fileno())



