import pytest

import conftest
import teslamax

# check that the teslamax program has the correct docstring
def test_teslamax_program_has_correct_doctring():
    assert teslamax.__doc__ == conftest.HELP_MESSAGE

# check that, if the program gets the help flag as argument,
# it will print its doctring
def test_teslamax_print_doctring_with_help_argument(tmpdir):

    work_dir = tmpdir.strpath

    status_code, return_message = conftest.get_output_from_teslamax_command(work_dir,
                                                                   conftest.HELP_FLAG)

    assert return_message == teslamax.__doc__

# check that, if the program gets the help flag as argument,
# it will exit with success
def test_teslamax_exit_sucess_with_help_argument(tmpdir):

    work_dir = tmpdir.strpath

    status_code, return_message = conftest.get_output_from_teslamax_command(work_dir,
                                                                   conftest.HELP_FLAG)

    assert status_code == 0

# if no argument is passed, the docstring should not be printed
def test_teslamax_doesnt_print_doctring_with_no_argument(tmpdir):

    work_dir = tmpdir.strpath

    status_code, return_message = conftest.get_output_from_teslamax_command(work_dir)

    assert return_message != teslamax.__doc__

# if no argument is passed, the COMSOL batch command should be called

# if no argument is passed, the correct COMSOL class file should be passed to
# the batch command



