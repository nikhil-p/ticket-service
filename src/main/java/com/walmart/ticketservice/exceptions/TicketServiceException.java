package com.walmart.ticketservice.exceptions;

import java.io.Serializable;

/**
 * An exception class, extending runtime, throwing all user errors as runtime exceptions.
 *
 * @author npasupuk
 */
public class TicketServiceException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    private int faultCode;
    private String message;

    /**
     * @param faultcode - error code which customer service would ask, if customer calls them when they encounter any error.
     * @param message   - user friendly message to send to customer, this is needs to be documents in userguide for the application.
     */
    public TicketServiceException(int faultcode, String message) {
        super(message);
        this.faultCode = faultcode;
        this.message = message;
    }

    public int getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(int faultCode) {
        this.faultCode = faultCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
