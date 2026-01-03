package az.kapitalbank.mb.bff.transfermobile.exceptions;

import java.io.Serial;

public class InsufficientBalanceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InsufficientBalanceException(Long id) {
        super("Insufficient balance for customerId: " + id);
    }
}