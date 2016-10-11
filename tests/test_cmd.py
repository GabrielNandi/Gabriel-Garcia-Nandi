import subprocess
import pytest

import conftest


# I want to see how the teslamax script works. Following standard Unix rules,
# I run 'teslamax --help' from an arbitrary directory and get a helpful message
def test_run_command_with_help_flag_return_message(teslamax_help_mode):


    return_message = teslamax_help_mode.return_message
    
    assert return_message == conftest.HELP_MESSAGE


# I then create a correct parameters file and call the script with no arguments
# The teslamax program name will execute the Java class file and
# return sucessfully
def test_run_command_with_no_flag_return_success(teslamax_comsol_mode_params):

    return_message = teslamax_comsol_mode_params.return_message

    # based on some experimentation, there is a string to be printed
    # if the COMSOL model is ran successfully
    success_string = "Current Progress: 100 % - Done"

    # make sure that the success string is contained in the last line
    # (hence the split and the -1 index) of the return message
    assert success_string in return_message.strip().split("\n")[-2]


