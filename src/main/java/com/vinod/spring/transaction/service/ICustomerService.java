package com.vinod.spring.transaction.service;

import com.vinod.spring.transaction.dto.CustomerDto;
import com.vinod.spring.transaction.dto.CustomerRegisterDto;
import com.vinod.spring.transaction.dto.CustomerUpdateDto;
import com.vinod.spring.transaction.exception.CustomerNotFoundException;

public interface ICustomerService {

    /**
     * Get customer object by customer email id.
     *
     * @param emailId    - Customer Email ID.
     * @return      - Customer query object.
     */
    CustomerDto getCustomerByEmailId(final String emailId) throws CustomerNotFoundException;

    /**
     * Add customer object to database.
     *
     * @param customerRegisterDto - Customer register object.
     */
    void addCustomer(final CustomerRegisterDto customerRegisterDto);

    /**
     * Add customer object to database.
     *  @param customerUpdateDto - Customer update object.
     * @param emailId
     */
    void updateCustomer(final CustomerUpdateDto customerUpdateDto, final String emailId) throws CustomerNotFoundException;

    /**
     * Delete customer by customer email id.
     *
     * @param emailId - Customer Email id.
     */
    void deleteCustomer(final String emailId) throws CustomerNotFoundException;
}
