package az.transfer.money.util.service;

import az.transfer.money.clients.CustomerClient;
import az.transfer.money.dtos.CustomerResponse;
import az.transfer.money.dtos.requests.CreateAccountRequest;
import az.transfer.money.entities.Account;
import az.transfer.money.exceptions.*;
import az.transfer.money.mappers.AccountMapper;
import az.transfer.money.repositories.AccountRepository;
import az.transfer.money.services.AccountService;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_shouldReturnAccount_whenCustomerExistsAndAccountDoesNotExist() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setCustomerId(1L);

        Account account = new Account();
        account.setCustomerId(1L);

        when(customerClient.getCustomerById(1L))
                .thenReturn(new CustomerResponse(1L, "", ""));

        when(accountRepository.existsByCustomerId(1L))
                .thenReturn(false);

        when(accountMapper.toEntity(request))
                .thenReturn(account);

        when(accountRepository.save(account))
                .thenReturn(account);

        Account result = accountService.createBalance(request);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(account);

        verify(customerClient).getCustomerById(1L);
        verify(accountRepository).existsByCustomerId(1L);
        verify(accountMapper).toEntity(request);
        verify(accountRepository).save(account);
    }

    @Test
    void createAccount_shouldReturnAccount_whenCustomerDoesNotExist() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setCustomerId(1L);
        when(customerClient.getCustomerById(1L))
                .thenThrow(FeignException.NotFound.class);

        assertThatThrownBy(() -> accountService.createBalance(request))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer not found with id: " + request.getCustomerId());

        verify(customerClient).getCustomerById(1L);
        verify(accountRepository, never()).existsByCustomerId(any());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void createAccount_shouldReturnAccount_whenAccountAlreadyExists() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setCustomerId(1L);

        when(customerClient.getCustomerById(1L))
                .thenReturn(new CustomerResponse(1L, "", ""));

        when(accountRepository.existsByCustomerId(1L))
                .thenReturn(true);

        assertThatThrownBy(() -> accountService.createBalance(request))
                .isInstanceOf(AccountAlreadyExistsException.class)
                .hasMessageContaining("Account already exists");
        verify(customerClient).getCustomerById(1L);
        verify(accountRepository).existsByCustomerId(1L);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void getBalance_shouldReturnBalance_whenAccountExists() {
        Long customerId = 1L;
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(500));
        when(accountRepository.findById(customerId)).thenReturn(Optional.of(account));

        BigDecimal balance = accountService.getBalance(customerId);

        assertThat(balance).isEqualByComparingTo(BigDecimal.valueOf(500));
    }

    @Test
    void getAccount_shouldThrowException_whenAccountDoesNotExist() {
        Long customerId = 1L;
        when(accountRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getBalance(customerId))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found for customerId: " + customerId);

        verify(accountRepository).findById(customerId);
    }

    @Test
    void debitAccount_shouldReturnBalance_whenAccountExists() {
        Long customerId = 1L;
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findById(customerId)).thenReturn(Optional.of(account));

        accountService.debit(customerId, BigDecimal.valueOf(300));

        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(700));
        assertThat(account.getUpdatedAt()).isNotNull();

        verify(accountRepository).save(account);
    }

    @Test
    void debitAccount_shouldThrowInsufficientBalanceException_whenBalanceTooLow() {
        Long customerId = 1L;

        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(200));

        when(accountRepository.findById(customerId)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.debit(customerId, BigDecimal.valueOf(500)))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Insufficient balance for customerId: " + customerId);

        verify(accountRepository, never()).save(any());
    }

    @Test
    void debitAccount_shouldThrowInvalidAmountException_whenAmountIsInvalid() {
        Long customerId = 1L;
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(500));
        when(accountRepository.findById(customerId)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> accountService.debit(customerId, BigDecimal.valueOf(0)))
                .isInstanceOf(InvalidAmountException.class);

        assertThatThrownBy(() -> accountService.debit(customerId, BigDecimal.valueOf(-10)))
                .isInstanceOf(InvalidAmountException.class);

        assertThatThrownBy(() -> accountService.debit(customerId, null))
                .isInstanceOf(InvalidAmountException.class);

        verify(accountRepository, never()).save(any());
    }

    @Test
    void creditAccount_shouldReturnBalance_whenAccountExists() {
        Long customerId = 1L;
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(100));
        when(accountRepository.findById(customerId)).thenReturn(Optional.of(account));

        accountService.credit(customerId, BigDecimal.valueOf(300));
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(400));
        assertThat(account.getUpdatedAt()).isNotNull();
        verify(accountRepository).save(account);
    }

    @Test
    void creditAccount_shouldThrowInvalidAmountException_whenAmountIsInvalid() {
        Long customerId = 1L;
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(500));
        when(accountRepository.findById(customerId)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> accountService.credit(customerId, BigDecimal.valueOf(0)))
        .isInstanceOf(InvalidAmountException.class);
        assertThatThrownBy(() -> accountService.credit(customerId, BigDecimal.valueOf(-10)))
        .isInstanceOf(InvalidAmountException.class);
        assertThatThrownBy(() -> accountService.credit(customerId, null))
        .isInstanceOf(InvalidAmountException.class);
        verify(accountRepository, never()).save(any());
    }
}
