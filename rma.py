#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
rma.py - Run Mesh Analysis

This script runs the TeslaMax model with various mesh configurations
and plot the results for high field and computing time.

This generates a file "rma.txt", where each line is the number of
mesh elements, the magnetic field at the pole center and the computing time,
all in percentage of the values obtained with the default mesh.

The model parameters were taken from the final 2D optimization

"""

from pathlib import Path

import numpy as np

import teslamax as tm

N_MULTIPLIER = 3
NUMBER_ELEMENTS_FILE_NAME = "noe.txt"
WALL_TIME_FILE_NAME = "wall.txt"
MESH_ANALYSIS_FIGURE_NAME = "rma"


# Default mesh parameters
L_ELEMENT_MAX_DEFAULT = 0.0111  # [m]
L_ELEMENT_MIN_DEFAULT = 3.75e-5  # [m]
N_NARROW_DEFAULT = 5

RUN_PATH = Path("teslamax-play")
FIG_DIR = Path('.')

CALCULATEQ = True

# Define constant parameters for the TeslaMax model
TESLAMAX_PARAMETERS_REF = {
    "R_i": 15e-3,
    "R_o": 40e-3,
    "R_g": 62e-3,
    "R_s": 120e-3,
    "h_fc": 5e-3,
    "R_e": 300e-3,
    "n_II": 0,
    "phi_C_II": 15,
    "phi_S_II": 45,
    "n_IV": 4,
    "phi_S_IV": 45,
    "mu_r_II": 1.05,
    "mu_r_IV": 1.05,
    "mu_r_iron": 1e3,
    "linear_iron": 1,
    "B_rem_IV_1": 1.4,
    "B_rem_IV_2": 1.4,
    "B_rem_IV_3": 1.4,
    "B_rem_IV_4": 1.4,
    "Hc_j": 1000e3,
    "l_element_min": L_ELEMENT_MIN_DEFAULT,
    "l_element_max": L_ELEMENT_MAX_DEFAULT,
    "n_narrow": N_NARROW_DEFAULT
}

alpha_optimal = None


def compute_output_values(params, optimizeq=False):

    tmpd = tm.TeslaMaxPreDesign(params)

    global alpha_optimal
    if optimizeq:
        alpha_optimal = tmpd.get_optimal_remanence_angles(
            target_profile_function=tm.calculate_ramp_profile,
            target_profile_args=(1.0, 0, 0.35)
        )

    tmm = tm.TeslaMaxModel(
        tmpd,
        alpha_optimal,
        RUN_PATH
    )

    tmm.run()

    noe = int(
                (RUN_PATH / NUMBER_ELEMENTS_FILE_NAME).read_text()
            )

    wall_time = float(
                (RUN_PATH / WALL_TIME_FILE_NAME).read_text()
            )

    B = tmm.get_profile_data()[1, 0]

    return (noe, wall_time, B)

# Define variable parameters for the TeslaMax model (R_o, h_gap, R_s)

# with this magnetic circuit and the range of parameters used in my thesis,
# the biggest impact seem to be with the narrow region parameters

# Define a range of mesh parameters around default values
l_element_max_values = [L_ELEMENT_MAX_DEFAULT, ]
l_element_min_values = [L_ELEMENT_MIN_DEFAULT, ]
n_narrow_values = np.array([1, 2, 5, 10])

# For each mesh configuration, and for each geometry:
n_simulations = (len(l_element_max_values) *
                 len(l_element_min_values) *
                 len(n_narrow_values))

noe_vector = np.empty(n_simulations)
wall_time_vector = np.empty(n_simulations)
B_vector = np.empty(n_simulations)

i = 0
params = TESLAMAX_PARAMETERS_REF.copy()

if CALCULATEQ:

    (noe_ref, time_ref, B_ref, ) = compute_output_values(
        TESLAMAX_PARAMETERS_REF,
        optimizeq=True,
    )

    for n_narrow in n_narrow_values:

        params["n_narrow"] = n_narrow

        for l_element_min in l_element_min_values:

            params["l_element_min"] = l_element_min

            for l_element_max in l_element_max_values:

                params["l_element_max"] = l_element_max

                output_values = compute_output_values(
                    params,
                    optimizeq=False,
                )

                noe_vector[i], wall_time_vector[i], B_vector[i] = output_values
                i = i+1

    #  normalize

    noe_normalized_vector = noe_vector / noe_ref * 100
    wall_time_normalized_vector = wall_time_vector / time_ref * 100
    B_normalized_vector = B_vector / B_ref * 100

    # save results
    np.savetxt(
        "rma.txt",
        np.array(
            [
                noe_normalized_vector,
                B_normalized_vector,
                wall_time_normalized_vector,
            ]
        ),
        )
