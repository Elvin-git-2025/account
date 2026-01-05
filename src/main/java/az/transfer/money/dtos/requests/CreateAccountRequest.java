package az.transfer.money.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {
    private Long customerId;
}