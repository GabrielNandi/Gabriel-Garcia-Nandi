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

NOE_LABEL = "Number of mesh elements"
B_HIGH_LABEL = "Maximum Magnetic Field [T]"
TIME_LABEL = "Computation time [s]"

# Default mesh parameters
l_element_min_default = 0.001
l_element_max_default = 0.03
n_narrow_default = 2

# Define constant parameters for the TeslaMax model
teslamax_params = {
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
    "l_element_min": l_element_min_default,
    "l_element_max": l_element_max_default,
    "n_narrow": n_narrow_default
}

# Define variable parameters for the TeslaMax model (R_o, h_gap, R_s)

# Define a range of mesh parameters around default values

n_multiplier = 3
multiplier_vector = np.geomspace(1/(n_multiplier-1),
                                 (n_multiplier-1),
                                 num=n_multiplier)

l_element_min_values = l_element_min_default * multiplier_vector
l_element_max_values = l_element_max_default * multiplier_vector
n_narrow_values = n_narrow_default * multiplier_vector

# Define where to save the simulation files
run_path = Path("teslamax-play")
fig_dir = Path('.')

nemplot.set_figures_dir(fig_dir)

# For each mesh configuration, and for each geometry:
tmpd = tm.TeslaMaxPreDesign(params=teslamax_params)

alpha_optimal = tmpd.get_optimal_remanence_angles(
    target_profile_function=tm.calculate_ramp_profile,
    target_profile_args=(1.0, 0, 0.35)
)

tmm = tm.TeslaMaxModel(tmpd,
                       alpha_optimal,
                       run_path)

n_simulations = n_multiplier**3  # 3 because three variable parameters

noe_vector = np.empty(n_simulations)
wall_time_vector = np.empty(n_simulations)
B_vector = np.empty(n_simulations)

i = 0
params = teslamax_params.copy()
for n_narrow in n_narrow_values:

    params["n_narrow"] = n_narrow

    for l_element_min in l_element_min_values:

        params["l_element_min"] = l_element_min

        for l_element_max in l_element_max_values:

            params["l_element_max"] = l_element_max

            tmpd = tm.TeslaMaxPreDesign(params=params)
            tmm = tm.TeslaMaxModel(tmpd,
                                   alpha_optimal,
                                   run_path)

            tmm.run()

            noe_vector[i] = int(
                (run_path / NUMBER_ELEMENTS_FILE_NAME).read_text()
            )

            wall_time_vector = float(
                (run_path / WALL_TIME_FILE_NAME).read_text()
            )

            B_vector[i] = tmm.get_profile_data()[0, 1]
            i = i+1


# Create plots of B and t as function of number of elements,
# with curves for each geometry
fig, B_ax, t_ax = nemplot.create_two_axes_plot(NOE_LABEL,
                                               B_HIGH_LABEL,
                                               TIME_LABEL)

B_ax.plot(noe_vector, B_vector, "kx-")
t_ax.plot(noe_vector, wall_time_vector, "ko--")

B_ax.grid(True)

B_ax.set_xticks(noe_vector)
nemplot.refine_yticks(B_ax, 4)

B_ticks = B_ax.get_yticks()

t_ax.set_yticks(B_ticks)
t_ax.set_ylim(B_ax.get_ylim())

t_ticklabels = ["%.2f" % (t,) for t in wall_time_vector]
t_ax.set_yticklabels(t_ticklabels,
                     fontdict={
                         "fontsize": nemplot.nemplot_parameters["FONTSIZE"]
                     })

nemplot.save_figure(fig, MESH_ANALYSIS_FIGURE_NAME)
