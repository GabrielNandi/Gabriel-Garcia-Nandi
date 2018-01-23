"""Usage: run_Rs_maps_large.py 

Generates various TeslaMax models from common parameters ,
and calculates the minimum R_s that can be used to optimize the model.

The results are saved in a file 'map_Rs_large.txt'.
The first lines of this file are a string representation of the dictionary of
common  parameters for all simulations;
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
B_REM = 1.4
B_MIN = 0
B_MAX = 1.4
FIELD_FRACTION = 0.35
PHI_S_IV = 45
L_AMR = 120e-3
L_MAGNET = 1.1*L_AMR
W = 30e-3
H_GAP = 33e-3

OVERWRITE = False

args = docopt(__doc__,help=True)
print(args)

os.chdir(str(Path.home() / "code" / "TeslaMax"))

params_optimization_ref = {"R_i": 0.015,
                "h_fc": 0.005,
                "R_e": 0.7,
                "n_II": 0,
                "h_gap": H_GAP,
                "n_IV": 4,
                "phi_C_II": 0,
                "phi_S_II": 0,
                "phi_S_IV": PHI_S_IV,
                "mu_r_II": 1.05,
                "mu_r_IV": 1.05,
              "linear_iron": 1,
              "mu_r_iron": 5e3,
             }

n = params_optimization_ref["n_II"] + params_optimization_ref["n_IV"]

params_optimization_ref = teslamax.expand_parameters_from_remanence_array(
    B_REM*np.ones(n), 
    params_optimization_ref, 
    "B_rem")

params_optimization_ref["F_M[%]"] = 100 * FIELD_FRACTION

target_function = teslamax.calculate_ramp_profile
target_args = (B_MAX,B_MIN,FIELD_FRACTION)

map_file_path = Path("map_Rs_large.txt")

### Generate the results file

params_header_str = str(params_optimization_ref).replace(',',',\n') + '\n\n'
print(params_header_str)
if OVERWRITE:
    map_file_path.write_text(params_header_str)


### Update the results file

Q_c_reg_values = np.array([75,100,150,200,250])
#Q_c_values = np.array([2000,8000])
Q_c_values = np.array([5000,])

dRs = 10e-3

params = params_optimization_ref.copy()

COLUMNS_NAMES_STR = '\t'.join(['Q_c_reg[W]',
                               'Q_c[W]',
                               'N_r',
                               'R_o[mm]',
                               'R_s[mm]',
                               'V_PMM[L]',
                               'K[]\n'])


print(COLUMNS_NAMES_STR)
if OVERWRITE:
    with map_file_path.open(mode='a') as f:
        f.write(COLUMNS_NAMES_STR)

results_str=""

for Q_c_reg in Q_c_reg_values:
    
    for Q_c in Q_c_values:

        N_r = np.ceil(Q_c / Q_c_reg)
        R_o = (N_r * W)/(2*np.pi) - params["h_gap"]/2
        
        params["R_o"] = R_o

        # the minimum value of the external radius must allow for
        # sufficient magnet in the external magnet
        # Hence, we just add 3 times the air gap height
        # (one for the actual gap and two for the cylinder)
        # Keep in mind this script is intended for a rough estimative
        R_s_min = R_o + 3*H_GAP
        R_s_max = 2*R_s_min
        
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

                V_PMM = tmpd.geometry.calculate_magnet_volume(L_MAGNET)


                alpha_B_rem_g = tmpd.get_optimal_remanence_angles(
                    target_function,
                    target_args)

                K = tmpd.calculate_functional_target(alpha_B_rem_g,
                                                     target_function,
                                                     target_args)

                
                results_str = "%d\t%d\t%d\t%d\t%d\t%.2f\t%.6f" %(
                    Q_c_reg,
                    Q_c,
                    N_r,
                    1e3*R_o,
                    1e3*R_s,
                    1e3*V_PMM,
                    K)

                results_str = results_str + "\n"


            except:
                continue

        print(results_str)
        with map_file_path.open(mode='a',buffering=1) as f:
            f.write(results_str)
            f.flush()
            os.fsync(f.fileno())



