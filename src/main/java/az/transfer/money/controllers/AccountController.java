package az.transfer.money.controllers;

import az.transfer.money.dtos.requests.CreateAccountRequest;
import az.transfer.money.dtos.requests.CreditAccountRequest;
import az.transfer.money.dtos.requests.DebitAccountRequest;
import az.transfer.money.dtos.responses.AccountBalanceResponse;
import az.transfer.money.entities.Account;
import az.transfer.money.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @PostMapping
    public ResponseEntity<Account> createAccount(
            @RequestBody CreateAccountRequest request) {

        Account account = accountService.createBalance(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/{customerId}/balance")
    public AccountBalanceResponse getBalance(
            @PathVariable Long customerId
    ) {
        return new AccountBalanceResponse(
                accountService.getBalance(customerId)
        );
    }

    @PostMapping("/{customerId}/debit")
    public void debit(
            @PathVariable Long customerId,
            @RequestBody DebitAccountRequest request
    ) {
        accountService.debit(customerId, request.getAmount());
    }

    @PostMapping("/{customerId}/credit")
    public void credit(
            @PathVariable Long customerId,
            @RequestBody CreditAccountRequest request
    ) {
        accountService.credit(customerId, request.getAmount());
    }
}
