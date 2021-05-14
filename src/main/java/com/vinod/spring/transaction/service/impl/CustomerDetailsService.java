package com.vinod.spring.transaction.service.impl;

import com.vinod.spring.transaction.dto.CustomerDto;
import com.vinod.spring.transaction.dto.CustomerRegisterDto;
import com.vinod.spring.transaction.dto.CustomerUpdateDto;
import com.vinod.spring.transaction.exception.CustomerNotFoundException;
import com.vinod.spring.transaction.model.CustomerDetails;
import com.vinod.spring.transaction.repository.CustomerDetailsRepository;
import com.vinod.spring.transaction.service.ICustomerDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class CustomerDetailsService implements ICustomerDetailsService {

    @Autowired
    private CustomerDetailsRepository customerDetailsRepository;

    @Override
    public void addCustomerDetails(CustomerRegisterDto customerRegisterDto, Long customerId) {
        log.trace("Request came to add customerDetails details for customer id: {}, having details: {}", customerId, customerRegisterDto);
        CustomerDetails customerDetails = mapCustomerRegisterDtoToCustomerDetails(customerRegisterDto, customerId);
        CustomerDetails resultCustomerDetails = saveCustomerDetails(customerDetails);
        log.info("Successfully saved customerDetails object: {}", resultCustomerDetails);
    }

    @Override
    public void updateCustomerDetails(CustomerUpdateDto customerUpdateDto, Long customerId) throws CustomerNotFoundException {
        log.trace("Request came to update customer details for: {}", customerUpdateDto);
        CustomerDetails customerDetails = fetchCustomerDetailsByCustomerId(customerId);
        if(null!=customerDetails) {
            customerDetails.setAddress1(customerUpdateDto.getAddress1());
            customerDetails.setAddress2(customerUpdateDto.getAddress2());
            CustomerDetails persistedCustomerDetails = saveCustomerDetails(customerDetails);
            log.info("Successfully updated customer details: {}", persistedCustomerDetails);
        } else {
            throw new CustomerNotFoundException("Customer detail update operation failed, customer not found for id: "+customerId);
        }
    }

    @Override
    public void deleteCustomerDetails(Long customerId) throws CustomerNotFoundException {
        log.trace("Request came to delete customer details for: {}", customerId);
        CustomerDetails customerDetails = fetchCustomerDetailsByCustomerId(customerId);
        if(null!=customerDetails) {
            customerDetailsRepository.delete(customerDetails);
            log.info("Successfully deleted customer details for customer id: {}", customerId);
        } else {
            throw new CustomerNotFoundException("Customer detail delete operation failed, customer not found for id: "+customerId);
        }
    }

    @Override
    public CustomerDto getCustomerDetailsByCustomerId(Long customerId) throws CustomerNotFoundException {
        CustomerDetails customerDetails = fetchCustomerDetailsByCustomerId(customerId);
        if(null!=customerDetails) {
            CustomerDto customerDto = mapCustomerDetailsToCustomerDto(customerDetails);
            log.info("Successfully fetched customer details: {}", customerDto);
            return customerDto;
        } else {
            throw new CustomerNotFoundException("Customer detail fetch operation failed, customer not found for id: "+customerId);
        }
    }

    /**
     * Save the customerDetails object to DB.
     *
     * @param customerDetails
     * @return
     */
    private CustomerDetails saveCustomerDetails(CustomerDetails customerDetails) {
        log.trace("Request came to save the customerDetails details to DB having customerDetails details: {}", customerDetails);
        CustomerDetails persistedCustomerDetails = customerDetailsRepository.save(customerDetails);
        log.trace("Persisted customerDetails details: {}", persistedCustomerDetails);
        return persistedCustomerDetails;
    }

    /**
     * Fetch the customer object from DB using customer email id.
     *
     * @param CustomerId
     * @return
     */
    private CustomerDetails fetchCustomerDetailsByCustomerId(Long CustomerId) {
        log.trace("Fetch customer details from DB for customer email id: {}", CustomerId);
        Optional<CustomerDetails> optionalCustomerDetails=customerDetailsRepository.findCustomerDetailsByCustomerId(CustomerId);
        return optionalCustomerDetails.isPresent() ? optionalCustomerDetails.get() : null;
    }

    /**
     * Map CustomerDetails to CustomerDto object.
     *
     * @param customerDetails
     * @return
     */
    private CustomerDto mapCustomerDetailsToCustomerDto(final CustomerDetails customerDetails) {
        return CustomerDto.builder()
                .address1(customerDetails.getAddress1())
                .address2(customerDetails.getAddress2())
                .build();
    }

    /**
     * Map CustomerRegisterDto to CustomerDetails object.
     *
     * @param customerRegisterDto
     * @param customerId
     * @return
     */
    private CustomerDetails mapCustomerRegisterDtoToCustomerDetails(CustomerRegisterDto customerRegisterDto, Long customerId) {
        CustomerDetails customerDetails = CustomerDetails.builder().address1(customerRegisterDto.getAddress1())
                .address2(customerRegisterDto.getAddress2())
                .customerId(customerId)
                .build();
        return customerDetails;
    }

    /**
     * Map CustomerUpdateDto to CustomerDetails object.
     *
     * @param customerUpdateDto
     * @return
     */
    private CustomerDetails mapCustomerUpdateDtoToCustomerDetails(CustomerUpdateDto customerUpdateDto) {
        CustomerDetails customerDetails = CustomerDetails.builder()
                .address1(customerUpdateDto.getAddress1())
                .address2(customerUpdateDto.getAddress2())
                .build();
        return customerDetails;
    }
}
