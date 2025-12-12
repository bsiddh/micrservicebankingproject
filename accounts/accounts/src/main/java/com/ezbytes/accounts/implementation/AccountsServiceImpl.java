package com.ezbytes.accounts.implementation;

import com.ezbytes.accounts.constants.AccountsConstants;
import com.ezbytes.accounts.dto.CustomerDto;
import com.ezbytes.accounts.entity.Accounts;
import com.ezbytes.accounts.entity.Customer;
import com.ezbytes.accounts.exception.CustomerAlreadyExistsException;
import com.ezbytes.accounts.mapper.CustomerMapper;
import com.ezbytes.accounts.repository.AccountsRepository;
import com.ezbytes.accounts.repository.CustomerRepository;
import com.ezbytes.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    /**
     * @param customerDto -CustomerDto object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto,new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber"+customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDate.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedBy("Anonymous");
        return newAccount;
    }

}
