package edu.yu.cs.intro.automotive.manufacturing;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * represents a vehicle manufacturer
 */
public class Manufacturer 
{
    //Instance variables
    private String currentMake;
    private Map<Integer, PartsSupplier> partsSupplierMap = new HashMap<>();
    private Set<VehicleModel> vehicleModelsSet;
    private Map<Order, Vehicle> completedOrderMap = new HashMap<>();
    private Set<Factory> availableFactorySet = new HashSet<>();

    /**
     * create a manufacturer with the given name (e.g. "Honda"), which manufactures the given models, and orders the needed parts from the given suppliers.
     * if models OR suppliers is null OR empty, throw an IllegalArgumentException.
     * More details in comments below.
     * @param make
     * @param models
     * @param suppliers
     * @throws java.lang.IllegalArgumentException
     */
    Manufacturer(String make, Set<VehicleModel> models, Set<PartsSupplier> suppliers) 
    {
        this.currentMake = make;
        this.vehicleModelsSet = new HashSet<VehicleModel>((HashSet)models);

        
        //check for null or empty sets.
        this.validConstruction(models, suppliers);

        //create parts suppliers map.
        this.createPartSuppliersMap(suppliers); //Each key will represent a Part Number corrosponding to a specific PartsSupplier

        //check that every model's make is this manufacturer. If not, throw an IllegalArgumentException.
        this.isCorrectMake(models);

        //check that every part in every model (both base parts and options parts) has a supplier. If not, throw an IllegalArgumentException.
        this.doPartsHaveSupplier(models, suppliers);
        
        //create factories, one for each VehicleModel.
        int factoryNum = 1;
        for(VehicleModel currentModel: this.vehicleModelsSet)
        {
            Factory tempFactory = new Factory("Factory #" + factoryNum++, currentModel, this);
            this.availableFactorySet.add(tempFactory);
        }

        //create place to store/access completed orders/vehicles.
        this.completedOrderMap = new HashMap<Order, Vehicle>();

    }

        private void validConstruction(Set<VehicleModel> models, Set<PartsSupplier> suppliers)
        {
            if(models == null || suppliers == null || models.size() == 0 || suppliers.size() == 0)
            {
                throw new IllegalArgumentException();
            }
        }

        private void isCorrectMake(Set<VehicleModel> models)
        {
            for(VehicleModel vehicleModel: models)
            {
                if(vehicleModel.getMake() != this.currentMake)
                {
                    throw new IllegalArgumentException();
                }
            }
        }

        private void doPartsHaveSupplier(Set<VehicleModel> models, Set<PartsSupplier> suppliers)
        {

            //Iterate through the VehicleModels
            for(VehicleModel tempModel: models)
            {
                //Iterate through every VehicleModels baseParts to see if there is a supplier for that PartSpec
                for(PartSpecification basePart: tempModel.getBaseRequiredParts())
                {
                    boolean isPartSpecPresent = false; //If this variable remains false,then it's because the current basePart wasn't found in any of the suppliers

                    for(PartsSupplier tempSupplier: suppliers) //Iterate through the suppliers, and determine if the current basePart is contained within the PartSpec sets of any of the suppliers
                    {
                        if(tempSupplier.getSuppliedPartSpecifications().contains(basePart))
                        {
                            isPartSpecPresent = true;
                        }
                    }

                    //If the basePart wasn't present in any of the suppliers, then shalom al Yisrael
                    if(isPartSpecPresent == false)
                    {
                        throw new IllegalArgumentException();
                    }
                }

                //Iterate through every VehicleModels baseParts to see if there is a supplier for that PartSpec
                for(Option option: tempModel.getModelOptions())
                {
                    boolean isOptionPresent = false;

                    for(PartsSupplier tempSupplier: suppliers)
                    {
                        if(tempSupplier.getSuppliedPartSpecifications().contains(option.getPartSpecification()))
                        {
                            isOptionPresent = true;
                        }
                    }

                    //If the basePart wasn't present in any of the suppliers, then shalom al Yisrael
                    if(isOptionPresent == false)
                    {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }

        private void createPartSuppliersMap(Set<PartsSupplier> suppliers)
        {
            //Iterate through all the PartSuppliers in suppliers
            for(PartsSupplier currentSupplier: suppliers)
            {
                //Iterate through all the available PartSpecs in the currentSupplier
                for(PartSpecification spec: currentSupplier.getSuppliedPartSpecifications())
                {
                    this.partsSupplierMap.put(spec.getPartNumber(), currentSupplier);
                }
            }
        }

    /**
     *
     * @return the return value of getMake()
     */
    @Override
    public String toString()
    {
        return this.currentMake;
    }

    /**
     *
     * @return the make (e.g. Honda, Ford, etc.)
     */
    public String getMake()
    {
        return this.currentMake;
    }

    /**
     * get an unmodifiable view of the map of part numbers to PartSuppliers, i.e. a map of which supplier is supplying the part that has a given part number
     * @return
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    public Map<Integer, PartsSupplier> getSupplierMap() 
    {
        Map<Integer, PartsSupplier> immutableMap = Collections.unmodifiableMap(this.partsSupplierMap);
        return immutableMap;
    }

    /**
     * get an unmodifiable view of the set of VehicleModels that this Manufacturer makes
     * @return
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    public Set<VehicleModel> getModelsManufactured() 
    {
        Set<VehicleModel> immutableSet = Collections.unmodifiableSet(this.vehicleModelsSet);
        return immutableSet;
    }



    //TEST CLASS/////////////////////////

    protected Part testClass(int partNumber)
    {
        return this.supplyPart(partNumber);
    }


    //TEST CLASS/////////////////////////



    /**
     * Create, and return as a set, the given quantity of Parts that have the PartSpecification with the given partNumber.
     * If this manufacturer doesn't have a supplier for the given partNumber, return null
     * @param partNumber
     * @param quantity
     * @return
     */
    Set<Part> supplyPart(int partNumber, int quantity)
    {
        //If this manufacturer doesn't have a supplier for the given partNumber, return null
        if(this.partsSupplierMap.containsKey(partNumber) == false || this.partsSupplierMap.get(partNumber) == null)
        {
            return null;
        }
        else
        {
            return partsSupplierMap.get(partNumber).supplyPart(partNumber, quantity);
        }
    }

    /**
     * Create and return one Part that has the PartSpecification with the given partNumber.
     * If this manufacturer doesn't have a supplier for the given partNumber, return null
     * @param partNumber
     * @return
     */
    Part supplyPart(int partNumber) 
    {
        if(this.supplyPart(partNumber, 1) == null)
        {
            return null;
        }
        else
        {
            //Return the first element in the Set of Parts that this Set returns
            for(Part p: this.supplyPart(partNumber, 1))
            {
                return p;
            }
        }
        System.out.println("Something went wrong bc the loop should always run at least once");
        return null;   
    }

    /**
     * place an order for the vehicle specified by the RequestedVehicle
     * @param rv
     * @return
     */
    public Order placeOrder(RequestedVehicle rv) 
    {
        //if this manufacturer doesn't make the VehicleModel of the RequestedVehicle, throw an IllegalArgumentException
        if (this.vehicleModelsSet.contains(rv.getModel()) == false) 
        {
            throw new IllegalArgumentException();   
        }
        //Create a new order, call Vehicle.buildVehicle on the factory responsible for the given model to build the Vehicle
        Order tempOrder = new Order(rv, false);
        Vehicle tempVehicle = null;
            //There has to be a factory for ever vehicleModel in this manufacter bc they were made in the constructor, and this method already confirmed that the requested vehicle is a valid vehicle model for this manufacter
        for(Factory currentFactory: this.availableFactorySet)
        {
            if(rv.getModel().equals(currentFactory.getVehicleModel()))
            {
                //System.out.println("Found the right factory");
                //If the Vehicle cannot be built, then it will return null
                tempVehicle = currentFactory.buildVehicle(rv);
                //System.out.println("Vehicle: " + tempVehicle);
            }
        }
        //If parts for any chosen options are unavailable, call order.setUnavailableOptions(the unavailable options).
        Set<Option> unavailableOptions = new HashSet<>();
        for(Option o: rv.getChosenOptions())
        {
            if(this.haveSupplierForPart(o.getPartSpecification().getPartNumber()) == false)
            {
                unavailableOptions.add(o);
            }
        }

        //System.out.println("Unavailbe Options: " + unavailableOptions);

        tempOrder.setUnavailableOptions(unavailableOptions);

        //Save the vehicle in a map that maps an Order to a Vehicle.
        //System.out.println(tempOrder);
        //System.out.println(tempVehicle);
        this.completedOrderMap.put(tempOrder, tempVehicle);

        //Return the order
        return tempOrder;

    }

    /**
     * Set the supplier for spec to be supplier.
     * @param spec
     * @param supplier
     * @return the previous supplier of the part, null if there was none.
     */
    PartsSupplier changePartsSupplier(PartSpecification spec, PartsSupplier supplier) 
    {
        return this.partsSupplierMap.put(spec.getPartNumber(), supplier);
    }

    /**
     * complete manufacturing by adding in whatever options parts were missing in the vehicle for the corresponding order.
     * Throw IllegalStateException if there is no vehicle for the given order.
     * @param order
     * @return
     * @throws java.lang.IllegalStateException
     */
    public boolean retryCompletingVehicle(Order order) 
    {
        if(this.completedOrderMap.get(order) == null)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            //There has to be a factory for ever vehicleModel in this manufacter bc they were made in the constructor, and this method already confirmed that the requested vehicle is a valid vehicle model for this manufacter
            for(Factory currentFactory: this.availableFactorySet)
            {
                if(this.completedOrderMap.get(order).getModel().equals(currentFactory.getVehicleModel()))
                {
                    if(currentFactory.finishVehicle(order, this.completedOrderMap.get(order)))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
            System.out.println("Something went wrong bc this method should have never left the loop");
            return false;
        }
    }

    /**
     * get the vehicle that was built for the given order.
     * @param order
     * @return return the vehicle, IFF it is complete, i.e. not missing any parts, and null if not
     */
    public Vehicle getFinishedVehicle(Order order) 
    {
        if(order.allOptionsAreAvailable())
        {
            //System.out.println("unavailableOptions: " + order.unavailableOptions());
            //System.out.println("We are NOT returning null");
            //System.out.println(this.completedOrderMap.get(order));
            return this.completedOrderMap.get(order);
        }
        else 
        {
            //System.out.println("unavailableOptions: " + order.unavailableOptions());
            //System.out.println("We are returning null");
            return null;
        }
    }

    /**
     * submit an inquiry, checking if the vehicle described by RequestedVehicle can be built
     * @param v
     * @return
     */
    public Order doInquiry(RequestedVehicle v) 
    {
        Order currentOrder = new Order(v, true);

        //Does the factory have a base model available? If not, do suppliers have all the pieces to build a base model?
        //if base mode not available, don't even bother looking up options, return null

        for(Factory currentFactory: this.availableFactorySet)
        {
            //Determine the correct factory for the requested vehicle
            if(currentFactory.getVehicleModel().equals(v.getModel()))
            {
                //If there are no outstanding base models in the factory, can all the parts be built 
                if(currentFactory.getBaseModelInventory().size() < 0)
                {
                    for(PartSpecification partSpec: v.getModel().getBaseRequiredParts())
                    {
                        if(this.haveSupplierForPart(partSpec.getPartNumber()) == false)
                        {
                            return null;
                        }
                    }
                }
            }
        }
        //Do the suppliers have all the parts for the options?
        Set<Option> unavailableOptions = new HashSet<>();
        for(Factory currentFactory: this.availableFactorySet)
        {
            //Determine the correct factory for the requested vehicle
            if(currentFactory.getVehicleModel().equals(v.getModel()))
            {
                //If an option cannot be built, then update the set and after the loop, place the set in the order
                for(Option o: v.getChosenOptions())
                {
                    if(this.haveSupplierForPart(o.getPartSpecification().getPartNumber()) == false)
                    {
                        unavailableOptions.add(o);
                    }
                }
            }
        }
        //Set the order's unavailable option set to whatever options can't be supplied
        currentOrder.setUnavailableOptions(unavailableOptions);
        return currentOrder;
    }

    /**
     *
     * @param partNumber
     * @return true if there is a supplier for the given part, false if not
     */
    boolean haveSupplierForPart(int partNumber)
    {
        if(this.partsSupplierMap.get(partNumber) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}