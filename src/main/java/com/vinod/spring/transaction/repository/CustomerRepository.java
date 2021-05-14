package com.vinod.spring.transaction.repository;

import com.vinod.spring.transaction.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findCustomerByEmailId(String emailId);
}
