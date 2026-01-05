package az.transfer.money.exceptions;

import java.io.Serial;

public class AccountAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AccountAlreadyExistsException(Long id) {
        super("Account already exists for customer id: " + id);
    }
}
