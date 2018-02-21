from collections import namedtuple

import math

import pytest
import numpy as np

import teslamax

SAMPLE_MAGNET_PARAMETERS = [
    {"R_i": 10e-3,
    "h_fc": 5e-3,
    "R_o": 40e-3,
    "h_gap": 20e-3,
     "R_s": 100e-3,
    "R_e": 0.3,
    "n_II": 2,
    "n_IV": 4,
    "phi_C_II": 15,
     "phi_S_II": 45,
     "phi_S_IV": 45,
    "mu_r_II": 1.05,
    "mu_r_IV": 1.05,
    "linear_iron": 1,
    "mu_r_iron": 5e3,
    },
    {"R_i": 10e-3,
     "h_fc": 5e-3,
     "R_o": 30e-3,
     "h_gap": 22e-3,
     "R_s": 100e-3,
     "R_e": 0.3,
     "n_II": 0,
     "n_IV": 4,
     "phi_C_II": 0,
     "phi_S_II": 0,
     "phi_S_IV": 45,
     "mu_r_II": 1.05,
     "mu_r_IV": 1.05,
     "linear_iron": 1,
     "mu_r_iron": 3e3,
     },
    {"R_i": 15e-3,
     "h_fc": 15e-3,
     "R_o": 70e-3,
     "h_gap": 30e-3,
     "R_s": 150e-3,
     "R_e": 0.3,
     "n_II": 2,
     "n_IV": 3,
     "phi_C_II": 10,
     "phi_S_II": 45,
     "phi_S_IV": 45,
     "mu_r_II": 1.05,
     "mu_r_IV": 1.05,
     "linear_iron": 1,
     "mu_r_iron": 5e3,
     },
    {"R_i": 15e-3,
     "h_fc": 10e-3,
     "R_o": 50e-3,
     "h_gap": 18e-3,
     "R_s": 95e-3,
     "R_e": 0.5,
     "n_II": 0,
     "n_IV": 3,
     "phi_C_II": 0,
     "phi_S_II": 0,
     "phi_S_IV": 55,
     "mu_r_II": 1.05,
     "mu_r_IV": 1.05,
     "linear_iron": 0,
     },
]

B_REM_VALUES = [1.08, 1.38]
B_MAX_VALUES = [1.0, 1.4]
B_MIN_VALUES = [0, 0.05]
F_M_VALUES = [0.35, 0.50]
TARGET_FUNCTION_NAMES = [teslamax.calculate_instantaneous_profile,
                         teslamax.calculate_ramp_profile]

OptimizationCase = namedtuple("OptimizationCase",[
    "TeslaMaxPreDesign",
    "function",
    "args"
])


def build_tmpd(params,B_rem):

    n = params["n_II"] + params["n_IV"]

    magnet_params = teslamax.expand_parameters_from_remanence_array(
        B_rem * np.ones(n),
        params,
        "B_rem"
    )

    return teslamax.TeslaMaxPreDesign(magnet_params)

optimization_cases = [OptimizationCase(
    build_tmpd(params,B_rem),
    function,
    (B_max,B_min,F_M)) for params in SAMPLE_MAGNET_PARAMETERS
                        for function in TARGET_FUNCTION_NAMES
                      for B_rem in B_REM_VALUES
                      for B_max in B_MAX_VALUES
                      for B_min in B_MIN_VALUES
                      for F_M in F_M_VALUES]

class TestTeslaMaxPreDesign():


    @pytest.fixture(scope='class',
                    params=optimization_cases)
    def optcase(self,request):

        return request.param


    def test_calculate_correct_number_angles(self,
                                            optcase):


        tmpd = optcase.TeslaMaxPreDesign
        target_function = optcase.function
        target_args = tuple(optcase.args)

        angles = tmpd.get_optimal_remanence_angles(target_function,target_args)

        magnet_parameters = tmpd.geometry_material_parameters

        n = magnet_parameters["n_II"] + magnet_parameters["n_IV"]

        assert n == len(angles)

    def test_calculate_angles_in_correct_range(self,
                                               optcase):

        tmpd = optcase.TeslaMaxPreDesign
        target_function = optcase.function
        target_args = optcase.args

        angles = tmpd.get_optimal_remanence_angles(target_function,target_args)

        for alpha in angles:
            assert ((alpha >= 0) and (alpha <= 360))

    def test_cost_function_is_finite(self,
                                   optcase,):

        tmpd = optcase.TeslaMaxPreDesign
        target_function = optcase.function
        target_args = optcase.args

        angles = tmpd.get_optimal_remanence_angles(target_function, target_args)

        K = tmpd.calculate_functional_target(angles,target_function,target_args)

        assert math.isfinite(K)

    def test_cost_function_is_positive(self,
                                       optcase):

        tmpd = optcase.TeslaMaxPreDesign
        target_function = optcase.function
        target_args = optcase.args

        angles = tmpd.get_optimal_remanence_angles(target_function, target_args)

        K = tmpd.calculate_functional_target(angles, target_function,
                                             target_args)

        assert K >= 0







