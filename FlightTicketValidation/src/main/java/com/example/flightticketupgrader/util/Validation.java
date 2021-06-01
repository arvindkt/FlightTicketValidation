package com.example.flightticketupgrader.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * The Class Validation.
 */
public class Validation {

    /**
     * validate email.
     *
     * @param p_sEmail
     *            the p_sEmail
     *
     * @return the boolean value
     */
    public boolean validateEmail (String p_sEmail) {
        String l_sEmailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(l_sEmailRegex);
        return pattern.matcher(p_sEmail).matches();
    }

    /**
     * validate mobile number
     *
     * @param p_sMobileNo
     *            the p_sMobileNo
     *
     * @return the boolean value
     */
    public boolean validateMobileNo (String p_sMobileNo) {
        String l_sMobileRegex = "(0/91)?[6-9][0-9]{9}";
        Pattern pattern = Pattern.compile(l_sMobileRegex);
        return pattern.matcher(p_sMobileNo).matches();

    }

    /**
     * validate Ticket Date
     *
     * @param p_sTicketDate
     *            the p_sTicketDate
     *  @param p_sTravelDate
     *           the p_sTravelDate
     *
     * @return the boolean value
     */
    public boolean validateTicketDate (String p_sTicketDate, String p_sTravelDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date l_dateTicketDate = simpleDateFormat.parse(p_sTicketDate);
            Date l_dateTravelDate = simpleDateFormat.parse(p_sTravelDate);
            if (l_dateTicketDate.compareTo(l_dateTravelDate) > 0) {
                return false;
            }
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return true;
    }

    /**
     * validate PNR number
     *
     * @param p_sPnrNumber
     *            the p_sPnrNumber
     *
     * @return the boolean value
     */
    public boolean validatePNRNumber (String p_sPnrNumber) {
        String l_sPnrRegex = "^[a-zA-Z0-9]{6}+$";
        Pattern pattern = Pattern.compile(l_sPnrRegex);
        return pattern.matcher(p_sPnrNumber).matches();
    }

    /**
     * validate Booked cabin
     *
     * @param p_sBookedCabin
     *            the p_sBookedCabin
     *
     * @return the boolean value
     */
    public boolean validateBookedCabin (String p_sBookedCabin) {
        String[] l_arrBookedCabin =  {"Economy", "Premium Economy", "Business", "First"};
        return Arrays.asList(l_arrBookedCabin).contains(p_sBookedCabin);
    }
}

