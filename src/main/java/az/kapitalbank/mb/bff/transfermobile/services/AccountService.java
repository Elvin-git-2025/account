package az.kapitalbank.mb.bff.transfermobile.services;

import az.kapitalbank.mb.bff.transfermobile.entities.Account;
import az.kapitalbank.mb.bff.transfermobile.exceptions.AccountNotFoundException;
import az.kapitalbank.mb.bff.transfermobile.exceptions.InsufficientBalanceException;
import az.kapitalbank.mb.bff.transfermobile.exceptions.InvalidAmountException;
import az.kapitalbank.mb.bff.transfermobile.repositories.AccountRepository;
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
