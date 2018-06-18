package com.walmart.ticketservice.dto;

import com.walmart.ticketservice.enums.SeatState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Object representing the seating space of the event.
 * @author npasupuk
 */
public class EventSpace {

    public List<List<Seat>> getTotalSeats() {
        return totalSeats;
    }


    private List<List<Seat>> totalSeats;

    private final int totalRows;

    private final int maxSeatNumberForAnyRow;

    /**
     * build the event space when program starts.
     *
     * @param totalRows              - total rows in the space.
     * @param maxSeatNumberForAnyRow - maximum number of seats in any row.
     */
    public EventSpace(int totalRows, int maxSeatNumberForAnyRow) {
        this.totalRows = totalRows;
        this.maxSeatNumberForAnyRow = maxSeatNumberForAnyRow;
        initializeEventSpace();
    }


    /**
     * Run through total rows and columns, assign proper number to each seat and make them available for hold or reserve.
     */
    private void initializeEventSpace() {
        totalSeats = new ArrayList<>();
        for (int i = 0; i < totalRows; i++) {
            List<Seat> row = new ArrayList<>();
            for (int j = 0; j < maxSeatNumberForAnyRow; j++) {
                row.add(new Seat(i, j, SeatState.AVAILABLE));
            }
            totalSeats.add(row);
        }
    }

    /**
     * @return - returns the total seating capacity of this event space.
     */
    public int getCapacity() {
        return totalRows * maxSeatNumberForAnyRow;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventSpace)) return false;
        EventSpace that = (EventSpace) o;
        return totalRows == that.totalRows &&
                maxSeatNumberForAnyRow == that.maxSeatNumberForAnyRow &&
                Objects.equals(getTotalSeats(), that.getTotalSeats());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getTotalSeats(), totalRows, maxSeatNumberForAnyRow);
    }

    @Override
    public String toString() {
        return "EventSpace{" +
                totalSeats +
                '}';
    }
}
