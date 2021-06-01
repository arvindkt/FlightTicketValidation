package com.example.flightticketupgrader;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FlightTicketUpgraderTest {

    FlightTicketUpgrader l_objFlightTicketUpgrader = new FlightTicketUpgrader();

    @Test
    void checkInputFileExits() {
        File file = new File("src/main/resources/input/input.csv");
        assertTrue(file.exists());
    }

    @Test
    void testOfferCode() {
        assertEquals("OFFER_20", l_objFlightTicketUpgrader.getOfferCode('A'));
        assertEquals("", l_objFlightTicketUpgrader.getOfferCode('Z'));

    }
}
