"""Usage: run_Rs_maps.py

Generates various TeslaMax models, from common parameters,
and calculates the minimum R_s that can be used to optimize the model for a
ramp profile of high field 'B_h' (in T) and field fration 'F_M' (0  < F_M < 1).

The results are saved in a file 'map_Rs.txt'. The first lines of this file are
a string representation of the dictionary of common parameters for all
simulations; the remainder rows form a table, with columns for the parameters;
the last columns are the cost function K and the remanence angles in order.
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

args = docopt(__doc__,help=True)
print(args)

os.chdir(str(Path.home() / "code" / "TeslaMax"))

params_optimization_ref = {"R_i": 0.015,
                "h_gap": 0.025,
                "h_fc": 0.010,
                "R_o": 0.050,
                "R_e": 0.3,
                "n_II": 2,
                "n_IV": 3,
                "phi_C_II": 15,
                "phi_S_II": 45,
                "phi_S_IV": 45,
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

target_function = teslamax.calculate_ramp_profile

map_file_path = Path("map_Rs.txt")

# #### Generate the results file


#params_header_str = str(params_optimization_ref).replace(',',',\n') + '\n\n'
#print(params_header_str)
#map_file_path.write_text(params_header_str)


# ### Update the results file

#B_max_values = np.array([1.2, 1.225, 1.25, 1.275, 1.3])
B_max_values = np.array([1.225, 1.25, 1.275, 1.3])

F_M_values = 1e-2*np.array([35,37.5,40,42.5,45])

R_s_min = 100e-3

R_s_max = 150e-3

dRs = 10e-3

K_max = 1.0


params = params_optimization_ref.copy()

COLUMNS_NAMES_STR = '\t'.join(['B_max[T]',
                               'F_M[%]',
                               'R_s[mm]',
                               'K[T^2]',
                               'alpha\n'])


print(COLUMNS_NAMES_STR)
#with map_file_path.open(mode='a') as f:
#    f.write(COLUMNS_NAMES_STR)

for B_max in B_max_values:

    for F_M in F_M_values:

        target_args = (B_max,B_min,F_M)

        R_s = R_s_min - dRs

        K = 1e3
        
        while (K > K_max):

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

                results_str = "%.3f\t%.3f\t%.1f\t%.3f" %(
                B_max,
                F_M*1e2,
                R_s*1e3,
                K)

                for a in alpha_B_rem_g:
                    results_str = results_str + "\t%.3f" %(a,)

                results_str = results_str + "\n"


            except:
                continue



        print(results_str)
        with map_file_path.open(mode='a',buffering=1) as f:
            f.write(results_str)
            f.flush()
            os.fsync(f.fileno())



