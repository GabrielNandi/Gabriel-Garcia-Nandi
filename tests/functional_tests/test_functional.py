import subprocess
import pytest

import conftest

# I want to see how the teslamax script works. Following standard Unix rules,
# I run 'teslamax --help' from an arbitrary directory and get a helpful message
def test_run_command_with_help_flag_return_message(tmpdir):

    work_dir = tmpdir.strpath

    status_code, return_message = conftest.get_output_from_teslamax_command(work_dir,
                                                                   conftest.HELP_FLAG)

    assert return_message == conftest.HELP_MESSAGE


# finish writing the tests!
def test_finish_tests():
    assert 0, "Finish the functional tests!"

