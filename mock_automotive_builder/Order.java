package edu.yu.cs.intro.automotive.manufacturing;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an order placed for a car with a manufacturer. Can also represent an inquiry, i.e. just checking if a vehicle of the given model & options COULD be ordered
 */
public class Order 
{
    //INSTANCE VARIABLES
    private RequestedVehicle currentRequestedVehicle;
    private boolean inquiryOnly;
    private long userID;
    private Set<Option> unavailableOptionsSet = new HashSet<>();

    /**
     * create an order for the given requestedVehicle. If inquiryOnly is true, this is just checking if the vehicle COULD be built, but is not actually ordering it.
     * Generate and save a unique ID for the order.
     * @param requestedVehicle
     * @param inquiryOnly
     * @see Util#getNextOrderId()
     */
    Order(RequestedVehicle requestedVehicle, boolean inquiryOnly)
    {
        this.currentRequestedVehicle = requestedVehicle;
        this.inquiryOnly = inquiryOnly;
        this.userID = Util.getNextOrderId(); //This method returns an int, im assuming it will get casted automatically into a long

    }

    public boolean isInquiryOnly()
    {
        return this.inquiryOnly;
    }

    public RequestedVehicle getRequestedVehicle()
    {
        return this.currentRequestedVehicle;
    }

    public long getOrderId()
    {
        return userID;
    }

    /**
     * true if there are NO unavailable options, i.e. parts are available for all the options in this order
     * @return
     */
    public boolean allOptionsAreAvailable()
    {
        //Iterate through all the options that the current Requested Vehicle Requires
        for(Option o: this.currentRequestedVehicle.getChosenOptions())
        {
            //If the o (option on requested vehicle) is not in the Set of unavailableOptions, then we are good, if it is there, then return false bc that option on the requestedVehicle is unavailable
            if(this.unavailableOptionsSet.contains(o))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * used by manufacturer to indicate which options in this request there are no parts for
     * @param unavailableOptions
     */
    void setUnavailableOptions(Set<Option> unavailableOptions)
    {
        this.unavailableOptionsSet = new HashSet((HashSet) unavailableOptions);
    }

    /**
     * get an unmodifiable view of the set of unavailable options
     * @return
     */
    public Set<Option> unavailableOptions()
    {
        Set<Option> unmodifiableSet = Collections.unmodifiableSet(this.unavailableOptionsSet);
        return unmodifiableSet; 
    }

    /**
     * two orders are equal if they have the same order id
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) 
    {
        {
        if(o == null)
        {
            //System.out.println("o is null");
            return false;
        }

        if(this == o)
        {
            //System.out.println("o is pointing to the same reference");
            return true;
        }

        if(this.getClass() != o.getClass())
        {
            //System.out.println("o is a different class");
            return false;
        }

        Order otherOrder = (Order)o;
        if(this.userID == otherOrder.userID)
        {
            //System.out.println("Part numbers are the same");
            return true;
        }
        
        //System.out.println("Part numbers and SN are not the same");
        return false;
        }
    }

    /**
     * based on order id
     * @return
     */
    @Override
    public int hashCode() 
    {
        int tempUserID = (int)this.userID;
        return tempUserID;
    }
}