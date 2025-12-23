package az.kapitalbank.mb.bff.transfermobile.customer.globalExceptionHandler;

import az.kapitalbank.mb.bff.transfermobile.customer.exceptions.CustomerNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handle(CustomerNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
