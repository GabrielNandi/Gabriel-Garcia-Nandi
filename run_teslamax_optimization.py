"""
Run a single case of the TeslaMax optimization procedure and output the main
parameters and the calculated angles
"""

from pathlib import Path
import os

import numpy as np


import teslamax
from teslamax import TeslaMaxPreDesign

B_REM = 1.4
B_MAX = 1.0
B_MIN = 0.0
FIELD_FRACTION = 0.35

teslamax_parameters = {"R_i": 0.015,
                "h_fc": 0.005,
                "R_o": 40e-3,
                "h_gap": 22e-3,
                "R_s" : 100e-3,
                "R_e": 0.3,
                "n_II": 2,
                "n_IV": 4,
                "phi_C_II": 15,
                "mu_r_II": 1.05,
                "mu_r_IV": 1.05,
              "linear_iron": 1,
              "mu_r_iron": 5e3,
                       }

n = teslamax_parameters["n_II"] + teslamax_parameters["n_IV"]

teslamax_parameters = teslamax.expand_parameters_from_remanence_array(
    B_REM*np.ones(n),
    teslamax_parameters,
    "B_rem")

target_function = teslamax.calculate_ramp_profile
target_args = (B_MAX, B_MIN, FIELD_FRACTION)

tmpd = TeslaMaxPreDesign(teslamax_parameters)

alpha_B_rem = tmpd.get_optimal_remanence_angles(
                        target_function,
                        target_args)

K = tmpd.calculate_functional_target(alpha_B_rem_g,
                                    target_function,
                                    target_args)

print("TeslaMax optimization results:")
print("Optimal angles [°]: " + alpha_B_rem)
print("Cost function value: %.3f" % (K, ))