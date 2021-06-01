package com.example.flightticketupgrader.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {

    Validation validation = new Validation();

    @Test
    void testWithValidRequests() {
        assertTrue(validation.validateEmail("arvind@gmail.com"));
        assertTrue(validation.validateEmail("arvind-kt@gmail.org"));
        assertTrue(validation.validateEmail("k123.abc@zzz.org"));
        assertTrue(validation.validateMobileNo("9786852995"));
        assertTrue(validation.validateMobileNo("6987543212"));
        assertTrue(validation.validatePNRNumber("ABC123"));
        assertTrue(validation.validatePNRNumber("ABCBD1"));
        assertTrue(validation.validatePNRNumber("ABCBDF"));
        assertTrue(validation.validatePNRNumber("1111DD"));
        assertTrue(validation.validateBookedCabin("Economy"));
        assertTrue(validation.validateTicketDate("2021-03-30", "2021-04-01"));
    }

    @Test
    void testWithInvalidRequests() {
        assertFalse(validation.validateEmail("arvind-kt@gmail"));
        assertFalse(validation.validateEmail("123.123@org"));
        assertFalse(validation.validateEmail("123-a@.abc"));
        assertFalse(validation.validateMobileNo("1234567890"));
        assertFalse(validation.validateMobileNo("543267"));
        assertFalse(validation.validateMobileNo("978685299"));
        assertFalse(validation.validateMobileNo("97868529951"));
        assertFalse(validation.validatePNRNumber("ABCDEF1"));
        assertFalse(validation.validatePNRNumber("11111AB"));
        assertFalse(validation.validatePNRNumber("1111A"));
        assertFalse(validation.validateBookedCabin(""));
        assertFalse(validation.validateBookedCabin("First class"));
        assertFalse(validation.validateBookedCabin("FirstClass"));
        assertFalse(validation.validateBookedCabin(""));
        assertFalse(validation.validateTicketDate("2021-05-31", "2021-04-20"));
    }
}
