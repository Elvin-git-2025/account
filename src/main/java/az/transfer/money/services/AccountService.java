package az.transfer.money.services;

import az.transfer.money.clients.CustomerClient;
import az.transfer.money.dtos.requests.CreateAccountRequest;
import az.transfer.money.entities.Account;
import az.transfer.money.exceptions.AccountNotFoundException;
import az.transfer.money.exceptions.CustomerNotFoundException;
import az.transfer.money.exceptions.AccountAlreadyExistsException;
import az.transfer.money.exceptions.InsufficientBalanceException;
import az.transfer.money.exceptions.InvalidAmountException;
import az.transfer.money.mappers.AccountMapper;
import az.transfer.money.repositories.AccountRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    private final CustomerClient customerClient;

    private final AccountMapper accountMapper;

    @Transactional
    public Account createBalance(CreateAccountRequest request) {

        try {
            customerClient.getCustomerById(request.getCustomerId());
        } catch (FeignException.NotFound ex) {
            throw new CustomerNotFoundException(request.getCustomerId());
        }

        if (accountRepository.existsByCustomerId(request.getCustomerId())) {
            throw new AccountAlreadyExistsException(request.getCustomerId());
        }

        Account account = accountMapper.toEntity(request);
        return accountRepository.save(account);
    }
    public BigDecimal getBalance(Long customerId) {
        Account account = getAccount(customerId);
        return account.getBalance();
    }

    public void debit(Long customerId, BigDecimal amount) {
        validateAmount(amount);

        Account account = getAccount(customerId);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(customerId);
        }

        account.setBalance(account.getBalance().subtract(amount));
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);
    }

    public void credit(Long customerId, BigDecimal amount) {
        validateAmount(amount);

        Account account = getAccount(customerId);
        account.setBalance(account.getBalance().add(amount));
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);
    }

    private Account getAccount(Long customerId) {
        return accountRepository.findById(customerId)
                .orElseThrow(() -> new AccountNotFoundException(customerId));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
    }
}
