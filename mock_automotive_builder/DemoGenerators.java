package edu.yu.cs.intro.automotive.manufacturing;

import java.util.HashSet;

public class DemoGenerators 
    {
    public static Manufacturer getHonda(){
        VehicleModel accord = designHondaAccord();
        VehicleModel civic = designHondaCivic();
        HashSet<VehicleModel> models = new HashSet<>(2);
        models.add(accord);
        models.add(civic);
        HashSet<PartsSupplier> suppliers = createSuppliersForAllParts(accord);
        suppliers.addAll(createSuppliersForAllParts(civic));
        return new Manufacturer("Honda",models,suppliers);
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
        HashSet<PartSpecification> baseParts = new HashSet<>(4);
        baseParts.add(engine);
        baseParts.add(transmission);
        baseParts.add(chassis);
        baseParts.add(steering);
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
    public static VehicleModel designHondaCivic(){
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

    public static Manufacturer getFord()
    {
        VehicleModel f150 = designFordF150();
        HashSet<VehicleModel> models = new HashSet<>(2);
        models.add(f150);
        HashSet<PartsSupplier> suppliers = createSuppliersForAllParts(f150);
        return new Manufacturer("Ford",models,suppliers);
    }

    public static VehicleModel designFordF150(){
        VehicleModel f150 = new VehicleModel("Ford","F-150");

        //base parts
        PartSpecification engine = new PartSpecification(Util.getNextPartNumber(),"3.3L Ti-VCT V6",3000);
        PartSpecification transmission = new PartSpecification(Util.getNextPartNumber(),"SelectShift Automatic Transmission with Progressive Range Select",2500);
        PartSpecification chassis = new PartSpecification(Util.getNextPartNumber(),"F-150 Chasis",1500);
        PartSpecification steering = new PartSpecification(Util.getNextPartNumber(),"Steering - Power Rack-and-Pinion Steering",800);
        HashSet<PartSpecification> baseParts = new HashSet<>(4);
        baseParts.add(engine);
        baseParts.add(transmission);
        baseParts.add(chassis);
        baseParts.add(steering);
        f150.setBaseRequiredParts(baseParts);

        //options
        PartSpecification fx4 = new PartSpecification(Util.getNextPartNumber(),"FX4 Off-Road Package",750);
        f150.addModelOption(new Option(fx4.getDescription(),fx4));
        PartSpecification tow = new PartSpecification(Util.getNextPartNumber(),"Tow Technology Package",1000);
        f150.addModelOption(new Option(tow.getDescription(),tow));
        PartSpecification hitch = new PartSpecification(Util.getNextPartNumber(),"Class IV Trailer Hitch",250);
        f150.addModelOption(new Option(hitch.getDescription(),hitch));

        return f150;
    }

    public static HashSet<PartsSupplier> createSuppliersForAllParts(VehicleModel model){
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
}
