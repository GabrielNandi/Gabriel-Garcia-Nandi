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
os.chdir(str(Path.home() / "code" / "TeslaMax"))

wb = ex.load_workbook("Planejamento das simulações - Modelo Analítico - NOVO.xlsx")
ws = wb['Simulações_Modelo']
R_i = ws.cell(row=4, column=2).value*0.001
print (R_i)
RS2 = 165
ws.cell(row=4, column=12).value = "%.4f" %RS2
wb.save("Planejamento das simulações - Modelo Analítico - NOVO.xlsx")
