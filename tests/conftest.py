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

def get_parent_directory():
    """Return the string path of the parent directory of the present file ('..')
    
    """

    return os.path.abspath(
        os.path.join(os.path.dirname(__file__),
                     os.pardir)
        )

def run_pip_install_development(dirname):

    pip_install_arg = ["pip","install","-e", dirname]
    subprocess.run(pip_install_arg)


def install_teslamax():
    """Install the teslamax project in development mode
    
    """

    teslamax_dir = get_parent_directory()
    run_pip_install_development(teslamax_dir)

def uninstall_teslamax():
    pip_uninstall_arg = ["pip", "uninstall",TESLAMAX_NAME]

    subprocess.run(pip_uninstall_arg)

@pytest.fixture(scope="module")
def configure_installation():
    install_teslamax()
    yield

    # procedures after the yield statement will be executed as teardown code
    uninstall_teslamax


    
