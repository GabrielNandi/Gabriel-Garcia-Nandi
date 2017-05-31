# coding: utf-8

from pathlib import Path
import os
import subprocess
import shutil
import math
import tempfile
from pandas import Series, DataFrame
import pandas as pd
import numpy as np
import numpy.ma as ma
import matplotlib.pyplot as plt
from matplotlib.patches import Wedge
from scipy.interpolate import  (NearestNDInterpolator, LinearNDInterpolator,
CloughTocher2DInterpolator,griddata, interp1d)
from scipy.integrate import trapz, simps, quad
from scipy.constants import mu_0

COMSOL_JAVA_DIR = Path.home() / 'code' / 'teslamax' / 'java'

COMSOL_CLASS_FILE = COMSOL_JAVA_DIR / 'TeslaMax.class'

COMSOL_CMD = ['comsolbatch', '-inputfile', str(COMSOL_CLASS_FILE)]


B_HIGH_FILENAME = "B_high.txt"
B_LOW_FILENAME = "B_low.txt"

B_III_FILENAME = "B_III.txt"

H_IV_FILENAME = "H_IV_1Q.txt"

MAIN_RESULTS_FILENAME = "COMSOL Main Results.txt"

MAGNETIC_PROFILE_FILENAME = "COMSOL Magnetic Profile.txt"

COMSOL_PARAMETER_FILENAME = "params.txt"

TESLAMAX_RESULTS_FILENAME = "TeslaMax Main Results.txt"
TESLAMAX_RESULTS_RANDOM_FILENAME = "TeslaMax Main Results (Random).txt"

N_PROFILE_POINTS = 100

N_POINTS_PER_AXIS = 400

FIGSIZE_CM = 20
FIGSIZE_INCHES = FIGSIZE_CM / 2.54

FONTSIZE = 20

TARGET_PROFILE = 1.3

DEBUG = True

def get_comsol_parameters_series():
    """Parse the COMSOL parameters file in the current directory and
    return a pandas Series from it.
    
    """
    param_comsol_file = Path('.') / COMSOL_PARAMETER_FILENAME

    param_comsol_series = pd.read_table(str(param_comsol_file),
                                        squeeze=True,
                                        sep=" ",
                                        index_col=0,
                                        header=None)

    param_comsol_series.name = "COMSOL Parameters"
    param_comsol_series.index.name = None
    
    # append the units to the parameters names
    names_with_units = {}
    for name in param_comsol_series.keys():
        if name.startswith("h_") or name.startswith("R_"):
            names_with_units[name] = name+"[m]"
        if name.startswith("alpha") or name.startswith("phi") or name.startswith("delta_phi"):
            names_with_units[name] = name + "[deg]"
        if name.startswith("B_"):
            names_with_units[name] = name + "[T]"
        if name.startswith("H_c"):
            names_with_units[name] = name + "[A/m]"

    param_comsol_series = param_comsol_series.rename(names_with_units)
    return param_comsol_series

def read_comsol_data_file(filename):
    """Read and parse 'filename' as exported by COMSOL.
    Export the numerical data as a numpy array containing only the numerical
    data; the first two columns are x and y values. All values are in SI.
    
    Keyword Arguments:
    filename -- str
    """

    return np.loadtxt(filename,skiprows=9)

def process_main_results_file():
    """Take the file "COMSOL Main Results.txt" as exported by COMSOL and
    clean the header data.
    
    """
    
    p = Path('.') / MAIN_RESULTS_FILENAME

    param_comsol_series = get_comsol_parameters_series()

    results = pd.read_table(MAIN_RESULTS_FILENAME,
                            sep="\s+",
                            skiprows=5,
                            index_col=None,
                            header=None,
                            names=["B_high[T]",
                                   "B_low[T]",
                                   "A_gap[m2]",
                                   "A_magnet[m2]",
                                   "-H_Brem_II_max[A/m]",
                                   "-H_Brem_IV_max[A/m]"])
    
    results_series = results.ix[0]
       
    results_series.to_csv(str(p),
                       float_format="%.6f",
                       sep=" ",
                       index=True)


def read_main_results_file():
    """Return a Series where each row is one of the COMSOL Main results"""
    
    results_filepath = Path(MAIN_RESULTS_FILENAME)

    results_series = pd.read_table(results_filepath,
                              sep=" ",
                              squeeze=True,
                              index_col=0,
                              header=None)
    results_series.index.name=None
    results_series.name = "COMSOL Main Results"
    return results_series


def calculate_magnitude(components_grid):
    """
    Return an array [x, y, norm(V)] from [x, y, Vx, Vy]
    """
    
    x, y, Vx, Vy = components_grid.T
    
    V = np.sqrt(Vx*Vx + Vy*Vy)
    
    return np.array((x,y,V)).T


def calculate_magnetic_profile(B_data, params):
    """
    Return the magnetic profile array [phi, B] based on data for the
    magnetic flux density [x, y, B] and a dictionary of parameters.
    
    The grid for 'B_data' is supposed to span the interval 0 <= phi <= pi
    (the top half-circle); this function mirrors this interval and return phi
    in the interval [0, 2 pi].
    
    """

    n_phi_points = 200

    params = expand_parameter_dictionary(params)
    
    R_g = params['R_g']
    R_o = params['R_o']


    # create ranges for phi and r
    phi_min = 0.0
    phi_max = np.pi

    phi_vector_top = np.linspace(phi_min,phi_max,N_PROFILE_POINTS)

    # slightly offset the boundaries to avoid numerical problems at the interfaces
    r_min = 1.001*R_o 
    r_max = 0.999*R_g
    n_r_points = 5

    r_vector = np.linspace(r_min,r_max,n_r_points)

    r_grid, phi_grid = np.meshgrid(r_vector,phi_vector_top)

    # calculate the points (x,y) distributed along
    # radial lines
    x_grid = r_grid * np.cos(phi_grid)
    y_grid = r_grid * np.sin(phi_grid)
    
    fB = NearestNDInterpolator(B_data[:,0:2],B_data[:,2])
    B_data_final = fB(x_grid,y_grid)


    # because both x_grid and y_grid have shape (n_r_points, N_PROFILE_POINTS),
    # when we apply the above created function we will get an array with the
    # same shape. We then take the average value along each row,
    # resuting in an array (N_PROFILE_POINTS)
    B_profile_top = np.mean(B_data_final,axis=1)

    # extrapolate data to the full circle
    phi_vector = np.concatenate((phi_vector_top,
                                 phi_vector_top+np.pi,))

    B_profile = np.concatenate((B_profile_top,
                                B_profile_top[::-1],))
    
    profile_data = np.array((np.rad2deg(phi_vector),B_profile)).T
    return profile_data
    
def write_magnetic_profile_file():
    """Create a file "COMSOL Magnetic Profile.txt" in the current directory,
    assuming the teslamax command was already ran, and write the magnetic
    profile data (average magnetic induction over radial chords).
    
    """
    
    p = Path('.') / MAGNETIC_PROFILE_FILENAME
    column_names = ["phi[deg]","B[T]"]
    column_header = " ".join(column_names)
    
    # load data from the B_III filename
    B_III_data = read_comsol_data_file(B_III_FILENAME)
    # get the columns corresponding to [x, y, B_x, B_y] and calculate [x, y, B]
    B_top = calculate_magnitude(B_III_data[:,:4])
    
    case_series = get_comsol_parameters_series()
    
    profile_data = calculate_magnetic_profile(B_top, case_series)
    
    np.savetxt(str(p),
               profile_data,
               fmt=("%.2f","%.5f"),
               delimiter=" ",
               header=column_header,
               comments='')
    
def calculate_average_high_field(profile_data):
        """
        Return the average magnetic profile through the high field region,
        based on profile data [theta, B_profile]
        """
        
        # in the present model, the high field region (equivalent to the cold
        # blow in an AMR device) goes from -45° to +45°, and from 135° to 225°
        
        theta_min = 135.0
        theta_max = 225.0
        
        theta_vector = profile_data[:,0]
        B_profile_vector = profile_data[:,1]
        
        # select values only where theta_vector falls in the high field region
        # this will return an array with True only at those positions
        region_filter = np.logical_and(theta_vector > theta_min,
                                       theta_vector < theta_max)
        
        
        # select the region of the magnetic profile that satisfy this condition
        theta_high_field = theta_vector[region_filter]
        B_profile_high_field = B_profile_vector[region_filter]
        
        # return the integral of these samples, divided by the range
        B_integrated = trapz(B_profile_high_field,theta_high_field)
        theta_range = theta_max - theta_min
        
        B_high_avg = B_integrated/theta_range
        
        return B_high_avg

def write_magnetic_profile_central_file():
    """Create a file "COMSOL Magnetic Profile.txt" in the current directory,
    assuming the teslamax command was already ran, and write the magnetic
    profile data (magnetic induction at central radial position).
    
    """
    
    p = Path('.') / MAGNETIC_PROFILE_FILENAME
    column_names = ["phi[deg]","B[T]"]
    column_header = " ".join(column_names)
    
    # load data from the high and low field regions
    B_h = read_comsol_data_file(B_HIGH_FILENAME)
    B_l = read_comsol_data_file(B_LOW_FILENAME)
    
    B_1q = np.concatenate((B_h,B_l),axis=0)
    
    # calcualte vector of angles for the first quadrant
    case_series = get_comsol_parameters_series()
    
    n_phi_points = 100
    
    R_g = case_series['R_g[m]']
    R_o = case_series['R_o[m]']
    
    
    # create ranges for phi and r
    phi_min = 0.0
    phi_max = np.pi/2
    
    phi_vector_1q = np.linspace(phi_min,phi_max,N_PROFILE_POINTS)
    
    r_min = R_o 
    r_max = R_g
    
    r_central = .5*(R_o + R_g)

    
    # calcualte the points (x,y) distributed along
    # radial lines
    x_grid = r_central * np.cos(phi_vector_1q)
    y_grid = r_central * np.sin(phi_vector_1q)

    B_profile_1q = griddata(B_1q[:,0:2],B_1q[:,2],np.array([x_grid,y_grid]).T)

    # extrapolate data to the full circle
    phi_vector = np.concatenate((phi_vector_1q,
                                 phi_vector_1q+np.pi/2,
                                 phi_vector_1q+np.pi,
                                 phi_vector_1q+(3/2)*np.pi))

    B_profile = np.concatenate((B_profile_1q,
                                B_profile_1q[::-1],
                                B_profile_1q,
                                B_profile_1q[::-1]))

    profile_data = np.array((np.rad2deg(phi_vector),B_profile)).T
    
    np.savetxt(str(p),
               profile_data,
               fmt=("%.2f","%.5f"),
               delimiter=" ",
               header=column_header,
               comments='')
    
def run_teslamax(verbose=False):
    """
    Run the teslamax process in the current directory, clean the results file 
    and create a magnetic profile file.
    
    Assumes the parameters file is present in the current directory."""
    comsol_process = subprocess.run(COMSOL_CMD,
                                    shell=True,
                                    stdout=subprocess.PIPE,
                                    stderr=subprocess.STDOUT,
                                    universal_newlines=True)
    if verbose: print(comsol_process.stdout)
    process_main_results_file()
    write_magnetic_profile_file()



def remove_units_from_dict_keys(dictionary):
    """Remove a string '[<anything>]' from every key of 'dictionary'"""
    
    new_dictionary = {}
    
    for key in dictionary.keys():
        new_key = key.split('[')[0]
        new_dictionary[new_key] = dictionary[key]
        
    return new_dictionary


def expand_parameter_dictionary(param_simple):
    """
    Return a new dictionary, calculating derivative parameters from
    'param_simple', which is usually passed to COMSOL.

    If the input dictionary contain units, they are removed. The returned
    dict does not contain units
    """
    param_dict = remove_units_from_dict_keys(param_simple)

    # cast the number of segments to int, if necessary
    param_dict["n_IV"] = int(param_dict["n_IV"])
    
    # remove some keys that are not necessary (and which may cause errors)
    try:
        del param_dict["-H_Brem_IV_max"]
    except:
        pass
    
    # calculate magnet geometry
    param_dict["R_g"] = param_dict["R_o"] + param_dict["h_gap"]
    param_dict["R_c"] = param_dict["R_s"] + param_dict["h_fc"]
    if param_dict["n_II"] > 0:
        param_dict["delta_phi_S_II"] = ((param_dict["phi_S_II"] -
                                        param_dict["phi_C_II"]) /
                                        param_dict["n_II"])
    if param_dict["n_IV"] > 0:
        param_dict["delta_phi_S_IV"] = (param_dict["phi_S_IV"] /
                                        param_dict["n_IV"])

    return param_dict
    

def write_parameter_file_from_dict(param_dict):
    """From a basic 'param_dict', calculate the necessary other parameters 
    (e.g. magnet segment size from total size and number of segments) and write
    the correct parameters file.
    
    If 'param_dict' contains units in the names, they are removed.
    """
    
    
    param_dict = expand_parameter_dictionary(param_dict)
    
    
    # write the dictionary file in the appropriate format that COMSOL can parse
    parameters_file_path = Path(".") / COMSOL_PARAMETER_FILENAME
    
    param_text = ""
    
    for (key, value) in param_dict.items():
        param_text = param_text + "%s %s\n" %(key,value)
    
    parameters_file_path.write_text(param_text)


def run_teslamax_from_params(params,verbose=False):
    """Write the 'params' dictionary in the apropriate format to the current
    directory (removing units if necessary) and run the teslamax process"""
    write_parameter_file_from_dict(params)
    run_teslamax(verbose)

def save_and_close_figure(fig,name):
    """
    Save the 'fig' Figure object as 'name' (with extension PLOT_EXTENSION),
    inside FIG_FILE_PATH, and close the figure"""
    
    
    file_path = str(FIG_FILE_PATH / (name + PLOT_EXTENSION))
    fig.savefig(file_path,dpi=DPI,bbox_inches='tight')
    plt.close(fig)


def normalize_vector(v):
    """
    Return the normalized (dimensionless) form of vector
    (or list of vectors) v"""
    
    # v could be a single vector or a list of vectors,
    # so we handle different cases
    if v.ndim == 1:
        return v/np.linalg.norm(v)
    else:
        v_norm = np.linalg.norm(v,axis=1)
        v_norm_inv = np.reciprocal(v_norm).reshape(len(v),1)
        return np.multiply(v,v_norm_inv)


def create_quater_circle_figure_template(r_lim,params):
    """
    Return (fig,axes) correspondent to a figure of the first quadrant,
    limited by r_lim. 
    Both magnets are also drawn.
    
    The size of the figure is controlled by FIGSIZE_INCHES"""
    
    fig = plt.figure(figsize=(FIGSIZE_INCHES,FIGSIZE_INCHES))
    axes = fig.add_subplot(111,aspect='equal')
    
    axes.set_ylim(0,1e3*r_lim)
    axes.set_xlim(0,1e3*r_lim)

    axes.set_ylabel(r'$y\ [\si{\mm}$]')
    axes.set_xlabel(r'$x\ [\si{\mm}$]')
    
    R_o = params['R_o']
    R_i = params['R_i']
    R_s = params['R_s']
    R_g = params.get('R_g', params['R_o'] + params['h_gap'])
    
    magnet_II_outer=plt.Circle((0,0),1e3*R_o,color='k',fill=False)
    magnet_II_inner=plt.Circle((0,0),1e3*R_i,color='k',fill=False)
    axes.add_artist(magnet_II_outer)
    axes.add_artist(magnet_II_inner)

    magnet_IV_outer=plt.Circle((0,0),1e3*R_s,color='k',fill=False)
    magnet_IV_inner=plt.Circle((0,0),1e3*R_g,color='k',fill=False)
    axes.add_artist(magnet_IV_outer)
    axes.add_artist(magnet_IV_inner)
    
    return fig, axes

def generate_sector_mesh_points(R1,R2,phi1,phi2):
    """
    Return a list of points [X,Y] uniformily distributed in a circle between
    radii R1 and R2 and angular positions phi1 and phi2
    
    The number of points is controlled by N_POINTS_PER_AXIS.
    """
    
    phi_min = phi1
    phi_max = phi2
    
    phi_vector = np.linspace(phi_min,phi_max,N_POINTS_PER_AXIS)
    
    r_vector = np.linspace(R1,R2,N_POINTS_PER_AXIS)
    
    phi_grid, r_grid = np.meshgrid(phi_vector,r_vector)
    
    X_vector = (r_grid*np.cos(phi_grid)).flatten()
    Y_vector = (r_grid*np.sin(phi_grid)).flatten()
    
    return np.array([X_vector,Y_vector]).T

def create_magnet_IV_figure_template(params):
    """
    Return (fig,axes) correspondent to a figure of the
    first quadrant of magnet IV. 
        
    The size of the figure is controlled by FIGSIZE_INCHES"""
    
    fig = plt.figure(figsize=(FIGSIZE_INCHES,FIGSIZE_INCHES))
    axes = fig.add_subplot(111,aspect='equal')
    
    R_o = params['R_o']
    R_i = params['R_i']
    R_s = params['R_s']
    R_g = params.get('R_g', params['R_o'] + params['h_gap'])
    R_c = params.get('R_c', params['R_s'] + params['h_fc'])
    r_lim = R_c
    
    axes.set_ylim(0,1e3*r_lim)
    axes.set_xlim(0,1e3*r_lim)

    axes.set_ylabel(r'$y\ [\si{\mm}$]')
    axes.set_xlabel(r'$x\ [\si{\mm}$]')
    
    width_IV = R_s - R_g
    n_IV = int(params['n_IV'])
    delta_phi_S_IV = params['delta_phi_S_IV']
    for i in range(0,n_IV):
        theta_0 = i*delta_phi_S_IV
        theta_1 = (i+1)*delta_phi_S_IV
        magnet_segment = Wedge((0,0),
                               1e3*R_s,
                               theta_0,
                               theta_1,
                               1e3*width_IV,
                               color='k',
                               fill=False)
        axes.add_artist(magnet_segment)
        
    return fig, axes

def create_magnets_figure_template(params):
    """
    Return (fig,axes) correspondent to a figure of the
    first quadrant of both magnets. 
        
    The size of the figure is controlled by FIGSIZE_INCHES"""
    
    fig = plt.figure(figsize=(FIGSIZE_INCHES,FIGSIZE_INCHES))
    axes = fig.add_subplot(111,aspect='equal')
    
    R_o = params['R_o']
    R_i = params['R_i']
    R_s = params['R_s']
    R_g = params.get('R_g', params['R_o'] + params['h_gap'])
    R_c = params.get('R_c', params['R_s'] + params['h_fc'])
    r_lim = R_c
    
    axes.set_ylim(0,1e3*r_lim)
    axes.set_xlim(0,1e3*r_lim)

    axes.set_ylabel(r'$y\ [\si{\mm}$]')
    axes.set_xlabel(r'$x\ [\si{\mm}$]')
    
    width_II = R_o - R_i
    n_II = int(params['n_II'])
    delta_phi_S_II = params['delta_phi_S_II']
    for i in range(0,n_II):
        theta_0 = i*delta_phi_S_II
        theta_1 = (i+1)*delta_phi_S_II
        magnet_segment = Wedge((0,0),
                               1e3*R_o,
                               theta_0,
                               theta_1,
                               1e3*width_II,
                               color='k',
                               fill=False)
        axes.add_artist(magnet_segment)
        
    width_IV = R_s - R_g
    n_IV = int(params['n_IV'])
    delta_phi_S_IV = params['delta_phi_S_IV']
    for j in range(0,n_IV):
        theta_0 = j*delta_phi_S_IV
        theta_1 = (j+1)*delta_phi_S_IV
        magnet_segment = Wedge((0,0),
                               1e3*R_s,
                               theta_0,
                               theta_1,
                               1e3*width_IV,
                               color='k',
                               fill=False)
        axes.add_artist(magnet_segment)
        
    return fig, axes

class TeslaMaxGeometry():
    """
    Class representing the physical geometry of the TeslaMax system,
    with all radii and angles.

    To instantiante, pass a dictionary (or similar object) with all geometric
    parameters in SI units (except for the angles, which must be provided in
    degrees). The names for the keys follow the standard convention, without
    the units in the names. E.g.

    >>> params = {'R_i': 0.015, 'phi_C_II': 15, ...} # provide other parameters
    >>> tmg = TeslaMaxGeometry(params)

    The parameters 'R_o', 'h_gap' and 'R_g' are not independent. If two are
    provided, the class automatically calculates the other one. If you provide
    all three, it's your responsibility to provide three consistent values.
    
    Currently, the only possible calculations are volume-related.
    """

    def __init__(self, params):
        """
        Keyword Arguments:
        params -- dict-like
        """

        self.geometric_parameters = params.copy()
        self._complete_geometric_parameters()

    def _complete_geometric_parameters(self):
        """
        For two of the parameters 'R_o', 'R_g', 'h_gap', calculate the
        third one and populate the 'geometric_parameters' field.

        If all three parameters are provided, nothing happens
        """

        gp = self.geometric_parameters

        if ('R_o' in gp) and ('R_g' in gp) and ('h_gap' in gp):
            pass
        else:        
            if ('R_o' in gp) and ('R_g' in gp):

                gp['h_gap'] = gp['R_g'] - gp['R_o']

            elif ('R_o' in gp ) and ('h_gap' in gp):

                gp['R_g'] = gp['R_o'] + gp['h_gap']

            elif ('R_g' in gp) and ('h_gap' in gp):

                gp['R_o'] = gp['R_g'] - gp['h_gap']


def expand_parameters_from_remanence_array(magnet_parameters, params, prefix):
    """
    Return a new parameters dict with the magnet parameters in the form
    '<prefix>_<magnet>_<segment>', with the values from 'magnet_parameters'
    and other parameters from 'params'.
    
    The length of the array 'magnet_parameters' must be equal to the sum of
    the number of segments in both cylinders.
    
    The first n_II elements refer to the inner magnet,
    and the remaining elements to the outer magnet.
    """
    
    params_expanded = params.copy()
    
    n_II = params["n_II"]
    for i in range(0,n_II):
        params_expanded["%s_II_%d" %(prefix,i+1,)] = magnet_parameters[i]
        
    n_IV = params["n_IV"]
    for j in range(0,n_IV):
        k =  j + n_II #the first n_II elements refer to magnet II
        params_expanded["%s_IV_%d" %(prefix,j+1,)] = magnet_parameters[k]
        
    return params_expanded

def calculate_instantaneous_profile(phi, B_high):
    """
    Calculate the value of the two-pole instantaneous magnetic profile at
    angular position 'phi' (in degrees), where the profile oscillates from
    0 to 'B_high'
    
    """
    
    high_region = (phi <= 45)
    high_region = np.logical_or(high_region,
                                np.logical_and((phi >= 135),
                                               (phi <= 225)))
    high_region = np.logical_or(high_region, (phi >= 315))
                 
    return np.where(high_region,B_high,0.0)

class TeslaMaxPreDesign():
    """
    Class representing a fixed-geometry pre-design of the TeslaMax system,
    with all geometric and material parameters, but without the direction of
    magnetization for the magnet segments.

    To instantiate, you have to pass a dictionary with the parameters:
    >>> tmpd = TeslaMaxPreDesign({'R_i': 0.015, 'mu_r_II': 1.05, ...})

    This dictionary should at least contain all geometric parameters,
    with which a TeslaMaxGeometry object will be created. The material
    properties can be added  directly in the constructor;
    the remanences magnitudes for each segment in this case are specified
    as a vector:
    
    >>> geom_params = {'R_i': 0.015, 'n_II': 2, ...}
    >>> tmpd = TeslaMaxPreDesign(geom_params,
                                 mu_r_II=1.05,
                                 mu_r_IV=1.10,
                                 mu_r_iron=5e5,
                                 linear_iron=1,
                                 B_rem_vector=np.array([1.4,1.4,1.2,1.2])

    The parameters dictionary may contain some of the material properties;
    if you provide a parameter in the dictionary and via the constructor,
    it is the latter value that will be used to build the object. In the
    dictionary, the remanences must be provided in the usual form
    (e.g. 'B_rem_II_1')
    """

    def __init__(self,
                 params,
                 mu_r_II=None,
                 mu_r_IV=None,
                 B_rem_vector=None,
                 mu_r_iron=None,
                 linear_iron=None):

        
        self.geometry = TeslaMaxGeometry(params)
        self.geometry_material_parameters = self.geometry.geometric_parameters

        if mu_r_II is not None:
            self.geometry_material_parameters['mu_r_II'] = mu_r_II

        if mu_r_IV is not None:
            self.geometry_material_parameters['mu_r_IV'] = mu_r_IV

        if mu_r_iron is not None:
            self.geometry_material_parameters['mu_r_iron'] = mu_r_iron

        if linear_iron is not None:
            self.geometry_material_parameters['linear_iron'] = linear_iron

        if B_rem_vector is not None:
            self.geometry_material_parameters = expand_parameters_from_remanence_array(B_rem_vector,
                                                                                       self.geometry_material_parameters,
                                                                                       'B_rem')

        self.points_F_operators = None
        self.F_operators = None

    def calculate_B_III_from_single_block(self,
                                          point,
                                          segment,
                                          magnet,
                                          magnitude,
                                          angle):
        """
        Return B_III(point) when 'segment' (1, 2, 3, ...)  of 'magnet'
        (either 'II' or 'IV') has a remanence of 'magnitude' and 'angle',
        and all other segments have null remanence. 

        """

        n_II = self.geometry_material_parameters["n_II"]
        n_IV = self.geometry_material_parameters["n_IV"]
        n_total = n_II + n_IV
        
        B_rem_vector = np.zeros(n_total)
        alpha_B_rem_vector = np.zeros(n_total)
        
        if magnet == "II":
            element = segment - 1

        else:
            element  = n_II + (segment -1)
            
        B_rem_vector[element] = magnitude
        alpha_B_rem_vector[element] = angle

        tmpd = TeslaMaxPreDesign(self.geometry_material_parameters,
                                 B_rem_vector=B_rem_vector)

        # SETUP WITH TEMPORARY DIRECTORY
        # with tempfile.TemporaryDirectory() as tempdir:
        #     tmm = TeslaMaxModel(tmpd,alpha_B_rem_vector,tempdir)
        #     tmm.run(verbose=DEBUG)
        #     result = tmm.calculate_B_III_from_position(point)

        # SETUP WITH A PLAYGROUND DIRECTORY FROM THE CURRENT DIR
        auxdir = str(Path('.') / 'teslamax-optimization')
        tmm = TeslaMaxModel(tmpd,alpha_B_rem_vector,auxdir)
        tmm.run(verbose=DEBUG)
        result = tmm.calculate_B_III_from_position(point)

        return result


    def calculate_F_operators(self):
        """
        Return (F_II_x, F_II_y, F_IV_x, F_IV_y), where each element is a list
        of the F-operators vector fields calculated at a uniform mesh
        in the air gap.

        For instance, F_II_x[0] is an array of (B_x, B_y), calculated when
        only the first segment of magnet II is magnetized in the x-direction,
        with unit remanence
        """

        n_II = self.geometry_material_parameters['n_II']
        n_IV = self.geometry_material_parameters['n_IV']

        R_o = self.geometry_material_parameters["R_o"]
        R_g = self.geometry_material_parameters["R_g"] 
        points = generate_sector_mesh_points(1.001*R_o,
                                                      0.999*R_g,
                                                      0.0,
                                                      np.pi)
        self.points_F_operators = points
        
        F_II_x = []
        F_II_y = []

        F_IV_x = []
        F_IV_y = []

        for k in range(0,n_II):
            F_II_x.append(self.calculate_B_III_from_single_block(point=points, 
                                                       segment=k+1, 
                                                       magnet='II', 
                                                       magnitude=1.0, 
                                                        angle=0.0))


            F_II_y.append(self.calculate_B_III_from_single_block(point=points, 
                                                       segment=k+1,
                                                       magnet='II', 
                                                       magnitude=1.0, 
                                                        angle=90.0))

        for j in range(0,n_IV):
            F_IV_x.append(self.calculate_B_III_from_single_block(point=points, 
                                                       segment=j+1, 
                                                       magnet='IV', 
                                                       magnitude=1.0, 
                                                        angle=0.0))


            F_IV_y.append(self.calculate_B_III_from_single_block(point=points, 
                                                       segment=j+1,
                                                       magnet='IV', 
                                                       magnitude=1.0, 
                                                        angle=90.0))

        self.F_operators =  (F_II_x, F_II_y, F_IV_x, F_IV_y)

    def get_points_F_operators(self):
        if self.points_F_operators is None:
            self.calculate_F_operators()
            
        return self.points_F_operators
    
    def get_F_operators(self):

        if self.F_operators is None:
            self.calculate_F_operators()
            
        return self.F_operators

    def superposition_B_III(self, alpha_B_rem):
        """
        Return (x, y, B_x, B_y) based on a  vector of remanence angles.

        - 'alpha_B_rem' is a vector of (n_II + n_IV) remanences, where the
        first n_II elements represent magnet II and the remaining elements
        represent magnet IV

        """

        B_III = 0

        points = self.get_points_F_operators()
        F_II_x, F_II_y, F_IV_x, F_IV_y = self.get_F_operators()

        params = self.geometry_material_parameters
        
        n_II = params["n_II"]
        for k in range(0,n_II):
            B_rem = params["B_rem_II_%d" %(k+1)]
            alpha = np.deg2rad(alpha_B_rem[k])

            B = B_rem * (np.cos(alpha) * F_II_x[k] + np.sin(alpha) * F_II_y[k])

            B_III = B_III + B

        n_IV = params["n_IV"]
        for j in range(0,n_IV):
            B_rem = params["B_rem_IV_%d" %(j+1)]
            alpha = np.deg2rad(alpha_B_rem[n_II + j])

            B = B_rem * (np.cos(alpha) * F_IV_x[j] + np.sin(alpha) * F_IV_y[j])

            B_III = B_III + B

        B_III_grid = np.concatenate((points, B_III), axis=1)

        return B_III_grid

    def calculate_functional_average(self,alpha_B_rem):
        """
        Return the objective functional based on  a vector of remanence angles.
        The objective functional is defined as the reciprocal of the
        average high field, to be minimized.
    
        - 'alpha_B_rem' is a vector of (n_II + n_IV) remanences, where the
        first n_II elements represent magnet II and the remaining elements
        represent magnet IV
        """

        B_III_data = self.superposition_B_III(alpha_B)

        # the above statement will return [x,y,B_x,B_y]. We have to calculate the magnitude to pass it
        # to the magnetic profile data
        B_III_data = calculate_magnitude(B_III_data)

        B_profile_data = calculate_magnetic_profile(B_III_data,
                                                             self.geometry_material_parameters).T

        S = -calculate_average_high_field(B_profile_data)
        return S

    def calculate_functional_instantaneous(self,alpha_B_rem):
        """
        Return the objective functional based on  a vector of remanence angles.
        The objective functional is defined as the difference between the
        resulting profile and an instantaneous profile of TARGET_PROFILE
        as the high level, and is to be minimized.
        
        - 'alpha_B_rem' is a vector of (n_II + n_IV) remanences, where the
        first n_II elements represent magnet II and the remaining elements
        represent magnet IV
        """

        B_III_data = self.superposition_B_III(alpha_B_rem)

        # the above statement will return [x,y,B_x,B_y]. We have to calculate the magnitude to pass it
        # to the magnetic profile data
        B_III_data = calculate_magnitude(B_III_data)

        phi_vector, B_profile = calculate_magnetic_profile(B_III_data,
                                                                    self.geometry_material_parameters).T

        B_inst_profile = calculate_instantaneous_profile(phi_vector,TARGET_PROFILE)

        # use a "least squares" approach
        B_lsq = (B_inst_profile - B_profile)**2
        S = np.trapz(B_lsq,phi_vector)

        return S

    def calculate_functional(self,alpha_B_rem):
        return self.calculate_functional_instantaneous(alpha_B_rem)

    def calculate_functional_derivative(self,alpha_B_rem, i):
        """
        Return the derivative of the functional in respect to the i-th element
        of the remanence angles vector.
        
        - 'alpha_B_rem' is a vector of (n_II + n_IV) remanences, where the
        first n_II elements represent magnet II and the remaining elements
        represent magnet IV
        - 'i' is the element (0-based) in respect to which the derivative
        is being evaluated
        """

        S = self.calculate_functional( alpha_B_rem )

        alpha_B_rem_plus = alpha_B_rem.copy()
        delta = 1e-6
        alpha_B_rem_plus[i]  = alpha_B_rem_plus[i] + delta

        S_plus = self.calculate_functional(alpha_B_rem_plus)

        dS = (S_plus - S)/delta

        return dS

    def calculate_funcional_derivative_second_order(self, alpha_B_rem, i, j):
        """
        Return the second-order derivative of the functional in respect
        to the (i,j) elements (e.g. d/dalpha_i (dfunctional/dalpha_j))
        """

        dS_j = self.calculate_functional_derivative(alpha_B_rem,j)

        alpha_B_rem_plus = alpha_B_rem.copy()
        delta = 1e-6
        alpha_B_rem_plus[i]  = alpha_B_rem_plus[i] + delta

        dS_j_plus = self.calculate_functional_derivative(alpha_B_rem_plus, j)

        ddS = (dS_j_plus - dS_j)/delta

        return ddS


    def calculate_functional_gradient(self, alpha_B_rem):
        """
        Return the gradient of the functional evaluated at point 'alpha_B_rem'.
        
        Keyword Arguments:
        alpha_B_rem -- array, vector of (n_II + n_IV) remanences, where the
        gradient is to be evaluated
        """

        n = len(alpha_B_rem)

        grad = np.array([self.calculate_functional_derivative(alpha_B_rem,
                                                              i)
                         for i in range(0,n)])

        return grad

    def calculate_functional_hessian(self, alpha_B_rem):
        """
        Return the Hessian matrix of the functional evaluated at
        point 'alpha_B_rem'.

        Keyword Argumets:
        alpha_B_rem -- array, vector of (n_II + n_IV) remanences, where the
        gradient is to be evaluated
        """

        n = len(alpha_B_rem)

        hess = np.array([
            [self.calculate_funcional_derivative_second_order(alpha_B_rem,
                                                              i,
                                                              j)
             for j in range(0,n)]
            for i in range(0,n)])

        return hess

        


class TeslaMaxModel():
    """
    Class representing the full TeslaMax model.
    
    To create an instance of the class, first you have to create a
    TeslaMaxPreDesign object and an array of remanence angles,
    and then pass these parameters along with  a path where to store the
    generated text files:
    
    >>> tmpd = TeslaMaxPreDesign({...})
    >>> alpha_B_rem = [...]
    >>> tmm = TeslaMaxModel(tmpd, alpha_B_rem, "teslamax-results")
    
    If the path already exists, it will be cleaned up to avoid confusion
    between different simulations.

    If you want to preserve the data in 'path', set the argument 'clean'
    to False in the constructor
    
    """
    
    def __init__(self,teslamax_predesign,alpha_vector,path,clean=True):
        
        self.pre_design = teslamax_predesign

        self.params = expand_parameters_from_remanence_array(alpha_vector,
                                                             self.pre_design.geometry_material_parameters,
                                                             "alpha_rem")
        
        self.path = Path(path)
        if (self.path.exists() ) and (self.path.is_dir()) and clean:
            shutil.rmtree(str(self.path))
        
        self.path.mkdir(exist_ok=True)
        
        self.profile_data = np.empty(2)
        
                
    def run(self,verbose=False):
        """
        Change the current directory temporarily to the object's path
        and run the TeslaMax program in it.
        
        Also populates the appropriate fields with results from TeslaMax
        """
        
        cwd = os.getcwd()
        os.chdir(str(self.path))
        run_teslamax_from_params(self.params,verbose)
        os.chdir(cwd)
        
        self.profile_data = self.get_profile_data()
        self.fB_profile = interp1d(self.profile_data[:,0],
                                   self.profile_data[:,1],
                                   kind='linear')
        
    def get_B_III_data(self):
        """
        Return an array [x, y, Bx, By] for the air gap region
        """
        
        B_III_file_path = self.path / B_III_FILENAME
        B_III_full_data = read_comsol_data_file(str(B_III_file_path))
        
        B_III_data = B_III_full_data[:,:4]
        return B_III_data
    
    def calculate_B_III_from_position(self,point_cartesian):
        """
        Return B_III(x,y), where 'point_cartesian' = [x,y]
        """
        
        B_III_data = self.get_B_III_data()
        
        return griddata(B_III_data[:,:2],
                       B_III_data[:,2:4],
                       point_cartesian)
        
        
    def get_profile_data(self):
        """
        Return an array of the magnetic profile data, where the first column
        is the angle in degrees [0,360] and the second is the average magnetic
        flux density in tesla
        """
        
        profile_file_path = self.path / MAGNETIC_PROFILE_FILENAME
        profile_data = np.loadtxt(str(profile_file_path),
                         skiprows=1)
        
        return profile_data
    
    def calculate_B_profile(self,theta):
        """
        Return average magnetic flux density at angular position 'theta'
        (as an array with the same length).
        """
        
        return self.fB_profile(theta)


