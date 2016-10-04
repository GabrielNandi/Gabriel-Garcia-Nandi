"""Usage: teslamax [--help]

Read 'comsol-parameters.txt' from the current directory and execute the
TeslaMax COMSOL model

Options:

-h, --help    Print this help message and exit
"""

import subprocess
import os.path
import sys
from pathlib import Path
from docopt import docopt

B_HIGH_FILENAME = "B_high.txt"
B_LOW_FILENAME = "B_low.txt"

MAIN_RESULTS_FILENAME = "COMSOL Main Results.txt"

MAGNETIC_PROFILE_FILENAME = "COMSOL Magnetic Profile.txt"

def get_teslamax_class_file_path():
    """Return the path string to the main TeslaMax java class file.
    
    """

    # the class file is located in a 'java' directory,
    # in the parent directory of the present file

    java_dir = os.path.join(os.path.dirname(__file__),
                            os.pardir,
                            "java")
    class_file = os.path.join(java_dir,"TeslaMax.class")
    return os.path.realpath(class_file)

def write_main_results_file():
    """Create a file "COMSOL Main Results.txt" in the current directory,
    assuming the teslamax command was already ran
    
    """

    p = Path(MAIN_RESULTS_FILENAME)
    p.touch()
    
    

def run_comsol_mode():
    """Run the TeslaMax COMSOL Java model from the current directory
    
    """

    # locate the .class file to be executed
    teslamax_class_file_path = get_teslamax_class_file_path()

    # call the comsol batch command from it
    comsol_cmd_args = ["comsolbatch", "-inputfile", teslamax_class_file_path]
    
    comsol_cmd_process = subprocess.run(comsol_cmd_args,
                                        shell=True,
                                        stdout=subprocess.PIPE,
                                        stderr=subprocess.STDOUT,
                                        universal_newlines=True)

    print(comsol_cmd_process.stdout,end='')

    write_main_results_file()
    write_magnetic_profile_file()
    
    sys.exit(comsol_cmd_process.returncode)

def main():

    arguments = docopt(__doc__)

    help_mode_q = arguments['--help']

    if not help_mode_q:

        run_comsol_mode()


if __name__ == '__main__':
    main()
