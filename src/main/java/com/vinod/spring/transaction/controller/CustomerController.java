package com.vinod.spring.transaction.controller;


import com.vinod.spring.transaction.dto.CustomerDto;
import com.vinod.spring.transaction.dto.CustomerRegisterDto;
import com.vinod.spring.transaction.dto.CustomerUpdateDto;
import com.vinod.spring.transaction.exception.CustomerNotFoundException;
import com.vinod.spring.transaction.service.ICustomerService;
import com.vinod.spring.transaction.util.Response;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.vinod.spring.transaction.util.ApplicationConstant.CUSTOMER_SERVICE;
import static com.vinod.spring.transaction.util.GlobalUtility.buildResponseForError;
import static com.vinod.spring.transaction.util.GlobalUtility.buildResponseForSuccess;

@RestController
@RequestMapping(CUSTOMER_SERVICE)
@Log4j2
@Validated
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    /**
     * This endpoint is for adding new customer information into the system.
     *
     * @param customerRegisterDto   - CustomerRegisterDto object.
     * @return                      - Response.
     */
    @PostMapping
    public ResponseEntity<Response> addNewCustomer(@RequestBody CustomerRegisterDto customerRegisterDto) {
        try{
            log.trace("Request came to add new customer with following details: {}", customerRegisterDto);
            customerService.addCustomer(customerRegisterDto);
            return buildResponseForSuccess(HttpStatus.SC_OK,"Successfully added the customer details.",null);
        } catch (Exception e) {
            log.error("Exception occurred while adding the customer details, error msg: {}",e.getMessage(), e);
            return buildResponseForError(HttpStatus.SC_INTERNAL_SERVER_ERROR, String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR),"Unable to add the customer details.",null);
        }
    }

    /**
     * This endpoint is for updating the existing customer details into the system.
     *
     * @param emailId               - Customer Email Id.
     * @param customerUpdateDto     - CustomerUpdateDto object.
     * @return                      - Response.
     */
    @PutMapping("/{emailId}")
    public ResponseEntity<Response> updateCustomer(@PathVariable("emailId") String emailId, @RequestBody CustomerUpdateDto customerUpdateDto) throws CustomerNotFoundException {
        try{
            log.trace("Request came to update customer details: {}", customerUpdateDto);
            customerService.updateCustomer(customerUpdateDto, emailId);
            return buildResponseForSuccess(HttpStatus.SC_OK,"Successfully updated the customer details.",null);
        } catch (CustomerNotFoundException e) {
            log.warn("CustomerNotFoundException occurred while updating the customer details, error msg: {}",e.getMessage(), e);
            return buildResponseForError(HttpStatus.SC_BAD_REQUEST, String.valueOf(HttpStatus.SC_BAD_REQUEST),"No customer detail found for the given email id.",null);
        } catch (Exception e) {
            log.error("Exception occurred while updating the customer details, error msg: {}",e.getMessage(), e);
            return buildResponseForError(HttpStatus.SC_INTERNAL_SERVER_ERROR, String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR),"Unable to update the customer details.",null);
        }
    }

    /**
     * This endpoint is for deleting the existing customer details from the system.
     *
     * @param emailId   -   Customer Email Id.
     * @return          -   String msg.
     */
    @DeleteMapping("/{emailId}")
    public ResponseEntity<Response> deleteCustomer(@PathVariable("emailId") String emailId) throws CustomerNotFoundException {
        log.trace("Request came to get the customer details having the email id: {}", emailId);
        CustomerDto customerDto= null;
        try {
            customerService.deleteCustomer(emailId);
            return buildResponseForSuccess(HttpStatus.SC_OK,"Successfully deleted the customer.",null);
        } catch (CustomerNotFoundException e) {
            return buildResponseForError(HttpStatus.SC_BAD_REQUEST, String.valueOf(HttpStatus.SC_BAD_REQUEST),"No customer detail found for the given email id.",null);
        } catch (Exception e) {
            log.error("Exception occurred while deleting the customer details, error msg: {}",e.getMessage(), e);
            return buildResponseForError(HttpStatus.SC_INTERNAL_SERVER_ERROR, String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR),"Unable to delete the customer details.",null);
        }
    }

    /**
     * Get customer details base on the customer Email id.
     *
     * @param emailId   - Customer Email Id.
     * @return          - CustomerDto object.
     * @throws CustomerNotFoundException
     */
    @GetMapping("/{emailId}")
    public ResponseEntity<Response> getCustomer(@PathVariable("emailId") String emailId) throws CustomerNotFoundException {
        log.info("Request came to get the customer details having the email id: {}", emailId);
        try {
            CustomerDto customerDto= customerService.getCustomerByEmailId(emailId);
            return buildResponseForSuccess(HttpStatus.SC_OK,"Successfully fetched customer",customerDto);
        } catch (CustomerNotFoundException e) {
            return buildResponseForError(HttpStatus.SC_BAD_REQUEST, String.valueOf(HttpStatus.SC_BAD_REQUEST),"No customer detail found for the given email id.",null);
        } catch (Exception e) {
            log.error("Exception occurred while deleting the customer details, error msg: {}",e.getMessage(), e);
            return buildResponseForError(HttpStatus.SC_INTERNAL_SERVER_ERROR, String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR),"Unable to delete the customer details.",null);
        }
    }
}
