package edu.yu.cs.intro.automotive.manufacturing;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Used to specify the details of the Vehicle a customer would like to order
 */
public class RequestedVehicle //CLASS IS DONE
{
    //Instance variables
    private VehicleModel requestedModel;
    private Set<Option> setOfChosenOptions = new HashSet<>();

    /**
     * create a new request regarding the given VehicleModel
     * @param model
     */
    public RequestedVehicle(VehicleModel model)
    {
        this.requestedModel = model;
    }

    /**
     *  creates a string representation of this RequestedVehicle
     * @return "Requested vehicle: " + the return value of calling toString() on the VehicleModel + the result of ("\n" + calling toString on each chosen option)
     * @see VehicleModel#toString()
     * @see Option#toString()
     */
    @Override
    public String toString()
    {        
        String toString = "Requested vehicle: " + this.requestedModel.toString();
        for(Option o: setOfChosenOptions)
        {
            toString += "\n" + o.toString();
        }

        return toString;
    }

    /**
     * @return the VehicleModel this request is asking about
     */
    public VehicleModel getModel()
    {
        return this.requestedModel;
    }

    /**
     * @return the price of the VehcileModel's base model + the price of all the options that were added to this request, rounded to 2 places
     * @see VehicleModel#getBasePrice()
     * @see Option#getPrice()
     * @see Util#round(double, int)
     */
    public double getConfiguredPrice()
    {
        double total = this.requestedModel.getBasePrice();
        
        for(Option o: setOfChosenOptions)
        {
            total += o.getPrice();
        }
        return Util.round(total, 2);
    }

    /**************************************************************************
     * CODE DEALING WITH THE CHOSEN OPTIONS
     **************************************************************************/

    /**
     * @return an umodifiable view of all the Options that have been included in this request
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    public Set<Option> getChosenOptions()
    {
        Set<Option> immmutableSet = Collections.unmodifiableSet(this.setOfChosenOptions);
        return immmutableSet;
    }

    /**
     * @param option the option to check on
     * @return true of the given option has been previously added to this request, false if not
     */
    boolean optionIsChosen(Option option)
    {
        return this.setOfChosenOptions.contains(option);
    }

    /**
     * add the given option to this request. Throw an IllegalArgumentException if the given option is not available in the request's VehicleModel
     * @param option
     * @return true if the request did not already include the given option
     * @see VehicleModel#hasOption(Option)
     * @throws java.lang.IllegalArgumentException
     */
    public boolean addChosenOption(Option option)
    {
        //Check if the the given Option is a valid Option
        if(this.requestedModel.hasOption(option) == false)
            {
                throw new IllegalArgumentException();
            }

        //Determine if this Option was already added to the set of chosen Options
        boolean doesExist = true;
        if(this.setOfChosenOptions.contains(option))
        {
            doesExist = false;
        }

        //Add the given Option to this classes chosen options
        this.setOfChosenOptions.add(option);

        return doesExist;

    }

    /**
     * replace the existing set of chosen options with the set being passed in here. If any of the options in the set being passed in are not available in the request's VehicleModel, throw an IllegalArgumentException and DO NOT replace the set of chosen options, rather leave it as it was before this method was called
     * @param options
     * @return the old set of chosen options, which has been entirely replaced by the given set of options
     * @throws java.lang.IllegalArgumentException
     */
    public Set<Option> setChosenOptions(Set<Option> options)
    {
        //Check if new set of Options are valid
        for(Option o: options)
        {
            if(!(this.requestedModel.hasOption(o)))
            {
                throw new IllegalArgumentException();
            }
        }
        //Make a set of the old options in order to return them later 
        HashSet<Option> oldChosenOptions = (HashSet)this.setOfChosenOptions;
        
        //Set the new options to this.setOfChosenOptions
        this.setOfChosenOptions = options;
        
        //Return the old set of options
        return oldChosenOptions;
    }

    /**
     * remove the given option from the set of options chosen for this vehicle request
     * @param option
     * @return true of the option was previously chosen
     */
    public boolean removeChosenOption(Option option)
    {
        boolean doesExist = this.setOfChosenOptions.contains(option);
        
        //Remove the give option from the set of chosen options 
        this.setOfChosenOptions.remove(option);

        return doesExist;
    }
}