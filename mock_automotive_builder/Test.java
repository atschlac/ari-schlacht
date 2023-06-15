package edu.yu.cs.intro.automotive.manufacturing;
import java.util.*;

public class Test
{
	public static void main(String[] args)
	{
		Manufacturer honda = getHonda();
		VehicleModel accord = designHondaAccord();
		VehicleModel civic = designHondaCivic();
		Factory accordFactory = new Factory("accordFactory", accord, honda);
		/*System.out.println(accordFactory.getVehicleModel());
		System.out.println(accordFactory.toString());
		System.out.println(accordFactory.getName());
		System.out.println(accordFactory.getManufacturer());*/

		//System.out.println(honda.testClass(6));



   	//base parts
        PartSpecification engine = new PartSpecification(Util.getNextPartNumber(),"In-Line 4-Cylinder with Turbocharger",2000);
        PartSpecification transmission = new PartSpecification(Util.getNextPartNumber(),"Continuously Variable Transmission (CVT) with Sport Mode",3000);
        PartSpecification chassis = new PartSpecification(Util.getNextPartNumber(),"Accord Chasis",1000);
        PartSpecification steering = new PartSpecification(Util.getNextPartNumber(),"Dual-Pinion, Variable-Ratio Electric Power Steering (EPS)",500);
        PartSpecification chair = new PartSpecification(5, "Test Part", 200);
        HashSet<PartSpecification> baseParts = new HashSet<>(5);
        baseParts.add(engine);
        baseParts.add(transmission);
        baseParts.add(chassis);
        baseParts.add(steering);
        baseParts.add(chair);
        accord.setBaseRequiredParts(baseParts);

    //options
        PartSpecification allSeason = new PartSpecification(Util.getNextPartNumber(),"All Season Protection Package I",457);
        accord.addModelOption(new Option(allSeason.getDescription(),allSeason));
        PartSpecification emblems = new PartSpecification(Util.getNextPartNumber(),"Fender Emblems",57);
        accord.addModelOption(new Option(emblems.getDescription(),emblems));
        PartSpecification spoiler = new PartSpecification(Util.getNextPartNumber(),"Underbody Spoiler Kit",1402);
        accord.addModelOption(new Option(spoiler.getDescription(),spoiler));
        PartSpecification heatedSteeringWheel = new PartSpecification(Util.getNextPartNumber(),"Heated Steering Wheel",157);
        accord.addModelOption(new Option(heatedSteeringWheel.getDescription(),heatedSteeringWheel));
        PartSpecification interiorIllumination = new PartSpecification(Util.getNextPartNumber(),"Interior Illumination",141);
        accord.addModelOption(new Option(interiorIllumination.getDescription(),interiorIllumination));
		
		Set<Option> testUnOpts = new HashSet<>(2);
		testUnOpts.add(new Option(interiorIllumination.getDescription(),interiorIllumination));
		testUnOpts.add(new Option(heatedSteeringWheel.getDescription(),heatedSteeringWheel));

		setUnavailableOptions(testUnOpts);



		/*//specify the details of the car to be ordered
        RequestedVehicle rv = new RequestedVehicle(accord);
        Set<Option> options = accord.getModelOptions();
        Iterator<Option> opts = options.iterator();
        Option opt1 = opts.next();
        Option opt2 = opts.next();
        rv.addChosenOption(opt1);
        rv.addChosenOption(opt2);
        System.out.println("VModel: " + rv.getModel());
		accordFactory.buildVehicle(rv);*/
	}

	    /**
     * used by manufacturer to indicate which options in this request there are no parts for
     * @param unavailableOptions
     */
    static void setUnavailableOptions(Set<Option> unavailableOptions)
    {
    	Set<Option> unavailableOptionsSet = new HashSet<>();
        unavailableOptionsSet = new HashSet(unavailableOptions);
    }

	




	Test()
	{

	}

	public static VehicleModel designHondaAccord()
	{
        //HONDA ACCORD
        VehicleModel accord = new VehicleModel("Honda","Accord");

        //base parts
        PartSpecification engine = new PartSpecification(Util.getNextPartNumber(),"In-Line 4-Cylinder with Turbocharger",2000);
        PartSpecification transmission = new PartSpecification(Util.getNextPartNumber(),"Continuously Variable Transmission (CVT) with Sport Mode",3000);
        PartSpecification chassis = new PartSpecification(Util.getNextPartNumber(),"Accord Chasis",1000);
        PartSpecification steering = new PartSpecification(Util.getNextPartNumber(),"Dual-Pinion, Variable-Ratio Electric Power Steering (EPS)",500);
        PartSpecification chair = new PartSpecification(5, "Test Part", 200);
        HashSet<PartSpecification> baseParts = new HashSet<>(5);
        baseParts.add(engine);
        baseParts.add(transmission);
        baseParts.add(chassis);
        baseParts.add(steering);
        baseParts.add(chair);
        accord.setBaseRequiredParts(baseParts);

        //options
        PartSpecification allSeason = new PartSpecification(Util.getNextPartNumber(),"All Season Protection Package I",457);
        accord.addModelOption(new Option(allSeason.getDescription(),allSeason));
        PartSpecification emblems = new PartSpecification(Util.getNextPartNumber(),"Fender Emblems",57);
        accord.addModelOption(new Option(emblems.getDescription(),emblems));
        PartSpecification spoiler = new PartSpecification(Util.getNextPartNumber(),"Underbody Spoiler Kit",1402);
        accord.addModelOption(new Option(spoiler.getDescription(),spoiler));
        PartSpecification heatedSteeringWheel = new PartSpecification(Util.getNextPartNumber(),"Heated Steering Wheel",157);
        accord.addModelOption(new Option(heatedSteeringWheel.getDescription(),heatedSteeringWheel));
        PartSpecification interiorIllumination = new PartSpecification(Util.getNextPartNumber(),"Interior Illumination",141);
        accord.addModelOption(new Option(interiorIllumination.getDescription(),interiorIllumination));

        return accord;
    }


    public static String All_SEASON_2 = "All Season Protection Package II";
    public static String EMBLEMS = "Emblems, Front and Rear H-Mark and Civic – Gloss Black";
    public static VehicleModel designHondaCivic()
    {
        VehicleModel civic = new VehicleModel("Honda","Civic");

        //base parts
        PartSpecification engine = new PartSpecification(Util.getNextPartNumber(),"In-Line 4-Cylinder",1500);
        PartSpecification transmission = new PartSpecification(Util.getNextPartNumber(),"Continuously Variable Transmission (CVT)",2000);
        PartSpecification chassis = new PartSpecification(Util.getNextPartNumber(),"Civic Chasis",750);
        PartSpecification steering = new PartSpecification(Util.getNextPartNumber(),"Variable Ratio Electric Power-Assisted Rack-and-Pinion Steering (EPS)",400);
        HashSet<PartSpecification> baseParts = new HashSet<>(4);
        baseParts.add(engine);
        baseParts.add(transmission);
        baseParts.add(chassis);
        baseParts.add(steering);
        civic.setBaseRequiredParts(baseParts);

        //options
        PartSpecification allSeason = new PartSpecification(Util.getNextPartNumber(),All_SEASON_2,370);
        civic.addModelOption(new Option(allSeason.getDescription(),allSeason));
        PartSpecification emblems = new PartSpecification(Util.getNextPartNumber(),EMBLEMS,113);
        civic.addModelOption(new Option(emblems.getDescription(),emblems));
        PartSpecification spoiler = new PartSpecification(Util.getNextPartNumber(),"Underbody Spoiler – HPD",679);
        civic.addModelOption(new Option(spoiler.getDescription(),spoiler));
        PartSpecification shades = new PartSpecification(Util.getNextPartNumber(),"Rear Passenger Window Shades",188);
        civic.addModelOption(new Option(shades.getDescription(),shades));
        PartSpecification interiorIllumination = new PartSpecification(Util.getNextPartNumber(),"Illuminated Door Sill Trim – Red",322);
        civic.addModelOption(new Option(interiorIllumination.getDescription(),interiorIllumination));

        return civic;
    }

    public static Manufacturer getHonda()
    {
        VehicleModel accord = designHondaAccord();
        VehicleModel civic = designHondaCivic();
        HashSet<VehicleModel> models = new HashSet<>(2);
        models.add(accord);
        models.add(civic);
        HashSet<PartsSupplier> suppliers = createSuppliersForAllParts(accord);
        suppliers.addAll(createSuppliersForAllParts(civic));
        return new Manufacturer("Honda",models,suppliers);
    }

    public static HashSet<PartsSupplier> createSuppliersForAllParts(VehicleModel model)
    {
        HashSet<PartsSupplier> suppliers = new HashSet<>();
        PartsSupplier base = new PartsSupplier(model.getMake() + " " + model.getModelName() + " BASE PARTS supplier");
        for(PartSpecification spec : model.getBaseRequiredParts()){
            base.addSuppliedPartSpecification(spec);
        }
        suppliers.add(base);
        PartsSupplier optional = new PartsSupplier(model.getMake() + " " + model.getModelName() + " OPTIONS PARTS supplier");
        for(Option opt : model.getModelOptions()){
            optional.addSuppliedPartSpecification(opt.getPartSpecification());
        }
        suppliers.add(optional);
        return suppliers;
    }

    


    public static void myTests()
    {
    	PartSpecification testPartSpecification = new PartSpecification(1, "Wheel", 2.5);
		PartSpecification otherPartSpecification = new PartSpecification(2, "Door", 3.5);
		PartSpecification partSpecification2 = new PartSpecification(3, "Handle", 4.5);
		PartSpecification partSpecification3 = new PartSpecification(4, "Window", 5.5);

		Part testPart = new Part(testPartSpecification);
		Part otherPart = new Part(otherPartSpecification);
		Part part3 = new Part(partSpecification3);

		Option testOption = new Option(testPartSpecification.getDescription(), testPartSpecification);
		Option otherOption = new Option(otherPartSpecification.getDescription(), otherPartSpecification);
		Option option2 = new Option (partSpecification2.getDescription(), partSpecification2);
		Option option3 = new Option (partSpecification3.getDescription(), partSpecification3);


		VehicleModel testVehicleModel = new VehicleModel("Honda", "Accord");
		testVehicleModel.addModelOption(testOption);
		testVehicleModel.addModelOption(otherOption);
		testVehicleModel.addModelOption(option2);

		VehicleModel otherVehicleModel = new VehicleModel("Honda", "Accord");
		/*System.out.println(testVehicleModel.getBasePrice());
		System.out.println(testVehicleModel.getMake());
		System.out.println(testVehicleModel.getModelName());
		
		HashSet<PartSpecification> baseParts = new HashSet<>(2);
		baseParts.add(testPartSpecification);
		baseParts.add(otherPartSpecification);
		
		testVehicleModel.setBaseRequiredParts(baseParts);*/

		PartsSupplier testPartsSupplier = new PartsSupplier("Toyota");
		
		testPartsSupplier.addSuppliedPartSpecification(testPartSpecification);
		testPartsSupplier.addSuppliedPartSpecification(otherPartSpecification);

		//Add base parts to the VehicleModel
		PartSpecification engine = new PartSpecification(Util.getNextPartNumber(),"In-Line 4-Cylinder with Turbocharger",1);
        PartSpecification transmission = new PartSpecification(Util.getNextPartNumber(),"Continuously Variable Transmission (CVT) with Sport Mode",1);
        PartSpecification chassis = new PartSpecification(Util.getNextPartNumber(),"Accord Chasis",1);
        PartSpecification steering = new PartSpecification(Util.getNextPartNumber(),"Dual-Pinion, Variable-Ratio Electric Power Steering (EPS)",1);
        HashSet<PartSpecification> baseParts = new HashSet<>(4);
        baseParts.add(engine);
        baseParts.add(transmission);
        baseParts.add(chassis);
        baseParts.add(steering);
        testVehicleModel.setBaseRequiredParts(baseParts);

        //options
        PartSpecification allSeason = new PartSpecification(Util.getNextPartNumber(),"All Season Protection Package I",457);
        testVehicleModel.addModelOption(new Option(allSeason.getDescription(),allSeason));
        PartSpecification emblems = new PartSpecification(Util.getNextPartNumber(),"Fender Emblems",57);
        testVehicleModel.addModelOption(new Option(emblems.getDescription(),emblems));
        PartSpecification spoiler = new PartSpecification(Util.getNextPartNumber(),"Underbody Spoiler Kit",1402);
        testVehicleModel.addModelOption(new Option(spoiler.getDescription(),spoiler));
        PartSpecification heatedSteeringWheel = new PartSpecification(Util.getNextPartNumber(),"Heated Steering Wheel",157);
        testVehicleModel.addModelOption(new Option(heatedSteeringWheel.getDescription(),heatedSteeringWheel));
        PartSpecification interiorIllumination = new PartSpecification(Util.getNextPartNumber(),"Interior Illumination",141);
        testVehicleModel.addModelOption(new Option(interiorIllumination.getDescription(),interiorIllumination));

		Vehicle testVehicle = new Vehicle(testVehicleModel);
		System.out.println(testVehicle.addOptionPart(testOption, testPart));
		System.out.println(testVehicle.addOptionPart(otherOption, otherPart));
		//System.out.println(testVehicle.getOptionParts());
		//System.out.println();
		//Make a Set of Parts with the required Base parts for this vehicle
        HashSet<Part> requiredBaseParts = new HashSet<>(4);
        requiredBaseParts.add(new Part(engine));
        requiredBaseParts.add(new Part(transmission));
        requiredBaseParts.add(new Part(chassis));
        requiredBaseParts.add(new Part(steering));
		testVehicle.setBaseParts(requiredBaseParts);
		//System.out.println(testVehicle.getBaseParts());

		//System.out.println(testVehicle.hasAllRequiredBaseParts(requiredBaseParts));
		//System.out.println(testVehicle.isBaseComplete());
		//System.out.println("Base Parts Price: " + testVehicleModel.recalculateBasePrice() + " testOption's Price: " + testOption.getPrice() + " otherOption's Price: " + otherOption.getPrice());
		//System.out.println(testVehicle.getBuiltPrice());
		System.out.println(testVehicle.hashCode());
    }

		
}