import subprocess
import pytest

HELP_FLAG = '--help'

HELP_MESSAGE = """Usage: teslamax [--help]

Read 'comsol-parameters.txt' from the current directory and execute the
TeslaMax COMSOL model

Options:

-h, --help    Print this help message and exit
"""

TESLAMAX_COMMAND = 'teslamax'

pytestmark = pytest.mark.usefixtures("configure_installation")

def get_output_from_teslamax_command(work_dir,flag=None):
    """Run the teslamax command from 'work_dir', possibly
    passing 'flag', and return a tuple (status_code, output_message)
    """

    teslamax_args = [TESLAMAX_COMMAND]

    if flag:
        teslamax_args.append(flag)

    process = subprocess.run(teslamax_args,
                             stdout=subprocess.PIPE,
                             stderr=subprocess.STDOUT,
                             universal_newlines=True,
                             cwd=work_dir,
                             shell=True)

    return (process.returncode, process.stdout)

    


# I want to see how the teslamax script works. Following standard Unix rules,
# I run 'teslamax --help' from an arbitrary directory and get a helpful message
def test_run_command_with_help_flag_return_message(tmpdir):

    work_dir = tmpdir.strpath

    status_code, return_message = get_output_from_teslamax_command(work_dir,
                                                                   HELP_FLAG)

    assert return_message == HELP_MESSAGE


# finish writing the tests!
def test_finish_tests():
    assert 0, "Finish the functional tests!"

