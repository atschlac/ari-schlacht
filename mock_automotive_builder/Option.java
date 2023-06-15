package edu.yu.cs.intro.automotive.manufacturing;

/**
 * represents an option that can be added to a vehicle
 */
public class Option //CLASS IS DONE
{

    //Instance variables
    String description;
    PartSpecification currentPartSpecification;


    /**
     * Create an option, with the given description, for adding the given optional part to a vehicle
     * @param description
     * @param partSpecification
     */
    Option(String description, PartSpecification partSpecification)
    {
        this.description = description;
        this.currentPartSpecification = partSpecification;
    }

    public String getDescription()
    {
        return this.description;
    }

    /**
     *
     * @return PartSpecification.getPrice() * 2, rounded to two decimal places
     * @see PartSpecification#getPrice()
     * @see Util#round(double, int)
     */
    public double getPrice()
    {
        return Util.round((currentPartSpecification.getPrice() * 2), 2);
    }

    PartSpecification getPartSpecification()
    {
        return this.currentPartSpecification;
    }

    /**
     *
     * @return "Option description: " + description + ". " + partSpecification.toString()
     */
    @Override
    public String toString()
    {
        String toString = "Option description: " + this.description + ". " + currentPartSpecification.toString();
        return toString;
    }

    /**
     * two options are equal if they have the same PartSpecification
     * @param obj
     * @return
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

        Option otherOption = (Option)obj;
        if(this.currentPartSpecification.equals(otherOption.currentPartSpecification))
        {
            //System.out.println("Part Specification numbers are the same");
            return true;
        }
        
        //System.out.println("Part specification numbers are not the same");
        return false;
    }

    @Override
    public int hashCode() 
    {
        return this.description.hashCode();
    }
}