# -*- coding: utf-8 -*-
"""
Created on Fri Feb 15 12:55:28 2019

@author: Luis Felipe
"""
import openpyxl as ex
import os
from pathlib import Path
import numpy as np
import teslamax
from teslamax import TeslaMaxGeometry, TeslaMaxPreDesign, TeslaMaxModel
from scipy.integrate import trapz

B = []
phi = []
R4 = 160
R3 = 100
R2 = 80
B_high = 1e3
field_fraction = 0.3
sdir = RAMP_RESULTS_DIR_FMT %(B_high*1e3, field_fraction*1e2)
teslamax_playground = TESLAMAX_PATH / PLAYGROUND_DIR / sdir
profile_file = teslamax_playground / teslamax.MAGNETIC_PROFILE_FILENAME
phi, B = teslamax.read_comsol_profile_data(str(profile_file))
W  = (R3**2 - R2**2)*0.5*trapz(B**2,phi)
W_mag = 51870.96197/1.05
M_ef = 4*W / W_mag
print (W)
print (M_ef)