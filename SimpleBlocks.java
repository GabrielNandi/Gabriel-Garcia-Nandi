/*
 * SimpleBlocks.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;

/** Model exported on Aug 25 2016, 15:39 by COMSOL 5.2.1.152. */
public class SimpleBlocks {

  public static Model run() {
    Model model = ModelUtil.create("Model");

    model.modelPath("C:\\Users\\fabiofortkamp\\code\\TeslaMax");

    model.label("SimpleBlocks.mph");

    model.comments("Untitled\n\n");

    model.modelNode().create("comp1");

    model.geom().create("geom1", 2);

    model.mesh().create("mesh1", "geom1");

    model.geom().create("part1", "Part", 2);
    model.geom("part1").label("Cylinder block");
    model.geom("part1").inputParam().set("r1", "10[mm]");
    model.geom("part1").inputParam().set("r2", "20[mm]");
    model.geom("part1").inputParam().set("phi1", "15[deg]");
    model.geom("part1").inputParam().set("phi2", "30[deg]");
    model.geom("part1").create("c1", "Circle");
    model.geom("part1").feature("c1").set("r", "r2");
    model.geom("part1").feature("c1").set("angle", "phi2-phi1");
    model.geom("part1").feature("c1").set("rot", "phi1");
    model.geom("part1").create("c2", "Circle");
    model.geom("part1").feature("c2").set("r", "r1");
    model.geom("part1").feature("c2").set("angle", "phi2-phi1");
    model.geom("part1").feature("c2").set("rot", "phi1");
    model.geom("part1").create("dif1", "Difference");
    model.geom("part1").feature("dif1").selection("input2").set(new String[]{"c2"});
    model.geom("part1").feature("dif1").selection("input").set(new String[]{"c1"});
    model.geom("part1").run();
    model.geom("geom1").create("pi1", "PartInstance");
    model.geom("geom1").feature("pi1").set("inputexpr", new String[]{"10[mm]", "20[mm]", "0[deg]", "15[deg]"});
    model.geom("geom1").feature("pi1").set("selkeepnoncontr", false);
    model.geom("geom1").create("pi2", "PartInstance");
    model.geom("geom1").feature("pi2").set("selkeepnoncontr", false);
    model.geom("geom1").create("pi3", "PartInstance");
    model.geom("geom1").feature("pi3").set("inputexpr", new String[]{"10[mm]", "20[mm]", "30[deg]", "45[deg]"});
    model.geom("geom1").feature("pi3").set("selkeepnoncontr", false);
    model.geom("geom1").create("c1", "Circle");
    model.geom("geom1").feature("c1").set("r", "10[mm]");
    model.geom("geom1").feature("c1").set("angle", "45");
    model.geom("geom1").run();

    model.material().create("mat1", "Common", "comp1");
    model.material("mat1").selection().set(new int[]{2, 3, 4});
    model.material("mat1").propertyGroup().create("BHCurve", "BH curve");
    model.material("mat1").propertyGroup("BHCurve").func().create("BH", "Interpolation");
    model.material("mat1").propertyGroup().create("HBCurve", "HB curve");
    model.material("mat1").propertyGroup("HBCurve").func().create("HB", "Interpolation");
    model.material("mat1").propertyGroup().create("EffectiveBHCurve", "Effective BH curve");
    model.material("mat1").propertyGroup("EffectiveBHCurve").func().create("BHeff", "Interpolation");
    model.material("mat1").propertyGroup().create("EffectiveHBCurve", "Effective HB curve");
    model.material("mat1").propertyGroup("EffectiveHBCurve").func().create("HBeff", "Interpolation");

    model.physics().create("mfnc", "MagnetostaticsNoCurrents", "geom1");
    model.physics("mfnc").create("mfc2", "MagneticFluxConservation", 2);
    model.physics("mfnc").feature("mfc2").selection().set(new int[]{1});
    model.physics("mfnc").create("zsp1", "ZeroMagneticScalarPotential", 1);
    model.physics("mfnc").feature("zsp1").selection().set(new int[]{2, 6});

    model.mesh("mesh1").autoMeshSize(3);

    model.view("view1").axis().set("abstractviewxscale", "2.691410008992534E-5");
    model.view("view1").axis().set("abstractviewtratio", "0.04999999329447746");
    model.view("view1").axis().set("abstractviewlratio", "-0.43051785230636597");
    model.view("view1").axis().set("abstractviewyscale", "2.6914098270935938E-5");
    model.view("view1").axis().set("abstractviewrratio", "0.4305177330970764");
    model.view("view1").axis().set("abstractviewbratio", "-0.04999999329447746");
    model.view("view1").axis().set("ymax", "0.017533432692289352");
    model.view("view1").axis().set("xmax", "0.020499998703598976");
    model.view("view1").axis().set("ymin", "-0.0033912966027855873");
    model.view("view1").axis().set("xmin", "-4.99999150633812E-4");
    model.view("view2").axis().set("abstractviewxscale", "2.3769989638822153E-5");
    model.view("view2").axis().set("abstractviewtratio", "0.18695257604122162");
    model.view("view2").axis().set("abstractviewlratio", "-0.05000004917383194");
    model.view("view2").axis().set("abstractviewyscale", "2.3769984181853943E-5");
    model.view("view2").axis().set("abstractviewrratio", "0.05000004917383194");
    model.view("view2").axis().set("abstractviewbratio", "-0.1869526207447052");
    model.view("view2").axis().set("ymax", "0.011869525536894798");
    model.view("view2").axis().set("xmax", "0.020284444093704224");
    model.view("view2").axis().set("ymin", "-0.0018695262260735035");
    model.view("view2").axis().set("xmin", "-9.659267961978912E-4");

    model.material("mat1").label("Soft Iron (with losses)");
    model.material("mat1").set("family", "iron");
    model.material("mat1").propertyGroup("def")
         .set("electricconductivity", new String[]{"1.12e7[S/m]", "0", "0", "0", "1.12e7[S/m]", "0", "0", "0", "1.12e7[S/m]"});
    model.material("mat1").propertyGroup("def")
         .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
    model.material("mat1").propertyGroup("BHCurve").func("BH").set("extrap", "linear");
    model.material("mat1").propertyGroup("BHCurve").func("BH")
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
    model.material("mat1").propertyGroup("BHCurve").set("normB", "BH(normH[m/A])[T]");
    model.material("mat1").propertyGroup("BHCurve").addInput("magneticfield");
    model.material("mat1").propertyGroup("HBCurve").func("HB").set("extrap", "linear");
    model.material("mat1").propertyGroup("HBCurve").func("HB")
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
    model.material("mat1").propertyGroup("HBCurve").set("normH", "HB(normB[1/T])[A/m]");
    model.material("mat1").propertyGroup("HBCurve").addInput("magneticfluxdensity");
    model.material("mat1").propertyGroup("EffectiveBHCurve").func("BHeff").set("extrap", "linear");
    model.material("mat1").propertyGroup("EffectiveBHCurve").func("BHeff")
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
    model.material("mat1").propertyGroup("EffectiveBHCurve").set("normBeff", "BHeff(normHeff[m/A])[T]");
    model.material("mat1").propertyGroup("EffectiveBHCurve").addInput("magneticfield");
    model.material("mat1").propertyGroup("EffectiveHBCurve").func("HBeff").set("extrap", "linear");
    model.material("mat1").propertyGroup("EffectiveHBCurve").func("HBeff")
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
    model.material("mat1").propertyGroup("EffectiveHBCurve").set("normHeff", "HBeff(normBeff[1/T])[A/m]");
    model.material("mat1").propertyGroup("EffectiveHBCurve").addInput("magneticfluxdensity");

    model.physics("mfnc").feature("mfc1").set("ConstitutiveRelationH", "BHCurve");
    model.physics("mfnc").feature("mfc2").set("ConstitutiveRelationH", "RemanentFluxDensity");
    model.physics("mfnc").feature("mfc2").set("Br", new String[][]{{"1"}, {"0"}, {"0"}});
    model.physics("mfnc").feature("mfc2")
         .set("mur", new String[][]{{"1.05"}, {"0"}, {"0"}, {"0"}, {"1.05"}, {"0"}, {"0"}, {"0"}, {"1.05"}});

    model.mesh("mesh1").run();

    model.physics("mfnc").feature("mfc2").set("mur_mat", "userdef");

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

    model.result().create("pg1", "PlotGroup2D");
    model.result("pg1").create("surf1", "Surface");

    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg1").label("Magnetic Flux Density Norm (mfnc)");
    model.result("pg1").feature("surf1").set("descr", "Magnetic flux density norm");
    model.result("pg1").feature("surf1").set("unit", "T");
    model.result("pg1").feature("surf1").set("expr", "mfnc.normB");
    model.result("pg1").feature("surf1").set("resolution", "normal");

    return model;
  }

  public static void main(String[] args) {
    run();
  }

}
