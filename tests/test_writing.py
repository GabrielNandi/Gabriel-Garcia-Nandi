import os.path
import pytest
import numpy as np
import pandas as pd

# with a correct parameters file, two files, 'B_high.txt' and 'B_low.txt'
# are created
def test_B_files_are_written_in_comsol_mode(teslamax_comsol_mode_params):
    
    B_files = ["B_high.txt", "B_low.txt"]

    work_dir = teslamax_comsol_mode_params.cwd

    for B_file in B_files:
        assert os.path.exists(os.path.join(work_dir,B_file))
        
# with a correct parameters file, a summary file, 'COMSOL Main Results.txt' is
# created
def test_results_file_is_written_in_comsol_mode(teslamax_comsol_mode_params):
    
    B_files = ['COMSOL Main Results.txt']

    work_dir = teslamax_comsol_mode_params.cwd
    
    for B_file in B_files:
        assert os.path.exists(os.path.join(work_dir,B_file))
       

# the results file should be table like, with fields for the magnet and
# gap volumes and for the average field in the high and low field regions
def test_results_file_has_correct_fields(teslamax_comsol_mode_params):

    work_dir = teslamax_comsol_mode_params.cwd

    results_file = os.path.join(work_dir,"COMSOL Main Results.txt")

    fields = ["A_magnet[m2]",
              "A_gap[m2]",
              "B_high[T]",
              "B_low[T]"]

    results = pd.read_table(results_file,
                            sep=" ")

    for field in fields:
        assert results[field].all()

    


# with a correct parameters file, a file 'COMSOL Magnetic Profile.txt'
# is created
def test_profile_is_written_in_comsol_mode(teslamax_comsol_mode_params):
    
    B_files = ["COMSOL Magnetic Profile.txt"]

    work_dir = teslamax_comsol_mode_params.cwd

    for B_file in B_files:
        assert os.path.exists(os.path.join(work_dir,B_file))
       

# the parameter file has two columns, "phi[deg]" and "B[T]"
def test_profile_has_correct_columns_in_comsol_mode(teslamax_comsol_mode_params):
    
    work_dir = teslamax_comsol_mode_params.cwd
    profile_file_path = os.path.join(work_dir,"COMSOL Magnetic Profile.txt")
    
    with open(profile_file_path) as f:
        profile_header = f.readline().strip().split()
        
    assert profile_header == ["phi[deg]","B[T]"]

def test_profile_has_correct_shape_in_comsol_mode(teslamax_comsol_mode_params):
    
    work_dir = teslamax_comsol_mode_params.cwd
    profile_file_path = os.path.join(work_dir,"COMSOL Magnetic Profile.txt")
    
    profile_data = np.loadtxt(profile_file_path,delimiter=" ",skiprows=1)
    
    assert profile_data.shape[1] == 2

def test_profile_has_real_number_in_comsol_mode(teslamax_comsol_mode_params):

    work_dir = teslamax_comsol_mode_params.cwd
    profile_file_path = os.path.join(work_dir,"COMSOL Magnetic Profile.txt")
    
    profile_data = np.loadtxt(profile_file_path,delimiter=" ",skiprows=1)
    
    assert np.isreal(profile_data).all()
