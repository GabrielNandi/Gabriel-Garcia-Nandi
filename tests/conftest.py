import os.path
import subprocess
import tempfile
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


PARAMETER_FILE_NAME = "params.txt"

def get_output_from_teslamax_command(work_dir,flag=None):
    """Run the teslamax command from 'work_dir', possibly
    passing 'flag', and return a subprocess.CompletedProcess object
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

    return process

@pytest.fixture()
def teslamax_help_mode():
    """Run the teslamax command with a '--help' in a temporary directory.

    Yields a subprocess.CompletedProcess object; at teardown, the
    temporary directory is deleted.
    """

    with tempfile.TemporaryDirectory() as td:

        teslamax_process = get_output_from_teslamax_command(td,flag='--help')

        yield teslamax_process

@pytest.fixture()
def teslamax_comsol_mode_params():
    """Run the teslamax command with no args in a temporary directory,
    copying a sample (correct) parameter file to it.

    Yields a subprocess.CompletedProcess object; at teardown, the
    temporary directory is deleted.
    """
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

    with tempfile.TemporaryDirectory() as td:

        param_file_path = os.path.join(td,PARAMETER_FILE_NAME)
        param_file = open(param_file_path,'w')
        param_file.write(param_file_content)
        param_file.close()

        teslamax_process = get_output_from_teslamax_command(td)

        yield teslamax_process
    

