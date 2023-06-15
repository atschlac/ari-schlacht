package edu.yu.cs.intro.automotive.manufacturing;

/**
 * Describes a part that a supplier can supply. You can think of this as an entry in a part supplier's product catalog.
 */
public class PartSpecification //CLASS IS DONE
{

    //Class Variables
    private int partNumber;
    private String description;
    private double price;

    /**
     * create a PartSpecification with the given part number, description, and price
     * @param number
     * @param description
     * @param price
     */
    PartSpecification(int number, String description, double price)
    {
        this.partNumber = number;
        this.description = description;
        this.price = price;
    }

    public int getPartNumber()
    {
        return this.partNumber;
    }

    public String getDescription()
    {
        return this.description;
    }

    double getPrice()
    {
        return this.price;
    }

    /**
     *
     * @return "Part Number: " + partNumber + " Description: " + description;
     */
    public String toString()
    {
        String toString = "Part Number: " + partNumber + " Description: " + description;
        return toString;
    }

    /**
     * two PartSpecifications are equal IFF their part numbers are equal
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) 
    {
        if(obj == null)
        {
            //System.out.println("obj is null");
            return false;
        }

        if(this == obj)
        {
            //System.out.println("obj is pointing to the same reference");
            return true;
        }

        if(this.getClass() != obj.getClass())
        {
            //System.out.println("obj is a different class");
            return false;
        }

        PartSpecification otherPartSpecification = (PartSpecification)obj;
        if(this.partNumber == otherPartSpecification.partNumber)
        {
            //System.out.println("Part numbers are the same");
            return true;
        }
        
        //System.out.println("Part numbers are not the same");
        return false;
    }

    /**
     * hashcode must be based on the part number
     * @return
     */
    @Override
    public int hashCode() 
    {
        Integer objPartNumber = (Integer)partNumber;
        return objPartNumber.hashCode();
    }
}