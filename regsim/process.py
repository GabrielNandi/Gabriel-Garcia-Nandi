import re
import numpy as np
import pandas as pd

FREQUENCY_COLUMN_NAME = 'f[Hz]'
UTILIZATION_COLUMN_NAME = 'U_HB[-]'
H_GAP_COLUMN_NAME = 'h_gap[m]'
H_GAP_MM_COLUMN_NAME = 'h_gap[mm]'
HEIGHT_COLUMN_NAME = 'H[m]'
WIDTH_COLUMN_NAME = 'W[m]'
LENGTH_COLUMN_NAME = 'L[m]'
NR_COLUMN_NAME = 'N_reg[-]'
COOLING_CAPACITY_COLUMN_NAME = 'Qc[W]'
COP_COLUMN_NAME = 'COP[-]'
T_INSULATION_COLUMN_NAME = 't_insulation[m]'
R_I_COLUMN_NAME = 'R_i[m]'
R_O_COLUMN_NAME = 'R_o[m]'
R_S_COLUMN_NAME = 'R_s[m]'
R_G_COLUMN_NAME = 'R_g[m]'

H_GAP_LABEL =  r'$\hgap\,[\si{\milli\meter}]$'
MASS_GD_LABEL = r'$m\ped{Gd}\,[\si{\gram}]$'
AVERAGE_FIELD_LABEL = r'$\average{B}\,[\si{\tesla}]$'



def normalize_length_units(table):
    """
    Return a new dict-like table. For every key in the pattern 'name[mm]', a 
    new key 'name[m]' will be added, with the appropriate unit conversion.
    """
    
    pattern_string = r'(\w*)\[(\w*)\]'
    pattern = re.compile(pattern_string)

    columns_mm = [column for column in table if "[mm]" in column]
    for column_mm in columns_mm:
        # select the first group from pattern_string,
        # e.g. 'L[mm]' will result in a list ['L','mm']
        name = pattern.match(column_mm).groups()[0]
        
        name_with_unit_m = name + '[m]'
        table[name_with_unit_m] = 1e-3 * table[column_mm]
        
    return table

def expand_regsim_table(table_filename,params):
    """
    Read a tab-separated file 'table_filename' and return a pandas DataFrame from it.
    Included aditional constant-value columns from 'params'
    All columns with units in mm will be converted to m.
    """
    
    table = pd.read_csv(table_filename,sep='\t')
    
    for (key,value) in params.items():
        table[key] = value

    table = normalize_length_units(table)
    
    table[HEIGHT_COLUMN_NAME] = table[H_GAP_COLUMN_NAME] - 2*table[T_INSULATION_COLUMN_NAME]
    
    # normally either the pair (h_gap, R_o) or (h_gap, R_g) is included either in the 
    # original table or in the 'params' dictionary
    # we include the missing column
    
    if H_GAP_COLUMN_NAME in table:
        
        if R_O_COLUMN_NAME in table:
            table[R_G_COLUMN_NAME] = table[R_O_COLUMN_NAME] + table[H_GAP_COLUMN_NAME]
        elif R_G_COLUMN_NAME in table:
            table[R_O_COLUMN_NAME] = table[R_G_COLUMN_NAME] - table[H_GAP_COLUMN_NAME]
    
    return table

def remove_units_from_dict_keys(dictionary):
    """Remove a string '[<anything>]' from every key of 'dictionary'"""
    
    new_dictionary = {}
    
    for key in dictionary.keys():
        new_key = key.split('[')[0]
        new_dictionary[new_key] = dictionary[key]
        
    return new_dictionary

def filter_table_from_column(table,column,value):
    """
    Return a view into the 'table' DataFrame, selecting only the rows where
    'column' equals 'value'
    """
    
    return table[table[column] == value]
