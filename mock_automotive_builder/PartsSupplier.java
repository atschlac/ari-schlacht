package edu.yu.cs.intro.automotive.manufacturing;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * Car manufacturers buy many of the parts that make up their cars from parts suppliers, e.g. https://en.wikipedia.org/wiki/Magna_International
 * This class models a supplier from whom an auto manufacturer can order parts.
 * The supplier must maintain an inventory of each of the parts it supplies. The number of each part on hand should be 1/3 of all previous orders.
 */
class PartsSupplier //CLASS IS DONE
{
    //Instance variables
    private String name;
    private Set<PartSpecification> suppliedPartSpecs = new HashSet<>();
    private Map<Integer, Integer> partInventoryNumbers = new HashMap<>();

    /**
     * creates a PartsSupplier whose name is set to the given name
     * @param name
     */
    PartsSupplier(String name)
    {
        this.name = name;
    }

    /**
     * Supply the given quantity of the given part. The partNumber should match the part number of a PartSpecification which is in this supplier's set of supplied parts specs.
     * Before returning, create more Part instances so that the inventory of the part is at the correct level - 1/3 of the number ordered in all previous orders, including this one.
     * If the given part number does not match any such spec's part number, or if quantity is <= 0 , throw an IllegalArgumentException
     * @param partNumber
     * @param quantity
     * @return a set of Part instances of size quantity, each of whose PartSpecification has the given part number
     * @see PartSpecification#getPartNumber()
     * @throws java.lang.IllegalArgumentException
     */
    Set<Part> supplyPart(int partNumber, int quantity) //Tzarich Iyun on this whole method (I think I did the first bit correctly and that the Parts are supposed to be created in addSuppliedPartSpecification and stored in a HashMap instance variable)
    {  
        this.isLegalInput(partNumber, quantity);
        this.addToPartInventoryNumbers(partNumber, quantity);   
        return this.getNewParts(partNumber, quantity);
    }
        private void isLegalInput(int partNumber, int quantity)
        {
            //Determine if this partNumber exists in the intance HashSet named suppliedPartSpecs
            if(!(this.isSuppliedPart(partNumber))|| quantity <= 0)
            {
                throw new IllegalArgumentException();
            }
        }

        private PartSpecification getPartSpec(int partNumber)
        {
            for(PartSpecification p: this.suppliedPartSpecs)
            {
            if(p.getPartNumber() == partNumber)
                {
                    return p;
                }
            }
            System.out.println("Consider this an exception, there is no such Part for the given partNumber");
        return null;
        }

        private void addToPartInventoryNumbers(int partNumber, int quantity)
        {
            if(this.partInventoryNumbers.containsKey(partNumber))
            {
                int tempQuantity = this.partInventoryNumbers.get(partNumber) + quantity;
                this.partInventoryNumbers.put(partNumber, tempQuantity);
            }
            else
            {
                this.partInventoryNumbers.put(partNumber, quantity);
            }
        }

        private Set<Part> getNewParts(int partNumber, int quantity)
        {
            //Add the given number of Parts to a Set and return it
            Set<Part> tempPartSupply = new HashSet<>(quantity);
            for(int i = 0; i < quantity; i++)
            {
                tempPartSupply.add(new Part(this.getPartSpec(partNumber)));
            }
            return tempPartSupply;
        }

    /**
     * add the given PartSpecification to the set of PartSpecifications that this supplier can supply parts of
     * @param p
     * @return true if the PartSpecification wasn't already among the set supplied by this supplier
     */
    boolean addSuppliedPartSpecification(PartSpecification p)
    {
        //Determine if p is already in the HashSet suppliedParts
        boolean isNotAlreadyHere = true;
        if(this.suppliedPartSpecs.contains(p))
        {
            isNotAlreadyHere = false;
        }

        //Add p to suppliedPartSpecs
        suppliedPartSpecs.add(p);

        return isNotAlreadyHere;
    }

    /**
     * remove the given PartSpecification from the set of PartSpecifications that this supplier can supply parts of
     * @param p
     * @return true if the PartSpecification was in the set before this method was called
     */
    boolean removeSuppliedPartSpecification(PartSpecification p)
    {
        //Determine if p is already in the HashSet suppliedPartSpecs
        boolean isAlreadyHere = this.suppliedPartSpecs.contains(p);

        //Add p to suppliedPartSpecs
        this.suppliedPartSpecs.remove(p);
        this.partInventoryNumbers.remove(p.getPartNumber());

        return isAlreadyHere;
    }

    /**
     *
     * @param partNumber
     * @return true if the given partNumber corresponds to the part number of one of the PartSpecifications that this supplier supplies parts of
     */
    boolean isSuppliedPart(int partNumber)
    {
        for(PartSpecification p: this.suppliedPartSpecs)
        {
            if(p.getPartNumber() == partNumber)
            {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @return an unmodifiable view of the set of PartSpecifications this supplier supplies parts of
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    Set<PartSpecification> getSuppliedPartSpecifications()
    {
        Set<PartSpecification> unmodifiableSet = Collections.unmodifiableSet(this.suppliedPartSpecs);
        return unmodifiableSet;
    }

    /**
     *
     * @param partNumber
     * @return the number of parts in stock whose PartSpecifications have the given part number
     */
    int getInventoryCount(int partNumber)
    {
        return this.partInventoryNumbers.get(partNumber) / 3;
    }

    /**
     *
     * @return a string which includes the suplier's name as well as the description, part number, and price of every part it supplies
     */
    public String toString()
    {
        String toString = "Name: " + this.name + " ";
        for(PartSpecification p: suppliedPartSpecs)
        {
            toString += "\n[" + p.toString() + " Price: " + p.getPrice() + "]";
        }
        return toString + "\n";
    }
}