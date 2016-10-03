import subprocess
import pytest

import conftest

PARAMETER_FILE_NAME = "params.txt"

# I want to see how the teslamax script works. Following standard Unix rules,
# I run 'teslamax --help' from an arbitrary directory and get a helpful message
def test_run_command_with_help_flag_return_message(tmpdir):

    work_dir = tmpdir.strpath

    status_code, return_message = conftest.get_output_from_teslamax_command(work_dir,
                                                                   conftest.HELP_FLAG)

    assert return_message == conftest.HELP_MESSAGE


# I then create a correct parameters file and call the script with no arguments
# The teslamax program name will execute the Java class file and
# return sucessfully
def test_run_command_with_no_flag_return_success(tmpdir):

    work_dir = tmpdir.strpath

    # copy a sample parameter file to work_dir
    param_file_content = """R_i 15[mm]
    R_o 50[mm]
    h_gap 20[mm]
    R_s 140[mm]
    h_fc 20[mm]
    R_e 2[m]
    n_II 3 
    n_IV 3
    alpha_rem_II_1 30
    alpha_rem_II_2 15
    alpha_rem_II_3 -15
    alpha_rem_IV_1 -15
    alpha_rem_IV_2 15
    alpha_rem_IV_3 45
    R_g R_o+h_gap
    phi_S_II 45[deg]
    phi_S_IV 45[deg]
    delta_phi_S_II (phi_S_II)/n_II
    delta_phi_S_IV (phi_S_IV)/n_IV
    B_rem 1.47[T]
    R_c R_s+h_fc"""

    param_file = tmpdir.join(PARAMETER_FILE_NAME)

    param_file.write(param_file_content)    
    
    status_code, return_message = conftest.get_output_from_teslamax_command(work_dir)

    # based on some experimentation, a string "Total time: ..." is the last
    # string to be printed if the COMSOL model is ran successfully
    success_string = "Total time: "

    # make sure that the success string is contained in the last line
    # (hence the split and the -1 index) of the return message
    assert success_string in return_message.strip().split("\n")[-1]
    

# finish writing the tests!
def test_finish_tests():
    assert 0, "Finish the functional tests!"

