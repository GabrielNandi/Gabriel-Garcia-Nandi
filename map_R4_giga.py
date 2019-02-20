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

K_CRITICAL = 0.0006
B_rem = 1.26
B_min = 0.0
field_fraction = 0.3
os.chdir(str(Path.home() / "code" / "TeslaMax"))

wb = ex.load_workbook("Planejamento das simulações - Modelo Analítico - NOVO.xlsx")
ws = wb['Simulações_Modelo']

params_optimization_ref = {"h_fc": 0.025,
                "R_e": 0.8,
                "n_II": 0,
                "n_IV": 5,
                "phi_C_IV": 0.0,
                "phi_C_II": 0.0,
                "phi_S_II": 0.0,
                "phi_S_IV": 60.0,
                "mu_r_II": 1.05,
                "mu_r_IV": 1.05,
              "linear_iron": 1,
              "mu_r_iron": 5e3,
             }
n = int(params_optimization_ref["n_II"] + params_optimization_ref["n_IV"])
params_optimization_ref = teslamax.expand_parameters_from_remanence_array(
    B_rem*np.ones(n), 
    params_optimization_ref, 
    "B_rem")

params_optimization_ref["F_M[%]"] = field_fraction*100
params = params_optimization_ref.copy()

target_function = teslamax.calculate_ramp_profile

for i in range(9,99):
    wb = ex.load_workbook("Planejamento das simulações - Modelo Analítico - NOVO.xlsx")
    ws = wb['Simulações_Modelo']
    R_i = ws.cell(row=i, column=2).value*0.001
    R_o = ws.cell(row=i, column=3).value*0.001
    R_g = ws.cell(row=i, column=4).value*0.001
    B_max = ws.cell(row=i, column=5).value
    params["R_g"] = R_g
    params["R_o"] = R_o
    params["R_i"] = R_i
    target_args = (B_max, B_min, field_fraction)
    print ("SIMULACAO COM B_max IGUAL A %.3f" %(B_max))
    print ("SIMULACAO COM R1 IGUAL A %.1f" %(R_i*1000))
    print ("SIMULACAO COM R2 IGUAL A %.1f" %(R_o*1000))
    print ("SIMULACAO COM R3 IGUAL A %.2f" %(R_g*1000))
    K=1e3
    K1=1e4
    R_s = R_g
    dRs=10e-3
    RS1 = R_s
    while (K > K_CRITICAL):
        if K == 1e3:
            dRs = 5e-3
        else:
            if K >= 0.2:
                dRs = 0.025
            else:
                if K>= 0.1:
                    dRs = 0.02
                else:
                    if K >= 0.05:
                        dRs = 0.015
                    else:
                        if K>= 0.01:
                            dRs= 0.011
                        else:
                            if K> 0.005:
                                dRs = 0.006
                            else: 
                                if K>0.002:
                                    dRs = 0.003
                                else:
                                    if K > 0.0015:
                                        dRs = 0.002
                                    else: 
                                        if K > 0.008:
                                            dRs = 0.001
                                        else: dRs = 0.0005

        R_s = R_s + dRs
        print ("SIMULACAO COM R4 IGUAL A %.3f" %(R_s*1000))
        params["R_s"] = R_s
        if R_s >= 4*R_g:
            break
        else: 
            try:
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
                    RS1 = R_s
            except:
                print ("NAO SIMULOU")
                continue
           
    RS2 = RS1*1000
    ws.cell(row=i, column=12).value = "%.4f" %RS2
    ws.cell(row=i, column=13).value = "%.8f" %K1
    wb.save("Planejamento das simulações - Modelo Analítico - NOVO.xlsx")
