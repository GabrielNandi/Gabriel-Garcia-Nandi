/*
 * TeslaMax_Model.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;

/** Model exported on Jan 26 2017, 10:36 by COMSOL 5.2.1.229. */
public class TeslaMax_Model {

  public static Model run() {
    Model model = ModelUtil.create("Model");

    model.modelPath("C:\\Users\\fabiofortkamp\\code\\TeslaMax\\java");

    model.label("TeslaMax_Model.mph");

    model.comments("TeslaMax Model\n\n");

    model.param().set("phi_C_II", "15");
    model.param().set("phi_S_II", "45");
    model.param().set("alpha_rem_IV_2", "45");
    model.param().set("n_IV", "3");
    model.param().set("B_rem_II", "1.4");
    model.param().set("B_rem_IV", "1.4");
    model.param().set("mu_r_iron", "5000.0");
    model.param().set("linear_iron", "1");
    model.param().set("delta_phi_S_IV", "15.0");
    model.param().set("R_c", "0.15000000000000002");
    model.param().set("delta_phi_S_II", "10.0");
    model.param().set("R_s", "0.14");
    model.param().set("h_fc", "0.01");
    model.param().set("R_o", "0.07");
    model.param().set("alpha_rem_II_2", "30");
    model.param().set("R_e", "2");
    model.param().set("R_i", "0.015");
    model.param().set("phi_S_IV", "45");
    model.param().set("mu_r_II", "1.05");
    model.param().set("alpha_rem_IV_1", "15");
    model.param().set("h_gap", "0.02");
    model.param().set("alpha_rem_II_3", "45");
    model.param().set("n_II", "3");
    model.param().set("alpha_rem_II_1", "15");
    model.param().set("alpha_rem_IV_3", "135");
    model.param().set("mu_r_IV", "1.05");
    model.param().set("R_g", "0.09000000000000001");

    model.modelNode().create("nhalbach_system");

    model.geom().create("nhalbach_geometry", 2);

    model.mesh().create("mesh1", "nhalbach_geometry");

    model.geom().create("cylinder_block", "Part", 2);
    model.geom().create("cylinder_shell", "Part", 2);
    model.geom("cylinder_block").label("Cylinder Block");
    model.geom("cylinder_block").inputParam().set("r1", "R_i");
    model.geom("cylinder_block").inputParam().set("r2", "R_o");
    model.geom("cylinder_block").inputParam().set("phi1", "15[deg]");
    model.geom("cylinder_block").inputParam().set("phi2", "45[deg]");
    model.geom("cylinder_block").create("c1", "Circle");
    model.geom("cylinder_block").feature("c1").set("r", "r2");
    model.geom("cylinder_block").feature("c1").set("rot", "phi1");
    model.geom("cylinder_block").feature("c1").set("angle", "phi2-phi1");
    model.geom("cylinder_block").create("c2", "Circle");
    model.geom("cylinder_block").feature("c2").set("r", "r1");
    model.geom("cylinder_block").feature("c2").set("rot", "phi1");
    model.geom("cylinder_block").feature("c2").set("angle", "phi2-phi1");
    model.geom("cylinder_block").create("dif1", "Difference");
    model.geom("cylinder_block").feature("dif1").selection("input2").set(new String[]{"c2"});
    model.geom("cylinder_block").feature("dif1").selection("input").set(new String[]{"c1"});
    model.geom("cylinder_block").create("dom", "ExplicitSelection");
    model.geom("cylinder_block").feature("dom").selection("selection").set("dif1(1)", new int[]{1});
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
    model.geom("cylinder_shell").feature("dif1").selection("input2").set(new String[]{"c2"});
    model.geom("cylinder_shell").feature("dif1").selection("input").set(new String[]{"c1"});
    model.geom("cylinder_shell").create("dom", "ExplicitSelection");
    model.geom("cylinder_shell").feature("dom").selection("selection").set("dif1(1)", new int[]{1});
    model.geom("cylinder_shell").run();
    model.geom("nhalbach_geometry").create("iron_ii1", "PartInstance");
    model.geom("nhalbach_geometry").feature("iron_ii1").label("Cylinder Block - Iron II Wedge 1Q");
    model.geom("nhalbach_geometry").feature("iron_ii1")
         .set("inputexpr", new String[]{"R_i", "R_o", "0.0[deg]", "phi_C_II"});
    model.geom("nhalbach_geometry").feature("iron_ii1").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("iron_ii1").setEntry("selkeepdom", "iron_ii1_dom", "on");
    model.geom("nhalbach_geometry").create("iron_ii2", "PartInstance");
    model.geom("nhalbach_geometry").feature("iron_ii2").label("Cylinder Block - Iron II Wedge 2Q");
    model.geom("nhalbach_geometry").feature("iron_ii2")
         .set("inputexpr", new String[]{"R_i", "R_o", "180[deg] - phi_C_II", "180[deg]"});
    model.geom("nhalbach_geometry").feature("iron_ii2").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("iron_ii2").setEntry("selkeepdom", "iron_ii2_dom", "on");
    model.geom("nhalbach_geometry").create("magnet_ii_1q1", "PartInstance");
    model.geom("nhalbach_geometry").feature("magnet_ii_1q1").label("Cylinder Block 1 - Magnet II - 1Q");
    model.geom("nhalbach_geometry").feature("magnet_ii_1q1")
         .set("inputexpr", new String[]{"R_i", "R_o", "phi_C_II + 0 * delta_phi_S_II", "phi_C_II + 1 * delta_phi_S_II"});
    model.geom("nhalbach_geometry").feature("magnet_ii_1q1").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("magnet_ii_1q1").setEntry("selkeepdom", "magnet_ii_1q1_dom", "on");
    model.geom("nhalbach_geometry").create("magnet_ii_2q1", "PartInstance");
    model.geom("nhalbach_geometry").feature("magnet_ii_2q1").label("Cylinder Block 1 - Magnet II - 2Q");
    model.geom("nhalbach_geometry").feature("magnet_ii_2q1")
         .set("inputexpr", new String[]{"R_i", "R_o", "(180[deg] - phi_C_II) - 1 * delta_phi_S_II", "(180[deg] - phi_C_II) - 0 * delta_phi_S_II"});
    model.geom("nhalbach_geometry").feature("magnet_ii_2q1").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("magnet_ii_2q1").setEntry("selkeepdom", "magnet_ii_2q1_dom", "on");
    model.geom("nhalbach_geometry").create("magnet_ii_1q2", "PartInstance");
    model.geom("nhalbach_geometry").feature("magnet_ii_1q2").label("Cylinder Block 2 - Magnet II - 1Q");
    model.geom("nhalbach_geometry").feature("magnet_ii_1q2")
         .set("inputexpr", new String[]{"R_i", "R_o", "phi_C_II + 1 * delta_phi_S_II", "phi_C_II + 2 * delta_phi_S_II"});
    model.geom("nhalbach_geometry").feature("magnet_ii_1q2").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("magnet_ii_1q2").setEntry("selkeepdom", "magnet_ii_1q2_dom", "on");
    model.geom("nhalbach_geometry").create("magnet_ii_2q2", "PartInstance");
    model.geom("nhalbach_geometry").feature("magnet_ii_2q2").label("Cylinder Block 2 - Magnet II - 2Q");
    model.geom("nhalbach_geometry").feature("magnet_ii_2q2")
         .set("inputexpr", new String[]{"R_i", "R_o", "(180[deg] - phi_C_II) - 2 * delta_phi_S_II", "(180[deg] - phi_C_II) - 1 * delta_phi_S_II"});
    model.geom("nhalbach_geometry").feature("magnet_ii_2q2").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("magnet_ii_2q2").setEntry("selkeepdom", "magnet_ii_2q2_dom", "on");
    model.geom("nhalbach_geometry").create("magnet_ii_1q3", "PartInstance");
    model.geom("nhalbach_geometry").feature("magnet_ii_1q3").label("Cylinder Block 3 - Magnet II - 1Q");
    model.geom("nhalbach_geometry").feature("magnet_ii_1q3")
         .set("inputexpr", new String[]{"R_i", "R_o", "phi_C_II + 2 * delta_phi_S_II", "phi_C_II + 3 * delta_phi_S_II"});
    model.geom("nhalbach_geometry").feature("magnet_ii_1q3").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("magnet_ii_1q3").setEntry("selkeepdom", "magnet_ii_1q3_dom", "on");
    model.geom("nhalbach_geometry").create("magnet_ii_2q3", "PartInstance");
    model.geom("nhalbach_geometry").feature("magnet_ii_2q3").label("Cylinder Block 3 - Magnet II - 2Q");
    model.geom("nhalbach_geometry").feature("magnet_ii_2q3")
         .set("inputexpr", new String[]{"R_i", "R_o", "(180[deg] - phi_C_II) - 3 * delta_phi_S_II", "(180[deg] - phi_C_II) - 2 * delta_phi_S_II"});
    model.geom("nhalbach_geometry").feature("magnet_ii_2q3").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("magnet_ii_2q3").setEntry("selkeepdom", "magnet_ii_2q3_dom", "on");
    model.geom("nhalbach_geometry").create("iron_ii3", "PartInstance");
    model.geom("nhalbach_geometry").feature("iron_ii3").label("Cylinder Block - Iron II");
    model.geom("nhalbach_geometry").feature("iron_ii3")
         .set("inputexpr", new String[]{"R_i", "R_o", "phi_S_II", "180[deg] - phi_S_II"});
    model.geom("nhalbach_geometry").feature("iron_ii3").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("iron_ii3").setEntry("selkeepdom", "iron_ii3_dom", "on");
    model.geom("nhalbach_geometry").create("magnet_iv_1q1", "PartInstance");
    model.geom("nhalbach_geometry").feature("magnet_iv_1q1").label("Cylinder Block 1 - Magnet IV - 1Q");
    model.geom("nhalbach_geometry").feature("magnet_iv_1q1")
         .set("inputexpr", new String[]{"R_g", "R_s", "0 * delta_phi_S_IV", "1 * delta_phi_S_IV"});
    model.geom("nhalbach_geometry").feature("magnet_iv_1q1").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("magnet_iv_1q1").setEntry("selkeepdom", "magnet_iv_1q1_dom", "on");
    model.geom("nhalbach_geometry").create("magnet_iv_2q1", "PartInstance");
    model.geom("nhalbach_geometry").feature("magnet_iv_2q1").label("Cylinder Block 1 - Magnet IV - 2Q");
    model.geom("nhalbach_geometry").feature("magnet_iv_2q1")
         .set("inputexpr", new String[]{"R_g", "R_s", "180[deg] - 1 * delta_phi_S_IV", "180[deg] - 0 * delta_phi_S_IV"});
    model.geom("nhalbach_geometry").feature("magnet_iv_2q1").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("magnet_iv_2q1").setEntry("selkeepdom", "magnet_iv_2q1_dom", "on");
    model.geom("nhalbach_geometry").create("magnet_iv_1q2", "PartInstance");
    model.geom("nhalbach_geometry").feature("magnet_iv_1q2").label("Cylinder Block 2 - Magnet IV - 1Q");
    model.geom("nhalbach_geometry").feature("magnet_iv_1q2")
         .set("inputexpr", new String[]{"R_g", "R_s", "1 * delta_phi_S_IV", "2 * delta_phi_S_IV"});
    model.geom("nhalbach_geometry").feature("magnet_iv_1q2").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("magnet_iv_1q2").setEntry("selkeepdom", "magnet_iv_1q2_dom", "on");
    model.geom("nhalbach_geometry").create("magnet_iv_2q2", "PartInstance");
    model.geom("nhalbach_geometry").feature("magnet_iv_2q2").label("Cylinder Block 2 - Magnet IV - 2Q");
    model.geom("nhalbach_geometry").feature("magnet_iv_2q2")
         .set("inputexpr", new String[]{"R_g", "R_s", "180[deg] - 2 * delta_phi_S_IV", "180[deg] - 1 * delta_phi_S_IV"});
    model.geom("nhalbach_geometry").feature("magnet_iv_2q2").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("magnet_iv_2q2").setEntry("selkeepdom", "magnet_iv_2q2_dom", "on");
    model.geom("nhalbach_geometry").create("magnet_iv_1q3", "PartInstance");
    model.geom("nhalbach_geometry").feature("magnet_iv_1q3").label("Cylinder Block 3 - Magnet IV - 1Q");
    model.geom("nhalbach_geometry").feature("magnet_iv_1q3")
         .set("inputexpr", new String[]{"R_g", "R_s", "2 * delta_phi_S_IV", "3 * delta_phi_S_IV"});
    model.geom("nhalbach_geometry").feature("magnet_iv_1q3").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("magnet_iv_1q3").setEntry("selkeepdom", "magnet_iv_1q3_dom", "on");
    model.geom("nhalbach_geometry").create("magnet_iv_2q3", "PartInstance");
    model.geom("nhalbach_geometry").feature("magnet_iv_2q3").label("Cylinder Block 3 - Magnet IV - 2Q");
    model.geom("nhalbach_geometry").feature("magnet_iv_2q3")
         .set("inputexpr", new String[]{"R_g", "R_s", "180[deg] - 3 * delta_phi_S_IV", "180[deg] - 2 * delta_phi_S_IV"});
    model.geom("nhalbach_geometry").feature("magnet_iv_2q3").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("magnet_iv_2q3").setEntry("selkeepdom", "magnet_iv_2q3_dom", "on");
    model.geom("nhalbach_geometry").create("iron_iv1", "PartInstance");
    model.geom("nhalbach_geometry").feature("iron_iv1").label("Cylinder Block - Iron IV");
    model.geom("nhalbach_geometry").feature("iron_iv1")
         .set("inputexpr", new String[]{"R_g", "R_s", "phi_S_IV", "180[deg] - phi_S_IV"});
    model.geom("nhalbach_geometry").feature("iron_iv1").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("iron_iv1").setEntry("selkeepdom", "iron_iv1_dom", "on");
    model.geom("nhalbach_geometry").create("shaft1", "Circle");
    model.geom("nhalbach_geometry").feature("shaft1").label("Circle");
    model.geom("nhalbach_geometry").feature("shaft1").set("selresult", "on");
    model.geom("nhalbach_geometry").feature("shaft1").set("r", "R_i");
    model.geom("nhalbach_geometry").feature("shaft1").set("angle", "180[deg]");
    model.geom("nhalbach_geometry").create("air_gap1", "PartInstance");
    model.geom("nhalbach_geometry").feature("air_gap1").label("Air Gap Cylinder Block High Field  1Q");
    model.geom("nhalbach_geometry").feature("air_gap1").set("inputexpr", new String[]{"R_o", "R_g", "0", "45[deg]"});
    model.geom("nhalbach_geometry").feature("air_gap1").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("air_gap1").setEntry("selkeepdom", "air_gap1_dom", "on");
    model.geom("nhalbach_geometry").create("air_gap2", "PartInstance");
    model.geom("nhalbach_geometry").feature("air_gap2").label("Air Gap Cylinder Block Low Field  1Q");
    model.geom("nhalbach_geometry").feature("air_gap2")
         .set("inputexpr", new String[]{"R_o", "R_g", "45[deg]", "90[deg]"});
    model.geom("nhalbach_geometry").feature("air_gap2").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("air_gap2").setEntry("selkeepdom", "air_gap2_dom", "on");
    model.geom("nhalbach_geometry").create("air_gap3", "PartInstance");
    model.geom("nhalbach_geometry").feature("air_gap3").label("Air Gap Cylinder Block High Field  2Q");
    model.geom("nhalbach_geometry").feature("air_gap3")
         .set("inputexpr", new String[]{"R_o", "R_g", "180[deg]-45[deg]", "180[deg]"});
    model.geom("nhalbach_geometry").feature("air_gap3").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("air_gap3").setEntry("selkeepdom", "air_gap3_dom", "on");
    model.geom("nhalbach_geometry").create("air_gap4", "PartInstance");
    model.geom("nhalbach_geometry").feature("air_gap4").label("Air Gap Cylinder Block Low Field  2Q");
    model.geom("nhalbach_geometry").feature("air_gap4")
         .set("inputexpr", new String[]{"R_o", "R_g", "90[deg]", "180[deg]-45[deg]"});
    model.geom("nhalbach_geometry").feature("air_gap4").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("air_gap4").setEntry("selkeepdom", "air_gap4_dom", "on");
    model.geom("nhalbach_geometry").create("flux_concentrator1", "PartInstance");
    model.geom("nhalbach_geometry").feature("flux_concentrator1").label("Flux Concentrator Shell");
    model.geom("nhalbach_geometry").feature("flux_concentrator1").set("part", "cylinder_shell");
    model.geom("nhalbach_geometry").feature("flux_concentrator1")
         .set("inputexpr", new String[]{"R_s", "R_c", "180[deg]"});
    model.geom("nhalbach_geometry").feature("flux_concentrator1").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("flux_concentrator1")
         .setEntry("selkeepdom", "flux_concentrator1_dom", "on");
    model.geom("nhalbach_geometry").create("environment1", "PartInstance");
    model.geom("nhalbach_geometry").feature("environment1").label("Environment Cylinder Shell");
    model.geom("nhalbach_geometry").feature("environment1").set("part", "cylinder_shell");
    model.geom("nhalbach_geometry").feature("environment1").set("inputexpr", new String[]{"R_c", "R_e", "180[deg]"});
    model.geom("nhalbach_geometry").feature("environment1").set("selkeepnoncontr", false);
    model.geom("nhalbach_geometry").feature("environment1").setEntry("selkeepdom", "environment1_dom", "on");
    model.geom("nhalbach_geometry").run();
    model.geom("nhalbach_geometry").run("fin");

    model.selection().create("shaft_selection", "Explicit");
    model.selection("shaft_selection").set(new int[]{14});
    model.selection().create("air_gap_high_selection", "Explicit");
    model.selection("air_gap_high_selection").set(new int[]{20});
    model.selection().create("air_gap_low_selection", "Explicit");
    model.selection("air_gap_low_selection").set(new int[]{15});
    model.selection().create("air_gap_selection", "Explicit");
    model.selection("air_gap_selection").set(new int[]{7, 10, 15, 20});
    model.selection().create("environment_selection", "Explicit");
    model.selection("environment_selection").set(new int[]{1});
    model.selection().create("environment_horization_boundary_selection", "Union");
    model.selection("environment_horization_boundary_selection").set("entitydim", "1");
    model.selection().create("environment_sel_right", "Ball");
    model.selection("environment_sel_right").set("entitydim", "1");
    model.selection().create("environment_sel_left", "Ball");
    model.selection("environment_sel_left").set("entitydim", "1");
    model.selection().create("magnets_selection", "Explicit");
    model.selection("magnets_selection").set(new int[]{3, 4, 5, 9, 11, 12, 16, 17, 18, 21, 22, 23});
    model.selection().create("magnets_ii_1q_selection", "Explicit");
    model.selection("magnets_ii_1q_selection").set(new int[]{16, 17, 18});
    model.selection().create("magnets_iv_1q_selection", "Explicit");
    model.selection("magnets_iv_1q_selection").set(new int[]{21, 22, 23});
    model.selection().create("iron_selection", "Explicit");
    model.selection("iron_selection").set(new int[]{2, 6, 8, 13, 19});
    model.selection().create("air_regions_selection", "Explicit");
    model.selection("air_regions_selection").set(new int[]{1, 7, 10, 14, 15, 20});
    model.selection().create("magnetic_circuit_regions_seletion", "Explicit");
    model.selection("magnetic_circuit_regions_seletion")
         .set(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23});
    model.selection("shaft_selection").label("Shaft");
    model.selection("air_gap_high_selection").label("Air Gap High");
    model.selection("air_gap_low_selection").label("Air Gap Low");
    model.selection("air_gap_selection").label("Air Gap");
    model.selection("environment_selection").label("Environment");
    model.selection("environment_horization_boundary_selection")
         .set("input", new String[]{"environment_sel_right", "environment_sel_left"});
    model.selection("environment_horization_boundary_selection").label("Environment boundary");
    model.selection("environment_sel_right").set("posx", "1.075");
    model.selection("environment_sel_right").set("r", "0.001");
    model.selection("environment_sel_right").label("Environment Horizontal Right Boundary");
    model.selection("environment_sel_left").set("posx", "-1.075");
    model.selection("environment_sel_left").set("r", "0.001");
    model.selection("environment_sel_left").label("Environment Horizontal Left Boundary");
    model.selection("magnets_selection").label("Magnets region");
    model.selection("magnets_ii_1q_selection").label("Magnet II 1Q region");
    model.selection("magnets_iv_1q_selection").label("Magnet IV 1Q region");
    model.selection("iron_selection").label("Iron region");
    model.selection("air_regions_selection").label("Air regions");
    model.selection("magnetic_circuit_regions_seletion").label("Magnetic circuit regions (except environment)");

    model.material().create("mat1", "Common", "nhalbach_system");
    model.material().create("mat2", "Common", "nhalbach_system");
    model.material().create("mat3", "Common", "nhalbach_system");
    model.material("mat1").selection().named("air_regions_selection");
    model.material("mat1").propertyGroup("def").func().create("eta", "Piecewise");
    model.material("mat1").propertyGroup("def").func().create("Cp", "Piecewise");
    model.material("mat1").propertyGroup("def").func().create("rho", "Analytic");
    model.material("mat1").propertyGroup("def").func().create("k", "Piecewise");
    model.material("mat1").propertyGroup("def").func().create("cs", "Analytic");
    model.material("mat1").propertyGroup().create("RefractiveIndex", "Refractive index");
    model.material("mat2").selection().named("iron_selection");
    model.material("mat3").selection().named("magnets_selection");

    model.coordSystem().create("ie1", "nhalbach_geometry", "InfiniteElement");
    model.coordSystem("ie1").selection().named("environment_selection");

    model.physics().create("mfnc", "MagnetostaticsNoCurrents", "nhalbach_geometry");
    model.physics("mfnc").create("mfc2", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc2").selection().named("iron_selection");
    model.physics("mfnc").create("mfc3", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc3").selection().set(new int[]{18});
    model.physics("mfnc").create("mfc4", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc4").selection().set(new int[]{9});
    model.physics("mfnc").create("mfc5", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc5").selection().set(new int[]{17});
    model.physics("mfnc").create("mfc6", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc6").selection().set(new int[]{11});
    model.physics("mfnc").create("mfc7", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc7").selection().set(new int[]{16});
    model.physics("mfnc").create("mfc8", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc8").selection().set(new int[]{12});
    model.physics("mfnc").create("mfc9", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc9").selection().set(new int[]{23});
    model.physics("mfnc").create("mfc10", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc10").selection().set(new int[]{3});
    model.physics("mfnc").create("mfc11", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc11").selection().set(new int[]{22});
    model.physics("mfnc").create("mfc12", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc12").selection().set(new int[]{4});
    model.physics("mfnc").create("mfc13", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc13").selection().set(new int[]{21});
    model.physics("mfnc").create("mfc14", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc14").selection().set(new int[]{5});

    model.mesh("mesh1").create("dis1", "Distribution");
    model.mesh("mesh1").create("ftri1", "FreeTri");
    model.mesh("mesh1").create("map1", "Map");
    model.mesh("mesh1").feature("dis1").selection().named("environment_horization_boundary_selection");
    model.mesh("mesh1").feature("ftri1").selection().named("magnetic_circuit_regions_seletion");
    model.mesh("mesh1").feature("map1").selection().named("environment_selection");

    model.result().table().create("results_table", "Table");

    model.probe().create("prb1", "Domain");
    model.probe().create("prb2", "Domain");
    model.probe().create("prb3", "Domain");
    model.probe().create("prb4", "Domain");
    model.probe().create("prb5", "Domain");
    model.probe().create("prb6", "Domain");
    model.probe("prb1").model("nhalbach_system");
    model.probe("prb1").selection().named("air_gap_high_selection");
    model.probe("prb2").model("nhalbach_system");
    model.probe("prb2").selection().named("air_gap_low_selection");
    model.probe("prb3").model("nhalbach_system");
    model.probe("prb3").selection().named("air_gap_selection");
    model.probe("prb4").model("nhalbach_system");
    model.probe("prb4").selection().named("magnets_selection");
    model.probe("prb5").model("nhalbach_system");
    model.probe("prb5").selection().named("magnets_ii_1q_selection");
    model.probe("prb6").model("nhalbach_system");
    model.probe("prb6").selection().named("magnets_iv_1q_selection");

    model.view("view1").axis().set("abstractviewrratio", "0.050000011920928955");
    model.view("view1").axis().set("abstractviewlratio", "-0.050000011920928955");
    model.view("view1").axis().set("abstractviewxscale", "0.004646251443773508");
    model.view("view1").axis().set("abstractviewbratio", "-0.1713833212852478");
    model.view("view1").axis().set("xmax", "2.200000047683716");
    model.view("view1").axis().set("xmin", "-2.200000047683716");
    model.view("view1").axis().set("abstractviewyscale", "0.004646251909434795");
    model.view("view1").axis().set("ymax", "2.342766523361206");
    model.view("view1").axis().set("ymin", "-0.34276652336120605");
    model.view("view1").axis().set("abstractviewtratio", "0.17138338088989258");

    model.material("mat1").label("Air");
    model.material("mat1").set("family", "air");
    model.material("mat1").propertyGroup("def").func("eta")
         .set("pieces", new String[][]{{"200.0", "1600.0", "-8.38278E-7+8.35717342E-8*T^1-7.69429583E-11*T^2+4.6437266E-14*T^3-1.06585607E-17*T^4"}});
    model.material("mat1").propertyGroup("def").func("eta").set("arg", "T");
    model.material("mat1").propertyGroup("def").func("Cp")
         .set("pieces", new String[][]{{"200.0", "1600.0", "1047.63657-0.372589265*T^1+9.45304214E-4*T^2-6.02409443E-7*T^3+1.2858961E-10*T^4"}});
    model.material("mat1").propertyGroup("def").func("Cp").set("arg", "T");
    model.material("mat1").propertyGroup("def").func("rho").set("args", new String[]{"pA", "T"});
    model.material("mat1").propertyGroup("def").func("rho").set("expr", "pA*0.02897/8.314/T");
    model.material("mat1").propertyGroup("def").func("rho").set("dermethod", "manual");
    model.material("mat1").propertyGroup("def").func("rho")
         .set("plotargs", new String[][]{{"pA", "0", "1"}, {"T", "0", "1"}});
    model.material("mat1").propertyGroup("def").func("rho")
         .set("argders", new String[][]{{"pA", "d(pA*0.02897/8.314/T,pA)"}, {"T", "d(pA*0.02897/8.314/T,T)"}});
    model.material("mat1").propertyGroup("def").func("k")
         .set("pieces", new String[][]{{"200.0", "1600.0", "-0.00227583562+1.15480022E-4*T^1-7.90252856E-8*T^2+4.11702505E-11*T^3-7.43864331E-15*T^4"}});
    model.material("mat1").propertyGroup("def").func("k").set("arg", "T");
    model.material("mat1").propertyGroup("def").func("cs").set("args", new String[]{"T"});
    model.material("mat1").propertyGroup("def").func("cs").set("expr", "sqrt(1.4*287*T)");
    model.material("mat1").propertyGroup("def").func("cs").set("dermethod", "manual");
    model.material("mat1").propertyGroup("def").func("cs").set("plotargs", new String[][]{{"T", "0", "1"}});
    model.material("mat1").propertyGroup("def").func("cs")
         .set("argders", new String[][]{{"T", "d(sqrt(1.4*287*T),T)"}});
    model.material("mat1").propertyGroup("def")
         .set("relpermeability", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.material("mat1").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.material("mat1").propertyGroup("def").set("dynamicviscosity", "eta(T[1/K])[Pa*s]");
    model.material("mat1").propertyGroup("def").set("ratioofspecificheat", "1.4");
    model.material("mat1").propertyGroup("def")
         .set("electricconductivity", new String[]{"0[S/m]", "0", "0", "0", "0[S/m]", "0", "0", "0", "0[S/m]"});
    model.material("mat1").propertyGroup("def").set("heatcapacity", "Cp(T[1/K])[J/(kg*K)]");
    model.material("mat1").propertyGroup("def").set("density", "rho(pA[1/Pa],T[1/K])[kg/m^3]");
    model.material("mat1").propertyGroup("def")
         .set("thermalconductivity", new String[]{"k(T[1/K])[W/(m*K)]", "0", "0", "0", "k(T[1/K])[W/(m*K)]", "0", "0", "0", "k(T[1/K])[W/(m*K)]"});
    model.material("mat1").propertyGroup("def").set("soundspeed", "cs(T[1/K])[m/s]");
    model.material("mat1").propertyGroup("def").addInput("temperature");
    model.material("mat1").propertyGroup("def").addInput("pressure");
    model.material("mat1").propertyGroup("RefractiveIndex").set("n", "");
    model.material("mat1").propertyGroup("RefractiveIndex").set("ki", "");
    model.material("mat1").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.material("mat1").propertyGroup("RefractiveIndex")
         .set("ki", new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0"});
    model.material("mat2").label("Linear Iron");
    model.material("mat2").set("family", "iron");
    model.material("mat2").propertyGroup("def")
         .set("relpermeability", new String[]{"mu_r_iron", "0", "0", "0", "mu_r_iron", "0", "0", "0", "mu_r_iron"});
    model.material("mat3").label("Nd-Fe-B");

    model.coordSystem("ie1").label("External environment");
    model.coordSystem("ie1").set("ScalingType", "Cylindrical");

    model.physics("mfnc").prop("MeshControl").set("EnableMeshControl", "1");
    model.physics("mfnc").feature("mfc1").label("Magnetic Flux Conservation - Air regions");
    model.physics("mfnc").feature("mfc2").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc2").label("Magnetic Flux Conservation - Iron regions");
    model.physics("mfnc").feature("mfc3").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc3")
         .set("Br", new String[][]{{"B_rem_II*cos(15.000000[deg])"}, {"B_rem_II*sin(15.000000[deg])"}, {"0"}});
    model.physics("mfnc").feature("mfc3")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.physics("mfnc").feature("mfc3").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc3").label("Magnetic Flux Conservation - Magnet II 1 - 1Q");
    model.physics("mfnc").feature("mfc4").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc4")
         .set("Br", new String[][]{{"B_rem_II*cos(-15.000000[deg])"}, {"B_rem_II*sin(-15.000000[deg])"}, {"0"}});
    model.physics("mfnc").feature("mfc4")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.physics("mfnc").feature("mfc4").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc4").label("Magnetic Flux Conservation - Magnet II 1 - 2Q");
    model.physics("mfnc").feature("mfc5").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc5")
         .set("Br", new String[][]{{"B_rem_II*cos(30.000000[deg])"}, {"B_rem_II*sin(30.000000[deg])"}, {"0"}});
    model.physics("mfnc").feature("mfc5")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.physics("mfnc").feature("mfc5").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc5").label("Magnetic Flux Conservation - Magnet II 2 - 1Q");
    model.physics("mfnc").feature("mfc6").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc6")
         .set("Br", new String[][]{{"B_rem_II*cos(-30.000000[deg])"}, {"B_rem_II*sin(-30.000000[deg])"}, {"0"}});
    model.physics("mfnc").feature("mfc6")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.physics("mfnc").feature("mfc6").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc6").label("Magnetic Flux Conservation - Magnet II 2 - 2Q");

    return model;
  }

  public static Model run2(Model model) {
    model.physics("mfnc").feature("mfc7").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc7")
         .set("Br", new String[][]{{"B_rem_II*cos(45.000000[deg])"}, {"B_rem_II*sin(45.000000[deg])"}, {"0"}});
    model.physics("mfnc").feature("mfc7")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.physics("mfnc").feature("mfc7").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc7").label("Magnetic Flux Conservation - Magnet II 3 - 1Q");
    model.physics("mfnc").feature("mfc8").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc8")
         .set("Br", new String[][]{{"B_rem_II*cos(-45.000000[deg])"}, {"B_rem_II*sin(-45.000000[deg])"}, {"0"}});
    model.physics("mfnc").feature("mfc8")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.physics("mfnc").feature("mfc8").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc8").label("Magnetic Flux Conservation - Magnet II 3 - 2Q");
    model.physics("mfnc").feature("mfc9").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc9")
         .set("Br", new String[][]{{"B_rem_IV*cos(15.000000[deg])"}, {"B_rem_IV*sin(15.000000[deg])"}, {"0"}});
    model.physics("mfnc").feature("mfc9")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.physics("mfnc").feature("mfc9").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc9").label("Magnetic Flux Conservation - Magnet IV 1 - 1Q");
    model.physics("mfnc").feature("mfc10").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc10")
         .set("Br", new String[][]{{"B_rem_IV*cos(-15.000000[deg])"}, {"B_rem_IV*sin(-15.000000[deg])"}, {"0"}});
    model.physics("mfnc").feature("mfc10")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.physics("mfnc").feature("mfc10").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc10").label("Magnetic Flux Conservation - Magnet IV 1 - 2Q");
    model.physics("mfnc").feature("mfc11").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc11")
         .set("Br", new String[][]{{"B_rem_IV*cos(45.000000[deg])"}, {"B_rem_IV*sin(45.000000[deg])"}, {"0"}});
    model.physics("mfnc").feature("mfc11")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.physics("mfnc").feature("mfc11").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc11").label("Magnetic Flux Conservation - Magnet IV 2 - 1Q");
    model.physics("mfnc").feature("mfc12").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc12")
         .set("Br", new String[][]{{"B_rem_IV*cos(-45.000000[deg])"}, {"B_rem_IV*sin(-45.000000[deg])"}, {"0"}});
    model.physics("mfnc").feature("mfc12")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.physics("mfnc").feature("mfc12").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc12").label("Magnetic Flux Conservation - Magnet IV 2 - 2Q");
    model.physics("mfnc").feature("mfc13").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc13")
         .set("Br", new String[][]{{"B_rem_IV*cos(135.000000[deg])"}, {"B_rem_IV*sin(135.000000[deg])"}, {"0"}});
    model.physics("mfnc").feature("mfc13")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.physics("mfnc").feature("mfc13").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc13").label("Magnetic Flux Conservation - Magnet IV 3 - 1Q");
    model.physics("mfnc").feature("mfc14").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc14")
         .set("Br", new String[][]{{"B_rem_IV*cos(-135.000000[deg])"}, {"B_rem_IV*sin(-135.000000[deg])"}, {"0"}});
    model.physics("mfnc").feature("mfc14")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.physics("mfnc").feature("mfc14").set("materialType", "from_mat");
    model.physics("mfnc").feature("mfc14").label("Magnetic Flux Conservation - Magnet IV 3 - 2Q");

    model.mesh("mesh1").feature("size").set("hauto", 3);
    model.mesh("mesh1").feature("size").set("custom", "on");
    model.mesh("mesh1").feature("size").set("hnarrow", "5");
    model.mesh("mesh1").feature("dis1").set("type", "predefined");
    model.mesh("mesh1").feature("map1").set("adjustedgdistr", true);
    model.mesh("mesh1").run();

    model.probe("prb1").label("Maximum Field");
    model.probe("prb1").set("table", "results_table");
    model.probe("prb1").set("descr", "mfnc.normB");
    model.probe("prb1").set("window", "window1");
    model.probe("prb1").set("expr", "mfnc.normB");
    model.probe("prb1").set("unit", "T");
    model.probe("prb1").set("descr", "mfnc.normB");
    model.probe("prb2").label("Minimum Field");
    model.probe("prb2").set("table", "results_table");
    model.probe("prb2").set("descr", "mfnc.normB");
    model.probe("prb2").set("window", "window1");
    model.probe("prb2").set("expr", "mfnc.normB");
    model.probe("prb2").set("unit", "T");
    model.probe("prb2").set("descr", "mfnc.normB");
    model.probe("prb3").label("Air Gap Area");
    model.probe("prb3").set("table", "results_table");
    model.probe("prb3").set("descr", "2");
    model.probe("prb3").set("window", "window1");
    model.probe("prb3").set("expr", "2");
    model.probe("prb3").set("type", "integral");
    model.probe("prb3").set("unit", "m^2");
    model.probe("prb4").label("Magnets Area");
    model.probe("prb4").set("table", "results_table");
    model.probe("prb4").set("descr", "2");
    model.probe("prb4").set("window", "window1");
    model.probe("prb4").set("expr", "2");
    model.probe("prb4").set("type", "integral");
    model.probe("prb4").set("unit", "m^2");
    model.probe("prb5").label("Magnet II H . B_rem Max");
    model.probe("prb5").set("table", "results_table");
    model.probe("prb5").set("descr", "-( (mfnc.Hx * mfnc.Brx) + (mfnc.Hy * mfnc.Bry) )/mfnc.normBr");
    model.probe("prb5").set("window", "window1");
    model.probe("prb5").set("expr", "-( (mfnc.Hx * mfnc.Brx) + (mfnc.Hy * mfnc.Bry) )/mfnc.normBr");
    model.probe("prb5").set("type", "maximum");
    model.probe("prb5").set("unit", "");
    model.probe("prb6").label("Magnet IV H . B_rem Max");
    model.probe("prb6").set("table", "results_table");
    model.probe("prb6").set("descr", "-( (mfnc.Hx * mfnc.Brx) + (mfnc.Hy * mfnc.Bry) )/mfnc.normBr");
    model.probe("prb6").set("window", "window1");
    model.probe("prb6").set("expr", "-( (mfnc.Hx * mfnc.Brx) + (mfnc.Hy * mfnc.Bry) )/mfnc.normBr");
    model.probe("prb6").set("type", "maximum");
    model.probe("prb6").set("unit", "");

    model.physics("mfnc").feature("mfc3").set("mur_mat", "userdef");
    model.physics("mfnc").feature("mfc4").set("mur_mat", "userdef");
    model.physics("mfnc").feature("mfc5").set("mur_mat", "userdef");
    model.physics("mfnc").feature("mfc6").set("mur_mat", "userdef");
    model.physics("mfnc").feature("mfc7").set("mur_mat", "userdef");
    model.physics("mfnc").feature("mfc8").set("mur_mat", "userdef");
    model.physics("mfnc").feature("mfc9").set("mur_mat", "userdef");
    model.physics("mfnc").feature("mfc10").set("mur_mat", "userdef");
    model.physics("mfnc").feature("mfc11").set("mur_mat", "userdef");
    model.physics("mfnc").feature("mfc12").set("mur_mat", "userdef");
    model.physics("mfnc").feature("mfc13").set("mur_mat", "userdef");
    model.physics("mfnc").feature("mfc14").set("mur_mat", "userdef");

    model.study().create("std1");
    model.study("std1").create("stat", "Stationary");

    model.sol().create("sol1");
    model.sol("sol1").study("std1");
    model.sol("sol1").attach("std1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").create("i1", "Iterative");
    model.sol("sol1").feature("s1").feature().remove("fcDef");

    model.result().dataset().create("avh1", "Average");
    model.result().dataset().create("avh2", "Average");
    model.result().dataset().create("int1", "Integral");
    model.result().dataset().create("int2", "Integral");
    model.result().dataset().create("max1", "Maximum");
    model.result().dataset().create("max2", "Maximum");
    model.result().dataset().create("dset2", "Solution");
    model.result().dataset("dset1").set("probetag", "prb6");
    model.result().dataset("avh1").set("probetag", "prb1");
    model.result().dataset("avh1").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("avh1").selection().set(new int[]{20});
    model.result().dataset("avh2").set("probetag", "prb2");
    model.result().dataset("avh2").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("avh2").selection().set(new int[]{15});
    model.result().dataset("int1").set("probetag", "prb3");
    model.result().dataset("int1").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("int1").selection().set(new int[]{7, 10, 15, 20});
    model.result().dataset("int2").set("probetag", "prb4");
    model.result().dataset("int2").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("int2").selection().set(new int[]{3, 4, 5, 9, 11, 12, 16, 17, 18, 21, 22, 23});
    model.result().dataset("max1").set("probetag", "prb5");
    model.result().dataset("max1").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("max1").selection().set(new int[]{16, 17, 18});
    model.result().dataset("max2").set("probetag", "prb6");
    model.result().dataset("max2").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("max2").selection().set(new int[]{21, 22, 23});
    model.result().numerical().create("pev1", "EvalPoint");
    model.result().numerical().create("pev2", "EvalPoint");
    model.result().numerical().create("pev3", "EvalPoint");
    model.result().numerical().create("pev4", "EvalPoint");
    model.result().numerical().create("pev5", "EvalPoint");
    model.result().numerical().create("pev6", "EvalPoint");
    model.result().numerical("pev1").set("probetag", "prb1");
    model.result().numerical("pev2").set("probetag", "prb2");
    model.result().numerical("pev3").set("probetag", "prb3");
    model.result().numerical("pev4").set("probetag", "prb4");
    model.result().numerical("pev5").set("probetag", "prb5");
    model.result().numerical("pev6").set("probetag", "prb6");
    model.result().create("pg1", "PlotGroup1D");
    model.result("pg1").set("probetag", "window1_default");
    model.result("pg1").create("tblp1", "Table");
    model.result("pg1").feature("tblp1").set("probetag", "prb1,prb2,prb3,prb4,prb5,prb6");

    model.probe("prb1").genResult(null);
    model.probe("prb2").genResult(null);
    model.probe("prb3").genResult(null);
    model.probe("prb4").genResult(null);
    model.probe("prb5").genResult(null);
    model.probe("prb6").genResult(null);

    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result().dataset("dset1").label("Probe Solution 1");
    model.result().numerical("pev1").set("descr", new String[]{""});
    model.result().numerical("pev2").set("descr", new String[]{""});
    model.result().numerical("pev6").set("unit", new String[]{"A/m"});
    model.result().numerical("pev1").setResult();
    model.result().numerical("pev2").appendResult();
    model.result().numerical("pev3").appendResult();
    model.result().numerical("pev4").appendResult();
    model.result().numerical("pev5").appendResult();
    model.result().numerical("pev6").appendResult();
    model.result("pg1").label("Probe Plot Group 1");
    model.result("pg1").set("solrepresentation", "solnum");
    model.result("pg1").set("windowtitle", "Probe Plot 1");
    model.result("pg1").feature("tblp1").label("Probe Table Graph 1");

    return model;
  }

  public static void main(String[] args) {
    Model model = run();
    run2(model);
  }

}