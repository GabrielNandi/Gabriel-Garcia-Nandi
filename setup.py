from setuptools import setup

setup(
    name='teslamax',
    version='0.2',
    packages=['teslamax'],
    url='',
    license='',
    author='Fábio Fortkamp',
    author_email='fabio@fabiofortkamp.com',
    description='Design and simulate nested Halbach cylinders with COMSOL',
    long_description=open('README.md').read(),
    install_requires=['numpy',
                      'scipy',
                      'matplotlib',
                      'pandas']
)
