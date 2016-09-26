import shutil

TESLAMAX_COMMAND = 'teslamax'

# check to see if the 'teslamax' command is in the PATH
def test_teslamax_in_system_path():

    assert shutil.which(TESLAMAX_COMMAND)
