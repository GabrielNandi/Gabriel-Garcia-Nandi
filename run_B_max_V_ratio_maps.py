
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

K_CRITICAL = 0.001
OVERWRITE = True
R_MAGNET = 0.5921809896


os.chdir(str(Path.home() / "code" / "TeslaMax"))

params_optimization_ref = {"R_i": 0.030,
                "h_fc": 0.025,
                "R_e": 0.7,
                "n_II": 5,
                "n_IV": 5,
                "phi_C_IV": 0,
                "phi_C_II": 0,
                "phi_S_IV": 60.0,
                "mu_r_II": 1.05,
                "mu_r_IV": 1.05,
              "linear_iron": 1,
              "mu_r_iron": 5e3,
             }
R_i = params_optimization_ref["R_i"]
phi_S_IV = params_optimization_ref["phi_S_IV"]
B_rem = 1.26

phi_S_II = np.array([60, 0])

n = params_optimization_ref["n_II"] + params_optimization_ref["n_IV"]

params_optimization_ref = teslamax.expand_parameters_from_remanence_array(
    B_rem*np.ones(n), 
    params_optimization_ref, 
    "B_rem")

B_min = 0.0
field_fraction = 0.3
params_optimization_ref["F_M[%]"] = field_fraction*100

target_function = teslamax.calculate_ramp_profile

map_file_path = Path("map_B_V_ratio.txt")

### Generate the results file


params_header_str = str(params_optimization_ref).replace(',',',\n') + '\n\n'
print(params_header_str)
if OVERWRITE:
    map_file_path.write_text(params_header_str)


### Update the results file

R_s_min = 300e-3
R_s_max = 350e-3
R_s_step = 50e-3

R_s_values =  np.arange(R_s_min,R_s_max+R_s_step,R_s_step)

V_ratio_values = np.array([10,7,5,3,2,1,0.5])

params = params_optimization_ref.copy()

COLUMNS_NAMES_STR = '\t'.join(['phi_S_II[deg]',
                               'R1[mm]',
                               'R4[mm]',
                               'R2[mm]',
                               'R3[mm]',
                               'V_in[mm2]',
                               'V_out[mm2]',
                               'Volume_ratio (R_v)',
                               'B_max[T]',
                               'K[]\n'])


print(COLUMNS_NAMES_STR)
if OVERWRITE:
    with map_file_path.open(mode='a') as f:
        f.write(COLUMNS_NAMES_STR)

results_str=""
for phi_S2 in phi_S_II:
    print ("SIMULACAO COM phi_S_II IGUAL A %.1f" %(phi_S2))
    params["phi_S_II"] = phi_S2
    for R_s in R_s_values:
        print ("SIMULACAO COM R_s IGUAL A %.3f" %(R_s))
        params["R_s"] = R_s
        for R_v in V_ratio_values:
            print ("SIMULACAO COM R_v IGUAL A %.1f" %(R_v))
            V_out = R_MAGNET*np.pi*(R_s**2)*90/(phi_S_IV + (phi_S2/R_v))
            V_in = R_MAGNET*np.pi*(R_s**2)*90 / (phi_S_IV*R_v + phi_S2)
            R_g = np.sqrt((R_s**2)-(V_out/(np.pi)))
            R_o = np.sqrt((V_in/(np.pi))+(R_i**2))
            params["R_g"] = R_g
            params["R_o"] = R_o
            print ("SIMULACAO COM R_g IGUAL A %.3f" %(R_g))
            print ("SIMULACAO COM R_o IGUAL A %.3f" %(R_o))
            K = 1e3
            K1 = 1e4
            B1 = 1.7
            B = 1.9
            B_max_step = 0.2
            
            while (K > K_CRITICAL):
                if K < 0.01:
                    if K > 0.007:
                        B_max_step = 0.03
                    else:
                        if K<0.0025:
                            if K<0.0015:
                                B_max_step = 0.005
                            else:
                                B_max_step = 0.01
                        else: B_max_step = 0.02
                else:
                    if K > 0.05:
                        B_max_step = 0.2
                    else: B_max_step = 0.1
                    
                B = B - B_max_step
                if B<=0.2:
                    break
                else: 
                    try:
                        
                        target_args = (B,B_min,field_fraction)
                        print ("SIMULACAO COM B IGUAL A %.4f" %(B))
                        print (target_args)
                        tmpd = TeslaMaxPreDesign(params)
                        alpha_B_rem_g = tmpd.get_optimal_remanence_angles(
                            target_function,
                            target_args)
                        print (alpha_B_rem_g)
                        K = tmpd.calculate_functional_target(alpha_B_rem_g,
                                                             target_function,
                                                             target_args)            
                        print(K)
                        if K < K1:
                            K1 = K
                            B1 = B
                        results_str = "%.3f\t%.3f\t%.3f\t%.3f\t%.7f%.7f%.1f%.7f%.6f%" %(
                                R_i,
                                R_s,
                                R_o,
                                R_g,
                                V_in,
                                V_out,
                                R_v,
                                B1,
                                K1)
                        
    
                        results_str = results_str + "\n"
                    except:
                        continue
            print (results_str)
            with map_file_path.open(mode='a',buffering=1) as f:
                f.write(results_str)
                f.flush()
                os.fsync(f.fileno())
                    
                    
                    
            
    

                        
            
