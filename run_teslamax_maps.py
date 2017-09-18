# coding: utf-8

from pathlib import Path
import os

import matplotlib
import matplotlib.pyplot as plt
import numpy as np
from scipy.optimize import minimize, differential_evolution, basinhopping
from pandas import Series, DataFrame

import teslamax
from teslamax import TeslaMaxGeometry, TeslaMaxPreDesign, TeslaMaxModel


os.chdir(str(Path.home() / "code" / "TeslaMax"))


COLUMNS_NAMES_STR = 'R_s[mm]\t\phi_S[deg]\tK[T^2]\n'

# ### 1.300 T for 35% of the time

# Based on AMR simulations, we choose to use the ramp profile of 1.3 T,
# for 35% of the time, as the basis for our simulation.
# The fixed parameters are given on the cell below:

params_optimization_ref = {"R_i": 0.015,
                "R_o": 0.060,
                "h_gap": 0.025,
                "h_fc": 0.010,
                "R_e": 0.3,
                "n_IV": 4,
                "n_II": 3,
                "phi_C_II": 15,
                "mu_r_II": 1.05,
                "mu_r_IV": 1.05,
              "linear_iron": 1,
              "mu_r_iron": 5e3,
             }

n_II = params_optimization_ref["n_II"]
n_IV = params_optimization_ref["n_IV"]

n = n_II + n_IV

B_rem = 1.4

# expand parameters to include remanence magnitudes for each segment
params_optimization_ref = teslamax.expand_parameters_from_remanence_array(
    B_rem*np.ones(n), 
    params_optimization_ref, 
    "B_rem")

B_high = 1.300
field_fraction = 0.35

map_file_path = Path("map_Rs_phiS_B_%d_FM_%d.txt" %(
    B_high*1000, field_fraction*100))

# #### Generate the results file

# To avoid having to run the notebook many times,
# we write the results to a file:

params_header_str = str(params_optimization_ref).replace(',',',\n') + '\n\n'

map_file_path.write_text(params_header_str)

with map_file_path.open(mode='a') as f:
    f.write(COLUMNS_NAMES_STR)

# ### Update the results file

# We define a range of values for the external radius and the
# iron-magnet separating angle and calculate the cost function.

R_s_values = 1e-3*np.array([120,130,140,150])

phi_S_values = np.array([35,45,55])


params = params_optimization_ref.copy()

for R_s in R_s_values:
    
    params["R_s"] = R_s
    
    for phi_S in phi_S_values:
        
        params["phi_S_II"] = phi_S
        params["phi_S_IV"] = phi_S
        
        tmpd = TeslaMaxPreDesign(params)
        target_function = teslamax.calculate_ramp_profile
    
        B_low = 0.0
    
        target_args = (B_high,B_low,field_fraction)

        alpha_B_rem_g = tmpd.get_optimal_remanence_angles(
            target_function,
            target_args)
        
        K = tmpd.calculate_functional_target(alpha_B_rem_g,
                                            target_function,
                                            target_args)
        
        results_str = "%.1f\t%.1f\t%.3f\n" %(R_s*1e3,phi_S,K)
        print(results_str)
        with map_file_path.open(mode='a') as f:
            f.write(results_str)

