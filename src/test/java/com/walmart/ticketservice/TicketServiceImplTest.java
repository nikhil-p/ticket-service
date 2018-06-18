
package com.walmart.ticketservice;

import com.walmart.ticketservice.dto.Seat;
import com.walmart.ticketservice.dto.SeatHold;
import com.walmart.ticketservice.enums.SeatState;
import com.walmart.ticketservice.exceptions.TicketServiceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail;


public class TicketServiceImplTest {

    private TicketServiceImpl ticketService;

    @Before
    public void setUp() {
        ticketService = new TicketServiceImpl(10, 10, 60);
    }

    @Test
    public void initializeTest() {
        Assert.assertEquals(ticketService.numSeatsAvailable(), 100);
    }

    @Test
    public void testHoldsPositiveFlow() throws InterruptedException {

        SeatHold seatHold = ticketService.findAndHoldSeats(3, "nikhil.kp11@gmail.com");
        Assert.assertEquals(ticketService.numSeatsAvailable(), 97);
        Assert.assertEquals(seatHold.getOnHoldSeats().size(), 3);
        Assert.assertEquals(seatHold.getCustomerEmail(), "nikhil.kp11@gmail.com");
        Thread.sleep(1000 * 65);
        Assert.assertEquals(ticketService.numSeatsAvailable(), 100);
    }

    @Test
    public void testReservePositive() {
        SeatHold seatHold = ticketService.findAndHoldSeats(5, "nikhil.kp11@gmail.com");
        Assert.assertNotNull(ticketService.reserveSeats(seatHold.getHoldID(), seatHold.getCustomerEmail()));
        Assert.assertEquals(ticketService.numSeatsAvailable(), 95);
    }

    @Test
    public void testRequestMoreSeatsThanAvailable() {
        try {
            ticketService.findAndHoldSeats(101, "nikhil.kp11@gmail.com");
            fail("TicketServiceException should have been thrown.");
        } catch (TicketServiceException exc) {
            Assert.assertEquals(exc.getFaultCode(), 1003);
            Assert.assertEquals(exc.getMessage(), "Not enough seats to hold. Please try again later.");
        }
    }

    @Test(expected = TicketServiceException.class)
    public void testReserveExpiredHolds() throws InterruptedException {
        SeatHold seatHold = ticketService.findAndHoldSeats(5, "nikhil.kp11@gmail.com");
        Thread.sleep(1000 * 65);
        ticketService.reserveSeats(seatHold.getHoldID(), seatHold.getCustomerEmail());
    }


    @Test(expected = TicketServiceException.class)
    public void testReserveWithNonExistentHoldID() {
        SeatHold seatHold = ticketService.findAndHoldSeats(5, "nikhil.kp11@gmail.com");
        ticketService.reserveSeats(1234, seatHold.getCustomerEmail());
    }

    @Test
    public void testReserveAnExistingReservation() {
        SeatHold seatHold = ticketService.findAndHoldSeats(5, "nikhil.kp11@gmail.com");
        String reservationID = ticketService.reserveSeats(seatHold.getHoldID(), seatHold.getCustomerEmail());
        String reservationID2 = ticketService.reserveSeats(seatHold.getHoldID(), seatHold.getCustomerEmail());
        Assert.assertEquals(reservationID, reservationID2);
    }

    @Test
    public void testHoldBestAvailableSeats() throws InterruptedException {

        Seat seat1 = new Seat(0, 0, SeatState.ON_HOLD);
        Seat seat2 = new Seat(0, 1, SeatState.ON_HOLD);
        SeatHold seatHold = ticketService.findAndHoldSeats(5, "nikhil.kp11@gmail.com");
        Thread.sleep(1000 * 65);
        SeatHold seatHold1 = ticketService.findAndHoldSeats(2, "nikhil.kp11@gmail.com");
        Assert.assertEquals(ticketService.numSeatsAvailable(), 98);
        List<Seat> onHolds = seatHold1.getOnHoldSeats();
        Assert.assertEquals(onHolds.get(0), seat1);
    }

}