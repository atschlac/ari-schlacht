package edu.yu.cs.intro.automotive.manufacturing;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * Describes an actual/specific vehicle that would be sold to a customer.
 * Each vehicle stores:
 * 1) the Vehicle model which this vehicle is one of
 * 2) a VIN number
 * 3) the base parts that were used to build this vehicle
 * 4) the options parts that were used to build this vehicle
 */
public class Vehicle //CLASS IS DONE
{
    private VehicleModel currentVehicleModel;
    private String currentVIN;
    private Set<Part> requiredBasePartsSet = new HashSet<> (); //Set in the method setBaseParts
    private Map<Option, Part> currentOptionsMap; //Set in the constructor

    /**
     * Hold on to the model that this vehicle is one of, and generate and store the vehicle's VIN number
     * @param model
     * @see Util#generateVIN() 
     */
    public Vehicle(VehicleModel model)
    {
        this.currentVehicleModel = model;
        this.currentVIN = Util.generateVIN();
        this.currentOptionsMap = new HashMap<>();
    }

    /**
     * @return this vehicle's VIN number
     */
    public String getVIN()
    {
        return this.currentVIN;
    }

    /**
     * @return the result of calling toString on this vehcile's VehicleModel + "; VIN: " + this vehicle's VIN number
     */
    public String toString()
    {
        String toString = this.currentVehicleModel.toString() + "; VIN: " + this.currentVIN;
        return toString;
    }

    /**
     * @return this vehicle's VehicleModel
     */
    public VehicleModel getModel()
    {
        return this.currentVehicleModel;
    }

    /**
     * @return the price of the VehcileModel's base model + the price of all the options that were added to this vehicle, rounded to 2 places
     * @see VehicleModel#getBasePrice() 
     * @see Option#getPrice() 
     * @see Util#round(double, int) 
     */
    public double getBuiltPrice() ///COME BACK TO THIS METHOD
    {
        double total = this.currentVehicleModel.getBasePrice();
        for(Map.Entry<Option,Part> currentPart: this.currentOptionsMap.entrySet())
        {
            total += currentPart.getKey().getPrice();
        }

        return Util.round(total, 2);
    }

    /**
     * @return true if this Vehicle has a Part for every required part in the base model, otherwise false
     * @see VehicleModel#getBaseRequiredParts() 
     * @see Part
     */
    public boolean isBaseComplete() //FIX THIS METHOD
    {
        return this.hasAllRequiredBaseParts(this.requiredBasePartsSet);
    }

    /**
     * The caller of this method must pass in a Set that contains one Part for very required base part in this vehicle's model.
     * If the set is missing one or more required part, throw an IllegalArgumentException
     * @param baseParts the parts to use for all of this vehicle's base parts
     * @see #hasAllRequiredBaseParts(java.util.Set) 
     * @throws java.lang.IllegalArgumentException
     */
    void setBaseParts(Set<Part> baseParts)
    {
        if(this.hasAllRequiredBaseParts(baseParts) == false)
        {
            throw new IllegalArgumentException();
        }

        this.requiredBasePartsSet = new HashSet<> ((HashSet)baseParts);
    }

    /**
     * 
     * @return an unmodifiable view of the set of base parts in this vehicle
     * @see java.util.Collections#unmodifiableSet(java.util.Set) 
     */
    Set<Part> getBaseParts()
    {
        Set<Part> unmodifiableSet = Collections.unmodifiableSet(this.requiredBasePartsSet);
        return unmodifiableSet;
    }

    /**
     * checks if the given set of parts has all of the parts required to build the base model of this vehicle
     * @param baseParts
     * @return true if the set has all the required parts, otherwise false
     */
    boolean hasAllRequiredBaseParts(Set<Part> baseParts)
    {
        //System.out.println(baseParts);

        //Iterate through the required PartSpecs in this VehicleModel
        for(PartSpecification requiredSpec: this.currentVehicleModel.getBaseRequiredParts())
        {
            boolean isHere = false;

            //Iterate through each Part in this Vehicle's parts and deteremine if the Part's PartSpecs are equal to the required spec
            for(Part currentPart: baseParts)
            {
                
                if(requiredSpec.equals(currentPart.getPartSpecification()))
                {
                    isHere = true;
                }
                //System.out.println(currentPart.getPartSpecification());
                //System.out.println(requiredSpec);
                //System.out.println(isHere);                
                //System.out.println();
            }
            //System.out.println("Iteration Complete");
            if(isHere == false)
            {
                return isHere;
            }
        }
        return true;
    }

    /**
     * @return an unmodifiable view of the options->parts map in this vehicle, which maps a selected Option to the actual part being used to build that option into this vehicle
     * @see java.util.Collections#unmodifiableMap(java.util.Map)
     */
    public Map<Option,Part> getOptionParts()
    {
        return Collections.unmodifiableMap(this.currentOptionsMap);
    }

    /**
     * Makes the given part be the actual part to be used to build the given option in this vehicle.
     * @param option the Option that the given part is to be used for
     * @param part the part to use for the given option
     * @return if there was already a part stored to be used for the given Option, return that old part (which has now replaced), otherwise return null
     * @throws java.lang.IllegalArgumentException part spec of the option and the part do not match OR if the given Option is not available on this vehicle's VehicleMOdel
     * @see VehicleModel#hasOption(Option) 
     * @see Option#getPartSpecification() 
     * @see Part#getPartSpecification() 
     */
    Part addOptionPart(Option option, Part part)
    {
        if(option.getPartSpecification() != part.getPartSpecification() || this.currentVehicleModel.hasOption(option) == false)
        {
            throw new IllegalArgumentException();
        }

        return this.currentOptionsMap.put(option, part);
    }

    /**
     * two Vehicles are equal if they have the same VehicleModel AND the same VIN number
     * @param obj
     * @return true if equal, otherwise false
     */
    @Override
    public boolean equals(Object obj) 
    {
        if(obj == null)
        {
            //System.out.println("o is null");
            return false;
        }

        if(this == obj)
        {
            //System.out.println("o is pointing to the same reference");
            return true;
        }

        if(this.getClass() != obj.getClass())
        {
            //System.out.println("o is a different class");
            return false;
        }

        Vehicle otherVehicle = (Vehicle)obj;
        if(this.currentVehicleModel.equals(otherVehicle.currentVehicleModel) && this.currentVIN.equals(otherVehicle.currentVIN))
        {
            System.out.println("This is only true if the Vehicle's are pointing to the same reference, bc the VIN number is different for each Vehicle object, this code will never run bc it will turn true in one of the earlier conditions");
            return true;
        }
        
        System.out.println("Part numbers and SN are not the same");
        return false;
    }

    /**
     * @return the hashcode, calculated by 31 * the hashcode of the VehicleModel + the haschode of the VIN number
     */
    @Override
    public int hashCode() 
    {
        return 31 * this.currentVehicleModel.hashCode() + currentVIN.hashCode();
    }
}