package com.walmart.ticketservice.dto;

import com.walmart.ticketservice.enums.SeatState;

import java.time.Instant;
import java.util.Objects;

/**
 * Class representing any seat in the place where event is happening.
 * seats are numbered in the format {rowNumber}{seatNumber}.
 * @author npasupuk
 */
public class Seat {

    private final int rowNumber;
    private final int seatNumber;
    private SeatState seatState;

    /**
     * @param rowNumber  - row number of the seat.
     * @param seatNumber - seat number of the seat.
     * @param seatState  - current state of the state.
     */
    public Seat(int rowNumber, int seatNumber, SeatState seatState) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.seatState = seatState;
    }

    /**
     * @return - returns true is seat neither reserved or held.
     */
    public boolean isSeatAvailable() {
        return !seatState.equals(SeatState.ON_HOLD) || !seatState.equals(SeatState.RESERVED);
    }

    /**
     * changes the state of this seat to reserved.
     */
    public void markSeatAsReserved() {
        seatState = SeatState.RESERVED;
    }

    /**
     * changes the state of this seat to on-hold.
     */
    public void markSeatAsOnHold() {
        seatState = SeatState.ON_HOLD;
    }

    /**
     * changes the state of this seat to available.
     */
    public void markSeatAsAvailable() {
        seatState = SeatState.AVAILABLE;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "rowNumber=" + rowNumber +
                ", seatNumber=" + seatNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat)) return false;
        Seat seat = (Seat) o;
        return rowNumber == seat.rowNumber &&
                seatNumber == seat.seatNumber &&
                seatState == seat.seatState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNumber, seatNumber, seatState);
    }
}
