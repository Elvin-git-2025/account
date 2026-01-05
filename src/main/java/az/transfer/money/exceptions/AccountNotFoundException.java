package az.transfer.money.exceptions;

import java.io.Serial;

public class AccountNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(Long id) {
        super("Account not found for customerId: " + id);
    }
}