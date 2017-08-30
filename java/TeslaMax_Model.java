/*
 * TeslaMax_Model.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;

/** Model exported on Aug 30 2017, 13:59 by COMSOL 5.3.0.248. */
public class TeslaMax_Model {

  public static Model run() {
    Model model = ModelUtil.create("Model");

    model.modelPath("M:\\code\\TeslaMax\\java");

    model.label("TeslaMax_Model.mph");

    model.comments("TeslaMax Model\n\n");

    model.param().set("R_i", "0.015");
    model.param().set("R_o", "0.06");
    model.param().set("h_gap", "0.023");
    model.param().set("R_s", "0.18");
    model.param().set("h_fc", "0.01");
    model.param().set("R_e", "0.3");
    model.param().set("n_IV", "4");
    model.param().set("phi_S_IV", "45");
    model.param().set("n_II", "3");
    model.param().set("phi_C_II", "15");
    model.param().set("phi_S_II", "45");
    model.param().set("mu_r_II", "1.05");
    model.param().set("mu_r_IV", "1.05");
    model.param().set("linear_iron", "1");
    model.param().set("mu_r_iron", "5000.0");
    model.param().set("B_rem_II_1", "1.4");
    model.param().set("B_rem_II_2", "1.4");
    model.param().set("B_rem_II_3", "1.4");
    model.param().set("B_rem_IV_1", "1.4");
    model.param().set("B_rem_IV_2", "1.4");
    model.param().set("B_rem_IV_3", "1.4");
    model.param().set("B_rem_IV_4", "1.4");
    model.param().set("R_g", "0.08299999999999999");
    model.param().set("alpha_rem_II_1", "34.7620238277");
    model.param().set("alpha_rem_II_2", "22.2608160699");
    model.param().set("alpha_rem_II_3", "133.282364409");
    model.param().set("alpha_rem_IV_1", "0.0");
    model.param().set("alpha_rem_IV_2", "27.9671045052");
    model.param().set("alpha_rem_IV_3", "77.7285674534");
    model.param().set("alpha_rem_IV_4", "137.095784632");
    model.param().set("R_c", "0.19");
    model.param().set("delta_phi_S_II", "10.0");
    model.param().set("delta_phi_S_IV", "11.25");

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
    model.geom("cylinder_block").feature("dif1").selection("input").set(new String[]{"c1"});
    model.geom("cylinder_block").feature("dif1").selection("input2").set(new String[]{"c2"});
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
    model.geom("cylinder_shell").feature("dif1").selection("input").set(new String[]{"c1"});
    model.geom("cylinder_shell").feature("dif1").selection("input2").set(new String[]{"c2"});
    model.geom("cylinder_shell").create("dom", "ExplicitSelection");
    model.geom("cylinder_shell").feature("dom").selection("selection").set("dif1(1)", new int[]{1});
    model.geom("cylinder_shell").run();
    model.component("nhalbach_system").geom("nhalbach_geometry").create("iron_ii1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii1")
         .label("Cylinder Block - Iron II Wedge 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii1")
         .set("inputexpr", new String[]{"R_i", "R_o", "0.0[deg]", "phi_C_II"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii1").set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii1")
         .setEntry("selkeepdom", "iron_ii1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("iron_ii2", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii2").active(false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii2")
         .label("Cylinder Block - Iron II Wedge 2Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii2")
         .set("inputexpr", new String[]{"R_i", "R_o", "180[deg] - phi_C_II", "180[deg]"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii2").set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii2")
         .setEntry("selkeepdom", "iron_ii2_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_ii_1q1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q1")
         .label("Cylinder Block 1 - Magnet II - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q1")
         .set("inputexpr", new String[]{"R_i", "R_o", "phi_C_II + 0 * delta_phi_S_II", "phi_C_II + 1 * delta_phi_S_II"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q1")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q1")
         .setEntry("selkeepdom", "magnet_ii_1q1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_ii_2q1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q1").active(false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q1")
         .label("Cylinder Block 1 - Magnet II - 2Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q1")
         .set("inputexpr", new String[]{"R_i", "R_o", "(180[deg] - phi_C_II) - 1 * delta_phi_S_II", "(180[deg] - phi_C_II) - 0 * delta_phi_S_II"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q1")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q1")
         .setEntry("selkeepdom", "magnet_ii_2q1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_ii_1q2", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q2")
         .label("Cylinder Block 2 - Magnet II - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q2")
         .set("inputexpr", new String[]{"R_i", "R_o", "phi_C_II + 1 * delta_phi_S_II", "phi_C_II + 2 * delta_phi_S_II"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q2")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q2")
         .setEntry("selkeepdom", "magnet_ii_1q2_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_ii_2q2", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q2").active(false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q2")
         .label("Cylinder Block 2 - Magnet II - 2Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q2")
         .set("inputexpr", new String[]{"R_i", "R_o", "(180[deg] - phi_C_II) - 2 * delta_phi_S_II", "(180[deg] - phi_C_II) - 1 * delta_phi_S_II"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q2")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q2")
         .setEntry("selkeepdom", "magnet_ii_2q2_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_ii_1q3", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q3")
         .label("Cylinder Block 3 - Magnet II - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q3")
         .set("inputexpr", new String[]{"R_i", "R_o", "phi_C_II + 2 * delta_phi_S_II", "phi_C_II + 3 * delta_phi_S_II"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q3")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_1q3")
         .setEntry("selkeepdom", "magnet_ii_1q3_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_ii_2q3", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q3").active(false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q3")
         .label("Cylinder Block 3 - Magnet II - 2Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q3")
         .set("inputexpr", new String[]{"R_i", "R_o", "(180[deg] - phi_C_II) - 3 * delta_phi_S_II", "(180[deg] - phi_C_II) - 2 * delta_phi_S_II"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q3")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_ii_2q3")
         .setEntry("selkeepdom", "magnet_ii_2q3_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("iron_ii3", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii3")
         .label("Cylinder Block - Iron II");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii3")
         .set("inputexpr", new String[]{"R_i", "R_o", "phi_S_II", "90"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii3").set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_ii3")
         .setEntry("selkeepdom", "iron_ii3_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_iv_1q1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q1")
         .label("Cylinder Block 1 - Magnet IV - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q1")
         .set("inputexpr", new String[]{"R_g", "R_s", "0 * delta_phi_S_IV", "1 * delta_phi_S_IV"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q1")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q1")
         .setEntry("selkeepdom", "magnet_iv_1q1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_iv_2q1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q1").active(false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q1")
         .label("Cylinder Block 1 - Magnet IV - 2Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q1")
         .set("inputexpr", new String[]{"R_g", "R_s", "180[deg] - 1 * delta_phi_S_IV", "180[deg] - 0 * delta_phi_S_IV"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q1")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q1")
         .setEntry("selkeepdom", "magnet_iv_2q1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_iv_1q2", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q2")
         .label("Cylinder Block 2 - Magnet IV - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q2")
         .set("inputexpr", new String[]{"R_g", "R_s", "1 * delta_phi_S_IV", "2 * delta_phi_S_IV"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q2")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q2")
         .setEntry("selkeepdom", "magnet_iv_1q2_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_iv_2q2", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q2").active(false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q2")
         .label("Cylinder Block 2 - Magnet IV - 2Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q2")
         .set("inputexpr", new String[]{"R_g", "R_s", "180[deg] - 2 * delta_phi_S_IV", "180[deg] - 1 * delta_phi_S_IV"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q2")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q2")
         .setEntry("selkeepdom", "magnet_iv_2q2_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_iv_1q3", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q3")
         .label("Cylinder Block 3 - Magnet IV - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q3")
         .set("inputexpr", new String[]{"R_g", "R_s", "2 * delta_phi_S_IV", "3 * delta_phi_S_IV"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q3")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q3")
         .setEntry("selkeepdom", "magnet_iv_1q3_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_iv_2q3", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q3").active(false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q3")
         .label("Cylinder Block 3 - Magnet IV - 2Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q3")
         .set("inputexpr", new String[]{"R_g", "R_s", "180[deg] - 3 * delta_phi_S_IV", "180[deg] - 2 * delta_phi_S_IV"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q3")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q3")
         .setEntry("selkeepdom", "magnet_iv_2q3_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_iv_1q4", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q4")
         .label("Cylinder Block 4 - Magnet IV - 1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q4")
         .set("inputexpr", new String[]{"R_g", "R_s", "3 * delta_phi_S_IV", "4 * delta_phi_S_IV"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q4")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_1q4")
         .setEntry("selkeepdom", "magnet_iv_1q4_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("magnet_iv_2q4", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q4").active(false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q4")
         .label("Cylinder Block 4 - Magnet IV - 2Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q4")
         .set("inputexpr", new String[]{"R_g", "R_s", "180[deg] - 4 * delta_phi_S_IV", "180[deg] - 3 * delta_phi_S_IV"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q4")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("magnet_iv_2q4")
         .setEntry("selkeepdom", "magnet_iv_2q4_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("iron_iv1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_iv1")
         .label("Cylinder Block - Iron IV");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("iron_iv1")
         .set("inputexpr", new String[]{"R_g", "R_s", "phi_S_IV", "90"});
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
         .set("inputexpr", new String[]{"R_o", "R_g", "0", "45[deg]"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap1").set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap1")
         .setEntry("selkeepdom", "air_gap1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("air_gap2", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap2")
         .label("Air Gap Cylinder Block Low Field  1Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap2")
         .set("inputexpr", new String[]{"R_o", "R_g", "45[deg]", "90[deg]"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap2").set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap2")
         .setEntry("selkeepdom", "air_gap2_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("air_gap3", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap3").active(false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap3")
         .label("Air Gap Cylinder Block High Field  2Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap3")
         .set("inputexpr", new String[]{"R_o", "R_g", "180[deg]-45[deg]", "180[deg]"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap3").set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap3")
         .setEntry("selkeepdom", "air_gap3_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("air_gap4", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap4").active(false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap4")
         .label("Air Gap Cylinder Block Low Field  2Q");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap4")
         .set("inputexpr", new String[]{"R_o", "R_g", "90[deg]", "180[deg]-45[deg]"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap4").set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("air_gap4")
         .setEntry("selkeepdom", "air_gap4_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").create("flux_concentrator1", "PartInstance");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("flux_concentrator1")
         .label("Flux Concentrator Shell");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("flux_concentrator1")
         .set("part", "cylinder_shell");
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("flux_concentrator1")
         .set("inputexpr", new String[]{"R_s", "R_c", "90"});
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
         .set("inputexpr", new String[]{"R_c", "R_e", "90"});
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("environment1")
         .set("selkeepnoncontr", false);
    model.component("nhalbach_system").geom("nhalbach_geometry").feature("environment1")
         .setEntry("selkeepdom", "environment1_dom", "on");
    model.component("nhalbach_system").geom("nhalbach_geometry").run();

    model.component("nhalbach_system").selection().create("shaft_selection", "Explicit");
    model.component("nhalbach_system").selection("shaft_selection").set(new int[]{1});
    model.component("nhalbach_system").selection().create("air_gap_high_selection", "Explicit");
    model.component("nhalbach_system").selection("air_gap_high_selection").set(new int[]{11});
    model.component("nhalbach_system").selection().create("air_gap_low_selection", "Explicit");
    model.component("nhalbach_system").selection("air_gap_low_selection").set(new int[]{3});
    model.component("nhalbach_system").selection().create("air_gap_selection", "Explicit");
    model.component("nhalbach_system").selection("air_gap_selection").set(new int[]{3, 11});
    model.component("nhalbach_system").selection().create("environment_selection", "Explicit");
    model.component("nhalbach_system").selection("environment_selection").set(new int[]{6});
    model.component("nhalbach_system").selection().create("environment_horization_boundary_selection", "Union");
    model.component("nhalbach_system").selection("environment_horization_boundary_selection").set("entitydim", 1);
    model.component("nhalbach_system").selection().create("environment_sel_right", "Ball");
    model.component("nhalbach_system").selection("environment_sel_right").set("entitydim", 1);
    model.component("nhalbach_system").selection().create("environment_sel_left", "Ball");
    model.component("nhalbach_system").selection("environment_sel_left").set("entitydim", 1);
    model.component("nhalbach_system").selection().create("magnets_selection", "Explicit");
    model.component("nhalbach_system").selection("magnets_selection").set(new int[]{7, 8, 9, 12, 13, 14, 15});
    model.component("nhalbach_system").selection().create("magnets_ii_1q_selection", "Explicit");
    model.component("nhalbach_system").selection("magnets_ii_1q_selection").set(new int[]{7, 8, 9});
    model.component("nhalbach_system").selection().create("magnets_iv_1q_selection", "Explicit");
    model.component("nhalbach_system").selection("magnets_iv_1q_selection").set(new int[]{12, 13, 14, 15});
    model.component("nhalbach_system").selection().create("iron_selection", "Explicit");
    model.component("nhalbach_system").selection("iron_selection").set(new int[]{2, 4, 5, 10});
    model.component("nhalbach_system").selection().create("air_regions_selection", "Explicit");
    model.component("nhalbach_system").selection("air_regions_selection").set(new int[]{1, 3, 6, 11});
    model.component("nhalbach_system").selection().create("magnetic_circuit_regions_seletion", "Explicit");
    model.component("nhalbach_system").selection("magnetic_circuit_regions_seletion")
         .set(new int[]{1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15});
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
    model.component("nhalbach_system").selection("environment_sel_right").set("posx", 0.245);
    model.component("nhalbach_system").selection("environment_sel_right").set("r", 0.001);
    model.component("nhalbach_system").selection("environment_sel_left")
         .label("Environment Horizontal Left Boundary");
    model.component("nhalbach_system").selection("environment_sel_left").set("posx", -0.245);
    model.component("nhalbach_system").selection("environment_sel_left").set("r", 0.001);
    model.component("nhalbach_system").selection("magnets_selection").label("Magnets region");
    model.component("nhalbach_system").selection("magnets_ii_1q_selection").label("Magnet II 1Q region");
    model.component("nhalbach_system").selection("magnets_iv_1q_selection").label("Magnet IV 1Q region");
    model.component("nhalbach_system").selection("iron_selection").label("Iron region");
    model.component("nhalbach_system").selection("air_regions_selection").label("Air regions");
    model.component("nhalbach_system").selection("magnetic_circuit_regions_seletion")
         .label("Magnetic circuit regions (except environment)");

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
    model.component("nhalbach_system").physics("mfnc").create("mfc2", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc2").selection().named("iron_selection");
    model.component("nhalbach_system").physics("mfnc").create("mfc3", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc3").selection().set(new int[]{9});
    model.component("nhalbach_system").physics("mfnc").create("mfc4", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").create("mfc5", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc5").selection().set(new int[]{8});
    model.component("nhalbach_system").physics("mfnc").create("mfc6", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").create("mfc7", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc7").selection().set(new int[]{7});
    model.component("nhalbach_system").physics("mfnc").create("mfc8", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").create("mfc9", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc9").selection().set(new int[]{15});
    model.component("nhalbach_system").physics("mfnc").create("mfc10", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").create("mfc11", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc11").selection().set(new int[]{14});
    model.component("nhalbach_system").physics("mfnc").create("mfc12", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").create("mfc13", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc13").selection().set(new int[]{13});
    model.component("nhalbach_system").physics("mfnc").create("mfc14", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").create("mfc15", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").feature("mfc15").selection().set(new int[]{12});
    model.component("nhalbach_system").physics("mfnc").create("mfc16", "MagneticFluxConservation", 2);
    model.component("nhalbach_system").physics("mfnc").create("zsp1", "ZeroMagneticScalarPotential", 1);
    model.component("nhalbach_system").physics("mfnc").feature("zsp1").selection().set(new int[]{1, 3, 4, 5, 6, 7});

    model.component("nhalbach_system").mesh("mesh1").create("dis1", "Distribution");
    model.component("nhalbach_system").mesh("mesh1").create("ftri1", "FreeTri");
    model.component("nhalbach_system").mesh("mesh1").create("map1", "Map");
    model.component("nhalbach_system").mesh("mesh1").feature("dis1").selection()
         .named("environment_horization_boundary_selection");

    return model;
  }

  public static Model run2(Model model) {
    model.component("nhalbach_system").mesh("mesh1").feature("ftri1").selection()
         .named("magnetic_circuit_regions_seletion");
    model.component("nhalbach_system").mesh("mesh1").feature("map1").selection().named("environment_selection");

    model.component("nhalbach_system").probe().create("prb1", "Domain");
    model.component("nhalbach_system").probe().create("prb2", "Domain");
    model.component("nhalbach_system").probe().create("prb3", "Domain");
    model.component("nhalbach_system").probe().create("prb4", "Domain");
    model.component("nhalbach_system").probe().create("prb5", "Domain");
    model.component("nhalbach_system").probe().create("prb6", "Domain");
    model.component("nhalbach_system").probe("prb1").selection().named("air_gap_high_selection");
    model.component("nhalbach_system").probe("prb2").selection().named("air_gap_low_selection");
    model.component("nhalbach_system").probe("prb3").selection().named("air_gap_selection");
    model.component("nhalbach_system").probe("prb4").selection().named("magnets_selection");
    model.component("nhalbach_system").probe("prb5").selection().named("magnets_ii_1q_selection");
    model.component("nhalbach_system").probe("prb6").selection().named("magnets_iv_1q_selection");

    model.capeopen().label("Thermodynamics Package");

    model.component("nhalbach_system").view("view1").axis().set("xmin", -0.005036689341068268);
    model.component("nhalbach_system").view("view1").axis().set("xmax", 0.19876524806022644);
    model.component("nhalbach_system").view("view1").axis().set("ymin", -0.0032449066638946533);
    model.component("nhalbach_system").view("view1").axis().set("ymax", 0.1330413669347763);
    model.component("nhalbach_system").view("view1").axis().set("abstractviewlratio", -0.2807692587375641);
    model.component("nhalbach_system").view("view1").axis().set("abstractviewrratio", 0.2807692289352417);
    model.component("nhalbach_system").view("view1").axis().set("abstractviewbratio", -0.05000004172325134);
    model.component("nhalbach_system").view("view1").axis().set("abstractviewtratio", 0.05000004172325134);
    model.component("nhalbach_system").view("view1").axis().set("abstractviewxscale", 1.596153888385743E-4);
    model.component("nhalbach_system").view("view1").axis().set("abstractviewyscale", 1.596153888385743E-4);
    model.view("view2").axis().set("xmin", 0.0029047499410808086);
    model.view("view2").axis().set("xmax", 0.06565739959478378);
    model.view("view2").axis().set("ymin", 0.001955077052116394);
    model.view("view2").axis().set("ymax", 0.04435361176729202);
    model.view("view2").axis().set("abstractviewlratio", -0.16266152262687683);
    model.view("view2").axis().set("abstractviewrratio", 0.16266152262687683);
    model.view("view2").axis().set("abstractviewbratio", -0.05000005289912224);
    model.view("view2").axis().set("abstractviewtratio", 0.04999995604157448);
    model.view("view2").axis().set("abstractviewxscale", 7.042945799184963E-5);
    model.view("view2").axis().set("abstractviewyscale", 7.042945799184963E-5);
    model.view("view3").axis().set("xmin", -0.06599999964237213);
    model.view("view3").axis().set("xmax", 0.06599999964237213);
    model.view("view3").axis().set("ymin", -0.014592591673135757);
    model.view("view3").axis().set("ymax", 0.07459259033203125);
    model.view("view3").axis().set("abstractviewlratio", -0.05000000819563866);
    model.view("view3").axis().set("abstractviewrratio", 0.05000000819563866);
    model.view("view3").axis().set("abstractviewbratio", -0.2432098686695099);
    model.view("view3").axis().set("abstractviewtratio", 0.2432098686695099);
    model.view("view3").axis().set("abstractviewxscale", 1.4814814494457096E-4);
    model.view("view3").axis().set("abstractviewyscale", 1.4814813039265573E-4);

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

    model.component("nhalbach_system").physics("mfnc").prop("MeshControl").set("EnableMeshControl", true);
    model.component("nhalbach_system").physics("mfnc").feature("mfc1")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc1")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc1")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc1")
         .label("Magnetic Flux Conservation - Air regions");
    model.component("nhalbach_system").physics("mfnc").feature("mfc2")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc2")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc2")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc2").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc2")
         .label("Magnetic Flux Conservation - Iron regions");
    model.component("nhalbach_system").physics("mfnc").feature("mfc3")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc3")
         .set("Br", new String[][]{{"1.400000*cos(34.762024[deg])"}, {"1.400000*sin(34.762024[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc3")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc3")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc3")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc3")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc3").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc3")
         .label("Magnetic Flux Conservation - Magnet II 1 - 1Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc4")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc4")
         .set("Br", new String[][]{{"1.400000*cos(-34.762024[deg])"}, {"1.400000*sin(-34.762024[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc4")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc4")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc4")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc4")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc4").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc4").active(false);
    model.component("nhalbach_system").physics("mfnc").feature("mfc4")
         .label("Magnetic Flux Conservation - Magnet II 1 - 2Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc5")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc5")
         .set("Br", new String[][]{{"1.400000*cos(22.260816[deg])"}, {"1.400000*sin(22.260816[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc5")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc5")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc5")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc5")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc5").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc5")
         .label("Magnetic Flux Conservation - Magnet II 2 - 1Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc6")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc6")
         .set("Br", new String[][]{{"1.400000*cos(-22.260816[deg])"}, {"1.400000*sin(-22.260816[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc6")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc6")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc6")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc6")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc6").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc6").active(false);
    model.component("nhalbach_system").physics("mfnc").feature("mfc6")
         .label("Magnetic Flux Conservation - Magnet II 2 - 2Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc7")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc7")
         .set("Br", new String[][]{{"1.400000*cos(133.282364[deg])"}, {"1.400000*sin(133.282364[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc7")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc7")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc7")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc7")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc7").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc7")
         .label("Magnetic Flux Conservation - Magnet II 3 - 1Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc8")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc8")
         .set("Br", new String[][]{{"1.400000*cos(-133.282364[deg])"}, {"1.400000*sin(-133.282364[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc8")
         .set("mur", new String[][]{{"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}, {"0"}, {"0"}, {"0"}, {"mu_r_II"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc8")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc8")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc8")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc8").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc8").active(false);
    model.component("nhalbach_system").physics("mfnc").feature("mfc8")
         .label("Magnetic Flux Conservation - Magnet II 3 - 2Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc9")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc9")
         .set("Br", new String[][]{{"1.400000*cos(0.000000[deg])"}, {"1.400000*sin(0.000000[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc9")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc9")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc9")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc9")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc9").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc9")
         .label("Magnetic Flux Conservation - Magnet IV 1 - 1Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc10")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc10")
         .set("Br", new String[][]{{"1.400000*cos(-0.000000[deg])"}, {"1.400000*sin(-0.000000[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc10")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc10")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc10")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc10")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc10").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc10").active(false);
    model.component("nhalbach_system").physics("mfnc").feature("mfc10")
         .label("Magnetic Flux Conservation - Magnet IV 1 - 2Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc11")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc11")
         .set("Br", new String[][]{{"1.400000*cos(27.967105[deg])"}, {"1.400000*sin(27.967105[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc11")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc11")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc11")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc11")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc11").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc11")
         .label("Magnetic Flux Conservation - Magnet IV 2 - 1Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc12")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc12")
         .set("Br", new String[][]{{"1.400000*cos(-27.967105[deg])"}, {"1.400000*sin(-27.967105[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc12")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc12")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc12")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc12")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc12").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc12").active(false);
    model.component("nhalbach_system").physics("mfnc").feature("mfc12")
         .label("Magnetic Flux Conservation - Magnet IV 2 - 2Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc13")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc13")
         .set("Br", new String[][]{{"1.400000*cos(77.728567[deg])"}, {"1.400000*sin(77.728567[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc13")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc13")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc13")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc13")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc13").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc13")
         .label("Magnetic Flux Conservation - Magnet IV 3 - 1Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc14")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc14")
         .set("Br", new String[][]{{"1.400000*cos(-77.728567[deg])"}, {"1.400000*sin(-77.728567[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc14")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc14")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc14")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc14")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc14").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc14").active(false);
    model.component("nhalbach_system").physics("mfnc").feature("mfc14")
         .label("Magnetic Flux Conservation - Magnet IV 3 - 2Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc15")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc15")
         .set("Br", new String[][]{{"1.400000*cos(137.095785[deg])"}, {"1.400000*sin(137.095785[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc15")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc15")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc15")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc15")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc15").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc15")
         .label("Magnetic Flux Conservation - Magnet IV 4 - 1Q");
    model.component("nhalbach_system").physics("mfnc").feature("mfc16")
         .set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.component("nhalbach_system").physics("mfnc").feature("mfc16")
         .set("Br", new String[][]{{"1.400000*cos(-137.095785[deg])"}, {"1.400000*sin(-137.095785[deg])"}, {"0"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc16")
         .set("mur", new String[][]{{"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}, {"0"}, {"0"}, {"0"}, {"mu_r_IV"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc16")
         .set("MsJA", new String[][]{{"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}, {"0"}, {"0"}, {"0"}, {"1e6"}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc16")
         .set("aJA", new int[][]{{200}, {0}, {0}, {0}, {200}, {0}, {0}, {0}, {200}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc16")
         .set("kJA", new int[][]{{300}, {0}, {0}, {0}, {300}, {0}, {0}, {0}, {300}});
    model.component("nhalbach_system").physics("mfnc").feature("mfc16").set("materialType", "from_mat");
    model.component("nhalbach_system").physics("mfnc").feature("mfc16").active(false);
    model.component("nhalbach_system").physics("mfnc").feature("mfc16")
         .label("Magnetic Flux Conservation - Magnet IV 4 - 2Q");

    model.component("nhalbach_system").mesh("mesh1").feature("size").set("hauto", 3);
    model.component("nhalbach_system").mesh("mesh1").feature("size").set("custom", "on");
    model.component("nhalbach_system").mesh("mesh1").feature("size").set("hmax", 0.0222);
    model.component("nhalbach_system").mesh("mesh1").feature("size").set("hmin", 7.5E-5);
    model.component("nhalbach_system").mesh("mesh1").feature("size").set("hnarrow", 5);
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
    model.component("nhalbach_system").probe("prb3").set("expr", "2");
    model.component("nhalbach_system").probe("prb3").set("unit", "m^2");
    model.component("nhalbach_system").probe("prb3").set("descr", "2");
    model.component("nhalbach_system").probe("prb3").set("table", "results_table");
    model.component("nhalbach_system").probe("prb3").set("window", "window1");
    model.component("nhalbach_system").probe("prb4").label("Magnets Area");
    model.component("nhalbach_system").probe("prb4").set("type", "integral");
    model.component("nhalbach_system").probe("prb4").set("expr", "2");
    model.component("nhalbach_system").probe("prb4").set("unit", "m^2");
    model.component("nhalbach_system").probe("prb4").set("descr", "2");
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

    model.component("nhalbach_system").physics("mfnc").feature("mfc3").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc4").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc5").set("mur_mat", "userdef");

    return model;
  }

  public static Model run3(Model model) {
    model.component("nhalbach_system").physics("mfnc").feature("mfc6").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc7").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc8").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc9").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc10").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc11").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc12").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc13").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc14").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc15").set("mur_mat", "userdef");
    model.component("nhalbach_system").physics("mfnc").feature("mfc16").set("mur_mat", "userdef");

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
    model.result().dataset().create("dset3", "Solution");
    model.result().dataset().create("dset4", "Solution");
    model.result().dataset().create("dset5", "Solution");
    model.result().dataset().create("dset6", "Solution");
    model.result().dataset().create("dset7", "Solution");
    model.result().dataset().create("dset8", "Solution");
    model.result().dataset().create("pc1", "ParCurve2D");
    model.result().dataset("dset1").set("probetag", "prb6");
    model.result().dataset("avh1").set("probetag", "prb1");
    model.result().dataset("avh1").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("avh1").selection().set(new int[]{11});
    model.result().dataset("avh2").set("probetag", "prb2");
    model.result().dataset("avh2").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("avh2").selection().set(new int[]{3});
    model.result().dataset("int1").set("probetag", "prb3");
    model.result().dataset("int1").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("int1").selection().set(new int[]{3, 11});
    model.result().dataset("int2").set("probetag", "prb4");
    model.result().dataset("int2").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("int2").selection().set(new int[]{7, 8, 9, 12, 13, 14, 15});
    model.result().dataset("max1").set("probetag", "prb5");
    model.result().dataset("max1").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("max1").selection().set(new int[]{7, 8, 9});
    model.result().dataset("max2").set("probetag", "prb6");
    model.result().dataset("max2").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("max2").selection().set(new int[]{12, 13, 14, 15});
    model.result().dataset("dset3").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("dset3").selection().set(new int[]{3, 11});
    model.result().dataset("dset4").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("dset4").selection().set(new int[]{7, 8, 9, 12, 13, 14, 15});
    model.result().dataset("dset5").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("dset5").selection().set(new int[]{7, 8, 9});
    model.result().dataset("dset6").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("dset6").selection().set(new int[]{12, 13, 14, 15});
    model.result().dataset("dset7").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("dset7").selection().set(new int[]{11});
    model.result().dataset("dset8").selection().geom("nhalbach_geometry", 2);
    model.result().dataset("dset8").selection().set(new int[]{3});
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
    model.result().create("pg2", "PlotGroup2D");
    model.result().create("pg3", "PlotGroup2D");
    model.result().create("pg4", "PlotGroup1D");
    model.result("pg1").set("probetag", "window1_default");
    model.result("pg1").create("tblp1", "Table");
    model.result("pg1").feature("tblp1").set("probetag", "prb1,prb2,prb3,prb4,prb5,prb6");
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

    model.sol("sol1").attach("std1");
    model.sol("sol1").feature("s1").feature("dDef").set("ooc", false);
    model.sol("sol1").feature("s1").feature("i1").set("linsolver", "bicgstab");
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
    model.result().numerical("pev6").set("unit", new String[]{"A/m"});
    model.result("pg1").label("Probe Plot Group 1");
    model.result("pg1").set("solrepresentation", "solnum");
    model.result("pg1").set("xlabel", "mfnc.normB (T), Maximum Field");
    model.result("pg1").set("windowtitle", "Probe Plot 1");
    model.result("pg1").set("xlabelactive", false);
    model.result("pg1").feature("tblp1").label("Probe Table Graph 1");
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
    model.result("pg3").feature("arws1").set("scale", 0.007092635900585284);
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
    model.result("pg3").feature("arws2").set("scale", 0.003794983702596596);
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
    model.result("pg4").feature("lngr1").set("resolution", "normal");
    model.result().export("export1").set("data", "dset7");
    model.result().export("export1").set("expr", new String[]{"mfnc.normB"});
    model.result().export("export1").set("unit", new String[]{"T"});
    model.result().export("export1").set("descr", new String[]{"Magnetic flux density norm"});
    model.result().export("export1").set("filename", "B_high.txt");
    model.result().export("export1").set("allowmaterialsmoothing", true);
    model.result().export("export2").set("data", "dset8");
    model.result().export("export2").set("expr", new String[]{"mfnc.normB"});
    model.result().export("export2").set("unit", new String[]{"T"});
    model.result().export("export2").set("descr", new String[]{"Magnetic flux density norm"});
    model.result().export("export2").set("filename", "B_low.txt");
    model.result().export("export2").set("allowmaterialsmoothing", true);
    model.result().export("export4").set("data", "dset5");
    model.result().export("export4")
         .set("expr", new String[]{"mfnc.Bx", "mfnc.By", "mfnc.Hx", "mfnc.Hy", "mfnc.Brx", "mfnc.Bry"});
    model.result().export("export4").set("unit", new String[]{"T", "T", "A/m", "A/m", "T", "T"});
    model.result().export("export4")
         .set("descr", new String[]{"Magnetic flux density, x component", "Magnetic flux density, y component", "Magnetic field, x component", "Magnetic field, y component", "Remanent flux density, x component", "Remanent flux density, y component"});
    model.result().export("export4").set("filename", "H_II_1Q.txt");
    model.result().export("export4").set("allowmaterialsmoothing", true);
    model.result().export("export5").set("data", "dset6");
    model.result().export("export5")
         .set("expr", new String[]{"mfnc.Bx", "mfnc.By", "mfnc.Hx", "mfnc.Hy", "mfnc.Brx", "mfnc.Bry"});
    model.result().export("export5").set("unit", new String[]{"T", "T", "A/m", "A/m", "T", "T"});
    model.result().export("export5")
         .set("descr", new String[]{"Magnetic flux density, x component", "Magnetic flux density, y component", "Magnetic field, x component", "Magnetic field, y component", "Remanent flux density, x component", "Remanent flux density, y component"});
    model.result().export("export5").set("filename", "H_IV_1Q.txt");
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
    model = run2(model);
    run3(model);
  }

}
