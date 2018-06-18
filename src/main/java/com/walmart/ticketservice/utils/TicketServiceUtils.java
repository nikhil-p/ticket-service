package com.walmart.ticketservice.utils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * A utilities class for the ticket service.
 * @author npasupuk.
 */
public class TicketServiceUtils {
    /**
     * @return- returns unique id which is an integer, id only unique within this process
     */
    private static  AtomicInteger atomicInteger = new AtomicInteger(10000);

    private static Supplier<Integer> getUniqueIntID = () -> atomicInteger.incrementAndGet();

    /**
     * @return- returns unique uuid which is a string.
     */
    private static Supplier<String> getUniqueID = () -> UUID.randomUUID().toString();


    public synchronized static int getHoldID() {
        return getUniqueIntID.get();
    }

    public synchronized static String getReservationID() {
        return getUniqueID.get();
    }
}
