package Model;

/**
 * In-house parts class
 * This class inherits from the Part class.
 */

public class InhousePart extends Part{

    private int machineId;

    /**
     * Constructor sets part ID,name,
     * price, stock, min, max, and
     * machine ID.
     * @param id The part ID.
     * @param name The part name.
     * @param price The price of the part.
     * @param stock The amount in stock.
     * @param min The minimum stock allowed.
     * @param max The maximum stock allowed.
     * @param machineId The ID of the machine creating the part.
     */

    public InhousePart(int id, String name, double price, int stock, int min, int max, int machineId) {
        //Calls Part constructor
        super(id, name, price, stock, min, max);

        //Sets company name
        this.machineId = machineId;
    }

    /**
     * The setMachineId method sets the ID number of the manufacturing machine.
     * @param machineId The ID number of the manufacturing machine.
     */

    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /**
     * The getMachineId returns the ID number of the manufacturing machine.
     * @return the machine ID
     */

    public int getMachineId(){
        return machineId;
    }
}
