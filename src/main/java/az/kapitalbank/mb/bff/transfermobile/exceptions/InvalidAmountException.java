package az.kapitalbank.mb.bff.transfermobile.exceptions;

import java.io.Serial;

public class InvalidAmountException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidAmountException() {
        super("Amount must be greater than zero");
    }
}