package com.walmart.ticketservice.utils;

import org.junit.Assert;
import org.junit.Test;

public class TicketServiceUtilsTest {

    @Test
    public void testTestUniqueID() {
        int holdID1 = TicketServiceUtils.getHoldID();
        int holdID2 = TicketServiceUtils.getHoldID();

        Assert.assertNotNull(holdID1);
        Assert.assertNotNull(holdID2);

        Assert.assertNotEquals(holdID1, holdID2);

        Assert.assertEquals(holdID2-holdID1, 1);
    }
}
