package com.demobank.accountmanagementsystem.models;

import java.math.BigDecimal;
import java.util.List;

public class AccountDTO {

	Long accountNumber;
	String ifsc;
	String accountType;
	BigDecimal availableBalance;
	String accountStatus;
	List<TransactionDTO> transactionDto;
	

	public List<TransactionDTO> getTransactionDto() {
		return transactionDto;
	}

	public void setTransactionDto(List<TransactionDTO> transactionDto) {
		this.transactionDto = transactionDto;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	

}
