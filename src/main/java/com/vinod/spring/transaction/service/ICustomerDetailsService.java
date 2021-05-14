package com.vinod.spring.transaction.service;

import com.vinod.spring.transaction.dto.CustomerDto;
import com.vinod.spring.transaction.dto.CustomerRegisterDto;
import com.vinod.spring.transaction.dto.CustomerUpdateDto;
import com.vinod.spring.transaction.exception.CustomerNotFoundException;

public interface ICustomerDetailsService {

    /**
     * Add customer details.
     *
     * @param customerUpdateDto
     * @param customerId
     */
    void addCustomerDetails(CustomerRegisterDto customerUpdateDto, Long customerId);

    /**
     * Update customer details.
     *
     * @param customerUpdateDto
     * @param customerId
     * @throws CustomerNotFoundException
     */
    void updateCustomerDetails(final CustomerUpdateDto customerUpdateDto, final Long customerId) throws CustomerNotFoundException;

    /**
     * Delete customer details.
     *
     * @param customerId
     * @throws CustomerNotFoundException
     */
    void deleteCustomerDetails(Long customerId) throws CustomerNotFoundException;

    /**
     * Get customer details by customer id.
     *
     * @param customerId
     * @return
     * @throws CustomerNotFoundException
     */
    CustomerDto getCustomerDetailsByCustomerId(final Long customerId) throws CustomerNotFoundException;
}
