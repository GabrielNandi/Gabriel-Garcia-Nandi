#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
rma.py - Run Mesh Analysis

This script runs the TeslaMax model with various mesh configurations
and plot the results for high field and computing time.

"""

from pathlib import Path

import numpy as np

import nemplot
import teslamax as tm

N_MULTIPLIER = 3
NUMBER_ELEMENTS_FILE_NAME = "noe.txt"
WALL_TIME_FILE_NAME = "wall.txt"
MESH_ANALYSIS_FIGURE_NAME = "rma"

NOE_LABEL = r"Number of mesh elements [\si{\percent}]"
B_HIGH_LABEL = r"Magnetic field at pole center [\si{\percent}]"
TIME_LABEL = r"Computation time [\si{\percent}]"

# Default mesh parameters
L_ELEMENT_MAX_DEFAULT = 0.0111  # [m]
L_ELEMENT_MIN_DEFAULT = 3.75e-5  # [m]
N_NARROW_DEFAULT = 5

RUN_PATH = Path("teslamax-play")
FIG_DIR = Path('.')

CALCULATEQ = True
PLOTQ = False

# Define constant parameters for the TeslaMax model
TESLAMAX_PARAMETERS_REF = {
    "R_i": 15e-3,
    "R_o": 40e-3,
    "R_g": 73e-3,
    "R_s": 120e-3,
    "h_fc": 5e-3,
    "R_e": 300e-3,
    "n_II": 2,
    "phi_C_II": 15,
    "phi_S_II": 45,
    "n_IV": 3,
    "phi_S_IV": 45,
    "mu_r_II": 1.05,
    "mu_r_IV": 1.05,
    "mu_r_iron": 1e3,
    "linear_iron": 1,
    "B_rem_II_1": 1.4,
    "B_rem_II_2": 1.4,
    "B_rem_IV_1": 1.4,
    "B_rem_IV_2": 1.4,
    "B_rem_IV_3": 1.4,
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

# Define where to save the simulation files
nemplot.set_figures_dir(FIG_DIR)
nemplot.set_latex_font("Palatino")
nemplot.set_dpi(800)
nemplot.set_figsize_cm(12)
nemplot.set_fontsize(12)

(noe_ref, time_ref, B_ref, ) = compute_output_values(
    TESLAMAX_PARAMETERS_REF,
    optimizeq=True,
)

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


    # normalize

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

if PLOTQ:

    # load results
    results = np.loadtxt(
        "rma.txt",
        ).T

    noe_normalized_vector = results[0]
    B_normalized_vector = results[1]
    wall_time_normalized_vector = results[2]

    # Create plots of B and t as function of number of elements,
    # with curves for each geometry
    fig, B_ax, t_ax = nemplot.create_two_axes_plot(xlabel=NOE_LABEL,
                                                ylabel_left=B_HIGH_LABEL,
                                                ylabel_right=TIME_LABEL)

    B_ax.plot(noe_normalized_vector, B_normalized_vector, "kx-")
    """ t_ax.plot(
        noe_normalized_vector,
        wall_time_normalized_vector,
        "o--",
        color='gray'
        ) """

    B_ax.grid(True)

    B_ax.set_xticks(noe_normalized_vector)
    nemplot.refine_yticks(B_ax, 4)

    B_ticks = B_ax.get_yticks()

    # add vertical lines representing the limits for the AMR and the magnet
    B_ax.axvline(
        100,
        color='k',
        linestyle='-'
        )

    B_ax.axhline(
        100,
        color='k',
        linestyle='-'
        )

    """ t_ax.set_yticks(B_ticks)
    t_ax.set_ylim(B_ax.get_ylim())

    t_ticklabels = ["%.3f" % (t,) for t in wall_time_vector]
    t_ax.set_yticklabels(t_ticklabels,
                        fontdict={
                            "fontsize": nemplot.nemplot_parameters["FONTSIZE"]
                        }) """

    nemplot.save_figure(fig, MESH_ANALYSIS_FIGURE_NAME)
