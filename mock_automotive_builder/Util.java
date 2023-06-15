package edu.yu.cs.intro.automotive.manufacturing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Random;

public class Util {
    /**
     * rounds a givne double to the given number of places
     * @param value
     * @param places
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0){
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    //order IDs
    private static int nextOrderID = new Random().nextInt(1,100000);

    /**
     * generates unique IDs to be used in instances of Order
     * @return the unique ID
     * @see Order#getOrderId()
     */
    protected static int getNextOrderId(){
        return nextOrderID++;
    }
    //part serial numbers
    private static int nextSerial = new Random().nextInt(1,100000);

    /**
     * generates unique serial numbers for instances of Part
     * @return the unique serial number
     * @see Part#getPartSerialNumber()
     */
    protected static int getNextSerialNumber(){
        return nextSerial++;
    }
    //part numbers
    private static int nextPartNumber = new Random().nextInt(1000,9999);

    /**
     * generates unique parts numbers for PartSpecifications
     * @return the unique part number
     * @see PartSpecification#getPartNumber()
     */
    protected static int getNextPartNumber(){
        return nextPartNumber++;
    }

    //vehicle VIN numbers
    private static HashSet<String> ALL_USED_VINS= new HashSet<>();
    static {
        ALL_USED_VINS.add("");
    }
    /**
     * Generates unique VIN numbers for vehicles.
     *
     * A vehicle identification number (VIN) is a unique code assigned to every motor vehicle when it's manufactured. The VIN is a 17-character string of letters and numbers.
     * For our purposes, we are going to define VIN as 3 numbers followed by an uppercase letter, repeated 4 times for a total of 16 characters, and then a 17th character number between 0 and 9
     * @return a new, unique VIN number
     * @see Vehicle#getVIN()
     */
    protected static String generateVIN(){
        String vin = "";
        while(ALL_USED_VINS.contains(vin)){
            for(int i = 0; i < 4; i++) {
                vin += Integer.toString((int)(100 + (Math.random() * 100)));
                vin += (char) ((int)(Math.random() * 26) + 65);
            }
            vin += Integer.toString((int)(Math.random() * 10));
        }
        ALL_USED_VINS.add(vin);
        return vin;
    }
}
