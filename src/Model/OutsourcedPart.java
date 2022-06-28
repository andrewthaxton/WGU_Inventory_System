package Model;

/**
 * Outsourced parts class inherits from the Part class.
 */

public class OutsourcedPart extends Part{

    private String companyName;

    /**
     * Constructor sets part ID,name,
     * price, stock, min, max, and
     * company name.
     * @param id The part ID.
     * @param name The part name.
     * @param price The price of the part.
     * @param stock The amount in stock.
     * @param min The minimum stock allowed.
     * @param max The maximum stock allowed.
     * @param companyName The name of the outsourcing company.
     */

    public OutsourcedPart(int id, String name, double price, int stock, int min, int max, String companyName) {
        //Calls Part constructor
        super(id, name, price, stock, min, max);

        //Sets company name
        this.companyName = companyName;
    }

    /**
     * The setCompanyName method sets the name of the outsourcing company.
     * @param companyName The name of the outsourcing company.
     */

    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    /**
     * The getCompanyName returns the name of the outsourcing company.
     * @return the company name
     */

    public String getCompanyName(){
        return companyName;
    }
}
