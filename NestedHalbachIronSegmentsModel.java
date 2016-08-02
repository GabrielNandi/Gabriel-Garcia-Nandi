/*
 * NestedHalbachIronSegmentsModel.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;

/** Model exported on Aug 2 2016, 16:15 by COMSOL 5.2.1.152. */
public class NestedHalbachIronSegmentsModel {

  public static Model run() {
    Model model = ModelUtil.create("Model");

    model
         .modelPath("C:\\Users\\fabiofortkamp\\Google Drive\\PoloMag\\AMR and Magnet Design\\COMSOL\\nested-halbach-fabio");

    model.comments("Untitled\n\n");

    model.modelNode().create("comp1");

    model.geom().create("geom1", 2);

    model.mesh().create("mesh1", "geom1");

    model.physics().create("mfnc", "MagnetostaticsNoCurrents", "geom1");

    model.study().create("std1");
    model.study("std1").create("stat", "Stationary");
    model.study("std1").feature("stat").activate("mfnc", true);

    model.material().create("mat1", "Common", "comp1");
    model.material("mat1").label("Air");
    model.material("mat1").set("family", "air");
    model.material("mat1").propertyGroup("def").set("relpermeability", "1");
    model.material("mat1").propertyGroup("def").set("relpermittivity", "1");
    model.material("mat1").propertyGroup("def").set("dynamicviscosity", "eta(T[1/K])[Pa*s]");
    model.material("mat1").propertyGroup("def").set("ratioofspecificheat", "1.4");
    model.material("mat1").propertyGroup("def").set("electricconductivity", "0[S/m]");
    model.material("mat1").propertyGroup("def").set("heatcapacity", "Cp(T[1/K])[J/(kg*K)]");
    model.material("mat1").propertyGroup("def").set("density", "rho(pA[1/Pa],T[1/K])[kg/m^3]");
    model.material("mat1").propertyGroup("def").set("thermalconductivity", "k(T[1/K])[W/(m*K)]");
    model.material("mat1").propertyGroup("def").set("soundspeed", "cs(T[1/K])[m/s]");
    model.material("mat1").propertyGroup("def").func().create("eta", "Piecewise");
    model.material("mat1").propertyGroup("def").func("eta").set("funcname", "eta");
    model.material("mat1").propertyGroup("def").func("eta").set("arg", "T");
    model.material("mat1").propertyGroup("def").func("eta").set("extrap", "constant");
    model.material("mat1").propertyGroup("def").func("eta")
         .set("pieces", new String[][]{{"200.0", "1600.0", "-8.38278E-7+8.35717342E-8*T^1-7.69429583E-11*T^2+4.6437266E-14*T^3-1.06585607E-17*T^4"}});
    model.material("mat1").propertyGroup("def").func().create("Cp", "Piecewise");
    model.material("mat1").propertyGroup("def").func("Cp").set("funcname", "Cp");
    model.material("mat1").propertyGroup("def").func("Cp").set("arg", "T");
    model.material("mat1").propertyGroup("def").func("Cp").set("extrap", "constant");
    model.material("mat1").propertyGroup("def").func("Cp")
         .set("pieces", new String[][]{{"200.0", "1600.0", "1047.63657-0.372589265*T^1+9.45304214E-4*T^2-6.02409443E-7*T^3+1.2858961E-10*T^4"}});
    model.material("mat1").propertyGroup("def").func().create("rho", "Analytic");
    model.material("mat1").propertyGroup("def").func("rho").set("funcname", "rho");
    model.material("mat1").propertyGroup("def").func("rho").set("args", new String[]{"pA", "T"});
    model.material("mat1").propertyGroup("def").func("rho").set("expr", "pA*0.02897/8.314/T");
    model.material("mat1").propertyGroup("def").func("rho").set("dermethod", "manual");
    model.material("mat1").propertyGroup("def").func("rho")
         .set("argders", new String[][]{{"pA", "d(pA*0.02897/8.314/T,pA)"}, {"T", "d(pA*0.02897/8.314/T,T)"}});
    model.material("mat1").propertyGroup("def").func().create("k", "Piecewise");
    model.material("mat1").propertyGroup("def").func("k").set("funcname", "k");
    model.material("mat1").propertyGroup("def").func("k").set("arg", "T");
    model.material("mat1").propertyGroup("def").func("k").set("extrap", "constant");
    model.material("mat1").propertyGroup("def").func("k")
         .set("pieces", new String[][]{{"200.0", "1600.0", "-0.00227583562+1.15480022E-4*T^1-7.90252856E-8*T^2+4.11702505E-11*T^3-7.43864331E-15*T^4"}});
    model.material("mat1").propertyGroup("def").func().create("cs", "Analytic");
    model.material("mat1").propertyGroup("def").func("cs").set("funcname", "cs");
    model.material("mat1").propertyGroup("def").func("cs").set("args", new String[]{"T"});
    model.material("mat1").propertyGroup("def").func("cs").set("expr", "sqrt(1.4*287*T)");
    model.material("mat1").propertyGroup("def").func("cs").set("dermethod", "manual");
    model.material("mat1").propertyGroup("def").func("cs")
         .set("argders", new String[][]{{"T", "d(sqrt(1.4*287*T),T)"}});
    model.material("mat1").propertyGroup("def").addInput("temperature");
    model.material("mat1").propertyGroup("def").addInput("pressure");
    model.material("mat1").propertyGroup().create("RefractiveIndex", "Refractive index");
    model.material("mat1").propertyGroup("RefractiveIndex").set("n", "1");
    model.material("mat1").set("family", "air");

    model.geom("geom1").run();
    model.geom().create("part1", "Part", 2);
    model.geom("part1").create("pol1", "Polygon");
    model.geom("part1").feature("pol1").label("Cylinder Block");
    model.geom("part1").label("Cylinder Block");
    model.geom("part1").feature("pol1").label("Cylinder Block Edges");
    model.geom("part1").feature("pol1").set("x", "r1*cos(phi1),r2*cos(phi1)");
    model.geom("part1").feature("pol1").set("y", "r1*cos(phi1),r2*cos(phi1)");
    model.geom("part1").run("");
    model.geom().remove("part1");

    model.param().set("R_i", "15[mm]");
    model.param().set("R_o", "80.49[mm]");
    model.param().set("h_gap", "20[mm]");
    model.param().set("R_s", "140[mm]");
    model.param().set("h_fc", "50[mm]");
    model.param().set("R_e", "2[m]");

    model.variable().create("var1");
    model.variable("var1").set("R_g", "R_o+h_gap");

    model.geom().create("part1", "Part", 2);
    model.geom("part1").label("Cylinder Block");
    model.geom("part1").inputParam().set("r1", "R_i");
    model.geom("part1").inputParam().set("r2", "R_o");
    model.geom("part1").inputParam().set("phi1", "0[deg]");
    model.geom("part1").inputParam().set("phi2", "45[deg]");
    model.geom("part1").create("pol1", "Polygon");
    model.geom("part1").feature("pol1").set("x", "r1*cos(phi1),r2*cos(phi1)");
    model.geom("part1").feature("pol1").set("y", "r1");
    model.geom("part1").feature("pol1").set("x", "r1*cos(phi1),r2*cos(phi1),r2*cos(phi2),r1*cos(phi1)");
    model.geom("part1").feature("pol1").set("y", "r1*sin(phi1),r2*sin(phi1),r2*sin(phi2),r1*sin(phi1)");
    model.geom("geom1").create("pi1", "PartInstance");
    model.geom("geom1").feature("pi1").set("selkeepnoncontr", "off");
    model.geom("geom1").feature("pi1").set("part", "part1");
    model.geom("geom1").run("pi1");
    model.geom("part1").feature("pol1").label("Cylinder Block Edges");
    model.geom("part1").feature("pol1").set("y", "r1*sin(phi1),r2*sin(phi1),r2*sin(phi2),r1*sin(phi2)");
    model.geom("part1").feature("pol1").set("x", "r1*cos(phi1),r2*cos(phi1),r2*cos(phi2),r1*cos(phi2)");
    model.geom("geom1").run("pi1");
    model.geom("geom1").feature("pi1").setEntry("inputexpr", "phi2", "15[deg]");
    model.geom("geom1").run("pi1");
    model.geom("geom1").feature().duplicate("pi2", "pi1");
    model.geom("geom1").feature("pi2").setEntry("inputexpr", "phi1", "15[deg]");
    model.geom("geom1").feature("pi2").setEntry("inputexpr", "phi2", "30[deg]");
    model.geom("geom1").run("pi2");
    model.geom("geom1").run("pi2");
    model.geom("geom1").create("if1", "If");
    model.geom("geom1").feature().createAfter("endif1", "EndIf", "if1");
    model.geom("geom1").feature().remove("if1");
    model.geom("geom1").feature().remove("endif1");
    model.geom("geom1").feature().duplicate("pi3", "pi2");
    model.geom("geom1").feature("pi3").setEntry("inputexpr", "phi1", "30[deg]");
    model.geom("geom1").feature("pi3").setEntry("inputexpr", "phi2", "45[deg]");
    model.geom("geom1").run("pi3");
    model.geom("geom1").feature().duplicate("pi4", "pi3");
    model.geom("geom1").feature("pi4").setEntry("inputexpr", "r1", "R_g");
    model.geom("geom1").feature("pi4").setEntry("inputexpr", "phi1", "0[deg]");
    model.geom("geom1").feature("pi4").setEntry("inputexpr", "phi2", "15[deg]");
    model.geom("geom1").run("pi3");

    model.variable().create("var2");
    model.variable("var2").model("comp1");
    model.variable().remove("var1");
    model.variable("var2").set("R_g", "R_o+h_gap");

    model.geom("geom1").feature("pi4").setEntry("inputexpr", "r1", "R_o+h_gap");
    model.geom("geom1").run();

    model.variable().remove("var2");

    model.geom("geom1").feature("pi4").setEntry("inputexpr", "r2", "R_s");
    model.geom("geom1").run("pi4");
    model.geom("geom1").feature().duplicate("pi5", "pi4");
    model.geom("geom1").feature("pi5").setEntry("inputexpr", "phi1", "15[deg]");
    model.geom("geom1").feature("pi5").setEntry("inputexpr", "phi2", "30[deg]");
    model.geom("geom1").run("pi5");
    model.geom("geom1").feature().duplicate("pi6", "pi5");
    model.geom("geom1").feature("pi6").setEntry("inputexpr", "phi1", "30[deg]");
    model.geom("geom1").feature("pi6").setEntry("inputexpr", "phi2", "45[deg]");
    model.geom("geom1").run("pi6");

    model.label("nested-halbach-2d-blocks-iron.mph");

    model.param().set("n_II", "3");
    model.param().set("n_IV", "3");
    model.param().descr("n_II", "Number of segments in magnet II");
    model.param().descr("n_IV", "Number of segments in magnet IV");
    model.param().set("R_g", "R_o+h_gap");

    model.geom("geom1").feature("pi1").label("Cylinder Block 1 - Magnet II - 1Q");

    model.param().set("phi_S", "45[deg]");
    model.param().rename("phi_S", "phi_S_II");
    model.param().set("phi_S_IV", "45[deg]");

    model.geom("geom1").feature("pi1").setEntry("inputexpr", "phi2", "(90[deg]-phi_S_II)/n_II");
    model.geom("geom1").run("pi1");
    model.geom("geom1").feature("pi2").label("Cylinder Block 2 - Magnet II - 1Q");

    model.param().set("delta_phi_B", "(90[deg]-phi_S_II)/n_II");
    model.param().rename("delta_phi_B", "delta_phi_B_II");
    model.param().set("delta_phi_B_IV", "(90[deg]-phi_S_IV)/n_IV");

    model.geom("geom1").feature("pi1").setEntry("inputexpr", "phi2", "1*delta_phi");
    model.geom("geom1").feature("pi1").setEntry("inputexpr", "phi2", "1*delta_phi_B_II");
    model.geom("geom1").feature("pi2").setEntry("inputexpr", "phi1", "1*delta_phi_B_I");
    model.geom("geom1").feature("pi2").setEntry("inputexpr", "phi1", "1*delta_phi_B_II");
    model.geom("geom1").feature("pi2").setEntry("inputexpr", "phi2", "2*delta_phi_B_II");
    model.geom("geom1").feature("pi3").label("Cylinder Block 3 - Magnet II - 1Q");
    model.geom("geom1").feature("pi3").setEntry("inputexpr", "phi1", "2*delta_phi_B_II");
    model.geom("geom1").feature("pi3").setEntry("inputexpr", "phi2", "3*delta_phi_B_II");
    model.geom("geom1").run("pi3");
    model.geom("geom1").run("pi3");
    model.geom("geom1").feature("pi4").label("Cylinder Block 1 - Magnet IV - 1Q");
    model.geom("geom1").feature("pi4").setEntry("inputexpr", "phi2", "1*delta_phi_B_IV");
    model.geom("geom1").feature("pi5").label("Cylinder Block 2 - Magnet IV - 1Q");
    model.geom("geom1").feature("pi5").setEntry("inputexpr", "phi1", "1*delta_phi_B_IV");
    model.geom("geom1").feature("pi5").setEntry("inputexpr", "phi2", "2*delta_phi_B_IV");
    model.geom("geom1").feature("pi6").label("Cylinder Block 3 - Magnet IV - 1Q");
    model.geom("geom1").feature("pi6").setEntry("inputexpr", "phi1", "2*delta_phi_B_IV");
    model.geom("geom1").feature("pi6").setEntry("inputexpr", "phi2", "3*delta_phi_B_IV");
    model.geom("geom1").runPre("fin");
    model.geom("geom1").feature().duplicate("pi7", "pi3");
    model.geom("geom1").feature().move("pi7", 3);
    model.geom("geom1").feature("pi7").label("Cylinder Block 3 - Iron II - 1Q");

    model.param().set("delta_phi_B_II", "phi_S_II/n_II");
    model.param().set("delta_phi_B_IV", "phi_S_IV/n_IV");
    model.param().set("delta_phi_S_II", "(90[deg]-phi_S_II)/n_II");
    model.param().set("delta_phi_S_IV", "(90[deg]-phi_S_IV)/n_IV");

    model.geom("geom1").feature("pi7").setEntry("inputexpr", "phi1", "3*delta_phi_B_II");
    model.geom("geom1").feature("pi7").setEntry("inputexpr", "phi2", "3*delta_phi_B_II+1*delta_phi_S_II");
    model.geom("geom1").run("pi7");
    model.geom("geom1").feature().duplicate("pi8", "pi7");
    model.geom("geom1").feature().duplicate("pi9", "pi8");
    model.geom("geom1").feature("pi7").label("Cylinder Block 1 - Iron II - 1Q");
    model.geom("geom1").feature("pi8").label("Cylinder Block 2 - Iron II - 1Q 1");
    model.geom("geom1").feature("pi8").setEntry("inputexpr", "phi2", "3*delta_phi_B_II+2*delta_phi_S_II");
    model.geom("geom1").run("pi8");
    model.geom("geom1").feature("pi8")
         .setEntry("inputexpr", "phi1", "3*delta_phi_B_II+3*delta_phi_B_II+1*delta_phi_S_II");
    model.geom("geom1").run("pi8");
    model.geom("geom1").feature("pi8")
         .setEntry("inputexpr", "phi1", "3*delta_phi_B_II+1*delta_phi_B_II+1*delta_phi_S_II");
    model.geom("geom1").run("pi8");
    model.geom("geom1").feature("pi8")
         .setEntry("inputexpr", "phi1", "3*delta_phi_B_II+1*delta_phi_S_II+1*delta_phi_S_II");
    model.geom("geom1").run("pi8");
    model.geom("geom1").feature("pi8").setEntry("inputexpr", "phi1", "3*delta_phi_B_II+1*delta_phi_S_II");
    model.geom("geom1").run("pi8");
    model.geom("geom1").feature("pi9").label("Cylinder Block 3 - Iron II - 1Q");
    model.geom("geom1").feature("pi9").setEntry("inputexpr", "phi1", "3*delta_phi_B_II+2*delta_phi_S_II");
    model.geom("geom1").feature("pi9").setEntry("inputexpr", "phi2", "3*delta_phi_B_II+3*delta_phi_S_II");
    model.geom("geom1").feature("pi8").label("Cylinder Block 2 - Iron II - 1Q");
    model.geom("geom1").run("pi8");
    model.geom("geom1").run("pi9");
    model.geom("geom1").feature().duplicate("pi10", "pi7");
    model.geom("geom1").feature().move("pi10", 9);
    model.geom("geom1").feature("pi10").label("Cylinder Block 1 - Iron IV - 1Q 1");
    model.geom("geom1").feature("pi10").setEntry("inputexpr", "phi1", "3*delta_phi_B_IV");
    model.geom("geom1").feature("pi10").setEntry("inputexpr", "phi2", "3*delta_phi_B_IV+1*delta_phi_S_IV");
    model.geom("geom1").run("pi10");
    model.geom("geom1").run("pi10");
    model.geom("geom1").run("pi10");
    model.geom("geom1").run("pi6");
    model.geom("geom1").run("pi4");
    model.geom("geom1").run("pi5");
    model.geom("geom1").run("pi6");
    model.geom("geom1").run("pi10");
    model.geom("geom1").run("fin");
    model.geom("geom1").feature("pi4").setEntry("inputexpr", "r1", "R_g");
    model.geom("geom1").run("pi4");
    model.geom("geom1").feature("pi5").setEntry("inputexpr", "r1", "R_g");
    model.geom("geom1").run("pi5");
    model.geom("geom1").feature("pi6").setEntry("inputexpr", "r2", "R_g");
    model.geom("geom1").run("pi6");
    model.geom("geom1").feature("pi6").setEntry("inputexpr", "r1", "R_g");
    model.geom("geom1").feature("pi6").setEntry("inputexpr", "r2", "R_s");
    model.geom("geom1").run("pi6");
    model.geom("geom1").feature("pi10").setEntry("inputexpr", "r1", "R_g");
    model.geom("geom1").feature("pi10").setEntry("inputexpr", "r2", "R_s");
    model.geom("geom1").run("pi10");
    model.geom("geom1").feature().duplicate("pi11", "pi10");
    model.geom("geom1").feature("pi11").label("Cylinder Block 2 - Iron IV - 1Q 1.1");
    model.geom("geom1").feature("pi11").setEntry("inputexpr", "phi2", "3*delta_phi_B_IV+2*delta_phi_S_IV");
    model.geom("geom1").feature("pi11").setEntry("inputexpr", "phi1", "3*delta_phi_B_IV+1*delta_phi_S_IV");
    model.geom("geom1").run("pi11");
    model.geom("geom1").feature("pi11").label("Cylinder Block 2 - Iron IV - 1Q");
    model.geom("geom1").feature("pi10").label("Cylinder Block 1 - Iron IV - 1Q");
    model.geom("geom1").feature().duplicate("pi12", "pi11");
    model.geom("geom1").feature("pi12").label("Cylinder Block 3 - Iron IV - 1Q");
    model.geom("geom1").feature("pi12").setEntry("inputexpr", "phi1", "3*delta_phi_B_IV+2*delta_phi_S_IV");
    model.geom("geom1").feature("pi12").setEntry("inputexpr", "phi2", "3*delta_phi_B_IV+3*delta_phi_S_IV");
    model.geom("geom1").run("pi12");
    model.geom("geom1").run("pi12");
    model.geom("geom1").create("mir1", "Mirror");
    model.geom("geom1").feature("mir1").selection("input")
         .set(new String[]{"pi1", "pi10", "pi11", "pi12", "pi2", "pi3", "pi4", "pi5", "pi6", "pi7", 
         "pi8", "pi9"});
    model.geom("geom1").feature("mir1").set("axis", new String[]{"0", "1"});
    model.geom("geom1").feature("mir1").set("keep", "on");
    model.geom("geom1").run("mir1");
    model.geom("geom1").feature("mir1").set("axis", new String[]{"1", "0"});
    model.geom("geom1").run("mir1");

    model.param().set("phi_S_II", "30[deg]");

    model.geom("geom1").run("fin");

    model.param().set("phi_S_II", "45[deg]");

    model.geom("geom1").run("fin");
    model.geom("geom1").run("mir1");
    model.geom("geom1").create("c1", "Circle");
    model.geom("geom1").feature("c1").set("r", "R_");
    model.geom().create("part2", "Part", 2);
    model.geom("part2").label("Cylinder shell");
    model.geom("part2").inputParam().set("r1", "R_i");
    model.geom("part2").inputParam().set("r2", "R_o");
    model.geom("part2").inputParam().set("delta_phi", "180[deg]");
    model.geom("part2").create("c1", "Circle");
    model.geom("part2").feature("c1").set("r", "r2");
    model.geom("part2").feature("c1").set("angle", "delta_phi");
    model.geom("part2").run("c1");
    model.geom("part2").create("c2", "Circle");
    model.geom("part2").feature("c2").set("r", "r1");
    model.geom("part2").feature("c2").set("angle", "delta_phi");
    model.geom("part2").run("c2");
    model.geom("part2").create("dif1", "Difference");
    model.geom("part2").feature("dif1").selection("input2").set(new String[]{});
    model.geom("part2").feature("dif1").selection("input").set(new String[]{});

    model.label("nested-halbach-2d-blocks-iron.mph");

    model.geom("part2").run("c2");
    model.geom("part2").feature("dif1").selection("input").set(new String[]{"c1"});
    model.geom("part2").feature("dif1").selection("input2").set(new String[]{"c2"});
    model.geom("part2").run("dif1");
    model.geom("part2").label("Cylinder Shell");
    model.geom("part2").run("dif1");
    model.geom("geom1").feature().remove("c1");
    model.geom("geom1").run("mir1");
    model.geom("geom1").create("pi13", "PartInstance");
    model.geom("geom1").feature("pi13").set("selkeepnoncontr", "off");
    model.geom("geom1").feature("pi13").set("part", "part2");
    model.geom("geom1").run("pi13");
    model.geom("geom1").feature("pi13").setEntry("inputexpr", "r1", "0");
    model.geom("geom1").feature("pi13").setEntry("inputexpr", "r2", "R_i");
    model.geom("geom1").run("mir1");
    model.geom("geom1").run("mir1");
    model.geom("geom1").create("c1", "Circle");
    model.geom("geom1").feature("c1").set("r", "R_i");
    model.geom("geom1").feature("c1").set("angle", "180");
    model.geom("geom1").run("c1");
    model.geom("geom1").feature().remove("pi13");
    model.geom("geom1").run("c1");
    model.geom("geom1").create("copy1", "Copy");
    model.geom("geom1").feature("copy1").selection("input")
         .set(new String[]{"mir1(1)", "mir1(10)", "mir1(11)", "mir1(12)", "mir1(5)", "mir1(6)", "pi1", "pi2", "pi3", "pi7", 
         "pi8", "pi9"});
    model.geom("geom1").run("copy1");
    model.geom("geom1").run("copy1");
    model.geom("geom1").create("dif1", "Difference");
    model.geom("geom1").feature("dif1").selection("input").set(new String[]{"copy1"});
    model.geom("geom1").feature("dif1").selection("input2").set(new String[]{"copy1"});
    model.geom("geom1").feature("dif1").selection("input").set(new String[]{"c1"});
    model.geom("geom1").run("dif1");
    model.geom("geom1").run("dif1");
    model.geom("geom1").create("c2", "Circle");
    model.geom("geom1").feature("c2").set("angle", "R_g");
    model.geom("geom1").feature("c2").set("r", "R_g");
    model.geom("geom1").feature("c2").set("angle", "180");
    model.geom("geom1").run("c2");
    model.geom("geom1").run("c2");
    model.geom("geom1").create("copy2", "Copy");
    model.geom("geom1").feature("copy2").selection("input")
         .set(new String[]{"mir1", "pi1", "pi10", "pi11", "pi12", "pi2", "pi3", "pi4", "pi5", "pi6", 
         "pi7", "pi8", "pi9"});
    model.geom("geom1").run("copy2");
    model.geom("geom1").run("copy2");
    model.geom("geom1").create("dif2", "Difference");
    model.geom("geom1").feature("dif2").selection("input").set(new String[]{"c2"});
    model.geom("geom1").feature("dif2").selection("input2").set(new String[]{"copy2"});
    model.geom("geom1").run("dif2");
    model.geom("geom1").run();

    model.material("mat1").selection().set(new int[]{});

    model.selection().create("sel1", "Explicit");
    model.selection("sel1").label("Shaft");
    model.selection("sel1").set(new int[]{14});
    model.selection().create("sel2", "Explicit");
    model.selection("sel2").label("Air gap");
    model.selection("sel2").set(new int[]{4});

    model.material("mat1").selection().named("sel1");
    model.material("mat1").selection().named("sel2");
    model.material("mat1").selection().set(new int[]{4, 14});
    model.material().create("mat2", "Common", "comp1");
    model.material("mat2").label("Soft Iron (with losses)");
    model.material("mat2").set("family", "iron");
    model.material("mat2").propertyGroup("def").set("electricconductivity", "1.12e7[S/m]");
    model.material("mat2").propertyGroup("def").set("relpermittivity", "1");
    model.material("mat2").propertyGroup().create("BHCurve", "BH curve");
    model.material("mat2").propertyGroup("BHCurve").set("normB", "BH(normH[m/A])[T]");
    model.material("mat2").propertyGroup("BHCurve").func().create("BH", "Interpolation");
    model.material("mat2").propertyGroup("BHCurve").func("BH").set("sourcetype", "user");
    model.material("mat2").propertyGroup("BHCurve").func("BH").set("source", "table");
    model.material("mat2").propertyGroup("BHCurve").func("BH").set("funcname", "BH");
    model.material("mat2").propertyGroup("BHCurve").func("BH")
         .set("table", new String[][]{{"0", "0"}, 
         {"663.146", "1"}, 
         {"1067.5", "1.1"}, 
         {"1705.23", "1.2"}, 
         {"2463.11", "1.3"}, 
         {"3841.67", "1.4"}, 
         {"5425.74", "1.5"}, 
         {"7957.75", "1.6"}, 
         {"12298.3", "1.7"}, 
         {"20462.8", "1.8"}, 
         {"32169.6", "1.9"}, 
         {"61213.4", "2.0"}, 
         {"111408", "2.1"}, 
         {"175070", "2.2"}, 
         {"261469", "2.3"}, 
         {"318310", "2.4"}});
    model.material("mat2").propertyGroup("BHCurve").func("BH").set("interp", "linear");
    model.material("mat2").propertyGroup("BHCurve").func("BH").set("extrap", "linear");
    model.material("mat2").propertyGroup().create("HBCurve", "HB curve");
    model.material("mat2").propertyGroup("HBCurve").set("normH", "HB(normB[1/T])[A/m]");
    model.material("mat2").propertyGroup("HBCurve").func().create("HB", "Interpolation");
    model.material("mat2").propertyGroup("HBCurve").func("HB").set("sourcetype", "user");
    model.material("mat2").propertyGroup("HBCurve").func("HB").set("source", "table");
    model.material("mat2").propertyGroup("HBCurve").func("HB").set("funcname", "HB");
    model.material("mat2").propertyGroup("HBCurve").func("HB")
         .set("table", new String[][]{{"0", "0"}, 
         {"1", "663.146"}, 
         {"1.1", "1067.5"}, 
         {"1.2", "1705.23"}, 
         {"1.3", "2463.11"}, 
         {"1.4", "3841.67"}, 
         {"1.5", "5425.74"}, 
         {"1.6", "7957.75"}, 
         {"1.7", "12298.3"}, 
         {"1.8", "20462.8"}, 
         {"1.9", "32169.6"}, 
         {"2.0", "61213.4"}, 
         {"2.1", "111408"}, 
         {"2.2", "175070"}, 
         {"2.3", "261469"}, 
         {"2.4", "318310"}});
    model.material("mat2").propertyGroup("HBCurve").func("HB").set("interp", "linear");
    model.material("mat2").propertyGroup("HBCurve").func("HB").set("extrap", "linear");
    model.material("mat2").propertyGroup().create("EffectiveBHCurve", "Effective BH curve");
    model.material("mat2").propertyGroup("EffectiveBHCurve").set("normBeff", "BHeff(normHeff[m/A])[T]");
    model.material("mat2").propertyGroup("EffectiveBHCurve").func().create("BHeff", "Interpolation");
    model.material("mat2").propertyGroup("EffectiveBHCurve").func("BHeff").set("sourcetype", "user");
    model.material("mat2").propertyGroup("EffectiveBHCurve").func("BHeff").set("source", "table");
    model.material("mat2").propertyGroup("EffectiveBHCurve").func("BHeff").set("funcname", "BHeff");
    model.material("mat2").propertyGroup("EffectiveBHCurve").func("BHeff")
         .set("table", new String[][]{{"0", "0"}, 
         {"663.146", "1"}, 
         {"1067.5", "1.4943906486860214"}, 
         {"1705.23", "1.941630073817125"}, 
         {"2463.11", "2.257619494050335"}, 
         {"3841.67", "2.729755059668001"}, 
         {"5425.74", "2.8756651489647296"}, 
         {"7957.75", "3.149029234016385"}, 
         {"12298.3", "3.4529372126833744"}, 
         {"20462.8", "3.7845615956017395"}, 
         {"32169.6", "4.060195910283011"}, 
         {"61213.4", "4.421777266072753"}, 
         {"111408", "4.721954005107204"}, 
         {"175070", "4.941198649690261"}, 
         {"261469", "5.1446599438425515"}, 
         {"318310", "5.253346039640234"}});
    model.material("mat2").propertyGroup("EffectiveBHCurve").func("BHeff").set("interp", "linear");
    model.material("mat2").propertyGroup("EffectiveBHCurve").func("BHeff").set("extrap", "linear");
    model.material("mat2").propertyGroup().create("EffectiveHBCurve", "Effective HB curve");
    model.material("mat2").propertyGroup("EffectiveHBCurve").set("normHeff", "HBeff(normBeff[1/T])[A/m]");
    model.material("mat2").propertyGroup("EffectiveHBCurve").func().create("HBeff", "Interpolation");
    model.material("mat2").propertyGroup("EffectiveHBCurve").func("HBeff").set("sourcetype", "user");
    model.material("mat2").propertyGroup("EffectiveHBCurve").func("HBeff").set("source", "table");
    model.material("mat2").propertyGroup("EffectiveHBCurve").func("HBeff").set("funcname", "HBeff");
    model.material("mat2").propertyGroup("EffectiveHBCurve").func("HBeff")
         .set("table", new String[][]{{"0", "0"}, 
         {"1", "663.146"}, 
         {"1.4943906486860214", "1067.5"}, 
         {"1.941630073817125", "1705.23"}, 
         {"2.257619494050335", "2463.11"}, 
         {"2.729755059668001", "3841.67"}, 
         {"2.8756651489647296", "5425.74"}, 
         {"3.149029234016385", "7957.75"}, 
         {"3.4529372126833744", "12298.3"}, 
         {"3.7845615956017395", "20462.8"}, 
         {"4.060195910283011", "32169.6"}, 
         {"4.421777266072753", "61213.4"}, 
         {"4.721954005107204", "111408"}, 
         {"4.941198649690261", "175070"}, 
         {"5.1446599438425515", "261469"}, 
         {"5.253346039640234", "318310"}});
    model.material("mat2").propertyGroup("EffectiveHBCurve").func("HBeff").set("interp", "linear");
    model.material("mat2").propertyGroup("EffectiveHBCurve").func("HBeff").set("extrap", "linear");
    model.material("mat2").set("family", "iron");
    model.material("mat2").selection().set(new int[]{5, 8, 10, 11, 12, 13, 15, 16, 17, 18, 22, 23});

    model.selection().create("sel3", "Explicit");
    model.selection("sel3").label("Iron regions");
    model.selection("sel3").set(new int[]{5, 8, 10, 11, 12, 13, 15, 16, 17, 18, 22, 23});

    model.physics("mfnc").create("mfc2", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc2").selection().named("sel3");
    model.physics("mfnc").feature("mfc2").set("ConstitutiveRelationH", "BHCurve");
    model.physics("mfnc").feature("mfc1").label("Magnetic Flux Conservation - Air regions");
    model.physics("mfnc").feature("mfc2").label("Magnetic Flux Conservation - Iron regions");

    model.material().create("mat3", "Common", "comp1");

    model.selection().create("sel4", "Explicit");
    model.selection("sel4").label("Magnet regions");
    model.selection("sel4").set(new int[]{1, 2, 3, 6, 7, 9, 19, 20, 21, 24, 25, 26});

    model.material("mat3").label("Nd-Fe-B");
    model.material("mat3").selection().named("sel4");
    model.material("mat3").propertyGroup("def").set("relpermeability", new String[]{"1.05"});

    model.physics("mfnc").create("mfc3", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc3").label("Magnetic Flux Conservation - Magnet II 1");
    model.physics("mfnc").feature("mfc3").selection().set(new int[]{21});
    model.physics("mfnc").feature("mfc3").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").create("mfc4", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc4").selection().set(new int[]{20});
    model.physics("mfnc").feature("mfc4").label("Magnetic Flux Conservation - Magnet II 2 - 1Q");
    model.physics("mfnc").feature("mfc3").label("Magnetic Flux Conservation - Magnet II 1 - 1Q");
    model.physics("mfnc").feature().duplicate("mfc5", "mfc4");
    model.physics("mfnc").feature("mfc5").selection().set(new int[]{19});
    model.physics("mfnc").feature("mfc5").label("Magnetic Flux Conservation - Magnet II 3 - 1Q");
    model.physics("mfnc").feature("mfc5").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc4").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc4").setIndex("materialType", "solid", 0);
    model.physics("mfnc").feature("mfc3").setIndex("materialType", "solid", 0);
    model.physics("mfnc").feature("mfc3").setIndex("materialType", "from_mat", 0);
    model.physics("mfnc").feature("mfc4").setIndex("materialType", "from_mat", 0);
    model.physics("mfnc").feature("mfc5").setIndex("materialType", "from_mat", 0);
    model.physics("mfnc").feature("mfc2").setIndex("materialType", "from_mat", 0);
    model.physics("mfnc").feature().duplicate("mfc6", "mfc5");
    model.physics("mfnc").feature("mfc6").label("Magnetic Flux Conservation - Magnet II 1 - 2Q");
    model.physics("mfnc").feature("mfc6").selection().set(new int[]{6});
    model.physics("mfnc").feature().duplicate("mfc7", "mfc6");
    model.physics("mfnc").feature("mfc7").label("Magnetic Flux Conservation - Magnet II 2 - 2Q");
    model.physics("mfnc").feature("mfc7").selection().set(new int[]{7});
    model.physics("mfnc").feature().duplicate("mfc8", "mfc7");
    model.physics("mfnc").feature("mfc8").label("Magnetic Flux Conservation - Magnet II 3 - 2Q 1");
    model.physics("mfnc").feature("mfc8").label("Magnetic Flux Conservation - Magnet II 3 - 2Q");
    model.physics("mfnc").feature("mfc8").selection().set(new int[]{9});
    model.physics("mfnc").feature().duplicate("mfc9", "mfc3");
    model.physics("mfnc").feature("mfc9").label("Magnetic Flux Conservation - Magnet IV 1 - 1Q");
    model.physics("mfnc").feature("mfc9").selection().set(new int[]{26});
    model.physics("mfnc").feature().duplicate("mfc10", "mfc9");
    model.physics("mfnc").feature("mfc10").label("Magnetic Flux Conservation - Magnet IV 2 - 1Q");
    model.physics("mfnc").feature("mfc10").selection().set(new int[]{25});
    model.physics("mfnc").feature().duplicate("mfc11", "mfc10");
    model.physics("mfnc").feature("mfc11").label("Magnetic Flux Conservation - Magnet IV 3 - 1Q");
    model.physics("mfnc").feature("mfc11").selection().set(new int[]{24});
    model.physics("mfnc").feature().duplicate("mfc12", "mfc11");
    model.physics("mfnc").feature("mfc12").selection().set(new int[]{1});
    model.physics("mfnc").feature("mfc12").label("Magnetic Flux Conservation - Magnet IV 1 - 2Q");
    model.physics("mfnc").feature().duplicate("mfc13", "mfc12");
    model.physics("mfnc").feature("mfc13").label("Magnetic Flux Conservation - Magnet IV 2 - 2Q");

    return model;
  }

  public static Model run2(Model model) {
    model.physics("mfnc").feature("mfc13").selection().set(new int[]{2});
    model.physics("mfnc").feature().duplicate("mfc14", "mfc13");
    model.physics("mfnc").feature("mfc14").label("Magnetic Flux Conservation - Magnet IV 3 - 2Q");
    model.physics("mfnc").feature("mfc14").selection().set(new int[]{3});

    model.func().create("an1", "Analytic");
    model.func().remove("an1");

    model.param().set("B_rem", "1.47[T]");

    model.physics("mfnc").feature("mfc3").set("Br", new String[]{"B_rem", "0", "0"});
    model.physics("mfnc").feature("mfc4").set("Br", new String[]{"B_rem", "0", "0"});
    model.physics("mfnc").feature("mfc5").set("Br", new String[]{"B_rem", "0", "0"});
    model.physics("mfnc").feature("mfc6").set("Br", new String[]{"B_rem", "0", "0"});
    model.physics("mfnc").feature("mfc7").set("Br", new String[]{"B_rem", "0", "0"});
    model.physics("mfnc").feature("mfc8").set("Br", new String[]{"B_rem", "0", "0"});
    model.physics("mfnc").feature("mfc9").set("Br", new String[]{"B_rem*cos(0)", "B_rem*sin(0)", "0"});
    model.physics("mfnc").feature("mfc10").set("Br", new String[]{"B_rem*cos(15)", "B_rem*sin(15)", "0"});
    model.physics("mfnc").feature("mfc11").set("Br", new String[]{"B_rem*cos(30)", "B_rem*sin(30)", "0"});
    model.physics("mfnc").feature("mfc12").set("Br", new String[]{"B_rem*cos(0)", "0", "0"});
    model.physics("mfnc").feature("mfc13").set("Br", new String[]{"B_rem*cos(-15)", "B_rem*sin(-15)", "0"});
    model.physics("mfnc").feature("mfc14").set("Br", new String[]{"B_rem*cos(-30)", "B_rem*sin(-30)", "0"});

    model.geom("geom1").run("dif2");
    model.geom("geom1").create("c3", "Circle");
    model.geom("geom1").feature("c3").set("r", "R_e");
    model.geom("geom1").feature("c3").set("angle", "180");
    model.geom("geom1").run("c3");
    model.geom("geom1").run("c3");
    model.geom("geom1").create("copy3", "Copy");
    model.geom("geom1").feature().move("copy3", 19);
    model.geom("geom1").run("dif2");
    model.geom("geom1").feature("copy3").selection("input")
         .set(new String[]{"dif2", "mir1", "pi1", "pi10", "pi11", "pi12", "pi2", "pi3", "pi4", "pi5", 
         "pi6", "pi7", "pi8", "pi9"});
    model.geom("geom1").run("copy3");
    model.geom("geom1").feature().move("c3", 19);
    model.geom("geom1").run("copy3");
    model.geom("geom1").create("dif3", "Difference");
    model.geom("geom1").feature("dif3").selection("input").set(new String[]{"c3"});
    model.geom("geom1").feature("dif3").selection("input2").set(new String[]{"copy3"});
    model.geom("geom1").run("dif3");
    model.geom("geom1").run("fin");

    model.material("mat1").selection().set(new int[]{1, 5, 15});

    model.physics("mfnc").prop("MeshControl").set("EnableMeshControl", true);

    model.mesh("mesh1").autoMeshSize(1);
    model.mesh("mesh1").autoMeshSize(3);
    model.mesh("mesh1").run();

    model.sol().create("sol1");
    model.sol("sol1").study("std1");

    model.study("std1").feature("stat").set("notlistsolnum", 1);
    model.study("std1").feature("stat").set("notsolnum", "1");
    model.study("std1").feature("stat").set("listsolnum", 1);
    model.study("std1").feature("stat").set("solnum", "1");

    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");

    model.result().create("pg1", "PlotGroup2D");
    model.result("pg1").label("Magnetic Flux Density Norm (mfnc)");
    model.result("pg1").set("data", "dset1");
    model.result("pg1").feature().create("surf1", "Surface");
    model.result("pg1").feature("surf1").set("expr", "mfnc.normB");
    model.result("pg1").feature("surf1").set("data", "parent");
    model.result().remove("pg1");

    model.sol("sol1").feature("s1").create("i1", "Iterative");

    model.result().create("pg1", "PlotGroup2D");
    model.result("pg1").label("Magnetic Flux Density Norm (mfnc)");
    model.result("pg1").set("data", "dset1");
    model.result("pg1").feature().create("surf1", "Surface");
    model.result("pg1").feature("surf1").set("expr", "mfnc.normB");
    model.result("pg1").feature("surf1").set("data", "parent");

    model.sol("sol1").runAll();

    model.result("pg1").run();
    model.result().dataset().create("dset2", "Solution");
    model.result().dataset("dset1").selection().geom("geom1", 2);
    model.result().dataset("dset1").selection().geom("geom1", 2);
    model.result().dataset("dset1").selection().set(new int[]{5});
    model.result().dataset("dset1").label("Air gap");
    model.result().create("pg2", "PlotGroup2D");
    model.result("pg2").run();
    model.result("pg2").label("Air gap");
    model.result("pg2").create("surf1", "Surface");
    model.result("pg2").feature("surf1").set("expr", "mfnc.normB");
    model.result("pg2").feature("surf1").set("descr", "Magnetic flux density norm");
    model.result("pg2").run();
    model.result("pg2").feature("surf1").label("B");
    model.result("pg1").run();
    model.result().remove("pg1");
    model.result().dataset("dset2").label("Magnets");
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().set(new int[]{22});
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().set(new int[]{21, 22});
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().set(new int[]{20, 21, 22});
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().set(new int[]{20, 21, 22, 27});
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().set(new int[]{20, 21, 22, 26, 27});
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().set(new int[]{20, 21, 22, 25, 26, 27});
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().set(new int[]{7, 20, 21, 22, 25, 26, 27});
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().set(new int[]{7, 8, 20, 21, 22, 25, 26, 27});
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().set(new int[]{7, 8, 10, 20, 21, 22, 25, 26, 27});
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().set(new int[]{2, 7, 8, 10, 20, 21, 22, 25, 26, 27});
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().set(new int[]{2, 3, 7, 8, 10, 20, 21, 22, 25, 26, 27});
    model.result().dataset("dset2").selection().geom("geom1", 2);
    model.result().dataset("dset2").selection().set(new int[]{2, 3, 4, 7, 8, 10, 20, 21, 22, 25, 26, 27});
    model.result().create("pg3", "PlotGroup2D");
    model.result("pg3").run();
    model.result("pg3").set("data", "dset2");
    model.result("pg3").create("arws1", "ArrowSurface");
    model.result("pg3").feature("arws1").set("expr", new String[]{"mfnc.Brx", "mfnc.Bry"});
    model.result("pg3").feature("arws1").set("descr", "Remanent flux density");
    model.result("pg3").run();

    model.func().create("an1", "Analytic");
    model.func("an1").label("rad2deg");
    model.func("an1").label("Radians to degress");
    model.func("an1").set("funcname", "rad2deg");
    model.func("an1").set("args", "phi");
    model.func("an1").set("expr", "phi * pi/180");
    model.func("an1").set("argunit", "rad");
    model.func("an1").set("fununit", "deg");
    model.func("an1").set("expr", "phi *180/pi");
    model.func("an1").label("Degrees to radians");
    model.func("an1").set("funcname", "deg2rad");
    model.func("an1").set("expr", "phi*pi/180");
    model.func("an1").set("argunit", "deg");
    model.func("an1").set("fununit", "rad");

    model.physics("mfnc").feature("mfc9")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*sin(deg2rad(0))", "0"});
    model.physics("mfnc").feature("mfc10")
         .set("Br", new String[]{"B_rem*cos(deg2rad(15))", "B_rem*sin(deg2rad(15))", "0"});
    model.physics("mfnc").feature("mfc11")
         .set("Br", new String[]{"B_rem*cos(deg2rad(30))", "B_rem*sin(deg2rad(30))", "0"});
    model.physics("mfnc").feature("mfc12").set("Br", new String[]{"B_rem*cos(deg2rad(0))", "0", "0"});
    model.physics("mfnc").feature("mfc13")
         .set("Br", new String[]{"B_rem*cos(deg2rad(-15))", "B_rem*sin(deg2rad(-15))", "0"});
    model.physics("mfnc").feature("mfc14")
         .set("Br", new String[]{"B_rem*cos(deg2rad(-30))", "B_rem*sin(deg2rad(-30))", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.label("nested-halbach-2d-blocks-iron.mph");

    model.physics("mfnc").feature("mfc3")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*sin(deg2rad(0))", "0"});
    model.physics("mfnc").feature("mfc4")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*sin(deg2rad(0))", "0"});
    model.physics("mfnc").feature("mfc5")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*sin(deg2rad(0))", "0"});
    model.physics("mfnc").feature("mfc6")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*sin(deg2rad(0))", "0"});
    model.physics("mfnc").feature("mfc7")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*sin(deg2rad(0))", "0"});
    model.physics("mfnc").feature("mfc8")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*cos(deg2rad(0))", "0"});
    model.physics("mfnc").feature("mfc12")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*sin(deg2rad(0))", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg3").label("Magnets");
    model.result("pg3").run();
    model.result("pg3").feature("arws1").label("B_rem");

    model.physics("mfnc").feature("mfc8")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*sin(deg2rad(0))", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc10")
         .set("Br", new String[]{"B_rem*cos(15*pi/180)", "B_rem*sin(15*pi/180)", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.func("an1").set("expr", "t*pi/180");
    model.func("an1").set("args", "t");

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.func("an1").set("argunit", "");
    model.func("an1").set("fununit", "");

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg2").run();
    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg3").create("surf1", "Surface");
    model.result("pg3").feature("surf1").label("Psi");
    model.result("pg3").feature("surf1")
         .set("expr", "((((mfnc.Bx*mfnc.Brx+mfnc.By*mfnc.Bry)/mfnc.normBr)*-((mfnc.Hx*mfnc.Brx+mfnc.Hy*mfnc.Bry)/mfnc.normBr)))");
    model.result("pg3").feature("surf1").set("unit", "kPa");
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc5")
         .set("Br", new String[]{"B_rem*cos(deg2rad(-15))", "B_rem*sin(deg2rad(-15))", "0"});

    model.result("pg3").run();

    model.physics("mfnc").feature("mfc8")
         .set("Br", new String[]{"B_rem*cos(deg2rad(15))", "B_rem*sin(deg2rad(15))", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg2").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc4")
         .set("Br", new String[]{"B_rem*cos(deg2rad(15))", "B_rem*sin(deg2rad(15))", "0"});
    model.physics("mfnc").feature("mfc5")
         .set("Br", new String[]{"B_rem*cos(deg2rad(30))", "B_rem*sin(deg2rad(30))", "0"});
    model.physics("mfnc").feature("mfc7")
         .set("Br", new String[]{"B_rem*cos(deg2rad(-15))", "B_rem*sin(deg2rad(-15))", "0"});
    model.physics("mfnc").feature("mfc8")
         .set("Br", new String[]{"B_rem*cos(deg2rad(-30))", "B_rem*sin(deg2rad(-30))", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc4")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*sin(deg2rad(0))", "0"});
    model.physics("mfnc").feature("mfc5")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*sin(deg2rad(0))", "0"});
    model.physics("mfnc").feature("mfc7")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*sin(deg2rad(0))", "0"});
    model.physics("mfnc").feature("mfc8")
         .set("Br", new String[]{"B_rem*cos(deg2rad(0))", "B_rem*sin(deg2rad(0))", "0"});
    model.physics("mfnc").feature("mfc10")
         .set("Br", new String[]{"B_rem*cos(deg2rad(15))", "B_rem*sin(deg2rad(15))", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();

    model.param().set("phi_S_IV", "60[deg]");

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.param().set("phi_S_II", "30[deg]");
    model.param().set("phi_S_IV", "30[deg]");

    model.sol("sol1").runAll();

    model.result("pg2").run();

    model.param().set("R_c", "R_s+h_fc");

    model.geom("geom1").run("dif3");
    model.geom("geom1").create("c4", "Circle");
    model.geom("geom1").feature("c4").set("r", "R_c");
    model.geom("geom1").feature("c4").set("angle", "180");
    model.geom("geom1").run("c4");
    model.geom("geom1").run("c4");
    model.geom("geom1").create("copy4", "Copy");
    model.geom("geom1").feature().move("copy4", 22);
    model.geom("geom1").runPre("copy4");
    model.geom("geom1").feature("copy4").selection("input")
         .set(new String[]{"dif2", "mir1", "pi1", "pi10", "pi11", "pi12", "pi2", "pi3", "pi4", "pi5", 
         "pi6", "pi7", "pi8", "pi9"});
    model.geom("geom1").run("copy4");
    model.geom("geom1").run("copy4");
    model.geom("geom1").create("dif4", "Difference");
    model.geom("geom1").feature().move("c4", 23);
    model.geom("geom1").runPre("dif4");
    model.geom("geom1").feature("dif4").selection("input").set(new String[]{"c4"});
    model.geom("geom1").feature("dif4").selection("input2").set(new String[]{"copy4"});
    model.geom("geom1").run("dif4");
    model.geom("geom1").runPre("fin");
    model.geom("geom1").run("fin");
    model.geom("geom1").run("pi1");
    model.geom("geom1").run("pi2");
    model.geom("geom1").run("mir1");
    model.geom("geom1").run("c1");
    model.geom("geom1").run("copy1");
    model.geom("geom1").run("dif1");
    model.geom("geom1").run("c2");
    model.geom("geom1").feature("copy2").selection("input")
         .set(new String[]{"dif1", "mir1", "pi1", "pi10", "pi11", "pi12", "pi2", "pi3", "pi4", "pi5", 
         "pi6", "pi7", "pi8", "pi9"});
    model.geom("geom1").run("copy2");
    model.geom("geom1").run("dif2");
    model.geom("geom1").run("c3");
    model.geom("geom1").feature().move("c3", 24);
    model.geom("geom1").run("copy2");
    model.geom("geom1").feature().move("copy4", 19);
    model.geom("geom1").feature().move("copy4", 22);
    model.geom("geom1").run("copy2");
    model.geom("geom1").run("dif2");
    model.geom("geom1").feature().move("c4", 19);
    model.geom("geom1").run("c4");
    model.geom("geom1").feature("copy3").selection("input")
         .set(new String[]{"dif1", "dif2", "mir1", "pi1", "pi10", "pi11", "pi12", "pi2", "pi3", "pi4", 
         "pi5", "pi6", "pi7", "pi8", "pi9"});
    model.geom("geom1").run("copy3");
    model.geom("geom1").feature("dif3").selection("input").set(new String[]{"c4"});
    model.geom("geom1").run("dif3");
    model.geom("geom1").feature().move("c3", 22);
    model.geom("geom1").feature().move("copy4", 22);
    model.geom("geom1").run("copy4");
    model.geom("geom1").runPre("copy4");
    model.geom("geom1").feature("copy4").selection("input")
         .set(new String[]{"dif1", "dif2", "mir1", "pi1", "pi10", "pi11", "pi12", "pi2", "pi3", "pi4", 
         "pi5", "pi6", "pi7", "pi8", "pi9"});
    model.geom("geom1").run("copy4");
    model.geom("geom1").runPre("copy3");
    model.geom("geom1").run("copy3");
    model.geom("geom1").run("dif3");
    model.geom("geom1").feature("copy4").selection("input")
         .set(new String[]{"dif1", "dif2", "dif3", "mir1", "pi1", "pi10", "pi11", "pi12", "pi2", "pi3", 
         "pi4", "pi5", "pi6", "pi7", "pi8", "pi9"});
    model.geom("geom1").run("copy4");
    model.geom("geom1").run("c3");
    model.geom("geom1").feature("dif4").selection("input").set(new String[]{"c3"});
    model.geom("geom1").run("dif4");
    model.geom("geom1").run("fin");
    model.geom("geom1").run("fin");

    model.physics("mfnc").feature("mfc2").selection()
         .set(new int[]{2, 6, 8, 12, 13, 14, 15, 17, 18, 19, 20, 24, 25});

    model.material("mat2").selection().set(new int[]{2, 6, 8, 12, 13, 14, 15, 17, 18, 19, 20, 24, 25});

    model.sol("sol1").runAll();

    model.result("pg2").run();

    model.param().set("phi_S_II", "45[deg]");
    model.param().set("phi_S_IV", "45[deg]");

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc3").set("Br", new String[]{"B_rem*cos(15[deg])", "B_rem*sin(15[deg])", "0"});
    model.physics("mfnc").feature("mfc4").set("Br", new String[]{"B_rem*cos(15[deg])", "B_rem*sin(15[deg])", "0"});
    model.physics("mfnc").feature("mfc5").set("Br", new String[]{"B_rem*cos(-15[deg])", "B_rem*sin(-15[deg])", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg2").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc3").set("Br", new String[]{"B_rem*cos(30[deg])", "B_rem*sin(30[deg])", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc9").set("Br", new String[]{"B_rem*cos(-15[deg])", "B_rem*sin(-15[deg])", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc11").set("Br", new String[]{"B_rem*cos(45[deg])", "B_rem*sin(45[deg])", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc6").set("Br", new String[]{"B_rem*cos(-15[deg])", "B_rem*sin(-15[deg])", "0"});
    model.physics("mfnc").feature("mfc7").set("Br", new String[]{"B_rem*cos(-15[deg])", "B_rem*sin(-15[deg])", "0"});
    model.physics("mfnc").feature("mfc8").set("Br", new String[]{"B_rem*cos(15[deg])", "B_rem*sin(15[deg])", "0"});
    model.physics("mfnc").feature("mfc10").set("Br", new String[]{"B_rem*cos(15[deg])", "B_rem*sin(15[deg])", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();

    model.physics("mfnc").feature("mfc14")
         .set("Br", new String[]{"B_rem*cos(-30[deg])", "B_rem*sin(-30[deg])", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc14")
         .set("Br", new String[]{"B_rem*cos(-45[deg])", "B_rem*sin(-45[deg])", "0"});
    model.physics("mfnc").feature("mfc12").set("Br", new String[]{"B_rem*cos(15[deg])", "B_rem*sin(15[deg])", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc6").set("Br", new String[]{"B_rem*cos(-45[deg])", "B_rem*sin(-45[deg])", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc8").set("Br", new String[]{"B_rem*cos(30[deg])", "B_rem*sin(30[deg])", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc8").set("Br", new String[]{"B_rem*cos(15[deg])", "B_rem*sin(15[deg])", "0"});
    model.physics("mfnc").feature("mfc12").set("Br", new String[]{"B_rem*cos(0[deg])", "B_rem*sin(0[deg])", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc3").set("Br", new String[]{"B_rem*cos(0[deg])", "B_rem*sin(0[deg])", "0"});
    model.physics("mfnc").feature("mfc9").set("Br", new String[]{"B_rem*cos(0[deg])", "B_rem*sin(0[deg])", "0"});
    model.physics("mfnc").feature("mfc11").set("Br", new String[]{"B_rem*cos(30[deg])", "B_rem*sin(30[deg])", "0"});
    model.physics("mfnc").feature("mfc4").set("Br", new String[]{"B_rem*cos(-15[deg])", "B_rem*sin(-15[deg])", "0"});
    model.physics("mfnc").feature("mfc5").set("Br", new String[]{"B_rem*cos(-30[deg])", "B_rem*sin(-30[deg])", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();

    model.physics("mfnc").feature("mfc6").set("Br", new String[]{"B_rem*cos(0[deg])", "B_rem*sin(0[deg])", "0"});
    model.physics("mfnc").feature("mfc7").set("Br", new String[]{"B_rem*cos(15[deg])", "B_rem*sin(15[deg])", "0"});
    model.physics("mfnc").feature("mfc8").set("Br", new String[]{"B_rem*cos(30[deg])", "B_rem*sin(30[deg])", "0"});
    model.physics("mfnc").feature("mfc13")
         .set("Br", new String[]{"B_rem*cos(-15[deg])", "B_rem*sin(-15[deg])", "0"});
    model.physics("mfnc").feature("mfc14")
         .set("Br", new String[]{"B_rem*cos(-30[deg])", "B_rem*sin(-30[deg])", "0"});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").feature("surf1").set("rangedataactive", "on");
    model.result("pg3").feature("surf1")
         .set("expr", "abs((mfnc.Bx*mfnc.Brx+mfnc.By*mfnc.Bry)/mfnc.normBr)*abs((mfnc.Hx*mfnc.Brx+mfnc.Hy*mfnc.Bry)/mfnc.normBr)");
    model.result("pg3").run();
    model.result("pg3").feature("surf1").set("rangedataactive", "off");
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg2").run();
    model.result("pg3").run();

    model.coordSystem().create("ie1", "geom1", "InfiniteElement");
    model.coordSystem("ie1").selection().set(new int[]{1});
    model.coordSystem("ie1").set("ScalingType", "Cylindrical");

    model.sol("sol1").runAll();

    model.result("pg2").run();

    model.coordSystem("ie1").label("External environment");

    model.sol("sol1").runAll();

    model.result("pg2").run();

    model.mesh("mesh1").automatic(false);
    model.mesh("mesh1").feature("size").set("hnarrow", "5");
    model.mesh("mesh1").run();

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").feature("surf1").set("rangecoloractive", "on");
    model.result("pg3").feature("surf1").set("rangecolormin", "0");
    model.result("pg3").feature("surf1").set("rangecolormax", "409");
    model.result("pg3").feature("surf1").set("rangedataactive", "on");
    model.result("pg3").feature("surf1").set("rangedatamin", "0");
    model.result("pg3").feature("surf1").set("rangedatamax", "409");
    model.result("pg2").run();

    model.label("nested-halbach-2d-blocks-iron.mph");

    model.result("pg2").run();
    model.result().dataset().create("pc1", "ParCurve2D");
    model.result().dataset("pc1").set("parmax1", "pi");
    model.result("pg3").run();
    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg2").run();

    model.param().set("phi_S_II", "30[deg]");

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg2").run();
    model.result().dataset("pc1").set("exprx", "");
    model.result("pg3").run();
    model.result("pg2").run();
    model.result().dataset("pc1").label("Air gap central line");
    model.result().dataset("pc1").set("exprx", "(R_o+h_gap/2)*cos(0)");
    model.result().dataset("pc1").set("expry", "(R_o+h_gap/2)*sin(s)");
    model.result().dataset("pc1").set("exprx", "(R_o+h_gap/2)*cos(s)");
    model.result().dataset("pc1").run();
    model.result().create("pg4", "PlotGroup1D");
    model.result("pg4").run();
    model.result("pg4").label("Air gap central line");
    model.result("pg4").create("lngr1", "LineGraph");
    model.result("pg4").run();
    model.result("pg4").set("data", "pc1");
    model.result("pg4").run();
    model.result("pg4").feature("lngr1").set("expr", "mfnc.normB");
    model.result("pg4").feature("lngr1").set("descr", "Magnetic flux density norm");
    model.result("pg4").run();
    model.result("pg4").run();

    model.label("nested-halbach-2d-blocks-iron.mph");

    model.result("pg4").run();
    model.result("pg3").run();
    model.result("pg3").create("arws2", "ArrowSurface");
    model.result("pg3").feature("arws2").set("expr", new String[]{"mfnc.Hx", "mfnc.Hy"});
    model.result("pg3").feature("arws2").set("descr", "Magnetic field");
    model.result("pg3").feature("arws2").set("expr", new String[]{"mu0_const*mfnc.Hx", "mfnc.Hy"});
    model.result("pg3").feature("arws2").setIndex("expr", "mu0_const*mfnc.Hy", 1);
    model.result("pg3").feature("arws2").set("descractive", "on");
    model.result("pg3").feature("arws2").set("descr", "mu0*H");
    model.result("pg3").run();
    model.result("pg3").feature("arws2").label("H");
    model.result("pg3").run();
    model.result("pg3").feature("surf1").active(false);
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").feature("arws2").set("arrowbase", "center");
    model.result("pg3").feature("arws2").set("color", "black");
    model.result("pg3").run();
    model.result("pg3").feature("arws1").set("arrowbase", "center");
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").run();
    model.result("pg3").feature("surf1").active(true);
    model.result("pg3").run();
    model.result("pg3").run();

    model.label("nested-halbach-2d-blocks-iron.mph");

    model.result("pg3").run();

    model.param().set("R_o", "50[mm]");

    model.geom("geom1").run("fin");

    model.param().set("phi_S_II", "45[deg]");

    model.geom("geom1").run("fin");

    model.mesh("mesh1").run();

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg4").run();
    model.result("pg3").run();

    model.geom("part1").run("pol1");
    model.geom().create("part3", "Part", 2);
    model.geom("part3").label("Cylinder sector");
    model.geom("part3").label("Cylinder Sector");
    model.geom("part3").inputParam().set("r1", "R_i");
    model.geom("part3").inputParam().set("r2", "R_o");
    model.geom("part3").inputParam().set("phi1", "0[deg]");
    model.geom("part3").inputParam().set("phi2", "45[deg]");
    model.geom("part3").create("c1", "Circle");
    model.geom("part3").feature("c1").set("r", "r2");
    model.geom("part3").inputParam().set("phi1", "15[deg]");
    model.geom("part3").feature("c1").set("angle", "phi2");
    model.geom("part3").run("c1");
    model.geom("part3").run("c1");
    model.geom("part3").create("c2", "Circle");
    model.geom("part3").feature("c2").set("r", "r1");
    model.geom("part3").feature("c2").set("angle", "phi2");
    model.geom("part3").run("c2");
    model.geom("part3").run("c2");
    model.geom("part3").create("c3", "Circle");
    model.geom("part3").feature("c3").set("r", "r2");
    model.geom("part3").feature("c3").set("angle", "phi1");
    model.geom("part3").run("c3");
    model.geom("part3").run("c3");
    model.geom("part3").create("dif1", "Difference");
    model.geom("part3").feature("dif1").selection("input").set(new String[]{"c1"});
    model.geom("part3").feature("dif1").selection("input2").set(new String[]{"c2", "c3"});
    model.geom("part3").run("dif1");
    model.geom("geom1").run("dif4");
    model.geom("geom1").create("pi13", "PartInstance");
    model.geom("geom1").feature("pi13").set("selkeepnoncontr", "off");
    model.geom("geom1").feature("pi13").set("part", "part3");
    model.geom("geom1").feature().move("pi13", 1);
    model.geom("geom1").feature("pi13").label("Cylinder Sector 1 - Magnet II");
    model.geom("geom1").feature().remove("pi13");
    model.geom("part1").inputParam().set("phi1", "15[deg]");
    model.geom("part1").run("pol1");
    model.geom("part1").run("pol1");
    model.geom("part1").create("c1", "Circle");
    model.geom("part1").feature("c1").set("r", "r2");
    model.geom("part1").feature("c1").set("angle", "phi2");
    model.geom("part1").run("c1");
    model.geom("part1").run("c1");
    model.geom("part1").create("c2", "Circle");
    model.geom("part1").feature("c2").set("r", "r1");
    model.geom("part1").feature("c2").set("angle", "phi2");
    model.geom("part1").run("c2");
    model.geom("part1").run("c2");
    model.geom("part1").create("c3", "Circle");
    model.geom("part1").feature("c3").set("r", "r2");
    model.geom("part1").feature("c3").set("angle", "phi1");
    model.geom("part1").run("c3");
    model.geom("part1").feature().remove("pol1");
    model.geom("part1").run("c3");
    model.geom("part1").create("dif1", "Difference");
    model.geom("part1").feature("dif1").selection("input").set(new String[]{"c1"});
    model.geom("part1").feature("dif1").selection("input2").set(new String[]{"c2", "c3"});
    model.geom("part1").run("dif1");
    model.geom().remove("part3");
    model.geom("geom1").run("");
    model.geom("geom1").feature("pi1").setEntry("inputexpr", "phi2", "15[deg]");
    model.geom("part1").feature().remove("dif1");
    model.geom("part1").run("c1");
    model.geom("part1").run("c2");
    model.geom("part1").feature().remove("c3");
    model.geom("part1").feature("c1").set("rot", "phi1");
    model.geom("part1").run("c1");
    model.geom("part1").feature("c2").set("rot", "phi1");
    model.geom("part1").run("c1");
    model.geom("part1").create("dif1", "Difference");
    model.geom("part1").feature().move("dif1", 2);
    model.geom("part1").run("c2");
    model.geom("part1").feature("dif1").selection("input").set(new String[]{"c1"});
    model.geom("part1").feature("dif1").selection("input2").set(new String[]{"c2"});
    model.geom("part1").run("dif1");
    model.geom("geom1").run("pi1");
    model.geom("geom1").run("pi2");
    model.geom("geom1").runPre("fin");
    model.geom("geom1").feature("pi1").setEntry("inputexpr", "phi1", "1*delta_phi_B_II");
    model.geom("geom1").feature("pi1").setEntry("inputexpr", "phi1", "0");
    model.geom("geom1").feature("pi1").setEntry("inputexpr", "phi2", "1*delta_phi_B_II");
    model.geom("geom1").runPre("fin");
    model.geom("geom1").run("pi1");
    model.geom("geom1").run("pi2");
    model.geom("geom1").run("pi3");
    model.geom("geom1").run("pi1");
    model.geom("geom1").run("pi2");
    model.geom("geom1").run("pi3");
    model.geom("part1").run("dif1");

    return model;
  }

  public static Model run3(Model model) {
    model.geom("geom1").run("fin");

    model.mesh("mesh1").run();

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg3").run();
    model.result("pg4").run();

    model.material("mat1").selection().set(new int[]{1, 2, 7});
    model.material("mat2").selection().set(new int[]{2, 6, 8, 12, 13, 14, 15, 16, 17, 18, 22, 23});

    model.mesh("mesh1").run();

    model.physics("mfnc").feature("mfc2").selection().set(new int[]{2, 6, 8, 12, 13, 14, 15, 16, 17, 18, 22, 23});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg4").run();

    model.physics("mfnc").feature("mfc2").selection().set(new int[]{2, 6, 8, 12, 13, 14, 16, 17, 18, 22, 23});

    model.material("mat2").selection().set(new int[]{2, 6, 8, 12, 13, 14, 16, 17, 18, 22, 23});
    model.material("mat1").selection().set(new int[]{1, 2, 7, 15});

    model.sol("sol1").runAll();

    model.result("pg2").run();
    model.result("pg4").run();
    model.result("pg3").run();
    model.result("pg2").run();
    model.result("pg2").run();

    model.label("nested-halbach-2d-segments-iron.mph");

    model.result("pg3").run();

    return model;
  }

  public static void main(String[] args) {
    Model model = run();
    model = run2(model);
    run3(model);
  }

}
