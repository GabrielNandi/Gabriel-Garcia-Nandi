import subprocess
import pytest

import conftest

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

    # based on some experimentation, a string "Total time: ..." is the last
    # string to be printed if the COMSOL model is ran successfully

    work_dir = tmpdir.strpath
    
    status_code, return_message = conftest.get_output_from_teslamax_command(work_dir)

    success_string = "Total time: "

    # make sure that the success string is contained in the last line
    # (hence the split and the -1 index) of the return message
    assert success_string in return_message.split()[-1]
    

# finish writing the tests!
def test_finish_tests():
    assert 0, "Finish the functional tests!"

