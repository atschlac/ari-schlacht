package edu.yu.cs.intro.automotive.manufacturing;

/**
 * Represents a specific, actual part, built according a part specification
 */
public class Part //CLASS IS DONE
{

    //Class variables
    private PartSpecification currentPartSpecification;
    private int partSerialNumber;

    /**
     * create a new Part with the given PartSpecification
     * if specification is null, throw IllegalArgument Exception
     * generate and save a unique serial number for the part
     * @param specification
     * @see java.lang.IllegalArgumentException
     * @see Util#getNextSerialNumber()
     */
    Part(PartSpecification specification)
    {
        if(specification == null)
        {
            throw new IllegalArgumentException();
        }

        this.currentPartSpecification = specification;
        this.partSerialNumber = Util.getNextSerialNumber();
    }

    public PartSpecification getPartSpecification()
    {
        return this.currentPartSpecification;
    }

    public int getPartSerialNumber()
    {
        return this.partSerialNumber;
    }

    /**
     *
     * @return "Serial Number: " + partSerialNumber + ". " + PartSpecification.toString()
     */
    @Override
    public String toString()
    {
        String toString = "Serial Number: " + this.partSerialNumber + ". " + this.currentPartSpecification.toString();
        return toString;
    }

    /**
     * two Parts are equal only if they have the same serial number and the same PartSpecification
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) 
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

        Part otherPart = (Part)o;
        if(this.partSerialNumber == otherPart.partSerialNumber && this.currentPartSpecification.equals(otherPart.currentPartSpecification))
        {
            //System.out.println("Part numbers are the same");
            return true;
        }
        
        //System.out.println("Part numbers and SN are not the same");
        return false;
    }

    /**
     * calculated based on the hashcode of the specification AND the part serial number
     * @return
     */
    @Override
    public int hashCode() 
    {
        return this.currentPartSpecification.hashCode() * this.partSerialNumber;
    }
}