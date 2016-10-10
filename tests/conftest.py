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

class TeslaMaxProcess:
    """
    Represents the result of running the teslamax program
    in a given directory.
    """

    def __init__(self,flag,cwd,return_code,return_message):
        
        self.flag = flag
        self.cwd = cwd
        self.return_code = return_code
        self.return_message = return_message
        
    


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

    teslamax_process = TeslaMaxProcess(flag="",
                                       cwd=work_dir,
                                       return_code=process.returncode,
                                       return_message=process.stdout)

    return teslamax_process

@pytest.fixture(scope="module")
def teslamax_help_mode():
    """Run the teslamax command with a '--help' in a temporary directory.

    Yields a subprocess.CompletedProcess object; at teardown, the
    temporary directory is deleted.
    """

    with tempfile.TemporaryDirectory() as td:

        teslamax_process = get_output_from_teslamax_command(td,flag='--help')

        yield teslamax_process

@pytest.fixture(scope="module")
def teslamax_comsol_mode_params():
    """Run the teslamax command with no args in a temporary directory,
    copying a sample (correct) parameter file to it.

    Yields a subprocess.CompletedProcess object; at teardown, the
    temporary directory is deleted.
    """
    # copy a sample parameter file to work_dir
    param_file_content = "\n".join(["R_i 0.015",
                                    "R_o 0.050",
                                    "h_gap 0.020",
                                    "R_s 0.140",
                                    "h_fc 0.020",
                                    "R_e 2",
                                    "n_II 3",
                                    "n_IV 3",
                                    "alpha_rem_II_1 30",
                                    "alpha_rem_II_2 15",
                                    "alpha_rem_II_3 -15",
                                    "alpha_rem_IV_1 -15",
                                    "alpha_rem_IV_2 15",
                                    "alpha_rem_IV_3 45",
                                    "R_g 0.070",
                                    "phi_S_II 45",
                                    "phi_S_IV 45",
                                    "delta_phi_S_II 15",
                                    "delta_phi_S_IV 15",
                                    "B_rem 1.47",
                                    "R_c 0.160"])
    print(param_file_content)

    with tempfile.TemporaryDirectory() as td:

        param_file_path = os.path.join(td,PARAMETER_FILE_NAME)
        param_file = open(param_file_path,'w')
        param_file.write(param_file_content)
        param_file.close()

        teslamax_process = get_output_from_teslamax_command(td)

        yield teslamax_process
    

