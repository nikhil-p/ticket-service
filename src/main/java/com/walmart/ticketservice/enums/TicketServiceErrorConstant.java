package com.walmart.ticketservice.enums;

/**
 * enum class which has all the error message and codes related to ticket service.
 * @author npasupuk
 */
public enum TicketServiceErrorConstant {

    SEAT_HOLD_ID_NOT_FOUND(1001, "Your reservation was requested either with an invalid Hold ID or an invalid email."),
    INVALID_HOLD_REQUEST(1002, "Please request atleast one seat to hold."),
    NOT_ENOUGH_SEATS_TO_HOLD(1003, "Not enough seats to hold. Please try again later.");

    /**
     * Error code.
     */
    private int errorCode;
    /**
     * Error message.
     */
    private String errorMessage;

    /**
     * Parameterized constructor.
     *
     * @param errorCode    - Error code
     * @param errorMessage - Error message
     */
    TicketServiceErrorConstant(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Get error message.
     *
     * @return - Error message.
     */
    public final String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Get error code.
     *
     * @return - error code.
     */
    public int getErrorCode() {
        return this.errorCode;
    }
}
