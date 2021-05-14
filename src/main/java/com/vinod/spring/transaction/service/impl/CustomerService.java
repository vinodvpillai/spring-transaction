package com.vinod.spring.transaction.service.impl;

import com.vinod.spring.transaction.dto.CustomerDto;
import com.vinod.spring.transaction.dto.CustomerRegisterDto;
import com.vinod.spring.transaction.dto.CustomerUpdateDto;
import com.vinod.spring.transaction.exception.CustomerNotFoundException;
import com.vinod.spring.transaction.model.Customer;
import com.vinod.spring.transaction.repository.CustomerRepository;
import com.vinod.spring.transaction.service.ICustomerDetailsService;
import com.vinod.spring.transaction.service.ICustomerService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.vinod.spring.transaction.util.ApplicationConstant.CustomerStatus.REGISTERED;

@Service
@Log4j2
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ICustomerDetailsService customerDetailsService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Add customer object to database and raise event.
     *
     * @param customerRegisterDto - Customer register command object.
     */
    @Override
    public void addCustomer(CustomerRegisterDto customerRegisterDto) {
        log.trace("Request came to add new customer : {}", customerRegisterDto);
        Customer customer = mapAndSaveCustomer(customerRegisterDto);
        log.info("Successfully saved customer object: {}", customer);
    }

    /**
     * Update the customer object to database.
     *
     * @param customerUpdateDto - Customer update dto object.
     * @param emailId           - Customer Email ID.
     */
    @Override
    public void updateCustomer(CustomerUpdateDto customerUpdateDto, String emailId) throws CustomerNotFoundException {
        log.trace("Request came to update customer details for: {}", customerUpdateDto);
        Customer customer = mapCustomerRegisterDTOToCustomer(customerUpdateDto, emailId);
        if(null!=customer) {
            Customer persistedCustomer=customerRepository.save(customer);
            customerDetailsService.updateCustomerDetails(customerUpdateDto,persistedCustomer.getId());
            log.info("Successfully updated customer details: {}", persistedCustomer);
        } else {
            throw new CustomerNotFoundException("Customer update operation failed, customer not found for email id:"+emailId);
        }
    }

    /**
     * Delete customer by customer emailId.
     *
     * @param emailId - Customer emailId.
     */
    @Override
    @Transactional
    public void deleteCustomer(String emailId) throws CustomerNotFoundException {
        log.trace("Request came to delete customer having emailId: {}", emailId);
        Optional<Customer> optionalCustomer=customerRepository.findCustomerByEmailId(emailId);
        if(optionalCustomer.isPresent()) {
            Customer  customer = optionalCustomer.get();
            customerDetailsService.deleteCustomerDetails(customer.getId());
            if(emailId.equals("anil@yopmail.com")) {
                throw new RuntimeException("Error occurred while deleting, customer not deleted for email id:"+emailId);
            }
            customerRepository.delete(customer);
            log.info("Successfully deleted the customer details for customer email id: {}", emailId);
        } else {
            throw new CustomerNotFoundException("Customer delete operation failed, customer not found for email id:"+emailId);
        }
    }

    /**
     * Get customer object by Email ID.
     *
     * @param emailId    - Customer Email ID.
     * @return      - Customer Query object.
     */
    @Override
    public CustomerDto getCustomerByEmailId(String emailId) throws CustomerNotFoundException {
        log.trace("Request came to get customer details for customer email id: {}", emailId);
        Customer customer = fetchCustomerByEmailId(emailId);
        if(null!=customer) {
            CustomerDto customerDto = mapCustomerToCustomerDto(customer);
            CustomerDto customerDetailsDTO = customerDetailsService.getCustomerDetailsByCustomerId(customer.getId());
            customerDto.setAddress1(customerDetailsDTO.getAddress1());
            customerDto.setAddress2(customerDetailsDTO.getAddress2());
            return customerDto;
        } else {
            throw new CustomerNotFoundException("Get Customer details operation failed, customer not found for email id:"+emailId);
        }
    }

    /**
     * Fetch the customer object from DB using customer email id.
     *
     * @param emailId
     * @return
     */
    private Customer fetchCustomerByEmailId(String emailId) {
        log.trace("Fetch customer details from DB for customer email id: {}", emailId);
        Optional<Customer> optionalCustomer=customerRepository.findCustomerByEmailId(emailId);
        return optionalCustomer.isPresent() ? optionalCustomer.get() : null;
    }

    /**
     * Save the customer object to DB.
     *
     * @param customer
     * @return
     */
    private Customer saveCustomer(Customer customer) {
        log.trace("Request came to save the customer details to DB having customer details: {}", customer);
        Customer persistedCustomer = customerRepository.save(customer);
        log.trace("Persisted customer details: {}", persistedCustomer);
        return persistedCustomer;
    }

    /**
     * Save the customer object to database.
     *
     * @param customerRegisterDto - Customer register dto object.
     * @return  - Customer object.
     */
    private Customer mapAndSaveCustomer(CustomerRegisterDto customerRegisterDto) {
        log.trace("Request came to map and save the customer object with customer details: {}", customerRegisterDto);
        Customer customer = mapCustomerRegisterDTOToCustomer(customerRegisterDto);
        Customer persistedCustomer = saveCustomer(customer);
        customerDetailsService.addCustomerDetails(customerRegisterDto,persistedCustomer.getId());
        return persistedCustomer;
    }

    /**
     * Map CustomerRegisterDto to Customer object.
     *
     * @param customerRegisterDto - customerRegisterDto object.
     * @return  - Customer object.
     */
    private Customer mapCustomerRegisterDTOToCustomer(CustomerRegisterDto customerRegisterDto) {
        modelMapper.typeMap(CustomerRegisterDto.class, Customer.class).addMappings(mapper -> mapper.skip(Customer::setId));
        Customer customer = modelMapper.map(customerRegisterDto, Customer.class);
        customer.setStatus(REGISTERED.value());
        return customer;
    }

    /**
     * Map Customer to CustomerDto object.
     *
     * @param customer - Customer object.
     * @return  - Customer Query object.
     */
    private CustomerDto mapCustomerToCustomerDto(Customer customer) {
        modelMapper.typeMap(Customer.class, CustomerDto.class).addMappings(mapper -> mapper.skip(CustomerDto::setId));
        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
        customerDto.setId(customer.getId());
        return customerDto;
    }

    /**
     * Map CustomerUpdateDto to Customer object.
     *
     * @param customerUpdateDto - customerUpdateDto object.
     * @param emailId
     * @return  - Customer object.
     */
    private Customer mapCustomerRegisterDTOToCustomer(CustomerUpdateDto customerUpdateDto, String emailId) {
        Customer customer = fetchCustomerByEmailId(emailId);
        if(null!=customer) {
            customer.setName(customerUpdateDto.getName());
            return customer;
        }
        return null;
    }
}
