package com.example.flightticketupgrader;

import com.example.flightticketupgrader.modal.TicketDetailsVO;

import com.example.flightticketupgrader.util.Validation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The Class FlightTicketUpgrader
 */
public class FlightTicketUpgrader {

    private static final Logger logger = Logger.getLogger(FlightTicketUpgrader.class.getName());

    /**
     * The main method to run the program.
     *
     * @param args
     *      the arguments
     */
    public static void main(String[] args) {
        FlightTicketUpgrader l_objFlightTicketUpgrader = new FlightTicketUpgrader();
        List<TicketDetailsVO> l_lstTicketDetails = l_objFlightTicketUpgrader.read();
        if (!l_lstTicketDetails.isEmpty()) {
            l_objFlightTicketUpgrader.process(l_objFlightTicketUpgrader, l_lstTicketDetails);
            logger.info("Completed processing the ticket details");
        }
    }

    /**
     * Read input csv file
     *
     */
    private List <TicketDetailsVO> read() {
        logger.info("Reading the input data");
        List <TicketDetailsVO> l_lstTicketDetails = new ArrayList<>();
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/input/input.csv"));
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                TicketDetailsVO l_objTicketDetails = new TicketDetailsVO();
                l_objTicketDetails.setFirstName(record.get("First_name"));
                l_objTicketDetails.setLastName(record.get("Last_name"));
                l_objTicketDetails.setPnrNumber(record.get("PNR"));
                l_objTicketDetails.setFareClass(record.get("Fare_class").charAt(0));
                l_objTicketDetails.setTravelDate(record.get("Travel_date"));
                l_objTicketDetails.setPax(Integer.parseInt(record.get("Pax")));
                l_objTicketDetails.setTicketingDate(record.get("Ticketing_date"));
                l_objTicketDetails.setEmail(record.get("Email"));
                l_objTicketDetails.setMobileNo(record.get("Mobile_phone"));
                l_objTicketDetails.setBookedCabin(record.get("Booked_cabin"));

                l_lstTicketDetails.add(l_objTicketDetails);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return l_lstTicketDetails;
    }

    /**
     * Process the input data
     *
     *   @param flightTicketUpgrader
     * 	            the FlightTicketUpgarder
     * 	 @param p_lstTicketDetails
     * 	             the p_lstTicketDetails
     */
    private void process(FlightTicketUpgrader flightTicketUpgrader, List<TicketDetailsVO> p_lstTicketDetails) {
        logger.info("processing the input data");
        Validation l_objValidation = new Validation();
        List<TicketDetailsVO> l_lstSuccessRecords = new ArrayList<>();
        List<TicketDetailsVO> l_lstFailureRecords = new ArrayList<>();
        for (TicketDetailsVO l_objTicketDetailsVo : p_lstTicketDetails) {

            if (!l_objValidation.validateEmail(l_objTicketDetailsVo.getEmail())) {
                l_objTicketDetailsVo.setError("Invalid Email");
            } else if (!l_objValidation.validateMobileNo(l_objTicketDetailsVo.getMobileNo())) {
                l_objTicketDetailsVo.setError("Invalid Mobile No");
            } else if (!l_objValidation.validateBookedCabin(l_objTicketDetailsVo.getBookedCabin())) {
                l_objTicketDetailsVo.setError("Invalid Booked Cabin");
            } else if (!l_objValidation.validatePNRNumber(l_objTicketDetailsVo.getPnrNumber())) {
                l_objTicketDetailsVo.setError("Invalid PNR Number");
            } else if (!l_objValidation.validateTicketDate(l_objTicketDetailsVo.getTicketingDate(), l_objTicketDetailsVo.getTravelDate())) {
                l_objTicketDetailsVo.setError("Invalid Travel Date");
            }

            if (l_objTicketDetailsVo.getError() == null || l_objTicketDetailsVo.getError().isEmpty() ) {
                l_objTicketDetailsVo.setDiscountCode(flightTicketUpgrader.getOfferCode(l_objTicketDetailsVo.getFareClass()));

                if (!l_objTicketDetailsVo.getDiscountCode().isEmpty()) {
                    sendEmail(l_objTicketDetailsVo.getEmail(), l_objTicketDetailsVo.getDiscountCode());
                }

                l_lstSuccessRecords.add(l_objTicketDetailsVo);
            } else {
                l_lstFailureRecords.add(l_objTicketDetailsVo);
            }
        }

        flightTicketUpgrader.write(l_lstSuccessRecords, Paths.get("src/main/resources/output/success.csv"), "Discount_code", true);
        flightTicketUpgrader.write(l_lstFailureRecords, Paths.get("src/main/resources/output/failure.csv"), "Error", false);
    }

    /**
     * write the processed data to output file
     *
     *   @param p_lstTicketDetails
     * 	            the p_lstTicketDetails
     * 	 @param path
     * 	            the path
     * 	 @param header
     *           the header
     *  @param flag
     *          the flag
     */

    private void write(List<TicketDetailsVO> p_lstTicketDetails, final Path path, String header, boolean flag) {
        logger.info("writing the output to csv file");
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            final CSVPrinter printer = new CSVPrinter(writer, CSVFormat.EXCEL.withHeader("First_name", "Last_name", "PNR", "Fare_class", "Travel_date", "Pax", "Ticketing_date", "Email", "Mobile_phone", "Booked_cabin", header));
            for (TicketDetailsVO l_objTicketDetailsVO : p_lstTicketDetails) {
                String discountOrError = (flag ? l_objTicketDetailsVO.getDiscountCode() : l_objTicketDetailsVO.getError());
                printer.printRecord(l_objTicketDetailsVO.getFirstName(), l_objTicketDetailsVO.getLastName(), l_objTicketDetailsVO.getPnrNumber(),
                        l_objTicketDetailsVO.getFareClass(), l_objTicketDetailsVO.getTravelDate(), l_objTicketDetailsVO.getPax(),
                        l_objTicketDetailsVO.getTicketingDate(), l_objTicketDetailsVO.getEmail(), l_objTicketDetailsVO.getMobileNo(), l_objTicketDetailsVO.getBookedCabin(), discountOrError);
                printer.flush();
            }
        } catch (IOException ioException) {
            logger.finer(ioException.getMessage());
        }

    }

    /**
     * Gets the Offer code
     *
     * @param p_cFareClass
     *            the fare class
     *
     * @return the offer code
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public String getOfferCode(char p_cFareClass) {
        int asciiEquivalent =  (p_cFareClass);
        if (asciiEquivalent >= 65 && asciiEquivalent <= 69) {
            return "OFFER_20";
        } else if (asciiEquivalent >= 70 && asciiEquivalent <= 75) {
            return "OFFER_30";
        } else if (asciiEquivalent >= 76 && asciiEquivalent <= 82) {
            return "OFFER_25";
        }
        return "";
    }


    public void sendEmail(String p_sEmail, String p_sDiscountCode) {
        logger.info(p_sEmail +  " have got discount " + p_sDiscountCode);

    }
}
