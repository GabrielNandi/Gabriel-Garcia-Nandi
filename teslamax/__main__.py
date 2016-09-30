"""Usage: teslamax [--help]

Read 'comsol-parameters.txt' from the current directory and execute the
TeslaMax COMSOL model

Options:

-h, --help    Print this help message and exit
"""

from docopt import docopt

def main():

    arguments = docopt(__doc__)


if __name__ == '__main__':
    main()
