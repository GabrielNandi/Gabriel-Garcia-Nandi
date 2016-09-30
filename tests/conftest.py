import os.path
import subprocess
import pytest

TESLAMAX_NAME = 'teslamax'

HELP_FLAG = '--help'

HELP_MESSAGE = """Usage: teslamax [--help]

Read 'comsol-parameters.txt' from the current directory and execute the
TeslaMax COMSOL model

Options:

-h, --help    Print this help message and exit
"""

TESLAMAX_COMMAND = 'teslamax'



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

