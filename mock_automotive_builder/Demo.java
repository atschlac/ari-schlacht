package edu.yu.cs.intro.automotive.manufacturing;

import java.util.*;

public class Demo {
    private VehicleModel accord;
    private VehicleModel civic;
    private VehicleModel f150;
    private Manufacturer honda;
    private Manufacturer ford;

    public static void main(String[] args) {
        Demo d = new Demo();
        d.runDemo();
    }

    public Demo(){
        //create manufacturers and print out their info

        //Honda
        this.honda = DemoGenerators.getHonda();
        Iterator<VehicleModel> it = this.honda.getModelsManufactured().iterator();
        VehicleModel vm = it.next();
        if(vm.getModelName().equalsIgnoreCase("accord")){
            this.accord = vm;
            this.civic = it.next();
        }else{
            this.civic = vm;
            this.accord = it.next();
        }
        this.printManufacturerInformation(this.honda);

        //Ford
        this.ford = DemoGenerators.getFord();
        this.printManufacturerInformation(this.ford);

        this.f150 = this.ford.getModelsManufactured().iterator().next();
    }

    public void runDemo(){
        //order a car that gets successfully built right away
        this.successfulOrder();
        System.out.println();

        //order a car that we have to retry completing it
        this.retryCompletingVehicle();
        System.out.println();

        //doInquiry - succeed
        this.doInquiry();
    }

    private void doInquiry(){
        RequestedVehicle rv = new RequestedVehicle(this.f150);
        Set<Option> options = this.f150.getModelOptions();
        Iterator<Option> it = options.iterator();
        rv.addChosenOption(it.next());
        rv.addChosenOption(it.next());
        rv.addChosenOption(it.next());
        System.out.println("Inquiring about a vehicle:\n" + rv);

        Order order = this.ford.doInquiry(rv);
        assert(order.allOptionsAreAvailable());
        assert(order.isInquiryOnly());
        Vehicle v = this.ford.getFinishedVehicle(order);
        assert(v == null);

        System.out.println("Inquiry Successful");
    }

    private void retryCompletingVehicle(){
        System.out.println("Ordering a Civic, with an option part not having a supplier...");

        //specify the details of the car to be ordered
        RequestedVehicle rv = new RequestedVehicle(this.civic);
        Set<Option> options = this.civic.getModelOptions();
        Iterator<Option> opts = options.iterator();
        Option opt1 = opts.next();
        Option opt2 = opts.next();
        rv.addChosenOption(opt1);
        rv.addChosenOption(opt2);

        //remove the supplier
        PartSpecification spec = opt1.getPartSpecification();
        PartsSupplier ps = this.honda.getSupplierMap().get(spec.getPartNumber());
        ps.removeSuppliedPartSpecification(spec);
        this.honda.changePartsSupplier(spec,null);

        //order the vehicle and check that opt1 is missing and therefore the vehicle is not complete
        Order order = this.honda.placeOrder(rv);        
        assert(!order.allOptionsAreAvailable());
        assert(order.unavailableOptions().contains(opt1));
        assert(this.honda.getFinishedVehicle(order) == null);

        //add the part spec back to the supplier
        System.out.println("Putting the part spec back into the supplier and calling retryCompletingVehicle...");
        ps.addSuppliedPartSpecification(spec);
        this.honda.changePartsSupplier(spec,ps);

        //try to complete the order now
        assert(this.honda.getSupplierMap().get(opt1.getPartSpecification().getPartNumber()).equals(ps));
        boolean success = this.honda.retryCompletingVehicle(order);
        assert(success);
        assert(order.allOptionsAreAvailable());
        assert(order.unavailableOptions().isEmpty());
        assert(this.honda.getFinishedVehicle(order) != null);
//
        System.out.println("The Vehicle: " + this.honda.getFinishedVehicle(order));
//
        double expectedPrice = Util.round(this.civic.getBasePrice() + opt1.getPrice() + opt2.getPrice(),2);
        Vehicle vehicle = this.honda.getFinishedVehicle(order);

        this.compareExpectedAndBuiltPrices(expectedPrice,vehicle);
    }

    private void compareExpectedAndBuiltPrices(double expectedPrice, Vehicle builtVehicle){
        double builtPrice = builtVehicle.getBuiltPrice();
        assert(expectedPrice == builtPrice);
        System.out.println(builtVehicle.toString());
        System.out.println("Expected price: " + expectedPrice);
        System.out.println("Built price: " + builtPrice);
    }

    private void successfulOrder(){
        System.out.println("Ordering an Accord...");

        //specify the details of the car to be ordered
        RequestedVehicle rv = new RequestedVehicle(this.accord);
        Set<Option> options = this.accord.getModelOptions();
        Iterator<Option> opts = options.iterator();
        Option opt1 = opts.next();
        Option opt2 = opts.next();
        rv.addChosenOption(opt1);
        rv.addChosenOption(opt2);
        Order order = this.honda.placeOrder(rv);

        double expectedPrice = Util.round(this.accord.getBasePrice() + opt1.getPrice() + opt2.getPrice(),2);
        Vehicle vehicle = this.honda.getFinishedVehicle(order);
        this.compareExpectedAndBuiltPrices(expectedPrice,vehicle);
    }

    private void printManufacturerInformation(Manufacturer mfctr){
        String name = mfctr.getMake();
        System.out.println("Manufacturer: " + name);
        //getModelsManufactured
        System.out.println(name + " vehicle models:");
        for(VehicleModel model : mfctr.getModelsManufactured()){
            System.out.println("\t" + model);
        }
        //getSupplierMap
        System.out.println(name + " supplier map:");
        Map<Integer,PartsSupplier> supplierMap = mfctr.getSupplierMap();
        HashSet<PartsSupplier> suppliers = new HashSet<>(supplierMap.values());
        for(PartsSupplier ps : suppliers){
            System.out.println(ps);
        }
        System.out.println();
    }

}