package com.demobank.accountmanagementsystem.services;

import com.demobank.accountmanagementsystem.exceptions.CustomerNotFoundException;
import com.demobank.accountmanagementsystem.models.CustomerDTO;

public interface CustomerService {
	public CustomerDTO createCustomer(CustomerDTO customer);
	public CustomerDTO findCustomer(Long customerId) throws CustomerNotFoundException;
	public  CustomerDTO updateCustomer(Long customerId ,CustomerDTO customer) throws CustomerNotFoundException;
	public  void deleteCustomer(Long customerId) throws CustomerNotFoundException;

}
