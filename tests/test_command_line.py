import shutil

TESLAMAX_COMMAND = 'teslamax'

# check to see if the 'teslamax' command is in the PATH
def test_teslamax_in_system_path():

    install_message = "Make sure to install teslamax by running " + \
                      "'pip install .' from the main directory"

    assert shutil.which(TESLAMAX_COMMAND), install_message

# check that the teslamax program has the correct docstring

# check that, if the program gets the help flag as argument,
# it will print the docstring

# check that, if the program gets the help flag as argument,
# it will exit immediately
    
