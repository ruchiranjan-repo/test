package com.demobank.accountmanagementsystem.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.demobank.accountmanagementsystem.constants.AccountStatus;
import com.demobank.accountmanagementsystem.exceptions.AccountNumberNotFoundException;
import com.demobank.accountmanagementsystem.exceptions.TransactionFailedException;
import com.demobank.accountmanagementsystem.mapper.Mapper;
import com.demobank.accountmanagementsystem.models.Account;
import com.demobank.accountmanagementsystem.models.AccountDTO;
import com.demobank.accountmanagementsystem.models.TransactionDTO;
import com.demobank.accountmanagementsystem.repositories.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired 
	Mapper mapper;
	
	@Override
	public AccountDTO createAccount(AccountDTO account) {
		Account acc= accountRepository.save(mapper.mapAccountDTOToAccount(account));
		
		//account.setAccountNumber(AccountManagementUtils.accountNumberGenerator());		
	    return  mapper.mapAccountToAccountDTO(acc);
		
	}

	@Override
	public AccountDTO updateAccount(Long accountNumber, AccountDTO accountDto) throws AccountNumberNotFoundException {
		Account acc= accountRepository.findByAccountNumber(accountNumber);
		if(acc !=null)
		{
			acc.setAccountStatus(accountDto.getAccountStatus());			
			acc.setAvailableBalance(accountDto.getAvailableBalance());
			
			return mapper.mapAccountToAccountDTO(accountRepository.save(acc));			
		}
		else
		{
		throw new AccountNumberNotFoundException();
		}
	
	}

	@Override
	public AccountDTO deleteAccount(Long accountNumber) throws AccountNumberNotFoundException {
		Account acc= accountRepository.findByAccountNumber(accountNumber);
		if(acc !=null)
		{
			acc.setAccountStatus(AccountStatus.DELETED.toString());
			return mapper.mapAccountToAccountDTO(accountRepository.save(acc));			
		}
		else
		{
		throw new AccountNumberNotFoundException();
		}
	}

	@Override
	public AccountDTO fetchAccountByAccountNumber(Long accountNumber) throws AccountNumberNotFoundException {
		Account acc= accountRepository.findByAccountNumber(accountNumber);
		if(acc == null)
		{
			throw new AccountNumberNotFoundException();
		}
		return mapper.mapAccountToAccountDTO(acc);
	}

	@Override
	public List<AccountDTO> fetchAccountsByIfscCode(String ifscCode) throws AccountNumberNotFoundException	 {
		List<Account> accounts= new ArrayList<Account>();
		accounts= accountRepository.findByIfsc(ifscCode);
		if(CollectionUtils.isEmpty(accounts))
		{
			throw new AccountNumberNotFoundException();
		}
		return mapper.mapAccountListToAccoutDTOs(accounts);
	}

	@Override
	public void initiateTransaction(TransactionDTO transactionDto) throws TransactionFailedException {
		
		
			Long BenificiaryAccountNumber =transactionDto.getBenificiaryAccount();
			Long fromAccountNumber= transactionDto.getFromAccount();			
			BigDecimal amount=transactionDto.getAmount();	
			
			AccountDTO benificiaryAccount= fetchAccountByAccountNumber(BenificiaryAccountNumber);
			AccountDTO fromAccount= fetchAccountByAccountNumber(fromAccountNumber);
			
			BigDecimal fromBalance= fromAccount.getAvailableBalance();
			if(fromBalance.compareTo(amount)==1) {
				
				TransactionDTO fromAccountTransaction= new TransactionDTO();
				fromAccountTransaction.setAmount(amount);
				fromAccountTransaction.setBenificiaryAccount(transactionDto.getBenificiaryAccount());
				fromAccountTransaction.setTransactionDate(LocalDateTime.now());
				fromAccountTransaction.setTransactionStatus("SUCCESS");
				fromAccountTransaction.setTransactionType("DEBIT");
				
				List<TransactionDTO> transactionListFromAccount= new ArrayList<>();
				transactionListFromAccount.add(fromAccountTransaction);
				fromAccount.setAvailableBalance( fromBalance.subtract(amount));	
				fromAccount.setTransactionDto(transactionListFromAccount);
				
				TransactionDTO benificiaryAccountTransaction= new TransactionDTO();
				benificiaryAccountTransaction.setAmount(amount);
				benificiaryAccountTransaction.setBenificiaryAccount(transactionDto.getBenificiaryAccount());
				benificiaryAccountTransaction.setTransactionDate(LocalDateTime.now());
				benificiaryAccountTransaction.setTransactionStatus("SUCCESS");
				benificiaryAccountTransaction.setTransactionType("DEBIT");
				
				List<TransactionDTO> transactionListBenificiaryAccount= new ArrayList<>();
				transactionListBenificiaryAccount.add(benificiaryAccountTransaction);
				benificiaryAccount.setAvailableBalance(benificiaryAccount.getAvailableBalance().add(amount));
				benificiaryAccount.setTransactionDto(transactionListBenificiaryAccount);
				
				accountRepository.save(mapper.mapAccountDTOToAccount(benificiaryAccount));
				accountRepository.save(mapper.mapAccountDTOToAccount(benificiaryAccount));
				
			}
			else
			{
				throw new TransactionFailedException();
			}
				
			
			
			
		
		
		
		
	}

}
