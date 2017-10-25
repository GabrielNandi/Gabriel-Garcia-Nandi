"""Usage: run_K_maps.py <B_h> <F_M>

Generates various TeslaMax models and optimizes them for a ramp profile
of high field 'B_h' (in T) and field fration 'F_M' (0  < F_M < 1).

The results are saved in a file 'map_K_B_<B_h>_FM_<F_M>.txt', where the
first argument is printed in mT and the second in integer percentage units. The
first lines of this file are a string representation of the dictionary of
common parameters for all simulations; the remainder rows form a table,
with columns for the parameters; the last columns are the cost function K and
the remanence angles in order.
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

OVERWRITE = False

args = docopt(__doc__,help=True)
print(args)

os.chdir(str(Path.home() / "code" / "TeslaMax"))

params_optimization_ref = {"R_i": 0.015,
                "h_gap": 0.025,
                "h_fc": 0.010,
                "R_e": 0.3,
                "phi_C_II": 15,
                "mu_r_II": 1.05,
                "mu_r_IV": 1.05,
              "linear_iron": 1,
              "mu_r_iron": 5e3,
             }

B_rem = 1.4

B_low = 0.0
B_high = float(args["<B_h>"])
field_fraction = float(args["<F_M>"])

target_function = teslamax.calculate_ramp_profile
target_args = (B_high,B_low,field_fraction)

map_file_path = Path("map_K_B_%d_FM_%d.txt" %(
    B_high*1000, field_fraction*100))

# #### Generate the results file


params_header_str = str(params_optimization_ref).replace(',',',\n') + '\n\n'
print(params_header_str)
if OVERWRITE:
    map_file_path.write_text(params_header_str)


# ### Update the results file

# We define a range of values for the external radius and the
# iron-magnet separating angle and calculate the cost function.

#R_o_values = 1e-3*np.array([40,50,60])
R_o_values = 1e-3*np.array([40])

n_II_values = np.array([1,2,3])

n_IV_values = np.array([2,3,4])

# R_s_values = 1e-3*np.array([100,110,120,130])
R_s_values = 1e-3*np.array([130])

phi_S_values = np.array([35,45,55])


params = params_optimization_ref.copy()

COLUMNS_NAMES_STR = '\t'.join(['R_o[mm]',
                               'R_s[mm]',
                               'phi_S[deg]',
                               'n_II[]',
                               'n_IV[]',
                               'K[T^2]',
                               'alpha\n'])


print(COLUMNS_NAMES_STR)
if OVERWRITE:
    with map_file_path.open(mode='a') as f:
        f.write(COLUMNS_NAMES_STR)

for R_o in R_o_values:

    params["R_o"] = R_o

    for R_s in R_s_values:

        params["R_s"] = R_s

        for phi_S in phi_S_values:

            params["phi_S_II"] = phi_S
            params["phi_S_IV"] = phi_S

            for n_II in n_II_values:

                params["n_II"] = n_II

                for n_IV in n_IV_values:

                    params["n_IV"] = n_IV

                    n = n_II + n_IV

                    params = teslamax.expand_parameters_from_remanence_array(
                        B_rem*np.ones(n), 
                        params, 
                        "B_rem")

                    try:

                        tmpd = TeslaMaxPreDesign(params)


                        alpha_B_rem_g = tmpd.get_optimal_remanence_angles(
                            target_function,
                            target_args)

                        K = tmpd.calculate_functional_target(alpha_B_rem_g,
                                                            target_function,
                                                            target_args)

                        results_str = "%.1f\t%.1f\t%.1f\t%d\t%d\t%.3f" %(
                            R_o*1e3,
                            R_s*1e3,
                            phi_S,
                            n_II,
                            n_IV,
                            K)

                        for a in alpha_B_rem_g:
                            results_str = results_str + "\t%.3f" %(a,)

                        results_str = results_str + "\n"

                        print(results_str)
                        with map_file_path.open(mode='a',buffering=1) as f:
                            f.write(results_str)
                            f.flush()
                            os.fsync(f.fileno()),
                    except:
                        continue

