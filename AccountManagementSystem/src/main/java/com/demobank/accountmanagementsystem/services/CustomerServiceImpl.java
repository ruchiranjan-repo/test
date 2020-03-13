package com.demobank.accountmanagementsystem.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Service;

import com.demobank.accountmanagementsystem.exceptions.CustomerNotFoundException;
import com.demobank.accountmanagementsystem.mapper.Mapper;
import com.demobank.accountmanagementsystem.models.Account;
import com.demobank.accountmanagementsystem.models.Customer;
import com.demobank.accountmanagementsystem.models.CustomerDTO;
import com.demobank.accountmanagementsystem.repositories.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	Mapper mapper;

	@Override
	public CustomerDTO createCustomer(CustomerDTO customerDto) {
		Customer customer= mapper.mapCustomerDtoToCustomer(customerDto);
		return mapper.mapCustomerToCustomerDto(customerRepository.save(customer));

	}

	@Override
	public CustomerDTO findCustomer(Long customerId) throws CustomerNotFoundException {
		Customer customer = customerRepository.findByCustomerId(customerId);
		if (customer == null) {
			throw new CustomerNotFoundException();
		}
		return mapper.mapCustomerToCustomerDto(customer);
	}

	@Override
	public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDto) throws CustomerNotFoundException {
		
		Customer cust = customerRepository.findByCustomerId(customerId);
		if (cust != null) {
			cust.setCustomerContact(customerDto.getCustomerContact());
			cust.setCustomerEmail(customerDto.getCustomerEmail());
			if(!org.springframework.util.CollectionUtils.isEmpty(customerDto.getAccounts()))
					{
				     List<Account> existingAccounts=cust.getAccounts();
				     existingAccounts.addAll(mapper.mapAccountDtosToAccouts(customerDto.getAccounts()));
				     cust.setAccounts(existingAccounts);
					}
			
			return mapper.mapCustomerToCustomerDto(customerRepository.save(cust));
		} else {
			throw new CustomerNotFoundException();
		}

	}

	@Override
	public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
		Customer cust = customerRepository.findByCustomerId(customerId);
		if (cust != null) {
			customerRepository.delete(cust);
		} else {
			throw new CustomerNotFoundException();
		}
	}

}
