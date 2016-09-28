import pytest
import teslamax

import conftest

pytestmark = pytest.mark.usefixtures("configure_installation")

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
