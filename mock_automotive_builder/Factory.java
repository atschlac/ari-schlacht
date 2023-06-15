package edu.yu.cs.intro.automotive.manufacturing;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Talk to Manufacturer for all orders to part suppliers - factory remains ignorant of mapping of parts to suppliers.
 * Factory never deals directly with PartsSupplier - it always goes through the Manufacturer to get parts, and the Manufacturer deals with the PartsSupplier.
 */
class Factory 
{
    //Instance Variables
    private String name;
    private VehicleModel currentVehicleModel;
    private Manufacturer currentManufacturer;
    private List<Vehicle> baseModelInventoryList = new ArrayList<>();
    private int numOfCompletedVehicles = 0;

    /**
     * create a factory with the given name, which builds vehicles of the given model, owned by the given manufacturer
     * @param name
     * @param model
     * @param mfctr
     */
    Factory(String name, VehicleModel model, Manufacturer mfctr)
    {
        this.name = name;
        this.currentVehicleModel = model;
        this.currentManufacturer = mfctr;
    }

    /**
     * @return the VehicleModel manufactured in this factory
     */
    public VehicleModel getVehicleModel()
    {
        return this.currentVehicleModel;
    }

    /**
     *
     * @return "Factory: " + name + "\n" + "Manufacturer: " + manufacturer.getMake() + "\n" + "Model Built by Factory: " + vehicleModel.getModelName()
     */
    @Override
    public String toString()
    {
        String toString = "Factory: " + this.name + "\n" + "Manufacturer: " + this.currentManufacturer.getMake() + "\n" + "Model Built by Factory: " + currentVehicleModel.getModelName();
        return toString;
    }

    /**
     * @return the name that was passed in to the constructor when the object was created
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return the manufacturer that this factory builds vehicles for
     */
    public Manufacturer getManufacturer()
    {
        return this.currentManufacturer;
    }

    /**
     * @return an unmodifiable view of the inventory of vehicles that the factory has built with all the base parts, but have not yet been sold to a customer
     * @see java.util.Collections#unmodifiableList(List)
     */
    List<Vehicle> getBaseModelInventory()
    {
        List unmodifiableList = Collections.unmodifiableList(this.baseModelInventoryList);
        return unmodifiableList;
    }

    /**
     * Build a vehicle that has the options described in the given request.
     * 1) Make sure it's the model this factory produces. If not, throw an IllegalArgumentException
     * 2) if there is at least one prebuilt base vehicle use it for this request/order. If not, then build a new base vehicle. If a new base vehicle can not be built, return null.
     * 3) add all the requested options parts to the vehicle
     * 4) maintain the correct inventory level of built base vehicles
     * 5) return the built vehicle
     * @param requested
     * @return
     * @throws java.lang.IllegalArgumentException
     * @see #buildNewBaseVehicle()
     */
    Vehicle buildVehicle(RequestedVehicle requested)
    {
        //System.out.println("Started the building process");
     //1) Make sure it's the model this factory produces. If not, throw an IllegalArgumentException
        if(requested.getModel().equals(this.currentVehicleModel) == false)
        {
            throw new IllegalArgumentException();
        }

    //2) if there is at least one prebuilt base vehicle use it for this request/order. If not, then build a new base vehicle. If a new base vehicle can not be built, return null.
        Vehicle vehicleUnderConstruction = null;
        //Determine if there is an existing base vehicle in the List inventory
        if(this.baseModelInventoryList.size() > 0)
        {
            //Take out the first existing Vehicle in the List
            vehicleUnderConstruction = this.baseModelInventoryList.get(0);
            //Now remove the Vehicle that is currently under construction from the List of Inventory
            this.baseModelInventoryList.remove(0);
        }
        else
        {
            vehicleUnderConstruction = new Vehicle(this.currentVehicleModel);
            //If a new base vehicle can not be built, return null
            if(this.addBasePartsToVehicle(vehicleUnderConstruction) == false)
            {
                //System.out.println("Base vehicle cannot be built");
                return null;
            }
        }
    //3) add all the requested options parts to the vehicle
        //If there are any options missing bc the partsSupplier has been remove from the Manufacturer, the supplyPart method will chap NullPointerException and will be caught but nothing will happen bc one this vehicle is return, the manufacturer will log all the missing options
        //Iterate through the Options requested in the paramater requested Vehicle
        for(Option o: requested.getChosenOptions())
        {
            //add the current o (option) to the Vehicle by giving it the option and the actual part (which is built by requesting the part from the manufactuter)
            try
            {
                vehicleUnderConstruction.addOptionPart(o, this.currentManufacturer.supplyPart(o.getPartSpecification().getPartNumber()));
            }
            catch (NullPointerException e)
            {
                //System.out.println("There was a part missing");
            }
        }
        
    //4) maintain the correct inventory level of built base vehicles
        //At the end of this method update the numOfCompletedVehciles
        this.numOfCompletedVehicles++;
        //System.out.println(this.baseModelInventoryList.size()); //////////////////////////////////////////////////////////////////////////////////////////

        this.maintainInventoryLevel();

    //5) return the built vehicle
        //System.out.println("Returning the constructed Vehicle");
        return vehicleUnderConstruction;
    }

    /**
     * Try to complete the given vehicle for the given order.
     * 1) make sure it's the model this factory produces; if not, throw an IllegalArgumentException
     * 2) try to add parts for all its option parts that were missing
     * 3) update the order's record of what options are not available
     * 4) return true if there are no more missing options
     * @param order
     * @param vehicle
     * @return
     * @throws java.lang.IllegalArgumentException
     * @see Order#unavailableOptions()
     * @see Order#setUnavailableOptions(Set)
     */
    boolean finishVehicle(Order order, Vehicle vehicle)
    {
    //* 1) make sure it's the model this factory produces; if not, throw an IllegalArgumentException
        if(vehicle.getModel() != this.currentVehicleModel)
        {
            throw new IllegalArgumentException();
        }
    //* 2) try to add parts for all its option parts that were missing
        Set<Option> outstandingMissingParts = new HashSet<>();
        if(order.allOptionsAreAvailable() == false)
        {
            for(Option o: order.unavailableOptions())
            {
                if(this.currentManufacturer.haveSupplierForPart(o.getPartSpecification().getPartNumber()))
                {
                    vehicle.addOptionPart(o, this.currentManufacturer.supplyPart(o.getPartSpecification().getPartNumber()));
                }
                else
                {
                    outstandingMissingParts.add(o);
                }
            }
        }
    //* 3) update the order's record of what options are not available
        order.setUnavailableOptions(outstandingMissingParts);
    //* 4) return true if there are no more missing options*/
        if(order.allOptionsAreAvailable())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * If we have no base vehicles left, automatically replenish the inventory of completed base model vehicles, keeping inventory levels at 1/3 of total number of vehicles built to date.
     * @see #buildVehicle(RequestedVehicle)
     */
    private void maintainInventoryLevel()
    {       
            if(this.baseModelInventoryList.size() == 0)
            {
                //Determine how many more vehicles with base parts need to be made
                int amountToReplenish = (this.numOfCompletedVehicles / 3);
                //build new base vehicles and put them in the list of available base behicles
                for(int i = 0; i < amountToReplenish; i++)
                {
                    Vehicle vehicleUnderConstruction = new Vehicle(this.currentVehicleModel);
                    //I'm assuming that baseParts will be able to be added to the vehicle and that it will never return false
                    this.addBasePartsToVehicle(vehicleUnderConstruction);
                    this.baseModelInventoryList.add(vehicleUnderConstruction);
                }
            }
    }

    /**
     * Add the base parts to the given vehicle.
     * If all base parts are available, add them to the vehicle and return true. If some are missing, add those that are available to the base parts inventory (NOT the vehicle) and return false.
     * @param v
     * @return
     * @see VehicleModel#getBaseRequiredParts()
     * @see Vehicle#setBaseParts(Set)
     */
    boolean addBasePartsToVehicle(Vehicle v)
    {
        //Iterate through all the required base parts on the current vehicle model
        for(PartSpecification spec: this.currentVehicleModel.getBaseRequiredParts())
        {
            //System.out.println("Requried Part: " + spec);
            //Check if the spec number exists as a key in the partsSuppliers map in the Manufacturer's class, I.E. The part can be built
            if(this.currentManufacturer.haveSupplierForPart(spec.getPartNumber()) == false)
            {
                //System.out.println("Part is unavailable");
                //If the spec's partNum doesn't exist as a key in the map, theoretically add all the parts that are available in this manufacturer to a parts inventory, but don't actually cuz that inventory will never be used
                //add the parts that aren't missing to the inventory of base parts
                //Don't actually do anytning
                return false;
            }
        }
        //Leaving this loop means that there is a key in the map for every spec in the current vehicle model's PartSpec
        Set<Part> actualBasePartsSet = new HashSet<>();
        for(PartSpecification spec: this.currentVehicleModel.getBaseRequiredParts())
        {
            Part newPart = this.currentManufacturer.supplyPart(spec.getPartNumber());
            actualBasePartsSet.add(newPart);
        }
        
        v.setBaseParts(actualBasePartsSet);
        return true;
    }

    /**
     * build a new base vehicle and add it to the List which contains the inventory of built base vehicles
     * @return
     * @see #addBasePartsToVehicle(Vehicle)
     * @see java.util.Queue#add(Object)
     */
    boolean buildNewBaseVehicle()
    {
        //Instanciate a new Vehicle and then add the base parts to it
        Vehicle tempVehicle = new Vehicle(this.currentVehicleModel);
        if(this.addBasePartsToVehicle(tempVehicle) == false)
        {
            return false;
        }
        else
        {
            //Add a new Vehicle with its base parts to the List of inventory Vehicles
            this.baseModelInventoryList.add(tempVehicle);
            return true;
        }
    }
}