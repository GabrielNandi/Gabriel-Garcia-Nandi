
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
OVERWRITE = True

R_o = 153

os.chdir(str(Path.home() / "code" / "TeslaMax"))

params_optimization_ref = {"R_i": 0.030,
                "h_fc": 0.025,
                "R_o": 1e-3*R_o,
                "R_e": 0.7,
                "n_II": 0,
                "n_IV": 5,
                "phi_C_II": 15,
                "mu_r_II": 1.05,
                "mu_r_IV": 1.05,
              "linear_iron": 1,
              "mu_r_iron": 5e3,
             }

B_rem = 1.26


n = params_optimization_ref["n_II"] + params_optimization_ref["n_IV"]

params_optimization_ref = teslamax.expand_parameters_from_remanence_array(
    B_rem*np.ones(n), 
    params_optimization_ref, 
    "B_rem")

B_min = 0.0
field_fraction = 0.3
params_optimization_ref["F_M[%]"] = field_fraction*100

target_function = teslamax.calculate_ramp_profile

map_file_path = Path("map_Rs.txt")

### Generate the results file


params_header_str = str(params_optimization_ref).replace(',',',\n') + '\n\n'
print(params_header_str)
if OVERWRITE:
    map_file_path.write_text(params_header_str)



B_max_values =  np.array([1.2, 1.4, 1.6, 1.8, 1.15, 1.35, 1.55, 1.75, 1.1, 1.3, 1.5, 1.7])

R_s_min = 90e-3
R_s_max = 150e-3
dRs = 5e-3

params = params_optimization_ref.copy()

COLUMNS_NAMES_STR = '\t'.join([ 'R1[mm]',
                               'R2[mm]',
                               'R3[mm]',
                               'R4[mm]',
                               'B_max[T]',
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

                    results_str = "%.1f\t%.1f\t%.2f\t%.5f\t%.3f\t%.7f" %(
                        1e3*R_i,
                        1e3*R_o,
                        1e3*R_g,
                        1e3*R_s,
                        B_max,
                        K1)

                    results_str = results_str + "\n"


                except:
                    continue

            print(results_str)
            with map_file_path.open(mode='a',buffering=1) as f:
                f.write(results_str)
                f.flush()
                os.fsync(f.fileno())



