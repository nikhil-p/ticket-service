package com.walmart.ticketservice.dto;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Represents the seat hold object.
 *
 * @author npasupuk
 */
public class SeatHold {

    private final int holdID;
    private final List<Seat> onHoldSeats;
    private final Instant expiresIn;
    private final String customerEmail;

    /**
     * @param holdSeats     - seats which are held.
     * @param expiresIn-    time when hold will expire.
     * @param customerEmail - email of the customer.
     */
    public SeatHold(List<Seat> holdSeats, Instant expiresIn, String customerEmail, int holdID) {
        this.onHoldSeats = holdSeats;
        this.expiresIn = expiresIn;
        this.customerEmail = customerEmail;
        this.holdID = holdID;
    }

    /**
     * @return holdConfirmationNumber - its the combination of holdID plus customerEmail.
     */
    public String getHoldConfirmationID() {
        return holdID + customerEmail;
    }


    /**
     * @return - expiry time for this hold.
     */
    public Instant getExpiresIn() {
        return expiresIn;
    }

    /**
     * @return - seats which are on hold.
     */
    public List<Seat> getOnHoldSeats() {
        return onHoldSeats;
    }

    /**
     * @return - customer email for the hold.
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * @return - return the hold id, getters are mainly used for tests.
     */
    public int getHoldID() {
        return holdID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeatHold)) return false;
        SeatHold seatHold = (SeatHold) o;
        return holdID == seatHold.holdID &&
                Objects.equals(getOnHoldSeats(), seatHold.getOnHoldSeats()) &&
                Objects.equals(getExpiresIn(), seatHold.getExpiresIn()) &&
                Objects.equals(getCustomerEmail(), seatHold.getCustomerEmail());
    }

    @Override
    public int hashCode() {

        return Objects.hash(holdID, getOnHoldSeats(), getExpiresIn(), getCustomerEmail());
    }
}
