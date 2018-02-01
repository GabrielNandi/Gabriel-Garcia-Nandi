# THIS TEST FILE IS UNFINISHED
# FIXTURES HAVE TO BE DEFINED TO TEST VALUES FOR REMANENCE, TARGET FUNCTIONS AND
# TARGET PARAMETERS (B_HIGH AND F_M)

import pytest
import numpy as np

import teslamax

B_REM_VALES = [1.08,1.38]
B_HIGH_VALUES = [1.0,1.4]
F_M_VALUES = [0.35,0.50]

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

class TestTeslaMaxPreDesign():

    # create one instance of the TeslaMaxPreDesign class for each
    # value in SAMPLE_MAGNET_PARAMETERS
    @pytest.fixture(scope='class',
                    params=SAMPLE_MAGNET_PARAMETERS)
    def tmpd(self,request):
        magnet_parameters = request.param

        n = magnet_parameters["n_II"] + magnet_parameters["n_IV"]

        magnet_parameters = teslamax.expand_parameters_from_remanence_array(
            B_REM * np.ones(n),
            magnet_parameters,
            "B_rem"
        )

        return teslamax.TeslaMaxPreDesign(magnet_parameters)

    def test_calculate_correct_number_angles(self,
                                            tmpd,
                                            function,):

        tmpd.get_optimal_remanence_angles(
                                     target_profile_function=calculate_instantaneous_profile,
                                     target_profile_args=(B_HIGH_LEVEL,)

    def test_calculate_angles_in_correct_range(self,tmpd):

    def test_cost_function_is_real(self):


    def test_cost_function_is_positive(self):






