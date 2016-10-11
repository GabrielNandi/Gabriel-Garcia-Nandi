from setuptools import setup, find_packages

package = 'teslamax'
version = '0.6'

setup(name=package,
      version=version,
      description="A script to run the TeslaMax COMSOL model from the command line",
      packages=['teslamax'],
      entry_points={
          'console_scripts': [
              'teslamax = teslamax.__main__:main'
              ]
          },
      license=open('LICENSE').read(),
      author="Fábio Fortkamp")
