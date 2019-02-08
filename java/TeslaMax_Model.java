/*
 * TeslaMax_Model.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;

/** Model exported on Feb 8 2019, 14:06 by COMSOL 5.4.0.295. */
public class TeslaMax_Model {

  public static Model run() {
    Model model = ModelUtil.create("Model");

    model.modelPath("C:\\Users\\fabiofortkamp\\code\\TeslaMax\\java");

    model.label("TeslaMax_Model.mph");

    model.param().set("R_i", "0.015");
    model.param().set("R_o", "0.07");
    model.param().set("h_gap", "0.02");
    model.param().set("R_s", "0.14");
    model.param().set("h_fc", "0.01");
    model.param().set("R_e", "0.3");
    model.param().set("n_IV", "3");
    model.param().set("phi_S_IV", "45");
    model.param().set("n_II", "3");
    model.param().set("phi_C_II", "15");
    model.param().set("phi_S_II", "45");
    model.param().set("B_rem_II_1", "1.4");
    model.param().set("B_rem_II_2", "1.4");
    model.param().set("B_rem_II_3", "1.4");
    model.param().set("mu_r_II", "1.05");
    model.param().set("B_rem_IV_1", "1.4");
    model.param().set("B_rem_IV_2", "1.4");
    model.param().set("B_rem_IV_3", "1.4");
    model.param().set("mu_r_IV", "1.05");
    model.param().set("linear_iron", "1");
    model.param().set("mu_r_iron", "5000.0");
    model.param().set("Hc_j", "900000.0");
    model.param().set("R_g", "0.09000000000000001");
    model.param().set("alpha_rem_II_1", "-3.67109344");
    model.param().set("alpha_rem_II_2", "4.98966041");
    model.param().set("alpha_rem_II_3", "-15.7967738");
    model.param().set("alpha_rem_IV_1", "8.2380897");
    model.param().set("alpha_rem_IV_2", "30.8698398");
    model.param().set("alpha_rem_IV_3", "87.03318185");
    model.param().set("R_c", "0.15000000000000002");
    model.param().set("delta_phi_S_II", "10.0");
    model.param().set("delta_phi_S_IV", "15.0");
    model.param().set("l_element_max", "0.0111 [m]");
    model.param().set("n_narrow", "5");

    model.component().create("nhalbach_system", false);

    model.component("nhalbach_system").geom().create("nhalbach_geometry", 2);

    model.result().table().create("results_table", "Table");

    model.component("nhalbach_system").mesh().create("mesh1");

    model.geom().create("cylinder_block", "Part", 2);
    model.geom().create("cylinder_shell", "Part", 2);
    model.geom("cylinder_block").label("Cylinder Block");
    model.geom("cylinder_block").inputParam().set("r1", "R_i");
    model.geom("cylinder_block").inputParam().set("r2", "R_o");
    model.geom("cylinder_block").inputParam().set("phi1", "15[deg]");
    model.geom("cylinder_block").inputParam().set("phi2", "45[deg]");
    model.geom("cylinder_block").create("c1", "Circle");
    model.geom("cylinder_block").feature("c1").set("rot", "phi1");
    model.geom("cylinder_block").feature("c1").set("r", "r2");
    model.geom("cylinder_block").feature("c1").set("angle", "phi2-phi1");
    model.geom("cylinder_block").create("c2", "Circle");
    model.geom("cylinder_block").feature("c2").set("rot", "phi1");
    model.geom("cylinder_block").feature("c2").set("r", "r1");
    model.geom("cylinder_block").feature("c2").set("angle", "phi2-phi1");
    model.geom("cylinder_block").create("dif1", "Difference");
    model.geom("cylinder_block").feature("dif1").selection("input").set("c1");
    model.geom("cylinder_block").feature("dif1").selection("input2").set("c2");
    model.geom("cylinder_block").create("dom", "ExplicitSelection");
    model.geom("cylinder_block").feature("dom").selection("selection").set("dif1(1)", 1);
    model.geom("cylinder_block").run();
    model.geom("cylinder_shell").label("Cylinder Shell");
    model.geom("cylinder_shell").inputParam().set("r1", "R_i");
    model.geom("cylinder_shell").inputParam().set("r2", "R_o");
    model.geom("cylinder_shell").inputParam().set("delta_phi", "180[deg]");
    model.geom("cylinder_shell").create("c1", "Circle");
    model.geom("cylinder_shell").feature("c1").set("r", "r2");
    model.geom("cylinder_shell").feature("c1").set("angle", "delta_phi");
    model.geom("cylinder_shell").create("c2", "Circle");
    model.geom("cylinder_shell").feature("c2").set("r", "r1");
    model.geom("cylinder_shell").feature("c2").set("angle", "delta_phi");
    model.geom("cylinder_shell").create("dif1", "Difference");
    model.geom("cylinder_shell").feature("dif1").selection("input").set("c1");
    model.geom("cylinder_shell").feature("dif1").selection("input2").set("c2");
    model.geom("cylinder_shell").create("dom", "ExplicitSelection");
    model.geom("cylinder_shell").feature("dom").selection("selection").set("dif1(1)", 1);
    model.geom("cylinder_shell").run();
    model.component("nhalbach_system").geom("nhalbach_geometry").create("iron_ii1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii1")
         .label("Cylinder Block - Iron II Wedge 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii1")
         .setEntry("inputexpr", "r1", "R_i");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii1")
         .setEntry("inputexpr", "r2", "R_o");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii1")
         .setEntry("inputexpr", "phi1", "0.0[deg]");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii1")
         .setEntry("inputexpr", "phi2", "phi_C_II");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii1").set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii1")
         .setEntry("selkeepdom", "iron_ii1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_ii_1q1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q1")
         .label("Cylinder Block 1 - Magnet II - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q1")
         .setEntry("inputexpr", "r1", "R_i");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q1")
         .setEntry("inputexpr", "r2", "R_o");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q1")
         .setEntry("inputexpr", "phi1", "phi_C_II + 0 * delta_phi_S_II");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q1")
         .setEntry("inputexpr", "phi2", "phi_C_II + 1 * delta_phi_S_II");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q1")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q1")
         .setEntry("selkeepdom", "magnet_ii_1q1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_ii_1q2", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q2")
         .label("Cylinder Block 2 - Magnet II - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q2")
         .setEntry("inputexpr", "r1", "R_i");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q2")
         .setEntry("inputexpr", "r2", "R_o");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q2")
         .setEntry("inputexpr", "phi1", "phi_C_II + 1 * delta_phi_S_II");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q2")
         .setEntry("inputexpr", "phi2", "phi_C_II + 2 * delta_phi_S_II");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q2")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q2")
         .setEntry("selkeepdom", "magnet_ii_1q2_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_ii_1q3", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q3")
         .label("Cylinder Block 3 - Magnet II - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q3")
         .setEntry("inputexpr", "r1", "R_i");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q3")
         .setEntry("inputexpr", "r2", "R_o");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q3")
         .setEntry("inputexpr", "phi1", "phi_C_II + 2 * delta_phi_S_II");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q3")
         .setEntry("inputexpr", "phi2", "phi_C_II + 3 * delta_phi_S_II");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q3")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q3")
         .setEntry("selkeepdom", "magnet_ii_1q3_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("iron_ii2", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii2")
         .label("Cylinder Block - Iron II");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii2")
         .setEntry("inputexpr", "r1", "R_i");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii2")
         .setEntry("inputexpr", "r2", "R_o");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii2")
         .setEntry("inputexpr", "phi1", "phi_S_II");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii2")
         .setEntry("inputexpr", "phi2", "90[deg]");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii2").set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii2")
         .setEntry("selkeepdom", "iron_ii2_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_iv_1q1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q1")
         .label("Cylinder Block 1 - Magnet IV - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q1")
         .setEntry("inputexpr", "r1", "R_g");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q1")
         .setEntry("inputexpr", "r2", "R_s");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q1")
         .setEntry("inputexpr", "phi1", "0 * delta_phi_S_IV");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q1")
         .setEntry("inputexpr", "phi2", "1 * delta_phi_S_IV");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q1")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q1")
         .setEntry("selkeepdom", "magnet_iv_1q1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_iv_1q2", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q2")
         .label("Cylinder Block 2 - Magnet IV - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q2")
         .setEntry("inputexpr", "r1", "R_g");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q2")
         .setEntry("inputexpr", "r2", "R_s");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q2")
         .setEntry("inputexpr", "phi1", "1 * delta_phi_S_IV");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q2")
         .setEntry("inputexpr", "phi2", "2 * delta_phi_S_IV");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q2")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q2")
         .setEntry("selkeepdom", "magnet_iv_1q2_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_iv_1q3", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q3")
         .label("Cylinder Block 3 - Magnet IV - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q3")
         .setEntry("inputexpr", "r1", "R_g");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q3")
         .setEntry("inputexpr", "r2", "R_s");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q3")
         .setEntry("inputexpr", "phi1", "2 * delta_phi_S_IV");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q3")
         .setEntry("inputexpr", "phi2", "3 * delta_phi_S_IV");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q3")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q3")
         .setEntry("selkeepdom", "magnet_iv_1q3_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("iron_iv1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_iv1")
         .label("Cylinder Block - Iron IV");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_iv1")
         .setEntry("inputexpr", "r1", "R_g");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_iv1")
         .setEntry("inputexpr", "r2", "R_s");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_iv1")
         .setEntry("inputexpr", "phi1", "phi_S_IV");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_iv1")
         .setEntry("inputexpr", "phi2", "90[deg]");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_iv1").set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_iv1")
         .setEntry("selkeepdom", "iron_iv1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("shaft1", "Circle");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("shaft1").label("Circle");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("shaft1").set("selresult", true);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("shaft1").set("r", "R_i");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("shaft1").set("angle", "90[deg]");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("air_gap1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap1")
         .label("Air Gap Cylinder Block High Field  1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap1")
         .setEntry("inputexpr", "r1", "R_o");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap1")
         .setEntry("inputexpr", "r2", "R_g");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap1")
         .setEntry("inputexpr", "phi1", "0");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap1")
         .setEntry("inputexpr", "phi2", "45[deg]");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap1").set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap1")
         .setEntry("selkeepdom", "air_gap1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("air_gap2", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap2")
         .label("Air Gap Cylinder Block Low Field  1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap2")
         .setEntry("inputexpr", "r1", "R_o");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap2")
         .setEntry("inputexpr", "r2", "R_g");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap2")
         .setEntry("inputexpr", "phi1", "45[deg]");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap2")
         .setEntry("inputexpr", "phi2", "90[deg]");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap2").set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap2")
         .setEntry("selkeepdom", "air_gap2_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("flux_concentrator1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("flux_concentrator1")
         .label("Flux Concentrator Shell");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("flux_concentrator1")
         .set("part", "cylinder_shell");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("flux_concentrator1")
         .setEntry("inputexpr", "r1", "R_s");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("flux_concentrator1")
         .setEntry("inputexpr", "r2", "R_c");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("flux_concentrator1")
         .setEntry("inputexpr", "delta_phi", "90[deg]");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("flux_concentrator1")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("flux_concentrator1")
         .setEntry("selkeepdom", "flux_concentrator1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("environment1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("environment1")
         .label("Environment Cylinder Shell");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("environment1")
         .set("part", "cylinder_shell");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("environment1")
         .setEntry("inputexpr", "r1", "R_c");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("environment1")
         .setEntry("inputexpr", "r2", "R_e");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("environment1")
         .setEntry("inputexpr", "delta_phi", "90[deg]");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("environment1")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("environment1")
         .setEntry("selkeepdom", "environment1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").run();
    model.component("nhalbach_system").geom("nhalbach_geometry").run("fin");

    model.component("nhalbach_system").selection().create("shaft_selection", "Explicit");
    model.component("nhalbach_system").selection("shaft_selection").set(1);
    model.component("nhalbach_system").selection().create("air_gap_high_selection", "Explicit");
    model.component("nhalbach_system").selection("air_gap_high_selection").set(11);
    model.component("nhalbach_system").selection().create("air_gap_low_selection", "Explicit");
    model.component("nhalbach_system").selection("air_gap_low_selection").set(3);
    model.component("nhalbach_system").selection().create("air_gap_selection", "Explicit");
    model.component("nhalbach_system").selection("air_gap_selection").set(3, 11);
    model.component("nhalbach_system").selection().create("environment_selection", "Explicit");
    model.component("nhalbach_system").selection("environment_selection").set(6);
    model.component("nhalbach_system").selection().create("environment_horization_boundary_selection", "Union");
    model.component("nhalbach_system").selection("environment_horization_boundary_selection").set("entitydim", 1);
    model.component("nhalbach_system").selection().create("environment_sel_right", "Ball");
    model.component("nhalbach_system").selection("environment_sel_right").set("entitydim", 1);
    model.component("nhalbach_system").selection().create("environment_sel_left", "Ball");
    model.component("nhalbach_system").selection("environment_sel_left").set("entitydim", 1);
    model.component("nhalbach_system").selection().create("magnets_selection", "Explicit");
    model.component("nhalbach_system").selection("magnets_selection").set(7, 8, 9, 12, 13, 14);
    model.component("nhalbach_system").selection().create("magnets_ii_1q_selection", "Explicit");
    model.component("nhalbach_system").selection("magnets_ii_1q_selection").set(7, 8, 9);
    model.component("nhalbach_system").selection().create("magnets_iv_1q_selection", "Explicit");
    model.component("nhalbach_system").selection("magnets_iv_1q_selection").set(12, 13, 14);
    model.component("nhalbach_system").selection().create("iron_selection", "Explicit");
    model.component("nhalbach_system").selection("iron_selection").set(2, 4, 5, 10);
    model.component("nhalbach_system").selection().create("air_regions_selection", "Explicit");
    model.component("nhalbach_system").selection("air_regions_selection").set(1, 3, 6, 11);
    model.component("nhalbach_system").selection().create("magnetic_circuit_regions_seletion", "Explicit");
    model.component("nhalbach_system").selection("magnetic_circuit_regions_seletion")
         .set(1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14);
    model.component("nhalbach_system").selection().create("zero_scalar_potential_selection", "Box");
    model.component("nhalbach_system").selection("zero_scalar_potential_selection").set("entitydim", 1);
    model.component("nhalbach_system").selection("shaft_selection").label("Shaft");
    model.component("nhalbach_system").selection("air_gap_high_selection").label("Air Gap High");
    model.component("nhalbach_system").selection("air_gap_low_selection").label("Air Gap Low");
    model.component("nhalbach_system").selection("air_gap_selection").label("Air Gap");
    model.component("nhalbach_system").selection("environment_selection").label("Environment");
    model.component("nhalbach_system").selection("environment_horization_boundary_selection")
         .label("Environment boundary");
    model.component("nhalbach_system").selection("environment_horization_boundary_selection")
         .set("input", new String[]{"environment_sel_right", "environment_sel_left"});
    model.component("nhalbach_system").selection("environment_sel_right")
         .label("Environment Horizontal Right Boundary");
    model.component("nhalbach_system").selection("environment_sel_right").set("posx", 0.225);
    model.component("nhalbach_system").selection("environment_sel_right").set("r", 0.001);
    model.component("nhalbach_system").selection("environment_sel_left")
         .label("Environment Horizontal Left Boundary");
    model.component("nhalbach_system").selection("environment_sel_left").set("posx", -0.225);
    model.component("nhalbach_system").selection("environment_sel_left").set("r", 0.001);
    model.component("nhalbach_system").selection("magnets_selection").label("Magnets region");
    model.component("nhalbach_system").selection("magnets_ii_1q_selection").label("Magnet II 1Q region");
    model.component("nhalbach_system").selection("magnets_iv_1q_selection").label("Magnet IV 1Q region");
    model.component("nhalbach_system").selection("iron_selection").label("Iron region");
    model.component("nhalbach_system").selection("air_regions_selection").label("Air regions");
    model.component("nhalbach_system").selection("magnetic_circuit_regions_seletion")
         .label("Magnetic circuit regions (except environment)");
    model.component("nhalbach_system").selection("zero_scalar_potential_selection").label("x = 0 line");
    model.component("nhalbach_system").selection("zero_scalar_potential_selection").set("xmin", -0.001);
    model.component("nhalbach_system").selection("zero_scalar_potential_selection").set("xmax", 0);
    model.component("nhalbach_system").selection("zero_scalar_potential_selection").set("ymin", 0);
    model.component("nhalbach_system").selection("zero_scalar_potential_selection").set("ymax", 0.301);
    model.component("nhalbach_system").selection("zero_scalar_potential_selection").set("condition", "inside");

    model.component("nhalbach_system").material().create("mat1", "Common");
    model.component("nhalbach_system").material().create("mat2", "Common");
    model.component("nhalbach_system").material().create("mat3", "Common");
    model.component("nhalbach_system").material("mat1").selection().named("air_regions_selection");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func().create("eta", "Piecewise");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func().create("Cp", "Piecewise");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func().create("rho", "Analytic");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func().create("k", "Piecewise");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func().create("cs", "Analytic");
    model.component("nhalbach_system").material("mat1").propertyGroup()
         .create("RefractiveIndex", "Refractive index");
    model.component("nhalbach_system").material("mat2").selection().named("iron_selection");
    model.component("nhalbach_system").material("mat3").selection().named("magnets_selection");

    model.component("nhalbach_system").coordSystem().create("ie1", "InfiniteElement");
    model.component("nhalbach_system").coordSystem("ie1").selection().named("environment_selection");

    model.component("nhalbach_system").physics().create("mfnc", "MagnetostaticsNoCurrents", "nhalbach_geometry");
    model.component("nhalbach_system").physics("mfnc").create("zsp1", "ZeroMagneticScalarPotential", 1);
    model.component("nhalbach_system").physics("mfnc").feature("zsp1").selection()
         .named("zero_scalar_potential_selection");
    model.component("nhalbach_system").physics("mfnc").create("mfc2", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc2").selection().named("iron_selection");
    model.component("nhalbach_system").physics("mfnc").create("mfc3", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc3").selection().set(9);
    model.component("nhalbach_system").physics("mfnc").create("mfc4", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc4").selection().set(8);
    model.component("nhalbach_system").physics("mfnc").create("mfc5", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc5").selection().set(7);
    model.component("nhalbach_system").physics("mfnc").create("mfc6", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc6").selection().set(14);
    model.component("nhalbach_system").physics("mfnc").create("mfc7", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc7").selection().set(13);
    model.component("nhalbach_system").physics("mfnc").create("mfc8", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc8").selection().set(12);

    model.component("nhalbach_system").mesh("mesh1").create("dis1", "Distribution");
    model.component("nhalbach_system").mesh("mesh1").create("ftri1", "FreeTri");
    model.component("nhalbach_system").mesh("mesh1").create("map1", "Map");
    model.component("nhalbach_system").mesh("mesh1").feature("dis1").selection()
         .named("environment_horization_boundary_selection");
    model.component("nhalbach_system").mesh("mesh1").feature("ftri1").selection()
         .named("magnetic_circuit_regions_seletion");
    model.component("nhalbach_system").mesh("mesh1").feature("map1").selection().named("environment_selection");

    model.component("nhalbach_system").probe().create("prb1", "Domain");
    model.component("nhalbach_system").probe().create("prb2", "Domain");
    model.component("nhalbach_system").probe().create("prb3", "Domain");
    model.component("nhalbach_system").probe().create("prb4", "Domain");
    model.component("nhalbach_system").probe().create("prb5", "Domain");
    model.component("nhalbach_system").probe().create("prb6", "Domain");
    model.component("nhalbach_system").probe().create("prb7", "Domain");
    model.component("nhalbach_system").probe("prb1").selection().named("air_gap_high_selection");
    model.component("nhalbach_system").probe("prb2").selection().named("air_gap_low_selection");
    model.component("nhalbach_system").probe("prb3").selection().named("air_gap_selection");
    model.component("nhalbach_system").probe("prb4").selection().named("magnets_selection");
    model.component("nhalbach_system").probe("prb5").selection().named("magnets_ii_1q_selection");
    model.component("nhalbach_system").probe("prb6").selection().named("magnets_iv_1q_selection");
    model.component("nhalbach_system").probe("prb7").selection().named("magnets_selection");

    model.component("nhalbach_system").view("view1").axis().set("xmin", -0.18435564637184143);
    model.component("nhalbach_system").view("view1").axis().set("xmax", 0.2743556499481201);
    model.component("nhalbach_system").view("view1").axis().set("ymin", -0.11716492474079132);
    model.component("nhalbach_system").view("view1").axis().set("ymax", 0.20716492831707);

    model.component("nhalbach_system").material("mat1").label("Air");
    model.component("nhalbach_system").material("mat1").set("family", "air");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("eta").set("arg", "T");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("eta")
         .set("pieces", new String[][]{{"200.0", "1600.0", "-8.38278E-7+8.35717342E-8*T^1-7.69429583E-11*T^2+4.6437266E-14*T^3-1.06585607E-17*T^4"}});
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("Cp").set("arg", "T");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("Cp")
         .set("pieces", new String[][]{{"200.0", "1600.0", "1047.63657-0.372589265*T^1+9.45304214E-4*T^2-6.02409443E-7*T^3+1.2858961E-10*T^4"}});
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("rho")
         .set("expr", "pA*0.02897/8.314/T");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("rho")
         .set("args", new String[]{"pA", "T"});
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("rho").set("dermethod", "manual");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("rho")
         .set("argders", new String[][]{{"pA", "d(pA*0.02897/8.314/T,pA)"}, {"T", "d(pA*0.02897/8.314/T,T)"}});
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("rho")
         .set("plotargs", new String[][]{{"pA", "0", "1"}, {"T", "0", "1"}});
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("k").set("arg", "T");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("k")
         .set("pieces", new String[][]{{"200.0", "1600.0", "-0.00227583562+1.15480022E-4*T^1-7.90252856E-8*T^2+4.11702505E-11*T^3-7.43864331E-15*T^4"}});
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("cs")
         .set("expr", "sqrt(1.4*287*T)");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("cs")
         .set("args", new String[]{"T"});
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("cs").set("dermethod", "manual");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("cs")
         .set("argders", new String[][]{{"T", "d(sqrt(1.4*287*T),T)"}});
    model.component("nhalbach_system").material("mat1").propertyGroup("def").func("cs")
         .set("plotargs", new String[][]{{"T", "0", "1"}});
    model.component("nhalbach_system").material("mat1").propertyGroup("def")
         .set("relpermeability", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("nhalbach_system").material("mat1").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});

    return model;
  }

  public static Model run2(Model model) {
    model.component("nhalbach_system").material("mat1").propertyGroup("def")
         .set("dynamicviscosity", "eta(T[1/K])[Pa*s]");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").set("ratioofspecificheat", "1.4");
    model.component("nhalbach_system").material("mat1").propertyGroup("def")
         .set("electricconductivity", new String[]{"0[S/m]", "0", "0", "0", "0[S/m]", "0", "0", "0", "0[S/m]"});
    model.component("nhalbach_system").material("mat1").propertyGroup("def")
         .set("heatcapacity", "Cp(T[1/K])[J/(kg*K)]");
    model.component("nhalbach_system").material("mat1").propertyGroup("def")
         .set("density", "rho(pA[1/Pa],T[1/K])[kg/m^3]");
    model.component("nhalbach_system").material("mat1").propertyGroup("def")
         .set("thermalconductivity", new String[]{"k(T[1/K])[W/(m*K)]", "0", "0", "0", "k(T[1/K])[W/(m*K)]", "0", "0", "0", "k(T[1/K])[W/(m*K)]"});
    model.component("nhalbach_system").material("mat1").propertyGroup("def").set("soundspeed", "cs(T[1/K])[m/s]");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").addInput("temperature");
    model.component("nhalbach_system").material("mat1").propertyGroup("def").addInput("pressure");
    model.component("nhalbach_system").material("mat1").propertyGroup("RefractiveIndex").set("n", "");
    model.component("nhalbach_system").material("mat1").propertyGroup("RefractiveIndex").set("ki", "");
    model.component("nhalbach_system").material("mat1").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.component("nhalbach_system").material("mat1").propertyGroup("RefractiveIndex")
         .set("ki", new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0"});
    model.component("nhalbach_system").material("mat2").label("Linear Iron");
    model.component("nhalbach_system").material("mat2").set("family", "iron");
    model.component("nhalbach_system").material("mat2").propertyGroup("def")
         .set("relpermeability", new String[]{"mu_r_iron", "0", "0", "0", "mu_r_iron", "0", "0", "0", "mu_r_iron"});
    model.component("nhalbach_system").material("mat3").label("Nd-Fe-B");

    model.component("nhalbach_system").coordSystem("ie1").label("External environment");
    model.component("nhalbach_system").coordSystem("ie1").set("ScalingType", "Cylindrical");

    model.component("nhalbach_system").physics("mfnc").feature("mfc1")
         .label("Magnetic Flux Conservation - Air regions");
    model.component("nhalbach_system").physics("mfnc").feature("mfc2").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc2")
         .label("Magnetic Flux Conservation - Iron regions");
    model.component("nhalbach_system").physics("mfnc").feature("mfc3")
         .set("ConstitutiveRelationBH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc3")
         .set("Br", new String[][]{{"1.400000*cos(-3.671093[deg])"}, {"1.400000*sin(-3.671093[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc3")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc3").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc3")
         .label("Magnetic Flux Conservation - Magnet II 1 - 1Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc4")
         .set("ConstitutiveRelationBH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc4")
         .set("Br", new String[][]{{"1.400000*cos(4.989660[deg])"}, {"1.400000*sin(4.989660[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc4")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc4").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc4")
         .label("Magnetic Flux Conservation - Magnet II 2 - 1Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc5")
         .set("ConstitutiveRelationBH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc5")
         .set("Br", new String[][]{{"1.400000*cos(-15.796774[deg])"}, {"1.400000*sin(-15.796774[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc5")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc5").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc5")
         .label("Magnetic Flux Conservation - Magnet II 3 - 1Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc6")
         .set("ConstitutiveRelationBH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc6")
         .set("Br", new String[][]{{"1.400000*cos(8.238090[deg])"}, {"1.400000*sin(8.238090[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc6")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc6").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc6")
         .label("Magnetic Flux Conservation - Magnet IV 1 - 1Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc7")
         .set("ConstitutiveRelationBH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc7")
         .set("Br", new String[][]{{"1.400000*cos(30.869840[deg])"}, {"1.400000*sin(30.869840[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc7")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc7").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc7")
         .label("Magnetic Flux Conservation - Magnet IV 2 - 1Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc8")
         .set("ConstitutiveRelationBH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc8")
         .set("Br", new String[][]{{"1.400000*cos(87.033182[deg])"}, {"1.400000*sin(87.033182[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc8")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc8").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc8")
         .label("Magnetic Flux Conservation - Magnet IV 3 - 1Q");

    model.component("nhalbach_system").mesh("mesh1").feature("size").set("hauto", 3);
    model.component("nhalbach_system").mesh("mesh1").feature("size").set("custom", "on");
    model.component("nhalbach_system").mesh("mesh1").feature("size").set("hmax", "l_element_max");
    model.component("nhalbach_system").mesh("mesh1").feature("size").set("hnarrow", "n_narrow");
    model.component("nhalbach_system").mesh("mesh1").feature("dis1").set("type", "predefined");
    model.component("nhalbach_system").mesh("mesh1").feature("map1").set("adjustedgdistr", true);
    model.component("nhalbach_system").mesh("mesh1").run();

    model.component("nhalbach_system").probe("prb1").label("Maximum Field");
    model.component("nhalbach_system").probe("prb1").set("expr", "mfnc.normB");
    model.component("nhalbach_system").probe("prb1").set("unit", "T");
    model.component("nhalbach_system").probe("prb1").set("descr", "mfnc.normB");
    model.component("nhalbach_system").probe("prb1").set("table", "results_table");
    model.component("nhalbach_system").probe("prb1").set("window", "window1");
    model.component("nhalbach_system").probe("prb2").label("Minimum Field");
    model.component("nhalbach_system").probe("prb2").set("expr", "mfnc.normB");
    model.component("nhalbach_system").probe("prb2").set("unit", "T");
    model.component("nhalbach_system").probe("prb2").set("descr", "mfnc.normB");
    model.component("nhalbach_system").probe("prb2").set("table", "results_table");
    model.component("nhalbach_system").probe("prb2").set("window", "window1");
    model.component("nhalbach_system").probe("prb3").label("Air Gap Area");
    model.component("nhalbach_system").probe("prb3").set("type", "integral");
    model.component("nhalbach_system").probe("prb3").set("expr", "4");
    model.component("nhalbach_system").probe("prb3").set("unit", "m^2");
    model.component("nhalbach_system").probe("prb3").set("descr", "4");
    model.component("nhalbach_system").probe("prb3").set("table", "results_table");
    model.component("nhalbach_system").probe("prb3").set("window", "window1");
    model.component("nhalbach_system").probe("prb4").label("Magnets Area");
    model.component("nhalbach_system").probe("prb4").set("type", "integral");
    model.component("nhalbach_system").probe("prb4").set("expr", "4");
    model.component("nhalbach_system").probe("prb4").set("unit", "m^2");
    model.component("nhalbach_system").probe("prb4").set("descr", "4");
    model.component("nhalbach_system").probe("prb4").set("table", "results_table");
    model.component("nhalbach_system").probe("prb4").set("window", "window1");
    model.component("nhalbach_system").probe("prb5").label("Magnet II H . B_rem Max");
    model.component("nhalbach_system").probe("prb5").set("type", "maximum");
    model.component("nhalbach_system").probe("prb5")
         .set("expr", "-( (mfnc.Hx * mfnc.Brx) + (mfnc.Hy * mfnc.Bry) )/mfnc.normBr");
    model.component("nhalbach_system").probe("prb5").set("unit", "");
    model.component("nhalbach_system").probe("prb5")
         .set("descr", "-( (mfnc.Hx * mfnc.Brx) + (mfnc.Hy * mfnc.Bry) )/mfnc.normBr");
    model.component("nhalbach_system").probe("prb5").set("table", "results_table");
    model.component("nhalbach_system").probe("prb5").set("window", "window1");
    model.component("nhalbach_system").probe("prb6").label("Magnet IV H . B_rem Max");
    model.component("nhalbach_system").probe("prb6").set("type", "maximum");
    model.component("nhalbach_system").probe("prb6")
         .set("expr", "-( (mfnc.Hx * mfnc.Brx) + (mfnc.Hy * mfnc.Bry) )/mfnc.normBr");
    model.component("nhalbach_system").probe("prb6").set("unit", "");
    model.component("nhalbach_system").probe("prb6")
         .set("descr", "-( (mfnc.Hx * mfnc.Brx) + (mfnc.Hy * mfnc.Bry) )/mfnc.normBr");
    model.component("nhalbach_system").probe("prb6").set("table", "results_table");
    model.component("nhalbach_system").probe("prb6").set("window", "window1");
    model.component("nhalbach_system").probe("prb7").label("Demagnetized Area");
    model.component("nhalbach_system").probe("prb7").set("type", "integral");
    model.component("nhalbach_system").probe("prb7")
         .set("expr", "if((-(mfnc.Hx * mfnc.Brx + mfnc.Hy * mfnc.Bry) / mfnc.normBr - Hc_j)>0, 4, 0)");
    model.component("nhalbach_system").probe("prb7").set("unit", "");
    model.component("nhalbach_system").probe("prb7")
         .set("descr", "if((-(mfnc.Hx * mfnc.Brx + mfnc.Hy * mfnc.Bry) / mfnc.normBr - Hc_j)>0, 4, 0)");
    model.component("nhalbach_system").probe("prb7").set("table", "results_table");
    model.component("nhalbach_system").probe("prb7").set("window", "window1");

    model.component("nhalbach_system").physics("mfnc").feature("mfc3").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc4").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc5").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc6").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc7").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc8").set("mur_mat", "userdef");

    model.study().create("std1");
    model.study("std1").create("stat", "Stationary");

    model.sol().create("sol1");
    model.sol("sol1").study("std1");
    model.sol("sol1").attach("std1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature().remove("fcDef");

    model.result().dataset().create("avh1", "Average");
    model.result().dataset().create("avh2", "Average");
    model.result().dataset().create("int1", "Integral");
    model.result().dataset().create("int2", "Integral");
    model.result().dataset().create("max1", "Maximum");
    model.result().dataset().create("max2", "Maximum");
    model.result().dataset().create("int3", "Integral");
    model.result().dataset().create("dset2", "Solution");
    model.result().dataset().create("dset3", "Solution");
    model.result().dataset().create("dset4", "Solution");
    model.result().dataset().create("dset5", "Solution");
    model.result().dataset().create("dset6", "Solution");
    model.result().dataset().create("dset7", "Solution");
    model.result().dataset().create("dset8", "Solution");
    model.result().dataset().create("pc1", "ParCurve2D");
    model.result().dataset("dset1").set("probetag", "prb7");
    model.result().dataset("avh1").set("probetag", "prb1");
    model.result().dataset("avh1").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("avh1").selection().set(11);
    model.result().dataset("avh2").set("probetag", "prb2");
    model.result().dataset("avh2").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("avh2").selection().set(3);
    model.result().dataset("int1").set("probetag", "prb3");
    model.result().dataset("int1").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("int1").selection().set(3, 11);
    model.result().dataset("int2").set("probetag", "prb4");
    model.result().dataset("int2").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("int2").selection().set(7, 8, 9, 12, 13, 14);
    model.result().dataset("max1").set("probetag", "prb5");
    model.result().dataset("max1").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("max1").selection().set(7, 8, 9);
    model.result().dataset("max2").set("probetag", "prb6");
    model.result().dataset("max2").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("max2").selection().set(12, 13, 14);
    model.result().dataset("int3").set("probetag", "prb7");
    model.result().dataset("int3").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("int3").selection().set(7, 8, 9, 12, 13, 14);
    model.result().dataset("dset3").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("dset3").selection().set(3, 11);
    model.result().dataset("dset4").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("dset4").selection().set(7, 8, 9, 12, 13, 14);
    model.result().dataset("dset5").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("dset5").selection().set(7, 8, 9);
    model.result().dataset("dset6").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("dset6").selection().set(12, 13, 14);
    model.result().dataset("dset7").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("dset7").selection().set(11);
    model.result().dataset("dset8").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("dset8").selection().set(3);
    model.result().numerical().create("pev1", "EvalPoint");
    model.result().numerical().create("pev2", "EvalPoint");
    model.result().numerical().create("pev3", "EvalPoint");
    model.result().numerical().create("pev4", "EvalPoint");
    model.result().numerical().create("pev5", "EvalPoint");
    model.result().numerical().create("pev6", "EvalPoint");
    model.result().numerical().create("pev7", "EvalPoint");
    model.result().numerical("pev1").set("probetag", "prb1");
    model.result().numerical("pev2").set("probetag", "prb2");
    model.result().numerical("pev3").set("probetag", "prb3");
    model.result().numerical("pev4").set("probetag", "prb4");
    model.result().numerical("pev5").set("probetag", "prb5");
    model.result().numerical("pev6").set("probetag", "prb6");
    model.result().numerical("pev7").set("probetag", "prb7");
    model.result().create("pg1", "PlotGroup1D");
    model.result().create("pg2", "PlotGroup2D");
    model.result().create("pg3", "PlotGroup2D");
    model.result().create("pg4", "PlotGroup1D");
    model.result("pg1").set("probetag", "window1_default");
    model.result("pg1").create("tblp1", "Table");
    model.result("pg1").feature("tblp1").set("probetag", "prb1,prb2,prb3,prb4,prb5,prb6,prb7");
    model.result("pg2").set("data", "dset3");
    model.result("pg2").create("surf1", "Surface");
    model.result("pg3").set("data", "dset4");
    model.result("pg3").create("arws1", "ArrowSurface");
    model.result("pg3").create("surf1", "Surface");
    model.result("pg3").create("arws2", "ArrowSurface");
    model.result("pg4").create("lngr1", "LineGraph");
    model.result().export().create("export1", "Data");
    model.result().export().create("export2", "Data");
    model.result().export().create("export4", "Data");
    model.result().export().create("export5", "Data");
    model.result().export().create("export6", "Data");
    model.result().export().create("export3", "Table");

    model.component("nhalbach_system").probe("prb1").genResult(null);
    model.component("nhalbach_system").probe("prb2").genResult(null);
    model.component("nhalbach_system").probe("prb3").genResult(null);
    model.component("nhalbach_system").probe("prb4").genResult(null);
    model.component("nhalbach_system").probe("prb5").genResult(null);
    model.component("nhalbach_system").probe("prb6").genResult(null);
    model.component("nhalbach_system").probe("prb7").genResult(null);

    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result().dataset("dset1").label("Probe Solution 1");
    model.result().dataset("dset3").label("Air gap");
    model.result().dataset("dset4").label("Magnets");
    model.result().dataset("dset5").label("Magnet II 1Q");
    model.result().dataset("dset6").label("Magnet IV 1Q");
    model.result().dataset("dset7").label("Air Gap High Field Region");
    model.result().dataset("dset8").label("Air Gap Low Field Region");
    model.result().dataset("pc1").label("Air gap central line");
    model.result().dataset("pc1").set("parmax1", "pi");
    model.result().dataset("pc1").set("exprx", "(R_o+h_gap/2)*cos(s)");
    model.result().dataset("pc1").set("expry", "(R_o+h_gap/2)*sin(s)");
    model.result().numerical("pev1").set("descr", new String[]{""});
    model.result().numerical("pev2").set("descr", new String[]{""});
    model.result().numerical("pev7").set("unit", new String[]{"m^2"});
    model.result("pg1").label("Probe Plot Group 1");
    model.result("pg1").set("solrepresentation", "solnum");
    model.result("pg1").set("xlabel", "mfnc.normB (T), Maximum Field");
    model.result("pg1").set("windowtitle", "Probe Plot 1");
    model.result("pg1").set("xlabelactive", false);
    model.result("pg1").feature("tblp1").label("Probe Table Graph 1");
    model.result("pg1").feature("tblp1").set("legend", true);
    model.result("pg2").label("Air gap");
    model.result("pg2").feature("surf1").label("B");
    model.result("pg2").feature("surf1").set("expr", "mfnc.normB");
    model.result("pg2").feature("surf1").set("unit", "T");
    model.result("pg2").feature("surf1").set("descr", "Magnetic flux density norm");
    model.result("pg2").feature("surf1").set("resolution", "normal");
    model.result("pg3").label("Magnets");
    model.result("pg3").feature("arws1").label("B_rem");
    model.result("pg3").feature("arws1").set("expr", new String[]{"mfnc.Brx", "mfnc.Bry"});
    model.result("pg3").feature("arws1").set("descr", "Remanent flux density");
    model.result("pg3").feature("arws1").set("arrowbase", "center");
    model.result("pg3").feature("arws1").set("scale", 0.0077221241233942925);
    model.result("pg3").feature("arws1").set("scaleactive", false);
    model.result("pg3").feature("surf1").label("Psi");
    model.result("pg3").feature("surf1")
         .set("expr", "abs((mfnc.Bx*mfnc.Brx+mfnc.By*mfnc.Bry)/mfnc.normBr)*abs((mfnc.Hx*mfnc.Brx+mfnc.Hy*mfnc.Bry)/mfnc.normBr)");
    model.result("pg3").feature("surf1").set("unit", "kPa");
    model.result("pg3").feature("surf1")
         .set("descr", "abs((mfnc.Bx*mfnc.Brx+mfnc.By*mfnc.Bry)/mfnc.normBr)*abs((mfnc.Hx*mfnc.Brx+mfnc.Hy*mfnc.Bry)/mfnc.normBr)");
    model.result("pg3").feature("surf1").set("rangecoloractive", true);
    model.result("pg3").feature("surf1").set("rangecolormax", 409);
    model.result("pg3").feature("surf1").set("rangedataactive", true);
    model.result("pg3").feature("surf1").set("rangedatamax", 409);
    model.result("pg3").feature("surf1").set("resolution", "normal");
    model.result("pg3").feature("arws2").label("H");
    model.result("pg3").feature("arws2").set("expr", new String[]{"mu0_const*mfnc.Hx", "mu0_const*mfnc.Hy"});
    model.result("pg3").feature("arws2").set("descractive", true);
    model.result("pg3").feature("arws2").set("descr", "mu0*H");
    model.result("pg3").feature("arws2").set("arrowbase", "center");
    model.result("pg3").feature("arws2").set("scale", 0.010592406010573617);
    model.result("pg3").feature("arws2").set("color", "black");
    model.result("pg3").feature("arws2").set("scaleactive", false);
    model.result("pg4").label("Air gap central line");
    model.result("pg4").set("data", "pc1");
    model.result("pg4").set("xlabel", "Arc length");
    model.result("pg4").set("ylabel", "Magnetic flux density norm (T)");
    model.result("pg4").set("xlabelactive", false);
    model.result("pg4").set("ylabelactive", false);
    model.result("pg4").feature("lngr1").label("Magnetic Profile");
    model.result("pg4").feature("lngr1").set("expr", "mfnc.normB");
    model.result("pg4").feature("lngr1").set("unit", "T");
    model.result("pg4").feature("lngr1").set("descr", "Magnetic flux density norm");
    model.result("pg4").feature("lngr1").set("smooth", "internal");
    model.result("pg4").feature("lngr1").set("resolution", "normal");
    model.result().export("export1").set("data", "dset7");
    model.result().export("export1").set("expr", new String[]{"mfnc.normB"});
    model.result().export("export1").set("unit", new String[]{"T"});
    model.result().export("export1").set("descr", new String[]{"Magnetic flux density norm"});
    model.result().export("export1").set("filename", "B_high.txt");
    model.result().export("export1").set("smooth", "internal");
    model.result().export("export1").set("allowmaterialsmoothing", true);
    model.result().export("export2").set("data", "dset8");
    model.result().export("export2").set("expr", new String[]{"mfnc.normB"});
    model.result().export("export2").set("unit", new String[]{"T"});
    model.result().export("export2").set("descr", new String[]{"Magnetic flux density norm"});
    model.result().export("export2").set("filename", "B_low.txt");
    model.result().export("export2").set("smooth", "internal");
    model.result().export("export2").set("allowmaterialsmoothing", true);
    model.result().export("export4").set("data", "dset5");
    model.result().export("export4")
         .set("expr", new String[]{"mfnc.Bx", "mfnc.By", "mfnc.Hx", "mfnc.Hy", "mfnc.Brx", "mfnc.Bry"});
    model.result().export("export4").set("unit", new String[]{"T", "T", "A/m", "A/m", "T", "T"});
    model.result().export("export4")
         .set("descr", new String[]{"Magnetic flux density, x component", "Magnetic flux density, y component", "Magnetic field, x component", "Magnetic field, y component", "Remanent flux density, x component", "Remanent flux density, y component"});
    model.result().export("export4").set("filename", "H_II_1Q.txt");
    model.result().export("export4").set("smooth", "internal");
    model.result().export("export4").set("allowmaterialsmoothing", true);
    model.result().export("export5").set("data", "dset6");
    model.result().export("export5")
         .set("expr", new String[]{"mfnc.Bx", "mfnc.By", "mfnc.Hx", "mfnc.Hy", "mfnc.Brx", "mfnc.Bry"});
    model.result().export("export5").set("unit", new String[]{"T", "T", "A/m", "A/m", "T", "T"});
    model.result().export("export5")
         .set("descr", new String[]{"Magnetic flux density, x component", "Magnetic flux density, y component", "Magnetic field, x component", "Magnetic field, y component", "Remanent flux density, x component", "Remanent flux density, y component"});
    model.result().export("export5").set("filename", "H_IV_1Q.txt");
    model.result().export("export5").set("smooth", "internal");
    model.result().export("export5").set("allowmaterialsmoothing", true);
    model.result().export("export6").set("data", "dset3");
    model.result().export("export6").set("expr", new String[]{"mfnc.Bx", "mfnc.By", "mfnc.Hx", "mfnc.Hy"});
    model.result().export("export6").set("unit", new String[]{"T", "T", "A/m", "A/m"});
    model.result().export("export6")
         .set("descr", new String[]{"Magnetic flux density, x component", "Magnetic flux density, y component", "Magnetic field, x component", "Magnetic field, y component"});
    model.result().export("export6").set("filename", "B_III.txt");
    model.result().export("export3").set("filename", "COMSOL Main Results.txt");

    return model;
  }

  public static void main(String[] args) {
    Model model = run();
    run2(model);
  }

}
