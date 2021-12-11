package com.marioJmartDR.model;

import java.util.Date;

/**
 * Model untuk objek Invoice
 * @author Mario Claudius
 * @version 11 Desember 2021
 */

public abstract class Invoice extends Serializable{
    public enum Status
    {
        WAITING_CONFIRMATION,
        CANCELLED,
        DELIVERED,
        ON_PROGRESS,
        ON_DELIVERY,
        COMPLAINT,
        FINISHED,
        FAILED
    }

    enum Rating
    {
        NONE,
        BAD,
        NEUTRAL,
        GOOD
    }

    public Date date;           //harusnya final di backend
    public int buyerId;
    public int productId;
    public int complaintId;
    public Rating rating;
}
