package com.vinod.spring.transaction.repository;

import com.vinod.spring.transaction.model.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails,Long> {

    Optional<CustomerDetails> findCustomerDetailsByCustomerId(Long customerId);
}
