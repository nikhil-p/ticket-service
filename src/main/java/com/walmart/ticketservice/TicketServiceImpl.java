package com.walmart.ticketservice;

import com.walmart.ticketservice.exceptions.TicketServiceException;
import com.walmart.ticketservice.utils.TicketServiceUtils;
import com.walmart.ticketservice.dto.EventSpace;
import com.walmart.ticketservice.dto.Seat;
import com.walmart.ticketservice.dto.SeatHold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.walmart.ticketservice.enums.TicketServiceErrorConstant.INVALID_HOLD_REQUEST;
import static com.walmart.ticketservice.enums.TicketServiceErrorConstant.NOT_ENOUGH_SEATS_TO_HOLD;
import static com.walmart.ticketservice.enums.TicketServiceErrorConstant.SEAT_HOLD_ID_NOT_FOUND;

/**
 * Implementation class for the ticket service.
 *
 * @author npasupuk.
 */
public class TicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);

    /**
     * represents the whole space for the event.
     */
    private EventSpace eventSpace;

    /**
     * map of ticket holds.
     */
    private Map<String, SeatHold> holds;

    /**
     * map to hold the reservation confirmations.
     */
    private Map<String, String> reservedConfirmations;

    /**
     * max time duration in seconds to hold the tickets.
     */
    private int maxHoldTimeInSeconds;


    /**
     * maintains the count of total reserved.
     */
    private int totalReserved;

    /**
     * Initialize the event space.
     */
    public TicketServiceImpl(int totalRows, int maxSeatsPerRow, int maxHoldTimeInSeconds) {
        eventSpace = new EventSpace(totalRows, maxSeatsPerRow);
        this.maxHoldTimeInSeconds = maxHoldTimeInSeconds;
        reservedConfirmations = new HashMap<>();
        holds = new HashMap<>();
        totalReserved = 0;
    }

    @Override
    public int numSeatsAvailable() {
        return eventSpace.getCapacity() - cleanUpExpiredHolds() - totalReserved;
    }


    /**
     * @return - returns the total seats on hold after cleaning up the expired holds.
     */
    private int cleanUpExpiredHolds() {

        LOGGER.info("cleaning up expired seats if any.");

        List<SeatHold> removedHolds = new ArrayList<>();

        //remove the expired holds from the map and add them to a list.
        holds.entrySet().removeIf(e -> {
            boolean isExpired = e.getValue().getExpiresIn().isBefore(Instant.now());
            if (isExpired)
                removedHolds.add(e.getValue());
            return isExpired;
        });

        //update the status of the expired holds to available.
        removedHolds.stream().map(seats -> seats.getOnHoldSeats())
                .flatMap(List::stream)
                .forEach(s -> s.markSeatAsAvailable());

        //now return the current holds.
        return holds.entrySet().stream().mapToInt(e -> e.getValue().getOnHoldSeats().size()).sum();
    }

    @Override
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {

        LOGGER.debug("Customer {} requested {} to hold.", customerEmail, numSeats);
        if (numSeats <= 0 || Objects.isNull(customerEmail) || customerEmail.isEmpty()) {
            LOGGER.info("Customer {} requested hold with number of seats {}.", customerEmail, numSeats);
            throw new TicketServiceException(INVALID_HOLD_REQUEST.getErrorCode(), INVALID_HOLD_REQUEST.getErrorMessage());
        }

        if(numSeats > numSeatsAvailable()){
            LOGGER.info("Customer {} requested more seats to hold than available {}.", customerEmail, numSeats);
            throw new TicketServiceException(NOT_ENOUGH_SEATS_TO_HOLD.getErrorCode(), NOT_ENOUGH_SEATS_TO_HOLD.getErrorMessage());
        }

        // clean up expired holds.
        cleanUpExpiredHolds();

        //now sequentially for available, giving each customer best possible seat.
        List<Seat> holdSeats = new ArrayList<>();
        eventSpace.getTotalSeats().stream().flatMap(s -> s.stream()).limit(numSeats).forEach(s -> {
            if (s.isSeatAvailable()) {
                s.markSeatAsOnHold();
                holdSeats.add(s);
            }
        });

        //create a seat hold object, push that to the map and then return the object.
        int confirmationID = TicketServiceUtils.getHoldID();
        SeatHold seatHold = new SeatHold(holdSeats, Instant.now().plusSeconds(maxHoldTimeInSeconds),
                customerEmail, confirmationID);

        //store the seat holds, using confirmID and email as the key in hashmap.
        holds.put(seatHold.getHoldConfirmationID(), seatHold);
        return seatHold;
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        //clean the expired holds. already taken care of the case if user trying to reserve an expired hold.
        cleanUpExpiredHolds();

        String holdConfirmationString = seatHoldId + customerEmail;

        //check if reservation already exists.
        if (Objects.nonNull(reservedConfirmations.get(holdConfirmationString))) {
            LOGGER.info("Reservation already exists for this email and hold id. so just returning the existing reservation ID.");
            return reservedConfirmations.get(seatHoldId + customerEmail);
        }

        SeatHold seatHold = holds.get(holdConfirmationString);

        //check if invalid holdid or customeremail.
        if (Objects.isNull(seatHold)) {
            LOGGER.info("customer {} is looking for a non existing hold id {}.", customerEmail, seatHold);
            throw new TicketServiceException(SEAT_HOLD_ID_NOT_FOUND.getErrorCode(), SEAT_HOLD_ID_NOT_FOUND.getErrorMessage());
        }

        List<Seat> seatsToMarkAsReserved = new ArrayList<>();

        //remove the seats which we want to mark as reserved from the holds map.
        holds.entrySet().removeIf(e -> {
            boolean foundObject = e.getKey().equals(seatHoldId + customerEmail);
            if (foundObject)
                seatsToMarkAsReserved.addAll(e.getValue().getOnHoldSeats());
            return foundObject;
        });

        //mark seats as reserved.
        seatsToMarkAsReserved.stream().forEach(s -> s.markSeatAsReserved());

        // incrementing the total reserved count.
        totalReserved += seatsToMarkAsReserved.size();

        //storing the reservation id in a map, for checking on future requests.
        String reservationID = TicketServiceUtils.getReservationID();
        reservedConfirmations.put(seatHoldId + customerEmail, reservationID);

        return reservationID;
    }
}
