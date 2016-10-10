/*
 * TeslaMax.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;
import com.comsol.model.physics.*;
import java.io.*;

/** Model exported on Aug 8 2016, 09:24 by COMSOL 5.2.1.152. */
public class TeslaMax {

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
    private static final String FLUX_CONCENTRATOR_TAG = "flux_concentrator";
    private static final String ENVIRONMENT_TAG = "environment";

    private static final String SELECTION_DOMAIN_SUFFIX = "dom";

    private static final String SHAFT_SELECTION_TAG = "shaft_selection";
    private static final String MAGNETS_SELECTION_TAG = "magnets_selection";
    private static final String IRON_SELECTION_TAG = "iron_selection";
    private static final String AIR_GAP_SELECTION_TAG = "air_gap_selection";
    private static final String AIR_GAP_HIGH_SELECTION_TAG = "air_gap_high_selection";
    private static final String AIR_GAP_LOW_SELECTION_TAG = "air_gap_low_selection";
    private static final String ENVIRONMENT_SELECTION_TAG = "environment_selection";
    private static final String ENVIRONMENT_HORIZONTAL_BOUNDARY_SELECTION_TAG = "environment_horization_boundary_selection";
    private static final String AIR_REGIONS_SELECTION_TAG = "air_regions_selection";
    private static final String CIRCUIT_REGIONS_SELECTION_TAG = "magnetic_circuit_regions_seletion";

    private static final String PARAMETER_FILE_NAME = "params.txt";

    private static final String RESULTS_TABLE_TAG = "results_table";
           
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
    private static String airGapHigh1QTag;
    private static String airGapHigh2QTag;
    private static String airGapLow1QTag;
    private static String airGapLow2QTag;
    private static String fluxConcentratorTag;
    private static String environmentTag;

    private static GeomFeature[] magnetII1QBlockFeatures;
    private static GeomFeature[] magnetII2QBlockFeatures;
    private static GeomFeature[] magnetIV1QBlockFeatures;
    private static GeomFeature[] magnetIV2QBlockFeatures;
    private static GeomFeature ironIIBlockFeature;
    private static GeomFeature ironIVBlockFeature;
    private static GeomFeature shaftFeature;
    private static GeomFeature airGapHigh1QFeature;
    private static GeomFeature airGapHigh2QFeature;
    private static GeomFeature airGapLow1QFeature;
    private static GeomFeature airGapLow2QFeature;
    private static GeomFeature fluxConcentratorFeature;
    private static GeomFeature environmentFeature;

    private static Material airMaterial;
    private static Material ironMaterial;
    private static Material magnetMaterial;

    private static Physics mfncPhysics;

    private static int nII;
    private static int nIV;

    private static MeshSequence modelMesh;

    private static Results modelResults;
    private static DatasetFeatureList modelDataSets;

    private static TableFeature resultsTable;

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

	part.run("dif1");
	part.create(SELECTION_DOMAIN_SUFFIX,"ExplicitSelection");
	part.feature(SELECTION_DOMAIN_SUFFIX).selection("selection").set("dif1",new int[]{1});
	part.run(SELECTION_DOMAIN_SUFFIX);

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

	part.run("dif1");
	part.create(SELECTION_DOMAIN_SUFFIX,"ExplicitSelection");
	part.feature(SELECTION_DOMAIN_SUFFIX).selection("selection").set("dif1",new int[]{1});
	part.run(SELECTION_DOMAIN_SUFFIX);

	return part;
    }

    private static GeomFeature buildCylinderBlock(String tag, String label, String[] inputExpression) {

	GeomFeature cylinderBlock;

	cylinderBlock = geomFeatures.create(tag,"PartInstance");
	cylinderBlock.set("part",CYLINDER_BLOCK_PART_NAME);
	cylinderBlock.label(label);
	cylinderBlock.set("inputexpr",inputExpression);
	cylinderBlock.set("selkeepnoncontr", false);

	String selTag = tag + "_" + SELECTION_DOMAIN_SUFFIX;
	cylinderBlock.setEntry("selkeepdom",selTag,"on");

	return cylinderBlock;
	
    }

    private static GeomFeature buildIronBlock(String magnetRegion) {

	String tag = "";
	String label = "";
	String[] expression = null;

	GeomFeature block;

	if (magnetRegion.equals("II")) {
	    
	    tag = geomFeatures.uniquetag(IRON_II_BLOCK_TAG);
	    ironIIBlockTag = tag;
	    label = "Cylinder Block - Iron II";
	    expression = new String[]{"R_i", "R_o", "phi_S_II", "180[deg] - phi_S_II"};
	    
	} else if (magnetRegion.equals("IV")) {

	    tag = geomFeatures.uniquetag(IRON_IV_BLOCK_TAG);
	    ironIVBlockTag = tag;
	    label = "Cylinder Block - Iron IV";
	    expression = new String[]{"R_g", "R_s", "phi_S_IV", "180[deg] - phi_S_IV"};
	}

	block = buildCylinderBlock(tag,label,expression);
	return block;


    }

    private static  GeomFeature buildMagnetBlock(int index, String magnetRegion, String quadrant) {

	GeomFeature blockFeature;
	String tag = "";
	String label = "";
	String innerAngleExpr = "";
	String outerAngleExpr = "";
	String[] expression = null;

	if (magnetRegion.equals("II")) {

	    if (quadrant.equals("1Q")) {

		tag = geomFeatures.uniquetag(MAGNET_II_1Q_BLOCK_TAG);
		label = "Cylinder Block " + (index+1) + " - Magnet II - 1Q";
		magnetII1QBlockTags[index] = tag;

		// the cylinder block is builting sweeping phi from innerAngleExpr to outerAngleExpr
		innerAngleExpr = String.format("%d * delta_phi_S_II",index);
		outerAngleExpr = String.format("%d * delta_phi_S_II",index+1);
		expression = new String[]{"R_i", "R_o", innerAngleExpr, outerAngleExpr};
	    
	    } else if (quadrant.equals("2Q")) {

		tag = geomFeatures.uniquetag(MAGNET_II_2Q_BLOCK_TAG);
		label = "Cylinder Block " + (index+1) + " - Magnet II - 2Q";
		magnetII2QBlockTags[index] = tag;

		// the cylinder block is builting sweeping phi from innerAngleExpr to outerAngleExpr
		innerAngleExpr = String.format("180[deg] - %d * delta_phi_S_II",index+1);
		outerAngleExpr = String.format("180[deg] - %d * delta_phi_S_II",index);
		expression = new String[]{"R_i", "R_o", innerAngleExpr, outerAngleExpr};
		
	    }

	} else if (magnetRegion.equals("IV")) {
	    
	    if (quadrant.equals("1Q")) {
		
		tag = geomFeatures.uniquetag(MAGNET_IV_1Q_BLOCK_TAG);
		label = "Cylinder Block " + (index+1) + " - Magnet IV - 1Q";
		magnetIV1QBlockTags[index] = tag;

		// the cylinder block is builting sweeping phi from innerAngleExpr to outerAngleExpr
		innerAngleExpr = String.format("%d * delta_phi_S_IV",index);
		outerAngleExpr = String.format("%d * delta_phi_S_IV",index+1);
		expression = new String[]{"R_g", "R_s", innerAngleExpr, outerAngleExpr};
	    
	    } else if (quadrant.equals("2Q")) {

		tag = geomFeatures.uniquetag(MAGNET_IV_2Q_BLOCK_TAG);
		label = "Cylinder Block " + (index+1) + " - Magnet IV - 2Q";
		magnetIV2QBlockTags[index] = tag;

		// the cylinder block is builting sweeping phi from innerAngleExpr to outerAngleExpr
		innerAngleExpr = String.format("180[deg] - %d * delta_phi_S_IV",index+1);
		outerAngleExpr = String.format("180[deg] - %d * delta_phi_S_IV",index);
		expression = new String[]{"R_g", "R_s", innerAngleExpr, outerAngleExpr};
		
	    }

       
	}

	blockFeature = buildCylinderBlock(tag,label, expression);
	return blockFeature;
	
    }

    private static GeomFeature buildShaftCircle() {

	GeomFeature feature;
	shaftTag = geomFeatures.uniquetag(SHAFT_TAG);

	String label = "Circle";

	feature = geomFeatures.create(shaftTag,"Circle");
	feature.label(label);
	feature.set("r","R_i");
	feature.set("angle","180[deg]");
	feature.set("selresult","on");

	return feature;
    }

    private static GeomFeature buildCylinderShell(String tag, String label, String[] inputExpression) {

	GeomFeature cylinderShell;

	cylinderShell = geomFeatures.create(tag,"PartInstance");
	cylinderShell.set("part",CYLINDER_SHELL_PART_NAME);
	cylinderShell.label(label);
	cylinderShell.set("inputexpr",inputExpression);
	cylinderShell.set("selkeepnoncontr", false);

	String selTag = tag + "_" + SELECTION_DOMAIN_SUFFIX;
	cylinderShell.setEntry("selkeepdom",selTag,"on");

	return cylinderShell;
	
    }

    private static GeomFeature buildAirGapBlock(String region, String quadrant) {

	String label = "Air Gap Cylinder Block " + region + " Field  " + quadrant;

	// by convention, the high field region will be delimited by the
	// mean angle between phi_S_II and phi_S_IV
	String angle = String.format("(0.5*(%s + %s))",params.get("phi_S_II"),params.get("phi_S_IV"));
	String angle_start_expr = "";
	String angle_end_expr = "";
	String tag = "";

	if (region.equals("High")) {

	    if (quadrant.equals("1Q")) {

		airGapHigh1QTag = geomFeatures.uniquetag(AIR_GAP_TAG);
		tag = airGapHigh1QTag;
		angle_start_expr = "0";
		angle_end_expr = String.format("%s", angle);
	    
	    }
	    else {

		airGapHigh2QTag = geomFeatures.uniquetag(AIR_GAP_TAG);
		tag = airGapHigh2QTag;
		angle_start_expr = String.format("180[deg]-%s",angle);
		angle_end_expr = "180[deg]";
	    }
	    
	} else {
	    
	    if (quadrant.equals("1Q")) {

		airGapLow1QTag = geomFeatures.uniquetag(AIR_GAP_TAG);
		tag = airGapLow1QTag;
		angle_start_expr = String.format("%s", angle);
		angle_end_expr = "90[deg]";
	    
	    }
	    else {

		airGapLow2QTag = geomFeatures.uniquetag(AIR_GAP_TAG);
		tag = airGapLow2QTag;
		angle_start_expr = "90[deg]";
		angle_end_expr = String.format("180[deg]-%s",angle);
	    }
	}

	
	
	String[] expression = new String[]{"R_o", "R_g", angle_start_expr,angle_end_expr};
	
	GeomFeature block = buildCylinderBlock(tag,label,expression);
	return block;

    }



    private static GeomFeature buildFluxConcentratorShell() {

	GeomFeature shell;
	
	fluxConcentratorTag = geomFeatures.uniquetag(FLUX_CONCENTRATOR_TAG);
	String label = "Flux Concentrator Shell";
	String[] expression = new String[]{"R_s","R_c","180[deg]"};

	shell = buildCylinderShell(fluxConcentratorTag,label,expression);
	return shell;

    }

    private static GeomFeature buildEnvironmentShell() {
	    
	GeomFeature shell;
	
	environmentTag = geomFeatures.uniquetag(ENVIRONMENT_TAG);
	String label = "Environment Cylinder Shell";
	String[] expression = new String[]{"R_c","R_e","180[deg]"};

	shell = buildCylinderShell(environmentTag,label,expression);
	return shell;

    }

    private static void buildGeometry(){

	geomFeatures = geometry.feature();

	// loop to build the magnet II blocks

	magnetII1QBlockTags = new String[nII];
	magnetII1QBlockFeatures = new GeomFeature[nII];

	magnetII2QBlockTags = new String[nII];
	magnetII2QBlockFeatures = new GeomFeature[nII];

	for (int i = 0; i < nII; i++) {
	    
	    magnetII1QBlockFeatures[i] = buildMagnetBlock(i,"II","1Q");
	    magnetII2QBlockFeatures[i] = buildMagnetBlock(i,"II","2Q");
	    
	}

	// build the iron block in region II
	ironIIBlockFeature = buildIronBlock("II");

	// loop to build magnet blocks for region IV
	// see previous loop for explanations
	
	magnetIV1QBlockTags = new String[nIV];
	magnetIV1QBlockFeatures = new GeomFeature[nIV];

	magnetIV2QBlockTags = new String[nIV];
	magnetIV2QBlockFeatures = new GeomFeature[nIV];
	
	for (int i = 0; i < nIV; i++) {

	    magnetIV1QBlockFeatures[i] = buildMagnetBlock(i,"IV","1Q");
	    magnetIV2QBlockFeatures[i] = buildMagnetBlock(i,"IV","2Q");

	}

	// build the iron block in region IV
	ironIVBlockFeature = buildIronBlock("IV");
	
	// build the shaft region
	shaftFeature = buildShaftCircle();

	
	// build the air gap region
	airGapHigh1QFeature = buildAirGapBlock("High","1Q");
	airGapLow1QFeature = buildAirGapBlock("Low","1Q");
	airGapHigh2QFeature = buildAirGapBlock("High","2Q");
	airGapLow2QFeature = buildAirGapBlock("Low","2Q");

	// build the flux concentrator region
	fluxConcentratorFeature = buildFluxConcentratorShell();

	// build the external environment
	environmentFeature = buildEnvironmentShell();
	
	geometry.run();
	geometry.run("fin");
    }

    private static String getDomainSelectionTag(String ftag){

	String suffix = "";
	
	if (ftag.equals(shaftTag)) {
	    suffix = "dom";
	} else {
	    suffix = SELECTION_DOMAIN_SUFFIX;
	}

	String tag = GEOMETRY_TAG + "_" + ftag + "_" + suffix;
	return tag;
	}

    private static int[] getDomainEntities(String ftag){

	String domainTag = getDomainSelectionTag(ftag);
	return model.selection(domainTag).entities(2);
	}

    private static void createSelections() {

	String selTag;
	String domTag;
	int[] entities;
	
	
	model.selection().create(SHAFT_SELECTION_TAG, "Explicit");
	model.selection(SHAFT_SELECTION_TAG).label("Shaft");
	entities = getDomainEntities(shaftTag);
	model.selection(SHAFT_SELECTION_TAG).set(entities);

	model.selection().create(AIR_GAP_HIGH_SELECTION_TAG, "Explicit");
	model.selection(AIR_GAP_HIGH_SELECTION_TAG).label("Air Gap High");
	entities = getDomainEntities(airGapHigh1QTag);
	model.selection(AIR_GAP_HIGH_SELECTION_TAG).set(entities);

	model.selection().create(AIR_GAP_LOW_SELECTION_TAG, "Explicit");
	model.selection(AIR_GAP_LOW_SELECTION_TAG).label("Air Gap Low");
	entities = getDomainEntities(airGapLow1QTag);
	model.selection(AIR_GAP_LOW_SELECTION_TAG).set(entities);

	
	model.selection().create(AIR_GAP_SELECTION_TAG, "Explicit");
	model.selection(AIR_GAP_SELECTION_TAG).label("Air Gap");
	model.selection(AIR_GAP_SELECTION_TAG).add(getDomainEntities(airGapHigh1QTag));
	model.selection(AIR_GAP_SELECTION_TAG).add(getDomainEntities(airGapHigh2QTag));
	model.selection(AIR_GAP_SELECTION_TAG).add(getDomainEntities(airGapLow1QTag));
	model.selection(AIR_GAP_SELECTION_TAG).add(getDomainEntities(airGapLow2QTag));

	model.selection().create(ENVIRONMENT_SELECTION_TAG, "Explicit");
	model.selection(ENVIRONMENT_SELECTION_TAG).label("Environment");
	entities = getDomainEntities(environmentTag);
	model.selection(ENVIRONMENT_SELECTION_TAG).set(entities);

	// for the 'infinite element' configuration of the environment layer,
	// we need to access its horizontal boundary
	// we do this by selecting the boundary that is at coorfinates (+-x0,0),
	// where x0 is between the flux concentrator and the final boundary
	SelectionFeature environmentBoundarySelection = model.selection().create(ENVIRONMENT_HORIZONTAL_BOUNDARY_SELECTION_TAG,"Union");

	double R_c = params.evaluate("R_c");
	double R_e = params.evaluate("R_e");
	double posx = (R_c+R_e)/2;
	double r = 0.001;

	SelectionFeature environmentBoundarySelectionRight = model.selection().create("environment_sel_right","Ball");
	environmentBoundarySelectionRight.set("entitydim",1);
	environmentBoundarySelectionRight.set("posx",posx);
	environmentBoundarySelectionRight.set("r",r);
	environmentBoundarySelectionRight.label("Environment Horizontal Right Boundary");
	
	SelectionFeature environmentBoundarySelectionLeft = model.selection().create("environment_sel_left","Ball");
	environmentBoundarySelectionLeft.set("entitydim",1);
	environmentBoundarySelectionLeft.set("posx",-posx);
	environmentBoundarySelectionLeft.set("r",r);
	environmentBoundarySelectionRight.label("Environment Horizontal Left Boundary");

	
	environmentBoundarySelection.label("Environment boundary");
	environmentBoundarySelection.set("entitydim",1);
	environmentBoundarySelection.set("input",new String[]{"environment_sel_right","environment_sel_left"});

	model.selection().create(MAGNETS_SELECTION_TAG, "Explicit");
	model.selection(MAGNETS_SELECTION_TAG).label("Magnets region");
	for (String ftag : magnetII1QBlockTags) {
	    model.selection(MAGNETS_SELECTION_TAG).add(getDomainEntities(ftag));
	}

	for (String ftag : magnetII2QBlockTags) {
	    model.selection(MAGNETS_SELECTION_TAG).add(getDomainEntities(ftag));
	}

	for (String ftag : magnetIV1QBlockTags) {
	    model.selection(MAGNETS_SELECTION_TAG).add(getDomainEntities(ftag));
	}

	for (String ftag : magnetIV2QBlockTags) {
	    model.selection(MAGNETS_SELECTION_TAG).add(getDomainEntities(ftag));
	}

	model.selection().create(IRON_SELECTION_TAG,"Explicit");
	model.selection(IRON_SELECTION_TAG).label("Iron region");
	model.selection(IRON_SELECTION_TAG).add(getDomainEntities(ironIIBlockTag));
	model.selection(IRON_SELECTION_TAG).add(getDomainEntities(ironIVBlockTag));
	model.selection(IRON_SELECTION_TAG).add(getDomainEntities(fluxConcentratorTag));

	model.selection().create(AIR_REGIONS_SELECTION_TAG,"Explicit");
	model.selection(AIR_REGIONS_SELECTION_TAG).label("Air regions");
	model.selection(AIR_REGIONS_SELECTION_TAG).add(getDomainEntities(shaftTag));
	model.selection(AIR_REGIONS_SELECTION_TAG).add(getDomainEntities(environmentTag));
	model.selection(AIR_REGIONS_SELECTION_TAG).add(model.selection(AIR_GAP_SELECTION_TAG).entities(2));

	// configure selection for the "circuit" (everything but the environment)
	model.selection().create(CIRCUIT_REGIONS_SELECTION_TAG,"Explicit");
	model.selection(CIRCUIT_REGIONS_SELECTION_TAG).label("Magnetic circuit regions (except environment)");
	model.selection(CIRCUIT_REGIONS_SELECTION_TAG).add(getDomainEntities(shaftTag));
	model.selection(CIRCUIT_REGIONS_SELECTION_TAG).add(model.selection(AIR_GAP_SELECTION_TAG).entities(2));
	for (String ftag : magnetII1QBlockTags) {
	    model.selection(CIRCUIT_REGIONS_SELECTION_TAG).add(getDomainEntities(ftag));
	}

	for (String ftag : magnetII2QBlockTags) {
	    model.selection(CIRCUIT_REGIONS_SELECTION_TAG).add(getDomainEntities(ftag));
	}

	for (String ftag : magnetIV1QBlockTags) {
	    model.selection(CIRCUIT_REGIONS_SELECTION_TAG).add(getDomainEntities(ftag));
	}

	for (String ftag : magnetIV2QBlockTags) {
	    model.selection(CIRCUIT_REGIONS_SELECTION_TAG).add(getDomainEntities(ftag));
	}
	model.selection(CIRCUIT_REGIONS_SELECTION_TAG).add(getDomainEntities(ironIIBlockTag));
	model.selection(CIRCUIT_REGIONS_SELECTION_TAG).add(getDomainEntities(ironIVBlockTag));
	model.selection(CIRCUIT_REGIONS_SELECTION_TAG).add(getDomainEntities(fluxConcentratorTag));



    }

    private static void createProbes(){

	ProbeFeatureList modelProbes = model.probe();

	resultsTable = model.result().table().create(RESULTS_TABLE_TAG,"Table");

	ProbeFeature airGapHighProbe = modelProbes.create("prb1","Domain");
	airGapHighProbe.model(COMPONENT_NAME);
	airGapHighProbe.selection().named(AIR_GAP_HIGH_SELECTION_TAG);
	airGapHighProbe.set("expr","mfnc.normB");
	airGapHighProbe.set("unit","T");
	airGapHighProbe.set("table",RESULTS_TABLE_TAG);
	airGapHighProbe.label("Maximum Field");
	airGapHighProbe.genResult(null);
	
	ProbeFeature airGapLowProbe = modelProbes.create("prb2","Domain");
	airGapLowProbe.model(COMPONENT_NAME);
	airGapLowProbe.selection().named(AIR_GAP_LOW_SELECTION_TAG);
	airGapLowProbe.set("expr","mfnc.normB");
	airGapLowProbe.set("unit","T");
	airGapLowProbe.set("table",RESULTS_TABLE_TAG);
	airGapLowProbe.label("Minimum Field");
	airGapLowProbe.genResult(null);

	ProbeFeature airGapAreaProbe = modelProbes.create("prb3","Domain");
	airGapAreaProbe.model(COMPONENT_NAME);
	airGapAreaProbe.selection().named(AIR_GAP_SELECTION_TAG);
	airGapAreaProbe.set("expr","2");
	airGapAreaProbe.set("unit","m^2");
	airGapAreaProbe.set("type","integral");
	airGapAreaProbe.set("table",RESULTS_TABLE_TAG);
	airGapAreaProbe.label("Air Gap Area");
	airGapAreaProbe.genResult(null);
	
	ProbeFeature magnetAreaProbe = modelProbes.create("prb4","Domain");
	magnetAreaProbe.model(COMPONENT_NAME);
	magnetAreaProbe.selection().named(MAGNETS_SELECTION_TAG);
	magnetAreaProbe.set("expr","2");
	magnetAreaProbe.set("unit","m^2");
	magnetAreaProbe.set("type","integral");
	magnetAreaProbe.set("table",RESULTS_TABLE_TAG);
	magnetAreaProbe.label("Magnets Area");
	magnetAreaProbe.genResult(null);

    }

    private static void configureAirMaterial(Material mat){

	mat.label("Air");
	mat.set("family", "air");

	mat.propertyGroup("def").func().create("eta", "Piecewise");
	mat.propertyGroup("def").func().create("Cp", "Piecewise");
	mat.propertyGroup("def").func().create("rho", "Analytic");
	mat.propertyGroup("def").func().create("k", "Piecewise");
	mat.propertyGroup("def").func().create("cs", "Analytic");
	mat.propertyGroup().create("RefractiveIndex", "Refractive index");

	mat.propertyGroup("def").func("eta")
	    .set("pieces", new String[][]{{"200.0", "1600.0", "-8.38278E-7+8.35717342E-8*T^1-7.69429583E-11*T^2+4.6437266E-14*T^3-1.06585607E-17*T^4"}});
	mat.propertyGroup("def").func("eta").set("arg", "T");
	mat.propertyGroup("def").func("Cp")
	    .set("pieces", new String[][]{{"200.0", "1600.0", "1047.63657-0.372589265*T^1+9.45304214E-4*T^2-6.02409443E-7*T^3+1.2858961E-10*T^4"}});
	mat.propertyGroup("def").func("Cp").set("arg", "T");
	mat.propertyGroup("def").func("rho").set("args", new String[]{"pA", "T"});
	mat.propertyGroup("def").func("rho").set("expr", "pA*0.02897/8.314/T");
	mat.propertyGroup("def").func("rho").set("dermethod", "manual");
	mat.propertyGroup("def").func("rho")
	    .set("plotargs", new String[][]{{"pA", "0", "1"}, {"T", "0", "1"}});
	mat.propertyGroup("def").func("rho")
	    .set("argders", new String[][]{{"pA", "d(pA*0.02897/8.314/T,pA)"}, {"T", "d(pA*0.02897/8.314/T,T)"}});
	mat.propertyGroup("def").func("k")
	    .set("pieces", new String[][]{{"200.0", "1600.0", "-0.00227583562+1.15480022E-4*T^1-7.90252856E-8*T^2+4.11702505E-11*T^3-7.43864331E-15*T^4"}});
	mat.propertyGroup("def").func("k").set("arg", "T");
	mat.propertyGroup("def").func("cs").set("args", new String[]{"T"});
	mat.propertyGroup("def").func("cs").set("expr", "sqrt(1.4*287*T)");
	mat.propertyGroup("def").func("cs").set("dermethod", "manual");
	mat.propertyGroup("def").func("cs").set("plotargs", new String[][]{{"T", "0", "1"}});
	mat.propertyGroup("def").func("cs")
	    .set("argders", new String[][]{{"T", "d(sqrt(1.4*287*T),T)"}});
	mat.propertyGroup("def")
	    .set("relpermeability", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
	mat.propertyGroup("def")
	    .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
	mat.propertyGroup("def").set("dynamicviscosity", "eta(T[1/K])[Pa*s]");
	mat.propertyGroup("def").set("ratioofspecificheat", "1.4");
	mat.propertyGroup("def")
	    .set("electricconductivity", new String[]{"0[S/m]", "0", "0", "0", "0[S/m]", "0", "0", "0", "0[S/m]"});
	mat.propertyGroup("def").set("heatcapacity", "Cp(T[1/K])[J/(kg*K)]");
	mat.propertyGroup("def").set("density", "rho(pA[1/Pa],T[1/K])[kg/m^3]");
	mat.propertyGroup("def")
	    .set("thermalconductivity", new String[]{"k(T[1/K])[W/(m*K)]", "0", "0", "0", "k(T[1/K])[W/(m*K)]", "0", "0", "0", "k(T[1/K])[W/(m*K)]"});
	mat.propertyGroup("def").set("soundspeed", "cs(T[1/K])[m/s]");
	mat.propertyGroup("def").addInput("temperature");
	mat.propertyGroup("def").addInput("pressure");
	mat.propertyGroup("RefractiveIndex").set("n", "");
	mat.propertyGroup("RefractiveIndex").set("ki", "");
	mat.propertyGroup("RefractiveIndex")
	    .set("n", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
	mat.propertyGroup("RefractiveIndex")
	    .set("ki", new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0"});
	}

    private static void configureIronMaterial(Material mat){

	mat.label("Soft Iron (with losses)");
	mat.set("family", "iron");
	
	mat.propertyGroup().create("BHCurve", "BH curve");
	mat.propertyGroup("BHCurve").func().create("BH", "Interpolation");
	mat.propertyGroup().create("HBCurve", "HB curve");
	mat.propertyGroup("HBCurve").func().create("HB", "Interpolation");
	mat.propertyGroup().create("EffectiveBHCurve", "Effective BH curve");
	mat.propertyGroup("EffectiveBHCurve").func().create("BHeff", "Interpolation");
	mat.propertyGroup().create("EffectiveHBCurve", "Effective HB curve");
	mat.propertyGroup("EffectiveHBCurve").func().create("HBeff", "Interpolation");
	mat.propertyGroup("def")
	    .set("electricconductivity", new String[]{"1.12e7[S/m]", "0", "0", "0", "1.12e7[S/m]", "0", "0", "0", "1.12e7[S/m]"});
	mat.propertyGroup("def")
	    .set("relpermittivity", new String[]{"1", "0", "0", "0", "1", "0", "0", "0", "1"});
	mat.propertyGroup("BHCurve").func("BH").set("extrap", "linear");
	mat.propertyGroup("BHCurve").func("BH")
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
	mat.propertyGroup("BHCurve").set("normB", "BH(normH[m/A])[T]");
	mat.propertyGroup("BHCurve").addInput("magneticfield");
	mat.propertyGroup("HBCurve").func("HB").set("extrap", "linear");
	mat.propertyGroup("HBCurve").func("HB")
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
	mat.propertyGroup("HBCurve").set("normH", "HB(normB[1/T])[A/m]");
	mat.propertyGroup("HBCurve").addInput("magneticfluxdensity");
	mat.propertyGroup("EffectiveBHCurve").func("BHeff").set("extrap", "linear");
	mat.propertyGroup("EffectiveBHCurve").func("BHeff")
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
	mat.propertyGroup("EffectiveBHCurve").set("normBeff", "BHeff(normHeff[m/A])[T]");
	mat.propertyGroup("EffectiveBHCurve").addInput("magneticfield");
	mat.propertyGroup("EffectiveHBCurve").func("HBeff").set("extrap", "linear");
	mat.propertyGroup("EffectiveHBCurve").func("HBeff")
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
	mat.propertyGroup("EffectiveHBCurve").set("normHeff", "HBeff(normBeff[1/T])[A/m]");
	mat.propertyGroup("EffectiveHBCurve").addInput("magneticfluxdensity");

	}

    private static void configureMagnetMaterial(Material mat){

	
	mat.label("Nd-Fe-B");
	mat.propertyGroup("def")
	    .set("relpermeability", new String[]{"1.05", "0", "0", "0", "1.05", "0", "0", "0", "1.05"});
	}

    private static void configureMaterials() {

	airMaterial = model.material().create("mat1", "Common", COMPONENT_NAME);
	configureAirMaterial(airMaterial);
	airMaterial.selection().named(AIR_REGIONS_SELECTION_TAG);

	ironMaterial = model.material().create("mat2", "Common", COMPONENT_NAME);
	configureIronMaterial(ironMaterial);
	ironMaterial.selection().named(IRON_SELECTION_TAG);

	magnetMaterial = model.material().create("mat3", "Common", COMPONENT_NAME);
	configureMagnetMaterial(magnetMaterial);
	magnetMaterial.selection().named(MAGNETS_SELECTION_TAG);


    }
    private static void configureInfiniteElementLayer(){

	model.coordSystem().create("ie1", GEOMETRY_TAG, "InfiniteElement");
	model.coordSystem("ie1").selection().named(ENVIRONMENT_SELECTION_TAG);
	model.coordSystem("ie1").label("External environment");
	model.coordSystem("ie1").set("ScalingType", "Cylindrical");
    }

    private static double calculateRemanenceAngle(int index,String magnet, String quadrant){

	// in the parameter file, the angles are specified like this: alpha_rem_II_2
	String param = String.format("alpha_rem_%s_%d", magnet, index+1);
	double angle = 0.0;

	// it can be shown that, to maintain the two-pole symmetry, the y components of the 1st and 2nd
	// quadrant must be opposite
	if (quadrant.equals("1Q")) {
	    angle = params.evaluate(param);
	} else {
	    angle = -params.evaluate(param);
	}

	return angle;
	
	
    }
    

    private static void configureMagnetFluxConservation(int index, String magnet, String quadrant){


	String physicsFeatureTag = mfncPhysics.feature().uniquetag("mfc");
	PhysicsFeature feature = mfncPhysics.create(physicsFeatureTag,"MagneticFluxConservation",2);
	
	Double angle = calculateRemanenceAngle(index,magnet,quadrant);

	String label = String.format("Magnetic Flux Conservation - Magnet %s %d - %s", magnet,index+1,quadrant);

	if (magnet.equals("II")) {

	    

	    if (quadrant.equals("1Q")) {

		feature.selection().set(getDomainEntities(magnetII1QBlockTags[index]));
		 		
	    }
	    else if (quadrant.equals("2Q")) {
		
		feature.selection().set(getDomainEntities(magnetII2QBlockTags[index]));
	    }
	    
	} else if (magnet.equals("IV")){

	    if (quadrant.equals("1Q")) {

		feature.selection().set(getDomainEntities(magnetIV1QBlockTags[index]));
		
	    } else if (quadrant.equals("2Q")) {

		feature.selection().set(getDomainEntities(magnetIV2QBlockTags[index]));
	    }

	    
	}

	feature.set("ConstitutiveRelationH", "RemanentFluxDensity");
	feature.set("materialType", "from_mat");
	feature.label(label);

	String B_rem_x_expr = String.format("B_rem*cos(%f[deg])",angle);
	String B_rem_y_expr = String.format("B_rem*sin(%f[deg])",angle);
	String B_rem_z_expr = "0";
	feature.set("Br", new String[][]{{B_rem_x_expr}, {B_rem_y_expr}, {B_rem_z_expr}});

    }

    private static void configurePhysics(){

	mfncPhysics = model.physics().create("mfnc", "MagnetostaticsNoCurrents", GEOMETRY_TAG);
	mfncPhysics.prop("MeshControl").set("EnableMeshControl", "1");

	model.physics("mfnc").feature("mfc1").label("Magnetic Flux Conservation - Air regions");
	
	PhysicsFeature ironFluxConservation = model.physics("mfnc").create("mfc2", "MagneticFluxConservation", 2);
	ironFluxConservation.selection().named(IRON_SELECTION_TAG);
	ironFluxConservation.set("ConstitutiveRelationH", "BHCurve");
	ironFluxConservation.set("materialType", "from_mat");
	ironFluxConservation.label("Magnetic Flux Conservation - Iron regions");

	
	for (int i = 0; i < nII; i++) {
	    
	    configureMagnetFluxConservation(i,"II","1Q");
	    configureMagnetFluxConservation(i,"II","2Q");
	}

	for (int i = 0; i < nIV; i++) {
	    configureMagnetFluxConservation(i,"IV","1Q");
	    configureMagnetFluxConservation(i,"IV","2Q");
	}

    }

    private static void configureMesh(){

	modelMesh = model.mesh().create("mesh1", GEOMETRY_TAG);

	MeshFeature meshSizeConfiguration = modelMesh.feature("size");
	meshSizeConfiguration.set("hauto", 3);
	meshSizeConfiguration.set("custom", "on");
	meshSizeConfiguration.set("hnarrow", "5");
	
	MeshFeature meshBoundaryDistributionConfiguration = modelMesh.create("dis1", "Distribution");
	meshBoundaryDistributionConfiguration.selection().named(ENVIRONMENT_HORIZONTAL_BOUNDARY_SELECTION_TAG);
	meshBoundaryDistributionConfiguration.set("type", "predefined");

		
	MeshFeature meshFreeTriangularConfiguration = modelMesh.create("ftri1", "FreeTri");
	meshFreeTriangularConfiguration.selection().geom(GEOMETRY_TAG, 2);
	meshFreeTriangularConfiguration.selection().named(CIRCUIT_REGIONS_SELECTION_TAG);

		
	MeshFeature meshMapConfiguration = modelMesh.create("map1", "Map");
	meshMapConfiguration.selection().geom(GEOMETRY_TAG, 2);
	meshMapConfiguration.selection().named(ENVIRONMENT_SELECTION_TAG);
	meshMapConfiguration.set("adjustedgdistr", true);

	modelMesh.run();
    }

    private static void configureStudy(){

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

	
	model.sol("sol1").attach("std1");
	model.sol("sol1").runAll();

	}

    private static DatasetFeature configureDataSet(String label, String selectionTag){

	String tag = modelDataSets.uniquetag("dset");
	DatasetFeature dset = modelDataSets.create(tag,"Solution");
	dset.selection().geom(GEOMETRY_TAG,2);
	dset.label(label);
	dset.selection().named(selectionTag);
	dset.selection().inherit(true);

	return dset;
	}
    

    private static void configureResults(){

	modelResults = model.result();
	modelDataSets = modelResults.dataset();
	
	// configure the datasets
	DatasetFeature airGapDataSet = configureDataSet("Air gap", AIR_GAP_SELECTION_TAG);

	
	DatasetFeature magnetsDataSet = configureDataSet("Magnets", MAGNETS_SELECTION_TAG);

	DatasetFeature airGapHighDataSet = configureDataSet("Air Gap High Field Region", AIR_GAP_HIGH_SELECTION_TAG);
	DatasetFeature airGapLowDataSet = configureDataSet("Air Gap Low Field Region", AIR_GAP_LOW_SELECTION_TAG);
	
	
	DatasetFeature magneticProfileDataSet = modelDataSets.create("pc1", "ParCurve2D");
	magneticProfileDataSet.label("Air gap central line");
	magneticProfileDataSet.set("parmax1", "pi");
	magneticProfileDataSet.set("exprx", "(R_o+h_gap/2)*cos(s)");
	magneticProfileDataSet.set("expry", "(R_o+h_gap/2)*sin(s)");
	

	// configure plot groups

	ResultFeature airGapPlotGroup = modelResults.create("pg2", "PlotGroup2D");
	airGapPlotGroup.create("surf1", "Surface");
	airGapPlotGroup.label("Air gap");
	airGapPlotGroup.set("data",airGapDataSet.tag());
	airGapPlotGroup.feature("surf1").label("B");
	airGapPlotGroup.feature("surf1").set("unit", "T");
	airGapPlotGroup.feature("surf1").set("expr", "mfnc.normB");
	airGapPlotGroup.feature("surf1").set("descr", "Magnetic flux density norm");
	airGapPlotGroup.feature("surf1").set("resolution", "normal");

	
	ResultFeature magnetsPlotGroup = modelResults.create("pg3", "PlotGroup2D");
	magnetsPlotGroup.set("data", magnetsDataSet.tag());
	magnetsPlotGroup.label("Magnets");
	
	ResultFeature magnetsRemanenceVectorPlot = magnetsPlotGroup.create("arws1", "ArrowSurface");
	magnetsRemanenceVectorPlot.label("B_rem");
	magnetsRemanenceVectorPlot.set("scale", "0.0077221241233942925");
	magnetsRemanenceVectorPlot.set("arrowbase", "center");
	magnetsRemanenceVectorPlot.set("expr", new String[]{"mfnc.Brx", "mfnc.Bry"});
	magnetsRemanenceVectorPlot.set("descr", "Remanent flux density");
	magnetsRemanenceVectorPlot.set("scaleactive", false);
	
	ResultFeature magnetsEnergyPlot = magnetsPlotGroup.create("surf1", "Surface");
	magnetsEnergyPlot.label("Psi");
	magnetsEnergyPlot.set("unit", "kPa");
	magnetsEnergyPlot
	    .set("expr", "abs((mfnc.Bx*mfnc.Brx+mfnc.By*mfnc.Bry)/mfnc.normBr)*abs((mfnc.Hx*mfnc.Brx+mfnc.Hy*mfnc.Bry)/mfnc.normBr)");
	magnetsEnergyPlot.set("rangedatamax", "409");
	magnetsEnergyPlot
	    .set("descr", "abs((mfnc.Bx*mfnc.Brx+mfnc.By*mfnc.Bry)/mfnc.normBr)*abs((mfnc.Hx*mfnc.Brx+mfnc.Hy*mfnc.Bry)/mfnc.normBr)");
	magnetsEnergyPlot.set("rangecoloractive", "on");
	magnetsEnergyPlot.set("rangedataactive", "on");
	magnetsEnergyPlot.set("rangecolormax", "409");
	magnetsEnergyPlot.set("resolution", "normal");
	
	ResultFeature magnetsMagneticFieldVectorPlot = magnetsPlotGroup.create("arws2", "ArrowSurface");
	magnetsMagneticFieldVectorPlot.label("H");
	magnetsMagneticFieldVectorPlot.set("scale", "0.010592406010573617");
	magnetsMagneticFieldVectorPlot.set("descractive", true);
	magnetsMagneticFieldVectorPlot.set("arrowbase", "center");
	magnetsMagneticFieldVectorPlot.set("expr", new String[]{"mu0_const*mfnc.Hx", "mu0_const*mfnc.Hy"});
	magnetsMagneticFieldVectorPlot.set("descr", "mu0*H");
	magnetsMagneticFieldVectorPlot.set("color", "black");
	magnetsMagneticFieldVectorPlot.set("scaleactive", false);

	
	ResultFeature magneticProfilePlotGroup = modelResults.create("pg4", "PlotGroup1D");
	magneticProfilePlotGroup.label("Air gap central line");
	magneticProfilePlotGroup.set("data", magneticProfileDataSet.tag());
	magneticProfilePlotGroup.set("ylabel", "Magnetic flux density norm (T)");
	magneticProfilePlotGroup.set("xlabel", "Arc length");
	magneticProfilePlotGroup.set("xlabelactive", false);
	magneticProfilePlotGroup.set("ylabelactive", false);
	ResultFeature magneticProfileLinePlot = magneticProfilePlotGroup.create("lngr1", "LineGraph");
	magneticProfileLinePlot.label("Magnetic Profile");
	magneticProfileLinePlot.set("unit", "T");
	magneticProfileLinePlot.set("descr", "Magnetic flux density norm");
	magneticProfileLinePlot.set("expr", "mfnc.normB");
	magneticProfileLinePlot.set("resolution", "normal");

	// create export groups
	ExportFeatureList  modelExportList = modelResults.export();
	
	ExportFeature airGapHighDataExport = modelExportList.create("export1",airGapHighDataSet.tag(),"Data");
	airGapHighDataExport.set("filename","B_high.txt");
	airGapHighDataExport.set("expr","mfnc.normB");
	airGapHighDataExport.run();

	ExportFeature airGapLowDataExport = modelExportList.create("export2",airGapLowDataSet.tag(),"Data");
	airGapHighDataExport.set("filename","B_low.txt");
	airGapHighDataExport.set("expr","mfnc.normB");
	airGapHighDataExport.run();

	// save the probe data
	ExportFeature probesDataExport = modelExportList.create("export3","Table");
	probesDataExport.set("filename","COMSOL Main Results.txt");
	probesDataExport.set("table",RESULTS_TABLE_TAG);
	probesDataExport.set("header","on");
	probesDataExport.run();
	
	}
    
    public static Model run() {
        model = ModelUtil.create("Model");

	params = model.param();

	params.loadFile(PARAMETER_FILE_NAME);

	nII = Integer.parseInt(params.get("n_II"));
	nIV = Integer.parseInt(params.get("n_IV"));
	
	modelNodes = model.modelNode();

	component = modelNodes.create(COMPONENT_NAME);

	geometryList = model.geom();
	geometry = geometryList.create(GEOMETRY_TAG, 2);

	GeomSequence cylinderBlockPart = configureCylinderBlock();
	GeomSequence cylinderShellPart = configureCylinderShell();

	buildGeometry();

	createSelections();

	createProbes();

	configureMaterials();

	configureInfiniteElementLayer();

	configurePhysics();

	configureMesh();

	configureStudy();

	configureResults();

 	return model;
    }


    public static void main(String[] args) {
	model = run();

    }

}
