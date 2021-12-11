package com.marioJmartDR.model;

/**
 * Model untuk objek Product
 * @author Mario Claudius
 * @version 11 Desember 2021
 */

public class Product extends Serializable{
    public String name;
    public int weight;
    public boolean conditionUsed;
    public double price;
    public ProductCategory category;
    public int accountId;
    public double discount;
    public byte shipmentPlans;

    public String toString()
    {
        String str = "Name : " + this.name + "\nWeight : " + this.weight +
                "\nconditionUsed : " + this.conditionUsed + "\nprice : " +
                this.price + "\ncategory : " + this.category + "\ndiscount : " +
                this.discount + "\naccountId : " + this.accountId;
        return str;
    }
}
