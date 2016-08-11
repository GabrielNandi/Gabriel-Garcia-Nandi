/*
 * NestedHalbachIronSegmentsModel.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;

/** Model exported on Aug 8 2016, 09:24 by COMSOL 5.2.1.152. */
public class NestedHalbachIronSegmentsModel {

    public static final String COMPONENT_NAME = "nhalbach_system";

    public static final String GEOMETRY_TAG = "nhalbach_geometry";
    public static final String CYLINDER_BLOCK_PART_NAME = "cylinder_block";
    public static final String CYLINDER_SHELL_PART_NAME = "cylinder_shell";

    private static final String MAGNET_II_1Q_BLOCK_TAG = "magnet_ii_1q";
    private static final String MAGNET_II_2Q_BLOCK_TAG = "magnet_ii_2q";
    private static final String MAGNET_IV_1Q_BLOCK_TAG = "magnet_iv_1q";
    private static final String MAGNET_IV_2Q_BLOCK_TAG = "magnet_iv_2q";

    private static final String IRON_II_BLOCK_TAG = "iron_ii";
    private static final String IRON_IV_BLOCK_TAG = "iron_iv";

    private static final String SHAFT_TAG = "shaft";
    private static final String AIR_GAP_TAG = "air_gap";
    private static final String ENVIRONMENT_TAG = "environment";
    
    private static Model model;
    private static ModelNodeList modelNodes;
    private static ModelParam params;
    private static ModelNode component;
    
    private static GeomList geometryList;
    private static GeomSequence geometry;
    private static GeomFeatureList geomFeatures;

    private static String[] magnetII1QBlockTags;
    private static String[] magnetII2QBlockTags;
    private static String[] magnetIV1QBlockTags;
    private static String[] magnetIV2QBlockTags;
    private static String ironIIBlockTag;
    private static String ironIVBlockTag;
    private static String shaftTag;
    private static String airGapTag;
    private static String environmentTag;

    private static GeomFeature[] magnetII1QBlockFeatures;
    private static GeomFeature[] magnetII2QBlockFeatures;
    private static GeomFeature[] magnetIV1QBlockFeatures;
    private static GeomFeature[] magnetIV2QBlockFeatures;
    private static GeomFeature ironIIBlockFeature;
    private static GeomFeature ironIVBlockFeature;
    private static GeomFeature shaftFeature;
    private static GeomFeature airGapFeature;
    private static GeomFeature environmentFeature;

    private static GeomSequence configureCylinderBlock(){

	GeomSequence part = geometryList.create(CYLINDER_BLOCK_PART_NAME, "Part", 2);
	
	part.label("Cylinder Block");
	part.inputParam().set("r1", "R_i");
	part.inputParam().set("r2", "R_o");
	part.inputParam().set("phi1", "15[deg]");
	part.inputParam().set("phi2", "45[deg]");
	part.create("c1", "Circle");
	part.feature("c1").set("r", "r2");
	part.feature("c1").set("rot", "phi1");
	part.feature("c1").set("angle", "phi2-phi1");
	part.create("c2", "Circle");
	part.feature("c2").set("r", "r1");
	part.feature("c2").set("rot", "phi1");
	part.feature("c2").set("angle", "phi2-phi1");
	part.create("dif1", "Difference");
	part.feature("dif1").selection("input2").set(new String[]{"c2"});
	part.feature("dif1").selection("input").set(new String[]{"c1"});
	part.run();

	return part;

	
    }

    private static GeomSequence configureCylinderShell() {

	GeomSequence part = geometryList.create(CYLINDER_SHELL_PART_NAME, "Part", 2);
	
	part.label("Cylinder Shell");
	part.inputParam().set("r1", "R_i");
	part.inputParam().set("r2", "R_o");
	part.inputParam().set("delta_phi", "180[deg]");
	part.create("c1", "Circle");
	part.feature("c1").set("r", "r2");
	part.feature("c1").set("angle", "delta_phi");
	part.create("c2", "Circle");
	part.feature("c2").set("r", "r1");
	part.feature("c2").set("angle", "delta_phi");
	part.create("dif1", "Difference");
	part.feature("dif1").selection("input2").set(new String[]{"c2"});
	part.feature("dif1").selection("input").set(new String[]{"c1"});
	part.run();

	return part;
    }
    

    public static Model run() {
        model = ModelUtil.create("Model");

	params = model.param();

	params.set("R_i", "15[mm]");
	params.set("R_o", "50[mm]");
	params.set("h_gap", "20[mm]");
	params.set("R_s", "140[mm]");
	params.set("h_fc", "20[mm]");
	params.set("R_e", "2[m]");
	params.set("n_II", "3", "Number of segments in magnet II");
	params.set("n_IV", "3", "Number of segments in magnet IV");
	params.set("R_g", "R_o+h_gap");
	params.set("phi_S_II", "45[deg]");
	params.set("phi_S_IV", "45[deg]");
	params.set("delta_phi_S_II", "(phi_S_II)/n_II");
	params.set("delta_phi_S_IV", "(phi_S_IV)/n_IV");
	params.set("B_rem", "1.47[T]");
	params.set("R_c", "R_s+h_fc");

	modelNodes = model.modelNode();

	component = modelNodes.create(COMPONENT_NAME);

	geometryList = model.geom();
	geometry = geometryList.create(GEOMETRY_TAG, 2);

	model.mesh().create("mesh1", GEOMETRY_TAG);

	GeomSequence cylinderBlockPart = configureCylinderBlock();
	GeomSequence cylinderShellPart = configureCylinderShell();

	int nII = Integer.parseInt(params.get("n_II"));
	

	geomFeatures = geometry.feature();

	magnetII1QBlockTags = new String[nII];
	magnetII1QBlockFeatures = new GeomFeature[nII];

	magnetII2QBlockTags = new String[nII];
	magnetII2QBlockFeatures = new GeomFeature[nII];

	GeomFeature blockFeature;
	String innerAngleExpr = null;
	String outerAngleExpr = null;

	String tag1Q;
	String tag2Q;

	// loop to build the magnet II blocks
	for (int i = 0; i < nII; i++) {
	    tag1Q = geomFeatures.uniquetag(MAGNET_II_1Q_BLOCK_TAG);
	    magnetII1QBlockTags[i] = tag1Q;
	    
	    blockFeature = geomFeatures.create(tag1Q, "PartInstance");
	    blockFeature.set("part",CYLINDER_BLOCK_PART_NAME);
	    blockFeature.label("Cylinder Block " + (i+1) + " - Magnet II - 1Q");

	    // the cylinder block is builting sweeping phi from innerAngleExpr to outerAngleExpr
	    innerAngleExpr = String.format("%d * delta_phi_S_II",i);
	    outerAngleExpr = String.format("%d * delta_phi_S_II",i+1);
	    blockFeature.set("inputexpr", new String[]{"R_i", "R_o", innerAngleExpr, outerAngleExpr});
	    blockFeature.set("selkeepnoncontr", false);
	    magnetII1QBlockFeatures[i] = blockFeature;

	    // repeat the above procedure, but "mirroring" the angles for the second quadrant
	    tag2Q = geomFeatures.uniquetag(MAGNET_II_2Q_BLOCK_TAG);
	    magnetII2QBlockTags[i] = tag2Q;
	    blockFeature = geomFeatures.create(tag2Q, "PartInstance");
	    blockFeature.set("part",CYLINDER_BLOCK_PART_NAME);
	    blockFeature.label("Cylinder Block " + (i+1) + " - Magnet II - 2Q");
	    blockFeature.set("inputexpr", new String[]{"R_i", "R_o", "180[deg] - " + outerAngleExpr, "180[deg] - " + innerAngleExpr});
	    blockFeature.set("selkeepnoncontr", false);
	    magnetII2QBlockFeatures[i] = blockFeature;

	    
	}

	// build the iron block in region II
	ironIIBlockTag  = geomFeatures.uniquetag(IRON_II_BLOCK_TAG);
	ironIIBlockFeature = geomFeatures.create(ironIIBlockTag, "PartInstance");
	ironIIBlockFeature.set("part",CYLINDER_BLOCK_PART_NAME);
	ironIIBlockFeature.label("Cylinder Block 1 - Iron II - 1Q");
	ironIIBlockFeature.set("inputexpr", new String[]{"R_i", "R_o", outerAngleExpr, "180[deg] - " + outerAngleExpr});
	ironIIBlockFeature.set("selkeepnoncontr", false);

	// loop to build magnet blocks for region IV
	// see previous loop for explanations
	
	int nIV = Integer.parseInt(params.get("n_IV"));
	magnetIV1QBlockTags = new String[nII];
	magnetIV1QBlockFeatures = new GeomFeature[nII];

	magnetIV2QBlockTags = new String[nII];
	magnetIV2QBlockFeatures = new GeomFeature[nII];
	
	for (int i = 0; i < nIV; i++) {
	    tag1Q = geomFeatures.uniquetag(MAGNET_IV_1Q_BLOCK_TAG);
	    magnetIV1QBlockTags[i] = tag1Q;
	    
	    blockFeature = geomFeatures.create(tag1Q, "PartInstance");
	    blockFeature.set("part",CYLINDER_BLOCK_PART_NAME);
	    blockFeature.label("Cylinder Block " + i + " - Magnet IV - 1Q");
	    innerAngleExpr = String.format("%d * delta_phi_S_IV",i);
	    outerAngleExpr = String.format("%d * delta_phi_S_IV",i+1);
	    blockFeature.set("inputexpr", new String[]{"R_g", "R_s", innerAngleExpr, outerAngleExpr});
	    blockFeature.set("selkeepnoncontr", false);
	    magnetIV1QBlockFeatures[i] = blockFeature;

	    tag2Q = geomFeatures.uniquetag(MAGNET_IV_2Q_BLOCK_TAG);
	    magnetIV2QBlockTags[i] = tag2Q;
	    blockFeature = geomFeatures.create(tag2Q, "PartInstance");
	    blockFeature.set("part",CYLINDER_BLOCK_PART_NAME);
	    blockFeature.label("Cylinder Block " + i + " - Magnet IV - 2Q");
	    blockFeature.set("inputexpr", new String[]{"R_g", "R_s", "180[deg] - " + outerAngleExpr, "180[deg] - " + innerAngleExpr});
	    blockFeature.set("selkeepnoncontr", false);
	    magnetIV2QBlockFeatures[i] = blockFeature;
	}

	// build the iron block in region IV
	ironIVBlockTag  = geomFeatures.uniquetag(IRON_IV_BLOCK_TAG);
	ironIVBlockFeature = geomFeatures.create(ironIVBlockTag, "PartInstance");
	ironIVBlockFeature.set("part",CYLINDER_BLOCK_PART_NAME);
	ironIVBlockFeature.label("Cylinder Block 1 - Iron IV - 1Q");
	ironIVBlockFeature
	    .set("inputexpr", new String[]{"R_g", "R_s", outerAngleExpr, "180[deg] - " + outerAngleExpr});
	ironIVBlockFeature.set("selkeepnoncontr", false);

	// build the shaft region
	shaftTag = geomFeatures.uniquetag(SHAFT_TAG);
	shaftFeature = geomFeatures.create(shaftTag, "Circle");
	shaftFeature.label("Shaft Circle");
	shaftFeature.set("r", "R_i");
	shaftFeature.set("angle", "180");

	// build the air gap region
	airGapTag = geomFeatures.uniquetag(AIR_GAP_TAG);
	airGapFeature = geomFeatures.create(airGapTag,"PartInstance");
	airGapFeature.set("part",CYLINDER_SHELL_PART_NAME);
	airGapFeature.label("Air Gap Cylinder Shell");
	airGapFeature.set("inputexpr", new String[]{"R_o","R_g","180[deg]"});
	airGapFeature.set("selkeepnoncontr", false);

	// build the external environment
	environmentTag = geomFeatures.uniquetag(ENVIRONMENT_TAG);
	environmentFeature = geomFeatures.create(environmentTag,"PartInstance");
	environmentFeature.set("part",CYLINDER_SHELL_PART_NAME);
	environmentFeature.label("Environment Cylinder Shell");
	environmentFeature.set("inputexpr", new String[]{"R_s","R_e","180[deg]"});
	environmentFeature.set("selkeepnoncontr",false);

	
	geometry.run();
	geometry.run("fin");

	model.selection().create("sel1", "Explicit");
	model.selection("sel1").set(new int[]{15});
	model.selection().create("sel2", "Explicit");
	model.selection("sel2").set(new int[]{7});
	model.selection().create("sel3", "Explicit");
	model.selection("sel3").set(new int[]{6, 8, 12, 13, 14, 16, 17, 18, 22, 23});
	model.selection().create("sel4", "Explicit");
	model.selection("sel4").set(new int[]{3, 4, 5, 9, 10, 11, 19, 20, 21, 24, 25, 26});
	model.selection("sel1").label("Shaft");
	model.selection("sel2").label("Air gap");
	model.selection("sel3").label("Iron regions");
	model.selection("sel4").label("Magnet regions");

	model.material().create("mat1", "Common", COMPONENT_NAME);
	model.material().create("mat2", "Common", COMPONENT_NAME);
	model.material().create("mat3", "Common", COMPONENT_NAME);
	model.material("mat1").selection().set(new int[]{1, 2, 7, 15});
	model.material("mat1").propertyGroup("def").func().create("eta", "Piecewise");
	model.material("mat1").propertyGroup("def").func().create("Cp", "Piecewise");
	model.material("mat1").propertyGroup("def").func().create("rho", "Analytic");
	model.material("mat1").propertyGroup("def").func().create("k", "Piecewise");
	model.material("mat1").propertyGroup("def").func().create("cs", "Analytic");
	model.material("mat1").propertyGroup().create("RefractiveIndex", "Refractive index");
	model.material("mat2").selection().set(new int[]{2, 6, 8, 12, 13, 14, 16, 17, 18, 22, 23});
	model.material("mat2").propertyGroup().create("BHCurve", "BH curve");
	model.material("mat2").propertyGroup("BHCurve").func().create("BH", "Interpolation");
	model.material("mat2").propertyGroup().create("HBCurve", "HB curve");
	model.material("mat2").propertyGroup("HBCurve").func().create("HB", "Interpolation");
	model.material("mat2").propertyGroup().create("EffectiveBHCurve", "Effective BH curve");
	model.material("mat2").propertyGroup("EffectiveBHCurve").func().create("BHeff", "Interpolation");
	model.material("mat2").propertyGroup().create("EffectiveHBCurve", "Effective HB curve");
	model.material("mat2").propertyGroup("EffectiveHBCurve").func().create("HBeff", "Interpolation");
	model.material("mat3").selection().named("sel4");

	model.coordSystem().create("ie1", GEOMETRY_TAG, "InfiniteElement");
	model.coordSystem("ie1").selection().set(new int[]{1});

	model.physics().create("mfnc", "MagnetostaticsNoCurrents", GEOMETRY_TAG);
	model.physics("mfnc").create("mfc2", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc2").selection().set(new int[]{2, 6, 8, 12, 13, 14, 16, 17, 18, 22, 23});
	model.physics("mfnc").create("mfc3", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc3").selection().set(new int[]{21});
	model.physics("mfnc").create("mfc4", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc4").selection().set(new int[]{20});
	model.physics("mfnc").create("mfc5", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc5").selection().set(new int[]{19});
	model.physics("mfnc").create("mfc6", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc6").selection().set(new int[]{9});
	model.physics("mfnc").create("mfc7", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc7").selection().set(new int[]{10});
	model.physics("mfnc").create("mfc8", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc8").selection().set(new int[]{11});
	model.physics("mfnc").create("mfc9", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc9").selection().set(new int[]{26});
	model.physics("mfnc").create("mfc10", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc10").selection().set(new int[]{25});
	model.physics("mfnc").create("mfc11", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc11").selection().set(new int[]{24});
	model.physics("mfnc").create("mfc12", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc12").selection().set(new int[]{3});
	model.physics("mfnc").create("mfc13", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc13").selection().set(new int[]{4});
	model.physics("mfnc").create("mfc14", "MagneticFluxConservation", 2);
	model.physics("mfnc").feature("mfc14").selection().set(new int[]{5});

	model.mesh("mesh1").create("dis1", "Distribution");
	model.mesh("mesh1").create("ftri1", "FreeTri");
	model.mesh("mesh1").create("map1", "Map");
	model.mesh("mesh1").feature("dis1").selection().set(new int[]{1, 32});
	model.mesh("mesh1").feature("ftri1").selection().geom(GEOMETRY_TAG, 2);
	model.mesh("mesh1").feature("ftri1").selection()
	    .set(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26});
	model.mesh("mesh1").feature("map1").selection().geom(GEOMETRY_TAG, 2);
	model.mesh("mesh1").feature("map1").selection().set(new int[]{1});

	model.view("view1").axis().set("abstractviewrratio", "0.05000001937150955");
	model.view("view1").axis().set("abstractviewlratio", "-0.05000001937150955");
	model.view("view1").axis().set("abstractviewxscale", "1.7412134911864996E-4");
	model.view("view1").axis().set("abstractviewbratio", "-0.20146028697490692");
	model.view("view1").axis().set("xmax", "0.1679999679327011");
	model.view("view1").axis().set("xmin", "-0.1679999679327011");
	model.view("view1").axis().set("abstractviewyscale", "1.7412134911864996E-4");
	model.view("view1").axis().set("ymax", "0.18563207983970642");
	model.view("view1").axis().set("ymin", "-0.025632094591856003");
	model.view("view1").axis().set("abstractviewtratio", "0.20146028697490692");
	model.view("view2").axis().set("abstractviewrratio", "0.2330528348684311");
	model.view("view2").axis().set("abstractviewlratio", "-0.2330528348684311");
	model.view("view2").axis().set("abstractviewxscale", "6.460277654696256E-5");
	model.view("view2").axis().set("abstractviewbratio", "-0.049999963492155075");
	model.view("view2").axis().set("xmax", "0.05863120034337044");
	model.view("view2").axis().set("xmin", "-0.002834910526871681");
	model.view("view2").axis().set("abstractviewyscale", "6.460277654696256E-5");
	model.view("view2").axis().set("ymax", "0.04428674280643463");
	model.view("view2").axis().set("ymin", "0.002896810881793499");
	model.view("view2").axis().set("abstractviewtratio", "0.049999963492155075");
	model.view("view3").axis().set("abstractviewrratio", "0.05000002309679985");
	model.view("view3").axis().set("abstractviewlratio", "-0.05000002309679985");
	model.view("view3").axis().set("abstractviewxscale", "2.487050660420209E-4");
	model.view("view3").axis().set("abstractviewbratio", "-0.4331461191177368");
	model.view("view3").axis().set("xmax", "0.1111711636185646");
	model.view("view3").axis().set("xmin", "-0.1111711636185646");
	model.view("view3").axis().set("abstractviewyscale", "2.487050660420209E-4");
	model.view("view3").axis().set("ymax", "0.11510521918535233");
	model.view("view3").axis().set("ymin", "-0.03461522236466408");
	model.view("view3").axis().set("abstractviewtratio", "0.43314605951309204");

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
	model.material("mat2").label("Soft Iron (with losses)");
	model.material("mat2").set("family", "iron");
	model.material("mat2").propertyGroup("def")
	    .set("electricconductivity", new String[]{"1.12e7[S/m]", "0", "0", "0", "1.12e7[S/m]", "0", "0", "0", "1.12e7[S/m]"});
	model.material("mat2").propertyGroup("def")
	    .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
	model.material("mat2").propertyGroup("BHCurve").func("BH").set("extrap", "linear");
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
	model.material("mat2").propertyGroup("BHCurve").set("normB", "BH(normH[m/A])[T]");
	model.material("mat2").propertyGroup("BHCurve").addInput("magneticfield");
	model.material("mat2").propertyGroup("HBCurve").func("HB").set("extrap", "linear");
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
	model.material("mat2").propertyGroup("HBCurve").set("normH", "HB(normB[1/T])[A/m]");
	model.material("mat2").propertyGroup("HBCurve").addInput("magneticfluxdensity");
	model.material("mat2").propertyGroup("EffectiveBHCurve").func("BHeff").set("extrap", "linear");
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
	model.material("mat2").propertyGroup("EffectiveBHCurve").set("normBeff", "BHeff(normHeff[m/A])[T]");
	model.material("mat2").propertyGroup("EffectiveBHCurve").addInput("magneticfield");
	model.material("mat2").propertyGroup("EffectiveHBCurve").func("HBeff").set("extrap", "linear");
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
	model.material("mat2").propertyGroup("EffectiveHBCurve").set("normHeff", "HBeff(normBeff[1/T])[A/m]");
	model.material("mat2").propertyGroup("EffectiveHBCurve").addInput("magneticfluxdensity");
	model.material("mat3").label("Nd-Fe-B");
	model.material("mat3").propertyGroup("def")
	    .set("relpermeability", new String[]{"1.05", "0", "0", "0", "1.05", "0", "0", "0", "1.05"});

	model.coordSystem("ie1").label("External environment");
	model.coordSystem("ie1").set("ScalingType", "Cylindrical");

	model.physics("mfnc").prop("MeshControl").set("EnableMeshControl", "1");
	model.physics("mfnc").feature("mfc1").label("Magnetic Flux Conservation - Air regions");
	model.physics("mfnc").feature("mfc2").set("ConstitutiveRelationH", "BHCurve");
	model.physics("mfnc").feature("mfc2").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc2").label("Magnetic Flux Conservation - Iron regions");
	model.physics("mfnc").feature("mfc3").set("ConstitutiveRelationH", "RemanentFluxDensity");
	model.physics("mfnc").feature("mfc3")
	    .set("Br", new String[][]{{"B_rem*cos(0[deg])"}, {"B_rem*sin(0[deg])"}, {"0"}});
	model.physics("mfnc").feature("mfc3").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc3").label("Magnetic Flux Conservation - Magnet II 1 - 1Q");
	model.physics("mfnc").feature("mfc4").set("ConstitutiveRelationH", "RemanentFluxDensity");
	model.physics("mfnc").feature("mfc4")
	    .set("Br", new String[][]{{"B_rem*cos(-15[deg])"}, {"B_rem*sin(-15[deg])"}, {"0"}});
	model.physics("mfnc").feature("mfc4").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc4").label("Magnetic Flux Conservation - Magnet II 2 - 1Q");
	model.physics("mfnc").feature("mfc5").set("ConstitutiveRelationH", "RemanentFluxDensity");
	model.physics("mfnc").feature("mfc5")
	    .set("Br", new String[][]{{"B_rem*cos(-30[deg])"}, {"B_rem*sin(-30[deg])"}, {"0"}});
	model.physics("mfnc").feature("mfc5").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc5").label("Magnetic Flux Conservation - Magnet II 3 - 1Q");
	model.physics("mfnc").feature("mfc6").set("ConstitutiveRelationH", "RemanentFluxDensity");
	model.physics("mfnc").feature("mfc6")
	    .set("Br", new String[][]{{"B_rem*cos(0[deg])"}, {"B_rem*sin(0[deg])"}, {"0"}});
	model.physics("mfnc").feature("mfc6").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc6").label("Magnetic Flux Conservation - Magnet II 1 - 2Q");
	model.physics("mfnc").feature("mfc7").set("ConstitutiveRelationH", "RemanentFluxDensity");
	model.physics("mfnc").feature("mfc7")
	    .set("Br", new String[][]{{"B_rem*cos(15[deg])"}, {"B_rem*sin(15[deg])"}, {"0"}});
	model.physics("mfnc").feature("mfc7").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc7").label("Magnetic Flux Conservation - Magnet II 2 - 2Q");
	model.physics("mfnc").feature("mfc8").set("ConstitutiveRelationH", "RemanentFluxDensity");
	model.physics("mfnc").feature("mfc8")
	    .set("Br", new String[][]{{"B_rem*cos(30[deg])"}, {"B_rem*sin(30[deg])"}, {"0"}});
	model.physics("mfnc").feature("mfc8").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc8").label("Magnetic Flux Conservation - Magnet II 3 - 2Q");
	model.physics("mfnc").feature("mfc9").set("ConstitutiveRelationH", "RemanentFluxDensity");
	model.physics("mfnc").feature("mfc9")
	    .set("Br", new String[][]{{"B_rem*cos(0[deg])"}, {"B_rem*sin(0[deg])"}, {"0"}});
	model.physics("mfnc").feature("mfc9").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc9").label("Magnetic Flux Conservation - Magnet IV 1 - 1Q");
	model.physics("mfnc").feature("mfc10").set("ConstitutiveRelationH", "RemanentFluxDensity");
	model.physics("mfnc").feature("mfc10")
	    .set("Br", new String[][]{{"B_rem*cos(15[deg])"}, {"B_rem*sin(15[deg])"}, {"0"}});
	model.physics("mfnc").feature("mfc10").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc10").label("Magnetic Flux Conservation - Magnet IV 2 - 1Q");
	model.physics("mfnc").feature("mfc11").set("ConstitutiveRelationH", "RemanentFluxDensity");
	model.physics("mfnc").feature("mfc11")
	    .set("Br", new String[][]{{"B_rem*cos(30[deg])"}, {"B_rem*sin(30[deg])"}, {"0"}});
	model.physics("mfnc").feature("mfc11").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc11").label("Magnetic Flux Conservation - Magnet IV 3 - 1Q");
	model.physics("mfnc").feature("mfc12").set("ConstitutiveRelationH", "RemanentFluxDensity");
	model.physics("mfnc").feature("mfc12")
	    .set("Br", new String[][]{{"B_rem*cos(0[deg])"}, {"B_rem*sin(0[deg])"}, {"0"}});
	model.physics("mfnc").feature("mfc12").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc12").label("Magnetic Flux Conservation - Magnet IV 1 - 2Q");
	model.physics("mfnc").feature("mfc13").set("ConstitutiveRelationH", "RemanentFluxDensity");
	model.physics("mfnc").feature("mfc13")
	    .set("Br", new String[][]{{"B_rem*cos(-15[deg])"}, {"B_rem*sin(-15[deg])"}, {"0"}});
	model.physics("mfnc").feature("mfc13").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc13").label("Magnetic Flux Conservation - Magnet IV 2 - 2Q");
	model.physics("mfnc").feature("mfc14").set("ConstitutiveRelationH", "RemanentFluxDensity");
	model.physics("mfnc").feature("mfc14")
	    .set("Br", new String[][]{{"B_rem*cos(-30[deg])"}, {"B_rem*sin(-30[deg])"}, {"0"}});
	model.physics("mfnc").feature("mfc14").set("materialType", "from_mat");
	model.physics("mfnc").feature("mfc14").label("Magnetic Flux Conservation - Magnet IV 3 - 2Q");

	model.mesh("mesh1").feature("size").set("hauto", 3);
	model.mesh("mesh1").feature("size").set("custom", "on");
	model.mesh("mesh1").feature("size").set("hnarrow", "5");
	model.mesh("mesh1").feature("dis1").set("type", "predefined");
	model.mesh("mesh1").feature("map1").set("adjustedgdistr", true);
	model.mesh("mesh1").run();

	model.study().create("std1");
	model.study("std1").create("stat", "Stationary");

	model.sol().create("sol1");
	model.sol("sol1").study("std1");
	model.sol("sol1").attach("std1");
	model.sol("sol1").create("st1", "StudyStep");
	model.sol("sol1").create("v1", "Variables");
	model.sol("sol1").create("s1", "Stationary");

	return model;
    }

    public static Model run2(Model model) {
	model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
	model.sol("sol1").feature("s1").create("i1", "Iterative");
	model.sol("sol1").feature("s1").feature().remove("fcDef");

	model.result().dataset().create("dset2", "Solution");
	model.result().dataset().create("pc1", "ParCurve2D");
	model.result().dataset().create("dset3", "Solution");
	model.result().dataset("dset1").selection().geom(GEOMETRY_TAG, 2);
	model.result().dataset("dset1").selection().set(new int[]{7});
	model.result().dataset("dset2").selection().geom(GEOMETRY_TAG, 2);
	model.result().dataset("dset2").selection().set(new int[]{3, 4, 5, 9, 10, 11, 19, 20, 21, 24, 25, 26});
	model.result().dataset("dset3").selection().geom(GEOMETRY_TAG, 2);
	model.result().dataset("dset3").selection()
	    .set(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26});
	model.result().create("pg2", "PlotGroup2D");
	model.result().create("pg3", "PlotGroup2D");
	model.result().create("pg4", "PlotGroup1D");
	model.result().create("pg5", "PlotGroup2D");
	model.result("pg2").create("surf1", "Surface");
	model.result("pg3").set("data", "dset2");
	model.result("pg3").create("arws1", "ArrowSurface");
	model.result("pg3").create("surf1", "Surface");
	model.result("pg3").create("arws2", "ArrowSurface");
	model.result("pg4").create("lngr1", "LineGraph");
	model.result("pg5").set("data", "dset3");
	model.result("pg5").create("str1", "Streamline");
	model.result("pg5").create("arws1", "ArrowSurface");
	model.result("pg5").feature("str1").selection()
	    .set(new int[]{35, 36, 37, 39, 41, 42, 43, 45, 46, 72, 74, 75, 76, 77, 78, 80, 81, 82});

	model.sol("sol1").attach("std1");
	model.sol("sol1").runAll();

	model.result().dataset("dset1").label("Air gap");
	model.result().dataset("dset2").label("Magnets");
	model.result().dataset("pc1").label("Air gap central line");
	model.result().dataset("pc1").set("parmax1", "pi");
	model.result().dataset("pc1").set("exprx", "(R_o+h_gap/2)*cos(s)");
	model.result().dataset("pc1").set("expry", "(R_o+h_gap/2)*sin(s)");
	model.result().dataset("dset3").label("Whole");
	model.result("pg2").label("Air gap");
	model.result("pg2").feature("surf1").label("B");
	model.result("pg2").feature("surf1").set("unit", "T");
	model.result("pg2").feature("surf1").set("expr", "mfnc.normB");
	model.result("pg2").feature("surf1").set("descr", "Magnetic flux density norm");
	model.result("pg2").feature("surf1").set("resolution", "normal");
	model.result("pg3").label("Magnets");
	model.result("pg3").feature("arws1").label("B_rem");
	model.result("pg3").feature("arws1").set("scale", "0.0077221241233942925");
	model.result("pg3").feature("arws1").set("arrowbase", "center");
	model.result("pg3").feature("arws1").set("expr", new String[]{"mfnc.Brx", "mfnc.Bry"});
	model.result("pg3").feature("arws1").set("descr", "Remanent flux density");
	model.result("pg3").feature("arws1").set("scaleactive", false);
	model.result("pg3").feature("surf1").label("Psi");
	model.result("pg3").feature("surf1").set("unit", "kPa");
	model.result("pg3").feature("surf1")
	    .set("expr", "abs((mfnc.Bx*mfnc.Brx+mfnc.By*mfnc.Bry)/mfnc.normBr)*abs((mfnc.Hx*mfnc.Brx+mfnc.Hy*mfnc.Bry)/mfnc.normBr)");
	model.result("pg3").feature("surf1").set("rangedatamax", "409");
	model.result("pg3").feature("surf1")
	    .set("descr", "abs((mfnc.Bx*mfnc.Brx+mfnc.By*mfnc.Bry)/mfnc.normBr)*abs((mfnc.Hx*mfnc.Brx+mfnc.Hy*mfnc.Bry)/mfnc.normBr)");
	model.result("pg3").feature("surf1").set("rangecoloractive", "on");
	model.result("pg3").feature("surf1").set("rangedataactive", "on");
	model.result("pg3").feature("surf1").set("rangecolormax", "409");
	model.result("pg3").feature("surf1").set("resolution", "normal");
	model.result("pg3").feature("arws2").label("H");
	model.result("pg3").feature("arws2").set("scale", "0.010592406010573617");
	model.result("pg3").feature("arws2").set("descractive", true);
	model.result("pg3").feature("arws2").set("arrowbase", "center");
	model.result("pg3").feature("arws2").set("expr", new String[]{"mu0_const*mfnc.Hx", "mu0_const*mfnc.Hy"});
	model.result("pg3").feature("arws2").set("descr", "mu0*H");
	model.result("pg3").feature("arws2").set("color", "black");
	model.result("pg3").feature("arws2").set("scaleactive", false);
	model.result("pg4").label("Air gap central line");
	model.result("pg4").set("data", "pc1");
	model.result("pg4").set("ylabel", "Magnetic flux density norm (T)");
	model.result("pg4").set("xlabel", "Arc length");
	model.result("pg4").set("xlabelactive", false);
	model.result("pg4").set("ylabelactive", false);
	model.result("pg4").feature("lngr1").set("unit", "T");
	model.result("pg4").feature("lngr1").set("descr", "Magnetic flux density norm");
	model.result("pg4").feature("lngr1").set("expr", "mfnc.normB");
	model.result("pg4").feature("lngr1").set("resolution", "normal");
	model.result("pg5").label("Whole domain");
	model.result("pg5").feature("str1").active(false);
	model.result("pg5").feature("str1").set("color", "blue");
	model.result("pg5").feature("str1").set("resolution", "normal");
	model.result("pg5").feature("arws1").label("B");
	model.result("pg5").feature("arws1").set("scale", "0.0048866785798066924");
	model.result("pg5").feature("arws1").set("arrowbase", "center");
	model.result("pg5").feature("arws1").set("scaleactive", false);

	return model;
    }

    public static void main(String[] args) {
	model = run();
	run2(model);
    }

}
