package edu.yu.cs.intro.automotive.manufacturing;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Describes a model / product line of vehicle, e.g. Honda Accord, Ford F150, etc.
 * This class does NOT describe an actual/specific vehicle that would be sold to a customer.
 */
public class VehicleModel //CLASS IS DONE
{
    //Instance variables
    private String makeName;
    private String modelName;
    private double basePrice = 0.0;
    private Set<PartSpecification> requiredBaseParts;
    private Set<Option> availableOptions = new HashSet<>();

    /**
     *
     * @param make the company that makes this model, e.g. Honda, Ford, Jeep, etc.
     * @param model the name of the model, e.g. Accord, F150, Cherokee, etc.
     */
    VehicleModel(String make, String model)
    {
        this.makeName = make;
        this.modelName = model;
    }

    /**
     *
     * @return the base price of this model.
     * @see #recalculateBasePrice()
     */
    public double getBasePrice()
    {
        return this.basePrice;
    }

    public String getModelName()
    {
        return this.modelName;
    }

    public String getMake()
    {
        return this.makeName;
    }

    void setBaseRequiredParts(Set<PartSpecification> parts)
    {
        //This assumes that parts is a HashSet (if it's not a HashSet, the casting will throw a runtime error)
        this.requiredBaseParts = new HashSet<PartSpecification>((HashSet)parts);

        //Set basePrice
        for(PartSpecification basePart:requiredBaseParts)
        {
            this.basePrice += basePart.getPrice();
        }

        this.basePrice *= 2;
        this.basePrice = Util.round(this.basePrice, 2);

    }

    /**
     * the base price is calculated by adding up the prices of the PartSpecification of all the required base parts, multiplying by 2, and rounding to two decimal places
     * @return the base price
     * @see PartSpecification#getPrice()
     * @see Util#round(double, int)
     */
    double recalculateBasePrice()
    {
        Double newBasePrice = 0.0;
        for(PartSpecification basePart:requiredBaseParts)
        {
            newBasePrice += basePart.getPrice();
        }

        newBasePrice *= 2;
        newBasePrice = Util.round(this.basePrice, 2);
        this.basePrice = newBasePrice;
        return basePrice;
    }

    /**
     * Must return an unmodifiable view of the parts that are required to build the base model
     * @return the set of PartSpecification that describe all the base parts required to build this model with no options added
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    Set<PartSpecification> getBaseRequiredParts()
    {
        Set<PartSpecification> unmodifiableBasePartsSet = Collections.unmodifiableSet(this.requiredBaseParts);
        return unmodifiableBasePartsSet;
    }

    /**
     *
     * @param option the option to check if it is available on this model
     * @return true if the option passed as the parameter is an option that can be added to this model, otherwise false
     */
    public boolean hasOption(Option option)
    {
        return this.availableOptions.contains(option);
    }

    /**
     * Must return an unmodifiable view of all the options available on this model
     * @return the set of options available on this model
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    public Set<Option> getModelOptions()
    {
        Set<Option> unmodifiableAvailableOptionsSet = Collections.unmodifiableSet(this.availableOptions);
        return unmodifiableAvailableOptionsSet;
    }

    /**
     * adds the given option to the set of options available in this model
     * @param option
     * @return true if this option was not already available in this model, otherwise false
     */
    boolean addModelOption(Option option)
    {
        //Determine if this option is already in the hashSet
        boolean isNotAlreadyInSet = true;
        if(this.availableOptions.contains(option))
        {
            isNotAlreadyInSet = false;
        }

        //Add this option to the hashSet
        this.availableOptions.add(option);

        //Return a boolean indicating if this option was already in the hashSet before this method
        return isNotAlreadyInSet;
    }

    /**
     * removes the given option from the set of options available in this model
     * @param option
     * @return true if this option was available in this model before this method was called, otherwise false
     */
    boolean removeModelOptions(Option option)
    {
        //Determine if this option is already in the hashSet
        boolean isAlreadyInSet = this.availableOptions.contains(option);

        //Remove this option to the hashSet
        this.availableOptions.remove(option);

        //Return a boolean indicating if this option was already in the hashSet before this method
        return isAlreadyInSet;

    }

    /**
     * must return the make followed by space followed by the model, e.g. "Honda Accord", "Nissan Maxima", etc.
     * @return a string representation of this model
     */
    @Override
    public String toString() 
    {
        String toString = this.makeName + " " + this.modelName;
        return toString;   
    }

    /**
     * Tests if the given object is logically identical to this object. They are logically equivalent IFF obj is an instance of VehicleModel AND it has the same make and model as this instance
     * @param obj
     * @return true if they are equal, false if not
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

        if(!(obj instanceof VehicleModel))
        {
            //System.out.println("o is a different class");
            return false;
        }

        VehicleModel otherVehicleModel = (VehicleModel)obj;
        if(obj instanceof VehicleModel && this.modelName.equals(otherVehicleModel.modelName) && this.makeName.equals(otherVehicleModel.makeName))
        {
            //System.out.println("Model and Make are the same");
            return true;
        }
        
        //System.out.println("Model and Make are not the same");
        return false;
    }

    /**
     * the hashcode should be calculated by 31 multiplied by the hashcode of the model, and then adding the hashcode of the make.
     * @return
     */
    @Override
    public int hashCode() 
    {
        //Tzarich iyun, bc the result is a lower number than it should be but when using smaller numbers it evaluates correctly...
        //System.out.println(modelName.hashCode() + " * 31 + " + makeName.hashCode());
        return (this.modelName.hashCode() * 31) + this.makeName.hashCode();
    }
}